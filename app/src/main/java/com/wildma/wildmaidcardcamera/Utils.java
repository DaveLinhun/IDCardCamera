package com.wildma.wildmaidcardcamera;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Utils {

    /**
     * 判断身份证号是否有效
     *
     * @param id 身份证号码
     * @return true有效，false无效
     */
    public static boolean isSfzh(String id) {
        String birthstr, sexstr;

        // 判断格式
        if (TextUtils.isEmpty(id))
            return false;
        if (id.length() != 15 && id.length() != 18)
            return false;
        String idstr = id.toUpperCase();
        if (idstr.length() == 15) {
            if (!idstr.matches("\\d{15}")) {// 格式错误
                return false;
            }
            birthstr = "19" + idstr.substring(6, 12);
            sexstr = idstr.substring(12);
        } else {
            if (!idstr.matches("^\\d{17}(\\d|X|x)")) {// 格式错误
                return false;
            }
            birthstr = idstr.substring(6, 14);
            sexstr = idstr.substring(14, 17);
        }
        // 判断出生日期
        if (!isDate(birthstr, "yyyyMMdd"))
            return false;

        // 判断性别顺序号              2017.11.7  李发根修改，存在性别序号为000的身份证，例如411425198412140003
//        if (sexstr.equals("000")) {
//            return false;
//        }

        if (idstr.length() == 18) {
            // 判断校验位
            String parity = getParityBitForSfzh(idstr);
            return parity.length() == 1 && idstr.endsWith(parity);
        }
        return true;
    }

    /**
     * 生成身份证号的校验位
     *
     * @param id 身份证号（17位及以上）
     * @return 校验位
     */
    private static String getParityBitForSfzh(String id) {
        if (TextUtils.isEmpty(id) || id.length() < 17)
            return "";

        int[] weight = new int[]{
                7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5,
                8, 4, 2};
        String parity = "10X98765432";
        char[] s17 = id.substring(0, 17).toCharArray();
        int s = 0;
        for (int i = 0; i < s17.length; i++) {
            s += (s17[i] - 0x30) * weight[i];
        }

        return String.valueOf(parity.charAt(s % 11));
    }

    /**
     * 判断字符串是否为日期型
     *
     * @param str      需要判断的字符串
     * @param template 字符串格式模版
     */
    private static boolean isDate(String str, String template) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(template))
            return false;

        SimpleDateFormat format = new SimpleDateFormat(template, Locale.SIMPLIFIED_CHINESE);
        format.setLenient(false);
        try {
            format.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
