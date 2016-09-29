package ps.hell.util.sql.parse2.enum2;

public enum SelectFunctionEnum {

	GT(">="),LT("<="),NT("!="),G(">"),L("<"),ET("="),NOT_IN(" not in "),IN(" in "),NOT_EXISTS(" not exists"),
	EXISTS(" exists "),IS_NOT(" is not "),IS(" is ");
	
	public String key;
	
	private SelectFunctionEnum(String type){
		this.key=type;
	}
	
	public String getKey(){
		return key;
	}
	
	public static void main(String[] args) {
		System.out.println(LinkedEnum.AND);
	}
}
