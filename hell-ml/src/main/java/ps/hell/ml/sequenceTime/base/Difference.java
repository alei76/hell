package ps.hell.ml.sequenceTime.base;

/**
 * 差分
 * @author Administrator
 *
 */
public class Difference {

	
	public Difference()
	{
		
	}
	/**
	 * 
	 * @param data 数据
	 * @param n 差分数量 n需要>0
	 * @return 差分为 f'(t)= f(t+n)-f(t) 
	 *  返回长度 比data长度小n
	 */
	public static double[] compute(double[] data,int n)
	{
		if(n<0)
		{
			return null;
		}
		double[] out=new double[data.length-n];
		
		for(int i=0;i<data.length-n;i++)
		{
			out[i]=data[i+n]-data[i];
		}
		return out;
	}
}
