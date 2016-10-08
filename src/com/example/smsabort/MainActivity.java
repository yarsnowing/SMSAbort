package com.example.smsabort;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class MainActivity extends Activity {
	Button APPOS_BT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        APPOS_BT=(Button)findViewById(R.id.AppOS_BT);
        APPOS_BT.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				          intent.setClassName("com.android.settings",
				                  "com.android.settings.Settings");
				          intent.setAction(Intent.ACTION_MAIN);
				          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				                  | Intent.FLAG_ACTIVITY_CLEAR_TASK
				                  | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
				          intent.putExtra(PreferenceActivity.EXTRA_SHOW_FRAGMENT,
				                  "com.android.settings.applications.AppOpsSummary");
				         startActivity(intent);	
				         finish();
			}
		});
       // <span style="white-space:pre">  </span>
        /*Intent intent = new Intent(Intent.ACTION_MAIN);  
        ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.Settings");  
        intent.setComponent(cn);  
        intent.putExtra(":android:show_fragment", "com.android.settings.applications.AppOpsSummary");  
        startActivity(intent);  */
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
