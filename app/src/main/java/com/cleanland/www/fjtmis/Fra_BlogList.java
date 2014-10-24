package com.cleanland.www.fjtmis;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
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

public class Fra_BlogList extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //get layout by inflat...★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
        final View rootview = inflater.inflate(R.layout.custlist, container, false);

        //异步加载数据。
        new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);

                // TODO bind data to the list of this page:
                JSONObject jsobj = null;
                try {
                    final Activity ctx = getActivity();
                    jsobj = new JSONObject(result);
                    final JSONArray listjson = jsobj.getJSONArray("rows");
                    ListView lv = (ListView) rootview.findViewById(R.id.listView);
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

                                convertView.setTag(viewHolder);
                            } else {
                                viewHolder = (ViewHolder) convertView.getTag();
                            }

                            //处理完列表优化之后才可以做以下的事：否则数据错乱！！！！！★★★★★★★★
                            //但是这其实不就效率又很低下了？有啥优化可言呢？
                            try {
                                final JSONObject obj = listjson.getJSONObject(position);
                                viewHolder.description.setText(obj.getString("RegEmpID_DisplayText") + "@" + obj.getString("Title"));
                                viewHolder.description.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        try {
                                            JSONObject pos = listjson.getJSONObject(position);
                                            int id = pos.getInt("ID");
                                            Intent newIntent = new Intent(ctx, Act_BlogDetail.class);
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

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... arg0) {
                try {
                    LinkedList params = new LinkedList<BasicNameValuePair>();
                    URL url = new URL(((MyApplication) getActivity().getApplication()).getSiteUrl() + "/Office/GetBlogList");
                    return CwyWebJSON.postToUrl(url.toString(), params);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return "";
            }
        }.execute();

        return rootview;
    }

    private static class ViewHolder {
        TextView description;
    }
}
