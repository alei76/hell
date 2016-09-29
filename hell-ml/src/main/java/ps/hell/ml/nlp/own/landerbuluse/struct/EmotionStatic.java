package ps.hell.ml.nlp.own.landerbuluse.struct;

import java.util.HashMap;

public class EmotionStatic {

	/**
	 * 名词
	 */
	public static int noun=1;
	/**
	 * 动词
	 */
	public static int verb=2;
	/**
	 * 形容词
	 */
	public static int adj=3;
	/**
	 * 副词
	 */
	public static int adv=4;
	/**
	 * 网络词
	 */
	public static int nw=5;
	/**
	 * 成语
	 */
	public static int idiom=6;
	/**
	 * 介词短语
	 */
	public static int prep=7;
	
	//	每个词在每一类情感下都对应了一个极性。其中，0代表中性，1代表褒义，2代表贬义，3代表兼有褒贬两性。
	//注：褒贬标注时，通过词本身和情感共同确定，所以有些情感在一些词中可能极性1，而其他的词中有可能极性为0。
	/**
	 * 乐
	 * 快乐
	 */
	public static int pa=11;
	/**
	 * 乐
	 * 安心
	 */
	public static int pe=12;
	/**
	 * 好
	 * 尊敬
	 */
	public static int pd=13;
	/**
	 * 好
	 * 表扬
	 */
	public static int ph=14;
	/**
	 * 好
	 * 相信
	 */
	public static int pg=15;
	/**
	 * 好
	 * 喜爱
	 */
	public static int pb=16;
	/**
	 * 好 
	 * 祝愿
	 */
	public static int pk=17;
	/**
	 * 怒
	 * 愤怒
	 */
	public static int na=18;
	/**
	 * 哀
	 * 悲伤
	 */
	public static int nb=19;
	/**
	 * 哀
	 * 失望
	 */
	public static int nj=20;
	/**
	 * 哀
	 * 疚
	 */
	public static int nh=21;
	/**
	 * 哀
	 * 思
	 */
	public static int pf=22;
	/**
	 * 惧
	 * 慌
	 */
	public static int ni=23;
	/**
	 * 惧
	 * 恐惧
	 */
	public static int nc=24;
	/**
	 * 惧
	 * 羞
	 */
	public static int ng=25;
	/**
	 * 恶
	 * 烦闷
	 */
	public static int ne=26;
	/**
	 * 恶
	 * 憎恶
	 */
	public static int nd=27;
	/**
	 * 恶
	 * 贬责
	 */
	public static int nn=28;
	/**
	 * 恶
	 * 嫉妒
	 */
	public static int nk=29;
	/**
	 * 恶
	 * 怀疑
	 */
	public static int nl=30;
	/**
	 * 惊
	 * 惊奇
	 */
	public static int pc =31;
	
	public static HashMap<String,Integer> map=new HashMap<String,Integer>();
	public static HashMap<Integer,String> mapRev=new HashMap<Integer,String>();
	/**
	 * 类别对应的中文名字
	 */
	public static HashMap<String,String> mapName=new HashMap<String,String>();
	/**
	 * 类别对应的中文名字
	 */
	public static HashMap<String,String> mapNameTwo=new HashMap<String,String>();
	/**
	 * 获取以及情感词性
	 * @param simple
	 * @return
	 */
	public static String getSimpleNameCategoryOne(String simple){
		return mapName.get(simple);
	}
	/**
	 * 获取二级情感词性
	 * @param simple
	 * @return
	 */
	public static String getSimpleNameCategoryTwo(String simple){
		return mapNameTwo.get(simple);
	}
	static {
		/**
		 * 名词
		 */
		map.put("noun".toUpperCase(),1);
		/**
		 * 动词
		 */
		map.put("verb".toUpperCase(),2);
		/**
		 * 形容词
		 */
		map.put("adj".toUpperCase(),3);
		/**
		 * 副词
		 */
		map.put("adv".toUpperCase(),4);
		/**
		 * 网络词
		 */
		map.put("nw".toUpperCase(),5);
		/**
		 * 成语
		 */
		map.put("idiom".toUpperCase(),6);
		/**
		 * 介词短语
		 */
		map.put("prep".toUpperCase(),7);
		
		//	每个词在每一类情感下都对应了一个极性。其中，0代表中性，1代表褒义，2代表贬义，3代表兼有褒贬两性。
		//注：褒贬标注时，通过词本身和情感共同确定，所以有些情感在一些词中可能极性1，而其他的词中有可能极性为0。
		/**
		 * 乐
		 * 快乐
		 */
		map.put("pa".toUpperCase(),11);
		/**
		 * 乐
		 * 安心
		 */
		map.put("pe".toUpperCase(),12);
		/**
		 * 好
		 * 尊敬
		 */
		map.put("pd".toUpperCase(),13);
		/**
		 * 好
		 * 表扬
		 */
		map.put("ph".toUpperCase(),14);
		/**
		 * 好
		 * 相信
		 */
		map.put("pg".toUpperCase(),15);
		/**
		 * 好
		 * 喜爱
		 */
		map.put("pb".toUpperCase(),16);
		/**
		 * 好 
		 * 祝愿
		 */
		map.put("pk".toUpperCase(),17);
		/**
		 * 怒
		 * 愤怒
		 */
		map.put("na".toUpperCase(),18);
		/**
		 * 哀
		 * 悲伤
		 */
		map.put("nb".toUpperCase(),19);
		/**
		 * 哀
		 * 失望
		 */
		map.put("nj".toUpperCase(),20);
		/**
		 * 哀
		 * 疚
		 */
		map.put("nh".toUpperCase(),21);
		/**
		 * 哀
		 * 思
		 */
		map.put("pf".toUpperCase(),22);
		/**
		 * 惧
		 * 慌
		 */
		map.put("ni".toUpperCase(),23);
		/**
		 * 惧
		 * 恐惧
		 */
		map.put("nc".toUpperCase(),24);
		/**
		 * 惧
		 * 羞
		 */
		map.put("ng".toUpperCase(),25);
		/**
		 * 恶
		 * 烦闷
		 */
		map.put("ne".toUpperCase(),26);
		/**
		 * 恶
		 * 憎恶
		 */
		map.put("nd".toUpperCase(),27);
		/**
		 * 恶
		 * 贬责
		 */
		map.put("nn".toUpperCase(),28);
		/**
		 * 恶
		 * 嫉妒
		 */
		map.put("nk".toUpperCase(),29);
		/**
		 * 恶
		 * 怀疑
		 */
		map.put("nl".toUpperCase(),30);
		/**
		 * 惊
		 * 惊奇
		 */
		map.put("pc".toUpperCase(),31);
		
		
		//--------------------------------

		/**
		 * 名词
		 */
		mapRev.put(1,"noun".toUpperCase());
		/**
		 * 动词
		 */
		mapRev.put(2,"verb".toUpperCase());
		/**
		 * 形容词
		 */
		mapRev.put(3,"adj".toUpperCase());
		/**
		 * 副词
		 */
		mapRev.put(4,"adv".toUpperCase());
		/**
		 * 网络词
		 */
		mapRev.put(5,"nw".toUpperCase());
		/**
		 * 成语
		 */
		mapRev.put(6,"idiom".toUpperCase());
		/**
		 * 介词短语
		 */
		mapRev.put(7,"prep".toUpperCase());
		
		//	每个词在每一类情感下都对应了一个极性。其中，0代表中性，1代表褒义，2代表贬义，3代表兼有褒贬两性。
		//注：褒贬标注时，通过词本身和情感共同确定，所以有些情感在一些词中可能极性1，而其他的词中有可能极性为0。
		/**
		 * 乐
		 * 快乐
		 */
		mapRev.put(11,"pa".toUpperCase());
		/**
		 * 乐
		 * 安心
		 */
		mapRev.put(12,"pe".toUpperCase());
		/**
		 * 好
		 * 尊敬
		 */
		mapRev.put(13,"pd".toUpperCase());
		/**
		 * 好
		 * 表扬
		 */
		mapRev.put(14,"ph".toUpperCase());
		/**
		 * 好
		 * 相信
		 */
		mapRev.put(15,"pg".toUpperCase());
		/**
		 * 好
		 * 喜爱
		 */
		mapRev.put(16,"pb".toUpperCase());
		/**
		 * 好 
		 * 祝愿
		 */
		mapRev.put(17,"pk".toUpperCase());
		/**
		 * 怒
		 * 愤怒
		 */
		mapRev.put(18,"na".toUpperCase());
		/**
		 * 哀
		 * 悲伤
		 */
		mapRev.put(19,"nb".toUpperCase());
		/**
		 * 哀
		 * 失望
		 */
		mapRev.put(20,"nj".toUpperCase());
		/**
		 * 哀
		 * 疚
		 */
		mapRev.put(21,"nh".toUpperCase());
		/**
		 * 哀
		 * 思
		 */
		mapRev.put(22,"pf".toUpperCase());
		/**
		 * 惧
		 * 慌
		 */
		mapRev.put(23,"ni".toUpperCase());
		/**
		 * 惧
		 * 恐惧
		 */
		mapRev.put(24,"nc".toUpperCase());
		/**
		 * 惧
		 * 羞
		 */
		mapRev.put(25,"ng".toUpperCase());
		/**
		 * 恶
		 * 烦闷
		 */
		mapRev.put(26,"ne".toUpperCase());
		/**
		 * 恶
		 * 憎恶
		 */
		mapRev.put(27,"nd".toUpperCase());
		/**
		 * 恶
		 * 贬责
		 */
		mapRev.put(28,"nn".toUpperCase());
		/**
		 * 恶
		 * 嫉妒
		 */
		mapRev.put(29,"nk".toUpperCase());
		/**
		 * 恶
		 * 怀疑
		 */
		mapRev.put(30,"nl".toUpperCase());
		/**
		 * 惊
		 * 惊奇
		 */
		mapRev.put(31,"pc".toUpperCase());
		
		//////////////////////////
		
		/**
		 * 名词
		 */
		mapName.put("noun".toUpperCase(),"名词");
		/**
		 * 动词
		 */
		mapName.put("verb".toUpperCase(),"动词");
		/**
		 * 形容词
		 */
		mapName.put("adj".toUpperCase(),"形容词");
		/**
		 * 副词
		 */
		mapName.put("adv".toUpperCase(),"副词");
		/**
		 * 网络词
		 */
		mapName.put("nw".toUpperCase(),"网络词");
		/**
		 * 成语
		 */
		mapName.put("idiom".toUpperCase(),"成语");
		/**
		 * 介词短语
		 */
		mapName.put("prep".toUpperCase(),"介词短语");
		
		//	每个词在每一类情感下都对应了一个极性。其中，0代表中性，1代表褒义，2代表贬义，3代表兼有褒贬两性。
		//注：褒贬标注时，通过词本身和情感共同确定，所以有些情感在一些词中可能极性1，而其他的词中有可能极性为0。
		/**
		 * 乐
		 * 快乐
		 */
		mapName.put("pa".toUpperCase(),"乐");
		/**
		 * 乐
		 * 安心
		 */
		mapName.put("pe".toUpperCase(),"乐");
		/**
		 * 好
		 * 尊敬
		 */
		mapName.put("pd".toUpperCase(),"好");
		/**
		 * 好
		 * 表扬
		 */
		mapName.put("ph".toUpperCase(),"好");
		/**
		 * 好
		 * 相信
		 */
		mapName.put("pg".toUpperCase(),"好");
		/**
		 * 好
		 * 喜爱
		 */
		mapName.put("pb".toUpperCase(),"好");
		/**
		 * 好 
		 * 祝愿
		 */
		mapName.put("pk".toUpperCase(),"好");
		/**
		 * 怒
		 * 愤怒
		 */
		mapName.put("na".toUpperCase(),"怒");
		/**
		 * 哀
		 * 悲伤
		 */
		mapName.put("nb".toUpperCase(),"哀");
		/**
		 * 哀
		 * 失望
		 */
		mapName.put("nj".toUpperCase(),"哀");
		/**
		 * 哀
		 * 疚
		 */
		mapName.put("nh".toUpperCase(),"哀");
		/**
		 * 哀
		 * 思
		 */
		mapName.put("pf".toUpperCase(),"哀");
		/**
		 * 惧
		 * 慌
		 */
		mapName.put("ni".toUpperCase(),"惧");
		/**
		 * 惧
		 * 恐惧
		 */
		mapName.put("nc".toUpperCase(),"惧");
		/**
		 * 惧
		 * 羞
		 */
		mapName.put("ng".toUpperCase(),"惧");
		/**
		 * 恶
		 * 烦闷
		 */
		mapName.put("ne".toUpperCase(),"恶");
		/**
		 * 恶
		 * 憎恶
		 */
		mapName.put("nd".toUpperCase(),"恶");
		/**
		 * 恶
		 * 贬责
		 */
		mapName.put("nn".toUpperCase(),"恶");
		/**
		 * 恶
		 * 嫉妒
		 */
		mapName.put("nk".toUpperCase(),"恶");
		/**
		 * 恶
		 * 怀疑
		 */
		mapName.put("nl".toUpperCase(),"恶");
		/**
		 * 惊
		 * 惊奇
		 */
		mapName.put("pc".toUpperCase(),"惊");
		
		////////////////////////////
		
		
		/**
		 * 名词
		 */
		mapNameTwo.put("noun".toUpperCase(),"名词");
		/**
		 * 动词
		 */
		mapNameTwo.put("verb".toUpperCase(),"动词");
		/**
		 * 形容词
		 */
		mapNameTwo.put("adj".toUpperCase(),"形容词");
		/**
		 * 副词
		 */
		mapNameTwo.put("adv".toUpperCase(),"副词");
		/**
		 * 网络词
		 */
		mapNameTwo.put("nw".toUpperCase(),"网络词");
		/**
		 * 成语
		 */
		mapNameTwo.put("idiom".toUpperCase(),"成语");
		/**
		 * 介词短语
		 */
		mapNameTwo.put("prep".toUpperCase(),"介词短语");
		
		//	每个词在每一类情感下都对应了一个极性。其中，0代表中性，1代表褒义，2代表贬义，3代表兼有褒贬两性。
		//注：褒贬标注时，通过词本身和情感共同确定，所以有些情感在一些词中可能极性1，而其他的词中有可能极性为0。
		/**
		 * 乐
		 * 快乐
		 */
		mapNameTwo.put("pa".toUpperCase(),"快乐");
		/**
		 * 乐
		 * 安心
		 */
		mapNameTwo.put("pe".toUpperCase(),"安心");
		/**
		 * 好
		 * 尊敬
		 */
		mapNameTwo.put("pd".toUpperCase(),"尊敬");
		/**
		 * 好
		 * 表扬
		 */
		mapNameTwo.put("ph".toUpperCase(),"表扬");
		/**
		 * 好
		 * 相信
		 */
		mapNameTwo.put("pg".toUpperCase(),"相信");
		/**
		 * 好
		 * 喜爱
		 */
		mapNameTwo.put("pb".toUpperCase(),"喜爱");
		/**
		 * 好 
		 * 祝愿
		 */
		mapNameTwo.put("pk".toUpperCase(),"祝愿");
		/**
		 * 怒
		 * 愤怒
		 */
		mapNameTwo.put("na".toUpperCase(),"愤怒");
		/**
		 * 哀
		 * 悲伤
		 */
		mapNameTwo.put("nb".toUpperCase(),"悲伤");
		/**
		 * 哀
		 * 失望
		 */
		mapNameTwo.put("nj".toUpperCase(),"失望");
		/**
		 * 哀
		 * 疚
		 */
		mapNameTwo.put("nh".toUpperCase(),"疚");
		/**
		 * 哀
		 * 思
		 */
		mapNameTwo.put("pf".toUpperCase(),"思");
		/**
		 * 惧
		 * 慌
		 */
		mapNameTwo.put("ni".toUpperCase(),"慌");
		/**
		 * 惧
		 * 恐惧
		 */
		mapNameTwo.put("nc".toUpperCase(),"恐惧");
		/**
		 * 惧
		 * 羞
		 */
		mapNameTwo.put("ng".toUpperCase(),"羞");
		/**
		 * 恶
		 * 烦闷
		 */
		mapNameTwo.put("ne".toUpperCase(),"烦闷");
		/**
		 * 恶
		 * 憎恶
		 */
		mapNameTwo.put("nd".toUpperCase(),"憎恶");
		/**
		 * 恶
		 * 贬责
		 */
		mapNameTwo.put("nn".toUpperCase(),"贬责");
		/**
		 * 恶
		 * 嫉妒
		 */
		mapNameTwo.put("nk".toUpperCase(),"嫉妒");
		/**
		 * 恶
		 * 怀疑
		 */
		mapNameTwo.put("nl".toUpperCase(),"怀疑");
		/**
		 * 惊
		 * 惊奇
		 */
		mapNameTwo.put("pc".toUpperCase(),"惊奇");
		
	}
	public static void main(String[] args) {
		System.out.println(map.get("ADJ"));
	}
}
