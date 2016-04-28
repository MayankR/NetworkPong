package Networking;

import java.net.*;

public class PlayGame {
	public static String[] IP = {"", "", "", ""};
	public static boolean[] human = {true, false, false, false};
	public static int myNum = 1;
	static int playGamePort = 9879;
	static int[] posArray = {0, 0, 0, 0};
	
	public static void sendPos(int pos) throws Exception {
		DatagramSocket clientSocket = new DatagramSocket();
		byte[] sendData = new byte[1024];
//	    System.out.println("Broadcasting my position");
		String sentence = myNum + ";" + pos + ";";
		sendData = sentence.getBytes();
	    
		for(int i=0;i<4;i++) {
			if(!human[i]) continue;
			if(i == myNum-1) continue;
			
			InetAddress IPAddress = InetAddress.getByName(IP[i]);
			
//			System.out.println("Sending to: " + i + " at IP: " + IP[i]);
			
			DatagramPacket sendPacket = 
					new DatagramPacket(sendData, sendData.length, IPAddress, playGamePort);
			clientSocket.send(sendPacket);
		}
		
		clientSocket.close();
	}
	
	public static void triggerStart() throws Exception {
		DatagramSocket clientSocket = new DatagramSocket();
		byte[] sendData = new byte[1024];
	    System.out.println("Broadcasting my position");
		String sentence = "g;";
		sendData = sentence.getBytes();
	    
		for(int i=0;i<4;i++) {
			if(!human[i]) continue;
			if(i == myNum-1) continue;
			
			InetAddress IPAddress = InetAddress.getByName(IP[i]);
			
			System.out.println("Sending start trigger to: " + i + " at IP: " + IP[i]);
			
			DatagramPacket sendPacket = 
					new DatagramPacket(sendData, sendData.length, IPAddress, playGamePort);
			clientSocket.send(sendPacket);
		}
		
		clientSocket.close();
	}
	
	public static void setPos(int playerNum, int pos) {
		posArray[playerNum - 1] = pos;
	}
	
	public static int getPos(int playerNum) {
		int serverNum = (playerNum + myNum - 1);
		if(serverNum > 4) serverNum -= 4;
//		System.out.println("Sending back requested pos of player " + playerNum);
		return posArray[serverNum - 1];
	}
	
	public static void startGettingData() {
		ServerThread st = new ServerThread();
//		System.out.println("Starting server thread to get data");
		st.start();
	}
}
