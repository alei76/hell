package ps.hell.util.sql.parse;

/** */
/**
 *
 * 单句删除语句解析器
 * 
 * @author 赵朝峰
 *
 * @since 2013-6-10
 * @version 1.00
 */
public class DeleteSqlParser extends BaseSingleSqlParser {
	public DeleteSqlParser(String originalSql) {
		super(originalSql);
	}

	@Override
	protected void initializeSegments() {
		segments.add(new SqlSegment("(delete from)(.+)( where | ENDOFSQL)",
				"[,]"));
		segments.add(new SqlSegment("(where)(.+)( ENDOFSQL)", "(and|or)"));
	}
}