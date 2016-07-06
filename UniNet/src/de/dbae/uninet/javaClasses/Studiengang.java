package de.dbae.uninet.javaClasses;
/**
 * Klasse zur Verwaltung und Darstellung der Unis in der Uni-Übersicht
 * @author Leon Schaffert
 */
public class Studiengang {
	/**
	 * Die ID des Studiengangs.
	 */
	private int sgID;
	/**
	 * Der Name des Studiengangs.
	 */
	private String sgName;
	/**
	 * Der normale Konstruktor der Studiengang-Klasse
	 */
	public Studiengang (int sgID, String sgName) {
		this.sgID = sgID;
		this.sgName = sgName;
	}
	/**
	 * Gibt die ID des Studiengangs zur&uuml;ck
	 * @return Die ID des Studiengangs
	 */
	public int getStudiengangsID() {
		return sgID;
	}
	/**
	 * Gibt den Namen des Studiengangs zur&uuml;ck
	 * @return Der Name des Studiengangs
	 */
	public String getStudiengangsName() {
		return sgName;
	}
}
