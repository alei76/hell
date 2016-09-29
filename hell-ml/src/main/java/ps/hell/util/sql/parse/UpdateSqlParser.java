package ps.hell.util.sql.parse;

/** *//**
*
* 单句更新语句解析器
* @author 赵朝峰
*
* @since 2013-6-10
* @version 1.00
*/
public class UpdateSqlParser extends BaseSingleSqlParser{
public UpdateSqlParser(String originalSql) {
    super(originalSql);
}
@Override
protected void initializeSegments() {
    segments.add(new SqlSegment("(update)(.+)(set)","[,]"));
    segments.add(new SqlSegment("(set)(.+)( where | ENDOFSQL)","[,]"));
    segments.add(new SqlSegment("(where)(.+)( ENDOFSQL)","(and|or)"));
}
}