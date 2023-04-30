package sudokuProject;

import edu.macalester.graphics.*;
import edu.macalester.graphics.events.Key;
import edu.macalester.graphics.ui.Button;
import java.util.HashMap;
import java.util.HashSet;
import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Adam Schroeder, Owen Suelflow
 * The User Interface employing the Sudoku solver and generator algorithms
 */
public class SudokuUI {
    private Sudoku su;
    private CanvasWindow canvas;
    String puzzle;
    HashMap<String, HashSet<String>> solution;
    ArrayList<Cell> cells;
    HashMap<String, HashSet<String>> squareValues;
    private int accessIndex = -1;
    private Scanner scan;
    private GameState gameState;
    private GraphicsText timer;
    private long time;
    private long startTime;
    private int initNumDigits;
    private int squaresRemaining;

    /**
     * Creates a new instance of the SudokuUI
     */
    public SudokuUI() {
        su = new Sudoku();
        canvas = new CanvasWindow("Sudoku", 1200, 900);
        timer = new GraphicsText("", 990, 700);
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
                if(!checkSolution("1") && gameState == GameState.WRITE_IN){
                    displayIncorrect();
                }
            }
            if (event.getKey() == Key.NUM_2) {
                setCellValue("2");
                if(!checkSolution("2") && gameState == GameState.WRITE_IN){
                    displayIncorrect();
                }
            }
            if (event.getKey() == Key.NUM_3) {
                setCellValue("3");
                if(!checkSolution("3") && gameState == GameState.WRITE_IN){
                    displayIncorrect();
                }
            }
            if (event.getKey() == Key.NUM_4) {
                setCellValue("4");
                if(!checkSolution("4") && gameState == GameState.WRITE_IN){
                    displayIncorrect();
                }
            }
            if (event.getKey() == Key.NUM_5) {
                setCellValue("5");
                if(!checkSolution("5") && gameState == GameState.WRITE_IN){
                    displayIncorrect();
                }
            }
            if (event.getKey() == Key.NUM_6) {
                setCellValue("6");
                if(!checkSolution("6") && gameState == GameState.WRITE_IN){
                    displayIncorrect();
                }
            }
            if (event.getKey() == Key.NUM_7) {
                setCellValue("7");
                if(!checkSolution("7") && gameState == GameState.WRITE_IN){
                    displayIncorrect();
                }
            }
            if (event.getKey() == Key.NUM_8) {
                setCellValue("8");
                if(!checkSolution("8") && gameState == GameState.WRITE_IN){
                    displayIncorrect();
                }
            }
            if (event.getKey() == Key.NUM_9) {
                setCellValue("9");
                if(!checkSolution("9") && gameState == GameState.WRITE_IN){
                    displayIncorrect();
                }
            }
            if (event.getKey() == Key.SPACE) {
                if (gameState == GameState.PENCIL_IN) {
                    gameState = GameState.WRITE_IN;
                }
                else {
                    gameState = GameState.PENCIL_IN;
                }
            }
            if (event.getKey() == Key.DELETE_OR_BACKSPACE){
                if (gameState == GameState.PENCIL_IN) {
                    removePencilMark();
                }
                else {
                    removeWriteIn();
                }
            }
        });
        canvas.animate(() -> {
            updateTimer();
        });
    }

    public static void main(String[] args) {
        SudokuUI SudokuApp = new SudokuUI();
        SudokuApp.run();
    }

    /**
     * Removes the current displayed value for the cell currently at the value of accessIndex in the ArrayList of cells
     */
    private void removeWriteIn(){
        Cell curCell = cells.get(accessIndex);
        curCell.writeOutValue();
    }

    /**
     * Removes the last penciled-in value for the cell currently at accessIndex
     */
    private void removePencilMark(){
        Cell curCell = cells.get(accessIndex);
        curCell.pencilOutValue();
    }

    /**
     * Called by the main method to run the program
     */
    private void run() {
        setUpAll();
    }

    /**
     * Sets up the puzzle, the instructions, the cells, the grid, and the timer. Note the number of digits passed to the puzzle generator must be manually edited
     */
    private void setUpAll() {
        initNumDigits = 30;
        squaresRemaining = 81;
        canvas.removeAll();
        //puzzle = getPuzzleFromFile(new File("src/HardPuzzles.txt"));
        setUpPuzzle(initNumDigits);
        cells = new ArrayList<>();
        scan = new Scanner(System.in);
        gameState = GameState.WRITE_IN;
        setUpInstructions();
        setUpCells();
        setUpGrid();
        setUpBoldLines();
        setUpTimer();
        accessIndex = -1;
        Button restartButton = new Button("New Puzzle");
        restartButton.setPosition(361, 820);
        canvas.add(restartButton);
        restartButton.onClick(() -> setUpAll());
    }

    /**
     * Invokes the puzzle generator algorithm using the given number of digits
     * @param initNumDigits The number of digits used to generate the new puzzle. Note the actual amount may be greater due to the algorithm's constraints
     */
    private void setUpPuzzle(int initNumDigits){
        String puzzleTemp = su.createRandomPuzzle(initNumDigits);
        HashMap<String, HashSet<String>> squareValuesTemp = su.parseGrid(puzzleTemp);
        solution = su.search(squareValuesTemp);
        puzzle = su.addGivenDigits(puzzleTemp, su.displayPuzzleString(solution), 15, squareValuesTemp);
        squareValues = squareValuesTemp;
    }

    /**
     * Sets up the graphics for the instructions as well as retrives the current top time from the times.txt file
     */
    private void setUpInstructions() {
        // Learned how to read from a file here https://www.digitalocean.com/community/tutorials/java-read-file-line-by-line 
        GraphicsText instructions = new GraphicsText("________How to Play________ \n \n - Cycle through cells with the arrow keys \n \n - To enter a number, hit that number key. A red border indicates a wrong selection, while a blue border indicates a correct selection. Once a value is correctly selected, that cell may no longer accept pencil-in values, nor may it be changed \n \n - To pencil in values, toggle the PENCIL_IN gamemode by pressing <SPACE>. To return to the WRITE_IN gamemode, press <SPACE>. \n \n ________Best Time________", 855, 65);
        instructions.setFont(FontStyle.BOLD, 20);
        instructions.setWrappingWidth(290);
        Rectangle bg = new Rectangle(850, 45, 300, 729);
        bg.setFillColor(Color.LIGHT_GRAY);
        bg.setStrokeWidth(5);
        GraphicsText topScore = new GraphicsText("", 980, 640);
        topScore.setFont(FontStyle.BOLD, 20);
        BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("src/sudokuProject/times.txt"));
			String line = reader.readLine();
            String maxScore = line;
			while (line != null) {
				if (maxScore.compareTo(line) < 0) {
                    maxScore = line;
                }
				line = reader.readLine();
			}
            topScore.setText(maxScore);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        canvas.add(bg);
        canvas.add(instructions);
        canvas.add(topScore);
    }

    /**
     * Sets up the timer using the System's time in milliseconds, converted to seconds by TimeUnit
     */
    private void setUpTimer() {
        time = 0;
        timer.setText(Long.toString(time));
        timer.setScale(5.0);
        canvas.add(timer);
        startTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        time = startTime;
    }

    /**
     * This method is called continuously by the canvas's animate(). Updates the timer incrementally by one second
     */
    private void updateTimer() {
        time = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        timer.setText(Long.toString(time - startTime));
    }

    /**
     * Retrieves a puzzle from the given file. By default not invoked, but can be invoked by setUpAll()
     * @param fname
     * @return
     */
    private String getPuzzleFromFile(File fname){
        HashMap<Integer, String> puzzles = su.parseFile(fname);
        Random rand = new Random();
        int ind = rand.nextInt(1, puzzles.size());
        return puzzles.get(ind);
    }

    /**
     * Checks whether or not an input value for a cell is correct. If not, the border is set to Red; otherwise the value becomes fixed
     * @param value The String value to be checked against the actual answer
     * @return true if correct, false otherwise
     */
    private boolean checkSolution(String value) {
        if (accessIndex != -1 && gameState == GameState.WRITE_IN) {
            Cell currentCell = cells.get(accessIndex);
            HashSet<String> correctDigit = solution.get(currentCell.getCellTag());
            Iterator<String> iter = correctDigit.iterator();
            String d = iter.next();
            System.out.println("d: "+d);
            System.out.println("value: "+value);
            if(d.equals(value) && !currentCell.isFixed()){
                currentCell.setFixed(true);
                System.out.println("yay");
                currentCell.getCellShape().setStrokeColor(Color.BLUE);
                // currentCell.getCellShape().setStrokeWidth(1);
                squaresRemaining -= 1;
                if (squaresRemaining == 0) {
                    winScreen();
                }
                return true;
            }
            else if (currentCell.isFixed()) {
                System.out.println("value already set");
            }
            else{
                System.out.println("nope");
                return false;
            }
        }
        return true;
    }

    /**
     * Creates the sudoku board as a grid of empty cell objects
     */
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
                    currentCell.setFixed(true);
                    squaresRemaining -= 1;
                }
                index++;
            }
        }
    }

    /**
     * Assigns to each cell its correpsonding square from the sudoku algorithm and adds each cell in alphanumeric order to the ArrayList of cells
     */
    private void setUpCells() {
        double columnCount = 45;
        double rowCount = 45;
        for (int i=0; i < 81; i++) {
            if(columnCount > 729) {
                rowCount += 81;
                columnCount = 45;
            }
            Rectangle cellShape = new Rectangle(columnCount, rowCount, 81, 81);
            Cell cell = new Cell("", cellShape, canvas);
            canvas.add(cell.getCellShape());
            cells.add(cell);
            columnCount += 81;
        }
    }

    /**
     * If the gamemode is WRITE_IN: Sets the displayed value of the cell at accessIndex to the given String value provided the cell is not already fixed
     * If the gamemode is PENCIL_IN: Adds the given String value to the cell's penciled-in values if not already present, provided the cell is not already fixed
     */
    private void setCellValue(String value) {
        if (gameState == GameState.WRITE_IN) {
            if (accessIndex != -1) {
                Cell currentCell = cells.get(accessIndex);
                if (squareValues.get(currentCell.getCellTag()).size() > 1) {
                    currentCell.setDisplay(value);
                }
            }
        }
        else if (gameState == GameState.PENCIL_IN) {
            if (accessIndex != 1) {
                Cell currentCell = cells.get(accessIndex);
                if (squareValues.get(currentCell.getCellTag()).size() > 1) {
                    currentCell.pencilInValue(value);
                }
            }
        }
    }

    /**
     * If the current cell at accessIndex is not fixed, this method sets the border to Red
     */
    private void displayIncorrect(){
        if(accessIndex != -1){
            Cell currentCell = cells.get(accessIndex);
            if (!currentCell.isFixed()) {
                currentCell.getCellShape().setStrokeColor(Color.RED);
                time += 5;
            }
            // currentCell.setDisplay("");
        }
    }

    // private void resetFill(){
    //     Cell currentCell = cells.get(accessIndex);
    //     currentCell.getCellShape().setFilled(false);
    // }
    
    // private void pause(){
    //     try{
    //         Thread.sleep(2000);
    //     } catch(Exception e){
    //         System.out.println("shit");
    //     }
    // }

    /**
     * Moves the current cell selection by appropriately incrementing accessIndex. Bolds the cell at the resulting accessIndex.
     * Note: accessIndex begins at -1, so the first cell to be highlighted will be A1
     */
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

    /**
     * Sets up the overlying 3x3 matrix of submatrices on the sudoku board. This overlying matrix is denoted by bold blue lines
     */
    private void setUpBoldLines(){
        double x1 = 45;
        double y1 = 45;
        double x2 = 774;
        double y2 = 45;
        for(int i = 1; i < 5; i ++){
            Line l = new Line(x1, y1, x2, y2);
            l.setStrokeWidth(5);
            l.setStrokeColor(Color.BLUE);
            canvas.add(l);
            y1 += 243;
            y2 += 243;
        }
        x1 = 45;
        y1 = 45;
        y2 = 774;
        x2 = 45;
        for(int i = 1; i < 5; i ++){
            Line l = new Line(x1, y1, x2, y2);
            l.setStrokeWidth(5);
            l.setStrokeColor(Color.BLUE);
            canvas.add(l);
            x1 += 243;
            x2 += 243;
        }
    }

    /**
     * If the current solution matches the generated solution, the win screen along with the elapsed time is displayed
     */
    private void winScreen() {
        // Learned how to write to a file here: https://www.geeksforgeeks.org/java-program-to-write-into-a-file/
        Long winTime = time - startTime;
        try {
            FileWriter writer = new FileWriter("src/sudokuProject/times.txt",true);
            writer.write(Long.toString(winTime).concat(" \r\n"));
            writer.close();
        }
        catch(IOException e) {
            System.out.println(e.getMessage());
        }
        Rectangle winbg = new Rectangle(230, 230, 363, 200);
        winbg.setFillColor(Color.WHITE);
        GraphicsText winGraphics = new GraphicsText("You Win", winbg.getX() + winbg.getWidth() / 2.9, winbg.getY() + 80);
        GraphicsText winGraphics2 = new GraphicsText("Your Time (in seconds):", winbg.getX() + winbg.getWidth() / 5, winbg.getY() + 120);
        GraphicsText winTimeText = new GraphicsText(Long.toString(winTime).concat("\n"), winbg.getX() + winbg.getWidth() / 2.1, winbg.getY() + 160);
        winGraphics.setFont(FontStyle.BOLD,25);
        winGraphics2.setFont(FontStyle.BOLD,20);
        winTimeText.setFont(FontStyle.BOLD,20);
        canvas.add(winbg);
        canvas.add(winGraphics);
        canvas.add(winGraphics2);
        canvas.add(winTimeText);
    }
}
