/*
 * 
 */
package maximizer;

/**
 *
 * @author Reuben Sonnenberg, Tung Nguyen, Dong Nguyen
 * 
 * Programming Assignment #2 - Evolution Strategies
 * Goal: evolve a solution x = {x1, x2} that maximizes the following function:
 *      f(x1, x2) = 21.5 + x1 * sin(4*pi*x1) + x2 * sin(20*pi*x2)
 * subject to the constraints
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
 *  Run the ES 10 times using a different random seed for each run. Record the x
 * values of the best-of-run individual for each run, as well as its fitness
 * value, f(x). As with any programming project, your program should exemplify
 * principles of high-quality computer software design. Program-level 
 * documentation should indicate the name and email address of each author.
 * 
 */
public class Maximizer {
   
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int numParents = 3;
        int numOffspring = 21;
        double mutationInitialStepSize = 1;
        int terminationCount = 10000;
        int n = 2; //initially the number of variables (x1 and x2)
        
        Maximizer_API myAPI = new Maximizer_API(numParents, numOffspring, n, 
                mutationInitialStepSize, terminationCount);
        double[] solutionPair = myAPI.getBestSolution();
        
        //Print out the best solution after it has found it
        System.out.println("Best solution: (");
        for (int i = 0; i < solutionPair.length - 1; i++) {
            System.out.print(solutionPair[i] + ", ");
        }
        System.out.print(solutionPair[solutionPair.length - 1]);
        System.out.println(")");
    }
    
}
