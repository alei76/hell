package ps.hell.ml.dist.base;

import ps.hell.ml.dist.base.inter.SimilaryFactory;


/**
 * 汉明距离
 * 
 * 如果在字符串中则为编辑距离
 * 0 1 
 * 0 0
 * 不同分量得占比 0.5
 * @author Administrator
 *
 */
public class HammingDistanceSimilary implements SimilaryFactory{

	@Override
	public double getSimilary(double[] userNode1, double[] userNode2,
			double[] weight) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getSimilary(int[] userNode1, int[] userNode2, double[] weight) {
		// TODO Auto-generated method stub
		double count=0;
		double length=userNode1.length;
		if(weight==null)
		{
			for(int i=0;i<userNode1.length;i++)
			{
				if(userNode1[i]!=userNode2[i])
				{
					count++;
				}
			}
		}else{
			length=0f;
			for(int i=0;i<userNode1.length;i++)
			{
				length+=weight[i];
				if(userNode1[i]!=userNode2[i])
				{
					count+=weight[i];
				}
			}
			if(length<1E-10)
			{
				return 0f;
			}
		}
		return count/length;
	}

	@Override
	public double getSimilary(String s1, String s2) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	
}
