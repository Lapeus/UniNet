package de.dbae.uninet.sqlClasses;
/**
 * Diese Klasse stellt alle SQL-Statements in Verbindung mit Suchergebnissen zur Verf&uuml;gung.<br>
 * @author Marvin Wolf
 */
public class SuchergebnisseSql {
	public SuchergebnisseSql() {
		
	}
	
	// SQL Methoden
	
	public String getNutzerSql() {
		return "SELECT userid, vorname, nachname, geburtstag, online  "
				+ "FROM (nutzer JOIN studenten ON (userid = studentid)) "
				+ "WHERE userid != ? "
				+ "AND uniid = ? "
				+ "AND REPLACE(CONCAT(vorname, nachname), ' ', '') ~* ? "
				+ "ORDER BY nachname;";
	}
	
	public String getGruppenSql() {
		return "SELECT gruppenid, name, beschreibung, gruendung, adminid "
				+ "FROM gruppen "
				+ "JOIN (SELECT studentid, uniid FROM studenten WHERE uniid = ?) as temp ON (adminid = studentid) "
				+ "WHERE REPLACE(CONCAT(name, beschreibung), ' ', '') ~* ? ORDER BY name;";
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
	
	public String isFreundSql() {
		return "SELECT COUNT(freund) FROM freundeView WHERE nutzer = ? AND freund = ?";
	}
	
	public String erstelleFreundschaftsanfrage() {
		return "INSERT INTO Benachrichtigungen (UserID, FreundID, Art) VALUES (?, ?, ?)";
	}
	
	public String loescheFreundschaft() {
		return "DELETE FROM freunde WHERE (freund1 = ? AND freund2 = ?) OR (freund2 = ? AND freund2 = ?);";
	}
	
	public String getNameZuID() {
		return "SELECT Vorname, Nachname FROM Nutzer WHERE UserID = ?";
	}
	
	public String getUniZuUser() {
		return "SELECT uniid FROM studenten WHERE studentid = ?";
	}
}


