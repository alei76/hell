package ps.hell.util.sql.parse2.enum2;

public enum SqlSortEnum {

	DESC("desc"),ASC("asc");
	
	public String key=null;
	
	private SqlSortEnum(String type){
		this.key=type;
	}
	
	public String getKey(){
		return key;
	}
	
	public static void main(String[] args) {
		System.out.println(SqlSortEnum.DESC);
	}
}
