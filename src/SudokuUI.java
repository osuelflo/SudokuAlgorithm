
import edu.macalester.graphics.*;
import edu.macalester.graphics.events.Key;

import java.util.HashMap;
import java.util.HashSet;
import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

public class SudokuUI {
    private Sudoku su;
    private CanvasWindow canvas;
    String puzzle;
    HashMap<String, HashSet<String>> solution;
    ArrayList<Cell> cells;
    HashMap<String, HashSet<String>> squareValues;
    private int accessIndex = -1;
    private Scanner scan;

    public SudokuUI() {
        su = new Sudoku();
        canvas = new CanvasWindow("Sudoku", 455, 455);
        puzzle = getPuzzleFromFile(new File("/Users/owensuelflow/Documents/Comp128/SudokuAlgorithm/src/HardPuzzles.txt"));
        squareValues = su.parseGrid(puzzle);
        solution = su.search(squareValues);
        cells = new ArrayList<>();
        scan = new Scanner(System.in);
        setUpCells();
        setUpGrid();
        setUpBoldLines();
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
                setCellValue("1");
                if(!checkSolution("1")){
                    displayIncorrect();
                }
            }
            if (event.getKey() == Key.NUM_2) {
                setCellValue("2");
                if(!checkSolution("2")){
                    displayIncorrect();
                }
            }
            if (event.getKey() == Key.NUM_3) {
                setCellValue("3");
                if(!checkSolution("3")){
                    displayIncorrect();
                }
            }
            if (event.getKey() == Key.NUM_4) {
                setCellValue("4");
                if(!checkSolution("4")){
                    displayIncorrect();
                }
            }
            if (event.getKey() == Key.NUM_5) {
                setCellValue("5");
                if(!checkSolution("5")){
                    displayIncorrect();
                }
            }
            if (event.getKey() == Key.NUM_6) {
                setCellValue("6");
                if(!checkSolution("6")){
                    displayIncorrect();
                }
            }
            if (event.getKey() == Key.NUM_7) {
                setCellValue("7");
                if(!checkSolution("7")){
                    displayIncorrect();
                }
            }
            if (event.getKey() == Key.NUM_8) {
                setCellValue("8");
                if(!checkSolution("8")){
                    displayIncorrect();
                }
            }
            if (event.getKey() == Key.NUM_9) {
                setCellValue("9");
                if(!checkSolution("9")){
                    displayIncorrect();
                }
            }
        });
    }

    public static void main(String[] args) {
        new SudokuUI();
    }

    private String getPuzzleFromFile(File fname){
        HashMap<Integer, String> puzzles = su.parseFile(fname);
        Random rand = new Random();
        int ind = rand.nextInt(1, puzzles.size());
        return puzzles.get(ind);
    }

    private boolean checkSolution(String value){
        if (accessIndex != -1) {
            Cell currentCell = cells.get(accessIndex);
            HashSet<String> correctDigit = solution.get(currentCell.getCellTag());
            Iterator<String> iter = correctDigit.iterator();
            String d = iter.next();
            System.out.println("d: "+d);
            System.out.println("value: "+value);
            if(d.equals(value)){
                System.out.println("yay");
                currentCell.getCellShape().setStrokeColor(Color.BLACK);
                currentCell.getCellShape().setStrokeWidth(1);
                return true;
            }
            else{
                System.out.println("nope");
                return false;
            }
        }
        return true;
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

    private void setCellValue(String value) {
        if (accessIndex != -1) {
            Cell currentCell = cells.get(accessIndex);
            if (squareValues.get(currentCell.getCellTag()).size() > 1) {
                currentCell.setDisplay(value);
            }
        }
    }

    private void displayIncorrect(){
        if(accessIndex != -1){
            Cell currentCell = cells.get(accessIndex);
            currentCell.getCellShape().setStrokeColor(Color.RED);
            // currentCell.setDisplay("");
        }
    }

    private void resetFill(){
        Cell currentCell = cells.get(accessIndex);
        currentCell.getCellShape().setFilled(false);
    }
    
    private void pause(){
        try{
            Thread.sleep(2000);
        } catch(Exception e){
            System.out.println("shit");
        }
    }

    private void highlight(String direc) {
        if (accessIndex != -1) {
            cells.get(accessIndex).getCellShape().setStrokeColor(Color.BLACK);
            cells.get(accessIndex).getCellShape().setStrokeWidth(1);
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
        cells.get(accessIndex).getCellShape().setStrokeColor(Color.BLUE);
        cells.get(accessIndex).getCellShape().setStrokeWidth(5);
    }

    private void setUpBoldLines(){
        double x1 = 25;
        double y1 = 25;
        double x2 = 430;
        double y2 = 25;
        for(int i = 1; i < 5; i ++){
            Line l = new Line(x1, y1, x2, y2);
            l.setStrokeWidth(5);
            l.setStrokeColor(Color.BLUE);
            canvas.add(l);
            y1 += 135;
            y2 += 135;
        }
        x1 = 25;
        y1 = 25;
        y2 = 430;
        x2 = 25;
        for(int i = 1; i < 5; i ++){
            Line l = new Line(x1, y1, x2, y2);
            l.setStrokeWidth(5);
            l.setStrokeColor(Color.BLUE);
            canvas.add(l);
            x1 += 135;
            x2 += 135;
        }
    }
}
