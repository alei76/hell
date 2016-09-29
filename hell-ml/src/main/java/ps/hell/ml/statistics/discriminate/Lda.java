package ps.hell.ml.statistics.discriminate;

import ps.hell.math.base.matrix.Matrix;

/**
 * Lda判别线性判别
 * @author Administrator
 *
 */
public class Lda {
	private Matrix a;
	Matrix s1,s2,s3;//对应的0的协方差 1的协方差，0、1的协方差
	double[][] xavg;
	
	public  Lda(Matrix a)
	{
		this.a=a;
		xavg=new double[2][a.getColNum()];
		 s1=new Matrix(a.getColNum(),a.getColNum());
		 s2=new Matrix(a.getColNum(),a.getColNum());//初始值为0
		
	}
	
	/**
	 * 获取不同的输出值对应的列均值
	 */
	public void meansAll()
	{
		Matrix a2=a.truncMatrix(1,3,false);//获取样本段
		
		//**设定基础为0 1
		
		double[][] xavg=new double[2][a.getColNum()];//获取均值
		int x0=0;
		int x1=0;
		for(int i=0;i<a2.getRowNum();i++)
		{
			for(int j=0;j<a2.getColNum();j++)
			{
				if((a.data[i][a.getColNum()-1])==0.0)
				{
					xavg[0][j]+=a.data[i][j];
					x0++;
				}
				else
				{
					x1++;
					xavg[1][j]+=a.data[i][j];
				}
			}
		}
		for(int i=0;i<xavg[0].length;i++)
		{
			xavg[0][i]/=x0;
		}
		for(int i=0;i<xavg[1].length;i++)
		{
			xavg[1][i]/=x1;
		}
	}
	
	/**
	 * 计算协协方差
	 */
	public void computeCov()
	{
		//int k1,k2;
		
		for(int i=0;i<a.getRowNum();i++)
		{
			for(int k1=0;k1<a.getColNum();k1++)
			{
				for(int k2=0;k2<a.getColNum();k2++)
				{
					if(a.data[i][a.getColNum()-1]==0)
					{
						s1.data[k1][k2]+=(a.data[i][k1]-xavg[0][k1])*(a.data[i][k2]-xavg[0][k2]);
					}
					else
					{
						s2.data[k1][k2]+=(a.data[i][k1]-xavg[1][k1])*(a.data[i][k2]-xavg[1][k2]);
					}
				}
			}
		}
		
		
		//计算合并协方差
		 s3=a.truncMatrix(1,a.getColNum()-1,false).getCovMatrix();
	}
	
	
	/**
	 * 准则计算
	 */
	public void  compute()
	{
		Matrix s31=a.truncMatrix(1,a.getColNum()-1,false).inverse();
		//最后计算lda向量
		double lda[]={0,0,0};
		for(int i=0;i<xavg[0].length;i++)
		{
			for(int j=0;j<xavg[0].length;j++)
			{
				lda[i]+=s31.data[i][j]*(xavg[0][j]-xavg[1][j]);
			}
		}
		//计算分解准则
		double y=0;
		for(int j=0;j<xavg[0].length;j++)
		{
			y+=lda[j]*xavg[0][j];
			y+=lda[j]*xavg[1][j];
		}
		y=y/2;
	}
	
	
	
	/**
	 * 
	 */
	public void train()
	{
		meansAll();
		computeCov();
		compute();
	}
	public static void main(String[] args) {
	Matrix a=new Matrix(100,4);
	
	Lda lda=new Lda(a);
	lda.train();
	//f统计量做显著性检验c++中f函数不怎么会
//	cout<<"end"<<endl;
//	cout<<y<<endl;
//	cout<<lda[0]<<" "<<lda[1]<<" "<<lda[2]<<endl;
	//return 0;
	}
}
