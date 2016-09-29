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

package ps.hell.ml.statistics.validation;

import ps.hell.math.base.MathBase;


/**
 * 兰德指数。兰德指数被定义为对对象的数量
*在同一组或不同组织在两个分区
*除以总数量的成对的对象。兰德指数之间的谎言
* 0和1。当两个分区完全达成一致,兰德指数达到了
*最大值1。兰德的问题指标的期望值
*兰德指数两个随机分区之间并不是一个常数。这个问题
*由调整纠正兰德假定广义指数
*超几何分布的模型随机性。调整兰特
*指数最大值1,其预期值为0
*随机集群。调整兰德指数越大,意味着更高的协议
*两个分区。建议调整兰特指数来衡量
*协议即使在分区相比有不同数量的集群
 * Rand Index. Rand index is defined as the number of pairs of objects
 * that are either in the same group or in different groups in both partitions
 * divided by the total number of pairs of objects. The Rand index lies between
 * 0 and 1. When two partitions agree perfectly, the Rand index achieves the
 * maximum value 1. A problem with Rand index is that the expected value of
 * the Rand index between two random partitions is not a constant. This problem
 * is corrected by the adjusted Rand index that assumes the generalized
 * hyper-geometric distribution as the model of randomness. The adjusted Rand
 * index has the maximum value 1, and its expected value is 0 in the case
 * of random clusters. A larger adjusted Rand index means a higher agreement
 * between two partitions. The adjusted Rand index is recommended for measuring
 * agreement even when the partitions compared have different numbers of clusters.
 *
 * @author Haifeng Li
 */
public class RandIndex implements ClusterMeasure {

    @Override
    public double measure(int[] y1, int[] y2) {
        if (y1.length != y2.length) {
            throw new IllegalArgumentException(String.format("The vector sizes don't match: %d != %d.", y1.length, y2.length));
        }

        // Get # of non-zero classes in each solution
        int n = y1.length;

        int[] label1 = MathBase.unique(y1);
        int n1 = label1.length;
        
        int[] label2 = MathBase.unique(y2);
        int n2 = label2.length;

        // Calculate N contingency matrix
        int[][] count = new int[n1][n2];
        for (int i = 0; i < n1; i++) {
            for (int j = 0; j < n2; j++) {
                int match = 0;

                for (int k = 0; k < n; k++) {
                    if (y1[k] == label1[i] && y2[k] == label2[j]) {
                        match++;
                    }
                }

                count[i][j] = match;
            }
        }

        // Marginals
        int[] count1 = new int[n1];
        int[] count2 = new int[n2];

        for (int i = 0; i < n1; i++) {
            for (int j = 0; j < n2; j++) {
                count1[i] += count[i][j];
                count2[j] += count[i][j];
            }
        }

        // Calculate RAND - Non-adjusted
        double rand_T = 0.0;
        for (int i = 0; i < n1; i++) {
            for (int j = 0; j < n2; j++) {
                rand_T += MathBase.sqr(count[i][j]);
            }
        }
        rand_T -= n;

        double rand_P = 0.0;
        for (int i = 0; i < n1; i++) {
            rand_P += MathBase.sqr(count1[i]);
        }
        rand_P -= n;

        double rand_Q = 0.0;
        for (int j = 0; j < n2; j++) {
            rand_Q += MathBase.sqr(count2[j]);
        }
        rand_Q -= n;

        double rand = (rand_T - 0.5 * rand_P - 0.5 * rand_Q + MathBase.choose(n, 2)) / MathBase.choose(n, 2);
        return rand;
    }

    @Override
    public String toString() {
        return "Rand Index";
    }
}
