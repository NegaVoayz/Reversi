package view.components

import model.structs.Rect

abstract class AbstractDisplayBlock {
    var parent: AbstractDisplayBlock? = null

    var margin: Rect

    var verticalAlign: AlignType?

    var horizontalAlign: AlignType?

    constructor(margin: Rect, verticalAlign: AlignType?, horizontalAlign: AlignType?) {
        this.margin = margin
        this.verticalAlign = verticalAlign
        this.horizontalAlign = horizontalAlign
    }

    constructor() {
        this.margin = Rect(0, 0, 0, 0)
        this.verticalAlign = AlignType.MIDDLE
        this.horizontalAlign = AlignType.BEGIN
    }

    protected fun updateSizeRecursive(child: AbstractDisplayBlock?) {
        updateSize(child)
        if (parent != null) {
            parent!!.updateSizeRecursive(this)
        }
    }

    val occupiedWidth: Int
        get() = this.availableWidth + margin.left + margin.right

    val occupiedHeight: Int
        get() = this.availableHeight + margin.top + margin.bottom

    abstract val availableWidth: Int

    abstract val availableHeight: Int

    protected abstract fun updateSize(child: AbstractDisplayBlock?)
}
