package de.dbae.uninet.sqlClasses;

public class SuchergebnisseSql {
	public SuchergebnisseSql() {
		
	}
	
	// SQL Methoden
	
	public String getNutzerSql() {
		return "SELECT userid, vorname, nachname FROM nutzer WHERE REPLACE(CONCAT(vorname, nachname), ' ', '') ~* ? ORDER BY nachname;";
	}
	
	public String getGruppenSql() {
		return "SELECT gruppenid, name, beschreibung, gruendung, adminid FROM gruppen WHERE REPLACE(CONCAT(name, beschreibung), ' ', '') ~* ? ORDER BY name;";
	}
	
	public String getVeranstaltungenSql() {
		String sql = "SELECT veranstaltungsid, name, beschreibung, dozent, semester "
				+ "FROM Veranstaltungen JOIN (SELECT uniid FROM studenten WHERE studentid = ?) AS temp ON (Veranstaltungen.uniid = temp.uniid) "
				+ "WHERE REPLACE(CONCAT(CONCAT(name, beschreibung), dozent), ' ', '') ~* ? "
				+ "ORDER BY name";
		return sql;
	}
	
	public String getNutzerZuId() {
		return "SELECT vorname, nachname FROM Nutzer WHERE userid = ?";
	}
}


