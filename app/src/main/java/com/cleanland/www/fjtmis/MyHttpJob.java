package com.cleanland.www.fjtmis;

import android.os.AsyncTask;

import org.apache.http.message.BasicNameValuePair;

import java.util.LinkedList;

/**
 * Created by Administrator on 2014/10/25.
 * 用法:new MyHttpJob("http://www.baidu.com",null){ overwrite OnDone{}...};
 * 用途:开启一个线程，从WEB读取URL内容，然后回调用户指定的OnDone方法
 */
public abstract class MyHttpJob {

    public MyHttpJob(final String url, final LinkedList<BasicNameValuePair> params) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                // TODO bind data to the list of this page:
                OnDone(result);
            }

            @Override
            protected String doInBackground(Void... arg0) {
                try {
                    if (params == null)
                        return CwyWebJSON.postToUrl(url, new LinkedList<BasicNameValuePair>());
                    else
                        return CwyWebJSON.postToUrl(url, params);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return "";
            }
        }.execute();
    }

    protected abstract void OnDone(String ResponsedStr);
}
