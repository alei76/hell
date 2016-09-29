package ps.hell.util.sql.parse2;

import java.util.ArrayList;

import ps.hell.util.sql.parse2.enum2.SqlCategoryEnum;
import ps.hell.util.sql.parse2.enum2.TableLinkedEnum;
import ps.hell.util.sql.parse2.enum2.TableLinkedEnum2;


public class TableNode {

	/**
	 * 所使用的table
	 */
	public ArrayTableNode tables = null;

	public ArrayList<TableNode> unionTables = null;

	/**
	 * 深度
	 */
	public int deep = 1;

	/**
	 * 选择的数据
	 */
	public CloumnNode column = null;

	/**
	 * on
	 */
	public WhereNode on = null;
	/**
	 * where
	 */
	public WhereNode where = null;
	/**
	 * group by
	 */
	public GroupByNode groupby = null;
	/**
	 * order by
	 */
	public OrderByNode orderby = null;
	/**
	 * limit
	 */
	public LimitNode limit = null;
	/**
	 * having
	 */
	public HavingNode having = null;
	/**
	 * 表之间的连接状态 包含 关联类型
	 */
	public TableLinkedEnum before = null;

	/**
	 * 表之间的连接状态 包含 关联类型
	 */
	public TableLinkedEnum2 beforeUnion = null;

	public String alias = null;

	public TableNode(int deep) {
		this.deep = deep;
	}

	/**
	 * 进入的是 table 或者子查询属性 或者为 table1,table2 left join table3 之类
	 * 
	 * @param before
	 * @param input
	 * @param alais
	 */
	public TableNode(int deep, TableLinkedEnum before, String input,
			String alais,String databaseField) {
		this.deep = deep;
		System.out.println("tableNode:" + input + "\talais:" + alais);
		input = input.trim();

		// 需要将对应字符串分割出来

		this.alias = alais;
		if (input.contains(" from ")) {
			// 截取
			init(input,databaseField);
		} else {
			if (input.trim().length() > 0) {
				tables = new ArrayTableNode(this.deep + 1, input, null,databaseField);
			}
		}
		if (before != null) {
			this.before = before;

		}
	}

	public void initOn(String onWhere, ArrayTableNode parentTableNode) {
		System.out.println("\ton:" + onWhere);
		if (onWhere.length() > 0) {
			if (this.tables != null) {
				this.on = new WhereNode(parentTableNode, onWhere, 1, this.deep);
			} else {
				this.on = new WhereNode(unionTables, onWhere, 1, this.deep);
			}
		}
	}

	public TableNode(int deep, String input,String databaseField) {
		this.deep = deep;
		input = input.trim();
		init(input,databaseField);
	}

	/**
	 * 初始化函数
	 * 
	 * @param input
	 */
	public void init(String input,String databaseField) {
		getParse(input,databaseField);
	}

	public void getParse(String input,String databaseField) {
		input = clearnK(input);
		// 首先获取 table 及对应的别名
		String temp = input;
		StringBuffer sb = new StringBuffer();
		boolean flag = false;
		SqlCategoryEnum status = null;
		SqlCategoryEnum statusBefore = null;
		boolean flag2 = false;
		TableLinkedEnum2 statusTable = null;
		TableLinkedEnum2 statusTableBefore = null;

		SqlCategoryEnum[] enumSqls = SqlCategoryEnum.values();
		TableLinkedEnum2[] tableLinked2 = TableLinkedEnum2.values();
		TableLinkedEnum[] tableLinked = TableLinkedEnum.values();
		StringBuffer alais = new StringBuffer();
		int countk = 0;

		int[] countKs = new int[enumSqls.length];
		int[] countFunc = new int[enumSqls.length];
		char beforeChar = '1';
		int count = 0;
		boolean isBoolean = false;
		boolean hasSelect = false;
		boolean hasUnion = false;
		boolean hasJoin = false;
		boolean hasFrom = false;
		boolean hasKh = false;
		// 如果为
		String unionTempAlais = null;
		boolean unionTempAlaisBoolean = false;
		while (true) {
			count++;
			int start = 0;
			if (temp.length() <= 0) {
				break;
			}
			if (temp.charAt(0) == '(' && beforeChar == ' ') {
				if (statusBefore != null) {
					countKs[statusBefore.val]++;
					if (!statusBefore.equals(SqlCategoryEnum.from)) {
						sb.append(temp.charAt(0));
						temp = temp.substring(1);
						continue;
					}
					if (statusBefore.equals(SqlCategoryEnum.from)) {
						if (countKs[statusBefore.val] > 1 || hasJoin) {
							sb.append(temp.charAt(0));
						} else {
							hasKh = true;
						}
					} else if (countKs[statusBefore.val] > 1) {
						sb.append(temp.charAt(0));
					}
				}
				beforeChar = temp.charAt(0);
				temp = temp.substring(1);
				continue;
			} else if (temp.charAt(0) == '(') {
				if (statusBefore != null) {
					// countFunc[statusBefore.val]++;
					countKs[statusBefore.val]++;
				}
				sb.append(temp.charAt(0));
				beforeChar = temp.charAt(0);
				temp = temp.substring(1);
				continue;
			} else if (temp.charAt(0) == ')') {
				if (statusBefore != null) {
					countKs[statusBefore.val]--;
					if (!statusBefore.equals(SqlCategoryEnum.from)) {
						sb.append(temp.charAt(0));
						temp = temp.substring(1);
						continue;
					}
					if (statusBefore.equals(SqlCategoryEnum.from)) {
						if (countKs[statusBefore.val] > 0 || hasJoin) {
							sb.append(temp.charAt(0));
							isBoolean = true;
						} else {
							isBoolean = true;
						}
					} else {
						if (countKs[statusBefore.val] > 0) {
							sb.append(temp.charAt(0));
						} else {
							isBoolean = true;
						}
					}
				}
				beforeChar = temp.charAt(0);
				temp = temp.substring(1);
				continue;
			}
			// else if (temp.charAt(0) == ')') {
			// if (statusBefore != null) {
			// countFunc[statusBefore.val]--;
			// }
			// // countFuncK--;
			// sb.append(temp.charAt(0));
			// beforeChar = temp.charAt(0);
			// temp = temp.substring(1);
			// continue;
			// }
			else {
				if (statusBefore != null) {
					if (countKs[SqlCategoryEnum.from.val] > 0) {
						sb.append(temp.charAt(0));
						beforeChar = temp.charAt(0);
						temp = temp.substring(1);
						continue;
					}
					if (countKs[statusBefore.val] > 0) {
						sb.append(temp.charAt(0));
						beforeChar = temp.charAt(0);
						if (temp.startsWith(SqlCategoryEnum.select.getKey())) {
							hasSelect = true;
						}
						temp = temp.substring(1);

						continue;
					} else if (countFunc[statusBefore.val] > 0) {
						sb.append(temp.charAt(0));
						temp = temp.substring(1);
						continue;
					}
					beforeChar = temp.charAt(0);
				} else {
					beforeChar = temp.charAt(0);
				}
			}

			if (isBoolean && !hasJoin) {
				// 获取table 别名
				if (temp.startsWith(" as ")) {
					temp = temp.substring(4);
					int z = temp.indexOf(" ");
					int z2 = temp.indexOf(",");
					if (z < 0) {
						if (z2 < 0) {
							alais.append(temp);
							temp = "";
						} else {
							// if (hasSelect) {
							// sb = new StringBuffer("( " + sb.toString()
							// + " )");
							// }
							sb.append(" as " + temp.subSequence(0, z2));
							temp = temp.substring(z2);
						}
					} else {
						if (z2 < 0) {
							alais.append(temp.subSequence(0, z));
							temp = temp.substring(z);
						} else {
							if (hasKh) {
								sb = new StringBuffer("( " + sb.toString()
										+ " )");
							}
							hasJoin = true;
							hasKh = false;
							sb.append(" as ").append(temp.subSequence(0, z2));
							temp = temp.substring(z2);
						}
					}
					hasSelect = false;
					isBoolean = false;
					continue;
				}
				hasSelect = false;
				isBoolean = false;
			}
			hasSelect = false;
			// 获取sql
			for (SqlCategoryEnum enumSql : enumSqls) {
				if (temp.startsWith(enumSql.getKey())) {
					start = enumSql.getKey().length();
					if (hasJoin) {
						if (enumSql.equals(SqlCategoryEnum.on)) {
							break;
						}
					}
					status = enumSql;
					if (statusBefore == null) {
						statusBefore = status;
					}
					if (enumSql.equals(SqlCategoryEnum.from)) {
						hasFrom = true;
					} else {
						hasFrom = false;
					}
					flag = true;
					break;
				}
			}
			// if (temp.startsWith(" join ")) {
			// hasJoin = true;
			// }
			if (!hasJoin) {
				for (TableLinkedEnum tab : tableLinked) {
					if (temp.startsWith(tab.getKey())) {
						hasJoin = true;
						if (hasKh) {
							String temp2 = sb.toString();
							sb = new StringBuffer();
							sb.append("( ").append(temp2).append(" ) ");
							if (alais.length() > 0) {
								sb.append(" as ").append(alais);
								alais = new StringBuffer();
							}
						}
						sb.append(" ").append(tab.getKey()).append(" ");
						temp = temp.substring(tab.getKey().length());
						break;
					}
				}
			}
			if (flag) {
				if (sb.length() != 0) {
					if (sb.toString().trim().length() != 0) {
						System.out
								.println("1:" + statusBefore + "\tsource:"
										+ sb.toString() + "\talais:"
										+ alais.toString());
						fill(statusBefore, sb.toString(), alais.toString(),databaseField);
					}
					hasJoin = false;
					hasKh = false;
					alais = new StringBuffer();
				}
				sb = new StringBuffer();
				statusBefore = status;
				temp = temp.substring(start);
				beforeChar = ' ';
				flag = false;
			} else {
				// 获取 union
				for (TableLinkedEnum2 enumSql : tableLinked2) {
					if (enumSql.getKey().length() <= 0) {
						continue;
					}
					if (temp.startsWith(enumSql.getKey())) {
						start = enumSql.getKey().length();
						statusTable = enumSql;
						// if (statusTableBefore == null) {
						// statusTableBefore = statusTable;
						// }
						flag2 = true;
						hasUnion = true;
						break;
					}
				}
				if (flag2) {
					System.out.println("2:" + statusBefore + "\tsource:"
							+ sb.toString() + "\talais:" + alais.toString());
					if (!unionTempAlaisBoolean) {
						unionTempAlais = this.alias;
						this.alias = null;
						unionTempAlaisBoolean = true;
					}
					fill(statusBefore, sb.toString(), alais.toString(),databaseField);
					hasJoin = false;
					hasKh = false;
					// 修正数据
					TableNode tableBefore = new TableNode(this.deep + 1);
					tableBefore.tables = this.tables;
					tableBefore.column = this.column;
					tableBefore.groupby = this.groupby;
					tableBefore.having = this.having;
					tableBefore.on = this.on;
					tableBefore.orderby = this.orderby;
					tableBefore.where = where;
					tableBefore.alias = alias;
					if (statusTableBefore == null) {
						tableBefore.before = TableLinkedEnum.NULL;
						unionTables = new ArrayList<TableNode>();
					} else {
						tableBefore.beforeUnion = statusTableBefore;
					}
					statusTableBefore = statusTable;
					this.unionTables.add(tableBefore);
					this.tables = null;
					this.column = null;
					this.groupby = null;
					this.having = null;
					this.before = null;
					this.on = null;
					this.orderby = null;
					this.where = null;
					this.alias = null;
					temp = temp.substring(start);
					sb = new StringBuffer();
					alais = new StringBuffer();
					flag2 = false;
					continue;
				}
				sb.append(temp.substring(0, 1));
				temp = temp.substring(1);
			}
		}
		if (sb.length() > 0) {
			System.out.println("3:" + statusBefore + "\tsource:"
					+ sb.toString() + "\talais:" + alais.toString());
			fill(statusBefore, sb.toString(), alais.toString(),databaseField);
		}
		if (hasUnion) {
			TableNode tableBefore = new TableNode(this.deep + 1);
			if (!unionTempAlaisBoolean) {
				unionTempAlais = this.alias;
				this.alias = null;
				unionTempAlaisBoolean = true;
			}
			tableBefore.tables = this.tables;
			tableBefore.column = this.column;
			tableBefore.groupby = this.groupby;
			tableBefore.having = this.having;
			tableBefore.on = this.on;
			tableBefore.orderby = this.orderby;
			tableBefore.where = where;
			tableBefore.alias = alias;
			if (statusTableBefore == null) {
				tableBefore.before = TableLinkedEnum.NULL;
				unionTables = new ArrayList<TableNode>();
			} else {
				tableBefore.beforeUnion = statusTableBefore;
			}
			this.unionTables.add(tableBefore);
			this.tables = null;
			this.column = null;
			this.groupby = null;
			this.having = null;
			this.on = null;
			this.before = null;
			this.orderby = null;
			this.where = null;
			this.alias = unionTempAlais;
		}
		// 切换全部的table
		if (column != null) {
			column.init(this.tables);
		}
		if (unionTables != null) {
			for (TableNode ta : unionTables) {
				if (ta.column != null) {
					ta.column.init(ta.tables);
				}
			}
		}
	}

	public void fill(SqlCategoryEnum status, String input, String alais,String databaseField) {
		if (status.equals(SqlCategoryEnum.select)) {
			System.out.println("columns:" + input);
			column = new CloumnNode(input);
		} else if (status.equals(SqlCategoryEnum.from)) {
			// 别名需要处理
			System.out.println("from:" + input + "::::-" + alais + "-::");
			if (alais.length() > 0) {
				tables = new ArrayTableNode(this.deep + 1, input, alais,databaseField);
			} else {
				tables = new ArrayTableNode(this.deep + 1, input, null,databaseField);
			}
		} else if (status.equals(SqlCategoryEnum.on)) {
			System.out.println("on:" + input);
			on = new WhereNode(tables, input, 1, this.deep);
		} else if (status.equals(SqlCategoryEnum.where)) {
			where = new WhereNode(tables, input, 1, this.deep);
		} else if (status.equals(SqlCategoryEnum.orderby)) {
			orderby = new OrderByNode(tables, input);
		} else if (status.equals(SqlCategoryEnum.groupby)) {
			groupby = new GroupByNode(tables, input);
		} else if (status.equals(SqlCategoryEnum.limit)) {
			limit = new LimitNode(input);
		} else if (status.equals(SqlCategoryEnum.having)) {
			having = new HavingNode(tables,input,this.deep);
		}

	}

	/**
	 * 剔除过多的括号
	 * 
	 * @return
	 */
	public String clearnK(String input) {
		return input;
	}

	public String toTransIdString() {
		StringBuffer sb = new StringBuffer();
		String temp = FormatStringGet.getT(deep);
		String temp2 = FormatStringGet.getT(deep + 1);
		boolean isFirst = false;
		if (beforeUnion != null && beforeUnion.getKey().length() > 0) {
			sb.append("\n");
			isFirst = true;
			sb.append(temp + beforeUnion.getKey());

		}
		if (before != null && before.getKey().length() > 0) {
			sb.append("\n" + temp);
			isFirst = true;
			sb.append(before.getKey());
		}
		// 子查询方式 多表关联方式
		if (this.before != null
				&& this.alias != null
				&& (tables == null || tables.table == null || this.column != null)) {
			sb.append("\n");
			isFirst = true;
			sb.append(temp + "(\n");
		}
		if (this.column != null) {
			if (isFirst) {
				sb.append("\n");
			}
			isFirst = true;
			sb.append(temp + SqlCategoryEnum.select.getKey());
			sb.append("\n");
			sb.append(temp2 + this.column.toTransIdString());

		}
		if (tables != null) {
			if ((this.before == null || this.before
					.equals(TableLinkedEnum.NULL)) && this.column != null) {
				if (isFirst) {
					sb.append("\n");
				}
				isFirst = true;
				sb.append(temp + SqlCategoryEnum.from.getKey());
			}
			if (isFirst) {
				sb.append("\n");
			}
			isFirst = true;
			sb.append(tables.toTransIdString());
		} else {
			if (unionTables != null) {
				for (TableNode node : unionTables) {
					// sb.append("\n");
					sb.append(temp + node.toTransIdString());
				}
			}
		}

		if (this.where != null) {
			if (isFirst) {
				sb.append("\n");
			}
			isFirst = true;
			sb.append(temp + SqlCategoryEnum.where.getKey());
			sb.append("\n");
			sb.append(temp2 + this.where.toTransIdString());
		}
		if (this.groupby != null) {
			if (isFirst) {
				sb.append("\n");
			}
			isFirst = true;
			sb.append(temp + SqlCategoryEnum.groupby.getKey());
			sb.append("\n" + temp2);
			sb.append(this.groupby.toTransIdString());
		}
		if (this.orderby != null) {
			if (isFirst) {
				sb.append("\n");
			}
			isFirst = true;
			sb.append(temp + SqlCategoryEnum.orderby.getKey());
			sb.append("\n" + temp2);
			sb.append(this.orderby.toTransIdString());
		}
		if (this.having != null) {
			if (isFirst) {
				sb.append("\n");
			}
			isFirst = true;
			sb.append(temp + SqlCategoryEnum.having.getKey());
			sb.append("\n" + temp2);
			sb.append(this.having.toTransIdString());
		}
		if (this.limit != null) {
			if (isFirst) {
				sb.append("\n" + temp);
			}
			isFirst = true;
			sb.append(SqlCategoryEnum.limit.getKey());
			sb.append("\n" + temp2);
			sb.append(this.limit);
		}
		if (this.before != null
				&& this.alias != null
				&& (tables == null || tables.table == null || this.column != null)) {
			if (isFirst) {
				sb.append("\n" + temp);
			}
			isFirst = true;
			sb.append(")");
		}
		if (this.alias != null) {
			// sb.append("::>");
			sb.append(" as ").append(alias);
			// sb.append("<::");
		}
		if (this.on != null) {
			sb.append("\n" + temp);
			sb.append(SqlCategoryEnum.on.getKey());
			sb.append("\n" + temp2);
			sb.append(this.on.toTransIdString());
		}
		return sb.toString();
	}

	public String toTransString() {
		StringBuffer sb = new StringBuffer();
		String temp = FormatStringGet.getT(deep);
		String temp2 = FormatStringGet.getT(deep + 1);
		boolean isFirst = false;
		if (beforeUnion != null && beforeUnion.getKey().length() > 0) {
			sb.append("\n");
			isFirst = true;
			sb.append(temp + beforeUnion.getKey());

		}
		if (before != null && before.getKey().length() > 0) {
			sb.append("\n" + temp);
			isFirst = true;
			sb.append(before.getKey());
		}
		// 子查询方式 多表关联方式
		if (this.before != null
				&& this.alias != null
				&& (tables == null || tables.table == null || this.column != null)) {
			sb.append("\n");
			isFirst = true;
			sb.append(temp + "(\n");
		}
		if (this.column != null) {
			if (isFirst) {
				sb.append("\n");
			}
			isFirst = true;
			sb.append(temp + SqlCategoryEnum.select.getKey());
			sb.append("\n");
			sb.append(temp2 + this.column.toTransString());
		}
		if ((this.before == null || this.before.equals(TableLinkedEnum.NULL))
				&& this.column != null) {
			if (isFirst) {
				sb.append("\n");
			}
			isFirst = true;
			sb.append(temp + SqlCategoryEnum.from.getKey());
		}
		if (tables != null) {
			if (isFirst) {
				sb.append("\n");
			}
			isFirst = true;
			sb.append(tables.toTransString());
		} else {
			if (unionTables != null) {
				for (TableNode node : unionTables) {
					// sb.append("\n");
					sb.append(temp + node.toTransString());
				}
			}
		}
		if (this.where != null) {
			if (isFirst) {
				sb.append("\n");
			}
			isFirst = true;
			sb.append(temp + SqlCategoryEnum.where.getKey());
			sb.append("\n");
			sb.append(temp2 + this.where.toTransString());
		}
		if (this.groupby != null) {
			if (isFirst) {
				sb.append("\n");
			}
			isFirst = true;
			sb.append(temp + SqlCategoryEnum.groupby.getKey());
			sb.append("\n" + temp2);
			sb.append(this.groupby.toTransString());
		}
		if (this.orderby != null) {
			if (isFirst) {
				sb.append("\n");
			}
			isFirst = true;
			sb.append(temp + SqlCategoryEnum.orderby.getKey());
			sb.append("\n" + temp2);
			sb.append(this.orderby.toTransString());
		}
		if (this.having != null) {
			if (isFirst) {
				sb.append("\n");
			}
			isFirst = true;
			sb.append(temp + SqlCategoryEnum.having.getKey());
			sb.append("\n" + temp2);
			sb.append(this.having.toTransString());
		}
		if (this.limit != null) {
			if (isFirst) {
				sb.append("\n" + temp);
			}
			isFirst = true;
			sb.append(SqlCategoryEnum.limit.getKey());
			sb.append("\n" + temp2);
			sb.append(this.limit);
		}
		if (this.before != null
				&& this.alias != null
				&& (tables == null || tables.table == null || this.column != null)) {
			if (isFirst) {
				sb.append("\n" + temp);
			}
			isFirst = true;
			sb.append(")");
		}
		if (this.alias != null) {
			// sb.append("::>");
			sb.append(" as ").append(alias);
			// sb.append("<::");
		}
		if (this.on != null) {
			sb.append("\n" + temp);
			sb.append(SqlCategoryEnum.on.getKey());
			sb.append("\n" + temp2);
			sb.append(this.on.toTransString());
		}
		return sb.toString();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		String temp = FormatStringGet.getT(deep);
		String temp2 = FormatStringGet.getT(deep + 1);
		boolean isFirst = false;
		if (beforeUnion != null && beforeUnion.getKey().length() > 0) {
			sb.append("\n");
			isFirst = true;
			sb.append(temp + beforeUnion.getKey());
		}
		if (before != null && before.getKey().length() > 0) {
			sb.append("\n" + temp);
			isFirst = true;
			sb.append(before.getKey());
		}
		// 子查询方式 多表关联方式
		if (this.before != null
				&& this.alias != null
				&& (tables == null || tables.table == null || this.column != null)) {
			sb.append("\n");
			isFirst = true;
			sb.append(temp + "(\n");
		}
		if (this.column != null) {
			if (isFirst) {
				sb.append("\n");
			}
			isFirst = true;
			sb.append(temp + SqlCategoryEnum.select.getKey());
			sb.append("\n");
			sb.append(temp2 + this.column);
		}
		if ((this.before == null || this.before.equals(TableLinkedEnum.NULL))
				&& this.column != null) {
			if (isFirst) {
				sb.append("\n");
			}
			isFirst = true;
			sb.append(temp + SqlCategoryEnum.from.getKey());
		}
		if (tables != null) {
			if (isFirst) {
				sb.append("\n");
			}
			isFirst = true;
			sb.append(tables.toString());
		} else {
			if (unionTables != null) {
				for (TableNode node : unionTables) {
					// sb.append("\n");
					sb.append(temp + node.toString());
				}
			}
		}

		if (this.where != null) {
			if (isFirst) {
				sb.append("\n");
			}
			isFirst = true;
			sb.append(temp + SqlCategoryEnum.where.getKey());
			sb.append("\n");
			sb.append(temp2 + this.where);
		}
		if (this.groupby != null) {
			if (isFirst) {
				sb.append("\n");
			}
			isFirst = true;
			sb.append(temp + SqlCategoryEnum.groupby.getKey());
			sb.append("\n" + temp2);
			sb.append(this.groupby);
		}
		if (this.orderby != null) {
			if (isFirst) {
				sb.append("\n");
			}
			isFirst = true;
			sb.append(temp + SqlCategoryEnum.orderby.getKey());
			sb.append("\n" + temp2);
			sb.append(this.orderby);
		}
		if (this.having != null) {
			if (isFirst) {
				sb.append("\n");
			}
			isFirst = true;
			sb.append(temp + SqlCategoryEnum.having.getKey());
			sb.append("\n" + temp2);
			sb.append(this.having);
		}
		if (this.limit != null) {
			if (isFirst) {
				sb.append("\n" + temp);
			}
			isFirst = true;
			sb.append(SqlCategoryEnum.limit.getKey());
			sb.append("\n" + temp2);
			sb.append(this.limit);
		}
		if (this.before != null
				&& this.alias != null
				&& (tables == null || tables.table == null || this.column != null)) {
			if (isFirst) {
				sb.append("\n" + temp);
			}
			isFirst = true;
			sb.append(")");
		}
		if (this.alias != null) {
			// sb.append("::>");
			sb.append(" as ").append(alias);
			// sb.append("<::");
		}
		if (this.on != null) {
			sb.append("\n" + temp);
			sb.append(SqlCategoryEnum.on.getKey());
			sb.append("\n" + temp2);
			sb.append(this.on.toString());
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		String input = "select e.* from ((select * from table1 ) as a,table2 b left join table3 as c on a.id=c.id) as e";
		input = "select * from ( select e.* from table1 as e ) as c";
		input = "select a.*,c.id,d.* from ( select e.* from table1 as e ) as c,table2 as b where c.col1=b.col2 group by c.col2 order by c.col3 having count(1)>3  limit 2";
		// input =
		// "select e.* from (select * from table1 as a ) as a,table2 as b left join table3 as c on a.id=c.i where a.id=b.id";
		// input =
		// "select a.* from table1 as a left join table3 as b on a.id=b.id union all select b.* from table2 as b";
		// input ="select a.* from table as a";
		input = "select a.* from ( select b.* from table1 as a left join (select x1.1d,x2.x4 from table3 as x1,table2 as x2 where x1.1d=x2.2d group by x1.1d limit 100 ) as b on a.id=b.id left join table4 as e on table3.bc=table5.a ) as a where a.sd=3";
		// input = "select a.* from table1 as a where a.status not in (2,3,4)";
		input = "select a.user_id as e,a.uid,sum(1) as c from  (select * from dwd.dwd_user_base_full ) as e,dwd.dwd_user_base_full as a left join dwd.dwd_test as b on (a.id=b.id and a.id=b.id) or b.status in ('a','b') where b.user!=0 group by a.user_id,a.uid";
		// input
		// ="select a.user_id as e,a.uid,sum(1) as c from dwd.dwd_user_base_full as a left join dwd.dwd_test as b on a.id=b.id or b.status in ('a','b') where b.user!=0 group by a.user_id,a.uid";
		input = "select * from ( select a.user_id as e,a.uid,sum(1) as c from  (select * from dwd.dwd_user_base_full ) as e,dwd.dwd_user_base_full as a left join dwd.dwd_test as b on a.id=b.id and b.status in ('a','b') where (b.user!=0 and b.user=1) or b.user!=2 group by a.user_id,a.uid ) as e2 limit 100";
		input = "select a.user_id as e3,a.uid,sum(1) as c from  (select * from dwd.dwd_user_base_full ) as e,dwd.dwd_user_base_full as a left join dwd.dwd_test as b on a.id=b.id and b.status in ('a','b') where (b.user!=0 and b.user=1) or b.user!=2 group by a.user_id,a.uid";
		input = "select * from (select user_id from dwd.dwd_user_base_full union all select xt.user_id from dwd.dwd_user_base_full as xt) as c";
		TableNode node = new TableNode(1, input,"dws");
		System.out.println(node.toString());

	}
}
