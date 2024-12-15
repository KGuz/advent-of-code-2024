package advent

data class P2<T : Number>(
    var x: T,
    var y: T,
) {
    override fun toString() = "(${this.x}, ${this.y})"
}

operator fun P2<Int>.plus(other: P2<Int>): P2<Int> = P2(this.x + other.x, this.y + other.y)

operator fun P2<Int>.minus(other: P2<Int>): P2<Int> = P2(this.x - other.x, this.y - other.y)

operator fun P2<Int>.times(other: Int): P2<Int> = P2(this.x * other, this.y * other)

operator fun P2<Int>.div(other: Int): P2<Int> = P2(this.x / other, this.y / other)

var <T> Array<T>.x: T
    get() = this[0]
    set(value) { this[0] = value }

var <T> Array<T>.y: T
    get() = this[1]
    set(value) { this[1] = value }

var <T> Array<T>.z: T
    get() = this[2]
    set(value) { this[2] = value }
