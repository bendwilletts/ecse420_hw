//Ben Willetts 260610719 - 

package ca.mcgill.ecse420.a1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class MatrixMultiplication {
	
	private static final int NUMBER_THREADS = 4;
	private static final int MATRIX_SIZE = 500;

        public static void main(String[] args) throws InterruptedException, FileNotFoundException {
		
		int[] sizes = {100, 200, 500, 1000};
		int[] threads = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		
		//Write data to csv's to show graph
	    PrintWriter matrixSizeTest = new PrintWriter(new File("size-test.csv"));
		PrintWriter threadsTest = new PrintWriter(new File("threads-test.csv"));
		
		//1.4 # of threads test
		System.out.println("Starting Q1.4 ...");
        StringBuilder sbThr = new StringBuilder();
        sbThr.append("ThreadCount");
        sbThr.append(',');
        sbThr.append("ParallelTime(ms)");
        sbThr.append('\n');
        threadsTest.write(sbThr.toString());
        
        // Generate two random matrices, same size
        double[][] a = generateRandomMatrix(MATRIX_SIZE, MATRIX_SIZE);
        double[][] b = generateRandomMatrix(MATRIX_SIZE, MATRIX_SIZE);
        double min_time = Integer.MAX_VALUE;
        int min_thread = 0;
        for(int t: threads){
          sbThr = new StringBuilder();
          double tPar = timePar(a,b,t)/1e6;
          if (tPar <= min_time){
            min_time = tPar;
            min_thread = t;
          }
          sbThr.append(""+t);
          sbThr.append(',');
          sbThr.append(""+tPar);
          sbThr.append('\n');
          threadsTest.write(sbThr.toString());
        }
        System.out.println("Q1.4 Done"); 
        System.out.println("Thread with Minimum Time: " + min_thread);
        
		//1.5 # of matrix test
		System.out.println("Starting Q1.5 ...");
        StringBuilder sbMat = new StringBuilder();
        sbMat.append("MatrixDimension");
        sbMat.append(',');
        sbMat.append("SequentialTime(ms)");
        sbMat.append(',');
        sbMat.append("ParallelTime(ms)");
        sbMat.append('\n');
        matrixSizeTest.write(sbMat.toString());
        
        for(int size : sizes){
          sbMat = new StringBuilder();
          a = generateRandomMatrix(size, size);
          b = generateRandomMatrix(size, size);
          double tSeq = timeSeq(a,b)/1e6;
          double tPar = timePar(a,b,min_thread)/1e6;
          sbMat.append("" + size);
          sbMat.append(',');
          sbMat.append("" + tSeq);
          sbMat.append(',');
          sbMat.append("" + tPar);
          sbMat.append('\n');
          matrixSizeTest.write(sbMat.toString());
        }
        System.out.println("Q1.5 Done");  
        matrixSizeTest.close();
        threadsTest.close();
        //Q1.6 Graphs will be generated in excel
        //and shown in the report!
		
		
		
//		System.out.println("Sequential Multiply: " + Arrays.deepToString(sequentialMultiplyMatrix(a, b)));
//		System.out.println("Parallel Multiply: " +Arrays.deepToString(parallelMultiplyMatrix(a, b)));
		
//		System.out.println("Sequential Multiply: " + timePar(a,b,NUMBER_THREADS));
//		System.out.println("Parallel Multipl: " + timeSeq(a,b));
		
	}
	
	/**
	 * Returns the result of a sequential matrix multiplication
	 * The two matrices are randomly generated
	 * @param a is the first matrix
	 * @param b is the second matrix
	 * @return the result of the multiplication
	 * */
	public static double[][] sequentialMultiplyMatrix(double[][] a, double[][] b) {
	  double[][] out = new double[a.length][a[0].length];
	  for(int i=0; i<a[0].length; i++){    //Iterate rows of a
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
       public static double[][] parallelMultiplyMatrix(double[][] a, double[][] b, int numThreads) throws InterruptedException {
         
         double[][] output = new double[a.length][a[0].length]; //Output matrix
         //Thread[] threads = new Thread[numThreads]; //Thread list where tasks are added and run
    
//         for (int j = 0; j < a.length; j++) { //Row by row, create new Task and run if thread is not busy
//             if(threads[j%numThreads] != null){// If thread is busy, wait
//               threads[j%numThreads].join();
//             }
//             MultiplyTask mt = new MultiplyTask(a,b,output,j);
//             threads[j%numThreads] = new Thread(mt);
//             threads[j%numThreads].start();
//         }
         ExecutorService executor = Executors.newFixedThreadPool(numThreads);
         int start;
         int end;
         for(int i = 0; i < numThreads; i++){
           start = (a.length/numThreads)*i;
           end = (a.length/numThreads)*(i+1);
           
           Runnable wrk = new Thread(new MultiplyTask(a,b,output,start,end));
           executor.execute(wrk);
         }
         executor.shutdown();
         while(!executor.isTerminated()){}
         //wait until executer is shutdown
         return output;
         
	}
       //1.3 Method to measure the time of Parallel Mult
       public static long timePar(double[][] a, double[][] b, int numThreads){
         long startP = System.nanoTime();
         long endP = 0; 
         try {
            double[][] resultP = parallelMultiplyMatrix(a, b, numThreads);
            endP = System.nanoTime();
          } catch (InterruptedException e) {
            System.out.println("Multiplication on " + a.length + " by " + a[0].length + " matrix failed.");
          }
         return (endP - startP);
       }
       
       //1.3 time of Sequential Mult
       public static long timeSeq(double[][] a, double[][] b){
         long startS = System.nanoTime();
         double[][] resultS = sequentialMultiplyMatrix(a, b);
         long endS = System.nanoTime();   
         return (endS - startS);
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
