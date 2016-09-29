package ps.hell.ml.optimization.sgd;

import ps.hell.base.data.Dataset;

import java.io.File;
import java.util.ArrayList;

/** 
 * @author 玉心sober： http://weibo.com/karensober 
 * @date 2013-05-19 
 *  
 * */  
public class NMF {  
    private Dataset dataset = null;  
    private int M = -1; // 行数  
    private int V = -1; // 列数  
    private int K = -1; // 隐含主题数  
    double[][] P;  
    double[][] Q;  
  
    public NMF(String datafileName, int topics) {  
        File datafile = new File(datafileName);  
        if (datafile.exists()) {  
            if ((this.dataset = new Dataset(datafile)) == null) {  
                System.out.println(datafileName + " is null");  
            }  
            this.M = this.dataset.size();  
            this.V = this.dataset.getFeatureNum();  
            this.K = topics;  
        } else {  
            System.out.println(datafileName + " doesn't exist");  
        }  
    }  
  
    public void initPQ() {  
        P = new double[this.M][this.K];  
        Q = new double[this.K][this.V];  
  
        for (int k = 0; k < K; k++) {  
            for (int i = 0; i < M; i++) {  
                P[i][k] = Math.random();  
            }  
            for (int j = 0; j < V; j++) {  
                Q[k][j] = Math.random();  
            }  
        }  
    }  
  
    // 随机梯度下降，更新参数  
    public void updatePQ(double alpha, double beta) {  
        for (int i = 0; i < M; i++) {  
            ArrayList<Feature> Ri = this.dataset.getDataAt(i).getAllFeature();  
            for (Feature Rij : Ri) {  
                // eij=Rij.weight-PQ for updating P and Q  
                double PQ = 0;  
                for (int k = 0; k < K; k++) {  
                    PQ += P[i][k] * Q[k][Rij.dim];  
                }  
                double eij = Rij.weight - PQ;  
  
                // update Pik and Qkj  
                for (int k = 0; k < K; k++) {  
                    double oldPik = P[i][k];  
                    P[i][k] += alpha  
                            * (2 * eij * Q[k][Rij.dim] - beta * P[i][k]);  
                    Q[k][Rij.dim] += alpha  
                            * (2 * eij * oldPik - beta * Q[k][Rij.dim]);  
                }  
            }  
        }  
    }  
  
    // 每步迭代后计算SSE  
    public double getSSE(double beta) {  
        double sse = 0;  
        for (int i = 0; i < M; i++) {  
            ArrayList<Feature> Ri = this.dataset.getDataAt(i).getAllFeature();  
            for (Feature Rij : Ri) {  
                double PQ = 0;  
                for (int k = 0; k < K; k++) {  
                    PQ += P[i][k] * Q[k][Rij.dim];  
                }  
                sse += Math.pow((Rij.weight - PQ), 2);  
            }  
        }  
  
        for (int i = 0; i < M; i++) {  
            for (int k = 0; k < K; k++) {  
                sse += ((beta / 2) * (Math.pow(P[i][k], 2)));  
            }  
        }  
  
        for (int i = 0; i < V; i++) {  
            for (int k = 0; k < K; k++) {  
                sse += ((beta / 2) * (Math.pow(Q[k][i], 2)));  
            }  
        }  
  
        return sse;  
    }  
  
    // 采用随机梯度下降方法迭代求解参数，即求解最终分解后的矩阵  
    public boolean doNMF(int iters, double alpha, double beta) {  
        for (int step = 0; step < iters; step++) {  
            updatePQ(alpha, beta);  
            double sse = getSSE(beta);  
            if (step % 100 == 0)  
                System.out.println("step " + step + " SSE = " + sse);  
        }  
        return true;  
    }  
  
    public void printMatrix() {  
        System.out.println("===========原始矩阵==============");  
        for (int i = 0; i < this.dataset.size(); i++) {  
            for (Feature feature : this.dataset.getDataAt(i).getAllFeature()) {  
                System.out.print(feature.dim + ":" + feature.weight + " ");  
            }  
            System.out.println();  
        }  
    }  
  
    public void printFacMatrxi() {  
        System.out.println("===========分解矩阵==============");  
        for (int i = 0; i < P.length; i++) {  
            for (int j = 0; j < Q[0].length; j++) {  
                double cell = 0;  
                for (int k = 0; k < K; k++) {  
                    cell += P[i][k] * Q[k][j];  
                }  
                System.out.print(baoliu(cell, 3) + " ");  
            }  
            System.out.println();  
        }  
    }  
  
    // 为double类型变量保留有效数字  
    public static double baoliu(double d, int n) {  
        double p = Math.pow(10, n);  
        return Math.round(d * p) / p;  
    }  
  
    public static void main(String[] args) {  
        double alpha = 0.002;  
        double beta = 0.02;  
  
        NMF nmf = new NMF("D:\\myEclipse\\graphModel\\data\\nmfinput.txt", 10);  
        nmf.initPQ();  
        nmf.doNMF(3000, alpha, beta);  
  
        // 输出原始矩阵  
        nmf.printMatrix();  
  
        // 输出分解后矩阵  
        nmf.printFacMatrxi();  
    }  
}  