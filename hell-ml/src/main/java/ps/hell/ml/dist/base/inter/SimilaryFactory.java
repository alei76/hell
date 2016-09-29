package ps.hell.ml.dist.base.inter;

import java.util.HashMap;

/**
 * 通用接口
 * @author Administrator
 *
 */
public interface SimilaryFactory {

	/**
	 * 相似度公用方法数据集
	 */
	 HashMap<Long,Double> simiMatrix=new HashMap<Long,Double>();
	/**
	 * 是否使用内存相似度
	 */
	 boolean useSimiMatrix=true;
	
	/**
	 * 获取相似性
	 * @param userNode1
	 * @param userNode2
	 * @param weight 对应的权重类型
	 * @return
	 */
	public double getSimilary(double[] userNode1, double[] userNode2, double[] weight);
	/**
	 * 获取相似性
	 * @param userNode1
	 * @param userNode2
	 * @param weight 对应的权重类型
	 * @return
	 */
	public double getSimilary(int[] userNode1, int[] userNode2, double[] weight);
	/**
	 * 获取文本间得距离
	 * @param s1
	 * @param s2
	 * @return
	 */
	public double getSimilary(String s1, String s2);
	
}
