package com.cleanland.www.fjtmis;

import android.app.Fragment;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Administrator on 2014/10/20.
 * 这是一个没什么用的空页面。位于主屏中第三分屏。占位先。
 */
public class fra extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (false) {
            TextView tvx = new TextView(getActivity());
            tvx.setText("adsfadsf");
            if (true) return tvx;
            //以上已能正确执行。。。
            //每个NEW出来的本类对象都只显示一个TEXTVIEW即可。
        }

        View rootview = inflater.inflate(R.layout.bloglist, container, false);
        if(true)
        {
            ListView listview = (ListView) rootview.findViewById(R.id.listView);
            listview.setAdapter(new ListAdapter() {
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
                    return 10;
                }

                @Override
                public Object getItem(int position) {
                    return 123;
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
                public View getView(int position, View convertView, ViewGroup parent) {

                    TextView tv = new TextView(getActivity());
                    tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
                    tv.setGravity(Gravity.CENTER);
                    tv.setTextSize(30);
                    tv.setTextColor(Color.BLACK);
                    tv.setText("Page " + 0);
                    return tv;
                }

                @Override
                public int getItemViewType(int position) {
                    return 1;
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
            return rootview; //pass good code.
            // pass good code
        }
        return rootview;
    }

    public void update() {

    }
}
