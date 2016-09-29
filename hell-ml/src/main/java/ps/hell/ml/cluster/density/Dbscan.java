package ps.hell.ml.cluster.density;

import static com.googlecode.javacv.cpp.opencv_highgui.cvNamedWindow;
import static com.googlecode.javacv.cpp.opencv_highgui.cvShowImage;
import static com.googlecode.javacv.cpp.opencv_highgui.cvWaitKey;

import java.util.LinkedList;
import java.util.Vector;

import ps.hell.math.base.matrix.Matrix;
import ps.hell.ml.statistics.base.Normal;
import ps.hell.util.plot.opencv.BubbleGraph;

import com.googlecode.javacv.cpp.opencv_core;

/**
 * dbscan
 * 获取 最小 半径 及 在该半径中的 限制数量
 * 如果半径中的限制数量满足要求则 为核心点
 * 否则 如果存在核心点 则为 边界点
 * 其他则为 噪声点
 * @author Administrator
 *
 */
public class Dbscan {

	/**
	 * 最小聚类值
	 */
	private int minPis=5;
	
	
	/**
	 * 计算每个数据点相邻的数据点，邻域定义为以该点为中心以边长为平方的网格
	 */
	private double  radius=0.1;
	
	/**
	 * 数据集
	 */
	private double[][] data=null;
	
	/**
	 * 列宽度
	 */
	private int width=0;
	/**
	 * 
	 * @param data 输入数据集
	 * @param minPis 最小聚类值
	 */
	public Dbscan(LinkedList<double[]> data,double radius,int minPis)
	{

		if(data.size()==0)
		{
			System.out.println("输入数据集异常");
			System.exit(1);;
		}
		
		this.minPis=minPis;
		this.radius=Math.pow(radius,2.0d);
		width=data.get(0).length;
		this.data=new double[data.size()][width];
		int i=0;
		for(double[] da:data)
		{
			this.data[i]=da.clone();
			i++;
		}
	}
	/**
	 * 
	 * @param data 输入数据集
	 * @param minPis 最小聚类值
	 */
	public Dbscan(double[][] data,double radius,int minPis)
	{

		if(data.length==0)
		{
			System.out.println("输入数据集异常");
			System.exit(1);;
		}
		
		this.minPis=minPis;
		this.radius=Math.pow(radius,2.0d);
		width=data[0].length;
		this.data=new double[data.length][width];
		int i=0;
		for(double[] da:data)
		{
			this.data[i]=da.clone();
			i++;
		}
	}
	/**
	 * -1为 噪声 0-n为类簇
	 * @return 返回为聚类结果
	 * 
	 */
	public int[] run()
	{
		int[] cluster=new int[data.length];
		for(int i=0;i<data.length;i++)
		{
			cluster[i]=-1;
		}
		int clusterMaxIndex=-1;
		//转化为核心点的index
		LinkedList<Integer> clusterOk=new LinkedList<Integer>();
		//不是核心点的index 0 不是核心点  1 是边界点 2 是核心点
		int[] clusterNo=new int[data.length];
		for(int i=0;i<data.length;i++)
		{
			//初始化数量
			int count=0;
			//包含核心点数量
			int coreCount=0;
			int clusterIndex=-1;
			//计算每个点的关联点的数量
			for(int j=0;j<data.length;j++)
			{
				if(i==j)
				{
					continue;
				}else
				{
					double ll=0.0;
					for(int m=0;m<width;m++)
					{
						ll+=Math.pow(data[i][m]-data[j][m],2.0d);
					}
					//System.out.println(i+"\t"+j+"\tradiusPow:"+ll);
					if(ll<radius)
					{
						count++;
						if(clusterNo[j]==2)
						{
							coreCount++;
							if(cluster[j]>clusterIndex)
							{
								clusterIndex=cluster[j];
							}
						}
					}
				}
			}
			System.out.println(i+"\t"+count+"\tcore:"+coreCount+"\t"+clusterIndex);
			if(count>=minPis)
			{
				//标记为核心点
				clusterNo[i]=2;
				if(coreCount==0)
				{
					clusterMaxIndex++;
					cluster[i]=clusterMaxIndex;
				}else
				{
					cluster[i]=clusterIndex;
				}
			
			}else if(coreCount>0)
			{
				clusterNo[i]=2;
				cluster[i]=clusterIndex;
			}else if(i==0)
			{
				clusterNo[i]=1;
				clusterMaxIndex++;
				cluster[i]=clusterMaxIndex;
			}else
			{}
		}

		return cluster;
	}
	
	public static void main(String[] args) {
		Normal normal1=new Normal(4,0.3);
		Matrix data1=normal1.random(500,2);
		
		Normal normal2=new Normal(10,0.6);
		Matrix data2=normal2.random(400,2);
		
		Normal normal3=new Normal(-3,0.4);
		Matrix data3=normal3.random(200,2);
		
		Normal normal4=new Normal(1,0.4);
		Matrix data4=normal4.random(300,2);
		
		data1.addMatrix(data2);
		data1.addMatrix(data3);
		data1.addMatrix(data4);
		
		double[][] data=data1.getData();
		
		Dbscan dbscan=new Dbscan(data,0.4,40);
		
		
		Vector<Double> x=new Vector<Double>();
		Vector<Double> y=new Vector<Double>();
		Vector<Integer> cluster=new Vector<Integer>();
		int[] clusterK=dbscan.run();
		for(int i=0;i<data.length;i++)
		{
			x.add(data[i][0]);
			y.add(data[i][1]);
			cluster.add(clusterK[i]);
			System.out.println("行:"+i+"\t值:"+data[i][0]+":"+data[i][1]+"\t类："+clusterK[i]);
		}
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
