package com.cleanland.www.fjtmis;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
        setTheme(android.R.style.Theme_Holo_Light_DarkActionBar);
        setContentView(R.layout.custdetail_followlist);

        final Intent thisIntent = getIntent();

        new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                // TODO bind data to the list of this page:
                JSONObject jsobj = null;
                /*
                * {"page":1,"total":1,"rows":[{"ID":30,"CityName":"","ComAddress":"nn","ComName":"nnn","ComSpell":"nnn","CustName":"nn","Phone":"","CreateTime":"2014-10-26 09:48:53","LastFollowTime":"2014-10-26 09:48:53","FlagDeleted":false,"Memo":"","EmpCount":999,"EmpID_DisplayText":"管理员22233","EmpID":2}]}
                * */
                try {
                    jsobj = new JSONObject(result).getJSONArray("rows").getJSONObject(0);
                    TextView title = (TextView) findViewById(R.id.Title);
                    title.setText(
                            "基本信息："+jsobj.getString("CityName") + "." + jsobj.getString("ComName") + "." + jsobj.getString("CustName") +
                                    "\n手机号码：" + jsobj.getString("Phone") +
                                    "\n员工人数：" + jsobj.getString("EmpCount") +
                                    "\r\nBy:" + jsobj.getString("EmpID_DisplayText") + " @" + jsobj.getString("CreateTime"));

                    TextView c = (TextView) findViewById(R.id.Memo);
                    String memo=jsobj.getString("Memo");
                    if(memo.isEmpty())memo="<无>";
                    c.setText("备注："+memo);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... arg0) {
                try {
                    LinkedList params = new LinkedList<BasicNameValuePair>();
                    URL url = new URL(((MyApplication) getApplication()).getSiteUrl() + "/PreCust/GetMyPreCust?id=" +
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
                    ListView lv = (ListView) findViewById(R.id.FollowList);
                    lv.setBackgroundColor(Color.BLACK);//.parseColor("#555555"));
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
                                viewHolder.description.setTextColor(Color.parseColor("#987654"));
                                viewHolder.description.setBackgroundColor(Color.parseColor("#123456"));
                                viewHolder.description.setPadding(20,20,20,20);

                                try {
                                    final JSONObject obj = listjson[0].getJSONObject(position);
                                    String man = "出错了！";
                                    if (obj.has("EmpID_DisplayText")) {
                                        man = obj.getString("EmpID_DisplayText");
                                    }

                                    viewHolder.description.setText(obj.getString("Memo") + //跟进内容
                                            " \r\nBy " + man + "@" + //by whom @ when
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

                    EditText memo = (EditText) findViewById(R.id.newFollow);

                    memo.setHint("点击添加跟进内容");

                    Button b = (Button) findViewById(R.id.OK);
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
                                    //Toast.makeText(Act_CustDetail01.this, "" + result, Toast.LENGTH_LONG).show();
                                    startActivity(thisIntent);
                                    finish();
                                }


                                @Override
                                protected String doInBackground(Void... arg0) {
                                    try {
                                        LinkedList params = new LinkedList<BasicNameValuePair>();
                                        Form f = new Form();
                                        f.PreCustID = "" + Act_CustDetail01.this.getIntent().getExtras().getInt("id");
                                        EditText memo = (EditText) findViewById(R.id.newFollow);
                                        f.Memo = memo.getText().toString();
                                        params.add(new BasicNameValuePair("json", "" + new Gson().toJson(f)));
                                        return CwyWebJSON.postToUrl(((MyApplication) getApplication()).getSiteUrl() + "/PreCust/AddFollow", params);
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
                    URL url = new URL(((MyApplication) getApplication()).getSiteUrl() + "/PreCust/Follow_GetList");
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
        String PreCustID;
    }
}
