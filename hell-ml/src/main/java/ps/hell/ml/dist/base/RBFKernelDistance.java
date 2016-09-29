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
 * rbf核距离
 * exp(gama*(2*dot(x,y)-dot(x,x)-dot(y,y)))
 * @author Administrator
 *
 */
public class RBFKernelDistance implements SimilaryFactory{
    /**
     * 伽马值
     */
    double gama=0.01d;
    /**
     * 伽马系数值
     * @param gama
     */
    public RBFKernelDistance(double gama){
    	this.gama=gama;
    }
    public RBFKernelDistance(){
    	
    }
    /**
     * 获取距离
     * @param x
     * @param y
     * @return
     */
    public double dist(double[] x,double[] y) {

        return 1 - new RBFKernel(gama).dist(x, y);
    }

    public class RBFKernel {
    	/**
    	 * gama参数
    	 */
        private double gamma = 0.01d;

        public RBFKernel() {
            this(0.01d);
        }

        /**
         * Create a new RBF kernel with gamma as a parameter
         * 
         * @param gamma
         */
        public RBFKernel(double gamma) {
            this.gamma = gamma;
        }

        /**
         * Calculates a dot product between two instances
         * 
         * @param x
         *            the first instance
         * @param y
         *            the second instance
         * @return the dot product of the two instances.
         */
        private final double dotProduct(double[] x,double[] y) {
            double result = 0;
            for (int i = 0; i < x.length; i++) {
                result += x[i] * y[i];
            }
            return result;
        }

        /**
         * XXX DOC
         */
        public double dist(double[] x, double[] y) {
            if (x.equals(y))
                return 1.0f;
            double result = (double)Math.exp(gamma * (2.0 * dotProduct(x, y) - dotProduct(x, x) - dotProduct(y, y)));
            return result;

        }


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
