package ps.hell.ml.nlp.own.landerbuluse.participle.mmseg2;

import ps.hell.ml.nlp.own.landerbuluse.struct.AnalyzeParticiple;
import ps.hell.ml.nlp.own.landerbuluse.struct.Word;
import ps.hell.ml.nlp.own.landerbuluse.struct.WordFather;
import ps.hell.ml.nlp.own.landerbuluse.struct.WordsGet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.Map.Entry;

/**
 * mmseg算法重写
 * 
 * @author Administrator
 *
 */
public class mmseg implements AnalyzeParticiple {
	/**
	 * 复杂匹配的单词个数
	 */
	public int adaptMaxLength = 3;
	/**
	 * 词频文件
	 */
	public WordsGet words = new WordsGet();
	/**
	 * 需要清除的词
	 */
	public WordsGet wordsClearn = new WordsGet();
	/**
	 * 标点符号
	 */
	public WordsGet wordsParams = new WordsGet();

	public WordsGet wordsEmotion = new WordsGet();

	/**
	 * 
	 * @param usedWords
	 *            使用的词库
	 * @param clearnWords
	 *            清除的词库
	 */
	public mmseg(String[] usedWords, String[] clearnWords) {
		words.isTrie=false;
		// 添加基础词库
		words.addChineseFamousPersonFile();
		words.addChinaAddressFile();
		words.addCineseWord();
		// 添加需要清除的词
		wordsClearn.isTrie=true;
		wordsClearn.addChineseStopWord();
		// 添加标点符号
		wordsParams.isTrie=false;
		wordsParams.addParamsFile();
		// 加载 情感词
		wordsEmotion.isTrie=false;
		wordsEmotion.addEmotionFile();
	}

	/**
	 * 分词 通过输入一个String来获取分词 其中 inputData 一定为无/n的句子
	 * 
	 * @return 按照某些标点符号将划分开 对应每一个子句子 出Word[]
	 */
	public LinkedList<LinkedList<WordFather>> analyze(String inputData) {
		LinkedList<String> input = clearnParams(inputData);
		LinkedList<LinkedList<WordFather>> result = new LinkedList<LinkedList<WordFather>>();
		for (String str : input) {
			LinkedList<WordFather> re = participleMethodMmseg(str);
			if (re == null || re.size() == 0) {
				continue;
			} else {
				result.addLast(re);
			}
		}
		return result;
	}

	/**
	 * 分词 通过输入一个String来获取分词 其中 inputData 一定为无/n的句子
	 * 
	 * @return 按照某些标点符号将划分开 对应每一个子句子 出Word[]
	 */
	public ArrayList<LinkedList<LinkedList<WordFather>>> analyze(
			LinkedList<String> inputData) {
		ArrayList<LinkedList<LinkedList<WordFather>>> out = new ArrayList<LinkedList<LinkedList<WordFather>>>();
		for (String inputString : inputData) {
			LinkedList<String> input = clearnParams(inputString);
			LinkedList<LinkedList<WordFather>> result = new LinkedList<LinkedList<WordFather>>();
			for (String str : input) {
				LinkedList<WordFather> re = participleMethodMmseg(str);
				if (re == null || re.size() == 0) {
					continue;
				} else {
					result.addLast(re);
				}
			}
			if (result.size() == 0) {

			} else {
				out.add(result);
			}
		}
		return out;
	}

	/**
	 * 读取文件并返回对应的分词
	 * 
	 * @param file
	 *            文件
	 * @param code
	 *            字符集
	 * @return
	 */
	public LinkedList<LinkedList<LinkedList<WordFather>>> analyze(File file,
			String code) {
		LinkedList<LinkedList<LinkedList<WordFather>>> result = new LinkedList<LinkedList<LinkedList<WordFather>>>();
		if (!file.exists()) {
			System.out.println("文件不存在");
			return null;
		}
		BufferedReader reader = null;
		try {

			InputStreamReader read = new InputStreamReader(new FileInputStream(
					file), code);
			reader = new BufferedReader(read);
			String tempString = null;
			// int line = 1;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				if (tempString.length() > 0) {
					LinkedList<LinkedList<WordFather>> re = analyze(tempString);
					if (re.size() == 0) {
						continue;
					} else {
						result.addLast(re);
					}
				}
			}
			reader.close();
			read.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("读取异常");
		}
		return result;
	}

	/**
	 * 分析情感词
	 * 
	 * @param words
	 * @return
	 */
	public LinkedList<LinkedList<WordFather>> analyzeEmotion(
			LinkedList<LinkedList<WordFather>> words) {
		LinkedList<LinkedList<WordFather>> result = new LinkedList<LinkedList<WordFather>>();
		while (words.size() > 0) {
			LinkedList<WordFather> temp = words.pollFirst();
			LinkedList<WordFather> re = new LinkedList<WordFather>();
			while (temp.size() > 0) {
				WordFather temp2 = temp.pollFirst();
				// System.out.println(temp2.word);
				HashMap<WordFather, WordFather> map = this.wordsEmotion.emotionWords
						.getWords(temp2.word);
				if (map == null) {
					continue;
				}
				// System.out.println(map.size());
				WordFather fa = map.get(temp2);
				if (fa == null) {
					continue;
				}
				re.addLast(fa);
			}
			result.addLast(re);
		}
		return result;
	}

	/**
	 * 移出分词后的词频中 是需要去处的词频 如停用词等
	 * 
	 * @param inputData
	 * @return
	 */
	public LinkedList<LinkedList<WordFather>> clearnStop(
			LinkedList<LinkedList<WordFather>> inputData) {
		for (LinkedList<WordFather> words : inputData) {
			Iterator<WordFather> iterator=words.iterator();
			while(iterator.hasNext())
			{
				Word word=iterator.next();
				if (wordsClearn.otherWords.contains(word)) {
					//System.out.println("移除:"+word.word);
					iterator.remove();
				}
			}
		}
		return inputData;
	}

	/**
	 * 移出分词后的词频中 是需要去处的词频 如停用词等
	 * 
	 * @param inputData
	 * @return
	 */
	public ArrayList<LinkedList<LinkedList<WordFather>>> clearnStop(
			ArrayList<LinkedList<LinkedList<WordFather>>> inputData) {
		for (LinkedList<LinkedList<WordFather>> wordList : inputData) {
			for (LinkedList<WordFather> words : wordList) {
				Iterator<WordFather> iterator=words.iterator();
				while(iterator.hasNext())
				{
					Word word=iterator.next();
					if (wordsClearn.otherWords.contains(word)) {
						//System.out.println("移除:"+word.word);
						iterator.remove();
					}
				}
			}
		}
		return inputData;
	}

	/**
	 * 移除去 所有 特殊分隔符 其中 以.?为换行符
	 * 
	 * @param inputData
	 * @return
	 */
	public LinkedList<String> clearnParams(String inputData) {
		LinkedList<String> result = new LinkedList<String>();
		char[] strs = inputData.toCharArray();
		int start = 0;
		for (int i = 0; i < strs.length; i++) {
			// System.out.println(strs[i]+"\t"+this.wordsParams.otherWords.size()+"\t"+Character.toString(strs[i]).hashCode());
			// System.out.println("，".hashCode());
			if (this.wordsParams.otherWords.contains(new Word(Character
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
	 * 分词方法 使用mmseg的联合方法 输入的inputData为不含有字符串的一串字符
	 * 
	 * @param inputData
	 * @return
	 */
	public LinkedList<WordFather> participleMethodMmseg(String inputData) {
		LinkedList<WordFather> result = new LinkedList<WordFather>();
		// 存储当前chunk单组的个数
		int count = 0;
		// 字符开始位置
		int start = 0;
		// 字符结束位置
		int end = 1;
		String str = null;
		while (true) {
			if (start >= inputData.length()) {// 如果到最长则跳出
				break;
			}
			str = inputData.substring(start, end);
			LinkedList<WordFather> re = sampleAdapt(str, inputData, start);

			if (re == null) {// 本过程为最大匹配原则
				result.addLast(new WordFather(str, 1, null));
				start++;
				end++;
				continue;
			} else {
				// System.out.println("::"+re.size());
				// 本过程为复杂最大匹配原则
				// 获取没一组的全部配比，标准是三组
				// 复杂匹配的输出类型
				LinkedList<LinkedList<WordFather>> comResult = compAdaptMethod(
						inputData, start, re);

				// 先获取 平均最大长度的index移出移除小雨的
				int index = -1;
				double maxLen = -1D;
				double[] val = new double[comResult.size()];
				ArrayList<Double> valList = new ArrayList<Double>();
				// System.out.println("0:"+comResult.size());
				if (comResult.size() > 1) {
					Iterator<LinkedList<WordFather>> iterator1 = comResult
							.iterator();
					while (iterator1.hasNext()) {
						LinkedList<WordFather> words = iterator1.next();
						index++;
						double len = 0;
						for (Word w : words) {
							len += w.word.length();
						}
						// 平均长度
						len /= words.size();
						if (maxLen < len) {
							maxLen = len;
						}
						val[index] = len;
					}
					index = -1;
					iterator1 = comResult.iterator();
					while (iterator1.hasNext()) {
						index++;
						iterator1.next();
						if (Double.compare(val[index], maxLen) != 0) {
							iterator1.remove();
						} else {
							valList.add(val[index]);
						}
					}
				}
				val = null;
				// 判断comResult 长度 如果超过1个则判断std
				index = -1;
				// System.out.println("1:"+comResult.size());
				if (comResult.size() > 1) {
					// 计算std最小
					double minStd = Double.MAX_VALUE;
					Iterator<LinkedList<WordFather>> iterator1 = comResult
							.iterator();
					double[] stdVal = new double[comResult.size()];
					while (iterator1.hasNext()) {
						LinkedList<WordFather> words = iterator1.next();
						index++;
						double std = 0;
						for (Word w : words) {
							std += Math.pow(
									w.word.length() - valList.get(index), 2D);
						}
						// 平均长度
						std /= words.size();
						if (minStd > std) {
							minStd = std;
						}
						stdVal[index] = std;
					}
					iterator1 = comResult.iterator();
					index = -1;
					while (iterator1.hasNext()) {
						iterator1.next();
						index++;
						if (Double.compare(stdVal[index], minStd) != 0) {
							iterator1.remove();
						}
					}
					stdVal = null;
				}
				index = -1;
				// 判断语意自由度最大和 长度 如果超过1个则判断
				// System.out.println("2:"+comResult.size());
				if (comResult.size() > 1) {
					// 语意最大
					double maxyi = -1E10;
					Iterator<LinkedList<WordFather>> iterator1 = comResult
							.iterator();
					double[] maxyiVal = new double[comResult.size()];
					while (iterator1.hasNext()) {
						index++;
						LinkedList<WordFather> words = iterator1.next();
						double yi = 0;
						for (Word w : words) {
							if (w.word.length() == 1)
								// 单字节汉字 语速自由度
								yi += Math.log(w.wordNum);
						}
						if (maxyi < yi) {
							maxyi = yi;
						}
						maxyiVal[index] = yi;
					}
					iterator1 = comResult.iterator();
					index = -1;
					while (iterator1.hasNext()) {
						iterator1.next();
						index++;
						if (Double.compare(maxyiVal[index], maxyi) != 0) {
							iterator1.remove();
						}
					}
					maxyiVal = null;
				}
				// System.out.println("3:"+comResult.size());
				// 此处不管是几个都返回第一个
				// return comResult.get(0);
				// 此处可以加快速度
				// WordFather en=comResult.get(0).get(0);
				LinkedList<WordFather> en = comResult.get(0);
				index = 0;
				int ll_len = 0;
				// 添加除了最后一位的所有
				for (WordFather wl : comResult.get(0)) {
					index++;
					if (index >= this.adaptMaxLength - 1) {
						break;
					}
					result.addLast(wl);
					ll_len += wl.word.length();
				}
				start += ll_len;
				end = start + 1;
			}
		}
		return result;
	}

	/**
	 * 简单最大匹配 只取头字母的对应的组 如果返回为null代表不存在 否则饿返回这个组
	 */
	public LinkedList<WordFather> sampleAdapt(String str, String inputData,
			int start) {
		// 获取对应的以str0 为开头的一组words
		HashMap<WordFather, WordFather> re = this.words.words.getWords(str);
		if (re == null) {
			return null;
		}
		// 讲不包含的内容清除掉
		LinkedList<WordFather> result = null;
		String s = inputData.substring(start);
		int oldLength = 0;
		int newLength = 0;
		// 遍历获取的内容 并分组
		for (Entry<WordFather, WordFather> enty : re.entrySet()) {
			oldLength = newLength;
			WordFather word = enty.getKey();
			newLength = word.word.length();
			if (newLength > s.length()) {
				newLength = oldLength;
				continue;
			}
			if (s.startsWith(word.word)) {
				if (result == null) {
					result = new LinkedList<WordFather>();
				}
				result.addLast(word);
			}
		}
		// 如果是否存在改字符
		return result;
	}

	/**
	 * 复杂匹配获取的对应全部组合
	 * 
	 * @param inputData
	 *            输入string
	 * @param endIndex
	 *            第一个单词结束的index值
	 * @param re
	 *            第一个单词的内容
	 * @return
	 */
	public LinkedList<LinkedList<WordFather>> compAdaptMethod(String inputData,
			int startIndex, LinkedList<WordFather> re) {

		LinkedList<LinkedList<WordFather>> result = new LinkedList<LinkedList<WordFather>>();
		int tempIndex;
		// 可优化使用长度作
		// HashSet<Integer> haveIndex=new HashSet<Integer>();
		for (WordFather sr : re) {
			tempIndex = startIndex + sr.word.length();
			// 获取下面几个内容
			LinkedList<WordFather> son = new LinkedList<WordFather>();
			son.add(sr);
			LinkedList<LinkedList<WordFather>> father = new LinkedList<LinkedList<WordFather>>();
			father.add(son);
			LinkedList<LinkedList<WordFather>> rz = compAdaptMethod(inputData,
					tempIndex, father, this.adaptMaxLength - 1);
			// 将father添加入 result中
			for (LinkedList<WordFather> r : rz) {
				result.addLast(r);
			}
		}
		return result;
	}

	/**
	 * 
	 * @param inputData
	 * @param startIndex
	 * @param re
	 * @param words
	 *            组的内容如 有第一组已经存在1-n的方式笛卡尔积
	 * @param surve
	 *            还可迭代的次数
	 * @return
	 */
	public LinkedList<LinkedList<WordFather>> compAdaptMethod(String inputData,
			int startIndex, LinkedList<LinkedList<WordFather>> words, int surve) {
		if (surve <= 0 || startIndex >= inputData.length()) {
			return words;
		}
		// 计算长度
		int tempIndex;
		String str = inputData.substring(startIndex, startIndex + 1);
		LinkedList<WordFather> re = sampleAdapt(str, inputData, startIndex);
		if (re == null) {
			WordFather w = new WordFather(str, 1, null);
			// 添加一个字符
			for (LinkedList<WordFather> wo : words) {
				wo.addLast(w);
			}
			return compAdaptMethod(inputData, startIndex + 1, words, surve - 1);
			// return words;
		} else {
			LinkedList<LinkedList<WordFather>> result = new LinkedList<LinkedList<WordFather>>();
			for (LinkedList<WordFather> w : words) {

				for (WordFather sr : re) {
					LinkedList<WordFather> wo = new LinkedList<WordFather>();
					for (WordFather w2 : w) {
						wo.addLast(w2);
					}
					tempIndex = startIndex + sr.word.length();
					if (inputData.length() < tempIndex) {
						// 如果已经是最后一个则返回null
						continue;
					}
					wo.addLast(sr);
					if (wo.size() == adaptMaxLength) {
						result.addLast(wo);
						continue;
					}
					LinkedList<LinkedList<WordFather>> wo2 = new LinkedList<LinkedList<WordFather>>();
					wo2.add(wo);
					LinkedList<LinkedList<WordFather>> r = compAdaptMethod(
							inputData, tempIndex, wo2, surve - 1);
					// 将信息遍历并放入result中
					for (LinkedList<WordFather> wo3 : r) {
						result.addLast(wo3);
					}

				}
			}
			return result;
		}
	}

	public static void main(String[] args) {
		String lll = "民国时期，尤其是五四以来，中国遭受列强侵略，社会各种思潮流行，舶来文化冲击传统文化，中国小说的发展出现多元化，各类小说题材涌现，其中现代言情小说的发端鸳鸯蝴蝶派就出现在此时。正统小说的代表性人物有“鲁郭茅巴老曹”六大家。晚清民国报纸兴起为小说创作提供了一个上佳的舞台，报纸通过了连载小说招揽人气，小说家通过报纸赚取稿费。近现代几乎所有著名的小说家最初都是从报纸上连载小说开始，从鸳鸯蝴蝶派的张恨水到鲁迅再到当代金庸。"
				+ "第二时期为建国后到文革结束，即1976年以前，是小说的阶级斗争阶段。"
				+ "这一时期的大陆小说的带有明显的政治倾向，同时，这一时期的大陆文艺青年经历了重大的人生转变，命运的沉浮、多视角的阅历以及对价值的思考，为下一个时期的辉煌埋下了伏笔（中国第一位诺贝尔文学奖得主莫言的人生转变就在这一时期）。而在港台，这一时期的言情小说和武侠小说发展到了巅峰，分别产生了琼瑶时代和金庸时代。"
				+ "（3）第三时期为改革开放后二十多年的时期，即2003年以前，是小说的反思和蜕变阶段。"
				+ "这一时期的大陆小说展现了强劲的生命力，文革结束，对外开放，知识分子思想解放，对过去的反思，对未来的向往，传统和新时代的撞击，使得小说界出现欣欣向荣的勃勃生机。以莫言、贾平凹、陈忠实等为代表文革后作家，在此期间创作了许多经典作品，莫言更是凭借在此期间创作的文学作品和影响力，在2012年获得中国第一个诺贝尔文学奖。"
				+ "（4）第四时期为2003年至今，是小说的“表性”网络文学阶段。"
				+ "随着网络普及，网络文学的出现颠覆了传统的书写和传播模式，使小说的发展更加多元，80后90后的生力军开始步入文坛并展现了惊人的创作能力。以起点为代表的武侠玄幻小说作者群和以晋江为代表的言情小说作者群（四小天后、六小公主、八小玲珑）的整体出现，标志着网络小说已经成为主流文学之外的又一创作主体";
		mmseg mm = new mmseg(null, null);
		// LinkedList<LinkedList<WordFather>> re=mm.analyze(lll);
		Date start = new Date();
		File file = new File("src/com/ml/participle/mmseg/衙内当官.txt");
		LinkedList<LinkedList<LinkedList<WordFather>>> ma = mm.analyze(file,
				"gbk");
		int i = 0;
		Date end = new Date();
		Double time = (end.getTime() - start.getTime()) / 1000D;
		System.out.println(time + "\t" + ma.size());
		System.out.println(file.length() / 1024 / time + "kb/s");
		System.out.println(Double.compare(0.333, 0.333));
		if (ma == null) {
			System.out.println("空");
			return;
		}

		System.exit(1);
		for (LinkedList<LinkedList<WordFather>> re : ma) {
			for (LinkedList<WordFather> words : re) {
				i++;
				for (Word word : words) {
					if (word.wordProperty == null) {
						System.out.print(word.word + "[]" + "\t");
					} else {
						System.out.print(word.word + "[" + word.wordProperty[0]
								+ "]" + "\t");
					}
				}
				System.out.println();
			}
		}

	}

}
