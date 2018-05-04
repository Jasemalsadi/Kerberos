package model;

import database.ports;

public class Endservers {
	public String servername;
	public String TGS_B;
	public int portNumber;
	public Endservers(String servername,int port) {
		this.servername = servername;
		TGS_B = usefullThings.generateRandomKey();
	
		this.portNumber = port;
	}
	
}
