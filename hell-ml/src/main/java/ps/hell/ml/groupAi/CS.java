package ps.hell.ml.groupAi;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Vector;

import ps.hell.ml.statistics.base.value.stat.Gamma;
import ps.hell.util.scriptEngine.StringToFunction;

/**
 * 布谷鸟搜索算法
 * 
 * @author Administrator
 *
 */
public class CS {

	public class EggStatus implements Comparable<EggStatus> {
		/**
		 * 拟合值
		 */
		public double fit = 0;
		/**
		 * 位置
		 */
		public HashMap<String, Double> step = null;

		public EggStatus(double fit, HashMap<String, Double> step) {
			this.fit = fit;
			this.step = step;
		}

		@Override
		public int compareTo(EggStatus o) {
			// TODO Auto-generated method stub
			return Double.compare(o.fit, fit);
		}
		@Override
		public String toString(){
			StringBuffer sb=new StringBuffer();
			for(Entry<String,Double> val:step.entrySet()){
				sb.append(val.getKey()).append(":").append(val.getValue());
				sb.append("\t");
			}
			sb.append("fit:"+fit);
			return sb.toString();
		}

	}

	public class CSTread extends Thread {
		/**
		 * 区间值
		 */
		public HashMap<String, double[]> section = null;
		/**
		 * 具体的字符串
		 */
		public String fxString = null;
		/**
		 * 迭代次数
		 */
		public int iterator = 1000;

		public Vector<Vector<EggStatus>> nest = null;

		public Random random = null;

		public CSTread(int iterator, Random random,
				HashMap<String, double[]> section, String fxString,
				Vector<Vector<EggStatus>> nest) {
			this.iterator = iterator;
			this.section = section;
			this.fxString = fxString;
			this.nest = nest;
			this.random = random;
		}

		public void run() {
			for (int i = 0; i < iterator; i++) {
				System.out.println(Thread.currentThread().getName()+"\t 迭代次数:"+i);
				// 获取鸟巢
				int nestIndex = getNestIndex();
				Vector<EggStatus> list = nest.get(nestIndex);
				synchronized (list) {
					if (list.size() <= 1) {
						// 下蛋
						String fxDemand = fxString;
						HashMap<String, Double> step = new HashMap<String, Double>();
						for (Entry<String, double[]> val : section.entrySet()) {
							Double fitEgg = getRandomSection(val.getValue());
							step.put(val.getKey(), fitEgg);
							fxDemand = fxDemand.replaceAll(val.getKey(),
									Double.toString(fitEgg));
						}
						double fit = fit(fxDemand);
						list.add(new EggStatus(fit, step));
						Collections.sort(list);
						continue;
					}
					// 创建一个新的蛋
					// 获取最好的鸟蛋
					EggStatus eggBest = list.get(0);
					// 随机获取一个鸟蛋
					int useIndex = getNestEggIndex(list.size());
					EggStatus eggUse = list.get(useIndex);
					// 修改步长
					double step = eggBest.fit - eggUse.fit;
					if (step == 0d) {
						step = 1E-4;
					}
					// 获取新的值
					String deFx = fxString;
					HashMap<String, Double> val2 = new HashMap<String, Double>();
					for (Entry<String, Double> map : eggUse.step.entrySet()) {
						double val22 = map.getValue() + (step * LEVYRandom());
						deFx = deFx.replaceAll(map.getKey(),
								Double.toString(val22));

						val2.put(map.getKey(), val22);
					}
					// 重新计算fit值

					list.add(new EggStatus(fit(deFx), val2));
					// 判断是否丢弃 最后一个egg
					if (Math.abs(random.nextDouble()) < delete) {
						list.remove(list.size() - 1);
					}
				}
			}
		}

		/**
		 * levy 随机数 需要修改 alpha*u/(|v|^(1/beta))
		 * 
		 * @return
		 */
		public double LEVYRandom() {
			return u * LEVY() / Math.pow(Math.abs(v), 1 / beta);
		}

		public double u = 0;
		public double v = 1;

		public double beta = 1.5;

		/**
		 * {F(1+beta)*sin(pi*beta/2)/F(((1+beta)/2)*beta*2^((beta-1)/2))}^(1/
		 * beta)
		 * 
		 * @return
		 */
		public double LEVY() {
			return Math.pow(
					Gamma.gamma(1 + beta)
							* Math.sin(Math.PI * beta / 2)
							/ Gamma.gamma((1 + beta) / 2 * beta
									* Math.pow(2, (beta - 1) / 2)), 1 / beta);
		}

		/**
		 * 随机获取 随机区间
		 * 
		 * @param val
		 * @return
		 */
		public double getRandomSection(double[] val) {
			return val[0] + (Math.abs(random.nextDouble())) * (val[1] - val[0]);
		}

		/**
		 * 获取鸟巢index
		 * 
		 * @return
		 */
		public int getNestIndex() {
			return Math.abs(random.nextInt() % nest.size());
		}

		/**
		 * 获取鸟巢index
		 * 
		 * @return
		 */
		public int getNestEggIndex(int size) {
			return Math.abs(random.nextInt() % size);
		}

		/**
		 * 求解fx函数值
		 * 
		 * @param fx
		 * @return
		 */
		public double fit(String fx) {
			System.out.println("fx:"+fx);
			return StringToFunction.evaluateDecode(fx);
		}
	}

	/**
	 * 区间值
	 */
	public HashMap<String, double[]> section = null;
	/**
	 * 具体的字符串
	 */
	public String fxString = null;
	/**
	 * 鸟数
	 */
	public int birdNum = 1;
	/**
	 * 鸟巢数量
	 */
	public int nestNum = 5;

	public Random random = new Random();

	public int iterator = 1000;

	public double delete = 0.1;

	public Vector<Vector<EggStatus>> nest = new Vector<Vector<EggStatus>>();

	/**
	 * 
	 * @param fxString
	 * @param section
	 * @param birdNum
	 * @param nestNum
	 */
	public CS(String fxString, HashMap<String, double[]> section, int birdNum,
			int nestNum) {
		this.fxString = fxString;
		this.section = section;
		this.birdNum = birdNum;
		this.nestNum = nestNum;
		for (int i = 0; i < nestNum; i++) {
			nest.add(new Vector<EggStatus>());
		}
	}

	public void exec() {
		train();
	}

	public void train() {
		System.out.println("启动");
		Thread[] birds = new Thread[birdNum];
		for (int i = 0; i < birdNum; i++) {
			birds[i] = new CSTread(iterator, random, section, fxString, nest);
			birds[i].start();
		}

		for (int i = 0; i < birdNum; i++) {
			try {
				birds[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("结束");
		// 统计所有鸟巢中最好的一个值 为最优值
		System.out.println("max:\t"+getMax().toString());
	}

	public EggStatus getMax() {
		double max=-Double.MAX_VALUE;
		EggStatus val=null;
		for (Vector<EggStatus> nestOne : nest) {
			for (EggStatus egg : nestOne) {
				if(egg.fit>max){
					val=egg;
					max=egg.fit;
				}
			}
		}
		return val;
	}

	public static void main(String[] args) {
		String[] x={"x1","x2"};
		//设定 x1 x2 的 有效区间
		double[][] rangeValue={{-3,12.1},{4.1,5.8}};
		int birdNum=1;
		int nestNum=1;
		//max(21.5+x1*sin(4*pi*x1)+x2*sin(20*pi*x2))
		String fx="21.5+x1*sin(4*pi*x1)+x2*sin(20*pi*x2)";
		fx=fx.replaceAll("pi",Double.toString(Math.PI));
		HashMap<String,double[]> section=new HashMap<String,double[]>();
		for(int i=0;i<x.length;i++)
		{
			section.put(x[i],rangeValue[i]);
		}
		CS cs=new CS(fx,section,birdNum,
					nestNum);
		cs.exec();
	}
}
