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
 * 斯皮尔曼简捷相关 
 * TODO WRITE DOC
 * @author Thomas Abeel
 *
 */
public class SpearmanFootruleDistance implements SimilaryFactory{


    public double dist(double[] a,double[] b) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Instances should be compatible.");
        long k = a.length;
        long denom;
        if(k%2==0)
            denom=(k*k)/2;
        else
            denom=((k+1)*(k-1))/2;
        double sum = 0.0f;
        for (int i = 0; i < a.length; i++) {
            double diff = Math.abs(a[i] - b[i]);
            sum += diff;
        }
        return 1.0f - (sum / ((double) denom));
    }

    public double getMaximumDistance(double[] data) {
        return 1;
    }

    public double getMinimumDistance(double[] data) {
        return 0;
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
