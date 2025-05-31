package view.renderer

import view.components.AbstractDisplayBlock

interface Renderer {
    fun render(block: AbstractDisplayBlock): Boolean
}
