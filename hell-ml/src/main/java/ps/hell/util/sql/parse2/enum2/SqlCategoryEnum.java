package ps.hell.util.sql.parse2.enum2;

public enum SqlCategoryEnum {
	select("select ",0),from("from ",1),where("where ",2),orderby("order by ",3),
	groupby("group by ",4),limit("limit ",5),having("having ",6),
	on("on ",7);
	
	public String key;
	public int val;
	private SqlCategoryEnum(String type,int val){
		this.key=type;
		this.val=val;
	}
	
	public String getKey(){
		return key;
	}
	public int getVal(){
		return val;
	}
}
