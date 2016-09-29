package ps.hell.ml.groupAi.ga;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * 将Ga做成 又输入的多线程算法
 */
public class GA_Input implements Runnable {
	
	
	// #10进制转二进制
	// #x1<=12.1 x1>=-3
	// #x2<=5.8 x2>=4.1
	// #取出区间以精度.4
	// m1<-matrix(c(-3,4.1,12.1,5.8),2,2,1)#all.names()all.vars()
	/**
	 * 外部执行程序
	 */
	public OuterExecInter outerExec = null;

	public int threadIndex = -1;
	/**
	 * rangeValue 获取每行为一个x对应的最小最大值 列为2列分别为最小值和最大值
	 */
	public double[][] rangeValue;

	/**
	 * 持续次数
	 */
	public int computeCount = 0;
	/**
	 * 记录 x的数量
	 */
	public int rangeXCount;
	/**
	 * 标记x的名字
	 */
	public String[] x;

	/**
	 * 标记染色体的数量
	 */
	public int yCount = 0;
	/**
	 * 染色体组string编码
	 */
	public String[] yString;

	/**
	 * 存储染色体组对应的多个x的解码10进制数
	 */
	public double[][] yValue;

	public double oldStd = 0.0;
	public double newStd = 0.0;
	/**
	 * 存储染色体组的适应度
	 */
	public double[] yFitness;
	
	/**
	 * 最有模型编号
	 */
	public int[] yFitnessModel;
	/**
	 * 存储为轮盘时的累计概率
	 */
	public double[] yFitnessSum;

	/**
	 * 标记最大的位置
	 */
	public int yFitnessMaxIndex = 0;
	/**
	 * 游走的精度
	 */
	public int precision = 0;

	/**
	 * 存储x1，x2，x3对应的编译成2进制长度
	 */
	public int[] yStringLength;
	public int yStringLengthSum = 0;
	/**
	 * 对应的函数
	 */
	public String fx = "";
	/**
	 * 是否存在对应的函数，还是是最终的适应率
	 */
	public boolean isStringFunction = true;
	/**
	 * 交配概率
	 */
	public double matingP = 0.3;
	/**
	 * 交配 长度
	 */
	public int matingCount = 3;
	/**
	 * 交配类型 对应的是 single交配，还是 complex交配
	 */
	public String matingContext = "single";
	/**
	 * 突变概率
	 */
	public double mutationP = 0.1;
	/**
	 * 是否为去最大适应都
	 */
	public boolean isUpMax=true;

	/**
	 * 执行到的线程数量
	 */
	public int threadStepCount=0;
	/**
	 * 是否结束程序
	 */
	public boolean isBreak=false;
	/**
	 * 误差
	 */
	public double error=1E-4;
	/**
	 * 迭代数量
	 */
	public double iterCount=100;
	/**
	 * 
	 * @param x
	 *            为x1，x2，x3的标注
	 * @param rangeValue
	 *            对应行为x1的最小到最大值
	 * @param precision
	 *            需要的精度
	 * @param yCount
	 *            需要的染色体数量
	 * @param fx
	 *            需要执行的 max(fx) fx函数
	 * @param px
	 *            交配概率
	 * @param count
	 *            交配 长度
	 * @param context
	 *            对应的是 single交配，还是 complex交配
	 * @param py
	 *            突变概率
	 * @param isStringFunction
	 *            是否是字符串函数 还是输入的为一个字符串double值
	 * @param isUpMax 为是否为去最大值
	 * @param error 误差
	 * @param iterCount迭代次数
	 */
	public GA_Input(String[] x, double[][] rangeValue, int precision,
			int yCount, String fx, boolean isStringFunction, double matingP,
			int matingCount, String matingContext, double mutationP,boolean isUpMax,double error,int iterCount) {
		this.rangeXx(x, rangeValue, precision);
		this.random(yCount);
		//初始化
		initIndexP();
		this.error=error;
		this.iterCount=iterCount;
		this.fx = fx;
		this.isStringFunction = isStringFunction;
		this.matingP = matingP;
		this.matingCount = matingCount;
		this.matingContext = matingContext;
		this.mutationP = mutationP;
		this.isUpMax=isUpMax;
		tempCount=yCount;
	}

	/**
	 * rangeValue 获取每行为一个x对应的最小最大值 列为2列分别为最小值和最大值 precision 设定精度
	 */
	public void rangeXx(String[] x, double[][] rangeValue, int precision) {
		this.rangeValue = rangeValue.clone();
		this.x = x.clone();
		// 获取区间
		this.precision = precision;
	}

	/**
	 * 
	 * @param yCount
	 *            染色体数量
	 */
	public void random(int yCount) {
		this.yCount = yCount;
		Random random = new Random();

		// 需要将行个 组合起来
		this.yString = new String[yCount];
		yStringLength = new int[rangeValue.length];
		for (int i = 0; i < yCount; i++) {
			yString[i] = "";
		}
		this.yFitness = new double[yCount];
		this.yFitnessModel=new int[yCount];
		this.yFitnessSum = new double[yCount];
		this.rangeXCount = rangeValue.length;
		this.yValue = new double[yCount][this.rangeXCount];
		for (int i = 0; i < rangeValue.length; i++) {
			int temp = (int) ((rangeValue[i][1] - rangeValue[i][0]) * Math.pow(
					10, this.precision));
			BigInteger src = new BigInteger(Integer.toString(temp));// 转换为BigInteger类型
			yStringLength[i] = src.toString(2).length();
			for (int j = 0; j < yCount; j++) {
				String te = src.toString(2);
				StringBuilder strb = new StringBuilder(te);
				// 转化为2进制
				for (int m = 0; m < yStringLength[i]; m++) {
					if (random.nextDouble() > 0.5) {
						// 修改
						// te.substring(len,len+yStringLength[i]-1);
						if (te.charAt(m) == '0') {
							strb.replace(m, m + 1, "1");
						} else {
							strb.replace(m, m + 1, "0");
						}
					}
				}
				yString[j] = yString[j] + strb.toString();
				//System.out.println("yStinrg[" + j + "]" + "\t" + yString[j]);
			}
		}
		for (int i = 0; i < this.rangeXCount; i++) {
			yStringLengthSum += yStringLength[i];
		}
	}

	/**
	 * #评价个体适应度 解码 带入需要寻优的函数中 fx为函数并且为 取 fx的最大值
	 * 
	 * @param threadYIndex
	 *            为对应的染色体位置
	 * @param fx
	 *            为对应的函数
	 */
	public double evaluate(int threadYIndex, String fx) {
		String fx1 = fx;
		for (int j = 0; j < this.rangeXCount; j++) {
			// System.out.println(this.yValue[i][j]);
			fx1 = fx1.replaceAll(this.x[j],
					Double.toString(this.yValue[threadYIndex][j]));
			fx1 = fx1.replace("pi", Double.toString(Math.PI));
		}
		// System.out.println(fx1);
		// System.exit(1);
		return StringToFunction.evaluateDecode(fx1);
	}

	/**
	 * 返回方差是否有效
	 * @param out 误差
	 */
	public boolean isStdOk(double error) {
		double sum = 0.0;
		ArrayList<Double> temp=new ArrayList<Double>();
		for(double d:yFitness)
		{
			temp.add(d);
		}
		Collections.sort(temp);
		int size=yCount*3/4;
		for (int i = 0; i < size; i++) {
			sum += temp.get(i);
		}
		sum /= size;
		double std = 0.0;
		for (int i = 0; i < size; i++) {
			// System.out.println(yFitness[i]+"\t"+sum+"\t"+(yCount-1));
			std += Math.pow(temp.get(i) - sum, 2.0);
		}
		std=Math.sqrt(std);
		std/= (yCount - 1);
		this.newStd = std;
		System.out.println("oldStd:" + oldStd + "\tnewStd:" + newStd);
		if (Math.abs(this.newStd - oldStd) < error) {
			oldStd = newStd;
			return true;
		} else {
			oldStd = newStd;
			return false;
		}
	}

	/**
	 * 将2进制转化为10进制 str为2进制字符串 index为对应所属的行
	 * 
	 * @param indexYCount
	 *            对某一个数据值做indexYCount
	 */
	public void changeTo10(int indexYCount) {
		for (int j = 0; j < this.rangeXCount; j++) {
			if (j == 0) {

				BigInteger src1 = new BigInteger(
						yString[indexYCount]
								.substring(0, this.yStringLength[j]),
						2);
				this.yValue[indexYCount][j] = this.rangeValue[j][1]
						+ src1.intValue() / Math.pow(10, this.precision);
			} else {
				// System.out.print(rangeXCount+"\t"+this.yStringLength[j-1]+"\t"+this.yStringLength[j]);
				BigInteger src1 = new BigInteger(
						yString[indexYCount].substring(
								this.yStringLength[j - 1],
								this.yStringLength[j - 1]
										+ this.yStringLength[j]), 2);
				this.yValue[indexYCount][j] = this.rangeValue[j][1]
						+ src1.intValue() / Math.pow(10, this.precision);
			}
		}

	}

	/**
	 * 计算每个染色体被复制累计概率
	 * @param flag 如果为true则为获取max
	 * 如果为false 则为活去min
	 * @return true表示继续 false表示结束
	 */
	public boolean sumP(boolean flag) {
		if(yFitness.length==1)
		{
			yFitnessMaxIndex=0;
			return false;
		}
		double temp = 0.0;
		double te = Double.MIN_VALUE;
		int index = -1;
		// 因存在负数所以调整为标准化
		Double min = Double.MAX_VALUE;
		Double max = Double.MIN_VALUE;
		
		for (int i = 0; i < yCount; i++) {
		//	System.out.println(i+"\t"+yFitness[i]);
			if (yFitness[i] < min) {
				min = yFitness[i];
			}
			if (yFitness[i] > max) {
				max = yFitness[i];
			}
		}
		//如果全为一样的数
		if(max-min<1E-10)
		{
//			for(int i=0;i<yFitness.length-1;i++)
//			{
//				yFitnessSum[i]=i*(1.0/yFitness.length);
//			}
//			yFitnessSum[yFitness.length-1]=1;
			return false;
		}
		//System.out.println("max:" + max + "\tmin:" + min);
		if(flag)
		{
			for (int i = 0; i < yCount; i++) {
				temp += (yFitness[i] - min) ;/// (max - min);
				if (yFitness[i] > te) {
					te = yFitness[i];
					index = i;
				}
			}
		}else{
			//如果为获取最小值 则调整为反向值
			for (int i = 0; i < yCount; i++) {
				temp += (max-yFitness[i]) ;/// (min-max);
				if ((max-yFitness[i]) > te) {
					te = max-yFitness[i];
					index = i;
				}
			}
		}
		yFitnessMaxIndex = index;
		// 计算比例
		for (int i = 0; i < yCount; i++) {
			if (i == 0) {
				yFitnessSum[i] = (yFitness[i]-min) / temp;
			} else if (i == yCount - 1) {
				yFitnessSum[i] = 1.0;
			} else {
				yFitnessSum[i] = yFitnessSum[i - 1] + (yFitness[i]-min) / temp;
			}
		}
		return true;
		//[0.12478520238094734, 0.23801070187401074, 0.23801070187401074, 0.5645308408910288, 0.9912319336440902, 1.0]
	}

	/**
	 * 轮盘选择 轮盘的作用是竞争机制 其中轮盘的最大值一定会被选择
	 */
	public void coronaSelect() {
		// 轮盘的作用是竞争机制
		Random random = new Random();
		String[] temp = new String[yCount];
		for (int i = 0; i < yCount; i++) {
			if (i == 0) {
				// 设定最高的一定会被选上1次
				temp[i] = this.yString[yFitnessMaxIndex];
				continue;
			}
			double te = random.nextDouble();
			for (int j = 0; j < yCount; j++) {
				if (te <= yFitnessSum[j]) {
					temp[i] = this.yString[j];
					break;
				} else {

				}
			}
		}
		this.yString=temp;
	}

	/**
	 * 执行交配 p 为交配概率 count 执行次数 count次数最好少于yCount/2个 context 为交配方式
	 * single为简单交配，complex为复杂交配 其中也考虑竞争机制 越低的交配概率越低，但最低也会是p/2
	 * 种群交配初始化交配概率#规定交配和突变种群中最大值不参与 一般交配概率为0.6-1
	 */
	public void mating(double p, int count, String context) {
		Random random = new Random();
		for (int i = 0; i < yCount/2; i++) {
			// 判断是否交配
			if (p < random.nextDouble()) {
				continue;
			}
			int int1 = -1;
			int int2 = -1;
			while (true) {
				double te = random.nextDouble();
				boolean flag = false;
				for (int j = 0; j < yCount; j++) {
					if (te < this.yFitnessSum[j]) {
						if (j != this.yFitnessMaxIndex) {
							flag = true;
							int1 = j;
							break;
						}
					} else {
						continue;
					}
				}
				if (flag == true) {
					break;
				}
			}
			if(int1==-1)
			{
				return;
			}
			while (true) {
				double te2 = random.nextDouble();
				boolean flag = false;
				for (int j = 0; j < yCount; j++) {
					if (te2 < this.yFitnessSum[j]) {
						if (j != this.yFitnessMaxIndex&& j!=int1) {
							flag = true;
							int2 = j;
							break;
						}
					}
				}
				if (flag == true) {
					break;
				}
			}
				if(int2==-1)
				{
					return;
				}
				String str1=this.yString[int1];
				String str2=this.yString[int2];
			if (context == "single") {
				int ll = (int) Math.floor(random.nextDouble()
						* this.yStringLengthSum);

				this.yString[int1] = str1.substring(0, ll)
						+ str2.substring(ll);
				this.yString[int2] = str2.substring(0, ll)
						+ str1.substring(ll);
			} else if (context == "complex") {
				// 将内容给分配为几段
				for (int m = 0; m < Math.abs(random.nextInt()) % (count*1.5>yStringLengthSum?yStringLengthSum:count*1.5); m++) {
					// 标记位置
					int ll = (int) Math.floor(random.nextDouble()
							* this.yStringLengthSum);
					// 每一段长度
					int len = Math.abs(random.nextInt() % count)+1;
					int len1 = ll + len > this.yStringLengthSum ? yStringLengthSum
							: ll + len;
					//System.out.println();
					this.yString[int1] = str1.substring(0, ll)
							+ str2.substring(ll, len1)
							+ str1.substring(len1);
					this.yString[int2] = str2.substring(0, ll)
							+ str1.substring(ll, len1)
							+ str2.substring(len1);
					//System.out.println(ll+"\t"+len+"\t"+len1);
					//System.out.println(str1+"\t"+str2);
				//	System.out.println(this.yString[int1]+"\t"+this.yString[int2]);
				}
			}

		}
	}

	/**
	 * 执行突变 种群交配初始化交配概率#规定交配和突变种群中最大值不参与 p 突变概率 一般突变概率都在0.2以下 p2
	 * 为每一个染色体中的一个标记突变概率
	 */
	public void mutation(double p) {
		Random random = new Random();
		for (int i = 0; i < yCount; i++) {
			if (random.nextDouble() > p) {
				continue;
			}
			StringBuilder strb = new StringBuilder(yString[i]);
			int len=(Math.abs(random.nextInt()%((int) (1/p)))+1);
			for(int l=0;l<len;l++)
			{//111001001101010101001110010000010
			for (int j = 0; j < this.yStringLengthSum; j++) {
				if (random.nextDouble() < p*indexP.get(j)) {
					if (strb.charAt(j) == '0') {
						strb.replace(j, j + 1, "1");
					} else {
						strb.replace(j, j + 1, "0");
					}
					break;
				}
			}
			}
			yString[i] = strb.toString();
		}
	}
	/**
	 * 不同位置对应的概率不同
	 */
	public ArrayList<Double> indexP=new ArrayList<Double>();
	public void initIndexP()
	{
		int size=this.yStringLengthSum;
		for(int i=0;i<size;i++)
		{
			indexP.add(((i+1)*1.5D)/(size));
		}
	}
	
	public int tempCount;
	
	public synchronized int addThreadStepCount()
	{
		this.threadStepCount++;
		return threadStepCount;
	}
	/**
	 * 多线程程序
	 */
	@Override
	public void run() {
		// y染色体inde位置
		int threadYIndex = ++threadIndex;
		ArrayList<String> input = new ArrayList<String>();
		input.add(Integer.toString(threadYIndex));
		while (true) {
			if(isBreak)
			{
				break;
			}
			computeCount++;
			//System.out.println("统计将编码转换为10进制++"+Thread.currentThread().getName());
			changeTo10(threadYIndex);
			//System.out.println("计算次数:" + computeCount);
			//System.out.println("执行适应度"+Thread.currentThread().getName());
			if (this.isStringFunction) {
				// 计算单个染色体的适应度值
				this.yFitness[threadYIndex] = this.evaluate(threadYIndex, fx);
			} else {
		//		System.out.println("执行外部方法");
				if (outerExec == null) {
					System.out.println("执行错误 没有外部执行程序的输入");
					System.exit(1);
				}
				// 第一个为对应的值
				// 第二个为index的string值
				synchronized (this) {
					//单度执行此处
				ModelValue modelValue = outerExec.get(
						this.yValue[threadYIndex], input);
				if(modelValue==null)
				{
					System.out.println("数据异常");
					//需要判断实际的数据情况
					//如果数据异常则认为输入的feather导致的svd数据信息错误
					isBreak=true;
				}else{
			//	System.out.println("适应度计算:" + threadYIndex + "\t"
			//			+ modelValue.getIndex() + "\t" + modelValue.getRmse());
				this.yFitness[threadYIndex] = modelValue.getRmse();
				//设置对应的model index
				this.yFitnessModel[threadYIndex]=modelValue.getIndex();
				}
				}
				
				
			}
			if(isBreak)
			{
				break;
			}
			//当前程数量达到染色体数量则使用其中一个线程执行此处 当该线程执行完成则其他线程继续执行
			;
			//System.out.println("执行程序判断"+Thread.currentThread().getName()+"\t"+(threadStepCount+1)+"\t"+tempCount);
			if(addThreadStepCount()==this.yCount)//tempCount)
			{
				//System.out.println("执行是否收敛"+Thread.currentThread().getName());
				boolean ll = this.isStdOk(this.error);
				//System.out.println("isOk:" + ll);
				if (ll == true) {
					//System.out.println("结束Ga，并收敛最大值为:"
					//		+ this.yFitness[this.yFitnessMaxIndex]);
					isBreak=true;
					//break;
				}
				//System.out.println("适应度最大值为:"
				//		+ this.yFitness[this.yFitnessMaxIndex]);
				//System.out.println("执行累计汇总");
				this.sumP(isUpMax);
				//System.out.println("执行轮盘选择");
				this.coronaSelect();
				//System.out.println("执行交配");
				this.mating(matingP, matingCount, matingContext);
				//System.out.println("执行突变");
				this.mutation(mutationP);
				threadStepCount=0;
				//激活其他线程继续执行
				System.out.println("激活其他所有线程--------------"+Thread.currentThread().getName()+"\t"+this.yFitness[this.yFitnessMaxIndex]);
				synchronized (this) {
					tempCount=1;
				this.notifyAll();
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
			}else{
				//其他线程暂停
				synchronized (this) {	
				try {
					this.wait();
					tempCount++;
					//System.out.println("线程激活:"+Thread.currentThread().getName()+"\t"+this.yFitness[threadYIndex]);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//System.out.println("线程激活:"+Thread.currentThread().getName());
				
				}
			}
			if(computeCount/yCount>=iterCount)
			{
				if(threadYIndex==0)
				{
					System.out.println("达到到最大迭代次数");
				}
				break;
			}
			
		}
	}

	/**
	 * 打印最终的结果
	 */
	public void printResult() {
		System.out.println("最大适应度为："+this.yFitness[this.yFitnessMaxIndex]);
		System.out.println("对应的model index:"+this.yFitnessModel[this.yFitnessMaxIndex]);
	}

	public static void main(String[] args) {

		String[] x = { "x1", "x2" };
		//String[] x={"learning_rate"};
		// 设定 x1 x2 的 有效区间
		double[][] rangeValue = { { -3, 12.1 }, { 4.1, 5.8 } };
		//double[][] rangeValue={{0.0005,0.01}};
		int precision = 4;
		int yCount = 50;
		// max(21.5+x1*sin(4*pi*x1)+x2*sin(20*pi*x2))
		String fx = "21.5+x1*sin(4*pi*x1)+x2*sin(20*pi*x2)";
		double px = 0.3;
		int matingCount = 6;
		String context = "complex";
		double py = 0.1;
		//System.out.println("Ga");
		GA_Input ga_input = new GA_Input(x, rangeValue, precision, yCount, fx, true,
				px, matingCount, context, py,true,1E-4,200);
		ga_input.outerExec=new GA_OuterExec();
		Thread[] ga = new Thread[yCount];
		for (int i = 0; i < yCount; i++) {
			ga[i] = new Thread(ga_input, Integer.toString(i));
			ga[i].start();
		}
		for (int i = 0; i < yCount; i++) {
			try {
				ga[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/**
		 * 打印结果
		 */
		ga_input.printResult();
	}
}
