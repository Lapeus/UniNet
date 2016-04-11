package de.dbae.uninet.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.dbae.uninet.dbConnections.DBConnection;
import de.dbae.uninet.javaClasses.Beitrag;
import de.dbae.uninet.javaClasses.StartseitenBeitrag;

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
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//is client behind something?
		String ipAddress = request.getHeader("X-FORWARDED-FOR");  
		if (ipAddress == null) {  
			ipAddress = request.getRemoteAddr();  
		}
		System.out.println(ipAddress);
		// Oeffne eine neue DB-Verbindung
		DBConnection dbcon = new DBConnection();
		Connection con = dbcon.getCon();
		try {
			// Chatfreunde
			new LadeChatFreundeServlet().setChatfreunde(request, response, con);
			// Die ID des Nutzers, dessen Startseite angezeigt werden soll
			int userID = Integer.parseInt(request.getSession().getAttribute("UserID").toString());
			// Setze alle Beitraege die angezeigt werden soll als Attribut
			request.setAttribute("beitragList", getBeitraege(request, con, userID));
			// Weiterleitung
			request.getRequestDispatcher("Startseite.jsp").forward(request, response);
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

	private List<Beitrag> getBeitraege(HttpServletRequest request, Connection con, int userID) throws SQLException {
		List<Beitrag> beitraege = new ArrayList<Beitrag>();
		BeitragServlet bs = new BeitragServlet();
		// Beitraege
		beitraege = bs.getBeitraege(request, con, "Startseite", userID);
		List<StartseitenBeitrag> sBeitraege = new ArrayList<StartseitenBeitrag>();
		// Zeitrelevanz berechnen und addieren
		long time = System.currentTimeMillis();
		// Fuer jeden Beitrag
		for (Beitrag b : beitraege) {
			// Caste den Beitrag
			StartseitenBeitrag beitrag = (StartseitenBeitrag)b;
			// Berechne die Differenz in Stunden zwischen Anlegen des Beitrags und jetzt
			double diff = (time - beitrag.getTime()) * 1.0 / 1000 / 60 / 60;
			// Quadratischer Zusammenhang zwischen Alter in Stunden (0-100) und Punkten (0-500)
			int bewertung = (int)(0.05 * Math.pow(diff - 100, 2));
			bewertung = bewertung < 0 ? 0 : bewertung;
			// Addiere die Zeitrelevanz auf die Freundesrelevanz
			beitrag.setBewertung(beitrag.getBewertung() + bewertung);
			sBeitraege.add(beitrag);
		}
		// Sortiere die Beitraege absteigend nach Bewertung
		Collections.sort(sBeitraege, new Comparator<StartseitenBeitrag>() {
			@Override
			public int compare(StartseitenBeitrag sb1, StartseitenBeitrag sb2) {
				return sb1.getBewertung() > sb2.getBewertung() ? -1 : 1;
			}
		});
		// Nimm die 20 relevantesten der letzten 100 Stunden
		sBeitraege = sBeitraege.subList(0, sBeitraege.size() > 19 ? 19 : sBeitraege.size());
		// Sortiere diese wieder antichronologisch
		Collections.sort(sBeitraege, new Comparator<StartseitenBeitrag>() {
			@Override
			public int compare(StartseitenBeitrag sb1, StartseitenBeitrag sb2) {
				return sb1.getTime() > sb2.getTime() ? -1 : 1;
			}
		});
		beitraege.clear();
		// Fuege sie der urspruenglichen Liste hinzu
		beitraege.addAll(sBeitraege);
		return beitraege;
	}
}