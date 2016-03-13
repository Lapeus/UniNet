package de.dbae.uninet.javaClasses;

public class Beitrag {

	private String name;
	private String timeStamp;
	private String nachricht;
	private int likes;
	private int kommentare;
	
	public Beitrag(String name, String timeStamp, String nachricht, int likes, int kommentare) {
		this.setName(name);
		this.setTimeStamp(timeStamp);
		this.setNachricht(nachricht);
		this.setLikes(likes);
		this.setKommentare(kommentare);
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
	
}
