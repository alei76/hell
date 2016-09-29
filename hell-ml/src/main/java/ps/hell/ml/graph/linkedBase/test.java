package ps.hell.ml.graph.linkedBase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import ps.hell.ml.nlp.own.landerbuluse.participle.mmseg2.mmseg;
import ps.hell.ml.nlp.own.landerbuluse.struct.WordFather;
import ps.hell.util.db.MongoDb;
import ps.hell.util.db.MysqlConnection;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

public class test {
	public static void main(String[] args) throws SQLException {
		mmseg mm=new mmseg(null,null);
		MysqlConnection mysql = new MysqlConnection("192.168.1.4", 3306,
				"fcMysql", "root", "zjroot");
		int shopIdMax = -1;
		int userIdMax=-1;
		try {
			ResultSet re = mysql
					.sqlSelect("select shopId,userId from BussinessInfoComment order by id DESC limit 1").resultSet;
			while (re.next()) {
				shopIdMax = re.getInt(1);
				userIdMax=re.getInt(2);
				break;
			}
		} catch (Exception e) {

		}
		boolean skipShopId = false;
		if (shopIdMax == -1) {
			skipShopId = true;
		}
		MysqlConnection inputMysql = new MysqlConnection("192.168.1.4", 3306,
				"fcMysql", "root", "zjroot");
		String str = "select a.dp_id from fangcheng_global.fc_brand_shop as a where a.dp_id is not null";
		MongoDb mongo = new MongoDb("192.168.1.11", 27017, "comments");
		DBCollection collection = mongo.getCollection("dp_comments");
		// 读取数据源
		ResultSet cursor = mysql.sqlSelect(str).resultSet;
		System.out.println("max:" + shopIdMax);
		while (cursor.next()) {
			// 515381
			Integer shopId = cursor.getInt(1);
			if (!skipShopId) {
				if (shopId == shopIdMax) {
				} else {
					continue;
				}
			}
			BasicDBObject ref = new BasicDBObject();
			// shopId=14743629;
			ref.put("shop_id", Integer.toString(shopId));
			DBCursor dbCursor = collection.find(ref);
			//ArrayList<LinkedList<LinkedList<WordFather>>> wordSeqAll = new ArrayList<LinkedList<LinkedList<WordFather>>>();
			int userCount = 0;
			while (dbCursor.hasNext()) {
				userCount++;
				if (userCount % 100 == 0) {
					System.out.println(shopId + ":" + userCount);
				}
//				if (userCount >= 500) {
//					break;
//				}
				BasicDBObject obj = (BasicDBObject) dbCursor.next();
				// System.out.println(obj.toString());
				String comment = obj.getString("comment_txt");
				if (comment == null) {
					continue;
				}
				String userId=obj.getString("user_id");
				if(userId==null||userId.length()<=0)
				{
					continue;
				}else if(!skipShopId&& shopId==shopIdMax && Integer.parseInt(userId)==userIdMax){
					skipShopId=true;
					continue;
				}else{
					
				}
				String dateTime=obj.getString("time");
				if(dateTime==null)
				{
					dateTime="null";
				}
				String star=obj.getString("star");
				if(star==null)
				{
					star=null;
				}
				String userName=obj.getString("user_name");
				if(userName==null)
				{
					userName=null;
				}
				String price=obj.getString("comm_per");
				if(price==null)
				{
					price=null;
				}
				LinkedList<LinkedList<WordFather>> resl=mm.analyze(comment);
				mm.clearnStop(resl);
				StringBuffer sb=new StringBuffer();
				int i=0;
				for(LinkedList<WordFather> list:resl)
				{
					for(WordFather word:list)
					{
						i++;
						if(i==1)
						{
							sb.append(word.word);
						}else{
							sb.append(",").append(word.word);
						}
						if(i>20)
						{
							break;
						}
					}
				}
				String te="insert into BussinessInfoComment(shopId,userId,dateTime,tags,star,userName,price,updateDate) values("
						+ shopId +","+userId+",\""+dateTime +"\",\"" + sb.toString() + "\","+star+",\""+userName+"\",\""+price+"\",now())";
				//System.out.println(te);
				inputMysql.sqlUpdate(te);
			}
			// 入库
	
		}
	}
}
