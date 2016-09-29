package ps.hell.base.struct;

import java.util.LinkedList;

/**
 * 应用于不平平衡的多叉树
 * 并且 输入节点 必须人为手动为 无重复信息
 * @author Administrator
 *
 * @param <T>
 */
public class TableTree<T extends Comparable>{

	public Node root=null;
	//全部的深度
	public int allDeepNode=0;
	//节点总数
	public int allSizeLeaf=0;
	public TableTree()
	{
		root=new Node(null,0);
	}
	/**
	 * 返回该节点
	 * @param parment 父节点
	 * @param value 对应的String名
	 * @return
	 */
	public Node add(Node parment,Object value,int deep)
	{
		Node temp=parment.add(value,deep);
		if(allDeepNode<deep)
		{
			allDeepNode=deep;
		}
		allSizeLeaf++;
		return temp;
	}
	public class Node
	{
		/**
		 * 父节点
		 */
		Node parentNode=null;
		/**
		 * 左节点
		 */
		LinkedList<Node> sonNode=new LinkedList<Node>();
		Object value=null;
		int deep=0;
		Node(Object value,int deep)
		{
			this.value=value;
			this.deep=deep;
		}
		public Node add(Object value,int deep)
		{
			Node temp=new Node(value,deep);
			sonNode.add(temp);
			return temp;
		}
	}
}
