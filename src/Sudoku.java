import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;
import java.util.List;
import java.lang.StringBuilder;
import java.util.Scanner;
import java.time.*;

public class Sudoku{
    private static final String ROWS_STRING = "123456789";
    private static final String COLS_STRING = "123456789";
    private static final HashSet<String> DIGITS = makeDigits();
    private static final String[][] SQUARES = createSquares();
    private static final HashMap<Integer, HashSet<String>> BOXES = createBoxes();
    private static final HashMap<Integer, HashSet<String>> ROWS = createRows();
    private static final HashMap<Integer, HashSet<String>> COLS = createCols();
    private static final HashMap<String, HashSet<String>> PEERS = createPeers();

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

    private static void addRowPeers(HashMap<String, HashSet<String>> map, String square, int r){
        for(String s : SQUARES[r]){
            if(!s.equalsIgnoreCase(square)){
                map.get(square).add(s);
            }
        }
    }

    private static void addColPeers(HashMap<String, HashSet<String>> map, String square, int c){
        for(int i = 0; i < 9; i ++){
            String s = SQUARES[i][c];
            if(!s.equalsIgnoreCase(square)){
                map.get(square).add(s);
            }
        }
    }

    private static void addBoxPeers(HashMap<String, HashSet<String>> map, String square, int b){
        for(String s : BOXES.get(b)){
            if(!s.equalsIgnoreCase(square)){
                map.get(square).add(s);
            }
        }
    }

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

    private HashSet<String> copyCandidates(HashMap<String, HashSet<String>> candidates, String s){
        HashSet<String> cands = new HashSet<>();
        for(String d : candidates.get(s)){
            cands.add(d);
        }
        return cands;
    }

    private HashMap<String, HashSet<String>> copyCandMap(HashMap<String, HashSet<String>> candidates){
        HashMap<String, HashSet<String>> map = new HashMap<>();
        for(int r = 0; r < 9; r ++){
            for(int c = 0; c < 9; c ++){
                map.put(SQUARES[r][c], copyCandidates(candidates, SQUARES[r][c]));
            }
        }
        return map;
    }

    private HashMap<String, HashSet<String>> assignValue(HashMap<String, HashSet<String>> candidates, String s, String d){
        HashSet<String> otherCands = copyCandidates(candidates, s);
        otherCands.remove(d);
        // System.out.println("d: " + d);
        // System.out.println("o: " + otherCands);
        // System.out.println("c: " + candidates.get(s));
        for(String digit : otherCands){
            if(eliminateValue(candidates, s, digit) == null){
                return null;
            }
        }
        return candidates;
    }

    private HashMap<String, HashSet<String>> eliminateValue(HashMap<String, HashSet<String>> candidates, String s, String d){
        HashSet<String> currentCands = candidates.get(s);
        //System.out.println("cur: " + currentCands);
        if(!currentCands.contains(d)){
            return candidates;
        }
        currentCands.remove(d);
        //System.out.println("cand: " + candidates.get(s));
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
        if(!constraintTwo(s, d, getRow(s), candidates, ROWS)){
            return null;
        }
        else if(!constraintTwo(s, d, getCol(s), candidates, COLS)){
            return null;
        }
        else if(!constraintTwo(s, d, calcBox(getRow(s), getCol(s)), candidates, BOXES)){
            return null;
        }
        else{
            return candidates;
        }
    }

    private HashMap<String, HashSet<String>> parseGrid(String grid){
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

    private boolean constraintTwo(String s, String d, int u, HashMap<String, HashSet<String>> candidates, HashMap<Integer, HashSet<String>> unit){
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

    private HashMap<String, HashSet<String>> search(HashMap<String, HashSet<String>> candidates){
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

    private String solve(String grid){
        return display(search(parseGrid(grid)));
    }

    private HashMap<Integer, String> parseFile(String fname){
        Scanner scan = new Scanner(fname);
        HashMap<Integer, String> puzzles =  new HashMap<>();
        int count = 1;
        while(scan.hasNextLine()){
            puzzles.put(count, scan.nextLine());
            count ++;
        }
        return puzzles;
    }

    private String parsePuzzles(HashMap<Integer, String> puzzles){
        StringBuilder sb = new StringBuilder();
        for(int n = 1; n <= puzzles.size(); n ++){
            String grid = puzzles.get(n);
            Instant start = Instant.now();
            search(parseGrid(grid));
            Instant end = Instant.now();
            Duration timeElapsed = Duration.between(start, end);
            sb.append("Time taken: "+ timeElapsed.toSeconds() +" seconds" + '\n');
        }
        return sb.toString();
    }
    public static void main(String[] args) {
        Sudoku su = new Sudoku();
        // HashMap<String, HashSet<String>> c = su.initializeCandidates();
        // HashMap<String, HashSet<String>> cc = su.copyCandMap(c);
        // System.out.println(c.get("11") + " c");
        // System.out.println(cc.get("11") + " cc");
        // System.out.println(c.get("11").equals(cc.get("11")));
        //System.out.println(su.display(su.search(su.parseGrid("003020600900305001001806400008102900700000008006708200002609500800203009005010300"))));
        //System.out.println(su.display(su.search(su.parseGrid("400000805030000000000700000020000060000080400000010000000603070500200000104000000"))));
        System.out.println(su.solve("200507406000031000000000230000020000860310000045000000009000700006950002001006008"));
        System.out.println(su.parsePuzzles(su.parseFile(fname)));
    }
}