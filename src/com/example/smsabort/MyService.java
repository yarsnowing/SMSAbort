package com.example.smsabort;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Telephony.Sms;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service{
	ContentObserver mObserver;
	Handler handler=new Handler();
	public static List<String> numberStrings=new ArrayList<String>();
	public static int DE_RING_STATUS =1;
	boolean shoulSetRingSilence=false;
	SQLiteDatabase db;
	Runnable runnable =new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			  AudioManager mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
              mAudioManager.setRingerMode(DE_RING_STATUS);
              shoulSetRingSilence=false;
            
		}
	};
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		SMSDataHelper dbhelper = null;
		if(db==null)
			{
			dbhelper=new SMSDataHelper(MyService.this, "MYSMS", null,1);
			db=dbhelper.getWritableDatabase();
			querySMS();
			}
		
	}
	private  void querySMS(){
		Cursor cursor=db.query(SMSDataHelper.TABLENAME, null, null,null, null, null, null);
		MyService.numberStrings.clear();
		if (cursor.moveToFirst()) {
			
			do {
			// 遍历Cursor对象，取出数据
			String smsnum = cursor.getString(cursor.
			getColumnIndex("number"));
			MyService.numberStrings.add(smsnum);
			} while (cursor.moveToNext());
			}
			cursor.close();
			
			
		}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		SMSDataHelper dbhelper = null;
		if(db==null)
		{
		dbhelper=new SMSDataHelper(MyService.this, "MYSMS", null,1);
		db=dbhelper.getWritableDatabase();
		querySMS();
		}
		
		// TODO Auto-generated method stub
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
		                
		             
		                for (int i = 0; i <numberStrings.size(); i++) {
							if(address.equals(numberStrings.get(i)))
								shoulSetRingSilence=true;
						}
		              
		              
		            }  
		            if(shoulSetRingSilence)
		            	handler.postDelayed(runnable, 10000);
		            else
		            handler.post(runnable);
		            cursor.close();  
		  
		            if (id != -1) {  
		                int count = resolver.delete(Sms.CONTENT_URI, "_id=" + id, null);  
		               
		            }  
		        }  
		  
		        };  
		  
		        getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, mObserver); 
		       // Log.i("MyService", "dddddddd");
		        flags =  START_STICKY;
		return super.onStartCommand(intent, flags, startId);
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Intent intent=new Intent(this,MyService.class);
		startService(intent);
		getContentResolver().unregisterContentObserver(mObserver);
		super.onDestroy();
	}


}
