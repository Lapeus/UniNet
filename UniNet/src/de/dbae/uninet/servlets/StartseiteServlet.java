package de.dbae.uninet.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.dbae.uninet.dbConnections.DBConnection;
import de.dbae.uninet.javaClasses.Beziehungsrechner;
import de.dbae.uninet.javaClasses.Semesterrechner;

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
		// Die ID des Nutzers, dessen Startseite angezeigt werden soll
		int userID = Integer.parseInt(request.getSession().getAttribute("UserID").toString());
		
		// Oeffne eine neue DB-Verbindung
		DBConnection dbcon = new DBConnection();
		Connection con = dbcon.getCon();
		try {
			// Setze alle Beitraege die angezeigt werden soll als Attribut
			request.setAttribute("beitragList", new BeitragServlet().getBeitraege(request, con, "Startseite", userID));
			// Weiterleitung
			request.getRequestDispatcher("Startseite.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
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
