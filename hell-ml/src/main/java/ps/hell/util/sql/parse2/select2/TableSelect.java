package ps.hell.util.sql.parse2.select2;

import ps.hell.util.sql.parse2.bean.LablesystemWarehouseDatabasesTable;



public class TableSelect {

	/**
	 * 表名
	 */
	public String tableName;

	public LablesystemWarehouseDatabasesTable mapNode = null;

	public TableSelect(String input) {
		this.tableName = input;
	}

	public String toString() {
		return tableName;
	}

	public String toTransIdString() {
		if (mapNode != null) {
			return "{tab=" + mapNode.getId() + "}";
		} else {
			return tableName;
		}
	}

	public String toTransString() {
		if (mapNode != null) {
			return mapNode.getDwdDatabase()+"."+mapNode.getTableTransName();
		} else {
			return tableName;
		}
	}

	public LablesystemWarehouseDatabasesTable getMapNode() {
		return mapNode;
	}

	public void setMapNode(LablesystemWarehouseDatabasesTable mapNode) {
		this.mapNode = mapNode;
	}

}
