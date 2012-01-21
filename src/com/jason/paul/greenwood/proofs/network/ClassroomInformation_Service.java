package com.jason.paul.greenwood.proofs.network;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class ClassroomInformation_Service  extends Service{
	
	private ScreenSend_Thread ssThread =  new ScreenSend_Thread();
	private LockScreen_Thread lsThread =  new LockScreen_Thread(null);
	
	public static String BROADCAST_ACTION = "com.jason.paul.greenwood.proof.network/UNLOCK";
	
	/**
	 * Provide clients access.
	 * No IPC as service is in the same process as clients
	 */
	public class ClassInformation_Binder extends Binder {
		ClassroomInformation_Service getService() {
			return ClassroomInformation_Service.this;
		}
	}
	
	//Object that receives interactions from clients
	private final IBinder mBinder = new ClassInformation_Binder();

	@Override
	public void onCreate(){
		Log.v("SERVICE", "onCreate called");
		
		getApplicationContext();
		WifiManager wm = (WifiManager)getSystemService(Context.WIFI_SERVICE);
		MulticastLock wmmc = wm.createMulticastLock("myLock");
		wmmc.acquire();
		
		//Create thread here
		ScreenSend_Thread ssThreadNew =  new ScreenSend_Thread();
		ssThread = ssThreadNew;
		//Open socket connection
		ssThread.endScreenSend(false);
		
		Thread sendScreenThread = new Thread(ssThread);
		sendScreenThread.setName("Tablet_Listener");
		sendScreenThread.setDaemon(true);
		sendScreenThread.start();
		Log.v("SERVICE", "sendScreenThread Initiated");
		
		LockScreen_Thread lsThreadNew =  new LockScreen_Thread(this);
		lsThread = lsThreadNew;
		//Open socket connection
		lsThread.endScreenSend(false);
		
		Thread LockScreen_Thread = new Thread(lsThread);
		LockScreen_Thread.setName("Interrupt_Listener");
		LockScreen_Thread.setDaemon(true);
		LockScreen_Thread.start();
		Log.v("SERVICE", "lockScreenThread Initiated");
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		Log.i("Class Service", "Recieved start id " + startId +": " + intent);
		//Return sticky so that only ends when explicitly stopped
		return START_STICKY;
	}
	
	public void removeLockIntent(){
		Intent broadcast = new Intent();
		broadcast.setAction(BROADCAST_ACTION);
		sendBroadcast(broadcast);
		//Object event;
		//return super.onKeyDown(KeyEvent.KEYCODE_BACK, event);
	}
	
	public void createLockIntent(){
		Intent intent = new Intent().setClass(this, LockScreen.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		this.startActivity(intent);
	}
}
