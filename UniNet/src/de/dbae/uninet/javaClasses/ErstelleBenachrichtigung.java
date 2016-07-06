package de.dbae.uninet.javaClasses;

// Importe
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.dbae.uninet.sqlClasses.BenachrichtigungErstellenSql;

/**
 * Diese Klasse erzeugt Benachrichtigungen f&uuml;r alle betroffenen Benutzer.<br>
 * Sie wird in den Servlets aufgerufen, zum Beispiel beim Posten oder Kommentieren eines Beitrags
 * und gestaltet den Code der Servlets durch Auslagerung &uuml;bersichtlicher.
 * @author Christian Ackermann
 */
public class ErstelleBenachrichtigung {

	/**
	 * Die Sql-Klasse mit den verwendeten Statements.
	 */
	private BenachrichtigungErstellenSql sqlSt = new BenachrichtigungErstellenSql();
	
	/**
	 * Das spezielle Sql-Statement
	 */
	private String sql = "";
	
	/**
	 * Das PreparedStatement.
	 */
	private PreparedStatement pStmt;
	
	/**
	 * Das entstehende ResultSet.
	 */
	private ResultSet rs;
	
	/**
	 * Die beim Erzeugen &uuml;bergebene, offene Datenbankverbindung.
	 */
	private Connection con;

	/**
	 * Erstellt ein neues Objekt dieser Klasse.
	 * @param con Die offene Datenbankverbindung
	 */
	public ErstelleBenachrichtigung(Connection con) {
		this.con = con;
	}
	
	/**
	 * Gibt den Namen zu einer UserID zur&uuml;ck.
	 * @param userID Die UserID des entsprechenden Nutzers
	 * @return Der vollst&auml;ndige Name ohne Anrede
	 * @throws SQLException
	 */
	private String getName(int userID) throws SQLException {
		// Lade das Sql-Statement
		sql = sqlSt.getSqlStatement("NameDerBeteiligtenPerson");
		pStmt = con.prepareStatement(sql);
		// Setze die UserID
		pStmt.setInt(1, userID);
		rs = pStmt.executeQuery();
		// Setze den Default-Wert, falls etwas schief geht
		String name = "Jemand";
		if (rs.next()) {
			// Setze den Namen aus Vorname und Nachname zusammen
			name = rs.getString(1) + " " + rs.getString(2);
		}
		return name;
	}
	
	/**
	 * Erstellt eine Benachrichtung, wenn jemand auf einen Beitrag reagiert hat.<br>
	 * Entweder wurde der Beitrag kommentiert oder mit <i>Interessiert mich nicht besonders</i>
	 * markiert.
	 * @param userID Die ID des aktuellen Users
	 * @param beitragsID Die ID des Beitrags auf den reagiert wurde
	 * @param isLike Gibt an, ob es eine &quot;Like&quot; oder ein Kommentar war
	 * @throws SQLException
	 */
	public void beitragReaktion(int userID, int beitragsID, boolean isLike) throws SQLException {
		// Lade das Sql-Statement um die UserID des Beitragsverfassers zu bekommen
		sql = sqlSt.getSqlStatement("VerfasserBeitrag");
		pStmt = con.prepareStatement(sql);
		// Setze die BeitragsID
		pStmt.setInt(1, beitragsID);
		rs = pStmt.executeQuery();
		// DefaultWert fuer BeitragsID
		int verfasserID = -1;
		if (rs.next()) {
			// Lies die VerfasserID aus
			verfasserID = rs.getInt(1);
		}
		// Lade das Sql-Statement um eine neue Benachrichtigung anzulegen
		sql = sqlSt.getSqlStatement("BenachrichtigungAnlegen");
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, verfasserID);
		String benachrichtigung;
		// Wenn es ein Like war
		if (isLike) {
			benachrichtigung = " interessiert deinen Beitrag nicht besonders.";
		// Wenn es ein Kommentar war
		} else {
			benachrichtigung = " hat deinen Beitrag kommentiert.";
		}
		// Setze die Benachrichtigung an sich mit Link auf den Beitrag
		pStmt.setString(2, getName(userID) + benachrichtigung+ "<br><a class='blau' href='BeitragServlet?beitragsID=" + beitragsID + "'>Ansehen</a>");
		pStmt.executeUpdate();
	}
	
	/**
	 * Erstellt eine Benachrichtigung f&uuml;r alle Gruppen- bzw. Veranstaltungsmitglieder, wenn ein Beitrag
	 * in der Gruppe bzw. Veranstaltung gepostet wurde.
	 * @param verfasserID Die ID des Verfassers des Beitrags
	 * @param beitragsID Die ID des Beitrags
	 * @param id Die ID der Gruppe bzw. der Veranstaltung
	 * @param istGruppe Gibt an, ob es sich um eine Gruppe oder eine Veranstaltung handelt
	 * @throws SQLException
	 */
	public void gruppenveranstaltungenpost(int verfasserID, int beitragsID, int id, boolean istGruppe) throws SQLException {
		// Lade das entsprechende Sql-Statement
		if (istGruppe) {
			sql = sqlSt.getSqlStatement("Gruppenname");
		} else {
			sql = sqlSt.getSqlStatement("Veranstaltungsname");
		}
		pStmt = con.prepareStatement(sql);
		// Setze die ID
		pStmt.setInt(1, id);
		rs = pStmt.executeQuery();
		String name = "";
		if (rs.next()) {
			// Lies den Gruppen- bzw. Veranstaltungsnamen aus
			name = rs.getString(1);
		}
		// Ermittle den Namen des Verfassers
		String verfasserName = getName(verfasserID);
		// Lade das Sql-Statement fuer die Mitglieder
		if (istGruppe) {
			sql = sqlSt.getSqlStatement("Gruppenmitglieder");
		} else {
			sql = sqlSt.getSqlStatement("Veranstaltungsmitglieder");
		}
		PreparedStatement pStmt1 = con.prepareStatement(sql);
		// Setze die ID
		pStmt1.setInt(1, id);
		rs = pStmt1.executeQuery();
		// Informiere jedes Mitglied
		while (rs.next()) {
			int mitgliedID = rs.getInt(1);
			// Ausser den Verfasser selbst
			if (mitgliedID != verfasserID) {
				// Erstelle eine neue Benachrichtigung
				sql = sqlSt.getSqlStatement("BenachrichtigungAnlegen");
				PreparedStatement pStmt2 = con.prepareStatement(sql);
				// An das jeweilige Mitglied
				pStmt2.setInt(1, mitgliedID);
				String benachrichtigung = verfasserName + " hat etwas in " + name + " gepostet.<br><a class='blau' href='BeitragServlet?beitragsID=" + beitragsID + "'>Ansehen</a>";
				pStmt2.setString(2, benachrichtigung);
				pStmt2.executeUpdate();
			}
		}
	}
	
	/**
	 * Erstellt eine Benachrichtigung, wenn der Nutzer einer Gruppe hinzugef&uuml;gt wird.
	 * @param einladerID Die ID des Administrators
	 * @param gruppenID
	 * @param eingeladenerID
	 * @throws SQLException
	 */
	public void gruppeneinladung(int einladerID, int gruppenID, int eingeladenerID) throws SQLException {
		// Lade das entsprechende Sql-Statement
		sql = sqlSt.getSqlStatement("Gruppenname");
		PreparedStatement pStmt = con.prepareStatement(sql);
		// Setze die ID
		pStmt.setInt(1, gruppenID);
		rs = pStmt.executeQuery();
		String gruppenName = "";
		if (rs.next()) {
			// Lies den Gruppennamen aus
			gruppenName = rs.getString(1);
		}
		// Erstelle eine neue Benachrichtigung
		sql = sqlSt.getSqlStatement("BenachrichtigungAnlegen");
		PreparedStatement pStmt2 = con.prepareStatement(sql);
		// An den Eingeladenen
		pStmt2.setInt(1, eingeladenerID);
		String einladerName = getName(einladerID);
		String benachrichtigung = einladerName + " hat dich der Gruppe " + gruppenName + " hinzugefügt.<br><a class='blau' href='GruppenServlet?tab=beitraege&gruppenID=" + gruppenID + "'>Ansehen</a>";
		pStmt2.setString(2, benachrichtigung);
		pStmt2.executeUpdate();
	}
	
	public void gruppenAdmin(int userID, int gruppenID) throws SQLException {
		// Lade das entsprechende Sql-Statement
		sql = sqlSt.getSqlStatement("Gruppenname");
		pStmt = con.prepareStatement(sql);
		// Setze die ID
		pStmt.setInt(1, gruppenID);
		rs = pStmt.executeQuery();
		String gruppenName = "";
		if (rs.next()) {
			// Lies den Gruppennamen aus
			gruppenName = rs.getString(1);
		}
		// Erstelle eine neue Benachrichtigung
		sql = sqlSt.getSqlStatement("BenachrichtigungAnlegen");
		PreparedStatement pStmt2 = con.prepareStatement(sql);
		// An den neuen Admin
		pStmt2.setInt(1, userID);
		String benachrichtigung = "Du bist jetzt Administrator der Gruppe <a class='blau' href='GruppenServlet?tab=beitraege&gruppenID=" + gruppenID + "'>" + gruppenName + "</a>";
		pStmt2.setString(2, benachrichtigung);
		pStmt2.executeUpdate();
	}
	
	/**
	 * Erstellt eine Benachrichtigung &uuml;ber eine Freundschaftsanfrage.
	 * @param userID Die ID des Users, der die Einladung abgesendet hat
	 * @param freundID Die ID des Users, an den die Einladung geht
	 * @throws SQLException
	 */
	public void freundschaftsanfrage(int userID, int freundID) throws SQLException {
		// Lade das Sql-Statement
		sql = sqlSt.getSqlStatement("FreundschaftsanfrageAnlegen");
		pStmt = con.prepareStatement(sql);
		// Der moegliche neue Freund soll informiert werden
		pStmt.setInt(1, freundID);
		String benachrichtigung = "<a class='verfasser' href='ProfilServlet?userID=" + userID + "'>" + getName(userID) + "</a> m&ouml;chte mit dir befreundet sein!<br>";
		pStmt.setString(2, benachrichtigung);
		// Setze explizit den Hinweis, dass es sich bei dieser Benachrichtigung um eine
		// Freundschaftsanfrage handelt
		pStmt.setInt(3, 1);
		pStmt.executeUpdate();
	}
	
}
