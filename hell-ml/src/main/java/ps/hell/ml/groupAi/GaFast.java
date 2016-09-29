package ps.hell.ml.groupAi;

import java.util.Random;

import ps.hell.util.scriptEngine.StringToFunction;

/**
*#GA算法作为生物计算的仿生算法产出应用在寻优的过程中
*#GA首选计算种群适应度，复制概率等
*#GA先将连续变量转换成连续的2进制变量
*#根据（轮盘选择）随机数选择性赋值与累计复制概率的比较
*#根据再一次随机数，选择交配染色体,其中交配规则可以使单断，或者是多段规则
*#从所有的2进制中随机选择对应的编号进行基因突变，2进制的反转
*#然后冲寻回到第二行开始寻混
*#GA算法核心是适应度和遗传过程的匹配
*/
public class GaFast {
//	#10进制转二进制
//	#x1<=12.1 x1>=-3
//	#x2<=5.8 x2>=4.1
//	#取出区间以精度.4
	//m1<-matrix(c(-3,4.1,12.1,5.8),2,2,1)#all.names()all.vars()
	/**
	 * rangeValue 获取每行为一个x对应的最小最大值
	 * 列为2列分别为最小值和最大值
	 */
	public double[][] rangeValue;
	
	/**
	 * 持续次数
	 */
	public int computeCount=0;
	/**
	 * 记录 x的数量
	 */
	public int rangeXCount;
	/**
	 * 标记x的名字
	 */
	public String[] x;
	
	/**
	 * 标记染色体的数量
	 */
	public int yCount=0;
	/**
	 * 染色体组 精度转换后的long 数组
	 */
	public long[][] yLong;
	
	/**
	 * 存储染色体组对应的多个x*精度的double 值
	 */
	public double[][] yValue;
	
	public double oldStd=0.0;
	public double newStd=0.0;
	/**
	 * 存储染色体组的适应度
	 */
	public double[] yFitness;
	/**
	 * 存储为轮盘时的累计概率
	 */
	public double[] yFitnessSum;
	
	/**
	 * 标记最大的位置
	 */
	public int yFitnessMaxIndex=0;
	/**
	 * 游走的精度
	 */
	public int precision=0;
	
	/**
	 * 存储x1，x2，x3对应的编译成2进制长度
	 */
	public int[] yLongLength;
	public int yLongLengthSum=0;
	public GaFast()
	{
		
	}
	/**
	 * rangeValue 获取每行为一个x对应的最小最大值
	 * 列为2列分别为最小值和最大值
	 * precision 设定精度
	 */
	public void rangeXx(String[] x,double[][] rangeValue,int precision)
	{
		this.rangeValue=rangeValue.clone();
		this.x=x.clone();
		//获取区间
		this.precision=precision;
	}
	
	/**
	 * 
	 * @param yCount 染色体数量
	 */
	public void random(int yCount)
	{
		this.yCount=yCount;
		Random random=new Random();
	
		//需要将行个 组合起来
		this.rangeXCount=rangeValue.length;
		yLongLength=new int[rangeValue.length];
		this.yFitness=new double[yCount];
		this.yFitnessSum=new double[yCount];
		
		this.yLong=new long[yCount][this.rangeXCount];
		this.yValue=new double[yCount][this.rangeXCount];
		double temp=Math.pow(10,this.precision);
		for(int i=0;i<rangeXCount;i++)
		{
			//转化为整数的信息
			long changePricesion=(long)((rangeValue[i][1]-rangeValue[i][0])*Math.pow(10,this.precision));
			yLongLength[i]=getBigInterLength(changePricesion);
			//初始化 染色体组的信息
			for(int j=0;j<yCount;j++)
			{
				long zo=random.nextLong()%changePricesion;
				yLong[j][i]=Math.abs(random.nextLong()%changePricesion);
				yValue[j][i]=(yLong[j][i])/temp;
			
			}
		}
	}
	
	/**
	 * 获取二进制的长度
	 * @return
	 */
	public int getBigInterLength(long px)
	{
		int length=0;
		long temp=px;
		while(true)
		{
			if(temp==0)
			{
				break;
			}
			temp=temp>>2;
			length++;
		}
		return length;
	}
	/**
	 * #评价个体适应度
	 *解码
	 *带入需要寻优的函数中
	 *fx为函数并且为 取 fx的最大值
	 */
	public void evaluate(String fx)
	{
		//计算染色体适应度
		for(int i=0;i<yCount;i++)
		{
			String fx1=fx;
			for(int j=0;j<this.rangeXCount;j++)
			{
				fx1=fx1.replaceAll(this.x[j],Double.toString(this.yValue[i][j]));
				fx1=fx1.replace("pi", Double.toString(Math.PI));
			}
			this.yFitness[i]=StringToFunction.evaluateDecode(fx1);
		}
	}
	/**
	 * 返回方差是否有效
	 */
	public boolean isStdOk(int computeCount)
	{
		double sum=yFitness[this.yFitnessMaxIndex];
		this.newStd=sum;
		System.out.println(computeCount+"次：oldStd:"+oldStd+"\tnewStd:"+newStd);
		if(Math.abs(this.newStd-oldStd)<1E-4)
		{
			oldStd=newStd;
			return true;
		}
		else
		{
			oldStd=newStd;
			return false;
		}
	}
	
	/**
	 * 计算每个染色体被复制累计概率
	 */
	public void sumP()
	{
		double temp=0.0;
		double te=Double.MIN_VALUE;
		int index=-1;
		//因存在负数所以调整为标准化
		Double min=Double.MAX_VALUE;
		Double max=Double.MIN_VALUE;
		for(int i=0;i<yCount;i++)
		{
			if(yFitness[i]<min)
			{
				min=yFitness[i];
			}
			if(yFitness[i]>max)
			{
				max=yFitness[i];
			}
		}
		//获取适应度的最大index
		for(int i=0;i<yCount;i++)
		{
			temp+=(yFitness[i]-min)/(max-min);
			
			if(yFitness[i]>te)
			{
				te=yFitness[i];
				index=i;
			}
			
		}
		yFitnessMaxIndex=index;
		//计算比例
		for(int i=0;i<yCount;i++)
		{
			if(i==0)
			{
				yFitnessSum[i]=(yFitness[i]-min)/(max-min)/temp;
			}
			else
			if(i==yCount-1)
			{
				yFitnessSum[i]=1.0;
			}
			else
			{
				yFitnessSum[i]=yFitnessSum[i-1]+(yFitness[i]-min)/(max-min)/temp;
			}
		}
	}
	/**
	 * 轮盘选择
	 * 轮盘的作用是竞争机制
	 *其中轮盘的最大值一定会被选择
	 *@maxCopy copy最大适应度点的数量
	 */
	public void coronaSelect(int maxCopy)
	{
		//轮盘的作用是竞争机制
		Random random=new Random();
		long[][] temp=new long[yCount][this.rangeXCount];
		double[][] yValueTemp=new double[yCount][rangeXCount];
		for(int i=0;i<yCount;i++)
		{
			if(i<=maxCopy)
			{
				//设定最高的一定会被选上1次
				temp[i]=this.yLong[yFitnessMaxIndex].clone();
				yValueTemp[i]=this.yValue[yFitnessMaxIndex].clone();
				continue;
			}
			double te=random.nextDouble();
			for(int j=0;j<yCount;j++)
			{
				if(te<=yFitnessSum[j])
				{
					if(j==yFitnessMaxIndex)
					{
						i--;
						break;
					}
					temp[i]=this.yLong[j].clone();
					yValueTemp[i]=this.yValue[j].clone();
					break;
				}
				else
				{
					
				}
			}
		}
		yFitnessMaxIndex=0;
		yLong=temp;
		yValue=yValueTemp;
	}
	/**
	 * 执行交配
	 * p 为交配概率
	 *  count 执行次数 count次数最好少于yCount/2个
	 *  context 为交配方式 single为简单交配，complex为复杂交配
	 * 其中也考虑竞争机制
	 * 越低的交配概率越低，但最低也会是p/2
	 * 种群交配初始化交配概率#规定交配和突变种群中最大值不参与
	 * 一般交配概率为0.6-1
	 * @maxCopy 拷贝最大适应度的长度
	 */
	public void  mating(int maxCopy,double p,int count,String context)
	{
		Random random=new Random();
		for(int i=0;i<count;i++)
		{
			//判断是否交配
			if(p<random.nextDouble())
			{
				continue;
			}
			int int1=-1;
			int int2=-1;
			while(true)
			{
				double te=random.nextDouble();
				boolean flag=false;
				for(int j=0;j<yCount;j++)
				{
					if(te<this.yFitnessSum[j])
					{
						if(j>=maxCopy)
						{
							flag=true;
							int1=j;
						}
						else
						{
							
						}
						break;
					}
					else
					{
						continue;
					}
				}
				if(flag==true)
				{
					break;
				}
			}
			while(true)
			{
				double te=random.nextDouble();
				boolean flag=false;
				for(int j=0;j<yCount;j++)
				{
					if(te<this.yFitnessSum[j])
					{
						if(j>=maxCopy)
						{
							flag=true;
							int2=j;
						}
						else
						{
							
						}
						break;
					}
					else
					{
						continue;
					}
				}
				if(flag==true)
				{
					break;
				}
			}
			if(context=="single")
			{
				for(int m=0;m<this.rangeXCount;m++)
				{
					if(random.nextDouble()>0.8)
					{
						int changeCount=(int) Math.floor(random.nextDouble()*this.yLongLengthSum);
						long firstI=yLong[int1][m];
						long secondI=yLong[int2][m];
		//				this.yLong[int1]=yLong[int1].substring(0,ll)+yLong[int2].substring(ll);
		//				this.yLong[int2]=yLong[int2].substring(0,ll)+yLong[int1].substring(ll);
						long temp=(firstI>>(changeCount)<<(changeCount))|(secondI&test.getInt(changeCount));
						yLong[int2][m]=(secondI>>(changeCount)<<(changeCount))|(firstI&test.getInt(changeCount));
						yLong[int1][m]=temp;
					}
				}
			}
			else if(context=="complex")
			{
				for(int m=0;m<this.rangeXCount;m++)
				{
				//将内容给分配为几段
				for(int k=0;k<Math.abs(random.nextInt())%2;k++)
				{
					//标记位置
					int changeCount=(int) Math.floor(random.nextDouble()*this.yLongLengthSum);
					//每一段长度
					int len=Math.abs(random.nextInt()%3);
					int len1=changeCount+len>this.yLongLengthSum?this.yLongLength[m]:changeCount+len;
					int changeCount2=changeCount+len1;
					long firstI=yLong[int1][m];
					long secondI=yLong[int2][m];
				//	System.out.println("交配id:"+int1+"\t"+int2);
				//	System.out.println("交配前:"+Long.toBinaryString(yLong[int1][m])+"\t:\t"+Long.toBinaryString(yLong[int2][m]));
				//	System.out.println("交配前值:"+yLong[int1][m]+"\t"+yLong[int2][m]);
//					this.yLong[int1]=yLong[int1].substring(0,ll)+yLong[int2].substring(ll,len1)+yLong[int1].substring(len1);
//					this.yLong[int2]=yLong[int2].substring(0,ll)+yLong[int1].substring(ll,len1)+yLong[int2].substring(len1);
					long temp=firstI&(~(getInt(changeCount2-changeCount+1)<<(changeCount)))|(secondI&getInt(changeCount2-changeCount+1)<<(changeCount));
					yLong[int2][m]=secondI&(~(getInt(changeCount2-changeCount+1)<<(changeCount)))|(firstI&getInt(changeCount2-changeCount+1)<<(changeCount));
					yLong[int1][m]=temp;
					//System.out.println("交配后:"+Long.toBinaryString(yLong[int1][m])+"\t:\t"+Long.toBinaryString(yLong[int2][m]));
					//System.out.println("交配前后:"+yLong[int1][m]+"\t"+yLong[int2][m]);
				}
				}
			}
			
		}
	}
	
	public int getInt(int count)
	{
		return (1<<count)-1;
	}
	/**
	 * 执行突变
	 * 种群交配初始化交配概率#规定交配和突变种群中最大值不参与
	 * p 突变概率 一般突变概率都在0.2以下
	 * p2 为每一个染色体中的一个标记突变概率
	 * @maxCopy 拷贝最大适应度的数量
	 */
	public void mutation(int maxCopy,double p)
	{
		Random random=new Random();
		for(int i=0;i<yCount;i++)
		{
			if(random.nextDouble()>p)
			{
				continue;
			}
			if(i!=this.yFitnessMaxIndex && i<maxCopy)
			{
				//做轻微突变
				for(int j=0;j<this.rangeXCount;j++)
					for(int k=0;k<random.nextInt()%3;k++)
				yLong[i][j]=yLong[i][j]^(1<<(Math.abs(random.nextInt()%((long)(this.yLongLength[j]/2)))));
				continue;
			}
			for(int j=0;j<this.rangeXCount;j++)
			{
				for(int k=0;k<random.nextInt()%3;k++)
				{
				//	System.out.println("原始:"+Long.toBinaryString(yLong[i][j])+"\t"+yLong[i][j]);
					yLong[i][j]=yLong[i][j]^(1<<(Math.abs(random.nextInt()%((long)(this.yLongLength[j]/1.3)))));
				//	System.out.println("修改:"+Long.toBinaryString(yLong[i][j])+"\t"+yLong[i][j]);
				}
			}
		}
	}
	/**
	 * 计算真实值
	 */
	public void computeRealValue()
	{
		double temp=Math.pow(10,this.precision);
		for(int i=0;i<yLong.length;i++)
		{
			for(int j=0;j<yLong[i].length;j++)
			{
				this.yValue[i][j]=this.yLong[i][j]/temp;
			}
		}
	}
	
/**
 * 
 * @param x 为x1，x2，x3的标注
 * @param rangeValue 对应行为x1的最小到最大值
 * @param precision 需要的精度
 * @param yCount 需要的染色体数量
 * @param fx 需要执行的 max(fx) fx函数
 * @param px 交配概率
 * @param count 交配 长度
 * @param context 对应的是 single交配，还是 complex交配
 * @param py 突变概率
 * @param maxCopy 最大适应度拷贝长度
 */
	public void run(String[] x,double[][] rangeValue,int precision,int yCount,
			String fx,double px,int count,String context,double py,int maxCopy)
	{
		this.rangeXx(x, rangeValue, precision);
		this.random(yCount);
		while(true)
		{
			computeCount++;
			//System.out.println("统计将编码转换为10进制");
			computeRealValue();
			//System.out.println("计算次数:"+computeCount);
			//System.out.println("执行适应度");
			this.evaluate(fx);
			//System.out.println("执行是否收敛");
			boolean ll=this.isStdOk(computeCount);
			//System.out.println("isOk:"+ll);
			if(ll==true)
			{
				System.out.println("结束Ga，并收敛最大值为:"+this.yFitness[this.yFitnessMaxIndex]);
				for(int i=0;i<this.rangeXCount;i++)
				{
					System.out.println(this.yValue[this.yFitnessMaxIndex][i]+"\t"+this.yLong[this.yFitnessMaxIndex][i]);
				}
				break;
			}
			//System.out.println("适应度最大值为:"+this.yFitness[this.yFitnessMaxIndex]+"\tindex:"+this.yFitnessMaxIndex);
			//System.out.println("执行累计汇总");
			this.sumP();
			//System.out.println("执行轮盘选择");
			this.coronaSelect(maxCopy);
			//System.out.println("执行交配");
			this.mating(maxCopy,px, count, context);
			//System.out.println("执行突变");
			this.mutation(maxCopy,py);
		}
	}
	public static void main(String[] args) {
		GaFast ga=new GaFast();
		String[] x={"x1","x2"};
		//设定 x1 x2 的 有效区间
		double[][] rangeValue={{-3,12.1},{4.1,5.8}};
		int precision= 4;
		int yCount=50;
		//max(21.5+x1*sin(4*pi*x1)+x2*sin(20*pi*x2))
		String fx="21.5+x1*sin(4*pi*x1)+x2*sin(20*pi*x2)";
		double px=0.2;
		int count=3;
		String context="complex";
		double py=1.0;
		System.out.println("Ga");
		ga.run(x,rangeValue, precision, yCount, fx, px, count, context, py,5);
	}
}
