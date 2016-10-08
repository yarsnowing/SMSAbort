package com.example.smsabort;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyCursorAdapter extends CursorAdapter {
	Cursor cursor=null;
	public MyCursorAdapter(Context context, Cursor c) {
		super(context, c);
		cursor=c;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void bindView(View arg0, Context arg1, Cursor arg2) {
		// TODO Auto-generated method stub
		TextView smsnumberTextView=(TextView)arg0.findViewById(R.id.sms_number);
		String smsString=cursor.getString(cursor.getColumnIndex("number"));
		if(smsString!=null)
			smsnumberTextView.setText(smsString);
		
	}

	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View view=View.inflate(arg0, R.layout.item_sms, null);
		return view;
	}

}
