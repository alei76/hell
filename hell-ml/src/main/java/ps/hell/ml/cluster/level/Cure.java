package ps.hell.ml.cluster.level;

import static com.googlecode.javacv.cpp.opencv_highgui.cvNamedWindow;
import static com.googlecode.javacv.cpp.opencv_highgui.cvShowImage;
import static com.googlecode.javacv.cpp.opencv_highgui.cvWaitKey;

import java.util.LinkedList;
import java.util.Vector;

import ps.hell.math.base.matrix.Matrix;
import ps.hell.ml.statistics.base.Normal;
import ps.hell.util.plot.opencv.BubbleGraph;

import com.googlecode.javacv.cpp.opencv_core;

/**
 * cure聚类算法
 * 
 * @author Administrator
 *
 */
public class Cure {

	/**
	 * 收缩因子
	 */
	private double alpha = 1.0;
	/**
	 * 数据源
	 */
	private double[][] data = null;
	/**
	 * 聚类数量
	 */
	private int clusterCount = 0;
	/**
	 * 维度
	 */
	private int width = 0;

	/**
	 * 
	 * @param data
	 *            数据
	 * @param alpha
	 *            收缩因子
	 */
	public Cure(LinkedList<double[]> data, int clusterCount, double alpha) {

		if (data.size() == 0) {
			System.out.println("输入数据集异常");
			System.exit(1);
			;
		}
		this.alpha = alpha;
		this.clusterCount = clusterCount;
		width = data.get(0).length;
		this.data = new double[data.size()][width];
		int i = 0;
		for (double[] da : data) {
			this.data[i] = da.clone();
			i++;
		}
	}

	/**
	 * 
	 * @param data
	 *            数据
	 * @param alpha
	 *            收缩因子
	 */
	public Cure(double[][] data, int clusterCount, double alpha) {

		if (data.length == 0) {
			System.out.println("输入数据集异常");
			System.exit(1);
			;
		}
		this.clusterCount = clusterCount;
		this.alpha = alpha;
		width = data[0].length;
		this.data = new double[data.length][width];
		int i = 0;
		for (double[] da : data) {
			this.data[i] = da.clone();
			i++;
		}
	}

	/**
	 * 
	 * @return
	 */
	public int[] run() {
		int[] cluster = new int[data.length];
		for (int i = 0; i < data.length; i++) {
			cluster[i] = i;
		}
		int clusterKCount = data.length;
		// 不适用kd树 和heap结构
		if (clusterKCount <= clusterCount) {
			return cluster;
		}

		// 全部样本都为代表点
		LinkedList<SampleClass> samplePoints = new LinkedList<SampleClass>();
		for (int i = 0; i < data.length; i++) {
			int[] clu = new int[1];
			clu[0] = i;
			double[][] cluVal = new double[1][width];
			for (int j = 0; j < width; j++) {
				cluVal[0][j] = data[i][j];
			}
			SampleClass sample = new SampleClass(i, clu, cluVal);
			samplePoints.add(sample);
		}
		while (clusterKCount > clusterCount) {
			// 获取最近的两个带标点
			int first = -1;
			int second = -1;
			double minDist = Double.MAX_VALUE;
			System.out.println("运行剩余簇:" + clusterKCount);
			for (int i = 0; i < samplePoints.size(); i++) {

				for (int j = i + 1; j < samplePoints.size(); j++) {
					for (double[] lp : samplePoints.get(i).clusterSampleValue) {
						for (double[] lp2 : samplePoints.get(j).clusterSampleValue) {
							double dist = 0.0;
							for (int m = 0; m < width; m++) {
								dist += Math.pow(lp[m] - lp2[m], 2.0d);
							}
							if (dist < minDist) {
								minDist = dist;
								first = i;
								second = j;
							}
						}
					}
				}
			}
			// merage 合并first 和second 簇
			SampleClass sam = samplePoints.get(first);
			sam.addClass(samplePoints.get(second).clusterIndex,
					samplePoints.get(second).clusterIndexValue);
			samplePoints.set(first, sam);
			samplePoints.remove(second);
			clusterKCount--;
		}
		// 返回 对应的分类号
		for (int i = 0; i < samplePoints.size(); i++) {
			int[] ln = samplePoints.get(i).clusterIndex;
			for (int j = 0; j < ln.length; j++) {
				cluster[ln[j]] = i;
			}
		}
		return cluster;
	}

	class SampleClass {
		// 属于的类编号
		int cluster = -1;
		// 样本集中的编号
		int[] clusterIndex = null;
		// 带标点的坐标值
		double[][] clusterIndexValue = null;
		double[][] clusterSampleValue = null;

		SampleClass(int cluster, int[] clusterIndex,
				double[][] clusterIndexValue) {
			this.cluster = cluster;
			this.clusterIndex = clusterIndex;
			this.clusterIndexValue = clusterIndexValue;
			this.clusterSampleValue = clusterIndexValue;
		}

		void addClass(int[] clusterIndex, double[][] clusterIndexValue) {
			int[] temp = new int[this.clusterIndex.length + clusterIndex.length];
			for (int i = 0; i < this.clusterIndex.length; i++) {
				temp[i] = this.clusterIndex[i];
			}
			for (int i = 0; i < clusterIndex.length; i++) {
				temp[i + this.clusterIndex.length] = clusterIndex[i];
			}
			this.clusterIndex = temp;
			double[][] tempVal = new double[this.clusterIndexValue.length
					+ clusterIndexValue.length][this.clusterIndexValue[0].length];
			for (int i = 0; i < this.clusterIndexValue.length; i++) {
				tempVal[i] = this.clusterIndexValue[i].clone();
			}
			for (int i = 0; i < clusterIndexValue.length; i++) {
				tempVal[i + this.clusterIndexValue.length] = clusterIndexValue[i]
						.clone();
			}
			this.clusterIndexValue = tempVal;
			clusterSampleValue = new double[this.clusterIndexValue.length][clusterIndexValue[0].length];
			for (int i = 0; i < this.clusterIndexValue.length; i++) {
				clusterSampleValue[i] = this.clusterIndexValue[i].clone();
			}
			double[] lp = new double[clusterIndexValue[0].length];
			for (int i = 0; i < this.clusterIndexValue.length; i++) {
				for (int j = 0; j < this.clusterIndexValue[i].length; j++) {
					lp[j] += this.clusterIndexValue[i][j];
				}
			}
			for (int i = 0; i < this.clusterIndexValue.length; i++) {
				for (int j = 0; j < this.clusterIndexValue[i].length; j++) {
					clusterSampleValue[i][j] += alpha
							* (lp[j] - clusterSampleValue[i][j]);
				}
			}

		}
	}

	public static void main(String[] args) {
		Normal normal1 = new Normal(4, 0.3);
		Matrix data1 = normal1.random(50, 2);

		Normal normal2 = new Normal(10, 0.6);
		Matrix data2 = normal2.random(40, 2);

		Normal normal3 = new Normal(-3, 0.4);
		Matrix data3 = normal3.random(20, 2);

		Normal normal4 = new Normal(1, 0.4);
		Matrix data4 = normal4.random(30, 2);

		data1.addMatrix(data2);
		data1.addMatrix(data3);
		data1.addMatrix(data4);

		double[][] data = data1.getData();

		Cure cure = new Cure(data, 4, 0.8);

		Vector<Double> x = new Vector<Double>();
		Vector<Double> y = new Vector<Double>();
		Vector<Integer> cluster = new Vector<Integer>();
		int[] clusterK = cure.run();
		for (int i = 0; i < data.length; i++) {
			x.add(data[i][0]);
			y.add(data[i][1]);
			cluster.add(clusterK[i]);
			System.out.println("行:" + i + "\t值:" + data[i][0] + ":"
					+ data[i][1] + "\t类：" + clusterK[i]);
		}
		BubbleGraph graph = new BubbleGraph();
		graph.width = 600;
		graph.height = 400;

		graph.parentent_config();// 计算改变的所有全局参量
		// 初始化图片
		BubbleGraph.img_trans img1 = new BubbleGraph.img_trans();
		// main_type="scatter";
		img1.img1 = opencv_core.cvCreateImage(
				opencv_core.cvSize(graph.width, graph.height), 8, 3);
		graph.main_type = "qipao2";

		if (graph.main_type == "scatter" || graph.main_type == "qipao"
				|| graph.main_type == "qipao2" || graph.main_type == "qipao3") {
			System.out.println("初始化图片");
			graph.plotScatter(img1, x, y, graph.main_type, cluster, 0);
		}

		cvNamedWindow("test", 1);
		cvShowImage("test", img1.img1);
		cvWaitKey(0);
	}
}
