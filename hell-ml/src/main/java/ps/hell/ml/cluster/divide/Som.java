package ps.hell.ml.cluster.divide;

import static com.googlecode.javacv.cpp.opencv_highgui.cvNamedWindow;
import static com.googlecode.javacv.cpp.opencv_highgui.cvShowImage;
import static com.googlecode.javacv.cpp.opencv_highgui.cvWaitKey;

import java.util.Random;
import java.util.Vector;

import ps.hell.math.base.MathBase;
import ps.hell.math.base.matrix.Matrix;
import ps.hell.ml.statistics.base.Normal;
import ps.hell.util.plot.opencv.BubbleGraph;

import com.googlecode.javacv.cpp.opencv_core;

/**
 *   SOM聚类，主要是因为这是基于神经网络的一种算法，
 *   而神经网络本身又是机器学习中的一个重要方法，
 * @author Administrator
 *
 */
public class Som {

	/**
	 * 输入数据
	 */
	public double[][] inputData=null;
	/**
	 * 0最小值1最大值2最小值index 3 最大值index 
	 */
	public double[][] minMaxValue=null;
	/**
	 * 分类号从0开始
	 */
	public int[] cluster=null;
	/**
	 * 初始化隐层数量
	 */
	public int layerCount=0;
	/**
	 * 隐层值
	 */
	public double[][] layerValue=null;
	/**
	 * 隐层是否被使用
	 */
	public boolean[] layerFlag=null;
	/**
	 * 中间键 两个隐层间的 高斯函数
	 * 
	 *存储为截断高斯函数
	 *h{i,j}=exp(-d{i,j}^2/(2*alpha^2))*cita(alpha-d{i,j})
	 *city(x)={1,x>=0  ; 0,x<=0}
	 */
	public double[][] hgaussian=null;
	
	/**
	 * 对应的 点的与临阶获胜节点的Hjc值
	 */
	public double[] hgaussianJ=null;
	/**
	 * 样本长度
	 */
	public int fileLength=0;
	/**
	 * 维度
	 */
	public int dimailyLength=0;
	/**
	 * 执行次数上限
	 */
	public int execMaxLimit=100;
	/**
	 * 迭代次数
	 */
	public int execCount=0;
	/**
	 * 误差
	 */
	public double errorOld=0.0;
	/**
	 * 误差差异上限
	 * |error-errorOld|<errorLimit
	 */
	public double errorLimit=1E-4;
	/**
	 * 学习率
	 * 0.2-0.9
	 */
	public double yita=0.2;
	public double yita0=0.2;
	/**
	 * 邻域的半径
	 * 初始默认为0.5
	 */
	public double alpha=0.1D;
	public double alpha0=0.1D;
	/**
	 * 
	 * @param inputData 输入数据
	 * @param layerCount 隐层数量
	 * @param execMaxLimit 迭代次数上限
	 * @param errorLimit 本次误差和上次误差之间的差异最大值
	 * @param yita 学习率
	 */
	public Som(double[][] inputData,int layerCount,int execMaxLimit,double errorLimit,double yita)
	{
		if(inputData==null ||inputData.length==0)
		{
			System.out.println("输入数据有误:请确认数据信息");
			System.exit(0);
		}
		this.execMaxLimit=execMaxLimit;
		this.yita=yita;
		fileLength=inputData.length;
		this.layerCount=layerCount;
		dimailyLength=inputData[0].length;
		cluster=new int[fileLength];
		minMaxValue=new double[4][dimailyLength];
		//输入数据的标准化
		this.inputData=MathBase.normalizationRange(inputData,0,
				1,"range",minMaxValue);
		
		System.out.println("打印标准化数据");
//		for(int i=0;i<inputData.length;i++)
//		{
//			for(int j=0;j<inputData[i].length;j++)
//			{
//				System.out.print(this.inputData[i][j]+"\t");
//			}
//			System.out.println();
//		}
		//初始化 随机节点数
		Random random=new Random();
		layerValue=new double[layerCount][dimailyLength];
		layerFlag=new boolean[layerCount];
		hgaussian=new double[layerCount][layerCount];
		hgaussianJ=new double[layerCount];
		for(int i=0;i<layerCount;i++)
		{
			for(int j=0;j<dimailyLength;j++)
			{
				layerValue[i][j]=random.nextDouble();
			}
		}
	}
	/**
	 * 执行程序
	 */
	public void exec()
	{
		while(true)
		{
			execCount++;
			if(execCount>execMaxLimit)
			{
				break;
			}
			//修改 每一个样本对应的layer
			double error=competitiveIndex();
			if(Math.abs(error-errorOld)<errorLimit)
			{
				System.out.println("数据已收敛，迭代次数为:"+execCount+"\t误差为:"+errorOld);
				break;
			}
			errorOld=error;
			//执行中间计算高斯过程
			cooperative();
			 //修改权值
			adaptive();
			//修改参数
			adaptParam();
			Vector<Double> x=new Vector<Double>();
			Vector<Double> y=new Vector<Double>();
			Vector<Integer> cluster2=new Vector<Integer>();
			int[] clusterK=cluster;
			for(int i=0;i<inputData.length;i++)
			{
				x.add(inputData[i][0]);
				y.add(inputData[i][1]);
				cluster2.add(clusterK[i]);
			//	System.out.println("数据:"+data[i][0]+"\t"+data[i][1]+"\t"+clusterK[i]);
			}
			//System.exit(1);
			//绘图模块
			
//			BubbleGraph graph=new BubbleGraph();
//			graph.width=600;
//		    graph.height=400;
//			
//			graph.parentent_config();//计算改变的所有全局参量
//			  //初始化图片 
//				BubbleGraph.img_trans img1=new BubbleGraph.img_trans();
//			    //main_type="scatter";
//			    img1.img1=opencv_core.cvCreateImage(opencv_core.cvSize(graph.width,graph.height),8,3);
//			    graph.main_type="qipao2";
//			    
//			    if(graph.main_type=="scatter" ||graph.main_type=="qipao" ||graph.main_type=="qipao2" ||graph.main_type=="qipao3" )
//			    {
//			        System.out.println("初始化图片");
//			        graph.plotScatter(img1,x,y,graph.main_type,cluster2,0);
//			    }
//			    
//			    cvNamedWindow("test",1);
//			    cvShowImage("test",img1.img1);
//			    cvWaitKey(0);
		}
	}
	/**
	 * 获取竞争后获取的layer的index
	 * min_j=1~k{||x-wj||}
	 * @return 全部误差
	 */
	public double competitiveIndex()
	{
		//初始化隐层使用情况
		for(int i=0;i<layerCount;i++)
		{
			layerFlag[i]=false;
		}
		//全部误差
		double error=0.0;
		for(int i=0;i<fileLength;i++)
		{
			int index=-1;
			double min=Double.MAX_VALUE;
			for(int k=0;k<this.layerCount;k++)
			{
				double minvalk=0.0;
				for(int j=0;j<this.dimailyLength;j++)
				{
					minvalk+=Math.abs(inputData[i][j]-layerValue[k][j]);
				}
				if(min>minvalk)
				{
					min=minvalk;
					index=k;
				}
			}
			//修改
			cluster[i]=index;
			layerFlag[index]=true;
			error+=min;
		}
		return error;
	}
	/**
	 * 合作过程 计算中间键过程
	 * hgaussian
	 */
	public void cooperative()
	{
		for(int i=0;i<this.layerCount;i++)
		{
			hgaussianJ[i]=0.0;
		}
		//h{j,t}=exp(-d{t,j}^2/(2*alpha^2))
		for(int i=0;i<this.layerCount;i++)
		{
			if(layerFlag[i])
			{
				//如果不为获胜神经元
			}
			//获取兴奋神经元集合
			for(int j=0;j<this.layerCount;j++)
			{
				//d{j,t}=|t-j| 或者 
				double dist=0.0;
				if(i==j)
				{
					//Wc(t+1)=Wc(t)+yita(t)*||x(t)-Wc(t)||
					//为1
					hgaussian[i][j]=1D;
					continue;
				}
				for(int l=0;l<this.dimailyLength;l++)
				{
					dist+=Math.pow(layerValue[i][l]-layerValue[j][l],2.0);
				}
				dist=Math.sqrt(dist);
				//获取是否为邻域神经元
				//下边这步已经包含了 是否为邻域神经元
				//d{j,t}=||r{j}-r{t}||
		
				 if(alpha-dist>=0)
				{
					hgaussian[i][j]=Math.exp(-dist*dist/2/(alpha*alpha));
					//修改对应兴奋神经元与该获胜神经元的高斯值
					//Hjc=max(|hij-hic|)
					double lp=0.0;
					
					hgaussianJ[j]=hgaussianJ[j]>hgaussian[i][j]?hgaussianJ[j]:hgaussian[i][j];
				
				}else{
					hgaussian[i][j]=0;
				}
					System.out.println(j+":+"+dist+"+:"+hgaussianJ[j]);
				hgaussian[j][i]=hgaussian[i][j];
			}
		
		}
//		System.out.println("打印hgaussian");
//		for(int i=0;i<this.layerCount;i++)
//		{
//			System.out.println(hgaussianJ[i]+"\t");
//		}
		
	}
	/**
	 * 动态调整隐层值过程
	 */
	public void adaptive()
	{
		for(int i=0;i<layerCount;i++)
		{
			//寻找 该节点的临界获胜节点
			for(int k=0;k<layerCount;k++)
			{
				if(layerFlag[k])
				{
					//如果不为获胜神经元
				}
			}
			for(int j=0;j<this.dimailyLength;j++)
			{
				//wi(n+1)=wi(n)+yita(n)*hij(n)*(x-wi(n))
				this.layerValue[i][j]=this.layerValue[i][j]+
						yita*hgaussianJ[i]*(this.inputData[i][j]-layerValue[i][j]);
			}
		}
		System.out.println("权值打印");
		for(int i=0;i<layerCount;i++)
		{
			for(int j=0;j<this.dimailyLength;j++)
			{
				System.out.print(layerValue[i][j]+"\t");
			}
			System.out.println();
		}
	//	System.exit(1);
	}
	/**
	 * 每一次修改参数值
	 */
	public void adaptParam()
	{
		//修改 yita
		//线性函数yita=yita0(1-n/T)
		//幂函数yita=yita0*exp(-n/T)
		//反比函数yita=yitao/(1+100n/T)
		//T为迭代次数
		//yita=yita0*Math.exp(-this.execCount*1.0/(-1/Math.log(yita0)));
		yita*=0.98;
		if(yita<0.2)
		{
			yita=0.2;
		}
		//System.out.println("yita:"+yita);
		//修改半径
		//alpha=alpha0*exp(-n/T1)
		//T1=Trp/log(alpha0)
		alpha=alpha0*Math.exp(-execCount*1.0/(-1/Math.log(alpha0+0.4)*2));
		if(alpha<0.02)
		{
			alpha=0.02;
		}
		//System.out.println("alpha:"+alpha);
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
		Som som=new Som(data,10,2000,1E-4,0.9);
		som.exec();
		
		
		Vector<Double> x=new Vector<Double>();
		Vector<Double> y=new Vector<Double>();
		Vector<Integer> cluster=new Vector<Integer>();
		int[] clusterK=som.getCluster();
		for(int i=0;i<data.length;i++)
		{
			x.add(data[i][0]);
			y.add(data[i][1]);
			cluster.add(clusterK[i]);
		//	System.out.println("数据:"+data[i][0]+"\t"+data[i][1]+"\t"+clusterK[i]);
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
