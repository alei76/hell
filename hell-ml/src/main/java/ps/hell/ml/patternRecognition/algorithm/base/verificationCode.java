package ps.hell.ml.patternRecognition.algorithm.base;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import com.googlecode.javacv.cpp.opencv_core.IplImage;
import static com.googlecode.javacv.cpp.opencv_highgui.cvNamedWindow;
import static com.googlecode.javacv.cpp.opencv_highgui.cvShowImage;
import static com.googlecode.javacv.cpp.opencv_highgui.cvWaitKey;  
/**
 * 验证码识别最终版本
 * @author sx
 *
 */
public class verificationCode {

	public  ColorModel sColor=ColorModel.getRGBdefault();
	public  int row=2;
	public  int col=2;
	public int featureName;
	public String filePath;
	public int start;
	public int end;
	
	/**
	 * 特征库
	 */
	public  FeatureFunc featureFunc;
	/**
	 * 
	 * @param filePath 特征源地址
	 * @param featureName 特证明编号 从0开始
	 * @param start 特征 开始
	 * @param end 特征结束
	 * @param row 行数
	 * @param col 列数
	 */
	public verificationCode(String filePath,int featureName,int start,int end,int row,int col)
	{
		this.filePath=filePath;
		this.row=row;
		this.col=col;
		this.featureName=featureName;
		this.start=start;
		this.end=end;
		featureFunc=new FeatureFunc(row,col,end-start+1);
		boolean readOk=featureFunc.ReadFeatureSource(filePath,featureName,start, end);
		if(!readOk)
		{
			//读取图片生成新的特征库
			readTrainSample("./verificationCodeImage/image1");
			readTrainSample("./verificationCodeImage/image2");
			readTrainSample("./verificationCodeImage/image_modify_first");//为一级重要目录
			readTrainSample("./verificationCodeImage/image_modify_new");//真实误差修正目录
			readTrainSample("./verificationCodeImage/image_modify_Second");//为错误检测目录
			readTrainSample("./verificationCodeImage/image_true");//正确目录
			//featureFunc.Print(filePath);
		}
		
		featureFunc.svmTrain();
	}
	/**
	 * 获取灰度值
	 * @param pixel
	 * @return
	 */
	public  int getYe(int pixel)
	{
		return (int) (0.299*sColor.getRed(pixel)+0.587*sColor.getGreen(pixel)+0.114*sColor.getBlue(pixel));
	}
	
	/**
	 * 获取样本内容
	 * @param direction 训练样本目录
	 */
	public void readTrainSample(String direction)
	{
		File file=new File(direction);
		File[] files=file.listFiles();
		int znp=0;
		if(files==null)
		{
			System.out.println("读取目录无效:"+direction);
			return ;
		}
		for(File f:files)
		{
			znp++;
			BufferedImage image=null;
			try {
				 image= ImageIO.read(f);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}
			String name=f.getName().substring(0,f.getName().indexOf("."));
			//System.out.println(l);
			pictureProcessingTrain(image,name);
//			IplImage ipl=IplImage.createFrom(limage);
//			cvShowImage("test",ipl);
//			cvNamedWindow("test",1);
//			cvWaitKey(0);
//			if(znp==300)
//			{
//				
//				break;
//			}
		}
	
	}
	/**
	 * 读取测试样本
	 * @param image 输入图片
	 * 输出 检测后的文件名
	 */
	public String readTestSample(BufferedImage image)
	{
			String computeName=pictureProcessingTest(image);
		 
		    return computeName;

	}
	
/**
 * 训练图像 处理过程
 * @param image
 * @param name
 */
	public void pictureProcessingTrain(BufferedImage image,String name)
	{
		/**
		 * 图像滤波
		 */
		BufferedImage image2=Filter(image);
		//执行切割器
		LinkedList<int[]> splitName=SplitImage.SplitHistogram(image2,0,4);
		if(splitName.size()!=4)
		{
			System.out.println("切词错误");
		}
		else
		{
//			for(int[] ll:splitName)
//			{
//				if(ll[3]>40 ||ll[3]<10 ||ll[3]>40||ll[3]<10)
//				{
//					System.out.println("切词错误:");
//				}
//			}
			System.out.println("识别文件名"+name);
			int l=-1;
			try{
				
			for(int[] ll:splitName)
			{
				l++;
			//	System.out.println("切割:"+ll[0]+"\t"+ll[1]+"\t"+ll[2]+"\t"+ll[3]);
				BufferedImage image4=image2.getSubimage(ll[0],ll[1],ll[2],ll[3]);
//				ipl=IplImage.createFrom(image4);
//				cvShowImage("test",ipl);
//				cvNamedWindow("test",1);
//				cvWaitKey(0);
				featureFunc.FeatureTrain(image4,""+name.charAt(l));
				
			}
			}
			catch(Exception e)
			{
				System.out.println("特征识别读取错误文件名为:"+name);
			}
			
		}
	}
/**
 * 测试图像 处理过程
 * @param image
 * @param name
 */
	public String pictureProcessingTest(BufferedImage image)
	{
		//String computeName="image_";
		String computeName="";
		/**
		 * 图像滤波
		 */
		BufferedImage image2=Filter(image);
		//执行切割器
		LinkedList<int[]> splitName=SplitImage.SplitHistogram(image2,0,4);
		if(splitName.size()!=4)
		{
			System.out.println("切词错误");
		}
		else
		{
			for(int[] ll:splitName)
			{
//				if(ll[3]>40 ||ll[3]<10 ||ll[3]>40||ll[3]<10)
//				{
//					System.out.println("切词错误:");
//				}
			}
			int l=-1;
			for(int[] ll:splitName)
			{
				l++;
				//System.out.println("切割:"+ll[0]+"\t"+ll[1]+"\t"+ll[2]+"\t"+ll[3]);
				BufferedImage image4=image2.getSubimage(ll[0],ll[1],ll[2],ll[3]);
				IplImage ipl=IplImage.createFrom(image4);
//				cvShowImage("test",ipl);
//				cvNamedWindow("test",1);
//				cvWaitKey(0);
				//获取测试值
				double[] featureTestValue=featureFunc.FeatureTest(image4);
				String Nametest=featureFunc.svmTest(featureTestValue);
				computeName+=Nametest;
			}
		}
		
		
		return computeName;
	}
	/**
	 * 图像滤波
	 * @param image
	 */
	public static BufferedImage Filter(BufferedImage image)
	{
		IplImage ipl3=IplImage.createFrom(image);
		cvShowImage("6",ipl3);
		cvNamedWindow("6",1);
		cvWaitKey(0);
		
		/**
		 * 图像滤波
		 */	
		//滤波
//		BufferedImage image2=FilterImage.FilterGaussionGet(image,1,3);
//
//		FilterImage.FilterFill(image2,0,0,0,0);
//		
//		image2=TranslateImage.TranslateSize(image2,2,0);
//		
//		image2=FilterImage.FilterGrayLevelGet(image2,-1);
//		
//		image2=FilterImage.FilterMeanDel(image2,1.1,80,110);
//		
//		image2=FilterImage.FilterGrayLevelGet(image2,-1);
//	
//		//FilterImage.FilterExpand(image2,1,0);
//		//填充
//		//FilterImage.FilterFill(image22,0,0,0,0);
//		//放大
//		
//	//	image2=TranslateImage.TranslateSize(image2,0.5,130);
//		FilterImage.FilterGaussion(image2,0,5);
//		//FilterImage.FilterErosion(image3,1,0);
//		
//
//		
//		FilterImage.FilterGrayLevel(image2,110);
//		
//	//	FilterImage.FilterErosion(image2,1,0);
//		//BufferedImage image33=TranslateImage.TranslateSize(image3,1.0,130);
		
		//滤波
		BufferedImage image2=FilterImage.FilterGaussionGet(image,1,3);
		
		image2=FilterImage.FilterGrayLevelGet(image2,130);
		//FilterImage.FilterErosion(image2,1,0);
		//FilterImage.FilterExpand(image2,1,0);
		//填充
		FilterImage.FilterFill(image2,0,0,0,0);
		//放大
		image2=TranslateImage.TranslateSize(image2,2,0);
		FilterImage.FilterErosion(image2,1,0);
		
		
	
		IplImage ipl34=IplImage.createFrom(image2);
		cvShowImage("7",ipl34);
		cvNamedWindow("7",1);
		cvWaitKey(0);
		return image2;
	}
	
	public static byte[] readInputStream(InputStream inStream) throws Exception{  
	    ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
	    //创建一个Buffer字符串  
	    byte[] buffer = new byte[1024];  
	    //每次读取的字符串长度，如果为-1，代表全部读取完毕  
	    int len = 0;  
	    //使用一个输入流从buffer里把数据读取出来  
	    while( (len=inStream.read(buffer)) != -1 ){  
	        //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度  
	        outStream.write(buffer, 0, len);  
	    }  
	    //关闭输入流  
	    inStream.close();  
	    //把outStream里的数据写入内存  
	    return outStream.toByteArray();  
	}  

	
	
	
	public static void main(String[] args) throws Exception {
		
		
		
	verificationCode verCode=new verificationCode("./data/chinessRecognition/tz.txt",0,1,11,3,3);
	//读取图片
	
	System.out.println("测试");

	URL url = new URL("http://www.95504.net/UserControl/Image.aspx?o=Thu%20May%2008%202014%2013:37:01%20GMT+0800%20");  
	
//	URL url = new URL("http://regcheckcode.taobao.com/auction/checkcode?sessionID=be1cde9109431c00bd796ec972e5a168&identity=sm-taobaoindex&type=default");
	
	int i=0;
	 while(true)
	{
		 i++;
		 if(i==101)
		 {
			 break;
		 }
	HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
    //设置请求方式为"GET"  
    conn.setRequestMethod("GET");  
    //超时响应时间为5秒  
    conn.setConnectTimeout(5 * 1000);  
    //通过输入流获取图片数据  
    InputStream inStream = conn.getInputStream();  
    //得到图片的二进制数据，以二进制封装得到数据，具有通用性  
    byte[] data = readInputStream(inStream);  
    //new一个文件对象用来保存图片，默认保存当前工程根目录  
    File imageFile = new File("h://image/"+Integer.toString(i)+".gif");
  
    //创建输出流  
    FileOutputStream outStream = new FileOutputStream(imageFile);  
    //写入数据  
    outStream.write(data);  
    //关闭输出流  
    outStream.close();
    //执行测试样本读取
    String name=verCode.readTestSample(ImageIO.read(new File("h://image/"+Integer.toString(i)+".gif")));
    imageFile.renameTo(new File("h://image/"+name+".gif"));
	}
}
	
	
}
