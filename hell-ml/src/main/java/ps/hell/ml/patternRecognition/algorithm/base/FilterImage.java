package ps.hell.ml.patternRecognition.algorithm.base;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.util.LinkedList;

public class FilterImage {

	public static ColorModel sColor=ColorModel.getRGBdefault();
	/**
	 * 16
	 */
	public static int[][] Gaussion3x3=
	{
		{1,2,1},
		{2,4,2},
		{1,2,1}
	};
	/**
	 * 273
	 */
	public static int[][] Gaussion5x5=
	{
		{1,4,7,4,1},
		{4,16,26,16,4},
		{7,26,41,26,7},
		{4,16,26,16,4},
		{1,4,7,4,1}
	};
	/**
	 * 12
	 */
	public static int[][] GaussionMean=
	{
		{1,1,1},
		{1,4,1},
		{1,1,1}
	};
	/**
	 * 33
	 */
	public static int[][] GaussionMean2=
	{
		{4,4,4},
		{4,1,4},
		{4,4,4}
	};
	/**
	 * 高斯滤波 --灰度值
	 * @param sytle 滤波级别
	 * @param colorModel 0表示灰度 1表示彩色 3
	 * @param 5为 5*5
	 * @param 3为 3*3
	 * @param 1为 3*3均值滤波
	 * @param 2为3*3均值滤波2
	 */
	public static void FilterGaussion(BufferedImage image,int colorModel,int sytle)
	{
		 if(sytle==5)
		 {
			 if(colorModel==0)
			 {
				 //计算
				 LinkedList<Integer> color=new LinkedList<Integer>();
				 for(int i=2;i<image.getWidth()-2;i++)
				 {
					 
					 for(int j=2;j<image.getHeight()-2;j++)
					 {
						 int sum=0;
						 for(int m=-2;m<=2;m++)
						 {
							 for(int n=-2;n<=2;n++)
							 {
								 sum+=sColor.getRed(image.getRGB(i+m, j+n))*Gaussion5x5[m+2][n+2];
							 }
						 }
						 color.add(sum/273);
					 }
					 
				 }
				 //赋色
				 for(int i=2;i<image.getWidth()-2;i++)
				 {
					 for(int j=2;j<image.getHeight()-2;j++)
					 {
						 int colPiexl=color.pollFirst();
						 image.setRGB(i, j,new Color(colPiexl,colPiexl,colPiexl).getRGB());
					 }
				 }
			 }
			 else if(colorModel==1)
			 {
				//计算
				 LinkedList<Integer> colorR=new LinkedList<Integer>();
				 LinkedList<Integer> colorG=new LinkedList<Integer>();
				 LinkedList<Integer> colorB=new LinkedList<Integer>();
				 for(int i=2;i<image.getWidth()-2;i++)
				 {
					 
					 for(int j=2;j<image.getHeight()-2;j++)
					 {
						 int sumR=0;
						 int sumG=0;
						 int sumB=0;
						 for(int m=-2;m<=2;m++)
						 {
							 for(int n=-2;n<=2;n++)
							 {
								 sumR+=sColor.getRed(image.getRGB(i+m, j+n))*Gaussion5x5[m+2][n+2];
								 sumG+=sColor.getGreen(image.getRGB(i+m, j+n))*Gaussion5x5[m+2][n+2];
								 sumB+=sColor.getBlue(image.getRGB(i+m, j+n))*Gaussion5x5[m+2][n+2];
							 }
						 }
						 colorR.add(sumR/273);
						 colorG.add(sumG/273);
						 colorB.add(sumB/273);
					 }
				
				 }
				 //赋色
				 for(int i=2;i<image.getWidth()-2;i++)
				 {
					 for(int j=2;j<image.getHeight()-2;j++)
					 {
						 image.setRGB(i, j,new Color(colorR.pollFirst(),colorG.pollFirst(),colorB.pollFirst()).getRGB());
					 }
				 }
			 }
			 
		 }
		 else if(sytle==3)
		 {
			 if(colorModel==0)
			 {
				 //计算
				 LinkedList<Integer> color=new LinkedList<Integer>();
				 for(int i=1;i<image.getWidth()-1;i++)
				 {
					 
					 for(int j=1;j<image.getHeight()-1;j++)
					 {
						 int sum=0;
						 for(int m=-1;m<=1;m++)
						 {
							 for(int n=-1;n<=1;n++)
							 {
								 sum+=sColor.getRed(image.getRGB(i+m, j+n))*Gaussion3x3[m+1][n+1];
							 }
						 }
						 int sumR=0;
						 int sumG=0;
						 int sumB=0;
					 }
					
				 }
				 //赋色
				 for(int i=1;i<image.getWidth()-1;i++)
				 {
					 for(int j=1;j<image.getHeight()-1;j++)
					 {
						 int colPiexl=color.pollFirst();
						 image.setRGB(i, j,new Color(colPiexl,colPiexl,colPiexl).getRGB());
					 }
				 }
			 }
			 else if(colorModel==1)
			 {
				//计算
				 LinkedList<Integer> colorR=new LinkedList<Integer>();
				 LinkedList<Integer> colorG=new LinkedList<Integer>();
				 LinkedList<Integer> colorB=new LinkedList<Integer>();
				 for(int i=1;i<image.getWidth()-1;i++)
				 {
					
					 for(int j=1;j<image.getHeight()-1;j++)
					 {
						 int sumR=0;
						 int sumG=0;
						 int sumB=0;
						 for(int m=-1;m<=1;m++)
						 {
							 for(int n=-1;n<=1;n++)
							 {
								 sumR+=sColor.getRed(image.getRGB(i+m, j+n))*Gaussion3x3[m+1][n+1];
								 sumG+=sColor.getGreen(image.getRGB(i+m, j+n))*Gaussion3x3[m+1][n+1];
								 sumB+=sColor.getBlue(image.getRGB(i+m, j+n))*Gaussion3x3[m+1][n+1];
							 }
						 }
						 colorR.add(sumR/16);
						 colorG.add(sumG/16);
						 colorB.add(sumB/16);
					 }
					
				 }
				 //赋色
				 for(int i=1;i<image.getWidth()-1;i++)
				 {
					 for(int j=1;j<image.getHeight()-1;j++)
					 {
						 image.setRGB(i, j,new Color(colorR.pollFirst(),colorG.pollFirst(),colorB.pollFirst()).getRGB());
					 }
				 }
			 }
		 }
		 else if(sytle==1)
		 {
			 if(colorModel==0)
			 {
				 //计算
				 LinkedList<Integer> color=new LinkedList<Integer>();
				 for(int i=1;i<image.getWidth()-1;i++)
				 {
					 
					 for(int j=1;j<image.getHeight()-1;j++)
					 {
						 int sum=0;
						 for(int m=-1;m<=1;m++)
						 {
							 for(int n=-1;n<=1;n++)
							 {
								 sum+=sColor.getRed(image.getRGB(i+m, j+n))*GaussionMean[m+1][n+1];
							 }
						 }
						 color.add(sum/12);
					 }
					
				 }
				 //赋色
				 for(int i=1;i<image.getWidth()-1;i++)
				 {
					 for(int j=1;j<image.getHeight()-1;j++)
					 {
						 int colPiexl=color.pollFirst();
						 image.setRGB(i, j,new Color(colPiexl,colPiexl,colPiexl).getRGB());
					 }
				 }
			 }
			 else if(colorModel==1)
			 {
				//计算
				 LinkedList<Integer> colorR=new LinkedList<Integer>();
				 LinkedList<Integer> colorG=new LinkedList<Integer>();
				 LinkedList<Integer> colorB=new LinkedList<Integer>();
				 for(int i=1;i<image.getWidth()-1;i++)
				 {
					 
					 for(int j=1;j<image.getHeight()-1;j++)
					 {
						 int sumR=0;
						 int sumG=0;
						 int sumB=0;
						 for(int m=-1;m<=1;m++)
						 {
							 for(int n=-1;n<=1;n++)
							 {
								 sumR+=sColor.getRed(image.getRGB(i+m, j+n))*GaussionMean[m+1][n+1];
								 sumG+=sColor.getGreen(image.getRGB(i+m, j+n))*GaussionMean[m+1][n+1];
								 sumB+=sColor.getBlue(image.getRGB(i+m, j+n))*GaussionMean[m+1][n+1];
							 }
						 }
						 colorR.add(sumR/12);
						 colorG.add(sumG/12);
						 colorB.add(sumB/12);
					 }
					 
				 }
				 //赋色
				 for(int i=1;i<image.getWidth()-1;i++)
				 {
					 for(int j=1;j<image.getHeight()-1;j++)
					 {
						 image.setRGB(i, j,new Color(colorR.pollFirst(),colorG.pollFirst(),colorB.pollFirst()).getRGB());
					 }
				 }
			 }
		 }
		 else if(sytle==2)
		 {
			 if(colorModel==0)
			 {
				 //计算
				 LinkedList<Integer> color=new LinkedList<Integer>();
				 for(int i=1;i<image.getWidth()-1;i++)
				 {
					 
					 for(int j=1;j<image.getHeight()-1;j++)
					 {
						 int sum=0;
						 for(int m=-1;m<=1;m++)
						 {
							 for(int n=-1;n<=1;n++)
							 {
								 sum+=sColor.getRed(image.getRGB(i+m, j+n))*GaussionMean2[m+1][n+1];
							 }
						 }
						 color.add(sum/33);
					 }
					
				 }
				 //赋色
				 for(int i=1;i<image.getWidth()-1;i++)
				 {
					 for(int j=1;j<image.getHeight()-1;j++)
					 {
						 int colPiexl=color.pollFirst();
						 image.setRGB(i, j,new Color(colPiexl,colPiexl,colPiexl).getRGB());
					 }
				 }
			 }
			 else if(colorModel==1)
			 {
				//计算
				 LinkedList<Integer> colorR=new LinkedList<Integer>();
				 LinkedList<Integer> colorG=new LinkedList<Integer>();
				 LinkedList<Integer> colorB=new LinkedList<Integer>();
				 for(int i=1;i<image.getWidth()-1;i++)
				 {
					
					 for(int j=1;j<image.getHeight()-1;j++)
					 {
						 int sumR=0;
						 int sumG=0;
						 int sumB=0;
						 for(int m=-1;m<=1;m++)
						 {
							 for(int n=-1;n<=1;n++)
							 {
								 sumR+=sColor.getRed(image.getRGB(i+m, j+n))*GaussionMean2[m+1][n+1];
								 sumG+=sColor.getGreen(image.getRGB(i+m, j+n))*GaussionMean2[m+1][n+1];
								 sumB+=sColor.getBlue(image.getRGB(i+m, j+n))*GaussionMean2[m+1][n+1];
							 }
						 }
						 colorR.add(sumR/33);
						 colorG.add(sumG/33);
						 colorB.add(sumB/33);
					 }
					
				 }
				 //赋色
				 for(int i=1;i<image.getWidth()-1;i++)
				 {
					 for(int j=1;j<image.getHeight()-1;j++)
					 {
						 image.setRGB(i, j,new Color(colorR.pollFirst(),colorG.pollFirst(),colorB.pollFirst()).getRGB());
					 }
				 }
			 }
		 }
		 else
		 {
			 System.out.println("输入sytle参数错误");
		 }
	}

	/**
	 * 高斯滤波 --灰度值
	 * @param sytle 滤波级别
	 * @param colorModel 0表示灰度 1表示彩色 3
	 * @param 5为 5*5
	 * @param 3为 3*3
	 * @param 1为 3*3均值滤波
	 * @param 2为3*3均值滤波2
	 */
	public static BufferedImage FilterGaussionGet(BufferedImage image,int colorModel,int sytle)
	{
		BufferedImage image2=new BufferedImage(image.getWidth(),image.getHeight(),BufferedImage.TYPE_3BYTE_BGR);
		
		 if(sytle==5)
		 {
			 if(colorModel==0)
			 {
				 //计算
				 LinkedList<Integer> color=new LinkedList<Integer>();
				 for(int i=2;i<image.getWidth()-2;i++)
				 {
					 
					 for(int j=2;j<image.getHeight()-2;j++)
					 {
						 int sum=0;
						 for(int m=-2;m<=2;m++)
						 {
							 for(int n=-2;n<=2;n++)
							 {
								 sum+=sColor.getRed(image.getRGB(i+m, j+n))*Gaussion5x5[m+2][n+2];
							 }
						 }
						 color.add(sum/273);
					 }
					 
				 }
				 //赋色
				 for(int i=2;i<image.getWidth()-2;i++)
				 {
					 for(int j=2;j<image.getHeight()-2;j++)
					 {
						 int colPiexl=color.pollFirst();
						 image2.setRGB(i, j,new Color(colPiexl,colPiexl,colPiexl).getRGB());
					 }
				 }
				 int col=new Color(255,255,255).getRGB();
				 for(int i=0;i<image.getHeight();i++)
				 {
					 image2.setRGB(0,i,col);
					 image2.setRGB(image.getWidth()-1,i,col);
					 image2.setRGB(1,i,col);
					 image2.setRGB(image.getWidth()-2,i,col);
				 }
				 for(int i=0;i<image.getWidth();i++)
				 {
					 image2.setRGB(i,0,col);
					 image2.setRGB(i,image.getHeight()-1,col);
					 image2.setRGB(i,1,col);
					 image2.setRGB(i,image.getHeight()-2,col);
				 }
			 }
			 else
			 {
				//计算
				 LinkedList<Integer> colorR=new LinkedList<Integer>();
				 LinkedList<Integer> colorG=new LinkedList<Integer>();
				 LinkedList<Integer> colorB=new LinkedList<Integer>();
				 for(int i=2;i<image.getWidth()-2;i++)
				 {
					 for(int j=2;j<image.getHeight()-2;j++)
					 {
						 int sumR=0;
						 int sumG=0;
						 int sumB=0;
						 for(int m=-2;m<=2;m++)
						 {
							 for(int n=-2;n<=2;n++)
							 {
								 sumR+=sColor.getRed(image.getRGB(i+m, j+n))*Gaussion5x5[m+2][n+2];
								 sumG+=sColor.getGreen(image.getRGB(i+m, j+n))*Gaussion5x5[m+2][n+2];
								 sumB+=sColor.getBlue(image.getRGB(i+m, j+n))*Gaussion5x5[m+2][n+2];
							 }
						 }
						 colorR.add(sumR/273);
						 colorG.add(sumG/273);
						 colorB.add(sumB/273);
					 }
					 
				 }
				 //赋色
				 int col=new Color(255,255,255).getRGB();
				 for(int i=0;i<image.getHeight();i++)
				 {
					 image2.setRGB(0,i,col);
					 image2.setRGB(image.getWidth()-1,i,col);
					 image2.setRGB(1,i,col);
					 image2.setRGB(image.getWidth()-2,i,col);
				 }
				 for(int i=0;i<image.getWidth();i++)
				 {
					 image2.setRGB(i,0,col);
					 image2.setRGB(i,image.getHeight()-1,col);
					 image2.setRGB(i,1,col);
					 image2.setRGB(i,image.getHeight()-2,col);
				 }
				 for(int i=2;i<image2.getWidth()-2;i++)
				 {
					 for(int j=2;j<image2.getHeight()-2;j++)
					 {
						 image2.setRGB(i, j,new Color(colorR.pollFirst(),colorG.pollFirst(),colorB.pollFirst()).getRGB());
					 }
				 }
				 
			 }
			 
		 }
		 else if(sytle==3)
		 {
			 if(colorModel==0)
			 {
				 //计算
				 LinkedList<Integer> color=new LinkedList<Integer>();
				 for(int i=1;i<image.getWidth()-1;i++)
				 {
					 
					 for(int j=1;j<image.getHeight()-1;j++)
					 {
						 int sum=0;
						 for(int m=-1;m<=1;m++)
						 {
							 for(int n=-1;n<=1;n++)
							 {
								 sum+=sColor.getRed(image.getRGB(i+m, j+n))*Gaussion3x3[m+1][n+1];
							 }
						 }
						 color.add(sum/16);
					 }
					 
				 }
				 //赋色
				 for(int i=1;i<image.getWidth()-1;i++)
				 {
					 for(int j=1;j<image.getHeight()-1;j++)
					 {
						 int colPiexl=color.pollFirst();
						 image2.setRGB(i, j,new Color(colPiexl,colPiexl,colPiexl).getRGB());
					 }
				 }
			 }
			 else
			 {
				//计算
				 LinkedList<Integer> colorR=new LinkedList<Integer>();
				 LinkedList<Integer> colorG=new LinkedList<Integer>();
				 LinkedList<Integer> colorB=new LinkedList<Integer>();
				 for(int i=1;i<image.getWidth()-1;i++)
				 {
					
					 for(int j=1;j<image.getHeight()-1;j++)
					 {
						 int sumR=0;
						 int sumG=0;
						 int sumB=0;
						 for(int m=-1;m<=1;m++)
						 {
							 for(int n=-1;n<=1;n++)
							 {
								 sumR+=sColor.getRed(image.getRGB(i+m, j+n))*Gaussion3x3[m+1][n+1];
								 sumG+=sColor.getGreen(image.getRGB(i+m, j+n))*Gaussion3x3[m+1][n+1];
								 sumB+=sColor.getBlue(image.getRGB(i+m, j+n))*Gaussion3x3[m+1][n+1];
							 }
						 }
						 colorR.add(sumR/16);
						 colorG.add(sumG/16);
						 colorB.add(sumB/16);
					 }
					
				 }
				 //赋色
				 for(int i=1;i<image.getWidth()-1;i++)
				 {
					 for(int j=1;j<image.getHeight()-1;j++)
					 {
						 image2.setRGB(i, j,new Color(colorR.pollFirst(),colorG.pollFirst(),colorB.pollFirst()).getRGB());
					 }
				 }
				 int col_w =new Color(255,255,255).getRGB();
				 int w=image.getWidth()-1;
				 int h=image.getHeight()-1;
				 for(int i=0;i<image.getHeight();i++)
				 {
					 image2.setRGB(0,i,col_w);
					 image2.setRGB(w,i,col_w);
				 }
				 for(int i=0;i<image.getWidth();i++)
				 {
					 image2.setRGB(i,0,col_w);
					 image2.setRGB(i,h,col_w);
				 }
			 }
		 }
		 else if(sytle==1)
		 {
			 if(colorModel==0)
			 {
				 //计算
				 LinkedList<Integer> color=new LinkedList<Integer>();
				 for(int i=1;i<image.getWidth()-1;i++)
				 {
					
					 for(int j=1;j<image.getHeight()-1;j++)
					 {
						 int sum=0;
						 for(int m=-1;m<=1;m++)
						 {
							 for(int n=-1;n<=1;n++)
							 {
								 sum+=sColor.getRed(image.getRGB(i+m, j+n))*GaussionMean[m+1][n+1];
							 }
						 }
						 //System.out.println(sum/12);
						 color.add(sum/12);
					 }

				 }
				 //赋色
				 for(int i=1;i<image.getWidth()-1;i++)
				 {
					 for(int j=1;j<image.getHeight()-1;j++)
					 {
						 int colPiexl=color.pollFirst();
						 image2.setRGB(i, j,new Color(colPiexl,colPiexl,colPiexl).getRGB());
					 }
				 }
			 }
			 else
			 {
				//计算
				 LinkedList<Integer> colorR=new LinkedList<Integer>();
				 LinkedList<Integer> colorG=new LinkedList<Integer>();
				 LinkedList<Integer> colorB=new LinkedList<Integer>();
				 for(int i=1;i<image.getWidth()-1;i++)
				 {
					 
					 for(int j=1;j<image.getHeight()-1;j++)
					 {
						 int sumR=0;
						 int sumG=0;
						 int sumB=0;
						 for(int m=-1;m<=1;m++)
						 {
							 for(int n=-1;n<=1;n++)
							 {
								 sumR+=sColor.getRed(image.getRGB(i+m, j+n))*GaussionMean[m+1][n+1];
								 sumG+=sColor.getGreen(image.getRGB(i+m, j+n))*GaussionMean[m+1][n+1];
								 sumB+=sColor.getBlue(image.getRGB(i+m, j+n))*GaussionMean[m+1][n+1];
							 }
						 }
						 colorR.add(sumR/12);
						 colorG.add(sumG/12);
						 colorB.add(sumB/12);
					 }
					 
				 }
				 //赋色
				 for(int i=1;i<image.getWidth()-1;i++)
				 {
					 for(int j=1;j<image.getHeight()-1;j++)
					 {
						 image2.setRGB(i, j,new Color(colorR.pollFirst(),colorG.pollFirst(),colorB.pollFirst()).getRGB());
					 }
				 }
			 }
		 }
		 else if(sytle==2)
		 {
			 if(colorModel==0)
			 {
				 //计算
				 LinkedList<Integer> color=new LinkedList<Integer>();
				 for(int i=1;i<image.getWidth()-1;i++)
				 {
					
					 for(int j=1;j<image.getHeight()-1;j++)
					 {
						 int sum=0;
						 for(int m=-1;m<=1;m++)
						 {
							 for(int n=-1;n<=1;n++)
							 {
								 sum+=sColor.getRed(image.getRGB(i+m, j+n))*GaussionMean2[m+1][n+1];
							 }
						 }
						 color.add(sum/33);
					 }
					
				 }
				 //赋色
				 for(int i=1;i<image.getWidth()-1;i++)
				 {
					 for(int j=1;j<image.getHeight()-1;j++)
					 {
						 int colPiexl=color.pollFirst();
						 image2.setRGB(i, j,new Color(colPiexl,colPiexl,colPiexl).getRGB());
					 }
				 }
			 }
			 else
			 {
				//计算
				 LinkedList<Integer> colorR=new LinkedList<Integer>();
				 LinkedList<Integer> colorG=new LinkedList<Integer>();
				 LinkedList<Integer> colorB=new LinkedList<Integer>();
				 for(int i=1;i<image.getWidth()-1;i++)
				 {
					
					 for(int j=1;j<image.getHeight()-1;j++)
					 {
						 int sumR=0;
						 int sumG=0;
						 int sumB=0;
						 for(int m=-1;m<=1;m++)
						 {
							 for(int n=-1;n<=1;n++)
							 {
								 sumR+=sColor.getRed(image.getRGB(i+m, j+n))*GaussionMean2[m+1][n+1];
								 sumG+=sColor.getGreen(image.getRGB(i+m, j+n))*GaussionMean2[m+1][n+1];
								 sumB+=sColor.getBlue(image.getRGB(i+m, j+n))*GaussionMean2[m+1][n+1];
							 }
						 }
						 colorR.add(sumR/33);
						 colorG.add(sumG/33);
						 colorB.add(sumB/33);
					 }
					
				 }
				 //赋色
				 for(int i=1;i<image.getWidth()-1;i++)
				 {
					 for(int j=1;j<image.getHeight()-1;j++)
					 {
						 image2.setRGB(i, j,new Color(colorR.pollFirst(),colorG.pollFirst(),colorB.pollFirst()).getRGB());
					 }
				 }
			 }
		 }
		 else
		 {
			 System.out.println("输入sytle参数错误");
		 }
		 return image2;
	}
	
	/**
	 * 获取灰度值
	 * @param pixel
	 * @return
	 */
	public static int GetGrayValue(int pixel)
	{
		return (int) (0.299*sColor.getRed(pixel)+0.587*sColor.getGreen(pixel)+0.114*sColor.getBlue(pixel));
	}
	/**
	 * 获取灰度值
	 * @param pixel
	 * @return
	 */
	public static int GetGrayValue(BufferedImage image,int x,int y)
	{
		int pixel=image.getRGB(x, y);
		return (int) (0.299*sColor.getRed(pixel)+0.587*sColor.getGreen(pixel)+0.114*sColor.getBlue(pixel));
	}
	/**
	 * 转灰度图
	 * @param image
	 * @limit 如果为<0则为不二值化，都则二值化
	 */
	public static void FilterGrayLevel(BufferedImage image,int limit)
	{
		if(limit<0)
		{
			for(int i=0;i<image.getWidth();i++)
			{
				for(int j=0;j<image.getHeight();j++)
				{
					int gray=GetGrayValue(image,i,j);
					image.setRGB(i, j,new Color(gray,gray,gray).getRGB());
				}
			}
		}
		else
		{
			for(int i=0;i<image.getWidth();i++)
			{
				for(int j=0;j<image.getHeight();j++)
				{
					int gray=GetGrayValue(image,i,j);
					if(gray<limit)
					{
						image.setRGB(i, j,new Color(0,0,0).getRGB());
					}
					else
					{
						image.setRGB(i, j,new Color(255,255,255).getRGB());
					}
				}
			}
		}
	}
	/**
	 * 转灰度图 返回灰度
	 * @param image
	 * @limit 如果为<0则为不二值化，都则二值化
	 */
	public static BufferedImage FilterGrayLevelGet(BufferedImage image,int limit)
	{
		BufferedImage image2=new BufferedImage(image.getWidth(),image.getHeight(),BufferedImage.TYPE_3BYTE_BGR);
		if(limit<0)
		{
			for(int i=0;i<image.getWidth();i++)
			{
				for(int j=0;j<image.getHeight();j++)
				{
					int gray=GetGrayValue(image,i,j);
					image2.setRGB(i, j,new Color(gray,gray,gray).getRGB());
				}
			}
		}
		else
		{
			for(int i=0;i<image.getWidth();i++)
			{
				for(int j=0;j<image.getHeight();j++)
				{
					int gray=GetGrayValue(image,i,j);
					if(gray<limit)
					{
						image2.setRGB(i, j,new Color(0,0,0).getRGB());
					}
					else
					{
						image2.setRGB(i, j,new Color(255,255,255).getRGB());
					}
				}
			}
		}
		return image2;
	}
	public static int[][] erosion={
		{0,1,0},
		{1,0,1},
		{0,1,0}
	};
	/**
	 * 
	 * @param image
	 * @param count腐蚀次数
	 * 3*3
	 */
	public static void FilterErosion(BufferedImage image,int count,int red)
	{
		for(int l=1;l<=count;l++)
		{
			LinkedList<Integer> x=new LinkedList<Integer>();
			LinkedList<Integer> y=new LinkedList<Integer>();
			for(int i=1;i<image.getWidth()-1;i++)
			{
				for(int j=1;j<image.getHeight()-1;j++)
				{
					if(sColor.getRed(image.getRGB(i, j))==red)
					{
						boolean flag=false;
						if(sColor.getRed(image.getRGB(i, j-1))==red)
						{
							if(sColor.getRed(image.getRGB(i, j+1))==red)
							{
								if(sColor.getRed(image.getRGB(i-1, j))==red)
								{
									if(sColor.getRed(image.getRGB(i+1, j))==red)
									{
										
									}
									else
									{
										flag=true;
									}
								}
								else
								{
									flag=true;
								}
							}
							else
							{
								flag=true;
							}
						}
						else
						{
							flag=true;
						}
						if(flag==true)
						{
							x.add(i);
							y.add(j);
						}
					}
					else
					{
						
					}
				}
			}
			//清除
			int col=new Color(255,255,255).getRGB();
			for(int i=0;i<x.size();i++)
			{
				image.setRGB(x.get(i),y.get(i),col);
			}
		}
	}
	
	/**
	 * 
	 * @param image
	 * @param count膨胀次数
	 * 3*3
	 */
	public static void FilterExpand(BufferedImage image,int count,int red)
	{
		for(int l=1;l<=count;l++)
		{
			LinkedList<Integer> x=new LinkedList<Integer>();
			LinkedList<Integer> y=new LinkedList<Integer>();
			for(int i=1;i<image.getWidth()-1;i++)
			{
				for(int j=1;j<image.getHeight()-1;j++)
				{
					if(sColor.getRed(image.getRGB(i, j))==red)
					{
						x.add(i-1);
						x.add(i);
						x.add(i);
						x.add(i+1);
						y.add(j);
						y.add(j+1);
						y.add(j-1);
						y.add(j);
					}
				
				}
			}
			//清除
			int col=new Color(0,0,0).getRGB();
			for(int i=0;i<x.size();i++)
			{
				image.setRGB(x.get(i),y.get(i),col);
			}
		}
	}
	
	
	/**
	 * 只处理黑白已经二值化后的图像
	 * @param image
	 * @param    1
	 * @param  1 0 1
	 * @param    1
	 * @param  image 中red值得判断条件
	 * @param fillRed 填充色
	 * @param fillGreen填充色
	 * @param fillBlue 填充色
	 */
	public static void FilterFill(BufferedImage image,int red,int fillRed,int fillGreen,int fillBlue)
	{
		int width=image.getWidth();
		int height=image.getHeight();
		int w=width-1;
		int h=height-1;
		int colPixel=new Color(255-fillRed,255-fillGreen,255-fillBlue).getRGB();
		int colPixel2=new Color(fillRed,fillGreen,fillBlue).getRGB();
		for(int i=0;i<width;i++)
		{
			image.setRGB(i,0,colPixel);
			image.setRGB(i,h,colPixel);
		}
		for(int j=1;j<h;j++)
		{
			image.setRGB(0,j,colPixel);
			image.setRGB(w,j,colPixel);
		}
		for(int i=1;i<w;i++)
		{
			for(int j=1;j<h;j++)
			{
				if(sColor.getRed(image.getRGB(i, j))==red)
				{
					if(sColor.getRed(image.getRGB(i-1,j))==red)
					{
						if(sColor.getRed(image.getRGB(i+1,j))==red)
						{
							if(sColor.getRed(image.getRGB(i,j-1))==red)
							{
								if(sColor.getRed(image.getRGB(i,j+1))==red)
								{
									image.setRGB(i,j,colPixel2);
								}
							}
						}
					}
					
				}
			}
		
		}
	}
	
	/**
	 *@param 均值删除器
	 *@param 在5*5范围内如果比均值低并且 比limit值小的保留否则设置为白色
	 *@param rate 为25*比率值
	 */
	public static BufferedImage FilterMeanDel(BufferedImage image,double rate,int Limitred,int LimitredWhite)
	{
		int ra=(int)(25*rate);
		int coly=new Color(100,100,100).getRGB();
		int colw=new Color(255,255,255).getRGB();
		int colg=new Color(50,50,50).getRGB();
		int colb=new Color(0,0,0).getRGB();
		BufferedImage image2=new BufferedImage(image.getWidth(),image.getHeight(),BufferedImage.TYPE_3BYTE_BGR);
		
		for(int i=2;i<image.getWidth()-2;i++)
		{
		
			for(int j=2;j<image.getHeight()-2;j++)
			{
				int value=0;
				for(int m=-2;m<=2;m++)
				{
					for(int n=-2;n<2;n++)
					{
						value+=sColor.getRed(image.getRGB(i+m,j+n));
					}
				}
				value/=ra;
				int red=sColor.getRed(image.getRGB(i,j));
				//System.out.println("red"+red+"\tvalue:"+value);
				if(red>=value)
				{
					if(red>Limitred)
					{
						if(red>LimitredWhite)
						{
							if(red>130)
							{
								image2.setRGB(i,j, colw);
							}
							else
							{
								image2.setRGB(i,j, coly);
							}
						}
						else
						{
							//不变
							image2.setRGB(i,j,image.getRGB(i, j));
						}
					}
					else
					{
						image2.setRGB(i,j,colb);
					}
				}
				else
				{
					image2.setRGB(i,j, colg);
				}
			}
		}
		int col=new Color(255,255,255).getRGB();
		for(int i=0;i<image.getHeight();i++)
		{
			image2.setRGB(0,i,col);
			image2.setRGB(1,i,col);
			image2.setRGB(image.getWidth()-1,i,col);
			image2.setRGB(image.getWidth()-2,i,col);
		}
		for(int i=0;i<image.getWidth();i++)
		{
			image2.setRGB(i,0,col);
			image2.setRGB(i,1,col);
			image2.setRGB(i,image.getHeight()-1,col);
			image2.setRGB(i,image.getHeight()-2,col);
		}
		return image2;
	}


}
