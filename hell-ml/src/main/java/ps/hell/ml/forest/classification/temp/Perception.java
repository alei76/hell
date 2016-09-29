package ps.hell.ml.forest.classification.temp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * 感知机
 * @author Administrator
 *
 */
public class Perception {          //感知机的对偶形式；
	public static void main(String args[]) throws IOException
	{
	  points=new ArrayList<int[]>();

		points=null;
		int[][]matrix=GramMatrix(points);
		weight=RunPerception(points,matrix);
		for(int i=0;i<weight.length;i++)
		{
			System.out.println(weight[i]);
		}

	}
	
	public static int[][] GramMatrix(List<int[]> points)      //计算Gram矩阵 Xi*Xj
	{
		int width=points.get(0).length-1;
		int len=points.size();
		int[][] matrix=new int[len][len];
		for(int i=0;i<len;i++)
		{
			for(int j=0;j<len;j++)
			{
			   int sum=0;
			   int[] ei=points.get(i);
			   int[] ej=points.get(j);
			   for(int k=0;k<width;k++)
			   {
				  sum=sum+ei[k]*ej[k];
			   }
			   matrix[i][j]=sum;
			}
		}
		return matrix;
	}
	
	public static int[] RunPerception(List<int[]> points,int[][] matrix)
	{
		int len=points.size();
		int width=points.get(0).length-1;
		int[] alpha=new int[len];
		int[] wei=new int[len+1];
		int fx,sum=0;
		int b=0,count=0;
		boolean flag=true;
		for(Integer e:alpha) {e=0;}
		for(Integer e:wei)   {e=0;}
		while(flag)
		{
			for(int i=0;i<len;i++)
			{
				int[] valuei=points.get(i);
				int yi=valuei[width];
				sum=0;
				for(int j=0;j<len;j++)
				{
				    int[] valuej=points.get(j);
				    int yj=valuej[width];
					sum=sum+alpha[j]*yj*matrix[i][j];
				}
				fx=yi*(sum+b);
				if(fx<=0)
				{
					alpha[i]=alpha[i]+anta;
					b=b+anta*yi;
					count=0;
					System.out.println("迭代~~~~~~~~~~~~~");
					break;
				}
				else    {count++;}
			}
			if(count==len)  flag=false;
		}
		
		for(int i=0;i<len;i++)  {  wei[i]=alpha[i];  }
		wei[len]=b;
		return wei;
	}

	
	private static List<int[]> points;
	private static int[]  weight;
	private static int bweight;
	private static int anta=1;
}
