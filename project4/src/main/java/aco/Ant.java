package aco;

import representation.JSP;
import utility.Tools;

import java.util.ArrayList;


public class Ant {

    int position;
    ArrayList<Integer> path = new ArrayList<>(JSP.numOfOperations);

    public void moveTo(int index) {
        position = index;
        path.add(index);
    }

    public int chooseEdge(ArrayList<Edge> edges, double[] jobEndTime, double[] machineEndTime){
        double totalValue = 0;
        double[] edgeValue = new double[edges.size()];

        for (int i = 0; i < edges.size(); i++) {
            Edge edge = edges.get(i);
            double distance = JSP.getOperation(edge.to).duration; // TODO - Replace with operation start time
            double heuristic = (Settings.distanceValueConstant/distance);
            edgeValue[i] = Math.pow(edge.pheromone, Settings.pheromoneInfluence)
                    * Math.pow(heuristic, Settings.heuristicInfluence);
            totalValue += edgeValue[i];
        }

        if (Tools.random.nextDouble() <= Settings.exploitationProbability) {
            int bestEdge = -1;
            double bestValue = Double.NEGATIVE_INFINITY;
            for (int i = 0; i < edgeValue.length; i++) {
                if (edgeValue[i] > bestValue) {
                    bestEdge = i;
                    bestValue = edgeValue[i];
                }
            }
            return edges.get(bestEdge).to;
        }
        double randomValue = Tools.random.nextDouble() * totalValue;
        for (int i = 0; i < edgeValue.length; i++) {
            randomValue -= edgeValue[i];
            if (randomValue <= 0) {
                return edges.get(i).to;
            }
        }
        System.out.println("NÅKKA E GALT HER");
        return -1;
    }


}
