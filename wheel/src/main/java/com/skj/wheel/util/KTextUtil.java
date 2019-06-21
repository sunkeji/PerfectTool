package com.skj.wheel.util;

import android.text.TextUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by 孙科技 on 2017/10/31.
 */

public class KTextUtil {

    /**
     * 对String 类型判空
     *
     * @param str
     * @return
     */

    public static boolean isEmpty(CharSequence str) {
        if (TextUtils.isEmpty(str) || str.toString().equals("null")) {
            return true;
        }
        return false;
    }

    /**
     * 数据返回文本处理
     *
     * @param text
     * @return
     */
    public static String getText(String text) {
        return getText(text, "");
    }

    public static String getText(String text, String returnStr) {
        if (TextUtils.isEmpty(text) || "null".equals(text))
            return returnStr;
        return text;
    }

    /**
     * float 转string 保留两位小数
     *
     * @param amount
     * @return
     */
    public static String getFloatStr(float amount) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        String p = decimalFormat.format(amount);//format 返回的是字符串
        return p;
    }

    public static String getFloatStr(String str) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);
        str = nf.format(Double.parseDouble(str));
        return str;
    }


    /**
     * 字符隐藏
     *
     * @param txt
     * @param start
     * @param end
     * @return
     */
    public static String getShowHint(String txt, int start, int end) {
        String str = getText(txt);
        if (isEmpty(str))
            return "";
        if (start >= (str.length() - end))
            return "";
        if (str.length() < (str.length() - end))
            return "";
        String x = "";
        for (int i = 0; i < ((str.length() - end) - start); i++) {
            x = "*" + x;
        }
        String result = str.replace(str.substring(start, (str.length() - end)), x);
        return result;
    }

}
