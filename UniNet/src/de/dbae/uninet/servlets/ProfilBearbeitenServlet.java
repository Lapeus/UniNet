package de.dbae.uninet.servlets;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Random;
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
				
				// Lade Sql-Statement um die Sichtbarkeiten zu laden
				sql = sqlSt.getSqlStatement("Sichtbarkeiten");
				pStmt = con.prepareStatement(sql);
				pStmt.setInt(1, userID);
				ResultSet rs2 = pStmt.executeQuery();
				if (rs2.next()) {
					// Sichtbarkeiten laden
					request.setAttribute("geburtSichtbar", rs2.getBoolean(1));
					request.setAttribute("wohnortSichtbar", rs2.getBoolean(2));
					request.setAttribute("hobbysSichtbar", rs2.getBoolean(3));
					request.setAttribute("interessenSichtbar", rs2.getBoolean(4));
					request.setAttribute("ueberMichSichtbar", rs2.getBoolean(5));
					// Weiterleitung
					request.getRequestDispatcher("ProfilBearbeiten.jsp").forward(request, response);
				}
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
			// Default-Wert ist 1
			int semester = 1;
			try {
				semester = Integer.parseInt(request.getParameter("semester"));
			} catch (NumberFormatException nfex) {
			}
			// Formatierung des Geburtstags
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
			Date date = null;
			try {
				date = new Date(sdf.parse(request.getParameter("geburtstag").toString()).getTime());
			} catch (Exception e) {
				// Default-Wert ist 01.01.1994
				date = new Date(757378800000L);
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
			// Lade SQL-Statement um die Sichtbarkeiten zu aendern
			sql = sqlSt.getSqlStatement("AendereSichtbarkeiten");
			pStmt = con.prepareStatement(sql);
			boolean geburtSichtbar = request.getParameter("radGeburt").equals("oeffentlich");
			boolean wohnortSichtbar = request.getParameter("radWohnort").equals("oeffentlich");
			boolean hobbysSichtbar = request.getParameter("radHobbys").equals("oeffentlich");
			boolean interessenSichtbar = request.getParameter("radInteressen").equals("oeffentlich");
			boolean ueberMichSichtbar = request.getParameter("radUeberMich").equals("oeffentlich");
			pStmt.setBoolean(1, geburtSichtbar);
			pStmt.setBoolean(2, wohnortSichtbar);
			pStmt.setBoolean(3, hobbysSichtbar);
			pStmt.setBoolean(4, interessenSichtbar);
			pStmt.setBoolean(5, ueberMichSichtbar);
			pStmt.setInt(6, userID);
			pStmt.executeUpdate();
			// Passwoerter
			String password1 = request.getParameter("password1").toString();
			String password2 = request.getParameter("password2").toString();
			if (password1.equals(password2) && !password1.equals("")) {
				String hash = "";
				String salt = "";
				try {
					MessageDigest digest = MessageDigest.getInstance("MD5");
					Random random = new Random();
					byte[] salt2 = new byte[16];
					random.nextBytes(salt2);
					password1 += salt2;
					digest.update(password1.getBytes(), 0, password1.length());
					hash = new BigInteger(1, digest.digest()).toString();
					salt = salt2.toString();
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String passwortSql = sqlSt.getSqlStatement("PasswortAendern");
				PreparedStatement pStmt2 = con.prepareStatement(passwortSql);
				pStmt2.setString(1, hash);
				pStmt2.setString(2, salt);
				pStmt2.setInt(3, userID);
				pStmt2.executeUpdate();
				// Weiterleitung
				response.sendRedirect("ProfilServlet?userID=" + userID);
			} else if (password1.equals("")) {
				// Weiterleitung
				response.sendRedirect("ProfilServlet?userID=" + userID);
			} else {
				// Passwoerter neu eingeben
				response.sendRedirect("ProfilBearbeitenServlet");
			}
		} catch (NullPointerException npe) {
			npe.printStackTrace();
			response.sendRedirect("FehlerServlet?fehler=Session");
		} catch (SQLException sqlex) {
			response.sendRedirect("FehlerServlet?fehler=DBCon");
		} finally {
			// Verbindung schliessen
			dbcon.close();
		}
	}

}
