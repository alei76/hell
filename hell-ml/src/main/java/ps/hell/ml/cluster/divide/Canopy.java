package ps.hell.ml.cluster.divide;

import static com.googlecode.javacv.cpp.opencv_highgui.cvNamedWindow;
import static com.googlecode.javacv.cpp.opencv_highgui.cvShowImage;
import static com.googlecode.javacv.cpp.opencv_highgui.cvWaitKey;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import ps.hell.math.base.matrix.Matrix;
import ps.hell.ml.statistics.base.Normal;
import ps.hell.util.plot.opencv.BubbleGraph;

import com.googlecode.javacv.cpp.opencv_core;


/**
 * canopy算法
 * @author Administrator
 *
 */
public class Canopy {
	/**
	 * 纬度
	 */
	public int dim=0;
	/**
	 * t1 t2 值
	 */
	public float t1=0f;
	/**
	 * t1 t2 值
	 */	
	public float t2=0f;
	/**
	 * 原数据
	 */
	LinkedList<float[]> data=new LinkedList<float[]>();
	LinkedList<float[]> data2=new LinkedList<float[]>();
	/**
	 * 簇中心
	 */
	ArrayList<float[]> centor=new ArrayList<float[]>();
	/**
	 * 用于临时存储每一次在T2和t1内的float值
	 */
	ArrayList<LinkedList<float[]>> centorTemp=new ArrayList<LinkedList<float[]>>();
	/**
	 * 簇中心对应的数量
	 */
	ArrayList<Integer> centorCount=new ArrayList<Integer>();
	public Canopy(LinkedList<float[]> data,float t1,float t2)
	{
		this.t1=t1;
		this.t2=t2;
		for(float[] da:data)
		{
			this.data.add(da.clone());
			this.data2.add(da.clone());
		}
		if(data.size()>0)
		dim=data.get(0).length;
	}
	public Canopy(float[][] data,float t1,float t2)
	{
		this.t1=t1;
		this.t2=t2;
		for(float[] da:data)
		{
			this.data.add(da.clone());
			this.data2.add(da.clone());
		}
		if(data.length>0)
		dim=data[0].length;
	}
	public Canopy(double[][] data,float t1,float t2)
	{
		this.t1=t1;
		this.t2=t2;
		for(double[] da:data)
		{
			float[] f=new float[da.length];
			for(int i=0;i<da.length;i++)
			{
				f[i]=(float)da[i];
			}
			this.data.add( f);
			this.data2.add( f);
		}
		if(data.length>0)
		dim=data[0].length;
	}
	/**
	 * 执行程序
	 */
	public void run()
	{
		int count=0;
		while(true)
		{
			count++;
			int size_old=this.data.size();
			int size_new=0;
			
			float[] newCluster=null;
			if(centor.size()==0)
			{
				newCluster=this.data.pollFirst();
			}
			Iterator<float[]> iter=this.data.iterator();
			while(iter.hasNext())
			{
				float[] val=iter.next();
				//判断是否在簇中
				int i=-1;
				boolean flag=false;
				for(float[] cen:centor)
				{
					i++;
					float dist=getEDist(val,cen);
					if(dist<t2)
					{
					//	centorTemp.get(i).add(val);
						iter.remove();
						flag=true;
						break;
					}else if(dist<t1)
					{
					//	centorTemp.get(i).add(val);
						//flag=true;
					}else{
					}
				}
				if(!flag)
				{
					newCluster=val;
					iter.remove();
					break;
				}else{
					//获取处理了多少样本
					size_new++;
				}
			}
			//重新计算类中心
			//mStep();
//			for(float[] f:centor)
//			{
//				for(float fl:f)
//				{
//					System.out.print(fl+"\t");
//				}
//				System.out.println();
//			}
			//判断是否新增簇
			if(newCluster!=null)
			{
				//新增簇
				centor.add(newCluster);
			//	centorTemp.add(new LinkedList<float[]>());
//				if(size_new+1==size_old)
//				{//最后一次可以不算入
//					break;
//				}
			}
			System.out.println("次数："+count+"\t数量:"+size_new+"\t"+this.data.size());
			if(this.data.size()==0)
			{
				break;
			}
//			if(size_new==size_old)
//			{//全部处理完成
//				break;
//			}
		}
		System.out.println("结束");
	}
	/**
	 * 获取f1 和f2的欧几里得
	 * @param f1
	 * @param f2
	 * @return
	 */
	public float getEDist(float[] f1,float[] f2)
	{
		float dist=0f;
		for(int i=0;i<f1.length;i++)
		{
			dist+=Math.pow(f1[i]-f2[i],2f);
		}
		return (float) Math.sqrt(dist);
	}
	
	public void mStep()
	{
		int i=-1;
		for(LinkedList<float[]> c:centorTemp)
		{
			i++;
			float[] temp=new float[dim];
			for(float[] c_temp:c)
			{
				for(int j =0;j<dim;j++)
				{
					temp[j]+=c_temp[j];
				}
			}
			//System.out.print(i+":"+c.size());
			for(int j=0;j<dim;j++)
			{
				//System.out.print("\t"+temp[j]);
				temp[j]/=c.size();
			}
			//System.out.println();
			//获取均值并填充
			centor.set(i,temp);
			c=new LinkedList<float[]>();
		}
	}
	
	public int[] cluster=null;
	public boolean[] clusterFlag=null;
	//分的数量
	public int[] centorC=null;
	public void runKmeans(int count)
	{
		centorC=new int[centor.size()];
		cluster=new int[data2.size()];
		clusterFlag=new boolean[data2.size()];
		float error_old=0f;
		for(int i=0;i<count;i++)
		{
			int j=-1;
			for(float[] val:data2)
			{
				j++;
				float min=Float.MAX_VALUE;
				int l=-1;
				//获取最近中心
				for(float[] cen:centor)
				{
					l++;
					float dist=getEDist(val,cen);
					if(min>dist)
					{
						min=dist;
						cluster[j]=l;
					}
				}
			}
			//调整中心
			float error=mStepKmeans();
			System.out.println("kmeans:"+i+"\terror:"+error);
			if(Math.abs(error-error_old)<1E-4)
			{
				System.out.println("收敛");
				break;
			}
			error_old=error;
		}
	}
	
	/**
	 * 随机初始化k数量
	 * @param k
	 */
	public void randomCentor(int k)
	{
		this.centor=new ArrayList<float[]>();
		//获取极值信息
		float[] max=new float[dim];
		float[] min=new float[dim];
		for(int i=0;i<dim;i++)
		{
			max[i]=-Float.MAX_VALUE;
			min[i]=Float.MAX_VALUE;
		}
		for(float[] f:data2)
		{
			for(int i=0;i<f.length;i++)
			{
				if(f[i]>max[i])
				{
					max[i]=f[i];
				}
				if(f[i]<min[i])
				{
					min[i]=f[i];
				}
			}
		}
		for(int i=0;i<k;i++)
		{
			float[] temp=new float[dim];
			for(int j=0;j<dim;j++)
			{
				temp[j]=(float) (Math.random()*(max[j]-min[j])-min[j]);
			}
			this.centor.add(temp);
		}
	}
	/**
	 * 执行kmeans步骤
	 * @return
	 */
	public float mStepKmeans()
	{
		float val=0f;
		int l=-1;
		ArrayList<float[]> temp=new ArrayList<float[]>();
		for(float[] cen:centor)
		{
			l++;
			centorC[l]=0;
			temp.add(cen.clone());
			for(int i=0;i<cen.length;i++)
			{
				cen[i]=0f;
			}
		}
		//计算k
		for(int i=0;i<cluster.length;i++)
		{
			for(int j=0;j<data2.get(i).length;j++)
			{
				centor.get(cluster[i])[j]+=data2.get(i)[j];
			}
			centorC[cluster[i]]++;
		}
		//
		for(int i=0;i<centor.size();i++)
		{
			if(centorC[i]>0)
			for(int j=0;j<dim;j++)
			{
				centor.get(i)[j]/=centorC[i];
			}
		}
		for(int i=0;i<centor.size();i++)
		{
			for(int j=0;j<dim;j++)
			{
				val+=Math.abs(temp.get(i)[j]-centor.get(i)[j]);	
			}
		}
		return val;
	}
	public int[] getCluster()
	{
		return cluster;
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
		
		double[][] data=data1.getData();
		Canopy canopy=new Canopy(data,4f,3.5f);
		canopy.randomCentor(8);
		//canopy.run();
		canopy.runKmeans(100);
		//绘图模块
		Vector<Double> x=new Vector<Double>();
		Vector<Double> y=new Vector<Double>();
		Vector<Integer> cluster=new Vector<Integer>();
		int[] clusterK=canopy.getCluster();
		for(int i=0;i<data.length;i++)
		{
			x.add(data[i][0]);
			y.add(data[i][1]);
			cluster.add(clusterK[i]);
			
			System.out.println("数据:"+data[i][0]+"\t"+data[i][1]+"\t"+clusterK[i]);
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
