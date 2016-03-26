package de.dbae.uninet.javaClasses;

public class Student {

	private String vorname = "";
	private String nachname = "";
	private int userID;
	private boolean online = false;
	
	public Student(String vorname, String nachname, int userID) {
		this(vorname, nachname, userID, false);
	}
	public Student(String vorname, String nachname, int userID, boolean online) {
		this.setVorname(vorname);
		this.setNachname(nachname);
		this.setUserID(userID);
		this.setOnline(online);
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

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}
	
	
}
