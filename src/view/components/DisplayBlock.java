package view.components;

import model.structs.Rect;

import java.util.ArrayList;

public class DisplayBlock extends AbstractDisplayBlock {

    int height;
    int width;
    boolean autoCalculateHeight;
    boolean autoCalculateWidth;
    ChildLayout childLayout;
    ArrayList<AbstractDisplayBlock> children;

    public DisplayBlock(Rect margin, AlignType verticalAlign, AlignType horizontalAlign) {
        super(margin, verticalAlign, horizontalAlign);
        children = new ArrayList<>();
        childLayout = ChildLayout.LEFT_TO_RIGHT;
        autoCalculateHeight = true;
        autoCalculateWidth = true;
    }

    public DisplayBlock() {
        super();
        children = new ArrayList<>();
        childLayout = ChildLayout.LEFT_TO_RIGHT;
        autoCalculateHeight = true;
        autoCalculateWidth = true;
    }

    public boolean removeChild(int index) {
        AbstractDisplayBlock child = children.remove(index);
        if(autoCalculateWidth) {
            switch(childLayout) {
                case FLEX_X, RIGHT_TO_LEFT, LEFT_TO_RIGHT -> width -= child.getOccupiedWidth();
                case FLEX_Y, UP_TO_DOWN, DOWN_TO_UP -> {
                    if(width == child.getOccupiedWidth()) {
                        width = 0;
                        for(AbstractDisplayBlock enumChild : children) {
                            width = Math.max(width, enumChild.getOccupiedWidth());
                        }
                    }
                }
            }
        }
        if(autoCalculateHeight) {
            switch(childLayout) {
                case FLEX_X, RIGHT_TO_LEFT, LEFT_TO_RIGHT -> {
                    if(height == child.getOccupiedHeight()) {
                        height = 0;
                        for(AbstractDisplayBlock enumChild : children) {
                            height = Math.max(height, enumChild.getOccupiedHeight());
                        }
                    }
                }
                case FLEX_Y, UP_TO_DOWN, DOWN_TO_UP -> height -= child.getOccupiedHeight();
            }
        }
        updateSizeRecursive(null);
        return true;
    }

    public boolean addChild(AbstractDisplayBlock child) {
        if(autoCalculateWidth) {
            switch(childLayout) {
                case FLEX_X, RIGHT_TO_LEFT, LEFT_TO_RIGHT -> width += child.getOccupiedWidth();
                case FLEX_Y, UP_TO_DOWN, DOWN_TO_UP -> width = Math.max(width, child.getOccupiedWidth());
            }
        }
        if(autoCalculateHeight) {
            switch(childLayout) {
                case FLEX_X, RIGHT_TO_LEFT, LEFT_TO_RIGHT -> height = Math.max(height, child.getOccupiedHeight());
                case FLEX_Y, UP_TO_DOWN, DOWN_TO_UP -> height += child.getOccupiedHeight();
            }
        }
        child.setParent(this);
        if(!children.add(child))
            return false;
        updateSizeRecursive(null);/* size already updated, passing null */
        return true;
    }

    public boolean changeChild(int index, AbstractDisplayBlock child) {
        child.setParent(this);
        AbstractDisplayBlock childOld = children.set(index, child);
        if(autoCalculateWidth) {
            switch(childLayout) {
                case FLEX_X, RIGHT_TO_LEFT, LEFT_TO_RIGHT -> width += child.getOccupiedWidth() - childOld.getOccupiedWidth();
                case FLEX_Y, UP_TO_DOWN, DOWN_TO_UP -> {
                    if(width <= child.getOccupiedWidth()) {
                        width = child.getOccupiedWidth();
                    } else if(width > child.getOccupiedWidth()) {
                        width = 0;
                        for(AbstractDisplayBlock enumChild : children) {
                            width = Math.max(width, enumChild.getOccupiedWidth());
                        }
                    }
                }
            }
        }
        if(autoCalculateHeight) {
            switch(childLayout) {
                case FLEX_X, RIGHT_TO_LEFT, LEFT_TO_RIGHT -> {
                    if(height <= child.getOccupiedHeight()) {
                        height = child.getOccupiedHeight();
                    } else if(height > child.getOccupiedHeight()) {
                        height = 0;
                        for(AbstractDisplayBlock enumChild : children) {
                            height = Math.max(height, enumChild.getOccupiedHeight());
                        }
                    }
                }
                case FLEX_Y, UP_TO_DOWN, DOWN_TO_UP -> height += child.getOccupiedHeight() - childOld.getOccupiedHeight();
            }
        }
        updateSizeRecursive(null);
        return true;
    }

    public ArrayList<AbstractDisplayBlock> getChildren() {
        return children;
    }

    public void setHeight(int height) {
        autoCalculateHeight = false;
        this.height = height;
    }

    public void setWidth(int width) {
        autoCalculateWidth = false;
        this.width = width;
    }

    public void setChildLayout(ChildLayout childLayout) {
        this.childLayout = childLayout;
    }

    @Override
    public int getAvailableWidth() {
        return width;
    }

    @Override
    public int getAvailableHeight() {
        return height;
    }

    @Override
    protected void updateSize(AbstractDisplayBlock child) {
        if(child == null) {
            return;
        }
        if(autoCalculateWidth) {
            switch(childLayout) {
                case FLEX_X, RIGHT_TO_LEFT, LEFT_TO_RIGHT -> {
                    width = 0;
                    for(AbstractDisplayBlock enumChild : children) {
                        width += enumChild.getOccupiedWidth();
                    }
                }
                case FLEX_Y, UP_TO_DOWN, DOWN_TO_UP -> {
                    if(width <= child.getOccupiedWidth()) {
                        width = child.getOccupiedWidth();
                    } else if(width > child.getOccupiedWidth()) {
                        width = 0;
                        for(AbstractDisplayBlock enumChild : children) {
                            width = Math.max(width, enumChild.getOccupiedWidth());
                        }
                    }
                }
            }
        }
        if(autoCalculateHeight) {
            switch(childLayout) {
                case FLEX_X, RIGHT_TO_LEFT, LEFT_TO_RIGHT -> {
                    if(height <= child.getOccupiedHeight()) {
                        height = child.getOccupiedHeight();
                    } else if(height > child.getOccupiedHeight()) {
                        height = 0;
                        for(AbstractDisplayBlock enumChild : children) {
                            height = Math.max(height, enumChild.getOccupiedHeight());
                        }
                    }
                }
                case FLEX_Y, UP_TO_DOWN, DOWN_TO_UP -> {
                    height = 0;
                    for(AbstractDisplayBlock enumChild : children) {
                        height += enumChild.getOccupiedHeight();
                    }
                }
            }
        }
    }

    public ChildLayout getChildLayout() {
        return childLayout;
    }
}
