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
package ps.hell.ml.statistics.distibution;

import ps.hell.math.base.MathBase;
import ps.hell.ml.statistics.base.special.Beta;
import ps.hell.ml.statistics.base.special.Gamma;


/**
 * 负二项分布是统计学上一种离散概率分布。满足以下条件的称为负二项分布：实验包含一系列独立的实验， 每个实验都有成功、失败两种结果，成功的概率是恒定的，实验持续到r次成功，r为正整数
 * 负二项分布的概率分布
*成功的数量在一系列的独立同分布
*伯努利试验需要指定(随机)r失败的数量。
*如果r是一个整数,它通常被称为帕斯卡分布。否则,它
*通常被称为玻利亚分布律实值的情况。我们当r = 1
*前得到许多成功的概率分布
*第一次失败,这是一个几何分布
 * Negative binomial distribution arises as the probability distribution of
 * the number of successes in a series of independent and identically distributed
 * Bernoulli trials needed to get a specified (non-random) number r of failures.
 * If r is an integer, it is usually called Pascal distribution. Otherwise, it
 * is often called Polya distribution for the real-valued case. When r = 1 we
 * get the probability distribution of number of successes before the
 * first failure, which is a geometric distribution.
 * <p>
 * An alternative definition is that X is the total number of trials needed
 * to get r failures, not simply the number of successes. This alternative
 * parameterization can be used as an alternative to the Poisson distribution.
 * It is especially useful for discrete data over an unbounded positive range
 * whose sample variance exceeds the sample mean. If a Poisson distribution is
 * used to model such data, the model mean and variance are equal. In that case,
 * the observations are overdispersed with respect to the Poisson model.
 * Since the negative binomial distribution has one more parameter than the
 * Poisson, the second parameter can be used to adjust the variance
 * independently of the mean. In the case of modest overdispersion, this may
 * produce substantially similar results to an overdispersed Poisson distribution.
 * <p>
 * The negative binomial distribution also arises as a continuous mixture
 * of Poisson distributions where the mixing distribution of the Poisson rate
 * is a gamma distribution. That is, we can view the negative binomial as a
 * Poisson(&lambda;) distribution, where &lambda; is itself a random variable,
 * distributed according to &Gamma;(r, p/(1 - p)).
 *
 * @author Haifeng Li
 */
public class NegativeBinomialDistribution extends DiscreteDistribution {
    /**
     * The number of failures until the experiment is stopped.
     */
    private double r;
    /**
     * Success probability in each experiment.
     */
    private double p;

    /**
     * Constructor.
     * @param r the number of failures until the experiment is stopped.
     * @param p success probability in each experiment.
     */
    public NegativeBinomialDistribution(double r, double p) {
        if (p <= 0 || p >= 1) {
            throw new IllegalArgumentException("Invalid p: " + p);
        }

        if (r <= 0) {
            throw new IllegalArgumentException("Invalid r: " + r);
        }

        this.p = p;
        this.r = r;
    }

    @Override
    public int npara() {
        return 2;
    }

    @Override
    public double mean() {
        return r * (1 - p) / p;
    }

    @Override
    public double var() {
        return r * (1 - p) / (p * p);
    }

    @Override
    public double sd() {
        return Math.sqrt(r * (1 - p)) / p;
    }

    /**
     * Shannon entropy. Not supported.
     */
    @Override
    public double entropy() {
        throw new UnsupportedOperationException("Negative Binomial distribution does not support entropy()");
    }

    @Override
    public String toString() {
        if (r == (int) r) {
            return String.format("Negative Binomial(%d, %.4f)", r, p);
        } else {
            return String.format("Negative Binomial(%.4f, %.4f)", r, p);
        }
    }

    @Override
    public double rand() {
        return inverseTransformSampling();
    }

    @Override
    public double p(int k) {
        if (k < 0) {
            return 0.0;
        } else {
            return Gamma.gamma(r + k) / (MathBase.factorial(k) * Gamma.gamma(r)) * Math.pow(p, r) * Math.pow(1 - p, k);
        }
    }

    @Override
    public double logp(int k) {
        if (k < 0) {
            return Double.NEGATIVE_INFINITY;
        } else {
            return Gamma.logGamma(r + k) - MathBase.logFactorial(k) - Gamma.logGamma(r) + r * Math.log(p) + k * Math.log(1 - p);
        }
    }

    @Override
    public double cdf(double k) {
        if (k < 0) {
            return 0.0;
        } else {
            return Beta.regularizedIncompleteBetaFunction(r, k + 1, p);
        }
    }

    @Override
    public double quantile(double p) {
        if (p < 0.0 || p > 1.0) {
            throw new IllegalArgumentException("Invalid p: " + p);
        }

        // Starting guess near peak of density.
        // Expand interval until we bracket.
        int kl, ku, inc = 1;
        int k = (int) mean();
        if (p < cdf(k)) {
            do {
                k = Math.max(k - inc, 0);
                inc *= 2;
            } while (p < cdf(k) && k > 0);
            kl = k;
            ku = k + inc / 2;
        } else {
            do {
                k += inc;
                inc *= 2;
            } while (p > cdf(k));
            ku = k;
            kl = k - inc / 2;
        }

        return quantile(p, kl, ku);
    }
}
