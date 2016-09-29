package ps.hell.util.db2;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

import org.apache.log4j.Logger;


public class MysqlTableInfo {

	public static Logger logger = Logger.getLogger(MysqlTableInfo.class);
	/**
	 * 查询sql
	 */
	public static String sql = "select table_schema,table_name,column_name,data_type,character_maximum_length "
			+ "from information_schema.columns " + "where table_schema =\"";
	public static String sql2 = "\" order by table_schema,table_name";

	/**
	 * 获取表数据的字段名
	 * @param conn
	 * @param tableName
	 * @parm clearn 不需要添加的内容
	 * @param map
	 * @param columns
	 *            一个空的 返回为对应的顺序
	 * @return
	 * @throws SQLException
	 */
	public static String getTableColumns(MysqlConnection conn,
			HashSet<String> clearn, String dataName, String tableName, Map map,Map mapLength,
			ArrayList<String> columns) throws SQLException {
		// 需要重置map到全部小写
		if (clearn != null) {
			ArrayList<String> inclearn = new ArrayList<String>();
			for (String val : clearn) {
				inclearn.add(val);
			}
			for (String val : inclearn) {
				clearn.remove(val);
			}
			for (String val : inclearn) {
				clearn.add(val.toLowerCase());
			}
		}
//		 logger.info(sql + dataName
//		+ "\" and table_name=\"" + tableName + sql2);
		// System.exit(1);
		ResultSet select = conn.sqlSelect(sql + dataName
				+ "\" and table_name=\"" + tableName + sql2).resultSet;
		StringBuffer result = new StringBuffer("(");
		while (select.next()) {
			String column_name = select.getString(3).toLowerCase();
			String data_type = select.getString(4);
			// logger.info(tableName+":表字段类型:"+column_name+":"+data_type);
			if(data_type.equals("varchar")||data_type.equals("text"))
			{
				//System.out.println(column_name);
				mapLength.put(column_name,Integer.parseInt(select.getString(5)));
			}
			
			if (clearn != null) {
				if (clearn.contains(column_name)) {
					// 则不添加
					continue;
				}
			}
			// 如果包含小写
			columns.add(column_name);
			if (columns.size() == 1) {
				result.append(column_name);
			} else {
				result.append(',').append(column_name);
			}
			map.put(column_name, MysqlFormatStatic.map.get(data_type));
		}
		return result.append(")").toString();
	}
}
