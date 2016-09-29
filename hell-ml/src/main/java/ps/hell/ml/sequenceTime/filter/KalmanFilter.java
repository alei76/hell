package ps.hell.ml.sequenceTime.filter;

import ps.hell.math.base.MathBase;

import java.util.Arrays;
import java.util.Random;

/**
 * 卡尔曼滤波器
 * 
 * @author Administrator
 *
 */
public class KalmanFilter {

	/**
	 * 数据源
	 */
	public double[] predict = null;
	/**
	 * 均值
	 */
	public double mean = 0d;
	/**
	 * 方差
	 */
	public double var = 0d;
	/**
	 * 标准差
	 */
	public double std = 0d;

	/**
	 * 数据大小
	 */
	public int size = 0;
	/**
	 * 迭代次数 延续k次
	 */
	public int N=2000;
	/**
	 * 系统参数 A(k)
	 */
	public double a=1d;
	
	public KalmanFilter(double[] data) {
		this.predict=data;
		N=data.length;
	}
	/**
	 * 卡尔曼 滤波后的数据
	 * 返回为 经过
	 * 如果是预测 效果不好，所以不考虑t+1时刻这种预测
	 */
	public double[] run()
	{
		double[] w = new double[N];// 过程噪声
		Random random = new Random();
		for (int i = 0; i < N; i++) {
			w[i] = random.nextGaussian();
		}
		double[] V = new double[N];// 测量噪声
		for (int i = 0; i < N; i++) {
			V[i] = random.nextGaussian();
		}
		double q1 = MathBase.std(V);
		double Rvv = q1 * q1;
		double q2 = MathBase.std(predict);
		double Rxx = q2 * q2;
		//测量噪声
		Rvv=Rxx;
		double q3 = MathBase.std(w);
		double Rww = q3 * q3;
		double h = 1;// H为方程中H(k),H是测量系统的参数
		double[] Z = new double[N]; // Z为测量值，如使用温度计测量得到
		for (int i = 0; i < N; i++) {
			Z[i] = h * predict[i] + V[i];
		}
		double[] p = new double[N];
		p[0] = 10;
		double[] s = new double[N];
		s[0] = predict[0];
		for (int t = 1; t < N; t++) {
			double p1 = a * a * p[t - 1] + Rww;// 预测结果的协方差 P1(k)=A*P(k-1)*A'+Q;        
			double kg = p1 * h / (h * p1 * h + Rvv);// Kg:卡尔曼增益（Kalman Gain） 其中第一个为h'  Kg(k)=P1(k)*H'/(H*P1(k)*H'+R);  
			s[t] = a * s[t - 1] + kg * (Z[t] - a * h * s[t - 1]);// 最优值
			p[t] = (1 - kg * h) * p1;// 当前最优值的结果的协方差
		}
		return s;
	}
	
	
	public void run2() {
		double[] w = new double[N];// 过程噪声
		Random random = new Random();
		for (int i = 0; i < N; i++) {
			w[i] = random.nextGaussian();
		}
		predict = new double[N];
		predict[0] = 25;// 真实值
		double a = 1d;// A(k)
		for (int i = 1; i < N; i++) {
			predict[i] = a * predict[i - 1] + w[i - 1];
		}
		double[] V = new double[N];// 测量噪声
		for (int i = 0; i < N; i++) {
			V[i] = random.nextGaussian();
		}
		double q1 = MathBase.std(V);
		double Rvv = q1 * q1;
		double q2 = MathBase.std(predict);
		double Rxx = q2 * q2;
		double q3 = MathBase.std(w);
		double Rww = q3 * q3;
		double h = 1;// H为方程中H(k),H是测量系统的参数
		double[] Z = new double[N]; // Z为测量值，如使用温度计测量得到
		for (int i = 0; i < N; i++) {
			Z[i] = h * predict[i] + V[i];
		}
		double[] p = new double[N];
		p[0] = 10;
		double[] s = new double[N];
		s[0] = 1;
		for (int t = 1; t < N; t++) {
			double p1 = a * a * p[t - 1] + Rww;// 预测结果的协方差 P1(k)=A*P(k-1)*A'+Q;        
			double kg = p1 * h / (h * p1 * h + Rvv);// Kg:卡尔曼增益（Kalman Gain） 其中第一个为h'  Kg(k)=P1(k)*H'/(H*P1(k)*H'+R);  
			s[t] = a * s[t - 1] + kg * (Z[t] - a * h * s[t - 1]);// 最优值
			p[t] = (1 - kg * h) * p1;// 当前最优值的结果的协方差
		}
		System.out.println(Arrays.toString(s));
		System.out.println(Arrays.toString(Z));
		System.out.println(Arrays.toString(predict));
	}

	public static void main(String[] args) {
		double[] value=new double[]{3,5,7,6,9,5,9,12,16};
		KalmanFilter filter = new KalmanFilter(value);
		//filter.run2();
		double[] val=filter.run();
		System.out.println(Arrays.toString(val));
		MathBase.printCol(value);
		MathBase.printCol(val);
	}
}
