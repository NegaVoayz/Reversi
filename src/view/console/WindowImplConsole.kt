package view.console

import model.structs.Point
import model.structs.Rect

open class WindowImplConsole(private val rect: Rect, private val screen: Screen) : Window {
    private val buffer: Array<Array<Pixel>> = Array(rect.bottom - rect.top) {
        Array(rect.right - rect.left) { PixelImplConsole() }
    }
    private var dirty = 0

    /**
     * set pixel on screen
     * set dirty to true if really changed
     *
     * @param position pixel position
     * @param pix the pixel 'color'
     * @return dirty since we don't care one pixel but the whole screen
     */
    override fun setPixel(position: Point, pix: Pixel): Int {
        dirty += buffer[position.y][position.x].set(pix)
        return dirty
    }

    /**
     * display the screen, force refresh by default
     */
    override fun paint() {
        check(screen is ScreenImplConsole) { "Screen is not a ScreenImplConsole" }
        val position = Point()
        position.y = rect.top
        while (position.y < rect.bottom) {
            position.x = rect.left
            while (position.x < rect.right) {
                screen.setPixel(position, buffer[position.y - rect.top][position.x - rect.left])
                position.x++
            }
            position.y++
        }
        screen.paint()
    }

    /**
     * print string on canvas
     * @param position the starting x-position
     * @param str the string to be print
     * @return number of characters printed
     */
    override fun print(position: Point, str: String): Int {
        var cnt = 0
        val shift = Point(1, 0)
        for (i in 0..<str.length) {
            cnt += setPixel(position, PixelImplConsole(str.get(i)))
            position.translate(shift)
        }
        return cnt
    }

    /**
     * print string on canvas, rewrite the whole line
     * @param y the starting line position
     * @param str the string to be print
     * @return number of characters changed
     */
    override fun println(y: Int, str: String): Int {
        var cnt = 0
        val position = Point(0, y)
        position.x = 0
        while (position.x < str.length) {
            cnt += setPixel(position, PixelImplConsole(str.get(position.x)))
            position.x++
        }
        val temp: Pixel = PixelImplConsole()
        while (position.x < rect.right - rect.left) {
            cnt += setPixel(position, temp)
            position.x++
        }
        return cnt
    }

    /**
     * clear the screen buffer
     */
    override fun clearWindowBuffer() {
        val pix: Pixel = PixelImplConsole()
        val point = Point(0, 0)
        point.y = rect.top
        while (point.y < rect.bottom) {
            point.x = rect.left
            while (point.x < rect.right) {
                setPixel(point, pix)
                point.x++
            }
            point.y++
        }
        paint()
    }
}
