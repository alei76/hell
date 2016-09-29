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
 * 交叉验证是一个技术评估的结果如何
*统计分析将推广到一个独立的数据集。
*主要用于设置的目标是预测,和一个
*想要估算出准确的预测模型将执行
*实践。一轮交叉验证包括分区一个示例
*数据子集互补,一个子集上执行分析
*(称为训练集),和验证分析另一个子集
*(被称为验证集或测试集)。为了减少可变性,多个
*轮交叉验证执行使用不同的分区,和
*验证结果平均轮
 * Cross-validation is a technique for assessing how the results of a
 * statistical analysis will generalize to an independent data set.
 * It is mainly used in settings where the goal is prediction, and one
 * wants to estimate how accurately a predictive model will perform in
 * practice. One round of cross-validation involves partitioning a sample
 * of data into complementary subsets, performing the analysis on one subset
 * (called the training set), and validating the analysis on the other subset
 * (called the validation set or testing set). To reduce variability, multiple
 * rounds of cross-validation are performed using different partitions, and the
 * validation results are averaged over the rounds.
 *
 * @author Haifeng Li
 */
public class CrossValidation {
    /**
     * The number of rounds of cross validation.
     */
    public final int k;
    /**
     * The index of training instances.
     */
    public final int[][] train;
    /**
     * The index of testing instances.
     */
    public final int[][] test;

    /**
     * Constructor.
     * @param n the number of samples.
     * @param k the number of rounds of cross validation.
     */
    public CrossValidation(int n, int k) {
        if (n < 0) {
            throw new IllegalArgumentException("Invalid sample size: " + n);
        }

        if (k < 0 || k > n) {
            throw new IllegalArgumentException("Invalid number of CV rounds: " + k);
        }

        this.k = k;
        //获取1-n的样本 并打乱顺序
        int[] index = MathBase.permutate(n);

        train = new int[k][];
        test = new int[k][];

        int chunk = n / k;
        for (int i = 0; i < k; i++) {
            int start = chunk * i;
            int end = chunk * (i + 1);
            if (i == k-1) end = n;

            train[i] = new int[n - end + start];
            test[i] = new int[end - start];
            for (int j = 0, p = 0, q = 0; j < n; j++) {
                if (j >= start && j < end) {
                    test[i][p++] = index[j];
                } else {
                    train[i][q++] = index[j];
                }
            }
        }
    }
}
