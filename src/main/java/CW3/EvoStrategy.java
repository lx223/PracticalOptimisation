package CW3;

import lombok.Setter;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class EvoStrategy {

    //EvoStrategy parameters
    @Setter
    private double tol = 0.001;
    @Setter
    private int lambda = 5;
    @Setter
    private int mu = 70;
    @Setter
    private int maxGen = 10000;
    @Setter
    private double sigma = 3;

    private BirdFunction birdFunction;
    private Random rand;

    /**
     * Class constructor
     *
     * @param randomSeed   the seed for random generator
     * @param birdFunction the bird function to be analysed
     */
    public EvoStrategy(long randomSeed, BirdFunction birdFunction) {
        this.rand = new Random(randomSeed);
        this.birdFunction = birdFunction;
    }

    /**
     * The method where Evolutionary Strategy does its work
     *
     * @return the minimum value found
     */
    public double run() {
        Population population = new Population(lambda, mu, birdFunction);
        population.generateRandomPop();
        int curGen = 0;
        // Stopping conditions
        // Either the solution is within 0.1% of the minimum or
        // curGen >= maxGen or
        // function evaluation limit reached
        while (birdFunction.hasCredit()) {
            population.reproduce();
            population.mutateOffspring((BirdFunction.MINIMUM - population.getBestIndividualFitness())
                    / BirdFunction.MINIMUM * sigma);
            population.select();
            if ((BirdFunction.MINIMUM - population.getWorstIndividualFitness()) / BirdFunction.MINIMUM < tol) break;
            curGen++;
        }
        System.out.println("***************");
        System.out.println("Evo strategy: ");
        System.out.println("Current generation: " + curGen);
        System.out.println("Used allowance: " + birdFunction.getUsedEvaluation());
        population.printAll();
        population.printBest();
        System.out.println("***************");
        return population.getBestIndividualFitness();
    }


    class Population {

        private Individual[] parents;
        private Individual[] offspring;
        private BirdFunction nature;

        /**
         * * Class constructor
         *
         * @param popSize  population size
         * @param offSize  offspring size
         * @param function the nature function
         */
        public Population(int popSize, int offSize, BirdFunction function) {
            this.parents = new Individual[popSize];
            this.offspring = new Individual[offSize];
            this.nature = function;
        }

        /**
         * This method initialises the parents array to a random population
         */
        public void generateRandomPop() {
            for (int i = 0; i < parents.length; i++) {
                parents[i] = new Individual(birdFunction.getRandomStart(rand), nature);
            }
        }

        /**
         * This method produces offspring array
         */
        public void reproduce() {
            //Intermediate recombine
            for (int i = 0; i < offspring.length; i++) {
                offspring[i] = recombine(parents[rand.nextInt(parents.length)], parents[rand.nextInt(parents.length)]);
            }
        }

        /**
         * This method mutates offspring
         *
         * @param mutatingFactor the factor as to how much it mutates
         */
        public void mutateOffspring(final double mutatingFactor) {
            for (Individual individual : offspring) {
                individual.mutate(mutatingFactor);
            }
        }

        /**
         * This method selects the next generation
         */
        public void select() {
            Individual[] population = ArrayUtils.addAll(parents, offspring);
            Arrays.sort(population, new FitnessComparator());
            System.arraycopy(population, 0, parents, 0, parents.length);
        }

        /**
         * This method returns the best parent's fitness
         *
         * @return the best parent's fitness
         */
        public double getBestIndividualFitness() {
            return parents[0].fitness;
        }

        /**
         * This method returns the worst parent's fitness
         *
         * @return the worst parent's fitness
         */
        public double getWorstIndividualFitness() {
            return parents[parents.length - 1].fitness;
        }


        /**
         * This method prints the fitness of all parents
         */
        public void printAll() {
            for (Individual parent : parents) System.out.print(parent.fitness + " ");
            System.out.println();
        }

        /**
         * This method prints the fitness of the best parent
         */
        public void printBest() {
            System.out.println("The best solution is: " + parents[0].fitness);
            parents[0].chromosome.print();
        }

        /**
         * To form an offspring
         *
         * @param a a one parent
         * @param b another parent
         * @return the offspring generated
         */
        private Individual recombine(Individual a, Individual b) {
            //discreet recombination
            //if (rand.nextInt(2) == 1) return new Individual(a.chromosome);
            //else return b;
            Vector newChromosome = a.chromosome.makeCopy();
            newChromosome.add(b.chromosome).divideBy(2);
            return new Individual(newChromosome, nature);
        }

    }

    private class Individual {

        private Vector chromosome;
        private double fitness;
        private BirdFunction nature;

        /**
         * Class constructor
         *
         * @param chromosome the gene of an individual
         * @param function   the function to map how good a gene is to a number
         */
        public Individual(Vector chromosome, BirdFunction function) {
            this.chromosome = chromosome;
            this.nature = function;
            this.fitness = nature.evaluate(chromosome);
        }

        /**
         * This method mutates the gene of an individual
         *
         * @param mutatingFactor the factor as to how big it mutates
         */
        public void mutate(final double mutatingFactor) {
            for (int i = 0; i < chromosome.data.length; i++) {
                chromosome.data[i] += rand.nextGaussian() * mutatingFactor;
            }
            validateChromosome(chromosome);
            this.fitness = birdFunction.evaluate(chromosome);
        }

        /**
         * Constraint check
         *
         * @param x vector to be checked
         */
        private void validateChromosome(Vector x) {
            if (x.data[0] > 6) x.data[0] = 6;
            if (x.data[0] < -6) x.data[0] = -6;
            if (x.data[1] > 6) x.data[0] = 6;
            if (x.data[1] < -6) x.data[0] = -6;
        }
    }

    class FitnessComparator implements Comparator<Individual> {

        @Override
        public int compare(Individual a, Individual b) {
            return (int) Math.signum(a.fitness - b.fitness);
        }
    }
}