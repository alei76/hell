package ps.hell.ml.nlp.own.landerbuluse.struct;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.StringTokenizer;

/**
 * 各语料库  加载器
 * @author Administrator
 *
 */
public class WordsGet {
	/**
	 * 词频的最长大小
	 */
	public final int maxLengthWord=5;
	/**
	 * 中文词库
	 */
	public final String chinesWordFile="./config/dataset/Text/chineseKeyWords.txt";
	/**
	 * 中文停用词文件
	 */
	public final String chineseStopWordFile="./config/dataset/Text/chineseStopWords.txt";
	/**
	 * 英文停用词文件
	 */
	public final String englishStopWordFile="./config/dataset/Text/englishStopWords.txt";
	/**
	 * 负面中文情感词
	 */
	public final String chineseNegativeEmotionWordsFile="./config/dataset/Text/chineseNegativeEmotionWords.txt";
	
	/**
	 * 正面中文情感词
	 */
	public final String chinesePositiveEmotionWordsFile="./config/dataset/Text/chinesePositiveEmotionWords.txt";
	
	
	/**
	 * 负面中文情感词 台湾
	 */
	public final String chineseNegativeEmotionWordsFileTW="./config/dataset/Text/chineseNegativeEmotionWords_ntusd.txt";
	
	/**
	 * 正面中文情感词 台湾
	 */
	public final String chinesePositiveEmotionWordsFileTW="./config/dataset/Text/chinesePositiveEmotionWords_ntusd.txt";
	
	/**
	 * 获取所有标点符号
	 */
	public final String paramsFile="./config/dataset/Text/params.txt";
	//N	名词
	//V	动词
	//ADJ	形容词
	//ADV	副词
	//CLAS	量词
	//ECHO	拟声词
	//STRU	结构助词
	//AUX	助词
	//COOR	并列连词
	//CONJ	连词
	//SUFFIX	前缀
	//PREFIX	后缀
	//PREP	介词
	//PRON	代词
	//QUES	疑问词
	//NUM	数词
	//IDIOM	成语
	
	
	/**
	 * 英文基本预料库
	*来源：华尔街日报语料库	
	*制作：北京语言大学-汉语国际教育技术研发中心 http://nlp.blcu.edu.cn
	*词汇 频数      频率				
	*/
	public final String englishKeywordsFile="./config/dataset/Text/englishKeywords.txt";
	
	/**
	 * 中国名人 名 words
	 */
	public final String chineseFamousPersonFile="./config/dataset/Text/古今中外各界名人词库.txt";
	
	/**
	 * 中国地名 但是没有父自级关系 words
	 */
	public final String chinesAddressFile="./config/dataset/Text/chinaAddressWords.txt";
	
	public final String emotionFile="./config/dataset/Text/emotionWords.txt";

	/**
	 * 词
	 */
	public WordRedBlackTree<WordFather> words=new WordRedBlackTree<WordFather>();
	/**
	 * 其他词
	 */
	public HashSet<Word> otherWords=new HashSet<Word>();
	/**
	 * 情感词库
	 */
	public WordRedBlackTree<WordFather> emotionWords=new WordRedBlackTree<WordFather>();
	/**
	 * 使用trie操作的
	 */
	public TrieTree<WordFather> wordsCommon=new TrieTree<WordFather>();
	/**
	 * 使用trie操作的树 
	 */
	public TrieTree<WordFather> emotionWrodsCommon=new TrieTree<WordFather>();
	/**
	 * 是否使用trie结构
	 */
	public boolean isTrie=true;
	/**
	 * 基础
	 * @return
	 */
	public String[] split(String str){
		StringTokenizer strTo=new StringTokenizer(str);
		String[] strList=new String[strTo.countTokens()];
		int i=-1;
		while(strTo.hasMoreTokens()){
			i++;
			strList[i]=strTo.nextToken();
		}
		return strList;
	}
	
	/**
	 * 获取中文词库
	 */
	public void addCineseWord()
	{
		System.out.println("加载中文词频");
		readFile(chinesWordFile);
		System.out.println("加载中文词频完成");
	}
	
	/**
	 * 获取中文停用词 words
	 */
	public void addChineseStopWord()
	{
		System.out.println("加载中文停用词");
		readStopFile(chineseStopWordFile);
		System.out.println("加载中文停用词完成");
	}
	/**
	 * 获取英文停用词 words
	 */
	public void addEnglishStopWord()
	{
		System.out.println("加载英文停用词");
		readFile(englishStopWordFile);
		System.out.println("加载英文停用词完成");
	}
	/**
	 * 获取负面中文情感词 来自百度 words
	 */
	public void addChineseNegativeEmotionWordsWord()
	{
		System.out.println("加载 中文消极情感词");
		readFile(chineseNegativeEmotionWordsFile);
		System.out.println("加载 中文消极情感词完成");
	}
	
	/**
	 * 获取正面中文情感词 来自百度 words
	 */
	public void addChinesePositiveEmotionWordsWord()
	{
		System.out.println("加载 中文积极情感词");
		readFile(chinesePositiveEmotionWordsFile);
		System.out.println("加载 中文积极情感词完成");
	}
	
	/**
	 * 获取负面中文情感词 来台湾大学 words
	 */
	public void addChineseNegativeEmotionWordsWordTW()
	{
		System.out.println("加载tw 中文消极情感词");
		readFile(chineseNegativeEmotionWordsFile);
		System.out.println("加载tw 中文消极情感词完成");
	}
	
	/**
	 * 获取正面中文情感词 来自台湾大学 words
	 */
	public void addChinesePositiveEmotionWordsWordTW()
	{
		System.out.println("加载tw 中文积极情感词");
		readFile(chinesePositiveEmotionWordsFile);
		System.out.println("加载tw 中文积极情感词完成");
	}
	
	/**
	 * 获取英文词频库 keywords
	 */
	public void addEnglishKeywords()
	{
		System.out.println("加载英文词频");
		readFile(englishKeywordsFile,"\t");
		System.out.println("加载英文词频完成");
	}
	
	/**
	 * 名人词频 words
	 */
	public void addChineseFamousPersonFile()
	{
		System.out.println("加载名人词频");
		readFile(chineseFamousPersonFile);
		System.out.println("加载名人词频完成");
	}
	
	/**
	 * 中国城市 words
	 */
	public void addChinaAddressFile()
	{
		System.out.println("加载城市地址");
		readFile(chinesAddressFile);
		System.out.println("加载城市地址完成");
	}
	
	/**
	 * 添加所有标点符号
	 */
	public void addParamsFile()
	{
		System.out.println("加载标点符号");
		readFile(paramsFile,false);
		System.out.println("加载标点符号完成");
	}
	
	public void addEmotionFile()
	{
		System.out.println("加载情感词");
		readEmotionFile(emotionFile);
		System.out.println("加载情感词完成");
	}
	/**
	 * 动词
	 */
	public String verbFile="./config/dataset/Text/verb.txt";;
	/**
	 * 添加动词
	 */
	public void addVert(){
		System.out.println("加载动词");
		readFile(verbFile,false);
		System.out.println("加载动词完成");
	}
	/**
	 * 形容词
	 */
	public String ajjectivesFile="./config/dataset/Text/adjectives.txt";;
	/**
	 * 添加形容词
	 */
	public void addAdjectives(){
		System.out.println("加载形容词");
		readFile(ajjectivesFile,false);
		System.out.println("加载形容词完成");
	}
	/**
	 * 介词
	 */
	public String prepositionsFile="./config/dataset/Text/prepositions.txt";;
	/**
	 * 添加介词
	 */
	public void addPrepositions(){
		System.out.println("加载介词");
		readFile(prepositionsFile,false);
		System.out.println("加载介词完成");
	}

	/**
	 * 量词
	 */
	public String quantifiersFile="./config/dataset/Text/quantifiers.txt";;
	/**
	 * 添加动词
	 */
	public void addQuantifiersFile(){
		System.out.println("加载量词");
		readFile(quantifiersFile,false);
		System.out.println("加载量词完成");
	}
	
	/**
	 * 量纲词
	 */
	public String quantifiersLabelFile="./config/dataset/Text/quantifiersLabel.txt";;
	/**
	 * 添加动词
	 */
	public void addQuantifiersLabel(){
		System.out.println("加载量纲词");
		readFile(quantifiersLabelFile,false);
		System.out.println("加载量纲词完成");
	}
	
	/**
	 * 地名后缀
	 */
	public String placeFile="./config/dataset/Text/place.txt";;
	/**
	 * 添加动词
	 */
	public void addPlace(){
		System.out.println("加载地名后缀");
		readFile(placeFile,false);
		System.out.println("加载地名后缀完成");
	}
	
	/**
	 * 文件读取类 存入words
	 * @param filePath
	 */
	public void readFile(String filePath)
	{
		File file=new File(filePath);
		if(!file.exists())
		{
			System.out.println("文件不存在");
			return;
		}
		LinkedList<Double[]> retu=new LinkedList<Double[]>();
		BufferedReader reader = null;
		try{
		 
		 InputStreamReader read = new InputStreamReader(new FileInputStream(file),"utf-8");  
         reader = new BufferedReader(read);
         String tempString = null;
         int line = 0;
         // 一次读入一行，直到读入null为文件结束
         while ((tempString = reader.readLine()) != null)
         {
        	 line++;
        	 if(line%10000==0)
        	 {
        		 System.out.println("加载行数:"+line);
        	 }
        	 if(tempString.startsWith("//"))
        	 {
        		 continue;
        	 }
        	 if(tempString.length()>0)
        	 {
        		 String[] str=tempString.split("\t");
        		 if(str.length==1)
        		 {
        			 if(isTrie)
        			 {
        				 wordsCommon.add(tempString, new WordFather(1,null));
        			 }else{
        			 words.add(new WordFather(tempString,1,null));
        			 }
        		 }else if(str.length==2)
        		 {
        			 if(isTrie)
        			 {
        				 wordsCommon.add(str[0], new WordFather(Long.parseLong(str[1]),null));
        			 }else{
        				 words.add(new WordFather(str[0],Long.parseLong(str[1]),null));
        				 
        			 }
        		 }else if(str.length==3)
        		 {
        			 if(isTrie)
        			 {
        				 wordsCommon.add(str[0], new WordFather(Long.parseLong(str[1]),str[2].split(",")));
        			 }else{
        				 words.add(new WordFather(str[0],Long.parseLong(str[1]),str[2].split(",")));
        				 
        			 }
        		 }
        	 }
         }
         reader.close();
         read.close();
		}
         catch(Exception e)
		{
        	 e.printStackTrace();
        	 System.out.println("读取异常");
		}
	}
	/**
	 * 文件读取类 存入words
	 * @param filePath
	 */
	public void readStopFile(String filePath)
	{
		File file=new File(filePath);
		if(!file.exists())
		{
			System.out.println("文件不存在");
			return;
		}
		LinkedList<Double[]> retu=new LinkedList<Double[]>();
		BufferedReader reader = null;
		try{
		 
		 InputStreamReader read = new InputStreamReader(new FileInputStream(file),"utf-8");  
         reader = new BufferedReader(read);
         String tempString = null;
         int line = 0;
         // 一次读入一行，直到读入null为文件结束
         while ((tempString = reader.readLine()) != null)
         {
        	 line++;
        	 if(line%10000==0)
        	 {
        		 System.out.println("加载行数:"+line);
        	 }
        	 if(tempString.startsWith("//"))
        	 {
        		 continue;
        	 }
        	 if(tempString.length()>0)
        	 {
        		 String[] str=tempString.split("\t");
        		 if(str.length==1)
        		 {
        			 this.otherWords.add(new WordFather(tempString,1,null));
        		 }else if(str.length==2)
        		 {
        			 this.otherWords.add(new WordFather(str[0],Long.parseLong(str[1]),null));
        		 }else if(str.length==3)
        		 {
        			 this.otherWords.add(new WordFather(str[0],Long.parseLong(str[1]),str[2].split(",")));
        		 }
        	 }
         }
         reader.close();
         read.close();
		}
         catch(Exception e)
		{
        	 e.printStackTrace();
        	 System.out.println("读取异常");
		}
	}
	
	/**
	 * 读取文件存入 keywords中
	 * @param filePath
	 */
	public void readFile(String filePath,String split)
	{
		File file=new File(filePath);
		if(!file.exists())
		{
			System.out.println("文件不存在");
			return;
		}
		LinkedList<Double[]> retu=new LinkedList<Double[]>();
		BufferedReader reader = null;
		try{
		 
		 InputStreamReader read = new InputStreamReader(new FileInputStream(file),"utf-8");  
         reader = new BufferedReader(read);
         String tempString = null;
         int line = 1;
         // 一次读入一行，直到读入null为文件结束
         while ((tempString = reader.readLine()) != null)
         {
        	 line++;
        	 if(line%10000==0)
        	 {
        		 System.out.println("加载行数:"+line);
        	 }
        	 if(tempString.startsWith("//"))
        	 {
        		 continue;
        	 }
        	 if(tempString.length()>0)
        	 {
        		 String[] str=tempString.split(split);
        		 if(str.length==1)
        		 {
        			 if(isTrie)
        			 {
        				 wordsCommon.add(tempString, new WordFather(1,null));
        			 }else{
        			 words.add(new WordFather(tempString,1,null));
        			 }
        		 }else if(str.length==2)
        		 {
        			 if(isTrie)
        			 {
        				 wordsCommon.add(str[0], new WordFather(Long.parseLong(str[1]),null));
        			 }else{
        				 words.add(new WordFather(str[0],Long.parseLong(str[1]),null));
        				 
        			 }
        		 }else if(str.length==3)
        		 {
        			 if(isTrie)
        			 {
        				 wordsCommon.add(str[0], new WordFather(Long.parseLong(str[1]),str[2].split(",")));
        			 }else{
        				 words.add(new WordFather(str[0],Long.parseLong(str[1]),str[2].split(",")));
        				 
        			 }
        		 }
        	 }
         }
         reader.close();
         read.close();
		}
         catch(Exception e)
		{
        	 System.out.println("读取异常");
		}
	}
	/**
	 * 存储其他词
	 * @param filePath
	 */
	public void readFile(String filePath,boolean flag)
	{
		File file=new File(filePath);
		if(!file.exists())
		{
			System.out.println("文件不存在");
			return;
		}
		//LinkedList<Double[]> retu=new LinkedList<Double[]>();
		BufferedReader reader = null;
		try{
		 
		 InputStreamReader read = new InputStreamReader(new FileInputStream(file),"utf-8");  
         reader = new BufferedReader(read);
         String tempString = null;
         //int line = 1;
         // 一次读入一行，直到读入null为文件结束
         while ((tempString = reader.readLine()) != null)
         {
        	 if(tempString.startsWith("//"))
        	 {
        		 continue;
        	 }
        	 if(tempString.length()>0)
        	 {
        		 String[] str=tempString.split("\t");
        		 if(str.length==1)
        		 {
        			 otherWords.add(new Word(tempString,1,null));
        		 }else if(str.length==2)
        		 {
        			 otherWords.add(new Word(str[0],Long.parseLong(str[1]),null));
        		 }else if(str.length==3)
        		 {
        			 otherWords.add(new Word(str[0],Long.parseLong(str[1]),str[2].split(",")));
        		 }
        	 }
         }
         reader.close();
         read.close();
		}
         catch(Exception e)
		{
        	 System.out.println("读取异常");
		}
	}
	/**
	 * 读取文件存入 中
	 * @param filePath
	 * 为存储其他词的
	 */
	public void readFile(String filePath,String split,boolean flag)
	{
		File file=new File(filePath);
		if(!file.exists())
		{
			System.out.println("文件不存在");
			return;
		}
		LinkedList<Double[]> retu=new LinkedList<Double[]>();
		BufferedReader reader = null;
		try{
		 
		 InputStreamReader read = new InputStreamReader(new FileInputStream(file),"utf-8");  
         reader = new BufferedReader(read);
         String tempString = null;
         //int line = 1;
         // 一次读入一行，直到读入null为文件结束
         while ((tempString = reader.readLine()) != null)
         {
        	 if(tempString.startsWith("//"))
        	 {
        		 continue;
        	 }
        	 if(tempString.length()>0)
        	 {
        		 String[] str=tempString.split(split);
        		 if(str.length==1)
        		 {
        			 otherWords.add(new Word(tempString,1,null));
        		 }else if(str.length==2)
        		 {
        			 otherWords.add(new Word(str[0],Long.parseLong(str[1]),null));
        		 }else if(str.length==3)
        		 {
        			 otherWords.add(new Word(str[0],Long.parseLong(str[1]),str[2].split(",")));
        		 }
        	 }
         }
         reader.close();
         read.close();
		}
         catch(Exception e)
		{
        	 System.out.println("读取异常");
		}
	}
	/**
	 * 文件读取类 存入words
	 * @param filePath
	 */
	public void readEmotionFile(String filePath)
	{
		File file=new File(filePath);
		if(!file.exists())
		{
			System.out.println("文件不存在");
			return;
		}
		LinkedList<Double[]> retu=new LinkedList<Double[]>();
		BufferedReader reader = null;
		try{
		 
		 InputStreamReader read = new InputStreamReader(new FileInputStream(file),"utf-8");  
         reader = new BufferedReader(read);
         String tempString = null;
         int line = 0;
         // 一次读入一行，直到读入null为文件结束
         while ((tempString = reader.readLine()) != null)
         {
        	 line++;
        	 if(line%10000==0)
        	 {
        		 System.out.println("加载行数:"+line);
        	 }
        	 if(tempString.startsWith("//"))
        	 {
        		 continue;
        	 }
        	 if(tempString.length()>0)
        	 {
        		 String[] str=tempString.split("\t");
        		 WordFather emotion=new WordFather();
        		 for(int i=0;i<str.length;i++)
        		 {
	        		 if(i==0)
	        		 {
	        			 emotion.setWord(str[0]);
	        		 }else if(i==1)
	        		 {
	        			 emotion.setWordclass(EmotionStatic.map.get(str[1].trim().toUpperCase()));
	        		 }else if(i==2)
	        		 {
	        			 if(str[2].length()>0)
	        			 {
	        				 emotion.setWordCount(Integer.parseInt(str[2]));
	        			 }
	        		 }else if(i==3)
	        		 {
	        			 if(str[3].length()>0)
	        			 {
	        				 emotion.setWordIndex(Integer.parseInt(str[3]));
	        			 }
	        		 }else if(i==4)
	        		 {
	        			 emotion.setEmotionCategory(EmotionStatic.map.get(str[4].trim().toUpperCase()));
	        		 }else if(i==5)
	        		 {
	        			 if(str[5].length()>0)
	        			 emotion.setPower(Integer.parseInt(str[5]));
	        		 }else if(i==6)
	        		 {
	        			 if(str[5].length()>0)
		        			 emotion.setPolr(Integer.parseInt(str[5]));
	        		 }else if(i==7)
	        		 {
	        			 emotion.setEmotionCategory2(EmotionStatic.map.get(str[7].toUpperCase()));
	        		 }else if(i==8)
	        		 {
	        			 if(str[8].length()>0)
	        			 emotion.setPower2(Integer.parseInt(str[8]));
	        		 }else if(i==9)
	        		 {
	        			 if(str[9].length()>0)
		        			 emotion.setPolr2(Integer.parseInt(str[9]));
	        		 }
        		 }
        		// System.out.println("添加:"+emotion.word);
        		 if(isTrie)
        		 {
        			 String strTemp=emotion.word;
        			 emotion.word=null;
        			emotionWrodsCommon.add(strTemp,emotion); 
        		 }else{
        			 emotionWords.add(emotion);
        		 }
        	 }
         }
         reader.close();
         read.close();
		}
         catch(Exception e)
		{
        	 e.printStackTrace();
        	 System.out.println("读取异常");
        	 System.exit(1);
		}
	}
	
	
	public static void main(String[] args) {
		WordsGet word=new WordsGet();
		word.addChineseStopWord();
//		for(Word str:word.words)
//		{
//			System.out.println(str);
//		}
	}
	
	
}
