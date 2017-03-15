package maximizer;

import java.util.Arrays;
import java.util.Random;

/**
 * Maximizer_API
 * @author Reuben Sonnenberg, Tung Nguyen, Dong Nguyen
 * 
 * Author Name          Email Address
 * Reuben Sonnenberg    rjsonnenberg@alaska.edu
 * Tung Nguyen          ttnguyen4@alaska.edu
 * Done Nguyen          dlnguyen@alaska.edu
 * 
 * Programming Assignment #2 - Evolution Strategies
 * Goal: evolve a solution x = {x1, x2} that maximizes the following function:
 *      f(x1, x2) = 21.5 + x1 * sin(4*pi*x1) + x2 * sin(20*pi*x2)
 * subject to the following constraints:
 *      -3.0 <= x1 <= 12.0 and 4.0 <= x2 <= 6.0     >*
 * 
 * Control parameters:
 *      μ = 3                       (number of parents)
 *      λ = 21                      (number of offspring)
 *      (μ, λ) selection            (the best μ offspring replace the parents)
 *      σ0 = 1                      (initial value of the mutation step size in 
 *                                  each dimension)
 *      termination count = 10,000  (stop the run after 10,000 fitness 
 *                                  evaluations)
 *      τ’ = 1 / sqrt(2 * n)        (the overall learning rate)
 *      τ = 1 / sqrt(2 * sqrt(n))   (the coordinate-specific learning rate)
 * NOTE: may need to use larger values for μ and λ, or increase (or decrease) 
 * the termination count.
 * 
 * Directions:
 * Run the ES 10 times using a different random seed for each run. Record the x
 * values of the best-of-run individual for each run, as well as its fitness
 * value, f(x). As with any programming project, your program should exemplify
 * principles of high-quality computer software design. Program-level 
 * documentation should indicate the name and email address of each author.
 */
public class Maximizer_API {
    private final double[][] parents;
    private final double[][] children;
    private final double mutationInitialStepSize;
    private final double[] mutationStepSize;
    private int terminationCount;
    private final int nNumber; // number of parameters (basically)
    private final double overallLearningRate;
    private final double coordinateLearningRate;
    
    // Randomization generator
    private final Random generatorRandom = new Random();
    
    /**
     * getBestSolution
     * @return Returns a parameter set that gives the best fitness.
     */
    public double[] getBestSolution() {

        init();
        double curFitness;
        double bestFitness = Double.MIN_VALUE;
        double[] bestSolution = {-1.0, -1.0};
        
        int fitIndex;
        do {
            generateOffspring();
            selectParents();
            fitIndex = getFittestIndex(parents);
            
            //Print best-of-run individual and resulting fitness
            curFitness = getFitness(parents[fitIndex]);
            System.out.println(terminationCount + ": f(" + 
                    parents[fitIndex][0] + ", " + parents[fitIndex][1] + ") = " 
                    + curFitness);
            
            //Check to see if it beats the current best solution
            if (curFitness > bestFitness)
            {
                bestFitness = curFitness;
                bestSolution = parents[fitIndex];
            }
            
            terminationCount--;
        } while (terminationCount > 0);
        
        return bestSolution;
    }
    
    /** 
     * init
     * Description: Set parents to randomized values between the given bounds:
     * -3.0 <= x1 <= 12.0
     * 4.0 <= x2 <= 6.0
     */
    private void init() {
        // Initialize parent values:
        System.out.println("Initialization Parameter Pairs:");
        for (double[] parent : parents) {
            parent[0] = (generatorRandom.nextDouble() * 
                    (15.0 + Double.MIN_VALUE)) - 3.0;
            parent[1] = (generatorRandom.nextDouble() * 
                    (2.0 + Double.MIN_VALUE)) + 4.0;
            System.out.println("x1: " + parent[0] + "  x2: " + parent[1]);
        }
    }
    
    /**
     * generateOffsprint
     * Description: Apply global recombination to create all child parameter
     * sets. Mutate each of the new child sets.
     */
    private void generateOffspring()
    {
        // Apply global recobmination to create all child sets
        for (int i = 0; i < children.length; i++) {
            children[i] = recombine(
                    parents[generatorRandom.nextInt(parents.length)], 
                    parents[generatorRandom.nextInt(parents.length)]);
        }
        
        // Use uncorrelated mutations with n step sizes to modify each of the
        // offspring produced via recombination (above).
        for (int i = 0; i < children.length; i++) {
            children[i] = mutate(children[i]);
        }
    }
    
    /**
     * selectParents
     * Description: Gets the fitness of all the children, sorts the resulting
     * fitness value/child index pairs, and sets all the parent parameter sets
     * to the child sets that have the highest fitness.
     */
    private void selectParents()
    {
        // Get the fitness of all the children
        double [][]currentFitness = new double[children.length][2];
        for (int i = 0; i < children.length; i++) {
            currentFitness[i][0] = getFitness(children[i]);
            currentFitness[i][1] = i;
        }
        // Sort the children by fitness
        Arrays.sort(currentFitness, (double[] o1, double[] o2) -> 
                Double.compare(o1[0], o2[0]));
        
        // Select the children with the best fitness to succeed the parents
        if (parents.length <= children.length)
        {
            for (int i = 0; i < parents.length; i++) {
                parents[i] = children[
                        (int)(currentFitness[children.length-i-1][1])];
            }
        }
        else{
            int index = children.length - 1;
            int parentsLeft = parents.length;
            do
            {
                if (index == 0)
                    index = children.length - 1;
                
                parents[index] = children[
                        (int)(currentFitness[children.length-index][1])];
                parentsLeft--;
            }while (parentsLeft > 0);
        }
    }
    
    /**
     * recombine
     * @param x Double array representing a parameter set
     * @param y Double array representing a parameter set
     * @return Return either x or y (discrete recombination)
     */
    private double[] recombine(double[] x, double[] y) {
        if (generatorRandom.nextBoolean())
            return x;
        else
            return y;
    }
    
    /**
     * mutate
     * @param individual Double array representing a parameter set
     * @return Returns the modified parameter set. Uses method from page 76.
     * Uncorrelated mutation with n step sizes.
     * 
     * Will not return until the hard-coded parameter bound requirements have
     * been met (see code).
     */
    private double[] mutate(double [] individual)
    {
        double ithNormal;
        double[] newIndividual = new double[individual.length];
        
        // Hard-coded parameter bounds
        double[] minVal = {-3.0, 4.0};
        double[] maxVal = {12.0, 6.0};
        
        // mutate each parameter
        double oldStepSize;
        for (int i = 0; i < nNumber; i++) {
            // Force it to repeat until the mutation is within bounds
            oldStepSize = mutationStepSize[i];
            do {
                // Make sure to return to original if it needs to retry the step
                mutationStepSize[i] = oldStepSize;
                // Get the ith value from a normal distribution
                ithNormal = generatorRandom.nextGaussian();
                // Mutate the step size
                mutationStepSize[i] =  mutationStepSize[i] * (Math.pow(Math.E, 
                        (overallLearningRate * 
                                generatorRandom.nextGaussian()) 
                        + (coordinateLearningRate * ithNormal)));
                // Mutate the individual using modified step size
                newIndividual[i] = individual[i] + 
                        (mutationStepSize[i] * ithNormal);
            } while (!(minVal[i] <= newIndividual[i] 
                    && newIndividual[i] <= maxVal[i]));
        }
        
        return newIndividual;
    }
    
    /**
     * getFittestIndex
     * @param individuals Double array representing an array of sets of values
     * @return Returns the index of the fittest individual in the given array.
     */
    private int getFittestIndex(double [][] individuals)
    {
        int fittestIndex = 0;
        double bestFitnes = Double.MIN_VALUE;
        double currentFitness;
        
        for (int i = 0; i < individuals.length; i++) {
            currentFitness = getFitness(individuals[i]);
            if (currentFitness>bestFitnes)
            {
                fittestIndex = i;
                bestFitnes = currentFitness;
            }
        }
        
        return fittestIndex;
    }
    
    /**
     * getFitness
     * @param solution Double value that represents a given parameter set
     * @return Returns the fitness of an given parameter set after plugging in 
     * the solution parameters into the hard-coded equation.
     * 
     * The hard-coded equation is:
     * f(x1, x2) = 21.5 + x1 * sin(4*π*x1) + x2 * sin(20*π*x2)
     */
    public double getFitness(double [] solution)
    {
        return (21.5 + (solution[0] * Math.sin(4 * Math.PI * solution[0])) + 
                (solution[1] * Math.sin(20 * Math.PI * solution[1])));
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////CONSTRUCTORS/////////////////////////////////
    // Maximizer_API
    public Maximizer_API()
    {
        parents = new double[3][2];
        children = new double[21][2];
        mutationInitialStepSize = 1;
        terminationCount = 10000;
        nNumber = 2; //initially the number of variables (x1 and x2)
        overallLearningRate= 1.0 / Math.sqrt(2. * nNumber);
        coordinateLearningRate = 1.0 / Math.sqrt(2 * Math.sqrt(nNumber));
        mutationStepSize = new double[nNumber];
        for (int i = 0; i < nNumber; i++) {
            mutationStepSize[i] = mutationInitialStepSize;
        }
    }
    
    // Full Constructor
    public Maximizer_API(int numParents, int numChildren, int n,
            double mutationStSz, int terminationNumber)
    {
        parents = new double[numParents][n];
        children = new double[numChildren][n];
        mutationInitialStepSize = mutationStSz;
        terminationCount = terminationNumber;
        nNumber = n;
        overallLearningRate = (1.0 / Math.sqrt(2. * nNumber));
        coordinateLearningRate = (1.0 / Math.sqrt(2 * Math.sqrt(nNumber)));
        mutationStepSize = new double[n];
        for (int i = 0; i < n; i++) {
            mutationStepSize[i] = mutationStSz;
        }
    }
}
