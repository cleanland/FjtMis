package com.cleanland.www.fjtmis;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.LinkedList;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;


public class Act_CustDetail01 extends SwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("客户详细资料");
        final Act_CustDetail01 ctx = Act_CustDetail01.this;
        final LinearLayout layout = new LinearLayout(ctx);
        final Intent thisIntent = getIntent();

        // setContentView(R.layout.activity_act__cust_detail01);
        new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                // TODO bind data to the list of this page:
                JSONObject jsobj = null;
                try {
                    jsobj = new JSONArray(result).getJSONObject(0);
                    layout.setOrientation(LinearLayout.VERTICAL);
                    TextView title = new TextView(ctx);
                    title.setPadding(10, 10, 10, 10);
                    title.setBackgroundColor(Color.GRAY);
                    title.setText(jsobj.getString("Name") + "@" + jsobj.getString("WebSiteID_DisplayText") + "." + jsobj.getString("CreateTime"));
                    layout.addView(title);

                    TextView c = new TextView(ctx);
                    c.setPadding(10, 10, 10, 10);
                    c.setText(jsobj.getString("Memo"));
                    layout.addView(c);

                    ctx.setContentView(layout);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... arg0) {
                try {
                    LinkedList params = new LinkedList<BasicNameValuePair>();
                    URL url = new URL(((MyApplication) getApplication()).getSiteUrl() + "/CustomerManage/GetCompany?id=" +
                            Act_CustDetail01.this.getIntent().getExtras().getInt("id"));

                    return CwyWebJSON.postToUrl(url.toString(), params);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return "";
            }
        }.execute();

        //加载上跟进信息：用LISTVIEW
        new AsyncTask<Void, Void, String>() {

            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                // TODO bind data to the list of this page:
                JSONObject jsobj = null;
                try {
                    final Activity ctx = Act_CustDetail01.this;
                    jsobj = new JSONObject(result);
                    final JSONArray[] listjson = {jsobj.getJSONArray("rows")};//★★★★★★★★
                    final ListView lv = new ListView(ctx);
                    lv.setDividerHeight(2);

                    final LayoutInflater inflater = LayoutInflater.from(ctx);
                    lv.setAdapter(new ListAdapter() {
                        @Override
                        public boolean areAllItemsEnabled() {
                            return false;
                        }

                        @Override
                        public boolean isEnabled(int position) {
                            return true;
                        }

                        @Override
                        public void registerDataSetObserver(DataSetObserver observer) {

                        }

                        @Override
                        public void unregisterDataSetObserver(DataSetObserver observer) {

                        }

                        @Override
                        public int getCount() {
                            return listjson[0].length();
                        }

                        @Override
                        public Object getItem(int position) {
                            try {
                                return listjson[0].getJSONObject(position);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        public long getItemId(int position) {
                            return position;
                        }

                        @Override
                        public boolean hasStableIds() {
                            return true;
                        }

                        @Override
                        public View getView(final int position, View convertView, ViewGroup parent) {
                            ViewHolder viewHolder = null;
                            if (null == convertView) {
                                viewHolder = new ViewHolder();
                                convertView = inflater.inflate(R.layout.listitem_min, parent, false);
                                viewHolder.description = (TextView) convertView.findViewById(R.id.textView);
                                try {
                                    final JSONObject obj = listjson[0].getJSONObject(position);
                                    String man = "出错了！";
                                    if (obj.has("EmpID_DisplayText")) {

                                        man = obj.getString("EmpID_DisplayText");
                                    }

                                    viewHolder.description.setBackgroundColor(Color.YELLOW);
                                    viewHolder.description.setTextColor(Color.RED);
                                    viewHolder.description.setText(obj.getString("Memo") + //跟进内容
                                            " By " + man + "@" + //by whom @ when
                                            obj.getString("CreateTime") + ".");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                convertView.setTag(viewHolder);
                            } else {
                                viewHolder = (ViewHolder) convertView.getTag();
                            }
                            // set item values to the viewHolder:
                            return convertView;
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


                    final EditText memo = new EditText(ctx);
                    //android:background="@drawable/backwithborder"
                    memo.setBackground(null);

                    memo.setHint("点击添加跟进内容");
                    memo.setMinLines(3);


                    final Button b = new Button(ctx);
                    b.setText("OK！");
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new AsyncTask<Void, Void, String>() {
                                @Override
                                protected void onPostExecute(String result) {
                                    // TODO Auto-generated method stub
                                    super.onPostExecute(result);

                                    //RELOAD PAGE:
                                    Toast.makeText(Act_CustDetail01.this, "" + result, Toast.LENGTH_LONG).show();
                                    startActivity(thisIntent);
                                    finish();
                                }


                                @Override
                                protected String doInBackground(Void... arg0) {
                                    try {
                                        LinkedList params = new LinkedList<BasicNameValuePair>();
                                        //Memo:"747474",ID:"",CompanyID:"6"}
                                        //http://192.168.1.58:8007/CustomerManage/AddFollow
                                        Form f = new Form();
                                        f.CompanyID = "" + Act_CustDetail01.this.getIntent().getExtras().getInt("id");
                                        f.Memo = memo.getText().toString();
                                        params.add(new BasicNameValuePair("json", "" + new Gson().toJson(f)));
                                        return CwyWebJSON.postToUrl(((MyApplication) getApplication()).getSiteUrl() + "/CustomerManage/AddFollow", params);
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


                    layout.addView(memo);
                    layout.addView(b);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... arg0) {
                try {
                    LinkedList params = new LinkedList<BasicNameValuePair>();
                    params.add(new BasicNameValuePair("page", "1"));
                    params.add(new BasicNameValuePair("rp", "20"));
                    params.add(new BasicNameValuePair("sortname", "ID"));
                    params.add(new BasicNameValuePair("sortorder", "desc"));
                    params.add(new BasicNameValuePair("query", "{pid:\"" + Act_CustDetail01.this.getIntent().getExtras().getInt("id") + "\"}"));
                    params.add(new BasicNameValuePair("qtype", "sql"));
                    params.add(new BasicNameValuePair("iegohell", "1413782533798"));
                    URL url = new URL(((MyApplication) getApplication()).getSiteUrl() + "/CustomerManage/CompanyFollow_GetList");
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
        getMenuInflater().inflate(R.menu.menu_act__cust_detail01, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static class ViewHolder {
        TextView description;
    }

    private static class Form {
        String Memo;
        String CompanyID;
        String ID = "";
    }
}
