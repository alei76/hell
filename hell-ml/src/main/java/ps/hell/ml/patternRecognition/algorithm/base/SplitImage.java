package ps.hell.ml.patternRecognition.algorithm.base;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.util.LinkedList;

/**
 * 连续字符分割器
 * @author sx
 *
 */
public class SplitImage {
	public static ColorModel sColor=ColorModel.getRGBdefault();
	/**
	 * 将输入图片分割成对应份 二值化图
	 * int[] 存储的为 x,y  width height;
	 * @param image
	 * @param red 为对应红色的值 做统计
	 * @param splitCount 分割数量
	 * @param 直方图方法 倒数方法
	 * @return
	 */
	public static LinkedList<int[]> SplitHistogram(BufferedImage image,int red,int splitCount)
	{
		LinkedList<int[]> oci=new LinkedList<int[]>();
		int width=image.getWidth();
		int height=image.getHeight();
		int[] xHist=new int[width];
		int[] yHist=new int[height];
		for(int i=0;i<width;i++)
		{
			for(int j=0;j<height;j++)
			{
				if(sColor.getRed(image.getRGB(i,j))==red)
				{
					xHist[i]++;
					yHist[j]++;
				}
			}
		}
		//
		LinkedList<Integer> xSplitStart=new LinkedList<Integer>();
		LinkedList<Integer> xSplitEnd=new LinkedList<Integer>();
		int[] ySplit=new int[2];
		/******************切割x过程***********/
		//计算5均值
		double[] xMean=new double[width];
		xMean[0]=1.0*xHist[0]/5;
		xMean[1]=1.0*(xHist[0]+xHist[1])/5;
		xMean[width-2]=1.0*(xHist[width-2]+xHist[width-1])/5;
		xMean[width-1]=1.0*(xHist[width-1])/5;
		for(int i=2;i<width-2;i++)
		{
			//xMean[i]=1.0*(xHist[i-2]+xHist[i-1]+xHist[i]+xHist[i+1]+xHist[i+2])/5;
			
			xMean[i]=1.0*(xHist[i-1]+4*xHist[i]+xHist[i+1])/6;
		}
		//取
		
		
		 //获取二阶导数为最大的位置
		//一阶倒数在附近最靠近0
		//并且左右2位置需要和不同方向
		double[] xFl=new double[width];
		xFl[0]=0;
		for(int i=1;i<width-1;i++)
		{
			xFl[i]=(xMean[i]-xMean[i-1]);
			//System.out.print(xFl[i]+"\t");
			
		}
		//System.out.println();
		for(int i=1;i<width-1;i++)
		{
			xFl[i]=(xFl[i]+xFl[i-1])/2;
			//System.out.print(xFl[i]+"\t");
			
		}
		//System.out.println();
		for(int i=1;i<width-1;i++)
		{
			//System.out.print(xMean[i]+"\t");
		}
		//System.out.println();
		for(int i=1;i<width-1;i++)
		{
		//	System.out.print(xHist[i]+"\t");
		}
		//System.out.println();
		//表示开始结束
		boolean flag=false;
		boolean flag_x=false;
		for(int i=1;i<width-1;i++)
		{
		//	System.out.println(xFl[i-1]+"\t"+xFl[i]+"\t"+xFl[i+1]);
			if(xFl[i]>0)
			{
				if(xFl[i-1]<=0 && xFl[i+1]>0)
				{
					//表示开始
					if(flag==false)
					{
						xSplitStart.add(i);
						flag=true;
					}
					else
					{
						xSplitStart.add(i-1);
						xSplitEnd.add(i-1);
					}
				}
			}
			if(i==width-2)
			{
				flag_x=true;
			}
		}
		if(xSplitStart.size()==xSplitEnd.size())
		{
			int temp_x=xSplitEnd.get(xSplitEnd.size()-1);
			for(int i=xHist.length-1;i>=0;i--)
			{
				if(xHist[i]>=1)
				{
					if(Math.abs(temp_x-i)<=4.0)
					{
						xSplitEnd.add(i);
						break;
					}
				}
			}
		}
		else
		{
			for(int i=xHist.length-1;i>=0;i--)
			{
				if(xHist[i]>=2)
				{
						xSplitEnd.add(i);
						break;
				}
			}
		}
		
		
		LinkedList<Integer> xSplitStart2=new LinkedList<Integer>();
		LinkedList<Integer> xSplitEnd2=new LinkedList<Integer>();
		//取最小值
		LinkedList<Double> xSplitValue2=new LinkedList<Double>();
		LinkedList<Integer> xSplitIndex2=new LinkedList<Integer>();
		for(int i=0;i<xSplitStart.size();i++)
		{
		//	System.out.println("kaishi:"+i+"\t"+xSplitStart.get(i)+"\t"+xSplitEnd.get(i));
		}
		for(int i=1;i<xSplitStart.size();i++)
		{
			boolean fl=false;
			for(int j=0;j<xSplitValue2.size();j++)
			{
				if(xSplitValue2.get(j)>xMean[xSplitStart.get(i)])
				{
					xSplitValue2.add(j,xMean[xSplitStart.get(i)]);
					xSplitIndex2.add(j,xSplitStart.get(i));
					fl=true;
					break;
				}
				
			}
			if(fl==false)
			{
				xSplitValue2.add(xMean[xSplitStart.get(i)]);
				xSplitIndex2.add(xSplitStart.get(i));
			}
		}
		//取前三个最小值//并排序
		for(int i=0;i<xSplitIndex2.size();i++)
		{
		//	System.out.println(i+":"+xSplitIndex2.get(i)+"\t"+xSplitValue2.get(i));
		}
		if(xSplitIndex2.size()<3)
		{
			//System.out.println("分割错误:"+xSplitIndex2);
		}
		//去最小的三个 按照从小到大排序
		for(int i=0;i<2;i++)
		{
			for(int j=0;j<2;j++)
			{
				if(xSplitIndex2.get(j)>xSplitIndex2.get(j+1))
				{
					int inl=xSplitIndex2.get(j+1);
					xSplitIndex2.set(j+1,xSplitIndex2.get(j));
					xSplitIndex2.set(j,inl);
					double ind=xSplitValue2.get(j+1);
					xSplitValue2.set(j+1,xSplitValue2.get(j));
					xSplitValue2.set(j,ind);
				}
			}
		}
		
		for(int i=0;i<xSplitIndex2.size();i++)
		{
			//System.out.println(i+":index:"+xSplitIndex2.get(i)+"\t"+xSplitValue2.get(i));
		}
		xSplitStart2.add(xSplitStart.get(0));
		for(int i=0;i<3;i++)
		{
			//获取位置
			
			int index=xSplitIndex2.get(i);
			//System.out.println("index:"+index);
			for(int j=1;j<xSplitStart.size();j++)
			{
				if(index==xSplitStart.get(j))
				{
					xSplitEnd2.add(xSplitEnd.get(j-1));
					xSplitStart2.add(index);
				}
			}
		}
		xSplitEnd2.add(xSplitEnd.get(xSplitEnd.size()-1));
		
		for(int i=0;i<xSplitEnd2.size();i++)
		{
			//System.out.println("开始:"+xSplitStart2.get(i)+"\t"+xSplitEnd2.get(i));
		}
		
		if(xSplitEnd2.size()!=splitCount)
		{
			System.out.println("切割错误:"+xSplitEnd2.size());
			return oci;
		}
		/******************切割y过程***********/
		//只切一份
		for(int i=0;i<yHist.length;i++)
		{
			//System.out.println(yHist[i]+"\t"+height/10);
			if(yHist[i]>height/10)
			{
				ySplit[0]=i;
				break;
			}
		}
		for(int i=yHist.length-1;i>=0;i--)
		{
			//System.out.println(yHist[i]+"\t"+height/10);
			if(yHist[i]>height/10)
			{
				ySplit[1]=i;
				break;
			}
		}
		if(ySplit[0]==0||ySplit[1]==0)
		{
			System.out.println("y切割错误");
			return oci;
		}
		for(int i=0;i<splitCount;i++)
		{
			int[] split=new int[4];
			split[0]=xSplitStart2.get(i);//x坐标
			split[1]=ySplit[0];//y坐标
			split[2]=xSplitEnd2.get(i)-xSplitStart2.get(i)+1;//长度
			split[3]=ySplit[1]-ySplit[0]+1;//高度
			oci.add(split);
		}
		//System.out.println("大小:"+oci.size());
		return oci;
	}

}
