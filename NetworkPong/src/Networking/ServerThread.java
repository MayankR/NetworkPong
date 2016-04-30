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
	        	System.out.println("RECEIVED: " + data);
	        	
	        	if(data.charAt(0) == 'g') {
	        		PlayGame.scc.hostMousePressed();
	        		continue;
	        	}
	        	
	        	if(data.charAt(0) == 'b') {
	        		int i = 2;
	        		String posXString = "", posYString = "", serverNumString = "";
	        		while(data.charAt(i) != ';') {
	        			posXString = posXString + data.charAt(i);
	        			i++;
	        		}
	        		i++;
	        		while(data.charAt(i) != ';') {
	        			posYString = posYString + data.charAt(i);
	        			i++;
	        		}
	        		i++;
	        		while(data.charAt(i) != ';') {
	        			serverNumString = serverNumString + data.charAt(i);
	        			i++;
	        		}
	        		PlayGame.setBallPos(Integer.parseInt(posXString),
	        				Integer.parseInt(posYString));
	        		PlayGame.ballBroadcaster = Integer.parseInt(serverNumString);
	        		continue;
	        	}
	        	
	        	if(data.charAt(0) == 'l') {
	        		int i = 2;
	        		String l1String = "", l2String = "", l3String = "", l4String = "";
	        		while(data.charAt(i) != ';') {
	        			l1String = l1String + data.charAt(i);
	        			i++;
	        		}
	        		i++;
	        		while(data.charAt(i) != ';') {
	        			l2String = l2String + data.charAt(i);
	        			i++;
	        		}
	        		i++;
	        		while(data.charAt(i) != ';') {
	        			l3String = l3String + data.charAt(i);
	        			i++;
	        		}
	        		i++;
	        		while(data.charAt(i) != ';') {
	        			l4String = l4String + data.charAt(i);
	        			i++;
	        		}
	        		PlayGame.setLives(Integer.parseInt(l1String),
	        				Integer.parseInt(l2String),
	        				Integer.parseInt(l3String),
	        				Integer.parseInt(l4String));
	        		continue;
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
