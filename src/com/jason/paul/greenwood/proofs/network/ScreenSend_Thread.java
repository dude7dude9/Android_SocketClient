package com.jason.paul.greenwood.proofs.network;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import android.graphics.Bitmap;
import android.util.Log;

public class ScreenSend_Thread implements Runnable {

	private static String IP_ADDRESS = "192.168.2.2";
//	private static String IP_ADDRESS = "192.168.75.41";
	private static String PORT_ADDRESS = "10012";
//	private static String PORT_ADDRESS2 = "10011";
	private boolean stopSending = false;
	private long startT, endT;

	public void endScreenSend(boolean send){
		stopSending = send;
	}

	public void run() {
		try { 
			Log.v("Socket_1-Socekt: " , "IP: " + IP_ADDRESS + ", Port: " + PORT_ADDRESS);
			InetAddress serverAddress = InetAddress.getByName( IP_ADDRESS );
			int serverPort = Integer.parseInt( PORT_ADDRESS ); 
//			int serverPort2 = Integer.parseInt( PORT_ADDRESS2 );
			
//			InetAddress address = InetAddress.getByName("225.4.5.6");
//			MulticastSocket mSocket = new MulticastSocket(serverPort2);
//			mSocket.setReuseAddress(true);
//			mSocket.joinGroup(address);
			Socket socket = new Socket( serverAddress, serverPort );
			while(true){
				//Connect to sockets
//				Log.v("Socket_1", "Start");
//				Socket socket = new Socket( serverAddress, serverPort );
//				Log.v("Socket_1", "Connected");
				
//				Log.v("Socket_2", "Start");
//				Socket socket2 = new Socket( serverAddress, serverPort2 );
//				Log.v("Socket_2", "Connected");
				

				//DatagramPacket packet;
				
				// TODO separate listen for interrupt and send screen into separate threads
				// then listen only needs messages when lock/unlock occurs
				
				//Set for delay timer
				startT = System.currentTimeMillis();
				try{
					if(stopSending){
						break;
					}
					Bitmap currScreen;
					if( (currScreen = networkingProof.getCurrentScreen()) != null){
//						Log.v("CHECK Image is set", networkingProof.getCurrentScreen().toString() );

						//Output Image to Server
						int rByte = currScreen.getRowBytes();
						int picHeight = currScreen.getHeight();
						int size = rByte*picHeight;
//						Log.v("ScreenSendThread", "Picture Size = " + Integer.toString(ba.length) );
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						BufferedOutputStream buffOut = new BufferedOutputStream( socket.getOutputStream() );
//						String result = String.valueOf(networkingProof.getCurrentScreen().compress(Bitmap.CompressFormat.JPEG, 100, buffOut) );
						String result = String.valueOf(networkingProof.getCurrentScreen().compress(Bitmap.CompressFormat.JPEG, 100, baos) );
						byte[] ba = baos.toByteArray();
						Log.v("ScreenSendThread", "Picture Size = " + Integer.toString(ba.length) );
						DataOutputStream DA = new DataOutputStream(buffOut);
						DA.writeBytes(Integer.toString(ba.length)+"\n");
//						buffOut.write(buffer)
						buffOut.write(ba);
//						Log.v("ScreenSend-Result", result);
						buffOut.flush();
//						buffOut.close();
					}
//					socket.close();

					//Get new screen for next send
					networkingProof.setCurrentScreen();
					
//					BufferedReader buffReadIn = new BufferedReader( new InputStreamReader( socket2.getInputStream() ) );
//					String response = buffReadIn.readLine();
//					DatagramPacket packet;
//					byte[] data = new byte[32];
					//packet = new DatagramPacket(data, data.length, address, serverPort2);
//					packet = new DatagramPacket(data, data.length);
					
					
					//mSocket.setSoTimeout(5000);
//					Log.v("Get Packet", "Pending the packet send");
//					mSocket.receive(packet);
//					String response = new String(packet.getData());
//					if(response == null){
//						Log.v("Thread Listen", "No Response(NULL)");
//						Log.e("THREAD LISTEN", "No Response(NULL)");
//					}else{
//						Log.e("THREAD ERR", "Im Gettin SOmethin");
//						Log.e("MESSAGE IS: ", response);
//
//						//createLockIntent();
//						if(response.trim().equals("LOCK")){
//							Log.v("Remote String", "Received LOCK response");
//							homeObject.createLockIntent();
//
//						}else if(response.trim().equals("UNLOCK")){
//							Log.v("Remote String", "Received UNLOCK response");
//							//Return to screen on before locking
//							homeObject.removeLockIntent();
//						}
//					}
//					buffReadIn.close();
					
					endT = System.currentTimeMillis();
					//Half a second delay before screen update
					if(endT - startT < 500){
						try {
							Thread.sleep(500 - (endT-startT));
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
//						threadTimer(this, (500 - (endT-startT)) );
					}
					
//					socket2.close();
				}finally{
//					if(!socket.isClosed()){
//						socket.close();
//					}
//					if(!socket2.isClosed()){
//						socket2.close();
//					}
				}
			}
			
//			if(!mSocket.isClosed()){
//				mSocket.leaveGroup(address);
//				mSocket.close();
//			}
		} catch( NumberFormatException e ) {
			e.printStackTrace();
		} catch( IOException e ) {
			System.out.println( e.getCause() );
			e.printStackTrace();
		}
	}


//	synchronized private void threadTimer(ScreenSend_Thread waitThread, long waitTime){
//		try {
//			waitThread.wait(waitTime);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		return;
//	}
}
