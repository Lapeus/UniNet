package de.dbae.uninet.javaClasses;

public class StartseitenBeitrag extends Beitrag {

	private long time;
	
	private int bewertung;
	
	public StartseitenBeitrag(long time, int bewertung, Beitrag beitrag) {
		super(beitrag.getUserID(), beitrag.getName(), beitrag.getTimeStamp(), beitrag.getNachricht(), beitrag.getLikes(), beitrag.getKommentare(), beitrag.getBeitragsID(), beitrag.isLike(), beitrag.getLoeschenErlaubt(), beitrag.isBearbeitet());
		this.setTime(time);
		this.setBewertung(bewertung);
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getBewertung() {
		return bewertung;
	}

	public void setBewertung(int bewertung) {
		this.bewertung = bewertung;
	}

}
