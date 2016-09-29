package ps.hell.util.sql.parse2.enum2;

public enum TableLinkedEnum {
	NULL(""),
	DOT(","),
	LEFT_JOIN("left join"),LEFT_OUTER_JOIN("left outer join"),
	RIGHT_JOIN("right join"),RIGHT_OUTER_JOIN("right outer join"),
	LEFT_SEMI_JOIN("left semi join"),RIGHT_SEMI_JOIN("right semi join"),SEMI_JOIN("semi join"),
	FULL_JOIN("full join"),JOIN("join"),UNION_ALL("union all"),UNION("union");
	
	public String key=null;
	
	private TableLinkedEnum(String type){
		this.key=type;
	}
	
	public String getKey(){
		return key;
	}
	
	public static void main(String[] args) {
		TableLinkedEnum[] linkedEnum = TableLinkedEnum.values();
		for(TableLinkedEnum val:linkedEnum){
			System.out.println(val.getKey());
		}
	}
}
