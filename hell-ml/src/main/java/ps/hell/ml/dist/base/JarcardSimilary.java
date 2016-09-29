package ps.hell.ml.dist.base;

import ps.hell.ml.dist.base.inter.SimilaryFactory;



/**
 * jarcard 相关系数
 * 股本相似度
 * @author Administrator
 *
 */
public class JarcardSimilary implements SimilaryFactory{

	/**
	 * 不支持
	 */
	@Override
	public double getSimilary(double[] userNode1, double[] userNode2,
			double[] weight) {
		// TODO Auto-generated method stub
			
		return 0;
	}
	
	@Override
	public double getSimilary(int[] userNode1, int[] userNode2,
			double[] weight) {
		double up=0f;
		double down=0f;
		if(userNode1.length==0)
		{
			return 0;
		}
		if(weight==null)
		{
			down=userNode1.length;
			for(int i=0;i<userNode2.length;i++)
			{
				if(userNode1[i]==userNode2[i])
				{
					up++;
				}
			}
		}else{
			down=0f;
			for(int i=0;i<userNode2.length;i++)
			{
				if(userNode1[i]==userNode2[i])
				{
					up+=weight[i];
				}
				down+=weight[i];
			}
		}
		return up/down;
	}

	@Override
	public double getSimilary(String s1, String s2) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
}
