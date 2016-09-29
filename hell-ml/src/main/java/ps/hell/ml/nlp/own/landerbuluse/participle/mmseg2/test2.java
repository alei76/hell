package ps.hell.ml.nlp.own.landerbuluse.participle.mmseg2;

import ps.hell.ml.nlp.own.landerbuluse.struct.WordFather;
import ps.hell.ml.nlp.own.landerbuluse.struct.WordsGet;

public class test2 {

	public static void main(String[] args) {
		WordsGet words=new WordsGet();
		words.addCineseWord();
		System.out.println(words.wordsCommon.get("民"));
		System.out.println(words.wordsCommon.get("民国"));
		words.addParamsFile();
		System.out.println(words.otherWords.contains(new WordFather(",",1,null)));
	}
}
