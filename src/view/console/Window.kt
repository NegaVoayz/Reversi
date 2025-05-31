package view.console

import model.structs.Point

interface Window {
    fun setPixel(position: Point, pix: Pixel): Int

    fun paint()

    fun print(position: Point, str: String): Int

    fun println(y: Int, str: String): Int

    fun clearWindowBuffer()
}
