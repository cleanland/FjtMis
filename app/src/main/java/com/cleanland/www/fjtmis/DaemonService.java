package com.cleanland.www.fjtmis;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class DaemonService extends Service {

    //用于记录上次获取的日志列表内容。
    //如果变化了，就振动提醒一下客户吧。
    String lastResult = "";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        new Timer(true).schedule(new TimerTask() {
            @Override
            public void run() {
                // TODO Auto-generated method stub

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected void onPostExecute(String result) {
                        // TODO Auto-generated method stub
                        super.onPostExecute(result);
                        if (!lastResult.isEmpty() && !result.equals(lastResult)) {
                            //振动提醒客户，3秒钟
                            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(3000);
                        }
                        lastResult = result;
                    }

                    @Override
                    protected String doInBackground(Void... arg0) {
                        try {

                            LinkedList params = new LinkedList<BasicNameValuePair>();

                            SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                            String account = preferences.getString("account", "");
                            String pwd = preferences.getString("pwd", "");
                            String siteurl = preferences.getString("siteUrl", "");
                            Log.i("account", "" + account);//
                            Log.i("pwd", "" + pwd);//
                            Log.i("siteUrl", "" + siteurl);//
                            if (siteurl.isEmpty()) return "";//如果是空值，是不会出错，也不会振动的，参见onPostExecute。。。

                            params.add(new BasicNameValuePair("user", "" + account));
                            params.add(new BasicNameValuePair("pwd", "" + pwd));

                            CwyWebJSON.postToUrl(siteurl + "/home/logon", params);

                            params.clear();
                            return CwyWebJSON.postToUrl(siteurl + "/Office/GetBlogList", params);

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.i("后台服务", "出错了。。。。。。");//
                        }
                        return "";
                    }
                }.execute();
            }
        }, 5000, 1 * 60 * 1000);
    }

    @Override
    public void onDestroy() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), "FJTMIS SERVICE being killed..." + "", Toast.LENGTH_LONG).show();
            }
        }, 100);
        super.onDestroy();
    }
}  