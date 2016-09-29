package ps.hell.ml.nlp.own.landerbuluse.participle.newWorDiscovery;

import ps.hell.ml.nlp.own.landerbuluse.struct.Word;
import ps.hell.ml.nlp.own.landerbuluse.struct.WordsGet;
import ps.hell.util.FileUtil;

import java.util.*;
import java.util.Map.Entry;

/**
 * 新词发现测试程序
 * 
 * @author Administrator
 *
 */
public class NewWordDiscovery {

	/**
	 * 标点符号
	 */
	public static WordsGet wordsParams = new WordsGet();

	/**
	 * 标点符号
	 */
	public static WordsGet wordsClearn = new WordsGet();
	/**
	 * 动词
	 */
	public static WordsGet wordsVert = new WordsGet();
	/**
	 * 介词
	 */
	public static WordsGet wordsPro = new WordsGet();
	/**
	 * 形容词
	 */
	public static WordsGet wordAdj = new WordsGet();
	/**
	 * 量词
	 */
	public static WordsGet wordQuen = new WordsGet();
	/**
	 * 量纲
	 */
	public static WordsGet wordQuenLabel = new WordsGet();

	/**
	 * 地名后缀
	 */
	public static WordsGet wordPlace = new WordsGet();

	public NewWordDiscovery() {
		// 添加标点符号
		wordsParams.addParamsFile();

		wordsPro.addPrepositions();
		wordsClearn.addChineseStopWord();
		// 把介词全部加入停用词中
		for (Word word : wordsPro.otherWords) {
			wordsClearn.otherWords.add(word);
		}
		wordsVert.addVert();
		wordAdj.addAdjectives();
		wordQuen.addQuantifiersFile();
		wordQuenLabel.addQuantifiersLabel();

		wordPlace.addPlace();
	}

	/**
	 * 移出分词后的词频中 是需要去处的词频 如停用词等
	 * 
	 * @param inputData
	 * @return
	 */
	public LinkedList<String> clearnStop(LinkedList<String> inputData) {
		LinkedList<String> result = new LinkedList<String>();
		while (inputData.size() > 0) {
			String temp = inputData.pollFirst();
			char[] strs = temp.toCharArray();
			int start = 0;
			for (int i = 0; i < strs.length; i++) {
				// System.out.println(strs[i]+"\t"+this.wordsParams.otherWords.size()+"\t"+Character.toString(strs[i]).hashCode());
				// System.out.println("，".hashCode());
				if (wordsClearn.otherWords.contains(new Word(Character
						.toString(strs[i]), 1, null))) {
					result.add(temp.substring(start, i));
					start = i + 1;
				}
			}
			if (start < strs.length - 1) {
				result.add(temp.substring(start, strs.length));
			}
			strs = null;
		}
		return result;
	}

	/**
	 * 移除去 所有 特殊分隔符 其中 以.?为换行符 并切分
	 * 
	 * @param inputData
	 * @return
	 */
	private LinkedList<String> clearnParams(String inputData) {
		LinkedList<String> result = new LinkedList<String>();
		char[] strs = inputData.toCharArray();
		int start = 0;
		for (int i = 0; i < strs.length; i++) {
			// System.out.println(strs[i]+"\t"+this.wordsParams.otherWords.size()+"\t"+Character.toString(strs[i]).hashCode());
			// System.out.println("，".hashCode());
			if (wordsParams.otherWords.contains(new Word(Character
					.toString(strs[i]), 1, null))) {
				result.add(inputData.substring(start, i));
				start = i + 1;
			}
		}
		if (start < strs.length - 1) {
			result.add(inputData.substring(start, strs.length));
		}
		strs = null;
		return result;
	}

	/**
	 * 
	 * @param inputData
	 * @param countClearn
	 *            每次清除掉过少的词频
	 * @param ngram
	 *            使用ngram
	 * @param count
	 *            提取>count 的词频
	 * @param clearnRateMin
	 *            合并清洗的占有比率 大并少
	 * @param clearnRateMax
	 * @return
	 */
	public HashMap<String, OutBean> getNewWord(LinkedList<String> inputData,
			int countClearn, int ngram, int count, double clearnRateMin,
			double clearnRateMax) {

		Model model = new Model(ngram, countClearn);
		int cou = inputData.size();
		int co2 = 0;
		while (inputData.size() > 0) {
			co2++;
			if (co2 % 10000 == 0) {
				System.out.println(co2 + "\t总数:" + cou);
			}
			// if (co2 > 50000) {
			// break;
			// }
			LinkedList<String> split = clearnParams(inputData.pollFirst());
			split = clearnStop(split);
			while (split.size() > 0) {
				model.readNewLine(split.pollFirst());
			}
		}
		return model.getNewWords(count, clearnRateMin, clearnRateMax);
	}

	public class OutBean implements Comparable<OutBean> {

		public ArrayList<String> desc = new ArrayList<String>();
		public double weight = 0;
		/**
		 * 如果为1 则为 此word，是前向被完全包含
		 */
		public int type = 0;

		public ArrayList<String> descV = new ArrayList<String>();

		public OutBean(double val) {
			this.weight = val;
		}

		public int compareTo(OutBean o) {
			return Double.compare(o.weight, weight);
		}

		public String toString() {
			StringBuffer sb = new StringBuffer("{ weight:");
			sb.append(weight).append(",[");
			for (int i = 0; i < desc.size(); i++) {
				if (i != 0) {
					sb.append(",");
				}
				sb.append(desc.get(i)).append("/").append(descV.get(i));
			}
			sb.append("]");
			return sb.toString();
		}
	}

	public class Model {
		/**
		 * ngram 值
		 */
		public int ngram = 2;
		/**
		 * 概率数据
		 */
		HashMap<String, HashMap<String, Double>> map = new HashMap<String, HashMap<String, Double>>();
		/**
		 * 每次新增countSize个，就会过滤一次比较少的数据
		 */
		public int countSize = 3;
		/**
		 * 
		 */
		public int iter = 500;
		public int iterCount = 0;

		public int from = 1;

		public int size = 2;

		public class WordBean {
			String word = null;
			double weight = 0;
		}

		public Model(int ngram, int countSize) {
			this.ngram = ngram;
			this.countSize = countSize;
		}

		/**
		 * 先gram-2
		 * 
		 * @param line
		 */
		public void readNewLine(String line) {
			iterCount++;
			if (iterCount % iter == 1) {
				// 清理
				Iterator<String> it = map.keySet().iterator();
				while (it.hasNext()) {
					String name = it.next();
					HashMap<String, Double> mm = map.get(name);
					int count = mm.size();
					int count1 = 0;
					Iterator<String> it2 = mm.keySet().iterator();
					while (it2.hasNext()) {
						String name2 = it2.next();
						Double mm2 = mm.get(name2);
						if (mm2 < countSize) {
							count1++;
							it2.remove();
						}
					}
					if (count == count1) {
						it.remove();
					}
				}
			}
			// 提取所有2个字-4个字的词语
			char[] chars = line.toCharArray();
			for (int i = 0; i < chars.length; i++) {
				for (int j = from; j < from + size; j++) {
					int end1 = i + j - 1;
					if (end1 + 2 >= chars.length) {
						break;
					}
					// 第一个词
					StringBuffer sb = new StringBuffer();
					for (int l = i; l <= end1; l++) {
						sb.append(chars[l]);
					}
					String word1 = sb.toString();
					HashMap<String, Double> val = map.get(word1);
					if (val == null) {
						val = new HashMap<String, Double>();
						map.put(word1, val);
					}
					int start2 = end1 + 1;
					for (int k = from; j < from + size; k++) {
						// 第二个词
						// 获取每一次的词提取
						int end2 = start2 + k - 1;
						if (end2 >= chars.length) {
							break;
						}
						StringBuffer sb2 = new StringBuffer();
						for (int l = start2; l <= end2; l++) {
							sb2.append(chars[l]);
						}
						String word2 = sb2.toString();
						Double val2 = val.get(word2);
						if (val2 == null) {
							val.put(word2, 1D);
						} else {
							val.put(word2, val2 + 1d);
						}
					}
				}
			}
		}

		/**
		 * 获取新词
		 * 
		 * @param count
		 * @param clearnRateMin
		 *            清洗比率
		 * @param clearnRateMax
		 * @return
		 */
		public HashMap<String, OutBean> getNewWords(int count,
				double clearnRateMin, double clearnRateMax) {
			HashMap<String, OutBean> re = new HashMap<String, OutBean>();
			for (Entry<String, HashMap<String, Double>> m : map.entrySet()) {
				String val = m.getKey();
				for (Entry<String, Double> m2 : m.getValue().entrySet()) {
					if (m2.getValue() >= count) {
						re.put(val + m2.getKey(), new OutBean(m2.getValue()));
					}
				}
			}
			// 需要做一次词语合并
			// [精神构 , 3775.0]
			// [神构 , 3775.0]
			// [神构装 , 3774.0]
			// [精神构装 , 3774.0]
			// 不重写数据结构了
			HashSet<String> clearn = new HashSet<String>();
			HashMap<String, OutBean> clearnBean = new HashMap<String, OutBean>();
			for (Entry<String, OutBean> m2 : re.entrySet()) {
				String name = m2.getKey();
				if (wordAdj.otherWords.contains(new Word(name, 1, null))) {
					// 剔除形容词
					clearn.add(name);
					continue;
				}
				OutBean val = m2.getValue();
				if (!isQuence(name, val)) {
					if (!isPlace(name, val)) {

					}
				}
				// 目标位高包少
				for (Entry<String, OutBean> m3 : re.entrySet()) {
					String name3 = m3.getKey();
					OutBean val3 = m3.getValue();
					if (!name.equals(name3) && name.contains(name3)) {
						double rate = (val.weight / (val.weight + val3.weight));
						if (rate < clearnRateMin || rate > clearnRateMax) {
							System.out
									.println(name
											+ "\t-----:"
											+ name3
											+ "\t"
											+ (val.weight / (val.weight + val3.weight)));
							if (rate > 0.1) {
								if (name.startsWith(name3)
										|| name.endsWith(name3)) {
									clearn.add(name3);
								}
							}
							continue;
						} else {
							System.out
									.println(name
											+ "\tclearn:"
											+ name3
											+ "\t"
											+ (val.weight / (val.weight + val3.weight)));
							// 处理是否为完全包含

							if (name.startsWith(name3) || name.endsWith(name3)) {
								clearn.add(name3);
							} else {
								if (t(name3, val3)) {
									OutBean out = clearnBean.get(name3);
									if (out == null) {
										clearnBean.put(name3, val3);
									}
								}
							}
						}
					}
				}
			}
			// 如果倒数第二个词为量词则剔除
			for (String str : clearn) {
				// if(str.equals("幻家老")){
				// System.out.println();
				// }
				re.remove(str);
			}
			return re;
		}
	}

	/**
	 * 是否为地名
	 * 
	 * @param str
	 * @param bean
	 * @return
	 */
	public boolean isPlace(String str, OutBean bean) {
		int n = 0;
		boolean flag = false;
		if (str.equals("修真家族")) {
			System.out.println();
		}
		for (int i = str.length() - 1; i >= 0; i--) {
			if (this.wordPlace.otherWords.contains(new Word(str.substring(i),
					1, null))) {
				n++;
				flag = true;
			} else {
				break;
			}
		}
		if (flag) {
			if (n == str.length()) {
				bean.desc.add(str);
				bean.descV.add("位置");
			} else {
				// 判断地址的占位符是否存在形容词
				int start = -1;
				for (int j = str.length() - n - 1; j > 0; j--) {
					String temp = str.substring(j, str.length() - n);
					if (this.wordAdj.otherWords
							.contains(new Word(temp, 1, null))) {
						start = j;
						break;
					}
				}
				if (start == 0) {
					bean.desc.add(str.substring(0, str.length() - n));
					bean.desc.add(str.substring(str.length() - n));
					bean.descV.add("形容词");
					bean.descV.add("位置");
				} else if (start > 0) {
					bean.desc.add(str.substring(0, start));
					bean.desc.add(str.substring(start, str.length() - n));
					bean.desc.add(str.substring(str.length() - n));
					bean.descV.add("地址");
					bean.descV.add("形容词");
					bean.descV.add("位置");
				} else {
					bean.desc.add(str.substring(0, str.length() - n));
					bean.desc.add(str.substring(str.length() - n));
					bean.descV.add("地址");
					bean.descV.add("位置");
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * 是否为量词
	 * 
	 * @param str
	 * @param bean
	 * @return
	 */
	public boolean isQuence(String str, OutBean bean) {
		// 判断是否为量词
		char[] temp = str.toCharArray();
		boolean flag = false;
		boolean flagLiang = false;
		int end1 = 0;
		int start2 = 0;
		int count2 = 1;
		for (int i = 0; i < temp.length; i++) {
			if (flag) {
				// 判断是否为量纲
				if (start2 == 0) {
					start2 = i;
				}
				Word word = null;
				word = new Word(str.substring(start2, start2 + count2), 1, null);
				if (this.wordQuen.otherWords
						.contains(new Word(temp[i], 1, null))) {
					flag = true;
					end1 = i + 1;
					continue;
				} else if (this.wordQuenLabel.otherWords.contains(word)) {
					if (i == temp.length - 1) {
						bean.desc.add(str.substring(0, end1));
						bean.desc.add(str.substring(start2, str.length()));
						bean.descV.add("数量");
						bean.descV.add("量纲");
						return true;
					}
					flagLiang = true;
					count2++;
					continue;
				} else {
					// System.out.println(str + "\t" + str.length() + "\t" + 0
					// + "\t" + end1 + "\t" + start2 + "\t"
					// + (start2 + count2) + "\t" + (start2 + count2)
					// + "\t");
					if (count2 != 1) {
						if (flagLiang) {
							count2--;
						}
						bean.desc.add(str.substring(0, end1));
						bean.desc.add(str.substring(start2, start2 + count2));
						bean.desc.add(str.substring(start2 + count2,
								str.length()));
						bean.descV.add("数量");
						bean.descV.add("量纲");
						bean.descV.add("名词");
					} else {
						bean.desc.add(str.substring(0, end1));
						bean.desc.add(str.substring(end1, str.length()));
						bean.descV.add("数量");
						bean.descV.add("名词");
					}
					return true;
				}
			} else {
				// 判断连续是否为量词
				if (this.wordQuen.otherWords
						.contains(new Word(temp[i], 1, null))) {
					flag = true;
					end1 = i + 1;
				}
			}
			if (!flag) {
				return false;
			}
		}
		if (flag) {
			bean.desc.add(str);
			bean.descV.add("数量");
			return true;
		}
		return true;
	}

	/**
	 * 判断是否剔除
	 * 
	 * @return
	 */
	public boolean t(String str, OutBean bean) {
		System.out.println("处理量词:" + str);
		if (str.length() <= 2) {
			return true;
		}
		char[] strArray = str.toCharArray();
		// 除去第一个词 获取最长的后缀词
		if (wordQuen.otherWords.contains(new Word(strArray[0], 1, null))) {
			// 如果前缀为全部则依然删除
			// bean.desc = "2量词";
			return false;
		}
		// 查询是否为ends
		return true;
	}

	public static void main(String[] args) {
		// WordsGet wordQuen=new WordsGet();
		// wordQuen.addQuantifiersFile();
		// System.out.println(wordQuen.otherWords);
		//
		// String temp="　　藤";
		// System.out.println(temp.replaceAll("[\\s　]", ""));
		// System.exit(1);
		String str = "src/ps/landerbuluse/ml/text/participle/mmseg2/仙逆.txt";
		FileUtil fileUtil = new FileUtil(str, "gbk", false, null);
		ArrayList<String> list2 = fileUtil.readAndClose();
		LinkedList<String> list =new LinkedList<String>();
		for(int i=0;i<list2.size();i++){
			list.add(list2.get(i));
		}
		NewWordDiscovery model = new NewWordDiscovery();
		HashMap<String, OutBean> val = model.getNewWord(list, 10, 2, 50, 0.42,
				0.9);
		ArrayList<SortBean> sort = new ArrayList<SortBean>();
		for (Entry<String, OutBean> m : val.entrySet()) {
			sort.add(new SortBean(m.getKey(), m.getValue()));
		}
		Collections.sort(sort);
		int count = 4000;
		for (int i = 0; i < (count > sort.size() ? sort.size() : count); i++) {
			System.out.println(sort.get(i));
		}

		// System.out.println(NewWordDiscovery.wordQuen.otherWords);
	}
}
