package ps.hell.util.db2;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

/**
 * DbcpManager 是数据库连接池的总管理类 封装了对于连接池的相关操作
 * 
 * @author 集成显卡 2010.9.11
 */
public class DbcpManager {

	// protected static Logger log=Logger.getLogger("DbcpManager:");

	// 连接数据库的相关参数

	private static String driver = "com.mysql.jdbc.Driver";
	/*
	 * url=
	 * "jdbc:mysql://localhost:3306/question?useUnicode=true&characterEncoding=GBK"
	 * , user="root", password="19891231";
	 */

	// 数据源
	private BasicDataSource basicDS = null;
	// 当前的连接数
	private int linkNum = 0;

	public DbcpManager(String ip,int port,String database,String user,String password)
	{
		String url="jdbc:mysql://"+ip+":"+port+"/"+database+"?useUnicode=true&characterEncoding=UTF8";
		basicDS = initDataSource(url, user, password);
	}
	
	/*
	 * 私人构造函数
	 */
	public DbcpManager(String url, String userName, String password) {
		basicDS = initDataSource(url, userName, password);
	}

	/**
	 * 初始DataSource
	 * 
	 * @param Url
	 * @param Name
	 * @param Password
	 * @return BasicDataSource
	 */
	public BasicDataSource initDataSource(String Url, String Name,
			String Password) {
		BasicDataSource tempDS = new BasicDataSource();

		tempDS.setDriverClassName(driver);
		tempDS.setUrl(Url);
		tempDS.setUsername(Name);
		tempDS.setPassword(Password);

		tempDS.setMaxIdle(20);// 池里不会被释放的最多空闲连接数量。设置为0时表示无限制。
		tempDS.setMinIdle(2);
		tempDS.setMaxActive(100);// 同一时间可以从池分配的最多连接数量。设置为0时表示无限制。
		tempDS.setMaxWait(3000);
		tempDS.setInitialSize(3);// 池启动时创建的连接数量

		tempDS.setRemoveAbandoned(true);
		tempDS.setRemoveAbandonedTimeout(6);// 当空闲连接耗尽,超过这个时间(秒),就会释放未关闭的连 接

		// log.info("BasicDataSource配置成功");

		return tempDS;
	}

	/**
	 * 断开DataSource
	 * 
	 * @param
	 * @return
	 */
	public void destoryDataSource() {
		try {
			basicDS.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		basicDS = null;
		// log.info("DataSource destoryed-----OK");
	}

	/**
	 * 返回DataSource
	 * 
	 * @return
	 */
	public DataSource getDataSource() {
		return basicDS;
	}

	/**
	 * 取得一个Connection 当已经是最大连接限制时warn
	 * 
	 * @return
	 */
	public Connection getConnection() {
		try {
			while (true) {
				// 取得当前连接数
				linkNum = basicDS.getNumActive();
				if (linkNum == basicDS.getMaxActive()) {
					// log.warning("达到了连接上限。");
				} else {
					break;
				}
			}
			// log.info("返回Connection成功，当前连接数："+(linkNum+1));
			Connection conn= basicDS.getConnection();
			conn.setAutoCommit(false);
			return conn;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 释放Connection
	 * 
	 * @param con
	 */
	public void freeConnection(Connection con) {
		if (con != null) {
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// log.info("释放Connection成功，当前连接数："+(basicDS.getNumActive()));
	}

	public static void main(String args[]) {
		DbcpManager dbcp = new DbcpManager(
				"jdbc:mysql://192.168.1.134:3306/fangcheng?useUnicode=true&characterEncoding=UTF8",
				"fangcheng_admin", "fc1234");
		try {
			Connection conn = dbcp.getConnection();
			if (conn != null) {
				Statement statement = conn.createStatement();
				ResultSet rs = statement
						.executeQuery("select name,content_type_id from  auth_permission ");
				int c = rs.getMetaData().getColumnCount();
				while (rs.next()) {
					for (int i = 1; i <= c; i++) {
						System.out.print(rs.getObject(i) + "   ");
					}
					System.out.println();
				}
				rs.close();
			}
			dbcp.freeConnection(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}