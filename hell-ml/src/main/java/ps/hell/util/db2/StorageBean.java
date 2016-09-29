package ps.hell.util.db2;

import java.util.ArrayList;
import java.util.List;

public class StorageBean {
	List<String> mysqlKey=new ArrayList<String>();
	
	List<List<Object>> mysqlValue=null;
	List<List<Object>> mongoValue=new ArrayList<List<Object>>();
	
	public StorageBean(List<Object> value)
	{
		this.mysqlValue=new ArrayList<List<Object>>();
		this.mysqlValue.add(value);
	}
}
