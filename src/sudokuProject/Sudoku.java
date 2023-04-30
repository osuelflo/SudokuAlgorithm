package sudokuProject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;
import java.util.List;
import java.io.File;
import java.lang.StringBuilder;
import java.util.Scanner;
import java.time.*;
import java.util.Random;

/**
 * Capable of creating and solving Sudoku puzzles using algorithm developed by Peter Norvig
 * https://norvig.com/sudoku.html
 */
public class Sudoku{
    private static final String ROWS_STRING = "123456789";
    private static final String COLS_STRING = "123456789";
    private static final HashSet<String> DIGITS = makeDigits();
    private static final String[][] SQUARES = createSquares();
    private static final HashMap<Integer, HashSet<String>> BOXES = createBoxes();
    private static final HashMap<Integer, HashSet<String>> ROWS = createRows();
    private static final HashMap<Integer, HashSet<String>> COLS = createCols();
    private static final HashMap<String, HashSet<String>> PEERS = createPeers();

    /**
     * ex. SQUARES[1][5] = "15"
     * @return Array of Strings
     */
    public String[][] getSquareTags() {
        return SQUARES;
    }

    /**
     * initialize set of possible digits
     * @return
    */
    private static HashSet<String> makeDigits(){
        HashSet<String> d = new HashSet<>();
        d.add("1");
        d.add("2");
        d.add("3");
        d.add("4");
        d.add("5");
        d.add("6");
        d.add("7");
        d.add("8");
        d.add("9");
        return d;
    }

    /**
     * initialize Squares array
     * @return
     */
    private static String[][] createSquares(){
        int R = -1;
        int C = -1;
        String[][] temp = new String[9][9];
        for(String r : ROWS_STRING.split("")){
            R ++;
            for(String c : COLS_STRING.split("")){
                C ++;
                temp[R][C] = r.concat(c);
            }
            C = -1;
        }
        return temp;
    }

    /**
     * initialize boxes map, which maps each box number to a set of cells belonging to it
     * 1st box has cells 11, 12, 13, 21, 22, 23, 31, 32, 33
     * @return
     */
    private static HashMap<Integer, HashSet<String>> createBoxes(){
        HashMap<Integer, HashSet<String>> map = new HashMap<>();
        for(int i = 1; i < 10; i++){
            map.put(i, new HashSet<>());
        }
        for(int r = 0; r < 9; r ++){
            for(int c = 0; c < 9; c ++){
                int box = calcBox(r+1,c+1);
                map.get(box).add(SQUARES[r][c]);
            }
        }
        return map;
    }

    /**
     * Calculates the box in which a cell with row # = row and col # = col belongs to
     * @param row
     * @param col
     * @return
     */
    private static Integer calcBox(int row, int col){
        if(row - 3 <= 0){
            if(col - 3 <= 0){
                return 1;
            }
            else if(col - 3 <= 3){
                return 2;
            }
            else{
                return 3;
            }
        }

        else if(row - 3 <= 3){
            if(col - 3 <= 0){
                return 4;
            }
            else if(col - 3 <= 3){
                return 5;
            }
            else{
                return 6;
            }
        }

        else{
            if(col - 3 <= 0){
                return 7;
            }
            else if(col - 3 <= 3){
                return 8;
            }
            else{
                return 9;
            }
        }
    }

    /**
     * Makes a map that maps each square to a set of all the squares in its row, column, and box
     * @return
     */
    private static HashMap<String, HashSet<String>> createPeers(){
        HashMap<String, HashSet<String>> map = new HashMap<>();
        for(int r = 0; r < 9; r ++){
            for(int c = 0; c < 9; c ++){
                String square = SQUARES[r][c];
                map.put(square, new HashSet<>());
                addBoxPeers(map, square, calcBox(r+1, c+1));
                addColPeers(map, square, c);
                addRowPeers(map, square, r);
            }
        }
        return map;
    }

    /**
     * Adds all squares in square's row
     * @param map
     * @param square
     * @param r
     */
    private static void addRowPeers(HashMap<String, HashSet<String>> map, String square, int r){
        for(String s : SQUARES[r]){
            if(!s.equalsIgnoreCase(square)){
                map.get(square).add(s);
            }
        }
    }

    /**
     * Adds all squares in square's column
     * @param map
     * @param square
     * @param c
     */
    private static void addColPeers(HashMap<String, HashSet<String>> map, String square, int c){
        for(int i = 0; i < 9; i ++){
            String s = SQUARES[i][c];
            if(!s.equalsIgnoreCase(square)){
                map.get(square).add(s);
            }
        }
    }

    /**
     * Adds all squares in square's box
     * @param map
     * @param square
     * @param b
     */
    private static void addBoxPeers(HashMap<String, HashSet<String>> map, String square, int b){
        for(String s : BOXES.get(b)){
            if(!s.equalsIgnoreCase(square)){
                map.get(square).add(s);
            }
        }
    }

    /**
     * Initializes a map of candidates, which maps each square to a set of the possible digits that it can take within the rules of Sudoku
     * @return
     */
    private HashMap<String, HashSet<String>> initializeCandidates(){
        HashMap<String, HashSet<String>> candidates = new HashMap<>();
        for(int r = 0; r < 9; r ++){
            for(int c = 0; c < 9; c ++){
                candidates.put(SQUARES[r][c], new HashSet<>());
                for(String s : ROWS_STRING.split("")){
                    candidates.get(SQUARES[r][c]).add(s);
                }
            }
        }
        return candidates;
    }

    /**
     * Maps each row to a set of the squares in that row
     * @return
     */
    private static HashMap<Integer, HashSet<String>> createRows(){
        HashMap<Integer, HashSet<String>> map = new HashMap<>();
        for(int i = 1; i < 10; i++){
            map.put(i, new HashSet<>());
        }
        for(int r = 0; r < 9; r ++){
            for(int c = 0; c < 9; c ++){
                map.get(r+1).add(SQUARES[r][c]);
            }
        }
        return map;
    }
    
    /**
     * Maps each row to a set of the squares in that column
     * @return
     */
    private static HashMap<Integer, HashSet<String>> createCols(){
        HashMap<Integer, HashSet<String>> map = new HashMap<>();
        for(int i = 1; i < 10; i++){
            map.put(i, new HashSet<>());
        }
        for(int r = 0; r < 9; r ++){
            for(int c = 0; c < 9; c ++){
                map.get(c+1).add(SQUARES[r][c]);
            }
        }
        return map;
    }

    /**
     * Returns a copy of square s candidates
     * @param candidates
     * @param s
     * @return
     */
    private HashSet<String> copyCandidates(HashMap<String, HashSet<String>> candidates, String s){
        HashSet<String> cands = new HashSet<>();
        for(String d : candidates.get(s)){
            cands.add(d);
        }
        return cands;
    }

    /**
     * Returns a copy of the candidates map
     * @param candidates
     * @return
     */
    private HashMap<String, HashSet<String>> copyCandMap(HashMap<String, HashSet<String>> candidates){
        HashMap<String, HashSet<String>> map = new HashMap<>();
        for(int r = 0; r < 9; r ++){
            for(int c = 0; c < 9; c ++){
                map.put(SQUARES[r][c], copyCandidates(candidates, SQUARES[r][c]));
            }
        }
        return map;
    }

    /**
     * Assigns a value d to square s by eliminating every other digit in square s candidates.
     * @param candidates
     * @param s
     * @param d
     * @return
     */
    private HashMap<String, HashSet<String>> assignValue(HashMap<String, HashSet<String>> candidates, String s, String d){
        HashSet<String> otherCands = copyCandidates(candidates, s);
        otherCands.remove(d);
        for(String digit : otherCands){
            if(eliminateValue(candidates, s, digit) == null){
                return null;
            }
        }
        return candidates;
    }

    /**
     * Eliminates digit d from square s candidates. Calls both constraint methods.
     * Returns null if a contradiction was found, otherwise returns candidates.
     * @param candidates
     * @param s
     * @param d
     * @return
     */
    private HashMap<String, HashSet<String>> eliminateValue(HashMap<String, HashSet<String>> candidates, String s, String d){
        HashSet<String> currentCands = candidates.get(s);
        if(!currentCands.contains(d)){
            return candidates;
        }
        currentCands.remove(d);
        if(currentCands.size() == 0){
            return null;
        }
        else if(currentCands.size() == 1){
            Iterator<String> iter = currentCands.iterator();
            String d2= iter.next();
            for(String sq : PEERS.get(s)){
                if(eliminateValue(candidates, sq, d2) == null){
                    return null;
                }
            }
            return candidates;
        }
        if(!constraintTwo(d, getRow(s), candidates, ROWS)){
            return null;
        }
        else if(!constraintTwo(d, getCol(s), candidates, COLS)){
            return null;
        }
        else if(!constraintTwo(d, calcBox(getRow(s), getCol(s)), candidates, BOXES)){
            return null;
        }
        else{
            return candidates;
        }
    }

    /**
     * Takes in a grid of digits and '.', and assigns the corresponding given digits to the appropriate square
     * @param grid
     * @return
     */
    public HashMap<String, HashSet<String>> parseGrid(String grid){
        HashMap<String, HashSet<String>> candidates = initializeCandidates();
        String[] gridString = grid.split("");
        String s = "";
        for(int r = 0; r < 9; r ++){
            for(int c = 0; c < 9; c ++){
                s = SQUARES[r][c];
                String d = gridString[c + 9*r];
                if(DIGITS.contains(d)){
                    assignValue(candidates, s, d);
                }
            }
        }
        return candidates;
    }

    /**
     * Returns a nicely formatted completed Sudoku puzzle
     * @param candidates
     * @return
     */
    private String display(HashMap<String, HashSet<String>> candidates){
        StringBuilder sb = new StringBuilder();
        for(int r = 0; r < 9; r ++){
            for(int c = 0; c < 9; c ++){
                sb.append('(');
                for(String d : candidates.get(SQUARES[r][c])){
                    sb.append(d);
                }
                sb.append(") ");
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    private int getRow(String s){
        return Character.getNumericValue(s.charAt(0));
    }

    private int getCol(String s){
        return Character.getNumericValue(s.charAt(1));
    }

    /**
     * Second constraint in the eliminate method:
     * If there is only one spot for a digit d to go in unit, then assign it there.
     * @param d
     * @param u
     * @param candidates
     * @param unit
     * @return
     */
    private boolean constraintTwo(String d, int u, HashMap<String, HashSet<String>> candidates, HashMap<Integer, HashSet<String>> unit){
        ArrayList<String> temp = new ArrayList<>();
        for(String sq : unit.get(u)){
            if(candidates.get(sq).contains(d)){
                temp.add(sq);
            }
        }
        if(temp.size() == 0){
            return false;
        }
        else if(temp.size() == 1){
            if(assignValue(candidates, temp.get(0), d) == null){
                return false;
            }
        }
        return true;
    }

    /**
     * Recursive searching method. Finds the square with the smallest number of possible digit, and then assigns it a random value, checking for contradictions along the way.
     * @param candidates
     * @return
     */
    public HashMap<String, HashSet<String>> search(HashMap<String, HashSet<String>> candidates){
        boolean solved = true;
        String s = "";
        int size = 100;
        int curSize = 0;
        for(int r = 0; r < 9; r ++){
            for(int c = 0; c < 9; c ++){
                curSize = candidates.get(SQUARES[r][c]).size();
                if(curSize != 1){
                    solved = false;
                }
                if(curSize == 0){
                    return null;
                }
                if(curSize != 1 && curSize < size){
                    s = SQUARES[r][c];
                    size = curSize;
                }
            }
        }
        if(solved){
            return candidates;
        }
        HashSet<String> currCands = candidates.get(s);
        for(String d : currCands){
            HashMap<String, HashSet<String>> nextCands = assignValue(copyCandMap(candidates), s, d);
            if(nextCands != null){
                HashMap<String, HashSet<String>> result = search(nextCands);
                if(result != null){
                    return result;
                }
            }
        }
        return null;
    }

    public String solve(String grid){
        return display(search(parseGrid(grid)));
    }

    /**
     * Rather than displaying as a neat finished puzzle, it returns a string of digits, i.e "123456789987654321... "
     * @param grid - initial puzzle
     * @return
     */
    public String solveAsLine(String grid){
        HashMap<String, HashSet<String>> cands = search(parseGrid(grid));
        StringBuilder sb = new StringBuilder();
        for(int r = 0; r < 9; r ++){
            for(int c = 0; c <9; c ++){
                HashSet<String> curCands = cands.get(SQUARES[r][c]);
                Iterator<String> iter = curCands.iterator();
                sb.append(iter.next());
            }
        }
        return sb.toString();
    }

    /**
     * Parses a file of puzzles into a map of puzzle line to string, i.e 1 to "1259..."
     */
    public HashMap<Integer, String> parseFile(File fname){
        HashMap<Integer, String> puzzles =  new HashMap<>();
        try{
            Scanner scan = new Scanner(fname);
            int count = 1;
            while(scan.hasNextLine()){
                String newLine = scan.nextLine();
                puzzles.put(count, newLine);
                // System.out.println(newLine.length());
                // System.out.println(newLine);
                count ++;
            }
        } catch(Exception e){
            System.out.println("file don't exist yo");
        }
        return puzzles;
    }

    /**
     * 
     * @param puzzles
     * @return
     */
    private String parsePuzzles(HashMap<Integer, String> puzzles){
        StringBuilder sb = new StringBuilder();
        for(int n = 1; n <= puzzles.size(); n ++){
            String grid = puzzles.get(n);
            Instant start = Instant.now();
            sb.append(display(search(parseGrid(grid))));
            Instant end = Instant.now();
            Duration timeElapsed = Duration.between(start, end);
            sb.append("Time taken: "+ timeElapsed.toMillis() +" milliseconds" + '\n');
        }
        return sb.toString();
    }

    /**
     * Similar to solveAsLine()
     * @param candidates
     * @return
     */
    public String displayPuzzleString(HashMap<String, HashSet<String>> candidates){
        StringBuilder sb = new StringBuilder();
        HashSet<String> curCands = new HashSet<>();
        for(int r = 0; r < 9; r ++){
            for(int c = 0; c < 9; c ++){
                curCands = candidates.get(SQUARES[r][c]);
                Iterator<String> iter = curCands.iterator();
                sb.append(iter.next());
            }
        }
        return sb.toString();
    }

    /**
     * Helper method for SudokuUI class. Adds numAdded digits to a randomly created puzzle so make it easier for humans to solve.
     * @param puzzle
     * @param solution
     * @param numAdded
     * @param candidates
     * @return
     */
    public String addGivenDigits(String puzzle, String solution, int numAdded, HashMap<String, HashSet<String>> candidates){
        int addedDigit = 0;
        StringBuilder sb = new StringBuilder();
        HashSet<Integer> indices = new HashSet<>();
        Random rand = new Random();
        System.out.println(puzzle);
        while(addedDigit < numAdded){
            int curInd = rand.nextInt(80);
            if(puzzle.charAt(curInd) == '.' && !indices.contains(curInd)){
                indices.add(curInd);
                addedDigit ++;
                int row = curInd / 9;
                int col = curInd % 9;
                HashSet<String> temp = new HashSet<>();
                StringBuilder s = new StringBuilder();
                s.append(solution.charAt(curInd));
                temp.add(s.toString());
                candidates.put(SQUARES[row][col], temp);
            }
        }
        for(int i = 0; i < 81; i ++){
            if(indices.contains(i)){
                sb.append(solution.charAt(i));
            }
            else{
                sb.append(puzzle.charAt(i));
            }
        }
        return sb.toString();
    }
    
    /**
     * Creates a random puzzle with the givenDigits specified
     * @param givenDigits
     * @return
     */
    public String createRandomPuzzle(int givenDigits){
        HashMap<String, HashSet<String>> candidates = initializeCandidates();
        ArrayList<String> shuffledSquares = shuffleSquares();
        for(String s : shuffledSquares){
            if(candidates.get(s).size() != 1){
                String d = getRandomDigit(candidates, s);
                if(assignValue(candidates, s, d) == null){
                    break;
                }
                ArrayList<String> goodSquares = new ArrayList<>();
                HashSet<String> uniqueDigits = new HashSet<>();
                for(int r = 0; r < 9; r ++){
                    for(int c = 0; c < 9; c ++){
                        HashSet<String> curCands = candidates.get(SQUARES[r][c]);
                        if(curCands.size() == 1){
                            Iterator<String> iter = curCands.iterator();
                            goodSquares.add(SQUARES[r][c]);
                            uniqueDigits.add(iter.next());
                        }
                    }
                }
                if(goodSquares.size() >= givenDigits && uniqueDigits.size() >= 8){
                    StringBuilder sb = new StringBuilder();
                    for(int r = 0; r < 9; r ++){
                        for(int c = 0; c < 9; c ++){
                            HashSet<String> curCands = candidates.get(SQUARES[r][c]);
                            if(curCands.size() == 1){
                                Iterator<String> iter = curCands.iterator();
                                sb.append(iter.next());
                            }
                            else{
                                sb.append(".");
                            }
                        }
                    }
                    return sb.toString();
                }
            }
        }
        return createRandomPuzzle(givenDigits);
    }   

    /**
     * Returns an ArrayList of the squares in SQUARES shuffled in a random order
     * @return
     */
    private ArrayList<String> shuffleSquares(){
        ArrayList<String> shuffledSquares = new ArrayList<>();
        for(int i = 0; i < 9; i ++){
            for(int j = 0; j < 9; j ++){
                shuffledSquares.add(SQUARES[i][j]);
            }
        }
        Collections.shuffle(shuffledSquares);
        return shuffledSquares;
    }

    /**
     * Returns a random digit from square s candidates
     * @param candidates
     * @param s
     * @return
     */
    private String getRandomDigit(HashMap<String, HashSet<String>> candidates, String s){
        HashSet<String> cands = candidates.get(s);
        String[] temp = cands.toArray(new String[cands.size()]);
        Random rand = new Random();
        return temp[rand.nextInt(cands.size()-1)];
    }
    
    public static void main(String[] args) {
        //Sudoku su = new Sudoku();

        // Code to create 100,000 random puzzles and determine the average solve time
        // long max = 0;
        // long avg = 0;
        // long min = Long.MAX_VALUE;
        // String maxPuzzle = "";
        // for(int i = 0; i < 100000; i ++){
        //     String puzzle = su.createRandomPuzzle(17);
        //     Instant start = Instant.now();
        //     su.search(su.parseGrid(puzzle));
        //     Instant end = Instant.now();
        //     Duration timeElapsed = Duration.between(start, end);
        //     if(max <= timeElapsed.toNanos()){
        //         max = timeElapsed.toNanos();
        //         maxPuzzle = puzzle;
        //     }
        //     if(min >= timeElapsed.toNanos()){
        //         min = timeElapsed.toNanos();
        //     }

        //     avg += timeElapsed.toNanos();
        // }
        // avg = avg / 100000;
        // System.out.println("Average solve time (nanoseconds): " + avg);
        // System.out.println("Max solve time (nanoseconds): " + max);
        // System.out.println("Min solve time (nanoseconds): "+min);
        // System.out.println("Hardest puzzle: " + maxPuzzle);
    }
}