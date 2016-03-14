package de.dbae.uninet.sqlClasses;

public class StartseiteSql {

	public StartseiteSql() {
	}
	
	public String getBeitraegeSql(String userID) {
		String sql = "SELECT Vorname, Nachname, Nachricht, AnzahlLikes, AnzahlKommentare FROM (beitragsView INNER JOIN freundeView ON VerfasserID = Nutzer) WHERE freund = ?;";
		return sql;
	}
}
