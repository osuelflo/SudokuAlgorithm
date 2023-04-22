import edu.macalester.graphics.*;

import java.util.HashSet;

public class Cell {
    private String cellTag;
    private Rectangle cellShape;
    private GraphicsText display;
    private CanvasWindow canvas;

    public Cell(String tag, Rectangle box, CanvasWindow canvas) {
        cellTag = tag;
        cellShape = box;
        display = new GraphicsText("", box.getX() + 17.0, box.getY() + 30.0);
        display.setFont(FontStyle.BOLD, 20);
        canvas.add(display);
    }

    public void setTag(String newTag) {
        cellTag = newTag;
    }

    public void setDisplay(String text) {
        display.setText(text);
    }

    public String getCellTag() {
        return cellTag;
    }

    public Rectangle getCellShape() {
        return cellShape;
    }
}
