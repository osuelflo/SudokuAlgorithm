import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

public class Square {
    private HashSet<Integer> candidates;
    private Integer row;
    private Integer col;
    private Integer box;
    private HashSet<Square> peers;

    public Square(int r, int c, int b){
        row = r;
        col = c;
        box = b;
        candidates = new HashSet<>();
        for(int i = 1; i <10; i++){
            candidates.add(i);
        }
        peers = new HashSet<>();
    }

    public void addPeer(Square s){
        peers.add(s);
    }

    public HashSet<Integer> getCandidates(){
        return candidates;
    }

    public Integer getRow(){
        return row;
    }

    public Integer getCol(){
        return col;
    }

    public Integer getBox(){
        return box;
    }

    public HashSet<Square> getPeers(){
        return peers;
    }

    public void updateCandidates(Integer digit){
        //candidates = copyCandidates();
        if(candidates.contains(digit)){
            candidates.remove(digit);
        }
    }

    // public void assignValue(Integer digit){
    //    //candidates = copyCandidates();
    //     Iterator<Integer> iter = candidates.iterator();
    //     while(iter.hasNext()){
    //         Integer current = iter.next();
    //         if(current != digit){
    //             candidates.remove(current);
    //         }
    //     }
    // }

    public HashSet<Integer> copyCandidates(){
        HashSet<Integer> cands = new HashSet<>();
        for(Integer s : candidates){
            cands.add(s);
        }
        return cands;
    }
}
