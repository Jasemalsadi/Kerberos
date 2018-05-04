package model;

import java.awt.RenderingHints.Key;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;
import java.util.logging.Level;  
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;  

public class usefullThings { // A class to not repeat myself
	private static String encryptionAlogrithm = "AES/CBC/PKCS5Padding";
	private static String HashingAlogrithm = "SHA-512";
	private static int passowrdSize = 16;
	public static String delimiter = "ServerNetowrk";
	public static  String hashSomething(String something) { 
		MessageDigest md;
        try { 
			md = MessageDigest.getInstance(HashingAlogrithm);
			md.update(something.getBytes());
			byte[] dataInBytes = md.digest();
			something  =  new String(dataInBytes,0,dataInBytes.length);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
			return "";
		}

		return something;
	}
	
	public static String encryptSomething(String something,String key) {  // Assuming the key already hashed using SHA-512
		SecretKeySpec keyData = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8),"AES");
		
        Cipher c;
		try {
			c = Cipher.getInstance(encryptionAlogrithm);
		    byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		    IvParameterSpec ivspec = new IvParameterSpec(iv);
	        c.init(Cipher.ENCRYPT_MODE, keyData,ivspec);  
	        byte[] cipher = c.doFinal(something.getBytes(StandardCharsets.UTF_8));
	        something =  new String(Base64.getEncoder().encode(cipher),"UTF-8");      
		} catch (Exception e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
			return "";
		}  

		return something;
	}
	
public static String decryptSomething(String something,String key) {
	something = new String(something);
	SecretKeySpec keyData = new SecretKeySpec(key.getBytes(),"AES");
    Cipher c;
	try {
		c = Cipher.getInstance(encryptionAlogrithm);
	    byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	    IvParameterSpec ivspec = new IvParameterSpec(iv);

        c.init(Cipher.DECRYPT_MODE, keyData,ivspec);

        byte[] cipherText = Base64.getMimeDecoder().decode(something);

        something  = new String(c.doFinal(cipherText));

	} catch (Exception e) {
		// TODO Auto-generated catch block
	//	e.printStackTrace();
		return null;
	}

		return something;
	}


public  static String checkPassLength(String something) { // Checking if password is 16 character or less 
	if( something.getBytes().length > passowrdSize) return null;
  	if(something.getBytes().length < passowrdSize) { // if the password is less than 16 characters , add paddings
		int length = passowrdSize-something.getBytes().length;
		for(int i =0;i<length;i++){	
			char a = 'c';
		something = something + a; 
		}
	}
  	return something;
	}

public static String generateRandomKey() {
	String password = "";
	for(int i=0;i<passowrdSize;i++) {
		password = password + rndChar();
	}
	return password;
}
private static char rndChar () {
    int rnd = (int) (Math.random() * 52); // or use Random or whatever
    char base = (rnd < 26) ? 'A' : 'a';
    return (char) (base + rnd % 26);

}

public static int generateT() {
	return Math.abs(new Random().nextInt()+1);
}


}
