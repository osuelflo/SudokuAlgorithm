import edu.macalester.graphics.*;
import edu.macalester.graphics.events.Key;

import java.util.HashMap;
import java.util.HashSet;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class SudokuUI {
    private Sudoku su;
    private CanvasWindow canvas;
    String puzzle;
    String solution;
    ArrayList<Cell> cells;
    HashMap<String, HashSet<String>> squareValues;
    private int accessIndex = -1;
    private Scanner scan;

    public SudokuUI() {
        su = new Sudoku();
        canvas = new CanvasWindow("Sudoku", 455, 455);
        puzzle = su.createRandomPuzzle(30);
        solution = su.solve(puzzle);
        squareValues = su.parseGrid(puzzle);
        cells = new ArrayList<>();
        scan = new Scanner(System.in);
        setUpCells();
        setUpGrid();
        canvas.onKeyDown(event -> {
            if (event.getKey() == Key.LEFT_ARROW) {  
                highlight("LEFT"); 
            }
            if (event.getKey() == Key.RIGHT_ARROW) {   
                highlight("RIGHT");
            }
            if (event.getKey() == Key.UP_ARROW) {  
                highlight("UP"); 
            }
            if (event.getKey() == Key.DOWN_ARROW) {   
                highlight("DOWN");
            }
            if (event.getKey() == Key.NUM_1) {
                setCellValue(1);
            }
            if (event.getKey() == Key.NUM_2) {
                setCellValue(2);
            }
            if (event.getKey() == Key.NUM_3) {
                setCellValue(3);
            }
            if (event.getKey() == Key.NUM_4) {
                setCellValue(4);
            }
            if (event.getKey() == Key.NUM_5) {
                setCellValue(5);
            }
            if (event.getKey() == Key.NUM_6) {
                setCellValue(6);
            }
            if (event.getKey() == Key.NUM_7) {
                setCellValue(7);
            }
            if (event.getKey() == Key.NUM_8) {
                setCellValue(8);
            }
            if (event.getKey() == Key.NUM_9) {
                setCellValue(9);
            }
        });
    }

    public static void main(String[] args) {
        new SudokuUI();
    }

    private void setUpGrid() {
        String[][] squareTags = su.getSquareTags();
        int index = 0;
        for (int i=0; i < 9; i++) {
            for (int j=0; j < 9; j++) {
                String tag = squareTags[i][j];
                Cell currentCell = cells.get(index);
                currentCell.setTag(tag);
                HashSet<String> cands = squareValues.get(tag);
                if (cands.size() == 1) {
                    Iterator<String> iter = cands.iterator();
                    currentCell.setDisplay(iter.next());
                }
                index++;
            }
        }
    }

    private void setUpCells() {
        double columnCount = 25;
        double rowCount = 25;
        for (int i=0; i < 81; i++) {
            if(columnCount > 405) {
                rowCount += 45;
                columnCount = 25;
            }
            Rectangle cellShape = new Rectangle(columnCount, rowCount, 43.75, 43.75);
            Cell cell = new Cell("", cellShape, canvas);
            canvas.add(cell.getCellShape());
            cells.add(cell);
            columnCount += 45;
        }
    }

    private void setCellValue(int value) {
        if (accessIndex != -1) {
            Cell currentCell = cells.get(accessIndex);
            if (squareValues.get(currentCell.getCellTag()).size() > 1) {
                currentCell.setDisplay(Integer.toString(value));
            }
        }
    }

    private void highlight(String direc) {
        if (accessIndex != -1) {
            cells.get(accessIndex).getCellShape().setStrokeColor(Color.BLACK);
        }
        else {
            if (direc.equals("LEFT") || direc.equals("UP")) {
                accessIndex = 0;
            }
        }
        if (direc.equals("LEFT")) {
            if ((accessIndex % 9) - 1 < 0) {
                accessIndex += 9;
            }
            accessIndex = accessIndex - 1;
        }
        if (direc.equals("RIGHT")) {
            if ((accessIndex % 9) + 1 == 9) {
                accessIndex -= 9;
            }
            accessIndex = accessIndex + 1;
        }
        if (direc.equals("UP")) {
            if ((accessIndex % 81) - 9 < 0) {
                accessIndex += 81;
            }
            accessIndex -= 9;
        }
        if (direc.equals("DOWN")) {
            accessIndex = (accessIndex + 9) % 81;
        }
        cells.get(accessIndex).getCellShape().setStrokeColor(Color.RED);
    }
}
