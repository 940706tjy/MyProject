package cn.bulaomeng.fragment.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtil {
	
	//取当前时间
    public static Timestamp nowTimestamp(){
        return new Timestamp(System.currentTimeMillis());
    }
    
    //String转Timestamp
    public static Timestamp strToTimestamp(String str, String formatStr) {
    	
    	formatStr = (formatStr == null) || (formatStr.trim().length() == 0) ?  "yyyy-MM-dd HH:mm:ss" : formatStr;
        SimpleDateFormat sf = new SimpleDateFormat(formatStr);
        try {
            Date date = sf.parse(str);
            Timestamp t1 = new Timestamp(date.getTime());
             return  t1;
        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
    }
    
    //Timestamp转String
  	public static String timestampToStr(Timestamp ts, String formatStr) {
  		
  		formatStr = (formatStr == null) || (formatStr.trim().length() == 0) ?  "yyyy-MM-dd HH:mm:ss" : formatStr;
  	    DateFormat sdf = new SimpleDateFormat(formatStr);  
  	  
  	    String tsStr = sdf.format(ts);  
  	    return tsStr; 
  	}
    
    //String转date
    public static Date strToDate(String thisDate, String formatStr) throws Exception {
		
		formatStr = (formatStr == null) || (formatStr.trim().length() == 0) ?  "yyyy-MM-dd HH:mm:ss" : formatStr;
		SimpleDateFormat formatter = new SimpleDateFormat(formatStr);
		try{
			return  new Timestamp(formatter.parse(thisDate).getTime());
		} catch (Exception e) {
			throw e;
		}
	}
	
    //date转String
	public static String dateToStr(Date thisDate, String formatStr) {

		formatStr = (formatStr == null) || (formatStr.trim().length() == 0) ?  "yyyy-MM-dd HH:mm:ss" : formatStr;
		SimpleDateFormat formater = new SimpleDateFormat(formatStr);
		return formater.format(thisDate);
	}
	
	//Timestamp时间差计算
	public static long dateDiff(String timeInterval, Timestamp t1, Timestamp t2) {
		Date date1 = new Date(t1.getTime());
		Date date2 = new Date(t2.getTime());
		return dateDiff(timeInterval, date1, date2);
	}

	//Date时间差计算
	public static long dateDiff(String timeInterval, Date date1, Date date2) {
		if (timeInterval.equals("year")) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date1);
			int time = calendar.get(1);
			calendar.setTime(date2);
			return time - calendar.get(1);
		}
		if (timeInterval.equals("quarter")) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date1);
			int time = calendar.get(1) * 4;
			calendar.setTime(date2);
			time -= calendar.get(1) * 4;
			calendar.setTime(date1);
			time += calendar.get(2) / 4;
			calendar.setTime(date2);
			return time - calendar.get(2) / 4;
		}
		if (timeInterval.equals("month")) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date1);
			int time = calendar.get(1) * 12;
			calendar.setTime(date2);
			time -= calendar.get(1) * 12;
			calendar.setTime(date1);
			time += calendar.get(2);
			calendar.setTime(date2);
			return time - calendar.get(2);
		}
		if (timeInterval.equals("week")) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date1);
			int time = calendar.get(1) * 52;
			calendar.setTime(date2);
			time -= calendar.get(1) * 52;
			calendar.setTime(date1);
			time += calendar.get(3);
			calendar.setTime(date2);
			return time - calendar.get(3);
		}
		if (timeInterval.equals("day")) {
			long time = date1.getTime() / 1000L / 60L / 60L / 24L;
			return time - date2.getTime() / 1000L / 60L / 60L / 24L;
		}
		if (timeInterval.equals("hour")) {
			long time = date1.getTime() / 1000L / 60L / 60L;
			return time - date2.getTime() / 1000L / 60L / 60L;
		}
		if (timeInterval.equals("minute")) {
			long time = date1.getTime() / 1000L / 60L;
			return time - date2.getTime() / 1000L / 60L;
		}
		if (timeInterval.equals("second")) {
			long time = date1.getTime() / 1000L;
			return time - date2.getTime() / 1000L;
		}
		return date1.getTime() - date2.getTime();
	}


	/**
	 * 日期转星期
	 *
	 * @param datetime
	 * @return
	 */
	public static String dateToWeek(String datetime) {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		String[] weekDays = { "7", "1", "2", "3", "4", "6", "7" };
		Calendar cal = Calendar.getInstance(); // 获得一个日历
		Date datet = null;
		try {
			datet = f.parse(datetime);
			cal.setTime(datet);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
		if (w < 0)
			w = 0;
		return weekDays[w];
	}

/*	public static void main(String[] args) {
		System.out.println(dateToWeek("2018-11-15"));
	}*/

	public static enum TimeInterval {
		year("year"), quarter("quarter"), month("month"), week("week"), day("day"), hour("hour"), minute("minute"), second("second");

		private final String value;

		private TimeInterval(String value) {
			this.value = value;
		}

		public String value() {
			return this.value;
		}
	}

	public static List<Date> findDates(Date dBegin, Date dEnd)
	{
		List lDate = new ArrayList();
		lDate.add(new SimpleDateFormat("yyyy-MM-dd").format(dBegin));
		Calendar calBegin = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calBegin.setTime(dBegin);
		Calendar calEnd = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calEnd.setTime(dEnd);
		// 测试此日期是否在指定日期之后
		while (dEnd.after(calBegin.getTime()))
		{
			// 根据日历的规则，为给定的日历字段添加或减去指定的时间量
			calBegin.add(Calendar.DAY_OF_MONTH, 1);
//			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-mm-dd");
			lDate.add(new SimpleDateFormat("yyyy-MM-dd").format(calBegin.getTime()));

		}
		return lDate;
	}


}
