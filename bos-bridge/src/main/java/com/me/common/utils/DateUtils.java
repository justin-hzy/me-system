package com.me.common.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static String getAboardCreateDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date tomorrowDate = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(tomorrowDate);
    }

    public static String getAboardEndDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date tomorrowDate = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(tomorrowDate);
    }

    public static String getMonthStartDate(){
        Calendar calendar = Calendar.getInstance();

        // 设置为本月第一天
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        String startDateStr = formatDate(calendar.getTime());
        return startDateStr+" 00:00:00";

    }

    private static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static String getMonthEndDate(){
        // 创建 Calendar 对象
        Calendar calendar = Calendar.getInstance();

        // 设置为本月第一天
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        // 将日期加上一个月后再减去一天得到本月最后一天
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DATE, -1);
        String endDateStr = formatDate(calendar.getTime());

        return endDateStr+" 23:59:59";
    }

    public static String getCurrentDateTime(){
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return currentDateTime.format(formatter);
    }

    public static Integer formatConversion(String dateTime){
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        LocalDateTime localDateTime = LocalDateTime.parse(dateTime, inputFormatter);
        return Integer.valueOf(localDateTime.format(outputFormatter));
    }
}
