package ps.hell.ml.dist.base;

import ps.hell.ml.dist.base.inter.SimilaryFactory;
import ps.hell.math.base.MathBase;


/**
 * 闵可夫斯基距离
 * @author Administrator
 *sum((x-y)^k)^(1/k)
 */
public class MinkowskiDistanceSimilary implements SimilaryFactory{

	public int kValue=2;
	@Override
	public double getSimilary(double[] userNode1, double[] userNode2,
			double[] weight) {
		// TODO Auto-generated method stub
		double val = 0f;
		if (weight == null) {
			for (int i = 0; i < userNode1.length; i++) {
				val += MathBase.pow(userNode1[i] - userNode2[i], kValue);
			}
		} else {
			for (int i = 0; i < userNode1.length; i++) {
				val += MathBase.pow(userNode1[i] - userNode2[i], 2)*weight[i];
			}
		}
		return (double) Math.pow(val,1.0/kValue);
	}

	@Override
	public double getSimilary(int[] userNode1, int[] userNode2, double[] weight) {
		// TODO Auto-generated method stub
		double val = 0f;
		if (weight == null) {
			for (int i = 0; i < userNode1.length; i++) {
				val += MathBase.pow(userNode1[i] - userNode2[i], kValue);
			}
		} else {
			for (int i = 0; i < userNode1.length; i++) {
				val += MathBase.pow(userNode1[i] - userNode2[i], 2)*weight[i];
			}
		}
		return (double) Math.pow(val,1.0/kValue);
	}

	@Override
	public double getSimilary(String s1, String s2) {
		// TODO Auto-generated method stub
		return 0;
	}

}
