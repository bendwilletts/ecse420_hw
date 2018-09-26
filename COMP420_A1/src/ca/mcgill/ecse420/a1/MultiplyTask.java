package ca.mcgill.ecse420.a1;

public class MultiplyTask extends Thread {
  private double[][] a;
  private double[][] b;
  private double[][] out;
  private int ind;
  public MultiplyTask(double[][] _a, double[][] _b, double[][] _out, int _ind){
    this.a = _a;
    this.b = _b;
    this.out = _out;
    this.ind = _ind;
  }
  
  @Override
  public void run() {
    // TODO Auto-generated method stub
    for(int i = 0; i < b[0].length; i++){   //row by row multiplication
      for(int j = 0; j<this.a.length; j++){
        out[j][ind] += a[j][i]*b[i][ind];   //multiply all a with the index row of b, add to the output row
      }
    }
  }

}
