package ps.hell.ml.forest.classification.decisionTree.cart;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map.Entry;

public class Cart {

	/**
	 * hashmap存储 对应 索引位置的 分类
	 */
	public HashMap<Integer, HashMap<String, Integer>> classIndexString = new HashMap<Integer, HashMap<String, Integer>>();
	/**
	 * 输出的分类
	 */
	public HashMap<String, Integer> classifyClass = new HashMap<String, Integer>();
	/**
	 * 读取文件
	 */
	public String filePath = null;
	/**
	 * 数据源最后一列对应的是分类
	 */
	public String[][] data = null;
	/**
	 * 列簇的数量 不包含最后一列
	 */
	public int classNum = 0;
	/**
	 * 跟节点
	 */
	public CartTreeNode root = new CartTreeNode();

	/**
	 * 最终的结果集
	 */
	public HashMap<String, Integer> claimValue = null;

	/**
	 * 数对应的node
	 * 
	 * @author Administrator
	 *
	 */
	public class CartTreeNode {
		/**
		 * 误差率
		 */
		public double errorValue = 0;
		/**
		 * 对应的概率值
		 */
		public double pValue = 0;
		/**
		 * 使用的分类
		 */
		public String className = "root";
		/**
		 * 目标名
		 */
		public String targetName = null;
		/**
		 * 左子树
		 */
		public CartTreeNode leftSonTree = null;
		/**
		 * 右子树
		 */
		public CartTreeNode rightSonTree = null;
		/**
		 * 使用的样本行
		 */
		public int[] useIndexRow = null;
		/**
		 * 使用的列
		 */
		public int useIndexCol = -1;
		/**
		 * 分类得分
		 */
		public HashMap<String, Double> targetP = null;

		public CartTreeNode() {

		}

		public void setUseIndexRow(HashSet<Integer> set) {
			useIndexRow = new int[set.size()];
			int z = -1;
			for (Integer i : set) {
				z++;
				useIndexRow[z] = i;
			}
		}

		public void print() {
			String str = "root:" + className;
			System.out.println(str);
			if (leftSonTree != null) {
				leftSonTree.print(str.length(), "l");
			}
			if (rightSonTree != null) {
				rightSonTree.print(str.length(), "r");
			}
		}

		public void print(int length, String status) {
			String str = status + ":" + className;
			System.out.println(getString(length) + str);
			for (Entry<String, Double> value : targetP.entrySet()) {
				System.out.println(getString(length) + " " + value.getKey()
						+ ":" + value.getValue());
			}
			if (leftSonTree != null) {
				leftSonTree.print(str.length() + length, "l");
			}
			if (rightSonTree != null) {
				rightSonTree.print(str.length() + length, "r");
			}
		}

		public String getString(int length) {
			StringBuffer sb = new StringBuffer();
			while (length > 0) {
				sb.append(" ");
				length--;
			}
			return sb.toString();
		}
	}

	/**
	 * 从文件中读取数据
	 */
	public void readDataFile() {
		if (filePath == null) {
			return;
		}
		File file = new File(filePath);
		ArrayList<String[]> dataArray = new ArrayList<String[]>();

		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String str;
			String[] tempArray;
			while ((str = in.readLine()) != null) {
				tempArray = str.split(" ");
				dataArray.add(tempArray);
			}
			in.close();
		} catch (IOException e) {
			e.getStackTrace();
		}
		data = new String[dataArray.size()][];
		dataArray.toArray(data);
		classNum = data[0].length - 1;
	}

	/**
	 * 读取 输入数据
	 * 
	 * @param inputData
	 */
	public void readData(String[] inputData) {
		data = new String[inputData.length][];
		for (int i = 0; i < inputData.length; i++) {
			data[i] = inputData[i].split(" ");
		}
		classNum = data[0].length - 1;
		classifyClass = new HashMap<String, Integer>();
		int index = 0;
		for (String[] val : data) {
			if (classifyClass.containsKey(val[classNum])) {
			} else {
				index++;
				classifyClass.put(val[classNum], index);
			}
		}
	}
	/**
	 * 读取标准数据源
	 * @param dataModel
	 */
	public void readData(String[][] inputData,String[] tag){
		data=(String[][])inputData;
		data=new String[inputData.length][];
		for(int i=0;i<data.length;i++){
			String[] zn=new String[inputData.length+1];
			for(int j=0;j<zn.length-1;j++){
				zn[j]=(String)inputData[i][j];
			}
			zn[zn.length-1]=(String)tag[0];
			data[i]=zn;
		}
		classNum = data[0].length - 1;
		classifyClass = new HashMap<String, Integer>();
		int index = 0;
		for (String[] val : data) {
			if (classifyClass.containsKey(val[classNum])) {
			} else {
				index++;
				classifyClass.put(val[classNum], index);
			}
		}
	}
	

	public class GiniNode implements Comparable<GiniNode> {
		public String className = null;
		public double giniValue;
		public int index = 0;
		public HashSet<Integer> useIndexRow = null;
		public int classCount = 0;

		// 第一个对应的是分类，内部对应的是目标具体的数量
		HashMap<String, HashMap<String, Integer>> stringCount = new HashMap<String, HashMap<String, Integer>>();
		// 目标值对应的数量
		HashMap<String, Integer> classAllCount = new HashMap<String, Integer>();

		/**
		 * @param className
		 *            列名字
		 * @param giniValue
		 *            gini值
		 * @param targetP
		 *            目标所占数量
		 * @param stringCount
		 *            维对应的 目标的数量
		 * @param classAllCount
		 *            维度的数量
		 * @param index
		 *            使用的列index
		 * @param useIndexRow
		 *            使用的行
		 * @param classCount
		 *            类包含的分类数量
		 */
		public GiniNode(String className, double giniValue,
				HashMap<String, HashMap<String, Integer>> stringCount,
				HashMap<String, Integer> classAllCount, int index,
				LinkedList<Integer> useIndexRow, int classCount) {
			this.className = className;
			this.giniValue = giniValue;
			this.index = index;
			this.stringCount = stringCount;
			this.classAllCount = classAllCount;
			if (useIndexRow == null) {
				return;
			}
			this.useIndexRow = new HashSet<Integer>();
			for (Integer in : useIndexRow) {
				this.useIndexRow.add(in);
			}
			this.classCount = classCount;
		}

		@Override
		public int compareTo(GiniNode o) {
			// TODO Auto-generated method stub
			return -Double.compare(giniValue, o.giniValue);
		}

		/**
		 * 获取target对应的概率
		 * 
		 * @return
		 */
		public HashMap<String, Double> getP() {
			int count = classAllCount.get(this.className);
			HashMap<String, Double> result = new HashMap<String, Double>();
			for (Entry<String, Integer> map : stringCount.get(this.className)
					.entrySet()) {
				result.put(map.getKey(), map.getValue() * 1d / count);
			}
			return result;
		}

		/**
		 * 获取!className 对应的target对应的目标值
		 * 
		 * @return
		 */
		public HashMap<String, Double> getPRev() {
			HashMap<String, Double> result = new HashMap<String, Double>();
			HashMap<String, Integer> targetMap = new HashMap<String, Integer>();
			int count = 0;
			int zn = -1;
			for (Entry<String, HashMap<String, Integer>> map : stringCount
					.entrySet()) {
				if (map.getKey().equals(this.className)) {
					continue;
				}
				zn++;
				count += classAllCount.get(map.getKey());
				for (Entry<String, Integer> val : map.getValue().entrySet()) {
					Integer targetValue = targetMap.get(val.getKey());
					if (targetValue == null) {
						targetMap.put(val.getKey(), val.getValue());
					} else {
						targetMap.put(val.getKey(),
								targetValue + val.getValue());
					}
				}
			}
			// 计算
			for (Entry<String, Integer> map : targetMap.entrySet()) {
				result.put(map.getKey(), map.getValue() * 1D / count);
			}
			return result;
		}

		/**
		 * 集合是否继续
		 * 
		 * @return
		 */
		public boolean hasNext() {
			return stringCount.get(this.className).size() <= 1 ? false : true;
		}

		/**
		 * ！集合是否继续
		 * 
		 * @return
		 */
		public boolean hasNextRev() {
			HashSet<String> targetMap = new HashSet<String>();
			for (Entry<String, HashMap<String, Integer>> map : stringCount
					.entrySet()) {
				if (map.getKey().equals(this.className)) {
					continue;
				}
				for (Entry<String, Integer> val : map.getValue().entrySet()) {
					targetMap.add(val.getKey());
				}
				if (targetMap.size() >= 2) {
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * 计算gini值
	 * 
	 * @param index
	 *            使用的col
	 * @param useDataRow
	 *            使用的行
	 * @return 返回最小的gini值以及对应的分类
	 */
	public GiniNode computGiniValue(int indexCol, HashSet<Integer> useDataRow) {
		// 对应的数量
		// 第一个对应的是分类，内部对应的是目标具体的数量
		HashMap<String, HashMap<String, Integer>> stringCount = new HashMap<String, HashMap<String, Integer>>();
		// 目标值对应的数量
		HashMap<String, Integer> classCount = new HashMap<String, Integer>();

		HashMap<String, LinkedList<Integer>> useIndexString = new HashMap<String, LinkedList<Integer>>();
		for (Integer indexRow : useDataRow) {
			// 使用的列
			String className = data[indexRow][indexCol];
			// 使用的col
			String targetName = data[indexRow][classNum];
			HashMap<String, Integer> indentyCount = stringCount.get(className);
			if (indentyCount == null) {
				indentyCount = new HashMap<String, Integer>();
				stringCount.put(className, indentyCount);
			}
			// 设置target具体指
			Integer valTarget = classCount.get(className);
			if (valTarget == null) {
				classCount.put(className, 1);
			} else {
				classCount.put(className, valTarget + 1);
			}

			LinkedList<Integer> useIndex = useIndexString.get(className);
			if (useIndex == null) {
				useIndex = new LinkedList<Integer>();
				useIndexString.put(className, useIndex);
			}
			useIndex.add(indexRow);
			// 设置target后分类的具体指
			Integer val = indentyCount.get(targetName);
			if (val == null) {
				indentyCount.put(targetName, 1);
			} else {
				indentyCount.put(targetName, val + 1);
			}
		}
		// 计算对应的值某一个类的基尼值
		// 对于该列获取最小的gini值
		double miniValue = Double.MAX_VALUE;
		String miniName = null;
		for (Entry<String, HashMap<String, Integer>> map : stringCount
				.entrySet()) {
			double gini = 1;
			double size = classCount.get(map.getKey());
			for (Entry<String, Integer> map2 : map.getValue().entrySet()) {
				double p = map2.getValue() / size;
				gini -= p * p;
			}
			// 每一个分类对应的gini值
			if (miniValue > gini) {
				miniValue = gini;
				miniName = map.getKey();
			}
		}
		// 如果没有拆分类则停止
		// boolean isNext=stringCount.get(miniName).size()<=1?false:true;
		return new GiniNode(miniName, miniValue, stringCount, classCount,
				indexCol, useIndexString.get(miniName), stringCount.size());
	}

	/**
	 * 创建树
	 */
	public void buildTree() {
		HashSet<Integer> useIndexRow = new HashSet<Integer>();
		HashSet<Integer> useIndexCol = new HashSet<Integer>();
		for (int i = 0; i < data.length; i++) {
			useIndexRow.add(i);
		}
		for (int i = 0; i < this.classNum; i++) {
			useIndexCol.add(i);
		}
		int[] useRow = new int[useIndexRow.size()];
		for (int i = 0; i < useIndexRow.size(); i++) {
			useRow[i] = i;
		}
		root.useIndexRow = useRow;
		buildTree(root, useIndexRow, useIndexCol);
	}

	/**
	 * 对子节点建树
	 * 
	 * @param parentNode
	 */
	public void buildTree(CartTreeNode parentNode,
			HashSet<Integer> useIndexRow, HashSet<Integer> useIndexCol) {
		if (useIndexCol.size() <= 0 || useIndexRow.size() == 0) {// 结束
			return;
		}
		GiniNode miniNode = new GiniNode(null, Double.MAX_VALUE, null, null,
				-1, null, 0);
		// 获取列对应的gini
		for (Integer useCol : useIndexCol) {// 获取不同纬度中最小的gini值
			GiniNode giniNode = computGiniValue(useCol, useIndexRow);
			// System.out.println(miniNode.giniValue+"\t"+giniNode.giniValue+"\t"+miniNode.compareTo(giniNode));
			if (miniNode.compareTo(giniNode) < 0) {
				miniNode = giniNode;
			}
		}
		// 剔除使用的列
		HashSet<Integer> tempUseIndexCol = new HashSet<Integer>();
		for (Integer use : useIndexCol) {
			tempUseIndexCol.add(use);
		}
		tempUseIndexCol.remove(miniNode.index);
		CartTreeNode leftNode = new CartTreeNode();
		leftNode.className = miniNode.className;
		leftNode.pValue = miniNode.giniValue;
		leftNode.targetP = miniNode.getP();
		leftNode.setUseIndexRow(miniNode.useIndexRow);
		parentNode.leftSonTree = leftNode;
		if (miniNode.hasNext()) {
			buildTree(leftNode, miniNode.useIndexRow, tempUseIndexCol);
		}

		if (miniNode.classCount <= 1) {
			return;
		}
		// 剔除left对应的行
		CartTreeNode rightNode = new CartTreeNode();
		rightNode.className = "!" + miniNode.className;
		rightNode.pValue = -miniNode.giniValue;
		rightNode.targetP = miniNode.getPRev();
		HashSet<Integer> useIndexRowRight = new HashSet<Integer>();
		for (Integer use : useIndexRow) {
			useIndexRowRight.add(use);
		}
		for (Integer use : miniNode.useIndexRow) {
			useIndexRowRight.remove(use);
		}
		rightNode.setUseIndexRow(useIndexRowRight);
		parentNode.rightSonTree = rightNode;
		if (miniNode.hasNextRev()) {
			buildTree(rightNode, useIndexRowRight, tempUseIndexCol);
		}
	}

	/**
	 * 打印
	 */
	public void print() {
		this.root.print();
	}
	/**
	 * 预测方法
	 * @param input
	 * @return
	 */
	public String forest(String[] input){
		return forest(input,this.root);
	}
	
	public String forest(String[] input,CartTreeNode node){
		String useString=input[node.leftSonTree.useIndexCol];
		if(useString.equals(node.leftSonTree.className))
		{//因为所有的都是左侧的为单一值 右侧为 ！值
			if(node.leftSonTree==null)
			{
				//targetP
				//已经到底部
				double max=0;
				String val=null;
				for(Entry<String,Double> m:node.targetP.entrySet()){
					if(max<m.getValue()){
						max=m.getValue();
						val=m.getKey();
					}
				}
				return val;
			}
			forest(input,node.leftSonTree);
		}else{
			if(node.leftSonTree==null)
			{
				//targetP
				//已经到底部
				double max=0;
				String val=null;
				for(Entry<String,Double> m:node.targetP.entrySet()){
					if(max<m.getValue()){
						max=m.getValue();
						val=m.getKey();
					}
				}
				return val;
			}
			forest(input,node.rightSonTree);
		}
		return null;
	}

	/**
	 * 执行程序
	 */
	public void exec(String[] inputData) {
		// 读取数据源
		if (inputData == null) {
			readDataFile();
		} else {
			readData(inputData);
		}
		buildTree();
	}
	/**
	 * 读取数据源
	 * @param dataModel
	 */
	public void exec(String[][] inputData,String[] tag){
		readData(inputData,tag);
		buildTree();
	}
	
	

	/**
	 * 减枝方法 误差率
	 */
	public void cutTree() {
		root.errorValue = getErrorValue(root);
		cutTree(root);

	}

	/**
	 * 减枝方法
	 */
	public void cutTree(CartTreeNode node) {
		// 获取对应的误差值
		if (node.leftSonTree != null) {
			node.leftSonTree.errorValue = getErrorValue(node.leftSonTree);
		}
		if (node.rightSonTree != null) {
			node.rightSonTree.errorValue = getErrorValue(node.rightSonTree);
		}
		if (node.leftSonTree != null && node.rightSonTree != null) {
			double val = (node.leftSonTree.errorValue
					* node.leftSonTree.useIndexRow.length + node.rightSonTree.errorValue
					* node.rightSonTree.useIndexRow.length)
					/ (node.useIndexRow.length);
			//System.out.println(node.className+"\t"+node.errorValue+":"+val);
			// 计算是否需要剔除
			if (node.errorValue < val) {
				// 需要建掉
				node.leftSonTree = null;
				node.rightSonTree = null;
				return;
			} else if (node.errorValue == val) {
				// 则减掉误差教大的
				if (node.leftSonTree.errorValue <= node.rightSonTree.errorValue) {
					node.rightSonTree = null;
				}
			}
		}
		if (node.leftSonTree != null) {
			cutTree(node.leftSonTree);
		}
		if (node.rightSonTree != null) {
			cutTree(node.rightSonTree);
		}
	}

	/**
	 * 获取该节点的额误差
	 * 
	 * @param node
	 * @return
	 */
	public double getErrorValue(CartTreeNode node) {
		double error = 0d;
		// 获取使用的列
		int[] useIndex = node.useIndexRow;
		// 记录对应分类数量
		int[] count = new int[classifyClass.size()];
		// 计算自己的误差
		if (useIndex == null) {
			return 1;
		}
		for (int index : useIndex) {
			String lll = this.data[index][classNum];
			count[classifyClass.get(lll) - 1]++;
		}
		// 获取最大的值得
		int max = 0;
		int all = 0;
		for (int val : count) {
			if (max < val) {
				max = val;
			}
			all += val;
		}
		error = max * 1d / all;
		return error;
	}

	public static void main(String[] args) {
		String[] stList = new String[] { "Youth High No Fair No",
				"Youth High No Excellent No", "MiddleAged High No Fair Yes",
				"Senior Medium No Fair Yes", "Senior Low Yes Fair Yes",
				"Senior Low Yes Excellent No",
				"MiddleAged Low Yes Excellent No", "Youth Medium No Fair No",
				"Youth Low Yes Fair Yes", "Senior Medium Yes Fair Yes",
				"Youth Medium Yes Excellent Yes",
				"MiddleAged Medium No Excellent Yes",
				"MiddleAged High Yes Fair Yes", "Senior Medium No Excellent No" };
		// CARTTool cart=new CARTTool("");
		// cart.startBuildingTree(stList);
		Cart cart = new Cart();
		cart.exec(stList);
		cart.print();
		cart.cutTree();
		cart.print();
	}
}
