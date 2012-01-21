package com.jason.paul.greenwood.proofs.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

import android.util.Log;

public class LockScreen_Thread implements Runnable{

	private ClassroomInformation_Service homeObject;
	private static String PORT_ADDRESS2 = "10011";
	int serverPort2 = Integer.parseInt( PORT_ADDRESS2 );
	private boolean stopSending = false;
	private boolean currentlyLocked = false;

	public LockScreen_Thread(ClassroomInformation_Service classInformationService) {
		homeObject = classInformationService;
		Log.v("THREAD", "Constructor called");
	}
	
	public void endScreenSend(boolean send){
		stopSending = send;
	}

	public void run() {
		InetAddress address;
		try {
			address = InetAddress.getByName("225.4.5.6");

			MulticastSocket mSocket;
			mSocket = new MulticastSocket(serverPort2);
			mSocket.setReuseAddress(true);
			mSocket.joinGroup(address);
			DatagramPacket packet;
			String response = null;
			long startT, endT;

			while(true){	
				startT = System.currentTimeMillis();
				if(stopSending){
					break;
				}
				Log.v("LOCK_Thread", "Is receiving packets");
				byte[] data = new byte[16];
				packet = new DatagramPacket(data, data.length);

				Log.v("Get Packet", "Pending the packet send");
				System.out.println("WAITING TO RECEIVE LOCK/UNLOCK SCREEN COMMAND");
				mSocket.receive(packet);
				System.out.println("RECEIVED THE LOCK/UNLOCK SCREEN INTERRUPT COMMAND");
				response = new String(packet.getData());
				response.trim();
				if(response == null){
					Log.v("Thread Listen", "No Response(NULL)");
					Log.e("THREAD LISTEN", "No Response(NULL)");
				}else{
					Log.e("THREAD ERR", "Im Gettin SOmethin");
					Log.e("MESSAGE IS: ", response);

					//createLockIntent();
					if(response.trim().equals("LOCK")){
						Log.v("Remote String", "Received LOCK response");
						if(!currentlyLocked){
							homeObject.createLockIntent();
							currentlyLocked = true;
						}
					}else if(response.trim().equals("UNLOCK")){
						Log.v("Remote String", "Received UNLOCK response");
						//Return to screen on before locking
						homeObject.removeLockIntent();
						currentlyLocked = false;
					}
				}
				
				response = null;
				endT = System.currentTimeMillis();
				//Half a second delay before screen update
				if(endT - startT < 500){
					try {
						Thread.sleep(500 - (endT-startT));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
			if(!mSocket.isClosed()){
				mSocket.leaveGroup(address);
				mSocket.close();
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
