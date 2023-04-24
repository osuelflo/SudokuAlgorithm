import edu.macalester.graphics.*;

import java.util.HashSet;
import java.util.ArrayList;

public class Cell {
    private String cellTag;
    private Rectangle cellShape;
    private GraphicsText display;
    private CanvasWindow canvas;
    private ArrayList<Integer> pencilInValues;
    private boolean isFixed;

    public Cell(String tag, Rectangle box, CanvasWindow canvas) {
        this.canvas = canvas;
        cellTag = tag;
        cellShape = box;
        isFixed = false;
        display = new GraphicsText("", box.getX() + 40.0, box.getY() + 55.0);
        display.setFont(FontStyle.BOLD, 20);
        this.canvas.add(display);
        pencilInValues = new ArrayList<>();
    }

    public void setFixed(boolean newFixedValue) {
        isFixed = newFixedValue;
    }

    public void setTag(String newTag) {
        cellTag = newTag;
    }

    public void setDisplay(String text) {
        if (!isFixed) {
            display.setText(text);
        }
    }

    public void pencilInValue(Integer value) {
        if (!pencilInValues.contains(value) && !isFixed) {
            pencilInValues.add(value);
        }
    }

    public void pencilOutValue(Integer value) {
        if (pencilInValues.contains(value) && !isFixed) {
            pencilInValues.remove(value);
        }
    }

    public boolean isFixed() {
        return isFixed;
    }

    public String getCellTag() {
        return cellTag;
    }

    public Rectangle getCellShape() {
        return cellShape;
    }

    public ArrayList<Integer> getPencilInValues() {
        return pencilInValues;
    }
}
