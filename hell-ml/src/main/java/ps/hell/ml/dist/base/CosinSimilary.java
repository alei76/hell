package ps.hell.ml.dist.base;

import ps.hell.ml.dist.base.inter.SimilaryFactory;
import ps.hell.math.base.MathBase;


/**
 * 余弦相似度
 * @author Administrator
 *
 */
public class CosinSimilary implements SimilaryFactory{

	@Override
	public double getSimilary(double[] userNode1, double[] userNode2,
			double[] weight) {
		double upValue=0f;
		double down1=0f;
		double down2=0f;
		if(weight==null)
		{
			for(int i=0;i<userNode1.length;i++)
			{
				upValue+=userNode1[i]*userNode2[i];
				down1+=MathBase.pow(userNode1[i],2);
				down2+=MathBase.pow(userNode2[i],2);
			}
		}else{
			for(int i=0;i<userNode1.length;i++)
			{
				upValue+=userNode1[i]*userNode2[i]*weight[i];
				down1+=MathBase.pow(userNode1[i],2)*weight[i];
				down2+=MathBase.pow(userNode2[i],2)*weight[i];
			}
		}
		double down=MathBase.sqrt(down1)*MathBase.sqrt(down2);
		if(down==0f)
		{
			return 0f;
		}
		return upValue/(down);
	}

	@Override
	public double getSimilary(int[] userNode1, int[] userNode2, double[] weight) {
		// TODO Auto-generated method stub
		double upValue=0f;
		double down1=0f;
		double down2=0f;
		if(weight==null)
		{
			for(int i=0;i<userNode1.length;i++)
			{
				upValue+=userNode1[i]*userNode2[i];
				down1+=MathBase.pow(userNode1[i],2);
				down2+=MathBase.pow(userNode2[i],2);
			}
		}else{
			for(int i=0;i<userNode1.length;i++)
			{
				upValue+=userNode1[i]*userNode2[i]*weight[i];
				down1+=MathBase.pow(userNode1[i],2)*weight[i];
				down2+=MathBase.pow(userNode2[i],2)*weight[i];
			}
		}
		double down=MathBase.sqrt(down1)*MathBase.sqrt(down2);
		if(down==0f)
		{
			return 0f;
		}
		return upValue/(down);
	}

	@Override
	public double getSimilary(String s1, String s2) {
		// TODO Auto-generated method stub
		return 0;
	}

	

}