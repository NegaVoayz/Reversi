package view.console

interface Pixel {
    fun set(pix: Pixel): Int

    fun paint(): String?

    fun flush()
}
