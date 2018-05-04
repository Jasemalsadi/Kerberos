package services;

import java.io.IOException;
import java.util.ArrayList;

import database.Repository;
import model.*;

public class TGSService extends service{
	
	private String AsTgsKey;
	private ArrayList<Endservers> EndserversMap;
	public void run() {
		Repository rb = Repository.getSharedRepository();
		EndserversMap =  rb.giveMeAccessToEndServersMap(this); // It will link this EndserversMap with Repository
		AsTgsKey = rb.giveMeMyAsTgsKey(this);
		if(AsTgsKey == null) {
			System.out.println("We couldn't find the AS_TGS key in our DB , Please connect later ...");
			super.closeEveryThing();
			return;
		}
		System.out.println("TGS Got some connection , let check what he send .....");
		String dataComingFromClient = super.readSomethingFromClient();
		if(dataComingFromClient == null) return;

		System.out.println("TGS got data from client, now process it ....");
		// split the data
		String AS_TGS_Ticket =  dataComingFromClient.split(usefullThings.delimiter)[0];
		String servernameFromTheClient =  dataComingFromClient.split(usefullThings.delimiter)[1];
		String TGS_B = isServerNameExisit(servernameFromTheClient);
		if(TGS_B.equals("-1"))   {
			System.out.println("System didn't find the " + servernameFromTheClient + " in his DB");
			super.closeEveryThing();
		}
		String timestamp =  dataComingFromClient.split(usefullThings.delimiter)[2];
		// Decrypt them 
		String decrypted_AS_TGS_Ticket = usefullThings.decryptSomething(AS_TGS_Ticket, AsTgsKey);
		// Getting client name and A-TGS key 
		String clientName = decrypted_AS_TGS_Ticket.split(usefullThings.delimiter)[0];
		String A_TGS = decrypted_AS_TGS_Ticket.split(usefullThings.delimiter)[1];
		// Decrypt the timestamp using A-TGS key
		timestamp = usefullThings.decryptSomething(timestamp,A_TGS);
		
	if(timestamp == null){
		System.out.println("TGS couldn't process the data coming, sorry will reject your connection ... :(");
		super.closeEveryThing();
		return;
	}
	System.out.println(" TGS finishes processing the data, great. Now sending the data to the client");
		
	String AB_Key = usefullThings.generateRandomKey();
	String packetTobeSent =  usefullThings.encryptSomething(clientName + usefullThings.delimiter + AB_Key, TGS_B);
	packetTobeSent =  usefullThings.encryptSomething(servernameFromTheClient + usefullThings.delimiter + AB_Key , A_TGS ) // Ticket for alice 
			+ usefullThings.delimiter + packetTobeSent ; // Ticket for TGS
	
	out.println(packetTobeSent);
	out.flush();
	System.out.println(" TGS finished Sending the packet, Now terminate the Session ");
	
	super.closeEveryThing();
	
		}
	
	
	private String isServerNameExisit(String servernameFromTheClient) {
		for(int i=0;i<EndserversMap.size();i++){
			if(servernameFromTheClient.equals(EndserversMap.get(i).servername) ) {
				return EndserversMap.get(i).TGS_B;
			}
		}
		return "-1";
	}

	
}
