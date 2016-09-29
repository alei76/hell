/*******************************************************************************
 * Copyright (c) 2010 Haifeng Li
 *   
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package ps.hell.ml.manifold;

import java.util.Arrays;
import java.util.Comparator;

import ps.hell.math.base.distance.EuclideanDistance;
import ps.hell.ml.graph.AdjacencyList;
import ps.hell.ml.graph.Graph;
import ps.hell.math.base.MathBase;
import ps.hell.math.base.matrix.SparseMatrix;
import ps.hell.math.base.matrix.decomposition.EigenValueDecomposition2;
import ps.hell.math.base.matrix.decomposition.LUDecomposition2;
import ps.hell.ml.neighbor.KDTree;
import ps.hell.ml.neighbor.KNNSearch;
import ps.hell.ml.neighbor.Neighbor;
import ps.hell.ml.neighbor.CoverTree;

/**
 * 局部线性嵌入。在Isomap它有几个优点,包括
*时更快的优化利用稀疏矩阵来实现
*算法,并与许多问题更好的结果。米歇尔也开始
*寻找一组每个点的最近的邻居。然后计算
*一组每个点最能描述点的权重作为线性的
*邻国的组合。最后,它使用一个eigenvector-based
*优化技术的低维嵌入点,
*这样每个点仍然是描述相同的线性组合
*它的邻居。米歇尔倾向于处理非均匀采样密度差
*因为没有固定单位防止权重漂流
不同的地区不同的样本密度
 * Locally Linear Embedding. It has several advantages over Isomap, including
 * faster optimization when implemented to take advantage of sparse matrix
 * algorithms, and better results with many problems. LLE also begins by
 * finding a set of the nearest neighbors of each point. It then computes
 * a set of weights for each point that best describe the point as a linear
 * combination of its neighbors. Finally, it uses an eigenvector-based
 * optimization technique to find the low-dimensional embedding of points,
 * such that each point is still described with the same linear combination
 * of its neighbors. LLE tends to handle non-uniform sample densities poorly
 * because there is no fixed unit to prevent the weights from drifting as
 * various regions differ in sample densities.
 * 
 * @see IsoMap
 * @see LaplacianEigenmap
 * 
 * <h2>References</h2>
 * <ol>
 * <li> Sam T. Roweis and Lawrence K. Saul. Nonlinear Dimensionality Reduction by Locally Linear Embedding. Science 290(5500):2323-2326, 2000. </li>
 * </ol>
 * 
 * @author Haifeng Li
 */
public class LLE {

    /**
     * The original sample index.
     */
    private int[] index;
    /**
     * Coordinate matrix.
     */
    private double[][] coordinates;
    /**
     * Nearest neighbor graph.
     */
    private Graph graph;

    /**
     * Constructor.
     * @param data the dataset.
     * @param d the dimension of the manifold.
     * @param k k-nearest neighbor.
     */
    public LLE(double[][] data, int d, int k) {
        int n = data.length;
        int D = data[0].length;

        double tol = 0.0;
        if (k > D) {
            System.out.println("LLE: regularization will be used since K > D.");
            tol = 1E-3;
        }

        KNNSearch<double[], double[]> knn = null;
        if (D < 10) {
            knn = new KDTree<double[]>(data, data);
        } else {
            knn = new CoverTree<double[]>(data, new EuclideanDistance());
        }

        Comparator<Neighbor<double[], double[]>> comparator = new Comparator<Neighbor<double[], double[]>>() {

            @Override
            public int compare(Neighbor<double[], double[]> o1, Neighbor<double[], double[]> o2) {
                return o1.index - o2.index;
            }
        };

        int[][] N = new int[n][k];
        graph = new AdjacencyList(n);
        for (int i = 0; i < n; i++) {
            Neighbor<double[], double[]>[] neighbors = knn.knn(data[i], k);
            Arrays.sort(neighbors, comparator);

            for (int j = 0; j < k; j++) {
                graph.setWeight(i, neighbors[j].index, neighbors[j].distance);
                N[i][j] = neighbors[j].index;
            }
        }

        // Use largest connected component.
        int[][] cc = graph.dfs();
        int[] newIndex = new int[n];
        if (cc.length == 1) {
            index = new int[n];
            for (int i = 0; i < n; i++) {
                index[i] = i;
                newIndex[i] = i;
            }
        } else {
            n = 0;
            int component = 0;
            for (int i = 0; i < cc.length; i++) {
                if (cc[i].length > n) {
                    component = i;
                    n = cc[i].length;
                }
            }

            System.out.format("LLE: %d connected components, largest one has %d samples.\n", cc.length, n);

            index = cc[component];
            graph = graph.subgraph(index);
            for (int i = 0; i < index.length; i++) {
                newIndex[index[i]] = i;
            }
        }

        int len = n * (k+1);
        double[] w = new double[len];
        int[] rowIndex = new int[len];
        int[] colIndex = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            colIndex[i] = colIndex[i - 1] + k + 1;
        }

        double[][] C = new double[k][k];
        double[] x = new double[k];
        double[] b = new double[k];
        for (int i = 0; i < k; i++) {
            b[i] = 1.0;
        }

        int m = 0;
        for (int i : index) {
            double trace = 0.0;
            for (int p = 0; p < k; p++) {
                for (int q = 0; q < k; q++) {
                    C[p][q] = 0.0;
                    for (int l = 0; l < D; l++) {
                        C[p][q] += (data[i][l] - data[N[i][p]][l]) * (data[i][l] - data[N[i][q]][l]);
                    }
                }
                trace += C[p][p];
            }

            if (tol != 0.0) {
                trace *= tol;
                for (int p = 0; p < k; p++) {
                    C[p][p] += trace;
                }
            }

            LUDecomposition2 lu = new LUDecomposition2(C, true);
            lu.solve(b, x);

            double sum = MathBase.sum(x);
            int shift = 0;
            for (int p = 0; p < k; p++) {
                if (newIndex[N[i][p]] > m && shift == 0) {
                    shift = 1;
                    w[m * (k + 1) + p] = 1.0;
                    rowIndex[m * (k + 1) + p] = m;
                }
                w[m * (k + 1) + p + shift] = -x[p] / sum;
                rowIndex[m * (k + 1) + p + shift] = newIndex[N[i][p]];
            }

            if (shift == 0) {
                w[m * (k + 1) + k] = 1.0;
                rowIndex[m * (k + 1) + k] = m;
            }

            m++;
        }

        // This is actually the transpose of W in the paper.
        SparseMatrix W = new SparseMatrix(n, n, w, rowIndex, colIndex);
        SparseMatrix Wt = W.transpose();
        SparseMatrix M = SparseMatrix.AAT(W, Wt);

        EigenValueDecomposition2 eigen = EigenValueDecomposition2.decompose(M, n);

        coordinates = new double[n][d];
        for (int j = 0; j < d; j++) {
            for (int i = 0; i < n; i++) {
                coordinates[i][j] = eigen.getEigenVectors()[i][n-j-2];
            }
        }
    }

    /**
     * Returns the original sample index. Because LLE is applied to the largest
     * connected component of k-nearest neighbor graph, we record the the original
     * indices of samples in the largest component.
     */
    public int[] getIndex() {
        return index;
    }

    /**
     * Returns the coordinates of projected data.
     */
    public double[][] getCoordinates() {
        return coordinates;
    }

    /**
     * Returns the nearest neighbor graph.
     */
    public Graph getNearestNeighborGraph() {
        return graph;
    }
}
