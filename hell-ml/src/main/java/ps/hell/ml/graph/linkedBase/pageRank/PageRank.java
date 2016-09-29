package ps.hell.ml.graph.linkedBase.pageRank;

import java.util.HashMap;

/**
 * pageRank
 * 
 * @author Administrator
 *
 */
public class PageRank {
	/**
	 * 连接矩阵
	 */
	HashMap<Integer, HashMap<Integer, Float>> matrix =null;
	/**
	 * 稀疏矩阵源
	 * @param matrix
	 */
	public PageRank(HashMap<Integer, HashMap<Integer, Float>> matrix)
	{
		 this.matrix=matrix;
	}
	
	
	public void run() {

	}
}
