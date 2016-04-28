package Networking;

import java.io.*;
import java.net.*;

import Game.StartingClass;

public class StartGame {
	UserType type;
	String myUserName = "";
	int startGamePort = 9876;
	String myIP;
	String[] IP = {"", "", "", ""};
	int[] playerPorts = {0, 0, 0, 0};
	int playerCount = 0;
	StartingClass mGame = null;
	boolean[] human = {true, false, false, false};
	static int totalJoined = 0;
	
	public StartGame(UserType type, String userName, String ip1, int numPlayers, StartingClass game) {
		this.type = type;
		mGame = game;
		numPlayers = 1;
		if(mGame.player2) { numPlayers++; human[1] = true; PlayGame.human[1] = true;}
		if(mGame.player3) { numPlayers++; human[2] = true; PlayGame.human[2] = true; }
		if(mGame.player4) { numPlayers++; human[3] = true; PlayGame.human[3] = true; }
		System.out.println("Number of players including me: " + (numPlayers));
		if(type == UserType.START) {
			try {
				playerCount = numPlayers;
				connectUsers(startGamePort, userName, numPlayers);
				totalJoined++;
			}
			catch(Exception e) {
				e.printStackTrace();
				System.out.println("Unable to Start Game... :(");
			}
		}
		else {
			IP[0] = ip1;
			PlayGame.IP[0] = ip1;
			startJoining(ip1, startGamePort, userName);
			totalJoined++;
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
		System.out.println("Setting server IP as: " + uName);
		myIP = uName;
		IP[0] = uName;
	}
	
	private void connectUsers(int myPort, String userName, int numPlayers) throws Exception {
		DatagramSocket serverSocket = new DatagramSocket(myPort);
        int cur = 1;
        System.out.println("Waiting to join");
        for(int i=1;i<numPlayers;i++) {
        	byte[] receiveData = new byte[1024];
        	//Receive user name of a player and my IP
        	DatagramPacket receivePacket = 
        			new DatagramPacket(receiveData, receiveData.length);
        	serverSocket.receive(receivePacket);
        	String data1 = new String( receivePacket.getData());
        	System.out.println("SERVER RECEIVED: " + data1);
        	
        	//Store my IP and player user name
        	setPlayerData(data1, i);
        	System.out.println("Server player data set");
        	
        	InetAddress IPAddressPlayer = receivePacket.getAddress();
        	int port = receivePacket.getPort();
        	
        	//Store player IP
        	while(!human[cur]) { cur++; }
        	IP[cur] = IPAddressPlayer.toString();
        	IP[cur] = IP[cur].substring(1, IP[cur].length());
        	playerPorts[cur] = port;
        	PlayGame.IP[cur] = IP[cur];
        	
        	System.out.println("Joining PLayer IP stored: " + IP[cur] + " at index " + cur);
        	cur++;

            byte[] sendData = new byte[1024];
        	//Send own user name, player IP and total player count to Player
        	String data = userName + ";" + IPAddressPlayer + ";" + numPlayers + ";" + (cur) + ";" + mGame.level;
        	sendData = data.getBytes();
        	DatagramPacket sendPacket =
        			new DatagramPacket(sendData, sendData.length, IPAddressPlayer, port);
        	serverSocket.send(sendPacket);

			totalJoined++;
        }
        System.out.println("Joined all players once");
        
        //Make string to send to all players
        String allIP = "";
        for(int i=0;i<=3;i++) {
        	allIP = allIP + IP[i] + ";";
        }
        System.out.println("Made string of all IP: " + allIP);
        long ttt = System.currentTimeMillis();
        while(System.currentTimeMillis() - ttt < 4000);

        //Send all IP to All players
        for(int i=1;i<=3;i++) {
        	if(!human[i]) continue;
            byte[] sendDataIP = new byte[1024];
        	sendDataIP = allIP.getBytes();
        	System.out.println("Set IP data to send");
        	InetAddress playerIP = InetAddress.getByName(IP[i]);
        	System.out.println("made InetAddress: " + playerIP.toString() + " at port " + playerPorts[i]);
        	DatagramPacket sendPacket =
        			new DatagramPacket(sendDataIP, sendDataIP.length, playerIP, playerPorts[i]);
        	System.out.println("Made packet");
        	serverSocket.send(sendPacket);
        	System.out.println("Packet Sent");
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
			e.printStackTrace();
			System.out.println("Unable to connect to server.");
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
		System.out.println("Data from server is " + serverData);
		
		String serverName = "", serverIP = "", totPlayers = "", myNum = "", myLevel = "";
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
		while(serverData.charAt(i) != ';') {
			totPlayers = totPlayers + serverData.charAt(i); 
			i++;
		}
		i++;
		myNum = "" + serverData.charAt(i);
		myLevel = "" + serverData.charAt(i + 2);
		playerCount = Integer.parseInt(totPlayers);
		mGame.level = Integer.parseInt(myLevel);
		System.out.println("Player count: " + playerCount);
		System.out.println("My player number: " + myNum);
		
		PlayGame.myNum = mGame.playerNum;
		
		byte[] receivePlayersData = new byte[1024];
		
		//Receive all IP
		DatagramPacket receivePlayerPacket = new DatagramPacket(receivePlayersData, receivePlayersData.length);
		clientSocket.receive(receivePlayerPacket);
		String serverPlayersData = new String(receivePlayerPacket.getData());
		System.out.println("More data from server: " + serverPlayersData);
		
		String curIP = "";
		int in=0, indexIP = 0;
		while(in < serverPlayersData.length()) {
			while(serverPlayersData.charAt(in) != ';') {
				curIP = curIP + serverPlayersData.charAt(in);
				in++;
			}
			in++;
			System.out.println("player IP: " + curIP);
			IP[indexIP] = curIP;
			PlayGame.IP[indexIP] = curIP;
			curIP = "";
			indexIP++;
			if(indexIP == 4) break;
		}
		if(!IP[(1 + mGame.playerNum - 1)%4].equals("")) { mGame.player2 = true; }
		if(!IP[(2 + mGame.playerNum - 1)%4].equals("")) { mGame.player3 = true; }
		if(!IP[(3 + mGame.playerNum - 1)%4].equals("")) { mGame.player4 = true; }

		if(!IP[1].equals("")) { PlayGame.human[1] = true; }
		if(!IP[2].equals("")) { PlayGame.human[2] = true; }
		if(!IP[3].equals("")) { PlayGame.human[3] = true; }
		
		clientSocket.close();
	}
}
