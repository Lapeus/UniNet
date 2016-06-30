package de.dbae.uninet.javaClasses;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.dbae.uninet.sqlClasses.BenachrichtigungErstellenSql;

public class ErstelleBenachrichtigung {

	BenachrichtigungErstellenSql sqlSt = new BenachrichtigungErstellenSql();
	String sql = "";
	PreparedStatement pStmt;
	ResultSet rs;
	Connection con;

	public ErstelleBenachrichtigung(Connection con) {
		this.con = con;
	}
	
	private String getName(int userID) throws SQLException {
		sql = sqlSt.getSqlStatement("NameDerBeteiligtenPerson");
		pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, userID);
		rs = pStmt.executeQuery();
		String name = "Jemand";
		if (rs.next()) {
			name = rs.getString(1) + " " + rs.getString(2);
		}
		return name;
	}
	
	public void beitragReaktion(int userID, int beitragsID, boolean isLike) throws SQLException {
		sql = sqlSt.getSqlStatement("VerfasserBeitrag");
		pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, beitragsID);
		rs = pStmt.executeQuery();
		int verfasserID = -1;
		if (rs.next()) {
			verfasserID = rs.getInt(1);
		}
		sql = sqlSt.getSqlStatement("BenachrichtigungAnlegen");
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, verfasserID);
		String benachrichtigung;
		if (isLike) {
			benachrichtigung = " interessiert deinen Beitrag nicht besonders.";
		} else {
			benachrichtigung = " hat deinen Beitrag kommentiert.";
		}
		pStmt.setString(2, getName(userID) + benachrichtigung+ "<br><a class='blau' href='BeitragServlet?beitragsID=" + beitragsID + "'>Ansehen</a>");
		pStmt.executeUpdate();
	}
	
	/*public void gruppenveranstaltungenpost(int userID, int beitragsID, int id, boolean istGruppe) throws SQLException {
		sql = sqlSt.getSqlStatement("VerfasserBeitrag");
		pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, beitragsID);
		rs = pStmt.executeQuery();
		int verfasserID = -1;
		if (rs.next()) {
			verfasserID = rs.getInt(1);
		}
		if (istGruppe) {
			sql = sqlSt.getSqlStatement("Gruppenname");
		} else {
			sql = sqlSt.getSqlStatement("Veranstaltungsname");
		}
		
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, id);
		rs = pStmt.executeQuery();
		String name = "";
		if (rs.next()) {
			name = rs.getString(1);
		}
		sql = sqlSt.getSqlStatement("BenachrichtigungAnlegen");
		pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, verfasserID);
		
	}*/
	
	public void freundschaftsanfrage(int userID, int freundID) throws SQLException {
		sql = sqlSt.getSqlStatement("FreundschaftsanfrageAnlegen");
		pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, freundID);
		String benachrichtigung = "<a class='verfasser' href='ProfilServlet?userID=" + userID + "'>" + getName(userID) + "</a> m&ouml;chte mit dir befreundet sein!<br>";
		pStmt.setString(2, benachrichtigung);
		pStmt.setInt(3, 1);
		pStmt.executeUpdate();
	}
	
}
