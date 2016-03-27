package de.dbae.uninet.servlets;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.dbae.uninet.dbConnections.DBConnection;

/**
 * Dieses Servlet verarbeit alle Anfragen die die Startseite betreffen.
 * @author Christian Ackermann
 *
 */
@WebServlet("/StartseiteServlet")
public class StartseiteServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StartseiteServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Chatfreunde
		new LadeChatFreundeServlet().setChatfreunde(request);
		
		// Oeffne eine neue DB-Verbindung
		DBConnection dbcon = new DBConnection();
		Connection con = dbcon.getCon();
		// Die ID des Nutzers, dessen Startseite angezeigt werden soll
		int userID = Integer.parseInt(request.getSession().getAttribute("UserID").toString());
		try {
			// Setze alle Beitraege die angezeigt werden soll als Attribut
			request.setAttribute("beitragList", new BeitragServlet().getBeitraege(request, con, "Startseite", userID));
			// Weiterleitung
			request.getRequestDispatcher("Startseite.jsp").forward(request, response);
		} catch (Exception e) {
			System.err.println("Fehler im StartseiteServlet");
			// TODO Fehler
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
