package view.console

import model.structs.Rect
import java.io.IOException

interface Screen {
    fun createWindow(rect: Rect): Window?

    val rect: Rect

    fun paint(): Boolean

    fun forcePaint(): Boolean

    fun clearScreenBuffer(): Int

    companion object {
        @JvmStatic
        fun clear() {
            try {
                ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor()
            } catch (e: InterruptedException) { /*I don't care!*/
            } catch (e: IOException) {
            }
        }
    }
}
