package ps.hell.ml.sequenceTime.base;

import ps.hell.math.base.MathBase;


/**
 * 偏自相关
 * @author Administrator
 *
 */
public class PACF {

	/**
	 * {E[(x(t)-Ex(t)][x(t-k)-Ex(t-k)]}/E{[x(t-k)-Ex(t-k)]^2}
	 * @param data
	 * @param lag 滞后数
	 * @return
	 */
	public static double compute(double[] data,int lag)
	{
		double[] data1=new double[data.length-lag];
		double[] data2=new double[data.length-lag];
		for(int i=0;i<data.length-lag;i++)
		{
			data1[i]=data[i];
			data2[i]=data[i+lag];
		}
		//获取期望值（均值）
		double mean1=MathBase.mean(data1);
		//获取方差
		double sd1=MathBase.var(data1);
		//获取期望值（均值）
		double mean2=MathBase.mean(data2);
		//获取方差
		double sd2=MathBase.var(data2);
		//计算 E((xi-u)(x(i-k)-u)) 为 cov(xi,x(i-k))//自协方差 
		double e=0.0;
		for(int i=0;i<data.length-lag;i++)
		{
			e+=(data[i]-mean1)*(data[i+lag]-mean2);
		}
		e/=data.length-lag;
		//E{[x(t-k)-Ex(t-k)]^2}
		double ze=0.0;
		for(int i=0;i<data1.length;i++)
		{
			ze+=Math.pow(data[i+lag]-mean2,2.0);
		}
		ze/=data.length-lag;
		//返回值
		return e/ze;
		
	}
}
