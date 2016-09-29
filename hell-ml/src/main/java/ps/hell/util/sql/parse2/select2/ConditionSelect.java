package ps.hell.util.sql.parse2.select2;

public class ConditionSelect {

	/**
	 * 对应的条件
	 */
	String condition=null;
	
	public ConditionSelect(String condition){
		this.condition=condition;
	}
	
	public String toString(){
		return condition;
	}
}
