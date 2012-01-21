package com.jason.paul.greenwood.proofs.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ClassroomBootService extends BroadcastReceiver{

	//Starts Service upon device startup
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Intent serviceIntent = new Intent(context, ClassroomInformation_Service.class);
		//serviceIntent.setAction("com.jason.paul.greenwood.ClassInformation_Service");
		context.startService(serviceIntent);
	}
}
