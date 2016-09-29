package ps.hell.ml.statistics.bayes;

import java.util.LinkedList;

/**
 * bayes接口
 * @author Administrator
 *#朴素贝叶斯作为最简单的贝叶斯方法被用在各类领域，以其速度和效果都能满足要求和广泛应用尤其是大数据类
 *#以贝叶斯公式为主要媒介
 *只以分类为基础
 *如果为数值的输入变量 需要转化为分类变量
 *和navieBayes一样 
 */
public class NavieBayes2 {

	/**
	 * 存储输入数据
	 */
	public LinkedList<String[]> data=new LinkedList<String[]>();
	
	/**
	 * 为输出变量
	 */
	public String[] output;
	/**
	 * 输出对应的统计数
	 */
	public ReadTree outputValueInt=new ReadTree();
	
	/**
	 * 输入对应的统计数
	 */
	public LinkedList<ReadInput> inputValueInt=new LinkedList<ReadInput>();
	
	public int fileLength=0;
	public int dimensionality=0;
	
	
	public LinkedList<double[]> outputTestPValue=new LinkedList<double[]>();
	/**
	 * 朴素贝叶斯基础算法
	 * @param data 输入变量
	 * @param output 输出变量
	 */
	public NavieBayes2(LinkedList<String[]> data,String[] output)
	{
		for(int i=0;i<data.size();i++)
		{
			this.data.add(data.get(i).clone());
		}
		this.output=output.clone();
		fileLength=data.size();
		try
		{
			dimensionality=data.get(0).length;
		}
		catch(Exception e)
		{
			System.out.println("输入数据错误");
			return;
		}
		
	}
	/**
	 * bayes 训练
	 */
	public void train()
	{
		//提取output分类对应的概率 p(c)
		for(int i=0;i<output.length;i++)
		{
			outputValueInt.add(output[i],1);
		}
		for(int j=0;j<dimensionality;j++)
		{
			ReadInput t1=new ReadInput();
			for(int i=0;i<fileLength;i++)
			{
				t1.add(data.get(i)[j],output[j],1);
			}
			t1.print();
			inputValueInt.add(t1);
		}
		outputValueInt.print();
	}
	/**
	 * 测试训练
	 * @return
	 */
	public String[] test(LinkedList<String[]> inputTest)
	{
		
		String[] te=new String[inputTest.size()];
		if(inputTest.get(0).length!=this.dimensionality)
		{
			System.out.println("输入数据与实际数据维度不符");
			return new String[0];
		}
	
		this.inputValueInt.size();
		
		for(int i=0;i<inputTest.size();i++)
		{
			double[] tempPx=new double[this.outputValueInt.size];
			for(int m=0;m<outputValueInt.size;m++)
			{
				tempPx[m]=1.0*outputValueInt.valueList.get(m)/this.fileLength;
				for(int j=0;j<this.dimensionality;j++)
				{
					tempPx[m]*=inputValueInt.get(j).search(inputTest.get(i)[j], outputValueInt.nameList.get(m));		
				}
			}
			//比较值取最大的值
			double d=Double.MIN_VALUE;
			int count=-1;
			outputTestPValue.add(tempPx.clone());
			for(int j=0;j<tempPx.length;j++)
			{
				if(tempPx[j]>d)
				{
					d=tempPx[j];
					count=j;
				}
			}
			te[i]=outputValueInt.nameList.get(count);
		}
		
		return te;
	}
	
	
	
	
	public static void main(String[] args) {
		
	}
}
