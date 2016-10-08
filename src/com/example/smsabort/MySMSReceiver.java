package com.example.smsabort;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;
import android.widget.Toast;

public class MySMSReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Intent intent2=new Intent(context,MyService.class);
		context.startService(intent2);
	//	Toast.makeText(context, "收到短信", Toast.LENGTH_SHORT).show();
		AudioManager mAudioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.setRingerMode(0);
       // Log.i("Myservice","执行陈功");
		 abortBroadcast();
		
	}

}
