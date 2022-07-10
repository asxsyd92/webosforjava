package com.asxsydutils.utils;

import okhttp3.*;

import java.io.*;
import java.net.*;


public class HttpHelper {
    /**
     * 定义全局默认编码格式
     */
    private static final String CHARSET_NAME = "UTF-8";
    /**
     * 定义全局OkHttpClient对象
     */
    private static final OkHttpClient httpClient = new OkHttpClient();

    /**
     * 功能说明：同步调用
     * 修改说明：
     *
     * @param request
     * @return
     * @throws IOException
     * @author 爱上歆随懿恫
     * @date 2018年1月8日 上午10:20:55
     */
    public static Response execute(Request request) throws IOException {
        return httpClient.newCall(request).execute();
    }

    /**
     * 功能说明：开启异步线程调用
     * 修改说明：
     *
     * @param request
     * @param responseCallback
     * @author 爱上歆随懿恫
     * @date 2018年1月8日 上午10:23:00
     */
    public static void enqueue(Request request, Callback responseCallback) {
        httpClient.newCall(request).enqueue(responseCallback);
    }

    /**
     * 功能说明：开启异步线程调用，且不在意返回结果（实现空callback）
     * 修改说明：
     *
     * @param request
     * @author 爱上歆随懿恫
     * @date 2018年1月8日 上午10:24:53
     */
    public static void enqueue(Request request) {
        httpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call arg0, IOException arg1) {

            }

            @Override
            public void onResponse(Call arg0, Response arg1) throws IOException {

            }

        });
    }
    public static String sendGet(String urlNameString) throws IOException {
        String result = "";


        Request req = new Request.Builder().url(urlNameString).removeHeader("User-Agent").addHeader("User-Agent",
               "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36").build();

        Response response = httpClient.newCall(req).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }
        result = response.body().string();

        return result;
    }
    public static String sendAgentChromeGet(String urlNameString) throws IOException {
        String result = "";


        Request req = new Request.Builder().url(urlNameString).removeHeader("User-Agent").addHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36").build();

        Response response = httpClient.newCall(req).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }
        result = response.body().string();

        return result;
    }
    /**
     * 功能说明：向指定URL发送GET方法的请求
     * 修改说明：
     *
     * @param url   发送请求的URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL所代表远程资源的响应结果
     * @throws IOException
     * @author 爱上歆随懿恫
     * @date 2018年1月8日 上午10:19:11
     */
    public static String sendGet(String url, String param) throws IOException {
        String result = "";
        String urlNameString = url + "?" + param;

        Request req = new Request.Builder().url(urlNameString).build();
        Response response = httpClient.newCall(req).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }
        result = response.body().string();

        return result;
    }

    /**
     * 功能说明：向指定URL发送GET方法的请求
     * 修改说明：
     *
     * @param url      发送请求的URL
     * @param param    请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @param encoding 设置响应信息的编码格式，如utf-8
     * @return URL所代表远程资源的响应结果
     * @throws IOException
     * @author 爱上歆随懿恫
     * @date 2018年1月8日 上午10:54:55
     */
    public static String sendGet(String url, String param, String encoding) throws IOException {
        String result = "";
        String urlNameString = url + "?" + param;

        Request req = new Request.Builder().url(urlNameString).build();
        Response response = httpClient.newCall(req).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }
        result = response.body().string();

        if (null == encoding || encoding.equals("")) {
            return result;
        }
        byte[] bresult = result.getBytes();
        result = new String(bresult, encoding);

        return result;
    }
public static String SendPostFormData(String url,RequestBody body) throws IOException {



    /**
     * 设置超时时间
     */
    OkHttpClient okHttpClient = new OkHttpClient
            .Builder()
            .build();

    //表单数据参数填入

    Request request = new Request.Builder()
            .url(url)
            .post(body)
            .build();

    Call call = okHttpClient.newCall(request);

        Response response = call.execute();

        String result = response.body().string();//得到数据
   return result;

}


    /**
     * 功能说明：向指定URL发送POST方法的请求
     * 修改说明：
     *
     * @param url      发送请求的URL
     * @param jsonData 请求参数，请求参数应该是Json格式字符串的形式。
     * @return URL所代表远程资源的响应结果
     * @throws IOException
     * @author 爱上歆随懿恫
     * @date 2018年1月8日 上午10:54:55
     */
    public static String sendPost(String url, String jsonData) throws IOException {
        String result = "";
        RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonData);
        Request req = new Request.Builder().url(url).header("Content-Type", "application/x-www-form-urlencoded").post(body).build();
        Response response = httpClient.newCall(req).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }
        result = response.body().string();

        return result;
    }

    /**
     * 功能说明：向指定URL发送POST方法的请求
     * 修改说明：
     *
     * @param url           发送请求的URL
     * @param jsonData      请求参数，请求参数应该是Json格式字符串的形式。
     * @param encoding      设置响应信息的编码格式，如utf-8
     * @param authorization 授权
     * @param postmanToken  票证
     * @return URL所代表远程资源的响应结果
     * @throws IOException
     * @author 爱上歆随懿恫
     * @date 2018年1月8日 上午10:54:55
     */
    public static String sendPost(String url, String jsonData, String encoding, String authorization, String postmanToken) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection con = realUrl.openConnection();
            HttpURLConnection conn = (HttpURLConnection) con;
            // 设置通用的请求属性
            conn.setRequestMethod("POST"); // 设置Post请求
            conn.setConnectTimeout(5 * 1000);
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded"); // 设置内容类型
           // conn.setRequestProperty("authorization", authorization);
           // conn.setRequestProperty("postman-token", postmanToken);

            // conn.setRequestProperty("Content-Length",
            // String.valueOf(param.length())); //设置长度
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(new OutputStreamWriter(
                    conn.getOutputStream(), encoding));
            // 发送请求参数
            // out.print(param);
            out.write(jsonData);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            byte[] bresult = result.getBytes();
            result = new String(bresult, encoding);
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 功能说明：向指定 URL 发送POST方法的请求
     * 修改说明：
     *
     * @param url      发送请求的 URL
     * @param jsonData 请求参数，请求参数应该是Json格式字符串的形式。
     * @param encoding 设置响应信息的编码格式，如utf-8
     * @return url所代表远程资源的响应结果
     * @throws IOException
     * @author 爱上歆随懿恫
     * @date 2018年1月8日 下午2:17:06
     */
    public static String sendPost(String url, String jsonData, String encoding) throws IOException {
        String result = "";
        RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonData);
        Request req = new Request.Builder().url(url).header("Content-Type", "application/json").post(body).build();
        Response response = httpClient.newCall(req).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }
        result = response.body().string();

        if (null == encoding || encoding.equals("")) {
            return result;
        }
        byte[] bresult = result.getBytes();
        result = new String(bresult, encoding);

        return result;
    }

    /**
     * 功能说明：上传文件
     * 修改说明：
     *
     * @param url  上传url
     * @param file 要上传的文件对象
     * @return 返回上传的结果
     * @author 爱上歆随懿恫
     * @date 2018年1月8日 下午2:15:51
     */
    public static String uploadPost(String url, File file) {
        DataOutputStream dos = null;
        FileInputStream fis = null;
        DataInputStream dis = null;
        BufferedReader in = null;
        String result = "";
        String end = "\r\n";
        String twoHyphens = "--"; // 用于拼接
        String boundary = "*****"; // 用于拼接 可自定义
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection con = realUrl.openConnection();
            HttpURLConnection conn = (HttpURLConnection) con;
            // 设置通用的请求属性
            conn.setRequestMethod("POST"); // 设置Post请求
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setConnectTimeout(5 * 1000);
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=" + boundary); // 设置内容类型

            // 获取URLConnection对象对应的输出流
            dos = new DataOutputStream(conn.getOutputStream());
            //1、写入媒体头部分
            StringBuilder sb = new StringBuilder();
            sb.append(twoHyphens).append(boundary).append(end);
            sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + file.getName() + "\"").append(end);
            sb.append("Content-Type:application/octet-stream").append(end).append(end);
            byte[] head = sb.toString().getBytes("utf-8");
            dos.write(head);

            //2、写入媒体正文部分， 对文件进行传输
            fis = new FileInputStream(file);
            dis = new DataInputStream(fis);
            byte[] buffer = new byte[8192]; // 8K
            int count = 0;
            while ((count = dis.read(buffer)) != -1) {
                dos.write(buffer, 0, count);
            }

            //3、写入媒体结尾部分。
            byte[] foot = (end + twoHyphens + boundary + twoHyphens + end).getBytes("utf-8");
            dos.write(foot);
            dos.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            byte[] bresult = result.getBytes();
            result = new String(bresult, "utf-8");
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (dos != null) {
                    dos.close();
                }
                if (dis != null) {
                    dis.close();
                }
                if (fis != null) {
                    fis.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 功能说明：下载素材文件
     * 修改说明：
     *
     * @param url         下载的接口地址
     * @param param       参数
     * @param outFileName 输出文件
     * @return 成功返回true，失败返回false
     * @throws IOException
     * @author 爱上歆随懿恫
     * @date 2018年1月9日 下午2:06:56
     */
    public static boolean downloadFile(String url, String param, String outFileName) throws IOException {
     try {
         File file=new File(outFileName);


         boolean result = false;
         String urlNameString = url ;
         Request req = new Request.Builder().header("Referer","https://www.tooopen.com/view/2358284.html").url(url).build();

         Response response = httpClient.newCall(req).execute();
         if (!response.isSuccessful()) {
             throw new IOException("Unexpected code " + response);
         }
         if (response.body().contentType().toString().toLowerCase().contains("application/json") || response.body().contentType().toString().toLowerCase().contains("text/plain")) {
             throw new IOException("下载资源失败,下载地址为=" + urlNameString);
         } else {
             InputStream in = response.body().byteStream();
             response.body().contentType();

             FileOutputStream out = new FileOutputStream(outFileName);
             int bufferSize = 2048;
             byte[] data = new byte[bufferSize];
             int length = 0;
             while ((length = in.read(data, 0, bufferSize)) > 0) {
                 out.write(data, 0, length);
             }
             out.close();
             in.close();
             result = true;
         }
         return result;
     }catch (Exception ex){
         System.out.print(ex.getMessage());
      return  false;}
    }


    /**
     * 功能说明：获取mac地址
     * 修改说明：
     *
     * @param ia
     * @return
     * @throws SocketException
     * @author 爱上歆随懿恫
     */
    public static String getLocalMac(InetAddress ia) throws SocketException {

        //获取网卡，获取地址
        byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
        System.out.println("mac数组长度：" + mac.length);
        StringBuffer sb = new StringBuffer("");
        for (int i = 0; i < mac.length; i++) {
            if (i != 0) {
                sb.append("-");
            }
            //字节转换为整数
            int temp = mac[i] & 0xff;
            String str = Integer.toHexString(temp);
            System.out.println("每8位:" + str);
            if (str.length() == 1) {
                sb.append("0" + str);
            } else {
                sb.append(str);
            }
        }
        System.out.println("本机MAC地址:" + sb.toString().toUpperCase());
        return sb.toString().toUpperCase();
    }



}
