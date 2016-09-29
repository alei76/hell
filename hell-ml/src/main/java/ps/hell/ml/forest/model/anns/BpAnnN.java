package ps.hell.ml.forest.model.anns;

import static com.googlecode.javacv.cpp.opencv_highgui.cvNamedWindow;
import static com.googlecode.javacv.cpp.opencv_highgui.cvShowImage;
import static com.googlecode.javacv.cpp.opencv_highgui.cvWaitKey;

import java.util.LinkedList;
import java.util.Random;
import java.util.TreeSet;
import java.util.Vector;

import ps.hell.math.base.MathBase;
import ps.hell.math.base.matrix.Matrix;
import ps.hell.ml.statistics.base.Normal;
import ps.hell.util.plot.opencv.BubbleGraph;

import com.googlecode.javacv.cpp.opencv_core;

/**
 * bp神经网络 支持多分类数据集 但输入变量不支持分类型
 * 多层网络（临时先不写）
 * 
 * @author Administrator
 *
 */
public class BpAnnN {

	/**
	 * 隐层变量个数
	 */
	public int implicitStrataNum;// 标记隐层变量数目

	/**
	 * 输出层有多少维度
	 */
	public int outputNum;// 输出层变量数//初始定义为2个 为 0-1
	/**
	 * 学习率
	 */
	public double alpha;// 标记初始学习率控制

	/**
	 * 冲量
	 */
	public double impulse;// 冲量1

	/**
	 * 输入维度
	 */
	public int dimensionality;// 输入层维度
	/**
	 * 样本量
	 */
	public int sampleCount = 0;// 存储样本量
	/**
	 * 对应隐层组
	 */
	public double[][] implicitStrataWeight;// 隐层
	/**
	 * 输出层
	 */
	public double[][] outputWeight;// 输出层

	/**
	 * 权重增量值
	 */
	public double[][] outputWeightDeltaOld;
	public double[][] outputWeightDeltaNew;
	/**
	 * 权重增量值
	 */
	public double[][] implicitStrataWeightDeltaOld;
	public double[][] implicitStrataWeightDeltaNew;
	/**
	 * 执行次数
	 */
	public int count = 0;// 记录bp执行次数
	/**
	 * 记录文件执行次数
	 */
	public int dataCount = 0;// 记录文件执行次数
	/**
	 * 输出值存储
	 */
	public double[] o_val;// 存储输出值
	/**
	 * 样本总量的总误差
	 */
	public double allError = 0.0;

	/**
	 * 设置总误差
	 * 
	 * @param value
	 */
	public void setAllError(double value) {
		this.allError = value;
	}

	/**
	 * 误差
	 */
	public double[][] y_error, y_error_st;// 记录两个对应的误差
	/**
	 * 对应的每行样本的总误差
	 */
	public double[] o_error, o_error_st;
	/**
	 * y计算值 及o计算值
	 */
	public double[] y_comp;
	/**
	 * 网络输出值
	 */
	public double[] o_comp;
	/**
	 * y阈值
	 */
	public double[] y_dim;
	/**
	 * o阈值
	 */
	public double[] o_dim;// 存储阈值
	/**
	 * 输入每条数据包含输出
	 */
	public double[] inputDate;// 输入数据
	// 标准化过程
	/**
	 * 每列最大值
	 */
	public double[] mean;// 分别存储每一列的最大值
	/**
	 * 每列最小值
	 */
	public double[] sd;// 分别存储每一列的最小值

	/**
	 * 标准的分类形式 如只有 n个分类 则存储n个
	 */
	public String[] outputStandardString;
	/**
	 * 存储对应标准分类的n个分类的的编译01二进制编码
	 */
	public LinkedList<int[]> outputStandardValue = new LinkedList<int[]>();

	/**
	 * 输出变量对应的string值
	 */
	public LinkedList<String> outputNormalString = new LinkedList<String>();
	/**
	 * 存储对应的编译01数据
	 */
	public LinkedList<int[]> outputNormalValue = new LinkedList<int[]>();
	// a表示隐层变量数量，alph，dlph都为初始学习率，c表示输出层变量数，现先定义为01两个变量,con表示每个样本有几个变量

	public String[] tempTest;

	/**
	 * 初始化隐层矩阵
	 * 
	 * @param a
	 *            implicitStrataNum //标记隐层变量数目
	 * @param alph
	 *            alph;//标记初始学习率控制学习率
	 * @param dlph
	 *            dlph;//初始学习率控制冲量学习率
	 * @param chognliang11
	 *            impulse;//冲量1
	 * @param chognliang12
	 *            impulse2;//冲量2
	 * @param c
	 *            this.outputNum;//输出层变量数//初始定义为1个 为 0-1
	 * @param con
	 *            dimensionality;//输入层维度
	 * @param fileLength
	 *            文件长度
	 */
	public BpAnnN(int a, double alph, double chognliang11, int c, int con,
			int filelength) {
		// 初始化学习率
		this.implicitStrataNum = a;// 存储隐层变量数
		this.outputNum = c;// 存储输出量
		this.dimensionality = con;// 存储维度
		this.alpha = alph;
		this.impulse = chognliang11;
		this.sampleCount = filelength;
		this.implicitStrataWeight = new double[this.implicitStrataNum][this.dimensionality];
		this.implicitStrataWeightDeltaOld = new double[this.implicitStrataNum][this.dimensionality];
		this.implicitStrataWeightDeltaNew = new double[this.implicitStrataNum][this.dimensionality];
		this.outputWeight = new double[this.outputNum][this.implicitStrataNum];
		this.outputWeightDeltaOld = new double[this.outputNum][this.implicitStrataNum];
		this.outputWeightDeltaNew = new double[this.outputNum][this.implicitStrataNum];
		this.o_dim = new double[this.outputNum];
		this.o_val = new double[this.outputNum];
		this.y_comp = new double[this.implicitStrataNum];
		this.o_comp = new double[this.outputNum];
		this.y_error = new double[this.outputNum][this.implicitStrataNum];
		this.y_dim = new double[this.implicitStrataNum];
		this.y_error_st = new double[this.outputNum][this.implicitStrataNum];
		this.o_error = new double[this.outputNum];
		this.o_error_st = new double[this.outputNum];
		// 初始化隐层权值
		Random random = new Random();
		for (int i = 0; i < this.outputNum; i++) {
			this.o_error[i] = 0.0;
			this.o_val[i] = 0.0;
			this.o_dim[i] = random.nextInt() % 10000.0 / 10000.0;
		}
		for (int l = 0; l < this.outputNum; l++) {
			for (int i = 0; i < this.implicitStrataNum; i++) {
				this.y_dim[i] = (random.nextInt() % 10000.0 / 10000.0 + 0.005);
				for (int j = 0; j < this.dimensionality; j++) {
					implicitStrataWeight[i][j] = (random.nextInt() % 100000.0 / 1000000.0 * 1.0);
				}
				outputWeight[l][i] = (random.nextInt() % 100000.0 / 1000000.0);
			}
		}
	}

	/**
	 * 初始化隐层矩阵
	 * 
	 * @param a
	 *            implicitStrataNum //标记隐层变量数目
	 * @param alph
	 *            alph;//标记初始学习率控制学习率
	 * @param dlph
	 *            dlph;//初始学习率控制冲量学习率
	 * @param chognliang11
	 *            impulse;//冲量1
	 * @param chognliang12
	 *            impulse2;//冲量2
	 * @param con
	 *            dimensionality;//输入层维度
	 */
	public BpAnnN(int a, double alph, double chognliang11, int con) {
		// 初始化学习率
		this.implicitStrataNum = a;// 存储隐层变量数
		this.dimensionality = con;// 存储维度
		this.alpha = alph;
		this.impulse = chognliang11;
	}

	/**
	 * 标记文件执行次数
	 */
	public void dataCount() {
		this.dataCount++;
	}

	public int getDataCount() {
		return dataCount;
	}

	public void setDataCount(int dataCount) {
		this.dataCount = dataCount;
	}

	public double[] getMean() {
		return mean;
	}

	public void setMean(double[] mean) {
		this.mean = mean.clone();
	}

	public double[] getSd() {
		return sd;
	}

	public void setSd(double[] sd) {
		this.sd = sd.clone();
	}

	public double[] getinputDate() {
		return inputDate;
	}

	/**
	 * 计算隐层
	 * 
	 * @param 为
	 *            样本中的第几行
	 * @param a
	 *            输入的一条数据//包括output
	 */
	public void implicitStrataCompute(double[] a) {
		inputDate = new double[a.length];
		for (int i = 0; i < a.length; i++) {
			inputDate[i] = a[i];
		}
		this.count++;// 添加迭代的第几次
		for (int i = 0; i < this.outputNum; i++) {
			this.o_val[i] = inputDate[this.dimensionality + i];
		}
		for (int i = 0; i < this.implicitStrataNum; i++) {
			y_comp[i] = 0.0;
			for (int j = 0; j < this.dimensionality; j++) {
				y_comp[i] += implicitStrataWeight[i][j] * inputDate[j];
			}
			y_comp[i] += y_dim[i];// 减阈值
			y_comp[i] = sigmoid(y_comp[i]);// sigma
		}
	}

	/**
	 * 计算输出层
	 * 
	 * @param index
	 *            样本第几行
	 */
	public void outputCompute() {
		for (int i = 0; i < this.outputNum; i++) {
			o_error[i] = 0.0;
			o_error_st[i] = 0.0;
			o_comp[i] = 0.0;
			for (int j = 0; j < this.implicitStrataNum; j++) {

				o_comp[i] += this.outputWeight[i][j] * y_comp[j];
			}
			o_comp[i] += o_dim[i];
			o_comp[i] = sigmoid(o_comp[i]);// 实际减误差
			o_error[i] = (o_val[i] - o_comp[i]) * (o_val[i] - o_comp[i]);
			o_error_st[i] = (o_val[i] - o_comp[i]) * o_comp[i]
					* (1.0 - o_comp[i]);
			allError += o_error[i];
		}
		// 添加误差
	}

	public double sigmoid(double val) {
		return 1d / (1d + Math.exp(-val));
	}

	/**
	 * 预测 计算隐层
	 * 
	 * @param index
	 *            样本的第几行
	 * @param a
	 *            输入的一条数据
	 */
	public void implicitStrataTest(double[] a) {
		for (int i = 0; i < this.implicitStrataNum; i++) {
			y_comp[i] = 0.0;
			for (int j = 0; j < this.dimensionality; j++) {
				y_comp[i] += implicitStrataWeight[i][j] * a[j];
			}
			y_comp[i] += y_dim[i];// 减阈值
			y_comp[i] = sigmoid(y_comp[i]);// sigma
		}
	}

	/**
	 * 预测 计算输出层
	 */
	public String outputTest() {
		String ll = "";
		for (int i = 0; i < this.outputNum; i++) {
			o_comp[i] = 0.0;
			for (int j = 0; j < this.implicitStrataNum; j++) {
				o_comp[i] += this.outputWeight[i][j] * y_comp[j];
			}
			o_comp[i] += o_dim[i];
			o_comp[i] = sigmoid(o_comp[i]);
		}
		// System.out.println("计算值:"+o_comp[0]+"\t"+o_dim[0]);
		boolean flag = false;
		for (int i = 0; i < outputStandardString.length; i++) {
			int lk = 0;
			for (int l = 0; l < this.outputStandardValue.get(i).length; l++) {
				// http://www.ujmp.org/
				if (o_comp[l] > 0.5 && outputStandardValue.get(i)[l] == 1) {
					lk++;
				} else if (o_comp[l] < 0.5
						&& outputStandardValue.get(i)[l] == 0) {
					lk++;
				} else {
					continue;
				}
			}
			if (lk == this.outputStandardValue.get(i).length) {
				flag = true;
				ll = this.outputStandardString[i];
				break;
			}
		}
		if (flag == false) {
			ll = "-2";
		}
		// 打印匹配值
		return ll;
	}

	/**
	 * 将梯度误差反向传递到隐层
	 */
	public void implicitStrataTransError(int index) {
		for (int j = 0; j < this.outputNum; j++) {
			for (int i = 0; i < this.implicitStrataNum; i++) {
				double m1 = o_error_st[j] * this.outputWeight[j][i];
				y_error_st[j][i] = m1 * y_comp[i] * (1.0 - y_comp[i]);
			}
		}

	}

	/**
	 * 输出层权重变化
	 */
	public void outputWeightChange() {
		for (int i = 0; i < this.outputNum; i++) {
			for (int j = 0; j < this.implicitStrataNum; j++) {
				this.outputWeightDeltaNew[i][j] = this.alpha * o_error_st[i]
						* y_comp[j];
				if (count == 1) {// 如果是第一次则不加入冲量
					this.outputWeight[i][j] += this.outputWeightDeltaNew[i][j];
					this.outputWeightDeltaOld[i][j] = this.outputWeightDeltaNew[i][j];
				} else {// 否则添加冲量
					this.outputWeight[i][j] += (1 - impulse)
							* this.outputWeightDeltaNew[i][j] + impulse
							* this.outputWeightDeltaOld[i][j];
					this.outputWeightDeltaOld[i][j] = this.outputWeightDeltaNew[i][j];
				}

			}
			o_dim[i] -= this.alpha * o_error_st[i];
		}
	}

	/**
	 * 隐层权重变化
	 */
	public void implicitStrataWeightChange() {

		for (int i = 0; i < this.implicitStrataNum; i++) {
			for (int j = 0; j < this.dimensionality; j++) {
				this.implicitStrataWeightDeltaNew[i][j] = 0.0;
				for (int l = 0; l < this.outputNum; l++) {
					this.implicitStrataWeightDeltaNew[i][j] = this.alpha
							* y_error_st[l][i] * inputDate[j];
					y_dim[i] += this.alpha * y_error_st[l][i];
				}
				if (count == 1) {// 如果是第一次则不加入冲量
					this.implicitStrataWeight[i][j] += this.implicitStrataWeightDeltaNew[i][j];
					this.implicitStrataWeightDeltaOld[i][j] = this.implicitStrataWeightDeltaNew[i][j];
				} else {// 否则添加冲量
					this.implicitStrataWeight[i][j] += (1 - impulse)
							* this.implicitStrataWeightDeltaNew[i][j] + impulse
							* this.implicitStrataWeightDeltaOld[i][j];
					this.implicitStrataWeightDeltaOld[i][j] = this.implicitStrataWeightDeltaNew[i][j];
				}

			}
		}
	}

	/**
	 * 学习率的变化 flag 如果为true 则执行 h =h +a×(Ep(n)- Ep(n-1))/ Ep(n) a为调整步长，0~1之间取值
	 * 修改方法 如果为false则为曲线方法
	 * 
	 * @param yita
	 *            为步长
	 * @param epn
	 *            当前误差
	 * @param epn1
	 *            上次误差
	 */
	public void changeYita(boolean flag, double yita, double epn, double epn1) {
		if (flag) {
			if (Double.compare(epn, 0.0) == 0) {
			} else if (Double.compare(epn1, 0.0) == 0) {
			} else {
				this.alpha += yita * (epn - epn1) / epn;
				if (alpha > 1)
					alpha = 1d;
				else if (alpha < 0.1)
					alpha = 0.1;
			}
		} else {
			if (count % 8 == 0)// 每8次调整一次学习率
			{
				if (this.alpha > 0.15) {
					this.alpha = this.alpha * 0.92;
				} else {
					this.alpha = 0.3;
				}

				this.impulse = this.impulse * 0.91;
			} else {

				double temp = Math.exp(Math.log(0.93) / 0.91) * 1.002;
				this.alpha = this.alpha * temp;
				this.impulse = this.impulse * temp;

			}
		}
	}

	/**
	 * 数据标准化 data为输入数据
	 */
	public double[] normalizationInput(double[] data) {
		double[] data1 = new double[data.length];
		if (data.length < 1) {
			System.out.println("数据不存在，不可标准化");
			System.exit(1);
		}
		for (int i = 0; i < data.length; i++) {
			data1[i] = (data[i] - mean[i]) / (sd[i]);
		}
		return data1;
	}

	/**
	 * 数据标准化 data为数据 outputNum为输出数据的列数 length 开始索引号 f 是否为数值型
	 */
	public double[] normalizationOutput(String[] data, boolean f) {
		LinkedList<Double> data1 = new LinkedList<Double>();
		if (data.length < 1) {
			System.out.println("数据不存在，不可标准化");
			System.exit(1);
		}
		int kn = 0;
		int knindex = 0;
		for (int i = 0; i < data.length; i++) {
			kn = knindex;
			if (f) {
				data1.add((Double.parseDouble(data[i]) - mean[this.dimensionality
						+ i])
						/ sd[this.dimensionality + i]);
			} else {
				// 转化为标准形
				double[] ll = null;
				for (int j = 0; j < this.outputStandardString.length; j++) {
					if (this.outputStandardString[j].equals(data[i])) {
						int[] lln = this.outputStandardValue.get(i);
						ll = new double[lln.length];
						for (int p = 0; p < lln.length; p++) {
							kn++;
							ll[p] = lln[p] * 1.0;
						}
					}
				}
				for (double lnb : ll) {
					data1.add(lnb * 1.0);
				}
			}
		}
		double[] na = new double[data1.size()];
		int oq = 0;
		for (double zbd : data1) {
			na[oq] = zbd;
			oq++;
		}
		return na;
	}

	/**
	 * 输入读取并赋如mean,sd中
	 */
	public void normalizationDouble(LinkedList<double[]> data) {
		mean = new double[data.get(0).length];
		sd = new double[mean.length];
		for (int i = 0; i < data.get(0).length; i++) {
			double[] da = new double[data.size()];
			for (int j = 0; j < data.size(); j++) {
				da[j] = data.get(j)[i];
			}
			mean[i] = MathBase.mean(da);
			sd[i] = MathBase.std(da);
		}
	}

	/**
	 * 将输出数据转化到Standard的list表中的二进制数
	 * 
	 * @param data
	 */
	public void changeToBinal(LinkedList<String[]> data) {
		for (int j = 0; j < data.get(0).length; j++) {
			System.out.println("标准化列:" + j);
			TreeSet<String> str = new TreeSet<String>();
			for (int i = 0; i < data.size(); i++) {
				// 遍历每一列将列编码并存储对应的数量
				// 取出唯一的string
				str.add(data.get(i)[j]);
			}
			System.out.println("读取统计完成");
			// 将对应值转化为标准结构
			String[] lnmm = new String[str.size()];
			int pp = 0;
			for (String sn : str) {
				lnmm[pp] = sn;
				pp++;
			}
			pp = 0;
			outputStandardString = lnmm.clone();
			lnmm = null;
			// 将对应信息转化为01编码
			if (str.size() == 2) {
				int[] ll3 = new int[1];
				LinkedList<int[]> lnm = new LinkedList<int[]>();
				ll3[0] = 0;
				outputStandardValue.add(ll3);
				ll3 = new int[1];
				ll3[0] = 1;
				outputStandardValue.add(ll3);
			} else if (str.size() > 2) {
				// 将信息对应编译
				// 计算需要用到的长度
				int count = 0;
				double vale = str.size() * 1.0;
				while (true) {
					vale = vale / 2;
					count++;
					System.out.println("二进制编码长度：" + count + "\tll:" + vale);
					System.out.println(vale);
					if (vale <= 1.0) {
						break;
					}
				}
				System.out.println("跳出");
				for (int ii = 0; ii < str.size(); ii++) {
					int[] te = new int[count];
					// 用二进制替换
					String st = Integer.toString(ii, 2);
					// System.out.println("二进制:"+st);
					int len = st.length();
					int jjj = 0;
					for (int jj = 0; jj < count - len; jj++) {
						te[jjj] = 0;
						System.out.print(te[jjj] + "\t");
						jjj++;
					}
					for (int jj = 0; jj < len; jj++) {

						te[jjj] = Integer.parseInt(Character.toString(st
								.charAt(jj)));
						System.out.print(te[jjj] + "\t");
						jjj++;
					}
					System.out.println();
					outputStandardValue.add(te.clone());
				}
			} else {
				System.out.println("输出文件中有某一列为唯一值，不符合要求");
				System.exit(1);
			}
		}
	}

	/**
	 * 将输出信息转化到二进制数中
	 */
	public void changeOutputToBinal(LinkedList<String[]> data) {
		// 只适合一个输出列形式
		for (int i = 0; i < data.size(); i++) {
			String str = data.get(i)[0];
			for (int j = 0; j < this.outputStandardString.length; j++) {
				if (str.equals(outputStandardString[j])) {
					 System.out.print(i + "转二进制:" + str + "\t");
					 for (int m = 0; m <
					 this.outputStandardValue.get(j).length; m++) {
					 System.out.print(outputStandardValue.get(j)[m]);
					 }
					 System.out.println();
					this.outputNormalString.add(str);
					this.outputNormalValue.add(this.outputStandardValue.get(j));
				}
			}
		}
		System.out.println("转换结束");
	}

	/**
	 * 数据标准化输出 返回的是标准化输出变量结构 将多分类修改成01编码数据
	 */
	public void normalizationString(LinkedList<String[]> data) {
		if (data.size() < 1) {
			System.out.println("数据不存在，不可标准化");
		}
		// 将二进制数据存入 standard信息中
		changeToBinal(data);
		System.out.println("编码结束");
		this.outputNum = outputStandardValue.get(0).length;
		this.implicitStrataWeight = new double[this.implicitStrataNum][this.dimensionality];
		this.sampleCount = data.size();
		this.outputWeight = new double[this.outputNum][this.implicitStrataNum];
		this.o_dim = new double[this.outputNum];
		this.o_val = new double[this.outputNum];
		this.y_comp = new double[this.implicitStrataNum];
		this.o_comp = new double[this.outputNum];
		this.y_error = new double[this.outputNum][this.implicitStrataNum];
		this.o_error = new double[this.outputNum];
		this.y_dim = new double[this.implicitStrataNum];
		this.y_error_st = new double[this.outputNum][this.implicitStrataNum];
		this.o_error_st = new double[this.outputNum];
		this.implicitStrataWeightDeltaOld = new double[this.implicitStrataNum][this.dimensionality];
		this.implicitStrataWeightDeltaNew = new double[this.implicitStrataNum][this.dimensionality];
		this.outputWeightDeltaOld = new double[this.outputNum][this.implicitStrataNum];
		this.outputWeightDeltaNew = new double[this.outputNum][this.implicitStrataNum];
		// 初始化隐层权值
		Random random = new Random();
		for (int i = 0; i < this.outputNum; i++) {
			this.o_error_st[i] = random.nextInt() % 10000.0 / 10000.0;
			this.o_dim[i] = random.nextInt() % 10000.0 / 10000.0;
		}
		System.out.println("初始化:");
		for (int l = 0; l < this.outputNum; l++) {
			for (int i = 0; i < this.implicitStrataNum; i++) {
				this.y_dim[i] = (random.nextInt() % 10000.0 / 10000.0 + 0.005);
				for (int j = 0; j < this.dimensionality; j++) {
					implicitStrataWeight[i][j] = (random.nextInt() % 100000.0 / 100000.0);
				}
				outputWeight[l][i] = (random.nextInt() % 100000.0 / 100000.0);
				System.out.print(outputWeight[l][i] + "\t");
			}
			System.out.println();
		}
		System.out.println("初始化成功");
	}

	/**
	 * 查看误差是否终止
	 * 
	 * @return
	 */
	public boolean toStop(double err) {
		if (this.allError < err) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 设置样本集总误差
	 * 
	 */
	public void setAllError() {
		allError = 0.0;
	}

	/**
	 * 获取样本集总误差
	 * 
	 */
	public double getAllError() {
		return allError;
	}

	/**
	 * 调用训练集过程
	 * 
	 * @param args
	 *            data为标准化后的数据 error 规定output的内容对比误差下线 errorRate 错误率 countLimit
	 *            为规定的循环次数上限
	 */
	public void trainDataAll(LinkedList<double[]> inputdata,
			LinkedList<String[]> outputdata, double error, double errorRate,
			int countLimit) {
		System.out.println("训练开始");
		tempTest = new String[inputdata.size()];
		LinkedList<double[]> inputAll = new LinkedList<double[]>();
		LinkedList<int[]> outputAll = new LinkedList<int[]>();
		for (int i = 0; i < inputdata.size(); i++) {
			double[] input1 = normalizationInput(inputdata.get(i));
			inputAll.add(input1);
		}
		// 将对应的信息存入normal中
		changeOutputToBinal(outputdata);
		outputAll = this.outputNormalValue;
		 System.out.println("进入迭代训练");
		while (true) {
			double epn1 = getAllError();
			setAllError();
			// 调用执行次数统计
			if (getDataCount() == countLimit) {
				break;
			}
			if (countLimit == getDataCount()) {
				System.out.println("训练达到规定上限");
				break;
			}
			// 添加迭代次数
			dataCount();
			int count_a = 0;// 记录错误数量
			int count_b = 0;
			for (int i = 0; i < inputdata.size(); i++) {
				//System.out.println("i:"+i);
				count_b++;
				double[] input = inputAll.get(i);
				int[] output = outputAll.get(i);
				// System.out.println("长度:"+inputdata.get(i)+"\t"+outputdata.get(i));
				// System.out.println("转码:"+inputdata.get(i)+"\t"+outputdata.get(i));
				double[] data1 = new double[input.length + output.length];
				for (int j = 0; j < input.length; j++) {
					data1[j] = input[j];
				}
				for (int j = input.length; j < data1.length; j++) {
					data1[j] = output[j - input.length];
				}
				// 计算隐层
				implicitStrataCompute(data1);
				// 计算输出层
				outputCompute();
				implicitStrataTransError(i);
				boolean flag = false;
				/*******************/
				String ll = "";
				for (int m = 0; m < outputStandardString.length; m++) {
					int lk = 0;
					for (int l = 0; l < this.outputStandardValue.get(m).length; l++) {
						// http://www.ujmp.org/
						if (o_comp[l] > 0.5
								&& outputStandardValue.get(m)[l] == 1) {
							lk++;
						} else if (o_comp[l] < 0.5
								&& outputStandardValue.get(m)[l] == 0) {
							lk++;
						} else {
							continue;
						}
					}
					if (lk == this.outputStandardValue.get(m).length) {
						flag = true;
						ll = this.outputStandardString[m];
						break;
					}
				}
				if (flag == false) {
					ll = "-2";
				}
				tempTest[i] = ll;
				for (int j = 0; j < output.length; j++) {
					if (o_comp[j] < 0.5) {
						if (output[j] < 0.5) {
							// System.out.println(i+"最终计算值:"+o_comp[j]+"\t"+output[j]+"正确");
						} else {
							// System.out.println(i+"最终计算值:"+o_comp[j]+"\t"+output[j]+"错误");
							count_a++;
							flag = true;
							break;
						}
					} else {
						if (output[j] > 0.5) {
							// System.out.println(i+"最终计算值:"+o_comp[j]+"\t"+output[j]+"正确");
							// System.out.println("最终计算值:"+o_comp[this.outputNum-1]+"\t"+data1[this.dimensionality]+"正确");
						} else {
							// System.out.println(i+"最终计算值:"+o_comp[j]+"\t"+output[j]+"错误");
							count_a++;
							flag = true;
							break;
						}
					}
				}
				outputWeightChange();
				implicitStrataWeightChange();
			}

			// 每一轮修改一次yita值
			changeYita(true, 0.3, getAllError(), epn1);// 调整学习率
			if (toStop(error) == true) {
				System.out.println("达到误差下限：训练次数为" + getDataCount() + ":误差为："
						+ getAllError());
				// System.out.println("上次误差:"+epn1+"\t本次误差:"+error_all.get(0));
				break;
			} else {
				System.out.println("训练次数:" + getDataCount() + "误差为:"
						+ getAllError() + "正确率："
						+ (1 - (count_a * 1.0) / inputdata.size()) + "\t错误量:"
						+ count_a + "\t总数量" + count_b + "\tyita:" + this.alpha);
			}
			if ((count_a * 1.0) / inputdata.size() < errorRate) {
				System.out.println("达到限制错误率");
				break;
			}
		}
		System.out.println("训练结束");
	}

	/**
	 * 
	 * @param data
	 *            为01编码
	 * @return
	 */
	public String getPredictString(int[] data) {
		for (int l = 0; l < this.outputStandardValue.size(); l++) {
			boolean flag = true;
			for (int i = 0; i < outputStandardValue.get(l).length; i++) {
				// System.out.println(outputStandardValue.get(l)[i]+"\t"+data[i]);
				if (outputStandardValue.get(l)[i] == data[i]) {

				} else {
					flag = false;
					break;
				}
			}
			if (flag == false) {
				// System.out.println("跳出");
				continue;
			} else {
				return this.outputNormalString.get(l);
			}
		}

		return "-1";// 错误
	}

	/**
	 * 测试数据集 返回的为最终的显示分类
	 */
	public String[] predictList(LinkedList<double[]> data) {
		int count_a = 0;// 记录错误数量
		int len = this.outputStandardValue.get(1).length;
		int[] retu = new int[len];
		String[] re = new String[data.size()];
		for (double[] data1 : data) {
			implicitStrataTest(this.normalizationInput(data1));
			outputTest();
			// -------------------------------------------------------------------------需要修改以后
			int i = 0;
			for (int j = 0; j < len; j++) {
				if (o_comp[j] < 0.5) {
					retu[j] = 0;
				} else {
					retu[j] = 1;
				}
			}

			// 判断状态
			re[i] = getPredictString(retu);
			i++;
		}
		return re;
		// System.out.println("测试数据结束:误差差异为:"+getNowErrors()+"正确率："+(1-(count_a*1.0)/data.size()));
	}

	/**
	 * 测试单条数据
	 * 
	 * @param data
	 */
	public String predict(double[] data) {
		int count_a = 0;// 记录错误数量
		// System.out.print("输入值:");
		// for(int i=0;i<data.length;i++)
		// {
		// System.out.print(data[i]+"\t");
		// }
		// System.out.println();
		double[] data1 = this.normalizationInput(data);
		// System.out.print("标准化值:");
		// for(int i=0;i<data1.length;i++)
		// {
		// System.out.print(data1[i]+"\t");
		// }
		// System.out.println();
		int len = this.outputStandardValue.get(1).length;

		implicitStrataTest(data1);
		String re = outputTest();
		// -------------------------------------------------------------------------需要修改以后
		return re;

		// System.out.println("测试数据结束:误差差异为:"+getNowErrors()+"正确率："+(1-(count_a*1.0)/data.size()));

	}

	public static void main(String[] args) {
		System.out.println("读取数据");
//		LinkedList<String[]> data = MatrixReadSource
//				.ReadToLinkedListString("data/cluster01.txt");
//		LinkedList<double[]> datainput = new LinkedList<double[]>();// 输入
//		LinkedList<String[]> dataoutput = new LinkedList<String[]>();// 输出
//		for (int i = 0; i < data.size(); i++) {
//			double[] l = new double[2];
//			l[0] = Double.parseDouble(data.get(i)[0]);
//			l[1] = Double.parseDouble(data.get(i)[1]);
//			String[] n = new String[1];
//			n[0] = data.get(i)[2];
//			datainput.add(l);
//			dataoutput.add(n);
//		}
		Normal n1=new Normal(-3,1);
		Matrix m1=n1.random(50,2,0);
		
		Normal n2=new Normal(1,1);
		Matrix m2=n2.random(50,2,1);
		
		m1.addMatrix(m2);
		
		Normal n3=new Normal(3,1);
		Matrix m3=n3.random(50,2,2);
		
		m1.addMatrix(m3);
		
		 Normal n4=new Normal(-3,0.3);
		 Matrix m4=n4.random(100,1,3);
		 Normal n42=new Normal(3,0.4);
		 Matrix m42=n42.random(100,1);
		 m42.addColMatrix(m4);
		 m1.addMatrix(m42);
		 
		 
		 Normal n5=new Normal(7,0.3);
		 Matrix m5=n5.random(100,1,4);
		 Normal n52=new Normal(-7,0.4);
		 Matrix m52=n52.random(100,1);
		 m52.addColMatrix(m5);
		 m1.addMatrix(m52);
		 
		 Normal n6=new Normal(-6,0.3);
		 Matrix m6=n6.random(100,1,5);
		 Normal n62=new Normal(-2,0.4);
		 Matrix m62=n62.random(100,1);
		 m62.addColMatrix(m6);
		 m1.addMatrix(m62);
		 
		 System.out.println("m32:");
		
		 // Normal n4=new Normal(-3,0.3);
		 // Matrix m4=n4.random(30,2,4);
		 // m1.AddMatrix(m4);
		
		 m1=m1.randomMatrix(m1);
		 double[][] data=m1.getData();
		 LinkedList<double[]> datainput=new LinkedList<double[]>();//输入
		 LinkedList<String[]> dataoutput=new LinkedList<String[]>();//输出
		 for(int i=0;i<data.length;i++)
		 {
		 double[] da=new double[data[i].length-1];
		 for(int j=0;j<data[i].length-1;j++)
		 {
		 da[j]=data[i][j];
		 }
		 String[] st=new String[1];
		 st[0]=Integer.toString((int)(data[i][data[i].length-1]));
		 datainput.add(da);
		 dataoutput.add(st);
		 }
		// L=(m+n)1/2+c节点数量 控制 mn为输入和输出节点数量 c在1-10之间
		int y_len = 12;// 隐层变量数量
		int i_len = datainput.get(0).length;// 输入变量数量
		double error = 0.01;// 误差
		double alp = 0.3;// 学习率
		double impulse = 0.8;

		// 调用第二个初始化
		BpAnnN bp_test = new BpAnnN(y_len, alp, impulse, i_len);
		// 执行将输出标准化过程
		bp_test.normalizationString(dataoutput);
		bp_test.normalizationDouble(datainput);
		// if(true)
		// return;
		// //标准化

		// System.out.println("隐层width：");
		// for(int i=0;i<bp_test.getImplicitStrataNum();i++)
		// {
		// for(int j=0;j<bp_test.getDimensionality();j++)
		// {
		// System.out.print(bp_test.getImplicitStrataWeight()[i][j]+"\t");
		//
		// }
		// System.out.println();
		// }
		// System.out.println(bp_test.getImplicitStrataNum());

		System.out.println("训练数据");
		bp_test.trainDataAll(datainput, dataoutput, 0.05, 0.01, 1000);
		System.out.println("训练完毕完毕");
		// System.out.println("隐层weight：");
		// for(int i=0;i<bp_test.getImplicitStrataNum();i++)
		// {
		// for(int j=0;j<bp_test.getDimensionality();j++)
		// {
		// //cout<<bp_test.getImplicitStrataWeight()[i][j]<<"\t";
		// System.out.print(bp_test.getImplicitStrataWeight()[0][i][j]+"\t");
		// }
		// System.out.println();
		// }
		//
		//
		// System.out.println("输出层weight");
		// for(int i=0;i<bp_test.getOutputNum();i++)
		// {
		// for(int j=0;j<bp_test.getImplicitStrataNum();j++)
		// {
		// System.out.print(bp_test.getOutputWeight()[i][j]+"\t");
		// }
		// System.out.println();
		// }

		Vector<Double> x = new Vector<Double>();
		Vector<Double> y = new Vector<Double>();
		Vector<Integer> cluster = new Vector<Integer>();
		for (int i = 0; i < datainput.size(); i++) {
			x.add(datainput.get(i)[0]);
			y.add(datainput.get(i)[1]);

			cluster.add(Integer.parseInt(bp_test.tempTest[i]));

		}

		BubbleGraph graph = new BubbleGraph();
		graph.width = 300;
		graph.height = 200;

		graph.parentent_config();// 计算改变的所有全局参量
		// 初始化图片
		BubbleGraph.img_trans img = new BubbleGraph.img_trans();
		// main_type="scatter";
		img.img1 = opencv_core.cvCreateImage(
				opencv_core.cvSize(graph.width, graph.height), 8, 3);
		graph.main_type = "qipao2";

		if (graph.main_type == "scatter" || graph.main_type == "qipao"
				|| graph.main_type == "qipao2" || graph.main_type == "qipao3") {
			System.out.println("初始化图片");
			graph.plotScatter(img, x, y, graph.main_type, cluster, 0);
		}

		cvNamedWindow("train", 1);
		cvShowImage("train", img.img1);

		// 计算测试数据效果
		Vector<Double> x1 = new Vector<Double>();
		Vector<Double> y1 = new Vector<Double>();
		Vector<Integer> cluster1 = new Vector<Integer>();
		// for(int i=0;i<datainput.size();i++)
		// {
		// double[] ll=new double[2];
		// ll[0]=datainput.get(i)[0];
		// ll[1]=datainput.get(i)[1];
		// x1.add(ll[0]);
		// y1.add(ll[1]);
		// String predict= bp_test.predict(ll);
		// cluster1.add(Integer.parseInt(predict));
		// }
		for (int i = -10; i < 10; i++) {
			for (int j = -10; j < 10; j++) {
				double[] ll = new double[2];
				ll[0] = i * 1.0;
				ll[1] = j * 1.0;
				x1.add(ll[0]);
				y1.add(ll[1]);
				String predict = bp_test.predict(ll);
				cluster1.add(Integer.parseInt(predict));
			}
		}

		BubbleGraph graph1 = new BubbleGraph();
		graph1.width = 300;
		graph1.height = 200;

		graph1.parentent_config();// 计算改变的所有全局参量
		// 初始化图片
		BubbleGraph.img_trans img1 = new BubbleGraph.img_trans();
		// main_type="scatter";
		img1.img1 = opencv_core.cvCreateImage(
				opencv_core.cvSize(graph1.width, graph1.height), 8, 3);
		graph1.main_type = "qipao2";

		if (graph1.main_type == "scatter" || graph1.main_type == "qipao"
				|| graph1.main_type == "qipao2" || graph1.main_type == "qipao3") {
			System.out.println("初始化图片");
			graph1.plotScatter(img1, x1, y1, graph1.main_type, cluster1, 0);
		}

		cvNamedWindow("test", 1);
		cvShowImage("test", img1.img1);
		// cvWaitKey(0);

		Vector<Double> x3 = new Vector<Double>();
		Vector<Double> y3 = new Vector<Double>();
		Vector<Integer> cluster3 = new Vector<Integer>();
		for (int i = 0; i < datainput.size(); i++) {
			x3.add(datainput.get(i)[0]);
			y3.add(datainput.get(i)[1]);

			cluster3.add(Integer.parseInt(dataoutput.get(i)[0]));

		}

		BubbleGraph graph3 = new BubbleGraph();
		graph3.width = 300;
		graph3.height = 200;

		graph3.parentent_config();// 计算改变的所有全局参量
		// 初始化图片
		BubbleGraph.img_trans img3 = new BubbleGraph.img_trans();
		// main_type="scatter";
		img3.img1 = opencv_core.cvCreateImage(
				opencv_core.cvSize(graph3.width, graph3.height), 8, 3);
		graph3.main_type = "qipao2";

		if (graph3.main_type == "scatter" || graph3.main_type == "qipao"
				|| graph3.main_type == "qipao2" || graph3.main_type == "qipao3") {
			System.out.println("初始化图片");
			graph3.plotScatter(img3, x3, y3, graph3.main_type, cluster3, 0);
		}

		cvNamedWindow("source", 1);
		cvShowImage("source", img3.img1);
		cvWaitKey(0);

	}

}
