package ps.hell.ml.sequenceTime.filter;

import ps.hell.math.base.MathBase;

/**
 * http://blog.csdn.net/pi9nc/article/details/27384113
 * @author Administrator
 *
 */
public class KalmanFilter2 {

	
	public double[] dataY=null;
	public double[] dataX=null;
	//the number of the stateparamters  
	/**
	 * 状态参数
	 */
	public int StateParamNum=4;
	// the number of the control parameters  
	/**
	 * 控制参数
	 */
	public int ContrParamNum=2; 
	
	 //the number of theobservation parameters  
	/**
	 * 广义参数
	 */
	public int ObsevParamNum=2;
	//the motion transitionmatrix  
	/**
	 * 监控转换矩阵
	 */
	public double[][] F=new double[][]{{1,0,1,0},
								 {0,1,0,1},
								 {0,0,1,0},
								 {0,0,0,1}};
	//the control matrix  
	/**
	 * 控制矩阵
	 */
	public double[][] G=new double[][]{
								  {0.5,0},
								  {0,0.5},
								  {1,0},
								  {0,1}};
	/**
	 * 广义矩阵
	 * the observation matrix
	 */
	public double[][] H=new double[][]{
			{0.5,0},
			{0,0.5},
			{1,0},
			{0,1}
	};
	/**
	 * the control vector
	 */
	public double[][] U=new double[][]{{0},{0}};
	/**
	 * the observation vector  
	 */
	public double[][] Z=MathBase.number(ObsevParamNum,1, 0.0);
	/**
	 *  the covariance of thestate  
	 */
	public double[][] P=null;
	/**
	 * the covariance of thestate noise  
	 */
	public double[][] q=null;
	/**
	 * the covariance of theobserve noise 
	 */
	public double[][] r=null;
	/**
	 * the state vector 
	 */
	public double[][] X=null;
	/**
	 *  the optimal estimationof the the state 
	 */
	public double[][] Xf=null;
	/**
	 * the optimal estimationof the the observation  
	 */
	public double[][] Zf=null;
	public double[][] V=null;
	public double[][] Pf=null;
	/**
	 * 迭代次数
	 */
	public int iteroter=100;
	/**
	 * 数据源
	 */
	public double[][] data=null;
	/**
	 * @param x 输入的x
	 * @param Y 输入的y
	 */
	public KalmanFilter2(double[] x,double[] Y,int iteroter){
		this.dataX=x;
		this.dataY=Y;
		data=new double[x.length][];
		for(int i=0;i<x.length;i++){
			data[i]=new double[2];
			data[i][0]=x[i];
			data[i][1]=Y[i];
		}
		 X=new double[4][];
		 for(int i=0;i<4;i++){
			 X[i]=new double[1];
		 }
		 X[0][0]=x[0];
		 X[1][0]=x[1];
		 X[2][0]=0.001;
		 X[3][0]=0.001;
		 P=new double[StateParamNum][];
		 for(int i=0;i<StateParamNum;i++){
			 P[i]=new double[StateParamNum];
			 P[i][i]=10;
		 }
		 
		 q=new double[StateParamNum][];
		 for(int i=0;i<StateParamNum;i++){
			 q[i]=new double[StateParamNum];
		 }
		 q[0][0]=0.1;
		 q[1][1]=0.1;
		 q[2][2]=0.01;
		 q[3][3]=0.01;
		 
		 r=new double[ObsevParamNum][];
		 for(int i=0;i<ObsevParamNum;i++){
			 r[i]=new double[ObsevParamNum];
		 }
		 r[0][0]=10;
		 r[1][1]=10;
		 Xf=new double[StateParamNum][];
		 for(int i=0;i<StateParamNum;i++){
			 Xf[i]=new double[iteroter];
		 }
		 Zf=new double[StateParamNum][];
		 for(int i=0;i<StateParamNum;i++){
			 Zf[i]=new double[iteroter];
		 }
		 V=new double[StateParamNum][];
		 for(int i=0;i<StateParamNum;i++){
			 V[i]=new double[iteroter];
		 }
		 Pf=new double[StateParamNum][];
		 for(int i=0;i<StateParamNum;i++){
			 Pf[i]=new double[iteroter];
		 }
	}
	
	public void exec(){
		 train();
	}
	
	public void train(){
		for(int i=0;i<iteroter;i++){
			//theestimation of the state in time t 
			double[][] Xest=MathBase.plus(MathBase.times(F, X),MathBase.times(G,U));
			// thecovariance of the estimated state  
			double[][] Pest=MathBase.plus(MathBase.times(MathBase.times(F,P),MathBase.transport(F)),q);
			//theestimation of the observation in time t  
			double[][] Zest=MathBase.times(H, Xest);
			//thecovariance of the estimated observation  
			double[][] Sest=MathBase.plus(MathBase.times(MathBase.times(H, Pest),MathBase.transport(H)),r);
			// theKalman Gain
			double[][] K=MathBase.times(MathBase.times(Pest,H),MathBase.getInver(Sest));
			//thedifference between estimation and observation 
			double[][] v=MathBase.plus(MathBase.times(Zest,-1),MathBase.getCol(data,i),false);
			//theoptimal estimation of the state in time t 
			X=MathBase.plus(Xest, MathBase.times(K, v));
			//thecovariance of the optimal state  
			double[][] P=MathBase.times(MathBase.minus(MathBase.eye(StateParamNum, StateParamNum,1),(MathBase.times(K,H))),Pest);
			//theoptimal estimation of the observation in time t 
			Z=MathBase.times(H, X);
			for(int j=0;j<X.length;j++){
				Xf[j][i]=X[j][0];
				Zf[j][i]=Z[j][0];
				V[j][i]=v[j][0];
				Pf[j][i]=P[j][j];
			}
		}
	}
	
	public static void main(String[] args) {
		double[] Y=new double[]{3,5,7,6,9,5,9,12,16};
		double[] X=new double[]{1,2,3,4,5,6,7,8,9};
		KalmanFilter2 kalman=new KalmanFilter2(X,Y,100);
		kalman.exec();
		//最终的预测值
		MathBase.print(kalman.Zf);
	}
}
