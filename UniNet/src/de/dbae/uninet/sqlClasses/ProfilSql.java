package de.dbae.uninet.sqlClasses;

/**
 * Diese Klasse stellt alle SQL-Statements in Verbindung mit dem eigenen Profil zur Verf&uuml;gung.<br>
 * Mittels der Methode <b>getSqlStatement</b> l&auml;sst sich &uuml;ber den Parameter das entsprechende Statement abrufen.
 * @see #getSqlStatement(String)
 * @author Christian Ackermann
 */
public class ProfilSql {
	
	/**
	 * Diese Methode gibt das angeforderte SQL-Statement zur&uuml;ck.
	 * @param action Spezifiziert das geforderte Statement
	 * @return Das SQL-Statement
	 */
	public String getSqlStatement(String action) {
		String sql = "";
		// Unterscheidung der verschiedenen Statements
		switch (action) {
		// Gibt alle Chronikbeitraege des Studenten zurueck
		case "Beitraege":
			sql = "SELECT VerfasserID, Vorname, Nachname, Nachricht, AnzahlLikes, AnzahlKommentare, BeitragsID, Datum, Uhrzeit, Sichtbarkeit, Bearbeitet FROM beitragsView INNER JOIN chronikbeitraege USING (beitragsID) WHERE VerfasserID = ? ORDER BY beitragsID DESC";
			break;
		// Gibt an, ob der Beitrag vom Student mit 'interessiert mich nicht besonders' markiert wurde
		case "Like":
			sql = "SELECT COUNT(beitragsID) FROM beitraglikes WHERE beitragsID = ? AND studentID = ?";
			break;
		// Gibt alle relevanten Informationen des Studenten zurueck
		case "Infos":
			sql = "SELECT Vorname, Nachname, StudiengangName, Studienbeginn, Geburtstag, Wohnort, Hobbys, Interessen, UeberMich, Email FROM Nutzer INNER JOIN Studenten ON UserID = StudentID INNER JOIN Studiengaenge USING (StudiengangID) WHERE StudentID = ?";
			break;
		// Gibt alle Sichtbarkeiten der Informationen des Studenten zurueck
		case "Sichtbarkeiten":
			sql = "SELECT GeburtSichtbar, WohnortSichtbar, HobbysSichtbar, InteressenSichtbar, UeberMichSichtbar FROM Profilsichtbarkeiten WHERE StudentID = ?";
			break;
		// Gibt die Anzahl der Freunde zurueck
		case "AnzahlFreunde":
			sql = "SELECT COUNT(Freund) FROM freundeView WHERE Nutzer = ?";
			break;
		// Gibt die momentanen Werte der zu bearbeitenden Informationen zurueck
		case "InfosBearbeiten":
			sql = "SELECT Vorname, Nachname, Studienbeginn, Geburtstag, Wohnort, Hobbys, Interessen, UeberMich FROM Nutzer INNER JOIN Studenten ON UserID = StudentID WHERE StudentID = ?";
			break;
		// Gibt an, ob die beiden Nutzer befreundet sind
		case "Befreundet":
			sql = "SELECT Nutzer, Freund FROM Freundeview WHERE Nutzer = ? AND Freund = ?";
			break;
		// Aendert den Namen
		case "AendereNamen":
			sql = "UPDATE Nutzer SET vorname = ?, nachname = ? WHERE userID = ?";
			break;
		// Aendert die uebrigen Informationen
		case "AendereInfos":
			sql = "UPDATE Studenten SET Studienbeginn = ?, Geburtstag = ?, Wohnort = ?, Hobbys = ?, Interessen = ?, UeberMich = ? WHERE StudentID = ?";
			break;
		// Aendert die Sichtbarkeiten der Informationen
		case "AendereSichtbarkeiten":
			sql = "UPDATE Profilsichtbarkeiten SET GeburtSichtbar = ?, WohnortSichtbar = ?, HobbysSichtbar = ?, InteressenSichtbar = ?, UeberMichSichtbar = ? WHERE StudentID = ?";
			break;
		// Passwort aendern
		case "PasswortAendern":
			sql = "UPDATE Nutzer SET passwort = ?, salt = ? WHERE userID = ?";
			break;
		default:
			System.err.println("FEHLER IN PROFILSQL " + action);
			break;
		}
		return sql;
	}
	
}
