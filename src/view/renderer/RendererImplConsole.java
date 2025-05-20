package view.renderer;

import model.structs.Point;
import model.structs.Rect;
import view.console.*;
import view.components.AbstractDisplayBlock;
import view.components.DisplayBlock;
import view.components.GridBlock;
import view.components.TextBlock;

public class RendererImplConsole implements Renderer {
    private final Screen screen;
    private final Window window;
    public static final Rect WINDOW_RECT = new Rect(0,32,0,120);

    protected RendererImplConsole() {
        screen = new ScreenImplConsole(WINDOW_RECT.bottom,WINDOW_RECT.right);
        window = screen.createWindow(WINDOW_RECT);
    }

    private boolean recursiveRender(AbstractDisplayBlock view, Rect availableRect) {
        Rect displayRect = new Rect(
                availableRect.top + view.getMargin().top,
                availableRect.bottom - view.getMargin().bottom,
                availableRect.left + view.getMargin().left,
                availableRect.right - view.getMargin().right);

        if(displayRect.top >= displayRect.bottom
                || displayRect.left >= displayRect.right) {
            return false;
        }

        if(view.getAvailableWidth() > displayRect.right - displayRect.left) { return false; }
        switch (view.getHorizontalAlign()) {
            case BEGIN -> displayRect.right = displayRect.left + view.getAvailableWidth();
            case END -> displayRect.left = displayRect.right - view.getAvailableWidth();
            case MIDDLE -> {
                displayRect.left = (displayRect.right + displayRect.left - view.getAvailableWidth())/2;
                displayRect.right = displayRect.left + view.getAvailableWidth();
            }
        }

        if(view.getAvailableHeight() > displayRect.bottom - displayRect.top) { return false; }
        switch (view.getVerticalAlign()) {
            case BEGIN -> displayRect.bottom = displayRect.top + view.getAvailableHeight();
            case END -> displayRect.top = displayRect.bottom - view.getAvailableHeight();
            case MIDDLE -> {
                displayRect.top = (displayRect.top + displayRect.bottom - view.getAvailableHeight())/2;
                displayRect.bottom = displayRect.top + view.getAvailableHeight();
            }
        }

        Point startPoint = new Point(displayRect.left, displayRect.top);
        if (view instanceof TextBlock viewText) {
            window.print(startPoint, viewText.getText());
            return true;
        } else if (view instanceof GridBlock viewGrid) {
            int width = viewGrid.getGrid()[0].length;
            int height = viewGrid.getGrid().length;
            Point offset = new Point();
            Point showPosition = new Point(startPoint.x, startPoint.y);
            for(offset.x = 0; offset.x < width; offset.x++) {
                showPosition.y = startPoint.y;
                for(offset.y = 0; offset.y < height; offset.y++) {
                    window.setPixel(showPosition, new PixelImplConsole((char)(viewGrid.getGrid()[offset.y][offset.x])));
                    showPosition.y++;
                }
                showPosition.x += 2;
            }
            return true;
        } else if (view instanceof DisplayBlock viewDiv) {
            switch(viewDiv.getChildLayout()) {
                case FLEX_X -> {
                    int innerMargin = viewDiv.getAvailableWidth();
                    for(AbstractDisplayBlock child : viewDiv.getChildren()) {
                        innerMargin -= child.getOccupiedWidth();
                    }
                    innerMargin /= viewDiv.getChildren().size() - 1;
                    Rect childrenRect = new Rect(displayRect.top,displayRect.bottom,displayRect.left,displayRect.right);
                    for(AbstractDisplayBlock child : viewDiv.getChildren()) {
                        childrenRect.right = childrenRect.left + child.getOccupiedWidth();
                        if(!recursiveRender(child, childrenRect))
                            return false;
                        childrenRect.left = childrenRect.right + innerMargin;
                    }
                }
                case FLEX_Y -> {
                    int innerMargin = viewDiv.getAvailableHeight();
                    for(AbstractDisplayBlock child : viewDiv.getChildren()) {
                        innerMargin -= child.getOccupiedHeight();
                    }
                    innerMargin /= viewDiv.getChildren().size() - 1;
                    Rect childrenRect = new Rect(displayRect.top,displayRect.bottom,displayRect.left,displayRect.right);
                    for(AbstractDisplayBlock child : viewDiv.getChildren()) {
                        childrenRect.bottom = childrenRect.top + child.getOccupiedWidth();
                        if(!recursiveRender(child, childrenRect))
                            return false;
                        childrenRect.top = childrenRect.bottom + innerMargin;
                    }
                }
                case LEFT_TO_RIGHT -> {
                    Rect childrenRect = new Rect(displayRect.top,displayRect.bottom,displayRect.left,displayRect.right);
                    for(AbstractDisplayBlock child : viewDiv.getChildren()) {
                        childrenRect.right = childrenRect.left + child.getOccupiedWidth();
                        if(!recursiveRender(child, childrenRect))
                            return false;
                        childrenRect.left = childrenRect.right;
                    }
                }
                case RIGHT_TO_LEFT -> {
                    Rect childrenRect = new Rect(displayRect.top,displayRect.bottom,displayRect.left,displayRect.right);
                    for(AbstractDisplayBlock child : viewDiv.getChildren()) {
                        childrenRect.left = childrenRect.right - child.getOccupiedWidth();
                        if(!recursiveRender(child, childrenRect))
                            return false;
                        childrenRect.right = childrenRect.left;
                    }
                }
                case UP_TO_DOWN -> {
                    Rect childrenRect = new Rect(displayRect.top,displayRect.bottom,displayRect.left,displayRect.right);
                    for(AbstractDisplayBlock child : viewDiv.getChildren()) {
                        childrenRect.bottom = childrenRect.top + child.getOccupiedHeight();
                        if(!recursiveRender(child, childrenRect))
                            return false;
                        childrenRect.top = childrenRect.bottom;
                    }
                }
                case DOWN_TO_UP -> {
                    Rect childrenRect = new Rect(displayRect.top,displayRect.bottom,displayRect.left,displayRect.right);
                    for(AbstractDisplayBlock child : viewDiv.getChildren()) {
                        childrenRect.top = childrenRect.bottom - child.getOccupiedHeight();
                        if(!recursiveRender(child, childrenRect))
                            return false;
                        childrenRect.bottom = childrenRect.top;
                    }
                }
            }
        }
        return true;
    }

    /**
     * @param view the view
     * @return true if succeeded
     */
    @Override
    public boolean render(AbstractDisplayBlock view) {
        window.clearWindowBuffer();
        if(!recursiveRender(view, screen.getRect()))
            return false;
        window.paint();
        screen.paint();
        return true;
    }
}
