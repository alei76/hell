package ps.hell.util.sql.parse2.enum2;


public enum TableLinkedEnum2 {
	NULL(""),UNION_ALL("union all"),UNION("union");
	
	public String key=null;
	
	private TableLinkedEnum2(String type){
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
