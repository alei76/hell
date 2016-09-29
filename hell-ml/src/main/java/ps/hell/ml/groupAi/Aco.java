package ps.hell.ml.groupAi;

import java.util.Date;
import java.util.Random;

/**
 * 蚁群算法  网络结构计算
 * @author Administrator
 *
 */
public class Aco {
	
	/**
	 * 信息素最大数量
	 */
	public int pheromoneMax=1000;
	/**
	 * 锁点
	 */
	public Object lock=new Object();
	/**
	 * 随机数
	 */
	public Random random=new Random();
/**
 * 输入数据的 网络结构 并且为 int类型
 * 其中-1为障碍物  -2为 start -3为end
 *初始化
 */
	public int[][] inputData=null;
	/**
	 * 信息素矩阵
	 * 该部分需要加入线程锁
	 */
	public int[][] pheromone=null;
	/**
	 * 转义矩阵 //实际的共享矩阵
	 */
	//public double[][] translateP=null;
	
	/**
	 * 蚂蚁数量
	 */
	public int antCount=1;
	/**
	 * 结束地点
	 */
	public Point endPoint=null;
	/**
	 * 开始地点
	 */
	public Point startPoint=null;
	
	
	public static class Point
	{
		/**
		 * 行
		 */
		public int x=0;
		/**
		 * 列
		 */
		public int y=0;
		public Point(int x,int y)
		{
			this.x=x;
			this.y=y;
		}
	}
	/**
	 * 蚂蚁线程
	 * @author Administrator
	 *
	 */
	public class AntThread implements Runnable
	{

		/**
		 * 蚂蚁执行次数
		 */
		private int execCount=0;
		private int printCount=100;
		/**
		 * 打印周期
		 * @param printCount
		 */
		public AntThread(int execCount,int printCount)
		{
			this.execCount=execCount;
			this.printCount=printCount;
		}
		@Override
		public void run()
		{
			execCount++;
			int count=0;


			for(int cn=1;cn<execCount+1;cn++)
			{
				//初始化执行矩阵
				Point step=new Point(startPoint.x,startPoint.y);
				//如果为1则表示执行过 -1为障碍，-2为开始 -3为结束
				int[][] execP=new int[inputData.length][];
				for(int i=0;i<inputData.length;i++)
				{
					execP[i]=inputData[i].clone();
				}
				//根据信息素行进
				while(true)
				{
					if(cn%printCount==0)
					{
						System.out.println(Thread.currentThread().getName()+"号蚂蚁执行次数:"+cn);
					}
					//处理边界计算
					 int direct = getDirect(step,execP);
					 //添加信息素
					 if(direct==0)
					 {
						// System.out.println(Thread.currentThread().getName()+":堵死:"+cn);
						 //如果路线堵死则跳出
						 break;
					 }
					 else if(direct==1)
					 {
						 execP[step.x-1][step.y]++;
						 //调整 step位置
						 step=new Point(step.x-1,step.y);
					 }else if(direct==2)
					 {
						 execP[step.x][step.y-1]++;
						 //调整 step位置
						 step=new Point(step.x,step.y-1);
					 }else if(direct==3)
					 {
						 execP[step.x][step.y+1]++;
						 //调整 step位置
						 step=new Point(step.x,step.y+1);
					 }
					 else{
						 execP[step.x+1][step.y]++;
						 //调整 step位置
						 step=new Point(step.x+1,step.y);
					 }
					 //判断是否是结束点
					 if(step.x==endPoint.x && step.y==endPoint.y)
					 {
						 System.out.println("获取正确:"+Thread.currentThread().getName()+":序列:"+cn);
						 break;
					 }
				}
				//当蚂蚁正确找到终点则 调整信息素矩阵，否则 信息素矩阵不变化
				if(step.x==endPoint.x && step.y==endPoint.y)
				{
					//调整信息素矩阵
					synchronized (lock) {
						int max=0;
						for(int i=0;i<execP.length;i++)
						{
							for(int j=0;j<execP[i].length;j++)
							{
								if(execP[i][j]>0)
								{
									pheromone[i][j]++;
									if(pheromone[i][j]>max)
									{
										max=pheromone[i][j];
									}
								}
							}
						}
						max-=pheromoneMax;
						if(max>0)
						{
							for(int i=0;i<execP.length;i++)
							{
								for(int j=0;j<execP[i].length;j++)
								{
									if(pheromone[i][j]>1)
									{
										//最小值为1需要修正，并且值修改 路径上信息
										pheromone[i][j]=pheromone[i][j]-max<1?1:pheromone[i][j]-max;
									}
								}
							}
						}
					}
					
					//做减小操作
				}else
				{
					//不操作
				}
			}
			System.out.println(Thread.currentThread().getName()+"号蚂蚁已执行完毕:");
		}
	}
	/**
	 * 返回  如果为0则表示没有路可走了
	 *   1
	 *  2 3
	 *   4
	 * @param current 为当前蚂蚁运行的xy地点 对应的方向
	 * @param execP 
	 * @return
	 */
	public int getDirect(Point current,int[][] execP)
	{
		//计算信息素信息
		//获取上侧
		Point next=null;
		double[] listPhe=new double[4];
		double sum=0.0;
		if(current.x-1>=0)
		{
			next=new Point(current.x-1,current.y);
			//需要判断next节点 是不是终点
			if(next.x==this.endPoint.x && next.y==this.endPoint.y)
			{
				return 1;
			}
			//获取信息素
			listPhe[0]=getAvaPhern(next,execP);
			sum+=listPhe[0];
		}
		//获取左侧
		if(current.y-1>=0)
		{
			next=new Point(current.x,current.y-1);
			//需要判断next节点 是不是终点
			if(next.x==this.endPoint.x && next.y==this.endPoint.y)
			{
				return 2;
			}
			listPhe[1]=getAvaPhern( next,execP);
			sum+=listPhe[1];
		}
		//获取右侧
		if(current.y+1<inputData[0].length)
		{
			next=new Point(current.x,current.y+1);
			//需要判断next节点 是不是终点
			if(next.x==this.endPoint.x && next.y==this.endPoint.y)
			{
				return 3;
			}
			listPhe[2]=getAvaPhern( next,execP);
			sum+=listPhe[2];
		}
		//获取下侧
		if(current.x+1<inputData.length)
		{
			next=new Point(current.x+1,current.y);
			//需要判断next节点 是不是终点
			if(next.x==this.endPoint.x && next.y==this.endPoint.y)
			{
				return 4;
			}
			listPhe[3]=getAvaPhern(next,execP);
			sum+=listPhe[3];
		}
		if(sum<1E-10)
		{
			//没有信息素了 则 返回无
			return 0;
		}
		//获取比例
		for(int i=0;i<4;i++)
		{
			listPhe[i]/=sum;
		}
		//修改为累计求和
		for(int i=0;i<4;i++)
		{
			if(i==0)
			{}else{
			listPhe[i]+=listPhe[i-1];
			}
		}
		double ra=random.nextDouble();
		for(int i=0;i<4;i++)
		{
			if(ra<listPhe[i])
			{
				return i+1;
			}
		}
		 return 4;
		
	}
	
	/**
	 * 获取有效信息素
	 * @param next
	 * @param execP
	 * @return
	 */
	public int getAvaPhern(Point next,int[][] execP)
	{
		//如果走过或者为开始结束或者路障则返回信息素为0
		return (execP[next.x][next.y]<0|execP[next.x][next.y]>0)?0:pheromone[next.x][next.y];
	}
	/**
	 * 
	 * @param inputData 输入网络结构
	 * @param antCount 蚁群数量
	 * @param startPoint 开始点
	 * @param endPoint 结束点
	 * @param pheromoneMax 为信息素存储上限
	 * @param execCount 为每个蚂蚁的执行次数
	 * 其中信息素矩阵 基础值为1，-1为障碍 -2 为开始 -3为结束
	 */
	public Aco(int[][] inputData,int antCount,Point startPoint,Point endPoint,
			int pheromoneMax)
	{
		if(inputData==null|inputData.length==0)
		{
			System.out.println("输入数据数组错误 请检查并重新输入");
			System.exit(0);
		}
		this.pheromoneMax=pheromoneMax;
		this.inputData=new int[inputData.length][inputData[0].length];
		this.pheromone=new int[inputData.length][inputData[0].length];
		for(int i=0;i<inputData.length;i++)
		{
			this.inputData[i]=inputData[i].clone();
			//初始化pheromne为都是1
			for(int j=0;j<inputData[i].length;j++)
			{
				if(inputData[i][j]==0)
				{
					this.pheromone[i][j]=1;
				}else{
					this.pheromone[i][j]=inputData[i][j];
				}
			}
		}
		this.inputData[startPoint.x][startPoint.y]=-2;
		this.pheromone[startPoint.x][startPoint.y]=-2;
		this.pheromone[endPoint.x][endPoint.y]=-3;
		this.inputData[endPoint.x][endPoint.y]=-3;
		//this.translateP=new double[inputData.length][inputData[0].length];
		this.antCount=antCount;
		this.startPoint=startPoint;
		this.endPoint=endPoint;
	}
	/**
	 * 
	 * @param execCount 蚂蚁执行次数
	 * @param printCount 打印周期
	 */
	public void Exec(int execCount,int printCount)
	{
		Thread[] antThread=new Thread[antCount];
		AntThread ant=new AntThread(execCount,printCount);
		for(int i=0;i<antCount;i++)
		{
			antThread[i]=new Thread(ant,Integer.toString(i+1));
			antThread[i].start();
		}
		for(int i=0;i<antCount;i++)
		{
			try {
				antThread[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void printPathMatrix()
	{
		for(int i=0;i<pheromone.length;i++)
		{
			for(int j=0;j<pheromone[i].length;j++)
			{
				System.out.print(pheromone[i][j]+"\t");
			}
			System.out.println();
		}
	}
	public static void main(String[] args) {
		Date start=new Date();
		// 原始迷宫图
		int[][] inputData = { { -1, 0, -1, -1, -1, -1, -1, 0 },
	             { -1, 0, -2, -1, 0, 0, -3, 0 },
	             { -1, 0, 0, -1, 0, 0, 0, 0 },
	             { 0, 0, -1, 0, 0, -1, 0, -1 },
	             { -1, 0, 0, 0, -1, -1, 0, 0 },
	             { 0, -1, 0, 0, 0, 0, 0, 0 },
	             { 0, 0, 0, 0, 0, 0, 0, 0 } };
		Aco aco=new Aco(inputData,10,new Point(1,2),new Point(1,6),100);
		aco.Exec(100,101);
		aco.printPathMatrix();
		System.out.println((new Date().getTime()-start.getTime())+"毫秒");
	}
}
/*
 * 
	
	# -*- coding:utf-8 -*-
	"""
	Created on 2013-5-12
	@author: sx
	"""
	import numpy as np
	import pylab as pl
	import matplotlib.pyplot as plt
	#用蚁群解路径寻优问题
	#其实不用距离公式有一种很方便和简便的寻路算法
	#初始数据
	#4个障碍物4定点坐标
	barrier1=np.matrix('40 140;60 160;100 140;60 120')
	barrier2=np.matrix('50 30;30 40;80 80;100 40')
	barrier3=np.matrix('120 160;140 100;180 170;165 180')
	#barrier4=np.matrix('120 40;170 40;140 80')
	#初始化起点终点
	xstart=np.array([20,180])
	xend=np.array([160,90])
	#为减少复杂度规定每10个为一个点
	#初始化位置矩阵为0为不可用
	pos_x=np.arange(0,201,10).reshape(-1,1)*np.linspace(1,1,21)
	pos_y=np.linspace(1,1,21).reshape(-1,1)*np.arange(0,201,10)
	pos=np.zeros([21,21])
	for i in range(21):
	    for j in range(21):
	        if ((pos_y[i][j]-pos_x[i][j]-100)<0 and (pos_y[i][j]+0.5*pos_x[i][j]-190)<0 and (pos_y[i][j]-0.5*pos_x[i][j]-90)>0 and (pos_y[i][j]+pos_x[i][j]-180)>0 ) or\
	            ((pos_y[i][j]-0.8*pos_x[i][j]-16)<0 and (pos_y[i][j]+2*pos_x[i][j]-240)<0 and (pos_y[i][j]-0.2*pos_x[i][j]-20)>0 and (pos_y[i][j]+0.5*pos_x[i][j]-55)>0) or\
	            ((pos_y[i][j]-45.0/20*pos_x[i][j]-10)<0 and (pos_y[i][j]+2.0/3*pos_x[i][j]-290)<0 and (pos_y[i][j]-7.0/4*pos_x[i][j]+145)>0 and (pos_y[i][j]+3*pos_x[i][j]-520)>0) :
	            pos[i][j]=0
	        else:
	            pos[i][j]=1
	#计算距离
	#pos_x1=pos_x.reshape(-1,1)
	#pos_y1=pos_y.reshape(-1,1)
	pos_1=pos.reshape(-1,1)
	print 'pos_1:',pos_1.shape
	pos_dict=pos*5
	#此处不考虑用最小路径的dig
	#蚁群算法搜索
	p1=0.5#初始化信息素
	p2=2#初始化信息参数
	n=100#迭代次数
	m=20#蚁数量
	kmz=0
	for i in range(21):
	    for j in range(21):
	        if pos_x[i,j]==20 and pos_y[i,j]==180:
	            xstart1=[i,j]#初始化开始
	            kmz=1
	            break
	    if kmz==1:
	        break
	kmz=0
	for i in range(21):
	    for j in range(21):
	        if pos_x[i,j]==160 and pos_y[i,j]==90:
	            xend1=[i,j]#初始化结束
	            kmz=1
	            break
	    if kmz==1:
	        break
	print 'i,j',xstart1
	pos[2,18]=0
	pos2=pos.copy()#初始化信息素距离阵
	#蚁群迭代
	for i in range(20):#range(n):
	    path_all=[]
	    for j in range(10):
	        path1=[]#初始化搜索路径
	        path2=[]#记录对应的2位置
	        ks1=np.array(xstart1)#初始化第一搜索位置
	        ks=ks1.copy()
	        path_1=[]#获取单词搜索长度
	        zx1=[]
	        zx2=[]
	        #print '第',i,'次',j,'个蚂蚁'
	        for k in range(2000):#搜索一只蚂蚁的总路径如果搜索soun10次或者
	                                #总计2000次依然没有到达终点则终止
	            #print 'k:',k,'soun',soun
	            if k==1500 :
	                print '终结',j,k
	                break
	            zx1=[]
	            zx2=[]
	            zx1=[]
	            zx2=[]
	            #print 'ks',ks
	            if (ks[0]+1)<=20:
	                    if pos2[ks[0]+1][ks[1]] !=0:
	                        zx1.append(ks[0]+1)
	                        zx2.append(ks[1])
	            if (ks[0]-1)>=0:
	                    if pos2[ks[0]-1][ks[1]] !=0:
	                        zx1.append(ks[0]-1)
	                        zx2.append(ks[1])
	            if (ks[1]+1)<=20:
	                    if pos2[ks[0]][ks[1]+1] !=0:
	                        zx1.append(ks[0])
	                        zx2.append(ks[1]+1)
	            if (ks[1]-1)>=0:
	                    if pos2[ks[0]][ks[1]-1] !=0:
	                        zx1.append(ks[0])
	                        zx2.append(ks[1]-1)
	            if len(zx2) ==0:#如果到了劲头则直接初始化
	                break
	            #获取相关的信息素
	            q1=np.cumsum(pos2[zx1,zx2])/np.sum(pos2[zx1,zx2]+1e-14)
	            if max(q1)==0:#如果到了劲头
	                break
	            z1=[]
	            for k2 in range(len(q1)):
	                #q=np.random.rand(1)#初始化随机数
	                #计算适应度
	                z1.append(np.sum((np.array([zx1[k2],zx2[k2]])-xend1)**2))
	            z3=np.argsort(z1)
	            #print 'z1',z1
	            #print 'z3',z3
	            z4=z3.copy()
	            m2=0
	            for m1 in range(len(z3))[::-1]:
	                
	                for m3 in range(len(z3)):
	                    if z3[m3]==m1:
	                        if m2==len(z3)-1:
	                            z4[m3]=m2*1.5
	                            m2=m2+1
	                        else:
	                            z4[m3]=m2
	                            m2=m2+1
	                        break
	            z4=np.array(z4)+1
	            #z4为排序的逆序
	            #适应度对应的筛选
	            z5=np.cumsum(z4)/(np.sum(z4)+1e-10)
	            #print 'zx1',zx1
	            #print 'zx2',zx2
	            #print 'pos',pos2[zx1[0],zx2[0]]
	            #print 'z4',z4
	            #print 'z5',z5
	            #print 'q1',q1
	            z5=z5*q1
	            #print 'z5',z5
	            p=np.random.random(1)
	            #print '比较',p,z5
	            for m4 in range(len(list(z5))):
	                if z5[m4]>=p:
	                    m6=m4
	                    break
	            #print 'm6',m6,zx1
	            #print 'm6',m6,zx2
	            pos2[zx1[m6],zx2[m6]]=0#处理掉执行的点
	            path_1.append(pos_dict[zx1[m6],zx2[m6]])#选择距离
	            path1.append(zx1[m6])#选择位置
	            path2.append(zx2[m6])
	            #print 'path1',path1
	            #print 'path2',path2
	            #print 'path1内部:',path1
	            #print 'path2内部:',path2
	            ks=[zx1[m6],zx2[m6]]
	            #print 'hell'
	            if  ks== xend1:
	                path1.append(160)
	                path2.append(90)
	                break
	        #计算一次的总长度并添加以重新修改信息素
	        #信息素修改
	        #print 'path1:',path1
	        #print ks
	        #print '结束'
	        ln=np.sum(path_1)
	        
	        #print 'ln',ln
	        if ln !=0:
	            pos2=pos2*0.95
	            for b1 in range(len(path1)-1):
	                pos2[path1[b1],path2[b1]]=pos2[path1[b1]][path2[b1]]+166.4332/ln/10
	#最终路径
	print '最终路径：',path1,len(path1)
	print type(path1)
	print '最终路径：',path2,len(path2)
	#pl.plot(np.array(path1),np.array(path2))
	pl.plot(np.array(path1),np.array(path2))
	pl.plot([4,6,10,6,4],[14,16,14,12,14],'g-')
	pl.plot([5,3,8,10,5],[3,4,8,4,3],'g-')
	pl.plot([12,14,18,16.5,12],[16,10,17,18,16],'g-')
	pl.plot([2,16],[18,9],'ro')
	pl.xlim([0,20])
	pl.ylim([0,20])
	pl.show()
	print pos2
	        
	#200次的效果1
	 


	#200次效果2
	其中取了一个到达终点并且 相对效果较好的

	其中搜索到终点的概率不高
	#1000次效果 （效果不好但概率高）
	 

	#2000


	可以看到效果不明显了
	主要原因是，搜索方式和信息素矩阵的增长效果不稳定，在随机性累计多了后对后续依靠轮盘跳出循环概率不成比率导致 

 */ 
