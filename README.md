# SudokuAlgorithm
Owen Suelflow, Adam Schroeder COMP 128 Project: Sudoku Algorithm and UI

## Project Description ##
Sudoku is a 9 by 9 matrix where each column and each row must contain 1-9 with no duplicates; furthermore each of the 9 submatrices must contain 1-9 with no duplicates in order to be solved.

To run the program with the user interface run the main method in SudokuUI.java. To test just the elements of the algorithm, run the main method in Sudoku.java with the target methods. There is some commented out code in Sudoku.java that creates 100,000 random puzzles and solves them, reporting the avg, min, and max solve times. 

In the UI, if generating a puzzle using the puzzle generator, the number of starting digits has to be specified in the code under setUpAll(). Constraints may result in a greater number of starting digits on the screen.



## Accessebility and Societal Impact ##
We acknowledge that our program is not equally accessible for everyone; we tried to use colors that would be amenable to colorblindness but cannot account for each situation. Furthermore our program uses only English alphanumeric values. Use of the program implicitly assumes full ability (sight, touch, etc.). We do not foresee our program being misused, however, it contains UI elements which could be edited to display intentionally or unintentionally harmful phrases. Beyond this, our program does not implement learning algorithms, so we do not anticipate any learned bias to become prevalent.

## References ##
https://norvig.com/sudoku.html
