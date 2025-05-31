package view.renderer

object RendererFactory {
    fun getRenderer(type: String): Renderer? {
        val rendererImplConsole = RendererImplConsole()
        return when (type) {
            "console" -> rendererImplConsole
            "gui" -> null
            else -> null
        }
    }
}
