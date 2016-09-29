//package ps.hell.ml.optimization.sgd;
//
//
//import ps.hell.base.data.Dataset;
//
//import java.util.ArrayList;
//
//public class OtherSGD {
//    private Dataset dataset = null;
//    double[][] P;
//    double[][] Q;
//    private int M = -1; // 行数
//    private int V = -1; // 列数
//    private int K = -1; // 隐含主题数
//    // 随机梯度下降，更新参数
//    public void updatePQ_stochastic(double alpha, double beta) {
//        for (int i = 0; i < M; i++) {
//            ArrayList<Feature> Ri = this.dataset.getDataAt(i).getAllFeature();
//            for (Feature Rij : Ri) {
//                // eij=Rij.weight-PQ for updating P and Q
//                double PQ = 0;
//                for (int k = 0; k < K; k++) {
//                    PQ += P[i][k] * Q[k][Rij.dim];
//                }
//                double eij = Rij.weight - PQ;
//
//                // update Pik and Qkj
//                for (int k = 0; k < K; k++) {
//                    double oldPik = P[i][k];
//                    P[i][k] += alpha
//                            * (2 * eij * Q[k][Rij.dim] - beta * P[i][k]);
//                    Q[k][Rij.dim] += alpha
//                            * (2 * eij * oldPik - beta * Q[k][Rij.dim]);
//                }
//            }
//        }
//    }
//
//    // 批量梯度下降，更新参数
//    public void updatePQ_batch(double alpha, double beta) {
//
//        for (int i = 0; i < M; i++) {
//            ArrayList<Feature> Ri = this.dataset.getDataAt(i).getAllFeature();
//
//            for (Feature Rij : Ri) {
//                // Rij.error=Rij.weight-PQ for updating P and Q
//                double PQ = 0;
//                for (int k = 0; k < K; k++) {
//                    PQ += P[i][k] * Q[k][Rij.dim];
//                }
//                Rij.error = Rij.weight - PQ;
//            }
//        }
//
//        for (int i = 0; i < M; i++) {
//            ArrayList<Feature> Ri = this.dataset.getDataAt(i).getAllFeature();
//            for (Feature Rij : Ri) {
//                for (int k = 0; k < K; k++) {
//                    // 对参数更新的累积项
//                    double eq_sum = 0;
//                    double ep_sum = 0;
//
//                    for (int ki = 0; ki < M; ki++) {// 固定k和j之后,对所有i项加和
//                        ArrayList<Feature> tmp = this.dataset.getDataAt(i).getAllFeature();
//                        for (Feature Rj : tmp) {
//                            if (Rj.dim == Rij.dim)
//                                ep_sum += P[ki][k] * Rj.error;
//                        }
//                    }
//                    for (Feature Rj : Ri) {// 固定k和i之后,对多有j项加和
//                        eq_sum += Rj.error * Q[k][Rj.dim];
//                    }
//
//                    // 对参数更新
//                    P[i][k] += alpha * (2 * eq_sum - beta * P[i][k]);
//                    Q[k][Rij.dim] += alpha * (2 * ep_sum - beta * Q[k][Rij.dim]);
//                }
//            }
//        }
//    }
//}
