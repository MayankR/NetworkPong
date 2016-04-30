package Networking;

import java.net.*;

import Game.StartingClass;

public class PlayGame {
	public static String[] IP = {"", "", "", ""};
	public static boolean[] human = {true, false, false, false};
	public static int myNum = 1;	//According to server
	static int playGamePort = 9879;
	static int[] posArray = {0, 0, 0, 0};
	static int[] lastPosArray = {0, 0, 0, 0};
	static int[] sameCount = {0, 0, 0, 0};
	static StartingClass scc = null;
	static int dropFrameCount;
	static int ballXPos = 0, ballLastXPos = 0, ballSameXCount = 0;
	static int ballYPos = 0, ballLastYPos = 0, ballSameYCount = 0;
	static int[] lifeArray = {10, 10, 10, 10};
	static int ballBroadcaster = 0;
	
	public static void sendPos(int pos, int playerNum) throws Exception {
		String sentence = playerNum + ";" + pos + ";";
		broadcastData(sentence);
	}
	
	public static void sendBallPos(int posX, int posY) throws Exception {
		String sentence = "b;" + posX + ";" + posY + ";" + myNum + ";";
		broadcastData(sentence);
	}
	
	public static void sendLife(int l1, int l2, int l3, int l4) throws Exception {
		String sentence = "l;" + l1 + ";" + l2 + ";" + l3 + ";" + l4 + ";";
		broadcastData(sentence);
	}
	
	private static void broadcastData(String data) throws Exception {
		DatagramSocket clientSocket = new DatagramSocket();
		byte[] sendData = new byte[1024];
		String sentence = data;
		System.out.println("Sending data: " + data);
		sendData = sentence.getBytes();
	    
		for(int i=0;i<4;i++) {
			if(!human[i]) continue;
			if(i == myNum-1) continue;
			
			InetAddress IPAddress = InetAddress.getByName(IP[i]);
			
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
	
	public static void setBallPos(int ballX, int ballY) {
		ballXPos = ballX;
		ballYPos = ballY;
	}
	
	public static void setLives(int l1, int l2, int l3, int l4) {
		lifeArray[0] = l1;
		lifeArray[1] = l2;
		lifeArray[2] = l3;
		lifeArray[3] = l4;
	}
	
	public static int getLife(int playerNum) {
		int serverNum = (playerNum + myNum - 1);
		if(serverNum > 4) serverNum -= 4;
		return lifeArray[serverNum - 1];
	}
	
	public static int getPos(int playerNum) {
		int serverNum = (playerNum + myNum - 1);
		if(serverNum > 4) serverNum -= 4;
		if(lastPosArray[serverNum - 1] == posArray[serverNum - 1]) {
			sameCount[serverNum - 1]++;
		}
		else {
			sameCount[serverNum - 1] = 0;
		}
		if(posArray[serverNum - 1] != 0 && sameCount[serverNum - 1] >= 1000) {
			System.out.println(playerNum + " is declared disconnected!!!");
			setPlayerAsAI(playerNum);
		}
		lastPosArray[serverNum - 1] = posArray[serverNum - 1];
		return posArray[serverNum - 1];
	}
	
	private static void setPlayerAsAI(int playerNum) {
		if(playerNum == 2) { StartingClass.player2 = false; }
		if(playerNum == 3) { StartingClass.player3 = false; }
		if(playerNum == 4) { StartingClass.player4 = false; }
		human[playerNum - 1] = false;
	}
	
	public static int getBallXPos() {
		//Store previous positions of ball
		if(ballLastXPos == ballXPos) {
			ballSameXCount++;
		}
		else {
			ballSameXCount = 0;
		}
		if(ballXPos != 0 && ballSameXCount >= 1000 && ballYPos != 0 && ballSameYCount >= 1000) {
			System.out.println("Ball broadcaster is declared disconnected!!!");
			human[ballBroadcaster - 1] = false;
			
			//Check who becomes the next host.
			int maxIPIndex = myNum - 1, maxIPNum = 0;
			for(int i=0;i<4;i++) {
				if(!human[i]) continue;
				
				//Convert IP to a number by removing dots.
				String ipNumString = "";
				for(int j=4;j<=IP[i].length();j++) {
					if(IP[i].charAt(j) == '.') continue;
					ipNumString = ipNumString + IP[i].charAt(j);
				}
				if(Integer.parseInt(ipNumString) > maxIPNum) {
					maxIPNum = Integer.parseInt(ipNumString);
					maxIPIndex = i;
				}
			}
			
			//Set myself as host if I have max value of IP.
			if(maxIPIndex == myNum - 1) {
				StartingClass.broadcastBall = true;
			}
		}
		ballLastXPos = ballXPos;
		
		//Return ball x.
		return ballXPos;
	}
	
	public static int getBallYPos() {
		//Store previous positions of ball
		if(ballLastYPos == ballYPos) {
			ballSameYCount++;
		}
		else {
			ballSameYCount = 0;
		}
		ballLastYPos = ballYPos;
		
		//Return ball y.
		return ballYPos;
	}
	
	public static void startGettingData(StartingClass sc) {
		scc = sc;
		ServerThread st = new ServerThread();
		st.start();
	}
}
