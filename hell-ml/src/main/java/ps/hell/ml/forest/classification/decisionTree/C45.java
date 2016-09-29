package ps.hell.ml.forest.classification.decisionTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import ps.landerbuluse.ml.math.MathBase;
import ps.landerbuluse.ml.math.MathBase.MathTable;
import ps.landerbuluse.struct.store.struct.TableTree;
import ps.landerbuluse.struct.store.struct.TableTree.Node;


/**
 * C45为id3的进化版本
 * 主要使用的是 gini指数 以及
 * 最小误差剪枝方法
 * (代价复杂性剪枝，悲观误差剪枝)
 * @author Administrator
 *
 */
public class C45 {
	/**
	 * 0.95对应的卡方值
	 */
	public double[] CHI=new double[]
			{0.004,0.103,0.352,0.711,1.145,1.635,2.167,2.733,3.325,3.94,4.575,5.226,5.892,6.571,7.261,7.962};
	/**
	 * 输入原数据
	 */
	public String[][] inputData=null;
	/**
	 * 将信息转化为整数
	 */
	public int[][] inputDataInt=null;
	/**
	 * 对应string 对应的int
	 */
	public ArrayList<HashMap<String,Integer>> inputDataStringToInt=new ArrayList<HashMap<String,Integer>>();
	/**
	 * 对应的int地string
	 */
	public ArrayList<HashMap<Integer,String>> inputDataIntToString=new ArrayList<HashMap<Integer,String>>();
	
	/**
	 * 输出原数据
	 */
	public String[] outputData=null;
	/**
	 * 对应那个的int
	 */
	public int[] outputDataInt=null;
	
	/**
	 * 对应string 对应的int
	 */
	public HashMap<String,Integer> outDataStringToInt=new HashMap<String,Integer>();
	/**
	 * 对应的int地string
	 */
	public HashMap<Integer,String> outDataIntToString=new HashMap<Integer,String>();
	
	
	/**
	 * 输入数据+输出的输入string对应的int形式
	 */
	public ArrayList<HashMap<String,Integer>> dataMap=new ArrayList<HashMap<String,Integer>>();
	public TableTree fTree=new TableTree();
	
	public C45(String[][] inputData,String[] outputData)
	{
		this.inputData=new String[inputData.length][];
		for(int i=0;i<inputData.length;i++)
		{
			this.inputData[i]=inputData[i].clone();
		}
		this.outputData=outputData.clone();
		this.inputDataInt=new int[inputData.length][inputData[0].length];
		this.outputDataInt=new int[outputData.length];
		changeToInt();
	}
	/**
	 * 将信息转化为int格式
	 */
	public void changeToInt()
	{
		for(int j=0;j<inputData[0].length;j++)
		{
			int count=0;
			HashMap<String,Integer> map=new HashMap<String,Integer>();
			HashMap<Integer,String> map2=new HashMap<Integer,String>();
			for(int i=0;i<inputData.length;i++)
			{
				if(map.containsKey(inputData[i][j]))
				{
					inputDataInt[i][j]=map.get(inputData[i][j]);
				}else{
					map.put(inputData[i][j],count);
					map2.put(count,inputData[i][j]);
					count++;
				}
			}
			this.inputDataStringToInt.add(map);
			this.inputDataIntToString.add(map2);
		}
		int count=0;
		for(int i=0;i<outputData.length;i++)
		{
			if(outDataStringToInt.containsKey(outputData[i]))
			{
				outputDataInt[i]=outDataStringToInt.get(outputData[i]);
			}else{
				this.outDataStringToInt.put(outputData[i],count);
				this.outDataIntToString.put(count,outputData[i]);
				count++;
			}
		}
		//初始化输出数据
		changeStringToInt(inputData, inputDataInt, outputData, outputDataInt, dataMap);
	}
	
	
	/**
	 * c45执行程序
	 */
	public void exec()
	{

		//判断维度选择
		execSonLeaf(this.inputDataInt,this.inputData,this.outputDataInt,this.outputData,1,1,fTree.root,dataMap);
	}
	/**
	 * c45向下搜索方法
	 * @param input
	 * @param output
	 * @param deep 深度
	 * @param node null为根节点无 否则 向下添加子节点
	 * @param dataMap 为动态调整的结构 而跟节点上的dataMap不变 
	 */
	public void execSonLeaf(int[][] inputDataToInt,String[][] inputDataString,int[] outputDataToInt,String[] outputDataString,int deep,
			double errorFather,Node node,ArrayList<HashMap<String,Integer>> dataMap)
	{
		System.out.println("deep:"+deep);
		//获取相关概率 
		ArrayList<double[]> inputP=new ArrayList<double[]>();
		int outputlength=dataMap.get(dataMap.size()-1).size();
		double[] outputP=new double[outputlength];
		ArrayList<double[]> condtionP=new ArrayList<double[]>();
		//获取最小gini的位置
		int minIndex=getMinGiniIndex(inputDataToInt,outputDataToInt);

		if(minIndex==-1)
		{
			System.out.println("无法向下延伸:"+"深度:"+deep);
			return;
		}else 
		{
			System.out.println("已到叶子节点:"+"深度:"+deep);
		}
		int[] maxIndexValue=MathBase.getCol(inputDataToInt,minIndex);
		//从 inputDataToInt 中剔除 maxIndex列数据
		int[][] inputDataTempToInt=MathBase.moveCol(inputDataToInt, minIndex);
		System.out.println("inputDataToInt:"+inputDataToInt[0].length);
		System.out.println("inputDataTempToInt:"+inputDataTempToInt[0].length);
		//获取以maxIndex列分类的 子二维数组
		ArrayList<int[]> outputTemp=new ArrayList<int[]>();//存储分组后的outputDataToInt
		HashMap<Integer,Integer> map=new HashMap<Integer,Integer>();
		//分组后的输入String[][]
		ArrayList<String[][]> inputDataStringTemp=new ArrayList<String[][]>();
		//分组后的输出String[][]
		ArrayList<String[]> outputDataStringTemp=new ArrayList<String[]>();
		//需要将inputDataString剔除对应的列
		String[][] inputDataString2=MathBase.moveCol(inputDataString,minIndex);
		ArrayList<int[][]> groupInputData=MathBase.getGroupSon(inputDataStringTemp, 
				inputDataTempToInt, inputDataString2, maxIndexValue, outputDataToInt, outputDataString, outputDataStringTemp, outputTemp, map);
		//ArrayList<int[][]> groupInputData=MathMethod.getGroupSon(inputDataToInt,maxIndexValue,outputDataToInt,outputTemp,map);
		//对每个子集作向下查找
		int zq=0;
		//移除调掉对应的信息
//		dataMap.remove(maxIndex);
		deep++;
		for(int[][] sonInputDataToInt:groupInputData)
		{
			//重新计算 dataMap
			ArrayList<HashMap<String,Integer>> dataMapTemp=new ArrayList<HashMap<String,Integer>>();
			changeStringToInt(inputDataStringTemp.get(zq),sonInputDataToInt,outputDataStringTemp.get(zq),outputTemp.get(zq),dataMapTemp);
			//重新计算误差
			int index=-1;
			//添加到fTree中
			for(Entry<Integer, Integer> ll: map.entrySet())
			{
				if(ll.getValue()==zq)
				{
					index=ll.getValue();
				}
			}
			//不写其他方法
			 //并设置概率 及数量
			 int ln=MathBase.getMode(outputTemp.get(zq));
			 String lnString=Integer.toString(ln);
			 MathTable  table2=MathBase.getTable(outputTemp.get(zq), null);
			 
			 for(int i=0;i<table2.nameRow.length;i++)
			 {
				 if(table2.nameRow[i].equals(lnString))
				 {
					 ln=table2.num[i][0];
				 }
			 } 
				zq++;
				//继续调用子节点

			Leaf leaf=new Leaf(dataMapTemp.get(minIndex).get(index),sonInputDataToInt.length*1.0/inputDataToInt.length,
					sonInputDataToInt.length,1-(ln*1.0)/table2.num.length,dataMapTemp.get(dataMapTemp.size()-1).get(ln));
			Node sonNode=fTree.add(node,leaf,deep);
			System.out.println("sonInputDataToInt:"+sonInputDataToInt[0].length);
			execSonLeaf(sonInputDataToInt,inputDataStringTemp.get(zq-1),
					outputTemp.get(zq-1),outputDataStringTemp.get(zq-1),deep,0,sonNode,dataMapTemp);
		}
		
	}
	/**
	 * 将 字符串类型的统一转化为int类型
	 */
	public void changeStringToInt(String[][] inputDataA,int[][] inputDataToIntA,
			String[] outputDataA,int[] outputDataToIntA,
			ArrayList<HashMap<String,Integer>> dataMap)
	{
		//输入变量的类型统计
		for(int i=0;i<inputDataA[0].length;i++)
		{
			HashMap<String,Integer> temp=new HashMap<String,Integer>();
			int index=-1;
			for(int j=0;j<inputDataA.length;j++)
			{
				if(temp.containsKey(inputDataA[j][i]))
				{
					//输入变量类型修改
					inputDataToIntA[j][i]=temp.get(inputDataA[j][i]);
				}else{
					index++;
					temp.put(inputDataA[j][i],index);
					inputDataToIntA[j][i]=index;
				}
			}
			dataMap.add(temp);
		}
		//输出变量修改
		HashMap<String,Integer> temp=new HashMap<String,Integer>();
		int index=-1;
		for(int j=0;j<outputDataA.length;j++)
		{
			if(temp.containsKey(outputDataA[j]))
			{
				//输入变量类型修改
				outputDataToIntA[j]=temp.get(outputDataA[j]);
			}else{
				index++;
				temp.put(outputDataA[j],index);
				outputDataToIntA[j]=index;
			}
		}
		dataMap.add(temp);
	}
	/**
	 * 将 字符串类型的统一转化为int类型 group组方法
	 * 将对应的int[][] 修改为对应的值
	 */
	public void changeStringToIntT(String[][] inputDataA,int[][] inputDataToIntA,
			String[] outputDataA,int[] outputDataToIntA,
			ArrayList<HashMap<String,Integer>> dataMap)
	{	
		//输入变量的类型统计
		for(int i=0;i<inputDataToIntA[0].length;i++)
		{
			HashMap<String,Integer> temp=new HashMap<String,Integer>();
			int index=-1;
			for(int j=0;j<inputDataA.length;j++)
			{
				if(temp.containsKey(inputDataA[j][i]))
				{
					//输入变量类型修改
					inputDataToIntA[j][i]=temp.get(inputDataA[j][i]);
				}else{
					index++;
					temp.put(inputDataA[j][i],index);
					inputDataToIntA[j][i]=index;
				}
			}
			dataMap.add(temp);
		}
		//输出变量修改
		HashMap<String,Integer> temp=new HashMap<String,Integer>();
		int index=-1;
		for(int j=0;j<outputDataA.length;j++)
		{
			if(temp.containsKey(outputDataA[j]))
			{
				//输入变量类型修改
				outputDataToIntA[j]=temp.get(outputDataA[j]);
			}else{
				index++;
				temp.put(outputDataA[j],index);
				outputDataToIntA[j]=index;
			}
		}
		dataMap.add(temp);
	}
	
	
	/**
	 * 从输入和输出中获取 最小gini系数的index列
	 * @param in
	 * @param out
	 * @param inCount //改列有多少中类
	 * @return
	 */
	public int getMinGiniIndex(int[][] in,int[] out)
	{
		ArrayList<LinkedList<Integer>> outputGroup=getOutputGroup(out);
		double min=Double.MAX_VALUE;
		int index=-1;
		//类对应的最小gini的输入列的分类数
		int inCount=0;
		double val=0.0;
		//对每一列获取gini系数并获取其中最小的点
		for(int i=0;i<in[0].length;i++)
		{
			int cou=0;
			val=getGiniValue(in,out,outputGroup,i,cou);
			if(min>val)
			{
				min=val;
				index=i;
				inCount=cou;
			}
		}
		//判断是否终结
		boolean flag =isEnd(in,out,outputGroup,index,inCount);
		if(flag)
		{
			//可以分裂
			return index;
		}else
		{
			//终止分裂
			return -1;
		}
	}
	public ArrayList<LinkedList<Integer>> getOutputGroup(int[] out)
	{
		ArrayList<LinkedList<Integer>> result=new ArrayList<LinkedList<Integer>>();
		HashMap<Integer,Integer> value=new HashMap<Integer,Integer>();
		int count=0;
		LinkedList<Integer> re=new LinkedList<Integer>();
		for(int i=0;i<out.length;i++)
		{
			if(value.containsKey(out[i]))
			{
				result.get(value.get(out[i])).add(i);
			}else
			{
				value.put(out[i],count);
				if(count==0)
				{
				}else{
					re=new LinkedList<Integer>();
				}
				re.add(i);
				result.add(re);
				count++;
			}
		}
		return result;
	}
	/**
	 * 获取制定列的gini值
	 * @param in
	 * @param out
	 * @param ok 为根据out分隔出来 值相同的index组合
	 * @param index
	 * @param inCount 为in对应的index中有多少个分类
	 * @return
	 */
	public double getGiniValue(int[][] in,int[] out,ArrayList<LinkedList<Integer>> ok,int index,int inCount)
	{
		double result=0.0;
		for(int i=0;i<ok.size();i++)
		{
			//计算没一组的分组情况
			int[] count=new int[ok.get(i).size()];
			int co=0;
			HashMap<Integer,Integer> map=new HashMap<Integer,Integer>(); 
			for(Integer indexGroup:ok.get(i))
			{
				if(map.containsKey(in[indexGroup][index]))
				{
					count[map.get(in[indexGroup][index])]++;
				}else
				{
					map.put(in[indexGroup][index],co);
					count[co]++;
					co++;
				}
			}
			inCount=map.size();

			//计算值
			double gini=0.0;
			for(int j=0;j<count.length;j++)
			{
				if(count[j]==0)
				{
					break;
				}else
				{
					gini+=Math.pow(count[j]*1D, 2D);
				}
			}
			//gini=1-[(4/5)^2+(1/5)^2]
			gini=1-gini;
			//gini_gain=sum(p(a)*gini(a))
			result+=ok.get(i).size()*1D/out.length*gini;
		}
		return result;
	}
	/**
	 *  判断是否终止
	 * 使用卡方检验
	 * @param in
	 * @param out
	 * @param outGroup 一output为不同分类划分
	 * @param minIndex 最小giniindex
	 * @param inCount 输入的minIndex中有多少个分类
	 * @return
	 */
	public boolean isEnd(int[][] in,int[] out,ArrayList<LinkedList<Integer>> outGroup,int minIndex,int inCount)
	{
		//判断获取的最小gini系数的列是否达到终止条件
		if(outGroup.size()==0 ||inCount==0)
		{
			return false;
		}
		double[][] result=new double[outGroup.size()][inCount];
		HashMap<Integer,Integer> map=new HashMap<Integer,Integer>();
		int coun=0;
		for(int i=0;i<outGroup.size();i++)
		{
			for(Integer group:outGroup.get(i))
			{
				if(map.containsKey(in[group][minIndex]))
				{
					result[i][map.get(in[group][minIndex])]++;
				}else
				{
					map.put(in[group][minIndex],coun);
					result[i][coun]++;
					coun++;
				}
			}
		}
		double x2fang=cal_chi(result);
		//列-行数对应的自由度上的卡方值
		if(x2fang<CHI[result[0].length-result.length])
		{
			//则为独立 不需要分裂了
			return false;
		}
		return true;
	}
	/**
	 * 咖方值计算
	 * @param arr
	 * @param row
	 * @param col
	 * @return
	 */
	public double cal_chi(double[][] arr){
	    double[] rowsum=new double[arr.length];
	    double[] colsum=new double[arr[0].length];
	    double totalsum=0.0;
	    for(int i=0;i<arr.length;++i){
	        for(int j=0;j<arr[0].length;++j){
	            totalsum+=arr[i][j];
	            rowsum[i]+=arr[i][j];
	            colsum[j]+=arr[i][j];
	        }
	    }
	    double rect=0.0;
	    for(int i=0;i<arr.length;++i){
	        for(int j=0;j<arr[0].length;++j){
	            double excep=1.0*rowsum[i]*colsum[j]/totalsum;
	            if(excep!=0)
	                rect+=Math.pow(arr[i][j]-excep,2.0)/excep;
	        }
	    }
	    return rect;
	}
	/**
	 * 节点的实体对象
	 * @author Administrator
	 */
	public class Leaf<T extends Comparable>
	{
		//类名字
		public T name=null;
		//对应概率
		public double p=0.0;
		//对应数量
		public int num=0;
		//对应错误率
		public double errorP=0.0;
		//对应的y
		public T yName=null;
		public Leaf(T name,double p,int num,double errorP,T yName)
		{
			this.name=name;
			this.p=p;
			this.num=num;
			this.errorP=errorP;
			this.yName=yName;
		}
		//不用
		public int compareTo(Leaf leaf)
		{
			if(this.name.compareTo(leaf.name)==0)
			{
				return 0;
			}else if(this.name.compareTo(leaf.name)>0){
				return 1;
			}
			return -1;
		}
	}
		
		public static void main(String[] args) {
			String[] name=new String[]
					{
				"名称","体温","表面覆盖","胎生","产蛋","能飞","水生","有腿","冬眠","类标记"
					};
			String[][] inputData=new String[][]
					{{"人","恒温","毛发","是","否","否","否","是","否"},
					{"巨蟒","冷血","鳞片","否","是","否","否","否","是"},
					{"鲑鱼","冷血","鳞片","否","是","否","是","否","否"},
					{"鲸","恒温","毛发","是","否","否","是","否","否"},
					{"蛙","冷血","无","否","是","否","有时","是","是"},
					{"巨蜥","冷血","鳞片","否","是","否","否","是","否"},
					{"蝙蝠","恒温","毛发","是","否","是","否","是","否"},
					{"猫","恒温","皮","是","否","否","否","是","否"},
					{"豹纹鲨","冷血","鳞片","是","否","否","是","否","否"},
					{"海龟","冷血","鳞片","否","是","否","有时","是","否"},
					{"豪猪","恒温","刚毛","是","否","否","否","是","是"},
					{"鳗","冷血","鳞片","否","是","否","是","否","否"},
					{"蝾螈","冷血","无","否","是","否","有时","是","是"}};
			String[] outputData=new String[]{"哺乳类","爬行类","鱼类","哺乳类","两栖类","爬行类",
				"哺乳类","哺乳类","鱼类","爬行类","哺乳类","鱼类","两栖类"};	
			
			C45 c45=new C45(inputData,outputData);
			c45.exec();
		}

}
