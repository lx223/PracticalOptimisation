package CW2;

import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimulatedAnnealing {

    //Experiment parameters
    @Setter
    int trialTimes = 5;
    @Setter
    int errorRange = 0;

    //Simulated annealing parameters
    @Setter
    double startTemp = 1000000;
    @Setter
    double coolingRate = 0.000001;
    @Setter
    double endTemp = 1;
    @Setter
    int tempLength = 1;

    private Route route;
    private List<Double> bestRouteDistances;
    private List<Double> worstRouteDistances;

    /**
     * Class Constructor
     *
     * @param route the target destination
     */
    public SimulatedAnnealing(Route route) {
        this.route = route;
    }

    /**
     * This method calculates the acceptance probability of the neighbour route
     *
     * @param route     the current route
     * @param neighbour the random neighbour route
     * @param temp      the current temperature
     * @return the acceptance probability
     */
    private static double acceptanceProb(Route route, Route neighbour, double temp) {
        return Math.min(1, neighbour.stateProb(temp) / route.stateProb(temp));
    }

    /**
     * This method uses simulated annealing technique to compute the best route
     */
    public void solve() {
        bestRouteDistances = new ArrayList<>();
        worstRouteDistances = new ArrayList<>();

        for (int selectType = 0; selectType < 2; selectType++) {
            double bestRouteDistance = Double.MAX_VALUE;
            double worstRouteDistance = Double.MIN_VALUE;

            for (int trial = 0; trial < trialTimes; trial++) {
                System.out.println("Trial: " + trial + " with selectType " + selectType);
                route.initialiseRoute();
                route.addError(errorRange);

                // It terminates when temperature drops below a given value
                for (double temp = startTemp; temp > endTemp; temp *= (1 - coolingRate)) {
                    for (int tL = 0; tL < tempLength; tL++) {
                        Route neighbour = route.getRandomNeighbour(selectType);
                        if (acceptanceProb(route, neighbour, temp) >= new Random().nextDouble()) {
                            route = neighbour;
                        }
                    }
                }
                route.printRoute();
                if (route.getTotalDistance() < bestRouteDistance) {
                    bestRouteDistance = route.getTotalDistance();
                }

                if (route.getTotalDistance() > worstRouteDistance) {
                    worstRouteDistance = route.getTotalDistance();
                }
            }
            worstRouteDistances.add(worstRouteDistance);
            bestRouteDistances.add(bestRouteDistance);
        }
    }

    /**
     * This method prints the best and worst solutions obtained during the last solve method call
     */
    public void printAnswers() {
        for (int i = 0; i < bestRouteDistances.size(); i++) {
            System.out.println("****************** ");
            System.out.println("For selectType " + i);
            System.out.println("Best route distance: " + bestRouteDistances.get(i));
            System.out.println("Worst route distance: " + worstRouteDistances.get(i));
            System.out.println("****************** ");
        }
    }
}
