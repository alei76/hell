package ps.hell.ml.statistics.bayes;

import java.util.HashMap;
import java.util.Map.Entry;

public class BayesTree {

	public HashMap<String, BayesTree> parent = new HashMap<String, BayesTree>();

	public HashMap<String, BayesTree> son = new HashMap<String, BayesTree>();

	public String name = null;

	public float value = 0f;

	public BayesTree() {

	}

	public BayesTree(String name, float value) {
		this.name = name;
		this.value = value;
	}

	/**
	 * 添加一组数
	 * 
	 * @param strings
	 */
	public void add(String... strings) {
		BayesTree parent = null;
		for (int i = 0; i < strings.length; i++) {
			if (i == 0) {// 添加跟
				parent = add(strings[i], 1f);
			} else {
				parent = add(parent, strings[i], 1f);
			}
		}
	}

	/**
	 * 添加父级
	 * 
	 * @param parent
	 */
	public void addParent(BayesTree parent) {
		if (parent != null) {
			this.parent.put(parent.name, parent);
		}
	}
	
	/**
	 * 添加父级
	 * 
	 * @param parent
	 */
	public void addSon(BayesTree son) {
		if (son != null) {
			this.son.put(son.name, son);
		}
	}

	/**
	 * 添加一个 头
	 * 
	 * @param string
	 */
	public BayesTree add(String name, float val) {
		BayesTree s = son.get(name);
		if (s == null) {
			s = new BayesTree(name, val);
			son.put(name, s);
		} else {
			s.value = s.value + val;
		}
		return s;
	}

	/**
	 * 
	 * @param parent
	 *            上级
	 * @param valP
	 *            上级值
	 * @param name
	 *            下级
	 * @param val
	 *            下级值
	 */
	public BayesTree add(BayesTree parent, String name, float val) {
		BayesTree sonBayes = parent.add(name, val);
		// 添加父级别
		// sonBayes.parent.put(parent.name,parent);
		return sonBayes;
	}
	/**
	 * 对应匹配后续字符
	 * 得分的累加和
	 * @param words
	 * @return
	 */
	public double  computeScore(String[] words,double k)
	{
		HashMap<String,BayesTree> sonTemp=son;
		double matchScore=0;
		//记录父亲孩子的数量
		int size=0;
		for(int i=0;i<words.length;i++)
		{
			BayesTree bayesTree=sonTemp.get(words[i]);
			size=sonTemp.size();
			if(bayesTree==null)
			{
				return matchScore;
			}else{
				matchScore+=bayesTree.value*Math.exp(i);
			}
		}
		return (matchScore+k)/(size+k);
	}

	/**
	 * 打印
	 * @param deep 深度
	 * @param split 上一次的string
	 */
	public void print(int deep,String split) {
		// for(Entry<String,BayesTree> m:parent.entrySet())
		// {
		// System.out.println(m.toString());
		// }
		System.out.print(split + toString() + "->");
		for (Entry<String, BayesTree> m : son.entrySet()) {
			BayesTree zn = m.getValue();
		//	System.out.print(get(" ", deep)+deep + m.getValue().toString() + "->");
			m.getValue().print(deep + 1,get("\t",deep));
			System.out.println();
		}
	}

	/**
	 * 字符串 n
	 * @param str
	 * @param count
	 * @return
	 */
	public String get(String str, int count) {
		StringBuffer sb = new StringBuffer();
		while (count > 0) {
			sb.append(str);
			count--;
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(name);
		return sb.toString();
	}

	public final static String[] SPAM_DATA = { "offer is secret",
			"click secret link", "secret sports link" };

	public final static String[] HAM_DATA = { "play sports today",
			"went play sports", "secret sports event", "sports is today",
			"sports costs money" };

	public static void main(String[] args) {
		BayesTree tree = new BayesTree();
		for (String st : BayesTree.SPAM_DATA) {
			String[] zn = st.split(" ");
			tree.add(zn);
		}
		tree.print(0,"");
	}
}