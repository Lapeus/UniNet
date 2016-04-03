package de.dbae.uninet.sqlClasses;

/**
 * Diese Klasse stellt alle SQL-Statements in Verbindung mit der Startseite zur Verf&uuml;gung.<br>
 * Mittels der Methode <b>getSqlStatement</b> l&auml;sst sich &uuml;ber den Parameter das entsprechende Statement abrufen.
 * @see #getSqlStatement(String)
 * @author Christian Ackermann
 */
public class StartseiteSql {
	
	/**
	 * Diese Methode gibt das angeforderte SQL-Statement zur&uuml;ck.
	 * @param action Spezifiziert das geforderte Statement
	 * @return Das SQL-Statement
	 */
	public String getSqlStatement(String action) {
		String sql = "";
		// Unterscheidung der verschiedenen Statements
		switch (action) {
		// Gibt alle relevanten Beitraege zurueck
		case "Beitraege":
			sql = "SELECT VerfasserID, Vorname, Nachname, Nachricht, AnzahlLikes, AnzahlKommentare, BeitragsID, Datum, Uhrzeit, Sichtbarkeit, Bearbeitet FROM "
					+ "(beitragsView INNER JOIN freundeView ON VerfasserID = Nutzer) WHERE freund = ? ORDER BY beitragsID";
			break;
		// Gibt an, ob der Beitrag vom Nutzer mit 'interessiert mich nicht besonders' markiert wurde
		case "Like":
			sql = "SELECT COUNT(beitragsID) FROM beitraglikes WHERE beitragsID = ? AND studentID = ?";
			break;
		// Gibt eine Liste mit allen Freunden zurueck, die momentan online sind
		case "Chatfreunde":
			sql = "SELECT Vorname, Nachname, userID FROM (FreundeView INNER JOIN Studenten ON Freund = StudentID INNER JOIN Nutzer ON StudentID = UserID) WHERE Nutzer = ? AND Online = TRUE";
			break;
		default:
			System.err.println("FEHLER IN STARTSEITESQL " + action);
			break;
		}
		return sql;
	}

}
