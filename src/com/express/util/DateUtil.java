package com.express.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	
	/**
	 * 日期格式化
	 * @param date
	 * @param format
	 * @return
	 */
	public static String getDateFormat(Date date,String format){
		SimpleDateFormat sDateFormat = new SimpleDateFormat(format);
		String dateFormat = sDateFormat.format(date);
		return dateFormat;
	}
	
	/**
	 * 描述：String类型的日期时间转化为Date类型.
	 *
	 * @param strDate String形式的日期时间
	 * @param format  格式化字符串，如："yyyy-MM-dd HH:mm:ss"
	 * @return Date Date类型日期时间
	 */
	public static Date stringToDate(String strDate,String format){
		SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
	    Date date = null;
	    try {
	        date = mSimpleDateFormat.parse(strDate);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return date;
	}
	
	/**
	 * 日期类型转化为long型
	 * @param date
	 * @return
	 */
	public static Long date2Long(Date date){
		return date.getTime();
	}
	
	
	/**
	 * 获取上个月的今天
	 */
	public static Date lastMonth(){
		Date date = new Date();
		long lastMonth = date.getTime()-1000*60*60*24*30l;
		return new Date(lastMonth);
	}
	
	/**
	 * 获取当前月份
	 */
	public static int atMonth(){
		Date date = new Date();
		SimpleDateFormat sDateFormat = new SimpleDateFormat("MM");
		String dateFormat = sDateFormat.format(date);
		return Integer.valueOf(dateFormat);
	}
	
	/**
	 * 获取当前年份
	 */
	public static int atYear(){
		Date date = new Date();
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy");
		String dateFormat = sDateFormat.format(date);
		return Integer.valueOf(dateFormat);
	}
}
