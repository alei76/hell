package ps.hell.ml.nlp.own.landerbuluse.struct;

public class EmotionWord extends Word {

	/**
	 * 词性
	 */
	public int  wordclass=-1;
	/**
	 * 词义数
	 */
	public int wordCount=1;
	/**
	 * 词义序号
	 */
	public int wordIndex=1;
	/**
	 * 情感分类
	 */
	public int emotionCategory=-1;
	/**
	 * 强度
	 */
	public int power=0;
	/**
	 * 极性
	 */
	public int polr=0;
	/**
	 * 辅助类
	 */
	public int emotionCategory2=-1;
	/**
	 * 强度2
	 */
	public int power2=0;
	/**
	 * 极性2
	 */
	public int polr2=0;
	
	

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public int getWordclass() {
		return wordclass;
	}

	public void setWordclass(int wordclass) {
		this.wordclass = wordclass;
	}

	public int getWordCount() {
		return wordCount;
	}

	public void setWordCount(int wordCount) {
		this.wordCount = wordCount;
	}

	public int getWordIndex() {
		return wordIndex;
	}

	public void setWordIndex(int wordIndex) {
		this.wordIndex = wordIndex;
	}

	public int getEmotionCategory() {
		return emotionCategory;
	}

	public void setEmotionCategory(int emotionCategory) {
		this.emotionCategory = emotionCategory;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	public int getPolr() {
		return polr;
	}

	public void setPolr(int polr) {
		this.polr = polr;
	}

	public int getPower2() {
		return power2;
	}

	public void setPower2(int power2) {
		this.power2 = power2;
	}

	public int getPolr2() {
		return polr2;
	}

	public void setPolr2(int polr2) {
		this.polr2 = polr2;
	}

	public int getEmotionCategory2() {
		return emotionCategory2;
	}

	public void setEmotionCategory2(int emotionCategory2) {
		this.emotionCategory2 = emotionCategory2;
	}
	
	
}
