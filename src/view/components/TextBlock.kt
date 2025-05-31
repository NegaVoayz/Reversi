package view.components

import model.structs.Rect

class TextBlock : AbstractDisplayBlock {
    private var text: String? = null

    constructor(margin: Rect, verticalAlign: AlignType?, horizontalAlign: AlignType?) : super(
        margin,
        verticalAlign,
        horizontalAlign
    )

    constructor(margin: Rect, verticalAlign: AlignType?, horizontalAlign: AlignType?, text: String) : super(
        margin,
        verticalAlign,
        horizontalAlign
    ) {
        setText(text)
    }

    constructor(text: String) : super() {
        setText(text)
    }

    fun getText(): String {
        return text!!
    }

    fun setText(text: String) {
        assert(!text.isEmpty())
        assert(!text.contains("\n") && !text.contains("\r"))
        this.text = text
        updateSizeRecursive(null)
    }

    override val availableHeight: Int
        get() = 1

    override fun updateSize(child: AbstractDisplayBlock?) {}

    override val availableWidth: Int
        get() = text!!.length
}
