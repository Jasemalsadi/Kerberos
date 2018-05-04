package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

import database.ports;

public class client {
	private String username;
	private String password;
	private Socket server;
	private PrintWriter out;
	private BufferedReader in;
	private String A_TGS;
	private String  TicketToTGS;
	private boolean isAuthenticate;
	private boolean isTGSAuth;
	private boolean isConnectedToBob;
	private String AB_key;
	private String 	 TicketForTheEndServer;
	private int timestamp;
	public client(String name , String pass) {
		this.username = name;
		this.setPassword(pass);
		isAuthenticate = isTGSAuth  = isConnectedToBob = false;
		timestamp =-1;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = usefullThings.checkPassLength(password);
		System.out.println(password);
	}
	
	public void connectToServerName(String serverName) {
		System.out.println("---------------------------------------");
		System.out.println("I. Connect to AS Server");
		if(!isAuthenticate) this.authenticateMe();
		if(!isAuthenticate ||  TicketToTGS == null || A_TGS == null) {
		System.out.println("I.4 We couldn't autheticate you , so you will be getting out from the session");
		return;
		}
		System.out.println("II Client Authenticated. Now going to TGS :P ");
		GettingTicketFromTGS(serverName);
		
		if(!isTGSAuth ||  AB_key == null || TicketForTheEndServer == null || timestamp == -1) {
			System.out.println("II.4 We couldn't communicate with TGS server , so you will be getting out from the session");
			return;
			}
		
		System.out.println("II Connect to our End server ");

		CommunicateWithBob(serverName);
		if(isConnectedToBob){
			System.out.println( serverName + " Server is fully connected , authinticted and granted to communicate with you. You can enjoy now wit the server :>  ");
		}
		
	}
	
private void authenticateMe() {
		connectToSomeServer(ports.ASport);
		boolean isItSuccessfullyAuthenticated = true;
		System.out.println("I.1 Client is connected to AS server and  it sending the username");
		out.println(username);
		out.flush();
		String dataFromServer = readSomethingFromServer() ; 
		if(dataFromServer == null) return; 
	
		System.out.println("I.2 We recived the data from the AS server. Now process it ");


		if(dataFromServer.equals("null")) {  // Server didn't find a password in his DB
			System.out.println("I.3 Server Didn't find the username in is his DB");
			return;
		}
		System.out.println("username " + username +" PASSSWROD : " + password);
		System.out.println(dataFromServer);
		String decryptedData = usefullThings.decryptSomething(dataFromServer, password);
		if(decryptedData == null) {
			System.out.println("I.3 Your password isn't valid");
			return;
		}
		System.out.println("I.3 We decrypt the first message coming from AS using your password");
		 A_TGS = decryptedData.split(usefullThings.delimiter)[0];
		 TicketToTGS = decryptedData.split(usefullThings.delimiter)[1];
		
		isAuthenticate = isItSuccessfullyAuthenticated;
		
	}
	
private void GettingTicketFromTGS(String servername) {
		System.out.println("II TGS Communication ");
		System.out.println("\r II.1 Connecting to TGS ");
		connectToSomeServer(ports.TGSport);
		 timestamp = usefullThings.generateT();
		String PacketToBeSent = usefullThings.encryptSomething(Integer.toString(timestamp), A_TGS);
		PacketToBeSent = TicketToTGS  + usefullThings.delimiter + servername + usefullThings.delimiter +   PacketToBeSent;
		out.println(PacketToBeSent);
		System.out.println("\r II.2 Packet Sent to TGS ");
		
		String dataFromServer = readSomethingFromServer() ; 
		if(dataFromServer == null) return; 
		
		System.out.println("II.3 We recived the data from the TGS server. Now process it ");
		// split the packets to two boxes
		String TicketForTheClient = dataFromServer.split(usefullThings.delimiter)[0];
		 TicketForTheEndServer = dataFromServer.split(usefullThings.delimiter)[1];
		
		// Decrypt the Client Ticket
		String decryptedClientTicket = usefullThings.decryptSomething(TicketForTheClient, A_TGS);
		AB_key = decryptedClientTicket.split(usefullThings.delimiter)[1];
		isTGSAuth = true;
			
	}

private void connectToSomeServer(int port) {
		// Closing any previous communication 
		try {
			if (in != null)
			in.close();
			if (out != null)
				out.close();
			if (server != null)
				server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
		// Connecting to specific server
		try {
			server = new Socket("localhost", port);
			out = new PrintWriter(server.getOutputStream(),true);
			in = new BufferedReader(new InputStreamReader(server.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
	}

private void CommunicateWithBob(String serverName){
	int EndServerPort = ports.giveMePortForEndServer(serverName);
	if(EndServerPort == -1) {
		System.out.println("We didn't find the port for the server");
		return;
	}
	connectToSomeServer(EndServerPort);
	String PacketToBeSent = usefullThings.encryptSomething(Integer.toString(timestamp), AB_key) + // Time Stamp packet
			usefullThings.delimiter + 
			TicketForTheEndServer; // Packet to the Bob server
	out.println(PacketToBeSent);
	out.flush();
	String dataFromServer = readSomethingFromServer();
	if(dataFromServer == null) return;
	dataFromServer = usefullThings.decryptSomething(dataFromServer, AB_key);
	if(dataFromServer == null) return;
	int timeStampFromServer  = Integer.parseInt(dataFromServer);
	if(timestamp != timeStampFromServer+1){
		System.out.println("Error in validation of time stamp between end server and client");
	}
	
	isConnectedToBob = true;

}

private String readSomethingFromServer() {
	String dataFromServer = null;
	while(dataFromServer == null) {
		try {
			dataFromServer = in.readLine();	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return null;
				}
			}
	return dataFromServer;
	}

}
