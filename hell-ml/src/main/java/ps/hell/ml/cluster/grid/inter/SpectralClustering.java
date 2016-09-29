package ps.hell.ml.cluster.grid.inter;

import ps.hell.ml.cluster.divide.Kmeans2;
import ps.hell.math.base.MathBase;
import Jama.Matrix;
import Jama.SingularValueDecomposition;

/**
 * 谱聚类
 * 计算拉普拉斯矩阵
 * 并svd分解对应矩阵
 * 获取前n个特征值对应特征向量
 * 根据特征向量，使用kmeans计算值
 * @author Administrator
 *
 */
public class SpectralClustering {
	/**
	 * ftype support "Normalized" or "Ratio"
	 *		      two different way to calculate Laplacian
	 */
	public String ftype="Normalized";//Ratio
	
	public double[][] x=null;
	public int[] labels=null;
	public double[][] centroids=null;
	public double[][] w=null;
	public double[][] D=null;
	/**
	 * 拉普拉斯矩阵
	 */
	public double[][] L=null;
	public int k=4;
	
	public int iterator=100;
	public double threshold=0.1;
	/**
	 * 
	 * @param X 数据
	 * @param K 期望聚类的数量
	 * @param ftype 
	 */
	public SpectralClustering(double[][] X,int K,String ftype){
		this.x=X;
		this.k=K;
		this.ftype=ftype;
		this.labels=new int[X.length];
		this.centroids=new double[K][];
		for(int i=0;i<K;i++){
			centroids[i]=new double[x[0].length];
		}
		this.w=distmat(X,X);
		double[] temp=new double[X.length];
		for(int i=0;i<X.length;i++){
			for(int j=0;j<X[0].length;j++){
				temp[i]+=X[i][j];
			}
		}
		this.D=new double[X.length][];
		for(int i=0;i<X.length;i++){
			D[i]=new double[X.length];
			D[i][i]=temp[i];
		}
		this.L=MathBase.minus(D, w);
		if(ftype.endsWith("Normalized")){
			for(int i=0;i<X.length;i++){
				for(int j=0;j<X.length;j++){
					if(i==j){
						continue;
					}
					D[i][j]=1;
				}
			}
			double[][] te=new double[D.length][];
			for(int i=0;i<te.length;i++){
				te[i]=D[i].clone();
				for(int j=0;j<te.length;j++){
					te[i][j]=Math.pow(te[i][j],-0.5);
				}
			}
			this.L=MathBase.times(MathBase.times(te,this.L),te);
		}
	}
	
	public double[][] distmat(double[][] X,double[][] Y){
		double[][] dm=MathBase.number(X.length,Y.length,0d);
		for(int i=0;i<X.length;i++){
			for(int j=i+1;j<Y.length;j++){
				double dis=dist(X[i],Y[j]);
				dm[i][j]=dis;
				dm[j][i]=dis;
			}
		}
		return dm;
	}
	
	public double dist(double[] d1,double[] d2){
		//欧几里得距离
		double val=0;
		for(int i=0;i<d1.length;i++){
			val+=Math.pow(d1[i]-d2[i],2);
		}
		return val;
	}
	
	public void exec(){
		 train();
	}
	public void train(){
		//计算特征值 和特征向量
		Matrix a=new Matrix(L);
		SingularValueDecomposition s=a.svd();
		double[][] v=s.getV().getArray();//获取特征向量
		double[] single=s.getSingularValues();//获取特征值
		double[][] temp_v=MathBase.getRows(v, 0,k-1);
		Kmeans2 kmeans=new Kmeans2(temp_v,k);
		kmeans.run();
		this.labels=kmeans.clusterOld;
	}
	
	public static void main(String[] args) {
		SpectralClustering sc=new SpectralClustering(null,10,null);
		sc.exec();
	}
}

