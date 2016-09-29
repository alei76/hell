package ps.hell.util.db2;

import java.util.Date;
import java.util.HashMap;

public class MysqlFormatStatic {

	public static HashMap<String,Object> map=new HashMap<String,Object>();
	static{
		map.put("bigint",new Long(1));
		map.put("varchar",new String());
		map.put("int",new Integer(1));
		map.put("timestamp",new Date());
		map.put("text",new String(""));
		map.put("float",new Float(1f));
		map.put("double",new Double(1d));
		map.put("date",new Date());
		map.put("char",new String());
		map.put("decimal",new Double(1d));
	}
}
