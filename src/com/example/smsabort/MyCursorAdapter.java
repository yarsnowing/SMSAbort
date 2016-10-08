package com.example.smsabort;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyCursorAdapter extends CursorAdapter {
	//Cursor cursor=null;
	int viewResId;
	
	 public MyCursorAdapter(Context context, Cursor cursor) {  
	        super(context,cursor);  
	       // viewResId=resource;  
	    }  
	@Override
	public void bindView(View arg0, Context arg1, Cursor arg2) {
		// TODO Auto-generated method stub
		TextView smsnumberTextView=(TextView)arg0.findViewById(R.id.sms_number);
		String smsString=arg2.getString(arg2.getColumnIndex("number"));
		if(smsString!=null)
			smsnumberTextView.setText(smsString);
		
	}

	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		//TextView view =null;  
	       //LayoutInflater vi = null;  
	       //vi = (LayoutInflater)arg0.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
	       //view =(TextView)vi.inflate(viewResId, arg2, false);  
	           //v =(TextView)vi.inflate(textViewResourceId,null);  
	      // Log.i("hubin","newView"+view); 
		View view =View.inflate(arg0, R.layout.item_sms, null);
	        return view;  
		

}
}