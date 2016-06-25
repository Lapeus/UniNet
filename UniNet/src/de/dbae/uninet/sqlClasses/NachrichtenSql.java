package de.dbae.uninet.sqlClasses;

public class NachrichtenSql {
	public NachrichtenSql () {
		
	}
	
	public String getNachrichtenListe() {
		String sql = "SELECT vorname, nachname, nachricht, datum, uhrzeit FROM nachrichten JOIN nutzer ON (senderid = userid) WHERE senderid = ? AND empfaengerid = ? OR senderid = ? AND empfaengerid = ? ORDER BY nachrichtid;";
		return sql;
	}
	
	public String nachrichtSenden() {
		String sql = "INSERT INTO Nachrichten (senderID, empfaengerID, nachricht, datum, uhrzeit) VALUES (?, ?, ?, ?, ?)";
		return sql;
	}
	
	public String getFreundeSql() {
		String sql = "SELECT Vorname, Nachname, userID, online FROM (FreundeView INNER JOIN Studenten ON Freund = StudentID INNER JOIN Nutzer ON StudentID = UserID) WHERE Nutzer = ? AND Freund != ? ORDER BY online DESC, nachname, vorname";
		return sql;
	}
	
	public String getName() {
		String sql = "SELECT Vorname, Nachname FROM nutzer WHERE userid = ?";
		return sql;
	}
}
