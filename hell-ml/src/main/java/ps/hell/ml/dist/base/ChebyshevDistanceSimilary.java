package ps.hell.ml.dist.base;

import ps.hell.ml.dist.base.inter.SimilaryFactory;


/**
 * 切比雪夫距离
 * max(|xi-yi|)
 * @author Administrator
 *
 */
public class ChebyshevDistanceSimilary implements SimilaryFactory{

	@Override
	public double getSimilary(double[] userNode1, double[] userNode2,
			double[] weight) {
		// TODO Auto-generated method stub
		double val = Double.MIN_VALUE;
		double valNew=Double.MAX_VALUE;
		if (weight == null) {
			for (int i = 0; i < userNode1.length; i++) {
				valNew += Math.abs(userNode1[i] - userNode2[i]);
				if(val<valNew)
				{
					val=valNew;
				}
			}
		} else {
			for (int i = 0; i < userNode1.length; i++) {
				valNew += Math.abs(userNode1[i] - userNode2[i])*weight[i];;
				if(val<valNew)
				{
					val=valNew;
				}
			}
		}
		return val;
	}

	@Override
	public double getSimilary(int[] userNode1, int[] userNode2, double[] weight) {
		// TODO Auto-generated method stub
		double val = Double.MIN_VALUE;
		double valNew=Double.MAX_VALUE;
		if (weight == null) {
			for (int i = 0; i < userNode1.length; i++) {
				valNew += Math.abs(userNode1[i] - userNode2[i]);
				if(val<valNew)
				{
					val=valNew;
				}
			}
		} else {
			for (int i = 0; i < userNode1.length; i++) {
				valNew += Math.abs(userNode1[i] - userNode2[i])*weight[i];;
				if(val<valNew)
				{
					val=valNew;
				}
			}
		}
		return val;
	}

	@Override
	public double getSimilary(String s1, String s2) {
		// TODO Auto-generated method stub
		return 0;
	}

}
