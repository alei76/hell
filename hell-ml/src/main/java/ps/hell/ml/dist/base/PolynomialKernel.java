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
 * 多项式核距离
 * 点积 的系数次方
 * @author Administrator
 *
 */
public class PolynomialKernel implements SimilaryFactory{

    /**
     * 多项式系数
     */
    private double exponent=1;
    
    public PolynomialKernel(double exponent){
        this.exponent=exponent;
    }
    
    public double dist(double[] i,double[] j) {
        double result;
        result = dotProd(i, j);
        if (exponent != 1.0) {
            result = (double)Math.pow(result, exponent);
        }
        return result;
    }

    /**
     * Calculates a dot product between two instances
     */
    protected final double dotProd(double[] inst1,double[] inst2) {

        double result = 0;

        for (int i = 0; i < inst1.length; i++) {
            result += inst1[i] * inst2[i];
        }
        return result;    }

    public boolean compare(double x, double y) {
        throw new UnsupportedOperationException("Not implemented");
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
