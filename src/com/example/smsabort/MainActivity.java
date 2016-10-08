package com.example.smsabort;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceActivity;
import android.provider.Telephony.Sms;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends Activity {
	//Button APPOS_BT,AbortSMS;
	Button addSMSNumberButton;
	ListView listView;
	Cursor cursor;
	SMSDataHelper mySmsDataHelper;
	ContentObserver mObserver;
	SQLiteDatabase db;
	Handler handler=new Handler();
	Runnable runnable =new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			  AudioManager mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
              mAudioManager.setRingerMode(2);
              Toast.makeText(MainActivity.this, ";;;", Toast.LENGTH_SHORT).show();
		}
	};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mySmsDataHelper=new SMSDataHelper(MainActivity.this, "MYSMS", null, 1);
        
        ContentValues values = new ContentValues();
     // 开始组装第一条数据
     values.put("number", "1223333");
    // values.put("author", "Dan Brown");
    // values.put("pages", 454);
    // values.put("price", 16.96);
     db=mySmsDataHelper.getWritableDatabase();
     db.insert("MYSMS", null, values); // 插入第一条数据

        addSMSNumberButton=(Button)findViewById(R.id.title);
        listView=(ListView)findViewById(R.id.listview);
        
	//	 mAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, 0, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
		 //mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, 0, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
       mObserver = new ContentObserver(new Handler()) {  
			  
	        @SuppressLint("NewApi")
			@Override  
	        public void onChange(boolean selfChange) {  
	            super.onChange(selfChange);  
	            ContentResolver resolver = getContentResolver();  
	            Cursor cursor = resolver.query(Uri.parse("content://sms/inbox"), new String[] { "_id", "address", "body" }, null, null, "_id desc");  
	            long id = -1;  
	  
	            if (cursor.getCount() > 0 && cursor.moveToFirst()) {  
	                id = cursor.getLong(0);  
	                String address = cursor.getString(1);  
	                String body = cursor.getString(2);  
	                Toast.makeText(MainActivity.this, address, Toast.LENGTH_SHORT).show();
	                if(address.equals("+8618683997406"))
	                	handler.postDelayed(runnable, 10000);
	                else {
						handler.post(runnable);
					}
	                Toast.makeText(MainActivity.this, String.format("address: %s\n body: %s", address, body), Toast.LENGTH_SHORT).show();  
	            }  
	            cursor.close();  
	  
	            if (id != -1) {  
	                int count = resolver.delete(Sms.CONTENT_URI, "_id=" + id, null);  
	               // Toast.makeText(MainActivity.this, count == 1 ? "删除成功" : "删除失败", Toast.LENGTH_SHORT).show();  
	             //handler.postDelayed(runnable, 10000);
	            }  
	        }  
	  
	        };  
	  
	        getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, mObserver);  
	        cursor=db.query("MYSMS", null, null,null, null, null, null);
	        if (cursor!=null) {
	        	 listView.setAdapter(new MyCursorAdapter(MainActivity.this, cursor) );
			}
	       
       /* APPOS_BT=(Button)findViewById(R.id.AppOS_BT);
        AbortSMS=(Button)findViewById(R.id.Start_AbortSMS);
        AbortSMS.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				

			}
		});
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
		});*/
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
