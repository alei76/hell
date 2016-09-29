package ps.hell.ml.statistics.regression;

import ps.hell.math.base.MathBase;
import ps.hell.math.base.matrix.Matrix;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 罗迪斯回归
 * 
 * @author Administrator #logistic二分类极大似然，newton下降
 */
public class LogisticRegression {
	/**
	 * 输入数据 其中输入数据和实际数据不同第一列为1用来存储非x的数据 其他列一样
	 */
	public ArrayList<double[]> inputData = new ArrayList<double[]>();
	/**
	 * 输出数据
	 */
	public int[] outputData;

	/**
	 * 截距//截距为r中的第一个
	 */
	public double intercept = 0.0;

	/**
	 * 斜率
	 */
	public double rate = 0.0;

	/**
	 * 维度
	 */
	public int dimensionality = 0;
	/**
	 * 文件长度
	 */
	public int filelength = 0;

	/**
	 * 存储斜率
	 */
	public double[] r;

	int iteratorCount = 10000;
	/**
	 * 循环次数
	 */
	public int computeCount = 0;

	/**
	 * 误差
	 */
	public double error = 0.0;

	/**
	 * #logistic是以将连续变量p=exp(ax)/(1+exp(ax))的方式将线性转化为logistic的方式进行和输出样本的预测
	 * #以极大似然估计做为媒介l(p)=prop(pi) 首选对a做似然函数的偏导 在通过newton下山法迭代a，直到满足了迭代条件或者迭代次数为止
	 * inputData 输入变量 outputData 输出变量其中 输出变量必须为0 1
	 */
	public LogisticRegression(ArrayList<double[]> inputData, int[] outputData) {
		try {
			dimensionality = inputData.get(0).length;
		} catch (Exception e) {
			System.out.println("输入数据错误");
			System.exit(1);
		}
		this.outputData = outputData.clone();
		if (true) {
			//默认为sgd
			this.inputData = inputData;
			filelength = inputData.size();
			// r = new double[dimensionality + 1];
		} else {
			//如果是 牛顿梯度
			for (int i = 0; i < inputData.size(); i++) {
				double[] temp = new double[dimensionality + 1];
				temp[0] = 1.0;
				for (int j = 0; j < dimensionality; j++) {
					temp[j + 1] = inputData.get(i)[j];
				}
				this.inputData.add(temp);
			}
			filelength = inputData.size();
			r = new double[dimensionality + 1];
		}
	}

	public double[] w = null;

	/**
	 * 输入为0和1
	 * 
	 * @return
	 */
	public boolean computeSGD() {
		error = 0;
		w = new double[this.inputData.get(0).length];
		for (int i = 0; i < w.length; i++) {
			w[i] = MathBase.random() * 2 - 1;
		}
		// 每一次处理
		double[] sum = new double[w.length];
		double var = 0;
		for (int i = 0; i < inputData.size(); i++) {
			// 计算
			var = (1 / (1 + MathBase.exp(-outputData[i]
					* MathBase.dot(w, inputData.get(i)))) - 1)
					* outputData[i];
			// error+=var;
			sum = MathBase.sum(sum, MathBase.times(inputData.get(i), var));
		}
		w = MathBase.minus(w, sum);
		return false;
	}

	public int predict(double[] val) {
		double pre = MathBase.dot(w, val);
		if (pre <= 0.5) {
			return 0;
		} else {
			return 1;
		}
	}

	/**
	 * 计算过程
	 */
	public boolean compute() {
		double[] expTemp = new double[filelength];
		// System.out.println("exp");
		for (int i = 0; i < filelength; i++) {
			double temp = 0.0;
			for (int j = 0; j < dimensionality + 1; j++) {
				temp += inputData.get(i)[j] * r[j];
			}
			expTemp[i] = Math.exp(temp);
			// System.out.print(expTemp[i]+"\t");
		}
		// System.out.println();

		// 存储 (y-exp)/(1+exp)
		double[][] expTemp2 = new double[filelength][dimensionality + 1];
		// System.out.println("exp2");
		for (int i = 0; i < dimensionality + 1; i++) {
			for (int j = 0; j < filelength; j++) {
				expTemp2[j][i] = (outputData[j] - expTemp[j] / (1 + expTemp[j]));
				// System.out.print(expTemp2[j][i]+"\t");
			}
			// System.out.println();
		}
		double[] f1 = new double[dimensionality + 1];
		// System.out.println("f1");
		for (int i = 0; i < dimensionality + 1; i++) {
			for (int j = 0; j < filelength; j++) {
				// x*exp*x
				f1[i] += expTemp2[j][i] * inputData.get(j)[i];
			}
			// System.out.print(f1[i]+"\t");
		}
		// System.out.println();
		// 牛顿负梯度
		double[][] expTempNew = new double[filelength][dimensionality + 1];
		for (int i = 0; i < dimensionality + 1; i++) {
			for (int j = 0; j < filelength; j++) {
				expTempNew[j][i] = expTemp[j];
			}
		}
		// b值的修改
		// f2=-apply(x*exp2*x,2,sum);#负梯度方向
		// b<-bold-lamd*f1/f2;
		double[] f2 = new double[dimensionality + 1];
		// System.out.println("f2");
		for (int i = 0; i < dimensionality + 1; i++) {
			for (int j = 0; j < filelength; j++) {
				// x*exp*x
				f2[i] -= inputData.get(j)[i] * expTempNew[j][i]
						* inputData.get(j)[i];
			}
			// System.out.print(f2[i]+"\t");
		}
		// System.out.println();
		// 修改b值
		// lamd=0.2;
		double[] rTemp = r.clone();
		for (int j = 0; j < filelength; j++) {
			for (int i = 0; i < dimensionality + 1; i++) {
				// if(f2[i]==0.0)
				// {
				// f2[i]=1E-30;
				// }
				r[i] = r[i] - (0.2) * f1[i] / f2[i];
			}
		}
		// System.out.println();
		error = 0.0;
		for (int i = 0; i < dimensionality + 1; i++) {
			error += Math.abs(r[i] - rTemp[i]);
		}
		if (error < 1E-3) {
			System.out.println("收敛");
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 调度过程
	 */
	public void run() {
		while (true) {
			computeCount++;
			System.out.println("执行次数:" + computeCount + "\t" + error);
			boolean fl = compute();
			if (fl == true) {
				break;
			}
			if (computeCount >= iteratorCount) {
				break;
			}
		}
	}

	/**
	 * 通过sgd方法执行
	 */
	public void runSGD() {
		while (true) {
			computeCount++;
			System.out.println("执行次数:" + computeCount + "\t" + error);
			boolean fl = computeSGD();
			if (fl == true) {
				break;
			}
			if (computeCount >= iteratorCount) {
				break;
			}
		}
	}

	public static void main(String[] args) {
		Matrix input1 = new Matrix(11, 1);
		Matrix input2 = new Matrix(11, 1);

		double[] x1 = { -3.0, -2.0, -1.0, 0.0, 1.0, 2.0, 3.0, -5.0, -2.0, -3.0,
				-4.0 };
		double[] x2 = { -2.0, -1.0, -2.0, 0.0, 1.3, 2.0, 4.0, -4.0, -2.0, -3.0,
				-2.0 };
		ArrayList<double[]> x = new ArrayList<double[]>();
		for (int i = 0; i < 11; i++) {
			double[] xtemp = new double[2];
			xtemp[0] = x1[i];
			xtemp[1] = x2[i];
			x.add(xtemp);
		}

		int[] y = { 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0 };
		LogisticRegression logis = new LogisticRegression(x, y);
		// logis.run();
		logis.runSGD();
		System.out.println(Arrays.toString(logis.w));

		for (int i = 0; i < y.length; i++) {
			System.out.println("pre:" + logis.predict(x.get(i)) + "\ttarget:"
					+ y[i]);
		}
	}

}
