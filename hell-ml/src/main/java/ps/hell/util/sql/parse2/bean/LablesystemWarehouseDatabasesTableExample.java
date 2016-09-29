package ps.hell.util.sql.parse2.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LablesystemWarehouseDatabasesTableExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public LablesystemWarehouseDatabasesTableExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andDatabaseIdIsNull() {
            addCriterion("database_id is null");
            return (Criteria) this;
        }

        public Criteria andDatabaseIdIsNotNull() {
            addCriterion("database_id is not null");
            return (Criteria) this;
        }

        public Criteria andDatabaseIdEqualTo(Long value) {
            addCriterion("database_id =", value, "databaseId");
            return (Criteria) this;
        }

        public Criteria andDatabaseIdNotEqualTo(Long value) {
            addCriterion("database_id <>", value, "databaseId");
            return (Criteria) this;
        }

        public Criteria andDatabaseIdGreaterThan(Long value) {
            addCriterion("database_id >", value, "databaseId");
            return (Criteria) this;
        }

        public Criteria andDatabaseIdGreaterThanOrEqualTo(Long value) {
            addCriterion("database_id >=", value, "databaseId");
            return (Criteria) this;
        }

        public Criteria andDatabaseIdLessThan(Long value) {
            addCriterion("database_id <", value, "databaseId");
            return (Criteria) this;
        }

        public Criteria andDatabaseIdLessThanOrEqualTo(Long value) {
            addCriterion("database_id <=", value, "databaseId");
            return (Criteria) this;
        }

        public Criteria andDatabaseIdIn(List<Long> values) {
            addCriterion("database_id in", values, "databaseId");
            return (Criteria) this;
        }

        public Criteria andDatabaseIdNotIn(List<Long> values) {
            addCriterion("database_id not in", values, "databaseId");
            return (Criteria) this;
        }

        public Criteria andDatabaseIdBetween(Long value1, Long value2) {
            addCriterion("database_id between", value1, value2, "databaseId");
            return (Criteria) this;
        }

        public Criteria andDatabaseIdNotBetween(Long value1, Long value2) {
            addCriterion("database_id not between", value1, value2, "databaseId");
            return (Criteria) this;
        }

        public Criteria andDatabaseNameIsNull() {
            addCriterion("database_name is null");
            return (Criteria) this;
        }

        public Criteria andDatabaseNameIsNotNull() {
            addCriterion("database_name is not null");
            return (Criteria) this;
        }

        public Criteria andDatabaseNameEqualTo(String value) {
            addCriterion("database_name =", value, "databaseName");
            return (Criteria) this;
        }

        public Criteria andDatabaseNameNotEqualTo(String value) {
            addCriterion("database_name <>", value, "databaseName");
            return (Criteria) this;
        }

        public Criteria andDatabaseNameGreaterThan(String value) {
            addCriterion("database_name >", value, "databaseName");
            return (Criteria) this;
        }

        public Criteria andDatabaseNameGreaterThanOrEqualTo(String value) {
            addCriterion("database_name >=", value, "databaseName");
            return (Criteria) this;
        }

        public Criteria andDatabaseNameLessThan(String value) {
            addCriterion("database_name <", value, "databaseName");
            return (Criteria) this;
        }

        public Criteria andDatabaseNameLessThanOrEqualTo(String value) {
            addCriterion("database_name <=", value, "databaseName");
            return (Criteria) this;
        }

        public Criteria andDatabaseNameLike(String value) {
            addCriterion("database_name like", value, "databaseName");
            return (Criteria) this;
        }

        public Criteria andDatabaseNameNotLike(String value) {
            addCriterion("database_name not like", value, "databaseName");
            return (Criteria) this;
        }

        public Criteria andDatabaseNameIn(List<String> values) {
            addCriterion("database_name in", values, "databaseName");
            return (Criteria) this;
        }

        public Criteria andDatabaseNameNotIn(List<String> values) {
            addCriterion("database_name not in", values, "databaseName");
            return (Criteria) this;
        }

        public Criteria andDatabaseNameBetween(String value1, String value2) {
            addCriterion("database_name between", value1, value2, "databaseName");
            return (Criteria) this;
        }

        public Criteria andDatabaseNameNotBetween(String value1, String value2) {
            addCriterion("database_name not between", value1, value2, "databaseName");
            return (Criteria) this;
        }

        public Criteria andTableNameIsNull() {
            addCriterion("table_name is null");
            return (Criteria) this;
        }

        public Criteria andTableNameIsNotNull() {
            addCriterion("table_name is not null");
            return (Criteria) this;
        }

        public Criteria andTableNameEqualTo(String value) {
            addCriterion("table_name =", value, "tableName");
            return (Criteria) this;
        }

        public Criteria andTableNameNotEqualTo(String value) {
            addCriterion("table_name <>", value, "tableName");
            return (Criteria) this;
        }

        public Criteria andTableNameGreaterThan(String value) {
            addCriterion("table_name >", value, "tableName");
            return (Criteria) this;
        }

        public Criteria andTableNameGreaterThanOrEqualTo(String value) {
            addCriterion("table_name >=", value, "tableName");
            return (Criteria) this;
        }

        public Criteria andTableNameLessThan(String value) {
            addCriterion("table_name <", value, "tableName");
            return (Criteria) this;
        }

        public Criteria andTableNameLessThanOrEqualTo(String value) {
            addCriterion("table_name <=", value, "tableName");
            return (Criteria) this;
        }

        public Criteria andTableNameLike(String value) {
            addCriterion("table_name like", value, "tableName");
            return (Criteria) this;
        }

        public Criteria andTableNameNotLike(String value) {
            addCriterion("table_name not like", value, "tableName");
            return (Criteria) this;
        }

        public Criteria andTableNameIn(List<String> values) {
            addCriterion("table_name in", values, "tableName");
            return (Criteria) this;
        }

        public Criteria andTableNameNotIn(List<String> values) {
            addCriterion("table_name not in", values, "tableName");
            return (Criteria) this;
        }

        public Criteria andTableNameBetween(String value1, String value2) {
            addCriterion("table_name between", value1, value2, "tableName");
            return (Criteria) this;
        }

        public Criteria andTableNameNotBetween(String value1, String value2) {
            addCriterion("table_name not between", value1, value2, "tableName");
            return (Criteria) this;
        }

        public Criteria andTableCommentIsNull() {
            addCriterion("table_comment is null");
            return (Criteria) this;
        }

        public Criteria andTableCommentIsNotNull() {
            addCriterion("table_comment is not null");
            return (Criteria) this;
        }

        public Criteria andTableCommentEqualTo(String value) {
            addCriterion("table_comment =", value, "tableComment");
            return (Criteria) this;
        }

        public Criteria andTableCommentNotEqualTo(String value) {
            addCriterion("table_comment <>", value, "tableComment");
            return (Criteria) this;
        }

        public Criteria andTableCommentGreaterThan(String value) {
            addCriterion("table_comment >", value, "tableComment");
            return (Criteria) this;
        }

        public Criteria andTableCommentGreaterThanOrEqualTo(String value) {
            addCriterion("table_comment >=", value, "tableComment");
            return (Criteria) this;
        }

        public Criteria andTableCommentLessThan(String value) {
            addCriterion("table_comment <", value, "tableComment");
            return (Criteria) this;
        }

        public Criteria andTableCommentLessThanOrEqualTo(String value) {
            addCriterion("table_comment <=", value, "tableComment");
            return (Criteria) this;
        }

        public Criteria andTableCommentLike(String value) {
            addCriterion("table_comment like", value, "tableComment");
            return (Criteria) this;
        }

        public Criteria andTableCommentNotLike(String value) {
            addCriterion("table_comment not like", value, "tableComment");
            return (Criteria) this;
        }

        public Criteria andTableCommentIn(List<String> values) {
            addCriterion("table_comment in", values, "tableComment");
            return (Criteria) this;
        }

        public Criteria andTableCommentNotIn(List<String> values) {
            addCriterion("table_comment not in", values, "tableComment");
            return (Criteria) this;
        }

        public Criteria andTableCommentBetween(String value1, String value2) {
            addCriterion("table_comment between", value1, value2, "tableComment");
            return (Criteria) this;
        }

        public Criteria andTableCommentNotBetween(String value1, String value2) {
            addCriterion("table_comment not between", value1, value2, "tableComment");
            return (Criteria) this;
        }

        public Criteria andIsNewIsNull() {
            addCriterion("is_new is null");
            return (Criteria) this;
        }

        public Criteria andIsNewIsNotNull() {
            addCriterion("is_new is not null");
            return (Criteria) this;
        }

        public Criteria andIsNewEqualTo(Byte value) {
            addCriterion("is_new =", value, "isNew");
            return (Criteria) this;
        }

        public Criteria andIsNewNotEqualTo(Byte value) {
            addCriterion("is_new <>", value, "isNew");
            return (Criteria) this;
        }

        public Criteria andIsNewGreaterThan(Byte value) {
            addCriterion("is_new >", value, "isNew");
            return (Criteria) this;
        }

        public Criteria andIsNewGreaterThanOrEqualTo(Byte value) {
            addCriterion("is_new >=", value, "isNew");
            return (Criteria) this;
        }

        public Criteria andIsNewLessThan(Byte value) {
            addCriterion("is_new <", value, "isNew");
            return (Criteria) this;
        }

        public Criteria andIsNewLessThanOrEqualTo(Byte value) {
            addCriterion("is_new <=", value, "isNew");
            return (Criteria) this;
        }

        public Criteria andIsNewIn(List<Byte> values) {
            addCriterion("is_new in", values, "isNew");
            return (Criteria) this;
        }

        public Criteria andIsNewNotIn(List<Byte> values) {
            addCriterion("is_new not in", values, "isNew");
            return (Criteria) this;
        }

        public Criteria andIsNewBetween(Byte value1, Byte value2) {
            addCriterion("is_new between", value1, value2, "isNew");
            return (Criteria) this;
        }

        public Criteria andIsNewNotBetween(Byte value1, Byte value2) {
            addCriterion("is_new not between", value1, value2, "isNew");
            return (Criteria) this;
        }

        public Criteria andOdsTableNameIsNull() {
            addCriterion("ods_table_name is null");
            return (Criteria) this;
        }

        public Criteria andOdsTableNameIsNotNull() {
            addCriterion("ods_table_name is not null");
            return (Criteria) this;
        }

        public Criteria andOdsTableNameEqualTo(String value) {
            addCriterion("ods_table_name =", value, "odsTableName");
            return (Criteria) this;
        }

        public Criteria andOdsTableNameNotEqualTo(String value) {
            addCriterion("ods_table_name <>", value, "odsTableName");
            return (Criteria) this;
        }

        public Criteria andOdsTableNameGreaterThan(String value) {
            addCriterion("ods_table_name >", value, "odsTableName");
            return (Criteria) this;
        }

        public Criteria andOdsTableNameGreaterThanOrEqualTo(String value) {
            addCriterion("ods_table_name >=", value, "odsTableName");
            return (Criteria) this;
        }

        public Criteria andOdsTableNameLessThan(String value) {
            addCriterion("ods_table_name <", value, "odsTableName");
            return (Criteria) this;
        }

        public Criteria andOdsTableNameLessThanOrEqualTo(String value) {
            addCriterion("ods_table_name <=", value, "odsTableName");
            return (Criteria) this;
        }

        public Criteria andOdsTableNameLike(String value) {
            addCriterion("ods_table_name like", value, "odsTableName");
            return (Criteria) this;
        }

        public Criteria andOdsTableNameNotLike(String value) {
            addCriterion("ods_table_name not like", value, "odsTableName");
            return (Criteria) this;
        }

        public Criteria andOdsTableNameIn(List<String> values) {
            addCriterion("ods_table_name in", values, "odsTableName");
            return (Criteria) this;
        }

        public Criteria andOdsTableNameNotIn(List<String> values) {
            addCriterion("ods_table_name not in", values, "odsTableName");
            return (Criteria) this;
        }

        public Criteria andOdsTableNameBetween(String value1, String value2) {
            addCriterion("ods_table_name between", value1, value2, "odsTableName");
            return (Criteria) this;
        }

        public Criteria andOdsTableNameNotBetween(String value1, String value2) {
            addCriterion("ods_table_name not between", value1, value2, "odsTableName");
            return (Criteria) this;
        }

        public Criteria andOdsSnyStatusIsNull() {
            addCriterion("ods_sny_status is null");
            return (Criteria) this;
        }

        public Criteria andOdsSnyStatusIsNotNull() {
            addCriterion("ods_sny_status is not null");
            return (Criteria) this;
        }

        public Criteria andOdsSnyStatusEqualTo(String value) {
            addCriterion("ods_sny_status =", value, "odsSnyStatus");
            return (Criteria) this;
        }

        public Criteria andOdsSnyStatusNotEqualTo(String value) {
            addCriterion("ods_sny_status <>", value, "odsSnyStatus");
            return (Criteria) this;
        }

        public Criteria andOdsSnyStatusGreaterThan(String value) {
            addCriterion("ods_sny_status >", value, "odsSnyStatus");
            return (Criteria) this;
        }

        public Criteria andOdsSnyStatusGreaterThanOrEqualTo(String value) {
            addCriterion("ods_sny_status >=", value, "odsSnyStatus");
            return (Criteria) this;
        }

        public Criteria andOdsSnyStatusLessThan(String value) {
            addCriterion("ods_sny_status <", value, "odsSnyStatus");
            return (Criteria) this;
        }

        public Criteria andOdsSnyStatusLessThanOrEqualTo(String value) {
            addCriterion("ods_sny_status <=", value, "odsSnyStatus");
            return (Criteria) this;
        }

        public Criteria andOdsSnyStatusLike(String value) {
            addCriterion("ods_sny_status like", value, "odsSnyStatus");
            return (Criteria) this;
        }

        public Criteria andOdsSnyStatusNotLike(String value) {
            addCriterion("ods_sny_status not like", value, "odsSnyStatus");
            return (Criteria) this;
        }

        public Criteria andOdsSnyStatusIn(List<String> values) {
            addCriterion("ods_sny_status in", values, "odsSnyStatus");
            return (Criteria) this;
        }

        public Criteria andOdsSnyStatusNotIn(List<String> values) {
            addCriterion("ods_sny_status not in", values, "odsSnyStatus");
            return (Criteria) this;
        }

        public Criteria andOdsSnyStatusBetween(String value1, String value2) {
            addCriterion("ods_sny_status between", value1, value2, "odsSnyStatus");
            return (Criteria) this;
        }

        public Criteria andOdsSnyStatusNotBetween(String value1, String value2) {
            addCriterion("ods_sny_status not between", value1, value2, "odsSnyStatus");
            return (Criteria) this;
        }

        public Criteria andOdsDatabaseIsNull() {
            addCriterion("ods_database is null");
            return (Criteria) this;
        }

        public Criteria andOdsDatabaseIsNotNull() {
            addCriterion("ods_database is not null");
            return (Criteria) this;
        }

        public Criteria andOdsDatabaseEqualTo(String value) {
            addCriterion("ods_database =", value, "odsDatabase");
            return (Criteria) this;
        }

        public Criteria andOdsDatabaseNotEqualTo(String value) {
            addCriterion("ods_database <>", value, "odsDatabase");
            return (Criteria) this;
        }

        public Criteria andOdsDatabaseGreaterThan(String value) {
            addCriterion("ods_database >", value, "odsDatabase");
            return (Criteria) this;
        }

        public Criteria andOdsDatabaseGreaterThanOrEqualTo(String value) {
            addCriterion("ods_database >=", value, "odsDatabase");
            return (Criteria) this;
        }

        public Criteria andOdsDatabaseLessThan(String value) {
            addCriterion("ods_database <", value, "odsDatabase");
            return (Criteria) this;
        }

        public Criteria andOdsDatabaseLessThanOrEqualTo(String value) {
            addCriterion("ods_database <=", value, "odsDatabase");
            return (Criteria) this;
        }

        public Criteria andOdsDatabaseLike(String value) {
            addCriterion("ods_database like", value, "odsDatabase");
            return (Criteria) this;
        }

        public Criteria andOdsDatabaseNotLike(String value) {
            addCriterion("ods_database not like", value, "odsDatabase");
            return (Criteria) this;
        }

        public Criteria andOdsDatabaseIn(List<String> values) {
            addCriterion("ods_database in", values, "odsDatabase");
            return (Criteria) this;
        }

        public Criteria andOdsDatabaseNotIn(List<String> values) {
            addCriterion("ods_database not in", values, "odsDatabase");
            return (Criteria) this;
        }

        public Criteria andOdsDatabaseBetween(String value1, String value2) {
            addCriterion("ods_database between", value1, value2, "odsDatabase");
            return (Criteria) this;
        }

        public Criteria andOdsDatabaseNotBetween(String value1, String value2) {
            addCriterion("ods_database not between", value1, value2, "odsDatabase");
            return (Criteria) this;
        }

        public Criteria andTableTransNameIsNull() {
            addCriterion("table_trans_name is null");
            return (Criteria) this;
        }

        public Criteria andTableTransNameIsNotNull() {
            addCriterion("table_trans_name is not null");
            return (Criteria) this;
        }

        public Criteria andTableTransNameEqualTo(String value) {
            addCriterion("table_trans_name =", value, "tableTransName");
            return (Criteria) this;
        }

        public Criteria andTableTransNameNotEqualTo(String value) {
            addCriterion("table_trans_name <>", value, "tableTransName");
            return (Criteria) this;
        }

        public Criteria andTableTransNameGreaterThan(String value) {
            addCriterion("table_trans_name >", value, "tableTransName");
            return (Criteria) this;
        }

        public Criteria andTableTransNameGreaterThanOrEqualTo(String value) {
            addCriterion("table_trans_name >=", value, "tableTransName");
            return (Criteria) this;
        }

        public Criteria andTableTransNameLessThan(String value) {
            addCriterion("table_trans_name <", value, "tableTransName");
            return (Criteria) this;
        }

        public Criteria andTableTransNameLessThanOrEqualTo(String value) {
            addCriterion("table_trans_name <=", value, "tableTransName");
            return (Criteria) this;
        }

        public Criteria andTableTransNameLike(String value) {
            addCriterion("table_trans_name like", value, "tableTransName");
            return (Criteria) this;
        }

        public Criteria andTableTransNameNotLike(String value) {
            addCriterion("table_trans_name not like", value, "tableTransName");
            return (Criteria) this;
        }

        public Criteria andTableTransNameIn(List<String> values) {
            addCriterion("table_trans_name in", values, "tableTransName");
            return (Criteria) this;
        }

        public Criteria andTableTransNameNotIn(List<String> values) {
            addCriterion("table_trans_name not in", values, "tableTransName");
            return (Criteria) this;
        }

        public Criteria andTableTransNameBetween(String value1, String value2) {
            addCriterion("table_trans_name between", value1, value2, "tableTransName");
            return (Criteria) this;
        }

        public Criteria andTableTransNameNotBetween(String value1, String value2) {
            addCriterion("table_trans_name not between", value1, value2, "tableTransName");
            return (Criteria) this;
        }

        public Criteria andDwdThemeIsNull() {
            addCriterion("dwd_theme is null");
            return (Criteria) this;
        }

        public Criteria andDwdThemeIsNotNull() {
            addCriterion("dwd_theme is not null");
            return (Criteria) this;
        }

        public Criteria andDwdThemeEqualTo(String value) {
            addCriterion("dwd_theme =", value, "dwdTheme");
            return (Criteria) this;
        }

        public Criteria andDwdThemeNotEqualTo(String value) {
            addCriterion("dwd_theme <>", value, "dwdTheme");
            return (Criteria) this;
        }

        public Criteria andDwdThemeGreaterThan(String value) {
            addCriterion("dwd_theme >", value, "dwdTheme");
            return (Criteria) this;
        }

        public Criteria andDwdThemeGreaterThanOrEqualTo(String value) {
            addCriterion("dwd_theme >=", value, "dwdTheme");
            return (Criteria) this;
        }

        public Criteria andDwdThemeLessThan(String value) {
            addCriterion("dwd_theme <", value, "dwdTheme");
            return (Criteria) this;
        }

        public Criteria andDwdThemeLessThanOrEqualTo(String value) {
            addCriterion("dwd_theme <=", value, "dwdTheme");
            return (Criteria) this;
        }

        public Criteria andDwdThemeLike(String value) {
            addCriterion("dwd_theme like", value, "dwdTheme");
            return (Criteria) this;
        }

        public Criteria andDwdThemeNotLike(String value) {
            addCriterion("dwd_theme not like", value, "dwdTheme");
            return (Criteria) this;
        }

        public Criteria andDwdThemeIn(List<String> values) {
            addCriterion("dwd_theme in", values, "dwdTheme");
            return (Criteria) this;
        }

        public Criteria andDwdThemeNotIn(List<String> values) {
            addCriterion("dwd_theme not in", values, "dwdTheme");
            return (Criteria) this;
        }

        public Criteria andDwdThemeBetween(String value1, String value2) {
            addCriterion("dwd_theme between", value1, value2, "dwdTheme");
            return (Criteria) this;
        }

        public Criteria andDwdThemeNotBetween(String value1, String value2) {
            addCriterion("dwd_theme not between", value1, value2, "dwdTheme");
            return (Criteria) this;
        }

        public Criteria andDwdDatabaseIsNull() {
            addCriterion("dwd_database is null");
            return (Criteria) this;
        }

        public Criteria andDwdDatabaseIsNotNull() {
            addCriterion("dwd_database is not null");
            return (Criteria) this;
        }

        public Criteria andDwdDatabaseEqualTo(String value) {
            addCriterion("dwd_database =", value, "dwdDatabase");
            return (Criteria) this;
        }

        public Criteria andDwdDatabaseNotEqualTo(String value) {
            addCriterion("dwd_database <>", value, "dwdDatabase");
            return (Criteria) this;
        }

        public Criteria andDwdDatabaseGreaterThan(String value) {
            addCriterion("dwd_database >", value, "dwdDatabase");
            return (Criteria) this;
        }

        public Criteria andDwdDatabaseGreaterThanOrEqualTo(String value) {
            addCriterion("dwd_database >=", value, "dwdDatabase");
            return (Criteria) this;
        }

        public Criteria andDwdDatabaseLessThan(String value) {
            addCriterion("dwd_database <", value, "dwdDatabase");
            return (Criteria) this;
        }

        public Criteria andDwdDatabaseLessThanOrEqualTo(String value) {
            addCriterion("dwd_database <=", value, "dwdDatabase");
            return (Criteria) this;
        }

        public Criteria andDwdDatabaseLike(String value) {
            addCriterion("dwd_database like", value, "dwdDatabase");
            return (Criteria) this;
        }

        public Criteria andDwdDatabaseNotLike(String value) {
            addCriterion("dwd_database not like", value, "dwdDatabase");
            return (Criteria) this;
        }

        public Criteria andDwdDatabaseIn(List<String> values) {
            addCriterion("dwd_database in", values, "dwdDatabase");
            return (Criteria) this;
        }

        public Criteria andDwdDatabaseNotIn(List<String> values) {
            addCriterion("dwd_database not in", values, "dwdDatabase");
            return (Criteria) this;
        }

        public Criteria andDwdDatabaseBetween(String value1, String value2) {
            addCriterion("dwd_database between", value1, value2, "dwdDatabase");
            return (Criteria) this;
        }

        public Criteria andDwdDatabaseNotBetween(String value1, String value2) {
            addCriterion("dwd_database not between", value1, value2, "dwdDatabase");
            return (Criteria) this;
        }

        public Criteria andDwdIncPartitionValIsNull() {
            addCriterion("dwd_inc_partition_val is null");
            return (Criteria) this;
        }

        public Criteria andDwdIncPartitionValIsNotNull() {
            addCriterion("dwd_inc_partition_val is not null");
            return (Criteria) this;
        }

        public Criteria andDwdIncPartitionValEqualTo(String value) {
            addCriterion("dwd_inc_partition_val =", value, "dwdIncPartitionVal");
            return (Criteria) this;
        }

        public Criteria andDwdIncPartitionValNotEqualTo(String value) {
            addCriterion("dwd_inc_partition_val <>", value, "dwdIncPartitionVal");
            return (Criteria) this;
        }

        public Criteria andDwdIncPartitionValGreaterThan(String value) {
            addCriterion("dwd_inc_partition_val >", value, "dwdIncPartitionVal");
            return (Criteria) this;
        }

        public Criteria andDwdIncPartitionValGreaterThanOrEqualTo(String value) {
            addCriterion("dwd_inc_partition_val >=", value, "dwdIncPartitionVal");
            return (Criteria) this;
        }

        public Criteria andDwdIncPartitionValLessThan(String value) {
            addCriterion("dwd_inc_partition_val <", value, "dwdIncPartitionVal");
            return (Criteria) this;
        }

        public Criteria andDwdIncPartitionValLessThanOrEqualTo(String value) {
            addCriterion("dwd_inc_partition_val <=", value, "dwdIncPartitionVal");
            return (Criteria) this;
        }

        public Criteria andDwdIncPartitionValLike(String value) {
            addCriterion("dwd_inc_partition_val like", value, "dwdIncPartitionVal");
            return (Criteria) this;
        }

        public Criteria andDwdIncPartitionValNotLike(String value) {
            addCriterion("dwd_inc_partition_val not like", value, "dwdIncPartitionVal");
            return (Criteria) this;
        }

        public Criteria andDwdIncPartitionValIn(List<String> values) {
            addCriterion("dwd_inc_partition_val in", values, "dwdIncPartitionVal");
            return (Criteria) this;
        }

        public Criteria andDwdIncPartitionValNotIn(List<String> values) {
            addCriterion("dwd_inc_partition_val not in", values, "dwdIncPartitionVal");
            return (Criteria) this;
        }

        public Criteria andDwdIncPartitionValBetween(String value1, String value2) {
            addCriterion("dwd_inc_partition_val between", value1, value2, "dwdIncPartitionVal");
            return (Criteria) this;
        }

        public Criteria andDwdIncPartitionValNotBetween(String value1, String value2) {
            addCriterion("dwd_inc_partition_val not between", value1, value2, "dwdIncPartitionVal");
            return (Criteria) this;
        }

        public Criteria andDateCreateIsNull() {
            addCriterion("date_create is null");
            return (Criteria) this;
        }

        public Criteria andDateCreateIsNotNull() {
            addCriterion("date_create is not null");
            return (Criteria) this;
        }

        public Criteria andDateCreateEqualTo(Date value) {
            addCriterion("date_create =", value, "dateCreate");
            return (Criteria) this;
        }

        public Criteria andDateCreateNotEqualTo(Date value) {
            addCriterion("date_create <>", value, "dateCreate");
            return (Criteria) this;
        }

        public Criteria andDateCreateGreaterThan(Date value) {
            addCriterion("date_create >", value, "dateCreate");
            return (Criteria) this;
        }

        public Criteria andDateCreateGreaterThanOrEqualTo(Date value) {
            addCriterion("date_create >=", value, "dateCreate");
            return (Criteria) this;
        }

        public Criteria andDateCreateLessThan(Date value) {
            addCriterion("date_create <", value, "dateCreate");
            return (Criteria) this;
        }

        public Criteria andDateCreateLessThanOrEqualTo(Date value) {
            addCriterion("date_create <=", value, "dateCreate");
            return (Criteria) this;
        }

        public Criteria andDateCreateIn(List<Date> values) {
            addCriterion("date_create in", values, "dateCreate");
            return (Criteria) this;
        }

        public Criteria andDateCreateNotIn(List<Date> values) {
            addCriterion("date_create not in", values, "dateCreate");
            return (Criteria) this;
        }

        public Criteria andDateCreateBetween(Date value1, Date value2) {
            addCriterion("date_create between", value1, value2, "dateCreate");
            return (Criteria) this;
        }

        public Criteria andDateCreateNotBetween(Date value1, Date value2) {
            addCriterion("date_create not between", value1, value2, "dateCreate");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}