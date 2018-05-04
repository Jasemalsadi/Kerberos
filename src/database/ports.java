package database;

public class ports {
	private ports() {
		
	}
	public static int ASport = 5050;  // Constant ports 
	public static int TGSport = 5500;
	public static int giveMePortForEndServer(String server) { // This function will return the port number according to the End server name
			Repository db = Repository.getSharedRepository();
			return db.giveMePortServerName(server);
	}
}
