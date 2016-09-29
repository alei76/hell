package ps.hell.ml.statistics.base;

/**
 * 分布的 数据获取统一获取接口
 * @author Administrator
 *
 */
public interface DistributionInter {

	/**
	 * 随机获取一个值
	 * @return
	 */
	public double getRandom();
	/**
	 * 获取一组随机数
	 * @param count
	 * @return
	 */
	public double[] getRandom(int count);
}
