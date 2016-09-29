package ps.hell.ml.forest.classification.decisionTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import ps.landerbuluse.ml.math.MathBase;
import ps.landerbuluse.ml.math.MathBase.MathTable;
import ps.landerbuluse.ml.math.matrix.Matrix;
import ps.landerbuluse.ml.statistics.base.Normal;
import ps.landerbuluse.struct.store.struct.TableTree;
import ps.landerbuluse.struct.store.struct.TableTree.Node;


public class C50 {
/*
#c50决策树作为商业版本的决策树，尤其高效的速度和，更加符合理论意义的分类方式
#信息熵，信息熵增益率作为样本数据的分支方式，下文中没有对连续型变量做输入，可以参考chimerge转化为分类型变量
#如果想转变为2叉树形式可以参考以gini或者信息熵增益率作为分类方式
#最终形成的分类数做减枝的参考是子误差加权后比父误差要小，则分类延续，否则剪枝
#w1=matrix(sign(rnorm(120)),15,8,1)
*/
	/**
	 * 输入变量
	 */
	public String[][] inputDataA=null;
	
	/**
	 * 将输入变量转化为对应的int类型
	 */
	public int[][] inputDataToIntA=null;
	
	/**
	 * 输入数据+输出的输入string对应的int形式
	 */
	public ArrayList<HashMap<String,Integer>> dataMap=new ArrayList<HashMap<String,Integer>>();
	/**
	 * 输出变量
	 */
	public String[] outputDataA=null;
	/**
	 * 输出变量转化成对应的int类型
	 */
	public int[] outputDataToIntA=null;
	
	
	/**
	 * 文件长度
	 */
	public int fileLength=0;
	
	
	
	public TableTree fTree=new TableTree();
	/**
	 * 
	 * @param data 输入变量
	 *  默认最后一个为输出变量
	 */
	public C50(String[][] data)
	{
		try
		{
			fileLength=data.length;
		}
		catch(Exception e)
		{
			System.out.println("输入数据出现错误");
			System.exit(1);
		}
		//初始化输入数据
		inputDataA=new String[data.length][data[0].length-1];
		inputDataToIntA=new int[data.length][data[0].length-1];
		outputDataA=new String[data.length];
		outputDataToIntA=new int[data.length];
		int i=0;
		int j=0;
		i=0;
		for(String[] st:data)
		{
			j=0;
			for(String s:st)
			{
				if(j==data[0].length-1)
				{
					outputDataA[i]=s;
				}else
				{
					inputDataA[i][j]=s;
					j++;
				}
			}
			i++;
		}

		//初始化输出数据
		changeStringToInt(inputDataA, inputDataToIntA, outputDataA, outputDataToIntA, dataMap);
		//dataDimPCG=double[stringDimensionality];
		System.out.println("初始化成功");	
	}
	
	/**
	 * 执行程序
	 */
	public void Exec()
	{
		//判断维度选择
		execSonLeaf(this.inputDataToIntA,this.inputDataA,this.outputDataToIntA,this.outputDataA,1,1,fTree.root,dataMap);
	}
	/**
	 * c50向下搜索方法
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
		getContingentProbability(inputDataToInt,inputP,outputDataToInt,outputP,
				outputlength,condtionP,dataMap);
		//获取相关信息熵
		double[] outputEnt= getYEntropy(outputP);
		double[] inputEnt= getXEntropy(inputP);
		//计算条件熵
		ArrayList<double[]> condtionEnt=getConditionalEntropy(condtionP,outputP,dataMap);
		/**
		 * 对应的基尼系数
		 */
		double[] dataDimPCG=getConditionalEntropyGain(inputEnt, outputEnt,condtionEnt,condtionP);
		
		System.out.println("length:"+dataDimPCG.length);
		//获取最大基尼系数的列
		int maxIndex=MathBase.getMaxIndex(dataDimPCG);
		
		//计算该组的误差
		System.out.println(inputDataToInt.length);
		int[] maxIndexValue=MathBase.getCol(inputDataToInt,maxIndex);
		double errorSon=getStdSum(maxIndexValue,outputDataToInt);
		if(dataDimPCG.length<=1)
		{
			System.out.println("已到叶子节点:"+"深度:"+deep+"\t误差:"+errorSon);
			return;
		}
		if(errorSon>errorFather)
		{
			System.out.println("无法向下延伸:"+"深度:"+deep+"\t误差:"+errorSon);
			return;
		}
		
		//从 inputDataToInt 中剔除 maxIndex列数据
		int[][] inputDataTempToInt=MathBase.moveCol(inputDataToInt, maxIndex);
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
		String[][] inputDataString2=MathBase.moveCol(inputDataString,maxIndex);
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
			double errorFather2 =getStdSum(MathBase.getCol(sonInputDataToInt,maxIndex),outputTemp.get(zq));
			int index=-1;
			//添加到fTree中
			for(Entry<Integer, Integer> ll: map.entrySet())
			{
				if(ll.getValue()==zq)
				{
					index=ll.getValue();
				}
			}
	
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

			Leaf leaf=new Leaf(dataMapTemp.get(maxIndex).get(index),sonInputDataToInt.length*1.0/inputDataToInt.length,
					sonInputDataToInt.length,1-(ln*1.0)/table2.num.length,dataMapTemp.get(dataMapTemp.size()-1).get(ln));
			Node sonNode=fTree.add(node,leaf,deep);
			System.out.println("sonInputDataToInt:"+sonInputDataToInt[0].length);
			execSonLeaf(sonInputDataToInt,inputDataStringTemp.get(zq-1),
					outputTemp.get(zq-1),outputDataStringTemp.get(zq-1),deep,errorFather2,sonNode,dataMapTemp);
		}
		
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
	 * 获取条件概率
	 * @param inputDataToInt 输入int矩阵
	 * @param inputP 输出的概率
	 * @param outputDataToInt 输出矩阵
	 * @param outputP 输出概率
	 * @param outputLength 输出分类数量
	 * @param condtionP 条件概率
	 */
	public void getContingentProbability(int[][] inputDataToInt,ArrayList<double[]> inputP,int[] outputDataToInt,double[] outputP,
			int outputLength,ArrayList<double[]> condtionP,ArrayList<HashMap<String,Integer>> dataMap)
	{
		//inputP=new ArrayList<double[]>();
		//输出变量的统计量
		int[] temp=new int[outputLength];
		for(int i=0;i<outputDataToInt.length;i++)
		{
			//p(b)
			temp[outputDataToInt[i]]++;
		}
		//输出变量的概率
		//outputP=new double[outputLength];
		for(int i=0;i<temp.length;i++)
		{
			outputP[i]=temp[i]*1.0/outputDataToInt.length;
		}
		//System.out.println(outputP.length);
		//condtionP=new ArrayList<double[]>();
		//计算x条件概率\
		for(int j=0;j<inputDataToInt[0].length;j++)
		{
			//存储条件概率
			int len=dataMap.get(j).size();
			//条件概率
			double[] tempCP=new double[len*outputLength];
			//概率
			double[] tempP2=new double[len];
			//p(ab)
			for(int i=0;i<outputDataToInt.length;i++)
			{
				tempP2[inputDataToInt[i][j]]++;
				tempCP[inputDataToInt[i][j]*outputLength+outputDataToInt[i]]++;
			}
			for(int i=0;i<len*outputLength;i++)
			{
				//p(b/a)=p(ab)/p(a)
				//获取对应的位置
				int l=i/outputLength;
				tempP2[i]/=outputDataToInt.length;
				tempCP[i]/=outputDataToInt.length*tempP2[l];
			}
			condtionP.add(tempCP);
			inputP.add(tempP2);
		}		
	}
	/**
	 * 信息熵公式
	 * @param p
	 * @return
	 */
	public double getEntropy(double p)
	{
		return -p*Math.log(p)/Math.log(2D);
	}
	
	/**
	 * 计算条件熵
	 * @param condtionP 条件概率
	 */
	public ArrayList<double[]>  getConditionalEntropy(ArrayList<double[]> condtionP,
			double[] outputP,ArrayList<HashMap<String,Integer>> dataMap)
	{	
		ArrayList<double[]> condtionEnt=new ArrayList<double[]>();
		for(int i=0;i<condtionP.size();i++)
		{
			int len=dataMap.get(i).size();
			double[] temp=new double[len*outputP.length];
			condtionEnt.add(temp);
		}
		//计算a的条件熵
		for(int i=0;i<condtionP.size();i++)
		{
			int len=dataMap.get(i).size();
			for(int j=0;j<outputP.length;j++)
			{
				for(int m=0;m<len;m++)
				{
				//条件熵  计算公式-m_test*log(base=2,m_test)
					//存储的条件概率其中排序为 a*length+b   (b0,b1)(b0,b1)方式
					condtionEnt.get(i)[m*outputP.length+j]=getEntropy(condtionP.get(i)[m*outputP.length+j]);
				}
			}
		}
		return condtionEnt;
	}
	/**
	 * 获取a的信息熵
	 */
	public double[] getXEntropy(ArrayList<double[]> inputP)
	{	
		double[] inputEnt=new double[inputP.size()];
		//计算a的熵
		int t=0;
		for(double[] ll:inputP)
		{
			double te=0.0;
			for(double l:ll)
			{
				inputEnt[t]+=getEntropy(l);
			}
			t++;
		}
		return inputEnt;
	}
	/**
	 * 获取Y的信息熵
	 * @param outputP 输出的概率
	 * @return
	 */
	public double[] getYEntropy(double[] outputP)
	{
		//计算b的信息熵
		double[] outputEnt=new double[outputP.length];
		for(int i=0;i<outputP.length;i++)
		{
			outputEnt[i]=getEntropy(outputP[i]);
		}
		return outputEnt;
	}
	/**
	 * 获取信息熵增益Gains
	 * 用条件熵计算信息熵
	 * 同列上元素的正负信息熵-(sum(正负条件熵)*元素占总样本比率)
	 * @param inputEnt 输入的信息熵
	 * @param outputEnt 输出的信息熵
	 * @param condtionEnt 输入的条件熵 存储的条件概率其中排序为 a*length+b   (b0,b1)(b0,b1)方式
	 * @param condtionP 条件熵
	 * @return 	输入列对应的基尼系数
	 */
	public double[] getConditionalEntropyGain(double[] inputEnt,double[] outputEnt,ArrayList<double[]> condtionEnt,
			ArrayList<double[]>	condtionP)
	{
		double[] dataDimPCG=new double[inputEnt.length];
		for(int i=0;i<inputEnt.length;i++)
		{
			//(ent(b)-ent(a|b))/(ent(a))
			dataDimPCG[i]=0.0;
			int ln=0;
			for(double lp:condtionEnt.get(i))
			{
				//sum(条件熵*概率)
				dataDimPCG[i]+=-lp*condtionP.get(i)[ln];
				ln++;
			}
			for(double lp:outputEnt)
			{
				dataDimPCG[i]+=lp;
			}
			dataDimPCG[i]/=inputEnt[i];
		}
		return dataDimPCG;
	}
	/**
	 * 获取加权误差
	 */
	public double getStdSum(int[] x,int[] y)
	{
		//获取众数

		MathTable mode=MathBase.getTable(x,y);
		//行差异  行总数-众数对应的数求得的值
		double[] errorRow=new double[mode.nameRow.length];
		for(int i=0;i<mode.nameRow.length;i++)
		{
			errorRow[i]=mode.getSumRow(i)-mode.getMaxRow(i).num;
		}
		double lp=0.0;
		for(int i=0;i<errorRow.length;i++)
		{
			lp=errorRow[i]/errorRow.length;
			errorRow[i]=lp+Math.sqrt(lp*(1.0-lp)/errorRow.length);
		}
		return MathBase.sum(errorRow);
	}
	
	public static void main(String[] args) {
		 
		Normal n1=new Normal(-3,1);
		Matrix m1=n1.random(50,2,0);
		String[][] ll=m1.toTransString();
		
		C50 c50=new C50(m1.toTransString());
		c50.Exec();
		TableTree fTree=c50.fTree;
		System.out.println(fTree.allDeepNode+"\t"+fTree.allSizeLeaf);
	}
	
}
/*
#c50决策树作为商业版本的决策树，尤其高效的速度和，更加符合理论意义的分类方式
#信息熵，信息熵增益率作为样本数据的分支方式，下文中没有对连续型变量做输入，可以参考chimerge转化为分类型变量
#如果想转变为2叉树形式可以参考以gini或者信息熵增益率作为分类方式
#最终形成的分类数做减枝的参考是子误差加权后比父误差要小，则分类延续，否则剪枝
#w1=matrix(sign(rnorm(120)),15,8,1)
info_entropy<-function(x,y)#条件熵/信息熵增益率#分类型
{
#print(x);
#print(y);
z=as.matrix(cbind(x,y));
n=ncol(z);
m_test=c();
m2=c();
m_sel=c();
m_sel2=c();
m_test2=c();
m_sel3=c();
m_rat=c();
for(i in 1:(n-1))
 {
  m2<-NULL;
  m2=ftable(z[,n],z[,i]);
  m_test=m2/rep(colSums(m2),each=nrow(m2));#对应的条件概率
  m_test2=rep(colSums(m2)/(sum(m2)),each=nrow(m2))*(-m_test*log(base=2,m_test));#对应条件熵
  m_test2[which(is.nan(m_test2))]<-0;
  #条件熵
  m_sel<-sum(m_test2);
  #信息熵y
  m_test0<-table(z[,n]);
  m_sel2<-(-m_test0/sum(m_test0)*(log(base=2,m_test0/sum(m_test0))));
  m_sel2[which(is.nan(m_sel2))]<-0;
  m_sel2<-sum(m_sel2);
  #信息熵x
  m_test0<-table(z[,i]);
  m_sel3<--m_test0/sum(m_test0)*(log(base=2,m_test0/sum(m_test0)));
  m_sel3[which(is.nan(m_sel3))]<-0;
  m_sel3=sum(m_sel3);
  #信息熵增益
  if((m_sel2-m_sel)==0 & m_sel3==0)
  {
  m_rat=append(m_rat,10000);
  }
  else
  {
  #print((m_sel2-m_sel)/m_sel3);
  m_rat=append(m_rat,(m_sel2-m_sel)/(m_sel3+1e-10));
  #print("m_rat");
  #print(m_rat);
  }
 }
return(m_rat)
}

 

error_emite<-function(x,y)#计算加权误差x,y为vector
{
ei<-c();
ei0<-apply(table(x,y),1,function(x) which.max(x));#获取众数为正确预测
ei1<-apply(table(x,y),1,sum);
#print(paste("length(ei0):",length(ei0)));
for(i in 1:length(ei0))
 {
 ei=append(ei,ei1[i]-table(x,y)[i,ei0[i]]);
 #print(ei);
 }
ni<-length(x[!is.na(x)]);
error1=ei/ni+sqrt(ei/ni*(1-ei/ni)/ni);
#print(error1*(ni/sum(ni)));
return<-sum(error1*(ni/sum(ni)));#加权误差
}
#error_emite(m3[,5],m3[,6]);


#ifelse对矩阵无效替换函数
ifelse1<-function(test,yes,no)
{
c=c();
if(test==T)
{
c=yes;
c;
}
else
{ no;}
}


son_father<-function(xn,yn,son_fa=1,kk=1,choice1)
{
kk=kk+1;
x<-as.matrix(xn);
y<-as.matrix(yn);
info_descsion=c();
info_set<-c();
info_set2<-c();
error_son<-c();
set0<-c();
 info_descsion<-info_entropy(x,y);
 info_set<-which.max(info_descsion);#获取信息增益最大的列组

 info_set2<-unique(as.vector(x[,info_set]));#获取分组 
 x2=t(t(x[, -info_set]));
 error_son<-error_emite(x[,info_set],y);#获取已定子节点的所有误差
 #比较父节点和子节点的误差
 nc=ncol(x2);

 if(all(is.na(x2)))
 {
 nc=0;
 }
 if(son_fa>error_son & nc==0)#如果是最后一个则输出
 {
 print(paste("分类树底层结点 , ",info_set," , 父亲偏差:",son_fa,
   " , 孩子偏差：",error_son," 深度:",kk," 分枝条件条件：",choice1,sep=""));
 }
 else 
 {
  if(son_fa<error_son & nc!=0)
  {
   print(paste("无子结点 , ",info_set," , 父亲偏差:" ,son_fa,
    ", 孩子偏差：",error_son," 深度:",kk," 分枝条件：","无",sep=""));
  }

  else 
   {
   if(son_fa<error_son & nc==0)
    {
     print(paste("分类树底层无结点 , ",info_set," , 父亲偏差:" ,son_fa,
      ", 孩子偏差：",error_son," 深度:",kk," 分枝条件：","无",sep=""));
 
    }
   else  
    {
    if(son_fa>error_son & nc!=0)#判断误差分支条件并执行是否继续分枝
    {
     
     for(i in 1:length(info_set2))#获取已定子节点的所有误差
     { 
      print(paste("连续结点 , ",info_set," , 父亲偏差:" ,son_fa,
       ", 孩子偏差：",error_son," 深度:",kk," 分枝条件：",info_set2[i],sep=""));
     
      set0<-which(x[,info_set]==info_set2[i]);#获取分组i

      y1<-y[set0];

      x3<-x[set0,info_set];#父节点的下x2为剩下的  
      error_father1<-error_emite(x3,y1);#计算分支误差父节点
   
      fuzhi<-son_father(ifelse1(test=(length(set0)==1),yes=t(as.matrix(x2[set0,])),no=x2[set0,]),y1,son_fa=error_father1,kk=kk,choice1=info_set[i]);#n差树循环
     }     
    }

   }
  }
 }
}

 

c501<-function(x,y)#分类型
{
x1<-as.matrix(x);
#计算当前层的误差
son_father(x1,y);
}


# c501(m3[,1:5],m3[,6])
w1=matrix(sign(rnorm(60000)),3000,20,1);
c501(w1[,1:19],w1[,20]);
*/