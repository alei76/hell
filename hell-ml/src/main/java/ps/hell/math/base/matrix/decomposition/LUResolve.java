package ps.hell.math.base.matrix.decomposition;

/**
 * LU分解
 * @author Administrator
 *
 */
public class LUResolve {
	/**
	 * 输入数据
	 */
	 double[][] data ;
	 /**
	  * 输入数据的逆矩阵
	  */
	 double[][] dataInversion;
	   
	 double[] b = null;
	    
	 int anrow = 0;
	 /**
     * 每一行的最大数
     */
    double[] rowMax=null; 
    int[] indx=null;
    
    /**
     * lu 计算
     */
    private void lucmp() {
        int n = anrow, imax = 0;
        for (int i = 0; i < n; i++) {
            double big = 0.0;
            for (int j = 0; j < n; j++) {
                double temp = Math.abs(data[i][j]);
                if (temp > big) big = temp;
            }
            rowMax[i] = 1.0 / big;
        }
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < j; i++) {
                double sum = data[i][j];
                for (int k = 0; k < i; k++) sum -= data[i][k] * data[k][j];
                data[i][j] = sum;
            }
            double big = 0.0;
            for (int i = j; i < n; i++) {
                double sum = data[i][j];
                for (int k = 0; k < j; k++) sum -= data[i][k] * data[k][j];
                data[i][j] = sum;
                double dum = rowMax[i] * Math.abs(sum);
                if (dum >= big) {
                    big = dum;
                    imax = i;
                }
            }
            if (j != imax) {
                for (int i = 0; i < n; i++) {
                    double mid = data[imax][i];
                    data[imax][i] = data[j][i];
                    data[j][i] = mid;
                }
                double mid = rowMax[j];
                rowMax[j] = rowMax[imax];
                rowMax[imax] = mid;
            }
            indx[j] = imax;
            if (j != n - 1) {
                double dum = 1.0/data[j][j];
                for (int i = j + 1; i < n; i++) data[i][j] *= dum; 
            }
        }
    }
    
    private void lubksb(double[] b) {
        int n = anrow, ii = 0;
        // y
        for (int i = 0; i < n; i++) {    
            int ip = indx[i];        
            double sum = b[ip];
            b[ip] = b[i];
            if (ii != 0)
                for (int j = ii - 1; j < i; j++) sum -= data[i][j] * b[j];
            else
                ii = i + 1;
            b[i] = sum;
        }
        // x
        for (int i = n - 1; i >= 0; i--) {
            double sum = b[i];
            for (int j = i + 1; j < n; j++) sum -= data[i][j]*b[j];
            b[i] = sum / data[i][i];
        }
    }
    /**
     * 
     * @param inputData 输入矩阵
     */
    public LUResolve(double[][] inputData)
    {
    	this.data=inputData.clone();
    	anrow=inputData.length;
    	rowMax = new double[anrow];
    	indx= new int[anrow];
    }
    
    public void compute()
    {
    	lucmp();
    	double[] b = new double[anrow];
    	dataInversion = new double[anrow][anrow];
        for (int i = 0; i < anrow; i++) { 
            for (int j = 0; j < anrow; j++) b[j] = 0;
            b[i] = 1.0;
            lubksb(b);
            for (int j = 0; j < anrow; j++) dataInversion[j][i] = b[j];
        }
    }

    public static void main(String[] args) {
        double[][] a = {
                {0.0, 2.0, 0.0, 1.0},
                {2.0, 2.0, 3.0, 2.0},
                {4.0, -3.0, 0.0, 1.0},
                {6.0, 1.0, -6.0, -5.0}
        };
       LUResolve lu= new LUResolve(a);
       lu.compute();
       System.out.println("a的逆");
       for(int i=0;i<a.length;i++)
       {
    	   for(int j=0;j<a[0].length;j++)
    	   {
    		   System.out.print(lu.dataInversion[i][j]+"\t");
    	   }
    	   System.out.println();
       }
    }
}
