package de.dbae.uninet.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import de.dbae.uninet.dbConnections.DBConnection;
import de.dbae.uninet.javaClasses.ChatFreund;
import de.dbae.uninet.sqlClasses.NachrichtenSql;

/**
 * Servlet implementation class LadeChatAlleFreunde
 */
@WebServlet("/LadeChatAlleFreundeServlet")
public class LadeChatAlleFreundeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LadeChatAlleFreundeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		request.setAttribute("chatfreundeAlle", getChatfreunde(session));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	public List<ChatFreund> getChatfreunde(HttpSession session) {
		// Freunde (Online)
		Connection con = new DBConnection().getCon();
		System.out.println("Verbindung wurde geöffnet (Startseite)");
		NachrichtenSql sqlSt = new NachrichtenSql();
		List<ChatFreund> chatfreunde = new ArrayList<ChatFreund>();
		try {
			String sql = sqlSt.getFreundeSql();
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
			for (ChatFreund chatFreund : chatfreunde) {
				System.out.println(chatFreund.getNachname());
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
		System.out.println("Chatfreunde: " + chatfreunde);
		return chatfreunde;
	}
}
