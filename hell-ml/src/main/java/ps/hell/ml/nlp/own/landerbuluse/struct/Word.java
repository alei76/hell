package ps.hell.ml.nlp.own.landerbuluse.struct;

import java.util.Arrays;

import com.github.stuxuhai.jpinyin.ChineseHelper;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

/**
 * 词的基础类
 */
public class Word implements Comparable<Word> {
	/**
	 * 词
	 */
	public String word = "";
	/**
	 * 频率
	 */
	public long wordNum = 0;
	/**
	 * 词性
	 */
	public String[] wordProperty = null;

	public Word() {

	}
	/**
	 * 简体转繁体
	 * @return
	 */
	public String convertToTraditionalChinese(){
		return ChineseHelper.convertToTraditionalChinese(word);
	}
	
	/**
	 * 繁体转简体
	 * @return
	 */
	public String convertToSimplifiedChinese(){
		return ChineseHelper.convertToSimplifiedChinese(word);
	}
	/**
	 * 将word 获取Pinyin
	 * @param split
	 * @param type PinyinFormat.WITH_TONE_MARK
	 * 
	 * 
	 *  ",", PinyinFormat.WITH_TONE_MARK)); // nǐ,hǎo,shì,jiè
	 *  ",", PinyinFormat.WITH_TONE_NUMBER)); // ni3,hao3,shi4,jie4
	 *  ",", PinyinFormat.WITHOUT_TONE)); // ni,hao,shi,jie
	 * @return
	 */
	public String convertToPinyinString(String split,PinyinFormat type){
		return PinyinHelper.convertToPinyinString(word, split,type);
	}
	
	/**
	 * 将word 获取Pinyin
	 * @param split
	 * @param type PinyinFormat.WITH_TONE_MARK
	 * 
	 * 
	 *  ",", PinyinFormat.WITH_TONE_MARK)); // nǐ,hǎo,shì,jiè
	 *  ",", PinyinFormat.WITH_TONE_NUMBER)); // ni3,hao3,shi4,jie4
	 *  ",", PinyinFormat.WITHOUT_TONE)); // ni,hao,shi,jie
	 * @return
	 */
	public String convertToPinyinString(int index,String split,PinyinFormat type){
		return PinyinHelper.convertToPinyinString(Character.toString(word.charAt(index)), split,type);
	}
	
	/**
	 * 获取Pinyin首字母
	 * @return
	 */
	public String getShortPinyin(){
		return PinyinHelper.getShortPinyin(word); // nhsj
	}
	/**
	 * 获取第n个字的首字母
	 * @param index
	 * @return
	 */
	public String getShortPinyin(int index){
		return PinyinHelper.getShortPinyin(Character.toString(word.charAt(index)));
	}
	
	/**
	 * 判断首字母是否为多音字
	 * @return
	 */
	public boolean hasMultiPinyin(){
		return PinyinHelper.hasMultiPinyin(word.charAt(0));
	}
	/**
	 * 获取第n个字母是否为多音字
	 * @param type
	 * @return
	 */
	public boolean hasMultiPinyin(int index){
		return PinyinHelper.hasMultiPinyin(word.charAt(index));
	}

	public Word(String word, long wordNum, String[] wordProperty) {
		this.word = word;
		this.wordNum = wordNum;
		this.wordProperty = wordProperty;
	}
	
	public Word(char word, long wordNum, String[] wordProperty) {
		this.word = Character.toString(word);
		this.wordNum = wordNum;
		this.wordProperty = wordProperty;
	}
	public Word(char[] word, long wordNum, String[] wordProperty) {
		this.word = new String(word);
		this.wordNum = wordNum;
		this.wordProperty = wordProperty;
	}
	
	@Override
	public String toString(){
		StringBuffer sb=new StringBuffer();
		sb.append("{").append(this.word).append("/").append(this.wordNum).append("/").append(Arrays.toString(this.wordProperty));
		return sb.toString();
	}

	@Override
	public int compareTo(Word in) {
		return this.word.compareTo(in.word);
	}

	@Override
	public boolean equals(Object in) {
		Word st = (Word) in;
		return this.word.equals(st.word);
	}

	@Override
	public int hashCode() {
		// System.out.println(this.word+":hashCode:"+this.word.hashCode());
		return this.word.hashCode();
	}
}