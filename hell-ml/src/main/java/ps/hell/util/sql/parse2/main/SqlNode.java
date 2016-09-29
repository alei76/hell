package ps.hell.util.sql.parse2.main;

import java.util.List;

import ps.hell.util.sql.parse2.ArrayTableNode;
import ps.hell.util.sql.parse2.TableNode;
import ps.hell.util.sql.parse2.WhereNode;
import ps.hell.util.sql.parse2.bean.LablesystemWarehouseDatabasesTable;
import ps.hell.util.sql.parse2.bean.LablesystemWarehouseDatabasesTableColumn;
import ps.hell.util.sql.parse2.select2.ColumnSelect;
import ps.hell.util.sql.parse2.select2.ColumnSelectFunction;
import ps.hell.util.sql.parse2.select2.WhereSelect;

public class SqlNode {

	/**
	 * node读取前缀
	 */
	public String nodeHead=null;
	/**
	 * sql解析后的table
	 */
	public TableNode node = null;
	/**
	 * 映射数据
	 */
	public TableColumnsKey map = null;

	public String[] inputs = null;

	public int useIndex = -1;

	/**
	 * 所使用的数据库域
	 */
	public String useDabases;

	/**
	 * sqlNode
	 * 
	 * @param inputSql
	 */
	public SqlNode(String inputSql) {
		inputs = inputSql.split(";");
		for (int i = 0; i < inputs.length; i++) {
			String str = inputs[i];
			if (str.startsWith("use ")) {
				useDabases = str.substring(4);
			} else if(str.startsWith("set ")){
				
			}else if(str.startsWith("drop ")){
				
			}else if(str.startsWith("insert ")){
				this.useIndex=i;
				int indexFirst=str.indexOf(" select ");
				if(indexFirst<0){
					System.err.println("异常insert * select 位置");
				}
				nodeHead=str.substring(0,indexFirst);
				node=new TableNode(1,str.substring(indexFirst),useDabases);
			}else if(str.startsWith("alert ")){
				
			}else if(str.startsWith("select ")){
				this.useIndex=i;
				node = new TableNode(1, inputSql, useDabases);
			}
		}
	}

	/**
	 * 初始化
	 * 
	 * @param tabs
	 * @param cols
	 */
	public void init(List<LablesystemWarehouseDatabasesTable> tabs,
			List<LablesystemWarehouseDatabasesTableColumn> cols) {
		map = new TableColumnsKey();
		map.init(tabs, cols);
	}

	/**
	 * 修改表
	 * 
	 * @param table
	 */
	public void update(LablesystemWarehouseDatabasesTable table) {
		map.update(table);
		;
	}

	/**
	 * 修改列
	 * 
	 * @param column
	 */
	public void update(LablesystemWarehouseDatabasesTableColumn column) {
		map.update(column);
	}

	/**
	 * 删除表
	 * 
	 * @param tableId
	 */
	public void removeTable(Long tableId) {
		map.removeTable(tableId);
	}

	/**
	 * 删除列
	 * 
	 * @param column
	 */
	public void removeColumn(Long column) {
		map.removeColumn(column);
	}

	public void change() {
		change(this.node);
	}

	/**
	 * 修改node
	 * 
	 * @param node
	 */
	private void change(TableNode node) {
		if (node.tables != null) {
			change(node.tables);
		}
		if (node.column != null) {
			for (ColumnSelect select : node.column.columns) {
				change(select);
			}
		}
		if (node.groupby != null) {
			for (ColumnSelect select : node.groupby.group) {
				change(select);
			}
		}
		if (node.where != null) {
			change(node.where);
		}

		if (node.on != null) {
			change(node.on);
		}

		if (node.unionTables != null) {
			for (TableNode no : node.unionTables) {
				change(no);
			}
		}
	}

	private void change(ColumnSelect select) {
		if (select.alaisTableSource != null && select.columnMap != null) {
			String[] val = select.alaisTableSource.split("\\.");
			// long tableId=this.map.getTableId(val[0],val[1]);
			select.setMapNode(this.map.getColumn(val[0], val[1],
					select.columnMap));
		} else if (select.function != null) {
			ColumnSelectFunction func = select.function;
			if (func.usedList != null) {
				for (ColumnSelect se2 : func.usedList) {
					change(se2);
				}
			}
		}
	}

	private void change(WhereNode where) {
		if (where != null) {
			WhereNode nodeTemp = null;
			if (where.nodes.size() > 0) {
				for (WhereNode select : where.nodes) {
					nodeTemp = select;
					change(nodeTemp);
				}
			} else {
				nodeTemp = where;
				WhereSelect ss = nodeTemp.node;
				ColumnSelect left = ss.left;
				ColumnSelect right = ss.right;
				change(left);
				change(right);
			}
		}
	}

	private void change(ArrayTableNode node) {
		if (node.table != null) {
			String tableName = node.table.toString();
			// System.out.println("tableName:"+tableName);
			if (tableName.contains(".")) {
				String[] val = tableName.split("\\.");
				node.table.setMapNode(this.map.getTable(val[0], val[1]));
			}
		}
		if (node.nodes != null) {
			for (TableNode no : node.nodes) {
				change(no);
			}
		}
	}

	/**
	 * 获取sql
	 */
	public String toString() {
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<inputs.length;i++){
			if(this.useIndex==i){
				if(this.nodeHead!=null){
					sb.append(this.nodeHead).append("\n");
				}
				sb.append(node.toString()).append(";");
			}else{
				sb.append(inputs[i]).append(";");
			}
			if(i<inputs.length-1){
				sb.append("\n");
			}
		}
		return sb.toString();
	}

	/**
	 * 获取转换id的数据
	 * 
	 * @return
	 */
	public String toTransIdString() {
//		return node.toTransIdString();
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<inputs.length;i++){
			if(this.useIndex==i){
				if(this.nodeHead!=null){
					sb.append(this.nodeHead).append("\n");
				}
				sb.append(node.toTransIdString()).append(";");
			}else{
				sb.append(inputs[i]).append(";");
			}
			if(i<inputs.length-1){
				sb.append("\n");
			}
		}
		return sb.toString();
	}

	/**
	 * 获取实际的String 跟举实际变更的
	 * 
	 * @return
	 */
	public String toTransString() {
//		return node.toTransString();
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<inputs.length;i++){
			if(this.useIndex==i){
				if(this.nodeHead!=null){
					sb.append(this.nodeHead).append("\n");
				}
				sb.append(node.toTransString()).append(";");
			}else{
				sb.append(inputs[i]).append(";");
			}
			if(i<inputs.length-1){
				sb.append("\n");
			}
		}
		return sb.toString();
	}

}
