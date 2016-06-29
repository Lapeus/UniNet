package de.dbae.uninet.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import de.dbae.uninet.dbConnections.DBConnection;
import de.dbae.uninet.javaClasses.Semesterrechner;
import de.dbae.uninet.sqlClasses.ProfilSql;

/**
 * Dieses Servlet sorgt f&uuml;r die Bearbeitung eines Nutzer-Profils.
 * @author Christian Ackermann
 */
@WebServlet("/ProfilBearbeitenServlet")
@MultipartConfig
public class ProfilBearbeitenServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Die aktuelle Session.
	 */
	private HttpSession session;
	
	/**
	 * Die DB-Verbindung.
	 */
	private Connection con = null;
	
	/**
	 * Stellt die ben&ouml;tigten Sql-Statements zur Verf&uuml;gung.
	 */
	private ProfilSql sqlSt = new ProfilSql();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProfilBearbeitenServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Setze die aktuelle Session
		session = request.getSession();
		// DB-Verbindung oeffnen
		DBConnection dbcon = new DBConnection();
		con = dbcon.getCon();
		try {
			// Chatfreunde
			new LadeChatFreundeServlet().setChatfreunde(request, response, con);
			// Die ID des Nutzers, dessen Profil bearbeitet werden soll
			int userID = Integer.parseInt(session.getAttribute("UserID").toString());
			// Lade Sql-Statement um die zu bearbeitenden Infos zu laden
			String sql = sqlSt.getSqlStatement("InfosBearbeiten");
			PreparedStatement pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, userID);
			ResultSet rs = pStmt.executeQuery();
			if (rs.next()) {
				// Aktueller Vorname
				request.setAttribute("vorname", rs.getString(1));
				// Aktueller Nachname
				request.setAttribute("nachname", rs.getString(2));
				// Aktuelles Semester
				request.setAttribute("semester", new Semesterrechner().getSemester(rs.getDate(3).getTime()));
				// Formatierung des aktuellen Geburtstages
				SimpleDateFormat sdf = new SimpleDateFormat("d.MM.yyyy");
				String date = "";
				try {
					date = sdf.format(new Date(rs.getDate(4).getTime()));
				} catch (Exception e){}
				request.setAttribute("geburtstag", date);
				// Aktueller Wohnort
				request.setAttribute("wohnort", rs.getString(5));
				// Aktuelle Hobbys
				request.setAttribute("hobbys", rs.getString(6));
				// Aktuelle Interessen
				request.setAttribute("interessen", rs.getString(7));
				// Aktuelle Aussage ueber den Nutzer selbst
				request.setAttribute("ueberMich", rs.getString(8));
				
				// Sichtbarkeiten laden
				request.setAttribute("geburtsichtbar", rs.getBoolean(9));
				request.setAttribute("wohnortsichtbar", rs.getBoolean(10));
				request.setAttribute("hobbyssichtbar", rs.getBoolean(11));
				request.setAttribute("interessenichtbar", rs.getBoolean(12));
				request.setAttribute("uebermichsichtbar", rs.getBoolean(13));
				// Weiterleitung
				request.getRequestDispatcher("ProfilBearbeiten.jsp").forward(request, response);
			}
		} catch (NullPointerException npe) {
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
		// Setze aktuelle Session
		session = request.getSession();
		// Neue DB-Verbindung oeffnen
		DBConnection dbcon = new DBConnection();
		con = dbcon.getCon();
		try {
			// Die ID des Nutzers, dessen Profil bearbeitet werden soll
			int userID = Integer.parseInt(session.getAttribute("UserID").toString());
			// Lade Sql-Statement um den Namen zu aendern
			String sql = sqlSt.getSqlStatement("AendereNamen");
			PreparedStatement pStmt = con.prepareStatement(sql);
			pStmt.setString(1, request.getParameter("vorname").toString());
			pStmt.setString(2, request.getParameter("nachname").toString());
			pStmt.setInt(3, userID);
			pStmt.executeUpdate();
			// Lade Sql-Statement um die restlichen Infos zu aendern
			sql = sqlSt.getSqlStatement("AendereInfos");
			pStmt = con.prepareStatement(sql);
			int semester = Integer.parseInt(request.getParameter("semester"));
			// Formatierung des Geburtstags
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
			Date date = null;
			try {
				date = new Date(sdf.parse(request.getParameter("geburtstag").toString()).getTime());
			} catch (Exception e) {
			}
			String wohnort = request.getParameter("wohnort").toString();
			String hobbys = request.getParameter("hobbys").toString();
			String interessen = request.getParameter("interessen").toString();
			String ueberMich = request.getParameter("ueberMich").toString();
			pStmt.setDate(1, new Date(new Semesterrechner().getStudienbeginn(semester)));
			pStmt.setDate(2, date);
			pStmt.setString(3, wohnort);
			pStmt.setString(4, hobbys);
			pStmt.setString(5, interessen);
			pStmt.setString(6, ueberMich);
			pStmt.setInt(7, userID);
			pStmt.executeUpdate();
			// Weiterleitung
			response.sendRedirect("ProfilServlet?userID=" + userID);
		} catch (NullPointerException npe) {
			response.sendRedirect("FehlerServlet?fehler=Session");
		} catch (SQLException sqlex) {
			response.sendRedirect("FehlerServlet?fehler=DBCon");
		} finally {
			// Verbindung schliessen
			dbcon.close();
		}
	}

}
