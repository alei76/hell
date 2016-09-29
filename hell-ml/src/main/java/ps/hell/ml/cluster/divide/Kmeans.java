package ps.hell.ml.cluster.divide;

import static com.googlecode.javacv.cpp.opencv_highgui.cvNamedWindow;
import static com.googlecode.javacv.cpp.opencv_highgui.cvShowImage;
import static com.googlecode.javacv.cpp.opencv_highgui.cvWaitKey;

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import ps.hell.ml.cluster.divide.inter.KmeansInter;
import ps.hell.math.base.matrix.Matrix;
import ps.hell.ml.statistics.base.Normal;
import ps.hell.util.plot.opencv.BubbleGraph;

import com.googlecode.javacv.cpp.opencv_core;
/**
 * kmeans
 * @author Administrator
 *
 */
public class Kmeans implements KmeansInter {
	public double[][] matrix;//数据源
	public int clusterCount=1;//聚类数量
	/**
	 *每个样本对应的所属类别
	 */
	public int[] cluster;//
	public int[] oldcluster;//
	
	public boolean[] clusterCenterFlag;
	/**
	 * 文件长度
	 */
	public int fileLength=0;
	/**
	 * 维度
	 */
	public int dimensionalityCount=0;
	
	/**
	 * 存储当前类在聚类过程中的最大半径
	 */
	public double[] clusterTempDouble;
	/**
	 * conopy算法中运用到的t1>t2
	 */
	public double T1=0.3;
	public double T2=0.1;
	/**
	 * 迭代次数
	 */
	public int computeCount=0;
	/**
	 * 存储误差
	 */
	public double errorOld=0.0;
	/**
	 * cannopy簇，-1为没计算,1为<t2，强,2为<t1 弱
	 */
	public int[] canopyCluster;
	/**
	 * 存储维度的最大最小值
	 */
	public ArrayList<double[]> maxMinValue=new ArrayList<double[]>();
	
	/**
	 * 存储聚类数级对应p的类中心
	 */
	public ArrayList<double[]> clusterCenter=new ArrayList<double[]>();
	public ArrayList<double[]> oldclusterCenter=new ArrayList<double[]>();
	
	/**
	 * 存储在随机cannopy中聚类数量
	 */
	public ArrayList<Integer> canopyCount=new ArrayList<Integer>();
	
	/**
	 * 存储在随机cannopy聚类数对应的聚类中心
	 */
	public ArrayList<ArrayList<double[]>> canopyCountCenter=new ArrayList<ArrayList<double[]>>();
	
	/**
	 * 存储加权半径
	 */
	public double oldoldRadius=0.0;
	public double oldRadius=0.0;
	public double newRadius=0.0;
	
	/**
	 * 存储计算的半径
	 */
	public double oldoldComputeRadius=0.0;
	public double oldComputeRadius=0.0;
	public double newComputeRadius=0.0;
	/**
	 * 用于存储 第几次聚类中每个类中心的最长半径
	 */
	public ArrayList<ArrayList<Double>> cannopyRadius=new ArrayList<ArrayList<Double>>();
	/**
	 * 
	 * @param matrix
	 * @param T2
	 * @param T1
	 */
	public Kmeans(double[][] matrix,double T2,double T1)
	{
		this.T2=T2;
		this.T1=T1;
		this.matrix = matrix.clone();
		this.matrix=new double[matrix.length][matrix[0].length];
		for(int i=0;i<matrix.length;i++)
		{
			this.matrix[i]=matrix[i].clone();
		}
		fileLength=matrix.length;
		canopyCluster=new int[fileLength];
		cluster=new int[fileLength];
		try{
			dimensionalityCount=matrix[0].length;
		}
		catch(Exception e)
		{
			System.out.println("输入数据错误");
			System.exit(1);
		}
		for(int i=0;i<fileLength;i++)
		{
			canopyCluster[i]=-1;
			cluster[i]=-1;
		}
	}
	
	public Kmeans(double[][] matrix)
	{
		try{
				dimensionalityCount=matrix[0].length;
			}
			catch(Exception e)
			{
				System.out.println("输入数据错误");
				System.exit(1);
			}
			
			
			this.matrix = matrix.clone();
			this.matrix=new double[matrix.length][matrix[0].length];
			for(int i=0;i<matrix.length;i++)
			{
				this.matrix[i]=matrix[i].clone();
			}
			
		fileLength=matrix.length;
		canopyCluster=new int[fileLength];
		cluster=new int[fileLength];
		
		for(int i=0;i<fileLength;i++)
		{
			canopyCluster[i]=-1;
			cluster[i]=-1;
		}
	}
	/**
	 * 标准化到0-1
	 */
	public void stander()
	{
		System.out.println("标准化值");
		for(int i=0;i<this.dimensionalityCount;i++)
		{
			double[] mami={Double.MIN_VALUE,Double.MAX_VALUE};
			for(int j=0;j<this.fileLength;j++)
			{
				if(matrix[j][i]>mami[0])
				{
					mami[0]=matrix[j][i];
				}
				if(matrix[j][i]<mami[1])
				{
					mami[1]=matrix[j][i];
				}
			}
			this.maxMinValue.add(mami);
			//标准化
			for(int j=0;j<this.fileLength;j++)
			{
				matrix[j][i]=(matrix[j][i]-mami[1])/(mami[0]-mami[1]);
				//System.out.print(matrix[j][i]+"\t");
			}
			//System.out.println();
		}
	}
	
	
	/**
	 * 获取聚类个数及初始类中心
	 */
	public void Canopy()
	{
		//判断是否在中心
		
		for(int i=0;i<canopyCount.size();i++)
		{
			if(canopyCount.get(i)==clusterCount)
			{
				clusterCenterFlag=new boolean[clusterCount];
				for(int j=0;j<clusterCount;j++)
				{
					clusterCenterFlag[j]=true;
				}
				this.clusterCenter=canopyCountCenter.get(i);
				this.newRadius=0.0;
				for(double ll:this.cannopyRadius.get(i))
				{
					newRadius+=ll;
				}
				System.out.println("聚类数:"+clusterCount+"\t已经计算过");
				return;
			}
		}
		System.out.println("Canopy不存在该类：Canopy随机搜索执行");
		int i=-1;
		clusterCenter=new ArrayList<double[]>();
		ArrayList<Double> maxLen=new ArrayList<Double>();
		while(true)
		{
			i++;
			//System.out.println(i+"\t"+fileLength);
			if(i<fileLength)
			{
				//为有效没处理过或者为弱簇
				if(canopyCluster[i]==-1 || canopyCluster[i]==2)
				{
					clusterCenter.add(matrix[i].clone());
					//如果为没有处理过的
					double max=Double.MIN_VALUE;
					for(int j=0;j<fileLength;j++)
					{
						if(canopyCluster[j]==-1 || canopyCluster[i]==2)
						{
							double temp=0.0;
							for(int m=0;m<dimensionalityCount;m++)
							{
								temp+=Math.pow(matrix[i][m]-matrix[j][m],2.0);
							}
							temp=Math.sqrt(temp);
							//判断和T2，T1的关系
							if(temp<T2)
							{
								if(temp>max)
								{
									max=temp;
								}
								canopyCluster[j]=1;
							}
							else if(temp<T1)
							{
								
							}
							else
							{
								
							}
						}		
					}
					//用来存储半径
					maxLen.add(max);
				}
			}
			else
			{
				//执行完成
				break;
			}
		}
		//初始化canopyCluster
		clusterCenterFlag=new boolean[clusterCenter.size()];
		for(int j=0;j<clusterCenter.size();j++)
		{
			clusterCenterFlag[j]=true;
		}
		for(int j=0;j<this.fileLength;j++)
		{
			canopyCluster[j]=-1;
		}
		//添加 该类  到存储中
		//判断是否已经存在
		boolean cano=false;
		for(int j=0;j<canopyCount.size();j++)
		{
			if(canopyCount.get(j)==clusterCenter.size())
			{
				cano=true;
				break;
			}
		}
		if(cano==false)
		{
			this.cannopyRadius.add(maxLen);
			canopyCount.add(clusterCenter.size());
			canopyCountCenter.add(clusterCenter);
		}
		System.out.println("Canopy随机搜索执行：得到类:"+clusterCenter.size()+"\t"+this.clusterCount);
		Random random=new Random();
		if(clusterCenter.size()==clusterCount)
		{
			clusterCenterFlag=new boolean[this.clusterCenter.size()];
			clusterCount=this.clusterCenter.size();
			for(int j=0;j<clusterCenter.size();j++)
			{
				clusterCenterFlag[j]=true;
			}
			System.out.println("canopy计算完成：总共聚成:"+clusterCenter.size()+"个类");
			T2=T2/(1.0+random.nextDouble()/90);
			T1=T1/(1.0+random.nextDouble()/90);
			this.newRadius=0.0;
			for(Double ll:maxLen)
			{
				newRadius+=ll.doubleValue()/maxLen.size();
			}	
		}
		else
		{
			
			if(clusterCenter.size()>clusterCount)
			{
				T2=T2+0.01+random.nextDouble()/100;
				T1=T1+0.01+random.nextDouble()/100;
			}
			else
			{
				T2=T2/(1.0+random.nextDouble()/90);
				T1=T1/(1.0+random.nextDouble()/90);
			}
			Canopy();
		}
		
		
	}
	
	
	
	/**
	 * 执行
	 */
	public void clusterCompute()
	{
	
		System.out.println("初始");
		for(int i=0;i<fileLength;i++)
		{
			System.out.print(this.cluster[i]+"\t");
		}
		System.out.println();
		for(int i=0;i<this.clusterCount;i++)
		{
			for(int j=0;j<this.dimensionalityCount;j++)
			{
				//System.out.print(this.clusterCenter.get(i)[j]+"\t");
			}
			//System.out.println();
		}
		double errorNew=0.0;
		while(true)
		{
			int flag=0;
			if(Math.abs(oldRadius-newRadius)<1E-10 && this.clusterCount==2)
			{
				//如果满足条件则跳出
				System.out.println("跳出:"+"\t"+oldoldRadius+"\t"+oldRadius+"\t"+newRadius+"\t"+clusterCount);
				oldoldRadius=oldRadius;
				oldRadius=newRadius;
				newRadius=0.0;
				break;
			}
			if(Math.abs(oldRadius-newRadius)<(Math.abs(oldRadius-this.oldoldRadius)/3)&& newRadius-oldRadius<0 && this.clusterCount>2)
			{
				//如果满足条件则跳出
				System.out.println("跳出:"+"\t"+oldoldRadius+"\t"+oldRadius+"\t"+newRadius+"\t"+clusterCount);
				oldoldRadius=oldRadius;
				oldRadius=newRadius;
				newRadius=0.0;
				break;
			}
			if(clusterCount==3)
			{
				System.out.println("444:"+"\t"+oldoldRadius+"\t"+oldRadius+"\t"+newRadius+"\t"+clusterCount);
			}
			if(clusterCount==4)
			{
				System.out.println("444:"+"\t"+oldoldRadius+"\t"+oldRadius+"\t"+newRadius+"\t"+clusterCount);
			}
			if(clusterCount==5)
			{
				System.out.println("444:"+"\t"+oldoldRadius+"\t"+oldRadius+"\t"+newRadius+"\t"+clusterCount);
			}
			if(clusterCount==6)
			{
			
				System.out.println("444:"+"\t"+oldoldRadius+"\t"+oldRadius+"\t"+newRadius+"\t"+clusterCount);
				break;
			}
			oldoldRadius=oldRadius;
			oldRadius=newRadius;
			newRadius=0.0;
			boolean computeFlag=false;
			clusterTempDouble=new double[clusterCount];
		
			while(true)
			{
				for(int i=0;i<clusterCount;i++)
				{
					clusterTempDouble[i]=Double.MIN_VALUE;
				}
				//执行计算
				flag++;
				errorOld=errorNew;
				 errorNew=0.0;
				for(int i=0;i<fileLength;i++)
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
						for(int j=0;j<this.dimensionalityCount;j++)
						{
							value+=Math.pow(matrix[i][j]-clusterCenter.get(k)[j],2.0);
						}
						if(valueUp>value)
						{
							valueUp=value;
							this.cluster[i]=k;
						}
					}
					errorNew+=Math.sqrt(valueUp);
					if(errorNew>this.clusterTempDouble[cluster[i]])
					{
						clusterTempDouble[cluster[i]]=errorNew;
					}
					
				}
				//System.out.println("第"+flag+"次");
				//for(int i=0;i<fileLength;i++)
				//{
				//	System.out.print(this.cluster[i]+"\t");
				//}
				//System.out.println();
				
				//重新计算聚类中心
				for(int i=0;i<this.clusterCount;i++)
				{
					int count=0;
					double[] ll=new double[fileLength];
					for(int j=0;j<fileLength;j++)
					{
						if(cluster[j]==i)
						{
							count++;
							for(int m=0;m<this.dimensionalityCount;m++)
							{
								ll[m]+=matrix[j][m];
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
						for(int j=0;j<this.dimensionalityCount;j++)
						{
							clusterCenter.get(i)[j]=ll[j]/count;
						}
					}
				}
				for(int i=0;i<this.clusterCount;i++)
				{
					for(int j=0;j<this.dimensionalityCount;j++)
					{
					//	System.out.print(this.clusterCenter.get(i)[j]+"\t");
					}
					//System.out.println();
				}
				if(flag==1)
				{
					continue;
				}
				else
				{
					if(Math.abs(errorNew-errorOld)<1E-7)
					{
						//System.out.println("收敛:聚类算法执行次数:"+flag+"\t方差和为:"+errorNew);
						//收敛的时候需要判断
						newComputeRadius=0.0;
						for(int zn=0;zn<this.clusterCount;zn++)
						{
							if(clusterCenterFlag[zn]==true)
							{
								newComputeRadius+=this.clusterTempDouble[zn];
							}
						}
						if(Math.abs(oldComputeRadius-newComputeRadius)<1E-10 && this.clusterCount==2)
						{
							//如果满足条件则跳出
							System.out.println("跳出:"+"\t"+oldoldRadius+"\t"+oldRadius+"\t"+newRadius+"\t"+clusterCount);
							oldoldComputeRadius=oldComputeRadius;
							oldComputeRadius=newComputeRadius;
							newComputeRadius=0.0;
							computeFlag=true;//标记为最终
						}
						if(Math.abs(oldComputeRadius-newComputeRadius)<(Math.abs(oldComputeRadius-this.oldoldComputeRadius)/3)&& newRadius-oldRadius<0 && this.clusterCount>2)
						{
							//如果满足条件则跳出
							System.out.println("跳出:"+"\t"+oldoldRadius+"\t"+oldRadius+"\t"+newRadius+"\t"+clusterCount);
							oldoldComputeRadius=oldComputeRadius;
							oldComputeRadius=newComputeRadius;
							newComputeRadius=0.0;
							computeFlag=true;//标记为最终
						}
						else
						{
							oldoldComputeRadius=oldComputeRadius;
							oldComputeRadius=newComputeRadius;
							newComputeRadius=0.0;
						}
						
						break;
					}
					errorOld=errorNew;
				}
			}
			if(computeFlag==true)
			{
				//如果正确则选择 clusterCount--个
				clusterCount--;
				cluster=new int[fileLength];
				for(int i=0;i<this.fileLength;i++)
				{
					this.cluster[i]=this.oldcluster[i];
				}
				this.clusterCenter=new ArrayList<double[]>();
				for(int i=0;i<this.clusterCount;i++)
				{
					this.clusterCenter.add(this.oldclusterCenter.get(i).clone());
				}
				System.out.println("计算后挑出");
				break;
			}
			oldcluster=new int[fileLength];
			for(int i=0;i<this.fileLength;i++)
			{
				this.oldcluster[i]=this.cluster[i];
			}
			this.oldclusterCenter=new ArrayList<double[]>();
			for(int i=0;i<this.clusterCount;i++)
			{
				this.oldclusterCenter.add(this.clusterCenter.get(i).clone());
			}
			clusterCount++;
			Canopy();
		}
	}
	

	/**
	 * 打印方法
	 */
	public void print()
	{
		for(int i=0;i<fileLength;i++)
		{
			System.out.print(this.cluster[i]+"\t");
		}
		System.out.println();
	}
	
	
	
	/**
	 *执行方法
	 * @param args
	 */
	public void run()
	{
		stander();
		Canopy();
		this.clusterCompute();
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
		Kmeans kmeans=new Kmeans(data,0.1,0.2);
		kmeans.run();
		kmeans.print();
		
		
		
		Vector<Double> x=new Vector<Double>();
		Vector<Double> y=new Vector<Double>();
		Vector<Integer> cluster=new Vector<Integer>();
		int[] clusterK=kmeans.cluster;
		for(int i=0;i<data.length;i++)
		{
			x.add(data[i][0]);
			y.add(data[i][1]);
			cluster.add(clusterK[i]);
			
			//System.out.println("数据:"+data[i][0]+"\t"+data[i][1]+"\t"+clusterK[i]);
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
