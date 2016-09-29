package ps.hell.ml.nlp.own.landerbuluse.participle.ikAnalyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class IkAnalyzer {
	
	private IkAnalyzer(){
		
	}
	/**
	 * 对查询词进行分词
	 * @param keywords
	 * @return
	 */
	public static String[] keywordToArray(String keywords) {
			Analyzer analyzer = new IKAnalyzer();
			List<String> list=null;
			if(keywords!=null)
			{
				StringReader reader = new StringReader(keywords);
				TokenStream tokenStream = analyzer.tokenStream(keywords, reader);
				TermAttribute term = tokenStream.getAttribute(TermAttribute.class);
				list=new ArrayList<String>();
				try {
					while (tokenStream.incrementToken()) {
						String str=term.term();
						if(str!=null)
							list.add(str);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				return list.toArray(new String[list.size()]);
			}
			return null;
		}
	public static void main(String[] args){
		String text = "Lucene是apache软件基金会4 jakarta项目组的一个子项目，是一个开放源代码的全文检索引擎工具包. ";
	    String[] queryTerms = null;
	    queryTerms = IkAnalyzer.keywordToArray(text); 
	    for(int i = 0; i < queryTerms.length; i++){
	    	System.out.println(queryTerms[i]);
	    }
		
	}
}
