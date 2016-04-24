package com.librariy.net;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

public class UrlTools {
    private static final String TAG = UrlTools.class.getSimpleName();
    private static String HEX_S = "0123456789ABCDEF";
    private static char HEX_C[] = HEX_S.toCharArray();

    public static String encodeURI(String src, String charset) {
        try {
            StringBuffer sb = new StringBuffer("");
            if (src == null || src.length() <= 0) {
                return "";
            }
            Matcher mMatcher = Pattern.compile("[^;/?:@&=+$,#0-9a-zA-Z-_.!~*'()]+", Pattern.CASE_INSENSITIVE).matcher(src);
            while (mMatcher.find()) {
                byte[] b = mMatcher.group().getBytes(charset);
                StringBuffer s = new StringBuffer("");
                for (int i = 0; i < b.length; i++) {
                    s.append("%" + HEX_C[(byte) ((b[i] >> 4) & 0x000F)] + HEX_C[(byte) ((b[i]) & 0x000F)]);
                }
                mMatcher.appendReplacement(sb, s.toString());
            }
            mMatcher.appendTail(sb);
            return sb.toString();
        } catch (Exception e) {
            Log.e(TAG, "UrlTools.encodeURI(" + src + "," + charset + ")", e);
            return src;
        }
    }

    public static String encodeURIComponent(String src, String charset) {
        try {
            StringBuffer sb = new StringBuffer("");
            if (src == null || src.length() <= 0) {
                return "";
            }
            Matcher mMatcher = Pattern.compile("[^0-9a-zA-Z-_.!~*'()]+", Pattern.CASE_INSENSITIVE).matcher(src);
            while (mMatcher.find()) {
                byte[] b = mMatcher.group().getBytes(charset);
                StringBuffer s = new StringBuffer("");
                for (int i = 0; i < b.length; i++) {
                    s.append("%" + HEX_C[(byte) ((b[i] >> 4) & 0x000F)] + HEX_C[(byte) ((b[i]) & 0x000F)]);
                }
                mMatcher.appendReplacement(sb, s.toString());
            }
            mMatcher.appendTail(sb);
            return sb.toString();
        } catch (Exception e) {
            Log.e(TAG, "UrlTools.encodeURIComponent(" + src + "," + charset + ")", e);
            return src;
        }
    }

    public static String decodeURI(String src, String charset) {
        try {
            StringBuffer sb = new StringBuffer("");
            if (src == null || src.length() <= 0) {
                return "";
            }
            Matcher mMatcher = Pattern.compile("(?:%[0-9A-F]{2})+", Pattern.CASE_INSENSITIVE).matcher(src);
            byte[] buff = new byte[1024];
            while (mMatcher.find()) {
                String b = mMatcher.group().replaceAll("%", "").toUpperCase();
                int i = 0;
                for (i = 0; i < b.length() / 2; i++) {
                    int c0 = HEX_S.indexOf(b.charAt(2 * i));
                    int c1 = HEX_S.indexOf(b.charAt(2 * i + 1));
                    buff[i] = ((byte) (((c0 << 4) & 0x00F0) | (c1 & 0x000F)));
                }
                mMatcher.appendReplacement(sb, new String(buff, 0, i, charset));
            }
            mMatcher.appendTail(sb);
            return sb.toString();
        } catch (Exception e) {           
            Log.e(TAG, "UrlTools.decodeURI(" + src + "," + charset + ")", e);
            e.printStackTrace();
            return src;
        }
    }

    public static void main(String[] args) {
        String s1 = UrlTools.encodeURI("http://192.168.1.24:80/api/武汉/江汉汉区/Info?name=张三&sex=男", "GB2312");
        String s2 = UrlTools.encodeURIComponent("http://192.168.1.24:80/api/武汉/我江汉区/Info?name=张三&sex=男", "UTF-8");
        String s3 = UrlTools.decodeURI(s1, "GB2312");
        String s4 = UrlTools.decodeURI(s2, "UTF-8");
        System.out.println("s1=" + s1);
        System.out.println("s2=" + s2);
        System.out.println("s3=" + s3);
        System.out.println("s4=" + s4);
    }
}