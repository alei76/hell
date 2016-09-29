package ps.hell.util.db2;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * db连接的通用接口
 * 
 * @author Administrator
 *
 */
public class DBCommon {

	/**
	 * 获取 collection值
	 * 
	 * @param collection
	 *            表
	 * @param obj
	 *            条件{} mongo 的条件方法
	 * @param ref
	 *            返回值{} mogno 的返回值方法
	 * @return
	 */
	public static CursorFatherInter getMongoCursor(MongoDb mongo,
			String database, String collection, String obj, String ref) {
		if (mongo == null) {
			System.out.println("没有当前的mongo连接");
			System.exit(1);
		}
		return mongo.findCursorOne(database, collection, obj, ref);
	}

	public CursorFatherInter cursor = null;

	public Object[] otherInput = null;

	/**
	 * 
	 * @param cursor
	 *            所使用的游标
	 * @param otherInput
	 *            需要输入的其他数据
	 */
	public DBCommon(CursorFatherInter cursor, Object... otherInput) {
		this.cursor = cursor;
		this.otherInput = otherInput;
	}

	/**
	 * 执行游标的基础方法
	 */
	public void runCursor(DbcpManager manager) {
		Object obj = null;

		if (cursor.dbSelect == 1) {
			ResultSet set = null;
			while ((obj = cursor.nextObjOne()) != null) {
				set = (ResultSet) obj;
				try {
					runMysqlRow(set, otherInput);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else if (cursor.dbSelect == 2) {
			BasicDBObject doc = null;
			while ((obj = cursor.nextObjOne()) != null) {
				doc = (BasicDBObject) obj;
				try {
					runMongoRow(doc, otherInput);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		freeCursorInter(manager,cursor);
	}

	/**
	 * 如果需要使用游标 并执行mysqlRow的
	 * 
	 * @param obj
	 * @throws Exception
	 */
	public void runMysqlRow(ResultSet obj, Object[] otherInput)
			throws Exception {
		System.out.println(obj.getString(1) + "\t" + obj.getInt(2));
	}

	/**
	 * 如果需要使用游标并 执行 mongoRow的
	 * 
	 * @param obj
	 * @throws Exception
	 */
	public void runMongoRow(BasicDBObject obj, Object[] otherInput)
			throws Exception {
		System.out.println(obj.getString("demand_ctime") + "\t"
				+ obj.getLong("area_id"));
	}

	/**
	 * 获取游标
	 * 
	 * @param table
	 * @param columns
	 * @return
	 */
	public static CursorFatherInter getMysqlCursor(DbcpManager manager,
			String table, String[] columns, String otherSql) {
		StringBuffer sql = new StringBuffer("select ");
		int i = 0;
		for (String column : columns) {
			i++;
			if (i == 1) {
				sql.append(column);
			} else {
				sql.append(",").append(column);
			}
		}
		sql.append(" from ").append(table).append(" ").append(otherSql);
		java.sql.Connection conn = manager.getConnection();
		MysqlConnection mysql = new MysqlConnection(conn,false);

		return mysql.getCursor(sql.toString());
	}

	/**
	 * 获取游标
	 * 
	 * @param table
	 * @param columns
	 * @return
	 */
	public static CursorFatherInter getMysqlCursor(DbcpManager manager, String sql) {
		java.sql.Connection conn = manager.getConnection();
		MysqlConnection mysql = new MysqlConnection(conn,false);
		return mysql.getCursor(sql.toString());
	}

	/**
	 * 释放资源
	 * 
	 * @param cursor
	 */
	public static void freeCursorInter(DbcpManager mysql,CursorFatherInter cursor) {
		Object obj = cursor.cursor.getConnectionDb();
		if (obj instanceof MysqlConnection) {
			mysql.freeConnection(((MysqlConnection) (obj)).conn);
		}
	}

	/**
	 * 获取mongo条件的全部数据
	 * 
	 * @param collection
	 * @param obj
	 * @param ref
	 * @return
	 */
	public static List<DBObject> getMongoData(MongoDb mongo, String database,
			String collection, String obj, String ref) {
		if (mongo == null) {
			System.out.println("没有当前的mongo连接");
			System.exit(1);
		}
		return mongo.findAll(database, collection, obj, ref);
	}

	/**
	 * 获取mysql数据
	 * 
	 * @param table
	 * @param columns
	 * @param otherSql
	 * @param 解析使用的类
	 * @return
	 */
	public static List<List<Object>> getMysqlData(DbcpManager manager, String table,
			String[] columns, String otherSql) {
		List<List<Object>> list = null;
		StringBuffer sql = new StringBuffer("select ");
		int i = 0;
		for (String column : columns) {
			i++;
			if (i == 1) {
				sql.append(column);
			} else {
				sql.append(",").append(column);
			}
		}
		sql.append(" from ").append(table).append(" ").append(otherSql);
		java.sql.Connection conn = manager.getConnection();
		MysqlConnection mysql = new MysqlConnection(conn,false);
		MysqlSelect select = mysql.sqlSelect(sql.toString());
		if (select == null) {
			return list;
		}
		ResultSet set = select.resultSet;
		list = new ArrayList<List<Object>>();
		try {
			while (set.next()) {
				List<Object> row = new ArrayList<Object>();
				for (int j = 1; j <= columns.length; j++) {
					row.add(set.getObject(j));
				}
			}
		} catch (Exception e) {

		}
		manager.freeConnection(conn);
		return list;
	}

	/**
	 * 获取mysql的数据
	 * 
	 * @param sql
	 *            具体的sql
	 * @param columnCount
	 *            列
	 * @return
	 */
	public static List<List<Object>> getMysqlData(DbcpManager manager, String sql) {
		List<List<Object>> list = null;
		java.sql.Connection conn = manager.getConnection();
		MysqlConnection mysql = new MysqlConnection(conn,false);
		MysqlSelect select = mysql.sqlSelect(sql.toString());
		if (select == null) {
			return list;
		}
		ResultSet set = select.resultSet;
		list = new ArrayList<List<Object>>();
		int columnCount = 0;
		try {
			columnCount = set.getMetaData().getColumnCount();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			while (set.next()) {
				List<Object> row = new ArrayList<Object>();
				for (int j = 1; j <= columnCount; j++) {
					// System.out.print(set.getObject(j));
					row.add(set.getObject(j));
				}
				list.add(row);
				// System.out.println();
			}
		} catch (Exception e) {

		}
		manager.freeConnection(conn);
		return list;
	}

	/**
	 * 入库到内部mysql中
	 * 
	 * @param sql
	 * @param data
	 */
	public static void insertIoMysql(DbcpManager manager,String sql, List<List<Object>> data) {
		java.sql.Connection conn = manager.getConnection();
		MysqlConnection mysql = new MysqlConnection(conn,false);
		StringBuffer sb = new StringBuffer(sql);
		sb.append(" values");
		boolean flagTrue=false;
		for (int i = 0; i < data.size(); i++) {
			List<Object> list = data.get(i);
			if (!flagTrue) {
				sb.append("(");
				flagTrue=true;
			} else {
				sb.append(",(");
			}
			for (int j = 0; j < list.size(); j++) {
				Object obj = list.get(j);
				if (j == 0) {
					if (obj instanceof String) {
						sb.append("'").append(obj).append("'");
					} else if (obj instanceof Boolean) {
						Boolean flag = (Boolean) obj;
						if (Boolean.compare(flag, true) == 0) {
							sb.append(1);
						} else {
							sb.append(0);
						}
					} else {
						sb.append(obj);
					}
					continue;
				}
				if (obj instanceof String) {
					sb.append(",'").append(obj).append("'");
				} else if (obj instanceof Boolean) {
					Boolean flag = (Boolean) obj;
					if (Boolean.compare(flag, true) == 0) {
						sb.append(",").append(1);
					} else {
						sb.append(",").append(0);
					}
				} else {
					sb.append(",").append(obj);
				}
			}
			sb.append(")");
			if ((i + 1) % 1000 == 0) {
				mysql.sqlUpdate(sb.toString());
				sb = new StringBuffer(sql);
				sb.append(" values");
				flagTrue=false;
			}
		}
		if ((data.size() + 1) % 1000 == 0 || data.size() == 0) {
		} else {
			// System.out.println(sb.toString());
			mysql.sqlUpdate(sb.toString());
		}
		manager.freeConnection(conn);
	}

	/**
	 * @param mysqlId 
	 * 			  mysql使用的id
	 * @param sql
	 *            请不要使用 order by
	 * @param collection
	 *            使用的表
	 * @param keys
	 *            返回的值
	 * @param mongoRef
	 *            最终显示的字段
	 * @param mysqlKey
	 *            关联的字段
	 * @param mongoKey
	 * 
	 * @param isLeftJoin
	 *            如果为true 则为leftjoin 否则 为 where =
	 * @return
	 */
	public static DbJoinBean leftJoin(DbcpManager manager, String sql,
			MongoDb mongo, String database, String collection, String keys,
			String[] mongoRef, String[] mysqlKey, String[] mongoKey,
			boolean isLeftJoin) {
		// 定位mongo的位置
		HashMap<String, HashSet<Object>> mysqlSet = new HashMap<String, HashSet<Object>>();
		HashMap<String, Integer> mysqlIndex = new HashMap<String, Integer>();
		HashMap<String, Integer> mongIndex = new HashMap<String, Integer>();
		for (int i = 0; i < mongoKey.length; i++) {
			mongIndex.put(mongoKey[i], i);
		}
		for (int i = 0; i < mysqlKey.length; i++) {
			mysqlIndex.put(mysqlKey[i], i);
		}
		// 返回的最终结果
		DbJoinBean result = new DbJoinBean();
		result.columns = new ArrayList<String>();
		HashMap<String, StorageBean> map = null;
		java.sql.Connection conn =manager.getConnection();
		MysqlConnection mysql = new MysqlConnection(conn,false);
		// + " order by "+ mysqlKey
		MysqlSelect select = mysql.sqlSelect(sql.toString());
		Object obj = new String();
		if (select == null) {
			return null;
		}
		ResultSet set = select.resultSet;
		map = new HashMap<String, StorageBean>();
		int columnCount = 0;
		List<String> columnesName = new ArrayList<String>();
		// List<Object> columnesClass = new ArrayList<Object>();
		try {
			columnCount = set.getMetaData().getColumnCount();
			for (int j = 1; j <= columnCount; j++) {
				columnesName.add(set.getMetaData().getColumnName(j));
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try {
			int mysqlCount = 0;
			while (set.next()) {
				mysqlCount++;
				List<Object> row = new ArrayList<Object>();
				StringBuffer key = new StringBuffer();
				for (int j = 1; j <= columnCount; j++) {
					row.add(set.getObject(j));
					// 获取唯一键
					if (j == 1) {
						int lp = 0;
						for (String st : mysqlKey) {// 两个key之间没有分隔符//目前这样
							lp++;
							if (lp == 1) {
								key.append(set.getString(st));

							} else {
								key.append("_").append(set.getString(st));
							}
							// 添加唯一的键
							HashSet<Object> indexOlbp = mysqlSet.get(st);
							if (indexOlbp == null) {
								indexOlbp = new HashSet<Object>();
								mysqlSet.put(st, indexOlbp);
							}
							indexOlbp.add(set.getObject(st));
						}
					}
				}
				// if (mysqlCount == 1) {
				// for (int j = 1; j <= columnCount; j++) {
				// row.add(set.getObject(j));
				// // 获取唯一键
				// if (j == 1) {
				// for (String st : mysqlKey) {// 两个key之间没有分隔符//目前这样
				// columnesClass.add(set.getObject(st));
				// }
				// }
				// }
				// }
				StorageBean bean = null;
				String keyTemp = key.toString();
				bean = map.get(keyTemp);
				if (bean == null) {
					bean = new StorageBean(row);
					// System.out.println("新增id:"+obj);
					map.put(keyTemp, bean);
				} else {
					bean.mysqlValue.add(row);
				}
			}
		} catch (Exception e) {

		}
		manager.freeConnection(conn);
		// 读取 mongo
		StringBuffer sb = null;
		if (mongoKey.length > 1) {
			sb = new StringBuffer("{");
		} else {
			sb = new StringBuffer();
		}
		for (int k = 0; k < mongoKey.length; k++) {
			if (k == 0) {
			} else {
				sb.append(",");
			}
			sb.append(mongoKey[k]).append(":{$in:").append("[");
			int i = 0;
			HashSet<Object> valSet = mysqlSet.get(mongoKey[mysqlIndex
					.get(mongoKey[k])]);
			if (valSet == null) {
				continue;
			}
			for (Object obj3 : valSet) {
				i++;
				if (obj3 instanceof String) {
					if (i == 1) {
						sb.append("\"").append(obj3).append("\"");
					} else {
						sb.append(",\"").append(obj3).append("\"");
					}
				} else if (obj3 instanceof Integer || obj3 instanceof Long) {
					{
						if (i == 1) {
							sb.append(obj3);
						} else {
							sb.append(",").append(obj3);
						}
					}
				} else {
					System.out.println("不支持非string,long,Integer的格式 的关联字段");
				}
			}
			sb.append("]}");
		}
		if (mongoKey.length > 1) {
			sb.append("}");
		} else {
			sb.append("}}");
		}
		// System.out.println("条件:" + sb.toString());
		List<DBObject> mongoObj = mongo.findAll(database, collection,
				sb.toString(), keys);
		int mongoCount = 0;
		for (String mongoRefOne : mongoRef) {
			if (mongoRefOne.equals(mongoKey)) {

			} else {
				mongoCount++;
				columnesName.add(mongoRefOne);
			}
		}
		for (int j = 0; j < mongoObj.size(); j++) {
			List<Object> row = new ArrayList<Object>();
			DBObject obj1 = mongoObj.get(j);
			String[] st = new String[mongoKey.length];
			for (String mongoRefOne : mongoRef) {
				Integer indexL = mongIndex.get(mongoRefOne);
				String[] split = mongoRefOne.split("\\.");
				for (int k = 0; k < split.length; k++) {
					// System.out.println(split[k]);
					Object temp = obj1.get(split[k]);
					if (temp == null) {
						row.add(null);
						break;
					}
					if (k == split.length - 1) {
						if (indexL != null) {
							st[indexL] = temp.toString();
						} else {
							row.add(temp);
						}
					}
				}
			}
			StringBuffer sb2 = new StringBuffer();
			for (int e = 0; e < st.length; e++) {
				if (e == 0) {
					sb2.append(st[e]);
				} else {
					sb2.append("_").append(st[e]);
				}
			}
			// System.out.println(sb2.toString());
			StorageBean bean = map.get(sb2.toString());
			if (bean != null) {
				bean.mongoValue.add(row);
			}
		}
		// 获取最终的数据集
		DbJoinBean endValue = new DbJoinBean();
		endValue.columns = columnesName;
		List<List<Object>> val = new ArrayList<List<Object>>();
		int count = 0;
		endValue.data = val;
		for (Entry<String, StorageBean> m : map.entrySet()) {
			for (List<Object> v : m.getValue().mysqlValue) {
				if (m.getValue().mongoValue == null) {
					if (isLeftJoin) {
						val.add(v);
						List<Object> vM = new ArrayList<Object>();
						for (int e = 0; e < mongoCount; e++) {
							vM.add(null);
						}
						val.add(vM);
					}
				} else {
					for (List<Object> vM : m.getValue().mongoValue) {
						val.add(v);
						val.get(count).addAll(vM);
						count++;
					}
				}
			}
		}

		return endValue;
	}

	/**
	 * 打印入口
	 * 
	 * @param obj
	 */
	public static void println(Object obj) {
		if (obj instanceof List) {
			List<Object> listObj = (List<Object>) obj;
			for (Object val : listObj) {
				System.out.println(val);
			}
			// List<Object> listObj=(List<Object>)obj;
			// printlnList(listObj);
		} else {
			print(obj);
		}
	}

	public static void print(Object obj) {
		System.out.print(obj.toString() + ",");
	}

	public static void printlnList(List<Object> list) {
		Object obj = null;
		if (list.size() > 0) {
			obj = list.get(0);
		} else {
			return;
		}
		if (obj instanceof List) {
			for (Object val : list) {
				print(val);
			}
			System.out.println();
		} else {
			print(obj);
			System.out.println();
		}
	}

	public static void main(String[] args) {
//		String sql = "select name,content_type_id from  auth_permission ";
//		List<List<Object>> data = DBCommon.getMysqlData("134", sql);
//		println(data);
//
//		List<DBObject> mongoData = DBCommon.getMongoData("134", "fangcheng_v2",
//				"demand", "{}", "{demand_ctime:1,area_id:1}");
//		println(mongoData);
//
//		CursorFatherInter mysqlCursor = DBCommon.getMysqlCursor("134", sql);
//		Object obj = null;
//		ResultSet set = null;
//		while ((obj = mysqlCursor.nextObjOne()) != null) {
//			set = (ResultSet) obj;
//			try {
//				System.out.println(set.getString(1) + "\t" + set.getInt(2));
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		DBCommon.freeCursorInter(mysqlCursor);
//
//		CursorFatherInter mongoCursor = DBCommon.getMongoCursor("134",
//				"fangcheng_v2", "demand", "{}", "{demand_ctime:1,area_id:1}");
//		BasicDBObject doc = null;
//		while ((obj = mongoCursor.nextObjOne()) != null) {
//			doc = (BasicDBObject) obj;
//			System.out.println(doc.getString("demand_ctime") + "\t"
//					+ doc.getLong("area_id"));
//		}
//		DBCommon.freeCursorInter(mongoCursor);
//
//		CursorFatherInter mysqlCursor2 = DBCommon.getMysqlCursor("134", sql);
//		DBCommon dbCursor = new DBCommon(mysqlCursor2) {
//			@Override
//			public void runMysqlRow(ResultSet obj, Object[] otherInput)
//					throws Exception {
//				System.out.println("测试使用:" + obj.getString(1) + "\t"
//						+ obj.getInt(2));
//			}
//		};
//		dbCursor.runCursor();
//		DBCommon.freeCursorInter(mysqlCursor2);
//
//		DbJoinBean bean = DBCommon
//				.leftJoin(
//						"4",
//						"select fangCode,area from FangInfo where cityname='北京'  limit 10",
//						"11", "demo", "fang",
//						"{cbdCategory:1,fangCode:1,_id:0,area:1}",
//						new String[] { "cbdCategory", "fangCode", "area" },
//						new String[] { "fangCode", "area" },
//						new String[] { "fangCode", "area" }, true);
//		System.out.println(bean.columns);
//		DBCommon.print(bean.data);
	}
}
