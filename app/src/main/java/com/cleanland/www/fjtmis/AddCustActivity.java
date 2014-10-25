package com.cleanland.www.fjtmis;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;

import java.util.LinkedList;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;


public class AddCustActivity extends SwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cust);
        setTitle("添加新客户");

        ((Button) findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddCustActivity.this, "", Toast.LENGTH_SHORT).show();

                LinkedList<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();

                //JS:select * from tableName,get ColumnNames Array.then run in chrome console window:★★★★★★
                //var s='';var xx=' ID, CityName, EmpCount, ComAddress, ComName, ComSpell, CustName, Phone, CreateTime, LastFollowTime, FlagDeleted, Memo, EmpID'
                //$.each(xx.split(','),function(i,v){v=$tirm(v);s+=('\nparams.add(new BasicNameValuePair("'+v+'", "" + ((EditText)findViewById(R.id.'+v+')).getText()));');})
                //s
                params.add(new BasicNameValuePair("EmpCount", "" + ((EditText) findViewById(R.id.EmpCount)).getText()));
                params.add(new BasicNameValuePair("ComAddress", "" + ((EditText) findViewById(R.id.ComAddress)).getText()));
                params.add(new BasicNameValuePair("ComName", "" + ((EditText) findViewById(R.id.ComName)).getText()));
                params.add(new BasicNameValuePair("CustName", "" + ((EditText) findViewById(R.id.CustName)).getText()));
                params.add(new BasicNameValuePair("Phone", "" + ((EditText) findViewById(R.id.Phone)).getText()));
                params.add(new BasicNameValuePair("Memo", "" + ((EditText) findViewById(R.id.Memo)).getText()));

                new MyHttpJob(((MyApplication)getApplication()).getSiteUrl()+ "/PreCust/EditPreCusts?id=0", params) {
                    @Override
                    protected void OnDone(String ResponsedStr) {
                        if(true)Log.i("", ""+ResponsedStr);
                    }
                };
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_cust, menu);
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
}
