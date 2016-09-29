package ps.hell.ml.nlp.own.landerbuluse.participle.word2vec;

import ps.hell.ml.nlp.tool.hanlp.hankcs.hanlp.HanLP;
import ps.hell.ml.nlp.tool.hanlp.hankcs.hanlp.seg.CRF.CRFSegment;
import ps.hell.ml.nlp.tool.hanlp.hankcs.hanlp.seg.Segment;
import ps.hell.ml.nlp.tool.hanlp.hankcs.hanlp.seg.common.Term;
import ps.hell.ml.nlp.own.landerbuluse.participle.mmseg2.MmsegFast;
import ps.hell.util.FileUtil;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

public class Learn {

	private Map<String, Neuron> wordMap = new HashMap<>();
	/**
	 * 训练多少个特征
	 */
	private int layerSize = 200;

	/**
	 * 上下文窗口大小
	 */
	private int window = 5;

	private double sample = 1e-3;
	private double alpha = 0.025;
	private double startingAlpha = alpha;

	public int EXP_TABLE_SIZE = 1000;

	private Boolean isCbow = false;

	private double[] expTable = new double[EXP_TABLE_SIZE];

	private int trainWordsCount = 0;

	private int MAX_EXP = 6;

	public Learn(Boolean isCbow, Integer layerSize, Integer window,
			Double alpha, Double sample) {
		createExpTable();
		if (isCbow != null) {
			this.isCbow = isCbow;
		}
		if (layerSize != null)
			this.layerSize = layerSize;
		if (window != null)
			this.window = window;
		if (alpha != null)
			this.alpha = alpha;
		if (sample != null)
			this.sample = sample;
	}

	public Learn() {
		createExpTable();
	}

	/**
	 * trainModel
	 * 
	 * @throws IOException
	 */
	private void trainModel(File file) throws IOException {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(file)))) {
			String temp = null;
			long nextRandom = 5;
			int wordCount = 0;
			int lastWordCount = 0;
			int wordCountActual = 0;
			while ((temp = br.readLine()) != null) {
				if (wordCount - lastWordCount > 10000) {
					System.out.println("alpha:"
							+ alpha
							+ "\tProgress: "
							+ (int) (wordCountActual
									/ (double) (trainWordsCount + 1) * 100)
							+ "%");
					wordCountActual += wordCount - lastWordCount;
					lastWordCount = wordCount;
					alpha = startingAlpha
							* (1 - wordCountActual
									/ (double) (trainWordsCount + 1));
					if (alpha < startingAlpha * 0.0001) {
						alpha = startingAlpha * 0.0001;
					}
				}
				String[] strs = temp.split(" ");
				wordCount += strs.length;
				List<WordNeuron> sentence = new ArrayList<WordNeuron>();
				for (int i = 0; i < strs.length; i++) {
					Neuron entry = wordMap.get(strs[i]);
					if (entry == null) {
						continue;
					}
					// The subsampling randomly discards frequent words while
					// keeping the ranking same
					if (sample > 0) {
						double ran = (Math.sqrt(entry.freq
								/ (sample * trainWordsCount)) + 1)
								* (sample * trainWordsCount) / entry.freq;
						nextRandom = nextRandom * 25214903917L + 11;
						if (ran < (nextRandom & 0xFFFF) / (double) 65536) {
							continue;
						}
					}
					sentence.add((WordNeuron) entry);
				}

				for (int index = 0; index < sentence.size(); index++) {
					nextRandom = nextRandom * 25214903917L + 11;
					if (isCbow) {
						cbowGram(index, sentence, (int) nextRandom % window);
					} else {
						skipGram(index, sentence, (int) nextRandom % window);
					}
				}

			}
			System.out.println("Vocab size: " + wordMap.size());
			System.out.println("Words in train file: " + trainWordsCount);
			System.out.println("sucess train over!");
		}
	}

	/**
	 * skip gram 模型训练
	 * 
	 * @param sentence
	 * @param b
	 */
	private void skipGram(int index, List<WordNeuron> sentence, int b) {
		// TODO Auto-generated method stub
		WordNeuron word = sentence.get(index);
		int a, c = 0;
		for (a = b; a < window * 2 + 1 - b; a++) {
			if (a == window) {
				continue;
			}
			c = index - window + a;
			if (c < 0 || c >= sentence.size()) {
				continue;
			}

			double[] neu1e = new double[layerSize];// 误差项
			// HIERARCHICAL SOFTMAX
			List<Neuron> neurons = word.neurons;
			WordNeuron we = sentence.get(c);
			for (int i = 0; i < neurons.size(); i++) {
				HiddenNeuron out = (HiddenNeuron) neurons.get(i);
				double f = 0;
				// Propagate hidden -> output
				for (int j = 0; j < layerSize; j++) {
					f += we.syn0[j] * out.syn1[j];
				}
				if (f <= -MAX_EXP || f >= MAX_EXP) {
					continue;
				} else {
					f = (f + MAX_EXP) * (EXP_TABLE_SIZE / MAX_EXP / 2);
					f = expTable[(int) f];
				}
				// 'g' is the gradient multiplied by the learning rate
				double g = (1 - word.codeArr[i] - f) * alpha;
				// Propagate errors output -> hidden
				for (c = 0; c < layerSize; c++) {
					neu1e[c] += g * out.syn1[c];
				}
				// Learn weights hidden -> output
				for (c = 0; c < layerSize; c++) {
					out.syn1[c] += g * we.syn0[c];
				}
			}

			// Learn weights input -> hidden
			for (int j = 0; j < layerSize; j++) {
				we.syn0[j] += neu1e[j];
			}
		}

	}

	/**
	 * 词袋模型
	 * 
	 * @param index
	 * @param sentence
	 * @param b
	 */
	private void cbowGram(int index, List<WordNeuron> sentence, int b) {
		WordNeuron word = sentence.get(index);
		int a, c = 0;

		List<Neuron> neurons = word.neurons;
		double[] neu1e = new double[layerSize];// 误差项
		double[] neu1 = new double[layerSize];// 误差项
		WordNeuron last_word;

		for (a = b; a < window * 2 + 1 - b; a++)
			if (a != window) {
				c = index - window + a;
				if (c < 0)
					continue;
				if (c >= sentence.size())
					continue;
				last_word = sentence.get(c);
				if (last_word == null)
					continue;
				for (c = 0; c < layerSize; c++)
					neu1[c] += last_word.syn0[c];
			}

		// HIERARCHICAL SOFTMAX
		for (int d = 0; d < neurons.size(); d++) {
			HiddenNeuron out = (HiddenNeuron) neurons.get(d);
			double f = 0;
			// Propagate hidden -> output
			for (c = 0; c < layerSize; c++)
				f += neu1[c] * out.syn1[c];
			if (f <= -MAX_EXP)
				continue;
			else if (f >= MAX_EXP)
				continue;
			else
				f = expTable[(int) ((f + MAX_EXP) * (EXP_TABLE_SIZE / MAX_EXP / 2))];
			// 'g' is the gradient multiplied by the learning rate
			// double g = (1 - word.codeArr[d] - f) * alpha;
			// double g = f*(1-f)*( word.codeArr[i] - f) * alpha;
			double g = f * (1 - f) * (word.codeArr[d] - f) * alpha;
			//
			for (c = 0; c < layerSize; c++) {
				neu1e[c] += g * out.syn1[c];
			}
			// Learn weights hidden -> output
			for (c = 0; c < layerSize; c++) {
				out.syn1[c] += g * neu1[c];
			}
		}
		for (a = b; a < window * 2 + 1 - b; a++) {
			if (a != window) {
				c = index - window + a;
				if (c < 0)
					continue;
				if (c >= sentence.size())
					continue;
				last_word = sentence.get(c);
				if (last_word == null)
					continue;
				for (c = 0; c < layerSize; c++)
					last_word.syn0[c] += neu1e[c];
			}

		}
	}

	/**
	 * 统计词频
	 * 
	 * @param file
	 * @throws IOException
	 */
	private void readVocab(File file) throws IOException {
		MapCount<String> mc = new MapCount<>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(file)))) {
			String temp = null;
			while ((temp = br.readLine()) != null) {
				String[] split = temp.split(" ");
				trainWordsCount += split.length;
				for (String string : split) {
					mc.add(string);
				}
			}
		}
		for (Entry<String, Integer> element : mc.get().entrySet()) {
			wordMap.put(element.getKey(), new WordNeuron(element.getKey(),
					element.getValue(), layerSize));
		}
	}

	/**
	 * Precompute the exp() table f(x) = x / (x + 1)
	 */
	private void createExpTable() {
		for (int i = 0; i < EXP_TABLE_SIZE; i++) {
			expTable[i] = Math
					.exp(((i / (double) EXP_TABLE_SIZE * 2 - 1) * MAX_EXP));
			expTable[i] = expTable[i] / (expTable[i] + 1);
		}
	}

	/**
	 * 根据文件学习
	 * 
	 * @param file
	 * @throws IOException
	 */
	public void learnFile(File file) throws IOException {
		readVocab(file);
		new Haffman(layerSize).make(wordMap.values());

		// 查找每个神经元
		for (Neuron neuron : wordMap.values()) {
			((WordNeuron) neuron).makeNeurons();
		}

		trainModel(file);
	}

	/**
	 * 保存模型
	 */
	public void saveModel(File file) {
		// TODO Auto-generated method stub

		try (DataOutputStream dataOutputStream = new DataOutputStream(
				new BufferedOutputStream(new FileOutputStream(file)))) {
			dataOutputStream.writeInt(wordMap.size());
			dataOutputStream.writeInt(layerSize);
			double[] syn0 = null;
			for (Entry<String, Neuron> element : wordMap.entrySet()) {
				dataOutputStream.writeUTF(element.getKey());
				syn0 = ((WordNeuron) element.getValue()).syn0;
				for (double d : syn0) {
					dataOutputStream.writeFloat(((Double) d).floatValue());
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getLayerSize() {
		return layerSize;
	}

	public void setLayerSize(int layerSize) {
		this.layerSize = layerSize;
	}

	public int getWindow() {
		return window;
	}

	public void setWindow(int window) {
		this.window = window;
	}

	public double getSample() {
		return sample;
	}

	public void setSample(double sample) {
		this.sample = sample;
	}

	public double getAlpha() {
		return alpha;
	}

	public void setAlpha(double alpha) {
		this.alpha = alpha;
		this.startingAlpha = alpha;
	}

	public Boolean getIsCbow() {
		return isCbow;
	}

	public void setIsCbow(Boolean isCbow) {
		this.isCbow = isCbow;
	}

	public static LinkedList<String> readFile(String filePath) {
		File file = new File(filePath);
		LinkedList<String> list = new LinkedList<String>();
		if (!file.exists()) {
			System.out.println("文件不存在");
			return null;
		}
		LinkedList<Double[]> retu = new LinkedList<Double[]>();
		BufferedReader reader = null;
		try {

			InputStreamReader read = new InputStreamReader(new FileInputStream(
					file), "gbk");
			reader = new BufferedReader(read);
			String tempString = null;
			int line = 0;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				line++;
				if (tempString.trim().length() > 2) {
					list.add(tempString.trim());
				}
			}
			reader.close();
			read.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("读取异常");
		}
		return list;
	}

	public static void main(String[] args) throws IOException {
		MmsegFast mm = new MmsegFast(null, null);
		// ml\text\participle\mmseg\衙内当官.txt
		File file = new File(
				"src/ps/landerbuluse/ml/text/participle/mmseg/天逆.txt");
		System.out.println("开始解析");
		// LinkedList<LinkedList<LinkedList<WordFather>>> ma = mm.analyze(file,
		// "gbk");
		// System.out.println("解析完成");
		// LinkedList<String> input=new LinkedList<String>();
		// while(ma.size()>0){
		// LinkedList<LinkedList<WordFather>> ma2=ma.pollFirst();
		// while(ma2.size()>0){
		// StringBuffer sb=new StringBuffer();
		// LinkedList<WordFather> ma3=ma2.pollFirst();
		// boolean flag=false;
		// int i=0;
		// while(ma3.size()>0){
		// i++;
		// flag=true;
		// WordFather word=ma3.pollFirst();
		// System.out.print(word);
		// if(i==1){
		// sb.append(word.word);
		// }else{
		// sb.append(" ").append(word.word);
		// }
		// }
		// if(flag){
		// System.out.println();
		// input.add(sb.toString());
		// }
		// }
		// }
		// System.exit(1);
		HanLP.Config.ShowTermNature = false; // 关闭词性显示
		Segment segment = new CRFSegment().enableNameRecognize(true)
				.enableOffset(true);
		LinkedList<String> sentenceArray = readFile(file.getAbsolutePath());
		LinkedList<String> input = new LinkedList<String>();
		while (sentenceArray.size() > 0) {
			List<Term> termList = segment.seg(sentenceArray.pollFirst());
			StringBuffer sb = new StringBuffer();
			int i = 0;
			for (Term term : termList) {
				i++;
				if (i == 1) {
					sb.append(term.word);
				} else {
					sb.append(" ").append(term.word);
				}
			}
			input.add(sb.toString());
			//System.out.println(termList);
		}

		System.out.println("加载完成");
		String path = System.getProperty("user.dir")
				+ "/config/dataset/Text/word2vec/xh.txt";
		String model = System.getProperty("user.dir")
				+ "/config/dataset/Text/word2vec/model";
		FileUtil fileUtil = new FileUtil(path, "utf-8", true, "delete");
		fileUtil.write(input);
		fileUtil.close();
		Learn learn = new Learn();
		long start = System.currentTimeMillis();
		learn.learnFile(new File(path));
		System.out.println("use time " + (System.currentTimeMillis() - start));
		learn.saveModel(new File(model));

	}
}
