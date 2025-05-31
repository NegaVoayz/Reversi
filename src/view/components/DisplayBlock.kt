package view.components

import model.structs.Rect
import kotlin.math.max

class DisplayBlock : AbstractDisplayBlock {
    override var availableHeight: Int = 0
    override var availableWidth: Int = 0
    var autoCalculateHeight: Boolean
    var autoCalculateWidth: Boolean
    var childLayout: ChildLayout
    var children: ArrayList<AbstractDisplayBlock>

    constructor(margin: Rect, verticalAlign: AlignType?, horizontalAlign: AlignType?) : super(
        margin,
        verticalAlign,
        horizontalAlign
    ) {
        children = ArrayList<AbstractDisplayBlock>()
        childLayout = ChildLayout.LEFT_TO_RIGHT
        autoCalculateHeight = true
        autoCalculateWidth = true
    }

    constructor() : super() {
        children = ArrayList<AbstractDisplayBlock>()
        childLayout = ChildLayout.LEFT_TO_RIGHT
        autoCalculateHeight = true
        autoCalculateWidth = true
    }

    fun removeChild(index: Int): Boolean {
        val child = children.removeAt(index)
        if (autoCalculateWidth) {
            when (childLayout) {
                ChildLayout.FLEX_X, ChildLayout.RIGHT_TO_LEFT, ChildLayout.LEFT_TO_RIGHT -> this.availableWidth -= child.occupiedWidth
                ChildLayout.FLEX_Y, ChildLayout.UP_TO_DOWN, ChildLayout.DOWN_TO_UP -> {
                    if (this.availableWidth == child.occupiedWidth) {
                        this.availableWidth = 0
                        for (enumChild in children) {
                            this.availableWidth = max(this.availableWidth, enumChild.occupiedWidth)
                        }
                    }
                }
            }
        }
        if (autoCalculateHeight) {
            when (childLayout) {
                ChildLayout.FLEX_X, ChildLayout.RIGHT_TO_LEFT, ChildLayout.LEFT_TO_RIGHT -> {
                    if (this.availableHeight == child.occupiedHeight) {
                        this.availableHeight = 0
                        for (enumChild in children) {
                            this.availableHeight = max(this.availableHeight, enumChild.occupiedHeight)
                        }
                    }
                }

                ChildLayout.FLEX_Y, ChildLayout.UP_TO_DOWN, ChildLayout.DOWN_TO_UP -> this.availableHeight -= child.occupiedHeight
            }
        }
        updateSizeRecursive(null)
        return true
    }

    fun addChild(child: AbstractDisplayBlock): Boolean {
        if (autoCalculateWidth) {
            when (childLayout) {
                ChildLayout.FLEX_X, ChildLayout.RIGHT_TO_LEFT, ChildLayout.LEFT_TO_RIGHT -> this.availableWidth += child.occupiedWidth
                ChildLayout.FLEX_Y, ChildLayout.UP_TO_DOWN, ChildLayout.DOWN_TO_UP -> this.availableWidth = max(
                    this.availableWidth, child.occupiedWidth
                )
            }
        }
        if (autoCalculateHeight) {
            when (childLayout) {
                ChildLayout.FLEX_X, ChildLayout.RIGHT_TO_LEFT, ChildLayout.LEFT_TO_RIGHT -> this.availableHeight = max(
                    this.availableHeight, child.occupiedHeight
                )

                ChildLayout.FLEX_Y, ChildLayout.UP_TO_DOWN, ChildLayout.DOWN_TO_UP -> this.availableHeight += child.occupiedHeight
            }
        }
        child.parent = this
        if (!children.add(child)) return false
        updateSizeRecursive(null) /* size already updated, passing null */
        return true
    }

    fun changeChild(index: Int, child: AbstractDisplayBlock): Boolean {
        child.parent = this
        val childOld = children.set(index, child)
        if (autoCalculateWidth) {
            when (childLayout) {
                ChildLayout.FLEX_X, ChildLayout.RIGHT_TO_LEFT, ChildLayout.LEFT_TO_RIGHT -> this.availableWidth += child.occupiedWidth - childOld.occupiedWidth
                ChildLayout.FLEX_Y, ChildLayout.UP_TO_DOWN, ChildLayout.DOWN_TO_UP -> {
                    if (this.availableWidth <= child.occupiedWidth) {
                        this.availableWidth = child.occupiedWidth
                    } else if (this.availableWidth > child.occupiedWidth) {
                        this.availableWidth = 0
                        for (enumChild in children) {
                            this.availableWidth = max(this.availableWidth, enumChild.occupiedWidth)
                        }
                    }
                }
            }
        }
        if (autoCalculateHeight) {
            when (childLayout) {
                ChildLayout.FLEX_X, ChildLayout.RIGHT_TO_LEFT, ChildLayout.LEFT_TO_RIGHT -> {
                    if (this.availableHeight <= child.occupiedHeight) {
                        this.availableHeight = child.occupiedHeight
                    } else if (this.availableHeight > child.occupiedHeight) {
                        this.availableHeight = 0
                        for (enumChild in children) {
                            this.availableHeight = max(this.availableHeight, enumChild.occupiedHeight)
                        }
                    }
                }

                ChildLayout.FLEX_Y, ChildLayout.UP_TO_DOWN, ChildLayout.DOWN_TO_UP -> this.availableHeight += child.occupiedHeight - childOld.occupiedHeight
            }
        }
        updateSizeRecursive(null)
        return true
    }

    fun setHeight(height: Int) {
        autoCalculateHeight = false
        this.availableHeight = height
    }

    fun setWidth(width: Int) {
        autoCalculateWidth = false
        this.availableWidth = width
    }

    override fun updateSize(child: AbstractDisplayBlock?) {
        if (child == null) {
            return
        }
        if (autoCalculateWidth) {
            when (childLayout) {
                ChildLayout.FLEX_X, ChildLayout.RIGHT_TO_LEFT, ChildLayout.LEFT_TO_RIGHT -> {
                    this.availableWidth = 0
                    for (enumChild in children) {
                        this.availableWidth += enumChild.occupiedWidth
                    }
                }

                ChildLayout.FLEX_Y, ChildLayout.UP_TO_DOWN, ChildLayout.DOWN_TO_UP -> {
                    if (this.availableWidth <= child.occupiedWidth) {
                        this.availableWidth = child.occupiedWidth
                    } else if (this.availableWidth > child.occupiedWidth) {
                        this.availableWidth = 0
                        for (enumChild in children) {
                            this.availableWidth = max(this.availableWidth, enumChild.occupiedWidth)
                        }
                    }
                }
            }
        }
        if (autoCalculateHeight) {
            when (childLayout) {
                ChildLayout.FLEX_X, ChildLayout.RIGHT_TO_LEFT, ChildLayout.LEFT_TO_RIGHT -> {
                    if (this.availableHeight <= child.occupiedHeight) {
                        this.availableHeight = child.occupiedHeight
                    } else if (this.availableHeight > child.occupiedHeight) {
                        this.availableHeight = 0
                        for (enumChild in children) {
                            this.availableHeight = max(this.availableHeight, enumChild.occupiedHeight)
                        }
                    }
                }

                ChildLayout.FLEX_Y, ChildLayout.UP_TO_DOWN, ChildLayout.DOWN_TO_UP -> {
                    this.availableHeight = 0
                    for (enumChild in children) {
                        this.availableHeight += enumChild.occupiedHeight
                    }
                }
            }
        }
    }
}
