package ps.hell.ml.statistics.regression;

import Jama.Matrix;

import java.util.Arrays;


/**
 * 多元线性回归
 * @author Administrator
 *
 */
public class LM {
	private double[][] x=null;
	private double[][] y=null;
	
	public static void main(String[] args) {
		 double[][] x={ {0.0},{0.1},{0.2},{0.3},{0.4},{0.5},{0.6},{0.7},{0.8},{0.9},{1.0}};
		 double[] y={ 2.75,2.84,2.965,3.01,3.20, 3.25,3.38,3.43,3.55,3.66,3.74};
		 LM lm=new LM(x,y);
		 double[] c=lm.compute();
		 System.out.println(Arrays.toString(c));
		 System.out.println(c[0]+c[1]*0.9);

	}
	/**
	 * 
	 * @param x 输入变量 行
	 * @param y
	 */
	public LM(double[][] x,double[] y)
	{
		this.x=x.clone();
		this.y=new double[y.length][1];
		for(int i=0;i<y.length;i++)
		{
			this.y[i][0]=y[i];
		}
	}
	/**
	 * 返回回归a值
	 */
	public double[] compute()
	{
		
		//计算C值
		double[][] c=C();
		//计算t(C)
		double[][] tc=T(c);
		//计算t(C)*.C
//		System.out.println("C值");
//		for(int i=0;i<c.length;i++)
//		{
//			for(int j=0;j<c[i].length;j++)
//			{
//				System.out.print(c[i][j]+"\t");
//			}
//			System.out.println();
//		}
//		System.out.println("C转置值");
//		for(int i=0;i<tc.length;i++)
//		{
//			for(int j=0;j<tc[i].length;j++)
//			{
//				System.out.print(tc[i][j]+"\t");
//			}
//			System.out.println();
//		}
		double[][] temp=multiply(tc, c);
//		System.out.println("t(C)*.C");
//		for(int i=0;i<temp.length;i++)
//		{
//			for(int j=0;j<temp[i].length;j++)
//			{
//				System.out.print(temp[i][j]+"\t");
//			}
//			System.out.println();
//		}
		Matrix inv2=new Matrix(temp);
		Matrix inv3=inv2.inverse();
		double[][] inv=inv3.getArrayCopy();
		//double[][] inv=inversion(multiply(tc, c));
//		System.out.println("(t(C)*.C)逆值");
//		for(int i=0;i<inv.length;i++)
//		{
//			for(int j=0;j<inv[i].length;j++)
//			{
//				System.out.print(inv[i][j]+"\t");
//			}
//			System.out.println();
//		}
		
		double[][] l=multiply(multiply(inv,tc),y);
//		System.out.println("(t(C)*.C)*t(C).*.y逆值");
//		for(int i=0;i<inv.length;i++)
//		{
//			for(int j=0;j<inv[i].length;j++)
//			{
//				System.out.print(inv[i][j]+"\t");
//			}
//			System.out.println();
//		}
		double[] a=new double[l.length];
		for(int i=0;i<l.length;i++)
		{
			a[i]=l[i][0];
		}
		//计算 (t(C)*.C)'*.t(C)*.y
		return a;
	}
	
	public double[][] C()
	{
		int col=x[0].length;
		int row=x.length;
		double[][] c=new double[row][col+1];
			for(int j=0;j<row;j++)
			{
				c[j][0]=1;
			}
		for(int i=0;i<row;i++)
		{
			for(int j=0;j<col;j++)
			{
				c[i][j+1]=x[i][j];
			}
		}
		return c;
	}
	/**
	 * 转置
	 * @param c
	 * @return
	 */
	public  double[][] T(double[][] c)
	{
		int row=c.length;
		int col=c[0].length;
		double[][] tc=new double[col][row];
		for(int i=0;i<row;i++)
		{
			for(int j=0;j<col;j++)
			{
				tc[j][i]=c[i][j];
			}
		}
		return tc;
	}
	
	public double[][] multiply(double[][] a,double[][] b)
	{
		int row=a.length;
		int	col=a[0].length;
		int col1=b[0].length;
		int row1=b.length;
		double[][] c=new double[row][col1];
		for(int i=0;i<row;i++)
		{
			for(int jj=0;jj<col1;jj++)
			{
				for(int j=0;j<col;j++)
				{
					c[i][jj]+=a[i][j]*b[j][jj];
				}
			}
		}
		return c;
	}
	/**
	 * 求逆 
	 * @return
	 */
	public double[][] inversion(double[][] mm) {  
        // 先是求出行列式的模|data|
		int rowNum=mm.length;
		//int colNum=mm[0].length;
        double A = getHL(mm);
        System.out.println("行列式:"+A);
        // 创建一个等容量的逆矩阵  
     //   double[][] newData = new double[data.length][data.length];  
        System.out.println("代数余子式");
        for (int i = 0; i < rowNum; i++) {  
            for (int j = 0; j < rowNum; j++) {  
                double num;  
                if ((i + j) % 2 == 0) {  
                    num = getHL(getDY(mm, i + 1, j + 1));  
                } else {  
                    num = -getHL(getDY(mm, i + 1, j + 1));  
                }  
  
                mm[i][j] = num / A; 
                System.out.print(num+"\t");
            }  
            System.out.println();
        }  
  
        // 转置 代数余子式转制  
        mm=T(mm);  
        // 打印  
//        for (int i = 0; i < data.length; i++) {  
//            for (int j = 0; j < data.length; j++) {  
//                System.out.print("newData[" + i + "][" + j + "]= "  
//                        + mm.data[i][j] + "   ");  
//            }  
//  
//            System.out.println();  
//        }  
  
        return mm;  
    }
	 /** 
     * 求解行列式的模----------->最终的总结归纳 
     *  
     * @param data 
     * @return 
     */  
    public static double getHL(double[][] data) {  
  
        // 终止条件  
        if (data.length == 2) {  
            return data[0][0] * data[1][1] - data[0][1] * data[1][0];  
        }  
  
        double total = 0;  
        // 根据data 得到行列式的行数和列数  
        int num = data.length;  
        // 创建一个大小为num 的数组存放对应的展开行中元素求的的值  
        double[] nums = new double[num];  
  
        for (int i = 0; i < num; i++) {  
            if (i % 2 == 0) {  
                nums[i] = data[0][i] * getHL(getDY(data, 1, i + 1));  
            } else {  
                nums[i] = -data[0][i] * getHL(getDY(data, 1, i + 1));  
            }  
        }  
        for (int i = 0; i < num; i++) {  
            total += nums[i];  
        }  
        // System.out.println("total=" + total);  
        return total;  
    }  
    
    /** 
     * 2 
     * 求2阶行列式的数值 
     * @param data 
     * @return 
     */  
    public static double getHL2(double[][] data) {  
        // data 必须是2*2 的数组  
        double num1 = data[0][0] * data[1][1];  
        double num2 = -data[0][1] * data[1][0];  
        return num1 + num2;  
    }  
    /** 
     * 求3阶行列式的数值 
     *  
     * @param data 
     * @return 
     */  
    public static double getHL3(double[][] data) {  
        double num1 = data[0][0] * getHL2(getDY(data, 1, 1));  
        double num2 = -data[0][1] * getHL2(getDY(data, 1, 2));  
        double num3 = data[0][2] * getHL2(getDY(data, 1, 3));  
        // System.out.println("---->"+num1);  
        // System.out.println("---->"+num2);  
        // System.out.println("---->"+num3);  
        System.out.println("3阶行列式的数值是：----->" + (num1 + num2 + num3));  
        return num1 + num2 + num3;  
    }  
    /** 
     * 求4阶行列式的数值 
     *  
     * @param data 
     * @return 
     */  
    public static double getHL4(double[][] data) {  
        double num1 = data[0][0] * getHL3(getDY(data, 1, 1));  
        double num2 = -data[0][1] * getHL3(getDY(data, 1, 2));  
        double num3 = data[0][2] * getHL3(getDY(data, 1, 3));  
        double num4 = -data[0][3] * getHL3(getDY(data, 1, 4));  
        // System.out.println("--------->"+num1);  
        // System.out.println("--------->"+num2);  
        // System.out.println("--------->"+num3);  
        // System.out.println("--------->"+num4);  
        // System.out.println("4阶行列式的数值------->"+(num1+num2+num3+num4));  
  
        return num1 + num2 + num3 + num4;  
    } 
    
    /** 
     * 求5阶行列式的数值 
     */  
    public static double getHL5(double[][] data) {  
  
        double num1 = data[0][0] * getHL4(getDY(data, 1, 1));  
        double num2 = -data[0][1] * getHL4(getDY(data, 1, 2));  
        double num3 = data[0][2] * getHL4(getDY(data, 1, 3));  
        double num4 = -data[0][3] * getHL4(getDY(data, 1, 4));  
        double num5 = data[0][4] * getHL4(getDY(data, 1, 5));  
  
        System.out.println("5 阶行列式的数值是：  ------->"  
                + (num1 + num2 + num3 + num4 + num5));  
        return num1 + num2 + num3 + num4 + num5;  
  
    } 
    
    
    
    /** 
     * 1   
     * 求解代数余子式 输入：原始矩阵+行+列 现实中真正的行和列数目 
     */  
    public static double[][] getDY(double[][] data, int h, int v) {  
        int H = data.length;  
        int V = data[0].length;  
        System.out.println("输入数据");
        for(int i=0;i<data.length;i++)
        {
        	for(int j=0;j<data[i].length;j++)
        	{
        		System.out.print(data[i][j]+"\t");
        	}
        	System.out.println();
        }
        double[][] newData = new double[H - 1][V - 1];  
  
        for (int i = 0; i < newData.length; i++) {  
  
            if (i < h - 1) {  
                for (int j = 0; j < newData[i].length; j++) {  
                    if (j < v - 1) {  
                        newData[i][j] = data[i][j];  
                    } else {  
                        newData[i][j] = data[i][j + 1];  
                    }  
                }  
            } else {  
                for (int j = 0; j < newData[i].length; j++) {  
                    if (j < v - 1) {  
                        newData[i][j] = data[i + 1][j];  
                    } else {  
                        newData[i][j] = data[i + 1][j + 1];  
                    }  
                }  
  
            }  
        }  
         System.out.println("---------------------代数余子式测试.---------------------------------");  
         for(int i=0;i<newData.length;i++){  
         for(int j=0;j<newData[i].length;j++){  
         System.out.print("newData["+i+"]"+"["+j+"]="+newData[i][j]+"   ");  
         }  
          
         System.out.println();  
         }  
  
        return newData;  
    }  
}
