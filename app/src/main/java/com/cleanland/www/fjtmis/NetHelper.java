package com.cleanland.www.fjtmis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;


/**
 * CWY@2014年10月26日16:02:34
 * 来自网络文章：http://www.cnblogs.com/wainiwann/archive/2012/03/12/2391810.html
 * 用于APP的自动更新，这是它的一个底层辅助类
 * 包括三个方法：
 * 1 读取一个网上的图片，貌似有用。loadImage
 * 2 读取一个网页的内容，不能POST，意义不大。httpStringGet
 * 3 检查网络状态，貌似也用处不大。checkNetWorkStatus
 */
public class NetHelper {

    public static String httpStringGet(String url) throws Exception {
        return httpStringGet(url, "utf-8");
    }

    /**
     * 
     * 
     * @param url
     * @return
     */
    public static Drawable loadImage(String url) {
        try {
            return Drawable.createFromStream(
                    (InputStream) new URL(url).getContent(), "test");
        } catch (MalformedURLException e) {
            Log.e("exception", e.getMessage());
        } catch (IOException e) {
            Log.e("exception", e.getMessage());
        }
        return null;
    }

    public static String httpStringGet(String url, String enc) throws Exception {
        // This method for HttpConnection
        String page = "";
        BufferedReader bufferedReader = null;
        try {
            HttpClient client = new DefaultHttpClient();
            client.getParams().setParameter(CoreProtocolPNames.USER_AGENT,
                    "android");
            
             HttpParams httpParams = client.getParams();
             HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
             HttpConnectionParams.setSoTimeout(httpParams, 5000);
             
            HttpGet request = new HttpGet();
            request.setHeader("Content-Type", "text/plain; charset=utf-8");
            request.setURI(new URI(url));
            HttpResponse response = client.execute(request);
            bufferedReader = new BufferedReader(new InputStreamReader(response
                    .getEntity().getContent(), enc));

            StringBuffer stringBuffer = new StringBuffer("");
            String line = "";

            String NL = System.getProperty("line.separator");
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line + NL);
            }
            bufferedReader.close();
            page = stringBuffer.toString();
            Log.i("page", page);
            System.out.println(page + "page");
            return page;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    Log.d("BBB", e.toString());
                }
            }
        }
    }

    //目前没有用到这方法，感觉也未必有用。可能就是垃圾代码了。CWY@2014年10月26日15:53:56
    public static boolean checkNetWorkStatus(Context context) {
        boolean result;
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        if (netinfo != null && netinfo.isConnected()) {
            result = true;
            Log.i("NetStatus", "The net was connected");
        } else {
            result = false;
            Log.i("NetStatus", "The net was bad!");
        }
        return result;
    }
}