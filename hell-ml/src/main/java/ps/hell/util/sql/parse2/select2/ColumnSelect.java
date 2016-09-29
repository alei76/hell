package ps.hell.util.sql.parse2.select2;

import java.util.ArrayList;

import ps.hell.util.sql.parse2.ArrayTableNode;
import ps.hell.util.sql.parse2.TableNode;
import ps.hell.util.sql.parse2.bean.LablesystemWarehouseDatabasesTableColumn;
import ps.hell.util.sql.parse2.enum2.SqlSortEnum;


public class ColumnSelect {

	/**
	 * 对应的函数
	 */
	public ColumnSelectFunction function = null;

	/**
	 * 实际的列
	 */
	public String column = null;

	/**
	 * 字段对应的列明
	 */
	public String alaisColumn = null;

	public SqlSortEnum columnTail = null;
	/**
	 * 实际的列的表映射字段 如果为别名非直接因用则为空
	 */
	public String columnMap = null;
	/**
	 * 别名表
	 */
	public String alaisTable = null;
	/**
	 * 别名表实际的表名
	 */
	public String alaisTableSource = null;

	public String source = null;

	public LablesystemWarehouseDatabasesTableColumn mapNode = null;

	public ColumnSelect(ArrayTableNode tables, String str) {
		str = str.trim();
		this.source = str;
		if(str.startsWith("'")&&str.endsWith("'")){
			return;
		}
		if (!str.startsWith("(")
				&& (str.contains("(") || str.contains(" * ") || str.contains("/") || str
						.contains("case when "))) {
			function = new ColumnSelectFunction(tables, str);
			return;
		}

		// //////////////////////////////////////////

		TableNode node = null;
		int sortIndex = str.indexOf(" asc");
		if (sortIndex > 0) {
			this.columnTail = SqlSortEnum.ASC;
			str = str.substring(0, sortIndex);
		}
		sortIndex = str.indexOf(" desc");
		if (sortIndex > 0) {
			this.columnTail = SqlSortEnum.DESC;
			str = str.substring(0, sortIndex);
		}
		int index = str.indexOf(".");
		if (index < 0) {
			// 如果没有别名
			if (tables.table != null) {
				// 为数值 并没捕获的数据
				alaisTable = tables.alais;
				alaisTableSource = tables.table.tableName;
			}
		} else {
			// 不处理 别名为 table的数据
			alaisTable = str.substring(0, index);
			// System.out.println("table");
			// System.out.println(tables);
			if (tables.table == null) {
				for (TableNode nodes : tables.nodes) {
					if (alaisTable.equals(nodes.alias)) {
						if (nodes.tables == null) {

						} else if (nodes.tables.table != null) {
							alaisTableSource = nodes.tables.table.tableName;
						} else {
							System.out.println("子查询不处理");
						}
						node = nodes;
						break;
					}
				}
			} else {
				alaisTableSource = tables.table.tableName;
			}
		}
		int index2 = str.indexOf(" as ");
		if (index2 > 0) {
			if (index < 0) {
				column = str.substring(0, index2);
				if (!column.equals("*")) {
					columnMap = column;
				}
			} else {
				column = str.substring(index + 1, index2);
				// 需要判断是否为引用的别名
				if (node != null && node.column != null) {
					for (ColumnSelect sel : node.column.columns) {
						if (sel.column == null) {
							// 为函数化
						} else {
							if (sel.column.equals("*")) {
								columnMap = column;
								break;
							}
							if (column.equals(sel.alaisColumn)) {
								columnMap = null;
								break;
							} else if (column.equals(sel.column)) {
								columnMap = sel.columnMap;
								alaisTableSource = sel.alaisTableSource;
							}
						}
					}
				} else {
					// 如果为别名的别名
					columnMap = column;
				}
			}
			alaisColumn = str.substring(index2 + 4).trim();
			return;
		}
		int index3 = str.indexOf(" ");
		if (index3 > 0) {
			if (index < 0) {
				column = str.substring(0, index3);
				if (!column.equals("*")) {
					columnMap = column;
				}
			} else {
				column = str.substring(index + 1, index3);
				if (node != null && node.column != null) {
					for (ColumnSelect sel : node.column.columns) {
						if (sel.column == null) {
							// 为函数化
						} else {
							if (sel.column.equals("*")) {
								columnMap = column;
								break;
							}
							if (column.equals(sel.alaisColumn)) {
								columnMap = null;
								break;
							} else if (column.equals(sel.column)) {
								columnMap = sel.columnMap;
								alaisTableSource = sel.alaisTableSource;
							}
						}
					}
				} else {
					columnMap = column;
				}
			}
			alaisColumn = str.substring(index3 + 1).trim();
			return;
		}
		if (index < 0) {
			column = str.substring(0);
			if (!column.equals("*")) {
				columnMap = column;
			}
		} else {
			column = str.substring(index + 1);
			if (node != null && node.column != null) {
				for (ColumnSelect sel : node.column.columns) {
					if (sel.column == null) {
						// 为函数化
					} else {
						if (sel.column.equals("*")) {
							columnMap = column;
							break;
						}
						if (column.equals(sel.alaisColumn)) {
							columnMap = null;
							break;
						} else if (column.equals(sel.column)) {
							columnMap = sel.columnMap;
							alaisTableSource = sel.alaisTableSource;
							break;
						}
					}
				}
			} else {
				columnMap = column;
			}
		}
	}

	public ColumnSelect(ArrayTableNode tables, String str, boolean isString) {
		if(str.startsWith("'")&&str.endsWith("'")){
			return;
		}
		if (!isString) {
			System.err.println("错误的str 不为isString:" + str);
		}
		this.source = str;
	}

	public ColumnSelect(ArrayList<TableNode> tables, String str) {
		str = str.trim();
		this.source = str;
		int index = str.indexOf(".");
		TableNode node = null;
		if (index < 0) {
			// alaisTable = tables.alais;
			// alaisTableSource = tables.table.tableName;
			System.err.println("column 需要别名表:" + str);
		} else {
			// 不处理 别名为 table的数据
			alaisTable = str.substring(0, index);
			// System.out.println("table");
			// System.out.println(tables);
			for (TableNode nodes : tables) {
				if (alaisTable.equals(nodes.alias)) {
					if (nodes.tables == null) {

					} else if (nodes.tables.table != null) {
						alaisTableSource = nodes.tables.table.tableName;
					} else {
						System.out.println("子查询不处理");
					}
					node = nodes;
					break;
				}
			}
		}
		int index2 = str.indexOf(" as ");
		if (index2 > 0) {
			if (index < 0) {
				column = str.substring(0, index2);
				if (!column.equals("*")) {
					columnMap = column;
				}
			} else {
				column = str.substring(index + 1, index2);
				// 需要判断是否为引用的别名
				if (node != null && node.column != null) {
					for (ColumnSelect sel : node.column.columns) {
						if (sel.column.equals("*")) {
							columnMap = column;
							break;
						}
						if (column.equals(sel.alaisColumn)) {
							columnMap = null;
							break;
						} else if (column.equals(sel.column)) {
							columnMap = sel.columnMap;
							alaisTableSource = sel.alaisTableSource;
						}
					}
				} else {
					// 如果为别名的别名
					columnMap = column;
				}
			}
			alaisColumn = str.substring(index2 + 4).trim();
			return;
		}
		int index3 = str.indexOf(" ");
		if (index3 > 0) {
			if (index < 0) {
				column = str.substring(0, index3);
				if (!column.equals("*")) {
					columnMap = column;
				}
			} else {
				column = str.substring(index + 1, index3);
				if (node != null && node.column != null) {
					for (ColumnSelect sel : node.column.columns) {
						if (sel.column.equals("*")) {
							columnMap = column;
							break;
						}
						if (column.equals(sel.alaisColumn)) {
							columnMap = null;
							break;
						} else if (column.equals(sel.column)) {
							columnMap = sel.columnMap;
							alaisTableSource = sel.alaisTableSource;
						}
					}
				} else {
					columnMap = column;
				}
			}
			alaisColumn = str.substring(index3 + 1).trim();
			return;
		}
		if (index < 0) {
			column = str.substring(0);
			if (!column.equals("*")) {
				columnMap = column;
			}
		} else {
			column = str.substring(index + 1);
			if (node != null && node.column != null) {
				for (ColumnSelect sel : node.column.columns) {
					if (sel.column.equals("*")) {
						columnMap = column;
						break;
					}
					if (column.equals(sel.alaisColumn)) {
						columnMap = null;
						break;
					} else if (column.equals(sel.column)) {
						columnMap = sel.columnMap;
						alaisTableSource = sel.alaisTableSource;
						break;
					}
				}
			} else {
				columnMap = column;
			}
		}
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.source);
		return sb.toString();
	}

	public String toTransIdString() {
		StringBuffer sb = new StringBuffer();
		if (this.function != null) {
			sb.append(this.function.toTransIdString());
		} else {
			if (mapNode != null) {
				sb.append("{col=" + mapNode.getId() + "}");
			} else {
				sb.append(this.source);
				return sb.toString();
			}
			if (this.alaisColumn != null) {
				sb.append(" as ").append(this.alaisColumn);
			}
			if (columnTail != null) {
				sb.append(" ").append(columnTail.getKey());
			}
		}
		return sb.toString();
	}

	public String toTransString() {
		StringBuffer sb = new StringBuffer();
		if (this.function != null) {
			sb.append(this.function.toTransString());
		} else {
			if (mapNode != null) {
				if (this.alaisTable != null) {
					sb.append(this.alaisTable).append(".");
				}
				sb.append(mapNode.getColumnNameMap());
			} else {
				sb.append(this.source);
				return sb.toString();
			}
			if (this.alaisColumn != null) {
				sb.append(" as ").append(this.alaisColumn);
			}
			if (columnTail != null) {
				sb.append(" ").append(columnTail.getKey());
			}
		}
		return sb.toString();
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String getAlaisColumn() {
		return alaisColumn;
	}

	public void setAlaisColumn(String alaisColumn) {
		this.alaisColumn = alaisColumn;
	}

	public String getColumnMap() {
		return columnMap;
	}

	public void setColumnMap(String columnMap) {
		this.columnMap = columnMap;
	}

	public String getAlaisTable() {
		return alaisTable;
	}

	public void setAlaisTable(String alaisTable) {
		this.alaisTable = alaisTable;
	}

	public String getAlaisTableSource() {
		return alaisTableSource;
	}

	public void setAlaisTableSource(String alaisTableSource) {
		this.alaisTableSource = alaisTableSource;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public LablesystemWarehouseDatabasesTableColumn getMapNode() {
		return mapNode;
	}

	public void setMapNode(LablesystemWarehouseDatabasesTableColumn mapNode) {
		this.mapNode = mapNode;
	}

	public static void main(String[] args) {
		String input = "test(a.user_id) as e1";
		input="concat('fsdasd%',user_id,',')";
		ColumnSelect select = new ColumnSelect(new ArrayTableNode(1), input);
		System.out.println(select.toString());
		System.out.println(select.toTransIdString());
		System.out.println(select.toTransString());

	}

}
