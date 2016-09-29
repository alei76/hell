package ps.hell.ml.dist.base;

import ps.hell.ml.dist.base.inter.SimilaryFactory;


public class MathchHard implements SimilaryFactory{
	

	/**
	 * 返回的为match的概率比
	 * a/all
	 */
	@Override
	public double getSimilary(double[] userNode1, double[] userNode2,
			double[] weight) {
		double fval=0f;
		if(weight==null)
		{
			for(int i=0;i<userNode1.length;i++)
			{
				fval+=1.0/(Math.abs(userNode1[i]-userNode2[i])+1);
			}
		}else{
			double fWeight=0f;
			for(int i=0;i<userNode1.length;i++)
			{
				fval+=1.0/(Math.abs(userNode1[i]-userNode2[i])+1)*weight[i];
				fWeight+=weight[i];
			}
			return fval/fWeight;
		}
		return fval/userNode1.length;
	}

	@Override
	public double getSimilary(int[] userNode1, int[] userNode2, double[] weight) {
		// TODO Auto-generated method stub
		double fval=0f;
		if(weight==null)
		{
			for(int i=0;i<userNode1.length;i++)
			{
				fval+=1.0/(Math.abs(userNode1[i]-userNode2[i])+1);
			}
		}else{
			double fWeight=0f;
			for(int i=0;i<userNode1.length;i++)
			{
				fval+=1.0/(Math.abs(userNode1[i]-userNode2[i])+1)*weight[i];
				fWeight+=weight[i];
			}
			return fval/fWeight;
		}
		return fval/userNode1.length;
	}

	@Override
	public double getSimilary(String s1, String s2) {
		// TODO Auto-generated method stub
		return 0;
	}

}
