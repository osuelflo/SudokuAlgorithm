import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;
import java.util.List;

public class SudokuAlgorithm{
    // private ArrayList<String> squares;
    // private HashMap<String,HashSet<Integer>> candidates;
    // private HashSet<HashSet<String>> allUnitSet;
    // private HashMap<String,HashSet<String>> peers;
    private int count;
    private ArrayList<Square> squares;
    private ArrayList<ArrayList<Square>> rows;
    private ArrayList<ArrayList<Square>> cols;
    private ArrayList<ArrayList<Square>> boxes;
    private static final List<Integer> DIGITS = List.of(1,2,3,4,5,6,7,8,9);


    public SudokuAlgorithm(){
        squares = new ArrayList<>();
        rows = new ArrayList<>();
        cols = new ArrayList<>();
        boxes = new ArrayList<>();
        initializeSquares();
        initializePeers();
        count = 0;
    }

    // private boolean solve() {
    //     Collections.sort(squares);
    //     Iterator<Square> iter = squares.iterator();
    //     Square startSq = null;
    //     int size = 1;
    //     while (iter.hasNext() && size == 1) {
    //         startSq = iter.next();
    //         size = startSq.getCandidates().size();
    //     }
    //     return search();
    // }

    private void parseGrid(String grid){
        char[] gridChar = grid.toCharArray();
        for(int i = 0; i < squares.size(); i ++){
            Square s = squares.get(i);
            Integer d = Character.getNumericValue(gridChar[i]);
            if(DIGITS.contains(d)){
                assignValue(s, d);
            }
            // System.out.println(s.getCandidates().toString());
        }
    }

    private String display(){
        StringBuilder sb = new StringBuilder();
        int counter = 1;
        for(Square s : squares){
            Iterator<Integer> iter = s.getCandidates().iterator();
            sb.append('(');
            while(iter.hasNext()){
                int digit = iter.next();
                String d = Integer.toString(digit);
                sb.append(d);
            }
            sb.append(") ");
            if(counter % 9 == 0){
                sb.append('\n');
            }
            counter ++;
        }
        return sb.toString();
    }

    private boolean assignValue(Square s, int d){
        HashSet<Integer> otherCand = s.copyCandidates();
        otherCand.remove(d);
        for(int d2 : otherCand){
            if(!eliminateValue(s, d2)){
                return false;
            }
        }
        return true;
    }

    private boolean search() {
        //Collections.sort(squares);
        // if (cands.size() == 0) {
        //     return false;
        // }
        count ++;
        boolean solved = true;
        Square sqr = new Square(1,1,1);
        sqr.setCandidates(new HashSet<>(DIGITS));
        System.out.println(sqr.getCandidates().toString());
        for (Square sq : squares) {
            if(sq.getCandidates().size() != 1) {
                solved = false;
            }
            if(sq.getCandidates().size() == 0){
                return false;
            }
            if(sq.getCandidates().size() <= sqr.getCandidates().size() && sq.getCandidates().size() != 1){
                sqr = sq;
            }
        }
        System.out.println(sqr.getCandidates().toString());
        if (solved) {
            return solved;
        }
        HashSet<Integer> sqCands = sqr.copyCandidates();
        //Iterator<Integer> setIter = sqCands.iterator();
        boolean temp = false;
        int c = 0;
        for(int d : sqCands) {
            c ++;
            for(Square sq : squares){
                sq.setCandidates(sq.copyCandidates());
                if(c == 1){
                    sq.setOldCandidates(sq.copyCandidates());
                }
            }
            temp = assignValue(sqr, d);
            
            System.out.println(temp);
            System.out.println(d);
            if(temp){
                if(search()){
                    return true;
                }
                for(Square sq : squares){
                    sq.setCandidates(sq.getOldCandidates());
                }
            }
            //sq.setCandidates(sqCands);
        }
        return false;
    }

    private boolean eliminateValue(Square s, int d){
        if(!s.getCandidates().contains(d)){
            return true;
        }
        s.updateCandidates(d);

        // Constraint prop 1
        if(s.getCandidates().size()==0){
            return false;
        }
        else if(s.getCandidates().size()==1){
            Iterator<Integer> iter = s.getCandidates().iterator();
            int d2= iter.next();
            for(Square sq : s.getPeers()){
                if(!eliminateValue(sq, d2)){
                    return false;
                }
            }
            return true;
        }
        // Constraint prop 2
        if(!constraintTwo(rows, s.getRow(), s, d)){
            return false;
        }
        else if(!constraintTwo(cols, s.getCol(), s, d)){
            return false;
        }
        else if(!constraintTwo(boxes, s.getBox(), s, d)){
            return false;
        }
        else{
            return true;
        }
    }

    private boolean constraintTwo(ArrayList<ArrayList<Square>> unit, int spot, Square s, int d){
        ArrayList<Square> possibleSquares = new ArrayList<>();
        for(Square sr : unit.get(spot-1)){
            if(sr.getCandidates().contains(d)){
                possibleSquares.add(sr);
            }
        }
        if(possibleSquares.size() == 0){
            return false;
        }
        else if(possibleSquares.size() == 1){
            if(!assignValue(possibleSquares.get(0),d)){
                return false;
            }
        }
        return true;
    }

    private void initializePeers(){
        for(Square s : squares){
            int r = s.getRow();
            int c = s.getCol();
            int b = s.getBox();
            for(Square sr : rows.get(r-1)){
                if(!sr.equals(s)){
                    s.addPeer(sr);
                }
            }
            for(Square sc : cols.get(c-1)){
                if(!sc.equals(s)){
                    s.addPeer(sc);
                }
            }
            for(Square sb : boxes.get(b-1)){
                if(!sb.equals(s)){
                    s.addPeer(sb);
                }
            }
        }
    }

    private void initializeSquares(){
        for(int r = 1; r < 10; r ++){
            rows.add(new ArrayList<Square>());
            for(int c = 1; c <10; c++){
                if(r == 1){
                    cols.add(new ArrayList<Square>());
                    boxes.add(new ArrayList<Square>());
                }
                int box = calcBox(r, c);
                Square sqr = new Square(r, c, box);
                rows.get(r-1).add(sqr);
                cols.get(c-1).add(sqr);
                boxes.get(box-1).add(sqr);
                squares.add(sqr);
            }
        }
    }

    private Integer calcBox(int row, int col){
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
    public static void main(String[] args) {
        SudokuAlgorithm su = new SudokuAlgorithm();
        su.parseGrid("003020600900305001001806400008102900700000008006708200002609500800203009005010300");
        System.out.println(su.display());
        SudokuAlgorithm su2 = new SudokuAlgorithm();
        su2.parseGrid("400000805030000000000700000020000060000080400000010000000603070500200000104000000");
        su2.search();
        System.out.println(su2.display());
        System.out.println(su2.count);
    }

}