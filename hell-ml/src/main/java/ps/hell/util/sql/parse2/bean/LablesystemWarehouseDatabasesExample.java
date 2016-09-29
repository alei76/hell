package ps.hell.util.sql.parse2.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LablesystemWarehouseDatabasesExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public LablesystemWarehouseDatabasesExample() {
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

        public Criteria andConnDriverIsNull() {
            addCriterion("conn_driver is null");
            return (Criteria) this;
        }

        public Criteria andConnDriverIsNotNull() {
            addCriterion("conn_driver is not null");
            return (Criteria) this;
        }

        public Criteria andConnDriverEqualTo(String value) {
            addCriterion("conn_driver =", value, "connDriver");
            return (Criteria) this;
        }

        public Criteria andConnDriverNotEqualTo(String value) {
            addCriterion("conn_driver <>", value, "connDriver");
            return (Criteria) this;
        }

        public Criteria andConnDriverGreaterThan(String value) {
            addCriterion("conn_driver >", value, "connDriver");
            return (Criteria) this;
        }

        public Criteria andConnDriverGreaterThanOrEqualTo(String value) {
            addCriterion("conn_driver >=", value, "connDriver");
            return (Criteria) this;
        }

        public Criteria andConnDriverLessThan(String value) {
            addCriterion("conn_driver <", value, "connDriver");
            return (Criteria) this;
        }

        public Criteria andConnDriverLessThanOrEqualTo(String value) {
            addCriterion("conn_driver <=", value, "connDriver");
            return (Criteria) this;
        }

        public Criteria andConnDriverLike(String value) {
            addCriterion("conn_driver like", value, "connDriver");
            return (Criteria) this;
        }

        public Criteria andConnDriverNotLike(String value) {
            addCriterion("conn_driver not like", value, "connDriver");
            return (Criteria) this;
        }

        public Criteria andConnDriverIn(List<String> values) {
            addCriterion("conn_driver in", values, "connDriver");
            return (Criteria) this;
        }

        public Criteria andConnDriverNotIn(List<String> values) {
            addCriterion("conn_driver not in", values, "connDriver");
            return (Criteria) this;
        }

        public Criteria andConnDriverBetween(String value1, String value2) {
            addCriterion("conn_driver between", value1, value2, "connDriver");
            return (Criteria) this;
        }

        public Criteria andConnDriverNotBetween(String value1, String value2) {
            addCriterion("conn_driver not between", value1, value2, "connDriver");
            return (Criteria) this;
        }

        public Criteria andConnDatabaseIsNull() {
            addCriterion("conn_database is null");
            return (Criteria) this;
        }

        public Criteria andConnDatabaseIsNotNull() {
            addCriterion("conn_database is not null");
            return (Criteria) this;
        }

        public Criteria andConnDatabaseEqualTo(String value) {
            addCriterion("conn_database =", value, "connDatabase");
            return (Criteria) this;
        }

        public Criteria andConnDatabaseNotEqualTo(String value) {
            addCriterion("conn_database <>", value, "connDatabase");
            return (Criteria) this;
        }

        public Criteria andConnDatabaseGreaterThan(String value) {
            addCriterion("conn_database >", value, "connDatabase");
            return (Criteria) this;
        }

        public Criteria andConnDatabaseGreaterThanOrEqualTo(String value) {
            addCriterion("conn_database >=", value, "connDatabase");
            return (Criteria) this;
        }

        public Criteria andConnDatabaseLessThan(String value) {
            addCriterion("conn_database <", value, "connDatabase");
            return (Criteria) this;
        }

        public Criteria andConnDatabaseLessThanOrEqualTo(String value) {
            addCriterion("conn_database <=", value, "connDatabase");
            return (Criteria) this;
        }

        public Criteria andConnDatabaseLike(String value) {
            addCriterion("conn_database like", value, "connDatabase");
            return (Criteria) this;
        }

        public Criteria andConnDatabaseNotLike(String value) {
            addCriterion("conn_database not like", value, "connDatabase");
            return (Criteria) this;
        }

        public Criteria andConnDatabaseIn(List<String> values) {
            addCriterion("conn_database in", values, "connDatabase");
            return (Criteria) this;
        }

        public Criteria andConnDatabaseNotIn(List<String> values) {
            addCriterion("conn_database not in", values, "connDatabase");
            return (Criteria) this;
        }

        public Criteria andConnDatabaseBetween(String value1, String value2) {
            addCriterion("conn_database between", value1, value2, "connDatabase");
            return (Criteria) this;
        }

        public Criteria andConnDatabaseNotBetween(String value1, String value2) {
            addCriterion("conn_database not between", value1, value2, "connDatabase");
            return (Criteria) this;
        }

        public Criteria andStoreDatabaseIsNull() {
            addCriterion("store_database is null");
            return (Criteria) this;
        }

        public Criteria andStoreDatabaseIsNotNull() {
            addCriterion("store_database is not null");
            return (Criteria) this;
        }

        public Criteria andStoreDatabaseEqualTo(String value) {
            addCriterion("store_database =", value, "storeDatabase");
            return (Criteria) this;
        }

        public Criteria andStoreDatabaseNotEqualTo(String value) {
            addCriterion("store_database <>", value, "storeDatabase");
            return (Criteria) this;
        }

        public Criteria andStoreDatabaseGreaterThan(String value) {
            addCriterion("store_database >", value, "storeDatabase");
            return (Criteria) this;
        }

        public Criteria andStoreDatabaseGreaterThanOrEqualTo(String value) {
            addCriterion("store_database >=", value, "storeDatabase");
            return (Criteria) this;
        }

        public Criteria andStoreDatabaseLessThan(String value) {
            addCriterion("store_database <", value, "storeDatabase");
            return (Criteria) this;
        }

        public Criteria andStoreDatabaseLessThanOrEqualTo(String value) {
            addCriterion("store_database <=", value, "storeDatabase");
            return (Criteria) this;
        }

        public Criteria andStoreDatabaseLike(String value) {
            addCriterion("store_database like", value, "storeDatabase");
            return (Criteria) this;
        }

        public Criteria andStoreDatabaseNotLike(String value) {
            addCriterion("store_database not like", value, "storeDatabase");
            return (Criteria) this;
        }

        public Criteria andStoreDatabaseIn(List<String> values) {
            addCriterion("store_database in", values, "storeDatabase");
            return (Criteria) this;
        }

        public Criteria andStoreDatabaseNotIn(List<String> values) {
            addCriterion("store_database not in", values, "storeDatabase");
            return (Criteria) this;
        }

        public Criteria andStoreDatabaseBetween(String value1, String value2) {
            addCriterion("store_database between", value1, value2, "storeDatabase");
            return (Criteria) this;
        }

        public Criteria andStoreDatabaseNotBetween(String value1, String value2) {
            addCriterion("store_database not between", value1, value2, "storeDatabase");
            return (Criteria) this;
        }

        public Criteria andConnUrlIsNull() {
            addCriterion("conn_url is null");
            return (Criteria) this;
        }

        public Criteria andConnUrlIsNotNull() {
            addCriterion("conn_url is not null");
            return (Criteria) this;
        }

        public Criteria andConnUrlEqualTo(String value) {
            addCriterion("conn_url =", value, "connUrl");
            return (Criteria) this;
        }

        public Criteria andConnUrlNotEqualTo(String value) {
            addCriterion("conn_url <>", value, "connUrl");
            return (Criteria) this;
        }

        public Criteria andConnUrlGreaterThan(String value) {
            addCriterion("conn_url >", value, "connUrl");
            return (Criteria) this;
        }

        public Criteria andConnUrlGreaterThanOrEqualTo(String value) {
            addCriterion("conn_url >=", value, "connUrl");
            return (Criteria) this;
        }

        public Criteria andConnUrlLessThan(String value) {
            addCriterion("conn_url <", value, "connUrl");
            return (Criteria) this;
        }

        public Criteria andConnUrlLessThanOrEqualTo(String value) {
            addCriterion("conn_url <=", value, "connUrl");
            return (Criteria) this;
        }

        public Criteria andConnUrlLike(String value) {
            addCriterion("conn_url like", value, "connUrl");
            return (Criteria) this;
        }

        public Criteria andConnUrlNotLike(String value) {
            addCriterion("conn_url not like", value, "connUrl");
            return (Criteria) this;
        }

        public Criteria andConnUrlIn(List<String> values) {
            addCriterion("conn_url in", values, "connUrl");
            return (Criteria) this;
        }

        public Criteria andConnUrlNotIn(List<String> values) {
            addCriterion("conn_url not in", values, "connUrl");
            return (Criteria) this;
        }

        public Criteria andConnUrlBetween(String value1, String value2) {
            addCriterion("conn_url between", value1, value2, "connUrl");
            return (Criteria) this;
        }

        public Criteria andConnUrlNotBetween(String value1, String value2) {
            addCriterion("conn_url not between", value1, value2, "connUrl");
            return (Criteria) this;
        }

        public Criteria andConnPortIsNull() {
            addCriterion("conn_port is null");
            return (Criteria) this;
        }

        public Criteria andConnPortIsNotNull() {
            addCriterion("conn_port is not null");
            return (Criteria) this;
        }

        public Criteria andConnPortEqualTo(Integer value) {
            addCriterion("conn_port =", value, "connPort");
            return (Criteria) this;
        }

        public Criteria andConnPortNotEqualTo(Integer value) {
            addCriterion("conn_port <>", value, "connPort");
            return (Criteria) this;
        }

        public Criteria andConnPortGreaterThan(Integer value) {
            addCriterion("conn_port >", value, "connPort");
            return (Criteria) this;
        }

        public Criteria andConnPortGreaterThanOrEqualTo(Integer value) {
            addCriterion("conn_port >=", value, "connPort");
            return (Criteria) this;
        }

        public Criteria andConnPortLessThan(Integer value) {
            addCriterion("conn_port <", value, "connPort");
            return (Criteria) this;
        }

        public Criteria andConnPortLessThanOrEqualTo(Integer value) {
            addCriterion("conn_port <=", value, "connPort");
            return (Criteria) this;
        }

        public Criteria andConnPortIn(List<Integer> values) {
            addCriterion("conn_port in", values, "connPort");
            return (Criteria) this;
        }

        public Criteria andConnPortNotIn(List<Integer> values) {
            addCriterion("conn_port not in", values, "connPort");
            return (Criteria) this;
        }

        public Criteria andConnPortBetween(Integer value1, Integer value2) {
            addCriterion("conn_port between", value1, value2, "connPort");
            return (Criteria) this;
        }

        public Criteria andConnPortNotBetween(Integer value1, Integer value2) {
            addCriterion("conn_port not between", value1, value2, "connPort");
            return (Criteria) this;
        }

        public Criteria andDbCommentIsNull() {
            addCriterion("db_comment is null");
            return (Criteria) this;
        }

        public Criteria andDbCommentIsNotNull() {
            addCriterion("db_comment is not null");
            return (Criteria) this;
        }

        public Criteria andDbCommentEqualTo(String value) {
            addCriterion("db_comment =", value, "dbComment");
            return (Criteria) this;
        }

        public Criteria andDbCommentNotEqualTo(String value) {
            addCriterion("db_comment <>", value, "dbComment");
            return (Criteria) this;
        }

        public Criteria andDbCommentGreaterThan(String value) {
            addCriterion("db_comment >", value, "dbComment");
            return (Criteria) this;
        }

        public Criteria andDbCommentGreaterThanOrEqualTo(String value) {
            addCriterion("db_comment >=", value, "dbComment");
            return (Criteria) this;
        }

        public Criteria andDbCommentLessThan(String value) {
            addCriterion("db_comment <", value, "dbComment");
            return (Criteria) this;
        }

        public Criteria andDbCommentLessThanOrEqualTo(String value) {
            addCriterion("db_comment <=", value, "dbComment");
            return (Criteria) this;
        }

        public Criteria andDbCommentLike(String value) {
            addCriterion("db_comment like", value, "dbComment");
            return (Criteria) this;
        }

        public Criteria andDbCommentNotLike(String value) {
            addCriterion("db_comment not like", value, "dbComment");
            return (Criteria) this;
        }

        public Criteria andDbCommentIn(List<String> values) {
            addCriterion("db_comment in", values, "dbComment");
            return (Criteria) this;
        }

        public Criteria andDbCommentNotIn(List<String> values) {
            addCriterion("db_comment not in", values, "dbComment");
            return (Criteria) this;
        }

        public Criteria andDbCommentBetween(String value1, String value2) {
            addCriterion("db_comment between", value1, value2, "dbComment");
            return (Criteria) this;
        }

        public Criteria andDbCommentNotBetween(String value1, String value2) {
            addCriterion("db_comment not between", value1, value2, "dbComment");
            return (Criteria) this;
        }

        public Criteria andUserEncodeIsNull() {
            addCriterion("user_encode is null");
            return (Criteria) this;
        }

        public Criteria andUserEncodeIsNotNull() {
            addCriterion("user_encode is not null");
            return (Criteria) this;
        }

        public Criteria andUserEncodeEqualTo(String value) {
            addCriterion("user_encode =", value, "userEncode");
            return (Criteria) this;
        }

        public Criteria andUserEncodeNotEqualTo(String value) {
            addCriterion("user_encode <>", value, "userEncode");
            return (Criteria) this;
        }

        public Criteria andUserEncodeGreaterThan(String value) {
            addCriterion("user_encode >", value, "userEncode");
            return (Criteria) this;
        }

        public Criteria andUserEncodeGreaterThanOrEqualTo(String value) {
            addCriterion("user_encode >=", value, "userEncode");
            return (Criteria) this;
        }

        public Criteria andUserEncodeLessThan(String value) {
            addCriterion("user_encode <", value, "userEncode");
            return (Criteria) this;
        }

        public Criteria andUserEncodeLessThanOrEqualTo(String value) {
            addCriterion("user_encode <=", value, "userEncode");
            return (Criteria) this;
        }

        public Criteria andUserEncodeLike(String value) {
            addCriterion("user_encode like", value, "userEncode");
            return (Criteria) this;
        }

        public Criteria andUserEncodeNotLike(String value) {
            addCriterion("user_encode not like", value, "userEncode");
            return (Criteria) this;
        }

        public Criteria andUserEncodeIn(List<String> values) {
            addCriterion("user_encode in", values, "userEncode");
            return (Criteria) this;
        }

        public Criteria andUserEncodeNotIn(List<String> values) {
            addCriterion("user_encode not in", values, "userEncode");
            return (Criteria) this;
        }

        public Criteria andUserEncodeBetween(String value1, String value2) {
            addCriterion("user_encode between", value1, value2, "userEncode");
            return (Criteria) this;
        }

        public Criteria andUserEncodeNotBetween(String value1, String value2) {
            addCriterion("user_encode not between", value1, value2, "userEncode");
            return (Criteria) this;
        }

        public Criteria andPwdEncodeIsNull() {
            addCriterion("pwd_encode is null");
            return (Criteria) this;
        }

        public Criteria andPwdEncodeIsNotNull() {
            addCriterion("pwd_encode is not null");
            return (Criteria) this;
        }

        public Criteria andPwdEncodeEqualTo(String value) {
            addCriterion("pwd_encode =", value, "pwdEncode");
            return (Criteria) this;
        }

        public Criteria andPwdEncodeNotEqualTo(String value) {
            addCriterion("pwd_encode <>", value, "pwdEncode");
            return (Criteria) this;
        }

        public Criteria andPwdEncodeGreaterThan(String value) {
            addCriterion("pwd_encode >", value, "pwdEncode");
            return (Criteria) this;
        }

        public Criteria andPwdEncodeGreaterThanOrEqualTo(String value) {
            addCriterion("pwd_encode >=", value, "pwdEncode");
            return (Criteria) this;
        }

        public Criteria andPwdEncodeLessThan(String value) {
            addCriterion("pwd_encode <", value, "pwdEncode");
            return (Criteria) this;
        }

        public Criteria andPwdEncodeLessThanOrEqualTo(String value) {
            addCriterion("pwd_encode <=", value, "pwdEncode");
            return (Criteria) this;
        }

        public Criteria andPwdEncodeLike(String value) {
            addCriterion("pwd_encode like", value, "pwdEncode");
            return (Criteria) this;
        }

        public Criteria andPwdEncodeNotLike(String value) {
            addCriterion("pwd_encode not like", value, "pwdEncode");
            return (Criteria) this;
        }

        public Criteria andPwdEncodeIn(List<String> values) {
            addCriterion("pwd_encode in", values, "pwdEncode");
            return (Criteria) this;
        }

        public Criteria andPwdEncodeNotIn(List<String> values) {
            addCriterion("pwd_encode not in", values, "pwdEncode");
            return (Criteria) this;
        }

        public Criteria andPwdEncodeBetween(String value1, String value2) {
            addCriterion("pwd_encode between", value1, value2, "pwdEncode");
            return (Criteria) this;
        }

        public Criteria andPwdEncodeNotBetween(String value1, String value2) {
            addCriterion("pwd_encode not between", value1, value2, "pwdEncode");
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