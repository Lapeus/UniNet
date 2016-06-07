package de.dbae.uninet.javaClasses;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.dbae.uninet.dbConnections.DBConnection;
import de.dbae.uninet.sqlClasses.BeziehungsSql;

/**
 * Diese Klasse stellt Methoden zur Verf&uuml;gung, um die Beziehungen eines Nutzers zu seinen Freunden zu bewerten.<br>
 * Die Bewertung basiert auf einer nat&uuml;rlichen Zahl zwischen 0 und 1000, wobei 0 f&uuml;r <b>keine Relevanz / nicht vorhandene Beziehung</b>
 * und 1000 f&uuml;r <b>sehr hohe Relevanz / enge Beziehung</b>
 * @author Christian Ackermann
 */
public class Beziehungsrechner {
	
	/**
	 * Die Datenbank-Verbindungsklasse.
	 */
	private DBConnection dbcon = new DBConnection();
	
	/**
	 * Die Datenbank-Verbindung.
	 */
	private Connection con = dbcon.getCon();
	
	/**
	 * Stellt die ben&ouml;tigten SQL-Statements zur Verf&uuml;gung.
	 */
	private BeziehungsSql sqlSt = new BeziehungsSql();
	
	/**
	 * Eine Map mit UserID - Freundschaftsbewertung.
	 */
	private Map<Integer, Integer> bewertungsMap = new HashMap<Integer, Integer>();
	
	/**
	 * Die ID des aktuellen Users.
	 */
	private int userID;

	/**
	 * Berechnet die Beziehung des Users zu all seinen Freunden anhand folgender Kriterien:<br><br>
	 * <b>Anzahl der Nachrichten:</b> Verh&auml;ltnis der Anzahl der Nachrichten zu dem Maximum der Anzahl der Nachrichten des Nutzers
	 * mit einem anderen Freund. Z&auml;hlt 15% der Bewertung.<br>
	 * <b>Anzahl der Nachrichten der letzten 30 Tage:</b> s.o. mit Einschr&auml;nkung auf die letzten 30 Tage. Z&auml;hlt 35% der Bewertung.<br>
	 * <b>Anzahl der gemeinsamen Freunde:</b> Verh&auml;ltnis der Anzahl der gemeinsamen Freunde zu dem Maximum der Anzahl der 
	 * gemeinsamen Freunde mit einem anderen des Nutzers. Z&auml;hlt 10% der Bewertung.<br>
	 * <b>Anzahl gemeinsamer Gruppen:</b> Analog zu oben. Z&auml;hlt 10% der Bewertung.<br>
	 * <b>Anzahl gemeinsamer Veranstaltungen:</b> Analog zu oben. Z&auml;hlt 10% der Bewertung.<br>
	 * <b>Anzahl Beitr&auml;ge mit Likes des Nutzers:</b> Analog zu oben mit Anzahl der Beitr&auml;ge von Freunden, die der Nutzer
	 * geliket hat. Z&auml;hlt 10% der Bewertung.<br>
	 * <b>Anzahl Beitr&auml;ge mit Kommentaren des Nutzers:</b> Analog zu den Beitr&auml;gen. Z&auml;hlt 5% der Bewertung.<br>
	 * <b>Anzahl Likes von Freunden auf Beitr&auml;ge des Nutzers:</b> Analog zu oben. Z&auml;hlt 3.5% der Bewertung.<br>
	 * <b>Anzahl Kommentare von Freunden auf Beitr&auml;ge des Nutzers:</b> Analog zu oben. Z&auml;hlt 1.5% der Bewertung.<br>
	 * @param userID Die ID des Users, dessen Freundschaften bewertet werden sollen
	 */
	public void setBeziehung(int userID) {
		this.userID = userID;
		try {
			// Die Kategorien, die ausschlaggebend fuer die Bewertung sind
			String[] kategorien = {"AnzahlNachrichten", "AnzahlNachrichten30Tage", "AnzahlGemeinsamerFreunde", 
					"AnzahlGemeinsamerGruppen", "AnzahlGemeinsamerVeranstaltungen", "AnzahlBeitragLikesSelbst", 
					"AnzahlKommentareSelbst", "AnzahlBeitragLikesFreund", "AnzahlKommentareFreund"};
			for (String kategorie : kategorien) {
				setWert(kategorie);
			}
			// Setze die Bewertung in der DB
			setBewertungInDB();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO Fehler
		} finally {
			// Verbindung schliessen
			dbcon.close();
		}
	}
	
	/**
	 * Berechnet und setzt den Wert der jeweiligen Kategorie.
	 * @param kategorie Das Kriterium 
	 * @throws SQLException
	 */
	private void setWert(String kategorie) throws SQLException {
		// Hole das SqlStatement
		String sql = sqlSt.getSqlStatement(kategorie);
		PreparedStatement pStmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		switch (kategorie) {
		case "AnzahlNachrichten30Tage":
			pStmt.setDate(1, new Date(System.currentTimeMillis()));
			pStmt.setInt(2, userID);
			break;
		case "AnzahlBeitragLikesSelbst":
		case "AnzahlKommentareSelbst":
		case "AnzahlBeitragLikesFreund":
		case "AnzahlKommentareFreund":
			pStmt.setInt(2, userID);
		default:
			pStmt.setInt(1, userID);
			break;
		}
		ResultSet rs = pStmt.executeQuery();
		int maximum = 1;
		// Bestimme das Maximum
		while (rs.next()) {
			if (rs.getInt(2) > maximum)
				maximum = rs.getInt(2);
		}
		// Setze das ResultSet zurueck an den Anfang
		rs.beforeFirst();
		// Durchlaufe das ResultSet erneut und setze die Bewertung fuer die entsprechende Kategorie
		while (rs.next()) {
			int id = rs.getInt(1);
			int bewertung = (int)(getFaktor(kategorie) * rs.getInt(2) / maximum);
			if (id != userID) {
				if (bewertungsMap.containsKey(id))
					bewertungsMap.put(id, bewertungsMap.get(id) + bewertung);
				else
					bewertungsMap.put(id, bewertung);
			}
		}
	}
	
	/**
	 * Gibt den Bewertungsfaktor f&uuml;r die jeweilige Kategorie zur&uuml;ck.
	 * @param kategorie Das Kriterium
	 * @return Den Faktor f&uuml;r die Bewertung
	 */
	private int getFaktor(String kategorie) {
		int faktor = 0;
		switch (kategorie) {
		case "AnzahlNachrichten":
			faktor = 150;
			break;
		case "AnzahlNachrichten30Tage":
			faktor = 350;
			break;
		case "AnzahlKommentareSelbst":
			faktor = 50;
			break;
		case "AnzahlBeitragLikesFreund":
			faktor = 35;
			break;
		case "AnzahlKommentareFreund":
			faktor = 15;
			break;
		default:
			faktor = 100;
			break;
		}
		return faktor;
	}
	
	/**
	 * Setzt alle Bewertungen in die DB ein.
	 * @throws SQLException
	 */
	private void setBewertungInDB() throws SQLException {
		String sql = sqlSt.getSqlStatement("EinfuegenVorbereiten");
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, userID);
		String sql2;
		PreparedStatement pStmt2;
		// Fuer alle Bewertungen
		for (Entry<Integer, Integer> e : bewertungsMap.entrySet()) {
			pStmt.setInt(2, e.getKey());
			ResultSet rs = pStmt.executeQuery();
			// Schauen, ob der Nutzer oder der Freund die Freunschaft begonnen hat
			if (rs.next())
				sql2 = sqlSt.getSqlStatement("SetBewertung1");
			else
				sql2 = sqlSt.getSqlStatement("SetBewertung2");
			pStmt2 = con.prepareStatement(sql2);
			pStmt2.setInt(1, e.getValue());
			pStmt2.setInt(2, userID);
			pStmt2.setInt(3, e.getKey());
			pStmt2.executeUpdate();
		}
	}
	
}
