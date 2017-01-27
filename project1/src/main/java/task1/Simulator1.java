package task1;


class Simulator1 {

    private int trials, steps;
    private common.Plotter plotter;

    private Simulator1(){
        trials = 1000;
        steps = 50;
        plotter = new common.Plotter("Task 1 – Baseline agent", "Trial", "Score", trials);
    }

    private void runSimulation(){
        BaselineAgent agent = new BaselineAgent();
        int totalScore = 0;
        for (int i = 1; i <= trials; i++) {
            int trialScore = runTrial(agent);
            System.out.println(String.format("%s%5d%s%4d", "Trial", i, "  score:", trialScore));
            totalScore += trialScore;
            plotter.addData(i, trialScore);
        }
        System.out.println(String.format("%s%.1f", "--------------------------\nTotal avg score: ", (double)totalScore/trials));
        System.out.println("\nTask 1 – Baseline agent");
        System.out.println("\nSETTINGS");
        System.out.println("Trials: " + trials);
        plotter.plot();
    }

    private int runTrial(BaselineAgent agent){
        World world = new World();
        agent.registerNewWorld(world);
        world.placeAgentRandom();

        int step = 1;
        while(!world.simulationEnd && step <= steps) {
            agent.step();
            step++;
        }
        return agent.getScore();
    }

    private void printGrid(char[][] grid){
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Simulator1 simulator1 = new Simulator1();
        simulator1.runSimulation();
    }
}
