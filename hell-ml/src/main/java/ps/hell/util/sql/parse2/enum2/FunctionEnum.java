package ps.hell.util.sql.parse2.enum2;

public enum FunctionEnum {
	MUTIPLAY('*'),MINSUE('-'),DIVIDE('/'),PLUS('+'),LEFTK('('),RIGHTK(')'),EQUAL('='),DY('>'),NOT('!'),XY('<'),
	DOT(',');
	
	public char key;
	
	private FunctionEnum(char type){
		this.key=type;
	}
	
	public char getKey(){
		return key;
	}
	
	public static void main(String[] args) {
		System.out.println(LinkedEnum.AND);
	}
}
