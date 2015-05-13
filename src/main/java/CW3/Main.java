package CW3;

public class Main {

    public static void main(String[] args) {
        final double tol = 0.001;
        final int seedNo = 10000;
        final int functionAllowance = 1000;

        int evoCounter = 0;
        int hookeCounter = 0;
        for (long seed = 0; seed < seedNo; seed++) {
            EvoStrategy evoStrategy = new EvoStrategy(seed, new BirdFunction(functionAllowance));
            if (Math.abs((BirdFunction.MINIMUM - evoStrategy.run()) / BirdFunction.MINIMUM) < tol) evoCounter++;
            
            HookeSearch hooke = new HookeSearch(seed, new BirdFunction(functionAllowance));
            if (Math.abs((BirdFunction.MINIMUM - hooke.run()) / BirdFunction.MINIMUM) < tol) hookeCounter++;
        }
        System.out.println("success rate for evo: " + (double) evoCounter / seedNo);
        System.out.println("success rate for hooke: " + (double) hookeCounter / seedNo);
    }
}