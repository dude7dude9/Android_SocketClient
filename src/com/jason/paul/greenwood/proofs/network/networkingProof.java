package com.jason.paul.greenwood.proofs.network;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class networkingProof extends Activity {
	
	private Button Update;
	private Button Cancel;
	private Button Connect;
	private Button Lock;
	private EditText EditBox;
	
	private static RelativeLayout rootView = null;	
	private static Bitmap screenBmp = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        System.err.println("BEGININ");
        
        //Try sending bmp to remote of current activity
        rootView = (RelativeLayout) findViewById(R.id.NetworkProof_Screen_Layout);
        View v1 = rootView.getRootView();
        v1.setDrawingCacheEnabled(true);
        //String dir="myimages";
        screenBmp = v1.getDrawingCache();
        //saveBitmap(bm, dir, "capturedimage");
        
        Log.v("networkProof", "StartOfConstruct()");
        
        //Change Text programmatically
        //Button to Play or Pause audio file
        EditBox = (EditText) findViewById(R.id.entry);
		Update = (Button) findViewById(R.id.update);
		Update.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				//Update sets editable text region's text
				EditBox.setText("UPDATED TEXT");
			}
		});
		
		Cancel = (Button) findViewById(R.id.cancel);
		Cancel.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				//Cancel clears text from the editable text region
				EditBox.setText("");
				//ssThread.endScreenSend(true);
			}
		});
		
		Connect = (Button) findViewById(R.id.connect);
		Connect.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				createConnection();
			}
		});
		
		Lock = (Button) findViewById(R.id.lockButton);
		Lock.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				createLockIntent();
			}
		});
    }
    
    public static Bitmap getCurrentScreen(){
    	return screenBmp;
    }
    
    public static void setCurrentScreen(){
    	View v1 = rootView.getRootView();
        v1.setDrawingCacheEnabled(true);
        //String dir="myimages";
        screenBmp = v1.getDrawingCache();
    }
    
    private void createConnection(){
//    	ScreenSend_Thread ssThreadNew =  new ScreenSend_Thread(this);
//		ssThread = ssThreadNew;
//		//Open socket connection
//		ssThread.endScreenSend(false);
//		
//		Thread sendScreenThread = new Thread(ssThread);
//		sendScreenThread.setName("Tablet_Listener");
//		sendScreenThread.start();
		
		startService(new Intent(this, ClassroomInformation_Service.class));
    }
    
    private void createLockIntent(){
		Intent intent = new Intent().setClass(this, LockScreen.class);
		this.startActivity(intent);
	}
}