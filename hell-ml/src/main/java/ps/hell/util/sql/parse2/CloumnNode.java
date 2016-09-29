package ps.hell.util.sql.parse2;

import java.util.ArrayList;

import ps.hell.util.sql.parse2.select2.ColumnSelect;


public class CloumnNode {

	/**
	 * columns
	 */
	public ArrayList<ColumnSelect> columns=new ArrayList<ColumnSelect>();
	
	private String temp[]=null;
	
	public CloumnNode(String input){
		this.temp=input.split(",");
	}
	
	public void init(ArrayTableNode node){
		if(this.temp!=null){
			for(String tem:temp){
				columns.add(new ColumnSelect(node,tem));
			}
		}
	}
	
	
	public String toString(){
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<columns.size();i++){
			if(i>0){
				sb.append(",");
			}
			sb.append(columns.get(i));
		}
		return sb.toString();
	}
	
	public String toTransIdString(){
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<columns.size();i++){
			if(i>0){
				sb.append(",");
			}
			sb.append(columns.get(i).toTransIdString());
		}
		return sb.toString();
	}
	
	public String toTransString(){
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<columns.size();i++){
			if(i>0){
				sb.append(",");
			}
			sb.append(columns.get(i).toTransString());
		}
		return sb.toString();
	}
}
