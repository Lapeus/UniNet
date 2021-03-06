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

import de.dbae.uninet.javaClasses.Student;
import de.dbae.uninet.sqlClasses.StartseiteSql;

/**
 * Dieses Servlet l&auml;dt alle verf&uuml;gbaren Chatfreunde und setzt die Liste als Attribut f&uuml;r die Verwendung auf der jsp (rechte Spalte).
 * @author Christian Ackermann
 *
 */
@WebServlet("/LadeChatFreundeServlet")
public class LadeChatFreundeServlet extends HttpServlet{
	
 	private static final long serialVersionUID = 4636772916140807029L;

 	/**
     * @see HttpServlet#HttpServlet()
     */
	public LadeChatFreundeServlet() {
        super();
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Hier soll eigentlich nix passieren
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	/**
	 * Setzt eine Liste von verf&uuml;gbaren Chatfreunden als Attribut des Request-Objektes.
	 * @param request Das Request-Objekt
	 * @param response Das Response-Objekt
	 * @param con Die Datenbank-Verbindung
	 * @throws IOException
	 * @throws SQLException
	 * @throws NullPointerException
	 */
	public void setChatfreunde(HttpServletRequest request, HttpServletResponse response, Connection con) throws IOException, SQLException, NullPointerException {
		try {
			// Die aktuelle Session
			HttpSession session = request.getSession();
			// Stellt das Sql-Statement zur Verfuegung
			StartseiteSql sqlSt = new StartseiteSql();
			// Liste der Chatfreunde
			List<Student> chatfreunde = new ArrayList<Student>();
			// UserID
			int userID = Integer.parseInt(session.getAttribute("UserID").toString());
			// Lade das Sql-Statement um die Chatfreunde zu ermitteln
			String sql = sqlSt.getSqlStatement("Chatfreunde");
			PreparedStatement pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, userID);
			ResultSet rs = pStmt.executeQuery();
			// Fuer jeden Studenten / Chatfreund
			while (rs.next()) {
				String vorname = rs.getString(1);
				String nachname = rs.getString(2);
				int id = rs.getInt(3);
				// Lade die Anzahl der ungelesenen Nachrichten
				sql = sqlSt.getSqlStatement("AnzahlNachrichten");
				PreparedStatement pStmt2 = con.prepareStatement(sql);
				// Die Nachrichten an den Nutzer
				pStmt2.setInt(1, userID);
				// Von dem jeweiligen Studenten
				pStmt2.setInt(2, id);
				ResultSet rs2 = pStmt2.executeQuery();
				int anzahl = 0;
				if (rs2.next()) {
					anzahl = rs2.getInt(1);
				}
				// Lege einen neuen Studenten an und fuege ihn der Liste hinzu
				chatfreunde.add(new Student(vorname, nachname, id, true, anzahl));
			}
			// Setze die Liste als Attribut des Request-Objektes
			request.setAttribute("chatfreunde", chatfreunde);
			// Setze die UserID als Attribut des Request-Objektes
			request.setAttribute("id", userID);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}

}
