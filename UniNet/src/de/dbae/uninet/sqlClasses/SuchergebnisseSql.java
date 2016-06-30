package de.dbae.uninet.sqlClasses;

public class SuchergebnisseSql {
	public SuchergebnisseSql() {
		
	}
	
	// SQL Methoden
	
	public String getNutzerSql() {
		return "SELECT userid, vorname, nachname FROM nutzer WHERE vorname LIKE ? OR nachname LIKE ? ORDER BY nachname;";
	}
}


