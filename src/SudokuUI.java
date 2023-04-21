import edu.macalester.graphics.*;

public class SudokuUI {
    private Sudoku su;
    private CanvasWindow canvas;

    public SudokuUI() {
        su = new Sudoku();
        canvas = new CanvasWindow("Sudoku", 900, 900);
        String puzzle = su.createRandomPuzzle(40);
    }
}
