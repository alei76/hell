package ps.hell.util.sql.parse2.main;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import ps.hell.util.sql.parse2.bean.LablesystemWarehouseDatabasesTable;
import ps.hell.util.sql.parse2.bean.LablesystemWarehouseDatabasesTableColumn;


public class TableColumnsKey {

	/**
	 * 列 key database.table.column
	 */
	HashMap<String, LablesystemWarehouseDatabasesTableColumn> columns = new HashMap<String, LablesystemWarehouseDatabasesTableColumn>();
	/**
	 * column
	 */
	HashMap<Long, LablesystemWarehouseDatabasesTableColumn> columnsKey = new HashMap<Long, LablesystemWarehouseDatabasesTableColumn>();
	/**
	 * tbl key tableId val database.table
	 */
	HashMap<Long, String> tabsMap = new HashMap<Long, String>();
	/**
	 * tabls key database.table.column
	 */
	HashMap<String, LablesystemWarehouseDatabasesTable> tabs = new HashMap<String, LablesystemWarehouseDatabasesTable>();

	/**
	 * tabls key database.table.column
	 */
	HashMap<Long, LablesystemWarehouseDatabasesTable> tabskey = new HashMap<Long, LablesystemWarehouseDatabasesTable>();

	Object lock = new Object();

	public TableColumnsKey() {

	}

	/**
	 * 
	 * @param tlbs
	 *            tables
	 * @param cols
	 *            columns
	 */
	public void init(List<LablesystemWarehouseDatabasesTable> tabs,
			List<LablesystemWarehouseDatabasesTableColumn> cols) {
		// 初始化 table
		for (LablesystemWarehouseDatabasesTable tab : tabs) {
			// ods不支持
			// this.tabs.put(tab.getOdsDatabase()+"."+tab.getOdsTableName());
			this.tabsMap.put(tab.getId(),
					tab.getDwdDatabase() + "." + tab.getTableTransName());
			this.tabs.put(tab.getDwdDatabase() + "." + tab.getTableTransName(),
					tab);
		}
		// 初始化字段
		for (LablesystemWarehouseDatabasesTableColumn col : cols) {
			this.columns.put(
					this.tabsMap.get(col.getTableId()) + "."
							+ col.getColumnNameMap(), col);
			columnsKey.put(col.getId(),col);
		}
	}
	/**
	 * 删除表
	 * @param tableId
	 */
	public void removeTable(Long tableId){
		synchronized (lock) {
			LablesystemWarehouseDatabasesTable tabl=tabskey.get(tableId);
			tabskey.remove(tableId);
			tabs.remove(tabl.getDwdDatabase()+"."+tabl.getTableTransName());
			tabsMap.remove(tableId);
			HashSet<String> remove=new HashSet<String>();
			for(Entry<String, LablesystemWarehouseDatabasesTableColumn> map:columns.entrySet()){
				LablesystemWarehouseDatabasesTableColumn mall=map.getValue();
				if(mall.getTableId()==tableId){
					remove.add(map.getKey());
				}
				columnsKey.remove(mall.getId());
			}
			for(String tz:remove){
				columns.remove(tz);
			}
		}
	}
	/**
	 * 删除列
	 * @param column
	 */
	public void removeColumn(Long column){
		synchronized (lock) {
			LablesystemWarehouseDatabasesTableColumn tabl=columnsKey.get(column);
			columnsKey.remove(column);
			columns.remove(this.tabsMap.get(tabl.getTableId()) + "."
					+ tabl.getColumnNameMap());
		}
	}

	/**
	 * 修改表
	 * 
	 * @param table
	 */
	public void update(LablesystemWarehouseDatabasesTable table) {
		synchronized (lock) {
			LablesystemWarehouseDatabasesTable table2 = tabskey.get(table
					.getId());
			this.tabsMap.remove(table.getId());
			if (table2 == null) {
				this.tabsMap.put(table.getId(), table.getDwdDatabase() + "."
						+ table.getTableTransName());
				this.tabs.put(
						table.getDwdDatabase() + "."
								+ table.getTableTransName(), table);
			} else {
				this.tabsMap.put(table2.getId(), table2.getDwdDatabase() + "."
						+ table2.getTableTransName());
				tabs.remove(table2.getDwdDatabase() + "."
						+ table2.getTableTransName());
				table2.setDwdDatabase(table.getDatabaseName());
				table2.setTableTransName(table.getTableTransName());
				tabs.put(
						table2.getDwdDatabase() + "."
								+ table2.getTableTransName(), table2);
			}
		}
	}

	/**
	 * 修改列
	 * 
	 * @param column
	 */
	public void update(LablesystemWarehouseDatabasesTableColumn column) {
		synchronized (lock) {
			LablesystemWarehouseDatabasesTableColumn column2 = columnsKey
					.get(column.getId());
			columnsKey.put(column.getId(), column);
			columns.remove(this.tabsMap.get(column2.getTableId()) + "."
					+ column2.getColumnNameMap());
			columns.put(
					this.tabsMap.get(column.getTableId()) + "."
							+ column.getColumnNameMap(), column);
		}
	}

	/**
	 * 获取tableid
	 * 
	 * @param database
	 * @param tableName
	 * @return
	 */
	public long getTableId(String database, String tableName) {
		System.out.println("table:" + database + "\t" + tableName);
		return tabs.get(database + "." + tableName).getId();
	}

	/**
	 * 获取tableid
	 * 
	 * @param database
	 * @param tableName
	 * @return
	 */
	public LablesystemWarehouseDatabasesTable getTable(String database,
			String tableName) {
		System.out.println("table:" + database + "\t" + tableName);
		return tabs.get(database + "." + tableName);
	}

	/**
	 * 获取列的id
	 * 
	 * @param database
	 * @param tableName
	 * @param column
	 * @return
	 */
	public long getColumnId(String database, String tableName, String column) {
		System.out.println("column:" + database + "\t" + tableName + "\t"
				+ column);
		String val = database + "." + tableName + "." + column;
		LablesystemWarehouseDatabasesTableColumn cl = columns.get(val);
		if (cl == null) {
			System.err.println("不存在这样的列:" + val);
			return -1;
		}
		return cl.getId();
	}

	/**
	 * 获取列的id
	 * 
	 * @param database
	 * @param tableName
	 * @param column
	 * @return
	 */
	public LablesystemWarehouseDatabasesTableColumn getColumn(String database,
			String tableName, String column) {
		System.out.println("column:" + database + "\t" + tableName + "\t"
				+ column);
		String val = database + "." + tableName + "." + column;
		LablesystemWarehouseDatabasesTableColumn cl = columns.get(val);
		return cl;
	}

}
