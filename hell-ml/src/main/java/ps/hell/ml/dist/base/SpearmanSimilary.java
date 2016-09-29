package ps.hell.ml.dist.base;

import ps.hell.ml.dist.base.inter.SimilaryFactory;
import ps.hell.base.sort.AllSort;

/**
 * 斯皮尔曼相关系数
 * @author Administrator
 *
 */
public class SpearmanSimilary implements SimilaryFactory{
	
	public void getRank(double[] item,int[] rank)
	{
		int i=0;
		for(double item1:item)
		{
			int count1=1;//记录大于特定元素的元素个数
			int count2=-1;//记录与特定元素相同的元素个数  
			for(double item2:item)
			{
				if(item1<item2)
				{
					count1++;
				}else if(item1==item2)
				{
					count2++;
				}
			}
			rank[i]=count1+getMean(count2);
			i++;	
		}
	}

	public int getMean(int count2)
	{
		int val=0;
		for(int i=0;i<=count2;i++)
		{
			val+=i;
		}
		return val/(count2+1);
	}

	@Override
	public double getSimilary(double[] userNode1, double[] userNode2,
			double[] weight) {
		// TODO Auto-generated method stub
		double[] item1=userNode1.clone();
		double[] item2=userNode1.clone();
		AllSort.mergerSort(item1, 0,item1.length-1,item1.length,true);
		AllSort.mergerSort(item2, 0,item2.length-1,item2.length,true);
		int[] xRank=new int[item1.length];
		getRank(item1,xRank);
		int[] yRank=new int[item2.length];
		getRank(item2,yRank);
		//利用差分等级(或排行)序列计算斯皮尔曼等级相关系数
		double fenzi=0f;
		for(int j=0;j<xRank.length;j++)
		{
			fenzi+=Math.pow(xRank[j]-yRank[j],2f);
		}
		fenzi*=6;
		double fenmu=xRank.length*(xRank.length*xRank.length-1);
		return 1-fenzi/fenmu;
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
