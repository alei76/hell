package ps.hell.ml.dist.base;

import ps.hell.ml.dist.base.inter.SimilaryFactory;
import ps.hell.ml.revaluate.EntropyUtil;


/**
 * 信息熵 输入的为对应的分类的概率
 * @author Administrator
 * 使用相对熵作为统计量
 *
 */
public class Informationentropy implements SimilaryFactory{

	EntropyUtil comentropyUtil=new EntropyUtil();
	/**
	 * 输入为userNode1 为信息熵差异得概率值
	 */
	@Override
	public double getSimilary(double[] userNode1, double[] userNode2,
			double[] weight) {
		// TODO Auto-generated method stub
		return comentropyUtil.kLD(userNode1, userNode2);
	}

	@Override
	public double getSimilary(int[] userNode1, int[] userNode2, double[] weight) {
		// TODO Auto-generated method stub
		//使用其他方法 sum
		double entry=comentropyUtil.entry(userNode1);
		double entry2=comentropyUtil.entry(userNode2);
		return Math.abs(-entry+entry2);
	}

	@Override
	public double getSimilary(String s1, String s2) {
		// TODO Auto-generated method stub
		return 0;
	}

}
