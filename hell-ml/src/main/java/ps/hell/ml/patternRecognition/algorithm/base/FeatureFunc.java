package ps.hell.ml.patternRecognition.algorithm.base;

import java.awt.Color;
import java.awt.Font;
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
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_ml;
import com.googlecode.javacv.cpp.opencv_core.*;
import com.googlecode.javacv.cpp.opencv_ml.*;
/**
 * 特征计算
 * @author Administrator
 *
 */
public class FeatureFunc {
	
	/**
	 * 基础图片
	 */
	public BufferedImage image;
	public ColorModel sColor=ColorModel.getRGBdefault();
	public int width=50;
	public int height=50;
	public int row;
	public int col;
	public int count;
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
	
	public FeatureFunc(int row,int col,int count)
	{
		this.row=row;
		this.col=col;
		this.count=count;
		 this.font=new Font("黑体",Font.BOLD+Font.ITALIC,20);
	}
	

	/**
	 * 训练特征
	 * @param row行数
	 * @param col列数
	 */
	public void FeatureTrain(BufferedImage img,String name)
	{
	   this.image=img;
	   //特证名
	   featureList.add(name);
	   this.width=image.getWidth();
	   this.height=image.getHeight();
	   //截区域
	   getOci();
	   //特征编号
	   trainNPointString();
	 //训练特征
	   double[] feature1= FeatureNPoint();
	   //特征写入内存中
	   featureValue.add(feature1);
	}
	
	/**
	 * 测试特征
	 * @param row行数
	 * @param col列数
	 */
	public double[] FeatureTest(BufferedImage img)
	{
	   this.image=img;
	   this.width=img.getWidth();
	   this.height=image.getHeight();
	   getOci();
	   //可多个特征组合
	   double[] feature1= FeatureNPoint();
	   return feature1;
	}
	/**
	 * 测试特征
	 * @param image4
	 * @param row 行数
	 * @param col 列数
	 * @return
	 */
	public double[] getTestFeature(BufferedImage image4)
	{
	     image = image4;
	     width=image4.getWidth();
	     height=image4.getHeight();
	     getOci();
	  	 double[] feature1= FeatureNPoint();
	  	 return feature1; 
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
		int wh_count=0;
		for(int i=0;i<width;i++)
		{
			flag=false;
			for(int j=0;j<height;j++)
			{
				if(sColor.getRed(image.getRGB(i,j))<limitColor)
				{
					
				//	widthAlphaLower=i-1;
					flag=true;
					break;
				}
				
			}
			if(flag==true)
			{
				wh_count++;
				if(wh_count==2)
				{	
					widthAlphaLower=i-1;
					break;
				}
			}
			else
			{
				wh_count=0;
			}
		}
		flag=false;
		wh_count=0;
		for(int i=width-1;i>=0;i--)
		{
			flag=false;
			for(int j=0;j<height;j++)
			{
				if(sColor.getRed(image.getRGB(i,j))<limitColor)
				{
						flag=true;
						break;
				}
			}
			if(flag==true)
			{
				wh_count++;
				if(wh_count==2)
				{	
					widthAlphaUpper=i+1;
					break;
				}
			}
			else
			{
				wh_count=0;
			}
		}
		flag=false;
		wh_count=0;
		for(int i=0;i<height;i++)
		{
			flag=false;
			for(int j=widthAlphaLower;j<=widthAlphaUpper;j++)
			{
				if(sColor.getRed(image.getRGB(j,i))<limitColor)
				{
						flag=true;
						break;
				}
			}
			if(flag==true)
			{
				wh_count++;
				if(wh_count==2)
				{	
					HeightAlphaLower=i-1;
					break;
				}
			}
			else
			{
				wh_count=0;
			}
		}
		flag=false;
		wh_count=0;
		for(int i=height-1;i>=0;i--)
		{
			flag=false;	
			for(int j=widthAlphaLower;j<=widthAlphaUpper;j++)
			{
				
				if(sColor.getRed(image.getRGB(j,i))<limitColor)
				{
			//		System.out.println(j+"\t"+i+"\t"+width+"\t"+height+"\t"+wh_count);
						flag=true;
						break;
				}
			}
			if(flag==true)
			{
				wh_count++;
				if(wh_count==2)
				{	
					HeightAlphaUpper=i+1;
					break;
				}
			}
			else
			{
				wh_count=0;
			}
		}
		//System.out.println("执行oci:"+"\t"+widthAlphaLower+"\t"+widthAlphaUpper);
		//System.out.println("执行oci:"+"\t"+HeightAlphaLower+"\t"+HeightAlphaUpper);
		Color col=new Color(255,0,0);
		for(int i=widthAlphaLower;i<=widthAlphaUpper;i++)
		{
		//	image.setRGB(i, HeightAlphaLower, col.getRGB());
		//	image.setRGB(i, HeightAlphaUpper, col.getRGB());
		}
		for(int i=HeightAlphaLower;i<=HeightAlphaUpper;i++)
		{
		//	image.setRGB(widthAlphaLower, i, col.getRGB());
		//	image.setRGB(widthAlphaUpper, i, col.getRGB());
		}
//		cvShowImage("test",IplImage.createFrom(image));
//		cvNamedWindow("test",1);
//		cvWaitKey(0);
		
	//	System.out.println("widthAlphaLower"+widthAlphaLower+"\t"+widthAlphaUpper);
	//	System.out.println("HeightAlphaLower"+HeightAlphaLower+"\t"+HeightAlphaUpper);
	}

	
	/**
	 * 将pointn对应元素存入特征key中
	 * @param row
	 * @param col
	 * @param count其他特征数
	 */
	public void trainNPointString()
	{
		for(int i=0;i<count;i++)
		{
			featureKey.add("Point"+(i+1));
		}
	}
	/**
	 * 
	 * @param count 分割成几份
	 * 分割为几行，几列
	 * 并取其中的存在点的数量的比率
	 * 提取该特征
	 */
	public double[] FeatureNPoint()
	{
		int wV=1;
		int hV=1;
		//特征
		double[] value=new double[count];
		int indexSqare=(widthAlphaUpper-widthAlphaLower+1)*(HeightAlphaUpper-HeightAlphaLower+1);
		//System.out.println("indexSqare值:"+indexSqare);
		int All=0;
		int left=0;
		int right=0;
		int up=0;
		int down=0;
		for(int i=widthAlphaLower;i<=widthAlphaUpper;i++)
		{
			//按行分割
			wV=(int)((i-widthAlphaLower)*1.0/(widthAlphaUpper-widthAlphaLower+2)*col+1);
			for(int j=HeightAlphaLower;j<=HeightAlphaUpper;j++)
			{
				//存储宽度
				hV=(int)((j-HeightAlphaLower)*1.0/(HeightAlphaUpper-HeightAlphaLower+2)*row);
			
				int index=hV*col+wV;

				if(sColor.getRed(image.getRGB(i,j))>limitColor)
				{
					if(i<(widthAlphaLower+widthAlphaUpper)/2)
					{
						left++;
					}
					else
					{
						right++;
					}
					if(j<(HeightAlphaLower+HeightAlphaUpper)/2)
					{
						up++;
					}
					else
					{
						down++;
					}
					All++;
					value[index-1]+=1.0;

				}	
			}
		}
		for(int i=0;i<col*row;i++)
		{
			value[i]/=All;
		}
		value[col*row]=(widthAlphaUpper-widthAlphaLower)*1.0;
		value[col*row+1]=(HeightAlphaUpper-HeightAlphaLower)*1.0;
		return value;
	}
	
	public double[] FeatureNPoint2()
	{
		int wV=1;
		int hV=1;
		//特征
		double[] value=new double[count];
		int indexSqare=(widthAlphaUpper-widthAlphaLower+1)*(HeightAlphaUpper-HeightAlphaLower+1);
		//System.out.println("indexSqare值:"+indexSqare);
		int All=0;
		int left=0;
		int right=0;
		int up=0;
		int down=0;
		for(int i=widthAlphaLower;i<=widthAlphaUpper;i++)
		{
			//按行分割
			wV=(int)((i-widthAlphaLower)*1.0/(widthAlphaUpper-widthAlphaLower+2)*col+1);
			for(int j=HeightAlphaLower;j<=HeightAlphaUpper;j++)
			{
				//存储宽度
				hV=(int)((j-HeightAlphaLower)*1.0/(HeightAlphaUpper-HeightAlphaLower+2)*row);
			
				int index=hV*col+wV;

				if(sColor.getRed(image.getRGB(i,j))>limitColor)
				{
					if(i<(widthAlphaLower+widthAlphaUpper)/2)
					{
						left++;
					}
					else
					{
						right++;
					}
					if(j<(HeightAlphaLower+HeightAlphaUpper)/2)
					{
						up++;
					}
					else
					{
						down++;
					}
					All++;
					value[index-1]+=1.0;

				}	
			}
		}
		for(int i=0;i<col*row;i++)
		{
			value[i]/=All;
		}
		//value[col*row]= (widthAlphaUpper-widthAlphaLower)*1.0/(HeightAlphaUpper-HeightAlphaLower)/4;
//		value[col*row+1]= All*1.0/(HeightAlphaUpper-HeightAlphaLower)/(widthAlphaUpper-widthAlphaLower);
//		value[col*row+2]=left*1.0/right/4;
//		value[col*row+3]=up*1.0/down/4;
		value[col*row]=(widthAlphaUpper-widthAlphaLower)*1.0;
		value[col*row+1]=(HeightAlphaUpper-HeightAlphaLower)*1.0;
		//value[col*row+6]=All;
		//将对应值 添加入 featureValue中
		return value;
	}
	

	public void setBackGround(BufferedImage ip)
	{
		int w= ip.getWidth();
		int h = ip.getHeight();
		int pixel=255<<24|0<<16|0<<8|0;
		for(int i=0;i<w;i++)
		{
			for(int j=0;j<h;j++)
			{
				ip.setRGB(i,j,pixel);		
			}
		}
	}
	

	
	/**
	 * 打印特征
	 */
	public void printFeature()
	{
		//System.out.print("特征点:"+"\t");
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
	public void WriteFeature(String filePath) throws IOException
	{
		System.out.print("特征点:"+"\t");
	     
        File f = new File(filePath);      
        if (!f.exists())   
        {       
            f.createNewFile();      
        }
        else
        {
        	System.out.println("文件已存在，请检查是否需要删除并手工删除");
        	return;
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
//			if(featureValue.get(i)[featureValue.get(i).length-1]>70)
//			{
//				continue;
//			}
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
	 * 将特征信息写入文件中
	 */
	public void Print(String filePath)
	{
		try {
			WriteFeature(filePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 读取特征文件
	 * @param filePath
	 * @param featureName 特征名字位置
	 * @param Start 特征开始位置 从0开始
	 * @param end 结束位置
	 */
	public boolean ReadFeatureSource(String filePath,int featureName,int start,int end)
	{
		File file=new File(filePath);
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
	        	// System.out.println(ln.length+"\t"+count);
	        	try
	        	 {
	        		 featureList.add(ln[featureName]);
	        		 System.out.print(ln[featureName]);
	        		 double[] ld=new double[count];
	        		 for(int i=start;i<=end;i++)
	        		 {
	        			 System.out.print(ln[i]+"\t");
	        			 ld[i-1]=Double.parseDouble(ln[i]);
	        		 }
	        		 System.out.println();
	        		 featureValue.add(ld);
	        	 }
	        	 catch(Exception e)
	        	 {
	        		 System.out.println("读取错误");
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
			return true;
		}
		else
		{
			return false;
		}
	}

/**
 * svm训练库调用
 * @return
 */
	public double FLT_EPSILON=1.19209290E-07F;
	
	public LinkedList<CvSVM> svmP=new LinkedList<CvSVM>();
	LinkedList<String> name=new LinkedList<String>();
	class wh implements Comparable
	{
		int w;
		int h;
		public wh(int w,int h)
		{
			this.w=w;
			this.h=h;
		}
		public int getW()
		{
			return this.w;
		}
		public int getH()
		{
			return this.h;
		}
		@Override
		public int compareTo(Object o){  
			wh other = (wh) o;  
	  
	        // 先遵守name属性排序  
	        if(this.w>other.getW())  
	            return 1;  
	        if(this.w<other.getW()) 
	            return -1;  
	  
	        // 在遵守age属性排序  
	        if(this.h > other.getH())  
	            return 1;  
	        if(this.h < other.getH())  
	            return -1;  
	        return 0;  
	    }  
		@Override
		public int hashCode()
		{
			return w*100+h;
		}

		@Override
		public boolean equals(Object o)
		{
			wh other = (wh) o; 
			if(this.w==other.getW()&& this.h==other.getH())
			{
				return true;
			}
			else
			{
				return false;
			}
		}
	}
	class wh_val
	{
		LinkedList<double[]> value=new LinkedList<double[]>();
		LinkedList<String> name=new LinkedList<String>();
		LinkedList<Integer> index=new LinkedList<Integer>();
		CvSVM svm=new CvSVM();
		public wh_val()
		{
			
		}
		public wh_val(LinkedList<double[]> value,LinkedList<String> name)
		{
			this.value=value;
			this.name=name;
		}
		public void add(double[] data1,String name1)
		{
			this.value.add(data1.clone());
			boolean flag=false;
			for(int i=0;i<name.size();i++)
			{
				if(name1.equals(name.get(i)))
				{
					flag=true;
					this.index.add(i);
					break;
				}
			}
			if(flag==false)
			{
				this.index.add(name.size());
				this.name.add(name1);
			}
		}
		@Override
		public boolean equals(Object o)
		{
			wh_val other = (wh_val) o; 
			if(this.index.size()==other.getIndexSize())
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		public int getIndexSize()
		{
			return this.index.size();
		}
	}
	public Map<wh,wh_val> map=new HashMap<wh,wh_val>();
	public void svmTrain()
	{
		CvTermCriteria criteria =opencv_core.cvTermCriteria(opencv_core.CV_TERMCRIT_EPS, 1000,FLT_EPSILON);
		CvSVMParams	  param1 =new CvSVMParams(CvSVM.C_SVC, CvSVM.RBF, 10.0, 8.0, 1.0, 10.0, 0.5, 0.1, null, criteria);

		for(int i=0;i<featureList.size();i++)
		{
		//	System.out.println(featureValue.get(i).length);
			int w=(int)(featureValue.get(i)[count-2]);
			int h=(int)(featureValue.get(i)[count-1]);
			System.out.print(w+":"+h+"\t");
			boolean flag=false;
			double[] da=new double[count-2];
			for(int j=0;j<count-2;j++)
			{
				da[j]=featureValue.get(i)[j];
			}
			wh zp=new wh(w,h);
			if(map.containsKey(zp))
			{
				wh_val l2=map.get(zp);
				l2.add(da.clone(),featureList.get(i));
				map.put(zp, l2);
			}
			else
			{
				wh_val l2=new wh_val();
				l2.add(da.clone(),featureList.get(i));
				map.put(zp, l2);
			}
		}
		//System.out.println();
		//System.out.println("map:"+map.size());
		//Set<Entry<wh, LinkedList<double[]>>> iter=map.entrySet();
		int znCount=-1;
		//System.out.println(znCount+"\t"+map.size());
		for(Entry<wh,wh_val> data3:map.entrySet())
		{
			znCount++;
			//System.out.println(znCount);
			wh key=data3.getKey();
			wh_val val=data3.getValue();
			//System.out.println(key.w+"\t"+key.h);
			if(val.name.size()<=1)
			{
				continue;
			}
			CvMat data_mat=CvMat.create(val.index.size(),count-2,opencv_core.CV_32FC1);
			CvMat res_mat=CvMat.create(val.index.size(), 1,opencv_core.CV_32FC1);
			int q=-1;
			for(double[] dp:val.value)
			{
				//System.out.println(dp.length);
				q++;
				for(int e=0;e<dp.length;e++)
				{
				//	System.out.println(q*(count-2)+e+"\t"+dp.length);
					data_mat.put(q*(count-2)+e,dp[e]);
				}
			}
			for(int i=0;i<val.index.size();i++)
			{
				res_mat.put(i,0,val.index.get(i));
			}
			System.out.println(val.value.size()+"\t长:"+val.index.size());
			System.out.println(znCount+":开始:"+new Date());
			val.svm.train(data_mat,res_mat,null, null, param1);
			System.out.println(znCount+":结束:"+new Date());
			//val.index=null;
			//val.value=null;
			map.put(key, val);
			
		}
		/*
		 CvSVM svm=new CvSVM();
		CvSVMParams param1=null;
		CvTermCriteria criteria;
		CvMat data_mat, res_mat;
		int[] res=new int[featureList.size()];

		double[] data=new double[featureList.size()*count];
		for(int i=0;i<featureList.size();i++)
		{
			System.out.println("1:"+i+"\t"+name.size());
			boolean flag=false;
			for(int j=0;j<name.size();j++)
			{
				if(featureList.get(i).equals(name.get(j)))
				{
					flag=true;
					res[i]=j+1;
				}
			}
			if(flag==false)
			{
				name.add(featureList.get(i));
				res[i]=name.size();
			}
			for(int j=0;j<featureValue.get(i).length;j++)
			{
				data[i*count+j]=featureValue.get(i)[j];
			}
		}

//		CvMat.cvInitMatHeader(data_mat,featureList.size(), 2, opencv_core.CV_32FC1, data);
		data_mat=CvMat.create(featureList.size(), count,opencv_core.CV_32FC1);
		for(int i=0;i<featureList.size();i++)
		{
			System.out.println("2:"+i);
			for(int j=0;j<featureValue.get(i).length;j++)
			{
				data_mat.put(i,j, featureValue.get(i)[j]);
			}
		}
		//opencv_ml.cvInitMatHeader (res_mat,featureList.size(), 1, opencv_core.CV_32SC1, res);
		res_mat=CvMat.create(featureList.size(), 1,opencv_core.CV_32FC1);
		for(int i=0;i<res.length;i++)
		{
			res_mat.put(i,0,res[i]);
		}
		
		  criteria =opencv_core.cvTermCriteria(opencv_core.CV_TERMCRIT_EPS, 1000,FLT_EPSILON);
		  param1 =new opencv_ml.CvSVMParams(CvSVM.C_SVC, CvSVM.RBF, 10.0, 8.0, 1.0, 10.0, 0.5, 0.1, null, criteria);
		  System.out.println("训练开始");
		  svm.train(data_mat,res_mat,null, null, param1);
		  System.out.println("训练结束");
		  */
		

	}
	public String svmTest(double[] data)
	{
		  CvMat testMat=CvMat.create(1,count-2,opencv_core.CV_32FC1);
			for(int i=0;i<count-2;i++)
			{
				testMat.put(0,i,data[i]);
			}
			int w=(int)data[count-2];
			int h=(int)data[count-1];
			//System.out.println("长宽:"+w+"\t"+h);
			boolean flag=false;
		for(Entry<wh,wh_val> data2:map.entrySet())
		{
			
			wh key=data2.getKey();
			wh_val val2=data2.getValue();
//			for(int j=0;j<val2.name.size();j++)
//			{
//				System.out.print(val2.name.get(j));
//			}
//			System.out.println();
		//	System.out.println("长宽:"+key.w+"\t"+key.h);
			if(key.w==w && key.h==h)
			{
				flag=true;
				wh_val val=data2.getValue();
				try{
				double ret=val.svm.predict(testMat,true);
				System.out.println("正常:"+val.name.get((int)ret));
				return val.name.get((int)ret);
				
				}
				catch(Exception e)
				{
					//System.out.println("异常:"+"\t");
//					for(int j=0;j<val.name.size();j++)
//					{
//						System.out.print(val.name.get(j));
//					}
//					System.out.println();
				//	System.out.println(val.name.get(0));
					return val.name.get(0);
				}
			}
			
		}
		System.out.println("&");
		return "&";
		
		
//		  CvMat testMat=CvMat.create(1,data.length,opencv_core.CV_32FC1);
//			for(int i=0;i<data.length;i++)
//			{
//				testMat.put(0,i,data[i]);
//			}
//		  
//		  double ret=svm.predict(testMat,true);
//		  System.out.println(name.get((int)ret));
//		  return name.get((int)ret);
	}
}


