package ps.hell.util.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期格式化类
 * @author Administrator
 *
 */
public class DateUtil {

	
	/**
	 * 对时间 做秒+运算
	 * @param date
	 * @param count 为 数量
	 * @return
	 */
	public static Date addSecond(Date date,int count)
	{
		 Calendar c = Calendar.getInstance();  
         c.setTime(date);  
         c.add(c.SECOND,count);
         Date dateTemp = c.getTime();
         return dateTemp;
	}
	
	/**
	 * 对时间 做秒+运算
	 * @param date
	 * @param count 为 数量
	 * @param 时间格式
	 * @return
	 */
	public static Date addMinus(Date date,int count)
	{
		 Calendar c = Calendar.getInstance();  
         c.setTime(date);   
         c.add(c.MINUTE,count);
         Date dateTemp = c.getTime();
         return dateTemp;
	}
	/**
	 * 对时间 做秒+运算
	 * @param date
	 * @param count 为 数量
	 * @param 时间格式
	 * @return
	 */
	public static Date addHour(Date date,int count)
	{
		 Calendar c = Calendar.getInstance();  
         c.setTime(date);  
         c.add(c.HOUR,count);
         Date dateTemp = c.getTime();
         return dateTemp;
	}
	/**
	 * 对时间 做秒+运算
	 * @param date
	 * @param count 为 数量
	 * @param 时间格式
	 * @return
	 */
	public static Date addDate(Date date,int count)
	{
		 Calendar c = Calendar.getInstance();  
         c.setTime(date);  
         c.add(c.DATE,count);
         Date dateTemp = c.getTime();
         return dateTemp;
	}
	/**
	 * 对时间 做秒+运算
	 * @param date
	 * @param count 为 数量
	 * @param 时间格式
	 * @return
	 */
	public static Date addMonth(Date date,int count)
	{
		 Calendar c = Calendar.getInstance();  
         c.setTime(date);  
         c.add(c.MONTH,count);
         Date dateTemp = c.getTime();
         return dateTemp;
	}
	
	/**
	 * 对时间 做秒+运算
	 * @param date
	 * @param count 为 数量
	 * @param 时间格式
	 * @return
	 */
	public static Date addYear(Date date,int count)
	{
		 Calendar c = Calendar.getInstance();  
         c.setTime(date);
         c.add(c.YEAR,count);
         Date dateTemp = c.getTime();
         return dateTemp;
	}
	

	/**
	 * 2013年3月7日 3时3分3秒 只要有序包含即可解析
	 * 将中文时间格式化为格式
	 * @param str
	 */
	public static String transcateDate(String str)
	{
		String re= str.replaceAll("[年]","-").replaceAll("[月]","-").replaceAll("[日]","")
				.replaceAll("[时]",":").replaceAll("[分]",":").replaceAll("[秒]","").trim();
		if(re.length()>0&&re.indexOf(re.length()-1)=='-'){
			return re.substring(0,re.length()-1);
		}
		return re;
	}
	
	public static void main(String[] args) {
		Date date=new Date();
		System.out.println(date);
		System.out.println(DateUtil.addSecond(date,3600000));
	}
}
