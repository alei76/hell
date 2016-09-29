package ps.hell.ml.sequenceTime;

/**
 * ar 自回归
 * p 自回归项数
 * ma 滑动平均
 * q 滑动平均项数
 * d 差分数
 * @author Administrator
 *
 */
public class Arima {
	/**
	 * 数据
	 */
	public double[] data;
	/**
	 * 时间格式为 20140221
	 */
	public int[] time;
	/**
	 * 
	 * @param data 输入数据
	 * @param time 时间格式为 20140221
	 */
	public Arima(double[] data,int[] time)
	{
		this.data=data.clone();
		this.time=time.clone();
	}
	/**
	 * arima执行过程
	 * @param p 自回归项数
	 * @param d 差分数
	 * @param q 滑动平均项数
	 */
	public void compute(int p,int d,int q)
	{
		
	}
	/**
	 * 向后预测阶数
	 * @param count
	 */
	public void forecast(int count)
	{
		
	}
}
