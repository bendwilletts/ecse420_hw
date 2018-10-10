package ca.mcgill.ecse420.a1;

public class MultiplyTask implements Runnable {
  private double[][] a;
  private double[][] b;
  private double[][] out;
  private int start;
  private int end;
  
  public MultiplyTask(double[][] _a, double[][] _b, double[][] _out, int _start, int _end){
    this.a = _a;
    this.b = _b;
    this.out = _out;
    this.start = _start;
    this.end = _end;
  }
  
  @Override
  public void run() {
    // TODO Auto-generated method stub
    for(int k = this.start; k < this.end; k++){
      for(int i = 0; i < this.b[0].length; i++){   //row by row multiplication
        for(int j = 0; j<this.a.length; j++){
          out[j][k] += a[j][i]*b[i][k];   //multiply all a with the index row of b, add to the output row
        }
      }
    }
  }
}
