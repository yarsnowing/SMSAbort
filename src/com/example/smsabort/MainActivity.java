package com.example.smsabort;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Telephony.Sms;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.EditText;
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
	MyCursorAdapter cursorAdapter;
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
        
      //  ContentValues values = new ContentValues();
     // 寮�缁勮绗竴鏉℃暟鎹�
     //values.put("number", "1223333");
    // values.put("author", "Dan Brown");
    // values.put("pages", 454);
    // values.put("price", 16.96);
     db=mySmsDataHelper.getWritableDatabase();
     //db.insert("MYSMS", null, values); // 鎻掑叆绗竴鏉℃暟鎹�

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
	               // Toast.makeText(MainActivity.this, count == 1 ? "鍒犻櫎鎴愬姛" : "鍒犻櫎澶辫触", Toast.LENGTH_SHORT).show();  
	             //handler.postDelayed(runnable, 10000);
	            }  
	        }  
	  
	        };  
	  
	        getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, mObserver);  
	        cursor=db.query(SMSDataHelper.TABLENAME, null, null,null, null, null, null);
	        if (cursor!=null) {
	        	cursorAdapter=new MyCursorAdapter(MainActivity.this,cursor);
	        	listView.setAdapter(cursorAdapter);
			}
	       
	        initListener();
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
    		ContextMenuInfo menuInfo) {
    	// TODO Auto-generated method stub
    	getMenuInflater().inflate(R.menu.main, menu);
    	//super.onCreateContextMenu(menu, v, menuInfo);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	// TODO Auto-generated method stub
    
    	AdapterContextMenuInfo info=(AdapterContextMenuInfo)item.getMenuInfo();
    	int position =info.position;
    	Log.i("MainActivity", "position is"+position);
    	//CrimeAdapter adapter=(C)getListAdapter();
    //	Crime crime=adapter.getItem(position);
    	switch (item.getItemId()) {
    	case R.id.delete_sms:
    		db.delete(SMSDataHelper.TABLENAME, "_id = ?", new String[]{position+""});
    		db.close();
    		//CrimeLab.get(getActivity()).deleteCrime(crime);
    		//adapter.notifyDataSetChanged();
    		cursor.requery();
    		return true;
		
		}

    	return super.onContextItemSelected(item);
    }
    private void initListener(){
    	addSMSNumberButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showInputDialog();
			}
		});
    	registerForContextMenu(listView);
    }
   
    private void showInputDialog() {
        /*@setView 装入一个EditView
         */
        final EditText editText = new EditText(MainActivity.this);
        AlertDialog.Builder inputDialog = 
            new AlertDialog.Builder(MainActivity.this);
        inputDialog.setTitle("我是一个输入Dialog").setView(editText);
        inputDialog.setPositiveButton("确定", 
            new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	ContentValues values = new ContentValues();
            	// 开始组装第一条数据
            	values.put("number", editText.getText().toString());
            	//values.put("author", "Dan Brown");
            	//values.put("pages", 454);
            	//values.put("price", 16.96);
            	db.insert(SMSDataHelper.TABLENAME, null, values); // 插入第一条数据
            	//cursorAdapter.notifyDataSetChanged();
            cursor.requery();
            }
        }).show();
        
    }
}
