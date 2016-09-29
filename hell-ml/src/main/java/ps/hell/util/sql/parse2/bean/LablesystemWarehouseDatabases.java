package ps.hell.util.sql.parse2.bean;

import java.util.Date;

public class LablesystemWarehouseDatabases {
    private Long id;

    private String connDriver;

    private String connDatabase;

    private String storeDatabase;

    private String connUrl;

    private Integer connPort;

    private String dbComment;

    private String userEncode;

    private String pwdEncode;

    private Date dateCreate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConnDriver() {
        return connDriver;
    }

    public void setConnDriver(String connDriver) {
        this.connDriver = connDriver == null ? null : connDriver.trim();
    }

    public String getConnDatabase() {
        return connDatabase;
    }

    public void setConnDatabase(String connDatabase) {
        this.connDatabase = connDatabase == null ? null : connDatabase.trim();
    }

    public String getStoreDatabase() {
        return storeDatabase;
    }

    public void setStoreDatabase(String storeDatabase) {
        this.storeDatabase = storeDatabase == null ? null : storeDatabase.trim();
    }

    public String getConnUrl() {
        return connUrl;
    }

    public void setConnUrl(String connUrl) {
        this.connUrl = connUrl == null ? null : connUrl.trim();
    }

    public Integer getConnPort() {
        return connPort;
    }

    public void setConnPort(Integer connPort) {
        this.connPort = connPort;
    }

    public String getDbComment() {
        return dbComment;
    }

    public void setDbComment(String dbComment) {
        this.dbComment = dbComment == null ? null : dbComment.trim();
    }

    public String getUserEncode() {
        return userEncode;
    }

    public void setUserEncode(String userEncode) {
        this.userEncode = userEncode == null ? null : userEncode.trim();
    }

    public String getPwdEncode() {
        return pwdEncode;
    }

    public void setPwdEncode(String pwdEncode) {
        this.pwdEncode = pwdEncode == null ? null : pwdEncode.trim();
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }
}