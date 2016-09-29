package ps.hell.ml.dist.base;

import ps.hell.ml.dist.base.inter.SimilaryFactory;


/**
 * sum(|xi-yi|) pearson 曼哈顿距离
 * 
 * @author Administrator
 *
 */
public class CityBlockSimilary implements SimilaryFactory {

	@Override
	public double getSimilary(double[] userNode1, double[] userNode2,
			double[] weight) {
		double result = 0f;
		if (weight == null) {
			for (int i = 0; i < userNode1.length; i++) {
				result += Math.abs(userNode1[i] - userNode2[i]);
			}
		} else {
			for (int i = 0; i < userNode1.length; i++) {
				result += Math.abs(userNode1[i] - userNode2[i])*weight[i];
			}
		}
		return result;
	}

	@Override
	public double getSimilary(int[] userNode1, int[] userNode2, double[] weight) {
		// TODO Auto-generated method stub
		double result = 0f;
		if (weight == null) {
			for (int i = 0; i < userNode1.length; i++) {
				result += Math.abs(userNode1[i] - userNode2[i]);
			}
		} else {
			for (int i = 0; i < userNode1.length; i++) {
				result += Math.abs(userNode1[i] - userNode2[i])*weight[i];
			}
		}
		return result;
	}

	@Override
	public double getSimilary(String s1, String s2) {
		// TODO Auto-generated method stub
		return 0;
	}

}
