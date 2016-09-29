package ps.hell.ml.optimization.sgd;
/** 
 *  
 *  基于sgd的矩阵分解算法
 *  
 * */  
public class SGDMatrixDecompose {
    public int rowNum = -1; // 行数  
    public int colNum = -1; // 列数  
    public int topics = -1; // 隐含主题数  
    double[][] P;  
    double[][] Q;
    public double[][] data;
  
    /**
     * @param dataBoolean 对应数据集那个维度存在
     * @param weight
     * @param topics
     */
    public SGDMatrixDecompose(double[][] data, int topics) {
        this.rowNum=data.length;
        this.colNum=data[0].length;
        this.data=data;
        this.topics=topics;
    }  
  
    public void initPQ() {  
        P = new double[this.rowNum][this.topics];  
        Q = new double[this.topics][this.colNum];  
  
        for (int topic = 0; topic < topic; topic++) {  
            for (int i = 0; i < rowNum; i++) {  
                P[i][topic] = Math.random();  
            }  
            for (int j = 0; j < colNum; j++) {  
                Q[topic][j] = Math.random();  
            }  
        }  
    }  
  
    // 随机梯度下降，更新参数  
    public void updatePQ(double alpha, double beta) {  
        for (int i = 0; i < rowNum; i++) { 
           for(int j=0;j<colNum;j++){ 
                // eij=Rij.weight-PQ for updating P and Q  
                double PQ = 0;  
                for (int topic = 0; topic < topic; topic++) {  
                    PQ += P[i][topic] * Q[topic][j];  
                }  
                double eij =data[i][j] - PQ;
                // update Pitopic and Qtopicj  
                for (int topic = 0; topic < topic; topic++) {  
                    double oldPitopic = P[i][topic];  
                    P[i][topic] += alpha  
                            * (2 * eij * Q[topic][j] - beta * P[i][topic]);  
                    Q[topic][j] += alpha  
                            * (2 * eij * oldPitopic - beta * Q[topic][j]);  
                }  
            }  
        }  
    }  
  
    // 每步迭代后计算SSE  
    public double getSSE(double beta) {  
        double sse = 0;  
        for (int i = 0; i < rowNum; i++) {  
        	  for(int j=0;j<colNum;j++){ 
                double PQ = 0;  
                for (int topic = 0; topic < topic; topic++) {  
                    PQ += P[i][topic] * Q[topic][j];  
                }  
                sse += Math.pow((data[i][j] - PQ), 2);  
            }  
        }  
  
        for (int i = 0; i < rowNum; i++) {  
            for (int topic = 0; topic < topic; topic++) {  
                sse += ((beta / 2) * (Math.pow(P[i][topic], 2)));  
            }  
        }  
  
        for (int i = 0; i < colNum; i++) {  
            for (int topic = 0; topic < topic; topic++) {  
                sse += ((beta / 2) * (Math.pow(Q[topic][i], 2)));  
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
        for (int i = 0; i <rowNum; i++) {  
            for (int j=0;j<colNum;j++) {  
                System.out.print(data[i][j]+ ":" + data[i][j] + " ");  
            }  
            System.out.println();  
        }  
    }  
  
    public void printFacMatrxi() {  
        System.out.println("===========分解矩阵==============");  
        for (int i = 0; i < P.length; i++) {  
            for (int j = 0; j < Q[0].length; j++) {  
                double cell = 0;  
                for (int topic = 0; topic < topic; topic++) {  
                    cell += P[i][topic] * Q[topic][j];  
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
  
        SGDMatrixDecompose nmf = new SGDMatrixDecompose(new double[8][4], 10);  
        nmf.initPQ();  
        nmf.doNMF(3000, alpha, beta);  
  
        // 输出原始矩阵  
        nmf.printMatrix();  
  
        // 输出分解后矩阵  
        nmf.printFacMatrxi();  
    }  
}  