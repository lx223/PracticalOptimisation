package CW3;

import lombok.Getter;

import java.util.Random;


public class BirdFunction {

    static public final double MINIMUM = -106.76453674926472;
    private final int functionAllowance;
    @Getter
    private int usedEvaluation = 0;

    /**
     * Class constructor
     *
     * @param allowance the maximum allowed number of function evaluation
     */
    public BirdFunction(int allowance) {
        functionAllowance = allowance;
    }

    /**
     * This method returns the function value of the bird function
     *
     * @param x the input
     * @return the function value
     */
    public double evaluate(Vector x) {
        usedEvaluation++;
        final double x1 = x.data[0];
        final double x2 = x.data[1];
        return Math.sin(x1) * Math.exp(Math.pow(1 - Math.cos(x2), 2)) +
                Math.cos(x2) * Math.exp(Math.pow(1 - Math.sin(x1), 2)) + Math.pow(x1 - x2, 2);
    }

    /**
     * To generate a random valid vector
     *
     * @param rand random generator
     * @return a valid vector
     */
    public Vector getRandomStart(Random rand) {
        return new Vector(new double[]{rand.nextDouble() * 12 - 6, rand.nextDouble() * 12 - 6});
    }

    /**
     * To check whether there is still credit for function evaluation
     *
     * @return true / false
     */
    public boolean hasCredit() {
        return functionAllowance > usedEvaluation;
    }
}
