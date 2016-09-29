package ps.hell.ml.nlp.own.landerbuluse.participle.crf;

import java.io.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class CrfModel {
	// crf 模型

	public  Model model=null;
	
	/**
	 * 得到默认的模型
	 * 
	 * @return
	 */
	public  CrfModel() {
		// TODO Auto-generated method stub
		if (model != null) {
			return ;
		}
		try {
			model=loadModel(getInputStream("crf/crf.model"));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		tagConver = new int[model.template.tagNum];
		revTagConver = new int[model.template.tagNum];
		Set<Entry<String, Integer>> entrySet = model.template.statusMap.entrySet();
		//初始化参数集
		// case 0:'S';case 1:'B';case 2:'M';3:'E';
		for (Entry<String, Integer> entry : entrySet) {
			if ("S".equals(entry.getKey())) {
				tagConver[entry.getValue()] = 0;
				revTagConver[0] = entry.getValue();
			} else if ("B".equals(entry.getKey())) {
				tagConver[entry.getValue()] = 1;
				revTagConver[1] = entry.getValue();
			} else if ("M".equals(entry.getKey())) {
				tagConver[entry.getValue()] = 2;
				revTagConver[2] = entry.getValue();
			} else if ("E".equals(entry.getKey())) {
				tagConver[entry.getValue()] = 3;
				revTagConver[3] = entry.getValue();
			}
		}
		model.end1 = model.template.statusMap.get("S");
		model.end2 = model.template.statusMap.get("E");
	}
	/**
	 * 读取数据流
	 * @param str
	 * @return
	 */
	public InputStream getInputStream(String str)
	{
		InputStream in=null;
		try {
			in = new FileInputStream(new File(System.getProperty("user.dir")+"/data/"+str));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return in;
	}
	
	/**
	 * 写入模型
	 * @param modelStream
	 * @return
	 * @throws Exception
	 */
	public static Model loadModel(InputStream modelStream) throws Exception {
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new BufferedInputStream(new GZIPInputStream(modelStream)));

			Model model = new Model() {

				@Override
				public void writeModel(String path) throws FileNotFoundException, IOException {
					// TODO Auto-generated method stub
					throw new RuntimeException("you can not to calculate ,this model only use by cut ");
				}
			};

			model.template = (Template) ois.readObject();

			model.makeSide(model.template.left, model.template.right);

			int tagNum = model.template.tagNum;

			int featureNum = model.template.ft.length;

			model.smartForest = new SmartForest<double[][]>(0.8);

			model.status = (double[][]) ois.readObject();

			// 总共的特征数
			double[][] w = null;
			String key = null;
			int b = 0;
			int featureCount = ois.readInt();
			for (int i = 0; i < featureCount; i++) {
				key = ois.readUTF();
				w = new double[featureNum][0];
				for (int j = 0; j < featureNum; j++) {
					while ((b = ois.readByte()) != -1) {
						if (w[j].length == 0) {
							w[j] = new double[tagNum];
						}
						w[j][b] = ois.readFloat();
					}
				}
				model.smartForest.add(key, w);
			}

			return model;
		} finally {
			if (ois != null) {
				ois.close();
			}
		}
	}
	
	/**
	 * 讲模型写入
	 * 
	 * @param path
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void writeModel(String path) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub

		System.out.println("compute ok now to save model!");
		// 写模型
		ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream(path))));

		// 配置模板
		oos.writeObject(model.template);
		// 特征转移率
		oos.writeObject(model.status);
		// 总共的特征数
		oos.writeInt(model.myGrad.size());
		double[] ds = null;
		for (Entry<String, Feature> entry : model.myGrad.entrySet()) {
			oos.writeUTF(entry.getKey());
			for (int i = 0; i < model.template.ft.length; i++) {
				ds = entry.getValue().w[i];
				for (int j = 0; j < ds.length; j++) {
					oos.writeByte(j);
					oos.writeFloat((float) ds[j]);
				}
				oos.writeByte(-1);
			}
		}

		oos.flush();
		oos.close();

	}
	
	//////////////////////splitword

	private int[] tagConver = null;

	private int[] revTagConver = null;

	public List<String> cut(char[] chars) {
		return cut(new String(chars));
	}

	/**
	 * 分词
	 * @param line
	 * @return
	 */
	public List<String> cut(String line) {

		if (StringUtil.isBlank(line)) {
			return Collections.emptyList();
		}

		List<Element> elements = vterbi(line);

		LinkedList<String> result = new LinkedList<String>();

		Element e = null;
		int begin = 0;
		int end = 0;

		for (int i = 0; i < elements.size(); i++) {
			e = elements.get(i);
			switch (fixTag(e.getTag())) {
			case 0:
				end += e.len;
				result.add(line.substring(begin, end));
				begin = end;
				break;
			case 1:
				end += e.len;
				while (fixTag((e = elements.get(++i)).getTag()) != 3) {
					end += e.len;
				}
				end += e.len;
				result.add(line.substring(begin, end));
				begin = end;
			default:
				break;
			}
		}
		return result;
	}

	/**
	 * 维特比
	 * @param line
	 * @return
	 */
	private List<Element> vterbi(String line) {
		List<Element> elements = WordAlert.str2Elements(line);

		int length = elements.size();
		if (length == 0) { // 避免空list，下面get(0)操作越界
			return elements;
		}
		if (length == 1) {
			elements.get(0).updateTag(revTagConver[0]);
			return elements;
		}

		/**
		 * 填充图
		 */
		for (int i = 0; i < length; i++) {
			computeTagScore(elements, i);
		}

		// 如果是开始不可能从 m，e开始 ，所以将它设为一个很小的值
		elements.get(0).tagScore[revTagConver[2]] = -1000;
		elements.get(0).tagScore[revTagConver[3]] = -1000;
		//获取最终的转移后的值
		for (int i = 1; i < length; i++) {
			elements.get(i).maxFrom(model, elements.get(i - 1));
		}

		// 末位置只能从S,E开始
		Element next = elements.get(elements.size() - 1);
		Element self = null;
		int maxStatus = next.tagScore[model.end1] > next.tagScore[model.end2] ? model.end1 : model.end2;
		next.updateTag(maxStatus);
		maxStatus = next.from[maxStatus];
		// 逆序寻找
		for (int i = elements.size() - 2; i > 0; i--) {
			self = elements.get(i);
			self.updateTag(maxStatus);
			maxStatus = self.from[self.getTag()];
			next = self;
		}
		elements.get(0).updateTag(maxStatus);
		return elements;

	}

	/**
	 * 计算对应的tag的得分
	 * @param elements
	 * @param index
	 */
	private void computeTagScore(List<Element> elements, int index) {
		double[] tagScore = new double[model.template.tagNum];

		Template t = model.template;
		char[] chars = null;
		for (int i = 0; i < t.ft.length; i++) {
			chars = new char[t.ft[i].length];
			//获取对应model中ft中的对应位置值
			for (int j = 0; j < chars.length; j++) {
				chars[j] = getElement(elements, index + t.ft[i][j]).name;
			}
			dot(tagScore, model.getFeature(i, chars));
		}
		elements.get(index).tagScore = tagScore;
	}
	/**
	 * 两个数组对应位相加
	 * @param feature
	 * @param feature1
	 */
	public static void dot(double[] feature, double[] feature1) {
		if (feature1 == null) {
			return;
		}
		for (int i = 0; i < feature1.length; i++) {
			feature[i] += feature1[i];
		}
	}
	/**
	 * 获取元素如果=-1 为a 如果i为size 则为B
	 * 否则为 elements中对应的元素
	 * @param elements
	 * @param i
	 * @return
	 */
	private Element getElement(List<Element> elements, int i) {
		// TODO Auto-generated method stub
		if (i < 0) {
			return new Element((char) ('B' + i));
		} else if (i >= elements.size()) {
			return new Element((char) ('B' + i - elements.size() + 1));
		} else {
			return elements.get(i);
		}
	}

	public int fixTag(int tag) {
		return tagConver[tag];
	}

	/**
	 * 随便给一个词。计算这个词的内聚分值，可以理解为计算这个词的可信度
	 * 
	 * @param word
	 */
	public double cohesion(String word) {

		if (word.length() == 0) {
			return Integer.MIN_VALUE;
		}

		List<Element> elements = WordAlert.str2Elements(word);

		for (int i = 0; i < elements.size(); i++) {
			computeTagScore(elements, i);
		}

		double value = elements.get(0).tagScore[revTagConver[1]];

		int len = elements.size() - 1;

		for (int i = 1; i < len; i++) {
			value += elements.get(i).tagScore[revTagConver[2]];
		}

		value += elements.get(len).tagScore[revTagConver[3]];
		
		if(value<0){
			return 1; 
		}else{
			value += 1 ;
		}

		return value;
	}

	////////////////////////end 
	public static void main(String[] args) {
		CrfModel crf=new CrfModel();
//		List<String> list=crf.cut("说过，社交软件也是打着沟通的平台，让无数寂寞男女有了肉体与精神的寄托。");
//		for(String str:list)
//		{
//			System.out.println(str);
//		}
//		try {
//			crf.writeModel(System.getProperty("user.dir")+"/data/crf/crf_temp2.model");
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}
