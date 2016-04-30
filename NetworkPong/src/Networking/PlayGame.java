package Networking;

import java.net.*;

import Game.StartingClass;

public class PlayGame {
	public static String[] IP = {"", "", "", ""};
	public static boolean[] human = {true, false, false, false};
	public static int myNum = 1;
	static int playGamePort = 9879;
	static int[] posArray = {0, 0, 0, 0};
	static int[] lastPosArray = {0, 0, 0, 0};
	static int[] sameCount = {0, 0, 0, 0};
	static StartingClass scc = null;
	static int dropFrameCount;
	static int ballXPos = 0, ballLastXPos = 0, ballSameXCount = 0;
	static int ballYPos = 0, ballLastYPos = 0, ballSameYCount = 0;
	
	public static void sendPos(int pos) throws Exception {
		DatagramSocket clientSocket = new DatagramSocket();
		byte[] sendData = new byte[1024];
		String sentence = myNum + ";" + pos + ";";
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
	
	public static void sendBallPos(int posX, int posY) throws Exception {
		DatagramSocket clientSocket = new DatagramSocket();
		byte[] sendData = new byte[1024];
		String sentence = "b;" + posX + ";" + posY + ";";
		System.out.println("Sending ball pos " + posX + " " + posY);
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
		}
		lastPosArray[serverNum - 1] = posArray[serverNum - 1];
		return posArray[serverNum - 1];
	}
	
	public static int getBallXPos() {
		if(ballLastXPos == ballXPos) {
			ballSameXCount++;
		}
		else {
			ballSameXCount = 0;
		}
		if(ballXPos != 0 && ballSameXCount >= 1000 && ballYPos != 0 && ballSameYCount >= 1000) {
			System.out.println("Ball broadcaster is declared disconnected!!!");
		}
		ballLastXPos = ballXPos;
		return ballXPos;
	}
	
	public static int getBallYPos() {
		if(ballLastYPos == ballYPos) {
			ballSameYCount++;
		}
		else {
			ballSameYCount = 0;
		}
//		if(ballYPos != 0 && ballSameYCount >= 1000) {
//			System.out.println("Ball broadcaster is declared disconnected!!!");
//		}
		ballLastYPos = ballYPos;
		return ballYPos;
	}
	
	public static void startGettingData(StartingClass sc) {
		scc = sc;
		ServerThread st = new ServerThread();
		st.start();
	}
}
