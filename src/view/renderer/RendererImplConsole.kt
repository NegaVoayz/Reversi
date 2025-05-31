package view.renderer

import model.structs.Point
import model.structs.Rect
import view.components.*
import view.components.AlignType.*
import view.console.PixelImplConsole
import view.console.Screen
import view.console.ScreenImplConsole
import view.console.Window

class RendererImplConsole : Renderer {
    private val screen: Screen
    private val window: Window

    init {
        screen = ScreenImplConsole(WINDOW_RECT.bottom, WINDOW_RECT.right)
        window = screen.createWindow(WINDOW_RECT)
    }

    private fun recursiveRender(block: AbstractDisplayBlock, availableRect: Rect): Boolean {
        val displayRect = Rect(
            availableRect.top + block.margin.top,
            availableRect.bottom - block.margin.bottom,
            availableRect.left + block.margin.left,
            availableRect.right - block.margin.right
        )

        if (displayRect.top >= displayRect.bottom
            || displayRect.left >= displayRect.right
        ) {
            return false
        }

        if (block.availableWidth > displayRect.right - displayRect.left) {
            return false
        }
        when (block.horizontalAlign) {
            BEGIN -> displayRect.right = displayRect.left + block.availableWidth
            END -> displayRect.left = displayRect.right - block.availableWidth
            MIDDLE -> {
                displayRect.left = (displayRect.right + displayRect.left - block.availableWidth) / 2
                displayRect.right = displayRect.left + block.availableWidth
            }

            null -> throw IllegalArgumentException("NULL should not happen")
        }

        if (block.availableHeight > displayRect.bottom - displayRect.top) {
            return false
        }
        when (block.verticalAlign) {
            BEGIN -> displayRect.bottom = displayRect.top + block.availableHeight
            END -> displayRect.top = displayRect.bottom - block.availableHeight
            MIDDLE -> {
                displayRect.top = (displayRect.top + displayRect.bottom - block.availableHeight) / 2
                displayRect.bottom = displayRect.top + block.availableHeight
            }

            null -> throw IllegalArgumentException("NULL should not happen")
        }

        val startPoint = Point(displayRect.left, displayRect.top)
        if (block is TextBlock) {
            window.print(startPoint, block.getText())
            return true
        } else if (block is GridBlock) {
            val width = block.grid[0].size
            val height = block.grid.size
            val offset = Point()
            val showPosition = Point(startPoint.x, startPoint.y)
            offset.x = 0
            while (offset.x < width) {
                showPosition.y = startPoint.y
                offset.y = 0
                while (offset.y < height) {
                    window.setPixel(showPosition, PixelImplConsole((block.grid[offset.y][offset.x]).toChar()))
                    showPosition.y++
                    offset.y++
                }
                showPosition.x += 2
                offset.x++
            }
            return true
        } else if (block is DisplayBlock) {
            when (block.childLayout) {
                ChildLayout.FLEX_X -> {
                    var innerMargin = block.availableWidth
                    for (child in block.children) {
                        innerMargin -= child.occupiedWidth
                    }
                    innerMargin /= block.children.size - 1
                    val childrenRect = Rect(displayRect.top, displayRect.bottom, displayRect.left, displayRect.right)
                    for (child in block.children) {
                        childrenRect.right = childrenRect.left + child.occupiedWidth
                        if (!recursiveRender(child, childrenRect)) return false
                        childrenRect.left = childrenRect.right + innerMargin
                    }
                }

                ChildLayout.FLEX_Y -> {
                    var innerMargin = block.availableHeight
                    for (child in block.children) {
                        innerMargin -= child.occupiedHeight
                    }
                    innerMargin /= block.children.size - 1
                    val childrenRect = Rect(displayRect.top, displayRect.bottom, displayRect.left, displayRect.right)
                    for (child in block.children) {
                        childrenRect.bottom = childrenRect.top + child.occupiedWidth
                        if (!recursiveRender(child, childrenRect)) return false
                        childrenRect.top = childrenRect.bottom + innerMargin
                    }
                }

                ChildLayout.LEFT_TO_RIGHT -> {
                    val childrenRect = Rect(displayRect.top, displayRect.bottom, displayRect.left, displayRect.right)
                    for (child in block.children) {
                        childrenRect.right = childrenRect.left + child.occupiedWidth
                        if (!recursiveRender(child, childrenRect)) return false
                        childrenRect.left = childrenRect.right
                    }
                }

                ChildLayout.RIGHT_TO_LEFT -> {
                    val childrenRect = Rect(displayRect.top, displayRect.bottom, displayRect.left, displayRect.right)
                    for (child in block.children) {
                        childrenRect.left = childrenRect.right - child.occupiedWidth
                        if (!recursiveRender(child, childrenRect)) return false
                        childrenRect.right = childrenRect.left
                    }
                }

                ChildLayout.UP_TO_DOWN -> {
                    val childrenRect = Rect(displayRect.top, displayRect.bottom, displayRect.left, displayRect.right)
                    for (child in block.children) {
                        childrenRect.bottom = childrenRect.top + child.occupiedHeight
                        if (!recursiveRender(child, childrenRect)) return false
                        childrenRect.top = childrenRect.bottom
                    }
                }

                ChildLayout.DOWN_TO_UP -> {
                    val childrenRect = Rect(displayRect.top, displayRect.bottom, displayRect.left, displayRect.right)
                    for (child in block.children) {
                        childrenRect.top = childrenRect.bottom - child.occupiedHeight
                        if (!recursiveRender(child, childrenRect)) return false
                        childrenRect.bottom = childrenRect.top
                    }
                }
            }
        }
        return true
    }

    /**
     * @param block the block
     * @return true if succeeded
     */
    override fun render(block: AbstractDisplayBlock): Boolean {
        window.clearWindowBuffer()
        if (!recursiveRender(block, screen.rect)) return false
        window.paint()
        screen.paint()
        return true
    }

    companion object {
        val WINDOW_RECT: Rect = Rect(0, 32, 0, 120)
    }
}
