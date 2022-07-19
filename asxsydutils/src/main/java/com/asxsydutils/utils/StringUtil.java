package com.asxsydutils.utils;


 import net.sourceforge.pinyin4j.PinyinHelper;
 import org.apache.commons.lang3.StringUtils;
 import java.io.UnsupportedEncodingException;
 import java.lang.reflect.Field;
 import java.math.BigInteger;
 import java.net.URLEncoder;
 import java.security.MessageDigest;
 import java.security.NoSuchAlgorithmException;
 import java.sql.Timestamp;
 import java.text.SimpleDateFormat;
 import java.time.LocalDate;
 import java.time.LocalDateTime;
 import java.time.ZoneId;
 import java.time.format.DateTimeFormatter;
 import java.time.temporal.TemporalAdjusters;
 import java.util.*;
 import java.util.regex.Matcher;
 import java.util.regex.Pattern;
 import org.apache.commons.codec.binary.Base64;
 import javax.crypto.Cipher;
 import javax.crypto.spec.IvParameterSpec;
 import javax.crypto.spec.SecretKeySpec;

public class StringUtil {
    public static String SEPARATOR = String.valueOf((char) 29);
    private static boolean[] bcdLookup;

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


    public static byte[] AES_CBC_Decrypt(byte[] data, byte[] key, byte[] iv) throws Exception{
        Cipher cipher = getCipher(Cipher.DECRYPT_MODE, key, iv);
        return cipher.doFinal(data);
    }

    private static Cipher getCipher(int mode, byte[] key, byte[] iv) throws Exception{
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        //因为AES的加密块大小是128bit(16byte), 所以key是128、192、256bit无关
        //System.out.println("cipher.getBlockSize()： " + cipher.getBlockSize());

        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        cipher.init(mode, secretKeySpec, new IvParameterSpec(iv));

        return cipher;
    }

    public static String interceptTime(String timeStr) { if (!StringUtils.isBlank(timeStr)) { if (timeStr.contains("T")) { DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH); LocalDateTime ldt = LocalDateTime.parse(timeStr, df); ZoneId currentZone = ZoneId.of("UTC"); ZoneId newZone = ZoneId.of("Asia/Shanghai"); timeStr = ldt.atZone(currentZone).withZoneSameInstant(newZone).toLocalDateTime().toString(); } if (timeStr.length() >= 10) { return timeStr.substring(0, 10); } } return timeStr; }
public static  String getDateFormat(java.util.Date date,String format){

    SimpleDateFormat df = new SimpleDateFormat(format);//设置日期格式
   return df.format(date);
}
    public static Map<String, Object> ObjectToMap(Object obj)  {
        try{
        if(obj == null){
            return null;
        }

        Map<String, Object> map = new HashMap<String, Object>();

        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            map.put(field.getName(), field.get(obj));
        }

        return map;
        }catch (Exception ex){

            System.out.print("ObjectToMap转换错误");
            return  null;
        }
    }
    /**
     * 获取汉字首字母
     *
     * @param text 文本
     * @return {@link String}
     */
    public static String getPinyinInitials(String text) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            String[] s = PinyinHelper.toHanyuPinyinStringArray(ch);
            if (s != null) {
                sb.append(s[0].charAt(0));
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }
     ///获取当月第一天
    public static String FirstDate(){
        LocalDate today = LocalDate.now();
        // 获取本月的第一天
        LocalDate firstday = LocalDate.of(today.getYear(), today.getMonth(), 1);
        return  firstday.toString()+" 00:00:00";
    }
    ///获取最后一天
    public static String LastDte (){
        LocalDate today = LocalDate.now();
        // 获取本月的第一天
        LocalDate lastDay = today.with(TemporalAdjusters.lastDayOfMonth());
        return  lastDay.toString()+" 23:59:59";
    }
}

