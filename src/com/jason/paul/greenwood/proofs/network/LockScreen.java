package com.jason.paul.greenwood.proofs.network;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class LockScreen extends Activity {
	
	private BroadcastReceiver receiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent) {
			//Unlock screen
			Log.v("LockScreen", "Broadcasted message is received");
			Log.v("LockScreen", intent.getAction() );
			
			if( intent.getAction().equals("com.jason.paul.greenwood.proof.network/UNLOCK") ){
				finish();
			}
		}	
	};
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    //Stop task/status/notification bar from appearing
//	    requestWindowFeature(Window.FEATURE_NO_TITLE);
//	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
//	    						WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    
	    setContentView(R.layout.lock_screen_layout);
	    
	    IntentFilter filter = new IntentFilter();
	    filter.addAction(ClassroomInformation_Service.BROADCAST_ACTION);
	    registerReceiver(receiver, filter);
	}
	
	@Override
	protected void onPause(){
		unregisterReceiver(receiver);
		super.onPause();
	}
	
	@Override
    protected void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    }

//   	/**To go back a screen with the back button rather than selecting an mp3 file*/
//	@Override
//	protected void onPause() {
//		super.onPause();
//		Intent mIntent = new Intent();
//		setResult(RESULT_CANCELED, mIntent);
//	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		Toast.makeText(this, "KEY PRESSED", Toast.LENGTH_SHORT).show();
		
		return false;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	public void onAttachedToWindow()
	{  
		Toast.makeText(this, "HOME KEY PRESS", Toast.LENGTH_SHORT).show();
		Log.v("onAttachedToWindow", "Successfully called, hopefully when home pressed");
		
		this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);     
		super.onAttachedToWindow();  
	}
}
