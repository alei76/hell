package ps.hell.ml.statistics.base;

import ps.hell.math.base.matrix.Matrix;

import java.util.Random;


/**
 * 正态分布
 * @author Administrator
 *
 */
public class Normal implements DistributionInter{
	private double mean;//中值
	private double std;//标准差
	
	Random  r1 = new Random();
    Random  r2 = new Random();
    private int iset=0;
    private  double gset;
	
	public Normal(double mean,double std)
	{
		this.mean=mean;
		this.std=std;
	}
	public Normal(int mean,int std)
	{
		this.mean=mean*1.0;
		this.std=std*1.0;
	}
	public Normal(int mean,Double std)
	{
		this.mean=mean*1.0;
		this.std=std*1.0;
	}
	public Normal(double mean,int std)
	{
		this.mean=mean*1.0;
		this.std=std*1.0;
	}

	@Override
	/**
	 * 
	 * @param countcount 获取随机数的数量
	 * @return
	 */
	public double[] getRandom(int count) {
		// TODO Auto-generated method stub
		double[] ren=new double[count];
		for(int i=0;i<count;i++)
		{
			ren[i]=getRandom();
		}
		return ren;
	}
	/**
	 * 
	 */

	/**
	 * 获取概率密度函数值
	 * @param val
	 * @return
	 */
	public double getDensity(Double val)
	{
		return 1.0/Math.sqrt(2.0*Math.PI)/std*Math.exp(-(val-mean)*(val-mean)/2/std/std);
		
	}
	
	
	/**
	 * 获取随机数
	 */
	@Override
	public double getRandom()
	{
		  double V1, V2=0.0, S=0.0;
		    int phase = 0;
		    double X;
		     
		    if ( phase == 0 ) {
		        do {
		            double U1 = r1.nextDouble();
		            double U2 = r2.nextDouble();
		             
		            V1 = 2 * U1 - 1;
		            V2 = 2 * U2 - 1;
		            S = V1 * V1 + V2 * V2;
		        } while(S >= 1 || S == 0);
		         
		        X = V1 * Math.sqrt(-2 * Math.log(S) / S);
		    }
		    else
		    {
		        X = V2 * Math.sqrt(-2 * Math.log(S) / S);
		    }
		         
		    phase = 1 - phase;
		 
		    return X*std+mean;
		
		
	}
	
	
	/**
	 * 获取随机数
	 * box-muller方法
	 */
	public double getRandom2()
	{
		
		//Double x=Math.sqrt(-2*ln*r1.nextDouble())*Math.cos(2*Math.PI*r2.nextDouble());
		// y=Math.sqrt(-2*ln*r1.nextDouble())*Math.sin(2*Math.PI*r2.nextDouble());	
		double u1=r1.nextDouble();
		double u2=r2.nextDouble();
		double r=20+5*Math.sqrt(-2.0*(Math.log(u1)/Math.log(Math.E)))*Math.cos(2*Math.PI*u2);
		//return r*std+mean;
		return r;
	}
	
	
	/**
	 * 返回一个对应的矩阵
	 * @param rowNum 行数
	 * @param colNum 列数
	 * @return
	 */
	public Matrix random(int rowNum,int colNum)
	{
		Matrix matrix=new Matrix(rowNum,colNum);
		for(int i=0;i<matrix.getRowNum();i++)
		{
			for(int j=0;j<matrix.getColNum();j++)
			{
				
				matrix.getData()[i][j]=(float)getRandom();
			}
		}
		return matrix;
	}
	
	/**
	 * 返回一个对应的矩阵
	 * @param rowNum 行数
	 * @param colNum 列数
	 * @param outputValue 最后一列写入输出值
	 * @return
	 */
	public Matrix random(int rowNum,int colNum,int outputValue)
	{
		Matrix matrix=new Matrix(rowNum,colNum+1);
		for(int i=0;i<matrix.getRowNum();i++)
		{
			for(int j=0;j<matrix.getColNum();j++)
			{
				
				if(j==matrix.getColNum()-1)
				{
					matrix.getData()[i][j]=outputValue;
					
				}
				else
				{
					matrix.getData()[i][j]=(float)getRandom();
				}
			}
		}
		return matrix;
	}
	

	/**
	 * 返回一个对应的矩阵
	 * @param rowNum 行数
	 * @param colNum 列数
	 * @return
	 */
	public double[][] randomToDouble2(int rowNum,int colNum)
	{
		double[][] matrix=new double[rowNum][colNum];
		for(int i=0;i<matrix.length;i++)
		{
			for(int j=0;j<matrix[0].length;j++)
			{
				
				matrix[i][j]=getRandom();
			}
		}
		return matrix;
	}
	
	/**
	 * Should we use the last value.
	 */
	private boolean useLast = false;
	private double y2;
	/**
	 * 另一种计算gassion分布的的随机值的计算方法
	 * Compute a Gaussian random number.
	 * 
	 * @param m
	 *            The mean.
	 * @param s
	 *            The standard deviation.
	 * @return The random number.
	 */
	public double boxMuller(final double m, final double s) {
		double x1, x2, w, y1;

		// use value from previous call
		if (this.useLast) {
			y1 = this.y2;
			this.useLast = false;
		} else {
			do {
				x1 = 2.0 * Math.random() - 1.0;
				x2 = 2.0 *  Math.random() - 1.0;
				w = x1 * x1 + x2 * x2;
			} while (w >= 1.0);

			w = Math.sqrt((-2.0 * Math.log(w)) / w);
			y1 = x1 * w;
			this.y2 = x2 * w;
			this.useLast = true;
		}

		return (m + y1 * s);
	}
	public static void main(String[] args)
	{
//		Normal normal=new Normal(2.0,1.0);
//		for(Double ll:normal.Random(3))
//		System.out.println(ll);
//		System.out.println("-------------");
//		Double[] lll={-0.5,0.0,1.0,0.5,3.0,-3.0};
//		for(Double lll2:lll)
//			System.out.println(normal.Get(lll2));
		
		
//		Normal n1=new Normal(-5,1);
//		Matrix m1=n1.Random(50,2,0);
//		for(int i=0;i<m1.getRowNum();i++)
//		System.out.println(m1.getData()[i][2]);
		
		
		
		Normal n1=new Normal(-5,1);
		for(int i=0;i<20;i++)
		{
			System.out.println(n1.getRandom());
		}
	}

	
}
