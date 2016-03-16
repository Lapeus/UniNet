package de.dbae.uninet.sqlClasses;

public class NachrichtenSql {
	public NachrichtenSql () {
		
	}
	
	public String getNachrichtenListe() {
		String sql = "SELECT vorname, nachname, nachricht FROM nachrichten INNER JOIN (SELECT userid AS userid1, vorname AS EmpfaengerVorname, nachname AS EmpfaengerNachname FROM nutzer) AS temp ON empfaengerid = userid1 INNER JOIN nutzer ON senderid = userid WHERE Empfaengerid =? OR Senderid=? ORDER BY nachrichtid";
		return sql;
	}
	
	public String nachrichtSenden() {
		String sql = "INSERT INTO Nachrichten (senderID, empfaengerID, nachricht) VALUES (?, ?, ?)";
		return sql;
	}
}
