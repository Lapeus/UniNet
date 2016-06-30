package de.dbae.uninet.sqlClasses;

/**
 * Diese Klasse stellt alle SQL-Statements in Verbindung mit Benachrichtigungen zur Verf&uuml;gung.<br>
 * Mittels der Methode <b>getSqlStatement</b> l&auml;sst sich &uuml;ber den Parameter das entsprechende Statement abrufen.
 * @see #getSqlStatement(String)
 * @author Christian Ackermann
 */
public class BenachrichtigungErstellenSql {
	
	/**
	 * Diese Methode gibt das angeforderte SQL-Statement zur&uuml;ck.
	 * @param action Spezifiziert das geforderte Statement
	 * @return Das SQL-Statement
	 */
	public String getSqlStatement(String action) {
		String sql = "";
		// Unterscheidung der verschiedenen Statements
		switch (action) {
		case "NameDerBeteiligtenPerson":
			sql = "SELECT Vorname, Nachname FROM Nutzer WHERE UserID = ?";
			break;
		case "VerfasserBeitrag":
			sql = "SELECT verfasserID FROM Beitraege WHERE beitragsID = ?";
			break;
		case "BenachrichtigungAnlegen":
			sql = "INSERT INTO Benachrichtigungen (UserID, Benachrichtigung) VALUES (?, ?)";
			break;
		case "FreundschaftsanfrageAnlegen":
			sql = "INSERT INTO Benachrichtigungen (UserID, Benachrichtigung, Art) VALUES (?, ?, ?)";
			break;
		case "Veranstaltungsname":
			sql = "SELECT Name FROM Veranstaltungen WHERE veranstaltungsid = ?";
			break;
		case "Gruppenname":
			sql = "SELECT Name FROM Gruppen WHERE gruppenID = ?";
			break;
		default:
			System.err.println("FEHLER IM BENACHRICHTIGUNGERSTELLENGSQL " + action);
			break;
		}
		return sql;
	}
	
}
