package com.cleanland.www.fjtmis.employee;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cleanland.www.fjtmis.CwyWebJSON;
import com.cleanland.www.fjtmis.MyApplication;
import com.cleanland.www.fjtmis.MyHttpJob;
import com.cleanland.www.fjtmis.R;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

/**
 * Created by Administrator on 2014/10/20.
 * 员工列表。位于主屏中第三分屏。
 */
public class fra extends Fragment {
    private BaseAdapter emplistData;
    Activity act;
    private String url;
    private JSONArray listjson;
    int pageno;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        act = getActivity();
        pageno=1;
        final View rootview = inflater.inflate(R.layout.emplist, container, false);
        url = ((MyApplication) getActivity().getApplication()).getSiteUrl() + "/Emp/GetEmpList";
        LinkedList<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("page", "" + (pageno++)));
        params.add(new BasicNameValuePair("rp", "20"));
        params.add(new BasicNameValuePair("sortname", "ID"));
        params.add(new BasicNameValuePair("sortorder", "desc"));
        params.add(new BasicNameValuePair("query", "{Status:1}"));
        params.add(new BasicNameValuePair("qtype", "sql"));
        params.add(new BasicNameValuePair("iegohell", "1413782533798"));
        new MyHttpJob(url, params) {
            @Override
            protected void OnDone(String ResponsedStr) {
                try {
                    JSONObject jsobj = new JSONObject(ResponsedStr);
                    listjson = jsobj.getJSONArray("rows");
                    emplistData = new BaseAdapter() {
                        @Override
                        public int getCount() {
                            return listjson.length();
                        }

                        @Override
                        public Object getItem(int position) {
                            try {
                                return listjson.get(position);
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
                        public View getView(final int position, View convertView, ViewGroup parent) {
                            ViewHolder viewHolder = null;
                            if (null == convertView) {
                                viewHolder = new ViewHolder();
                                convertView = inflater.inflate(R.layout.listitem_min, parent, false);
                                viewHolder.description = (TextView) convertView.findViewById(R.id.textView);
                                convertView.setTag(viewHolder);
                            } else {
                                viewHolder = (ViewHolder) convertView.getTag();
                            }

                            //处理完列表优化之后才可以做以下的事：否则数据错乱！！！！！★★★★★★★★
                            try {
                                final JSONObject obj = listjson.getJSONObject(position);
                                String man = "无角色";
                                if (obj.has("AccountType_DisplayText")) {

                                    man = obj.getString("AccountType_DisplayText");
                                }
                                viewHolder.description.setBackgroundColor(Color.parseColor("#FFE7FFC2"));
                                viewHolder.description.setText(obj.getString("Name") + //公司
                                        "." + obj.getString("MobilePhone") + "." + man + "." + obj.getString("DeptID_DisplayText"));

                                viewHolder.description.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        try {
                                            JSONObject pos = listjson.getJSONObject(position);
                                            int id = pos.getInt("ID");
                                            Intent newIntent = new Intent(act, com.cleanland.www.fjtmis.employee.Act_EmpDetail.class);
                                            newIntent.putExtra("id", id);
                                            startActivity(newIntent);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            // set item values to the viewHolder:
                            return convertView;
                        }
                    };
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final ListView lv = (ListView) rootview.findViewById(R.id.listView2);
                lv.setAdapter(emplistData);
                lv.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {
                        // TODO Auto-generated method stub
                        int threshold = 1;
                        int count = lv.getCount();
                        if (scrollState == SCROLL_STATE_IDLE) {
                            if (lv.getLastVisiblePosition() >= count - threshold) {
                                // Execute LoadMoreDataTask AsyncTask
                                new AsyncTask<Void, Void, String>() {
                                    @Override
                                    protected String doInBackground(Void... arg0) {
                                        try {
                                            LinkedList params = new LinkedList<BasicNameValuePair>();
                                            params.add(new BasicNameValuePair("page", "" + (pageno++)));
                                            params.add(new BasicNameValuePair("rp", "20"));
                                            params.add(new BasicNameValuePair("sortname", "ID"));
                                            params.add(new BasicNameValuePair("sortorder", "desc"));
                                            params.add(new BasicNameValuePair("query", "{Status:1}"));
                                            params.add(new BasicNameValuePair("qtype", "sql"));
                                            params.add(new BasicNameValuePair("iegohell", "1413782533798"));
                                            return CwyWebJSON.postToUrl(url.toString(), params);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        return "";
                                    }

                                    @Override
                                    protected void onPostExecute(String s) {
                                        super.onPostExecute(s);
                                        try {
                                            //由于不想搞新MODEL，所以反复追加字符串吧。。。
                                            String olds = listjson.toString();

                                            JSONObject newdata = new JSONObject(s);
                                            String news = newdata.getJSONArray("rows").toString();
                                            if (news.equals("[]")) {
                                                return;
                                            }

                                            String newResult = olds.substring(0, olds.length() - 1);
                                            if (!news.isEmpty())
                                                newResult += "," + news.substring(1);
                                            listjson = new JSONArray(newResult);//★★★★★★★★

                                            //回归正路：
                                            ((BaseAdapter) lv.getAdapter()).notifyDataSetChanged();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }.execute();
                            }
                        }
                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem,
                                         int visibleItemCount, int totalItemCount) {
                        // TODO Auto-generated method stub

                    }

                });
            }
        };

        return rootview;
    }

    public void update() {

    }

    private static class ViewHolder {
        TextView description;
    }
}
