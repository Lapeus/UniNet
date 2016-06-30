package de.dbae.uninet.javaClasses;
/**
 * Klasse zur Verwaltung der Administratoren.
 * @author Leon Schaffert
 *
 */
public class Admin {
	/**
	 * Der Vorname des Admins
	 */
	private String adminVorname;
	/**
	 * Der Nachname des Admins
	 */
	private String adminNachname;
	/**
	 * Die verschiedenen Typen von Administratoren:
	 * 1: Systemadministrator - Verwaltet die LocalAdmins
	 * 2: LocalAdmins - Fügen ihrer zugewiesenen Uni neue Veranstaltungen, Gruppen und Studiengaenge hinzu.
	 *	  Kümmern sich um gemeldete Beiträge/User und entfernen sie ggfs. aus dem  Netzwerk.
	 */
	private int adminTyp;
	/**
	 * 
	 */
	private String zugehoerigeUni;
	/**
	 * 
	 */
	public Admin(String adminVorname, String adminNachname,int adminTyp,String zugehoerigeUni) {
		this.adminVorname = adminVorname;
		this.adminNachname = adminNachname;
		this.zugehoerigeUni = zugehoerigeUni;
		if (adminTyp != 1) {
			this.adminTyp = 2;
		} else {
			this.adminTyp = 1;
		}
	}
	/**
	 * Gibt den Namen des Administrators zur&uuml;ck.
	 * @return Den Namen des Admins
	 */
	public String getAdminVorname() {
		return adminVorname;
	}
	/**
	 * Setzt einen neuen Namen für den Administrator.
	 * @param adminName Der gewuenschte neue Name
	 */
	public void setAdminVorname(String adminVorname) {
		this.adminVorname = adminVorname;
	}
	/**
	 * Gibt den Typ des Administrators zur&uuml;ck.
	 * @return den Typ des Admins
	 */
	public int getAdminTyp() {
		return adminTyp;
	}
	/**
	 * Gibt den Nachnamen des Administrators zur&uuml;ck.
	 * @return
	 */
	public String getAdminNachname() {
		return adminNachname;
	}
	/**
	 * Setzt einen neuen Nachnamen f&uuml;r den Admin.
	 * @param adminNachname der neue Nachname
	 */
	public void setAdminNachname(String adminNachname) {
		this.adminNachname = adminNachname;
	}
	/**
	 * Gibt den Namen der zugeh&ouml;rigen Uni zur&uuml;ck.
	 * @return Den Namen der Uni
	 */
	public String getZugehoerigeUni() {
		return zugehoerigeUni;
	}

}
