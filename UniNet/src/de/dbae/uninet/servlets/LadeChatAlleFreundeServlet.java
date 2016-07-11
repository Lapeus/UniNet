package de.dbae.uninet.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import de.dbae.uninet.dbConnections.DBConnection;
import de.dbae.uninet.javaClasses.Student;
import de.dbae.uninet.sqlClasses.NachrichtenSql;

/**
 * Servlet stellt die Daten für die Freundesliste zur Verfuegung.
 * @author Marvin Wolf
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
	 * Stellt Alle Freunde fuer die Chatspalte zur Verfuegung
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		request.setAttribute("chatfreundeAlle", getChatfreunde(session));
		// Setze die UserID als Attribut des Request-Objektes
		request.setAttribute("id", Integer.parseInt(session.getAttribute("UserID").toString()));
	}

	/**
	 * Gibt die Daten fuer den Ausgewaehlten Nutzer Zurueck
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private List<Student> getChatfreunde(HttpSession session) {
		// Freunde (Online)
		DBConnection dbcon = new DBConnection();
		Connection con = dbcon.getCon();
		NachrichtenSql sqlSt = new NachrichtenSql();
		List<Student> chatfreunde = new ArrayList<Student>();
		try {
			String sql = sqlSt.getFreundeSql();
			PreparedStatement pStmt = con.prepareStatement(sql);
			int iUserId = Integer.parseInt(session.getAttribute("UserID").toString());
			pStmt.setInt(1, iUserId);
			pStmt.setInt(2, iUserId);
			System.out.println(pStmt.toString());
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				String vorname = rs.getString(1);
				String nachname = rs.getString(2);
				int userID = rs.getInt(3);
				boolean online = rs.getBoolean(4);
				System.out.println(vorname + " " + nachname);
				Student freund = new Student(vorname, nachname, userID, online);
				chatfreunde.add(freund);
			}
		} catch (Exception e) {
			System.out.println("SQL-Fehler ist aufgetreten (ChatFreunde)");
		} finally {
			try {
				if(dbcon != null) {
					dbcon.close();
				}
			} catch (Exception ignored) {
				ignored.printStackTrace();
			}
		}
		return chatfreunde;
	}
	
}
