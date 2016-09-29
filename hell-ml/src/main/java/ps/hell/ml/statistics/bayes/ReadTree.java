package ps.hell.ml.statistics.bayes;

/**
 * 读取文件树
 * @author Administrator
 *
 */
import java.util.LinkedList;


////////////带索引点的二叉树
public class ReadTree {

	public LinkedList<String> nameList=new LinkedList<String>();
	public LinkedList<Integer> valueList=new LinkedList<Integer>();
	public Node root;
	public int size=0;//存储总关键字
	//private String filename;
	//建立节点属性//存储二叉树节点
	public class Node{
		Node left;
		Node right;
		String name;
	 	int value;
		Node(String name)
		{
			this.left=null;
			this.right=null;
			this.name=name;
			this.value=1;
		}
		Node(String name,int value)
		{
			this.left=null;
			this.right=null;
			this.name=name;
			this.value=value;
		}
		public Node left()
		{
			return this.left;
		}
		public Node right()
		{
			return this.right;
		}
	}

	/**
	 * 添加内容
	 * @param userName
	 * @param userItemGroupFirst
	 */
	public void add(String name,int value)
	{
		root=add(root,name);
		size++;
	}
	public Node add(Node node,String name)
	{
		if(node==null)
		{//如果为左右节点为空那么就将数据直接赋值
			node=new Node(name);
			size++;
		}
		else{
			if(name.compareTo(node.name)<0){
				//System.out.println("加入左侧");
				node.left=add(node.left,name);
			}
			else if(name.compareTo(node.name)>0)
			{
				//System.out.println("加入右侧");
				node.right=add(node.right,name);
			}
			else
			{//去除重复
				//判断是否有重复的
				node.value++;
			}
			
		}
		return node;
	}

	
	
	public int search(String name)
	{
		return search(root,name);
	}
	public int search(Node node,String name)
	{
		if(node==null)
		{//如果为左右节点为空那么就将数据直接赋值
			return 0;
		}
		else{
			if(name.compareTo(node.name)<0){
				//System.out.println("加入左侧");
				return search(node.left,name);
			}
			else if(name.compareTo(node.name)>0)
			{
				//System.out.println("加入右侧");
				return search(node.right,name);
			}
			else
			{//去除重复
				//判断是否有重复的
				return node.value;
			}
			
		}
	}
	/**
	 * 插入内容
	 * @param userName
	 * @param userItemGroupFirst
	 */
	public void insert(String name,int value)
	{
		root=insert(root,name,value);
		size++;
	}
	public Node insert(Node node,String name,int value)
	{
		if(node==null)
		{//如果为左右节点为空那么就将数据直接赋值
			node=new Node(name,value);
			size++;
		}
		else{
			if(name.compareTo(node.name)<0){
				//System.out.println("加入左侧");
				node.left=insert(node.left,name,value);
			}
			else if(name.compareTo(node.name)>0)
			{
				//System.out.println("加入右侧");
				node.right=insert(node.right,name,value);
			}
			else
			{//去除重复
				//判断是否有重复的
			}
			
		}
		return node;
	}
	
	public void print()
	{
		print(this.root);
		System.out.println();
	}
	public void print(Node node)
	{
		if(node==null)
		{
			return;
		}
		print(node.left);
		this.nameList.add(node.name);
		this.valueList.add(node.value);
		System.out.println(node.name+":"+node.value+"\t");
		print(node.right);
	}
	
}

