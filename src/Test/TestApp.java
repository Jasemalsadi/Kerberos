package Test;
import java.util.ArrayList;

import database.*;
import model.*;
import services.*;

/*
 * 	Structure of the system 
 *  	The system below is structured into main 4 packages :
 *  	1. Database Package : It responsible for storing and getting any Data (arraylist(s)) . Such as users meta data , end servers, ports 
 *  	2. model Package :  It responsible for a static code that shared between objects . Because we implement don't repeat yourself (d.r.y) principle .
 *  	3. Services : Here Where the real work coming on. It include a services  classes 
 *  	4. Test  : The Test App .
 *  
 *  Note : To ensure quality of code and objects thinking , 
 * 			 we implement some design pattern like singleton , Factory pattern , MVC , Transfer Object pattern ,
 *   		 Data Access Object and Chain of Responsibility design patterns. 
 *  	     Also we use AES/CBC/PKCS5Padding Encryption 128-bit encryption algorithm
 *   
 *  Contributers : 
 *   	1. 	Jasem Alsadi  - 201402787
 *   	2. Ahmed Al-hasel - 201206560
 *   	3. Mohammed Aman  - 201205380
 *    
 *  How to read the code  ? 
 *   	1. Start with the model package , read  each of them But start with the client then server . 
 *   	2. Then Go to to services package  . Start with service abstract class -> ASService ->TGSService->BobService
 *   	3. Then Go to database package . Read the port class -> Repository . 
 *   	4. In Repository class, you will find some methods like giveMe***** . It just to make the code more controllable , 
 *   			modular and ensure that one class responsible for storing information  
 *   	5. Enjoy ;)
 *  
 */


public class TestApp {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ports.ASport = 5050;
		ports.TGSport = 5500;
		Repository rb = Repository.getSharedRepository();
		// Users in the AS & TGS 
		rb.addObejct(new UserMetaData("Ahmed","1234"), "UserMetaData");
		rb.addObejct(new UserMetaData("Jasem","asdjkdsaj12"), "UserMetaData");
		rb.addObejct(new UserMetaData("Mohammed","019238assda"), "UserMetaData");
		rb.addObejct(new UserMetaData("Mohammed2","01923s8assda"), "UserMetaData");
		rb.addObejct(new UserMetaData("Mohammed3","019238as3sda"), "UserMetaData");

		// End servers adding to the DB . Assuming that the port number entered below  is unique
		rb.addObejct(new Endservers("Bob",5007), "Endservers");
		rb.addObejct(new Endservers("Printer",5008), "Endservers");
		rb.addObejct(new Endservers("FTP",5009), "Endservers");

		
		establishAllServers();
		
		System.out.println("Welcome to kerbours Protocol Project");
		
		// Creating the clients
		client c = new client("Ahmed","1234");
		client b = new client("Jasem","asdjkdsaj12");
		client a = new client("Mohammed","019238assda");
		client d = new client("Mohammed2","01923s8assda");
		client e = new client("Mohammed3","019238as3sda");

		c.connectToServerName("Bob");
		b.connectToServerName("FTP");
		a.connectToServerName("Printer");

	}		

	
	private static  void establishAllServers() {
		Repository rb = Repository.getSharedRepository();
		server as_server =  new server(ports.ASport,new ASService());
		server TGS_server =  new server(ports.TGSport,new TGSService());
		server Bob_server =  new server(rb.giveMePortServerName("Bob"),new BobService("Bob"));
		server Printer_server =  new server(rb.giveMePortServerName("Printer"),new BobService("Printer"));
		server FTP_server =  new server(rb.giveMePortServerName("FTP"),new BobService("FTP"));

		// Running each server in new thread for listening for any incoming data
		new Thread(new Runnable() {
		     public void run() {
		    	 TGS_server.runTheServer();
		     }
		}).start();
		new Thread(new Runnable() {
		     public void run() {
		 		as_server.runTheServer();
		     }
		}).start();
		new Thread(new Runnable() {
		     public void run() {
		    	 Bob_server.runTheServer();
		     }
		}).start();
		
		new Thread(new Runnable() {
		     public void run() {
		    	 Printer_server.runTheServer();
		     }
		}).start();
		
		new Thread(new Runnable() {
		     public void run() {
		    	 FTP_server.runTheServer();
		     }
		}).start();
		
	}
}
