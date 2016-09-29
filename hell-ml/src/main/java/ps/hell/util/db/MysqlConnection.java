package ps.hell.util.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysql.jdbc.PreparedStatement;

public class MysqlConnection {

	protected Logger log = LoggerFactory.getLogger(MysqlConnection.class);
	private Connection conn = null;
	private String url = "";
	private String user = "";
	private String urlEnd="?useUnicode=true&characterEncoding=UTF8&autoReconnect=true";
	private String password = "";
	public String stringPath = "";

	public MysqlConnection(String ip, int port, String database, String user,
			String password) {
		String driver = "com.mysql.jdbc.Driver";
		this.url = "jdbc:mysql://" + ip + ":" + port + "/" + database+urlEnd;
		this.user = user;
		this.password = password;
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		while (true) {
			try {
				// 加载驱动程序
				// 连续数据库
				conn = DriverManager.getConnection(this.url, this.user,
						this.password);
				stringPath = this.url;
				if (!conn.isClosed()) {
					log.info("Succeeded connecting to the Database!");
				} else {
					log.error("数据库连接失败");
				}
				conn.setAutoCommit(false);
				break;
			} catch (Exception e1) {
				e1.printStackTrace();
				System.exit(1);
				break;
			}
		}
	}

	public MysqlConnection(String url, String user, String password) {
		System.out.println(url + ":" + user + ":" + password);
		String driver = "com.mysql.jdbc.Driver";
		this.url = url;
		this.user = user;
		this.password = password;
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		while (true) {
			try {
				// 加载驱动程序
				// 连续数据库
				conn = DriverManager.getConnection(this.url, this.user,
						this.password);
				stringPath = this.url;
				if (!conn.isClosed()) {
					log.info("Succeeded connecting to the Database!");
				} else {
					log.error("数据库连接失败");
				}
				conn.setAutoCommit(false);
				break;
			} catch (Exception e1) {
				e1.printStackTrace();
				log.error(e1.getMessage());
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 重连
	 * 
	 * @return
	 */
	public boolean reConnection() {
		while (true) {
			try {
				if (conn.isClosed()) {
					conn = DriverManager.getConnection(this.url, this.user,
							this.password);
					if (!conn.isClosed()) {
						log.info("Succeeded connecting to the Database!");
						conn.setAutoCommit(false);
						return true;
					} else {
						log.error("数据库连接失败");
					}
				} else {
					return false;
				}
			} catch (Exception e) {
				log.error(e.toString());
			}
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public MysqlSelect sqlSelect(String sqlString) {
		reConnection();
		ResultSet result = null;
		boolean flag = false;
		Statement statement = null;
		while (true) {
			try {
				// statement用来执行SQL语句
				statement = conn.createStatement();
				// 要执行的SQL语句
				result = statement.executeQuery(sqlString);
				conn.commit();
				flag = true;
				break;
			} catch (Exception e) {
				e.printStackTrace();
				try {
					if (conn.isClosed()) {
						// 需要重连
						return null;
					} else {
						log.error(sqlString);
						System.exit(1);
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				log.error(e.getMessage());
			}
		}
		return new MysqlSelect(flag, statement, result);
	}

	/**
	 * 带参数访问
	 * 
	 * @param sql
	 * @return
	 */
	public java.sql.PreparedStatement getPS(String sql) {
		reConnection();
		java.sql.PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ps;
	}

	public void runPS(java.sql.PreparedStatement ps, boolean isPrint) {
		reConnection();
		try {
			if (isPrint) {
				log.info(ps.toString());
			}
			ps.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			ps.clearBatch();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// /**
	// * 插入
	// *
	// * @param sqlString
	// * @return
	// * @throws SQLException
	// */
	// public boolean sqlInsert(String sqlString,boolean isPrint, boolean
	// isTest, ETL etl, Long id) {
	// boolean flag = false;
	// Statement statement = null;
	// // statement用来执行SQL语句
	// try {
	// if (isTest) {
	// log.info(sqlString);
	// return true;
	// } else {
	// if(isPrint)
	// {
	// log.info(sqlString);
	// }
	// statement = conn.createStatement();
	// statement.executeUpdate(sqlString);
	// conn.commit();
	// }
	// flag = true;
	// } catch (Exception e) {
	// e.printStackTrace();
	// log.error(sqlString);
	// etl.addMysqlErrorCount(id, 1L);
	// // System.exit(1);
	// }
	// return flag;
	// }

	/**
	 * 批量插入
	 * 
	 * @param sqlString
	 * @return
	 */
	public boolean sqlInsertBatch(LinkedList<String> sqlString) {
		reConnection();
		boolean flag = false;
		Statement statement = null;
		try {
			// statement用来执行SQL语句
			statement = conn.createStatement();
			for (String str : sqlString) {
				statement.addBatch(str);
			}
			statement.executeBatch();
			statement.close();
			conn.commit();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			System.exit(1);
		}
		return flag;
	}

	// /**
	// * 批量插入并删除
	// * @param isPrint 是否打印
	// * @param isTest 是否为测试程序 如果是则不入库
	// * @param sqlString
	// * @return
	// */
	// public boolean sqlInsertBatchPoll(LinkedList<String> sqlString,
	// boolean isPrint,boolean isTest, ETL etl, Long id) {
	// boolean flag = false;
	// Statement statement = null;
	// LinkedList<String> temp = new LinkedList<String>();
	// for (String str : sqlString) {
	// temp.add(str);
	// }
	// try {
	// // statement用来执行SQL语句
	// statement = conn.createStatement();
	// while (sqlString.size() > 0) {
	// String te = sqlString.pollFirst();
	// if (isTest) {
	// if(isPrint)
	// {
	// log.info(te);
	// }
	// continue;
	// } else {
	// if(isPrint)
	// {
	// log.info(te);
	// }
	// }
	// statement.addBatch(te);
	// }
	// if (isTest) {
	// return true;
	// }
	// statement.executeBatch();
	// statement.close();
	// conn.commit();
	// flag = true;
	// } catch (Exception e) {
	// e.printStackTrace();
	// log.error(e.getMessage());
	// // System.exit(1);
	// try {
	// conn.rollback();
	// } catch (SQLException e1) {
	// // TODO Auto-generated catch block
	// e1.printStackTrace();
	// }
	// while (temp.size() > 0) {
	// String str=null;
	// try {
	// str= temp.pollFirst();
	// // System.out.println(str);
	// sqlInsert(str,isPrint,isTest, etl, id);
	// } catch (Exception e2) {
	// log.error(str);
	// e2.printStackTrace();
	// etl.addMysqlErrorCount(id, 1L);
	// // System.exit(1);
	// }
	// }
	//
	// // System.exit(1);
	// }
	// return flag;
	// }

	/**
	 * 更新
	 * 
	 * @param sqlString
	 * @return
	 */
	public boolean sqlUpdate(String sqlString) {
		reConnection();
		boolean flag = false;
		Statement statement = null;
		try {
			// statement用来执行SQL语句

			statement = conn.createStatement();
			statement.executeUpdate(sqlString);
			statement.close();
			conn.commit();
			flag = true;
		} catch (Exception e) {
			// log.error(e.getMessage());
			e.printStackTrace();
		}

		// log.info("执行完成");
		return flag;
	}

	/**
	 * 更新 更新并且获取最后一个 id
	 * 
	 * @param sqlString
	 * @return
	 */
	public long sqlUpdate(String sqlString, String table) {
		reConnection();
		Statement statement = null;
		try {
			// statement用来执行SQL语句

			statement = conn.createStatement();
			statement.executeUpdate(sqlString);
			statement.close();
			conn.commit();
		} catch (Exception e) {
			// log.error(e.getMessage());
			e.printStackTrace();
		}

		// log.info("执行完成");
		return getLastId(table);
	}

	/**
	 * 在一个session中获取最大的id
	 * 
	 * @param tableName
	 * @return
	 */
	public long getLastId(String tableName) {
		boolean flag=reConnection();
		if(flag)
		{
			return 0L;
		}
		ResultSet result = sqlSelect("SELECT LAST_INSERT_ID() from "
				+ tableName + "  limit 1").resultSet;
		try {
			if (result.next()) {
				long lastId = result.getLong(1);
				return lastId;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		return -1L;
	}

	/**
	 * 批量更新
	 * 
	 * @param sqlString
	 * @return
	 */
	public boolean sqlUpdateBatch(LinkedList<String> sqlString) {
		reConnection();
		boolean flag = false;
		Statement statement = null;
		try {
			// statement用来执行SQL语句
			statement = conn.createStatement();
			for (String str : sqlString) {
				if (str == null || str.equals("")) {
				} else {
					statement.addBatch(str);
				}
			}
			statement.executeBatch();
			statement.close();
			conn.commit();
			flag = true;
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				log.error(e.getMessage());
				log.info("roolback执行错误");
			}
			log.error("已执行roolback");
			log.error("", e);
		}
		// log.info("执行完成");
		return flag;
	}

	/**
	 * 设置通用批量insert方法
	 * 
	 * @param str
	 * @return
	 */
	public PreparedStatement setPreparedStatement(String str) {
		reConnection();
		PreparedStatement pst = null;
		try {
			pst = (PreparedStatement) conn.prepareStatement(str);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pst;
	}

	/**
	 * 执行通用批量方法
	 * 
	 * @param pst
	 */
	public void runPreparedStatement(PreparedStatement pst) {
		reConnection();
		try {
			pst.executeBatch();
			conn.commit();
			// conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * 关闭操作
	 * 
	 * @return
	 */
	public boolean close() {
		if (conn == null) {

		} else {
			try {
				conn.close();
			} catch (Exception e) {
				log.error(e.getMessage());
				return false;
			}
		}
		log.info("数据库连接关闭");
		return true;

	}

	/**
	 * 提交操作
	 * 
	 * @return
	 */
	public boolean commit() {

		try {
			conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
			return false;
		}
		return true;

	}

	public boolean CallPurduce(String sql) {
		reConnection();
		// log.info(sql);
		CallableStatement st = null;
		boolean flag = false;
		try {
			// java中利用循环，即可获取结果际数据
			st = conn.prepareCall(sql);
			st.execute();
			conn.commit();
			flag = true;
		} catch (SQLException e) {

			// TODO Auto-generated catch block

			log.error(e.getMessage());

		} finally {
			try {
				if (st != null)
					st.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return flag;
	}

	public boolean CallPurduce(String sql, String date) {
		reConnection();
		// log.info(sql + "\tvalue:" + date);
		CallableStatement st = null;
		boolean flag = false;
		try {
			// java中利用循环，即可获取结果际数据
			st = conn.prepareCall(sql);
			st.setString(1, date);
			st.execute();
			conn.commit();
			flag = true;
		} catch (SQLException e) {

			// TODO Auto-generated catch block

			log.error(e.getMessage());

		} finally {
			try {
				if (st != null)
					st.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return flag;
	}

	public static void main(String[] args) throws SQLException {
		MysqlConnection mysql=new MysqlConnection( "jdbc:mysql://123.57.141.237:3306/fangcheng_global", "fangcheng_user",
				 "fc1234");
		ResultSet cursor=mysql.sqlSelect("select * from brand limit 10").resultSet;
		while(cursor.next()){
			System.out.println(cursor.getString(1));
		}
		// PropertyConfigurator.configure("./log4j.properties");
		// MysqlConnection mysql = new MysqlConnection(
		// "jdbc:mysql://192.168.2.243:3306/Uni2uni_Purchase", "root",
		// "root");
		// boolean flag = mysql
		// .sqlInsert("insert into UserPurchaseOrder(userid,ordername,purchasePrice,saveTime) values(10,'t4oit',33.46,now())");
		// if (flag) {
		// System.out.println("查询");
		// mysql.sqlSelect("select * from UserPurchaseOrder");
		// } else {
		// System.out.println("插入错误");
		// }
		// MysqlConnection mysql = new MysqlConnection(
		// "jdbc:mysql://192.168.85.11:3306/zjMysql", "root",
		// "root");
	}
}
// /**
// *
// * Title.mysql 连接类 <br>
// * Description.
// * <p>
// * Copyright: Copyright (c) 2014-6-26 上午11:51:04
// * <p>
// * Company: 联嘉云贸易有限公司
// * <p>
// * Author: tianlie@uni2uni-js.com
// * <p>
// * Version: 1.0
// * <p>
// */
// class MysqlSelect {
// public boolean flag = false;
// public Statement statement = null;
// public ResultSet resultSet = null;
//
// public MysqlSelect(){
//
// }
// public MysqlSelect(boolean flag, Statement statement, ResultSet resultSet) {
// this.flag = flag;
// this.statement = statement;
// this.resultSet = resultSet;
// }
// }