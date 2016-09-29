package ps.hell.ml.nlp.own.landerbuluse.struct;


import java.util.Arrays;

public class WordFather extends EmotionWord implements Cloneable{

	public WordFather()
	{
		
	}
	public WordFather(String word,long wordNum,String[] wordProperty)
	{
		this.word=word;
		this.wordNum=wordNum;
		this.wordProperty=wordProperty;
	}
	public WordFather(long wordNum,String[] wordProperty)
	{
		this.wordNum=wordNum;
		this.wordProperty=wordProperty;
	}
	
	@Override
	public WordFather clone()
	{
		return new WordFather(this.wordNum,this.wordProperty);
	}
	@Override
	public String toString()
	{
		return "["+word+","+wordNum+","+ Arrays.toString(wordProperty)+"]";
	}
}
