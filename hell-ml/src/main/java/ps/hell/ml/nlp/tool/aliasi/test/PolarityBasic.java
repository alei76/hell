package ps.hell.ml.nlp.tool.aliasi.test;

import ps.hell.ml.nlp.tool.aliasi.classify.Classification;
import ps.hell.ml.nlp.tool.aliasi.classify.Classified;
import ps.hell.ml.nlp.tool.aliasi.classify.DynamicLMClassifier;
import ps.hell.ml.nlp.tool.aliasi.lm.NGramProcessLM;
import ps.hell.ml.nlp.tool.aliasi.util.Files;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * 极性
 * @author Administrator
 *
 */
public class PolarityBasic {
	public static void main(String[] args) {
		try {
			new PolarityBasic().run();
		} catch (Throwable t) {
			System.out.println("Thrown: " + t);
			t.printStackTrace(System.out);
		}
	}

	/**
	 * 极性词目录
	 */
	File mPolarityDir;
	/**
	 * 目录对应的neg 核pos目录
	 */
	String[] mCategories;
	DynamicLMClassifier<NGramProcessLM> mClassifier;

	PolarityBasic() {
		System.out.println("\nBASIC POLARITY DEMO");
		mPolarityDir = new File(System.getProperty("user.dir")+"/data/txt_sentoken"); // 获取POLARITY_DIR/txt_sentoken中的语料集
		if(mPolarityDir.exists())
		{
			System.out.println("存在");
		}else{
			System.out.println("文件不存在");
			System.exit(1);;
		}
		mCategories = mPolarityDir.list(); // 获取类别neg，pos
		System.out.println(Arrays.toString(mCategories));
		/**
		 * n值使用8个
		 */
		int nGram = 8;
		mClassifier = DynamicLMClassifier
				.createNGramProcess(mCategories, nGram); // 使用N-Gram分类方法
	}

	// 仅仅先调用训练函数，再调用评估函数
	void run() throws ClassNotFoundException, IOException {
		train();
		evaluate();
	}

	// 我们需要一份测试集和一个训练集，但是我们只有一个语料库，只有人为分割
	// 如果文件名的第2位为9就是训练集
	boolean isTrainingFile(File file) { // 是否是训练集
		return file.getName().charAt(2) != '9'; // test on fold 9
	}

	// 分类器训练
	void train() throws IOException {
		int numTrainingCases = 0; // 训练文本数
		int numTrainingChars = 0; // 训练字符数
		System.out.println("\nTraining.");
		for (int i = 0; i < mCategories.length; ++i) {
			String category = mCategories[i];
			Classification classification = new Classification(category);
			File file = new File(mPolarityDir, mCategories[i]);
			File[] trainFiles = file.listFiles();
			for (int j = 0; j < trainFiles.length; ++j) {
				File trainFile = trainFiles[j];
				if (isTrainingFile(trainFile)) { // 判断一下是为了让一部分数据作为训练集、一部分作为测试集
					++numTrainingCases;
					String review = Files.readFromFile(trainFile, "ISO-8859-1"); // 文本类容
					numTrainingChars += review.length();
					Classified<CharSequence> classified = new Classified<CharSequence>(
							review, classification); // 指定内容和类别
					mClassifier.handle(classified); // 训练
				}
			}
		}
		System.out.println("  # Training Cases=" + numTrainingCases); // 训练集文件数
		System.out.println("  # Training Chars=" + numTrainingChars); // 训练集字符总和
	}

	// 评估
	void evaluate() throws IOException {
		System.out.println("\nEvaluating.");
		int numTests = 0;
		int numCorrect = 0;
		for (int i = 0; i < mCategories.length; ++i) {
			String category = mCategories[i];
			File file = new File(mPolarityDir, mCategories[i]);
			File[] trainFiles = file.listFiles();
			for (int j = 0; j < trainFiles.length; ++j) {
				File trainFile = trainFiles[j];
				if (!isTrainingFile(trainFile)) { // 测试集
					String review = Files.readFromFile(trainFile, "ISO-8859-1");
					++numTests;
					Classification classification = mClassifier
							.classify(review); // 对测试集文本进行分类
					System.out.println("源："+category+"\t测试:"+classification.bestCategory());
					if (classification.bestCategory().equals(category)) { // 分类器决策文本所属类=文本原标注所属类，即正确
						++numCorrect;
					}
				}
			}
		}
		System.out.println("  # Test Cases=" + numTests); // 测试文件数
		System.out.println("  # Correct=" + numCorrect); // 正确数
		System.out.println("  % Correct=" + ((double) numCorrect)
				/ (double) numTests); // 正确率
	}

}
