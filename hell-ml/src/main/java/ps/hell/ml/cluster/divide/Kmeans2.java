package ps.hell.ml.cluster.divide;
/**
 * 运用 方差控制 k值的kmeans 基础方法
 * 需要完善
 * @author Administrator
 *
 */
public class Kmeans2 {
	double[][] inputData;
	//初始化的k值
	int k;
	int fileLen;
	int length;
	double[] mu;
	/**
	 * 当前样本 对应到最短k中心的欧式距离
	 */
	double stdOld;
	/**
	 *累计欧氏距离
	 */
	double stdNew;
	
	public int[] clusterOld;
	//本来该写2个java文件完成的  就弄成一个了 
	public int[] clusterNew;
	/**
	 * 上一次对应的 k中心
	 */
	double[][] centerOld;
	/**
	 *本次对应的k中心
	 */
	double[][] centerNew;

	public Kmeans2(double[][] data, int cluster) {
		this.inputData=data;
		this.k=cluster;
		this.mu = new double[k];
		this.fileLen = data.length;
		this.length = data[0].length;
		this.clusterNew = new int[fileLen];
	
		//要调用一下 k中心初始化的函数你没调用 或者 放到 run函数中 都可
	}

public class Kmean {
	double[][] inputData;
	//初始化的k值
	int k;
	int fileLen;
	int length;
	/**
	 * 当前样本 对应到最短k中心的欧式距离
	 */
	double dist1;
	/**
	 * 用于存储上次 的累计欧氏距离
	 */
	double errorOld;
	/**
	 * 本次
	 */
	double errorNew;
	
	
	int[] clusterNew;

	double std=0D;
	/**
	 *本次对应的k中心
	 */
	double[][] centerNew;

	/**
	 * 用于存储k中心对应的样本数
	 */
	int[]  kCount=null;
	public Kmean(double[][] data, int cluster) {
		this.inputData=data;
		this.k=cluster;
		this.fileLen = data.length;
		this.length = data[0].length;
		this.clusterNew = new int[fileLen];
		kCount=new int[cluster];
	
		//要调用一下 k中心初始化的函数你没调用 或者 放到 run函数中 都可
	}

	/**
	 * 初始质心
	 * @param cluster 聚类数量
	 */
	public void initCenter(int cluster) {
		this.centerNew = new double[cluster][length];
		for (int i = 0; i <cluster; i++) {
			for (int j = 0; j < length; j++) {
				centerNew[i][j] = Math.random();
			}
		}
	}
	// 计算每一次 确定k值 的kmeans迭代方法s
	public void actionIterator(int cluster) {
		
		//初始化 k中心
		initCenter(cluster);
		System.out.println(this.clusterNew.length);
		//迭代 次数
		int count =0;
		while(true)
		{
			count++;
			if(count>1000)
			{
				break;
			}
			//初始化kcount
			for(int i=0;i<cluster;i++)
			{
				kCount[i]=0;
			}
			//每一次迭代要初始化 否则 存储的是上一次值
			errorNew=0.0;
			for(int i=0;i<this.fileLen;i++)
			{
				double  min=Double.MAX_VALUE;
				double value=0D;
				for(int k=0;k<cluster;k++)
				{
					value=0D;
					//计算样本到 某一个 k的欧氏距离
					for(int j=0;j<this.length;j++)
					{
						value+=Math.pow(inputData[i][j]-this.centerNew[k][j],2D);
					}
					//判断大小并存储
					if(value<min)
					{
						min=value;
						this.clusterNew[i]=k;
					}
				}
				errorNew+=min;
			}
			//计算 k中心
			//用于求和 所以要初始化下 double的初始化都是0.0
//			this.centerNew=new double[cluster][this.length];
			for(int i=0;i<cluster;i++)
			{
				for(int j=0;j<length;j++)
					this.centerNew[i][j]=0D;
			}
			for(int i=0;i<this.fileLen;i++)
			{
				//因为 我们的k是从0开始的 所以 clusterNew中存储 的样本归属的k值其实就是 序号
				kCount[this.clusterNew[i]]++;
				for(int j=0;j<this.length;j++)
				this.centerNew[this.clusterNew[i]][j]+=inputData[i][j];
			}
			//计算均值
			for(int i=0;i<cluster;i++)
			{
				for(int j=0;j<this.length;j++)
				{
					//好你那个其实一样  只是计算方法不同 如果 现在不明白没关系 你这个部分逻辑是正确的
					
					//这个地方会有一个bug
					if(kCount[i]!=0)
					this.centerNew[i][j]/=kCount[i];
				}
			}
			//查看是否收敛
			if(Math.abs(errorOld-errorNew)<1E-2)
			{
					System.out.println("收敛"+"\t"+errorNew);
					break;
			}
		//	System.out.println("iterator count is:"+count+"\t"+errorNew);
			errorOld=errorNew;
		}
		//当收敛后 我们需要计算方差
		//因为 迭代此次书很多  所以不该在内部做计算 会有很多 无效的计算
		///errorNew jiushi  sumzhi 
		// std  compute  is ok buzhidao  duibudui
		
		//计算方差过程 内部有冗余代码 看情况消除 也可自己写
		double[] snp=new double[cluster];
		double[] snp2=new double[cluster];
		for(int i=0;i<this.fileLen;i++)
		{
			double dis=0.0;
			for(int j=0;j<length;j++)
			{
				snp[this.clusterNew[i]]+=Math.pow(inputData[i][j]-this.centerNew[this.clusterNew[i]][j], 2D);
			}
		}
		for(int i=0;i<cluster;i++)
		{
			if(kCount[i]!=0)
			snp[i]/=length*this.kCount[i];
		}
		for(int i=0;i<this.fileLen;i++)
		{
			double dis=0.0;
			for(int j=0;j<length;j++)
			{
				dis+=Math.pow(inputData[i][j]-this.centerNew[this.clusterNew[i]][j], 2D);
			}
			snp2[clusterNew[i]]+=Math.pow(dis/length-snp[clusterNew[i]],2D);
		}
		for(int i=0;i<cluster;i++)
		{
			if(kCount[i]!=0)
			snp2[i]/=this.kCount[i];
			std+=snp2[i];
		}
		///System.out.println("st0:"+std);
		std/=cluster;
		System.out.println("std:"+std);
	}


	public void run() {
		// this.getRandomCluster();
		//kan ni 这门也没有初始化质心 光写 了函数 不去执行 没用
		this.actionIterator(k);
	}

	/**
	 * 执行方法
	 * 
	 * @param args
	 */
	public  void main2(String[] args) {

		double[][] data = new double[100][5];
		for (int i = 0; i < 100; i++) {
			for (int j = 0; j < 5; j++) {
//				if (i < 50 && j == 4) {
//					data[i][j] = 1;
//				} else if (i >= 50 && j == 4) {
//					data[i][j] = 4;
//				} else

				if (i < 50)
					data[i][j] = Math.random() / 2;
				else
					data[i][j] = 0.5 + Math.random() / 2;
			}
		}
		Kmean kmeans = new Kmean(data, 4);
		kmeans.run();

	}
}
	/**
	 * 计算k是否满足条件 为迭代停止
	 */
	public void computeSimK()
	{
		int cluster=k;
		while(true)
		{
			// evey k  to compute  once kmean
			Kmean km=new Kmean(this.inputData,cluster);
			km.run();
			// get  std
			stdNew=km.std;
			//compute  is  ok
			if((stdOld-stdNew)>0 && (stdOld-stdNew)/stdOld>0.7)
			{
				break;
			}
			stdOld=stdNew;
			cluster--;
			if(cluster==1)
			{
				break;
			}
		}
		System.out.println(" k is the end value:"+cluster);

	}

	public void print() {
		//for (int i = 0; i < fileLen; i++) {
		//	for (int j = 0; j < this.data[0].length; j++) {
	//			System.out.print(this.data[i][j] + "\t");
	//		}
	//		System.out.println("cluster:" + this.cluster[i]);
	//	}
	}

	public void run() {
		// this.getRandomCluster();
		//kan ni 这门也没有初始化质心 光写 了函数 不去执行 没用
		this.computeSimK();
	}



	/**
	 * 执行方法
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		double[][] data = new double[100][5];
		for (int i = 0; i < 100; i++) {
			for (int j = 0; j < 5; j++) {
//				if (i < 50 && j == 4) {
//					data[i][j] = 1;
//				} else if (i >= 50 && j == 4) {
//					data[i][j] = 4;
//				} else

				if (i < 50)
					data[i][j] = Math.random() / 2;
				else
					data[i][j] = 0.5 + Math.random() / 2;
			}
		}
		Kmeans2 kmeans = new Kmeans2(data, 50);
		kmeans.run();
		kmeans.print();

	}
}
