package ps.hell.ml.cluster.divide;

import java.util.LinkedList;
import java.util.Random;


/**
 * kmean的计算01矩阵的方法
 * @author Administrator
 *
 */
public class KmeansCos {
	/**
	 * 存储用户属性
	 */
	public LinkedList<String> name=new LinkedList<String>();
	/**
	 * 存储用户01列表
	 */
	public LinkedList<Integer[]> data=new LinkedList<Integer[]>();
	
	/**
	 * 每一行属于的分类
	 */
	public int[] cluster;
	
	
	/**
	 * 每一行属于的分类的差异值
	 */
	public Double[] clusterValue;
	
	/**
	 * 存储聚类对应的类是否有效
	 */
	public boolean[] clusterFlag; 
	
	/**
	 * 存储类对应的类中心
	 */
	public LinkedList<Double[]> clusterCenterValue=new LinkedList<Double[]>();
	
	/**
	 * 存储类中心的sqrt(x^2)值
	 */
	public LinkedList<Double> clusterCenterXValue=new LinkedList<Double>();
	
	
	
	/**
	 * 存储数据行的sqrt(x^2)值
	 */
	public LinkedList<Double> dataXValue=new LinkedList<Double>();
	/**
	 * 存储列长
	 */
	public int colCount=0;
	/**
	 * 存储横长
	 */
	public int rowCount=0;
	
	/**
	 * 聚类数量
	 */
	public int clusterCount=0;
	
	/**
	 * 迭代次数
	 */
	public int computeCount=0;
	/**
	 * 
	 * @param name 列表名
	 * @param data 对应的矩阵 其中0为多1为少
	 * @param clusterCount 初始化聚类数量
	 */
	public KmeansCos(LinkedList<String> name,LinkedList<String[]> data,int clusterCount)
	{
		this.clusterCount=clusterCount;
		try
		{
			colCount=data.get(0).length;
			rowCount=name.size();
		}
		catch(Exception e)
		{
			System.out.println("data数据输入错误");
			return;
		}
		if(rowCount!=data.size())
		{
			System.out.println("name和data长度不同");
			return;
		}
		for(String na:name)
		{
			this.name.add(na);
		}

		for(String[] inl:data)
		{
			Integer[] temp=new Integer[colCount];	
			double count=0.0;
			for(int j=0;j<inl.length;j++)
			{
				if(inl[j].equals("1"))
				{
					count+=1;
				}
				temp[j]=Integer.parseInt(inl[j]);
			}
			this.dataXValue.add(Math.sqrt(count));
			this.data.add(temp);
		}
		
	}
	/**
	 * 初始化类中心
	 */
	public void init()
	{
		clusterFlag=new boolean[clusterCount];
		cluster=new int[rowCount];
		clusterValue=new Double[rowCount];
		Random random=new Random();
		
		for(int i=0;i<rowCount;i++)
		{
			cluster[i]=-1;
			clusterValue[i]=0.0;
		}
		for(int i=0;i<this.clusterCount;i++)
		{
			clusterFlag[i]=true;
			Double[] temp=new Double[colCount];
			Double count=0.0;
			for(int j=0;j<colCount;j++)
			{
				if(random.nextDouble()<=0.3)
				{
					//赋值为1
					temp[j]=1.0;
					count+=1;
					
				}
				//否则赋值为0
				else
				{
					temp[j]=0.0;
				}
			}
			clusterCenterValue.add(temp);
			clusterCenterXValue.add(Math.sqrt(count));
		}
		
	}
	/**
	 * 执行
	 */
	public void  compute()
	{
		
		for(int i=0;i<clusterCount;i++)
		{
			System.out.print("类:"+i+"\t");
			for(int j=0;j<colCount;j++)
			{
				System.out.print(clusterCenterValue.get(i)[j]+"\t");
			}
			System.out.println();
		}
		double errorOldValue=0.0;
		double errorNewValue=0.0;
		
		while(true)
		{
			//计算类中心距离
			errorNewValue=0.0;
			this.computeCount++;
			for(int i=0;i<rowCount;i++)
			{
				Double value=Double.MIN_VALUE;
				int index=-1;
				for(int m=0;m<clusterCount;m++)
				{
					Double value1=0.0;
					if(clusterFlag[m]==false)
					{
						continue;
					}
					//用cos余弦计算
					for(int j=0;j<colCount;j++)
					{
						if(data.get(i)[j]==0)
						{
							continue;
						}
						value1+=Math.pow(data.get(i)[j]*clusterCenterValue.get(m)[j],2.0);
					}
					if(this.clusterCenterXValue.get(m)<1E-3)
					{
						System.out.println("初始化错误");
						continue;
					}
					//System.out.println(value1);
					value1=value1/this.dataXValue.get(i)/this.clusterCenterXValue.get(m);
					if(value1>value)
					{
						index=m;
						value=value1;
					}
				}
				cluster[i]=index;
				clusterValue[i]=value;
				errorNewValue+=value;
			}
		//	System.out.println("erroroValue:"+errorNewValue);
			//重新计算类中心
			for(int m=0;m<clusterCount;m++)
			{
				if(clusterFlag[m]==false)
				{
					continue;
				}
				int count=0;
				Double[] temp=new Double[colCount];
				for(int mn=0;mn<colCount;mn++)
				{
					temp[mn]=0.0;
				}
				for(int i=0;i<rowCount;i++)
				{
					if(cluster[i]==m)
					{
						count++;
						for(int j=0;j<colCount;j++)
						{
							temp[j]+=data.get(i)[j];
						}
					}
				}
				
				if(count<=1)
				{
					clusterFlag[m]=false;
					continue;
				}
				else
				{
					for(int j=0;j<colCount;j++)
					{
						temp[j]/=count;
					}
					//修改增益损失
					
					for(int j=0;j<colCount;j++)
					{
						if(temp[j]>0.6)
						{
							temp[j]=1.0;
						}
						else if(temp[j]>0.35)
						{
							temp[j]=temp[j]/1.3;
						}
						else if(temp[j]>0.1)
						{
							temp[j]=temp[j]/2.3;
						}
						else if(temp[j]>0.03)
						{
							temp[j]=temp[j]/3.3;
						}
						else
						{
							temp[j]=0.0;
						}
					}
					clusterCenterValue.set(m, temp.clone());
					//System.out.println("修改类中心");
//					for(Double tpm:temp)
//					{
//						System.out.print(tpm+"\t");
//					}
//					System.out.println();
					//重新计算Math.sqrt(x^2)
					Double valuec=0.0;
					for(Double tp:temp)
					{
						valuec+=Math.pow(tp,2.0);
					}
					
					clusterCenterXValue.set(m,Math.sqrt(valuec));
				}
			}
			//判断是否收敛
			int countfl=0;
			for(boolean te:clusterFlag)
			{
				if(te==true)
				{
					countfl++;
				}
			}
			System.out.println("迭代次数:"+computeCount+"\t类中心数量:"+countfl+"\t相似度累计和:"+errorNewValue);
			if(Math.abs(errorNewValue-errorOldValue)<1E-5)
			{
				System.out.println("收敛");
				errorOldValue=errorNewValue;
				break;
			}
			errorOldValue=errorNewValue;
			
		}
		
		for(int i=0;i<clusterCount;i++)
		{
			if(clusterFlag[i]==false)
			{
				continue;
			}
			System.out.print("类:"+i+"\t");
			for(int j=0;j<colCount;j++)
			{
				System.out.print(clusterCenterValue.get(i)[j]+"\t");
			}
			System.out.println();
		}
	}
	

	/**
	 * 调度
	 * 
	 */
	public int[] run()
	{
		this.init();
		this.compute();
		return cluster;
	}
}

