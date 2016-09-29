package ps.hell.util.db;

public interface CursorInter {

	/**
	 * 获取下一个 
	 * @return
	 */
	public String nextString();
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
	public void insertString(String insertString);
}
