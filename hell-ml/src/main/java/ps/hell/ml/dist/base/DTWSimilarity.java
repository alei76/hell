/**
 * This file is part of the Java Machine Learning Library
 * 
 * The Java Machine Learning Library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * The Java Machine Learning Library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with the Java Machine Learning Library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 * Copyright (c) 2006-2009, Thomas Abeel
 * 
 * Project: http://java-ml.sourceforge.net/
 * 
 */
package ps.hell.ml.dist.base;

import ps.hell.ml.dist.base.inter.SimilaryFactory;
import ps.hell.math.base.MathBase;

/**
 * 将时间规整与距离测度结合起来，采用动态规划技术，比较两个大小不同的模式，解决语音识别中语速多变的难题； 
 * DTW是用于与说话人有关（Speaker Dependent）的语音识别，使用者自行录音然后再以自己的声音來比对之前录好的语音资料。
 * 此方法比較适合同一位说话人的声音來进行比較，因此应用范围比较狭隘，譬如目前手机 Name Dialing 等等。
 * DTW的问题：
 * 运算量大；
 * 识别性能过分依赖于端点检测；
 * 太依赖于说话人的原来发音；
 * 不能对样本作动态训练；
 * 没有充分利用语音信号的时序动态特性；
 * DTW适合于特定人基元较小的场合，多用于孤立词识别；
 * A similarity measure based on "Dynamic Time Warping". The DTW distance is
 * mapped to a similarity measure using f(x)= 1 - (x / (1 + x)). Feature weights
 * are also supported.
 * 
 * @author Piotr Kasprzak
 * @author Thomas Abeel
 * 
 */
public class DTWSimilarity implements SimilaryFactory{


    private double pointDistance(int i, int j, double[] ts1, double[] ts2) {
    	double diff = ts1[i] - ts2[j];
        return (diff * diff);
    }

    private double distance2Similarity(double x) {
        return (1.0f - (x / (1 + x)));
    }

    public double dist(double[] l1,double[] l2) {

        int i, j;

        /** Transform the examples to vectors */
        double[] ts1 = l1.clone();
        double[] ts2 = l2.clone();


        /** Build a point-to-point distance matrix */
        double[][] dP2P = new double[ts1.length][ts2.length];
        for (i = 0; i < ts1.length; i++) {
            for (j = 0; j < ts2.length; j++) {
                dP2P[i][j] = pointDistance(i, j, ts1, ts2);
            }
        }

        /** Check for some special cases due to ultra short time series */
        if (ts1.length == 0 || ts2.length == 0) {
            return Double.NaN;
        }

        if (ts1.length == 1 && ts2.length == 1) {
            return distance2Similarity(MathBase.sqrt(dP2P[0][0]));
        }

        /**
         * Build the optimal distance matrix using a dynamic programming
         * approach
         */
        double[][] D = new double[ts1.length][ts2.length];

        D[0][0] = dP2P[0][0]; // Starting point

        for (i = 1; i < ts1.length; i++) { // Fill the first column of our
            // distance matrix with optimal
            // values
            D[i][0] = dP2P[i][0] + D[i - 1][0];
        }

        if (ts2.length == 1) { // TS2 is a point
            double sum = 0;
            for (i = 0; i < ts1.length; i++) {
                sum += D[i][0];
            }
            return distance2Similarity(MathBase.sqrt(sum) / ts1.length);
        }

        for (j = 1; j < ts2.length; j++) { // Fill the first row of our
            // distance matrix with optimal
            // values
            D[0][j] = dP2P[0][j] + D[0][j - 1];
        }

        if (ts1.length == 1) { // TS1 is a point
            double sum = 0;
            for (j = 0; j < ts2.length; j++) {
                sum += D[0][j];
            }
            return distance2Similarity(MathBase.sqrt(sum) / ts2.length);
        }

        for (i = 1; i < ts1.length; i++) { // Fill the rest
            for (j = 1; j < ts2.length; j++) {
                double[] steps = { D[i - 1][j - 1], D[i - 1][j], D[i][j - 1] };
                double min = Math.min(steps[0], Math.min(steps[1], steps[2]));
                D[i][j] = dP2P[i][j] + min;
            }
        }

        /**
         * Calculate the distance between the two time series through optimal
         * alignment.
         */
        i = ts1.length - 1;
        j = ts2.length - 1;
        int k = 1;
        double dist = D[i][j];

        while (i + j > 2) {
            if (i == 0) {
                j--;
            } else if (j == 0) {
                i--;
            } else {
                double[] steps = { D[i - 1][j - 1], D[i - 1][j], D[i][j - 1] };
                double min = Math.min(steps[0], Math.min(steps[1], steps[2]));

                if (min == steps[0]) {
                    i--;
                    j--;
                } else if (min == steps[1]) {
                    i--;
                } else if (min == steps[2]) {
                    j--;
                }
            }
            k++;
            dist += D[i][j];
        }

        return distance2Similarity(MathBase.sqrt(dist) / k);
    }

	@Override
	public double getSimilary(double[] userNode1, double[] userNode2,
			double[] weight) {
		// TODO Auto-generated method stub
		return dist(userNode1,userNode2);
	}

	@Override
	public double getSimilary(int[] userNode1, int[] userNode2, double[] weight) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getSimilary(String s1, String s2) {
		// TODO Auto-generated method stub
		return 0;
	}

}
