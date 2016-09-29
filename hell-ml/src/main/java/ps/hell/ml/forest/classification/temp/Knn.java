package ps.hell.ml.forest.classification.temp;

import java.util.ArrayList;

import ps.landerbuluse.struct.store.sort.AllSort;

/**
 * knn最近邻算法
 * 在k各最近样本点中 获取众数对应的分类
 * 为预测值
 * @author Administrator
 *
 */
public class Knn {

	/**
	 * 输出分类
	 */
	public String[] outputData=null;
	/**
	 * 输入变量
	 * 该过程或者最近临 可以切换成其他计算方法
	 */
	public double[][] inputData=null;
	
	/**
	 * 
	 * @param inputData 输入变量
	 * @param outputData 输出变量
	 */
	public Knn(double[][] inputData,String[] outputData)
	{
		this.inputData=new double[inputData.length][inputData[0].length];
		this.outputData=outputData.clone();
		for(int i=0;i<inputData.length;i++)
		{
			this.inputData[i]=inputData[i].clone();
		}
	}
	/**
	 * 
	 * @param in 需要预测的输入变量
	 * @param k 为k值
	 * @return 返回对应的分类
	 *用基本的欧式距离计算公式
	 */
	public String exec(double[] in,int k)
	{
		Node[] node=new Node[inputData.length];
		double value=0.0;
		for(int i=0;i<inputData.length;i++)
		{
			value=0.0;
			//计算距离
			//使用插入排序
			for(int j=0;j<in.length;j++)
			{
				value+=Math.pow(in[j]-inputData[i][j],2D);
			}
			node[i]=new Node(value,outputData[i]);
		}
		//快速排序获取前k个最大值
		AllSort.quickSort(node,k, node.length - 1,true);
		//统计最大String
		//改方法会慢
		ArrayList<String> name=new ArrayList<String>();
		ArrayList<Integer> val=new ArrayList<Integer>();
		for(int i=0;i<k;i++)
		{
			boolean flag=false;
			for(int j=0;j<name.size();j++)
			{
				if(node[i].getStringValue().equals(name.get(j)))
				{
					val.set(j,(val.get(j)+1));
					flag=true;
				}
			}
			if(!flag)
			{
				name.add(node[i].getStringValue());
				val.add(1);
			}
		}
		//获取最大的点
		int index=-1;
		Integer l=-1;
		Integer v=-1;
		for(int i=0;i<val.size();i++)
		{
			v=val.get(i);
			if(v>l)
			{
				l=v;
				index=i;
			}
		}
		return name.get(index);
	}
	
	public class Node implements Comparable<Node>
	{
		double distValue=0.0;
		String stringValue="";
		public Node(double distValue,String stringValue)
		{
			this.distValue=distValue;
			this.stringValue=stringValue;
		}
		@Override
		public int compareTo(Node obj)
		{
			if(this.distValue>obj.getDistValue())
			{
				return 1;
			}else if(this.distValue<obj.getDistValue())
			{
				return -1;
			}else
			{
				return 1;
			}
		}
		
		public double getDistValue() {
			return distValue;
		}
		public void setDistValue(double distValue) {
			this.distValue = distValue;
		}
		public String getStringValue() {
			return stringValue;
		}
		public void setStringValue(String stringValue) {
			this.stringValue = stringValue;
		}
		
	}
	
	public static void main(String[] args)
	{
		Knn knn=new Knn(null,null);
		System.out.println("预测分类为:"+knn.exec(null, 4));
	}
	
}
