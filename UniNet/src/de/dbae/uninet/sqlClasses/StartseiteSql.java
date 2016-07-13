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
		// Gibt alle Beitraege der letzten 100 Stunden zurueck
		case "Beitraege":
			sql = "SELECT VerfasserID, Vorname, Nachname, Nachricht, AnzahlLikes, AnzahlKommentare, BeitragsID, Datum, Uhrzeit, Sichtbarkeit, Bearbeitet, Bewertung FROM "
					+ "freundeView INNER JOIN beitragsView ON (freund = verfasserID) WHERE nutzer = ? AND "
					+ "(SELECT DATE_PART('day', ?::timestamp - datum::timestamp) * 24 + DATE_PART('hour', ?::time - uhrzeit::time)) < 100 ORDER BY beitragsID DESC";
			break;
		// Gibt an, ob der Beitrag vom Nutzer mit 'interessiert mich nicht besonders' markiert wurde
		case "Like":
			sql = "SELECT COUNT(beitragsID) FROM beitraglikes WHERE beitragsID = ? AND studentID = ?";
			break;
		// Gibt eine Liste mit allen Freunden zurueck, die momentan online sind
		case "Chatfreunde":
			sql = "SELECT Vorname, Nachname, userID FROM (FreundeView INNER JOIN Studenten ON Freund = StudentID INNER JOIN Nutzer ON StudentID = UserID) WHERE Nutzer = ? AND Nutzer != Freund AND Online = TRUE";
			break;
		// Gibt die Anzahl der ungelesenen Nachrichten an ? von ? zurueck
		case "AnzahlNachrichten":
			sql = "SELECT COUNT(empfaengerid) FROM nachrichten WHERE empfaengerid = ? AND senderid = ? AND gelesen = false;";
			break;
		default:
			System.err.println("FEHLER IN STARTSEITESQL " + action);
			break;
		}
		return sql;
	}

}
