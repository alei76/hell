package ps.hell.util.db2;

import java.sql.ResultSet;
import java.sql.Statement;

/**
 * 
 * Title.mysql 连接类 <br>
 * <p>
 */
 public class MysqlSelect {
	public boolean flag = false;
	public Statement statement = null;
	public ResultSet resultSet = null;

	public MysqlSelect(){
		
	}
	public MysqlSelect(boolean flag, Statement statement, ResultSet resultSet) {
		this.flag = flag;
		this.statement = statement;
		this.resultSet = resultSet;
	}
 }