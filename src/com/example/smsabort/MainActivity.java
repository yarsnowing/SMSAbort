package com.example.smsabort;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.Window;
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
	static SQLiteDatabase db;
	MyCursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        mySmsDataHelper=new SMSDataHelper(MainActivity.this, "MYSMS", null, 1);
   
     db=mySmsDataHelper.getWritableDatabase();


        addSMSNumberButton=(Button)findViewById(R.id.title);
        listView=(ListView)findViewById(R.id.listview);
        

 
	        cursor=db.query(SMSDataHelper.TABLENAME, null, null,null, null, null, null);
	        if (cursor!=null) {
	        	cursorAdapter=new MyCursorAdapter(MainActivity.this,cursor);
	        	listView.setAdapter(cursorAdapter);
			}
	       
	        initListener();
	        Intent intent =new Intent(MainActivity.this,MyService.class);
	        startService(intent);
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
    	
    	switch (item.getItemId()) {
    	case R.id.delete_sms:
    		cursor.moveToPosition(position);
    		String num=cursor.getString(cursor.getColumnIndex("number"));
    		db.delete(SMSDataHelper.TABLENAME, "number = ?", new String[]{num});
    		cursor.requery();
    		querySMS();
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
        inputDialog.setTitle("输入号码(区号也要输入上)").setView(editText);
        inputDialog.setPositiveButton("确定", 
            new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	ContentValues values = new ContentValues();
            	// 开始组装第一条数据
            	values.put("number", editText.getText().toString());
            	
            	db.insert(SMSDataHelper.TABLENAME, null, values); // 插入第一条数据
            	
            cursor.requery();
            querySMS();
            }
        }).show();
        
    }
	public static void querySMS(){
		Cursor cursor=db.query(SMSDataHelper.TABLENAME, null, null,null, null, null, null);
		MyService.numberStrings.clear();
		if (cursor.moveToFirst()) {
			
			do {
			// 遍历Cursor对象，取出数据并打印
			String smsnum = cursor.getString(cursor.
			getColumnIndex("number"));
			MyService.numberStrings.add(smsnum);
			} while (cursor.moveToNext());
			}
			cursor.close();
			Log.i("size----", MyService.numberStrings.size()+"  sizr");
			
		}
}
