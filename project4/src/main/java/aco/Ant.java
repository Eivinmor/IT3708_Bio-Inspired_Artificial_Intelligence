package aco;

import representation.JSP;

import java.util.ArrayList;
import java.util.HashSet;


public class Ant {

    int position;
    ArrayList<Integer> path = new ArrayList<>(JSP.numOfOperations);
    HashSet<Integer> visited = new HashSet<>(JSP.numOfOperations);

    public void moveTo(int index) {
        visited.add(index);
        position = index;
        path.add(index);
    }

//    public int chooseEdge(ArrayList<Edge> edges){
//
//    }


}
