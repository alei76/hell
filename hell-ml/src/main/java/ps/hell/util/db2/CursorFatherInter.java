package ps.hell.util.db2;

import java.sql.ResultSet;
import java.util.List;

import com.mongodb.DBCursor;

public class CursorFatherInter {

	/**
	 * 使用的id 针对 mongo使用
	 */
	public long cursorUseIndex = -1L;

	public CursorInter cursor = null;
	/**
	 * db所使用的类型 0 为 文件 1为mysql 2为mongo，3为redis
	 */
	public int dbSelect = 0;

	public int getDbSelect(String db) {
		if (db.equals("file")) {
			return 0;
		} else if (db.equals("mysql")) {
			return 1;
		} else if (db.equals("mongo")) {
			return 2;
		} else if (db.endsWith("redis")) {
			return 3;
		} else{
			System.out.println("不存在该类型");
			System.exit(1);
		}
		return 0;
	}

	/**
	 * mongo cursor
	 */
	private DBCursor useCursorMongo = null;

	private ResultSet useCursorMysql = null;

	/**
	 * 
	 * @param cursor
	 * @param cursorUseIndex
	 *            所使用的游标id
	 * @param useMongo
	 *            是否为mongo
	 */
	public CursorFatherInter(CursorInter cursor, long cursorUseIndex,
			String dbSelectString) {
		this.cursor = cursor;
		this.cursorUseIndex = cursorUseIndex;
		this.dbSelect = getDbSelect(dbSelectString);
		if(dbSelect==1)
		{
			this.useCursorMysql=(ResultSet)cursor.getCursorIndex(cursorUseIndex);
		}else if(dbSelect ==2)
		{
			this.useCursorMongo = (DBCursor) cursor.getCursorIndex(cursorUseIndex);
		}
	}

	public String nextString() {
		return cursor.nextString(useCursorMongo,useCursorMysql,cursorUseIndex);
	}

	/**
	 * 获取下一组信息 mysql
	 * 
	 * @return
	 */
	public List<Object> nextObj() {
		return cursor.nextObj(useCursorMysql);
	}

	/**
	 * 获取下一组信息
	 * 
	 * @return mongo
	 */
	public Object nextObjOne() {
		return cursor.nextObjOne(useCursorMongo,useCursorMysql, cursorUseIndex);
	}
}
