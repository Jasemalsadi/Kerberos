package database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import model.Endservers;
import model.UserMetaData;
import services.ASService;
import services.BobService;
import services.TGSService;
import services.service;


public class Repository { // This class implements Repository Design Pattern
private ArrayList<UserMetaData> users;
private ArrayList<Endservers> EndserversMap;
private String AsTgsKey = "1234567899999999";
private ports port;
private static Repository instance;
private Repository() {
	users = new ArrayList<UserMetaData> ();
	EndserversMap = new ArrayList<Endservers> ();

//	readArrayList("UserMetaData");
}
public static Repository getSharedRepository() {
	if(instance == null) {
		instance = new Repository();
	}
	return instance;
}


public void addObejct(Object t,String type) {
	switch(type) {
	case "UserMetaData": 
		users.add((UserMetaData) t);
		return;
	case "Endservers": 
		EndserversMap.add((Endservers) t);
		return;		
	}
}

public int getLengthOfObject(String type) { // Get the arraylist size;
	switch(type) {
	case "UserMetaData": 
		return users.size();
	}
	return 0;
}


private  void  saveArrayList(String type) {// Ignore it, we thought that we need to store to  file . But then I read the files and nothing about saving and reading
	try {
		FileOutputStream f = new FileOutputStream(new File("All"+type+".txt"));
		ObjectOutputStream o = new ObjectOutputStream(f);

		// Write objects to file
		switch(type) {
		case "UserMetaData": 
		o.writeObject(users);
			break;	
		}		

		o.close();
		f.close();

	} catch (FileNotFoundException e) {
		System.out.println("File not found");

	} catch (IOException e) {
		System.out.println("Error initializing stream");
	}

}
public void readArrayList(String type) { // Ignore it, we thought that we need to read from file . But then I read the files and nothing about saving and reading
	 try
     {
         FileInputStream fis = new FileInputStream("All"+type+".txt");
         ObjectInputStream ois = new ObjectInputStream(fis);
         System.out.println(ois.readObject());
         switch(type) {
 		case "UserMetaData": 
 			users = (ArrayList<UserMetaData>) ois.readObject();
 			break;

 			default : 	
 		}	         
         ois.close();
         fis.close();
      }catch(IOException ioe){
         // ioe.printStackTrace();
    	//  System.out.println("All"+type+".txt " +" file is empty");
          return;
       }catch(ClassNotFoundException c){
          System.out.println("Class not found");
        //  c.printStackTrace();
          return;
       }
	
}


public Object getObject(int d,String type) { // For getting any object, So we keep our self consitent
	switch(type) {
	case "UserMetaData": 
		if (d<0 || d>users.size()) return null;
		return users.get(d);
	}
	return null;
}
public void saveEveryThing() {
	this.saveArrayList("UserMetaData");
	
}


public ArrayList<Endservers> giveMeAccessToEndServersMap(TGSService server) { // If the object want to access to the End servers keys  , then he needs to prove that he is TGS server 
	if(server != null) {
		return EndserversMap;
	}
	return null;
}

public String giveMeMyAsTgsKey(service service) { // If object want to access the AsTgsKey , he need to prove tha he is the object 
	if(service == null) return null;

	if(service instanceof TGSService || service instanceof ASService ) {
	return AsTgsKey;
	}
	return null;
	
}

public String giveMeMyTGS_BKey(BobService s) {  // If object want to access the TGS_B_key , he need to prove tha he is the object 
	if(s == null) return null;
	for(int i =0;i<EndserversMap.size();i++){
		if(EndserversMap.get(i).servername.equals(s.getServername())) {
			return EndserversMap.get(i).TGS_B;
		}
	}
	return null;
}

public int giveMePortServerName(String server) { // If object want to access the port number , he need to prove tha he is the object 
	for(int i=0;i<EndserversMap.size();i++) {
		Endservers tmp = EndserversMap.get(i);
		if(tmp.servername.equals(server)) {
			return tmp.portNumber;
		}
	}
	return -1;
}

}
