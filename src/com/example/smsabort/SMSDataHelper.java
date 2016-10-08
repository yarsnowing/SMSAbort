package com.example.smsabort;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class SMSDataHelper extends SQLiteOpenHelper {
	public static String TABLENAME="MySMS";
public static final String CREATE_BOOK = "create table MySMS ("
+ "_id integer primary key autoincrement, "
+ "number text)";
private Context mContext;
public SMSDataHelper(Context context, String name, CursorFactory
factory, int version) {
super(context, name, factory, version);
mContext = context;
}
@Override
public void onCreate(SQLiteDatabase db) {
db.execSQL(CREATE_BOOK);
Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();
}
@Override
public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
}
}

