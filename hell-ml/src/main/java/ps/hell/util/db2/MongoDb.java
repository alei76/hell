package ps.hell.util.db2;

import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoOptions;
import com.mongodb.QueryOperators;
import com.mongodb.util.JSON;

/**
 * mongodb 类
 * 
 * @author Administrator
 *
 */
public class MongoDb implements CursorInter {
	private static Logger log = LoggerFactory.getLogger(MongoDb.class);
	public int POOLSIZE = 100;
	public int BLOCKSIZE = 100;
	/**
	 * mongdo实例化
	 */
	Mongo m = null;
	/**
	 * 当前连接数据库
	 */
	HashMap<Long, DB> db = new HashMap<Long, DB>();

	HashMap<Long, DBCursor> cursor = new HashMap<Long, DBCursor>();

	public String ip = null;
	public int port = 27017;
	public String user=null;
	public char[] pwd=null;

	public long useIndex = 0L;
	public HashMap<Long, String[]> collection = new HashMap<Long, String[]>();
	public HashMap<Long, Integer> useCollectionIndex = new HashMap<Long, Integer>();
	public String ref = "{}";
	public String obj = "{}";

	/**
	 * 
	 * @param ip
	 *            IP地址
	 * @param port
	 *            端口
	 * @param database
	 *            数据库
	 */
	public MongoDb(String ip, int port) {
		// 创建连接
		try {
			m = new Mongo(ip, port);
			this.ip = ip;
			this.port = port;
			MongoOptions opt = m.getMongoOptions();
			opt.connectionsPerHost = POOLSIZE;
			opt.threadsAllowedToBlockForConnectionMultiplier = BLOCKSIZE;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			// TODO Auto-generated catch block

			System.out.println("数据库连接异常");
			System.exit(1);
		}
	}

	/**
	 * 
	 * @param ip
	 *            IP地址
	 * @param port
	 *            端口
	 * @param database
	 *            数据库
	 */
	public MongoDb(String ip, int port,String database, String user, String pwd) {
		// 创建连接
		try {
			m = new Mongo(ip, port);
			this.ip = ip;
			this.port = port;
			MongoOptions opt = m.getMongoOptions();
			opt.connectionsPerHost = POOLSIZE;
			opt.threadsAllowedToBlockForConnectionMultiplier = BLOCKSIZE;
			// 得到数据库
			DB db = m.getDB(database);
			this.db.put(++useIndex, db);
			if(user==null||user.equals("")){}else{
				this.user=user;
				this.pwd=pwd.toCharArray();
			db.authenticate(user, pwd.toCharArray());
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
			// TODO Auto-generated catch block

			System.out.println("数据库连接异常");
			System.exit(1);
		}
	}
	
	public void auth(DB db){
		if(user==null){
			
		}else{
			db.authenticate(user, pwd);
		}
	}
	
	

	public DBCollection getCollection(String database, String collection) {
		DB db = m.getDB(database);
		auth(db);
		return db.getCollection(collection);
	}

	public boolean isExistCollection(String database, String collection) {
		DB db = m.getDB(database);
		return db.collectionExists(collection);
	}

	public MongoDb(String ipPort) {
		// 创建连接
		try {
			m = new Mongo(ipPort.substring(0, ipPort.indexOf(":")),
					Integer.parseInt(ipPort.substring(ipPort.indexOf(":") + 1)));
			// // 得到数据库
			// db = m.getDB(database);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			// TODO Auto-generated catch block

			System.out.println("数据库连接异常");
			System.exit(1);
		}
	}

	public void close() {
		this.m.close();
	}

	/**
	 * 获取所有数据库
	 * 
	 * @return
	 */
	public List<String> getDataBase() {
		return m.getDatabaseNames();
	}

	/**
	 * 当前数据库中，存在的所有表(集合)
	 * 
	 * @return
	 */
	public Set<String> getCollectionNames(String database) {
		DB db = m.getDB(database);
		auth(db);
		return db.getCollectionNames();
	}

	/**
	 * 删除一个数据库
	 * 
	 * @param database
	 */
	public void delDataBase(String database) {
		// 禁用
		// m.dropDatabase(database);
	}

	/**
	 * 从db库中获取 表collection的索引信息
	 * 
	 * @param collection
	 * @return
	 */
	public List<DBObject> getCollectionIndex(String database, String collection) {
		DB db = m.getDB(database);
		auth(db);
		DBCollection coll = db.getCollection(collection);
		// 查看一个表的索引
		return coll.getIndexInfo();
	}

	/**
	 * 从db库中获取 表collection表的一条信息
	 * 
	 * @param collection
	 * @return
	 */
	public DBObject getCollectionOne(String database, String collection) {
		DB db = m.getDB(database);
		auth(db);
		DBCollection coll = db.getCollection(collection);
		return coll.findOne();
	}

	/**
	 * 插入json 其中 不支持[]方法
	 * 
	 * @param collection
	 * @param jsonString
	 */
	public void insert(String database, String collection, String jsonString) {
		DB db = m.getDB(database);
		auth(db);
		DBCollection coll = db.getCollection(collection);

		// BasicDBObject doc=new BasicDBObject();
		if (jsonString.startsWith("[")) {
			log.info("输入mongodb数据格式错误:" + jsonString);
		} else {
			DBObject dbObject = (DBObject) JSON.parse(jsonString);
			coll.insert(dbObject);
		}
		// String转json
		// JSONObject json = JSONObject.fromObject(jsonString);
		// 将json解析成对应的插入信息
	}

	public void insert(String database, String collection, BasicDBObject doc) {
		// 添加
		DB db = m.getDB(database);
		auth(db);
		DBCollection coll = db.getCollection(collection);

		// BasicDBObject doc=new BasicDBObject();
		// doc.put("name", " sunshan");
		//
		// doc.put("sex", "男");
		//
		// doc.put("age", 22);
		//
		coll.insert(doc);
	}

	/**
	 * 通过 doc 去查询mongodb中的对应数据
	 * 
	 * @param collection
	 * @param doc
	 */
	public DBCursor find(String database, String collection, BasicDBObject doc) {
		DB db = m.getDB(database);
		auth(db);
		DBCollection coll = db.getCollection(collection);
		return coll.find(doc);
	}
	/**
	 * 通过 doc 去查询mongodb中的对应数据
	 * 
	 * @param collection
	 * @param doc
	 */
	public DBObject findOne(String database, String collection, BasicDBObject doc) {
		DB db = m.getDB(database);
		auth(db);
		DBCollection coll = db.getCollection(collection);
		return coll.findOne(doc);
	}
	
	public DBObject findOne(String database, String collection, String doc,String ref) {
		DB db = m.getDB(database);
		auth(db);
		DBCollection coll = db.getCollection(collection);
		DBObject refO = (DBObject) JSON.parse(doc);
		DBObject keysO = (DBObject) JSON.parse(ref);
		return coll.findOne(refO,keysO);
	}

	public DBCursor find(String database, String collection, BasicDBObject doc,
			BasicDBObject doc2) {
		DB db = m.getDB(database);
		auth(db);
		DBCollection coll = db.getCollection(collection);
		return coll.find(doc, doc2);
	}

	public DBCursor find(String database, String collection) {
		DB db = m.getDB(database);
		auth(db);
		DBCollection coll = db.getCollection(collection);
		return coll.find();
	}

	public DBCursor findCursor(String database, String collection, String ref,
			String keys) {
		DB db = m.getDB(database);
		auth(db);
		DBCollection coll = db.getCollection(collection);
		return coll.find((BasicDBObject) JSON.parse(ref),
				(BasicDBObject) JSON.parse(keys));
	}

	public CursorFatherInter findCursor(String database, String collection[],
			BasicDBObject doc, BasicDBObject doc2) {
		DB db = m.getDB(database);
		auth(db);
		long index = ++useIndex;
		if (collection.length > 0) {
			DBCollection coll = db.getCollection(collection[0]);
			this.cursor.put(index, coll.find(doc, doc2));
			this.collection.put(index, collection);
			this.useCollectionIndex.put(index, 0);
		}
		CursorFatherInter inter = new CursorFatherInter(this, index,"mongo");
		return inter;
	}

	public CursorFatherInter findCursor(String database, String collection[],
			String ref, String keys) {
		DB db = m.getDB(database);
		auth(db);
		long index = ++useIndex;
		if (collection.length > 0) {
			DBCollection coll = db.getCollection(collection[0]);
			this.cursor.put(
					index,
					coll.find((DBObject) JSON.parse(ref),
							(DBObject) JSON.parse(keys)));
			this.collection.put(index, collection);
			this.useCollectionIndex.put(index, 0);
		}
		CursorFatherInter inter = new CursorFatherInter(this, index,"mongo");
		return inter;
	}

	public CursorFatherInter findCursorOne(String database, String collection,
			String ref, String keys) {
		DB db = m.getDB(database);
		auth(db);
		DBCollection coll = db.getCollection(collection);
		long index = ++useIndex;
		this.collection.put(index, new String[1]);
		this.useCollectionIndex.put(index, 0);
		this.cursor.put(
				index,
				coll.find((DBObject) JSON.parse(ref),
						(DBObject) JSON.parse(keys)));

		CursorFatherInter inter = new CursorFatherInter(this,index,"mongo");
		return inter;
	}
	@Override
	public Object getCursorIndex(long index)
	{
		return this.cursor.get(index);
	}
	
	@Override
	public String nextString(DBCursor cur,ResultSet mysqlcur,long index) {
		if (cur.hasNext()) {
			return cur.next().toString();
		} else {
			int use=this.useCollectionIndex.get(index);
			use++;
			String[] coll=this.collection.get(index);
			if (coll.length >= use) {
				cur.close();
				this.db.remove(index);
				this.collection.remove(index);
				return null;
			}
			cur.close();
			this.useCollectionIndex.put(index,use);
			DBCollection co = db.get(index)
					.getCollection(coll[use]);
			cur= co.find((DBObject) JSON.parse(ref),
					(DBObject) JSON.parse(obj));
			return nextString(cur,mysqlcur,index);
		}
	}

	@Override
	public List<Object> nextObj(ResultSet mysqlCursor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object nextObjOne(DBCursor cur,ResultSet mysqlCursor,long index) {
		// TODO Auto-generated method stub
		if (cur.hasNext()) {
			return cur.next();
		}else{
			//移除
			cur.close();
			this.db.remove(index);
			this.collection.remove(index);
		}
		return null;
	}

	@Override
	public void insertString(String database,long useIndex, String insertString) {
		// TODO Auto-generated method stub
		insert(database, collection.get(useIndex)[this.useCollectionIndex.get(useIndex)], insertString);
	}

	@Override
	public String getDB() {
		return "MongoDB";
	}

	@Override
	public String getPath() {
		return this.ip + ":" + port;
	}

	/**
	 * 更新 doc 为doc2
	 * 
	 * @param collection
	 * @param doc
	 * @param doc2
	 * @param upsert
	 *            如果 为true则表示不存在就添加 否则 只更新不添加
	 * @param multi
	 *            如果为true 则表示 值更新查找到的第一个 否则 为全部更新
	 */
	public void update(String database,String collection, BasicDBObject doc,
			BasicDBObject doc2, boolean upsert, boolean multi) {
		DB db = m.getDB(database);
		auth(db);
		DBCollection coll = db.getCollection(collection);
		coll.update(doc, doc2, upsert, multi);
	}
	/**
	 * 更新 doc 为doc2
	 * 
	 * @param collection
	 * @param doc
	 * @param doc2
	 * @param upsert
	 *            如果 为true则表示不存在就添加 否则 只更新不添加
	 * @param multi
	 *            如果为true 则表示 值更新查找到的第一个 否则 为全部更新
	 */
	public void update(String database,String collection, DBObject doc,
			String doc2) {
		DB db = m.getDB(database);
		auth(db);
		DBCollection coll = db.getCollection(collection);
		coll.update(doc, (DBObject)JSON.parse(doc2),true,false);
	}
	/**
	 * 通过 doc 去查询mongodb中的对应数据
	 * 
	 * @param collection
	 * @param doc
	 */
	public DBCursor find(String database,String collection, DBObject doc) {
		DB db = m.getDB(database);
		auth(db);
		DBCollection coll = db.getCollection(collection);
		return coll.find(doc);
	}
	
	

	/**
	 * 修改doc 到doc2 其中doc为数据库中最后一个doc
	 * 
	 * @param collection
	 * @param doc
	 * @param doc2
	 */
	public void update(String database, String collection, BasicDBObject doc,
			BasicDBObject doc2) {
		DB db = m.getDB(database);
		auth(db);
		DBCollection coll = db.getCollection(collection);
		coll.update(doc, doc2);
	}

	/**
	 * 插入list类
	 * 
	 * @param collection
	 * @param docList
	 */
	public void insertList(String database, String collection,
			List<DBObject> docList) {
		DB db = m.getDB(database);
		auth(db);
		DBCollection coll = db.getCollection(collection);
		coll.insert(docList);
	}

	/**
	 * 插入list joson
	 * 
	 * @param collection
	 * @param docList
	 */
	public void insertListJson(String database, String collection,
			List<String> docList) {
		DB db = m.getDB(database);
		auth(db);
		DBCollection coll = db.getCollection(collection);
		List<DBObject> list = new LinkedList<DBObject>();
		for (String str : docList) {
			DBObject dbObject = (DBObject) JSON.parse(str);
			list.add(dbObject);
		}
		coll.insert(list);
	}

	/**
	 * 插入map值
	 * 
	 * @param collection
	 * @param map
	 */
	public void insertMap(String database, String collection,
			Map<Object, Object> map) {
		DB db = m.getDB(database);
		auth(db);
		DBCollection coll = db.getCollection(collection);
		coll.insert(new BasicDBObject(map));
	}

	/**
	 * 找到并且删除
	 * 
	 * @param collection
	 * @param doc
	 */
	public DBObject findAndRemove(String database, String collection,
			BasicDBObject doc) {
		DB db = m.getDB(database);
		auth(db);
		DBCollection coll = db.getCollection(collection);
		return coll.findAndRemove(doc);
	}

	/**
	 * 通过查询语句查询 key=value
	 * 
	 * @param collection
	 * @param key
	 * @param value
	 * @return
	 */
	public DBCursor findAndCursor(String database, String collection,
			String key, Object value) {
		DB db = m.getDB(database);
		auth(db);
		DBCollection coll = db.getCollection(collection);
		return coll.find(new BasicDBObject(key, value));
	}

	/**
	 * 通过查询语句查询 key=value
	 * 
	 * @param collection
	 * @param key
	 * @param value
	 * @return
	 */
	public List<DBObject> find(String database, String collection, String key,
			Object value) {
		DB db = m.getDB(database);
		auth(db);
		DBCollection coll = db.getCollection(collection);
		return coll.find(new BasicDBObject(key, value)).toArray();
	}

	/**
	 * 获取全部的数据
	 * 
	 * @param collection
	 * @param ref
	 * @param keys
	 * @return
	 */
	public List<DBObject> findAll(String database, String collection,
			String ref, String keys) {
		DB db = m.getDB(database);
		auth(db);
		DBCollection coll = db.getCollection(collection);
		DBObject refO = (DBObject) JSON.parse(ref);
		DBObject keysO = (DBObject) JSON.parse(keys);
		return coll.find(refO, keysO).toArray();
	}

	/**
	 * //查询age<=1 print("find: "+coll.find(new BasicDBObject("age", new
	 * BasicDBObject("$lte", 1))).toArray());
	 */
	public List<DBObject> findLte(String database, String collection,
			String key, Object value) {
		DB db = m.getDB(database);
		auth(db);
		DBCollection coll = db.getCollection(collection);
		return coll.find(
				new BasicDBObject(key, new BasicDBObject("$lte", value)))
				.toArray();
	}

	/**
	 * //查询age>=1 print(" fint: "+coll.find(new BasicDBObject("age", new
	 * BasicDBObject("$gte", 1))).toArray());
	 * 
	 * @param collection
	 * @param key
	 * @param value
	 * @return
	 */
	public List<DBObject> findGte(String database, String collection,
			String key, Object value) {
		DB db = m.getDB(database);
		auth(db);
		DBCollection coll = db.getCollection(collection);
		return coll.find(
				new BasicDBObject(key, new BasicDBObject("$gte", value)))
				.toArray();
	}

	/**
	 * //查询age！=1 print(" fint: "+coll.find(new BasicDBObject("age", new
	 * BasicDBObject("$ne", 1))).toArray());
	 */
	public List<DBObject> findNe(String database, String collection,
			String key, Object value) {
		DB db = m.getDB(database);
		auth(db);
		DBCollection coll = db.getCollection(collection);
		return coll.find(
				new BasicDBObject(key, new BasicDBObject("$ne", value)))
				.toArray();
	}

	/**
	 * 查询age=1,2,3 print(" fint: "+coll.find(new BasicDBObject("age", new
	 * BasicDBObject(QueryOperators.IN ,new int[]{1,2,3}))).toArray());
	 */
	public List<DBObject> findIn(String database, String collection,
			String key, Object value) {
		DB db = m.getDB(database);
		auth(db);
		DBCollection coll = db.getCollection(collection);
		return coll.find(
				new BasicDBObject(key, new BasicDBObject(QueryOperators.IN,
						value))).toArray();
	}

	/**
	 * 查询age！=1,2,3 print("find: "+coll.find(new BasicDBObject("age" ,new
	 * BasicDBObject(QueryOperators.NIN ,new int[]{1,2,3}))).toArray());
	 */
	public List<DBObject> findNotIn(String database, String collection,
			String key, Object value) {
		DB db = m.getDB(database);
		auth(db);
		DBCollection coll = db.getCollection(collection);
		return coll.find(
				new BasicDBObject(key, new BasicDBObject(QueryOperators.NIN,
						value))).toArray();
	}

	/**
	 * 只要key不为空
	 * 
	 * @param collection
	 * @param key
	 * @return
	 */
	public List<DBObject> findExist(String database, String collection,
			String key) {
		DB db = m.getDB(database);
		auth(db);
		DBCollection coll = db.getCollection(collection);
		return coll.find(
				new BasicDBObject(key, new BasicDBObject(QueryOperators.EXISTS,
						true))).toArray();
	}

	private static void print(String str) {

		System.out.println(str);
	}

	public static void main22(String[] args) {
		try {

			// 创建连接

			Mongo m = new Mongo("192.168.2.152", 27017);

			// 得到数据库

			DB db = m.getDB("sun");

			// 得到所有数据库

			List<String> colls = m.getDatabaseNames();

			for (String str : colls) {

				System.out.println(str + "database");

			}

			// //得到所有的集合（表）

			for (String collection : db.getCollectionNames()) {

				System.out.println(collection + "username");

			}

			// 删除一个数据库

			// m.dropDatabase("sun");

			// 得到sun表

			DBCollection coll = db.getCollection("stu");

			// 查看一个表的索引

			for (DBObject index : coll.getIndexInfo()) {

				System.out.println(index);

			}

			DBObject myDoc = coll.findOne();

			System.out.println(myDoc);

			// 添加

			// BasicDBObject doc=new BasicDBObject();

			// doc.put("name", " sunshan");

			// doc.put("sex", "男");

			// doc.put("age", 22);

			// coll.insert(doc);

			// 删除

			// coll.remove(doc);

			// BasicDBObject doc1=new BasicDBObject();

			// doc1.put("i", 0);

			// doc1.put("j", " foo");

			// BasicDBObject doc2=new BasicDBObject();

			// doc2.put("hello", "world");

			// doc1.put("doc2", doc2);

			// coll.insert(doc1);

			// 修改

			// BasicDBObject doc3=new BasicDBObject();

			// doc3.put("x", 2);

			// doc3.put("y", 4);

			// BasicDBObject doc4=new BasicDBObject();

			// doc4.put("x", 5);

			// doc4.put("y", 11);

			// coll.update(doc3, doc4,true,false);

			// //如果数据库不存在就添加 |多条修改 false只修改第一天，true如果有多条就不修改

			//

			// //条件查询

			// System.out.print("testing");

			// System.out.println(coll.find(doc4));

			// coll.findAndRemove(doc4);

			// //批量插入

			// List<DBObject> datas=new ArrayList<DBObject>();

			// for( int i=0;i<10;i++){

			// BasicDBObject bd=new BasicDBObject();

			// bd.put("name", "data");

			// bd.append("age", i);

			// datas.add( bd);

			// }

			// coll.insert( datas);

			// 添加

			// BasicDBObjectBuilder documentBuilder =
			// BasicDBObjectBuilder.start();

			// documentBuilder.add("database", "mkyongDB");

			// documentBuilder.add("table", "hosting");

			// BasicDBObjectBuilder documentBuilderDetail =
			// BasicDBObjectBuilder.start();

			// documentBuilderDetail.add("records", "99");

			// documentBuilderDetail.add("index", "vps_index1");

			// documentBuilderDetail.add("active", "true");

			// documentBuilder.add("detail", documentBuilderDetail.get());

			// coll.insert(documentBuilder.get());

			// 添加

			// Map<Object,Object> map=new HashMap<Object,Object>();

			// map.put("a", 1);

			// map.put("b", "b");

			// coll.insert(new BasicDBObject(map));

			//

			// 添加

			// String json
			// ="{'1' : '1','2' : '2',"+"'11' : {'1' : 1, '2' : '2', '3' : '3'}}";

			// DBObject dbobject=(DBObject)JSON.parse(json);

			// coll.insert( dbobject);

			// 更新只变最后一个

			// BasicDBObject bdo=new BasicDBObject();

			// bdo.put("x", 1111);

			// //coll.insert( bdo);

			// coll.update(new BasicDBObject().append("x", 0), bdo);

			// 更新

			// BasicDBObject bdo=new BasicDBObject().append("$inc", new
			// BasicDBObject().append("x", 12));

			// coll.update(new BasicDBObject().append("x", 11), bdo);

			// 更新

			// 如果不使用$set 直接是 age则所有的都会更新

			// 根据age为9条件把name：data修改为 name：sun

			// BasicDBObject bdo=new BasicDBObject().append("$set", new
			// BasicDBObject().append("name", "sunshan"));

			// coll.update(new BasicDBObject().append("age", 9), bdo);

			// 更新

			// 根据name为data条件把age：批量修改为 age:age

			// BasicDBObject bdo=new BasicDBObject().append("$set", new
			// BasicDBObject().append("age", "age"));

			// coll.update(new BasicDBObject().append("name", "data"),
			// bdo,false, true);

			// 查询age=1

			// print("find："+coll.find(new BasicDBObject("age", 1)).toArray());

			// 查询age<=1

			// print("find: "+coll.find(new BasicDBObject("age", new
			// BasicDBObject("$lte", 1))).toArray());

			// 查询age>=1

			// print(" fint: "+coll.find(new BasicDBObject("age", new
			// BasicDBObject("$gte", 1))).toArray());

			// 查询age！=1

			// print(" fint: "+coll.find(new BasicDBObject("age", new
			// BasicDBObject("$ne", 1))).toArray());

			// 查询age=1,2,3

			// print(" fint: "+coll.find(new BasicDBObject("age", new
			// BasicDBObject(QueryOperators.IN ,new int[]{1,2,3}))).toArray());

			// 查询age！=1,2,3

			// print("find: "+coll.find(new BasicDBObject("age" ,new
			// BasicDBObject(QueryOperators.NIN ,new int[]{1,2,3}))).toArray());

			// print("find: "+coll.find(new BasicDBObject("age" ,new
			// BasicDBObject(QueryOperators.EXISTS ,true))).toArray());

			// 查询age属性

			// print("find: "+coll.find(null ,new BasicDBObject("age"
			// ,true)).toArray());

			// List<DBObject> list=coll.find().toArray();

			// for(Object obj:list){

			// System.out.println( obj);

			// }

			System.out.println("ending");

			// DBObject dbc=new BasicDBObject();

			// dbc.put("name", 1111);

			// List<DBObject> list=new ArrayList<DBObject>();

			// list.add( dbc);

			// System.out.println(coll.insert(list).getN());

			// //查询部分数据块

			// DBCursor cursor=coll.find().skip(44);//跳到第44个

			// while(cursor.hasNext()){

			// System.out.println(cursor.next());

			// }

			// DBCursor cur=coll.find(); //DBCursor cur=coll.find().limit(2);

			// while(cur.hasNext()){

			// System.out.println(cur.next());

			// }

			// System.out.println(cur.getCursorId()+" "+cur.count()+" "+JSON.serialize(cur));

			// 条件查询

			BasicDBObject doc5 = new BasicDBObject();

			doc5.put("$gt", 1);

			doc5.put("$lt", 3);

			coll.insert(doc5);

			print("find 21<y<23："
					+ coll.find(new BasicDBObject("y", doc5)).toArray());

			// BasicDBObject doc5=new BasicDBObject();

			// doc5.put("$ gt", 1);

			// doc5.put("$ lt", 3);

			// BasicDBObject doc6=new BasicDBObject();

			// doc6.put("x", doc5);

			// System.out.println(coll.find(doc6));

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	public static void main(String[] args) {
		String ip = "192.168.85.11";
		int port = 10001;
		String database = "demo";
		MongoDb mongo = new MongoDb(ip, port);
		DBCursor ret = mongo.find("demo", "fang");

		// System.out.println("从数据集中读取数据：");
		// TreeSet<String> tree=new TreeSet<String>();
		// while(ret.hasNext()){
		// BasicDBObject bdbObj = (BasicDBObject) ret.next();
		// if(bdbObj != null){
		// String[]
		// strTemp=bdbObj.getString("stationName").replaceAll("[\\[\\]\"\\s]","").split(",");
		// for(String temp:strTemp)
		// {
		// while(true)
		// {
		// if(temp.contains("("))
		// {
		// String temp2="";
		// temp2=temp.substring(0,temp.indexOf("("));
		// if(temp.contains(")"))
		// {
		// temp2+=temp.substring(temp.indexOf(")")+1);
		// }
		// temp=temp2;
		// //System.out.println("temp:"+temp);
		//
		// }else if(temp.contains("（"))
		// {
		// String temp2="";
		// temp2=temp.substring(0,temp.indexOf("（"));
		// if(temp.contains("）"))
		// {
		// temp2+=temp.substring(temp.indexOf("）")+1);
		// }
		// temp=temp2;
		// //System.out.println("temp:"+temp);
		//
		// }
		// {
		// tree.add(temp);
		// break;
		// }
		// }
		// }
		// String[]
		// strTemp2=bdbObj.getString("stationNameRever").replaceAll("[\\[\\]\"\\s]","").split(",");
		// for(String temp:strTemp2)
		// {
		// while(true)
		// {
		// if(temp.contains("("))
		// {
		// String temp2="";
		// temp2=temp.substring(0,temp.indexOf("("));
		// if(temp.contains(")"))
		// {
		// temp2+=temp.substring(temp.indexOf(")")+1);
		// }
		// temp=temp2;
		// //System.out.println("temp:"+temp);
		//
		// }else if(temp.contains("（"))
		// {
		// String temp2="";
		// temp2=temp.substring(0,temp.indexOf("（"));
		// if(temp.contains("）"))
		// {
		// temp2+=temp.substring(temp.indexOf("）")+1);
		// }
		// temp=temp2;
		// //System.out.println("temp:"+temp);
		//
		// }
		// {
		// tree.add(temp);
		// break;
		// }
		// }
		// }
		// }
		// }
		// for(String st:tree)
		// {
		// System.out.println(st);
		// }
		// TreeSet<String> tree=new TreeSet<String>();
		while (ret.hasNext()) {
			BasicDBObject bdbObj = (BasicDBObject) ret.next();
			if (bdbObj != null) {
				String temp = bdbObj.getString("address");
				// System.out.println(temp);
				// String address="";
				String name = bdbObj.getString("officeBuildingName");
				while (true) {
					if (temp.contains("(")) {
						String temp2 = "";
						temp2 = temp.substring(0, temp.indexOf("("));
						if (temp.contains(")")) {
							temp2 += temp.substring(temp.indexOf(")") + 1);
						}
						temp = temp2;
						// System.out.println("temp:"+temp);

					} else if (temp.contains("（")) {
						String temp2 = "";
						temp2 = temp.substring(0, temp.indexOf("（"));
						if (temp.contains("）")) {
							temp2 += temp.substring(temp.indexOf("）") + 1);
						}
						temp = temp2;
						// System.out.println("temp:"+temp);

					}
					{
						// address=temp;
						// tree.add(temp);
						break;
					}
				}
				System.out.println(name + "\t" + temp);
			}
		}
		// for(String st:tree)
		// {
		// System.out.println(st);
		// }
	}

	@Override
	public Object getConnectionDb() {
		// TODO Auto-generated method stub
		return this;
	}

}
