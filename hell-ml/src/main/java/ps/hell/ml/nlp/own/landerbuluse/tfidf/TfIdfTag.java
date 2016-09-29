package ps.hell.ml.nlp.own.landerbuluse.tfidf;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import ps.hell.ml.nlp.own.landerbuluse.participle.mmseg2.mmseg;
import ps.hell.ml.nlp.own.landerbuluse.struct.WordFather;

/**
 * 生成某个数据的标签数据
 * @author Administrator
 *
 */
public class TfIdfTag {

	/**
	 * 使用的tfidf值
	 */
	public HashMap<String,TfIdfBean> tfidfMap=new HashMap<String,TfIdfBean>();
	
	public HashSet<WordFather> set=new HashSet<WordFather>();
	/**
	 * 使用的分词方法
	 */
	public mmseg mm=null;
	
	public TfIdfTag(mmseg mm)
	{
		this.mm=mm;
	}
	
	/**
	 * 输入的为 文本分词结果
	 * @param input
	 */
	public HashMap<String,TfIdfBean> run(LinkedList<LinkedList<WordFather>> input)
	{
		HashMap<String,TfIdfBean> map=new HashMap<String,TfIdfBean>();
		for(LinkedList<WordFather> words:input)
		{
			for(WordFather word:words)
			{
				TfIdfBean idf=tfidfMap.get(word.word);
				if(idf==null)
				{
					continue;
				}
				WordFather wordFather=new WordFather();
				wordFather.word=word.word;
				set.add(wordFather);
				map.put(word.word,new TfIdfBean(word,idf.value));
			}
		}
		return map;
	}
	/**
	 * 读取已经生成的tfidf文件
	 */
	public void readFile()
	{
		TfIdfUtil idf=new TfIdfUtil(mm);
		ArrayList<TfIdfBean> input=idf.readTFIDFSortWordFromFile();
		for(TfIdfBean bean:input)
		{
			tfidfMap.put(bean.word.word,bean);
		}
	}
	
	/**
	 * 读取已经生成的tfidf文件
	 */
	public void readMemery(ArrayList<TfIdfBean> input)
	{
		for(TfIdfBean bean:input)
		{
			tfidfMap.put(bean.word.word,bean);
		}
	}
	
	public static void main(String[] args) throws SQLException {
//		mmseg mm=new mmseg(null,null);
//		//TfIdfTag tag=new TfIdfTag(mm);
//		MysqlConnection mysql=new MysqlConnection("192.168.1.4",3306,"fcMysql","root","zjroot");
//		int shopIdMax=-1;
//		try{
//			ResultSet re=mysql.sqlSelect("select shopId from BussinessInfoTag order by id DESC limit 1").resultSet;
//			while(re.next())
//			{
//				shopIdMax=re.getInt(1);
//				break;
//			}
//		}catch(Exception e)
//		{
//
//		}
//		boolean skipShopId=false;
//		if(shopIdMax==-1)
//		{
//			skipShopId=true;
//		}
//		MysqlConnection inputMysql=new MysqlConnection("192.168.1.4",3306,"fcMysql","root","zjroot");
//		String str="select a.dp_id from fangcheng_global.fc_brand_shop as a where a.dp_id is not null";
//		MongoDb mongo=new MongoDb("192.168.1.11",27017,"comments");
//		DBCollection collection=mongo.getCollection("dp_comments");
//		//获取最大的shopId
//
//		//读取数据源
//		ResultSet cursor=mysql.sqlSelect(str).resultSet;
//		System.out.println("max:"+shopIdMax);
//		while(cursor.next())
//		{
//			//515381
//			Integer shopId=cursor.getInt(1);
//			if(!skipShopId)
//			{
//				if(shopId==shopIdMax)
//				{
//					skipShopId=true;
//					continue;
//				}else{
//					continue;
//				}
//			}
//			BasicDBObject ref=new BasicDBObject();
//			//shopId=14743629;
//			ref.put("shop_id",Integer.toString(shopId));
//			DBCursor dbCursor=collection.find(ref);
//			ArrayList<LinkedList<LinkedList<WordFather>>> wordSeqAll=new  ArrayList<LinkedList<LinkedList<WordFather>>>();
//			int userCount=0;
//
//			while(dbCursor.hasNext())
//			{
//				userCount++;
//				if(userCount%100==0)
//				{
//					System.out.println(shopId+":"+userCount);
//				}
//				if(userCount>=500)
//				{
//					break;
//				}
//				BasicDBObject obj=(BasicDBObject)dbCursor.next();
//				//System.out.println(obj.toString());
//				String comment=obj.getString("comment_txt");
//				if(comment==null)
//				{
//					continue;
//				}
//				LinkedList<LinkedList<WordFather>> rege=mm.analyze(comment);
//				mm.clearnStop(rege);
//				//一个文件的分词
//				wordSeqAll.add(rege);
//			}
//			TfIdfUtil tfidf = new TfIdfUtil(mm);
//			tfidf.addList(wordSeqAll);
//			ArrayList<HashMap<WordFather, Integer>> wordSeq = new ArrayList<HashMap<WordFather, Integer>>();
//			//获取bean
//			HashSet<WordFather> set=new HashSet<WordFather>();
//			for(LinkedList<LinkedList<WordFather>> li:wordSeqAll)
//			{
//				for(LinkedList<WordFather> li2:li)
//				{
//					for(WordFather li3:li2)
//					{
//						set.add(li3);
//					}
//				}
//			}
//			//tag.set如果使用固定的
//			ArrayList<TfIdfBean> bean=tfidf.readTFIDFSortWord(set);
//			//获取值大于0.8的//topN
//			StringBuffer sb=new StringBuffer();
//			int i=0;
//			for(TfIdfBean be:bean)
//			{
//				//并且长度必须>1
//				if(be.value>0.01 && be.word.word.length()>1)
//				{
//					i++;
//					if(i==1)
//					{
//						sb.append(be.word.word);
//					}else{
//						sb.append(",").append(be.word.word);
//					}
//					if(i>20)
//					{
//						break;
//					}
//				}
//			}
//			System.out.println(shopId+"\t"+sb.toString());
//			//入库
//			inputMysql.sqlUpdate("insert into BussinessInfoTag(shopId,tags,updateDate) values("+shopId+",\""+sb.toString()+"\",now())");
////			if(true)
////			{
////				break;
////			}
//		}
	}
}
