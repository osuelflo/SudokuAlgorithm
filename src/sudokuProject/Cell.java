package sudokuProject;

import edu.macalester.graphics.*;
import java.util.ArrayList;

/**
 * @author Adam Schroder, Owen Suelflow
 * A cell represents a single board element (81 total) on the sudoku board in the UI
 */
public class Cell {
    private String cellTag;
    private Rectangle cellShape;
    private GraphicsText display;
    private GraphicsText pencilInDisplay;
    private CanvasWindow canvas;
    private ArrayList<String> pencilInValues;
    private boolean isFixed;

    /**
     * Creates a new instance of a cell
     * @param tag The String corresponding to the tag given to each square, e.g. A1
     * @param box The Rectangle defining the graphics of the cell
     * @param canvas The CanvasWindow on which the cell will be drawn
     */
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

    /**
     * Sets the value of the boolean variable isFixed
     */
    public void setFixed(boolean newFixedValue) {
        canvas.remove(pencilInDisplay);
        isFixed = newFixedValue;
    }

    /**
     * Sets the tag of the cell to the given String
     */
    public void setTag(String newTag) {
        cellTag = newTag;
    }

    /**
     * Sets the number displayed by the cell to the given String
     */
    public void setDisplay(String text) {
        if (!isFixed) {
            display.setText(text);
        }
    }

    /**
     * Adds a given String value (if not already contained) to the penciled-in values for the cell
     */
    public void pencilInValue(String value) {
        if (!pencilInValues.contains(value) && !isFixed) {
            pencilInValues.add(value);
            canvas.remove(pencilInDisplay);
            pencilInDisplay.setText(pencilInValues.toString());
            canvas.add(pencilInDisplay);
        }
    }

    /**
     * Used in the UI; backspace will invoke this method. Removes the last value in the penciled-in values for the cell
     */
    public void pencilOutValue() {
        if (pencilInValues.size() > 0 && !isFixed) {
            pencilInValues.remove(pencilInValues.size()-1);
            if(pencilInValues.size() == 0){
                pencilInDisplay.setText("");
            }
            else{
                pencilInDisplay.setText(pencilInValues.toString());
            }
        }
    }

    /**
     * Sets the current displayed value to be the empty String
     */
    public void writeOutValue(){
        if(!isFixed){
            display.setText("");
        }
    }

    /**
     * Returns the current value of the boolean isFixed
     */
    public boolean isFixed() {
        return isFixed;
    }

    /**
     * Returns the String tag for the cell
     */
    public String getCellTag() {
        return cellTag;
    }

    /**
     * Returns the Rectangle graphics of the cell
     */
    public Rectangle getCellShape() {
        return cellShape;
    }

    /**
     * Returns the penciled-in values of the cell as an ArrayList<> of Strings
     */
    public ArrayList<String> getPencilInValues() {
        return pencilInValues;
    }
}
