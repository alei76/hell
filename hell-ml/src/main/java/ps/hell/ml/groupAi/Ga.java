package ps.hell.ml.groupAi;

import java.math.BigInteger;
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
public class Ga {
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
	 * 染色体组string编码
	 */
	public String[] yString;
	
	/**
	 * 存储染色体组对应的多个x的解码10进制数
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
	public int[] yStringLength;
	public int yStringLengthSum=0;
	public Ga()
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
		this.yString=new String[yCount];
		yStringLength=new int[rangeValue.length];
		for(int i=0;i<yCount;i++)
		{
			yString[i]="";
		}
		this.yFitness=new double[yCount];
		this.yFitnessSum=new double[yCount];
		this.rangeXCount=rangeValue.length;
		this.yValue=new double[yCount][this.rangeXCount];
		for(int i=0;i<rangeValue.length;i++)
		{
			int temp=(int)((rangeValue[i][1]-rangeValue[i][0])*Math.pow(10,this.precision));
			BigInteger src = new BigInteger(Integer.toString(temp));//转换为BigInteger类型
			yStringLength[i]=src.toString(2).length();
			for(int j=0;j<yCount;j++)
			{
				String te=src.toString(2);
				StringBuilder strb = new StringBuilder(te);
				//转化为2进制
				for(int m=0;m<yStringLength[i];m++)
				{
					if(random.nextDouble()>0.5)
					{
						//修改
						//te.substring(len,len+yStringLength[i]-1);
						if(te.charAt(m)=='0')
						{
							strb.replace(m,m+1, "1");
						}
						else
						{
							strb.replace(m,m+1, "0");
						}
					}
				}
				yString[j]=yString[j]+strb.toString();
				System.out.println("yStinrg["+j+"]"+"\t"+yString[j]);
			}
		}
		for(int i=0;i<this.rangeXCount;i++)
		{
			yStringLengthSum+=yStringLength[i];
		}
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
				//System.out.println(this.yValue[i][j]);
				fx1=fx1.replaceAll(this.x[j],Double.toString(this.yValue[i][j]));
				fx1=fx1.replace("pi", Double.toString(Math.PI));
			}
			//System.out.println(fx1);
			//System.exit(1);
			this.yFitness[i]=StringToFunction.evaluateDecode(fx1);
		}
	}
	/**
	 * 返回方差是否有效
	 */
	public boolean isStdOk()
	{
		double sum=0.0;
		for(int i=0;i<yCount;i++)
		{
			sum+=yFitness[i];
		}
		sum/=yCount;
		double std=0.0;
		for(int i=0;i<yCount;i++)
		{
			//System.out.println(yFitness[i]+"\t"+sum+"\t"+(yCount-1));
			std+=Math.pow(yFitness[i]-sum,2.0)/(yCount-1);
		}
		this.newStd=std;
		System.out.println("oldStd:"+oldStd+"\tnewStd:"+newStd);
		if(Math.abs(this.newStd-oldStd)<1E-10)
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
	 * 将2进制转化为10进制
	 *str为2进制字符串
	 *index为对应所属的行
	 */
	public void changeTo10()
	{
		for(int i=0;i<yCount;i++)
		{
			
			for(int j=0;j<this.rangeXCount;j++)
			{
				if(j==0)
				{
					
					BigInteger src1 = new BigInteger(yString[i].substring(0,this.yStringLength[j]),2);
					this.yValue[i][j]=this.rangeValue[j][1]+src1.intValue()/Math.pow(10,this.precision);
				}
				else
				{
					//System.out.print(rangeXCount+"\t"+this.yStringLength[j-1]+"\t"+this.yStringLength[j]);
					BigInteger src1 = new BigInteger(yString[i].substring(this.yStringLength[j-1],this.yStringLength[j-1]+this.yStringLength[j]),2);
					this.yValue[i][j]=this.rangeValue[j][1]+src1.intValue()/Math.pow(10,this.precision);
				}
			}
			//System.out.println();
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
		System.out.println("max:"+max+"\tmin:"+min);
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
				yFitnessSum[i]=yFitness[i]/temp;
			}
			else
			if(i==yCount-1)
			{
				yFitnessSum[i]=1.0;
			}
			else
			{
				yFitnessSum[i]=yFitnessSum[i-1]+yFitness[i]/temp;
			}
		}
	}
	/**
	 * 轮盘选择
	 * 轮盘的作用是竞争机制
	 *其中轮盘的最大值一定会被选择
	 */
	public void coronaSelect()
	{
		//轮盘的作用是竞争机制
		Random random=new Random();
		String[] temp=new String[yCount];
		for(int i=0;i<yCount;i++)
		{
			if(i==0)
			{
				//设定最高的一定会被选上1次
				temp[i]=this.yString[yFitnessMaxIndex];
				continue;
			}
			double te=random.nextDouble();
			for(int j=0;j<yCount;j++)
			{
				if(te<=yFitnessSum[j])
				{
					temp[i]=this.yString[j];
					continue;
				}
				else
				{
					
				}
			}
		}
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
	 */
	public void  mating(double p,int count,String context)
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
						if(j!=this.yFitnessMaxIndex)
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
						if(j!=this.yFitnessMaxIndex)
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
				int ll=(int) Math.floor(random.nextDouble()*this.yStringLengthSum);
				this.yString[int1]=yString[int1].substring(0,ll)+yString[int2].substring(ll);
				this.yString[int2]=yString[int2].substring(0,ll)+yString[int1].substring(ll);	
			}
			else if(context=="complex")
			{
				//将内容给分配为几段
				for(int m=0;m<Math.abs(random.nextInt())%6;m++)
				{
					//标记位置
					int ll=(int) Math.floor(random.nextDouble()*this.yStringLengthSum);
					//每一段长度
					int len=Math.abs(random.nextInt()%3);
					int len1=ll+len>this.yStringLengthSum?yStringLengthSum:ll+len;
					this.yString[int1]=yString[int1].substring(0,ll)+yString[int2].substring(ll,len1)+yString[int1].substring(len1);
					this.yString[int2]=yString[int2].substring(0,ll)+yString[int1].substring(ll,len1)+yString[int2].substring(len1);
				}
			}
			
		}
	}
	/**
	 * 执行突变
	 * 种群交配初始化交配概率#规定交配和突变种群中最大值不参与
	 * p 突变概率 一般突变概率都在0.2以下
	 * p2 为每一个染色体中的一个标记突变概率
	 */
	public void mutation(double p)
	{
		Random random=new Random();
		for(int i=0;i<yCount;i++)
		{
			if(random.nextDouble()>p)
			{
				continue;
			}
			StringBuilder strb = new StringBuilder(yString[i]);
			for(int j=0;j<this.yStringLengthSum;j++)
			{
				if(random.nextDouble()<p/yStringLengthSum*1.1)
				{
					if(strb.charAt(j)=='0')
					{
						strb.replace(j,j+1,"1");
					}
					else
					{
						strb.replace(j,j+1,"0");
					}
				}
			}
			yString[i]=strb.toString();
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
 */
	public void run(String[] x,double[][] rangeValue,int precision,int yCount,
			String fx,double px,int count,String context,double py)
	{
		this.rangeXx(x, rangeValue, precision);
		this.random(yCount);
		while(true)
		{
			computeCount++;
			System.out.println("统计将编码转换为10进制");
			changeTo10();
			System.out.println("计算次数:"+computeCount);
			System.out.println("执行适应度");
			this.evaluate(fx);
			System.out.println("执行是否收敛");
			boolean ll=this.isStdOk();
			System.out.println("isOk:"+ll);
			if(ll==true)
			{
				System.out.println("结束Ga，并收敛最大值为:"+this.yFitness[this.yFitnessMaxIndex]);
				break;
			}
			System.out.println("适应度最大值为:"+this.yFitness[this.yFitnessMaxIndex]);
			System.out.println("执行累计汇总");
			this.sumP();
			System.out.println("执行轮盘选择");
			this.coronaSelect();
			System.out.println("执行交配");
			this.mating(px, count, context);
			System.out.println("执行突变");
			this.mutation(py);
		}
	}
	public static void main(String[] args) {
		Ga ga=new Ga();
		String[] x={"x1","x2"};
		//设定 x1 x2 的 有效区间
		double[][] rangeValue={{-3,12.1},{4.1,5.8}};
		int precision= 4;
		int yCount=50;
		//max(21.5+x1*sin(4*pi*x1)+x2*sin(20*pi*x2))
		String fx="21.5+x1*sin(4*pi*x1)+x2*sin(20*pi*x2)";
		double px=0.7;
		int count=3;
		String context="complex";
		double py=0.5;
		System.out.println("Ga");
		ga.run(x,rangeValue, precision, yCount, fx, px, count, context, py);
	}
}
