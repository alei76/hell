package ps.hell.ml.cluster.divide;

import java.util.LinkedList;
import java.util.Random;

/**
 * 是一种kmeans的改进算法
 * 提供了解决数值与分类的聚类方法
 * 数值型需要标准化，并且为欧几里得距离
 * x1+拉姆达*x2为特征空间总距离
 * 其中拉姆达一般设置为1.1
 * @author Administrator
 *
 */
public class KPrototypes {

	
	/**
	 * 数据源 分别为数值和字符串型
	 */
	public LinkedList<String[]> dataString=new LinkedList<String[]>();
	
	/**
	 * 存储聚类中心类的距离sqrt（sum(x^2)）
	 */
	public LinkedList<Double> dataStringDouble=new LinkedList<Double>();
	
	public LinkedList<Double[]> dataDouble=new LinkedList<Double[]>();
	
	/**
	 * 存储每一列对应的所有分类元素，长度为列长
	 */
	public LinkedList<String[]> dataClassString=new LinkedList<String[]>();
	/**
	 * 拉姆达值
	 */
	public Double LMD=1.1;
	
	/**
	 * 聚类数量
	 */
	public int clusterCount=0;
	
	/**
	 * 聚类的中心
	 */
	public LinkedList<Double[]> clusterCenterDouble=new LinkedList<Double[]>();
	/**
	 * 聚类的中心
	 */
	public LinkedList<String[]> clusterCenterString=new LinkedList<String[]>();
	
	/**
	 * 存储聚类中心类的距离sqrt（sum(x^2)）
	 */
	public LinkedList<Double> clusterCenterStringDouble=new LinkedList<Double>();
	
	/**
	 * 存储数值型变量的标准化最大最小值
	 */
	public LinkedList<Double[]> maxMinValue=new LinkedList<Double[]>();
	
	/**
	 * 样本所属分类
	 */
	public Integer[] cluster;
	/**
	 * 样本相似度值
	 */
	public Double[] clusterValue;
	/**
	 * 记录类是否有效
	 */
	public boolean[] clusterFlag;
	
	/**
	 * 存储对应有多少列
	 */
	public int colDouble=0;
	public int colString=0;
	
	public int computeCount=0;
	/**
	 * 
	 * @param dataDouble 输入的数值数据
	 * @param dataString 输入的分类数据
	 */
 	public KPrototypes(LinkedList<Double[]> dataDouble,LinkedList<String[]> dataString)
	{
		for(Double[] data:dataDouble)
		{
//			for(int i=0;i<data.length;i++)
//			{
//				System.out.print(data[i]+"\t");
//			}
//			System.out.println();
			this.dataDouble.add(data.clone());
		}
		for(String[] data:dataString)
		{
			this.dataString.add(data.clone());
		}
		cluster=new Integer[dataString.size()];
		clusterValue=new Double[dataString.size()];
		
		try
		{
			this.colDouble=dataDouble.get(0).length;
		}
		catch(Exception e)
		{
			
		}
		try
		{
			this.colString=dataString.get(0).length;
		}
		catch(Exception e)
		{
			
		}	
		//计算每列的所有元素
		//System.out.println(colDouble+"\t"+colString+"\t"+dataString.get(0).length);
		for(int i=0;i<this.colString;i++)
		{
			LinkedList<String> str=new LinkedList<String>();
			for(int j=0;j<dataString.size();j++)
			{
				int count=0;
				for(int m=0;m<str.size();m++)
				{
					//System.out.println(dataString.get(j)[i]+"\t"+str.get(m)+"\t"+j+"\t"+i+"\t"+m);
					if(dataString.get(j)[i].equals(str.get(m)))
					{
						break;
					}
					else
					{
						count++;
					}
				}
				if(count==str.size())
				{
					//System.out.println("添加类:"+dataString.get(j)[i]);
					str.add(dataString.get(j)[i]);
				}
			}
			String[] str2=new String[str.size()];
			for(int j=0;j<str.size();j++)
			{
				str2[j]=str.get(j);
			}
			dataClassString.add(str2);
		}
		
	}
	
 	/**
 	 * 标准化
 	 */
 	public void stander()
 	{
 		if(dataDouble.size()==0)
 		{
 			return;
 		}
 		for(int i=0;i<dataDouble.get(0).length;i++)
 		{
 			Double valueMax=Double.MIN_VALUE;
 			Double valueMin=Double.MAX_VALUE;
 			for(int j=0;j<dataDouble.size();j++)
 			{
 				if(dataDouble.get(j)[i]<valueMin)
 				{
 					valueMin=dataDouble.get(j)[i];
 				}
 				if(dataDouble.get(j)[i]>valueMax)
 				{
 					valueMax=dataDouble.get(j)[i];
 				}
 			}
 			Double[] val=new Double[2];
 			val[0]=valueMax;
 			val[1]=valueMin;
 			maxMinValue.add(val);
 			//标准化
 			for(int j=0;j<dataDouble.size();j++)
 			{
 				dataDouble.get(j)[i]=(dataDouble.get(j)[i]-valueMin)/(valueMax-valueMin);
 			}
 		}
 	}
 	/**
	 * 
	 * @param clusterCount 预聚类数量
	 */
	public void Init(int clusterCount)
	{
		this.clusterCount=clusterCount;
		Random random=new Random();
		clusterFlag=new boolean[clusterCount];
		for(int i=0;i<this.clusterCount;i++)
		{
			clusterFlag[i]=true;
			
			Double[] d=new Double[this.colDouble];
			for(int j=0;j<this.colDouble;j++)
			{
				d[j]=random.nextDouble();
			}
			clusterCenterDouble.add(d);
			String[] s=new String[this.colString];
			for(int j=0;j<this.colString;j++)
			{
				Double l=random.nextDouble();
				int len=dataClassString.get(j).length;
				Double len2=1.0/len;//间隔长度
				//System.out.println(len+"\t"+len2+"\t"+"\t"+l+"\t"+((int)(l/len2)));
				s[j]=dataClassString.get(j)[((int)(l/len2))];
			}
			clusterCenterString.add(s);
		}
		System.out.println("初始化结束");
	}

	
	/**
	 * 聚类过程
	 */
	public void cluster()
	{
		
		System.out.println("类中心");
		for(int i=0;i<this.clusterCount;i++)
		{
			if(dataDouble.size()!=0)
			{
				for(int j=0;j<this.colDouble;j++)
				{	
					System.out.print(dataDouble.get(i)[j]+"\t");
				}
			}
			for(int j=0;j<colString;j++)
			{
				System.out.print(dataString.get(i)[j]+"\t");
			}
			System.out.println();
		}
		Double clusterOldValue=0.0;
		Double clusterNewValue=Double.MIN_VALUE;
		Double clusterOldOldValue=Double.MIN_VALUE;
		while(true)
		{
			computeCount++;
			System.out.println("computeCount:"+computeCount);
			clusterNewValue=0.0;
			boolean isDouble=true;
			if(dataDouble.size()==0)
			{
				isDouble=false;
			}
			for(int i=0;i<this.dataString.size();i++)
			{
				Double oldValue=Double.MAX_VALUE;
				int oldIndex=-1;
				for(int m=0;m<this.clusterCenterString.size();m++)
				{
					if(clusterFlag[m]==false)
					{
						continue;
					}
					Double value=0.0;
					if(isDouble)
					{
					
						for(int j=0;j<this.dataDouble.get(i).length;j++)
						{
							//欧几里得距离
							value+=Math.pow(dataDouble.get(i)[j]-clusterCenterDouble.get(m)[j],2.0);
						}
						value=Math.sqrt(value);
					}
					for(int j=0;j<this.dataString.get(i).length;j++)
					{
						//System.out.println("---");
						//System.out.println(i+"\t"+j+"\t"+m+"\t"+dataString.get(i)[j]+"\t"+clusterCenterString.get(m)[j]);
						//分类相似度距离
						Double value1=0.0;
						if(dataString.get(i)[j].equals(clusterCenterString.get(m)[j]))
						{
							value1+=1.0;
						}
						//clusterCenterStringDouble.get(m)/dataStringDouble.get(i)
						value+=this.LMD*value1/colString;
					}
					if(value<oldValue)
					{
						oldValue=value;
						oldIndex=m;
					}
				}
				
				clusterNewValue+=oldValue;
				//修改所属分类
				this.cluster[i]=oldIndex;
				this.clusterValue[i]=oldValue;
			}
			
			//重新计算类中心
			//类中心分开计算
			//计算聚类数量
			
			int cluCount=0;
			System.out.println("重新计算类中心");
			for(int m=0;m<this.clusterCenterString.size();m++)
			{
				//类中心Double值变化
				if(clusterFlag[m]==false)
				{
					continue;
				}
				if(isDouble)
				{
					Double[] value=new Double[colDouble];
					for(int mm=0;mm<colDouble;mm++)
					{
						value[mm]=0.0;
					}
					int count=0;
					//System.out.println(dataDouble.size()+"\t"+cluster.length+"\t"+colDouble+"\t"+dataDouble.get(0).length);
					for(int n=0;n<cluster.length;n++)
					{
						if(cluster[n]==m)
						{
							count++;
							for(int l=0;l<colDouble;l++)
							{
								//System.out.println("值:"+dataDouble.get(n)[l]);
								value[l]+=this.dataDouble.get(n)[l];
							}
						}
					}
					if(count<=1)
					{//如果长度小于等于1则无效标记为无效聚类簇
						clusterFlag[m]=false;
						continue;
					}
					for(int l=0;l<colDouble;l++)
					{
						value[l]=value[l]/count;
					}
					this.clusterCenterDouble.set(m, value.clone());
				}
				cluCount++;
				
				//类中心String值变化//取最多的类为一个组
				for(int n=0;n<clusterCenterString.get(m).length;n++)
				{
					LinkedList<String> StringTemp=new LinkedList<String>();
					
					LinkedList<Integer> DoubleTemp=new LinkedList<Integer>();
					int count2=0;
					if(clusterFlag[m]==false)
					{
						continue;
					}
					for(int l=0;l<dataString.size();l++)
					{
						if(this.cluster[l]!=m)
						{
							
							continue;
						}
						count2++;
						boolean flag=false;
						for(int p=0;p<StringTemp.size();p++)
						{
							if(dataString.get(l)[n].equals(StringTemp.get(p)))
							{
								DoubleTemp.set(p,DoubleTemp.get(p)+1);
								flag=true;
							}
						}
						if(flag==false)
						{
							StringTemp.add(dataString.get(l)[n]);
							DoubleTemp.add(1);
						}
					}
					if(count2<=1)
					{//如果长度小于等于1则无效标记为无效聚类簇
						clusterFlag[m]=false;
						continue;
					}
					
					for(int l=0;l<StringTemp.size();l++)
					{
						for(int p=0;p<dataString.size();p++)
						{
							if(this.cluster[p]!=m)
							{
								continue;
							}
							if(StringTemp.get(l).equals(dataString.get(p)[n]))
							{
								DoubleTemp.set(l,DoubleTemp.get(l)+1);
							}
						}
					}
					//取出最大的
					int maxIndex=Integer.MIN_VALUE;
					String maxString="";
					if(StringTemp.size()==0)
					{	
						continue;
					}
					if(StringTemp.get(0).equals("0")|| StringTemp.get(0).equals("1"))
					{
						//处理稀疏性
						if(StringTemp.size()==1)
						{
							maxString=StringTemp.get(0);
						}
						else
						{
							String l=StringTemp.get(0);
							String l2=StringTemp.get(1);
							Integer li=DoubleTemp.get(0);
							Integer li2=DoubleTemp.get(1);
							if(l.equals("0"))
							{
								if(li2*1.0/(li+li2)>0.1)
								{
									maxString="1";
								}
								else
								{
									maxString="0";
								}
							}
							else
							{
								if(li*1.0/(li+li2)>0.1)
								{
									maxString="1";
								}
								else
								{
									maxString="0";
								}
							}
						}
						
					}
					else
					{
						for(int l=0;l<DoubleTemp.size();l++)
						{
							if(DoubleTemp.get(l)>maxIndex)
							{
								maxIndex=DoubleTemp.get(l);
								//System.out.println(m+"\t"+n+"\t"+l+"\t"+DoubleTemp.get(l));
								maxString=StringTemp.get(l);
								
							}
						}
					}
					//System.out.println("修改:"+this.clusterCenterString.get(m)[n]+"\t:"+maxString);
					this.clusterCenterString.get(m)[n]=maxString;
				}
			}
			
			
			
			System.out.println("执行次数:"+computeCount+"\t误差:"+clusterNewValue+"\t类数量："+cluCount+"\t差异:"+Math.abs(clusterOldValue-clusterNewValue));
			if(clusterOldOldValue.equals(clusterNewValue))
			{
				clusterOldOldValue=clusterOldValue;
				clusterOldValue=clusterNewValue;
				System.out.println("聚类因循环而异常收敛：聚类次数为:"+this.computeCount+"\t误差值为:"+clusterOldValue);
				break;
			}
			if(Math.abs(clusterOldValue-clusterNewValue)<1E-5)
			{
				clusterOldOldValue=clusterOldValue;
				clusterOldValue=clusterNewValue;
				System.out.println("聚类正常收敛：聚类次数为:"+this.computeCount+"\t误差值为:"+clusterOldValue);
				
			}
			
			else
			{
				clusterOldOldValue=clusterOldValue;
				clusterOldValue=clusterNewValue;
			}
		}
	}

	/**
	 * 调度函数
	 * @param clusterCount
	 * @return
	 */
	public Integer[] run(int clusterCount)
	{
		stander();
		Init(clusterCount);
		cluster();
		return cluster;
	}
}
