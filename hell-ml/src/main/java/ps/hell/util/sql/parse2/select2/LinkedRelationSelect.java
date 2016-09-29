package ps.hell.util.sql.parse2.select2;

import ps.hell.util.sql.parse2.enum2.LinkedEnum;



public class LinkedRelationSelect {

	public LinkedEnum linked = null;

	/**
	 * 初始化
	 * 
	 * @param linked
	 */
	public LinkedRelationSelect(String linked) {
		LinkedEnum[] values = LinkedEnum.values();
		boolean flag=false;
		for (LinkedEnum val : values) {
			if (linked.equals(val.getKey())) {
				this.linked = val;
				flag=true;
				break;
			}
		}
		if(!flag){
			System.err.println("错误的 linkedEnum:"+linked);
		}
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append(" ").append(linked.key).append(" ");
		return sb.toString();
	}
}
