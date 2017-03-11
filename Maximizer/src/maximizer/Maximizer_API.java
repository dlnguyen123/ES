/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maximizer;

import java.util.Random;

/**
 *
 * @author Reuben
 */
public class Maximizer_API {
    private double[][] parents;
    private double[][] children;
    private double mutationInitialStepSize;
    private int terminationCount;
    private double nNumber; // n variable in problem to calculate learning rate
    private double overallLearningRate;
    private double coordinateLearningRate;
    
    
    // This is the main runner of the problem.
    // Returns a double array with the best parameter combinations.
    public double[] getBestSolution() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    // Discrete recombination
    // Takes two parents and performs discrete recobmination
    private double[] recombine(double[] x, double[] y) {
        Random r = new Random();
        if (r.nextBoolean())
            return x;
        else
            return y;
    }
    
    //Uncorrelated mutation with n step sizes
    private double[] mutate(double [] individual)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    //NOTE: We may not even need the two methods below since fitness is simply 
    //      the value that is the largest. We could easily keep track of the 
    //      fittest solution set with a single variable. This could cut down on
    //      computation time. We may only neet the fitness part.
    
    // Get fittest individual of current run (children)
    private double[] getFittestIndividual()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    // Get fitness of individual
    // Get the fitness by simply returning the value after plugging in the
    // solution parameters into the equation and seeing how good it is.
    private double getFitness(double [] solution)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    ////////////////////////////////////////////////////////////////////////////
    /////////////// Getters and Setters for instance variables /////////////////
    public int getTerminationCount() {
        return terminationCount;
    }

    public void setTerminationCount(int terminationCount) {
        this.terminationCount = terminationCount;
    }

    public double getnNumber() {
        return nNumber;
    }

    public void setnNumber(double nNumber) {
        this.nNumber = nNumber;
    }

    public double getOverallLearningRate() {
        return overallLearningRate;
    }

    public void setOverallLearningRate(double overallLearningRate) {
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
        nNumber = 2.0; //initially the number of variables (x1 and x2)
        overallLearningRate = 1.0 / Math.sqrt(2. * nNumber);
        coordinateLearningRate = 1.0 / Math.sqrt(2 * Math.sqrt(nNumber));
    }
    
    public Maximizer_API(int numParents, int numChildren, int n,
            double mutationStepSize, int terminationNumber)
    {
        parents = new double[numParents][n];
        children = new double[numChildren][n];
        mutationInitialStepSize = mutationStepSize;
        terminationCount = terminationNumber;
        nNumber = n;
        overallLearningRate = 1.0 / Math.sqrt(2. * nNumber);
        coordinateLearningRate = 1.0 / Math.sqrt(2 * Math.sqrt(nNumber));
    }
}
