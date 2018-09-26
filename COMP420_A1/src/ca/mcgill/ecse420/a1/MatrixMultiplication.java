package ca.mcgill.ecse420.a1;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MatrixMultiplication {
	
	private static final int NUMBER_THREADS = 1;
	private static final int MATRIX_SIZE = 2;
	private static double[][] parallel_out = new double[MATRIX_SIZE][MATRIX_SIZE];

        public static void main(String[] args) {
		
		// Generate two random matrices, same size
//		double[][] a = generateRandomMatrix(MATRIX_SIZE, MATRIX_SIZE);
//		double[][] b = generateRandomMatrix(MATRIX_SIZE, MATRIX_SIZE);
        double[][] a = {{1,2},{3,4}};
        double[][] b = {{2,0},{1,2}};		
		System.out.println(Arrays.deepToString(sequentialMultiplyMatrix(a, b)));
		//parallelMultiplyMatrix(a, b);	
	}
	
	/**
	 * Returns the result of a sequential matrix multiplication
	 * The two matrices are randomly generated
	 * @param a is the first matrix
	 * @param b is the second matrix
	 * @return the result of the multiplication
	 * */
	public static double[][] sequentialMultiplyMatrix(double[][] a, double[][] b) {
	  double[][] out = new double[MATRIX_SIZE][MATRIX_SIZE];
	  for(int i=0; i<b[0].length; i++){    //Iterate rows of a
		  for(int j=0; j<a.length; j++){
		    out[j][i] = computeMult(a,b,i,j);
		  }
		}
	  return out;
	}
	public static double computeMult(double[][] a, double[][] b, int i, int j){
	  int count = 0;
	  for(int k = 0; k < a.length; k++){
	    count += (a[j][k]*b[k][i]);
	  }
	  return count;
	}
	
	/**
	 * Returns the result of a concurrent matrix multiplication
	 * The two matrices are randomly generated
	 * @param a is the first matrix
	 * @param b is the second matrix
	 * @return the result of the multiplication
	 * */
       public static double[][] parallelMultiplyMatrix(double[][] a, double[][] b) {
         ExecutorService executeMult = Executors.newFixedThreadPool(NUMBER_THREADS);
         //Create thread list, interate Executor to run
         // Divide matrix columns by number of threads
         
         
	}
        
        /**
         * Populates a matrix of given size with randomly generated integers between 0-10.
         * @param numRows number of rows
         * @param numCols number of cols
         * @return matrix
         */
        private static double[][] generateRandomMatrix (int numRows, int numCols) {
             double matrix[][] = new double[numRows][numCols];
        for (int row = 0 ; row < numRows ; row++ ) {
            for (int col = 0 ; col < numCols ; col++ ) {
                matrix[row][col] = (double) ((int) (Math.random() * 10.0));
            }
        }
        return matrix;
    }
        
    class multiplyTask implements Runnable{
      private double[][] a;
      private double[][] b;
      private int start;
      private int end;
      public multiplyTask(double[][] _a, double[][] _b, int _start, int _end){
        a = _a;
        b = _b;
        start = _start;
        end = _end;
      }
      
      public void run(){
        for(int i = start; i < end; i++){
          for(int j = 0; j<a.length; j++){
            parallel_out[j][i] = computeMult(a,b,i,j);
          }
        }
      }
    }
	
}
