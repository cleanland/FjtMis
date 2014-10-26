package com.cleanland.www.fjtmis;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;


public class Login extends SwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //代码测试区
        if (false)
        {
            new MyHttpJob("http://www.baidu.com",new LinkedList<BasicNameValuePair>()){

                @Override
                protected void OnDone(String ResponsedStr) {
                    ((EditText)findViewById(R.id.siteUrl)).setText(ResponsedStr);
                }
            };

            Log.i("","");
            return;
        }
        //从SharedPreferences获取数据+保存到全局变量:
        //从SharedPreferences获取数据+保存到全局变量:
        //从SharedPreferences获取数据+保存到全局变量:
        //从SharedPreferences获取数据+保存到全局变量:
        //从SharedPreferences获取数据+保存到全局变量:
        //从SharedPreferences获取数据+保存到全局变量:
        SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        String s = preferences.getString("siteUrl", "");
        ((MyApplication) getApplication()).setSiteUrl(s);

        //将设置文件中的用户名和密码绑定到页面：
        EditText siteUrl = (EditText) findViewById(R.id.siteUrl);
        siteUrl.setText(((MyApplication) getApplication()).getSiteUrl());

        if (siteUrl.getText().toString().isEmpty())
            siteUrl.setText("http://fjtmis.huijumall.com");

        EditText acc = (EditText) findViewById(R.id.account);
        acc.setText(preferences.getString("account", ""));

        EditText pwd = (EditText) findViewById(R.id.pwd);
        pwd.setText(preferences.getString("pwd", ""));

        //定义事件：登录按钮。点击时：
        Button loginBtn = (Button) findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected void onPostExecute(String result) {
                        // TODO Auto-generated method stub
                        super.onPostExecute(result);
                        if (result.equals("Y")) {
                            //region 邪恶的服务，启动了。。。
                            //...
                            Intent i = new Intent();
                            i.setClass(Login.this, DaemonService.class);
                            i.setAction("com.example.administrator.kui.zzzzzzzz");
                            // 启动service
                            // 多次调用startService并不会启动多个service 而是会多次调用onStart
                            //Login.this.stopService(i);
                            Login.this.startService(i);
                            //endregion

                            startActivity(new Intent(Login.this, MainActivity.class));
                        } else {
                            if (result.isEmpty()) result = "无法正确连接到服务器。";//999
                            Toast.makeText(Login.this, "" + result, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    protected String doInBackground(Void... arg0) {
                        try {
                            //{ user: user, pwd: pwd,rand:new Date().toString() }
                            EditText siteUrl = (EditText) findViewById(R.id.siteUrl);
                            String siteUrlStr = siteUrl.getText().toString();

                            EditText acc = (EditText) findViewById(R.id.account);
                            String accStr = acc.getText().toString();

                            EditText pwd = (EditText) findViewById(R.id.pwd);
                            String pwdStr = pwd.getText().toString();

                            //将网址写入设置文件。下次就不必填写了。
                            SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("siteUrl", siteUrlStr);
                            editor.putString("account", accStr);
                            editor.putString("pwd", pwdStr);
                            editor.commit();

                            //将域名写入全局变量：
                            ((MyApplication) getApplication()).setSiteUrl(siteUrlStr);


                            LinkedList params = new LinkedList<BasicNameValuePair>();
                            params.add(new BasicNameValuePair("user", "" + accStr));
                            params.add(new BasicNameValuePair("pwd", "" + pwdStr));
                            params.add(new BasicNameValuePair("rand", new Date().toString()));
                            CwyWebJSON.client = new DefaultHttpClient();
                            return CwyWebJSON.postToUrl(((MyApplication) getApplication()).getSiteUrl() + "/home/logon", params);
                        } catch (Exception e) {
                            //Toast.makeText(Login.this,"晕，是不是网址不对啊？",Toast.LENGTH_LONG).show();
                            //上句好像加上也不起作用，不知道为啥。。。
                            e.printStackTrace();
                        }
                        return "";
                    }
                }.execute();
            }
        });

        // 如果域名不空+帐号不空+密码不空，立即自动登录:
        if (
                !s.isEmpty() &&
                        !((EditText) findViewById(R.id.account)).getText().toString().isEmpty() &&
                        !((EditText) findViewById(R.id.pwd)).getText().toString().isEmpty()
                ) {

            //region TEST AREA
            if (false) {

                //get loyout by findViewById:★★★★★★★★★★★★★★★★★★★★★★
                LinearLayout layout = (LinearLayout) findViewById(R.id.rootLayout);

                TextView tv = new TextView(this);
                tv.setBackgroundColor(Color.RED);
                tv.setText("23232323");
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(Login.this, "", Toast.LENGTH_LONG).show();
                    }
                });
                layout.addView(tv);
                Button bt = new Button(this);
                tv.setBackgroundColor(Color.RED);
                tv.setText("23232323");
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(Login.this, "", Toast.LENGTH_LONG).show();
                    }
                });
                layout.addView(bt);

                ListView lv = new ListView(Login.this);
                lv.setAdapter(new ListAdapter() {
                    @Override
                    public boolean areAllItemsEnabled() {
                        return false;
                    }

                    @Override
                    public boolean isEnabled(int position) {
                        return false;
                    }

                    @Override
                    public void registerDataSetObserver(DataSetObserver observer) {

                    }

                    @Override
                    public void unregisterDataSetObserver(DataSetObserver observer) {

                    }

                    @Override
                    public int getCount() {
                        return 100;
                    }

                    @Override
                    public Object getItem(int position) {
                        return null;
                    }

                    @Override
                    public long getItemId(int position) {
                        return position;
                    }

                    @Override
                    public boolean hasStableIds() {
                        return false;
                    }

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        TextView xtv = new TextView(Login.this);
                        xtv.setText("adfasdfasdfasdfqwerqwer");
                        xtv.setBackgroundColor(Color.GREEN);
                        return xtv;
                    }

                    @Override
                    public int getItemViewType(int position) {
                        return 0;
                    }

                    @Override
                    public int getViewTypeCount() {
                        return 1;
                    }

                    @Override
                    public boolean isEmpty() {
                        return false;
                    }
                });
                layout.addView(lv);
                return;
            }

            if (false) {
                new SimpleAdapter(this, new ArrayList<Map<String, String>>(), 111, new String[]{}, new int[]{});
                Log.i("klsdhfjklaaaaaaaaaaaa", "" + new Button(this).getResources());

            }
            //endregion

            loginBtn.performClick();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
