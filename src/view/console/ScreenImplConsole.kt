package view.console

import model.structs.Point
import model.structs.Rect
import view.console.Screen.Companion.clear

class ScreenImplConsole(height: Int, width: Int) : Screen {
    private val buffer: Array<Array<Pixel>> = Array(height) { Array(width) { PixelImplConsole() } }
    override val rect: Rect = Rect(0, height, 0, width)
    private val windows: ArrayList<Window> = ArrayList<Window>()
    private var dirty = 0

    /**
     * get a view to draw on
     * @param rect the rect of view.
     * @return Window if allocated
     */
    override fun createWindow(rect: Rect): Window {
        val window: Window = WindowImplConsole(rect, this)
        windows.add(window)
        return window
    }

    /**
     * set pixel on screen
     * set dirty to true if really changed
     *
     * @param position pixel position
     * @param pix the pixel 'color'
     * @return dirty since we don't care one pixel but the whole screen
     */
    fun setPixel(position: Point, pix: Pixel): Int {
        dirty += buffer[position.y][position.x].set(pix)
        return dirty
    }

    /**
     * display the screen, force refresh by default
     * @return true when repainted
     */
    override fun paint(): Boolean {
        if (dirty == 0) {
            return false
        }
        return forcePaint()
    }

    override fun forcePaint(): Boolean {
        clear()
        val sb = StringBuilder()
        sb.ensureCapacity(rect.right - rect.left)
        for (i in rect.top..<rect.bottom) {
            sb.setLength(0)
            for (j in rect.left..<rect.right) {
                sb.append(buffer[i][j].paint())
                buffer[i][j].flush()
            }
            println(sb)
        }
        dirty = 0
        return true
    }

    override fun clearScreenBuffer(): Int {
        val pixelBlank = PixelImplConsole()
        for (i in rect.top..<rect.bottom) {
            for (j in rect.left..<rect.right) {
                buffer[i][j].set(pixelBlank)
            }
        }
        return 0
    }
}
