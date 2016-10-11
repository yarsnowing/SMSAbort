package com.example.smsabort;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AutoStartService extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Intent intent2=new Intent(context,MyService.class);
		context.startService(intent2);
	}
}
