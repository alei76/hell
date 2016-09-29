package ps.hell.util.sql.parse2.select2;

import java.util.ArrayList;

import ps.hell.util.sql.parse2.ArrayTableNode;
import ps.hell.util.sql.parse2.TableNode;
import ps.hell.util.sql.parse2.enum2.SelectFunctionEnum;



public class WhereSelect {
	/**
	 * 左侧
	 */
	public ColumnSelect left=null;
	/**
	 * 条件
	 */
	public ConditionSelect condition=null;
	/**
	 * 右侧
	 */
	public ColumnSelect right=null;
	
	public TableNode rightNode=null;
	public WhereSelect(ArrayTableNode tables,String input){
		System.out.println("whereSelect:"+input);
		fill(tables,input);
	}
	
	public WhereSelect(ArrayList<TableNode> tables,String input){
		System.out.println("whereSelect:"+input);
		fill(tables,input);
	}
	
	public void fill(ArrayTableNode tables,String input){
//		String[] str=input.split(split);
		ColumnTempClass val=getSplit(input);
		condition=new ConditionSelect(val.split);
		left=new ColumnSelect(tables,val.columns[0].trim());
		if(val.split.contains("exists")){
			
		}else if(val.split.contains(" not in ")){
			right=new ColumnSelect(tables,val.columns[1],false);
		}else if(val.split.contains(" in ")){
			right=new ColumnSelect(tables,val.columns[1],false);
		}
		else{
			right=new ColumnSelect(tables,val.columns[1].trim());
		}
	}
	
	public void fill(ArrayList<TableNode> tables,String input){
//		String[] str=input.split(split);
		ColumnTempClass val=getSplit(input);
		condition=new ConditionSelect(val.split);
		left=new ColumnSelect(tables,val.columns[0].trim());
		if(val.split.contains("exists")){
			
		}else if(val.split.contains("in")){
			right=new ColumnSelect(tables,val.columns[1].trim());
		}else{
			right=new ColumnSelect(tables,val.columns[1].trim());
		}
	}
	
	public class ColumnTempClass{
		/**
		 * 实际的左右两侧
		 */
		public String[] columns=new String[2];
		/**
		 * 实际的分隔符
		 */
		public String split=null;
		public String[] getColumns() {
			return columns;
		}
		public void setColumns(String[] columns) {
			this.columns = columns;
		}
		public String getSplit() {
			return split;
		}
		public void setSplit(String split) {
			this.split = split;
		}
		
		
	}
	
	public ColumnTempClass getSplit(String input){
		ColumnTempClass re=new ColumnTempClass();
		StringBuffer sb=new StringBuffer();
		int index=0;
		char[] chars=input.toCharArray();
		boolean isDot=false;
		int funcCount=0;
		int lcount=0;
		SelectFunctionEnum[] vals=SelectFunctionEnum.values();
		for(int i=0;i<chars.length;i++){
			if(index>0){
				re.columns[index]=input.substring(i);
				break;
			}
			char cha=chars[i];
			
			if (cha == '\\') {
				lcount++;
			} else {
				lcount = 0;
			}
			if (cha == '\'') {
				if (lcount % 2 ==0) {
					// 如果为单个
					if (isDot) {
						isDot = false;
					} else {
						isDot = true;
					}
				}
				sb.append(cha);
				continue;
			}
			if(cha =='('){
				funcCount++;
				sb.append(cha);
				continue;
			}else if(cha==')'){
				funcCount--;
				sb.append(cha);
				continue;
			}
			if(funcCount>0){
				sb.append(cha);
			}
		
			String temp=input.substring(i);
			boolean flag=false;
			for(SelectFunctionEnum enu:vals){
			if(temp.startsWith(enu.getKey())){
				re.split=enu.getKey();
				re.columns[index]=sb.toString();
				sb=new StringBuffer();
				index++;
				i+=enu.getKey().length()-1;
				flag=true;
				break;
			}
			}
			if(flag){
				continue;
			}
			sb.append(cha);
		}
		if(sb.length()>0){
			re.columns[index]=sb.toString();
		}
		
		return re;
	}
	
	
	public String toTransIdString(){
		StringBuffer sb=new StringBuffer();
		sb.append(left.toTransIdString()).append(" ");
		sb.append(condition.toString()).append(" ");
		sb.append(right.toTransIdString()).append(" ");
		return sb.toString();
	}
	
	public String toTransString(){
		StringBuffer sb=new StringBuffer();
		sb.append(left.toTransString()).append(" ");
		sb.append(condition.toString()).append(" ");
		sb.append(right.toTransString()).append(" ");
		return sb.toString();
	}
	
	public String toString(){
		StringBuffer sb=new StringBuffer();
		sb.append(left.toString()).append(" ");
		sb.append(condition.toString()).append(" ");
		sb.append(right.toString()).append(" ");
		return sb.toString();
	}
	
	public static void main(String[] args) {
//		String input="'%asdf(sd)asd%'";
//		WhereSelect select=new WhereSelect(null,input);
	}
}
