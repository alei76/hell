package ps.hell.ml.sequenceTime.base;

import ps.hell.ml.statistics.regression.LM;

/**
 * 单位根检验 
 * @author Administrator
 *
 */
public class ADF {

	/**
	 * 单位根检验
	 * @return p值 如果p值小于0.05则为平稳
	 */
	public static double test(double[] data)
	{
		//对数据做回归
		int k=(int)Math.pow((data.length-1)*1.0,1.0/3);
		double[][] data1=new double[data.length][0];
		for(int i=0;i<data.length;i++)
		{
			data1[i][0]=data[i];
		}
		//做一阶差分
		double[] y=Difference.compute(data,1);
		double[] xt1=new double[y.length-k];
		double[] tt=new double[y.length-k];
		for(int i=0;i<y.length-k;i++)
		{
			xt1[i]=y[i+k];
			tt[i]=i+k;
		}
		//执行回归 
		if(k>1){
			double[][] yt1=new double[y.length][k-1];
			double[][] xTemp=new double[y.length][k+2];
			for(int i=0;i<y.length;i++)
			{
				xTemp[i][0]=1;//常数项
				xTemp[i][1]=xt1[i];
				xTemp[i][2]=tt[i];
				for(int j=0;j<k-1;j++)
				{
					xTemp[i][3+j]=yt1[i][j];
				}
				
			}
			LM lm=new LM(xTemp,y);
			double[] aValue=lm.compute();//a值
			//需要获取估计/误差
//			double[] er=lm.;
			
		}
		else
		{
			double[][] xTemp=new double[y.length][k];
			for(int i=0;i<y.length;i++)
			{
				xTemp[i][0]=1;//常数项
				xTemp[i][1]=xt1[i];
				xTemp[i][2]=tt[i];		
			}
			LM lm=new LM(xTemp,y);
			double[] aValue=lm.compute();//a值
			//需要获取估计/误差
//			double[] er=lm.getError();
		}
		return 0d;
	}
}
