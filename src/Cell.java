import edu.macalester.graphics.*;

import java.util.ArrayList;

public class Cell {
    private String cellTag;
    private Rectangle cellShape;
    private GraphicsText display;
    private GraphicsText pencilInDisplay;
    private CanvasWindow canvas;
    private ArrayList<String> pencilInValues;
    private boolean isFixed;

    public Cell(String tag, Rectangle box, CanvasWindow canvas) {
        this.canvas = canvas;
        cellTag = tag;
        cellShape = box;
        isFixed = false;
        display = new GraphicsText("", box.getX() + 35.0, box.getY() + 50.0);
        display.setFont(FontStyle.BOLD, 25);
        pencilInDisplay = new GraphicsText("", box.getX() + 5, box.getY() + 15);
        pencilInDisplay.setFont(FontStyle.BOLD, 10);
        pencilInDisplay.setWrappingWidth(box.getWidth() - 5);
        canvas.add(pencilInDisplay);
        this.canvas.add(display);
        pencilInValues = new ArrayList<>();
    }

    public void setFixed(boolean newFixedValue) {
        canvas.remove(pencilInDisplay);
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

    public void pencilInValue(String value) {
        if (!pencilInValues.contains(value) && !isFixed) {
            pencilInValues.add(value);
            canvas.remove(pencilInDisplay);
            pencilInDisplay.setText(pencilInValues.toString());
            canvas.add(pencilInDisplay);
        }
    }

    public void pencilOutValue(String value) {
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

    public ArrayList<String> getPencilInValues() {
        return pencilInValues;
    }
}
