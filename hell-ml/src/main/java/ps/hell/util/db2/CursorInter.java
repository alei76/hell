package ps.hell.util.db2;

import java.sql.ResultSet;
import java.util.List;

import com.mongodb.DBCursor;

public interface CursorInter {
	/**
	 * 获取下一个 
	 * @return
	 */
	public String nextString(DBCursor cur, ResultSet mysqlCursor, long index);
	/**
	 * 获取下一组信息 mysql
	 * @return
	 */
	public List<Object> nextObj(ResultSet mysqlCursor);
	/**
	 * 获取下一组信息
	 * @return mongo
	 */
	public Object nextObjOne(DBCursor cur, ResultSet mysqlCursor, long index);
	/**
	 * 获取db
	 * @return
	 */
	public String getDB();
	/**
	 * 获取路径
	 * @return
	 */
	public String getPath();
	/**
	 * 插入一条数据
	 */
	public void insertString(String database, long useIndex, String insertString);
	/**
	 * 获取实体
	 * @return
	 */
	public Object getConnectionDb();
	
	/**
	 * 获取固定的cursor 使用与mongo
	 * @return
	 */
	public Object getCursorIndex(long index);
}
