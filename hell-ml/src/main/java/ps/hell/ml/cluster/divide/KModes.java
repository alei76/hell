package ps.hell.ml.cluster.divide;

import static com.googlecode.javacv.cpp.opencv_highgui.cvNamedWindow;
import static com.googlecode.javacv.cpp.opencv_highgui.cvShowImage;
import static com.googlecode.javacv.cpp.opencv_highgui.cvWaitKey;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import ps.hell.math.base.matrix.Matrix;
import ps.hell.ml.statistics.base.Normal;
import ps.hell.util.plot.opencv.BubbleGraph;

import com.googlecode.javacv.cpp.opencv_core;

/**
 * kmodes 是kmean的改进方法 但是只能适应于 分类型数据
 * 其中差异度为  不同数 /总数
 * @author Administrator
 *
 */
public class KModes {
	
	/**
	 * 其中输入数据位分类型 转化成的 数值型数据
	 */
	private int[][] data;
	private int[] cluster;
	private boolean[] clusterCenterFlag;//聚类中心是否有效
	private int[][] clusterCenter;
	private int clusterCount;
	private double errorOld=0.0;
	private double errorNew;
	private int fileLen;
	private int length;
	/**
	 * kmeans基础算法
	 * @param clusterCount 聚类数
	 */
	public KModes(int[][] data,int clusterCount)
	{
		this.fileLen=data.length;
		this.length=data[0].length;
		this.clusterCount=clusterCount;
		this.data=data.clone();
		cluster=new int[data.length];
		clusterCenter=new int[clusterCount][data[0].length];
		clusterCenterFlag=new boolean[clusterCount];
		for(int i=0;i<clusterCount;i++)
		{//初始化为有效
			clusterCenterFlag[i]=true;
		}
		
	}
	
	/**
	 * 获取最长距离中心 为初始中心点
	 */
	public void getDistenceCluster()
	{	
		int[][] dist=new int[fileLen][fileLen];
		//首先获取 最远距离的两个点
		double tempMax=Double.MIN_VALUE;
		int first=-1;
		int second=-1;
		for(int i=0;i<fileLen;i++)
		{
			for(int j=0;j<fileLen;j++)
			{
				if(i==j)
				{
					continue;
				}else
				{
					int temp=0;
					for(int m=0;m<length;m++)
					{
						if(data[i][m]!=data[j][m])
						{
							temp+=1;
						}
					}
					dist[i][j]=temp;
					if(tempMax<temp)
					{
						tempMax=temp;
						first=i;
						second=j;
					}
				}
			}
		}
		clusterCenter[0]=data[first].clone();
		clusterCenter[1]=data[second].clone();
		//存储已经使用的数据
		Set<Integer> used=new HashSet<Integer>();
		used.add(first);
		used.add(second);
		for(int k=2;k<clusterCount;k++)
		{
			clusterCenter[k]=execDistCluster(dist,used).clone();
		}
		System.out.println("初始化中心点---");
		for(int i=0;i<clusterCenter.length;i++)
		{
			for(int j=0;j<clusterCenter[0].length;j++)
			{
				System.out.print(clusterCenter[i][j]+"\t");
			}
			System.out.println();
		}
		System.out.println("---------------");
		//System.exit(1);
		
	}
	
	/**
	 * 未初始化的调用过程
	 * @param dist
	 * @param used
	 */
	public int[] execDistCluster(int[][] dist,Set<Integer> used)
	{
		int[] re=null;
		double maxVal=Double.MIN_VALUE;
		int index=-1;
		for(int i=0;i<fileLen;i++)
		{
			if(used.contains(i))
			{
				continue;
			}
			else
			{
				//点到多个点之间的最小距离

				double temp=1.0d;
				for(Integer indexUsed:used)
				{
					if(i<indexUsed)
					{
						temp*=dist[i][indexUsed];
					}else
					{
						temp*=dist[indexUsed][i];
					}
				}
				temp/=used.size();
				if(maxVal<temp)
				{
					index=i;
					maxVal=temp;
				}
			}
		}
		used.add(index);
		re=data[index];
		return re;
	}
	
	
	/**
	 * 聚类执行过程
	 */
	public void clusterCompute()
	{
		int flag=0;
		System.out.println("初始");
		for(int i=0;i<fileLen;i++)
		{
			System.out.print(this.cluster[i]+"\t");
		}
		System.out.println();
		for(int i=0;i<this.clusterCount;i++)
		{
			for(int j=0;j<this.length;j++)
			{
				System.out.print(this.clusterCenter[i][j]+"\t");
			}
			System.out.println();
		}
		while(true)
		{
			//执行计算
			flag++;
			errorOld=errorNew;
			errorNew=0.0;
			for(int i=0;i<fileLen;i++)
			{
				double valueUp=Double.MAX_VALUE;
				//this.cluster[i]=-1;
				for(int k=0;k<this.clusterCount;k++)
				{
					//执行
					if(clusterCenterFlag[k]==false)
					{//如果中心点为无效点则跳过
						continue;
					}
					double value=0.0;
					for(int j=0;j<length;j++)
					{
						value+=Math.pow(data[i][j]-clusterCenter[k][j],2.0);
					}
					if(valueUp>value)
					{
						valueUp=value;
						this.cluster[i]=k;
					}
				}
				errorNew+=valueUp;
				
			}
			System.out.println("第"+flag+"次");
			for(int i=0;i<fileLen;i++)
			{
				System.out.print(this.cluster[i]+"\t");
			}
			System.out.println();
			
			//重新计算聚类中心
			for(int i=0;i<this.clusterCount;i++)
			{
				int count=0;
				double[] ll=new double[length];
				for(int j=0;j<fileLen;j++)
				{
					if(cluster[j]==i)
					{
						count++;
						for(int m=0;m<length;m++)
						{
							ll[m]+=data[j][m];
						}
					}
				}
				if(count<=1)
				{
					//剔除掉cluster类
					//并将分类标记为-1
					clusterCenterFlag[i]=false;
				//	System.out.println("标记为无效："+i+"\t----------------------------"+count);
					continue;
					
				}
				if(count>1)
				{
					for(int j=0;j<length;j++)
					{
						//可以使用众数来设置
						clusterCenter[i][j]=(int)ll[j]/count;
					}
				}
			}
			for(int i=0;i<this.clusterCount;i++)
			{
				for(int j=0;j<this.length;j++)
				{
					System.out.print(this.clusterCenter[i][j]+"\t");
				}
				System.out.println();
			}
			if(flag==1)
			{
				continue;
			}
			else
			{
				if(Math.abs(errorNew-errorOld)<1E-7)
				{
					System.out.println("收敛:聚类算法执行次数:"+flag+"\t方差和为:"+errorNew);
					break;
				}
				errorOld=errorNew;
			}
		}
	}

	/**
	 * 打印方法
	 */
	public void print()
	{
		for(int i=0;i<fileLen;i++)
		{
			System.out.println(this.cluster[i]);
		}
	}
	
	
	
	/**
	 *执行方法
	 */
	public void run()
	{
		this.getDistenceCluster();
		this.clusterCompute();
	}
	
	public int[] getCluster() {
		return cluster;
	}
	public void setCluster(int[] cluster) {
		this.cluster = cluster;
	}
	
	
	public static void main(String[] args) {
		Normal normal1=new Normal(4,0.3);
		Matrix data1=normal1.random(500,2);
		
		Normal normal2=new Normal(10,0.6);
		Matrix data2=normal2.random(900,2);
		
		Normal normal3=new Normal(-3,0.4);
		Matrix data3=normal3.random(100,2);
		
		Normal normal4=new Normal(1,0.4);
		Matrix data4=normal4.random(300,2);
		
		data1.addMatrix(data2);
		data1.addMatrix(data3);
		data1.addMatrix(data4);
		
		double[][] dataTemp=data1.getData();
		int[][] data=new int[dataTemp.length][dataTemp[0].length];
		for(int i=0;i<data.length;i++)
		{
			for(int j=0;j<data[0].length;j++)
			{
				data[i][j]=(int)Math.abs(dataTemp[i][j]);
			}
		}
		KModes kmodes=new KModes(data,4);
		kmodes.run();
		//kmeans.print();
		
		
		
		Vector<Double> x=new Vector<Double>();
		Vector<Double> y=new Vector<Double>();
		Vector<Integer> cluster=new Vector<Integer>();
		int[] clusterK=kmodes.getCluster();
		for(int i=0;i<data.length;i++)
		{
			x.add(data[i][0]*1.0);
			y.add(data[i][1]*1.0);
			cluster.add(clusterK[i]);
			
			System.out.println("数据:"+data[i][0]+"\t"+data[i][1]+"\t"+clusterK[i]);
		}
		
		//绘图模块
		
		BubbleGraph graph=new BubbleGraph();
		graph.width=600;
	    graph.height=400;
		
		graph.parentent_config();//计算改变的所有全局参量
		  //初始化图片 
			BubbleGraph.img_trans img1=new BubbleGraph.img_trans();
		    //main_type="scatter";
		    img1.img1=opencv_core.cvCreateImage(opencv_core.cvSize(graph.width,graph.height),8,3);
		    graph.main_type="qipao2";
		    
		    if(graph.main_type=="scatter" ||graph.main_type=="qipao" ||graph.main_type=="qipao2" ||graph.main_type=="qipao3" )
		    {
		        System.out.println("初始化图片");
		        graph.plotScatter(img1,x,y,graph.main_type,cluster,0);
		    }
		    
		    cvNamedWindow("test",1);
		    cvShowImage("test",img1.img1);
		    cvWaitKey(0);
		
		
	}

}

