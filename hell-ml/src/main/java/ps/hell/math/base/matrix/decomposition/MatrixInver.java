package ps.hell.math.base.matrix.decomposition;

/**
 * lu分解
 * @author Administrator
 *
 */
public class MatrixInver {

    double[][] a = {
            {0.0, 2.0, 0.0, 1.0},
            {2.0, 2.0, 3.0, 2.0},
            {4.0, -3.0, 0.0, 1.0},
            {6.0, 1.0, -6.0, -5.0}
    };
    
    double[] b = null;
    
    int anrow = 4;
    /**
     * 每一行的最大数
     */
    double[] rowMax = new double[anrow];
    int[] indx = new int[anrow];
    
    /**
     * lu 计算
     */
    private void lucmp() {
        int n = anrow, imax = 0;
        for (int i = 0; i < n; i++) {
            double big = 0.0;
            for (int j = 0; j < n; j++) {
                double temp = Math.abs(a[i][j]);
                if (temp > big) big = temp;
            }
            rowMax[i] = 1.0 / big;
        }
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < j; i++) {
                double sum = a[i][j];
                for (int k = 0; k < i; k++) sum -= a[i][k] * a[k][j];
                a[i][j] = sum;
            }
            double big = 0.0;
            for (int i = j; i < n; i++) {
                double sum = a[i][j];
                for (int k = 0; k < j; k++) sum -= a[i][k] * a[k][j];
                a[i][j] = sum;
                double dum = rowMax[i] * Math.abs(sum);
                if (dum >= big) {
                    big = dum;
                    imax = i;
                }
            }
            if (j != imax) {
                for (int i = 0; i < n; i++) {
                    double mid = a[imax][i];
                    a[imax][i] = a[j][i];
                    a[j][i] = mid;
                }
                double mid = rowMax[j];
                rowMax[j] = rowMax[imax];
                rowMax[imax] = mid;
            }
            indx[j] = imax;
            if (j != n - 1) {
                double dum = 1.0/a[j][j];
                for (int i = j + 1; i < n; i++) a[i][j] *= dum; 
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
                for (int j = ii - 1; j < i; j++) sum -= a[i][j] * b[j];
            else
                ii = i + 1;
            b[i] = sum;
        }
        // x
        for (int i = n - 1; i >= 0; i--) {
            double sum = b[i];
            for (int j = i + 1; j < n; j++) sum -= a[i][j]*b[j];
            b[i] = sum / a[i][i];
        }
    }
    /**
     * 显示矩阵
     * @param a
     * @param anrow
     */
    private void output(double a[][], int anrow) {
        for (int i = 0; i < anrow; i++) {
            System.out.println(" | " + a[i][0] + " " + 
                    a[i][1] + " " + 
                    a[i][2] + " " + 
                    a[i][3] + " | ");
        }
        System.out.println("-----------------------------------------------");
    }
    
    public MatrixInver() {
        System.out.println("Origin matrix:");
        output(a,4);
        lucmp();
        double[] b = new double[anrow];
        double[][] y = new double[anrow][anrow];
        for (int i = 0; i < anrow; i++) { 
            for (int j = 0; j < anrow; j++) b[j] = 0;
            b[i] = 1.0;
            lubksb(b);
            for (int j = 0; j < anrow; j++) y[j][i] = b[j];
        }
        System.out.println("Its inverse matrix:");
        output(y,4);
    }
    
    public static void main(String[] args) {
        new MatrixInver();
    }

}