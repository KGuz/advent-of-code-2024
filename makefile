# Variables and settings
TOP              := $(shell pwd)
SRCDIR           := $(TOP)/src
BUILDROOT        := $(TOP)/build
SDKDIR           := $(TOP)/sdk
SDKSYSROOT       := $(SDKDIR)/sysroot

KOTLINNATIVEADDR := https://github.com/JetBrains/kotlin/releases/download/v2.1.0-RC/kotlin-native-prebuilt-linux-x86_64-2.1.0-RC.tar.gz
KOTLINJVMADDR    := https://github.com/JetBrains/kotlin/releases/download/v2.0.21/kotlin-compiler-2.0.21.zip
OPENJDKADDR      := https://download.java.net/openjdk/jdk21/ri/openjdk-21+35_linux-x64_bin.tar.gz
KTLINTADDR       := https://github.com/pinterest/ktlint/releases/download/1.4.1/ktlint

KOTLINNATIVEHOME := $(SDKSYSROOT)/usr/lib/kotlin-native
KOTLINCHOME      := $(SDKSYSROOT)/usr/lib/kotlinc
OPENJDKHOME      := $(SDKSYSROOT)/usr/lib/openjdk

PROGRESS10   = 'BEGIN {ORS="."} { if(NR%10   == 0) print"" }'
PROGRESS1000 = 'BEGIN {ORS="."} { if(NR%1000 == 0) print"" }'

# Build options (default: jvm, release)
NATIVE := $(native)
DEBUG  := $(debug)

ifeq ($(NATIVE), 1)
	ENTRY    := main.kexe
	BUILDDIR := $(BUILDROOT)/native

	ifeq ($(DEBUG), 1)
		BUILDDIR := $(BUILDDIR)/debug
		FLAGS    := -g
	else
		BUILDDIR := $(BUILDDIR)/release
		FLAGS    := -opt
	endif
else
	ENTRY    := main.jar
	BUILDDIR := $(BUILDROOT)/jvm

	ifeq ($(DEBUG), 1)
		BUILDDIR := $(BUILDDIR)/debug
		FLAGS    := -Werror -Xdebug -Xno-optimize
	else
		BUILDDIR := $(BUILDDIR)/release
		FLAGS    := -Werror
	endif
endif

# Dependency discovery
PACKAGES    := $(shell find $(SRCDIR) -type d ! -name src)

JVMOBJECTS  := $(PACKAGES:$(SRCDIR)/%=$(BUILDDIR)/%.jar)
CLASSPATHS  := $(subst $() ,:,$(JVMOBJECTS))

NATIVELIBS  := $(PACKAGES:$(SRCDIR)/%=$(BUILDDIR)/%.klib)
LLIBS       := $(NATIVELIBS:%=-l %)

###############################################################################

.PHONY: help
help:
	@printf "\
	Usage: make target... [options]...\n\
	\n\
	Targets:\n\
	  all             Build executable\n\
	  check           Check source code using ktlint\n\
	  clean           Clean build directory\n\
	  format          Format source code using ktlint\n\
	  help            Print this information\n\
	  run             Build and run executable\n\
	  sdk             Fetch all tools required to compile the project\n\
	  vars            Print Makefile variables for debugging\n\
	Options:\n\
	  debug=1         Compile project in debug mode\n\
	  native=1        Compile project using kotlin-native instead of kotlin-jvm\n"

# kotlin-native executable
$(BUILDDIR)/main.jar: $(SRCDIR)/main.kt $(JVMOBJECTS)
	@echo "Building executable..."
	@kotlinc                                \
		-jdk-home $(OPENJDKHOME)            \
		-kotlin-home $(KOTLINCHOME)         \
		-cp $(CLASSPATHS)                   \
		-d $(BUILDDIR)/main.jar             \
		$(FLAGS) $(SRCDIR)/main.kt

# kotlin-native packages
$(BUILDDIR)/%.jar: $(SRCDIR)/%/*.kt
	@echo "Compiling $*..."
	@kotlinc                                \
		-jdk-home $(OPENJDKHOME)            \
		-kotlin-home $(KOTLINCHOME)         \
		-d $@                               \
		$(FLAGS)                            \
		$(filter-out %native.kt, $^)

# kotlin-jvm executable
$(BUILDDIR)/main.kexe: $(SRCDIR)/main.kt $(NATIVELIBS)
	@echo "Building executable..."
	@kotlinc-native                         \
		-kotlin-home $(KOTLINNATIVEHOME)    \
		$(LLIBS)                            \
		-output $(BUILDDIR)/main.kexe       \
		$(FLAGS) $(SRCDIR)/main.kt

# kotlin-jvm packages
$(BUILDDIR)/%.klib: $(SRCDIR)/%/*.kt
	@echo "Compiling $*..."
	@kotlinc-native                         \
		-kotlin-home $(KOTLINNATIVEHOME)    \
		-p library                          \
		-output $@                          \
		$(FLAGS)                            \
		$(filter-out %jvm.kt, $^)

.PHONY: all
all: $(BUILDDIR)/$(ENTRY)

.PHONY: run
run: all
ifeq ($(NATIVE), 1)
	@$(BUILDDIR)/main.kexe $(MAKEFLAGS)
else
	@kotlin -cp $(CLASSPATHS):$(BUILDDIR)/main.jar MainKt $(MAKEFLAGS)
endif

.PHONY: clean
clean:
	@rm -rf $(BUILDROOT)

.PHONY: check
check:
	@ktlint $(SRCDIR)/**/*.kt

.PHONY: format
format:
	@ktlint $(SRCDIR)/**/*.kt --format

.PHONY: sdk
sdk:
	@echo -n "Removing artifacts"
	@rm -rvf $(SDKDIR) | awk $(PROGRESS1000)

	@mkdir -p $(SDKSYSROOT)/usr/bin	$(SDKSYSROOT)/usr/lib

	@echo && echo -n "Installing openjdk"
	@cd $(SDKSYSROOT)/usr/lib                               \
		&& curl -sSLO $(OPENJDKADDR)                        \
		&& tar xf *.tar.gz --checkpoint=.1000               \
		&& rm *.tar.gz                                      \
		&& mv jdk-* openjdk

	@echo && echo -n "Installing kotlinc"
	@cd $(SDKSYSROOT)/usr/lib                               \
		&& curl -sSLO $(KOTLINJVMADDR)                      \
		&& unzip *.zip | awk $(PROGRESS10)                  \
		&& rm *.zip

	@echo && echo -n "Installing kotlin-native"
	@cd $(SDKSYSROOT)/usr/lib                               \
		&& curl -sSLO $(KOTLINNATIVEADDR)                   \
		&& tar xf *.tar.gz --checkpoint=.1000               \
		&& rm *.tar.gz                                      \
		&& mv kotlin-native-*/ kotlin-native/

	@echo && echo "Installing ktlint..."
	@cd $(SDKSYSROOT)/usr/bin                               \
		&& curl -sSLO $(KTLINTADDR)                         \

	@echo "Linking binaries..."
	@ln -s $(KOTLINNATIVEHOME)/bin/*  $(SDKSYSROOT)/usr/bin/
	@ln -s $(KOTLINCHOME)/bin/* 	  $(SDKSYSROOT)/usr/bin/
	@ln -s $(OPENJDKHOME)/bin/* 	  $(SDKSYSROOT)/usr/bin/
	@chmod a+x -R $(SDKSYSROOT)/usr/bin/

	@echo "Generating activation script..."
	@cd $(SDKDIR)                                           \
		&& echo                                             \
			"#!/usr/bin/bash\n"                             \
			"export PATH='$(SDKSYSROOT)/usr/bin:$${PATH}'"  \
		> activate

	@echo "SDK installed (source $(shell basename $(SDKDIR))/activate)"

.PHONY: vars
vars:
	@printf "\
	TOP:                \"$(TOP)\"\n\
	SRCDIR:             \"$(SRCDIR)\"\n\
	BUILDROOT:          \"$(BUILDROOT)\"\n\
	SDKDIR:             \"$(SDKDIR)\"\n\
	SDKSYSROOT:         \"$(SDKSYSROOT)\"\n\
	KOTLINNATIVEHOME:   \"$(KOTLINNATIVEHOME)\"\n\
	KOTLINCHOME:        \"$(KOTLINCHOME)\"\n\
	OPENJDKHOME:        \"$(OPENJDKHOME)\"\n\
	NATIVE:             \"$(NATIVE)\"\n\
	DEBUG:              \"$(DEBUG)\"\n\
	ENTRY:              \"$(ENTRY)\"\n\
	FLAGS:              \"$(FLAGS)\"\n\
	PACKAGES:           \"$(PACKAGES)\"\n\
	JVMOBJECTS:         \"$(JVMOBJECTS)\"\n\
	CLASSPATHS:         \"$(CLASSPATHS)\"\n\
	NATIVELIBS:         \"$(NATIVELIBS)\"\n\
	LLIBS:              \"$(LLIBS)\"\n"
