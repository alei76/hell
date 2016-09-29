package ps.hell.ml.cluster.density;

import static com.googlecode.javacv.cpp.opencv_highgui.cvNamedWindow;
import static com.googlecode.javacv.cpp.opencv_highgui.cvShowImage;
import static com.googlecode.javacv.cpp.opencv_highgui.cvWaitKey;

import java.util.LinkedList;
import java.util.Random;
import java.util.Vector;

import ps.hell.math.base.matrix.Matrix;
import ps.hell.ml.statistics.base.Normal;
import ps.hell.util.plot.opencv.BubbleGraph;

import com.googlecode.javacv.cpp.opencv_core;

/**
 * 模糊c聚类算法
 * @author Administrator
 *
 */
public class Fcm {

	public LinkedList<Double[]> data=new LinkedList<Double[]>();
	public int clusterCount=0;
	/**
	 * 样本长度
	 */
	public int fileLength=0;
	/**
	 * 统计迭代次数
	 */
	public int computeCount=0;
	/**
	 * 维度长度
	 */
	public int dimensionalityLength=0;
	
	/**
	 * 加权指数值
	 */
	public int mCluster=2;
	
	
	/**
	 * 存储olderror值
	 */
	public double oldError=0.0;
	/**
	 * 类中心
	 */
	public LinkedList<Double[]> clusterCenterDouble=new LinkedList<Double[]>();
	
	/**
	 * 存储列对应的最大，最小值
	 */
	public LinkedList<Double[]> dataMaxMin=new LinkedList<Double[]>();
	/**
	 * 隶属度行为类数，列为 样本长度
	 */
	public LinkedList<Double[]> clusterMembership=new LinkedList<Double[]>();
	
	
	/**
	 * 行为类 ，列为对应样本和类中心的值
	 */
	public LinkedList<Double[]> clusterMembershipValue=new LinkedList<Double[]>();
	/**
	 * 
	 * @param data 输入数据
	 * @param clusterCount 聚类数量
	 */
	public Fcm(LinkedList<Double[]> data,int clusterCount)
	{
		for(Double[] data1:data)
		{
			this.data.add(data1.clone());
		}
		this.clusterCount=clusterCount;
		this.fileLength=data.size();
		try
		{
		this.dimensionalityLength=data.get(1).length;
		}
		catch(Exception e)
		{
			System.out.println("输入数据错误");
			System.exit(1);
		}
	}
	/**
	 * 隶属度矩阵和类中心初始化函数
	 * 输入数据标准化
	 */
	public void init()
	{
		Random random=new Random();
		for(int i=0;i<this.clusterCount;i++)
		{
			Double[] temp=new Double[this.dimensionalityLength];
			for(int j=0;j<this.dimensionalityLength;j++)
			{
				temp[j]=random.nextDouble();
			}
			clusterCenterDouble.add(temp);
			Double[] temp1=new Double[this.fileLength];
			Double[] temp2=new Double[this.fileLength];
			//列向量需要满足之和为1每一个样本在多个类上隶属度之和为1
			for(int j=0;j<this.fileLength;j++)
			{
				temp1[j]=random.nextDouble();
				temp2[j]=random.nextDouble();
			}
			clusterMembershipValue.add(temp2.clone());
			clusterMembership.add(temp1.clone());
		}
		for(int j=0;j<this.fileLength;j++)
		{
			double ll=0.0;
			for(int i=0;i<this.clusterCount;i++)
			{
			//	System.out.println(clusterMembership.size()+"\t"+clusterMembership.get(i).length);
				ll+=clusterMembership.get(i)[j];
			}
			//修改为隶属度之和为1
			for(int i=0;i<this.clusterCount;i++)
			{
				clusterMembership.get(i)[j]=clusterMembership.get(i)[j]/ll;
			}
		}
		//执行输入数据标准化过程
		for(int j=0;j<this.dimensionalityLength;j++)
		{
			Double[] maxMin=new Double[2];
			double max=Double.MIN_VALUE;
			double min=Double.MAX_VALUE;
			for(int i=0;i<this.fileLength;i++)
			{
				if(data.get(i)[j]>max)
				{
					max=data.get(i)[j];
				}
				if(data.get(i)[j]<min)
				{
					min=data.get(i)[j];
				}
			}
			maxMin[0]=max;
			maxMin[1]=min;
			if(max==min)
			{
				System.out.println("输入列:"+j+"全部内容一样无效");
			}
			this.dataMaxMin.add(maxMin);
			//标准化
			for(int i=0;i<this.fileLength;i++)
			{
				data.get(i)[j]=(data.get(i)[j]-min)/(max-min);
			}
		}	
	}

	/**
	 * fcm执行过程
	 */
	public void cluster()
	{
		
		while(true)
		{
			if(computeCount>100)
				break;
			computeCount++;
			//执行距离计算公式
			for(int k=0;k<this.clusterCount;k++)
			{
				for(int i=0;i<this.fileLength;i++)
				{
					Double temp=0.0;
					for(int j=0;j<this.dimensionalityLength;j++)
					{
						temp+=Math.pow(data.get(i)[j]-this.clusterCenterDouble.get(k)[j],2.0);
					}
					this.clusterMembershipValue.get(k)[i]=Math.sqrt(temp);
				}
			}
			//计算新中心
			//修改隶属度矩阵
			for(int i=0;i<this.fileLength;i++)
			{
				boolean temp_flag=false;
				for(int k=0;k<this.clusterCount;k++)
				{
					//检索内部是否已经存在了0值
					double temp=0.0;
					for(int k1=0;k1<this.clusterCount;k1++)
					{
						if(this.clusterMembershipValue.get(k)[i]<1E-5)
						{
							temp_flag=true;//异常
							//修改值
							for(int m=0;m<this.clusterCount;m++)
							{
								clusterMembership.get(m)[i]=0.0;
							}
							clusterMembership.get(k)[i]=1.0;
							break;
						}
						if(this.clusterMembershipValue.get(k1)[i]<1E-5)
						{
							temp_flag=true;//异常
							for(int m=0;m<this.clusterCount;m++)
							{
								clusterMembership.get(m)[i]=0.0;
							}
							clusterMembership.get(k1)[i]=1.0;
							break;
						}
							temp+=Math.pow(this.clusterMembershipValue.get(k)[i]/
						this.clusterMembershipValue.get(k1)[i],(2.0/(mCluster-1)));
					}
					if(temp_flag==false)//如果没有异常则正常执行
					{
						clusterMembership.get(k)[i]=1.0/temp;
					}
				
				}
				if(temp_flag==true)
				{
					break;
				}
			}
			//修改类中心矩阵
			//因为根据拉格朗日乘子法所以下面之和为1
			for(int i=0;i<clusterCount;i++)
			{
				for(int m=0;m<this.dimensionalityLength;m++)
				{
					double temp=0.0;
					double temp2=0.0;
					for(int j=0;j<fileLength;j++)
					{
						double tem=Math.pow(clusterMembership.get(i)[j],2.0);
						temp+=tem*data.get(j)[m];
						temp2+=tem;
					}
					this.clusterCenterDouble.get(i)[m]=temp/temp2;
				}
			}
			//最终计算聚类
			//可以通过计算差异也可以通过计算
			//计算sum    menbershipvalue的变化即可
			double newError=0.0;
			System.out.println("聚类次数为:"+computeCount+"\t误差为:"+oldError);
			for(int i=0;i<clusterCount;i++)
			{
				for(int j=0;j<fileLength;j++)
				{
					newError+=this.clusterMembershipValue.get(i)[j];
				}
			}
			if(Math.abs(oldError-newError)<1E-5)
			{
				oldError=newError;
				System.out.println("聚类结束，聚类次数为:"+computeCount+"\t误差为:"+oldError);
				break;
			}
			oldError=newError;
		}
	}

/**
 * 调度程序
 */
	public void run()
	{
		init();
		cluster();
	}
	
	/**
	 * 打印程序
	 */
	public void print()
	{
		System.out.println("隶属度矩阵");
		for(int i=0;i<this.clusterCount;i++)
		{
			for(double d:this.clusterMembership.get(i))
			{
				System.out.print(d+"\t");
			}
			System.out.println();
		}
	}
	public LinkedList<Double[]> getClusterMembership()
	{
		return clusterMembership;
	}
	
	
	public static void main(String[] args) {
		//执行效果
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
		LinkedList<Double[]> dataTemp=new LinkedList<Double[]>();
		for(int i=0;i<data.length;i++)
		{
			Double[] da=new Double[data[i].length];
			for(int j=0;j<data[i].length;j++)
			{
				da[j]=data[i][j];
			}
			dataTemp.add(da);
		}
		
		Fcm fcm=new Fcm(dataTemp,10);
		fcm.run();
		fcm.print();
		
		
		
		Vector<Double> x=new Vector<Double>();
		Vector<Double> y=new Vector<Double>();
		Vector<Integer> cluster=new Vector<Integer>();
		LinkedList<Double[]> clusterK=fcm.getClusterMembership();
		for(int i=0;i<data.length;i++)
		{
			x.add(data[i][0]);
			y.add(data[i][1]);
			//取最大的数展示
			double max=Double.MIN_VALUE;
			int index=-1;
			for(int j=0;j<fcm.clusterCount;j++)
			{
				if(clusterK.get(j)[i]>max)
				{
					max=clusterK.get(j)[i];
					index=j;
				}
				System.out.print(clusterK.get(j)[i]+"\t");
			}
			System.out.println();
			//System.out.println("类添加:"+index+"\t"+max);
			cluster.add(index);
			
		//	System.out.println("数据:"+data[i][0]+"\t"+data[i][1]+"\t"+clusterK[i]);
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
