package ps.hell.ml.forest.model.anns;

import java.util.ArrayList;

import ps.hell.math.base.matrix.Matrix;
import ps.hell.base.sort.AllSort;


/**
 * 经典RBF
 * Rbf 径向基神经网络，主哟啊是根据 径向基函数处理
 * 并且隐层神经元数量和 训练样本一样
 * @author Administrator
 *
 *用聚类方法求数据中心和扩展常数，输出权值和阈值用伪逆法求解。
 *隐藏节点数M=10，隐藏节点重叠系数λ=1，初始聚类中心取前10个训练样本。
 */
public class RbfAnn {
	 
	private int P=100;        //输入样本的数量
	double[] X=new double[this.P];  //输入样本
	double[][] Y=new double[this.P][1];        //输入样本对应的期望输出
	private int M=10;         //隐藏层节点数目
	double[] center=new double[this.M];       //M个Green函数的数据中心
	double[] delta=new double[this.M];        //M个Green函数的扩展常数
	double[][] Green=new double[this.P][this.M];         //Green矩阵
	double[][] Weight=new double[this.M][1];       //权值矩阵
	 
	/*Hermit多项式函数*/
	public double Hermit(double x){
	    return 1.1*(1-x+2*x*x)*Math.exp(-1*x*x/2);
	}
	 
	/*产生指定区间上均匀分布的随机数*/
	public double uniform(double floor,double ceil){
	    return floor+1.0*Math.random()/1*(ceil-floor);
	}
	 
	/*产生区间[floor,ceil]上服从正态分布N[mu,sigma]的随机数*/
	public double RandomNorm(double mu,double sigma,double floor,double ceil){
	    double x,prob,y;
	    do{
	        x=uniform(floor,ceil);
	        prob=1/Math.sqrt(2*Math.PI*sigma)*Math.exp(-1*(x-mu)*(x-mu)/(2*sigma*sigma));
	        y=1.0*Math.random()/1;
	    }while(y>prob);
	    return x;
	}
	 
	/*产生输入样本*/
	public void generateSample(){
	    for(int i=0;i<P;++i){
	        double in=uniform(-4,4);
	        X[i]=in;
	        Y[i][0]=Hermit(in)+RandomNorm(0,0.1,-0.3,0.3);
	    }
	}
	 
	/*寻找样本离哪个中心最近*/
	public int nearest(double[] center,double sample){
	    int rect=-1;
	    double dist=Double.MAX_VALUE;
	    for(int i=0;i<center.length;++i){
	        if(Math.abs(sample-center[i])<dist){
	            dist=Math.abs(sample-center[i]);
	            rect=i;
	        }
	    }
	    return rect;
	}
	 
	/*计算簇的质心*/
	public double calCenter(Double[] g){
	    int len=g.length;
	    double sum=0.0;
	    for(int i=0;i<len;++i)
	        sum+=g[i];
	    return sum/len;
	}
	 
	/**KMeans聚类法产生数据中心
	 * 或者选择自组织选择法
	 */
	public void KMeans(){
	    assert(P%M==0);
	    ArrayList<ArrayList<Double>> group=new ArrayList<ArrayList<Double>>();          //记录各个聚类中包含哪些样本
	    for(int i=0;i<M;i++)
	    {
	    	//初始化
	    	group.add(new ArrayList<Double>());
	    }
	    double gap=0.001;       //聚类中心的改变量小于为个值时，迭代终止
	    for(int i=0;i<M;++i){   //从P个输入样本中随机选P个作为初始聚类中心
	        center[i]=X[10*i+3];     //输入是均匀分布的，所以我们均匀地选取
	    }
	    while(true){
	        for(int i=0;i<M;++i)
	            group.set(i,new ArrayList<Double>());   //先清空聚类信息
	        for(int i=0;i<P;++i){       //把所有输入样本归到对应的簇
	            int c=nearest(center,X[i]);
	            group.get(c).add(X[i]);
	        }
	        double[] new_center=new double[M];       //存储新的簇心
	        for(int i=0;i<M;++i){
	            Double[] g=new Double[group.get(i).size()];
	            group.get(i).toArray(g);
	            new_center[i]=calCenter(g);
	        }
	        boolean flag=false;
	        for(int i=0;i<M;++i){       //检查前后两次质心的改变量是否都小于gap
	            if(Math.abs(new_center[i]-center[i])>gap){
	                flag=true;
	                break;
	            }
	        }
	        center=new_center;
	        if(!flag)
	            break;
	    }
	}
	 
	/**生成Green矩阵
	 * 一般使用多元高斯函数
	 *g(x,xp)=exp(-1/(2*delta*delta)*(x-xp)*(x-xp)) 
	 */
	public void calGreen(){
	    for(int i=0;i<P;++i){
	        for(int j=0;j<M;++j){
	            Green[i][j]=Math.exp(-1.0*(X[i]-center[j])*(X[i]-center[j])/(2*delta[j]*delta[j]));
	        }
	    }
	}
	 
	/*求一个矩阵的伪逆*/
	double[][] getGereralizedInverse(double[][] matrix){
	   // return (matrix.getTranspose()*matrix).getInverse()*(matrix.getTranspose());
		return Matrix.getWeiInverse(matrix);
	}
	 
	/*利用已训练好的神经网络，由输入得到输出*/
	double getOutput(double x){
	    double y=0.0;
	    for(int i=0;i<M;++i)
	        y+=Weight[i][0]*Math.exp(-1.0*(x-center[i])*(x-center[i])/(2*delta[i]*delta[i]));
	    return y;
	}
	/**
	 * 排序算法
	 */
	 public void sort(double[] matrix)
	 {
		 AllSort.selectSort(matrix, matrix.length,true);
	 }
	public static void main(String[] args){
		RbfAnn rfb=new RbfAnn();
		rfb.generateSample();       //产生输入和对应的期望输出样本
		rfb.KMeans();           //对输入进行聚类，产生聚类中心
		rfb.sort(rfb.center);      //对聚类中心（一维数据）进行排序
	    
	    //根据聚类中心间的距离，计算各扩展常数
		rfb.delta[0]=rfb.center[1]-rfb.center[0];      
		rfb.delta[rfb.M-1]=rfb.center[rfb.M-1]-rfb.center[rfb.M-2];
	    for(int i=1;i<rfb.M-1;++i){
	        double d1=rfb.center[i]-rfb.center[i-1];
	        double d2=rfb.center[i+1]-rfb.center[i];
	        rfb.delta[i]=d1<d2?d1:d2;
	    }
	     
	    rfb.calGreen();     //计算Green矩阵
	    rfb.Weight=new Matrix(rfb.getGereralizedInverse(rfb.Green)).times(new Matrix(rfb.Y)).data;      //计算权值矩阵
	     
	    //根据已训练好的神经网络作几组测试
	    for(int x=-4;x<5;++x){
	    	System.out.print(x+"\t");
	        System.out.println(rfb.getOutput(x)+"\t"+rfb.Hermit(x));      //先输出我们预测的值，再输出真实值
	    }
	}
	
	
}
