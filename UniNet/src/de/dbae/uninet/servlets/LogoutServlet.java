package de.dbae.uninet.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import de.dbae.uninet.dbConnections.DBConnection;
import de.dbae.uninet.sqlClasses.AnmeldeSql;

/**
 * Dieses Servlet loggt einen User beim Klick auf den Logout-Button aus.
 * @author Christian Ackermann
 */
@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogoutServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Die aktuelle Session
		HttpSession session = request.getSession();
		// Oeffne eine neue DB-Verbindung
		DBConnection dbcon = new DBConnection();
		Connection con = dbcon.getCon();
		// Diese Klasse stellt das Sql-Statement zur Verfuegung
		AnmeldeSql sqlSt = new AnmeldeSql();
		try {
			// Lade Sql-Statement um den User auf offline zu setzen
			String sql = sqlSt.getOfflineUpdate();
			PreparedStatement pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, Integer.parseInt(session.getAttribute("UserID").toString()));
			pStmt.executeUpdate();
			// Weiterleitung auf die Anmeldeseite
			response.sendRedirect("Anmeldung.jsp");
		} catch (NullPointerException npex) {
			response.sendRedirect("FehlerServlet?fehler=Session");
		} catch (SQLException sqlex) {
			response.sendRedirect("FehlerServlet?fehler=DBCon");
		} finally {
			// Verbindung schliessen
			dbcon.close();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
