package top.guitoubing.ssenotification.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

  public static final String YYYYMMDD = "yyyyMMdd";
  public static final String YYYY_MM_DD = "yyyy-MM-dd";
  public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd hh:mm:ss";

  private static SimpleDateFormat formatter = new SimpleDateFormat();

  public static String formatDateToString(Date date, String pattern) {
    formatter.applyPattern(pattern);
    return formatter.format(date);
  }

  public static Date formatStringToDate(String date, String pattern) throws ParseException {
    formatter.applyPattern(pattern);
    return formatter.parse(date);
  }


}
