package com.cleanland.www.fjtmis.employee;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.cleanland.www.fjtmis.MyApplication;
import com.cleanland.www.fjtmis.MyHttpJob;
import com.cleanland.www.fjtmis.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;


public class Act_EmpDetail extends SwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("员工信息");
        final Act_EmpDetail ctx = Act_EmpDetail.this;
        setContentView(R.layout.empdetail);


        String url = MyApplication.getSiteUrl() +
                "/Emp/GetEmp?tablename=hr_Employee&id=" +
                Act_EmpDetail.this.getIntent().getExtras().getInt("id");

        new MyHttpJob(url, null) {
            @Override
            protected void OnDone(String ResponsedStr) {
                try {

                    JSONObject jsonObject = new JSONArray(ResponsedStr).getJSONObject(0);
                    ((TextView) findViewById(R.id.EmpName)).setText(""+jsonObject.getString("Name"));
                    ((TextView) findViewById(R.id.DeptName)).setText(""+jsonObject.getString("DeptID_DisplayText"));
                    ((TextView) findViewById(R.id.QQ)).setText(""+jsonObject.getString("QQ"));
                    ((TextView) findViewById(R.id.Email)).setText(""+jsonObject.getString("Email"));
                    ((TextView) findViewById(R.id.Phone)).setText(""+jsonObject.getString("MobilePhone"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
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

}
