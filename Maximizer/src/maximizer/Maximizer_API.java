/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maximizer;

import com.sun.org.apache.xerces.internal.xs.datatypes.XSDouble;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

/**
 *
 * @author Reuben
 */
public class Maximizer_API {
    private final double[][] parents;
    private final double[][] children;
    private final double mutationInitialStepSize;
    private final double[] mutationStepSize;
    private int terminationCount;
    private int nNumber; // number of parameters (basically)
    private double overallLearningRate;
    private double coordinateLearningRate;
    
    // Randomization generator
    private final Random generatorRandom = new Random();
    
    // This is the main runner of the problem.
    // Returns a double array with the best parameter combinations.
    public double[] getBestSolution() {

        init();
        
        int fitIndex;
        do {
            generateOffspring();
            selectParents();
            fitIndex = getFittestIndex(parents);
            
            //Print best-of-run individual and resulting fitness
            System.out.println(terminationCount + ": f(" + parents[fitIndex][0] 
                    + ", " + parents[fitIndex][1] + ") = " + 
                    getFitness(parents[fitIndex]));
            
            terminationCount--;
        } while (terminationCount > 0);
        
        return parents[getFittestIndex(parents)];
    }
    
    // Initialization
    // Set parents to randomized values between the given bounds:
    // -3.0 <= x1 <= 12.0
    // 4.0 <= x2 <= 6.0
    private void init() {
        // Initialize parent values:
        for (double[] parent : parents) {
            parent[0] = (generatorRandom.nextDouble() * 
                    (15.0 + Double.MIN_VALUE)) - 3.0;
            parent[1] = (generatorRandom.nextDouble() * 
                    (2.0 + Double.MIN_VALUE)) + 4.0;
        }
    }
    
    // Generate Offspring
    // Description: generates the children using recombination and mutation
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
    
    // Parent Selection
    // Selects the parents from the resulting children
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
        for (int i = 0; i < parents.length; i++) {
            parents[i] = children[(int)(currentFitness[i][1])];
        }
    }
    
    // Discrete recombination
    // Takes two parents and performs discrete recobmination
    private double[] recombine(double[] x, double[] y) {
        if (generatorRandom.nextBoolean())
            return x;
        else
            return y;
    }
    
    // Uncorrelated mutation with n step sizes
    // Input:    double array with n parameters
    // Output:   modified input
    // Accepts an individual and mutates each parameter using the methods from
    // page 76 in the book.
    //
    // Need to make sure that the parameters remain within original bounds:
    // -3.0 <= x1 <= 12.0
    // 4.0 <= x2 <= 6.0
    // For this instance, it is hard-coded; but could be made an instance var.
    private double[] mutate(double [] individual)
    {
        double ithNormal;
        double[] newIndividual = new double[individual.length];
        
        double[] minVal = {-3.0, 4.0};
        double[] maxVal = {12.0, 6.0};
        
        // mutate each parameter
        for (int i = 0; i < getnNumber(); i++) {
            // Force it to repeat until the mutation is within bounds
            do {
                ithNormal = generatorRandom.nextGaussian();
                mutationStepSize[i] =  mutationStepSize[i] * (Math.pow(Math.E, 
                        (getOverallLearningRate() * generatorRandom.nextGaussian()) 
                                + (getCoordinateLearningRate() * ithNormal)));
                newIndividual[i] = individual[i] + 
                        (mutationStepSize[i] * ithNormal);
            } while (minVal[i] <= individual[i] && individual[i] <= maxVal[i]);
        }
        
        return newIndividual;
    }
    
    //NOTE: We may not even need the two methods below since fitness is simply 
    //      the value that is the largest. We could easily keep track of the 
    //      fittest solution set with a single variable. This could cut down on
    //      computation time. We may only neet the fitness part.
    
    // Get fittest individual of current run (children)
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
    
    // Get fitness of individual
    // Returns the value after plugging in the solution parameters into the 
    // equation.
    private double getFitness(double [] solution)
    {
        return (21.5 + (solution[0] * Math.sin(4 * Math.PI * solution[0])) + 
                (solution[1] * Math.sin(20 * Math.PI * solution[1])));
    }
    
    ////////////////////////////////////////////////////////////////////////////
    /////////////// Getters and Setters for instance variables /////////////////
    public int getTerminationCount() {
        return terminationCount;
    }

    public void setTerminationCount(int terminationCount) {
        this.terminationCount = terminationCount;
    }

    public int getnNumber() {
        return nNumber;
    }

    public void setnNumber(int nNumber) {
        this.nNumber = nNumber;
    }

    public double getOverallLearningRate() {
        return overallLearningRate;
    }

    public void (double overallLearningRate) {
        this.overallLearningRate = overallLearningRate;
    }

    public double getCoordinateLearningRate() {
        return coordinateLearningRate;
    }

    public void setCoordinateLearningRate(double coordinateLearningRate) {
        this.coordinateLearningRate = coordinateLearningRate;
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
        overallLearningRate = 1.0 / Math.sqrt(2. * nNumber);
        coordinateLearningRate = 1.0 / Math.sqrt(2 * Math.sqrt(nNumber));
        mutationStepSize = new double[nNumber];
        for (int i = 0; i < nNumber; i++) {
            mutationStepSize[i] = mutationInitialStepSize;
        }
    }
    
    public Maximizer_API(int numParents, int numChildren, int n,
            double mutationStSz, int terminationNumber)
    {
        parents = new double[numParents][n];
        children = new double[numChildren][n];
        mutationInitialStepSize = mutationStSz;
        terminationCount = terminationNumber;
        nNumber = n;
        overallLearningRate = 1.0 / Math.sqrt(2. * nNumber);
        coordinateLearningRate = 1.0 / Math.sqrt(2 * Math.sqrt(nNumber));
        mutationStepSize = new double[n];
        for (int i = 0; i < n; i++) {
            mutationStepSize[i] = mutationStSz;
        }
    }
}
