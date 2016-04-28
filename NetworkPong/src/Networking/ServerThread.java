package Networking;

import java.net.*;

public class ServerThread extends Thread {

	
	public ServerThread() {
		
	}
	
	public void run() {
        System.out.println("Hello from a thread!");
        try {
	        DatagramSocket serverSocket = new DatagramSocket(9879);
	        while(true) {
	        	byte[] receiveData = new byte[1024];
	        	DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
	        	serverSocket.receive(receivePacket);
	        	String data = new String( receivePacket.getData());
//	        	System.out.println("RECEIVED: " + data);
	        	
	        	if(data.charAt(0) == 'g') {
	        		
	        	}
	        	
	        	int playerNum = Integer.parseInt("" + data.charAt(0));
	        	int i = 2;
	        	String playerPosString = "";
	        	while(data.charAt(i) != ';') {
	        		playerPosString += data.charAt(i);
	        		i++;
	        	}
	        	int playerPos = Integer.parseInt(playerPosString);
	        	
	        	PlayGame.setPos(playerNum, playerPos);
	        	
//	        	InetAddress IPAddress = receivePacket.getAddress();
//	        	int port = receivePacket.getPort();
//	        	String capitalizedSentence = sentence.toUpperCase();
//	        	sendData = capitalizedSentence.getBytes();
//	        	DatagramPacket sendPacket =
//	        			new DatagramPacket(sendData, sendData.length, IPAddress, port);
//	        	serverSocket.send(sendPacket);	
	        }
        }
        catch(Exception e) {
        	System.out.println("Server down!!");
        	e.printStackTrace();
        }
    }
}
