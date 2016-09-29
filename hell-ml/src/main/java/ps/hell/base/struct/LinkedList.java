package ps.hell.base.struct;

/**
 * 
 * @author Administrator
 * 
 */
public class LinkedList<T extends Comparable<T>> {

	Node<T> root;
	Node<T> end;
	long size = 0;

	public LinkedList() {
	}

	public Node<T> getRoot() {
		return root;
	}

	public void setRoot(Node<T> root) {
		this.root = root;
	}

	private static class Node<T extends Comparable<T>> {
		T data;
		Node<T> leftPix;
		Node<T> rightPix;

		Node(T data) {
			this.data = data;
		}

		@Override
		public Node<T> clone() {
			Node<T> temp = new Node<T>(this.data);
			temp.leftPix = this.leftPix;
			temp.rightPix = this.rightPix;
			return temp;
		}
	}

	// 链表 右侧添加
	public void addRight(T node) {
		if (root == null) {
			root = new Node<T>(node);
			end = root.clone();
		} else {
			addRightNode(node,root);
		}
		size++;
	}

	// 右侧添加 node方法
	public void addRightNode(T node, Node<T> pix) {
		if (pix.rightPix == null) {
			pix.rightPix = new Node<T>(node);
			pix.rightPix.leftPix = pix;
			end = pix.rightPix;
		} else {
			addRightNode(node, pix.rightPix);
		}
	}

	// 链表 左侧添加
	public void addLeft(T node) {
		if (root == null) {
			root = new Node<T>(node);
			end = root.clone();
		} else {
			addLeftNode(node, root);
		}
		size++;
	}

	// 左侧添加 node方法
	public void addLeftNode(T node, Node<T> pix) {
		if (pix.leftPix == null) {
			pix.leftPix = new Node<T>(node);
			pix.leftPix.rightPix = root.clone();
			root = pix.leftPix;
		} else {
			addLeftNode(node, pix.leftPix);
		}
	}

	// 链表 左侧添加
	public void add(long index, T node) {
		if (root == null) {
			// root=new Node<T>(node);
			// end=root.clone();
			if (index == 0) {
				root = new Node<T>(node);
				end = root.clone();
			}
		} else {
			addNode(1L, index, node, root);
		}
		size++;
	}

	// 左侧添加 node方法
	public void addNode(long indexAdd, long index, T node, Node<T> pix) 
	{
		if (indexAdd == index) {
			if (index == size) {
				pix.rightPix = new Node<T>(node);
				pix.rightPix.leftPix = pix;
				end = pix.rightPix;
			} else {
				Node<T> temp = pix.rightPix;
				pix.rightPix = new Node<T>(node);
				pix.rightPix.rightPix = temp;
				temp.leftPix = pix.rightPix;
				pix.rightPix.leftPix = pix;
			}
		} else {
			addNode(indexAdd + 1, index, node, pix.rightPix);
		}

	}

	// 链表删除
	public void move(long index) {
		if(index>size)
		{
			System.out.println("out size of linkedList");
			return;
		}
		if (root == null) {
			// root=new Node<T>(node);
			// end=root.clone();
			if (index == 0) {
				root = null;
				end = null;
			}
		} else {
			moveNode(1L, index, root);
		}
		size--;
	}

	// 删除添加 node方法
	public void moveNode(long indexAdd, long index, Node<T> pix) 
	{
		if (indexAdd == index) {
			if (index+1 == size) {
				pix.rightPix =null;
				end = pix;
			} else {
				pix.rightPix.rightPix.leftPix=pix;
				pix.rightPix=pix.rightPix.rightPix;
			}
		} else {
			moveNode(indexAdd + 1, index, pix.rightPix);
		}

	}
	// 打印方法
	public void print() {
		printRightNode(root);
		System.out.println("end:" + end.data);
	}

	// 打印方法 node
	public void printRightNode(Node<T> pix) {
		if (pix == null) {
			return;
		} else {
			System.out.println(pix.data);
			printRightNode(pix.rightPix);
		}
	}

	public static void main(String[] args) {
		LinkedList<String> test = new LinkedList<String>();
		test.addRight("a");
		test.addRight("b");
		test.addRight("c");
		test.addLeft("Z");
		test.add(1, "A");
		test.add(5, "A");
		test.print();
		test.move(3);
		test.print();

	}
}
