package ps.hell.ml.dist.base;

import ps.hell.ml.dist.base.inter.SimilaryFactory;



/**
 * 对数似然比
 * 举例：用户1和用户2都浏览的商品k11=2个，用户1还浏览其他k12=5，用户2还浏览其他k21=8,都没有被这2个用户浏览的商品985，那-2l
 * ogλ=10.55 如果存在用户1和用户3，它们k11,k12,k21,k22分别是2,5,18,975，那么-2logλ=7.65
 * 如果存在用户1和用户4，它们k11,k12,k21,k22分别是1,6,1,993，那么-2logλ=10.07
 * 
 * @author Administrator
 *
 */
public class LogLikelihoodRatioSimilarity implements SimilaryFactory {

	/**
	 * 物品总数
	 */
	public int itemSum = -1;

	public int itemSumInit = -1;

	public double logLikelihoodRatio(int k11, int k12, int k21, int k22) {
		double rowEntropy = entropy(k11, k12) + entropy(k21, k22);
		// System.out.println(entropy(k11, k12) +"\t"+entropy(k21, k22));
		double columnEntropy = entropy(k11, k21) + entropy(k12, k22);
		// System.out.println(entropy(k11, k21) +"\t"+entropy(k12, k22));
		double matrixEntropy = entropy(k11, k12, k21, k22);
		return 2 * (matrixEntropy - rowEntropy - columnEntropy);
	}

	public double entropy(int... elements) {
		double sum = 0;
		for (int element : elements) {
			sum += element;
		}
		double result = 0.0;
		for (int x : elements) {
			if (x < 0) {
				throw new IllegalArgumentException(
						"Should not have negative count for entropy computation: ("
								+ x + ')');
			}
			int zeroFlag = (x == 0 ? 1 : 0);
			result += x * Math.log((x + zeroFlag) / sum);
		}
		return -result;
	}

	/**
	 * 权重无效
	 */
	@Override
	public double getSimilary(int[] userNode1, int[] userNode2, double[] weight) {
		int k11 = 0;
		int k12 = 0;
		int k21 = 0;
		int k22 = 0;
		for (int i = 0; i < userNode1.length; i++) {
			if (userNode1[i] * userNode2[i] > 0) {
				k11++;
			} else if (userNode1[i] > 0) {
				k12++;
			} else if (userNode2[i] > 0) {
				k21++;
			} else {
			
			}

		}
		k22=this.itemSum-k11-k12-k21;
		return (double) logLikelihoodRatio(k11, k12, k21, k22);
	}

	@Override
	public double getSimilary(double[] userNode1, double[] userNode2,
			double[] weight) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getSimilary(String s1, String s2) {
		// TODO Auto-generated method stub
		return 0;
	}

}
