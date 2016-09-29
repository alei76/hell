/*
 * Encog(tm) Core v3.3 - Java Version
 * http://www.heatonresearch.com/encog/
 * https://github.com/encog/encog-java-core
 
 * Copyright 2008-2014 Heaton Research, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *   
 * For more information on Heaton Research copyrights, licenses 
 * and trademarks visit:
 * http://www.heatonresearch.com/copyright
 */
package ps.hell.util.format;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 格式化工具包
 * Provides the ability for Encog to format numbers and times.
 */
public final class FormatUtil {

	/**
	 * Seconds in a minute.
	 */
	public static final int SECONDS_INA_MINUTE = 60;

	/**
	 * Seconds in an hour.
	 */
	public static final int SECONDS_INA_HOUR = FormatUtil.SECONDS_INA_MINUTE * 60;

	/**
	 * Seconds in a day.
	 */
	public static final int SECONDS_INA_DAY = FormatUtil.SECONDS_INA_HOUR * 24;

	/**
	 * Bytes in a KB.
	 */
	public static final long MEMORY_K = 1024;

	/**
	 * Bytes in a MB.
	 */
	public static final long MEMORY_MEG = (1024 * FormatUtil.MEMORY_K);

	/**
	 * Bytes in a GB.
	 */
	public static final long MEMORY_GIG = (1024 * FormatUtil.MEMORY_MEG);

	/**
	 * Bytes in a TB.
	 */
	public static final long MEMORY_TERA = (1024 * FormatUtil.MEMORY_GIG);

	/**
	 * How many miliseconds in a second.
	 */
	public static final long MILI_IN_SEC = 1000;

	/**
	 * One hundred percent.
	 */
	public static final double HUNDRED_PERCENT = 100.0;

	/**
	 * Format a double.
	 * 
	 * @param d
	 *            The double value to format.
	 * @param i
	 *            The number of decimal places.
	 * @return The double as a string.
	 */
	public static String formatDouble(final double d, final int i) {
		final NumberFormat f = NumberFormat.getNumberInstance();
		f.setMinimumFractionDigits(i);
		return f.format(d);
	}

	/**
	 * Format an integer.
	 * 
	 * @param i
	 *            The integer to format.
	 * @return The integer as a string.
	 */
	public static String formatInteger(final int i) {
		final NumberFormat f = NumberFormat.getIntegerInstance();
		return f.format(i);
	}

	/**
	 * Format a memory amount, to something like 32 MB.
	 * 
	 * @param memory
	 *            The amount of bytes.
	 * @return The formatted memory size.
	 */
	public static String formatMemory(final long memory) {
		if (memory < FormatUtil.MEMORY_K) {
			return memory + " bytes";
		} else if (memory < FormatUtil.MEMORY_MEG) {
			return FormatUtil.formatDouble(((double) memory)
					/ ((double) FormatUtil.MEMORY_K), 2)
					+ " KB";
		} else if (memory < FormatUtil.MEMORY_GIG) {
			return FormatUtil.formatDouble(((double) memory)
					/ ((double) FormatUtil.MEMORY_MEG), 2)
					+ " MB";
		} else if (memory < FormatUtil.MEMORY_TERA) {
			return FormatUtil.formatDouble(((double) memory)
					/ ((double) FormatUtil.MEMORY_GIG), 2)
					+ " GB";
		} else {
			return FormatUtil.formatDouble(((double) memory)
					/ ((double) FormatUtil.MEMORY_TERA), 2)
					+ " TB";
		}
	}

	/**
	 * Format a percent. Using 6 decimal places.
	 * 
	 * @param e
	 *            The percent to format.
	 * @return The formatted percent.
	 */
	public static String formatPercent(final double e) {
		if( Double.isNaN(e) || Double.isInfinite(e) ) 
			return "NaN";
		final NumberFormat f = NumberFormat.getPercentInstance();
		f.setMinimumFractionDigits(6);
		return f.format(e);
	}

	/**
	 * Format a percent with no decimal places.
	 * 
	 * @param e
	 *            The format to percent.
	 * @return The formatted percent.
	 */
	public static String formatPercentWhole(final double e) {
		final NumberFormat f = NumberFormat.getPercentInstance();
		return f.format(e);
	}

	/**
	 * 格式秒的时间跨度
	 * Format a time span as seconds, minutes, hours and days.
	 * 
	 * @param seconds
	 *            The number of seconds in the timespan.
	 * @return The formatted timespan.
	 */
	public static String formatTimeSpan(final int seconds) {
		int secondsCount = seconds;
		final int days = seconds / FormatUtil.SECONDS_INA_DAY;
		secondsCount -= days * FormatUtil.SECONDS_INA_DAY;
		final int hours = secondsCount / FormatUtil.SECONDS_INA_HOUR;
		secondsCount -= hours * FormatUtil.SECONDS_INA_HOUR;
		final int minutes = secondsCount / FormatUtil.SECONDS_INA_MINUTE;
		secondsCount -= minutes * FormatUtil.SECONDS_INA_MINUTE;

		final NumberFormat f = NumberFormat.getIntegerInstance();
		f.setMinimumIntegerDigits(2);
		f.setMaximumIntegerDigits(2);
		final StringBuilder result = new StringBuilder();

		if (days > 0) {
			result.append(days);
			if (days > 1) {
				result.append(" days ");
			} else {
				result.append(" day ");
			}
		}

		result.append(f.format(hours));
		result.append(':');
		result.append(f.format(minutes));
		result.append(':');
		result.append(f.format(secondsCount));

		return result.toString();
	}
	
	/**
	 * Format a boolean as yes/no.
	 * @param b The boolean value.
	 * @return The yes/no result.
	 */
	public static String formatYesNo(final boolean b) {
		if (b) {
			return "Yes";
		} else {
			return "No";
		}
	}

	/**
	 * Private constructor.
	 */
	private FormatUtil() {
	}
	
	
	public static DecimalFormat df = new DecimalFormat("0.00");
	public static DateFormat datef = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	/**
	 * 钱的解析
	 * @param val
	 * @return
	 */
	public static String parseDoubleToString(double val) {
		return df.format(val);
	}
	/**
	 * 格式化double
	 * @param value
	 * @param type "0.00"
	 * @return
	 */
	public static String parseDoubleToString(double value,String type){
		DecimalFormat df2 = new DecimalFormat("0.00");
		return df2.format(value);
	}
	
	public static String parseDoubleToMoneyString(String value){
		return df.format(value)+"$";
	}
	/**
	 * 日期格式化2
	 * @param val
	 * @return
	 */
	public static String parseDateToString(Date val) {
		return datef.format(val);
	}
	
	/**
	 * 日期格式化2
	 * @param val
	 * @param type "yyyy-MM-dd hh:mm:ss"
	 * @return
	 */
	public static String parseDateToString(Date val,String type) {
		DateFormat datef2 = new SimpleDateFormat(type);
		return datef2.format(val);
	}


	
	/**
	 * long 转时间
	 * @param val
	 * @return
	 */
	public static String parseDate(long val)
	{
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(val);
		return parseDateToString(c.getTime());
	}
	
	// TimeZone srcTimeZone = TimeZone.getTimeZone("EST");
	// TimeZone destTimeZone = TimeZone.getTimeZone("GMT+8");
	/**
	 * 格式化抓换
	 * @param sourceDate
	 * @return
	 */
	public static String fromTimeZone(String sourceDate) {
		SimpleDateFormat sdf = new SimpleDateFormat(
				"EEE MMM dd HH:mm:ss zzz yyyy", Locale.CHINA);
		try {
			Date date = sdf.parse(sourceDate);
			return parseDateToString(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * 时间 市区转换
	 * @param sourceDate
	 * @param formatter
	 * @param sourceTimeZone
	 * @param targetTimeZone
	 * @return
	 */
	public static String dateTransformBetweenTimeZone(Date sourceDate,
			DateFormat formatter, TimeZone sourceTimeZone,
			TimeZone targetTimeZone) {
		Long targetTime = sourceDate.getTime() - sourceTimeZone.getRawOffset()
				+ targetTimeZone.getRawOffset();
		return getTime(new Date(targetTime), formatter);
	}

	public static String getTime(Date date, DateFormat formatter) {
		return formatter.format(date);
	}

	/**
	 * 
	 * @param str
	 */
	public static String transcateDate(String str)
	{
		return str.replaceAll("[年]","-").replaceAll("[月]","");
	}
	
	/**
	 * 对时间 做秒+运算
	 * @param date
	 * @param count 为 数量
	 * @param 时间格式
	 * @return
	 */
	public static Date addSecond(Date date,int count,int category)
	{
		 Calendar c = Calendar.getInstance();  
         c.setTime(date);  
//         c.add(c.YEAR, 1);//属性很多也有月等等，可以操作各种时间日期  
//         c.add(c.DATE, -1);  
        c.add(category,count);
         Date dateTemp = c.getTime();
         return dateTemp;
	}
	

	/**
	 * 将 yyyy-MM-dd HH:mm:ss 字符串转时间
	 * @param str
	 * @return
	 */
	public static Date strToDate(String str)
	{
		 Date date = null;
		   try {
		    date = datef.parse(str);
		   } catch (ParseException e) {
		    e.printStackTrace();
		   }
		   return date;
	}

	/**
	 * 10个空格
	 */
	private static String space = "          ";

	/**
	 * 格式化是否靠近左侧
	 * 
	 * @param input
	 * @param length
	 * @param isLeft
	 * @return
	 */
	public static String formatToString(String input, int length, boolean isLeft) {
		StringBuffer sb = new StringBuffer();
		if (isLeft) {
			if (input.length() > length) {
				sb.append(input.substring(0, length));
			} else {
				sb.append(input);
				while (true) {
					if (length - input.length() > 10) {
						sb.append(space);
						length-=10;
					} else {
						sb.append(space.substring(0, length - input.length()));
						break;
					}
				}
			}
		}else{
			if (input.length() > length) {
				sb.append(input.substring(0, length));
			} else {
				while (true) {
					if (length - input.length() > 10) {
						sb.append(space);
						length-=10;
					} else {
						sb.append(space.substring(0, length - input.length()));
						break;
					}
				}
				sb.append(input);
			}
		}
		return sb.toString();
	}
	
	
	/**
	 * double的字符串格式化
	 * @param val
	 * @param precise 精度
	 * @param length 需要的长度
	 * @param isLeft 是否左靠
	 * @return
	 */
	public static String formatToString(double val,int precise,int length, boolean isLeft){
		if(precise<0){
			try {
				throw new Exception("精度不能小于0");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		StringBuffer sb=new StringBuffer("#0.");
		for(int i=0;i<precise;i++){
			sb.append('0');
		}
		DecimalFormat df2=new DecimalFormat(sb.toString());
		String input=df2.format(val);
		return formatToString(input,length,isLeft);
	}
	
	
	public static void main(String[] args) {
		double d = 1.00999d;
		System.out.println(parseDoubleToString(d));

		System.out.println(parseDateToString(new Date()));
		
		System.out.println(formatToString(11.34332d,2,10,false));
	}
}
