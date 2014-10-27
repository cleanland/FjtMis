package com.cleanland.www.fjtmis;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class Act_BlogDetail extends SwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("工作日志");

        //$.GET blogData from server:
        //by session implicitly...
        new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                // TODO bind data to the list of this page:
                /*
                [
                    {
                        "ID": 16,
                        "Title": "dddddddd",
                        "Content": "HTML CODES...",
                        "BlogTypeID_DisplayText": "未分类",
                        "BlogTypeID": 0,
                        "ClickedCount": 0,
                        "RegTime": "2013-11-05 18:11:36",
                        "RegEmpID_DisplayText": "管理员22233",
                        "RegEmpID": 2,
                        "FlagDeleted": false,
                        "Best": false,
                        "ModTime": "2014-06-27 17:45:07",
                        "RegDeptID_DisplayText": "系统管理部",
                        "RegDeptID": 1,
                        "AllowEdit": "True"
                    }
                ]
                */

                JSONObject jsobj = null;
                try {
                    final Act_BlogDetail ctx = Act_BlogDetail.this;
                    jsobj = new JSONArray(result).getJSONObject(0);

                    LinearLayout layout = new LinearLayout(ctx);
                    layout.setOrientation(LinearLayout.VERTICAL);


                    TextView title = new TextView(ctx);
                    title.setPadding(10, 10, 10, 10);
                    title.setBackgroundColor(Color.GRAY);
                    title.setText(jsobj.getString("RegEmpID_DisplayText") + "@" + jsobj.getString("Title"));

                    FrameLayout frame = new FrameLayout(ctx);
                    frame.removeAllViews();

                    WebView wx = new WebView(ctx);
                    if (false) {
                        wx.loadData(
                                CwyWebJSON.unescape(jsobj.getString("Content")),
                                "text/html",
                                "UTF-8");//有中文就乱码啦！！
                    }

                    wx.loadData(
                            CwyWebJSON.unescape(jsobj.getString("Content")),
                            "text/html; charset=UTF-8",
                            null
                    );//这种写法可以正确解码
                    frame.addView(wx);
                    frame.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

                    layout.addView(title);
                    layout.addView(frame);

                    //如果是自己的日志，就可以修改：
                    if (jsobj.getString("AllowEdit").equals("True")) {
                        LinearLayout editArea = new LinearLayout(ctx);
                        editArea.setOrientation(LinearLayout.HORIZONTAL);
                        final EditText newblog = new EditText(ctx);
                        newblog.setHint("点击追加日志内容");
                        newblog.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                        editArea.addView(newblog);


                        Button ok = new Button(ctx);
                        ok.setText("OK");
                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                postToServer(newblog.getText().toString());
                            }
                        });
                        editArea.addView(ok);

                        layout.addView(editArea);
                    }

                    ctx.setContentView(layout);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... arg0) {
                try {
                    LinkedList params = new LinkedList<BasicNameValuePair>();
                    URL url = new URL(((MyApplication) getApplication()).getSiteUrl() + "/Office/GetBlog?id=" +
                            Act_BlogDetail.this.getIntent().getExtras().getInt("id"));

                    return CwyWebJSON.postToUrl(url.toString(), params);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return "";
            }
        }.execute();
    }

    private void postToServer(final String s) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                // TODO Reload Page
                finish();
                startActivity(Act_BlogDetail.this.getIntent());
            }

            @Override
            protected String doInBackground(Void... arg0) {
                try {
                    LinkedList params = new LinkedList<BasicNameValuePair>();
                    String currTime = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(new Date(System.currentTimeMillis()));
                    params.add(new BasicNameValuePair("blogContentTail", "" + CwyWebJSON.escape("<p>" + currTime + "@" + s + "</p>")));
                    URL url = new URL(((MyApplication) getApplication()).getSiteUrl() + "/Office/AppendBlog?id=" +
                            Act_BlogDetail.this.getIntent().getExtras().getInt("id"));
                    return CwyWebJSON.postToUrl(url.toString(), params);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return "";
            }
        }.execute();
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
