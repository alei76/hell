package ps.hell.ml.nlp.own.landerbuluse.participle.mmseg;

import java.util.Date;
import java.util.LinkedList;
import java.util.Vector;


////////////带索引点的二叉树
public class tree_fenci {
	private String name;
	private String value;
	private Node3 root;
	public static int size=0;//存储总关键字
	private static int size_length=0;//存储第一个关键字
	private fenci_read_source writetxt=new fenci_read_source(); 
	//private String filename;
	//存储三叉节点
	private static class Node3{
		Node3 left;
		Node3 right;
		Node center;
		private String name;//取第一个字符
		Node3(String name,String value)
		{
			this.left=null;
			this.right=null;
			//插入内部中间变量
			insert(name,value);
			this.name=name.substring(0,1);
		}
		public String name()
		{
			return this.name;
		}
		public Node3 left()
		{
			return this.left;
		}
		public Node3 right()
		{
			return this.right;
		}
		
		public void insert(String name,String value)
		{
			center=insert(center,name, value);
		}
		//递归插入节点
		public Node insert(Node node,String name,String value)
		{
			//System.out.println(name+ " 插入 " +value);
			if(node==null)
			{//如果为左右节点为空那么就将数据直接赋值
				node=new Node(name,value);
				size++;
			}
			else{
				if(name.compareTo(node.name())<0){
					//System.out.println("加入左侧");
					node.left=insert(node.left,name,value);
				}
				else if(name.compareTo(node.name())>0)
				{
					//System.out.println("加入右侧");
					node.right=insert(node.right,name,value);
				}
				else
				{//去除重复
					//判断是否有重复的
					System.out.println("重复");
					
				}
				
			}
			return node;
		}
		public void insert_add(String name,String value)
		{
			center=insert_add(center,name, value);
		}
		//递归插入节点
		public Node insert_add(Node node,String name,String value)
		{
			//System.out.println(name+ " 插入 " +value);
			if(node==null)
			{//如果为左右节点为空那么就将数据直接赋值
				node=new Node(name,value);
				size++;
			}
			else{
				if(name.compareTo(node.name())<0){
					//System.out.println("加入左侧");
					node.left=insert_add(node.left,name,value);
				}
				else if(name.compareTo(node.name())>0)
				{
					//System.out.println("加入右侧");
					node.right=insert_add(node.right,name,value);
				}
				else
				{//去除重复
					//判断是否有重复的
					node.value=Integer.toString(Integer.parseInt(node.value)+Integer.parseInt(value));
					//System.out.println("重复:"+Integer.parseInt(node.value)+":"+Integer.parseInt(value));
					//System.out.println(node.value);
					
				}
				
			}
			return node;
		}
	
	}
	
	//节点输入数据
	//添加NODE3的search方法
	public String node3_search(Node3 node,String name)
	{
		String value="";
		if(node==null)
		{
			System.out.println("索引中不存在该第一个字");
			return value;
		}
		if(name.substring(0,1).compareTo(node.name.substring(0,1))==0)
		{
			//搜索二叉节点
			value=node_search(node.center,name);
		}
		else if(name.substring(0,1).compareTo(node.name.substring(0,1))<0)
		{
			value=node3_search(node.left,name);
		}
		else
		{
			value=node3_search(node.right,name);
		}
		return value;
	}
	//建立节点属性//存储二叉树节点
	private static class Node{
		Node left;
		Node right;
		private String name;
		private String value;
		Node(String name,String value)
		{
			this.left=null;
			this.right=null;
			this.name=name;
			this.value=value;
		}
		public String name()
		{
			return this.name;
		}
		public String value()
		{
			return this.value;
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
	//节点输入数据
	//添加node的search方法
	public String node_search(Node node,String name)
	{
		String value="";
		if(node==null)
		{
			System.out.println(name+":查询的字不在字典中");
			if(name.length()==1)
			{
				value="1";
			}
			return value;
		}
		else
		{
			if(name.compareTo(node.name)==0)
			{
				//搜索二叉节点
				value=node.value;
			}
			else if(name.compareTo(node.name)<0)
			{
				value=node_search(node.left,name);
			}
			else
			{
				value=node_search(node.right,name);
		}
		}
		return value;
	}
	//递归插入二叉树；
	//查询ca匹配出以ca开头的所有词
	public Vector<Vector<String>> node_search_words(Node node,String name)
	{
		Vector<Vector<String>>value =new Vector<Vector<String>>();
		
		if(node==null)
		{
			return value;
		}
		else
		{
			String name_temp=node.name;
			if(name.compareTo(node.name.substring(0,node.name.length()<name.length()?node.name.length():name.length()))==0)
			{
				//搜索二叉节点
				Vector<String> temp1=new Vector<String>();
				temp1.add(node.name);
				temp1.add(node.value);
				value.add(temp1);
				Vector<Vector<String>> temp=new Vector<Vector<String>>();
				temp=node_search_words(node.left,name);
				for(int i=0;i<temp.size();i++)
				value.add(temp.elementAt(i));
				Vector<Vector<String>> temp2=new Vector<Vector<String>>();
				temp2=node_search_words(node.right,name);
				for(int i=0;i<temp2.size();i++)
					value.add(temp2.elementAt(i));
				
			}
			else if(name.compareTo(node.name.substring(0,node.name.length()<name.length()?node.name.length():name.length()))<0)
			{
				Vector<Vector<String>> temp=new Vector<Vector<String>>();
				temp=node_search_words(node.left,name);
				for(int i=0;i<temp.size();i++)
				value.add(temp.elementAt(i));
			}
			else
			{
				Vector<Vector<String>> temp=new Vector<Vector<String>>();
				temp=node_search_words(node.right,name);
				for(int i=0;i<temp.size();i++)
					value.add(temp.elementAt(i));
		}
		}
		return value;
	}
	
	//搜索一个词头并返回所有可能的后续
	public Vector<Vector<String>> node3_search_words(String name)
	{
		Vector<Vector<String>> ll=new Vector<Vector<String>>();
		ll=node3_search_words(this.root,name);
		return ll;
	}
	public Vector<Vector<String>> node3_search_words(Node3 node,String name)
	{
		Vector<Vector<String>> value=new Vector<Vector<String>>();
		if(node==null)
		{
			System.out.println("索引中不存在该第一个字");
			
			if(name.length()==1)
			{
				Vector<String> value_temp=new Vector<String>();
				value_temp.add(name);
				value_temp.add("1");
				value.add(value_temp);
			}
			return value;
		}
		if(name.substring(0,1).compareTo(node.name.substring(0,1))==0)
		{
			//搜索二叉节点
			value=node_search_words(node.center,name);
		}
		else if(name.substring(0,1).compareTo(node.name.substring(0,1))<0)
		{
			value=node3_search_words(node.left,name);
		}
		else
		{
			value=node3_search_words(node.right,name);
		}
		return value;
	}
	
	public void insert3(String name,String value)
	{
		root=insert3(root,name, value);
	}
	//递归插入节点
	public Node3 insert3(Node3 node,String name,String value)
	{
		//System.out.println(name+ " 插入 " +value);
		if(node==null)
		{//如果为左右节点为空那么就将数据直接赋值
			node=new Node3(name,value);
			size_length++;
		}
		else{
			if(name.substring(0, 1).compareTo(node.name().substring(0,1))<0){
				//System.out.println("加入左侧");
				node.left=insert3(node.left,name,value);
			}
			else if(name.substring(0, 1).compareTo(node.name().substring(0,1))>0)
			{
				//System.out.println("加入右侧");
				node.right=insert3(node.right,name,value);
			}
			else
			{//去除重复
				//
				node.insert(name, value);//中心点二叉树
			}
			
		}
		return node;
	}
	//创建新的二叉树
	
	//爬去更新操作
	////////////////////
	public void insert3_add(String name,String value)
	{
		root=insert3_add(root,name, value);
	}
	public Node3 insert3_add(Node3 node,String name,String value)
	{
		//System.out.println(name+ " 插入 " +value);
		if(node==null)
		{//如果为左右节点为空那么就将数据直接赋值
			node=new Node3(name,value);
			size_length++;
		}
		else{
			if(name.substring(0, 1).compareTo(node.name().substring(0,1))<0){
				//System.out.println("加入左侧");
				node.left=insert3_add(node.left,name,value);
			}
			else if(name.substring(0, 1).compareTo(node.name().substring(0,1))>0)
			{
				//System.out.println("加入右侧");
				node.right=insert3_add(node.right,name,value);
			}
			else
			{//去除重复
				//
				node.insert_add(name, value);//中心点二叉树
			}
			
		}
		return node;
	}
	
	////////////////////////
	
	public tree_fenci(){
		root=null;
	}
	//二叉树插入
	public void buildtree(struct data)
	{
		//int i=0;
//		System.out.println(data.value().next());
		int j=data.size();
		
		for(int jj=0;jj<j;jj++)
		{
			if(jj%1000==0)
			System.out.println(jj+":"+new Date());
			//i++;
			//this.size_length=this.size_length>=data.name().get(jj).length()?this.size_length:data.name().get(jj).length();
			insert3(data.name().get(jj),data.value().get(jj));
			//this.size++;
		}
	}
	public int size_length()
	{
		return tree_fenci.size_length;
	}
	public int size()
	{
		return tree_fenci.size;
	}
	//二叉树打印方法
	public void print()
	{
		print3(this.root);
		System.out.println();
	}
	public void print_node(Node node)
	{
		if(node==null)
		{
			return;
		}
		print_node(node.left);
		System.out.println(node.name+":"+node.value+"\t");
		print_node(node.right);
	}
	private void print3(Node3 node)
	{
		if(node==null)
		return;
		print3(node.left);
		System.out.println(node.name+":::");
		print_node(node.center);
		print3(node.right);
	}
	public LinkedList<LinkedList<String>> get_print()
	{
		LinkedList<LinkedList<String>> strr=new LinkedList<LinkedList<String>>();
		strr=get_print3(this.root);
		return strr;
		//System.out.println();
	}
	public LinkedList<LinkedList<String>> get_print_node(Node node)
	{
		LinkedList<LinkedList<String>> strr=new LinkedList<LinkedList<String>>();
		if(node==null)
		{
			return strr;
		}
		LinkedList<LinkedList<String>> strr2=new LinkedList<LinkedList<String>>();
		strr2=get_print_node(node.left);
		if(strr2.isEmpty())
		{}
		else
		{//添加
		
			for(int i=0;i<strr2.size();i++)
			{
				LinkedList<String> str_temp=new LinkedList<String>();
				for(int j=0;j<strr2.get(i).size();j++)
				{
					str_temp.add(strr2.get(i).get(j));
				}
				strr.add(str_temp);
			}
		}
		LinkedList<String> str_temp1=new LinkedList<String>();
		str_temp1.add(node.name);
		str_temp1.add(node.value);
		strr.add(str_temp1);
		//System.out.println(node.name+":"+node.value+"\t");
		LinkedList<LinkedList<String>> strr3=new LinkedList<LinkedList<String>>();
		strr3=get_print_node(node.right);
		if(strr3.isEmpty())
		{}
		else
		{//添加			
			for(int i=0;i<strr3.size();i++)
			{
				LinkedList<String> str_temp=new LinkedList<String>();
				for(int j=0;j<strr3.get(i).size();j++)
				{
					str_temp.add(strr3.get(i).get(j));
				}
				strr.add(str_temp);
			}
		}
		return strr;
	}
	private LinkedList<LinkedList<String>> get_print3(Node3 node)
	{
		LinkedList<LinkedList<String>> strr=new LinkedList<LinkedList<String>>();
		if(node==null)
		return strr;
		LinkedList<LinkedList<String>> strr2=new LinkedList<LinkedList<String>>();
		strr2=get_print3(node.left);
		if(strr2.isEmpty())
		{}
		else
		{//添加
		
			for(int i=0;i<strr2.size();i++)
			{
				LinkedList<String> str_temp=new LinkedList<String>();
				for(int j=0;j<strr2.get(i).size();j++)
				{
					str_temp.add(strr2.get(i).get(j));
				}
				strr.add(str_temp);
			}
		}
		LinkedList<LinkedList<String>> strr3=new LinkedList<LinkedList<String>>();
		//System.out.println(node.name+":::");
		strr3=get_print_node(node.center);
		if(strr3.isEmpty())
		{}
		else
		{//添加
		
			for(int i=0;i<strr3.size();i++)
			{
				LinkedList<String> str_temp=new LinkedList<String>();
				for(int j=0;j<strr3.get(i).size();j++)
				{
					str_temp.add(strr3.get(i).get(j));
				}
				strr.add(str_temp);
			}
		}
		LinkedList<LinkedList<String>> strr4=new LinkedList<LinkedList<String>>();
		strr4=get_print3(node.right);
		if(strr4.isEmpty())
		{}
		else
		{//添加
		
			for(int i=0;i<strr4.size();i++)
			{
				LinkedList<String> str_temp=new LinkedList<String>();
				for(int j=0;j<strr4.get(i).size();j++)
				{
					str_temp.add(strr4.get(i).get(j));
				}
				strr.add(str_temp);
			}
		}
		return strr;
	}
	
	public void print_txt(String filename)
	{
		//this.filename=filename;
		this.writetxt.writefilename(filename); 
		print3_txt(this.root);
		//System.out.println();
	}
	public void print_node_txt(Node node)
	{
		if(node==null)
		{
			return;
		}
		print_node_txt(node.left);
		this.writetxt.writeFile(node.name+"\t"+node.value+"\r\n");
		//System.out.println("输出:"+node.name+":"+node.value+"\t");
		print_node_txt(node.right);
	}
	private int write_line=0;
	
	private void print3_txt(Node3 node)
	{
		if(node==null)
		return;
		print3_txt(node.left);
		System.out.println("总长度:"+tree_fenci.size_length+ "总数量"+size+":::"+this.write_line);
		this.write_line++;
		print_node_txt(node.center);
		print3_txt(node.right);
	}
	//查询方法
	public String search(String str)
	{
		String value=new String();
		value="";
		try
		{
		value=node3_search(this.root,str);
		}
		catch(Exception e)
		{
			value=null;
		}
		finally{
		return value;
		}
	}

}
