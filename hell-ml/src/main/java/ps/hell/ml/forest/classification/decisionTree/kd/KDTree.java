package ps.hell.ml.forest.classification.decisionTree.kd;

import java.util.ArrayList;

import ps.landerbuluse.ml.math.MathBase;
import scala.actors.threadpool.Arrays;

/**
 * kd tree
 * 
 * @author Administrator
 *
 */
public class KDTree {

	public class KDNode {
		/**
		 * 父类
		 */
		public KDNode parent = null;
		/**
		 * 左子树
		 */
		public KDNode left = null;
		/**
		 * 又子树
		 */
		public KDNode right = null;
		/**
		 * 当前值
		 */
		public double[] value = null;
		
		public int deep=0;
		/**
		 * 使用的分类
		 */
		public ArrayList<Integer> useIndex = new ArrayList<Integer>();
	}

	public KDNode root = null;

	/**
	 * 数据源
	 */
	public double[][] data = null;
	/**
	 * 维度
	 */
	public int dim = 0;
	/**
	 * 数量
	 */
	public int length = 0;

	/**
	 * 构建
	 * 
	 * @param data
	 */
	public KDTree(double[][] data) {
		this.data = data;
		this.length = data.length;
		this.dim = data[0].length;
	}

	/**
	 * 执行
	 */
	public void exec() {
		train();
	}

	/**
	 * 训练
	 */
	public void train() {
		double[][] temp = data.clone();
		this.root = new KDNode();
		KDNode node = this.root;
		node.deep=0;
		SplitTable table=new SplitTable();
		trainOne(temp, node);
	}

	public class SplitTable {
		double[][] temp1 = null;
		double[][] temp2 = null;
		double[] val = null;
	}

	public void trainOne(double[][] temp, KDNode node) {
		if(temp==null||temp.length==0){
			return;
		}else if (temp.length == 1) {
			if(node.parent==null)
			{
				
			}else{
				node.deep=node.parent.deep+1;
			}
			node.value = temp[0];
			return;
		}
		// 获取方差
		double[] cov = cov(temp);
		// 获取一个最大方差
		int maxIndex = MathBase.getMaxIndex(cov);
		// 按照该index分割
		// 获取中位数对应的位置
		double[] col = new double[temp.length];
		for (int i = 0; i < temp.length; i++) {
			col[i] = temp[i][maxIndex];
		}
		int index = MathBase.getMedianIndex(col);
		// 拆分
		double val = temp[index][maxIndex];
		double[][] temp1 = null;
		double[][] temp2 = null;
		ArrayList<Integer> tempIndex1 = new ArrayList<Integer>();
		ArrayList<Integer> tempIndex2 = new ArrayList<Integer>();
		for (int i = 0; i < temp.length; i++) {
			if (temp[i][maxIndex] < val) {
				tempIndex1.add(i);
			} else if (temp[i][maxIndex] > val) {
				tempIndex2.add(i);
			} else {

			}
		}
		SplitTable table=new SplitTable();
		table.temp1 = MathBase.getSplitData(temp, tempIndex1, true);
		table.temp2 = MathBase.getSplitData(temp, tempIndex2, true);
		table.val = temp[index];
		tempIndex1 = null;
		tempIndex2 = null;
		node.value = table.val;
		if(node.parent==null)
		{
			
		}else{
			node.deep=node.parent.deep+1;
		}
		node.left=new KDNode();
		node.left.parent=node;
		trainOne(table.temp1,node.left);
		node.right=new KDNode();
		node.right.parent=node;
		trainOne(table.temp2,node.right);
	}

	/**
	 * 获取 不同维度的方差
	 */
	public double[] cov(double[][] dataInput) {
		double[] cov = null;
		double[] mean = MathBase.colMean(dataInput);
		cov = MathBase.colStd(dataInput, mean);
		return cov;
	}

	/**
	 * 打印
	 */
	public void print() {
		// 打印
		print(this.root);
	}

	/**
	 * 打印node
	 * 
	 * @param node
	 */
	public void print(KDNode node) {
		if (node == null) {
			return;
		}
		if (node.value == null) {
			return;
		}
		for(int i=0;i<node.deep;i++){
			System.out.print(" ");
		}
		System.out.println(Arrays.toString(node.value));
		if (node.left != null) {
			print(node.left);
		}
		if (node.right != null) {
			print(node.right);
		}
	}

	public static void main(String[] args) {
		double[][] data = new double[][] { { 2, 3 }, { 4, 7 }, { 5, 4 },
				{ 7, 2 }, { 8, 1 }, { 9, 6 } };
//		KDTree tree = new KDTree(data);
//		tree.exec();
//		tree.print();
		System.out.println(Arrays.toString(data));
	}
}
