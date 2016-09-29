package ps.hell.util.sql.parse2;

import java.util.ArrayList;

import ps.hell.util.sql.parse2.enum2.SqlCategoryEnum;
import ps.hell.util.sql.parse2.enum2.TableLinkedEnum;
import ps.hell.util.sql.parse2.select2.TableSelect;


public class ArrayTableNode {

	/**
	 * 所有使用的表
	 */
	public ArrayList<TableNode> nodes = new ArrayList<TableNode>();
	public int deep = 1;
	/**
	 * 别名
	 */
	public String alais = null;
	/**
	 * 是否包含括号
	 */
	public boolean contationK = false;

	/**
	 * 和 nodes 二选一 所使用的表的信息
	 */
	public TableSelect table = null;

	// public TableNode table=null;

	public ArrayTableNode(int deep) {
		this.deep = deep;
	}

	/**
	 * @param inputSql
	 *            对应的一个单独的sql
	 * @param linkeds
	 *            这个和上一个对应的sql
	 */
	public ArrayTableNode(int deep, String input, String alais,String databaseField) {
		this.deep = deep;
		input = input.trim();
		if (input.length() > 0) {
			this.alais = alais;
			System.out.println("ArryNode:" + input + "\t--" + alais);
			parse(input,databaseField);
		}
	}

	// /**
	// *
	// * @param input 其中内部包含别名
	// */
	// public ArrayTableNode(String input){
	// char[] chas=input.toCharArray();
	//
	// parse(input);
	// }

	/**
	 * 用于解析
	 * 
	 * @param input
	 */
	public void parse(String input,String databaseField) {
		if (input.startsWith("select ")) {
			this.nodes.add(new TableNode(this.deep, input,databaseField));
			return;
		}
		// 正常解析器
		char[] chars = input.toCharArray();
		int count = 0;
		TableLinkedEnum beforeSource = null;
		TableLinkedEnum before = null;
		StringBuffer sb = new StringBuffer();
		StringBuffer sbAlais = new StringBuffer();
		StringBuffer on = new StringBuffer();
		TableLinkedEnum[] linkedEnum = TableLinkedEnum.values();
		// SqlLinkedEnum[] sqlLinkedEnum=SqlLinkedEnum.values();
		boolean isAlais = false;
		boolean isOn = false;
		for (int i = 0, len = chars.length; i < len; i++) {
			char cha = chars[i];
			if (cha == '(' && !isOn) {
				count++;
				if (count > 1) {
					sb.append(cha);
				}
			} else if (cha == ')' && !isOn) {
				count--;
				if (count > 0) {
					sb.append(cha);
				}
			} else {
				if (count > 0) {
					sb.append(cha);
					continue;
				}
				if ((cha == ' ' || cha == ',')&&!isOn) {

					if (i + 4 > len) {
						sb.append(cha);
						continue;
					}
					String temp = input.substring(i);
					if (temp.startsWith(" on ")) {
						isOn = true;
						isAlais = false;
						i += 3;
						continue;
					}
					// System.out.println("temp:"+temp);
					if (temp.startsWith(" as ")) {
						isAlais = true;
						i += 3;
						continue;
					}
					boolean isNext = false;
					for (TableLinkedEnum linked : linkedEnum) {
						if (linked.getKey().length() <= 0) {
							continue;
						}
						int len2 = linked.getKey().length() + 2;
						if (i + len2 > len) {
							continue;
						}
						if (len2 == 3) {
							len2 = 1;
							if (cha == ',') {
								if (linked.getKey().equals(
										input.substring(i, i + 1))) {
									isNext = true;
									before = linked.DOT;
//									i=i+1;
									break;
								}
							}
						} else {
							String temp2 = input.substring(i, i + len2);

							if (temp2.equals(" " + linked.getKey() + " ")) {
								isNext = true;
								if (before == null) {
									before = linked;
								} else {
									if (before.getKey().length() < linked
											.getKey().length()) {
										before = linked;
									}
								}
							}
						}
					}
					if (isNext) {
						if (before.equals(TableLinkedEnum.UNION)
								|| before.equals(TableLinkedEnum.UNION_ALL)) {
							contationK = true;
						} else {
							isOn = true;
						}
						if(before.equals(TableLinkedEnum.DOT)){
							
						}else{
							i += before.getKey().length() + 1;
						}
					}
					if (isNext) {
						System.out.println("新:::\t" + beforeSource + "\t"
								+ sb.toString() + "--\t" + sbAlais.toString()
								+ "\ton:" + on.toString());
						if (beforeSource == null) {
							beforeSource = TableLinkedEnum.NULL;
						}
						TableNode tableTemp=new TableNode(this.deep, beforeSource, sb.toString(),
								sbAlais.toString(),databaseField);
								nodes.add(tableTemp);
								tableTemp.initOn(on.toString(),this);
						beforeSource = before;
						sb = new StringBuffer();
						sbAlais = new StringBuffer();
						isAlais = false;
						on = new StringBuffer();
						isOn = false;
					} else if (isOn) {
						on.append(cha);
					} else {
						sb.append(cha);
					}
				}
				else {
					if (isAlais) {
						sbAlais.append(cha);
					} else if (isOn) {
						on.append(cha);
					} else {
						sb.append(cha);
					}
				}
			}
		}
		if (sb.toString().length() > 0) {
			if (nodes.size() > 0) {
				System.out.println("end:::\t" + beforeSource + "\t::"
						+ sb.toString() + "--\t" + sbAlais.toString() + "\ton:"
						+ on.toString());
				TableNode tableTemp=new TableNode(this.deep, beforeSource, sb.toString(),
						sbAlais.toString(),databaseField);
						nodes.add(tableTemp);
						tableTemp.initOn(on.toString(),this);
			} else {
				String az = sb.toString();
				if (az.contains(SqlCategoryEnum.from.getKey())) {
					TableNode tableTemp=new TableNode(this.deep, beforeSource, sb.toString(),
							sbAlais.toString(),databaseField);
							nodes.add(tableTemp);
							tableTemp.initOn(on.toString(),this);
				} else {
					System.out.println("全:::" + sb.toString() + "\t::"
							+ sbAlais.toString() + "\ton:" + on.toString());
					table = new TableSelect(sb.toString());
					// table=new TableNode(null,sb.toString(),null);
					if (sbAlais.length() > 0) {
						this.alais = sbAlais.toString();
					}
				}
			}
		}
	}

	public String toTransIdString() {
		StringBuffer sb = new StringBuffer();
		String temp = FormatStringGet.getT(deep);
		String temp2 = FormatStringGet.getT(deep + 1);
		String temp3 = FormatStringGet.getT(deep + 2);
		boolean isFirst = false;
		if (alais != null && this.table == null) {
			if (isFirst) {
				sb.append("\n");
			}
			isFirst = true;
			sb.append(temp + "( ");
		}
		if (table == null) {
			for (TableNode node : nodes) {
				if (node.before == null && node.column != null) {
					sb.append("\n" + temp3);
					sb.append("( ");
				}
				// System.out.println("node:"+node.toString());
				sb.append(node.toTransIdString());
				if (node.before == null && node.column != null) {
					sb.append("\n" + temp3);
					sb.append(" )");
				}
			}
		} else {
			if (isFirst) {
				sb.append("\n");
			}
			isFirst = true;
			sb.append(temp2 + table.toTransIdString());
		}
		if (alais != null && this.table == null) {
			if (isFirst) {
				sb.append("\n");
			}
			isFirst = true;
			sb.append(temp2 + " )");
		}
		if (alais != null) {
			sb.append(" as ").append(alais);
		}
		return sb.toString();
	}
	
	
	public String toTransString() {
		StringBuffer sb = new StringBuffer();
		String temp = FormatStringGet.getT(deep);
		String temp2 = FormatStringGet.getT(deep + 1);
		String temp3 = FormatStringGet.getT(deep + 2);
		boolean isFirst = false;
		if (alais != null && this.table == null) {
			if (isFirst) {
				sb.append("\n");
			}
			isFirst = true;
			sb.append(temp + "( ");
		}
		if (table == null) {
			for (TableNode node : nodes) {
				if (node.before == null && node.column != null) {
					sb.append("\n" + temp3);
					sb.append("( ");
				}
				// System.out.println("node:"+node.toString());
				sb.append(node.toTransString());
				if (node.before == null && node.column != null) {
					sb.append("\n" + temp3);
					sb.append(" )");
				}
			}
		} else {
			if (isFirst) {
				sb.append("\n");
			}
			isFirst = true;
			sb.append(temp2 + table.toTransString());
		}
		if (alais != null && this.table == null) {
			if (isFirst) {
				sb.append("\n");
			}
			isFirst = true;
			sb.append(temp2 + " )");
		}
		if (alais != null) {
			sb.append(" as ").append(alais);
		}
		return sb.toString();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		String temp = FormatStringGet.getT(deep);
		String temp2 = FormatStringGet.getT(deep + 1);
		String temp3 = FormatStringGet.getT(deep + 2);
		boolean isFirst = false;
		if (alais != null && this.table == null) {
			if (isFirst) {
				sb.append("\n");
			}
			isFirst = true;
			sb.append(temp + "( ");
		}
		if (table == null) {
			for (TableNode node : nodes) {
				if (node.before == null && node.column != null) {
					sb.append("\n" + temp3);
					sb.append("( ");
				}
				// System.out.println("node:"+node.toString());
				sb.append(node.toString());
				if (node.before == null && node.column != null) {
					sb.append("\n" + temp3);
					sb.append(" )");
				}
			}
		} else {
			if (isFirst) {
				sb.append("\n");
			}
			isFirst = true;
			sb.append(temp2 + table.toString());
		}
		if (alais != null && this.table == null) {
			if (isFirst) {
				sb.append("\n");
			}
			isFirst = true;
			sb.append(temp2 + " )");
		}
		if (alais != null) {
			sb.append(" as ").append(alais);
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		String input = "(select * from table1 as c) as ab,table2 as b left join table3 as c";
		// input = "select e.* from table1 as e";
		// input="(  select e.* from table1 as e  ) as c,table2 as b";
		// input="(select * from table1 as c) as ab";
		// input="table1 as c,tabl2 as b";
		input = "table1 as a left join table as b on a.id=b.id";
		input = "(  select a.user_id as e3,a.uid,sum(1) as c from  (select * from dwd.dwd_user_base_full ) as e,dwd.dwd_user_base_full as a left join dwd.dwd_test as b on a.id=b.id and b.status in ('a','b') where (b.user!=0 and b.user=1) or b.user!=2 group by a.user_id,a.uid  )  as e2   left join dwd.dwd_user_base_full as e3 on e2.id=e3.user_id";
		input = "(    select * from dwd.dwd_user_base_full  ) as e,dwd.dwd_user_base_full as a left join dwd.dwd_test as b on a.id=b.id and b.status in 'a','b'";
		input ="(select * from dwd.dwd_user_base_full ) as e,dwd.dwd_user_base_full as a left join dwd.dwd_test as b on a.id=b.id and b.status in ('a','b')";
		ArrayTableNode node = new ArrayTableNode(1, input, "test","dwd");
		System.out.println(node.toString());

	}
}
