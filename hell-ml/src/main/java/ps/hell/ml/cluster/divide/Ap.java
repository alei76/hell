package ps.hell.ml.cluster.divide;

import static com.googlecode.javacv.cpp.opencv_highgui.cvNamedWindow;
import static com.googlecode.javacv.cpp.opencv_highgui.cvShowImage;
import static com.googlecode.javacv.cpp.opencv_highgui.cvWaitKey;

import java.util.HashSet;
import java.util.Vector;

import ps.hell.math.base.MathBase;
import ps.hell.math.base.matrix.Matrix;
import ps.hell.ml.statistics.base.Normal;
import ps.hell.util.plot.opencv.BubbleGraph;

import com.googlecode.javacv.cpp.opencv_core;

//存在错误 需要以后修改
/**
 *    Affinity Propagation（简称AP）是一个比较新的算法，在07年发表在
 *    Science上面，可见肯定是有一些独到之处的。
 *      吸引子传播算法的基本思想是将全部样本看作网络的节点,
 *       然后通过网络中各条边的消息传递,计算出各样本的聚类中心。
 *       聚类过程中,共有两种消息在各节点间传递,分别是吸引度
 *       ( responsibility)和归属度(availability) 。
 *       聚类的结果取决于样本间的相似性大小和消息传递。
 * @author Administrator
 *
 */
public class Ap {
	/**
	 * 输入数据
	 */
	public double[][] inputData=null;
	/**
 	* 相似度矩阵
 	* s(i,k)=-sqrt((xi-xk)^2+(yi-yk)^2) or jaccard 
 	* 
 	* 
 	* 图片或者 1 0值计算公式
 	* s(i,k)=m(i,k)-1/n*sum_j=1~n(m(i,j))-1/n*sum_j=1~n(m(j,k))
 	* 或 s(i,k)= - min|| ToXi-Txk||^2
 	*/
	public double[][] similarityMatrix=null;
	/**
	 * 传递信息矩阵 吸引度
	 * r(i,k)=s(i,k)-max_k'!=k{a(i,k')+s(i,k')};  
	 * k是否适合做i的聚类中心
	 *
	 */
	public double[][] responsibilityMatrix=null;
	
	/**
	 * 回馈矩阵 归属度
	 * 		|-   { min{0,r(k,k)+sum_i'!=i,k  max{0,r(i',k)}}  i!=k
	 * a(i,k)|
	 * 		|-   sum_i'!=k max{0,r(i',k)}   i==k
	 * i是否选择k为聚类中心
	 */
	public double[][] availabilityMatrix=null;
	
	/**
	 * a(i,k)+r(i,k)
	 */
	public double[][] exemplarMatrix=null;
	/**
	 * 分类号
	 */
	public int[] cluster=null;
	/**
	 * 样本长度
	 */
	public int fileLength=0;
	/**
	 * 维度
	 */
	public int dimailyLength=0;
	/**
	 * 执行次数
	 */
	public int execCount=100;
	
	public double den=0.0;
	/**
	 * 输入矩阵
	 * @param inputDate
	 * @param execCount 迭代次数
	 * @param den为阻尼 或者冲量 一般为 0.5-1  ---0.9
	 */
	public Ap(double[][] inputData,int execCount,double den)
	{
		if(inputData==null ||inputData.length==0)
		{
			System.out.println("输入数据有误:请确认数据信息");
			System.exit(0);
		}
		this.execCount=execCount;
		this.inputData=inputData;
		this.den=den;
		fileLength=inputData.length;
		dimailyLength=inputData[0].length;
		similarityMatrix=new double[fileLength][fileLength];
		this.responsibilityMatrix=new double[fileLength][fileLength];
		this.availabilityMatrix=new double[fileLength][fileLength];
		exemplarMatrix=new double[fileLength][fileLength];
		cluster=new int[fileLength];
	}

	public void exec()
	{
		//计算相似度
		for(int i=0;i<fileLength;i++)
		{
			for(int j=i+1;j<fileLength;j++)
			{
				if(i==j)
				{
					continue;
				}
				for(int l=0;l<dimailyLength;l++)
				{
					similarityMatrix[i][j]+=Math.pow(inputData[i][l]-inputData[j][l],2.0);	
				}
				similarityMatrix[i][j]=-Math.sqrt(similarityMatrix[i][j]);
				similarityMatrix[j][i]=similarityMatrix[i][j];
			}
		}
		for(int i=0;i<fileLength;i++)
		{
			//调整i,i位置 为Median
			similarityMatrix[i][i]=MathBase.getMedian(similarityMatrix[i]);
		}
		//迭代过程
		int eCountOld=0;
		for(int ec=0;ec<execCount;ec++)
		{
			double ll=0.0;
			ll++;
			if(ec%1==0)
			{
				System.out.println("ap执行次数:"+ec+"\t变更数量:"+eCountOld);
			}
			//获取 ememplar>=0的数量
			int eCount=0;
			for(int i=0;i<fileLength;i++)
			{
				//获取获得availabilityMatrix最高的k值
				double min=-Double.MAX_VALUE;
				int index=-1;
				for(int k=0;k<fileLength;k++)
				{
					//每个组对 第k类做计算
					//计算responsibility
					if(i==k)
					{
						// r(k,k)=s(k,k)-max_k'!=k{s(k,k')}
						//计算r值
						double r=similarityMatrix[k][k];
						double max=-Double.MAX_VALUE;
						double val=0.0;
						for(int kk=0;kk<fileLength;kk++)
						{
							if(kk==k)
							{
								continue;
							}
							val=similarityMatrix[k][kk];
							if(max<val)
							{
								//获取最大值
								max=val;
							}
						}
						//计算最终的r(i,k)值
						//加入阻尼
						responsibilityMatrix[k][k]=(den*responsibilityMatrix[k][k]+(1-den)*(r-max));
					}
					else
					{
						//计算r值
						double r=similarityMatrix[i][k];
						double max=-Double.MAX_VALUE;
						double val=0.0;
						// r(i,k)=s(i,k)-max_k'!=k{a(i,k')+s(i,k')}
						for(int kk=0;kk<fileLength;kk++)
						{
							if(kk==k)
							{
								continue;
							}
							val=availabilityMatrix[i][kk]+similarityMatrix[i][kk];
							if(max<val)
							{
								//获取最大值
								max=val;
							}
						}
						//计算最终的r(i,k)值
						//加入阻尼
						responsibilityMatrix[i][k]=den*responsibilityMatrix[i][k]+(1-den)*(r-max);
					}
					//计算a(i,k)值
					if(i==k)
					{
						//sum_i'!=k{max {0,r(i',k)}}
						double max22=0.0;
						for(int kk=0;kk<fileLength;kk++)
						{
							if(k==kk)
							{
								continue;
							}

							max22+=(responsibilityMatrix[kk][k]>0D?responsibilityMatrix[kk][k]:0D);
						}
						availabilityMatrix[i][k]=max22;
					}else{
						//min{0,r(k,k)+sum_i'!=i,k  max{0,r(i',k)}
						double max2=0.0;
						for(int kk=0;kk<fileLength;kk++)
						{
							if(i==kk || k==kk)
							{
								continue;
							}
							max2+=responsibilityMatrix[kk][k]>0D?responsibilityMatrix[kk][k]:0D;

						}
						double val3=(responsibilityMatrix[k][k]+max2)>0D?0D:(responsibilityMatrix[k][k]+max2);
						//做阻尼
						availabilityMatrix[i][k]=den*availabilityMatrix[i][k]+(1-den)*(val3);
					}
					//判断信息归属
					//计算e值
					//赋值
					exemplarMatrix[i][k]=availabilityMatrix[i][k]+responsibilityMatrix[i][k];
					if(exemplarMatrix[i][k]>min)
					{
						min=exemplarMatrix[i][k];
						index=k;
					}
				}
//				if(min>=0)
//				{
//					eCount++;
//				}
				if(cluster[i]==index)
				{
					
				}else{
					this.cluster[i]=index;
					eCount++;
				}
			}
			if(eCount!=eCountOld)
			{
				eCountOld=eCount;
				continue;
			}else{
				break;
			}
		}
		System.out.println("迭代结束");
		System.out.println("合并相同分类，修复cluster");
		modefiyCluster();
		System.out.println("结束执行");
		
//		System.out.println("r值");
//		for(int i=0;i<fileLength;i++)
//		{
//			for(int j=0;j<fileLength;j++)
//			{
//				System.out.print(responsibilityMatrix[i][j]+"\t");
//			}
//			System.out.println();
//		}
//		System.out.println("a值");
//		for(int i=0;i<fileLength;i++)
//		{
//			for(int j=0;j<fileLength;j++)
//			{
//				System.out.print(availabilityMatrix[i][j]+"\t");
//			}
//			System.out.println();
//		}
	}
	
	
	public void modefiyCluster()
	{
		//合并相同分类，修复cluster
		HashSet<Integer> set=new HashSet<Integer>();
		int[][] matrix=new int[fileLength][fileLength];
		for(int i=0;i<cluster.length;i++)
		{
			matrix[i][cluster[i]]=1;
			matrix[cluster[i]][i]=1;
		}
		int val=-1;
		for(int i=0;i<cluster.length;i++)
		{
			if(set.contains(i))
			{
				continue;
			}else
			{
				val=cluster[i];
				set.add(i);
				System.out.println(i+"对应值------:"+val);
				insertL(matrix,set,i,val);
			}
		}
	}
	/**
	 * @param matrix为0 1矩阵
	 * @param set 为已经加入的值
	 * @param index为 具体的值
	 * @param change 为修改的值
	 */
	public void insertL(int[][] matrix,HashSet<Integer> set,int index,int change)
	{
		for(int i=0;i<fileLength;i++)
		{
			if(matrix[i][index]==1)
			{
				if(set.contains(i))
				{
					//如果已经有了 则跳出 防止死循环
					continue;
				}
				//做递归调用
				set.add(i);
				int index2=cluster[i];
				cluster[i]=change;
				System.out.println("修改值:"+i+"\t为:"+change);
				insertL(matrix,set,index2,change);
			}
		}
	}
	/**
	 * 获取分类
	 * @return
	 */
	public int[] getCluster()
	{
		return cluster;
	}
	
	public static void main(String[] args) {
		System.out.println("初始化数据 ");
		Normal normal1=new Normal(4,0.3);
		Matrix data1=normal1.random(30,2);
		
		Normal normal2=new Normal(10,0.6);
		Matrix data2=normal2.random(30,2);
		
		Normal normal3=new Normal(-3,0.4);
		Matrix data3=normal3.random(20,2);
		
		Normal normal4=new Normal(1,0.4);
		Matrix data4=normal4.random(40,2);
		
		data1.addMatrix(data2);
		data1.addMatrix(data3);
		data1.addMatrix(data4);
		
		double[][] data=data1.getData();
		Ap ap=new Ap(data,100,0.9);
		ap.exec();
		
		
		Vector<Double> x=new Vector<Double>();
		Vector<Double> y=new Vector<Double>();
		Vector<Integer> cluster=new Vector<Integer>();
		int[] clusterK=ap.getCluster();
		for(int i=0;i<data.length;i++)
		{
			x.add(data[i][0]);
			y.add(data[i][1]);
			cluster.add(clusterK[i]);
			System.out.println("数据:"+data[i][0]+"\t"+data[i][1]+"\t"+clusterK[i]);
		}
		//System.exit(1);
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
