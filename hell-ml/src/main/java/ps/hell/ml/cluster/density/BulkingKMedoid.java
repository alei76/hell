package ps.hell.ml.cluster.density;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;


/**
 * 随机数使用弹性算法实现
 * 增量 medoid方法
 * 
 * @author Administrator
 *
 */
public class BulkingKMedoid {

	/**
	 * 数据源
	 * 
	 * @author Administrator
	 *
	 */
	public class DataPoint implements Comparable<DataPoint> {
		/**
		 * 数据源
		 */
		public int[] data = null;
		/**
		 * 数据源的时间戳
		 */
		public long dataTime = System.currentTimeMillis();

		public DataPoint(int[] data) {
			this.data = data;
		}

		/**
		 * 从小到大排序
		 */
		@Override
		public int compareTo(DataPoint o) {
			// TODO Auto-generated method stub
			return Long.compare(dataTime, o.dataTime);
		}

	}

	/**
	 * 主类
	 * 
	 * @author Administrator
	 *
	 */
	public class Medoid {

		public int[] dimension; // 质点的维度
		public double etdDisSum;// Medoid到本类簇中所有的欧式距离之和
		/**
		 * 老的类别
		 */
		public ArrayList<DataPoint> dataPoint = new ArrayList<DataPoint>();

		public Medoid(int[] dimension) {
			this.dimension = dimension;
		}

		public void addData(int[] data) {
			this.dataPoint.add(new DataPoint(data));
		}

		public void addData(DataPoint data) {
			this.dataPoint.add(data);
		}

		public void clearn() {
			this.dataPoint = new ArrayList<DataPoint>();
		}

		/**
		 * 移除一个时间点之前的数据
		 * 
		 * @param dataTime
		 */
		public void move(long dataTime) {
			while (dataPoint.size() > 0) {
				DataPoint da = dataPoint.get(0);
				if (da.dataTime < dataTime) {
					dataPoint.remove(0);
				}
			}
		}

		/**
		 * 重新 计算质心 并获取 新的误差
		 */
		public void calcMedoid() {// 取代价最小的点
			double minEucDisSum = this.etdDisSum;
			for (int i = 0; i < dataPoint.size(); i++) {
				double tempeucDisSum = calDist(dimension, dataPoint.get(i));
				if (tempeucDisSum < minEucDisSum) {
					dimension = dataPoint.get(i).data;
					minEucDisSum = tempeucDisSum;
				}
			}
		}

		/**
		 * 获取距离
		 * 
		 * @param data
		 * @return
		 */
		public double getDist(DataPoint data) {
			return calDist(dimension, data);
		}

		/**
		 * 获取距离
		 * 
		 * @param data
		 * @return
		 */
		public double getDist(int[] data) {
			return calDist(dimension, data);
		}

		/**
		 * 计算绝对距离
		 * 
		 * @param data1
		 * @param data2
		 * @return
		 */
		private double calDist(int[] data1, DataPoint data2) {
			int[] da2 = data2.data;
			return calDist(data1, da2);
		}

		/**
		 * 计算绝对距离
		 * 
		 * @param data1
		 * @param data2
		 * @return
		 */
		private double calDist(int[] data1, int[] data2) {
			double sum = 0;
			for (int i = 0; i < data1.length; i++) {
				if (data1[i] == data2[i]) {
				} else {
					sum += 1;
				}
			}
			return sum;
		}
	}

	/**
	 * 类中心
	 */
	public ArrayList<Medoid> medoid = new ArrayList<Medoid>();
	/**
	 * 全部数据源
	 */
	public ArrayList<DataPoint> allData = new ArrayList<DataPoint>();
	/**
	 * 内部存储的有效数量
	 */
	public int validSize = 1000;
	/**
	 * 维度
	 */
	public int dimNum = 0;
	public double oldError = -1;
	public int k = 0;

	/**
	 * @param k
	 *            数量
	 * @param center
	 *            初始中心
	 */
	public BulkingKMedoid(int k, int[][] center) {
		this.k = k;
		if (center != null) {
			for (int i = 0; i < center.length; i++) {
				Medoid me = new Medoid(center[i]);
				medoid.add(me);
			}
			this.dimNum = center[0].length;
		}
	}

	/**
	 * 新数据源的添加
	 * 
	 * @param newData
	 */
	public void train(int[][] newData) {
		// 剔除老数据源 添加新数据源
		int size = newData.length;
		if (size == 0) {
			return;
		}
		if (medoid.size() == 0) {
			// 随机初始化类中心
			int[][] val=randomCenter(newData);
			for(int i=0;i<val.length;i++){
				medoid.add(new Medoid(val[i]));
			}
		}
		this.dimNum = newData[0].length;
		// 计算每个数据源对应的位置
		long trankTime = getTrankTime(size);
		if (trankTime != 0) {
			for (int i = 0; i < medoid.size(); i++) {
				medoid.get(i).move(trankTime);
			}
		}
		// 开始训练
		// 添加数据集
		addData(newData);
		double error = 0;
		while (error != oldError) {
			oldError = error;
			for (int m = 0; m < medoid.size(); m++) {// 每次迭代开始情况各类簇的点
				medoid.get(m).clearn();
				;
			}
			for (int j = 0; j < allData.size(); j++) {
				int clusterIndex = 0;
				double minDistance = Double.MAX_VALUE;
				// 样本与质点的最小距离
				for (int k = 0; k < medoid.size(); k++) {// 判断样本点属于哪个类簇
					double eucDistance = medoid.get(k).getDist(allData.get(j));
					if (eucDistance < minDistance) {
						minDistance = eucDistance;
						clusterIndex = k;
					}
				}
				// 将该样本点添加到该类簇
				medoid.get(clusterIndex).addData(allData.get(j));
			}

			for (int m = 0; m < medoid.size(); m++) {
				medoid.get(m).calcMedoid();// 重新计算各类簇的质点
			}

			error = 0;
			for (int n = 0; n < medoid.size(); n++) {
				error += medoid.get(n).etdDisSum;
			}
		}
	}

	/**
	 * 测试数据
	 * 
	 * @return 对应的分组id
	 */
	public int test(int[] data) {
		int index = 0;
		double min = Double.MAX_VALUE;
		for (int k = 0; k < medoid.size(); k++) {// 判断样本点属于哪个类簇
			double eucDistance = medoid.get(k).getDist(data);
			if (min > eucDistance) {
				min = eucDistance;
				index = k;
			}
		}
		return index;
	}

	// 添加新数据
	public void addData(int[][] newData) {
		for (int i = 0; i < newData.length; i++) {
			allData.add(new DataPoint(newData[i]));
		}
		// 重排序
		sort();
	}

	/**
	 * 重排序
	 */
	public void sort() {
		Collections.sort(allData);
	}

	/**
	 * 获取需要被截断的时间戳
	 * 
	 * @param size
	 * @return 如果为0表示不截断
	 */
	public long getTrankTime(int size) {
		long val = 0;
		if (allData.size() + size > validSize) {
			// 如果
			int s = allData.size() + size - validSize;
			long time = allData.get(s).dataTime;
			remove(time, s);
		}
		return val;
	}

	/**
	 * 移除数据集
	 * 
	 * @param time
	 * @param index
	 */
	public void remove(long time, int index) {
		while (index >= 0) {
			allData.remove(0);
		}
		while (allData.size() > 0) {
			if (time < allData.get(0).dataTime) {
				allData.remove(0);
			} else {
				break;
			}
		}
	}

	/**
	 * 随机化类中心
	 * 
	 * @param data
	 * @return
	 */
	public int[][] randomCenter(int[][] data) {
		// 以为使用 铭刻符距离
		// 计算最大
		// 使用随机膨胀算法 计算最大距离的k个点
		RandomFuzzer random = new RandomFuzzer(data, k, 100);
		random.train();
		return random.getOutPut();
	}

	public class RandomFuzzer {
		public int[][] data = null;
		public int k = 0;
		public int iterCount = 0;
		public int[][] center = null;
		public double[][] centerPower = null;
		double[][] centerPower2 = null;
		public int[] cluster = null;
		public int dimNum = 0;
		public double alpha = 1;
		public double weight = 0.1;
		public double wegith2 = 0.2;

		public RandomFuzzer(int[][] data, int k, int iterCount) {
			this.data = data;
			this.k = k;
			this.iterCount = iterCount;
			this.dimNum = data[0].length;
		}

		/**
		 * 训练
		 */
		public void train() {
			center = new int[k][];
			HashSet<One> val = new HashSet<One>();
			int zn = 0;
			for (int i = 0; i < k; i++) {
				// 随机k个点
				while (true) {
					zn++;
					int index = (int) (Math.random() * data.length);
					val.add(new One(data[index]));
					if (val.size() == i + 1) {
						break;
					}
					if (zn > 3 * data.length) {
						return;
					}
				}
			}
			// 弹性方法细节
			center = new int[k][];
			centerPower=new double[k][];
			centerPower2=new double[k][];
			int index = 0;
			for (One one : val) {
				center[index] = one.data;
				centerPower[index] = new double[dimNum];
				centerPower2[index] = new double[dimNum];
				index++;
			}
			cluster = new int[k];
			for (int it = 0; it < iterCount; it++) {
				for(int i=0;i<center.length;i++){
					for(int j=0;j<dimNum;j++){
						centerPower[i][j]=0;
						centerPower2[i][j]=0;
					}
				}
				for (int zp = 0; zp < k; zp++) {
					// 计算每一个中心点的受力点
					for (int i = 0; i < data.length; i++) {
						double dist = euaDist(centerPower[zp], data[i]);
						if (dist == 0d) {
							continue;
						}
						for (int j = 0; j < dimNum; j++) {
							// 距离越远受力越小
							double di = (data[i][j] - centerPower[zp][j]);
							if (di == 0d) {
								continue;
							}
							centerPower[zp][j] += alpha / (dist / di);
						}
					}
				}
				// 调整核心点之间的作用力

				for (int zp = 0; zp < k; zp++) {
					for (int zp2 = 0; zp2 < k; zp2++) {
						double dist = euaDist(centerPower[zp], centerPower[zp2]);
						if (dist == 0d) {
							continue;
						}
						for (int j = 0; j < dimNum; j++) {
							centerPower2[zp][j] += (centerPower[zp][j] - centerPower[zp2][j])
									/ dist;
						}
					}
				}
				// 调整中心点能量
				for (int zp = 0; zp < k; zp++) {
					for (int i = 0; i < dimNum; i++) {
						centerPower[zp][i] = centerPower[zp][i] * weight
								+ centerPower2[zp][i];
					}
				}
				// 调整类中心
				for (int zp = 0; zp < k; zp++) {
					for (int i = 0; i < dimNum; i++) {
						center[zp][i] += centerPower[zp][i] * weight
								+ centerPower2[zp][i] * wegith2;
					}
				}
				// error不写
			}
		}

		/**
		 * 米考夫祭祀 1
		 * 
		 * @return
		 */
		public double euaDist(double[] data1, int[] data2) {
			double sum = 0;
			for (int i = 0; i < data1.length; i++) {
				sum += Math.abs(data1[i] - data2[i]);
			}
			return sum;
		}

		/**
		 * 米考夫祭祀 1
		 * 
		 * @return
		 */
		public double euaDist(double[] data1, double[] data2) {
			double sum = 0;
			for (int i = 0; i < data1.length; i++) {
				sum += Math.abs(data1[i] - data2[i]);
			}
			return sum;
		}

		/**
		 * 基本hash类
		 * 
		 * @author Administrator
		 *
		 */
		public class One implements Comparable<One> {
			public int[] data = null;

			public One(int[] data) {
				this.data = data;
			}

			@Override
			public int compareTo(One o) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int hashCode() {
				int v = 0;
				for (int i = 0; i < data.length; i++) {
					v += data[i] >> i;
				}
				return v;
			}
		}

		/**
		 * 获取最终的结果输出
		 * 
		 * @return
		 */
		public int[][] getOutPut() {
			return center;
		}

	}

	public static void main(String[] args) {
		double[][] cen = { { 8, 7 }, { 8, 6 }, { 7, 7 } };
		int[][] data = { { 2, 3 }, { 2, 4 }, { 1, 4 }, { 1, 3 }, { 2, 2 },
				{ 3, 2 },

				{ 8, 7 }, { 8, 6 }, { 7, 7 }, { 7, 6 }, { 8, 5 },

				{ 100, 2 },// 孤立点

				{ 8, 20 }, { 8, 19 }, { 7, 18 }, { 7, 17 }, { 7, 20 } };
		BulkingKMedoid test = new BulkingKMedoid(3,null);
		test.train(data);
		for(int i=0;i<data.length;i++){
			System.out.println(i+":"+test.test(data[i]));
		}
	}
}
