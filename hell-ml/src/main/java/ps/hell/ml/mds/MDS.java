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
package ps.hell.ml.mds;

import ps.hell.math.base.MathBase;
import ps.hell.math.base.matrix.decomposition.EigenValueDecomposition2;

/**
 * 经典多维标度,也被称为主要坐标
*分析。给定一个矩阵的不同(如两两距离),医学博士
*发现低维空间中的一个点集,well-approximates
*的a .差异我们并不局限于使用欧几里德
*距离度量。然而,当使用欧氏距离MDS
*相当于主成分分析
 * Classical multidimensional scaling, also known as principal coordinates
 * analysis. Given a matrix of dissimilarities (e.g. pairwise distances), MDS
 * finds a set of points in low dimensional space that well-approximates the
 * dissimilarities in A. We are not restricted to using a Euclidean
 * distance metric. However, when Euclidean distances are used MDS is
 * equivalent to PCA.
 *
 * smile.projection.PCA
 * @see SammonMapping
 * 
 * @author Haifeng Li
 */
public class MDS {

    /**
     * Component scores.
     */
    private double[] eigenvalues;
    /**
     * Coordinate matrix.
     */
    private double[][] coordinates;
    /**
     * The proportion of variance contained in each principal component.
     */
    private double[] proportion;

    /**
     * Returns the component scores, ordered from largest to smallest.
     */
    public double[] getEigenValues() {
        return eigenvalues;
    }

    /**
     * Returns the proportion of variance contained in each eigenvectors,
     * ordered from largest to smallest.
     */
    public double[] getProportion() {
        return proportion;
    }

    /**
     * Returns the principal coordinates of projected data.
     */
    public double[][] getCoordinates() {
        return coordinates;
    }

    /**
     * Constructor. Learn the classical multidimensional scaling.
     * Map original data into 2-dimensional Euclidean space.
     * @param proximity the nonnegative proximity matrix of dissimilarities. The
     * diagonal should be zero and all other elements should be positive and
     * symmetric. For pairwise distances matrix, it should be just the plain
     * distance, not squared.
     */
    public MDS(double[][] proximity) {
        this(proximity, 2);
    }

    /**
     * Constructor. Learn the classical multidimensional scaling.
     * @param proximity the nonnegative proximity matrix of dissimilarities. The
     * diagonal should be zero and all other elements should be positive and
     * symmetric. For pairwise distances matrix, it should be just the plain
     * distance, not squared.
     * @param k the dimension of the projection.
     */
    public MDS(double[][] proximity, int k) {
        this(proximity, k, false);
    }

    /**
     * Constructor. Learn the classical multidimensional scaling.
     * @param proximity the nonnegative proximity matrix of dissimilarities. The
     * diagonal should be zero and all other elements should be positive and
     * symmetric. For pairwise distances matrix, it should be just the plain
     * distance, not squared.
     * @param k the dimension of the projection.
     * @param add true to estimate an appropriate constant to be added
     * to all the dissimilarities, apart from the self-dissimilarities, that
     * makes the learning matrix positive semi-definite. The other formulation of
     * the additive constant problem is as follows. If the proximity is
     * measured in an interval scale, where there is no natural origin, then there
     * is not a sympathy of the dissimilarities to the distances in the Euclidean
     * space used to represent the objects. In this case, we can estimate a constant c
     * such that proximity + c may be taken as ratio data, and also possibly
     * to minimize the dimensionality of the Euclidean space required for
     * representing the objects.
     */
    public MDS(double[][] proximity, int k, boolean add) {
        int m = proximity.length;
        int n = proximity[0].length;

        if (m != n) {
            throw new IllegalArgumentException("The proximity matrix is not square.");
        }

        if (k < 1 || k >= n) {
            throw new IllegalArgumentException("Invalid k = " + k);
        }

        double[][] A = new double[n][n];
        double[][] B = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                A[i][j] = -0.5 * MathBase.sqr(proximity[i][j]);
                A[j][i] = A[i][j];
            }
        }

        double[] mean = MathBase.rowMean(A);
        double mu = MathBase.mean(mean);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= i; j++) {
                B[i][j] = A[i][j] - mean[i] - mean[j] + mu;
                B[j][i] = B[i][j];
            }
        }

        if (add) {
            double[][] Z = new double[2 * n][2 * n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    Z[i][n + j] = 2 * B[i][j];
                }
            }

            for (int i = 0; i < n; i++) {
                Z[n + i][i] = -1;
            }

            mean = MathBase.rowMean(proximity);
            mu = MathBase.mean(mean);
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    Z[n + i][n + j] = 2 * (proximity[i][j] - mean[i] - mean[j] + mu);
                }
            }

            EigenValueDecomposition2 eigen = MathBase.eigen(Z, false, true);
            double c = MathBase.max(eigen.getEigenValues());

            for (int i = 0; i < n; i++) {
                B[i][i] = 0.0;
                for (int j = 0; j < i; j++) {
                    B[i][j] = -0.5 * MathBase.sqr(proximity[i][j] + c);
                    B[j][i] = B[i][j];
                }
            }
        }

        EigenValueDecomposition2 eigen = MathBase.eigen(B, k);
        
        coordinates = new double[n][k];
        for (int j = 0; j < k; j++) {
            if (eigen.getEigenValues()[j] < 0) {
                throw new IllegalArgumentException(String.format("Some of the first %d eigenvalues are < 0.", k));
            }

            double scale = Math.sqrt(eigen.getEigenValues()[j]);
            for (int i = 0; i < n; i++) {
                coordinates[i][j] = eigen.getEigenVectors()[i][j] * scale;
            }
        }

        eigenvalues = eigen.getEigenValues();
        proportion = eigenvalues.clone();
        MathBase.unitize1(proportion);
    }
}
