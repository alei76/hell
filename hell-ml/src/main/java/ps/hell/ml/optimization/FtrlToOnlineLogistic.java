package ps.hell.ml.optimization;

import ps.hell.math.base.MathBase;

/**
 * 使用ftrl算法对logistic做处理
 * 
 * @author Administrator
 *
 */
public class FtrlToOnlineLogistic {

	/**
	 * 数据输入
	 */
	public double[][] input = null;
	/**
	 * 样本量
	 */
	int length = 0;
	/**
	 * 维度
	 */
	int dim = 0;
	/**
	 * 0-1
	 */
	public int[] target = null;
	/**
	 * 迭代次数
	 */
	public int iteratorCount = 30;
	/**
	 * 
	 */
	public double[] ni = null;

	public double[] zi = null;

	public double alpha;
	public double beta;

	public double lamdba1;
	public double lamdba2;

	public double[][] wti = null;
	
	public double[] w=null;
	
	public double error_old=0;
	public double error_new=0;
	
	public double[] deti=null;

	/**
	 * 如果 lamdba1=0 并且学习率为一个常数则就是 ogd算法，在线梯度下降
	 * @param input
	 * @param target
	 * @param alpha
	 * @param beta
	 * @param lamdba1
	 * @param lamdba2
	 */
	public FtrlToOnlineLogistic(double[][] input, int[] target, double alpha,
			double beta, double lamdba1, double lamdba2) {
		this.input = input;
		this.target = target;
		this.length = input.length;
		this.dim = input[0].length;
		ni = new double[dim];
		zi = new double[dim];
		deti=new double[dim];
		this.alpha = alpha;
		this.beta = beta;
		this.lamdba1 = lamdba1;
		this.lamdba2 = lamdba2;
		wti = new double[this.length][dim];
		w=new double[dim];
		
	}

	public void run() {

		for (int i = 0; i < iteratorCount; i++) {
			runOne();
			System.out.println("执行次数:"+i+"\t"+(error_new-error_old));
			error_old=error_new;
		}
	}

	public void runOne() {
		error_new=0;
		for (int n = 0; n < length; n++) {
			double[] wt = wti[n];
			// 获取
			for (int i = 0; i < dim; i++) {
//				if (Math.abs(zi[i]) <= lamdba1) {
//					wt[i] = 0;
//				} else {
					wt[i] = -(zi[i] - MathBase.sigmod(zi[i]) * lamdba1)
							/ ((beta + Math.sqrt(ni[i]) / alpha + lamdba2));
				//}
			}
			double pre= MathBase.dot(input[n], wt);
			error_new+=Math.abs(pre- target[n]);
			System.out.println("n:"+pre+"\t"+target[n]);
			for (int i = 0; i < dim; i++) {
				double gi = input[n][i] *( pre- target[n]);
				deti[i]=1/alpha*(Math.sqrt(ni[i]+gi*gi)-Math.sqrt(ni[i]));
				zi[i]=zi[i]+gi-deti[i]*wt[i];
				ni[i]=ni[i]+gi*gi;
			}
		}
	}
	/**
	 * 预测
	 * @param in
	 * @return
	 */
	public int predict(double[] in){
		double pre=MathBase.dot(in, wti[length-1]);
//		System.out.println(pre);
		if(pre<=0.5){
			return 0;
		}else{
			return 1;
		}
	}
	
	public static void main(String[] args) {
		double[] x1 = { -3.0, -2.0, -1.0, 0.0, 1.0, 2.0, 3.0, -5.0, -2.0, -3.0,
				-4.0 };
		double[] x2 = { -2.0, -1.0, -2.0, 0.0, 1.3, 2.0, 4.0, -4.0, -2.0, -3.0,
				-2.0 };
		double[][] x = new double[x1.length][2];
		for (int i = 0; i < 11; i++) {
			x[i][0]=x1[i];
			x[i][1]=x2[i];
		}

		int[] y = { 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0 };
		
		FtrlToOnlineLogistic tool=new FtrlToOnlineLogistic(x,y,-0.2,-0.15,1,2);
		tool.run();
		
		for(int i=0;i<y.length;i++){
			System.out.println("pre:"+tool.predict(x[i])+"\ttarget:"+y[i]);
		}
	}
}
