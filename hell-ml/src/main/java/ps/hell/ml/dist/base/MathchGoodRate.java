package ps.hell.ml.dist.base;

import ps.hell.ml.dist.base.inter.SimilaryFactory;



public class MathchGoodRate implements SimilaryFactory{
	@Override
	public double getSimilary(double[] userNode1, double[] userNode2,
			double[] weight) {
		// TODO Auto-generated method stub
		return 0;
	}
	/**
	 * good/(bad)*good/all
	 */
	@Override
	public double getSimilary(int[] userNode1, int[] userNode2, double[] weight) {
		// TODO Auto-generated method stub
		double fval=0f;
		double fweight=1f;
		if(weight==null)
		{
			for(int i=0;i<userNode1.length;i++)
			{
				if(userNode1[i]==userNode2[i])
					fval++;
			}
		}else{
			fweight=0f;
			for(int i=0;i<userNode1.length;i++)
			{
				if(userNode1[i]==userNode2[i])
					fval++;
				fweight+=weight[i];
			}
		}
		if(fval==fweight)
		{
			return fweight;
		}
		return fval/(fweight-fval)*fval/(fweight);
	}
	@Override
	public double getSimilary(String s1, String s2) {
		// TODO Auto-generated method stub
		return 0;
	}

}
