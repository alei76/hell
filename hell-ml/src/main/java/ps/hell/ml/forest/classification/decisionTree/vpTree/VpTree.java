package ps.hell.ml.forest.classification.decisionTree.vpTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import ps.landerbuluse.ml.dist.base.EuclidDistanceSimilary;
import ps.landerbuluse.ml.dist.base.inter.SimilaryFactory;
import ps.landerbuluse.ml.forest.classification.decisionTree.quad.INDArray;
import ps.landerbuluse.ml.forest.classification.decisionTree.quad.QuadTree;
import ps.landerbuluse.ml.forest.classification.decisionTree.quad.XyPoint;
import ps.landerbuluse.ml.math.MathBase;
import ps.landerbuluse.ml.math.matrix.Matrix;
import ps.landerbuluse.ml.statistics.base.Normal;

/**
 * 和kd-tree相似 不过是 使用距离作为度量 后选择media值做分割点
 * 
 * @author Administrator
 *
 */
public class VpTree {

	public VpTree parent;
	/**
	 * 
	 * 小于useDist
	 */
	public VpTree leftSon;
	/**
	 * 大于 useDist
	 */
	public VpTree rightSon;
	/**
	 * 使用的数据
	 */
	public double[] useValue = null;

	public static int size=10;

	public double useDist;
	public boolean isLeaf=false;

	/**
	 * 只有跟节点才有原始数据 在新增数据时，不做 递归修正树 源数据
	 */
	public ArrayList<double[]> data = null;
	/**
	 * 是否使用随机
	 */
	public boolean isRandom = true;
	/**
	 * 是否选择一个点作为跟节点
	 */
	public int useIndexToRoot = 0;
	public static Random random = new Random();
	/**
	 * 目前先写死局里算法 使用欧几里得距离
	 */
	public SimilaryFactory distFunction = new EuclidDistanceSimilary();

	/**
	 * 数据入口
	 * @param data 输入
	 * @param isRandom 初始跟分裂点是否使用随机
	 * @param useIndexToRoot 根节点使用的索引 和 random对应
	 * @param size 最小叶子节点最多数为 size
	 *            每一个区域对应的最大大小
	 */
	public VpTree(ArrayList<double[]> data, boolean isRandom,
			int useIndexToRoot, int size) {
		this.isRandom = isRandom;
		this.useIndexToRoot = useIndexToRoot;
		this.size=size;
		init(data);
	}

	/**
	 * 内部使用
	 */
	public VpTree(boolean in) {

	}

	/**
	 * 初始化
	 */
	public void init(ArrayList<double[]> da) {
		fill(this, da, isRandom, useIndexToRoot);
	}

	/**
	 * 执行方法
	 * 
	 * @param vp
	 * @param da
	 * @param isRandom
	 * @param useIndexToRoot
	 */
	public void fill(VpTree vp, ArrayList<double[]> da, boolean isRandom,
			int useIndexToRoot) {
		int useIndex = -1;
		if (isRandom) {
			useIndex = random.nextInt(da.size());
		} else {
			useIndex = useIndexToRoot;
		}
		// 计算其他点域该点的距离
		this.useValue = da.get(useIndex);
		double[] dist = new double[da.size()];
		for (int i = 0; i < da.size(); i++) {
			if (i == useIndex) {
				continue;
			}
			dist[i] = distFunction.getSimilary(da.get(i), this.useValue, null);
		}
		// 获取中位数的值
		int medianIndex = MathBase.getMedianIndex(dist, useIndex);
		double medianVal = dist[medianIndex];
		useDist = medianVal;
		// 获取左右数量
		ArrayList<double[]> left = new ArrayList<double[]>();
		ArrayList<double[]> right = new ArrayList<double[]>();
		for (int i = 0; i < da.size(); i++) {
			if (useIndex == i) {
				continue;
			}
			if (medianVal >= dist[i]) {
				left.add(da.get(i));
			} else {
				right.add(da.get(i));
			}
		}
		double[] leftDist = new double[left.size()];
		int indexL = 0;
		int indexR = 0;
		double[] rightDist = new double[right.size()];
		for (int i = 0; i < da.size(); i++) {
			if (useIndex == i) {
				continue;
			}
			if (medianVal >= dist[i]) {
				leftDist[indexL++] = dist[i];
			} else {
				rightDist[indexR++] = dist[i];
			}
		}

		if (left.size() > 0) {
			this.leftSon = new VpTree(true);
			fill(this.leftSon, left, MathBase.getMedianIndex(leftDist));
		}
		if (right.size() > 0) {
			this.rightSon = new VpTree(true);
			fill(this.rightSon, right, MathBase.getMedianIndex(rightDist));
		}
	}

	/**
	 * 子节点
	 * 
	 * @param vp
	 * @param da
	 * @param isRoot
	 * @param isRandom
	 * @param useIndexToRoot
	 */
	private void fill(VpTree vp, ArrayList<double[]> da, int useIndex) {
		// 计算其他点域该点的距离
		vp.useValue = da.get(useIndex);
		double[] dist = new double[da.size()];
		for (int i = 0; i < da.size(); i++) {
			if (i == useIndex) {
				continue;
			}
			dist[i] = distFunction.getSimilary(da.get(i), vp.useValue, null);
		}
		// 获取中位数的值
		int medianIndex = MathBase.getMedianIndex(dist, useIndex);

		double medianVal = dist[medianIndex];
		vp.useDist = medianVal;
		// 获取左右数量
		ArrayList<double[]> left = new ArrayList<double[]>();
		ArrayList<double[]> right = new ArrayList<double[]>();
		for (int i = 0; i < da.size(); i++) {
			if (useIndex == i) {
				continue;
			}
			if (medianVal >= dist[i]) {
				left.add(da.get(i));
			} else {
				right.add(da.get(i));
			}
		}
		//System.out.println((da.size() + 1) / 2 +"\t"+ VpTree.size);
		if ((da.size() + 1) / 2 <= VpTree.size) {
			vp.leftSon = new VpTree(true);
			vp.leftSon.data = left;
			vp.leftSon.isLeaf=true;
			vp.rightSon = new VpTree(true);
			vp.leftSon.data = right;
			vp.rightSon.isLeaf=true;
		} else {
			// 需要继续分裂
			double[] leftDist = new double[left.size()];
			int indexL = 0;
			int indexR = 0;
			double[] rightDist = new double[right.size()];
			for (int i = 0; i < da.size(); i++) {
				if (useIndex == i) {
					continue;
				}
				if (medianVal >= dist[i]) {
					leftDist[indexL++] = dist[i];
				} else {
					rightDist[indexR++] = dist[i];
				}
			}

			if (left.size() > 0) {
				vp.leftSon = new VpTree(true);
				fill(vp.leftSon, left, MathBase.getMedianIndex(leftDist));
			}
			if (right.size() > 0) {
				vp.rightSon = new VpTree(true);
				fill(vp.rightSon, right, MathBase.getMedianIndex(rightDist));
			}
		}
	}

	/**
	 * 获取 输入数据对应的索引位
	 * left =0
	 * right =1
	 * @param input
	 * @return
	 */
	public int[] getIndexVal(double[] input) {
		ArrayList<Integer> re=new ArrayList<Integer>();
		double dist=distFunction.getSimilary(input, useValue, null);
		if(dist<=this.useDist){
			re.add(0);
			this.leftSon.getIndex(input,re);
		}else{
			re.add(1);
			this.rightSon.getIndex(input,re);
		}
		int[] val=new int[re.size()];
		for(int i=0;i<re.size();i++){
			val[i]=re.get(i);
		}
		return val;
	}
	/**
	 * 子节点索引方式
	 * @param input
	 * @param index
	 */
	private void getIndex(double[] input,ArrayList<Integer> index){
		if(this.isLeaf){
			//如果是跟节点则返回
			return;
		}
		double dist=distFunction.getSimilary(input, useValue, null);
		if(dist<=this.useDist){
			index.add(0);
			this.leftSon.getIndex(input,index);
		}else{
			index.add(1);
			this.rightSon.getIndex(input,index);
		}
	}
	public static void main(String[] args) {
		int train_num=20000;
		int test_num=20;
		int size=6;
		Normal n3 = new Normal(-3, 0.3);
		Matrix m3 = n3.random(train_num+test_num, 2, 3);
		double[] x=null;
		ArrayList<double[]> data=new ArrayList<double[]>();
		for(int i=0;i<train_num;i++){
			data.add(new double[]{m3.get(i, 0),m3.get(i,1)});
		}
		VpTree tree=new VpTree(data,true,0,size);
		for(int i=train_num;i<m3.rowNum;i++){
			x=new double[]{m3.get(i, 0),m3.get(i,1)};
			System.out.println(Arrays.toString(x)+":"+Arrays.toString(tree.getIndexVal(x)));
		}
	}

}
