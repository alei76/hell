package ps.hell.ml.nlp.tool.aliasi.test;

import ps.hell.ml.nlp.tool.aliasi.spell.TfIdfDistance;
import ps.hell.ml.nlp.tool.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import ps.hell.ml.nlp.tool.aliasi.tokenizer.TokenizerFactory;

/**
 * 计算tfidf
 * @author Administrator
 *
 */
public class TfIdfDistanceDemo {

	public static void main(String[] args) {

		args=new String[]{"asdf asdflwer wer asdfl","qwer sdfl wer xc sadf wer","qwer sdfl wer xc df as wer wer"};
		TokenizerFactory tokenizerFactory = IndoEuropeanTokenizerFactory.FACTORY;
		TfIdfDistance tfIdf = new TfIdfDistance(tokenizerFactory);

		for (String s : args)
			tfIdf.handle(s);

		System.out.printf("n %18s %8s %8s\n", "Term", "Doc Freq", "IDF");
		for (String term : tfIdf.termSet())
			System.out.printf(" %18s %8d %8.2f\n", term,
					tfIdf.docFrequency(term), tfIdf.idf(term));
		int i=0;
		for (String s1 : args) {
			i++;
			int j=0;
			for (String s2 : args) {
				j++;
				if(i<=j)
				{
					continue;
				}
				System.out.println("nString1=" + s1);
				System.out.println("String2=" + s2);
				//使用的是余弦相似度
				System.out.printf("distance=%4.2f proximity=%4.2f\n",
						tfIdf.distance(s1, s2), tfIdf.proximity(s1, s2));
			}
		}
	}
}