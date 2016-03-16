package de.dbae.uninet.javaClasses;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import de.dbae.uninet.dbConnections.DBConnection;
import de.dbae.uninet.javaClasses.ChatFreund;
import de.dbae.uninet.sqlClasses.StartseiteSql;

public class LadeChatFreunde {
	
    public LadeChatFreunde() {
        super();
        // TODO Auto-generated constructor stub
    }
	
	public List<ChatFreund> getChatfreunde(HttpSession session) {
		// Freunde (Online)
		Connection con = new DBConnection().getCon();
		System.out.println("Verbindung wurde geöffnet (Startseite)");
		StartseiteSql sqlSt = new StartseiteSql();
		List<ChatFreund> chatfreunde = new ArrayList<ChatFreund>();
		try {
			String sql = sqlSt.getFreundeOnlineSql();
			PreparedStatement pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, Integer.parseInt(session.getAttribute("UserID").toString()));
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				String vorname = rs.getString(1);
				String nachname = rs.getString(2);
				int userID = rs.getInt(3);
				ChatFreund freund = new ChatFreund(vorname, nachname, userID);
				chatfreunde.add(freund);
			}
		} catch (Exception e) {
			System.out.println("SQL-Fehler ist aufgetreten (ChatFrende)");
		} finally {
			if (con != null) {
				try {
					con.close();
					System.out.println("Die Verbindung wurde erfolgreich beendet!");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return chatfreunde;
	}

}
