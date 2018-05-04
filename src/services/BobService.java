package services;

import java.io.IOException;
import java.util.ArrayList;

import database.Repository;
import model.Endservers;
import model.usefullThings;

public  class BobService extends service {
	private String servername;
	

	private String TGS_B;
	private String AB_key;


	public BobService(String servername) {
		super();
		this.servername = servername;
		Repository rb = Repository.getSharedRepository();
		TGS_B = rb.giveMeMyTGS_BKey(this);
		
	}
	
	
	public void run() {
		if(TGS_B == null) {
			System.out.println("We didn't find " + servername+" server in DB of the System");
			return;
		}
		System.out.println(this.servername + " Server  Got Some connection , let see what he want :) "); 
		String dataComingFromClient = super.readSomethingFromClient();
		if(dataComingFromClient == null) return;
		System.out.println(this.servername + " Server recived some data, now let's process it ....");
		String timestampEncrypted = dataComingFromClient.split(usefullThings.delimiter)[0];
		String TicketForBobEncrypted = dataComingFromClient.split(usefullThings.delimiter)[1];
		String TicketForBob = usefullThings.decryptSomething(TicketForBobEncrypted, TGS_B);
		// Now getting the AB key
		AB_key = TicketForBob.split(usefullThings.delimiter)[1];
		String clientName = TicketForBob.split(usefullThings.delimiter)[0];
		System.out.println("Hello " + clientName + " !!  " + servername + " server know you");
		// Now getting the time stamp T 
		String timestamp = usefullThings.decryptSomething(timestampEncrypted, AB_key);
		if(timestamp == null) return;
		String dataToBeSent = usefullThings.encryptSomething(Integer.toString(Integer.parseInt(timestamp) -1), AB_key);
		out.println(dataToBeSent);
		out.flush();
	}
	
	public String getServername() {
		return servername;
	}

	public void setServername(String servername) {
		this.servername = servername;
	}
	

}
