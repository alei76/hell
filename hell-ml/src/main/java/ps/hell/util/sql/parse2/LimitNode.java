package ps.hell.util.sql.parse2;

public class LimitNode {

	public String limit=null;
	
	public LimitNode(String input){
		System.out.println("limit:"+input);
		this.limit=input;
	}
	
	public String toString(){
		return limit;
	}
}
