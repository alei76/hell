package ps.hell.ml.nlp.own.landerbuluse.participle.mmseg2;

import ps.hell.ml.nlp.own.landerbuluse.struct.EmotionStatic;
import ps.hell.ml.nlp.own.landerbuluse.struct.WordFather;
import ps.hell.ml.nlp.own.landerbuluse.struct.WordsGet;

import java.util.LinkedList;

public class test {

	
	public static void main(String[] args) {
		String lll="让我说诺基亚手机比微软手机好";
		String ll2="让我说诺基亚手机比微软手机";
//		System.out.println(lll.hashCode());
//		System.out.println(ll2.hashCode());
//		System.exit(1);
		mmseg mm=new mmseg(null,null);
		LinkedList<LinkedList<WordFather>> re=mm.analyze(lll);
		for(LinkedList<WordFather> list:re)
		{
			for(WordFather word:list)
			{
				System.out.println(word.word+"\\");
			}
		}
		WordsGet words=new WordsGet();
//		words.addChineseFamousPersonFile();
//		words.addChinaAddressFile();
//		words.addCineseWord();
		words.addEmotionFile();
		mmseg mm2=new mmseg(null,null);
		mm2.words=words;
		LinkedList<LinkedList<WordFather>> re2=mm2.analyzeEmotion(re);
		for(LinkedList<WordFather> list:re2)
		{
			for(WordFather word:list)
			{
				//EmotionWord emo=(EmotionWord)word;
				System.out.println(word.word+"\t"+EmotionStatic.mapRev.get(word.emotionCategory)+"\t"+word.getPolr());//+emo.getEmotionCategory());
			}
		}
	}
}
