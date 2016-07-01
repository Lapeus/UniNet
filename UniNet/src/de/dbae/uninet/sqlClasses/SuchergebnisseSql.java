package de.dbae.uninet.sqlClasses;

public class SuchergebnisseSql {
	public SuchergebnisseSql() {
		
	}
	
	// SQL Methoden
	
	public String getNutzerSql() {
		return "SELECT userid, vorname, nachname FROM nutzer WHERE vorname LIKE ? OR nachname LIKE ? ORDER BY nachname;";
	}
	
	public String getGruppenSql() {
		return "SELECT gruppenid, name, beschreibung, gruendung, adminid FROM gruppen WHERE name LIKE ? OR beschreibung LIKE ? ORDER BY name;";
	}
	
	public String getNutzerZuId() {
		return "SELECT vorname, nachname FROM Nutzer WHERE userid = ?";
	}
}


