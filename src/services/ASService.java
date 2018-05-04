package services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import database.Repository;
import model.UserMetaData;
import model.usefullThings;

public class ASService extends service {
	private String AsTgsKey;
	

	public void run() {
		Repository rb = Repository.getSharedRepository();
		AsTgsKey = rb.giveMeMyAsTgsKey(this); // Getting the key from DB
		System.out.println("AS_Service Got the client");
		String usernameFromTheClient = super.readSomethingFromClient();
		if(usernameFromTheClient == null) return;

		System.out.println("AS Service got the username from the user");
		System.out.println("Processing ....");

		Repository database = Repository.getSharedRepository();
			int size = database.getLengthOfObject("UserMetaData");
			String password = null;
			for( int i=0;i<size;i++) {
				UserMetaData d = (UserMetaData) database.getObject(i, "UserMetaData");
				if(d != null) {
					if(usernameFromTheClient.equals(d.getName())) {
						password = d.getRealpassword();
						break;
					}
				}
			}
			System.out.println("FROM AS SERVICE : " + usernameFromTheClient + "  " + password);
				String toBeSent = "";
				if(password == null) toBeSent = "null"; // If password not exisit , send null flag to client
				else {
					toBeSent = getPacketTobeSent(usernameFromTheClient,password);
				}

				out.println(toBeSent);
				out.flush();
  System.out.println("3. AS server finsh sending the hole packet");
				// Close every thing
  	     	super.closeEveryThing();
			System.out.println("4. AS server closed the session");

				
	}
		
		
	
	private String getPacketTobeSent(String user , String userpass) {
		System.out.println("1. Preparing the first ticket to TGS");
		String A_TGS = usefullThings.generateRandomKey();
		String paketToBeSent = usefullThings.encryptSomething(user+usefullThings.delimiter+ A_TGS, AsTgsKey);
		System.out.println("2. Preparing the Hole packet that will be sent");
		paketToBeSent = A_TGS + usefullThings.delimiter + paketToBeSent ;
		String EncryptedPacket = usefullThings.encryptSomething(paketToBeSent,userpass);
		return EncryptedPacket;
	}
	
	public void setAsTgsKey(String asTgsKey) {
		AsTgsKey = asTgsKey;
	}
}
