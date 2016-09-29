package ps.hell.ml.statistics;

import Jama.Matrix;
import Jama.SingularValueDecomposition;
import ps.hell.math.base.MathBase;

/**标准和参数有些许不同
*标准化矩阵后可以用方差或者相关系数矩阵作为协方差矩阵（两者相等）
*通过计算特征值和特征向量可以知道方差贡献率和一定的标准化载荷量（表示不同维的样本在该主成分下的比率）
*计算载荷sqrt(lamd(i))*aij/sum(lamd)
*通过载荷计算样本的得分与总得分可以计算排名
*可以做因子旋转（下没做）
*/
public class PCA_2 {

	/**
	 * 输入数据集
	 */
	public double[][] inputData=null;
	
	/**
	 * 输入数据的每列方差值
	 */
	public double[] colStd=null;
	/**
	 * 列对应的均值
	 */
	public double[] colMean=null;
	/**
	 * 列对应的相关系数矩阵
	 */
	public double[][] corr=null;
	/**
	 * 全部特征值并为排序后从达到小
	 */
	public double[] singleValue=null;
	/**
	 * 特征值对应的特征向量
	 */
	public double[][] singleMo=null;
	/**
	 * 对应特征值的累积比率
	 */
	public double[] singleP=null;
	/**
	 * 对应的原始数据的列号
	 */
	public int[] singleIndex=null;
	/**
	 * 标准化载荷
	 */
	public double[][] loadMo=null;
	/**
	 * 再限制的最大贡献对应的有效index值
	 */
	public int maxIndex=0;
	/**
	 * 获取原数据得分
	 * 只包含 有效特征值部分
	 * @return
	 */
	public double[][] sourceSorce=null;
	/**
	 * 单成分得分
	 */
	public double[] singleScore=null;
	public PCA_2(double[][] inputData)
	{
		this.inputData=new double[inputData.length][];
		for(int i=0;i<inputData.length;i++)
		{
			this.inputData[i]=inputData[i].clone();
		}
		this.colMean=MathBase.colMean(inputData);
		this.colStd=MathBase.colStd(inputData,colMean);
		this.corr=MathBase.correlationMatrix(inputData,colStd,colMean);
		System.out.println("打印相关系数");
		for(int i=0;i<corr.length;i++)	
		{
			for(int j=0;j<corr[i].length;j++)
			{
				System.out.print(corr[i][j]+"\t");
			}
			System.out.println();
		}
		SingularValueDecomposition svd = (new Matrix(corr)).svd();
		singleValue=svd.getSingularValues();
		System.out.println("特征值");
		for(int i=0;i<singleValue.length;i++)
		{
			System.out.print(singleValue[i]+"\t");
		}
		System.out.println("特征向量 ");
		//其中u为特征向量 v为-u
		singleMo=svd.getU().getArray();
		for(int i=0;i<singleMo.length;i++)
		{
			for(int j=0;j<singleMo[0].length;j++)
			{
				System.out.print(singleMo[i][j]+"\t");
			}
			System.out.println();
		}
		// 调整特征值排序 为降序 及获取  累积概率
		sortSingle();
		
	}
	/**
	 * 贡献百分比下线
	 * @param plimit
	 */
	public void exec(double plimit)
	{
		//获取有效标记位置 再plimit位置
		maxIndex=0;
		boolean flag=true;
		for(int i=0;i<singleP.length;i++)
		{
			if(singleP[i]<plimit)
			{
				maxIndex=i;
				flag=false;
				break;
			}
		}
		if(flag)
		{
			maxIndex=singleP.length-1;
		}
		
		//需要获取 载荷 得分等
		computeLoadMo();
		compueElementScore();
	}
	/**
	 * 调整特征值排序 为降序
	 * 及获取  累积概率
	 */
	public void sortSingle()
	{
		//初始化 标号
		singleIndex=new int[singleValue.length];
		for(int i=1;i<singleValue.length;i++)
		{
			singleIndex[i]=i;
		}
		//排序 特征值使用 冒泡排序
		for(int i=0;i<singleValue.length-1;i++)
		{
			for(int j=0;j<singleValue.length-i-1;j++)
			{
				if(singleValue[j]<singleValue[j+1])
				{
					double temp=singleValue[j];
					singleValue[j]=singleValue[j+1];
					singleValue[j+1]=temp;
					int in=singleIndex[j];
					singleIndex[j]=singleIndex[j+1];
					singleIndex[j+1]=in;
					for(int l=0;l<singleMo.length;l++)
					{
						 temp=singleMo[l][j];
						 singleMo[l][j]=singleMo[l][j+1];
						 singleMo[l][j+1]=temp;
					}
				}
			}
		}
		singleP=new double[singleValue.length];


		double sum=0.0;
		for(int i=0;i<singleValue.length;i++)
		{
			sum+=singleValue[i];
		}
		for(int i=0;i<singleValue.length;i++)
		{
			if(i==0)
			{
				singleP[i]=singleValue[i];
			}else
			{
				singleP[i]=singleP[i-1]+singleValue[i];
			}
		}
		for(int i=0;i<singleValue.length;i++)
		{
			singleP[i]/=sum;
		}
	}
	/**
	 * 计算标准化载荷
	 * 计算公式
	 * sqrt(singleV)*singleMo/sqrt(sum(singleV));
	 */
	public void computeLoadMo()
	{
		loadMo=new double[maxIndex+1][maxIndex+1];
		double sum=0.0;
		for(int i=0;i<maxIndex+1;i++)
		{
			sum+=singleValue[i];
		}
		System.out.println("载荷状态:");
		for(int i=0;i<maxIndex+1;i++)
		{
			for(int j=0;j<maxIndex+1;j++)
			{
				loadMo[i][j]=Math.sqrt(singleValue[i])*singleMo[i][j]/Math.sqrt(sum);
				System.out.print(loadMo[i][j]+"\t");
			}
			System.out.println();
		}
	}
	/**
	 * 计算因子得分
	 * 只计算 有效的成分
	 * 获取对应的排名
	 *不计算排名
	 *返回的得分从高到低即为排名
	 * 原数据的因子得分为 坐标系状态
	 *但因素得分 为 排名情况
	 */
	public void compueElementScore()
	{
		sourceSorce=new double[inputData.length][maxIndex+1];
		System.out.println("因子得分:");
		for(int i=0;i<inputData.length;i++)
		{
			for(int j=0;j<maxIndex+1;j++)
			{
				for(int l=0;l<maxIndex+1;l++)
				{
					sourceSorce[i][j]+=inputData[i][l]*loadMo[singleIndex[j]][l];
				}
				System.out.print(sourceSorce[i][j]+"\t");
			}
			System.out.println();
		}
		//计算单成分得分 即每一个样本一个得分
		double[] p=new double[maxIndex+1];
		double sum=0.0;
		for(int i=0;i<maxIndex+1;i++)
		{
			sum+=singleValue[i];
		}
		singleScore=new double[inputData.length];
		//eig2score[,1:length(eigen2val)]%*%as.matrix(eigen2val)/sum(eigen2val)
		for(int i=0;i<inputData.length;i++)
		{
			for(int j=0;j<maxIndex+1;j++)
			{
				singleScore[i]+=inputData[i][singleIndex[j]]*sourceSorce[i][j];
			}
			singleScore[i]/=sum;
		}
	
		//
	}
	public static void main(String[] args) {
		double[][] data=new double[][]{
				{2,4,5},{3,4,5},{6,3,5},{6,7,3}
		};
		PCA_2 pca=new PCA_2(data);
		pca.exec(0.8);
		for(int i=0;i<data.length;i++)
		{
			System.out.println(i+":sorce:"+pca.singleScore[i]);
		}
	}
}
/*
pca_test<-function(x1)
{
#diag(var(x)) or
#标准化
x<-na.omit(x1);
n=nrow(x);
varxcol<-colSums(t((t(x)-as.vector(colSums(x)/n))^2))/(n-1)#计算方差
print(varxcol);
var1<-t((t(x)-colSums(x)/n)/varxcol)
print(dim(var1));
print(var1);
print(var1);
print("11");
#计算相关系数矩阵
cor1<-matrix(NA,ncol(x),ncol(x),1);
print(var1[,1]);
print(var1[,2])
for(i in 1:ncol(x))
{
 for(j in 1:ncol(x))
{
 a1<-x[,i]-sum(x[,i])/n
 a2<-x[,j]-sum(x[,j])/n
  cor1[i,j]<-(a1)%*%(a2)/(sqrt(a1%*%a1))/sqrt(a2%*%a2);
}
}
cor1<-cor(var1);
print("22");
print(cor1);
#计算对应的特征值特征向量
eigen1<-eigen(cor1);
#排序主成分
print(eigen1);
order2<-order(eigen1$values);
order1<-NULL;
for(i in length(eigen1$values):1)
{
order1<-append(order1,which(order2==i));
}
#计算主成分贡献率
conbi<-eigen1$values[order1]/(sum(eigen1$values))
print(eigen1$values[order1]);
print("33");
print(eigen1);
#提取总贡献率超过80%
print("zzz");
print(conbi)
m1=which(cumsum(conbi)>=0.8)[1];
eigen2val<-eigen1$values[order1][1:m1]
#eigen2vec<-eigen1$vectors[,order1][,1:m1]
if(length(eigen2val)==1)
{
eigen2val<-eigen1$values[order1][1:2];
#print(eigen1$vectors[,order1]);
#eigen2vec<-eigen1$vectors[,order1][,1:2];
}
print(eigen2val);
#print(eigen2vec);
#主成分载荷
load1<-t(sqrt(eigen1$val)*t(eigen1$vectors)/sqrt(sum(eigen1$val)));#标准化载荷
print("载荷");
print(eigen1$vectors);
print(load1);
#计算得得分
eig2score<-var1%*%load1;
eig2score<-eig2score[,order1]
print("xxx");
#print(eigen2vec);
print(cor1[,order1]);
print(eig2score);
#输出单成分得分
print("输出得分")
print(eig2score);
print(eigen2val);
eig2score2<-eig2score[,1:length(eigen2val)]%*%as.matrix(eigen2val)/sum(eigen2val)
#计算各个样本的总得分排名
print("排名");
print(eig2score2);
rank1<-order(eig2score2);
#输出排名
print(cbind(eig2score,rank1));
#提取2个主要主成分绘图
print(eig2score);
print(n);
plot(eig2score[,1:2],col=1:n);
text(eig2score[,1:2],labels=colnames(x))
abline(h=0,v=0,col="grey")
}
pca_test(USArrests)
*/
