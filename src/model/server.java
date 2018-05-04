package model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import database.Repository;
import database.ports;
import services.ASService;
import services.service;

public class server {
	private   Repository db;
	private ServerSocket server;
	private service service;
	private int port;
	public  server(int port  ,  service service) {
		db.getSharedRepository();
		this.service = service;
		this.port = port;
		try {
			server = new ServerSocket(port);
		} catch ( IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	public void runTheServer() {
		while (true) {
			try {
				Socket client = server.accept();
				if(client != null) {
					System.out.println( service.getClass().getName() + " server gets a request, he will route it   to service it");
					service.setClient(client);
					service.run();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("We couldn't inite the server");
				//e.printStackTrace();
			}

		}
		
	}
	

}
