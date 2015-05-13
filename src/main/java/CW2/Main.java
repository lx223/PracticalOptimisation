package CW2;

public class Main {

    public static void main(String[] args) {
        String file = "src/main/resources/CW2/targets";
        Route route = new Route(file);
        SimulatedAnnealing solver = new SimulatedAnnealing(route);
        solver.solve();
        solver.printAnswers();
    }
}
 