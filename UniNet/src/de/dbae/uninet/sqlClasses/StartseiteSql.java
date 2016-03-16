package de.dbae.uninet.sqlClasses;

public class StartseiteSql {

	public StartseiteSql() {
	}
	
	public String getBeitraegeSql() {
		String sql = "SELECT VerfasserID, Vorname, Nachname, Nachricht, AnzahlLikes, AnzahlKommentare, BeitragsID FROM (beitragsView INNER JOIN freundeView ON VerfasserID = Nutzer) WHERE freund = ?;";
		return sql;
	}
	
	
	public String getFreundeOnlineSql () {
		String sql = "SELECT Vorname, Nachname, userID FROM (FreundeView INNER JOIN Studenten ON Freund = StudentID INNER JOIN Nutzer ON StudentID = UserID) WHERE Nutzer = ? AND Online = TRUE";
		return sql;
	}
}
