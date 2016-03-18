package de.dbae.uninet.javaClasses;

import java.util.ArrayList;
import java.util.List;

public class Kommentar {

	private int userID;
	private int kommID;
	private String name;
	private String kommentar;
	private List<Unterkommentar> kommentarList = new ArrayList<Unterkommentar>();
	private int zielID;
	private int userIDsession;
	private String timeStamp;
	
	public Kommentar(int userID, int kommID, String name, String kommentar, List<Unterkommentar> kommentarList, int zielID, int userIDsession, String timeStamp) {
		this.setUserID(userID);
		this.setKommID(kommID);
		this.setName(name);
		this.setKommentar(kommentar);
		this.setKommentarList(kommentarList);
		this.setZielID(zielID);
		this.setUserIDsession(userIDsession);
		this.setTimeStamp(timeStamp);
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public int getKommID() {
		return kommID;
	}

	public void setKommID(int kommID) {
		this.kommID = kommID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKommentar() {
		return kommentar;
	}

	public void setKommentar(String kommentar) {
		this.kommentar = kommentar;
	}

	public List<Unterkommentar> getKommentarList() {
		return kommentarList;
	}

	public void setKommentarList(List<Unterkommentar> kommentarList) {
		this.kommentarList = kommentarList;
	}

	public int getZielID() {
		return zielID;
	}

	public void setZielID(int zielID) {
		this.zielID = zielID;
	}

	public int getUserIDsession() {
		return userIDsession;
	}

	public void setUserIDsession(int userIDsession) {
		this.userIDsession = userIDsession;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
}
