package ps.hell.util.sql.parse2.enum2;

public enum SqlLinkedEnum {

	
	UNION_ALL("union all"),UNION("union");

	public String key=null;
	
	private SqlLinkedEnum(String type){
		key=type;
	}
	public String getKey(){
		return key;
	}
}
