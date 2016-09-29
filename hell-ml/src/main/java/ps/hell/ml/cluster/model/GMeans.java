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
package ps.hell.ml.cluster.model;

import java.util.ArrayList;
import java.util.Arrays;

import ps.hell.math.base.MathBase;
import ps.hell.ml.statistics.distibution.GaussianDistribution;
import ps.hell.base.sort.QuickSort;

/**
 * G-Means clustering algorithm, an extended K-Means which tries to
 * automatically determine the number of clusters by normality test.
 * The G-means algorithm is based on a statistical test for the hypothesis
 * that a subset of data follows a Gaussian distribution. G-means runs
 * k-means with increasing k in a hierarchical fashion until the test accepts
 * the hypothesis that the data assigned to each k-means center are Gaussian.
 * 
 * <h2>References</h2>
 * <ol>
 * <li>G. Hamerly and C. Elkan. Learning the k in k-means. NIPS, 2003.</li>
 * </ol>
 * 
 * @see KMeans
 * @see XMeans
 * 
 * @author Haifeng Li
 */
public class GMeans extends KMeans {
    
    /**
     * Constructor. Clustering data with the number of clusters being
     * automatically determined by G-Means algorithm.
     * @param data the input data of which each row is a sample.
     * @param kmax the maximum number of clusters.
     */
    public GMeans(double[][] data, int kmax) {
        if (kmax < 2) {
            throw new IllegalArgumentException("Invalid parameter kmax = " + kmax);
        }

        int n = data.length;
        int d = data[0].length;

        k = 1;
        size = new int[k];
        size[0] = n;
        y = new int[n];
        centroids = new double[k][d];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < d; j++) {
                centroids[0][j] += data[i][j];
            }
        }

        for (int j = 0; j < d; j++) {
            centroids[0][j] /= n;
        }

        distortion = 0.0;
        for (int i = 0; i < n; i++) {
            distortion += MathBase.squaredDistance(data[i], centroids[0]);
        }
        System.out.format("G-Means distortion with %d clusters: %.5f\n", k, distortion);

        BBDTree bbd = new BBDTree(data);
        while (k < kmax) {
            ArrayList<double[]> centers = new ArrayList<double[]>();
            double[] score = new double[k];
            KMeans[] kmeans = new KMeans[k];
            
            for (int i = 0; i < k; i++) {
                // don't split too small cluster. anyway likelihood estimation
                // not accurate in this case.
                if (size[i] < 25) {
                    System.out.format("Cluster %3d\ttoo small to split: %d samples\n", i, size[i]);
                    continue;
                }
                
                double[][] subset = new double[size[i]][];
                for (int j = 0, l = 0; j < n; j++) {
                    if (y[j] == i) {
                        subset[l++] = data[j];
                    }
                }

                kmeans[i] = new KMeans(subset, 2, 100, 4);
                
                double[] v = new double[d];
                for (int j = 0; j < d; j++) {
                    v[j] = kmeans[i].centroids[0][j] - kmeans[i].centroids[1][j];
                }
                double vp = MathBase.dot(v, v);
                double[] x = new double[size[i]];
                for (int j = 0; j < x.length; j++) {
                    x[j] = MathBase.dot(subset[j], v) / vp;
                }
                
                // normalize to mean 0 and variance 1.
                MathBase.normalize(x);

                score[i] = AndersonDarling(x);
                System.out.format("Cluster %3d\tAnderson-Darling adjusted test statistic: %3.4f\n", i, score[i]);
            }

            int[] index = QuickSort.sort(score);
            for (int i = 0; i < k; i++) {
                if (score[index[i]] <= 1.8692) {
                    centers.add(centroids[index[i]]);
                }
            }
            
            int m = centers.size();
            for (int i = k; --i >= 0;) {
                if (score[i] > 1.8692) {
                    if (centers.size() + i - m + 1 < kmax) {
                        System.out.format("Split cluster %d...\n", index[i]);
                        centers.add(kmeans[index[i]].centroids[0]);
                        centers.add(kmeans[index[i]].centroids[1]);
                    } else {
                        centers.add(centroids[index[i]]);
                    }
                }
            }

            // no more split.
            if (centers.size() == k) {
                break;
            }

            k = centers.size();
            double[][] sums = new double[k][d];
            size = new int[k];
            centroids = new double[k][];
            for (int i = 0; i < k; i++) {
                centroids[i] = centers.get(i);
            }

            distortion = Double.MAX_VALUE;
            for (int iter = 0; iter < 100; iter++) {
                double newDistortion = bbd.clustering(centroids, sums, size, y);
                for (int i = 0; i < k; i++) {
                    if (size[i] > 0) {
                        for (int j = 0; j < d; j++) {
                            centroids[i][j] = sums[i][j] / size[i];
                        }
                    }
                }

                if (distortion <= newDistortion) {
                    break;
                } else {
                    distortion = newDistortion;
                }
            }
            
            System.out.format("G-Means distortion with %d clusters: %.5f\n", k, distortion);
        }
    }
    
    /**
     * Calculates the Anderson-Darling statistic for one-dimensional normality test.
     *
     * @param x the samples to test if drawn from a Gaussian distribution.
     */
    private static double AndersonDarling(double[] x) {
        int n = x.length;
        Arrays.sort(x);

        for (int i = 0; i < n; i++) {
            x[i] = GaussianDistribution.getInstance().cdf(x[i]);
            // in case overflow when taking log later.
            if (x[i] == 0) x[i] = 0.0000001;
            if (x[i] == 1) x[i] = 0.9999999;
        }

        double A = 0.0;
        for (int i = 0; i < n; i++) {
            A -= (2*i+1) * (Math.log(x[i]) + Math.log(1-x[n-i-1]));
        }

        A = A / n - n;
        A *= (1 + 4.0/n - 25.0/(n*n));

        return A;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append(String.format("G-Means distortion: %.5f\n", distortion));
        sb.append(String.format("Clusters of %d data points of dimension %d:\n", y.length, centroids[0].length));
        for (int i = 0; i < k; i++) {
            int r = (int) Math.round(1000.0 * size[i] / y.length);
            sb.append(String.format("%3d\t%5d (%2d.%1d%%)\n", i, size[i], r / 10, r % 10));
        }
        
        return sb.toString();
    }
}