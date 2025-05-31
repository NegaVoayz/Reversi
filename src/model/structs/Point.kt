package model.structs

class Point {
    @JvmField
    var x: Int = 0
    @JvmField
    var y: Int = 0

    constructor()

    constructor(x: Int, y: Int) {
        this.x = x
        this.y = y
    }

    constructor(start: Point) {
        this.x = start.x
        this.y = start.y
    }

    fun translate(p: Point): Point {
        this.x += p.x
        this.y += p.y
        return this
    }

    fun detranslate(p: Point): Point {
        this.x -= p.x
        this.y -= p.y
        return this
    }

    fun set(x: Int, y: Int): Point {
        this.x = x
        this.y = y
        return this
    }

    fun set(point: Point): Point {
        this.x = point.x
        this.y = point.y
        return this
    }
}
