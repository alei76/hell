package ps.hell.ml.nlp.own.landerbuluse.struct;

import com.github.stuxuhai.jpinyin.ChineseHelper;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;


public class WordPinyin {

	public static void main(String[] args) {
		Word word=new Word();
		word.word="中国人民大学";
		String str = "你好世界";
		System.out.println(PinyinHelper.convertToPinyinString(str, ",", PinyinFormat.WITH_TONE_MARK)); // nǐ,hǎo,shì,jiè
		System.out.println(PinyinHelper.convertToPinyinString(str, ",", PinyinFormat.WITH_TONE_NUMBER)); // ni3,hao3,shi4,jie4
		System.out.println(PinyinHelper.convertToPinyinString(str, ",", PinyinFormat.WITHOUT_TONE)); // ni,hao,shi,jie
		System.out.println(PinyinHelper.getShortPinyin(str)); // nhsj
		//判断是否为多音字
		System.out.println(PinyinHelper.hasMultiPinyin('说'));
		//简体繁体转换
		System.out.println(ChineseHelper.convertToTraditionalChinese("数量"));
		
	}
}
