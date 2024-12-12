package advent

data class P2<T : Number>(
    var x: T,
    var y: T,
) {
    override fun toString() = "(${this.x}, ${this.y})"
}

operator fun P2<Int>.plus(other: P2<Int>): P2<Int> = P2(this.x + other.x, this.y + other.y)

operator fun P2<Int>.minus(other: P2<Int>): P2<Int> = P2(this.x - other.x, this.y - other.y)
