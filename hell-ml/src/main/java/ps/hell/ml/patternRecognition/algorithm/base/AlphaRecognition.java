package ps.hell.ml.patternRecognition.algorithm.base;

import static com.googlecode.javacv.cpp.opencv_core.CV_AA;
import static com.googlecode.javacv.cpp.opencv_core.cvGet2D;
import static com.googlecode.javacv.cpp.opencv_core.cvSet2D;

import java.util.LinkedList;

import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.CvFont;
import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

/**
 * 英文字母识别
 * @author Administrator
 *
 */
public class AlphaRecognition {
	
	/**
	 * 基础图片
	 */
	public IplImage image;
	
	public IplImage image2;
	CvFont font=new CvFont();
	
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
	
	public AlphaRecognition()
	{
		opencv_core.cvInitFont(font,opencv_core.CV_FONT_HERSHEY_COMPLEX,0.8,0.8,0,0,CV_AA);//字体设置
	}
	/**
	 * 获取26字母特征
	 * inputDirector 输入图片目录
	 * outputFile 输出特征文件目录
	 */
	public void getFeature(int row,int col)
	{
		  CvPoint center=new CvPoint(10,10);
	    
	      for(int i=65;i<123;i++)
	      {
	    	  //字母
	    	  if(i<97 && i>90)
	    	  {
	    		  continue;
	    	  }
	    	  center=new CvPoint(10,20);
	    	  char ln=(char)i;
	    	//  System.out.println(ln);
	    	  featureList.add(Character.toString(ln));
	    	  image =opencv_core.cvCreateImage(opencv_core.cvSize(width,height),8,3);
	    	  setBackGround(image);
	    	  image2 =opencv_core.cvCreateImage(opencv_core.cvSize(width,height),8,3);
	    	  setBackGround(image2);
	    	  opencv_core.cvPutText(image,Character.toString(ln),center,font,opencv_core.CV_RGB(255,0,0));
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
				CvScalar color=cvGet2D(image,i,j);
				if(color.red()>limitColor)
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
				CvScalar color=cvGet2D(image,i,j);
				if(color.red()>limitColor)
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
				CvScalar color=cvGet2D(image,j,i);
				if(color.red()>limitColor)
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
				CvScalar color=cvGet2D(image,j,i);
				if(color.red()>limitColor)
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
	 * @param count 分割成几份
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
				CvScalar color=cvGet2D(image,i,j);
				//System.out.print(color.red()+"\t");
				if(color.red()>limitColor)
				{
					cvSet2D(image2,i,j,color);
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
	 * @param count 分割成几份
	 * 分割为几行，几列
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
				CvScalar color=cvGet2D(image,i,j);
				//System.out.print(color.red()+"\t");
				if(color.red()>limitColor)
				{
					CvScalar color1 =new CvScalar(0.0,0.0,255.0,1.0);
					cvSet2D(image2,i,j,color1);
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
  					  cvSet2D(image2,ii,jj,new CvScalar(0.0,255.0,0.0,1.0));
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
	public void setBackGround(IplImage ip)
	{
		int w= ip.width();
		int h = ip.height();
		CvScalar color =new CvScalar(0.0,0.0,0.0,1.0);
		for(int i=0;i<w;i++)
		{
			for(int j=0;j<h;j++)
			{
				cvSet2D(ip,i,j,color);
			}
		}
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
		
		image =opencv_core.cvCreateImage(opencv_core.cvSize(100,100),8,3);
		setBackGround(image);
		image2 =opencv_core.cvCreateImage(opencv_core.cvSize(100,100),8,3);
		setBackGround(image2);
		width=100;
		height=100;
		 CvPoint center=new CvPoint(40,30);
		opencv_core.cvInitFont(font,opencv_core.CV_FONT_HERSHEY_COMPLEX,0.8,0.8,0,0,CV_AA);//字体设置
		opencv_core.cvPutText(image,str,center,font,opencv_core.CV_RGB(255,0,0));
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
	/**
	 * 测试执行
	 * @param row
	 * @param col
	 */
	public void run(int row,int col)
	{
		
		getFeature(row,col);
		trainNPointString(row,col);
		printFeature();
	}
	
	
	public static void main(String[] args) {
		//IplImage image1=cvLoadImage();
		
//		IplImage image1=opencv_core.cvCreateImage(opencv_core.cvSize(20,24),8,3);
//		  CvFont font=new CvFont();//初始化字体
//		  CvPoint center=new CvPoint(10,10);
//	      opencv_core.cvInitFont(font,opencv_core.CV_FONT_HERSHEY_COMPLEX,0.8,0.8,0,0,CV_AA);//字体设置
//	      for(int i=65;i<123;i++)
//	      {
//	    	  if(i<97 && i>90)
//	    	  {
//	    		  continue;
//	    	  }
//	    	  center=new CvPoint(2,20);
//	    	  char ln=(char)i;
//	    	  System.out.println(ln);
//	    	  opencv_core.cvPutText(image1,Character.toString(ln),center,font,opencv_core.CV_RGB(255,0,0));
//	    	  break;
//	      }
//	      cvNamedWindow("test",1);
//		  cvShowImage("test",image1);
//		  cvWaitKey(0);
		
		AlphaRecognition alr=new AlphaRecognition();
		alr.run(2,3);
		alr.train(2,3,"p");
		//IplImage img3 =opencv_core.cvCreateImage(opencv_core.cvSize(100,100),8,3);
		//alr.setBackGround(img3);
		//cvNamedWindow("test",1);
		//cvShowImage("test",alr.image2);
		//cvNamedWindow("test2",1);
		//cvShowImage("test2",alr.image);
		//cvWaitKey(0);
		
	}
}
