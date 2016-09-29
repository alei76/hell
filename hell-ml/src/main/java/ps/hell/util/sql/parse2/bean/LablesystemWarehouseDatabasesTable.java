package ps.hell.util.sql.parse2.bean;

import java.util.Date;

public class LablesystemWarehouseDatabasesTable {
    private Long id;

    private Long databaseId;

    private String databaseName;

    private String tableName;

    private String tableComment;

    private Byte isNew;

    private String odsTableName;

    private String odsSnyStatus;

    private String odsDatabase;

    private String tableTransName;

    private String dwdTheme;

    private String dwdDatabase;

    private String dwdIncPartitionVal;

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

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName == null ? null : databaseName.trim();
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName == null ? null : tableName.trim();
    }

    public String getTableComment() {
        return tableComment;
    }

    public void setTableComment(String tableComment) {
        this.tableComment = tableComment == null ? null : tableComment.trim();
    }

    public Byte getIsNew() {
        return isNew;
    }

    public void setIsNew(Byte isNew) {
        this.isNew = isNew;
    }

    public String getOdsTableName() {
        return odsTableName;
    }

    public void setOdsTableName(String odsTableName) {
        this.odsTableName = odsTableName == null ? null : odsTableName.trim();
    }

    public String getOdsSnyStatus() {
        return odsSnyStatus;
    }

    public void setOdsSnyStatus(String odsSnyStatus) {
        this.odsSnyStatus = odsSnyStatus == null ? null : odsSnyStatus.trim();
    }

    public String getOdsDatabase() {
        return odsDatabase;
    }

    public void setOdsDatabase(String odsDatabase) {
        this.odsDatabase = odsDatabase == null ? null : odsDatabase.trim();
    }

    public String getTableTransName() {
        return tableTransName;
    }

    public void setTableTransName(String tableTransName) {
        this.tableTransName = tableTransName == null ? null : tableTransName.trim();
    }

    public String getDwdTheme() {
        return dwdTheme;
    }

    public void setDwdTheme(String dwdTheme) {
        this.dwdTheme = dwdTheme == null ? null : dwdTheme.trim();
    }

    public String getDwdDatabase() {
        return dwdDatabase;
    }

    public void setDwdDatabase(String dwdDatabase) {
        this.dwdDatabase = dwdDatabase == null ? null : dwdDatabase.trim();
    }

    public String getDwdIncPartitionVal() {
        return dwdIncPartitionVal;
    }

    public void setDwdIncPartitionVal(String dwdIncPartitionVal) {
        this.dwdIncPartitionVal = dwdIncPartitionVal == null ? null : dwdIncPartitionVal.trim();
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }
}