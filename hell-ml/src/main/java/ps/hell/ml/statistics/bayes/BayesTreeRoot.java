package ps.hell.ml.statistics.bayes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * 使用 baye net 的 ngram分类
 * match方法
 * @author Administrator
 *
 */
public class BayesTreeRoot {

	/**
	 * key对应的分类属性 value 对应的 nGram对应的序列值0 为第一个 1位第二个 2位第三个
	 */
	HashMap<String, ArrayList<HashMap<String, BayesTree>>> root = new HashMap<String, ArrayList<HashMap<String, BayesTree>>>();
	/**
	 * ngram数量 如果<0则不适用ngram方法
	 */
	public int ngram = 2;
	/**
	 * 拉普拉斯k值 只是解决inifinty值异常的问题以及，最小值得偏值问题
	 */
	double k = 0.00001;

	/**
	 * 添加一分类+string[]输入
	 * 
	 * @param strings
	 *            第一个对应的分类名字 后对应的输入字符串
	 */
	public void add(String className,String... strings) {
		BayesTree parent = null;
		for (int i = 0; i < strings.length; i++) {
			if (ngram <= 0) {// 如果
				parent = add(className, i, parent, strings[i], 1f);
			} else {
				parent = null;
				//添加ngram值
				for (int j = 0; j < (i + ngram < strings.length ? ngram
						: strings.length - i); j++) {
					parent = add(className, j, parent, strings[i + j], 1f);
				}
			}
		}
	}

	/**
	 * 添加具体节点对应添加
	 * 
	 * @param index
	 * @param parent
	 * @param name
	 * @param value
	 * @return
	 */
	public BayesTree add(String className, int index, BayesTree parent,
			String name, float value) {
		ArrayList<HashMap<String, BayesTree>> rootClass = root.get(className);
		if (rootClass == null) {
			rootClass = new ArrayList<HashMap<String, BayesTree>>();
			root.put(className, rootClass);
		}
		if (rootClass.size() <= index) {
			rootClass.add(new HashMap<String, BayesTree>());
		}
		HashMap<String, BayesTree> tree = rootClass.get(index);
		BayesTree son = tree.get(name);
		if (son == null) {
			son = new BayesTree(name, value);
			tree.put(name, son);
		}
		son.addParent(parent);
		if (parent != null) {
			parent.addSon(son);
		}
		return son;
	}

	public HashMap<String,Double> test(String wordString)
	{
		System.out.println(wordString);
		String[] words=wordString.split(" ");
		return test(words);
	}
	/**
	 * 输入的字符串属于的类型
	 * 
	 * @param strings
	 * @return
	 */
	public HashMap<String, Double> test(String[] words) {
		HashMap<String, Double> result = new HashMap<String, Double>();
		for (Entry<String, ArrayList<HashMap<String, BayesTree>>> map : root
				.entrySet()) {
			// 获取每一个分类对应的值
			result.put(map.getKey(),
					probability(map.getKey(), map.getValue(), words));
		}
		//标准化
		double value=0;
		for(Entry<String,Double> val:result.entrySet())
		{
			value+=val.getValue();
		}
		for(Entry<String,Double> val:result.entrySet())
		{
			val.setValue(val.getValue()/value);
			System.out.println(val.getKey()+"\t"+val.getValue());
		}
		return result;
	}

	/**
	 * 如果为单独的 则可直接获取第一层数据数量 如果为ngram的则需要获取涉及的全量
	 * 
	 * @param className
	 * @param word
	 * @return
	 */
	public double probability(String className,ArrayList<HashMap<String,BayesTree>> map,String[] words) {
		double value=1;
//		System.out.println(className+":source:"+Arrays.toString(words));
		int notCount=0;
		for(int i=0;i<words.length;i++)
		{
			int length=(i + ngram < words.length ? ngram
					: words.length - i);
			//System.out.println(length);
			//阶段字符成ngram
			for (int j = 0; j <length; j++) {
//			System.out.println("getvalue:"+words[i+j]);
				BayesTree val=map.get(j).get(words[i+j]);
				if(val==null)
				{
					notCount++;
					value*=0.5/map.get(j).size();
					continue;
				}
				if(length==1)
				{
					value*=val.value/map.get(j).size();
//					System.out.println("value:"+value);
					continue;
				}
				String[] temp=new String[length-1];
//				System.arraycopy(words,i+1,temp,0, length-1);
				double valDouble=val.value/map.get(j).size()+val.computeScore(temp,k);
//				System.out.println(Arrays.toString(temp)+"\t:valDouble:"+valDouble);
				value*=valDouble;
//				System.out.println("value:"+value);
			}
		}
		if(words.length==notCount)
		{
			return 0;
		}
//		System.out.println(className+":endValue:"+value);
		value=(value+k)/(notCount+k+1);
//		System.out.println(className+":endValue:"+value);
		return value;
	}

	/**
	 * 打印
	 */
	public void print() {
		for (Entry<String, ArrayList<HashMap<String, BayesTree>>> rootClass : root
				.entrySet()) {
			System.out.println("class:" + rootClass.getKey());
			for (HashMap<String, BayesTree> ro : rootClass.getValue()) {
				for (Entry<String, BayesTree> zn : ro.entrySet()) {
					zn.getValue().print(1, "");
				}
				break;
			}
		}
	}

	public final static String[] SPAM_DATA = { "secret sports link",
			"secret secret link", "offer is secret", };

	public final static String[] HAM_DATA = { "play sports today",
			"went play sports", "secret sports event", "sports is today",
			"sports costs money" };

	public static void main(String[] args) {
		BayesTreeRoot tree = new BayesTreeRoot();
		tree.ngram=2;
		for (String st : BayesTreeRoot.SPAM_DATA) {
			String[] zn = st.split(" ");
			tree.add("spam",zn);
		}
		for (String st : BayesTreeRoot.HAM_DATA) {
			String[] zn = st.split(" ");
			tree.add("ham",zn);
		}
		//tree.print();
		tree.test("secret"); // 0.0
		tree.test("today"); // 0.0
		tree.test("sports"); // 16.67
		tree.test("today is secret"); // 0.0
		tree.test("secret is secret"); // 96.15
		tree.test("today is play"); // 96.15
		tree.test("sports play money"); // 96.15
	}
}
