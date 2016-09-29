package ps.hell.ml.statistics.bayes;

import java.util.ArrayList;

/**
 * 朴素bayes
 * 使用的是 后验概率
 * @author Administrator
 *
 */
public class NavieBayes {

	/**
	 * 输入分类
	 */
	public String[][] inputData=null;
	
	/**
	 * 输出的概率值
	 */
	ArrayList<ArrayList<CP>> inputDataP=new ArrayList<ArrayList<CP>>();
	/**
	 * 输出分类
	 */
	public String[] outputData=null;
	
	/**
	 * 输出的概率值
	 */
	ArrayList<P> outputDataP=new ArrayList<P>();
	/**
	 * @param inputData 输入变量
	 * @param outputData 输出变量
	 */
	public NavieBayes(String[][] inputData,String[] outputData)
	{
		this.inputData=new String[inputData.length][];
		this.outputData=outputData.clone();
		for(int i=0;i<inputData.length;i++)
		{
			this.inputData[i]=inputData[i].clone();
		}
		//将内容转化为 int类型
		//该方法不考虑转化为int类型，如果以后需要则需要嵌入相应模块
		//计算分类概率
		computeOutputP();
		//计算条件概率
		computeConditionP();
		
	}
	/**
	 * 获取 分类概率
	 */
	public void computeOutputP()
	{

		for(int i=0;i<outputData.length;i++)
		{
			P p=new P(outputData[i],1.0);
			//效率中等
			for(int j=0;j<outputDataP.size();j++)
			{
				if(outputDataP.get(j).equals(p))
				{
					outputDataP.get(j).add(p);
				}else
				{
					outputDataP.add(p);
				}
			}
		}
		for(int i=0;i<outputDataP.size();i++)
		{
			outputDataP.get(i).value/=outputData.length;
		}
	}
	
	/**
	 * 获取对应条件概率 
	 */
	public void computeConditionP()
	{
		for(int l=0;l<inputData[0].length;l++)
		{
			ArrayList<CP> inputDataPTemp=new ArrayList<CP>();
			for(int i=0;i<inputData.length;i++)
			{
				CP p=new CP(inputData[i][l],outputData[i],1.0);
				//效率中等
				for(int j=0;j<inputDataP.size();j++)
				{
					if(inputDataPTemp.get(j).equals(p))
					{
						inputDataPTemp.get(j).add(p);
					}else
					{
						inputDataPTemp.add(p);
					}
				}
			}
			for(int i=0;i<inputDataPTemp.size();i++)
			{
				inputDataPTemp.get(i).value/=outputData.length;
				//计算条件概率
				for(int k=0;k<outputDataP.size();k++)
				{
					if(inputDataPTemp.get(i).compute(outputDataP.get(k)))
					{
						continue;
					}else
					{
						
					}
				}
			}
			inputDataP.add(inputDataPTemp);
		}
	}
	
	/**
	 * 分类概率类
	 * @author Administrator
	 *
	 */
	public class P  implements Comparable<P> 
	{
		public String name;
		public double value=0.0;
		public P(String name,double value)
		{
			this.name=name;
			this.value=value;
		}
		@Override
		public int compareTo(P cp)
		{
			if(this.name.compareTo(cp.name)==0)
			{
				return 0;
			}else if(this.name.compareTo(cp.name)>0)
			{
				return 1;
			}else {
				return -1;
			}
		}
		public boolean equals(P cp)
		{
			if(this.name.equals(cp.name))
			{
				return true;
			}
			return false;
		}
		public void add(P cp)
		{
			this.value+=cp.value;
		}
	}
	/**
	 * 条件概率
	 * @author Administrator
	 *
	 */
	public class CP implements Comparable<CP>
	{
		public String name;
		/**
		 * 分类名
		 */
		public String nameO;
		/**
		 * 条件概率
		 */
		public double value=0.0;
		public CP(String name,String nameO,double value)
		{
			this.name=name;
			this.nameO=nameO;
			this.value=value;
		}
		//计算条件概率
		public boolean compute(P p)
		{
			if(this.nameO.equals(p.name))
			{
				this.value/=p.value;
			}
			return false;
		}
		@Override
		public int compareTo(CP cp)
		{
			if(this.name.compareTo(cp.name)==0)
			{
				if(this.nameO.compareTo(cp.nameO)==0)
				{
					return 0;
				}else if(this.nameO.compareTo(cp.nameO)>0)
				{
					return 1;
				}else {
					return -1;
				}
			}else if(this.name.compareTo(cp.name)>0)
			{
				return 1;
			}else {
				return -1;
			}
		}
		public boolean equals(CP cp)
		{
			if(this.name.equals(cp.name) && this.nameO.equals(cp.nameO))
			{
				return true;
			}
			return false;
		}
		public void add(CP cp)
		{
			this.value+=cp.value;
		}
	}
	
	/**
	 * 预测函数
	 * 输入变量 获取 对应的概率值及 分类名
	 * @param in
	 */
	public ArrayList<P> exec(String[] in)
	{
		return getBayes(in);
	}
	/**
	 * 获取bayes值
	 * @param in
	 * @return 对应的从大到小的 排序  概率值 以及name为对应的分类
	 */
	public ArrayList<P> getBayes(String[] in)
	{
		ArrayList<P> result=new ArrayList<P>();
		for(P p:outputDataP)
		{
			double value=1.0;
			int i=-1;
			for(ArrayList<CP> cp:inputDataP)
			{
				i++;
				int j=-1;
				for(CP c:cp)
				{
					j++;
					if(c.name.equals(in[i]) && c.nameO.equals(p.name))
					{
						value*=c.value;
					}
				}
			}
			result.add(new P(p.name,value*p.value));
		}
		//排序降序
		//冒泡排序
		for(int i=0;i<result.size()-1;i++)
		{
			for(int j=0;j<result.size()-i-1;j++)
			{
				if(result.get(j).value<result.get(j+1).value)
				{
					P temp=result.get(j);
					result.set(j, result.get(j+1));
					result.set(j+1,temp);
				}
			}
		}
		return result;
	}
	
	public static void main(String[] args) {
		NavieBayes nb=new NavieBayes(null,null);
		ArrayList<P> re=nb.exec(null);
		System.out.println("从大到小排序");
		for(P p:re)
		{
			System.out.println("预测:"+p.name+"\t概率:"+p.value);
		}
		
	}
}
