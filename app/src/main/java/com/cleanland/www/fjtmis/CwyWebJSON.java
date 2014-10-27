package com.cleanland.www.fjtmis;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

/**
 * @author Administrator 2014年10月15日15:17:51
 */
public class CwyWebJSON {

    static DefaultHttpClient client = new DefaultHttpClient();

    /**
     * 读取URL返回的文本内容
     *
     * @param url 网址
     * @return 站点返回的页面内容.字符串
     * @throws Exception 要求调用者必须TRY.
     */
    protected static String getByUrl(String url)
            throws Exception {
        String result = null;
        HttpGet getMethod = new HttpGet(url);
        try {
            // getMethod.setHeader("User-Agent", USER_AGENT);
            HttpResponse httpResponse = client.execute(getMethod);

            // statusCode == 200 正常
            // 处理返回的httpResponse信息
            Log.i("正在向URL请求数据（GET）:", "URL = "+url);
            result = retrieveInputStream(httpResponse.getEntity());
            Log.i("从URL GET回来的内容：", "结果内容 = "+result);
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            getMethod.abort();
        }
        return result;
    }

    protected static String retrieveInputStream(HttpEntity httpEntity) {
        int length = (int) httpEntity.getContentLength();
        // the number of bytes of the content, or a negative number if unknown.
        // If the content length is known but exceeds Long.MAX_VALUE, a negative
        // number is returned.
        // length==-1，下面这句报错，println needs a message
        if (length < 0)
            length = 10000;
        StringBuffer stringBuffer = new StringBuffer(length);
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(
                    httpEntity.getContent(), HTTP.UTF_8);

            char buffer[] = new char[length];
            int count;
            while ((count = inputStreamReader.read(buffer, 0, length - 1)) > 0) {
                stringBuffer.append(buffer, 0, count);
            }
        } catch (UnsupportedEncodingException e) {

        } catch (IllegalStateException e) {

        } catch (IOException e) {

        }
        return stringBuffer.toString();
    }

    /**
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    public static String postToUrl(String url,
                                   LinkedList<BasicNameValuePair> params)
            throws Exception {
        // params = new LinkedList<BasicNameValuePair>();
        // params.add(new BasicNameValuePair("param1", "Post方法"));
        // params.add(new BasicNameValuePair("param2", "第二个参数"));
        String result = "";
        try {

            HttpPost postMethod = new HttpPost(url);
            postMethod.setEntity(new UrlEncodedFormEntity(params, "utf-8")); // 将参数填入POST
            Log.i("正在向URL POST数据:", "URL = " + url+".params:"+params);

            client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
            client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);

            HttpResponse response = client.execute(postMethod); // 执行POST方法
            result = EntityUtils.toString(response.getEntity(), "utf-8");
            Log.i("POST 状态:", "状态代码 = "
                    + response.getStatusLine().getStatusCode()+url); // 获取响应码
            Log.i("向POST后得到的消息", "内容= " + result); // 获取响应内容

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    /**
     * HTML（转码）
     * HTML STRING 保存到SERVER时，会转码。防止注入。。。显示时就需要反转码
     *
     * @param src
     * @return
     */
    public static String escape(String src) {
        int i;
        char j;
        StringBuffer tmp = new StringBuffer();
        tmp.ensureCapacity(src.length() * 6);
        for (i = 0; i < src.length(); i++) {
            j = src.charAt(i);
            if (Character.isDigit(j) || Character.isLowerCase(j)
                    || Character.isUpperCase(j))
                tmp.append(j);
            else if (j < 256) {
                tmp.append("%");
                if (j < 16)
                    tmp.append("0");
                tmp.append(Integer.toString(j, 16));
            } else {
                tmp.append("%u");
                tmp.append(Integer.toString(j, 16));
            }
        }
        return tmp.toString();
    }

    /**
     * HTML。（反（转码））
     * HTML STRING 保存到SERVER时，会转码。防止注入。。。显示时就需要反转码
     *
     * @param src
     * @return
     */
    public static String unescape(String src) {
        if (src == null) return null;
        StringBuffer tmp = new StringBuffer();
        tmp.ensureCapacity(src.length());
        int lastPos = 0, pos = 0;
        char ch;
        while (lastPos < src.length()) {
            pos = src.indexOf("%", lastPos);
            if (pos == lastPos) {
                if (src.charAt(pos + 1) == 'u') {
                    ch = (char) Integer.parseInt(src
                            .substring(pos + 2, pos + 6), 16);
                    tmp.append(ch);
                    lastPos = pos + 6;
                } else if (src.charAt(pos + 1) == ' '
                        || src.charAt(pos + 1) == ';') {
                    tmp.append(src.substring(pos, pos + 1));
                    lastPos = pos + 1;
                } else {
                    ch = (char) Integer.parseInt(src
                            .substring(pos + 1, pos + 3), 16);
                    tmp.append(ch);
                    lastPos = pos + 3;
                }
            } else {
                if (pos == -1) {
                    tmp.append(src.substring(lastPos));
                    lastPos = src.length();
                } else {
                    tmp.append(src.substring(lastPos, pos));
                    lastPos = pos;
                }
            }
        }
        return tmp.toString();
    }
}
