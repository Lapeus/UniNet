package de.dbae.uninet.javaClasses;

import java.util.List;

public class Beitrag {

	private int userID;
	private String name;
	private String timeStamp;
	private String nachricht;
	private int likes;
	private int kommentare;
	private int beitragsID;
	private boolean like;
	private List<Kommentar> kommentarList;
	private String loeschenErlaubt;
	private boolean nichtChronik = false;
	// Name der Gruppe oder der Veranstaltung, sofern vorhanden
	private String ortName = "";
	
	public Beitrag(int userID, String name, String timeStamp, String nachricht, int likes, int kommentare, int beitragsID, boolean like, String loeschenErlaubt) {
		this.setUserID(userID);
		this.setName(name);
		this.setTimeStamp(timeStamp);
		this.setNachricht(nachricht);
		this.setLikes(likes);
		this.setKommentare(kommentare);
		this.setBeitragsID(beitragsID);
		this.setLike(like);
		this.setLoeschenErlaubt(loeschenErlaubt);
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

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getNachricht() {
		return nachricht;
	}

	public void setNachricht(String nachricht) {
		this.nachricht = nachricht;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public int getKommentare() {
		return kommentare;
	}

	public void setKommentare(int kommentare) {
		this.kommentare = kommentare;
	}

	public int getBeitragsID() {
		return beitragsID;
	}

	public void setBeitragsID(int beitragsID) {
		this.beitragsID = beitragsID;
	}

	public boolean isLike() {
		return like;
	}

	public void setLike(boolean like) {
		this.like = like;
	}

	public List<Kommentar> getKommentarList() {
		return kommentarList;
	}

	public void setKommentarList(List<Kommentar> kommentarList) {
		this.kommentarList = kommentarList;
	}

	public String getLoeschenErlaubt() {
		return loeschenErlaubt;
	}

	public void setLoeschenErlaubt(String loeschenErlaubt) {
		this.loeschenErlaubt = loeschenErlaubt;
	}

	public boolean isNichtChronik() {
		return nichtChronik;
	}

	public void setNichtChronik(boolean nichtChronik) {
		this.nichtChronik = nichtChronik;
	}

	public String getOrtName() {
		return ortName;
	}

	public void setOrtName(String ortName) {
		this.ortName = ortName;
	}
	
}
