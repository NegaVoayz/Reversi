package view.components;

import model.structs.Rect;

public abstract class AbstractDisplayBlock {

    AbstractDisplayBlock parent;

    Rect margin;

    AlignType verticalAlign;

    AlignType horizontalAlign;

    public AbstractDisplayBlock(Rect margin, AlignType verticalAlign, AlignType horizontalAlign) {
        this.margin = margin;
        this.verticalAlign = verticalAlign;
        this.horizontalAlign = horizontalAlign;
    }

    public AbstractDisplayBlock() {
        this.margin = new Rect(0,0,0,0);
        this.verticalAlign = AlignType.MIDDLE;
        this.horizontalAlign = AlignType.BEGIN;
    }

    protected void setParent(AbstractDisplayBlock parent) {
        this.parent = parent;
    }

    protected void updateSizeRecursive(AbstractDisplayBlock child) {
        updateSize(child);
        if(parent != null) {
            parent.updateSizeRecursive(this);
        }
    }

    public Rect getMargin() {
        return margin;
    }

    public void setMargin(Rect margin) {
        this.margin = margin;
    }

    public void setVerticalAlign(AlignType verticalAlign) {
        this.verticalAlign = verticalAlign;
    }

    public void setHorizontalAlign(AlignType horizontalAlign) {
        this.horizontalAlign = horizontalAlign;
    }

    public AlignType getVerticalAlign() {
        return verticalAlign;
    }

    public AlignType getHorizontalAlign() {
        return horizontalAlign;
    }

    public int getOccupiedWidth() {
        return getAvailableWidth() + margin.left + margin.right;
    }

    public int getOccupiedHeight() {
        return getAvailableHeight() + margin.top + margin.bottom;
    }

    public abstract int getAvailableWidth();

    public abstract int getAvailableHeight();

    protected abstract void updateSize(AbstractDisplayBlock child);
}
