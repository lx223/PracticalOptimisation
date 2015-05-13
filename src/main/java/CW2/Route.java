package CW2;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.*;

public class Route {
    @Getter
    final private List<Target> route;

    /**
     * Class constructor
     *
     * @param fileName the filename of the input file
     */
    public Route(String fileName) {
        this.route = this.loadFile(fileName);
    }

    /**
     * Class constructor
     *
     * @param route an already constructed route
     */
    public Route(List<Target> route) {
        this.route = route;
    }

    /**
     * This method loads the data stored in a file into the programme.
     *
     * @param file the name of the file having the data
     * @return a list of target objects
     */
    private List<Target> loadFile(String file) {
        List<Target> targets = new ArrayList<>();
        try {
            Scanner in = new Scanner(new File(file));
            int index = 0;
            while (in.hasNextLine()) {
                String line = in.nextLine();
                String[] parts = line.split("\\s+");
                targets.add(new Target(Integer.valueOf(parts[0]), Integer.valueOf(parts[1]), index));
                index++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return targets;
    }

    /**
     * This method randomly generates an initial route
     */
    public void initialiseRoute() {
        Target tmp = route.remove(0);
        Collections.shuffle(route);
        route.add(0, tmp);
    }

    /**
     * This method generates a new neighbour
     *
     * @param selectType 0: any two; 1: consecutive two
     * @return the neighbour route
     */
    public Route getRandomNeighbour(int selectType) {
        Random rand = new Random();

        switch (selectType) {
            //Swap any two
            case 0:
                int swapPos1 = rand.nextInt(route.size() - 1) + 1;
                int swapPos2 = rand.nextInt(route.size() - 1) + 1;

                while (swapPos1 == swapPos2) {
                    swapPos2 = rand.nextInt(route.size() - 1) + 1;
                }
                return swap(swapPos1, swapPos2);

            //Swap consecutive two
            default:
                int swapPos = rand.nextInt(route.size() - 2) + 1;
                return swap(swapPos, swapPos + 1);
        }
    }

    /**
     * This method swap two targets in the current route
     *
     * @param pos1 one of the targets
     * @param pos2 the other targets
     * @return the new route
     */
    public Route swap(final int pos1, final int pos2) {
        List<Target> neighbour = new ArrayList<>(route);
        Collections.swap(neighbour, pos1, pos2);
        return new Route(neighbour);
    }

    /**
     * This method returns the total distance of the current route
     *
     * @return the total distance of the current route
     */
    public double getTotalDistance() {
        double distance = 0.0;
        for (int i = 0; i < route.size() - 1; i++) {
            distance += route.get(i).distanceTo(route.get(i + 1));
        }

        final double scaling = 200.0 / 115;
        return distance * scaling;
    }

    /**
     * This method prints out the current route and its total distance
     */
    public void printRoute() {
        System.out.println();
        for (Target target : route) {
            System.out.print(target.getIndex() + " ");
        }
        System.out.println("The total distance of this route: " + this.getTotalDistance());
        System.out.println();
    }

    /**
     * This method returns the exponential state probability
     *
     * @param temp the current temperature
     * @return the state probability
     */
    public Double stateProb(double temp) {
        double totalDistance = this.getTotalDistance();
        return Math.exp(-totalDistance / temp);
    }

    /**
     * This method adds a random error in the range specified
     *
     * @param errorRange the absolute range of the error
     */
    public void addError(int errorRange) {
        for (Target target : this.route) {
            target.setX(target.getX() + new Random().nextInt(errorRange * 2 + 1) - errorRange);
            target.setY(target.getY() + new Random().nextInt(errorRange * 2 + 1) - errorRange);

        }
    }

    private class Target {
        @Getter
        @Setter
        private int x, y, index;

        public Target(final int x, final int y, final int index) {
            this.x = x;
            this.y = y;
            this.index = index;
        }

        /**
         * This method returns the distance between the current target to another target
         *
         * @param that the other target
         * @return the pixel distance
         */
        public double distanceTo(Target that) {
            return Math.sqrt(Math.pow(this.x - that.getX(), 2) + Math.pow(this.y - that.getY(), 2));
        }
    }
}