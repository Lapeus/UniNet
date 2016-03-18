package de.dbae.uninet.javaClasses;

public class KommentarZuUnterkommentar {

	private int userID;
	private int kommID;
	private String name;
	private String kommentar;
	private String antwortAufKommName;
	private int antwortAufKommID;
	private String timeStamp;
	
	public KommentarZuUnterkommentar(int userID, int kommID, String name, String kommentar, String antwortAufKommName, int antwortAufKommID, String timeStamp) {
		this.setUserID(userID);
		this.setKommID(kommID);
		this.setName(name);
		this.setKommentar(kommentar);
		this.setAntwortAufKommName(antwortAufKommName);
		this.setAntwortAufKommID(antwortAufKommID);
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

	public String getAntwortAufKommName() {
		return antwortAufKommName;
	}

	public void setAntwortAufKommName(String antwortAufKommName) {
		this.antwortAufKommName = antwortAufKommName;
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
