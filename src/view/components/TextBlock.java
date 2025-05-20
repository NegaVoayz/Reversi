package view.components;

import model.structs.Rect;

public class TextBlock extends AbstractDisplayBlock{
    private String text;

    public TextBlock(Rect margin, AlignType verticalAlign, AlignType horizontalAlign) {
        super(margin, verticalAlign, horizontalAlign);
    }

    public TextBlock(Rect margin, AlignType verticalAlign, AlignType horizontalAlign, String text) {
        super(margin, verticalAlign, horizontalAlign);
        setText(text);
    }

    public TextBlock(String text) {
        super();
        setText(text);
    }

    public String getText() {
        return text;
    }

    public void setText(String text){
        assert(text != null && !text.isEmpty());
        assert(!text.contains("\n") && !text.contains("\r"));
        this.text = text;
        updateSizeRecursive(null);
    }

    @Override
    public int getAvailableHeight() {
        return 1;
    }

    @Override
    protected void updateSize(AbstractDisplayBlock child) {}

    @Override
    public int getAvailableWidth() {
        return text.length();
    }
}
