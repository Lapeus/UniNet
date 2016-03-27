package de.dbae.uninet.sqlClasses;

/**
 * Diese Klasse stellt alle SQL-Statements in Verbindung mit Veranstaltungen zur Verf&uuml;gung.<br>
 * Mittels der Methode <b>getSqlStatement</b> l&auml;sst sich &uuml;ber den Parameter das entsprechende Statement abrufen.
 * @see #getSqlStatement(String)
 * @author Christian Ackermann
 */
public class VeranstaltungenSql {

	/**
	 * Diese Methode gibt das angeforderte SQL-Statement zur&uuml;ck.
	 * @param action Spezifiziert das geforderte Statement
	 * @return Das SQL-Statement
	 */
	public String getSqlStatement(String action) {
		String sql = "";
		// Unterscheidung der verschiedenen Statements
		switch (action) {
		// Gibt alle Beitraege aus der Veranstaltung zurueck
		case "Beitraege":
			sql = "SELECT VerfasserID, Vorname, Nachname, Nachricht, AnzahlLikes, AnzahlKommentare, BeitragsID, Datum, Uhrzeit, Sichtbarkeit "
					+ "FROM veranstaltungsbeitraege INNER JOIN beitragsView USING (beitragsID) WHERE veranstaltungsID = ? ORDER BY beitragsID DESC";
			break;
		// Gibt alle relevanten Infos der Veranstaltung zurueck
		case "Infos":
			sql = "SELECT Name, Semester, Dozent, Beschreibung, Sonstiges FROM veranstaltungen WHERE veranstaltungsID = ?";
			break;
		// Gibt alle Mitglieder der Veranstaltung zurueck
		case "Mitglieder":
			sql = "SELECT Vorname, Nachname, StudentID "
					+ "FROM veranstaltungsmitglieder INNER JOIN nutzer ON studentID = userID WHERE veranstaltungsID = ? ORDER BY ";
			break;
		// Gibt alle eigenen Veranstaltungen zuruck (fuer die linke Spalte)
		case "EigeneVeranstaltungen":
			sql = "SELECT veranstaltungsID, Name "
					+ "FROM veranstaltungsmitglieder INNER JOIN veranstaltungen USING (veranstaltungsID) WHERE studentID = ?"
					+ "ORDER BY Name";
			break;
		// Gibt alle Veranstaltungen zurueck, in denen man nicht eingetragen ist (fuer die Uebersicht)
		case "AlleVeranstaltungen":
			sql = "SELECT DISTINCT veranstaltungsID, Name "
					+ "FROM veranstaltungsmitglieder INNER JOIN veranstaltungen USING (veranstaltungsID) INNER JOIN studenten USING (uniID) "
					+ "WHERE studenten.studentID = ? ORDER BY Name";
			break;
		// Schreibt den Studenten in die entsprechende Veranstaltung ein
		case "Einschreiben":
			sql = "INSERT INTO veranstaltungsmitglieder VALUES (?, ?)";
			break;
		// Schreibt den Studenten aus der entsprechenden Veranstaltung aus
		case "Ausschreiben":
			sql = "DELETE FROM veranstaltungsmitglieder WHERE veranstaltungsID = ? AND studentID = ?";
			break;
		default:
			System.err.println("FEHLER IN VERANSTALTUNGENSQL " + action);
			break;
		}
		return sql;
	}
	
}
