package ps.hell.util.sql.parse2.bean;

import java.util.Date;

public class LablesystemWarehouseDatabasesTableColumn {
    private Long id;

    private Long databaseId;

    private Long tableId;

    private String tableName;

    private Integer columnStatus;

    private String columnName;

    private String columnType;

    private String columnLength;

    private String columnPrecision;

    private String columnComment;

    private String columnNameMap;

    private String columnTypeMap;

    private String columnCommentMap;

    private Integer columnOrderMap;

    private String columnTransFuncionMap;

    private Byte isUsed;

    private Byte isPrimary;

    private Byte isCreateTime;

    private Byte isUpdateTime;

    private Date dateCreate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(Long databaseId) {
        this.databaseId = databaseId;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName == null ? null : tableName.trim();
    }

    public Integer getColumnStatus() {
        return columnStatus;
    }

    public void setColumnStatus(Integer columnStatus) {
        this.columnStatus = columnStatus;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName == null ? null : columnName.trim();
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType == null ? null : columnType.trim();
    }

    public String getColumnLength() {
        return columnLength;
    }

    public void setColumnLength(String columnLength) {
        this.columnLength = columnLength == null ? null : columnLength.trim();
    }

    public String getColumnPrecision() {
        return columnPrecision;
    }

    public void setColumnPrecision(String columnPrecision) {
        this.columnPrecision = columnPrecision == null ? null : columnPrecision.trim();
    }

    public String getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment == null ? null : columnComment.trim();
    }

    public String getColumnNameMap() {
        return columnNameMap;
    }

    public void setColumnNameMap(String columnNameMap) {
        this.columnNameMap = columnNameMap == null ? null : columnNameMap.trim();
    }

    public String getColumnTypeMap() {
        return columnTypeMap;
    }

    public void setColumnTypeMap(String columnTypeMap) {
        this.columnTypeMap = columnTypeMap == null ? null : columnTypeMap.trim();
    }

    public String getColumnCommentMap() {
        return columnCommentMap;
    }

    public void setColumnCommentMap(String columnCommentMap) {
        this.columnCommentMap = columnCommentMap == null ? null : columnCommentMap.trim();
    }

    public Integer getColumnOrderMap() {
        return columnOrderMap;
    }

    public void setColumnOrderMap(Integer columnOrderMap) {
        this.columnOrderMap = columnOrderMap;
    }

    public String getColumnTransFuncionMap() {
        return columnTransFuncionMap;
    }

    public void setColumnTransFuncionMap(String columnTransFuncionMap) {
        this.columnTransFuncionMap = columnTransFuncionMap == null ? null : columnTransFuncionMap.trim();
    }

    public Byte getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Byte isUsed) {
        this.isUsed = isUsed;
    }

    public Byte getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(Byte isPrimary) {
        this.isPrimary = isPrimary;
    }

    public Byte getIsCreateTime() {
        return isCreateTime;
    }

    public void setIsCreateTime(Byte isCreateTime) {
        this.isCreateTime = isCreateTime;
    }

    public Byte getIsUpdateTime() {
        return isUpdateTime;
    }

    public void setIsUpdateTime(Byte isUpdateTime) {
        this.isUpdateTime = isUpdateTime;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }
}