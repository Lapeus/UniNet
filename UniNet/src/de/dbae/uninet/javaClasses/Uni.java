package de.dbae.uninet.javaClasses;
/**
 * Klasse zur Verwaltung und Darstellung der Unis in der Uni-Übersicht
 * @author Leon Schaffert
 */
public class Uni {
	/**
	 * Die ID der Uni.
	 */
	private int uniID;
	/**
	 * Der Name der Uni.
	 */
	private String uniName;
	/**
	 * Der Standort der Uni.
	 */
	private String uniStandort;
	/**
	 * Der normale Konstruktor der Uni-Klasse
	 */
	public Uni (int uniID, String uniName, String uniStandort) {
		this.uniID = uniID;
		this.uniName = uniName;
		this.uniStandort = uniStandort;
	}
	/**
	 * Gibt die ID der Uni zur&uuml;ck
	 * @return Die ID der Uni
	 */
	public int getUniID() {
		return uniID;
	}
	/**
	 * Gibt den Namen der Uni zur&uuml;ck
	 * @return Der Name der Uni
	 */
	public String getUniName() {
		return uniName;
	}
	/**
	 * Gibt den Standort der Uni zur&uuml;ck
	 * @return Der Standort der Uni
	 */
	public String getUniStandort() {
		return uniStandort;
	}
}
