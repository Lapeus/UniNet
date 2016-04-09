package de.dbae.uninet.sqlClasses;

/**
 * Diese Klasse stellt alle SQL-Statements in Verbindung mit Gruppen zur Verf&uuml;gung.<br>
 * Mittels der Methode <b>getSqlStatement</b> l&auml;sst sich &uuml;ber den Parameter das entsprechende Statement abrufen.
 * @see #getSqlStatement(String)
 * @author Christian Ackermann
 */
public class GruppenSql {

	/**
	 * Diese Methode gibt das angeforderte SQL-Statement zur&uuml;ck.
	 * @param action Spezifiziert das geforderte Statement
	 * @return Das SQL-Statement
	 */
	public String getSqlStatement(String action) {
		String sql = "";
		// Unterscheidung der verschiedenen Statements
		switch (action) {
		// Legt eine neue Gruppe mit den entsprechenden Eigenschaften an
		case "GruppeGruenden":
			sql = "INSERT INTO gruppen(name, beschreibung, gruendung, adminID) VALUES (?, ?, ?, ?)";
			break;
		// Gibt die ID der neu gegruendeten Gruppe zurueck
		case "NeueGruppenID":
			sql = "SELECT MAX(gruppenID) FROM gruppen";
			break;
		// Loescht eine Gruppe ueber die gruppenID
		case "GruppeLoeschen":
			sql = "DELETE FROM gruppen WHERE gruppenID = ?";
			break;
		// Gibt saemtliche Beitraege aus der Gruppe mit allen Eigenschaften antichronologisch zurueck
		case "Beitraege":
			sql = "SELECT VerfasserID, Vorname, Nachname, Nachricht, AnzahlLikes, AnzahlKommentare, BeitragsID, Datum, Uhrzeit, Sichtbarkeit, Bearbeitet "
					+ "FROM (gruppenbeitraege INNER JOIN beitragsView USING (beitragsID)) LEFT JOIN freundeView ON (verfasserID = freund AND nutzer = ?) WHERE (nutzer IS NOT NULL OR sichtbarkeit = TRUE) AND gruppenID = ? ORDER BY beitragsID DESC";
			break;
		// Gibt alle Gruppen zurueck, in denen der Nutzer aktuell ist (fuer die linke Spalte)
		case "EigeneGruppen":
			sql = "SELECT Name, gruppenID FROM gruppenmitglieder INNER JOIN studenten USING (studentID) INNER JOIN gruppen USING (gruppenID) WHERE studentID = ?";
			break;
		// Gibt alle Infos der Gruppe zurueck
		case "Infos":
			sql = "SELECT Name, Beschreibung, Gruendung, Vorname, Nachname, AdminID FROM Gruppen INNER JOIN Nutzer ON (adminID = userID) WHERE gruppenID = ?";
			break;
		// Aendert die Beschreibung der angegebenen Gruppe (gruppenID)
		case "BeschreibungAendern":
			sql = "UPDATE gruppen SET beschreibung = ? WHERE gruppenID = ?";
			break;
		// Gibt alle aktuellen Mitglieder der angegebenen Gruppe zurueck
		case "Mitglieder":
			sql = "SELECT Vorname, Nachname, StudentID "
					+ "FROM gruppenmitglieder INNER JOIN nutzer ON studentID = userID WHERE gruppenID = ? ORDER BY ";
			break;
		// Fuegt einen Student einer Gruppe hinzu
		case "MitgliederZufuegen":
			sql = "INSERT INTO gruppenmitglieder VALUES (?, ?)";
			break;
		// Entfernt einen Studenten aus einer Gruppe (wird ebenfalls beim Verlassen aufgerufen)
		case "MitgliederEntfernen":
			sql = "DELETE FROM gruppenmitglieder WHERE gruppenID = ? AND studentID = ?";
			break;
		// Gibt alle Freunde aller Mitglieder zurueck, die noch nicht Teil der Gruppe sind
		// Man muss mindestens einen Freund in der Gruppe haben, um ebenfalls in die Gruppe gelangen zu koennen
		case "MoeglicheMitglieder":
			sql= "SELECT DISTINCT freund AS freundID, Vorname, Nachname FROM gruppenmitglieder "
					+ "INNER JOIN freundeview ON studentID = nutzer "
					+ "INNER JOIN nutzer ON freund = userID WHERE gruppenID = ?";
			break;
		case "IstAdmin":
			sql = "SELECT COUNT(gruppenID) FROM gruppen WHERE gruppenID = ? AND adminID = ?";
			break;
		case "EngsterFreundInGruppe":
			sql = "SELECT freund FROM freundeView "
					+ "INNER JOIN gruppenmitglieder ON (nutzer = ? AND gruppenID = ? AND freund = studentID) WHERE bewertung = "
					+ "(SELECT MAX(bewertung) FROM freundeView "
					+ "INNER JOIN gruppenmitglieder ON (nutzer = ? AND gruppenID = ? AND freund = studentID))";
			break;
		case "AdminIDSetzen":
			sql = "UPDATE gruppen SET adminID = ? WHERE gruppenID = ?";
			break;
		default:
			System.err.println("FEHLER IN GRUPPENSQL " + action);
			break;
		}
		return sql;
	}
	
}
