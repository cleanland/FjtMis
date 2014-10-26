package com.cleanland.www.fjtmis;

import android.app.Activity;
import android.app.AlertDialog;
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

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.LinkedList;

/**
 * Created by Administrator on 2014/10/20.
 * 显示客户列表
 */
public class Fra_CustList extends Fragment {
    LayoutInflater inflater;
    ListView lv;
    JSONArray listjson;
    Activity act;
    private BaseAdapter listAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //get layout by inflat...★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
        this.inflater = inflater;
        act= getActivity();
        final View rootview = inflater.inflate(R.layout.custlist, container, false);
        //异步加载数据。
        new AsyncTask<Void, Void, String>() {
            public int pageno = 1;

            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);

                // TODO bind data to the list of this page:
                JSONObject jsobj = null;
                try {
                    jsobj = new JSONObject(result);
                    listjson = jsobj.getJSONArray("rows");//★★★★★★★★
                    lv = (ListView) rootview.findViewById(R.id.listView);
                    lv.setDividerHeight(2);

                    final LayoutInflater inflater = LayoutInflater.from(act);
                    listAdapter = new BaseAdapter() {
                        @Override
                        public int getCount() {
                            return listjson.length();
                        }

                        @Override
                        public Object getItem(int position) {
                            try {
                                return listjson.getJSONObject(position);
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
                                String man = "没人管";
                                if (obj.has("EmpID_DisplayText")) {

                                    man = obj.getString("EmpID_DisplayText");
                                }
                                viewHolder.description.setBackgroundColor(Color.parseColor("#FFE7FFC2"));
                                viewHolder.description.setText(obj.getString("ComName") + //公司
                                        " By " + man + "@" + //by whom @ which siteUrl
                                        obj.getString("ComAddress") + ".");

                                viewHolder.description.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        try {
                                            JSONObject pos = listjson.getJSONObject(position);
                                            int id = pos.getInt("ID");
                                            Intent newIntent = new Intent(act, Act_CustDetail01.class);
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
                    lv.setAdapter(listAdapter);

                    //实现下拉加载更多的效果。
                    resetOnScrollListener(lv);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            private void resetOnScrollListener(final ListView lv) {
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
                                            params.add(new BasicNameValuePair("query", "{Date1:\"\",sCon1:\"\"}"));
                                            params.add(new BasicNameValuePair("qtype", "sql"));
                                            params.add(new BasicNameValuePair("iegohell", "1413782533798"));
                                            URL url = new URL(((MyApplication) getActivity().getApplication()).getSiteUrl() + "/precust/GetMyPreCust?id=0");
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

            @Override
            protected String doInBackground(Void... arg0) {
                try {
                    LinkedList params = new LinkedList<BasicNameValuePair>();
                    params.add(new BasicNameValuePair("page", "" + (pageno++)));
                    params.add(new BasicNameValuePair("rp", "20"));
                    params.add(new BasicNameValuePair("sortname", "ID"));
                    params.add(new BasicNameValuePair("sortorder", "desc"));
                    params.add(new BasicNameValuePair("query", "{Date1:\"\",sCon1:\"\"}"));
                    params.add(new BasicNameValuePair("qtype", "sql"));
                    params.add(new BasicNameValuePair("iegohell", "1413782533798"));
                    URL url = new URL(((MyApplication) getActivity().getApplication()).getSiteUrl() + "/precust/GetMyPreCust?id=0");
                    return CwyWebJSON.postToUrl(url.toString(), params);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return "";
            }
        }.execute();
        return rootview;
    }

    private void Alert(String info) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("提示");
        dialog.setMessage(info);
        dialog.setPositiveButton("确定", null);
        dialog.show();
    }

    public void update() {
        //重新绑定数据。。。。
        new AsyncTask<Void, Void, String>() {
            public int pageno = 1;

            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);

                // TODO bind data to the list of this page:
                JSONObject jsobj = null;
                try {
                    jsobj = new JSONObject(result);
                    listjson = jsobj.getJSONArray("rows");//★★★★★★★★

                    //回归正路：
                    ((BaseAdapter) lv.getAdapter()).notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... arg0) {
                try {
                    LinkedList params = new LinkedList<BasicNameValuePair>();
                    params.add(new BasicNameValuePair("page", "" + (pageno++)));
                    params.add(new BasicNameValuePair("rp", "20"));
                    params.add(new BasicNameValuePair("sortname", "ID"));
                    params.add(new BasicNameValuePair("sortorder", "desc"));
                    params.add(new BasicNameValuePair("query", "{Date1:\"\",sCon1:\"\"}"));
                    params.add(new BasicNameValuePair("qtype", "sql"));
                    params.add(new BasicNameValuePair("iegohell", "1413782533798"));
                    URL url = new URL(((MyApplication) getActivity().getApplication()).getSiteUrl() + "/precust/GetMyPreCust?id=0");
                    return CwyWebJSON.postToUrl(url.toString(), params);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return "";
            }
        }.execute();
    }

    private static class ViewHolder {
        TextView description;
    }
}
