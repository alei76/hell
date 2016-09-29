package ps.hell.ml.forest.classification.decisionTree.quad;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import ps.landerbuluse.ml.math.matrix.Matrix;
import ps.landerbuluse.ml.statistics.base.Normal;

/**
 * 主要是用在空间坐标中的 检索算法
 * @author Administrator
 *
 */
public class QuadTree implements Serializable {
	private QuadTree parent;
	private QuadTree northWest;
	private QuadTree northEast;
	private QuadTree southWest;
	private QuadTree southEast;
	private boolean isLeaf = true;

	private int size;
	/**
	 * 边界 x,y 对应切分的 中心点 w，h对应 四个区域的 最大值
	 */
	private Cell boundary;
	private INDArray data;

	public QuadTree(INDArray data, int size) {
		this(null, data, size);
		this.isLeaf = false;
	}

	/**
	 * 初始化
	 * 
	 * @param data
	 * @param size
	 *            区域中最多的数量
	 */
	public QuadTree(QuadTree parent, INDArray data, int size) {
		this.parent = parent;
		this.size = size;
		initBoundary(data);
		if (data.data.size() <= size) {
			return;

		} else {
			isLeaf = false;
		}
		fill();
	}

	/**
	 * 调整边界数据
	 * 
	 * @param data
	 */
	private void initBoundary(INDArray data) {
		XyPoint meanY = data.mean();
		XyPoint minx = data.minX();
		XyPoint maxx = data.maxX();
		XyPoint miny = data.minY();
		XyPoint maxy = data.maxY();
		init(data, meanY.x, meanY.y,
				Math.max(meanY.x - minx.x, maxx.x - meanY.x) + 1E-10,
				Math.max(meanY.y - miny.y, maxy.y - meanY.y) + 1E-10);
	}

	public QuadTree(QuadTree parent, INDArray data, Cell boundary) {
		this.parent = parent;
		this.boundary = boundary;
		this.data = data;
	}

	public QuadTree(Cell boundary) {
		this.boundary = boundary;
	}

	private void init(INDArray data, double x, double y, double hw, double hh) {
		this.boundary = new Cell(x, y, hw, hh);
		this.data = data;
	}

	private void fill() {
		ArrayList<INDArray> list = new ArrayList<INDArray>();
		for (int i = 0; i < 4; i++) {
			list.add(new INDArray());
		}
		// 插入数据
		for (int i = 0; i < this.data.rows(); ++i) {
			list.get(getIndex(data.data.get(i))).addData(this.data.data.get(i));
		}
		for (int i = 0; i < 4; i++) {
			if (list.get(i).data.size() > 0) {
				switch (i) {
				case 0:
					northEast = new QuadTree(this, list.get(i), size);
					break;
				case 1:
					northWest = new QuadTree(this, list.get(i), size);
					break;
				case 2:
					southWest = new QuadTree(this, list.get(i), size);
					break;
				case 3:
					southEast = new QuadTree(this, list.get(i), size);
					break;
				default:
					break;
				}
			}
		}
	}

	/**
	 * 获取index的位置 
	 * 3 0 
	 * 2 1
	 * 
	 * @param val
	 */
	private int getIndex(XyPoint val) {
		int index = -1;
		if (val.x >= boundary.getX()) {
			if (val.y >= boundary.getY()) {
				index = 0;
			} else {
				index = 1;
			}
		} else {
			if (val.y >= boundary.getY()) {
				index = 2;
			} else {
				index = 3;
			}
		}
		return index;
	}

	/**
	 * 插入一条数据
	 * 
	 * @param val
	 */
	public void insert(XyPoint val) {
		if (parent != null) {
			// 如果不为跟节点
			// 并且不更新上层节点的data数据
			if (this.data == null || this.data.data.size() < size) {
				// 如果不满则直接添加
				// 调整boundary的值
				this.data.addData(val);
				initBoundary(data);
				return;
			}
			// 判断下层节点位置
			int index = getIndex(val);
			switch (index) {
			case 0:
				if (northEast == null) {
					INDArray da = new INDArray();
					da.addData(val);
					northEast = new QuadTree(this, da, size);
				} else {
					northEast.insert(val);
				}
				break;
			case 1:
				if (northWest == null) {
					INDArray da = new INDArray();
					da.addData(val);
					northWest = new QuadTree(this, da, size);
				} else {
					northWest.insert(val);
				}
				break;
			case 2:
				if (southWest == null) {
					INDArray da = new INDArray();
					da.addData(val);
					southWest = new QuadTree(this, da, size);
				} else {
					southWest.insert(val);
				}
				break;
			case 3:
				if (southEast == null) {
					INDArray da = new INDArray();
					da.addData(val);
					southEast = new QuadTree(this, da, size);
				} else {
					southEast.insert(val);
				}
				break;
			default:
				break;
			}
		}

	}

	/**
	 * 获取索引值的实际位置
	 * 
	 * @return
	 */
	public int[] getIndexVal(XyPoint val) {
		ArrayList<Integer> indexList = new ArrayList<Integer>();
		getIndexList(indexList, val);
		int[] re = new int[indexList.size()];
		for (int i = 0; i < re.length; i++) {
			re[i] = indexList.get(i);
		}
		return re;
	}

	/**
	 * 获取索引值的实际位置
	 * 
	 * @return
	 */
	private ArrayList<Integer> getIndexList(ArrayList<Integer> list, XyPoint val) {
		if (this.isLeaf) {
			return list;
		}
		int index = getIndex(val);
		list.add(index);
		QuadTree temp=null;
		switch (index) {
		case 0:
			temp=northEast;
			break;
		case 1:
			temp=northWest;
			break;
		case 2:
			temp=southWest;
			break;
		case 3:
			temp=southEast;
			break;
		default:
			break;
		}
		temp.getIndexList(list, val);
		return list;
	}

	public static void main(String[] args) {
		int train_num=20000;
		int test_num=20;
		int size=20;
		Normal n3 = new Normal(-3, 0.3);
		Matrix m3 = n3.random(train_num+test_num, 2, 3);
		XyPoint x=null;
		INDArray data=new INDArray();
		for(int i=0;i<train_num;i++){
			
				data.addData(new XyPoint(m3.get(i, 0),m3.get(i,1)));
		}
		QuadTree tree=new QuadTree(data,size);
		for(int i=train_num;i<m3.rowNum;i++){
			x=new XyPoint(m3.get(i, 0),m3.get(i,1));
			System.out.println(x+":"+Arrays.toString(tree.getIndexVal(x)));
		}
	}
}
