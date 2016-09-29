package ps.hell.util.sql.parse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
//import com.sitinspring.common.sqlparser.single.NoSqlParserException;
/** *//**
* 单句Sql解析器制造工厂
* @author 赵朝峰
*
* @since 2013-6-10
* @version 1.00
*/
public class SingleSqlParserFactory{
public static BaseSingleSqlParser generateParser(String sql)
{
    if(contains(sql,"(insert into)(.+)(select)(.+)(from)(.+)"))
    {
        return new InsertSelectSqlParser(sql);
    }
    else if(contains(sql,"(select)(.+)(from)(.+)"))
    {
        return new SelectSqlParser(sql);
    }
    else if(contains(sql,"(delete from)(.+)"))
    {
        return new DeleteSqlParser(sql);
    }
    else if(contains(sql,"(update)(.+)(set)(.+)"))
    {
        return new UpdateSqlParser(sql);
    }
    else if(contains(sql,"(insert into)(.+)(values)(.+)"))
    {
        return new InsertSqlParser(sql);
    }
    //sql=sql.replaceAll("ENDSQL", "");
    else
        return new InsertSqlParser(sql);
       //throw new NoSqlParserException(sql.replaceAll("ENDOFSQL", ""));//对异常的抛出
}
/** *//**
　* 看word是否在lineText中存在，支持正则表达式
　* @param sql:要解析的sql语句
　* @param regExp:正则表达式
　* @return
　*/
private static boolean contains(String sql,String regExp){
    Pattern pattern=Pattern.compile(regExp,Pattern.CASE_INSENSITIVE);
    Matcher matcher=pattern.matcher(sql);
    return matcher.find();
}
}