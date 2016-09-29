package ps.hell.ml.nlp.tool.aliasi.test;

import ps.hell.ml.nlp.tool.aliasi.classify.ConfusionMatrix;
import ps.hell.ml.nlp.tool.aliasi.classify.ScoredClassification;
import ps.hell.ml.nlp.tool.aliasi.classify.ScoredClassifier;
import ps.hell.ml.nlp.tool.aliasi.util.Files;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.NumberFormat;

public class TestTClassifier {

	// 测试语料的存放目录
	private static File TDIR = new File("f:\\data\\test");
	private static String[] CATEGORIES = { "金融", "军事", "医学", "饮食" };

	public static void main(String[] args) throws ClassNotFoundException {

		// 分类器模型存放地址
		String modelFile = "f:\\data\\category\\tclassifier";
		ScoredClassifier<CharSequence> compiledClassifier = null;
		try {
			ObjectInputStream oi = new ObjectInputStream(new FileInputStream(
					modelFile));
			compiledClassifier = (ScoredClassifier<CharSequence>) oi
					.readObject();
			oi.close();
		} catch (IOException ie) {
			System.out
					.println("IO Error: Model file " + modelFile + " missing");
		}

		// 遍历分类目录中的文件测试分类准确度
		ConfusionMatrix confMatrix = new ConfusionMatrix(CATEGORIES);
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumIntegerDigits(1);
		nf.setMaximumFractionDigits(3);
		for (int i = 0; i < CATEGORIES.length; ++i) {
			File classDir = new File(TDIR, CATEGORIES[i]);

			// 对于每一个文件，通过分类器找出最适合的分类
			for (File file : classDir.listFiles()) {
				String text = "";
				try {
					text = Files.readFromFile(file, "utf-8");
				} catch (IOException ie) {
					System.out.println("不能读取 " + file.getName());
				}
				System.out.println("测试 " + CATEGORIES[i] + File.separator
						+ file.getName());

				ScoredClassification classification = compiledClassifier
						.classify(text.subSequence(0, text.length()));
				confMatrix.increment(CATEGORIES[i],
						classification.bestCategory());
				System.out.println("最适合的分类: " + classification.bestCategory());
			}
		}

		System.out.println("--------------------------------------------");
		System.out.println("- 结果 ");
		System.out.println("--------------------------------------------");
		int[][] imatrix = confMatrix.matrix();
		StringBuffer sb = new StringBuffer();
		sb.append(StringTools.fillin("CATEGORY", 10, true, ' '));
		for (int i = 0; i < CATEGORIES.length; i++)
			sb.append(StringTools.fillin(CATEGORIES[i], 8, false, ' '));
		System.out.println(sb.toString());

		for (int i = 0; i < imatrix.length; i++) {
			sb = new StringBuffer();
			sb.append(StringTools.fillin(CATEGORIES[i], 10, true, ' ',
					10 - CATEGORIES[i].length()));
			for (int j = 0; j < imatrix.length; j++) {
				String out = "" + imatrix[i][j];
				sb.append(StringTools.fillin(out, 8, false, ' ',
						8 - out.length()));
			}
			System.out.println(sb.toString());
		}

		System.out.println("准确度: " + nf.format(confMatrix.totalAccuracy()));
		System.out.println("总共正确数 : " + confMatrix.totalCorrect());
		System.out.println("总数：" + confMatrix.totalCount());
	}
}