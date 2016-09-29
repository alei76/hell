package ps.hell.util.sql.parse2;

import java.util.ArrayList;

import ps.hell.util.sql.parse2.select2.ColumnSelect;


public class GroupByNode {
	
	public String groupby=null;
	
	public ArrayList<ColumnSelect> group=new ArrayList<ColumnSelect>();
	
	public GroupByNode(ArrayTableNode table,String input){
		System.out.println("groupby:"+input);
		this.groupby=input;
		String[] strs=input.split(",");
		for(String str:strs){
			group.add(new ColumnSelect(table,str));
		}
	}
	
	public String toTransIdString(){
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<group.size();i++){
			if(i>0){
				sb.append(",");
			}
			sb.append(group.get(i).toTransIdString());
		}
		return sb.toString();
	}
	
	public String toTransString(){
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<group.size();i++){
			if(i>0){
				sb.append(",");
			}
			sb.append(group.get(i).toTransString());
		}
		return sb.toString();
	}
	
	public String toString(){
		return groupby;
	}
}
