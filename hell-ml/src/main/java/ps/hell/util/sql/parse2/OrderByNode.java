package ps.hell.util.sql.parse2;

import java.util.ArrayList;

import ps.hell.util.sql.parse2.select2.ColumnSelect;



public class OrderByNode {

	public String orderby = null;

	public ArrayList<ColumnSelect> order = new ArrayList<ColumnSelect>();

	public OrderByNode(ArrayTableNode table, String input) {
		// System.out.println("order:"+input);
		this.orderby = input;
		String[] strs = input.split(",");
		for (String str : strs) {
			order.add(new ColumnSelect(table, str));
		}
	}
	
	public String toString(){
		return orderby;
	}

	public String toTransIdString() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < order.size(); i++) {
			if (i > 0) {
				sb.append(",");
			}
			sb.append(order.get(i).toTransIdString());
		}
		return sb.toString();
	}

	public String toTransString() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < order.size(); i++) {
			if (i > 0) {
				sb.append(",");
			}
			sb.append(order.get(i).toTransString());
		}
		return sb.toString();
	}

}
