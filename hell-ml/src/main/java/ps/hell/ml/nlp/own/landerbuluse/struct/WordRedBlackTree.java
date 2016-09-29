package ps.hell.ml.nlp.own.landerbuluse.struct;
import java.util.*;  
  /**
   * 该类红黑数继成了红黑数的平衡二叉树特点
   * 并构造成如果第一个字符相同则对应的内容节点存储成TreeMap数据
   * @author Administrator
   *
   * @param <T>
   */
public class WordRedBlackTree<T extends Comparable>
{  
    //定义红黑树的颜色  
    private static final boolean RED   = false;  
    private static final boolean BLACK = true;  
    static class Node  
    {  
    	//用于存储索引值
        int data;  
        Node parent;   
        Node left;  
        Node right;  
        //节点的默认颜色是黑色  
        boolean color = BLACK;
        //用于存所有词频为首字母的词
        HashMap<WordFather,WordFather> map=new HashMap<WordFather,WordFather>();
        public Node(WordFather data , Node parent   
            , Node left , Node right)  
        {  
            this.data = data.word.substring(0,1).hashCode();  
            this.parent = parent;  
            this.left = left;  
            this.right = right; 
            map.put(data,data);
        }  
        @Override
		public String toString()  
        {  
            return "[data=" + data  
                + ", color=" + color + "]";   
        }  
        @Override
		public boolean equals(Object obj)  
        {  
            if (this == obj)  
            {  
                return true;  
            }  
            if (obj.getClass() == Node.class)  
            {  
                Node target = (Node)obj;  
                return data==target.data  
                    && color == target.color  
                    && left == target.left  
                    && right == target.right  
                    && parent == target.parent;  
            }  
            return false;  
        }  
    }  
    private Node root;  
    //两个构造器用于创建排序二叉树  
    public WordRedBlackTree()  
    {  
        root = null;  
    }  
    public WordRedBlackTree(WordFather o)  
    {  
        root = new Node(o , null , null , null);  
    }  
    //添加节点  
    public void add(WordFather ele)  
    {  
        //如果根节点为null  
        if (root == null)  
        {  
            root = new Node(ele , null , null , null);  
        }  
        else  
        {  
            Node current = root;  
            Node parent = null;  
            int cmp = 0;  
            //搜索合适的叶子节点，以该叶子节点为父节点添加新节点  
            int ind=ele.word.substring(0,1).hashCode();
            do  
            {  
                parent = current;  
               // System.out.println(ele.word+"\t"+ele.word.substring(0,1)+"\t"+current.data);
                cmp = ind-current.data; 
//                if(ele.word.startsWith("民"))
//            	{
//                	System.out.println(ele.word+"\t"+current.data+"\t:"+ele.word.substring(0,1).hashCode());
//            	}
                //如果新节点的值大于当前节点的值  
                if (cmp > 0)  
                {  
                    //以右子节点作为当前节点  
                    current = current.right;  
                }  
                //如果新节点的值小于当前节点的值  
                else  if(cmp<0)
                {  
                    //以左子节点作为当前节点  
                    current = current.left;  
                }  
                else if(cmp ==0)
                {
                	//如果为0则需要补充对应的HashMap
                	current.map.put(ele, ele);
//                	if(ele.word.startsWith("民"))
//                	{
//                	System.out.println("添加："+ele.word+"\t"+ele.word.substring(0,1).hashCode());
//                	System.out.println(current.map.size());
//                	}
                	return;
                }
            }  
            while (current != null);  
            //创建新节点  
            Node newNode = new Node(ele , parent , null , null);  
            //如果新节点的值大于父节点的值  
            if (cmp > 0)  
            {  
                //新节点作为父节点的右子节点  
                parent.right = newNode;  
            }  
            //如果新节点的值小于父节点的值  
            else  
            {  
                //新节点作为父节点的左子节点  
                parent.left = newNode;  
            }  
            //维护红黑树  
            fixAfterInsertion(newNode);  
        }  
    }
    //删除节点  
    public void remove(WordFather ele)  
    {  
        //获取要删除的节点  
        Node target = getNode(ele);
        if(target.map.size()>1)
        {
        	target.map.remove(ele.word);
        	return;
        }
        //如果改点信息已经清空则需要执行红黑数的移动方法
        //如果被删除节点的左子树、右子树都不为空  
        if (target.left != null && target.right != null)   
        {  
            //找到target节点中序遍历的前一个节点  
            //s用于保存target节点的左子树中值最大的节点  
            Node s = target.left;  
            //搜索target节点的左子树中值最大的节点  
            while (s.right != null)  
            {  
                s = s.right;  
            }  
            //用s节点来代替p节点  
            target.data = s.data;  
            target = s;  
        }   
        //开始修复它的替换节点，如果该替换节点不为null  
        Node replacement = (target.left != null ? target.left : target.right);  
        if (replacement != null)   
        {  
            // 让replacement的parent指向target的parent  
            replacement.parent = target.parent;  
            //如果target的parent为null，表明target本身是根节点  
            if (target.parent == null)  
            {  
                root = replacement;  
            }  
            //如果target是其父节点的左子节点  
            else if (target == target.parent.left)  
            {  
                //让target的父节点left指向replacement  
                target.parent.left  = replacement;  
            }  
            //如果target是其父节点的右子节点  
            else  
            {  
                //让target的父节点right指向replacement  
                target.parent.right = replacement;  
            }  
            //彻底删除target节点  
            target.left = target.right = target.parent = null;  
  
            // 修复红黑树  
            if (target.color == BLACK)  
            {  
                fixAfterDeletion(replacement);  
            }  
        }  
        //target本身是根节点  
        else if (target.parent == null)   
        {  
            root = null;  
        }   
        else   
        {  
            //target没有子节点，把它当成虚的替换节点。  
            //修复红黑树  
            if (target.color == BLACK)  
            {  
                fixAfterDeletion(target);  
            }  
            if (target.parent != null)   
            {  
                //如果target是其父节点的左子节点  
                if (target == target.parent.left)  
                {  
                    //将target的父节点left设为null  
                    target.parent.left = null;  
                }  
                //如果target是其父节点的右子节点  
                else if (target == target.parent.right)  
                {  
                    //将target的父节点right设为null  
                    target.parent.right = null;  
                }  
                //将target的parent设置null  
                target.parent = null;  
            }  
        }  
    }  
    //根据给定的值搜索节点  
    public Node getNode(WordFather ele)  
    {  
        //从根节点开始搜索  
        Node p = root;
        int temp=ele.word.substring(0,1).hashCode();
        while (p != null)   
        {  
            int cmp = temp-p.data;  
            //如果搜索的值小于当前p节点的值  
            if (cmp < 0)  
            {  
                //向左子树搜索  
                p = p.left;  
            }  
            //如果搜索的值大于当前p节点的值  
            else if (cmp > 0)  
            {  
                //向右子树搜索  
                p = p.right;  
            }  
            else  
            {  
                return p;  
            }  
        }  
        return null;  
    } 
  //通过ele获取以0为开头的组
    public HashMap<WordFather,WordFather> getWords(String ele)  
    {  
        //从根节点开始搜索  
        Node p = root;  
        int temp= ele.substring(0,1).hashCode();
        while (p != null)   
        {  
            int cmp =temp-p.data;  
            //如果搜索的值小于当前p节点的值  
            if (cmp < 0)  
            {  
                //向左子树搜索  
                p = p.left;  
            }  
            //如果搜索的值大于当前p节点的值  
            else if (cmp > 0)  
            {  
                //向右子树搜索  
                p = p.right;  
            }  
            else  
            {  
                return p.map;  
            }  
        }  
        return null;  
    } 
    //广度优先遍历  
    public List<Node> breadthFirst()  
    {  
        Queue<Node> queue = new ArrayDeque<Node>();  
        List<Node> list = new ArrayList<Node>();  
        if( root != null)  
        {  
            //将根元素入“队列”  
            queue.offer(root);  
        }  
        while(!queue.isEmpty())  
        {  
            //将该队列的“队尾”的元素添加到List中  
            list.add(queue.peek());  
            Node p = queue.poll();  
            //如果左子节点不为null，将它入“队列”  
            if(p.left != null)  
            {  
                queue.offer(p.left);  
            }  
            //如果右子节点不为null，将它入“队列”  
            if(p.right != null)  
            {  
                queue.offer(p.right);  
            }  
        }  
        return list;  
    }  
    //插入节点后修复红黑树  
    private void fixAfterInsertion(Node x)   
    {  
        x.color = RED;  
        //直到x节点的父节点不是根，且x的父节点不是红色  
        while (x != null && x != root   
            && x.parent.color == RED)   
        {  
            //如果x的父节点是其父节点的左子节点  
            if (parentOf(x) == leftOf(parentOf(parentOf(x))))   
            {  
                //获取x的父节点的兄弟节点  
                Node y = rightOf(parentOf(parentOf(x)));  
                //如果x的父节点的兄弟节点是红色  
                if (colorOf(y) == RED)   
                {  
                    //将x的父节点设为黑色  
                    setColor(parentOf(x), BLACK);  
                    //将x的父节点的兄弟节点设为黑色  
                    setColor(y, BLACK);  
                    //将x的父节点的父节点设为红色  
                    setColor(parentOf(parentOf(x)), RED);  
                    x = parentOf(parentOf(x));  
                }  
                //如果x的父节点的兄弟节点是黑色  
                else  
                {  
                    //如果x是其父节点的右子节点  
                    if (x == rightOf(parentOf(x)))   
                    {  
                        //将x的父节点设为x  
                        x = parentOf(x);  
                        rotateLeft(x);  
                    }  
                    //把x的父节点设为黑色  
                    setColor(parentOf(x), BLACK);  
                    //把x的父节点的父节点设为红色  
                    setColor(parentOf(parentOf(x)), RED);  
                    rotateRight(parentOf(parentOf(x)));  
                }  
            }   
            //如果x的父节点是其父节点的右子节点  
            else   
            {  
                //获取x的父节点的兄弟节点  
                Node y = leftOf(parentOf(parentOf(x)));  
                //如果x的父节点的兄弟节点是红色  
                if (colorOf(y) == RED)   
                {  
                    //将x的父节点设为黑色。  
                    setColor(parentOf(x), BLACK);  
                    //将x的父节点的兄弟节点设为黑色  
                    setColor(y, BLACK);  
                    //将x的父节点的父节点设为红色  
                    setColor(parentOf(parentOf(x)), RED);  
                    //将x设为x的父节点的节点  
                    x = parentOf(parentOf(x));  
                }  
                //如果x的父节点的兄弟节点是黑色  
                else   
                {  
                    //如果x是其父节点的左子节点  
                    if (x == leftOf(parentOf(x)))   
                    {  
                        //将x的父节点设为x  
                        x = parentOf(x);  
                        rotateRight(x);  
                    }  
                    //把x的父节点设为黑色  
                    setColor(parentOf(x), BLACK);  
                    //把x的父节点的父节点设为红色  
                    setColor(parentOf(parentOf(x)), RED);  
                    rotateLeft(parentOf(parentOf(x)));  
                }  
            }  
        }  
        //将根节点设为黑色  
        root.color = BLACK;  
    }  
    //删除节点后修复红黑树  
    private void fixAfterDeletion(Node x)   
    {  
        //直到x不是根节点，且x的颜色是黑色  
        while (x != root && colorOf(x) == BLACK)   
        {  
            //如果x是其父节点的左子节点  
            if (x == leftOf(parentOf(x)))  
            {  
                //获取x节点的兄弟节点  
                Node sib = rightOf(parentOf(x));  
                //如果sib节点是红色  
                if (colorOf(sib) == RED)  
                {  
                    //将sib节点设为黑色  
                    setColor(sib, BLACK);  
                    //将x的父节点设为红色  
                    setColor(parentOf(x), RED);  
                    rotateLeft(parentOf(x));  
                    //再次将sib设为x的父节点的右子节点  
                    sib = rightOf(parentOf(x));  
                }  
                //如果sib的两个子节点都是黑色  
                if (colorOf(leftOf(sib)) == BLACK  
                    && colorOf(rightOf(sib)) == BLACK)   
                {  
                    //将sib设为红色  
                    setColor(sib, RED);  
                    //让x等于x的父节点  
                    x = parentOf(x);  
                }   
                else   
                {  
                    //如果sib的只有右子节点是黑色  
                    if (colorOf(rightOf(sib)) == BLACK)   
                    {  
                        //将sib的左子节点也设为黑色  
                        setColor(leftOf(sib), BLACK);  
                        //将sib设为红色  
                        setColor(sib, RED);  
                        rotateRight(sib);  
                        sib = rightOf(parentOf(x));  
                    }  
                    //设置sib的颜色与x的父节点的颜色相同  
                    setColor(sib, colorOf(parentOf(x)));  
                    //将x的父节点设为黑色  
                    setColor(parentOf(x), BLACK);  
                    //将sib的右子节点设为黑色  
                    setColor(rightOf(sib), BLACK);  
                    rotateLeft(parentOf(x));  
                    x = root;  
                }  
            }  
            //如果x是其父节点的右子节点  
            else  
            {  
                //获取x节点的兄弟节点  
                Node sib = leftOf(parentOf(x));  
                //如果sib的颜色是红色  
                if (colorOf(sib) == RED)   
                {  
                    //将sib的颜色设为黑色  
                    setColor(sib, BLACK);  
                    //将sib的父节点设为红色  
                    setColor(parentOf(x), RED);  
                    rotateRight(parentOf(x));  
                    sib = leftOf(parentOf(x));  
                }  
                //如果sib的两个子节点都是黑色  
                if (colorOf(rightOf(sib)) == BLACK   
                    && colorOf(leftOf(sib)) == BLACK)   
                {  
                    //将sib设为红色  
                    setColor(sib, RED);  
                    //让x等于x的父节点  
                    x = parentOf(x);  
                }  
                else   
                {  
                    //如果sib只有左子节点是黑色  
                    if (colorOf(leftOf(sib)) == BLACK)   
                    {  
                        //将sib的右子节点也设为黑色  
                        setColor(rightOf(sib), BLACK);  
                        //将sib设为红色  
                        setColor(sib, RED);  
                        rotateLeft(sib);  
                        sib = leftOf(parentOf(x));  
                    }  
                    //将sib的颜色设为与x的父节点颜色相同  
                    setColor(sib, colorOf(parentOf(x)));  
                    //将x的父节点设为黑色  
                    setColor(parentOf(x), BLACK);  
                    //将sib的左子节点设为黑色  
                    setColor(leftOf(sib), BLACK);  
                    rotateRight(parentOf(x));  
                    x = root;  
                }  
            }  
        }  
        setColor(x, BLACK);  
    }  
    //获取指定节点的颜色  
    private boolean colorOf(Node p)  
    {  
        return (p == null ? BLACK : p.color);  
    }  
    //获取指定节点的父节点  
    private Node parentOf(Node p)   
    {  
        return (p == null ? null: p.parent);  
    }  
    //为指定节点设置颜色  
    private void setColor(Node p, boolean c)  
    {  
        if (p != null)  
        {  
            p.color = c;  
        }  
    }  
    //获取指定节点的左子节点  
    private Node leftOf(Node p)   
    {  
        return (p == null) ? null: p.left;  
    }  
    //获取指定节点的右子节点  
    private Node rightOf(Node p)   
    {  
        return (p == null) ? null: p.right;  
    }  
    /** 
     * 执行如下转换 
     *  p        r 
     *     r   p    
     *  q        q 
     */  
    private void rotateLeft(Node p)   
    {  
        if (p != null)   
        {  
            //取得p的右子节点  
            Node r = p.right;  
            Node q = r.left;  
            //将r的左子节点链到p的右节点链上  
            p.right = q;  
            //让r的左子节点的parent指向p节点  
            if (q != null)  
            {  
                q.parent = p;  
            }  
            r.parent = p.parent;  
            //如果p已经是根节点  
            if (p.parent == null)  
            {  
                root = r;  
            }  
            //如果p是其父节点的左子节点  
            else if (p.parent.left == p)  
            {  
                //将r设为p的父节点的左子节点  
                p.parent.left = r;  
            }  
            else  
            {  
                //将r设为p的父节点的右子节点  
                p.parent.right = r;  
            }  
            r.left = p;  
            p.parent = r;  
        }  
    }  
    /** 
     * 执行如下转换 
     *     p       l 
     *  l              p 
     *     q       q 
     */  
    private void rotateRight(Node p)   
    {  
        if (p != null)  
        {  
            //取得p的左子节点  
            Node l = p.left;  
            Node q = l.right;  
            //将l的右子节点链到p的左节点链上  
            p.left = q;  
            //让l的右子节点的parent指向p节点  
            if (q != null)   
            {  
                q.parent = p;  
            }  
            l.parent = p.parent;  
            //如果p已经是根节点  
            if (p.parent == null)  
            {  
                root = l;  
            }  
            //如果p是其父节点的右子节点  
            else if (p.parent.right == p)  
            {  
                //将l设为p的父节点的右子节点  
                p.parent.right = l;  
            }  
            else   
            {  
                //将l设为p的父节点的左子节点  
                p.parent.left = l;  
            }  
            l.right = p;  
            p.parent = l;  
        }  
    }  
    //实现中序遍历  
    public List<Node> inIterator()  
    {  
        return inIterator(root);  
    }  
    private List<Node> inIterator(Node node)  
    {  
        List<Node> list = new ArrayList<Node>();  
        //递归处理左子树  
        if (node.left != null)  
        {  
            list.addAll(inIterator(node.left));  
        }  
        //处理根节点  
        list.add(node);  
        //递归处理右子树  
        if (node.right != null)  
        {  
            list.addAll(inIterator(node.right));  
        }  
        return list;  
    }  
      
    public static void main(String[] args)   
    {  
//        WordRedBlackTree<> tree   
//            = new WordRedBlackTree<Integer>();  
//        //添加节点  
//        tree.add(5);  
//        tree.add(20);  
//        tree.add(10);  
//        tree.add(3);  
//        tree.add(8);  
//        tree.add(15);  
//        tree.add(30);  
//        System.out.println(tree.breadthFirst());  
//        //删除节点  
//        tree.remove(20);  
//        System.out.println(tree.breadthFirst());  
//		System.out.println(tree.inIterator());  
          
    }  
}  