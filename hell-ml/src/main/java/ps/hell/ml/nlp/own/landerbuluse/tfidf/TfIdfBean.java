package ps.hell.ml.nlp.own.landerbuluse.tfidf;

import java.util.Arrays;

import ps.hell.ml.nlp.own.landerbuluse.struct.WordFather;

public class TfIdfBean implements Comparable<TfIdfBean>{

	
	public WordFather word= null;
	
	public float value=0f;
	public TfIdfBean()
	{
	}
	public TfIdfBean(WordFather word,float value)
	{
		this.word=word;
		this.value=value;
	}

	@Override
	public int compareTo(TfIdfBean o) {
		// TODO Auto-generated method stub
		return Float.compare(o.value,this.value);
	}
	
	@Override
	public String toString()
	{
		return word.word+"\t:"+Arrays.toString(word.wordProperty)+"\t:"+value;
	}
	
	public void readString(String str)
	{
		String[] split=str.split("\t:");
		word=new WordFather();
		word.word=split[0];
		word.wordProperty=split[1].split(",");
		value=Float.parseFloat(split[2]);
	}
}
