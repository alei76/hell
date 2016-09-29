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


/**
 * 
 * 斯皮尔曼等级相关
 * Calculates the Spearman rank correlation of two instances. The value on
 * position 0 of the instance should be the rank of attribute 0. And so on and so forth.
 * {@jmlSource}
 * 
 * 
 * @version 0.1.4
 * 
 * @linkplain http://en.wikipedia.org/wiki/Spearman's_rank_correlation_coefficient
 * 
 * @author Thomas Abeel
 * 
 */
public class SpearmanRankCorrelation implements SimilaryFactory{

    public double dist(double[] a,double[] b) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Instances should be compatible.");
        long k = a.length;
        long denom = k * (k * k - 1);
        double sum = 0.0f;
        for (int i = 0; i < a.length; i++) {
            double diff = (a[i] - b[i]);
            sum += (diff * diff);
        }
        return 1.0f - (6.0f * (sum / ((double) denom)));
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
