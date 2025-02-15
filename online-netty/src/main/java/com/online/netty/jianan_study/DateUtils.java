package com.online.netty.jianan_study;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String formatDate(Date date) throws ParseException {
        return sdf.format(date);
    }

    public static Date parse(String strDate) throws ParseException {
        return sdf.parse(strDate);
    }
}
