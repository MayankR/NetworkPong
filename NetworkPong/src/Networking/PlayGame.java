package Networking;

import java.net.*;

public class PlayGame {
	static String[] IP = {"", "", "", ""};
	static boolean[] human = {true, false, false, false};
	static int myNum = 1;
	static int playGamePort = 9876;
	
	public static void sendPos(int pos) throws Exception {
		DatagramSocket clientSocket = new DatagramSocket();
		byte[] sendData = new byte[1024];
	       
	       
		String sentence = myNum + ";" + pos;
		sendData = sentence.getBytes();
	    
		for(int i=0;i<4;i++) {
			if(!human[i]) continue;
			
			InetAddress IPAddress = InetAddress.getByName(IP[i]);
			DatagramPacket sendPacket = 
					new DatagramPacket(sendData, sendData.length, IPAddress, playGamePort);
			clientSocket.send(sendPacket);
		}
		
		clientSocket.close();
	}
	
	public static void getData() {
		
	}
}
