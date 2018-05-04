package services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import model.client;

public abstract class  service extends Thread  {
	protected  Socket client; 
	protected  BufferedReader in = null;
	protected PrintWriter out = null;
	public void setClient(Socket client) {
		this.client = client;
		try {
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(client.getOutputStream(),true);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		}

	}
	protected String readSomethingFromClient() {
		if(in == null) return null;
		String dataFromServer = null;
		while(dataFromServer == null) {
			try {
				dataFromServer = in.readLine();	
			} catch (IOException e) {
				// TODO Auto-generated catch block
			//	e.printStackTrace();
				return null;
					}
				}
		return dataFromServer;
		}

	protected void closeEveryThing() {
		try {
			if (in != null)
			in.close();
			if (out != null)
				out.close();
			if (client != null)
				client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		}
}

}
