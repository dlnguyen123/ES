package maximizer;

/**
 * Maximizer
 * @author Reuben Sonnenberg, Tung Nguyen, Dong Nguyen
 * 
 * Author Name          Email Address
 * Reuben Sonnenberg    rjsonnenberg@alaska.edu
 * Tung Nguyen          ttnguyen4@alaska.edu
 * Done Nguyen          dlnguyen@alaska.edu
 * 
 * 
 */
public class Maximizer {
   
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int numParents = 5;
        int numOffspring = 200;
        double mutationInitialStepSize = 1;
        int terminationCount = 100;
        int n = 2; //initially the number of variables (x1 and x2)
        
        Maximizer_API myAPI = new Maximizer_API(numParents, numOffspring, n, 
                mutationInitialStepSize, terminationCount);
        double[] solutionPair = myAPI.getBestSolution();
        
        //Print out the best solution after it has found it
        System.out.print("Best solution: (");
        for (int i = 0; i < solutionPair.length - 1; i++) {
            System.out.print(solutionPair[i] + ", ");
        }
        System.out.print(solutionPair[solutionPair.length - 1]);
        System.out.println(") = " + myAPI.getFitness(solutionPair));
    }
    
}
