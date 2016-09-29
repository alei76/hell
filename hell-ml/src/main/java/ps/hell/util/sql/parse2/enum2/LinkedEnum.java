package ps.hell.util.sql.parse2.enum2;

public enum LinkedEnum {

	
	AND("and"),OR("or");
	
	public String key=null;
	
	private LinkedEnum(String type){
		this.key=type;
	}
	
	public String getKey(){
		return key;
	}
	
	public static void main(String[] args) {
		System.out.println(LinkedEnum.AND);
	}
}
