package de.dbae.uninet.javaClasses;

import java.util.List;

public class Unterkommentar {

	private int userID;
	private int kommID;
	private String name;
	private String kommentar;
	private int antwortAufKommID;
	private List<KommentarZuUnterkommentar> kommentarList;
	private String timeStamp;
	
	public Unterkommentar(int userID, int kommID, String name, String kommentar, int antwortAufKommID, String timeStamp) {
		this.setUserID(userID);
		this.setKommID(kommID);
		this.setName(name);
		this.setKommentar(kommentar);
		this.setAntwortAufKommID(antwortAufKommID);
		this.setTimeStamp(timeStamp);
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
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

	public List<KommentarZuUnterkommentar> getKommentarList() {
		return kommentarList;
	}

	public void setKommentarList(List<KommentarZuUnterkommentar> kommentarList) {
		this.kommentarList = kommentarList;
	}

	public int getKommID() {
		return kommID;
	}

	public void setKommID(int kommID) {
		this.kommID = kommID;
	}

	public int getAntwortAufKommID() {
		return antwortAufKommID;
	}

	public void setAntwortAufKommID(int antwortAufKommID) {
		this.antwortAufKommID = antwortAufKommID;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
}
