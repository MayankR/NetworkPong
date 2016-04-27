package Networking;

import java.io.*;
import java.net.*;

public class StartGame {
	UserType type;
	String myUserName = "";
	
	public StartGame(UserType type, String userName, String ip1, String ip2, String ip3, String ip4) {
		this.type = type;
		if(type == UserType.JOIN) {
			try {
				startJoining(getPort(ip1), userName);
			}
			catch(Exception e) {
				System.out.println("Unable to Join Game... :(");
			}
		}
		else {
			connectUsers(ip1, ip2, ip3, ip4, userName);
		}
	}
	
	private int getPort(String ip) {
		int last = 2;
		for(int i=ip.length()-1;i>=0;i--) {
			last += (int)ip.charAt(i);
		}
		return last*21;
	}
	
	private void startJoining(int myPort, String userName) throws Exception {
		DatagramSocket serverSocket = new DatagramSocket(myPort);
        byte[] sendData = new byte[1024];
        while(true) {
        	byte[] receiveData = new byte[1024];
        	//Receive user name of P1
        	DatagramPacket receivePacket = 
        			new DatagramPacket(receiveData, receiveData.length);
        	serverSocket.receive(receivePacket);
        	String name1 = new String( receivePacket.getData());
        	System.out.println("RECEIVED: " + name1);
        	
        	InetAddress IPAddress = receivePacket.getAddress();
        	int port = receivePacket.getPort();
        	
        	//Send own user name to P1
        	sendData = userName.getBytes();
        	DatagramPacket sendPacket =
        			new DatagramPacket(sendData, sendData.length, IPAddress, port);
        	serverSocket.send(sendPacket);
        	
        	byte[] receiveIPData = new byte[1024];
        	//Receive all IP address from P1
        	DatagramPacket receiveIPPacket = 
        			new DatagramPacket(receiveIPData, receiveIPData.length);
        	serverSocket.receive(receiveIPPacket);
        	String ipData = new String( receiveIPPacket.getData());
        	System.out.println("RECEIVED IP DATA: " + ipData);
        	serverSocket.close();
        	break;
        }
	}
	
	public void connectUsers(String ip1, String ip2, String ip3, String ip4, String userName) {
		int totPlayers = 0;
		String data = "";
		if(!ip1.equals("")) {
			totPlayers++;
			data = data + ";" + ip1;
		}
		if(!ip2.equals("")) {
			totPlayers++;
			data = data + ";" + ip2;
		}
		if(!ip3.equals("")) {
			totPlayers++;
			data = data + ";" + ip3;
		}
		if(!ip4.equals("")) {
			totPlayers++;
			data = data + ";" + ip4;
		}
		try {
			connectToIP(ip1, getPort(ip1), userName, "1;"+totPlayers+data);
			connectToIP(ip2, getPort(ip2), userName, "2;"+totPlayers+data);
			connectToIP(ip3, getPort(ip3), userName, "3;"+totPlayers+data);
			connectToIP(ip4, getPort(ip4), userName, "4;"+totPlayers+data);
		}
		catch(Exception e) {
			
		}
	}
	
	private void connectToIP(String ip, int port, String userName, String data) throws Exception {
//		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		DatagramSocket clientSocket = new DatagramSocket();
		InetAddress IPAddress = InetAddress.getByName(ip);
		
		byte[] sendData = new byte[1024];
		byte[] receiveData = new byte[1024];
		String sentence = userName;
		sendData = sentence.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
		clientSocket.send(sendPacket);
		
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		clientSocket.receive(receivePacket);
		String modifiedSentence = new String(receivePacket.getData());
		System.out.println("Name of user at IP " + ip + " is " + modifiedSentence);
		
		byte[] sendIPData = new byte[1024];
		String IPsentence = data;
		sendIPData = IPsentence.getBytes();
		DatagramPacket sendIPPacket = new DatagramPacket(sendIPData, sendIPData.length, IPAddress, port);
		
		clientSocket.send(sendIPPacket);
		
		clientSocket.close();
	}
}
