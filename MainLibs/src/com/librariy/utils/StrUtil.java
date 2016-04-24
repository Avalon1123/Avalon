package com.librariy.utils;
// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 
// Source File Name:   MD5.java



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrUtil {
    private static char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '@', '#' };

    public synchronized static String generalUUID() {
        return java.util.UUID.randomUUID().toString();
    }

    /**
     * 得到当前日期的格式化字符串
     * 
     * @param formatString
     *            如：yyyy(年)-MM(月)-dd(日)-HH(时)-mm(分)-ss(秒)-SSS(毫秒)
     * 
     * @return 格式化过的当前日期字符串
     */
    public static String getFormatDate(String formatString) {
        Date now = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat(formatString);
        String ret = sdf.format(now);
        return ret;
    }

    /**
     * 得到指定日期的格式化字符串
     * 
     * @param date
     *            指定的日期
     * @param formatString
     *            如：yyyy(年)-MM(月)-dd(日)-HH(时)-mm(分)-ss(秒)-SSS(毫秒)
     * 
     * @return 格式化过的日期字符串
     */
    public static String getFormatDate(Date date, String formatString) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatString);
        String ret = sdf.format(date);
        return ret;
    }

    /**
     * @param 无
     * @return 当前日期
     */
    public static Date getCurrentDate() {
        Date now = new Date(System.currentTimeMillis());
        return now;
    }

    /**
     * 将格式化的日期字符串转换为日期。
     * 
     * @param formatString
     *            如：yyyy(年)-MM(月)-dd(日)-HH(时)-mm(分)-ss(秒)-SSS(毫秒)
     * 
     * @return 字符串转换后的日期。
     */
    public static Date formatDate(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(dateString);
            return date;
        } catch (ParseException e) {
            return new Date();
        }
    }

    public static Date formatDate(String dateString, String formatString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(formatString);
            Date date = sdf.parse(dateString);
            return date;
        } catch (ParseException e) {
            return new Date();
        }
    }

    public static Date formatSysDate() {
        String dateString = "";

        Calendar rightNow = Calendar.getInstance();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-mm-dd hh mm ss");
        dateString = fmt.format(rightNow.getTime());
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(dateString);
            return date;
        } catch (ParseException e) {
            return new Date();
        }
    }

    public static int parseInt(Object numberStr,int defaultValue) {
        return StrUtil.parseDouble(numberStr, defaultValue).intValue();
    }
    public static Double parseDouble(Object numberStr, double defaultValue) {
        if (numberStr == null)
            return defaultValue;
        Pattern pattern = Pattern.compile("^[\\-]{0,1}[0-9.]+$");
        Matcher matcher = pattern.matcher(numberStr.toString());
        if (matcher.find()) {
            return Double.valueOf(numberStr.toString());
        } else {
            return defaultValue;
        }
    }

    public static String formatHidden(String src,int prefixLen,int suffixLen,char hiddenChar) {
        if(src==null) return "";
        if(src.length()<=(prefixLen+suffixLen)) return src;
        char c[]=src.toCharArray();
        for(int i=prefixLen;i<(c.length-suffixLen);i++){
            c[i]=hiddenChar;
        }
        return new String(c);
    }

    public static void main(String[] args) {
        System.out.println(StrUtil.parseInt("12.578", -1));
        System.out.println(StrUtil.parseDouble("52.578", -1));
        System.out.println(StrUtil.parseInt("-12.578", -1));
        System.out.println(StrUtil.parseDouble("-52.578", -1));
        
        System.out.println(StrUtil.formatHidden("0123456789", 2,3,'*'));
        System.out.println(StrUtil.formatHidden("0123456789", 5,0,'*'));
        System.out.println(StrUtil.formatHidden("0123456789", 5,5,'*'));
        System.out.println(StrUtil.formatHidden("张珊珊", 1,0,'*'));
        
    }

}
