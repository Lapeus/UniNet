package de.dbae.uninet.javaClasses;

public class ChatFreund {

	private String vorname = "";
	private String nachname = "";
	private int userID;
	
	public ChatFreund(String vorname, String nachname, int userID) {
		this.setVorname(vorname);
		this.setNachname(nachname);
		this.setUserID(userID);
	}

	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public String getNachname() {
		return nachname;
	}

	public void setNachname(String nachname) {
		this.nachname = nachname;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}
	
	
}
