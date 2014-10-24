package com.cleanland.www.fjtmis;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;


/**
 * 利用VIEWPAGER，组织FJTMIS的多个页面。导航条。底部。
 * 利用VIEWPAGER，组织FJTMIS的多个页面。导航条。底部。
 * 利用VIEWPAGER，组织FJTMIS的多个页面。导航条。底部。
 * 利用VIEWPAGER，组织FJTMIS的多个页面。导航条。底部。
 * 利用VIEWPAGER，组织FJTMIS的多个页面。导航条。底部。
 * 利用VIEWPAGER，组织FJTMIS的多个页面。导航条。底部。
 */
public class MainActivity extends SwipeBackActivity {
    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("房介通内部管理系统");

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        //【碎片页控制器】=【碎片管理者】
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        //【layout.ViewPager】=【碎片页控制器】=【碎片管理者】
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        final int[] id = {item.getItemId()};
        if (id[0] == R.id.action_settings) {
            return true;
        }
        if (id[0] == R.id.action_addblog) {
            new AsyncTask<Void, Void, String>() {
                @Override
                protected void onPostExecute(String result) {
                    // TODO Auto-generated method stub
                    super.onPostExecute(result);

                    int tid = 0;
                    try {
                        tid = Integer.parseInt(result);
                    } catch (Exception e) {
                        //Toast.makeText(Login.this,"晕，是不是网址不对啊？",Toast.LENGTH_LONG).show();
                        //上句好像加上也不起作用，不知道为啥。。。
                        e.printStackTrace();
                    }
                    if (tid == 0) return;
                    Log.i("tid--------------------------------", ""+tid);

                    Intent newIntent = new Intent(MainActivity.this, Act_BlogDetail.class);
                    newIntent.putExtra("id", tid);
                    startActivity(newIntent);
                }

                @Override
                protected String doInBackground(Void... arg0) {
                    try {
                        LinkedList params = new LinkedList<BasicNameValuePair>();
                        return CwyWebJSON.postToUrl(((MyApplication) getApplication()).getSiteUrl() + "/office/GetMyBlogIdOfToday", params);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return "";
                }
            }.execute();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_my, container, false);
            TextView tv = (TextView) rootView.findViewById(R.id.section_label);
            tv.setText(Math.random() + "");
            rootView.setBackgroundColor(Color.RED);
            return rootView;
        }
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(final int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            // 创建并返回FRA。。。。
            switch (position) {
                case 0: {
                    return new Fra_BlogList();
                }
                case 1: {
                    return new Fra_CustList();
                }
                case 2: {
                    return new fra();
                }
            }
            return new fra();
            //return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

}
