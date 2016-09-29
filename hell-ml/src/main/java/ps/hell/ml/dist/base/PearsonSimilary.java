package ps.hell.ml.dist.base;

import ps.hell.ml.dist.base.inter.SimilaryFactory;
import ps.hell.math.base.MathBase;


/**
 * fenzi = sum(X .* Y) - (sum(X) * sum(Y)) / length(X);  
 * fenmu = sqrt((sum(X .^2) - sum(X)^2 / length(X)) * (sum(Y .^2) - sum(Y)^2 / length(X)));  
 * coeff = fenzi / fenmu;  
 * pearson 相关系数
 * @author Administrator
 *
 */
public class PearsonSimilary implements SimilaryFactory{
	/**
	 * 不定长处理方法
	 * @param xy_sum
	 * @param x_sum
	 * @param y_sum
	 * @param size1
	 * @param size2
	 * @param same_len
	 * @param x_2_sum
	 * @param y_2_sum
	 * @return
	 */
	public double compute(double xy_sum,double x_sum,double y_sum,int size1,int size2,int same_len,double x_2_sum,double y_2_sum)
	{
		double nzi=xy_sum-(x_sum+y_sum)/(size1+size2-same_len);
		//System.out.println(x_2_sum+"\t"+Math.pow(x_sum,2f)/item1.size());
		//System.out.println(y_2_sum+"\t"+Math.pow(y_sum,2f)/item2.size());
		double fenmu=(double) Math.sqrt((x_2_sum-Math.pow(x_sum,2f)/size1)*(y_2_sum-Math.pow(y_sum,2f)/size2));
		return nzi/fenmu;
	}

	@Override
	public double getSimilary(double[] userNode1, double[] userNode2,
			double[] weight) {
		// TODO Auto-generated method stub
		double xy_sum=0f;
		double x_sum=0f;
		double x_2_sum=0f;
		double y_sum=0f;
		double y_2_sum=0f;
		int same_len=0;
		for(int i=0;i<userNode1.length;i++)
		{
			xy_sum+=userNode1[i]*userNode2[i];
			x_sum+=userNode1[i];
			x_2_sum+=MathBase.pow(userNode1[i],2);
			y_sum+=userNode2[i];
			y_2_sum+=MathBase.pow(userNode2[i],2);
		}
		return compute(xy_sum,x_sum,y_sum,userNode1.length,userNode2.length,same_len,x_2_sum,y_2_sum);
	}

	@Override
	public double getSimilary(int[] userNode1, int[] userNode2, double[] weight) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getSimilary(String s1, String s2) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
