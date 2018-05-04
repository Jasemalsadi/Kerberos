package model;

import java.io.Serializable;
import java.net.DatagramPacket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserMetaData implements Serializable  {
	private static final long serialVersionUID = 1L;
	private String name;
	private String Hpassword; // The password hashed
	private String Realpassword; // The real password
	
	public UserMetaData(String name, String realpassword) {
		super();
		this.name = name;
		setPassword(realpassword);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHpassword() {
		return Hpassword;
	}
	public String getRealpassword() {
		return Realpassword;
	}
	public void setPassword(String password) {
		String temp = usefullThings.checkPassLength(password);
		password = temp == null ? Realpassword : temp ;
		Realpassword = password;
		Hpassword = usefullThings.hashSomething(password);
	}
	
	
	
	
	
}
