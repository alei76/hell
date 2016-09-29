package ps.hell.ml.patternRecognition.algorithm.base;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.LinkedList;

import javax.swing.JFrame;

import ps.hell.math.base.matrix.read.MatrixReadSource;
import ps.hell.ml.forest.model.svm.SvmBaseN;

/**
 * 英文字母+汉字识别
 * @author Administrator
 *
 */
public class ChinessRecognition {
	
	/**
	 * 基础图片
	 */
	public BufferedImage image;
	
	public BufferedImage image2;
	public ColorModel sColor=ColorModel.getRGBdefault();
	public int width=50;
	public int height=50;
	
	/**
	 * 存储的为图片的oci地址
	 * 其中 width 为 从 左到右  从 低到高
	 * 其中 height为 从 上到下 从低到高
	 */
	public int widthAlphaUpper=0;
	public int widthAlphaLower=0;
	public int HeightAlphaUpper=0;
	public int HeightAlphaLower=0;
	/**
	 * 阈值
	 */
	public int limitColor=60;
	public Font font;
	/**
	 * 存储特征值
	 */
	public LinkedList<double[]> featureValue=new LinkedList<double[]>();
	
	/**
	 * 存储特征名
	 */
	public LinkedList<String> featureKey=new LinkedList<String>();
	
	/**
	 * 存储每一行的元素
	 */
	public LinkedList<String> featureList=new LinkedList<String>();
	
	public ChinessRecognition()
	{
		 this.font=new Font("宋体",Font.BOLD+Font.ITALIC,20);
	}
	
	class Point{
		int x;
		int y;
		public Point(int x,int y)
		{
			this.x=x;
			this.y=y;
		}
	}
	/**
	 * 获取26字母特征
	 * inputDirector 输入图片目录
	 * outputFile 输出特征文件目录
	 * @throws IOException 
	 */
	public void getFeature(int row,int col)
	{
		  Point center=new Point(10,10);
	    
	      for(int i=65;i<123;i++)
	      {
	    	  //字母
	    	  if(i<97 && i>90)
	    	  {
	    		  continue;
	    	  }
	    	  center=new Point(10,20);
	    	  char ln=(char)i;
	    	//  System.out.println(ln);
	    	  featureList.add(Character.toString(ln));
	    	  image = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
	    	  //TYPE_INT_ARGB
	    	 // image =opencv_core.cvCreateImage(opencv_core.cvSize(width,height),8,3);
	    	//  setBackGround(image);
	    	  image2 = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
	    	 // image2 =opencv_core.cvCreateImage(opencv_core.cvSize(width,height),8,3);
	    	 // setBackGround(image2);
	    	  setBackGround(image2);
	    	  int pixel = image2.getRGB(10, 10);
	    	//  System.out.println("sadddddd颜色:"+sColor.getRed(pixel)+"\t"+sColor.getGreen(pixel)+"\t"+sColor.getBlue(pixel));
	    	  
	    	  Graphics g=image.getGraphics();
	  		 g.fillRect(0,0,width,height);
	    	 
	  		 //char[] ln2={ln};
	  	 //	 g.drawChars(ln2, 0, 1,center.x, center.y);
	  		 Graphics2D gg=(Graphics2D)g;
	  		 Graphics g2=image2.getGraphics();
	  		 g2.fillRect(0,0,width,height);
		    	 
		  		//char[] ln2={ln};
		  	//	g.drawChars(ln2, 0, 1,center.x, center.y);
		  		Graphics2D gg2=(Graphics2D)g2;
		  		//gg2.setBackground(Color.white);
	  		gg.setBackground(Color.black);
	  		gg.clearRect(0, 0, width, height);
	  		gg.setColor(Color.red);
	  		gg.setFont(font);
	    	 // opencv_core.cvPutText(image,Character.toString(ln),center,font,opencv_core.CV_RGB(255,0,0));
	  		//gg.setFont(font);
	  		gg.drawString(Character.toString(ln),center.x, center.y);
	  		 
	  		
	  	 //  pixel = image2.getRGB(10, 10);
    	  //System.out.println("22222颜色:"+sColor.getRed(pixel)+"\t"+sColor.getGreen(pixel)+"\t"+sColor.getBlue(pixel));
//	    	 if(Character.toString(ln).equals("Y"))
//	    	  {
//	    		 gg.drawString("hello world",20,20);
//	    		 try {
//					ImageIO.write(image, "jpg", new File("C:/21.jpg"));
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//	    		 IplImage ipl=IplImage.createFrom(image);
//
//	    		ipl=IplImage.createCompatible(ipl);
//	    		 cvNamedWindow("test",1);
//	    		cvShowImage("test",ipl);
//	    			cvWaitKey(0);
//	    		 pixel = image2.getRGB(10, 10);
//	       	  System.out.println("3333颜色:"+sColor.getRed(pixel)+"\t"+sColor.getGreen(pixel)+"\t"+sColor.getBlue(pixel));
//	    		 
//	  		JFrame app = new JFrame();
//			app.setSize(800, 600);
//			Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
//			app.setLocation(dimension.width / 4, dimension.height / 4);
//			app.add(new ImagePanel(image));
//			app.setVisible(true);
//			
//			
//			JFrame app2 = new JFrame();
//			app2.setSize(800, 600);
//			Dimension dimension2 = Toolkit.getDefaultToolkit().getScreenSize();
//			app2.setLocation(dimension2.width / 4, dimension2.height / 4);
//			app2.add(new ImagePanel(image2));
//			app2.setVisible(true);
	    	//  }
	  		
	  		getOci();
	    	  trainNPoint(row,col);
	    	  
//	    	 if(Character.toString(ln).equals("Y"))
//	    	  {
//	    		  cvNamedWindow("test",1);
//	    		  for(int ii=this.widthAlphaLower;ii<=this.widthAlphaUpper;ii++)
//	    		  {
//	    			  for(int jj=this.HeightAlphaLower;jj<=this.HeightAlphaUpper;jj++)
//	    			  {
//	    				  if(ii ==widthAlphaLower || ii==widthAlphaUpper ||
//	    						  jj==HeightAlphaLower || jj==HeightAlphaUpper)
//	    				  {
//	    					  cvSet2D(image2,ii,jj,new CvScalar(0.0,255.0,0.0,1.0));
//	    				  }
//	    			  }
//	    		  }
//	    			cvShowImage("test",image2);
//	    			
//	    			
//	    			cvNamedWindow("test",1);
//	    		//	cvShowImage("test1",image);
//	    			cvWaitKey(0);
//	    			
//	    	  }
	    	  //  break;
	    	  //将特征拆分为4个部分 分成四份 
	      }
	      for(int i=19968;i<=40869;i++)
	      {
	    	  System.out.println(i);
	    	  //字母
//	    	  if(i<97 && i>90)
//	    	  {
//	    		  continue;
//	    	  }
	    	  if(i==20886)
	    	  {
	    		  JFrame app2 = new JFrame();
	  			app2.setSize(800, 600);
	  			Dimension dimension2 = Toolkit.getDefaultToolkit().getScreenSize();
	  			app2.setLocation(dimension2.width / 4, dimension2.height / 4);
	  			app2.add(new ImagePanel(image));
	  			app2.setVisible(true);
	    	  }
	    	  center=new Point(10,20);
	    	  char ln=(char)i;
	    	  System.out.println(ln);
	    	  featureList.add(Character.toString(ln));
	    	  image = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
	    	  image2 = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
	    	  setBackGround(image2);
	    	  int pixel = image2.getRGB(10, 10);
	    	  Graphics g=image.getGraphics();
	  		 g.fillRect(0,0,width,height);
	  		 Graphics2D gg=(Graphics2D)g;
	  		 Graphics g2=image2.getGraphics();
	  		 g2.fillRect(0,0,width,height);
		  		Graphics2D gg2=(Graphics2D)g2;
	  		gg.setBackground(Color.black);
	  		gg.clearRect(0, 0, width, height);
	  		gg.setColor(Color.red);
	  		gg.setFont(font);
	  		gg.drawString(Character.toString(ln),center.x, center.y);  		
	  		getOci();
	    	  trainNPoint(row,col);
	      }
	      
	}
	
	/**
	 * 获取字符的有效位置
	 */
	public void getOci()
	{
		widthAlphaLower=0;
		widthAlphaUpper=1000;
		HeightAlphaLower=0;
		HeightAlphaUpper=1000;
		//计算图片中最左和最右两侧第一次出现的地方的位置
		boolean flag=false;
		for(int i=0;i<width;i++)
		{
			for(int j=0;j<height;j++)
			{
				argb color=Get2D(image,i,j);
				if(color.r>limitColor)
				{
					widthAlphaLower=i;
					flag=true;
					break;
				}
			}
			if(flag==true)
			{
				break;
			}
		}
		flag=false;
		for(int i=width-1;i>=0;i--)
		{
			for(int j=0;j<height;j++)
			{
				argb color=Get2D(image,i,j);
				if(color.r>limitColor)
				{
					widthAlphaUpper=i;
					flag=true;
					break;
				}
			}
			if(flag==true)
			{
				break;
			}
		}
		flag=false;
		for(int i=0;i<height;i++)
		{
			for(int j=widthAlphaLower;j<=widthAlphaUpper;j++)
			{
				argb color=Get2D(image,j,i);
				if(color.r>limitColor)
				{
					HeightAlphaLower=i;
					flag=true;
					break;
				}
			}
			if(flag==true)
			{
				break;
			}
		}
		flag=false;
		for(int i=height-1;i>=0;i--)
		{
			for(int j=widthAlphaLower;j<=widthAlphaUpper;j++)
			{
				argb color=Get2D(image,j,i);
				if(color.r>limitColor)
				{
					HeightAlphaUpper=i;
					flag=true;
					break;
				}
			}
			if(flag==true)
			{
				break;
			}
		}
		
		//System.out.println("widthAlphaLower"+widthAlphaLower+"\t"+widthAlphaUpper);
		//System.out.println("HeightAlphaLower"+HeightAlphaLower+"\t"+HeightAlphaUpper);
	}

	
	/**
	 * 将pointn对应元素存入特征key中
	 * @param row
	 * @param col
	 */
	public void trainNPointString(int row,int col)
	{
		for(int i=0;i<row*col;i++)
		{
			featureKey.add(row*col+"Point"+(i+1));
		}
	}
	/**
	 * 
	 * @param row 分割成几份
	 * @param col
	 * 分割为几行，几列
	 * 并取其中的存在点的数量的比率
	 * 提取该特征
	 */
	public void trainNPoint(int row,int col)
	{
		int wV=1;
		int hV=1;
		double[] value=new double[col*row+2];
		int indexSqare=(widthAlphaUpper-widthAlphaLower+1)*(HeightAlphaUpper-HeightAlphaLower+1);
		//System.out.println("indexSqare值:"+indexSqare);
		for(int i=widthAlphaLower;i<=widthAlphaUpper;i++)
		{
			//按行分割
			wV=(int)((i-widthAlphaLower)*1.0/(widthAlphaUpper-widthAlphaLower+2)*col+1);
			for(int j=HeightAlphaLower;j<=HeightAlphaUpper;j++)
			{
				//存储宽度
				//System.out.println((i-widthAlphaLower)+"\t"+(HeightAlphaUpper-widthAlphaLower+1));
				hV=(int)((j-HeightAlphaLower)*1.0/(HeightAlphaUpper-HeightAlphaLower+2)*row);
			
				int index=hV*col+wV;
				//System.out.println(wV+"\t"+hV+"\t"+index);
				argb color=Get2D(image,i,j);
				//System.out.print(color.red()+"\t");
				if(color.r>limitColor)
				{
					Set2D(image2,i,j,color);
					value[index-1]+=1.0/indexSqare;
				}	
			}
		}
		value[col*row]= (widthAlphaUpper-widthAlphaLower)*1.0/(HeightAlphaUpper-HeightAlphaLower);
		value[col*row+1]= (HeightAlphaUpper-HeightAlphaLower)*1.0/(widthAlphaUpper-widthAlphaLower);
		//System.out.print("\r\n");
		//将对应值 添加入 featureValue中
		featureValue.add(value);
	}

	/**
	 * 
	 * @param row 分割成几份
	 * @param col
	 * 并取其中的存在点的数量的比率
	 * 提取该特征
	 * 其中为测试样例的特征获取阶段
	 */
	public double[] getTrainNPoint(int row,int col)
	{
		int wV=1;
		int hV=1;
		double[] value=new double[col*row+2];
		int indexSqare=(widthAlphaUpper-widthAlphaLower+1)*(HeightAlphaUpper-HeightAlphaLower+1);
		//System.out.println("indexSqare值:"+indexSqare);
		for(int i=widthAlphaLower;i<=widthAlphaUpper;i++)
		{
			//按行分割
			wV=(int)((i-widthAlphaLower)*1.0/(widthAlphaUpper-widthAlphaLower+2)*col+1);
			for(int j=HeightAlphaLower;j<=HeightAlphaUpper;j++)
			{
				//存储宽度
				//System.out.println((i-widthAlphaLower)+"\t"+(HeightAlphaUpper-widthAlphaLower+1));
				hV=(int)((j-HeightAlphaLower)*1.0/(HeightAlphaUpper-HeightAlphaLower+2)*row);
			
				int index=hV*col+wV;
				//System.out.println(wV+"\t"+hV+"\t"+index);
				argb color=Get2D(image,i,j);
				//System.out.print(color.red()+"\t");
				if(color.r>limitColor)
				{
					argb color1 =new argb(255,0,0,255);
					Set2D(image2,i,j,color1);
					value[index-1]+=1.0/indexSqare;
				}	
			}
		}
		//设置颜色
		
		for(int ii=this.widthAlphaLower;ii<=this.widthAlphaUpper;ii++)
  		  {
  			  for(int jj=this.HeightAlphaLower;jj<=this.HeightAlphaUpper;jj++)
  			  {
  				  if(ii ==widthAlphaLower || ii==widthAlphaUpper ||
  						  jj==HeightAlphaLower || jj==HeightAlphaUpper)
  				  {
  					  Set2D(image2,ii,jj,new argb(0,255,0,255));
  				  }
  			  }
  		  }
		
		
		value[col*row]= (widthAlphaUpper-widthAlphaLower)*1.0/(HeightAlphaUpper-HeightAlphaLower);
		value[col*row+1]= (HeightAlphaUpper-HeightAlphaLower)*1.0/(widthAlphaUpper-widthAlphaLower);
		System.out.println((widthAlphaUpper-widthAlphaLower)+"\t"+(HeightAlphaUpper-HeightAlphaLower));
		//System.out.print("\r\n");
		//将对应值 添加入 featureValue中
		return value.clone();
	}
	public void setBackGround(BufferedImage ip)
	{
		int w= ip.getWidth();
		int h = ip.getHeight();
		int pixel=255<<24|0<<16|0<<8|0;
		//System.out.println("颜色:"+sColor.getRed(pixel)+"\t"+sColor.getGreen(pixel)+"\t"+sColor.getBlue(pixel));
		//CvScalar color =new CvScalar(0.0,0.0,0.0,1.0);
		for(int i=0;i<w;i++)
		{
			for(int j=0;j<h;j++)
			{
				ip.setRGB(i,j,pixel);
				//cvSet2D(ip,i,j,pixel);
			
			}
		}
	}
	 class argb
	{
		int a;
		int r;
		int g;
		int b;
		public argb(int r,int g ,int b,int a)
		{
			this.r=r;
			this.g=g;
			this.b=b;
			this.a=a;
		}
	}
	public argb Get2D(BufferedImage img,int x,int y)
	{
//		int pixel=0;
//		try{
//		pixel=img.getRGB(x, y);
//		}
//		catch(Exception e)
//		{
//			System.out.println("错误:"+x+":"+y);
//			System.exit(1);
//		}
		int pixel=img.getRGB(x, y);
		return new argb(sColor.getRed(pixel),sColor.getGreen(pixel),sColor.getBlue(pixel),sColor.getAlpha(pixel));
	}
	
	public void Set2D(BufferedImage img,int x,int y,argb l)
	{
		int rgb=l.a<<24|l.r<<16|l.g<<8|l.b;
		img.setRGB(x, y, rgb);
	}
	/**
	 * 特征训练程序，作为特征调整
	 */
	public void train(int row,int col,String str)
	{
		//因为为数值型变量
		//用bp神经网络做训练
		//修改神经网络到多分类结构
		//用简单的算法做一个基本测试
	//	image=imageTemp.clone();
		image= new BufferedImage(100, 100,BufferedImage.TYPE_INT_RGB);
		//image =opencv_core.cvCreateImage(opencv_core.cvSize(100,100),8,3);
		//setBackGround(image);
		Graphics g=image.getGraphics();
		g.fillRect(0,0,100,100);
		Graphics2D gg=(Graphics2D)g;
		gg.setBackground(Color.black);
		gg.clearRect(0, 0, 100, 100);
		image2= new BufferedImage(100, 100,BufferedImage.TYPE_INT_RGB);
		//image2 =opencv_core.cvCreateImage(opencv_core.cvSize(100,100),8,3);
		//setBackGround(image2);
		Graphics g2=image2.getGraphics();
		g2.fillRect(0,0,100,100);
		Graphics2D gg2=(Graphics2D)g2;
		gg2.setBackground(Color.black);
		gg2.clearRect(0, 0, 100, 100);
		width=100;
		height=100;
		// Point center=new Point(40,30);
		//opencv_core.cvInitFont(font,opencv_core.CV_FONT_HERSHEY_COMPLEX,0.8,0.8,0,0,CV_AA);//字体设置
		//opencv_core.cvPutText(image,str,center,font,opencv_core.CV_RGB(255,0,0));
		gg.setColor(Color.red);
		gg.setFont(font);
		gg.drawString(str, 40, 30);
  	  	System.out.println("原始:"+this.widthAlphaLower+"\t"+this.widthAlphaUpper);
  	  	System.out.println("原始:"+this.HeightAlphaLower+"\t"+this.HeightAlphaUpper);
		getOci();
		System.out.println("训练:"+this.widthAlphaLower+"\t"+this.widthAlphaUpper);
  	  	System.out.println("训练:"+this.HeightAlphaLower+"\t"+this.HeightAlphaUpper);
  	  	double[] temp=getTrainNPoint(row,col);
  	  	System.out.println("特征");
  	  	for(int i=0;i<temp.length;i++)
  	  	{
  	  		System.out.print(temp[i]+"\t");
  	  	}
  	  	System.out.println();
  	  	int index=0;
  	  	double valueMin=Double.MAX_VALUE;
  	  	for(int i=0;i<this.featureValue.size();i++)
  	  	{
  	  		double value=0.0;
  	  		for(int j=0;j<this.featureValue.get(i).length;j++)
  	  		{
  	  			value+=Math.pow(featureValue.get(i)[j]-temp[j],2.0);
  	  		}
  	  		if(value<valueMin)
  	  		{
  	  			valueMin=value;
  	  			index=i;
  	  		}
  	  	}
  	  	System.out.println("识别字为:"+this.featureList.get(index)+"\t差异值："+valueMin);
		for(int i=0;i<this.featureValue.get(index).length;i++)
		{
			System.out.print(featureValue.get(index)[i]+"\t");
		}
		System.out.println();
		
	}
	
	
	/**
	 * 特征训练程序，作为特征调整
	 */
	public double[] GetTrain(int row,int col,String str)
	{
		//因为为数值型变量
		//用bp神经网络做训练
		//修改神经网络到多分类结构
		//用简单的算法做一个基本测试
	//	image=imageTemp.clone();
		image= new BufferedImage(100, 100,BufferedImage.TYPE_INT_RGB);
		//image =opencv_core.cvCreateImage(opencv_core.cvSize(100,100),8,3);
		//setBackGround(image);
		Graphics g=image.getGraphics();
		g.fillRect(0,0,100,100);
		Graphics2D gg=(Graphics2D)g;
		gg.setBackground(Color.black);
		gg.clearRect(0, 0, 100, 100);
		image2= new BufferedImage(100, 100,BufferedImage.TYPE_INT_RGB);
		//image2 =opencv_core.cvCreateImage(opencv_core.cvSize(100,100),8,3);
		//setBackGround(image2);
		Graphics g2=image2.getGraphics();
		g2.fillRect(0,0,100,100);
		Graphics2D gg2=(Graphics2D)g2;
		gg2.setBackground(Color.black);
		gg2.clearRect(0, 0, 100, 100);
		width=100;
		height=100;
		// Point center=new Point(40,30);
		//opencv_core.cvInitFont(font,opencv_core.CV_FONT_HERSHEY_COMPLEX,0.8,0.8,0,0,CV_AA);//字体设置
		//opencv_core.cvPutText(image,str,center,font,opencv_core.CV_RGB(255,0,0));
		gg.setColor(Color.red);
		gg.setFont(font);
		gg.drawString(str, 40, 30);
  	  	System.out.println("原始:"+this.widthAlphaLower+"\t"+this.widthAlphaUpper);
  	  	System.out.println("原始:"+this.HeightAlphaLower+"\t"+this.HeightAlphaUpper);
		getOci();
		System.out.println("训练:"+this.widthAlphaLower+"\t"+this.widthAlphaUpper);
  	  	System.out.println("训练:"+this.HeightAlphaLower+"\t"+this.HeightAlphaUpper);
  	  	double[] temp=getTrainNPoint(row,col);
  	  	System.out.println("特征");
  	  	for(int i=0;i<temp.length;i++)
  	  	{
  	  		System.out.print(temp[i]+"\t");
  	  	}
  	  	return  temp;
//  	  	System.out.println();
//  	  	int index=0;
//  	  	double valueMin=Double.MAX_VALUE;
//  	  	for(int i=0;i<this.featureValue.size();i++)
//  	  	{
//  	  		double value=0.0;
//  	  		for(int j=0;j<this.featureValue.get(i).length;j++)
//  	  		{
//  	  			value+=Math.pow(featureValue.get(i)[j]-temp[j],2.0);
//  	  		}
//  	  		if(value<valueMin)
//  	  		{
//  	  			valueMin=value;
//  	  			index=i;
//  	  		}
//  	  	}
//  	  	System.out.println("识别字为:"+this.featureList.get(index)+"\t差异值："+valueMin);
//		for(int i=0;i<this.featureValue.get(index).length;i++)
//		{
//			System.out.print(featureValue.get(index)[i]+"\t");
//		}
//		System.out.println();
		
	}
	
	/**
	 * 识别
	 * imageFile 识别的图片地址
	 */
	public void test(String imageFile)
	{
		
	}
	
	/**
	 * 打印特征
	 */
	public void printFeature()
	{
		System.out.print("特征点:"+"\t");
		for(int i=0;i<this.featureKey.size();i++)
		{	
			System.out.print(featureKey.get(i)+"\t");
		}
		System.out.println();
		for(int i=0;i<this.featureValue.size();i++)
		{
			System.out.print(featureList.get(i)+":值:"+"\t");
			
			for(double da:featureValue.get(i))
			{
				System.out.print(da+"\t");
			}
			System.out.println();
		}
		
	}
	public void WriteFeature(String fileName) throws IOException
	{
		System.out.print("特征点:"+"\t");
	     
        File f = new File(fileName);      
        if (!f.exists())   
        {       
            f.createNewFile();      
        }
        else
        {
        	
        }
        OutputStreamWriter write =new OutputStreamWriter(new FileOutputStream(f,false),"utf-8");      
        BufferedWriter writer=new BufferedWriter(write);
		for(int i=0;i<this.featureKey.size();i++)
		{	
			System.out.print(featureKey.get(i)+"\t");
		}
		System.out.println();
		for(int i=0;i<this.featureValue.size();i++)
		{
			System.out.print(featureList.get(i)+":值:"+"\t");
			writer.write(featureList.get(i)+"\t");
			for(double da:featureValue.get(i))
			{
				System.out.print(da+"\t");
				writer.write(da+"\t");
			}
			writer.write("\n");
			System.out.println();
		}
		writer.flush();
		writer.close();
		System.out.println("写入完成");
		
	}
	/**
	 * 测试执行
	 * @param row
	 * @param col
	 */
	public void run(int row,int col)
	{
		File file=new File("./data/chinessRecognition/chiness.txt");
		int featerlength=9;
		if(file.exists())
		{
			System.out.println("读取资源文件");
			//存在则读取资源
			BufferedReader reader = null;
			try{
			 
			 InputStreamReader read = new InputStreamReader(new FileInputStream(file),"utf-8");  
	         reader = new BufferedReader(read);
	         String temp=null;
	         while((temp=reader.readLine())!=null)
	         {
	        	 String[] ln=temp.split("\t");
	        	 if(ln.length==featerlength)
	        	 {
	   
	        		 featureList.add(ln[0]);
	        		 double[] ld=new double[featerlength-1];
	        		 for(int i=1;i<featerlength;i++)
	        		 {
	        		
	        			 ld[i-1]=Double.parseDouble(ln[i]);
	        		 }
	        		 featureValue.add(ld);
	        	 }
	        	 else
	        	 {
	        		 System.out.println("特征不同");
	        		 System.exit(1);
	        		 break;
	        	 }
	         }
			}
			catch(Exception e)
			{
				System.out.println("读取资源错误");
			}
			finally
			{
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else
		{
			getFeature(row,col);
			trainNPointString(row,col);
			printFeature();
			try {
				WriteFeature("./data/chinessRecognition/chiness.txt");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	
	
	public static void main(String[] args) {
		
		ChinessRecognition alr=new ChinessRecognition();
		alr.run(2,3);
		//alr.train(2,3,"差");
		System.out.println("读取数据");
//LinkedList<double[]> data=MatrixReadSource.ReadToLinkedListD("data/cluster01.txt");
		LinkedList<String[]> data= MatrixReadSource.readToLinkedListString("./data/chinessRecognition/chiness.txt");
		System.out.println("读取数据成功");
		LinkedList<double[]> datainput=new LinkedList<double[]>();//输入
		LinkedList<String[]> dataoutput=new LinkedList<String[]>();//输出
		for(int i=0;i<data.size();i++)
		{
			if(i%1000==0)
			{
				System.out.println(i);
			}
			if(i>100)
			{
				break;
			}
			String[] str=new String[1];
			double[] dou=new double[6];
			str[0]=data.get(i)[0];
			for(int j=1;j<7;j++)
			{
			dou[j-1]=Double.parseDouble(data.get(i)[j]);
			}
			datainput.add(dou);
			dataoutput.add(str);
		}
		
		System.out.println("调用ann");
//	    int y_len=30;//隐层变量数量
//	    int i_len=datainput.get(0).length;//输入变量数量
//	    double error=0.01;//误差
//	    double alp=0.4;//学习率
//	    double impulse11=0.5;
//	    
	    //调用第二个初始化new BpAnnN(y_len, alp, impulse, i_len);
	   // BpAnnN bp_test=new BpAnnN(y_len,alp,impulse11,i_len);
	    //执行将输出标准化过程
//	    System.out.println("执行输出标准化");
//	    bp_test.normalizationString(dataoutput);
//	    System.out.println("执行输入标准化");
//	    bp_test.normalizationDouble(datainput);
	    
	    
	
	    double sd=0.00001;//设定分类精度
	    //修改不同的C和alp初始值 可以获取不同的效果
	    double C=0.1;//对损失权重的c赋值
	    double wold=0,wnew=0;//更新前w(a)，更新后w（a）
	    double alp=0.1;//初始化apha
	    double std=1;//std=1//kernal方差值//与整体方差有关
	    //将输出更改为1,-1；当前输入为10
	        std=0.3;
		    SvmBaseN svm=new SvmBaseN(datainput,dataoutput,sd,std,C,alp);//初始化svm类
		    //svm.kernalPolynomialCompute(2.0);//多项式核
		    svm.train("Gaussian");
	    ////标准化	    
	    System.out.println("训练数据");
	   // bp_test.trainData(datainput,dataoutput,0.5,0.01,2000);
	    //bp_test.trainDataAll(datainput,dataoutput,0.5,0.01,2000);
	    System.out.println("获取信息特征");
	    double[] temp=alr.GetTrain(2,3,"依");
	    System.out.println("输出测试：依");
	   // String output=bp_test.predict(temp);
	    String output=svm.predict(temp,1);
	    System.out.println("预测值:"+output);
	    
	    System.out.println("获取信息特征");
	    temp=alr.GetTrain(2,3,"來");
	    System.out.println("输出测试:來");
	     output=svm.predict(temp,1);
	     System.out.println("预测值:"+output);
	     
	    System.out.println("获取信息特征");
	    temp=alr.GetTrain(2,3,"侍");
	    System.out.println("输出测试:侍");
	    output=svm.predict(temp,1);
	     System.out.println("预测值:"+output);
		
		
//		JFrame app = new JFrame();
//		app.setSize(800, 600);
//		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
//		app.setLocation(dimension.width / 4, dimension.height / 4);
//		app.add(new ImagePanel(alr.image2));
//		app.setVisible(true);
		

		
		
		//\u4e00-\u9fa5
		
//		System.out.println("\u4e00");
//		//Integer.valueOf("FFFF",16).toString() 
//		System.out.println(Integer.toHexString(Integer.parseInt(Integer.valueOf("4e00",16).toString())+16));
//		String sln=Integer.toHexString(Integer.parseInt(Integer.valueOf("4e00",16).toString())+16);
//		System.out.println("\\u"+Integer.parseInt(Integer.valueOf("4e00",16).toString()));
//		String  temp2="4e00";
//		for(int i=19968;i<=40869;i++)
//		{
//		//	System.out.println(new String(Integer.toHexString(Integer.valueOf(temp2,16)+i),"utf-8"));
//				//int temp=Integer.parseInt(Integer.valueOf(temp2,16).toString())+i;
//			System.out.print((char)(i));
//		}
	}
}
