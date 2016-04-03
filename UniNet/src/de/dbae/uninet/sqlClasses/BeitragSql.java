package de.dbae.uninet.sqlClasses;

/**
 * Diese Klasse stellt alle SQL-Statements in Verbindung mit Beitr&auml;gen zur Verf&uuml;gung.<br>
 * Mittels der Methode <b>getSqlStatement</b> l&auml;sst sich &uuml;ber den Parameter das entsprechende Statement abrufen.
 * @see #getSqlStatement(String)
 * @author Christian Ackermann
 */
public class BeitragSql {
	
	/**
	 * Diese Methode gibt das angeforderte SQL-Statement zur&uuml;ck.
	 * @param action Spezifiziert das geforderte Statement
	 * @return Das SQL-Statement
	 */
	public String getSqlStatement(String action) {
		String sql = "";
		// Unterscheidung der verschiedenen Statements
		switch (action) {
		// Alle Informationen fuer den Beitrag
		case "Beitrag":
			sql = "SELECT VerfasserID, Vorname, Nachname, Nachricht, AnzahlLikes, AnzahlKommentare, Datum, Uhrzeit, Sichtbarkeit, Bearbeitet FROM beitragsView WHERE beitragsID = ?";
			break;
		// Loescht den entsprechenden Beitrag mittels beitragsID
		case "BeitragLoeschen":
			sql = "DELETE FROM beitraege WHERE beitragsID = ?";
			break;
		// Gibt - sofern vorhanden - den Namen und die GruppenID der Gruppe zurueck, in der der Beitrag gepostet wurde
		case "GruppenID":
			sql = "SELECT gruppenID, name FROM gruppen INNER JOIN gruppenbeitraege USING (gruppenID) WHERE beitragsID = ?";
			break;
		// Gibt - sofern vorhanden - den Namen und die VeranstaltungsID der Veranstaltung zurueck, in der der Beitrag gepostet wurde	
		case "VeranstaltungsID":
			sql = "SELECT veranstaltungsID, name FROM veranstaltungen INNER JOIN veranstaltungsbeitraege USING (veranstaltungsID) WHERE beitragsID = ?";
			break;
		// Fuegt einen Beitrag hinzu
		case "BeitragAnlegen1":
			sql = "INSERT INTO beitraege (beitrag, verfasserID, sichtbarkeit, datum, uhrzeit) VALUES (?, ?, ?, ?, ?)";
			break;
		// Gibt die ID des neuen Beitrags aus
		case "BeitragAnlegen2":
			sql = "SELECT MAX(beitragsID) FROM beitraege";
			break;
		// Speichert den Beitrag als Chronikbeitrag
		case "BeitragAnlegenChronik":
			sql = "INSERT INTO chronikbeitraege VALUES (?)";
			break;
		// Speichert den Beitrag als Veranstaltungsbeitrag
		case "BeitragAnlegenVeranstaltung":
			sql = "INSERT INTO veranstaltungsbeitraege VALUES (?, ?)";
			break;
		// Speichert den Beitrag als Gruppenbeitrag
		case "BeitragAnlegenGruppe":
			sql = "INSERT INTO gruppenbeitraege VALUES (?, ?)";
			break;
		// Aktualisiert den Beitrag und setzt das Bearbeitet-Attribut
		case "BeitragBearbeiten":
			sql = "UPDATE beitraege SET beitrag = ?, bearbeitet = TRUE WHERE beitragsID = ?";
			break;
		// Speichert, dass dieser Beitrag von einer entsprechenden Person (studentID) mit 'interessiert mich nicht besonders' markiert wurde
		case "BeitragLiken":
			sql = "INSERT INTO beitraglikes VALUES (?, ?)";
			break;
		// Loescht die Markierung 'interessiert mich nicht besonders' fuer die entsprechende Person (studentID)
		case "BeitragEntliken":
			sql = "DELETE FROM beitraglikes WHERE beitragsID = ? AND studentID = ?";
			break;
		// Meldet diesen Beitrag (mit studentID)
		case "BeitragMelden":
			sql = "INSERT INTO beitragMeldungen VALUES (?, ?)";
			break;
		// Gibt an, ob der Beitrag mit 'interessiert mich nicht besonders' markiert wurde
		case "Like":
			sql = "SELECT COUNT(beitragsID) FROM beitraglikes WHERE beitragsID = ? AND studentID = ?";
			break;
		// Gibt die Personen an, die diesen Beitrag mit 'interessiert mich nicht besonders' markiert haben
		case "LikesPersonen":
			sql = "SELECT userID, vorname, nachname FROM nutzer INNER JOIN beitragLikes ON userID=studentID WHERE beitragsID = ? ";
			break;
		// Fuegt einen Kommentar hinzu
		case "Kommentieren":
			sql = "INSERT INTO kommentare (beitragsID, kommentar, verfasserID, datum, uhrzeit) VALUES (?, ?, ?, ?, ?)";
			break;
		// Loescht einen Kommentar
		case "LoescheKommentar":
			sql = "DELETE FROM kommentare WHERE kommentarID = ?";
			break;
		// Fuegt einen Unterkommentar hinzu (Antwort auf einen Kommentar)
		case "Unterkommentieren":
			sql = "INSERT INTO unterkommentare (antwortAuf, kommentar, verfasserID, datum, uhrzeit) VALUES (?, ?, ?, ?, ?)";
			break;
		// Loescht einen Unterkommentar
		case "LoescheUnterkommentar":
			sql = "DELETE FROM unterkommentare WHERE kommentarID = ?";
			break;
		// Fuegt einen Kommentar zu einem Unterkommentar hinzu
		case "UnterkommentarKommentieren":
			sql = "INSERT INTO kommentareZuUnterkommentare (antwortAuf, kommentar, verfasserID, datum, uhrzeit) VALUES (?, ?, ?, ?, ?)";
			break;
		// Loescht einen Kommentar zu einem Unterkommentar
		case "LoescheKommentarZuUnterkommentar":
			sql = "DELETE FROM kommentareZuUnterkommentare WHERE kommentarID = ?";
			break;
		// Gibt alle Kommentare des Beitrags zurueck
		case "Kommentare":
			sql = "SELECT verfasserID, kommentarID, vorname, nachname, kommentar, datum, uhrzeit FROM kommentare INNER JOIN Nutzer ON verfasserID = userID WHERE beitragsID = ?";
			break;
		// Gibt alle Unterkommentare eines Kommentars zuruck
		case "Unterkommentare":
			sql = "SELECT verfasserID, kommentarID, vorname, nachname, kommentar, datum, uhrzeit FROM unterkommentare INNER JOIN Nutzer ON verfasserID = userID WHERE antwortAuf = ?";
			break;
		// Gibt alle Kommentare zu einem Unterkommentar zurueck
		case "KommentareZuUnterkommentaren":
			sql = "SELECT verfasserID, kommentarID, vorname, nachname, kommentar, datum, uhrzeit FROM kommentareZuUnterkommentare INNER JOIN Nutzer ON verfasserID = userID WHERE antwortAuf = ?";
			break;
		default:
			System.err.println("FEHLER IM BETRAGSQL " + action);
			break;
		}
		return sql;
	}
	
}
