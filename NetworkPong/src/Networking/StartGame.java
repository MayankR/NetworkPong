package Networking;

import java.io.*;
import java.net.*;

public class StartGame {
	UserType type;
	String myUserName = "";
	int startGamePort = 9876;
	String myIP;
	String[] IP = new String[4];
	int playerCount = 0;
	
	public StartGame(UserType type, String userName, String ip1, int numPlayers) {
		this.type = type;
		if(type == UserType.START) {
			try {
				playerCount = numPlayers;
				connectUsers(startGamePort, userName, numPlayers);
			}
			catch(Exception e) {
				System.out.println("Unable to Start Game... :(");
			}
		}
		else {
			IP[0] = ip1;
			startJoining(ip1, startGamePort, userName);
		}
	}
	
	private int getPort(String ip) {
		int last = 2;
		for(int i=ip.length()-1;i>=0;i--) {
			last += (int)ip.charAt(i);
		}
		return last*21;
	}
	
	private void setPlayerData(String data, int num) {
		String uName = "", ip = "";
		int i=0;
		while(data.charAt(i) != ';') {
			uName = uName + data.charAt(i);
			i++;
		}
		i++;
		while(i<data.length()) {
			ip = ip + data.charAt(i);
			i++;
		}
		myIP = ip;
	}
	
	private void connectUsers(int myPort, String userName, int numPlayers) throws Exception {
		DatagramSocket serverSocket = new DatagramSocket(myPort);
        byte[] sendData = new byte[1024];
        for(int i=1;i<=numPlayers;i++) {
        	byte[] receiveData = new byte[1024];
        	//Receive user name of a player and my IP
        	DatagramPacket receivePacket = 
        			new DatagramPacket(receiveData, receiveData.length);
        	serverSocket.receive(receivePacket);
        	String data1 = new String( receivePacket.getData());
        	System.out.println("RECEIVED: " + data1);
        	
        	//Store my IP and player user name
        	setPlayerData(data1, i);
        	
        	InetAddress IPAddressPlayer = receivePacket.getAddress();
        	int port = receivePacket.getPort();
        	
        	//Store player IP
        	IP[i] = IPAddressPlayer.toString();
        	
        	//Send own user name, player IP and total player count to Player
        	String data = userName + ";" + IPAddressPlayer + ";" + numPlayers;
        	sendData = data.getBytes();
        	DatagramPacket sendPacket =
        			new DatagramPacket(sendData, sendData.length, IPAddressPlayer, port);
        	serverSocket.send(sendPacket);
        }
    	serverSocket.close();
    	System.out.println("Got data of all users");
	}
	
	public void startJoining(String ip1, int port, String userName) {
		String data = ip1 + ";" + userName;

		try {
			connectToIP(ip1, startGamePort, data);
		}
		catch(Exception e) {
			
		}
	}
	
	private void connectToIP(String ip, int port, String data) throws Exception {
//		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		DatagramSocket clientSocket = new DatagramSocket();
		InetAddress IPAddress = InetAddress.getByName(ip);
		
		//Send my data to server IP
		byte[] sendData = new byte[1024];
		byte[] receiveData = new byte[1024];
		String sentence = data;
		sendData = sentence.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
		clientSocket.send(sendPacket);
		
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		clientSocket.receive(receivePacket);
		String serverData = new String(receivePacket.getData());
		System.out.println("Data from server is" + serverData);
		
		String serverName = "", serverIP = "", totPlayers = "";
		int i=0;
		while(serverData.charAt(i) != ';') {
			serverName = serverName + serverData.charAt(i); 
			i++;
		}
		i++;
		while(serverData.charAt(i) != ';') {
			serverIP = serverIP + serverData.charAt(i); 
			i++;
		}
		i++;
		while(i < serverData.length()) {
			totPlayers = totPlayers + serverData.charAt(i); 
			i++;
		}
		playerCount = Integer.parseInt(totPlayers);
		
		clientSocket.close();
	}
}
