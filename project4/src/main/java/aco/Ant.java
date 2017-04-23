package aco;

import representation.JSP;
import representation.Operation;

import java.util.HashSet;


public class Ant {

    HashSet<Edge> availableEdges = new HashSet<>(JSP.numOfJobs);
    Operation[] path = new Operation[JSP.numOfJobs * JSP.numOfMachines];

}
