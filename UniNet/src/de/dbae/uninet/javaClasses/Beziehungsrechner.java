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
 * Die Bewertung basiert auf einer natürlichen Zahl zwischen 0 und 1000, wobei 0 f&uuml;r <b>keine Relevanz / nicht vorhandene Beziehung</b>
 * und 1000 f&uuml;r <b>sehr hohe Relevanz / enge Beziehung</b>
 * @author Christian Ackermann
 */
public class Beziehungsrechner {
	
	private DBConnection dbcon = new DBConnection();
	private Connection con = dbcon.getCon();
	private BeziehungsSql sqlSt = new BeziehungsSql();
	private Map<Integer, Integer> bewertungsMap = new HashMap<Integer, Integer>();
	private int userID;

	public void setBeziehung(int userID) {
		this.userID = userID;
		try {
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
		System.out.println(kategorie);
		// Durchlaufe das ResultSet erneut und setze die Bewertung fuer die entsprechende Kategorie
		while (rs.next()) {
			int id = rs.getInt(1);
			int bewertung = (int)(getFaktor(kategorie) * rs.getInt(2) / maximum);
			if (bewertungsMap.containsKey(id))
				bewertungsMap.put(id, bewertungsMap.get(id) + bewertung);
			else
				bewertungsMap.put(id, bewertung);
			System.out.println("ID: " + id + "; Bewertung: " + bewertung);
		}
	}
	
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
	
	private void setBewertungInDB() throws SQLException {
		String sql = sqlSt.getSqlStatement("EinfuegenVorbereiten");
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, userID);
		String sql2;
		PreparedStatement pStmt2;
		for (Entry<Integer, Integer> e : bewertungsMap.entrySet()) {
			pStmt.setInt(2, e.getKey());
			ResultSet rs = pStmt.executeQuery();
			if (rs.next())
				sql2 = sqlSt.getSqlStatement("SetBewertung1");
			else
				sql2 = sqlSt.getSqlStatement("SetBewertung2");
			pStmt2 = con.prepareStatement(sql2);
			pStmt2.setInt(1, e.getValue());
			pStmt2.setInt(2, userID);
			pStmt2.setInt(3, e.getKey());
			pStmt2.executeUpdate();
			System.out.println("Geupdatet: " + e.getKey() + ": " + e.getValue());
		}
	}
	
}
