package ps.hell.util.sql.parse2;

import java.util.ArrayList;

public class HavingNode {

	
	public String having=null;
	
	public WhereNode hav=null;
	
	public HavingNode(ArrayTableNode node,String input,int deepFrom){
//		System.out.println("having:"+input);
		this.having=input;
		hav=new WhereNode(node,input,1,deepFrom);
	}
	
	public HavingNode(ArrayList<TableNode> talbes,String input,int deepFrom){
		this.having=input;
		hav=new WhereNode(talbes,input,1,deepFrom);
	}
	
	public String toTransIdString(){
		return hav.toTransIdString();
	}
	
	public String toTransString(){
		return hav.toTransString();
	}
	
	public String toString(){
		return having;
	}
}
