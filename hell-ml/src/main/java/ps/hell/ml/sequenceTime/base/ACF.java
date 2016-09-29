package ps.hell.ml.sequenceTime.base;

import ps.hell.math.base.MathBase;

/**
 * 自相关
 * @author Administrator
 *
 */
public class ACF {

	/**
	 * 不知是否平稳的方法
	 * @param data 数据
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
		//计算自相关系数
		double relevance=e/Math.sqrt(sd1*sd2);
		return relevance;
	}
	/**
	 *平稳的计算方法
	 * @param data 数据
	 * @param lag 滞后数
	 * @return
	 */
	public static double compute2(double[] data,int lag)
	{
		//获取期望值（均值）
		double mean=MathBase.mean(data);
		//获取方差
		double sd=MathBase.var(data);
		//计算 E((xi-u)(x(i-k)-u)) 为 cov(xi,x(i-k))
		double e=0.0;
		for(int i=0;i<data.length-lag;i++)
		{
			e+=(data[i]-mean)*(data[i+lag]-mean);
		}
		e/=data.length-lag;
		//计算自相关系数
		double relevance=e/sd;
		return relevance;
	}
	
}
