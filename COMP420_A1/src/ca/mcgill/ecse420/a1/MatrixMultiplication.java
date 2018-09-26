package ca.mcgill.ecse420.a1;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MatrixMultiplication {
	
	private static final int NUMBER_THREADS = 4;
	private static final int MATRIX_SIZE = 100;

        public static void main(String[] args) throws InterruptedException {
		
		// Generate two random matrices, same size
		double[][] a = generateRandomMatrix(MATRIX_SIZE, MATRIX_SIZE);
		double[][] b = generateRandomMatrix(MATRIX_SIZE, MATRIX_SIZE);	
		System.out.println("Sequential Multiply: " + Arrays.deepToString(sequentialMultiplyMatrix(a, b)));
		System.out.println("Parallel Multiply: " +Arrays.deepToString(parallelMultiplyMatrix(a, b)));	
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
	 * @throws InterruptedException 
	 * */
       public static double[][] parallelMultiplyMatrix(double[][] a, double[][] b) throws InterruptedException {
         
         double[][] output = new double[MATRIX_SIZE][MATRIX_SIZE]; //Output matrix
         MultiplyTask threads[] = new MultiplyTask[NUMBER_THREADS]; //Thread list where tasks are added and run
    
         for (int j = 0; j < a.length; j++) { //Row by row, create new Task and run if thread is not busy
             if(threads[j%NUMBER_THREADS] != null){// If thread is busy, wait
               threads[j%NUMBER_THREADS].join();
             }
             threads[j%NUMBER_THREADS] = new MultiplyTask(a,b,output,j);
             threads[j%NUMBER_THREADS].start();
         }

         return output;
         
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
	
}
