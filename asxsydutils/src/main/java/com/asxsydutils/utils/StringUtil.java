package com.asxsydutils.utils;


 import org.apache.commons.lang3.StringUtils;

 import java.io.UnsupportedEncodingException;
 import java.math.BigInteger;
 import java.net.URLEncoder;
 import java.security.MessageDigest;
 import java.security.NoSuchAlgorithmException;
 import java.sql.Timestamp;
 import java.util.*;
 import java.util.regex.Matcher;
 import java.util.regex.Pattern;


public class StringUtil {
    public static String SEPARATOR = String.valueOf((char) 29);

    /**
     * 判断字符串是否为空
     *
     * @param value
     * @return
     */
    public static boolean isEmpty(String value) {
        if (value != null) {
            value = value.trim();
        }
        return StringUtils.isEmpty(value);
    }



    /**
     * 拆分字符串
     *
     * @param value
     * @param regex
     * @return
     */
    public static String[] spliteString(String value, String regex) {
        String[] result = null;
        if (StringUtil.isNotEmpty(value)) {
            result = value.split(regex);
        }
        return result;
    }


    public static boolean isNotEmpty(String arg0) {
        return !StringUtils.isEmpty(arg0);
    }

    public static boolean isBlank(String str) {
        return StringUtils.isBlank(str);
    }

    public static String substringAfter(String str, String separator) {
        return StringUtils.substringAfter(str, separator);
    }


    public static String substringBefore(String str, String separator) {
        return StringUtils.substringBefore(str, separator);
    }

    public static String GuidEmpty() {

        return "00000000-0000-0000-0000-000000000000";
    }

    public static Timestamp getDatatime() {
        return new java.sql.Timestamp(new Date().getTime());

    }

    public static String getPrimaryKey() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    /**
     * 去掉空格换行
     * @param str
     * @return
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
    public static String getMD5Str(String str) {
        byte[] digest = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            digest  = md5.digest(str.getBytes("utf-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //16是表示转换为16进制数
        String md5Str = new BigInteger(1, digest).toString(16);
        return md5Str;
    }

    /**
     * @param param 参数
     * @param encode 编码
     * @param isLower 是否小写
     * @return
     */
    public static String formatUrlParam(Map<String, Object> param, String encode, boolean isLower) {
        String params = "";
        Map<String, Object> map = param;

        try {
            List<Map.Entry<String, Object>> itmes = new ArrayList<Map.Entry<String, Object>>(map.entrySet());

            //对所有传入的参数按照字段名从小到大排序
            //Collections.sort(items); 默认正序
            //可通过实现Comparator接口的compare方法来完成自定义排序
            Collections.sort(itmes, new Comparator<Map.Entry<String, Object>>() {
                @Override
                public int compare(Map.Entry<String, Object> o1, Map.Entry<String, Object> o2) {
                    // TODO Auto-generated method stub
                    return (o1.getKey().toString().compareTo(o2.getKey()));
                }
            });

            //构造URL 键值对的形式
            StringBuffer sb = new StringBuffer();
            for (Map.Entry<String, Object> item : itmes) {
                if (StringUtils.isNotBlank(item.getKey())) {
                    String key = item.getKey();
                    String val = item.getValue().toString();
                    val = URLEncoder.encode(val, encode);
                    if (isLower) {
                        sb.append(key.toLowerCase() + "=" + val);
                    } else {
                        sb.append(key + "=" + val);
                    }
                    sb.append("&");
                }
            }

            params = sb.toString();
            if (!params.isEmpty()) {
                params = params.substring(0, params.length() - 1);
            }
        } catch (Exception e) {
            return "";
        }
        return params;
    }
}

