package ps.hell.ml.graph.base;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 图的搜索方法
 * @author Administrator
 *
 */
public class GraphMethod {
	/**
	 * 矩阵
	 */
	float[][] A;
//	/**
//	 * 连接名字映射
//	 */
//	HashMap<Integer,String> nameMapping=new HashMap<Integer,String>();
	/**
	 * 创建n*n的 图矩阵
	 * @param n
	 */
	public GraphMethod(int n) {
		A = new float[n][n];
		for (int i = 0; i < A.length; i++) {
			for (int j = 0; j < A.length; j++) {
				if (i == j)
					A[i][j] = 0;
				else
					A[i][j] = -1;
			}
		}

	}
	/**
	 * 使用的图 bean
	 */
	ArrayList<GraphBean> graphBeans = new ArrayList<GraphBean>();

	private double[] D;

	private void relax(int u, int v) {
		if (D[v] > D[u] + A[v][u])
			D[v] = D[u] + A[v][u];
	}
	/**
	 * 为 矩阵的 反序 >0 this
	 * i=i 为0
	 * 否则为 99999最大值
	 */
	private double[][] DD = null;

	/**
	 * 弗洛伊德沃肖尔 算法
	 */
	public void floydWarshall() {
		DD = new double[A.length][A.length];
		int i, j, k;
		for (i = 0; i < A.length; i++) {
			for (j = 0; j < A.length; j++) {
				if (A[i][j] > 0)
					DD[i][j] = A[i][j];
				else if (i == j)
					DD[i][j] = 0;
				else
					DD[i][j] = 99999999;

			}
		}
		for (k = 0; k < A.length; k++)
			for (i = 0; i < A.length; i++)
				for (j = 0; j < A.length; j++) {
					if (DD[i][j] > DD[i][k] + DD[k][j]) {
						DD[i][j] = DD[i][k] + DD[k][j];
					}
				}
	}

	public void printFloydWarshallForOneCity(GraphBean city) {
		System.out.println("floydWarshall:");
		if (DD == null) {
			floydWarshall();
		}
		for (int i = 0; i < A.length; i++) {
			System.out.printf("from %s to %s shortest path is:%f\n", city.name,
					graphBeans.get(i).name, DD[city.id][i]);
		}

	}
	/**
	 * 狄利克雷 算法
	 * @param city
	 */
	public void dijkstra(GraphBean city) {
		dijkstra(city.id);
		System.out.println("dijkstra:");
		for (int i = 0; i < A.length; i++) {
			System.out.printf("from %s to %s shortest path is:%f\n", city.name,
					graphBeans.get(i).name, D[i]);
		}
	}
	/**
	 * 狄利克雷 算法
	 * @param srcId
	 */
	public void dijkstra(int srcId) {
		D = new double[A.length];
		for (int i = 0; i < A.length; i++) {
			D[i] = 999999999;
		}
		D[srcId] = 0;
		int[] q = new int[A.length];
		int ql = 0, qf = 0; // 队列

		for (int i = 0; i < A.length; i++)
			q[ql++] = i;
		while (qf != ql) {
			int min = qf;
			for (int i = qf; i < ql; i++) {
				if (D[q[i]] < D[q[min]]) {
					min = i;
				}
			}
			int id = q[qf];
			q[qf] = q[min];
			q[min] = id; // q[qf] is the min
			int u = q[qf++];
			for (int i = 0; i < A.length; i++) {
				if (A[u][i] > 0) {
					relax(u, i);
				}
			}
		}
	}
	/**
	 * 贝尔曼福特 算法
	 * @param city
	 */
	public void bellmanFord(GraphBean city) {
		bellmanFord(city.id);
		System.out.println("bellmanFord:");
		for (int i = 0; i < A.length; i++) {
			System.out.printf("from %s to %s shortest path is:%f\n", city.name,
					graphBeans.get(i).name, D[i]);
		}
	}
	/**
	 * 贝尔曼福特 算法
	 */
	public void bellmanFord(int srcId) {
		D = new double[A.length];
		for (int i = 0; i < A.length; i++) {
			D[i] = 99999999;// 无穷大
		}
		D[srcId] = 0;
		for (int i = 0; i < A.length; i++)// 外层循环次数
		{
			for (int j = 0; j < A.length; j++) {
				for (int k = 0; k < A.length; k++) {
					if (A[j][k] > 0) {
						relax(j, k);
					}
				}
			}

		}
	}

	/**
	 * 广度优先使用的队列
	 */
	Queue<Integer> bfsQueue = new LinkedList<Integer>();
	/**
	 * 广度优先的状态
	 */
	boolean[] bfsFlag;
	/**
	 * 存储广度优先遍历对应的路径
	 */
	int bsfPre[];

	/**
	 * 获取 广度优先
	 * @param src
	 * @param dst
	 */
	public void findPathByBFS(GraphBean src, GraphBean dst) {
		System.out.printf("bfs find path between '%s' and '%s'!\n", src.name,
				dst.name);
		findPathByBFS(src.id, dst.id);
		printBFS(dst.id);

	}
	/**
	 * 广度优先遍历
	 * @param srcId
	 * @param dstId
	 */
	public void findPathByBFS(int srcId, int dstId) {
		bsfPre = new int[A.length];
		bfsQueue.clear();
		bfsFlag = new boolean[A.length];
		for (int i = 0; i < A.length; i++) {
			bfsFlag[i] = false;
			bsfPre[i] = -1;
		}

		bfsQueue.offer(srcId);
		bfsFlag[srcId] = true;

		while (!bfsQueue.isEmpty()) {
			int current = bfsQueue.poll();
			for (int index = 0; index < A.length; index++) {
				if (current == index)
					continue;
				if (A[current][index] > 0) // 两者相连
				{
					if (index == dstId)// 找到目标了
					{
						bfsFlag[index] = true;
						bsfPre[index] = current;
						return;// 直接返回
					}
					if (bfsFlag[index] == false)// 如果未访问过
					{
						bfsFlag[index] = true;
						bsfPre[index] = current;
						bfsQueue.offer(index);
					}
				}

			}
		}

	}

	/**
	 * 打印广度优先
	 * @param dstId
	 */
	private void printBFS(int dstId) {
		int index = dstId;

		do {
			System.out.printf("<-%s", graphBeans.get(index).name);
			index = bsfPre[index];
		} while (index != -1);
		System.out.println();
	}

	/**
	 * 深度优先
	 */
	ArrayList<Integer> dfsPath = new ArrayList<Integer>();
	/**
	 * 深度优先状态
	 */
	boolean[] dfsFlag;

	/**
	 * 打印深度优先
	 */
	private void printDFS() {
		for (Integer node : dfsPath) {
			System.out.printf("->%s", graphBeans.get(node).name);
		}
		System.out.println();
	}

	public void findPathByDFS(GraphBean src, GraphBean dst) {
		System.out.printf("dfs find path between '%s' and '%s'!\n", src.name,
				dst.name);
		findPathByDFS(src.id, dst.id);
	}
	/**
	 * 深度优先查找
	 * @param srcId
	 * @param dstId
	 */
	public void findPathByDFS(int srcId, int dstId) {
		dfsPath.clear();
		dfsFlag = new boolean[A.length];
		for (int i = 0; i < A.length; i++) {
			dfsFlag[i] = false;
		}
		dfsPath.add(srcId);
		dfsFlag[srcId] = true;
		dfs(srcId, dstId);
		printDFS();
	}

	/**
	 * 深度优先
	 * 获取 id 到id 的深度优先遍历方法
	 * @param srcId
	 * @param dstId
	 */
	private void dfs(int srcId, int dstId) {
		for (int index = 0; index < A[srcId].length; index++) {
			if (srcId == index)
				continue;
			if (A[srcId][index] > 0)// 两者连接
			{
				if (index == dstId)// 找到目标了
				{
					dfsFlag[index] = true;
					dfsPath.add(index);
					return;
				}
				if (dfsFlag[index] == false)// 如果该节点未访问过
				{
					dfsFlag[index] = true;
					dfsPath.add(index);
					dfs(index, dstId);
					if (dfsFlag[dstId] == false)// 目标没找到
						dfsPath.remove(index);
					else
						return;
				}

			}
		}
	}

	/**
	 * 创建边
	 * @param a
	 * @param b
	 * @param w
	 */
	public void createEdge(GraphBean a, GraphBean b,float w) {
		A[a.id][b.id] = w;
		A[b.id][a.id] = w;// added by me!
		graphBeans.add(a.id, a);
		graphBeans.add(b.id, b);
	}

	/**
	 * 转换为string
	 */
	@Override
	public String toString() {
		String r = "the graph model size:" + A.length;
		r += "\n\t";
		for(int i=0;i<A.length;i++)
		{
			r+=graphBeans.get(i).name+"\t";
		}
		r+="\n";
		for (int i = 0; i < A.length; i++) {
			r+=graphBeans.get(i).name+"\t";
			for (int j = 0; j < A[0].length; j++)
			{
				r += A[i][j] + "\t";
			}
			r += "\n";
		}
		return r;
	}
}