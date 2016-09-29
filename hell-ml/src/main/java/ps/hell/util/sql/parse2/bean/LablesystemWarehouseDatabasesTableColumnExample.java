package ps.hell.util.sql.parse2.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LablesystemWarehouseDatabasesTableColumnExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public LablesystemWarehouseDatabasesTableColumnExample() {
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

        public Criteria andTableIdIsNull() {
            addCriterion("table_id is null");
            return (Criteria) this;
        }

        public Criteria andTableIdIsNotNull() {
            addCriterion("table_id is not null");
            return (Criteria) this;
        }

        public Criteria andTableIdEqualTo(Long value) {
            addCriterion("table_id =", value, "tableId");
            return (Criteria) this;
        }

        public Criteria andTableIdNotEqualTo(Long value) {
            addCriterion("table_id <>", value, "tableId");
            return (Criteria) this;
        }

        public Criteria andTableIdGreaterThan(Long value) {
            addCriterion("table_id >", value, "tableId");
            return (Criteria) this;
        }

        public Criteria andTableIdGreaterThanOrEqualTo(Long value) {
            addCriterion("table_id >=", value, "tableId");
            return (Criteria) this;
        }

        public Criteria andTableIdLessThan(Long value) {
            addCriterion("table_id <", value, "tableId");
            return (Criteria) this;
        }

        public Criteria andTableIdLessThanOrEqualTo(Long value) {
            addCriterion("table_id <=", value, "tableId");
            return (Criteria) this;
        }

        public Criteria andTableIdIn(List<Long> values) {
            addCriterion("table_id in", values, "tableId");
            return (Criteria) this;
        }

        public Criteria andTableIdNotIn(List<Long> values) {
            addCriterion("table_id not in", values, "tableId");
            return (Criteria) this;
        }

        public Criteria andTableIdBetween(Long value1, Long value2) {
            addCriterion("table_id between", value1, value2, "tableId");
            return (Criteria) this;
        }

        public Criteria andTableIdNotBetween(Long value1, Long value2) {
            addCriterion("table_id not between", value1, value2, "tableId");
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

        public Criteria andColumnStatusIsNull() {
            addCriterion("column_status is null");
            return (Criteria) this;
        }

        public Criteria andColumnStatusIsNotNull() {
            addCriterion("column_status is not null");
            return (Criteria) this;
        }

        public Criteria andColumnStatusEqualTo(Integer value) {
            addCriterion("column_status =", value, "columnStatus");
            return (Criteria) this;
        }

        public Criteria andColumnStatusNotEqualTo(Integer value) {
            addCriterion("column_status <>", value, "columnStatus");
            return (Criteria) this;
        }

        public Criteria andColumnStatusGreaterThan(Integer value) {
            addCriterion("column_status >", value, "columnStatus");
            return (Criteria) this;
        }

        public Criteria andColumnStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("column_status >=", value, "columnStatus");
            return (Criteria) this;
        }

        public Criteria andColumnStatusLessThan(Integer value) {
            addCriterion("column_status <", value, "columnStatus");
            return (Criteria) this;
        }

        public Criteria andColumnStatusLessThanOrEqualTo(Integer value) {
            addCriterion("column_status <=", value, "columnStatus");
            return (Criteria) this;
        }

        public Criteria andColumnStatusIn(List<Integer> values) {
            addCriterion("column_status in", values, "columnStatus");
            return (Criteria) this;
        }

        public Criteria andColumnStatusNotIn(List<Integer> values) {
            addCriterion("column_status not in", values, "columnStatus");
            return (Criteria) this;
        }

        public Criteria andColumnStatusBetween(Integer value1, Integer value2) {
            addCriterion("column_status between", value1, value2, "columnStatus");
            return (Criteria) this;
        }

        public Criteria andColumnStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("column_status not between", value1, value2, "columnStatus");
            return (Criteria) this;
        }

        public Criteria andColumnNameIsNull() {
            addCriterion("column_name is null");
            return (Criteria) this;
        }

        public Criteria andColumnNameIsNotNull() {
            addCriterion("column_name is not null");
            return (Criteria) this;
        }

        public Criteria andColumnNameEqualTo(String value) {
            addCriterion("column_name =", value, "columnName");
            return (Criteria) this;
        }

        public Criteria andColumnNameNotEqualTo(String value) {
            addCriterion("column_name <>", value, "columnName");
            return (Criteria) this;
        }

        public Criteria andColumnNameGreaterThan(String value) {
            addCriterion("column_name >", value, "columnName");
            return (Criteria) this;
        }

        public Criteria andColumnNameGreaterThanOrEqualTo(String value) {
            addCriterion("column_name >=", value, "columnName");
            return (Criteria) this;
        }

        public Criteria andColumnNameLessThan(String value) {
            addCriterion("column_name <", value, "columnName");
            return (Criteria) this;
        }

        public Criteria andColumnNameLessThanOrEqualTo(String value) {
            addCriterion("column_name <=", value, "columnName");
            return (Criteria) this;
        }

        public Criteria andColumnNameLike(String value) {
            addCriterion("column_name like", value, "columnName");
            return (Criteria) this;
        }

        public Criteria andColumnNameNotLike(String value) {
            addCriterion("column_name not like", value, "columnName");
            return (Criteria) this;
        }

        public Criteria andColumnNameIn(List<String> values) {
            addCriterion("column_name in", values, "columnName");
            return (Criteria) this;
        }

        public Criteria andColumnNameNotIn(List<String> values) {
            addCriterion("column_name not in", values, "columnName");
            return (Criteria) this;
        }

        public Criteria andColumnNameBetween(String value1, String value2) {
            addCriterion("column_name between", value1, value2, "columnName");
            return (Criteria) this;
        }

        public Criteria andColumnNameNotBetween(String value1, String value2) {
            addCriterion("column_name not between", value1, value2, "columnName");
            return (Criteria) this;
        }

        public Criteria andColumnTypeIsNull() {
            addCriterion("column_type is null");
            return (Criteria) this;
        }

        public Criteria andColumnTypeIsNotNull() {
            addCriterion("column_type is not null");
            return (Criteria) this;
        }

        public Criteria andColumnTypeEqualTo(String value) {
            addCriterion("column_type =", value, "columnType");
            return (Criteria) this;
        }

        public Criteria andColumnTypeNotEqualTo(String value) {
            addCriterion("column_type <>", value, "columnType");
            return (Criteria) this;
        }

        public Criteria andColumnTypeGreaterThan(String value) {
            addCriterion("column_type >", value, "columnType");
            return (Criteria) this;
        }

        public Criteria andColumnTypeGreaterThanOrEqualTo(String value) {
            addCriterion("column_type >=", value, "columnType");
            return (Criteria) this;
        }

        public Criteria andColumnTypeLessThan(String value) {
            addCriterion("column_type <", value, "columnType");
            return (Criteria) this;
        }

        public Criteria andColumnTypeLessThanOrEqualTo(String value) {
            addCriterion("column_type <=", value, "columnType");
            return (Criteria) this;
        }

        public Criteria andColumnTypeLike(String value) {
            addCriterion("column_type like", value, "columnType");
            return (Criteria) this;
        }

        public Criteria andColumnTypeNotLike(String value) {
            addCriterion("column_type not like", value, "columnType");
            return (Criteria) this;
        }

        public Criteria andColumnTypeIn(List<String> values) {
            addCriterion("column_type in", values, "columnType");
            return (Criteria) this;
        }

        public Criteria andColumnTypeNotIn(List<String> values) {
            addCriterion("column_type not in", values, "columnType");
            return (Criteria) this;
        }

        public Criteria andColumnTypeBetween(String value1, String value2) {
            addCriterion("column_type between", value1, value2, "columnType");
            return (Criteria) this;
        }

        public Criteria andColumnTypeNotBetween(String value1, String value2) {
            addCriterion("column_type not between", value1, value2, "columnType");
            return (Criteria) this;
        }

        public Criteria andColumnLengthIsNull() {
            addCriterion("column_length is null");
            return (Criteria) this;
        }

        public Criteria andColumnLengthIsNotNull() {
            addCriterion("column_length is not null");
            return (Criteria) this;
        }

        public Criteria andColumnLengthEqualTo(String value) {
            addCriterion("column_length =", value, "columnLength");
            return (Criteria) this;
        }

        public Criteria andColumnLengthNotEqualTo(String value) {
            addCriterion("column_length <>", value, "columnLength");
            return (Criteria) this;
        }

        public Criteria andColumnLengthGreaterThan(String value) {
            addCriterion("column_length >", value, "columnLength");
            return (Criteria) this;
        }

        public Criteria andColumnLengthGreaterThanOrEqualTo(String value) {
            addCriterion("column_length >=", value, "columnLength");
            return (Criteria) this;
        }

        public Criteria andColumnLengthLessThan(String value) {
            addCriterion("column_length <", value, "columnLength");
            return (Criteria) this;
        }

        public Criteria andColumnLengthLessThanOrEqualTo(String value) {
            addCriterion("column_length <=", value, "columnLength");
            return (Criteria) this;
        }

        public Criteria andColumnLengthLike(String value) {
            addCriterion("column_length like", value, "columnLength");
            return (Criteria) this;
        }

        public Criteria andColumnLengthNotLike(String value) {
            addCriterion("column_length not like", value, "columnLength");
            return (Criteria) this;
        }

        public Criteria andColumnLengthIn(List<String> values) {
            addCriterion("column_length in", values, "columnLength");
            return (Criteria) this;
        }

        public Criteria andColumnLengthNotIn(List<String> values) {
            addCriterion("column_length not in", values, "columnLength");
            return (Criteria) this;
        }

        public Criteria andColumnLengthBetween(String value1, String value2) {
            addCriterion("column_length between", value1, value2, "columnLength");
            return (Criteria) this;
        }

        public Criteria andColumnLengthNotBetween(String value1, String value2) {
            addCriterion("column_length not between", value1, value2, "columnLength");
            return (Criteria) this;
        }

        public Criteria andColumnPrecisionIsNull() {
            addCriterion("column_precision is null");
            return (Criteria) this;
        }

        public Criteria andColumnPrecisionIsNotNull() {
            addCriterion("column_precision is not null");
            return (Criteria) this;
        }

        public Criteria andColumnPrecisionEqualTo(String value) {
            addCriterion("column_precision =", value, "columnPrecision");
            return (Criteria) this;
        }

        public Criteria andColumnPrecisionNotEqualTo(String value) {
            addCriterion("column_precision <>", value, "columnPrecision");
            return (Criteria) this;
        }

        public Criteria andColumnPrecisionGreaterThan(String value) {
            addCriterion("column_precision >", value, "columnPrecision");
            return (Criteria) this;
        }

        public Criteria andColumnPrecisionGreaterThanOrEqualTo(String value) {
            addCriterion("column_precision >=", value, "columnPrecision");
            return (Criteria) this;
        }

        public Criteria andColumnPrecisionLessThan(String value) {
            addCriterion("column_precision <", value, "columnPrecision");
            return (Criteria) this;
        }

        public Criteria andColumnPrecisionLessThanOrEqualTo(String value) {
            addCriterion("column_precision <=", value, "columnPrecision");
            return (Criteria) this;
        }

        public Criteria andColumnPrecisionLike(String value) {
            addCriterion("column_precision like", value, "columnPrecision");
            return (Criteria) this;
        }

        public Criteria andColumnPrecisionNotLike(String value) {
            addCriterion("column_precision not like", value, "columnPrecision");
            return (Criteria) this;
        }

        public Criteria andColumnPrecisionIn(List<String> values) {
            addCriterion("column_precision in", values, "columnPrecision");
            return (Criteria) this;
        }

        public Criteria andColumnPrecisionNotIn(List<String> values) {
            addCriterion("column_precision not in", values, "columnPrecision");
            return (Criteria) this;
        }

        public Criteria andColumnPrecisionBetween(String value1, String value2) {
            addCriterion("column_precision between", value1, value2, "columnPrecision");
            return (Criteria) this;
        }

        public Criteria andColumnPrecisionNotBetween(String value1, String value2) {
            addCriterion("column_precision not between", value1, value2, "columnPrecision");
            return (Criteria) this;
        }

        public Criteria andColumnCommentIsNull() {
            addCriterion("column_comment is null");
            return (Criteria) this;
        }

        public Criteria andColumnCommentIsNotNull() {
            addCriterion("column_comment is not null");
            return (Criteria) this;
        }

        public Criteria andColumnCommentEqualTo(String value) {
            addCriterion("column_comment =", value, "columnComment");
            return (Criteria) this;
        }

        public Criteria andColumnCommentNotEqualTo(String value) {
            addCriterion("column_comment <>", value, "columnComment");
            return (Criteria) this;
        }

        public Criteria andColumnCommentGreaterThan(String value) {
            addCriterion("column_comment >", value, "columnComment");
            return (Criteria) this;
        }

        public Criteria andColumnCommentGreaterThanOrEqualTo(String value) {
            addCriterion("column_comment >=", value, "columnComment");
            return (Criteria) this;
        }

        public Criteria andColumnCommentLessThan(String value) {
            addCriterion("column_comment <", value, "columnComment");
            return (Criteria) this;
        }

        public Criteria andColumnCommentLessThanOrEqualTo(String value) {
            addCriterion("column_comment <=", value, "columnComment");
            return (Criteria) this;
        }

        public Criteria andColumnCommentLike(String value) {
            addCriterion("column_comment like", value, "columnComment");
            return (Criteria) this;
        }

        public Criteria andColumnCommentNotLike(String value) {
            addCriterion("column_comment not like", value, "columnComment");
            return (Criteria) this;
        }

        public Criteria andColumnCommentIn(List<String> values) {
            addCriterion("column_comment in", values, "columnComment");
            return (Criteria) this;
        }

        public Criteria andColumnCommentNotIn(List<String> values) {
            addCriterion("column_comment not in", values, "columnComment");
            return (Criteria) this;
        }

        public Criteria andColumnCommentBetween(String value1, String value2) {
            addCriterion("column_comment between", value1, value2, "columnComment");
            return (Criteria) this;
        }

        public Criteria andColumnCommentNotBetween(String value1, String value2) {
            addCriterion("column_comment not between", value1, value2, "columnComment");
            return (Criteria) this;
        }

        public Criteria andColumnNameMapIsNull() {
            addCriterion("column_name_map is null");
            return (Criteria) this;
        }

        public Criteria andColumnNameMapIsNotNull() {
            addCriterion("column_name_map is not null");
            return (Criteria) this;
        }

        public Criteria andColumnNameMapEqualTo(String value) {
            addCriterion("column_name_map =", value, "columnNameMap");
            return (Criteria) this;
        }

        public Criteria andColumnNameMapNotEqualTo(String value) {
            addCriterion("column_name_map <>", value, "columnNameMap");
            return (Criteria) this;
        }

        public Criteria andColumnNameMapGreaterThan(String value) {
            addCriterion("column_name_map >", value, "columnNameMap");
            return (Criteria) this;
        }

        public Criteria andColumnNameMapGreaterThanOrEqualTo(String value) {
            addCriterion("column_name_map >=", value, "columnNameMap");
            return (Criteria) this;
        }

        public Criteria andColumnNameMapLessThan(String value) {
            addCriterion("column_name_map <", value, "columnNameMap");
            return (Criteria) this;
        }

        public Criteria andColumnNameMapLessThanOrEqualTo(String value) {
            addCriterion("column_name_map <=", value, "columnNameMap");
            return (Criteria) this;
        }

        public Criteria andColumnNameMapLike(String value) {
            addCriterion("column_name_map like", value, "columnNameMap");
            return (Criteria) this;
        }

        public Criteria andColumnNameMapNotLike(String value) {
            addCriterion("column_name_map not like", value, "columnNameMap");
            return (Criteria) this;
        }

        public Criteria andColumnNameMapIn(List<String> values) {
            addCriterion("column_name_map in", values, "columnNameMap");
            return (Criteria) this;
        }

        public Criteria andColumnNameMapNotIn(List<String> values) {
            addCriterion("column_name_map not in", values, "columnNameMap");
            return (Criteria) this;
        }

        public Criteria andColumnNameMapBetween(String value1, String value2) {
            addCriterion("column_name_map between", value1, value2, "columnNameMap");
            return (Criteria) this;
        }

        public Criteria andColumnNameMapNotBetween(String value1, String value2) {
            addCriterion("column_name_map not between", value1, value2, "columnNameMap");
            return (Criteria) this;
        }

        public Criteria andColumnTypeMapIsNull() {
            addCriterion("column_type_map is null");
            return (Criteria) this;
        }

        public Criteria andColumnTypeMapIsNotNull() {
            addCriterion("column_type_map is not null");
            return (Criteria) this;
        }

        public Criteria andColumnTypeMapEqualTo(String value) {
            addCriterion("column_type_map =", value, "columnTypeMap");
            return (Criteria) this;
        }

        public Criteria andColumnTypeMapNotEqualTo(String value) {
            addCriterion("column_type_map <>", value, "columnTypeMap");
            return (Criteria) this;
        }

        public Criteria andColumnTypeMapGreaterThan(String value) {
            addCriterion("column_type_map >", value, "columnTypeMap");
            return (Criteria) this;
        }

        public Criteria andColumnTypeMapGreaterThanOrEqualTo(String value) {
            addCriterion("column_type_map >=", value, "columnTypeMap");
            return (Criteria) this;
        }

        public Criteria andColumnTypeMapLessThan(String value) {
            addCriterion("column_type_map <", value, "columnTypeMap");
            return (Criteria) this;
        }

        public Criteria andColumnTypeMapLessThanOrEqualTo(String value) {
            addCriterion("column_type_map <=", value, "columnTypeMap");
            return (Criteria) this;
        }

        public Criteria andColumnTypeMapLike(String value) {
            addCriterion("column_type_map like", value, "columnTypeMap");
            return (Criteria) this;
        }

        public Criteria andColumnTypeMapNotLike(String value) {
            addCriterion("column_type_map not like", value, "columnTypeMap");
            return (Criteria) this;
        }

        public Criteria andColumnTypeMapIn(List<String> values) {
            addCriterion("column_type_map in", values, "columnTypeMap");
            return (Criteria) this;
        }

        public Criteria andColumnTypeMapNotIn(List<String> values) {
            addCriterion("column_type_map not in", values, "columnTypeMap");
            return (Criteria) this;
        }

        public Criteria andColumnTypeMapBetween(String value1, String value2) {
            addCriterion("column_type_map between", value1, value2, "columnTypeMap");
            return (Criteria) this;
        }

        public Criteria andColumnTypeMapNotBetween(String value1, String value2) {
            addCriterion("column_type_map not between", value1, value2, "columnTypeMap");
            return (Criteria) this;
        }

        public Criteria andColumnCommentMapIsNull() {
            addCriterion("column_comment_map is null");
            return (Criteria) this;
        }

        public Criteria andColumnCommentMapIsNotNull() {
            addCriterion("column_comment_map is not null");
            return (Criteria) this;
        }

        public Criteria andColumnCommentMapEqualTo(String value) {
            addCriterion("column_comment_map =", value, "columnCommentMap");
            return (Criteria) this;
        }

        public Criteria andColumnCommentMapNotEqualTo(String value) {
            addCriterion("column_comment_map <>", value, "columnCommentMap");
            return (Criteria) this;
        }

        public Criteria andColumnCommentMapGreaterThan(String value) {
            addCriterion("column_comment_map >", value, "columnCommentMap");
            return (Criteria) this;
        }

        public Criteria andColumnCommentMapGreaterThanOrEqualTo(String value) {
            addCriterion("column_comment_map >=", value, "columnCommentMap");
            return (Criteria) this;
        }

        public Criteria andColumnCommentMapLessThan(String value) {
            addCriterion("column_comment_map <", value, "columnCommentMap");
            return (Criteria) this;
        }

        public Criteria andColumnCommentMapLessThanOrEqualTo(String value) {
            addCriterion("column_comment_map <=", value, "columnCommentMap");
            return (Criteria) this;
        }

        public Criteria andColumnCommentMapLike(String value) {
            addCriterion("column_comment_map like", value, "columnCommentMap");
            return (Criteria) this;
        }

        public Criteria andColumnCommentMapNotLike(String value) {
            addCriterion("column_comment_map not like", value, "columnCommentMap");
            return (Criteria) this;
        }

        public Criteria andColumnCommentMapIn(List<String> values) {
            addCriterion("column_comment_map in", values, "columnCommentMap");
            return (Criteria) this;
        }

        public Criteria andColumnCommentMapNotIn(List<String> values) {
            addCriterion("column_comment_map not in", values, "columnCommentMap");
            return (Criteria) this;
        }

        public Criteria andColumnCommentMapBetween(String value1, String value2) {
            addCriterion("column_comment_map between", value1, value2, "columnCommentMap");
            return (Criteria) this;
        }

        public Criteria andColumnCommentMapNotBetween(String value1, String value2) {
            addCriterion("column_comment_map not between", value1, value2, "columnCommentMap");
            return (Criteria) this;
        }

        public Criteria andColumnOrderMapIsNull() {
            addCriterion("column_order_map is null");
            return (Criteria) this;
        }

        public Criteria andColumnOrderMapIsNotNull() {
            addCriterion("column_order_map is not null");
            return (Criteria) this;
        }

        public Criteria andColumnOrderMapEqualTo(Integer value) {
            addCriterion("column_order_map =", value, "columnOrderMap");
            return (Criteria) this;
        }

        public Criteria andColumnOrderMapNotEqualTo(Integer value) {
            addCriterion("column_order_map <>", value, "columnOrderMap");
            return (Criteria) this;
        }

        public Criteria andColumnOrderMapGreaterThan(Integer value) {
            addCriterion("column_order_map >", value, "columnOrderMap");
            return (Criteria) this;
        }

        public Criteria andColumnOrderMapGreaterThanOrEqualTo(Integer value) {
            addCriterion("column_order_map >=", value, "columnOrderMap");
            return (Criteria) this;
        }

        public Criteria andColumnOrderMapLessThan(Integer value) {
            addCriterion("column_order_map <", value, "columnOrderMap");
            return (Criteria) this;
        }

        public Criteria andColumnOrderMapLessThanOrEqualTo(Integer value) {
            addCriterion("column_order_map <=", value, "columnOrderMap");
            return (Criteria) this;
        }

        public Criteria andColumnOrderMapIn(List<Integer> values) {
            addCriterion("column_order_map in", values, "columnOrderMap");
            return (Criteria) this;
        }

        public Criteria andColumnOrderMapNotIn(List<Integer> values) {
            addCriterion("column_order_map not in", values, "columnOrderMap");
            return (Criteria) this;
        }

        public Criteria andColumnOrderMapBetween(Integer value1, Integer value2) {
            addCriterion("column_order_map between", value1, value2, "columnOrderMap");
            return (Criteria) this;
        }

        public Criteria andColumnOrderMapNotBetween(Integer value1, Integer value2) {
            addCriterion("column_order_map not between", value1, value2, "columnOrderMap");
            return (Criteria) this;
        }

        public Criteria andColumnTransFuncionMapIsNull() {
            addCriterion("column_trans_funcion_map is null");
            return (Criteria) this;
        }

        public Criteria andColumnTransFuncionMapIsNotNull() {
            addCriterion("column_trans_funcion_map is not null");
            return (Criteria) this;
        }

        public Criteria andColumnTransFuncionMapEqualTo(String value) {
            addCriterion("column_trans_funcion_map =", value, "columnTransFuncionMap");
            return (Criteria) this;
        }

        public Criteria andColumnTransFuncionMapNotEqualTo(String value) {
            addCriterion("column_trans_funcion_map <>", value, "columnTransFuncionMap");
            return (Criteria) this;
        }

        public Criteria andColumnTransFuncionMapGreaterThan(String value) {
            addCriterion("column_trans_funcion_map >", value, "columnTransFuncionMap");
            return (Criteria) this;
        }

        public Criteria andColumnTransFuncionMapGreaterThanOrEqualTo(String value) {
            addCriterion("column_trans_funcion_map >=", value, "columnTransFuncionMap");
            return (Criteria) this;
        }

        public Criteria andColumnTransFuncionMapLessThan(String value) {
            addCriterion("column_trans_funcion_map <", value, "columnTransFuncionMap");
            return (Criteria) this;
        }

        public Criteria andColumnTransFuncionMapLessThanOrEqualTo(String value) {
            addCriterion("column_trans_funcion_map <=", value, "columnTransFuncionMap");
            return (Criteria) this;
        }

        public Criteria andColumnTransFuncionMapLike(String value) {
            addCriterion("column_trans_funcion_map like", value, "columnTransFuncionMap");
            return (Criteria) this;
        }

        public Criteria andColumnTransFuncionMapNotLike(String value) {
            addCriterion("column_trans_funcion_map not like", value, "columnTransFuncionMap");
            return (Criteria) this;
        }

        public Criteria andColumnTransFuncionMapIn(List<String> values) {
            addCriterion("column_trans_funcion_map in", values, "columnTransFuncionMap");
            return (Criteria) this;
        }

        public Criteria andColumnTransFuncionMapNotIn(List<String> values) {
            addCriterion("column_trans_funcion_map not in", values, "columnTransFuncionMap");
            return (Criteria) this;
        }

        public Criteria andColumnTransFuncionMapBetween(String value1, String value2) {
            addCriterion("column_trans_funcion_map between", value1, value2, "columnTransFuncionMap");
            return (Criteria) this;
        }

        public Criteria andColumnTransFuncionMapNotBetween(String value1, String value2) {
            addCriterion("column_trans_funcion_map not between", value1, value2, "columnTransFuncionMap");
            return (Criteria) this;
        }

        public Criteria andIsUsedIsNull() {
            addCriterion("is_used is null");
            return (Criteria) this;
        }

        public Criteria andIsUsedIsNotNull() {
            addCriterion("is_used is not null");
            return (Criteria) this;
        }

        public Criteria andIsUsedEqualTo(Byte value) {
            addCriterion("is_used =", value, "isUsed");
            return (Criteria) this;
        }

        public Criteria andIsUsedNotEqualTo(Byte value) {
            addCriterion("is_used <>", value, "isUsed");
            return (Criteria) this;
        }

        public Criteria andIsUsedGreaterThan(Byte value) {
            addCriterion("is_used >", value, "isUsed");
            return (Criteria) this;
        }

        public Criteria andIsUsedGreaterThanOrEqualTo(Byte value) {
            addCriterion("is_used >=", value, "isUsed");
            return (Criteria) this;
        }

        public Criteria andIsUsedLessThan(Byte value) {
            addCriterion("is_used <", value, "isUsed");
            return (Criteria) this;
        }

        public Criteria andIsUsedLessThanOrEqualTo(Byte value) {
            addCriterion("is_used <=", value, "isUsed");
            return (Criteria) this;
        }

        public Criteria andIsUsedIn(List<Byte> values) {
            addCriterion("is_used in", values, "isUsed");
            return (Criteria) this;
        }

        public Criteria andIsUsedNotIn(List<Byte> values) {
            addCriterion("is_used not in", values, "isUsed");
            return (Criteria) this;
        }

        public Criteria andIsUsedBetween(Byte value1, Byte value2) {
            addCriterion("is_used between", value1, value2, "isUsed");
            return (Criteria) this;
        }

        public Criteria andIsUsedNotBetween(Byte value1, Byte value2) {
            addCriterion("is_used not between", value1, value2, "isUsed");
            return (Criteria) this;
        }

        public Criteria andIsPrimaryIsNull() {
            addCriterion("is_primary is null");
            return (Criteria) this;
        }

        public Criteria andIsPrimaryIsNotNull() {
            addCriterion("is_primary is not null");
            return (Criteria) this;
        }

        public Criteria andIsPrimaryEqualTo(Byte value) {
            addCriterion("is_primary =", value, "isPrimary");
            return (Criteria) this;
        }

        public Criteria andIsPrimaryNotEqualTo(Byte value) {
            addCriterion("is_primary <>", value, "isPrimary");
            return (Criteria) this;
        }

        public Criteria andIsPrimaryGreaterThan(Byte value) {
            addCriterion("is_primary >", value, "isPrimary");
            return (Criteria) this;
        }

        public Criteria andIsPrimaryGreaterThanOrEqualTo(Byte value) {
            addCriterion("is_primary >=", value, "isPrimary");
            return (Criteria) this;
        }

        public Criteria andIsPrimaryLessThan(Byte value) {
            addCriterion("is_primary <", value, "isPrimary");
            return (Criteria) this;
        }

        public Criteria andIsPrimaryLessThanOrEqualTo(Byte value) {
            addCriterion("is_primary <=", value, "isPrimary");
            return (Criteria) this;
        }

        public Criteria andIsPrimaryIn(List<Byte> values) {
            addCriterion("is_primary in", values, "isPrimary");
            return (Criteria) this;
        }

        public Criteria andIsPrimaryNotIn(List<Byte> values) {
            addCriterion("is_primary not in", values, "isPrimary");
            return (Criteria) this;
        }

        public Criteria andIsPrimaryBetween(Byte value1, Byte value2) {
            addCriterion("is_primary between", value1, value2, "isPrimary");
            return (Criteria) this;
        }

        public Criteria andIsPrimaryNotBetween(Byte value1, Byte value2) {
            addCriterion("is_primary not between", value1, value2, "isPrimary");
            return (Criteria) this;
        }

        public Criteria andIsCreateTimeIsNull() {
            addCriterion("is_create_time is null");
            return (Criteria) this;
        }

        public Criteria andIsCreateTimeIsNotNull() {
            addCriterion("is_create_time is not null");
            return (Criteria) this;
        }

        public Criteria andIsCreateTimeEqualTo(Byte value) {
            addCriterion("is_create_time =", value, "isCreateTime");
            return (Criteria) this;
        }

        public Criteria andIsCreateTimeNotEqualTo(Byte value) {
            addCriterion("is_create_time <>", value, "isCreateTime");
            return (Criteria) this;
        }

        public Criteria andIsCreateTimeGreaterThan(Byte value) {
            addCriterion("is_create_time >", value, "isCreateTime");
            return (Criteria) this;
        }

        public Criteria andIsCreateTimeGreaterThanOrEqualTo(Byte value) {
            addCriterion("is_create_time >=", value, "isCreateTime");
            return (Criteria) this;
        }

        public Criteria andIsCreateTimeLessThan(Byte value) {
            addCriterion("is_create_time <", value, "isCreateTime");
            return (Criteria) this;
        }

        public Criteria andIsCreateTimeLessThanOrEqualTo(Byte value) {
            addCriterion("is_create_time <=", value, "isCreateTime");
            return (Criteria) this;
        }

        public Criteria andIsCreateTimeIn(List<Byte> values) {
            addCriterion("is_create_time in", values, "isCreateTime");
            return (Criteria) this;
        }

        public Criteria andIsCreateTimeNotIn(List<Byte> values) {
            addCriterion("is_create_time not in", values, "isCreateTime");
            return (Criteria) this;
        }

        public Criteria andIsCreateTimeBetween(Byte value1, Byte value2) {
            addCriterion("is_create_time between", value1, value2, "isCreateTime");
            return (Criteria) this;
        }

        public Criteria andIsCreateTimeNotBetween(Byte value1, Byte value2) {
            addCriterion("is_create_time not between", value1, value2, "isCreateTime");
            return (Criteria) this;
        }

        public Criteria andIsUpdateTimeIsNull() {
            addCriterion("is_update_time is null");
            return (Criteria) this;
        }

        public Criteria andIsUpdateTimeIsNotNull() {
            addCriterion("is_update_time is not null");
            return (Criteria) this;
        }

        public Criteria andIsUpdateTimeEqualTo(Byte value) {
            addCriterion("is_update_time =", value, "isUpdateTime");
            return (Criteria) this;
        }

        public Criteria andIsUpdateTimeNotEqualTo(Byte value) {
            addCriterion("is_update_time <>", value, "isUpdateTime");
            return (Criteria) this;
        }

        public Criteria andIsUpdateTimeGreaterThan(Byte value) {
            addCriterion("is_update_time >", value, "isUpdateTime");
            return (Criteria) this;
        }

        public Criteria andIsUpdateTimeGreaterThanOrEqualTo(Byte value) {
            addCriterion("is_update_time >=", value, "isUpdateTime");
            return (Criteria) this;
        }

        public Criteria andIsUpdateTimeLessThan(Byte value) {
            addCriterion("is_update_time <", value, "isUpdateTime");
            return (Criteria) this;
        }

        public Criteria andIsUpdateTimeLessThanOrEqualTo(Byte value) {
            addCriterion("is_update_time <=", value, "isUpdateTime");
            return (Criteria) this;
        }

        public Criteria andIsUpdateTimeIn(List<Byte> values) {
            addCriterion("is_update_time in", values, "isUpdateTime");
            return (Criteria) this;
        }

        public Criteria andIsUpdateTimeNotIn(List<Byte> values) {
            addCriterion("is_update_time not in", values, "isUpdateTime");
            return (Criteria) this;
        }

        public Criteria andIsUpdateTimeBetween(Byte value1, Byte value2) {
            addCriterion("is_update_time between", value1, value2, "isUpdateTime");
            return (Criteria) this;
        }

        public Criteria andIsUpdateTimeNotBetween(Byte value1, Byte value2) {
            addCriterion("is_update_time not between", value1, value2, "isUpdateTime");
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