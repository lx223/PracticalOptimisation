package CW3;

import lombok.Setter;

import java.util.Random;

public class HookeSearch {

    //Hooke search parameters
    @Setter
    private double tol = Math.pow(10, -10);
    @Setter
    private int sampleSize = 20;
    @Setter
    private double scalingRate = 0.9;
    @Setter
    private Vector stepSize = new Vector(new double[]{6, 6});

    private Vector base;
    private double baseValue;

    private BirdFunction birdFunction;
    private Random rand;

    /**
     * Class constructor
     *
     * @param randomSeed   the seed for the random generator
     * @param birdFunction the function to analyse
     */
    public HookeSearch(long randomSeed, BirdFunction birdFunction) {
        rand = new Random(randomSeed);
        this.birdFunction = birdFunction;
        this.base = shotgun();
        this.baseValue = birdFunction.evaluate(this.base);
    }

    /**
     * The method where Hooke search does its work
     *
     * @return the obtained minimum value
     */
    public double run() {
        while (true) {
            Vector move = incVars();
            if (move.isZero()) {
                stepSize.times(scalingRate);
                if (stepSize.data[0] < tol && stepSize.data[1] < tol) {
                    break;
                } else {
                    this.base.add(move);
                }
            } else {
                this.base.add(move);
                patternMove(move);
            }
        }
        System.out.println("***************");
        System.out.println("Hooke search: ");
        System.out.println("Used allowance: " + birdFunction.getUsedEvaluation());
        baseValue = birdFunction.evaluate(this.base);
        System.out.println(baseValue);
        this.base.print();
        System.out.println("***************");
        return baseValue;
    }

    /**
     * This method tries to find a move that leads to a better solution by incrementing each variable by a step size
     *
     * @return the move vector
     */
    private Vector incVars() {
        Vector sol = this.base.makeCopy();
        //Increment every variable by its step size
        for (int i = 0; i < sol.data.length; i++) {
            sol.data[i] += stepSize.data[i];
            if (sol.data[i] > 6) sol.data[i] = 6;
            //Constraint check
            double solValue = birdFunction.evaluate(sol);
            if (solValue < baseValue) {
                baseValue = solValue;
            } else {
                sol.data[i] -= stepSize.data[i] * 2;
                if (sol.data[i] < -6) sol.data[i] = -6;
                //Constraint check
                solValue = birdFunction.evaluate(sol);
                if (solValue < baseValue) {
                    baseValue = solValue;
                } else {
                    sol.data[i] = this.base.data[i];    //Restore to the original value
                }
            }
        }
        return sol.subtract(this.base).makeCopy();
    }

    /**
     * This method tries to imitate the previous successful move
     *
     * @param move previous successful move vector
     */
    private void patternMove(Vector move) {
        Vector sol = this.base.makeCopy();
        sol.add(move);
        validateInput(sol);
        // If the function value at the new pos is smaller, set it to base
        if (baseValue > birdFunction.evaluate(sol)) {
            this.base = sol;
        }
    }

    /**
     * This method validates the control variable vector
     *
     * @param x variable vector
     */
    private void validateInput(Vector x) {
        if (x.data[0] > 6) x.data[0] = 6;
        if (x.data[0] < -6) x.data[0] = -6;
        if (x.data[1] > 6) x.data[0] = 6;
        if (x.data[1] < -6) x.data[0] = -6;
    }

    /**
     * This method returns an initial solution
     *
     * @return a seeding solution
     */
    private Vector shotgun() {
        Vector bestSol = birdFunction.getRandomStart(rand);
        double bestFunVal = Double.MAX_VALUE;
        for (int i = 0; i < sampleSize; i++) {
            Vector sol = birdFunction.getRandomStart(rand);
            double funcValue = birdFunction.evaluate(sol);
            if (funcValue < bestFunVal) {
                bestFunVal = funcValue;
                bestSol = sol;
            }
        }
        return bestSol;
    }
}
