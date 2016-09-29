package ps.hell.ml.nlp.own.landerbuluse.tfidf;

import java.util.*;
import java.util.Map.Entry;

import ps.hell.math.base.MathBase;
import ps.hell.ml.nlp.own.landerbuluse.participle.mmseg2.mmseg;
import ps.hell.ml.nlp.own.landerbuluse.struct.WordFather;
import ps.hell.util.FileUtil;


public class TfIdfUtil {
	/**
	 * 分词器
	 */
	public mmseg mmseg = null;
	/**
	 * 使用全部存储
	 */
	public ArrayList<HashMap<WordFather, Integer>> wordSeq = new ArrayList<HashMap<WordFather, Integer>>();

	public TfIdfUtil(mmseg mmseg) {
		this.mmseg = mmseg;
	}

	/**
	 * 获取tfidf值
	 * 
	 * @param word
	 * @return
	 */
	public float getTfIdf(String word) {
		LinkedList<LinkedList<WordFather>> wordList = mmseg.analyze(word);
		float val = 0f;
		for (LinkedList<WordFather> wordFatherList : wordList) {
			for (WordFather wordFather : wordFatherList) {
				val += getTfIdf(wordFather);
			}
		}
		return val;
	}

	/**
	 * 获取一个
	 * 
	 * @param wordFather
	 * @return
	 */
	public float getTfIdf(WordFather wordFather) {
		float val = 0f;
		LinkedList<Float> tfList = tf(wordFather);
		float dif = idfInvert(wordFather);
		for (Float f : tfList) {
			val += dif * f;
			// System.out.println("dif:"+dif+"\t"+f);
		}
		return val;
	}

	/**
	 * 获取一个
	 * 
	 * @param wordFather
	 * @return
	 */
	public TfIdfBean getTfIdfBean(WordFather wordFather) {
		float val = 0f;
		LinkedList<Float> tfList = tf(wordFather);
		float dif = idfInvert(wordFather);
		for (Float f : tfList) {
			val += dif * f;
			// System.out.println("dif:"+dif+"\t"+f);
		}
		return new TfIdfBean(wordFather, val);
	}

	/**
	 * 获取tf值 所有float>0的值
	 * 
	 * @param wordF
	 * @return
	 */
	public LinkedList<Float> tf(WordFather wordF) {
		LinkedList<Float> list = new LinkedList<Float>();
		if (wordSeq.size() == 0) {
		} else {
			for (HashMap<WordFather, Integer> wordFather : wordSeq) {
				int allCount = 0;
				int count = 0;
				for (Entry<WordFather, Integer> wordEntry : wordFather
						.entrySet()) {
					if (wordEntry.getKey().compareTo(wordF) == 0) {
						count += wordEntry.getValue();
					}
					allCount += wordEntry.getValue();
				}
				if (allCount == 0 || count == 0) {
				} else {
					list.add(count * 1f / allCount);
				}
			}
		}
		return list;
	}

	/**
	 * 获取tf值 所有float的值
	 * 
	 * @param wordF
	 * @return
	 */
	public LinkedList<Float> tfAll(WordFather wordF) {
		LinkedList<Float> list = new LinkedList<Float>();
		if (wordSeq.size() == 0) {
		} else {
			for (HashMap<WordFather, Integer> wordFather : wordSeq) {
				int allCount = 0;
				int count = 0;
				for (Entry<WordFather, Integer> wordEntry : wordFather
						.entrySet()) {
					if (wordEntry.getKey().compareTo(wordF) == 0) {
						count += wordEntry.getValue();
					}
					allCount += wordEntry.getValue();
				}
				if (allCount == 0 || count == 0) {
					list.add(0f);
				} else {
					list.add(count * 1f / allCount);
				}
			}
		}
		return list;
	}

	/**
	 * 获取某个词的inverse document frequency 
	 *  <code>log(1 + (numDocs - docFreq + 0.5)/(docFreq + 0.5))</code>. 
	 * @param wordF
	 * @return
	 */
	public float idf(WordFather wordF) {
		if (wordSeq.size() == 0) {
			return Float.MAX_VALUE;
		}
		int docFreq= getIdfSize(wordF);
		// 分词
		return (float) (Math.log(1+((wordSeq.size()-docFreq+0.5f)
				/ (docFreq+0.5))) / MathBase.log2);

	}
	
	public float idf(int docFreq,int numDocs){
		return (float) (Math.log(1+((docFreq-docFreq+0.5f)
				/ (docFreq+0.5))) / MathBase.log2);
	}
	
	public float tfidf(float tf,float idf){
		return tf/idf;
	}

	/**
	 * 获取某个词的inverse document frequency all/count
	 * 
	 * @param wordF
	 * @return
	 */
	public float idfInvert(WordFather wordF) {
		return 1 / idf(wordF);
	}

	/**
	 * 获取word 在 list中出现的次数
	 * 
	 * @param wordF
	 */
	public int getIdfSize(WordFather wordF) {
		int count = 0;
		for (HashMap<WordFather, Integer> wordFatherList : wordSeq) {
			if (wordFatherList.containsKey(wordF)) {
				count++;
			}
		}
		return count;
	}

	/**
	 * 添加分词后的
	 * 
	 * @param wordSeq
	 */
	public void add(LinkedList<WordFather> wordSeq) {
		HashMap<WordFather, Integer> map = new HashMap<WordFather, Integer>();
		for (WordFather wordFather : wordSeq) {
			Integer count = map.get(wordFather);
			if (count == null) {
				count = 1;
			} else {
				count += 1;
			}
			map.put(wordFather, count);
		}
		this.wordSeq.add(map);
	}

	/**
	 * 添加分词后的
	 * 
	 * @param wordSeq
	 */
	public void addList(LinkedList<LinkedList<WordFather>> wordSeq) {
		for (LinkedList<WordFather> listWordSeq : wordSeq) {
			HashMap<WordFather, Integer> map = new HashMap<WordFather, Integer>();
			for (WordFather wordFather : listWordSeq) {
				Integer count = map.get(wordFather);
				if (count == null) {
					count = 1;
				} else {
					count += 1;
				}
				map.put(wordFather, count);
			}
			this.wordSeq.add(map);
		}
	}

	/**
	 * 添加分词后的
	 * 后三个为 分词后结果 第一个为多组
	 * @param wordSeq
	 */
	public void addList(ArrayList<LinkedList<LinkedList<WordFather>>> wordSeq) {
		for (LinkedList<LinkedList<WordFather>> words : wordSeq) {
			HashMap<WordFather, Integer> map = new HashMap<WordFather, Integer>();
			for (LinkedList<WordFather> word : words) {
				for (WordFather w : word) {
					Integer count = map.get(w);
					if (count == null) {
						count = 1;
					} else {
						count += 1;
					}
					map.put(w, count);
				}
			}
			this.wordSeq.add(map);
		}
	}

	/**
	 * 将需要的 词语 及 值 全部写入文件中
	 * 
	 * @param set
	 * @param countLimit
	 *            数量限制
	 */
	public void writeTFIDFSortWord(HashSet<WordFather> set, int countLimit) {
		ArrayList<TfIdfBean> output = new ArrayList<TfIdfBean>();
		for (WordFather word : set) {
			output.add(getTfIdfBean(word));
			// System.out.println(word.word+"\t"+tfidf.getTfIdf(word));
		}
		Collections.sort(output);
		System.out.println("大小:" + output.size());
		int count2 = 0;
		ArrayList<String> input = new ArrayList<String>();
		for (TfIdfBean bean : output) {
			count2++;
			input.add(bean.toString());
			if (count2 >= countLimit) {
				break;
			}
		}
		FileUtil TFIDF = new FileUtil(System.getProperty("user.dir")
				+ "/config/dataset/Text/TFIDF/tfidf", "utf-8", true,"delete");
		TFIDF.write(input);
		TFIDF.close();
	}

	/**
	 * 将需要的 词语 及 值 全部写入文件中
	 * 
	 * @param set
	 * @param countLimit
	 *            数量限制
	 */
	public void writeTFIDFSortWord(HashSet<WordFather> set, int countLimit,
			String outputPath) {
		ArrayList<TfIdfBean> output = new ArrayList<TfIdfBean>();
		for (WordFather word : set) {
			output.add(getTfIdfBean(word));
			// System.out.println(word.word+"\t"+tfidf.getTfIdf(word));
		}
		Collections.sort(output);
		System.out.println("大小:" + output.size());
		int count2 = 0;
		ArrayList<String> input = new ArrayList<String>();
		for (TfIdfBean bean : output) {
			count2++;
			input.add(bean.toString());
			if (count2 >= countLimit) {
				break;
			}
		}
		FileUtil TFIDF = new FileUtil(outputPath, "utf-8", true,"delete");
		TFIDF.write(input);
		TFIDF.close();
	}

	/**
	 * 将需要的 词语 及 值 全部写入文件中
	 * 
	 * @param set
	 *            数量限制
	 */
	public ArrayList<TfIdfBean> readTFIDFSortWord(HashSet<WordFather> set) {
		ArrayList<TfIdfBean> output = new ArrayList<TfIdfBean>();
		for (WordFather word : set) {
			output.add(getTfIdfBean(word));
			// System.out.println(word.word+"\t"+tfidf.getTfIdf(word));
		}
		Collections.sort(output);
		return output;
	}

	/**
	 * 将需要的 词语 及 值 全部写入文件中
	 *            数量限制
	 */
	public ArrayList<TfIdfBean> readTFIDFSortWordFromFile() {
		ArrayList<TfIdfBean> out = new ArrayList<TfIdfBean>();
		FileUtil fileUtil = new FileUtil(System.getProperty("user.dir")
				+ "/config/dataset/Text/TFIDF/tfidf", "utf-8", false,null);
		ArrayList<String> input = fileUtil.readAndClose();
		for(int i=0;i<input.size();i++) {
			TfIdfBean tfidf = new TfIdfBean();
			tfidf.readString(input.get(i));
			out.add(tfidf);
		}
		return out;
	}

	/**
	 * 将需要的 词语 及 值 全部写入文件中
	 * 
	 * @param filePath
	 */
	public ArrayList<TfIdfBean> readTFIDFSortWordFromFile(String filePath) {
		ArrayList<TfIdfBean> out = new ArrayList<TfIdfBean>();
		FileUtil fileUtil = new FileUtil(filePath, "utf-8", false,null);
		ArrayList<String> input = fileUtil.readAndClose();
		for(int i=0;i<input.size();i++){
			TfIdfBean tfidf = new TfIdfBean();
			tfidf.readString(input.get(i));
			out.add(tfidf);
		}
		return out;
	}

	public static void main(String[] args) {
		// 使用分词器
		mmseg mm = new mmseg(null, null);
		String str = "最喜欢这里的面茶，但是对奶油炸糕非常失望，根本不像小时候吃的那么好吃，这里的奶油炸糕一咬一嘴油，吃一个就觉得特别腻了。";
		LinkedList<String> input = new LinkedList<String>();
		input.add(str);
		input.add("非常热心，做的很认真。推荐!环境很好，价位适中，以后还会过来做美甲和睫毛。特别");
		input.add("做的很推荐!，以后还会过来做美甲和睫毛。特别");
		ArrayList<LinkedList<LinkedList<WordFather>>> re = mm.analyze(input);
		re = mm.clearnStop(re);
		HashSet<WordFather> set = new HashSet<WordFather>();
		LinkedList<LinkedList<WordFather>> wordSeqAll = new LinkedList<LinkedList<WordFather>>();
		for (LinkedList<LinkedList<WordFather>> st : re) {
			System.out.println("第一组:");
			LinkedList<WordFather> wordSeq = new LinkedList<WordFather>();
			for (LinkedList<WordFather> list : st) {
				for (WordFather word : list) {
					set.add(word);
					System.out.print(word.word + "\t"
							+ Arrays.toString(word.wordProperty) + "\t");
				}
				wordSeq.addAll(list);
			}
			System.out.println();
			wordSeqAll.add(wordSeq);
		}

		TfIdfUtil tfidf = new TfIdfUtil(mm);
		tfidf.addList(wordSeqAll);
		// ArrayList<TfIdfBean> output=new ArrayList<TfIdfBean>();
		// for(WordFather word:set)
		// {
		// output.add(tfidf.getTfIdfBean(word));
		// // System.out.println(word.word+"\t"+tfidf.getTfIdf(word));
		// }
		// Collections.sort(output);
		// for(TfIdfBean bean:output)
		// {
		// System.out.println(bean.word.word+"\t"+bean.value);
		// }
		tfidf.writeTFIDFSortWord(set, 400,System.getProperty("user.dir")
				+ "/config/dataset/Text/TFIDF/tfidf");
	}
}
