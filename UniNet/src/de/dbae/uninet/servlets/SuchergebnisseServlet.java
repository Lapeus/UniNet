package de.dbae.uninet.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import de.dbae.uninet.dbConnections.DBConnection;
import de.dbae.uninet.javaClasses.Gruppe;
import de.dbae.uninet.javaClasses.GesuchterNutzer;
import de.dbae.uninet.javaClasses.Veranstaltung;
import de.dbae.uninet.sqlClasses.SuchergebnisseSql;

/**
 * Servlet verabeitet die Suchanfragen.
 * @author Marvin Wolf 
 */
@WebServlet("/SuchergebnisseServlet")
public class SuchergebnisseServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    private HttpSession session;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SuchergebnisseServlet() {
        super();
    }

	/**
	 * Holt die geuschtenNutzer / gesuchtenGruppen / gesuchten Veranstaltungen aus der DB
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		session = request.getSession();
		int userID = Integer.parseInt(session.getAttribute("UserID").toString());
		// Suchparameter auslesen
		String search = request.getParameter("suchanfrage");
		if (search == null) {
			search = "";
		}
				
		// Attribute setzen
		request.setAttribute("Suche", search);
		search = search.replaceAll(" ", "");
		if (!search.equals("") && !search.equals(null)) {
			request.setAttribute("search", search);
			request.setAttribute("Nutzerliste", (List<GesuchterNutzer>)getNutzer(search, userID));
			request.setAttribute("Gruppenliste", (List<Gruppe>)getGruppen(search, userID));
			request.setAttribute("Veranstaltungenliste", (List<Veranstaltung>)getVeranstaltungen(search, userID));
		}
		request.getRequestDispatcher("Suchergebnisse.jsp").forward(request, response);
	}

	/**
	 * Verarbeitet das schicken von Freundschaftsanfragen
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		session = request.getSession();
		int userID = Integer.parseInt(session.getAttribute("UserID").toString());
		String sfreundID = request.getParameter("freundID");
		String sfreundIDloeschen = request.getParameter("freundIDloeschen");
		String search = request.getParameter("search");
		int freundID = -1;
		if (sfreundID != null) {
			freundID = Integer.parseInt(sfreundID);
			sendFriendrequest(userID, freundID);
		}
		else if (sfreundIDloeschen != null) {
			freundID = Integer.parseInt(sfreundIDloeschen);
			loescheFreundschaft(userID, freundID);
		}
		
		if (search == null) {
			search = "";
		}
		
		response.sendRedirect("/UniNet/SuchergebnisseServlet?suchanfrage=" + search);;
	}
	
	/**
	 * Sendet eine Freundschaftsanfrage von user zu freund
	 */
	private void sendFriendrequest(int userID, int freundID) {
		DBConnection dbcon = null;
		SuchergebnisseSql seSql = new SuchergebnisseSql();
		
		try {
			dbcon = new DBConnection();
			Connection con = dbcon.getCon();
			PreparedStatement pStmt;
			pStmt = con.prepareStatement(seSql.erstelleFreundschaftsanfrage());
			pStmt.setInt(1, freundID);
			pStmt.setInt(2, userID);
			pStmt.setInt(3, 1);
			pStmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("ERROR - SuchergebnisServlet - sendFriendRequest");
			e.printStackTrace();
		} finally {
			try {
				if (dbcon != null) {
					dbcon.close();
				}
			} catch (Exception ignored) {
				ignored.printStackTrace();
			}
		}
	}
	
	private void loescheFreundschaft(int userID, int freundID) {
		DBConnection dbcon = null;
		SuchergebnisseSql seSql = new SuchergebnisseSql();
		
		try {
			dbcon = new DBConnection();
			Connection con = dbcon.getCon();
			PreparedStatement pStmt;
			pStmt = con.prepareStatement(seSql.loescheFreundschaft());
			pStmt.setInt(1, freundID);
			pStmt.setInt(2, userID);
			pStmt.setInt(4, freundID);
			pStmt.setInt(3, userID);
			pStmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("ERROR - SuchergebnisServlet - loscheFreundschaft");
			e.printStackTrace();
		} finally {
			try {
				if (dbcon != null) {
					dbcon.close();
				}
			} catch (Exception ignored) {
				ignored.printStackTrace();
			}
		}
	}
	
	
	/**
	 * Gibt eine Liste aller Nutzer zurück die den Suchbegriff im Namen enthalten
	 */
	private List<GesuchterNutzer> getNutzer(String search, int userID) { 
		DBConnection dbcon = null;
		SuchergebnisseSql seSql = new SuchergebnisseSql();
		List<GesuchterNutzer> nutzer = new ArrayList<>();
		
		try {
			dbcon = new DBConnection();
			Connection con = dbcon.getCon();
			PreparedStatement pStmt = con.prepareStatement(seSql.getUniZuUser());
			pStmt.setInt(1, userID);
			ResultSet uniRs = pStmt.executeQuery();
			int uniID = 0;
			if (uniRs.next()) {
				uniID = uniRs.getInt(1);
			}
			pStmt = con.prepareStatement(seSql.getNutzerSql());
			pStmt.setInt(1, userID);
			pStmt.setInt(2, uniID);
			pStmt.setString(3, search);
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				int gesuchterID   = rs.getInt(1);
				String vorname    = rs.getString(2);
				String nachname   = rs.getString(3);
				Date geburtsdatum = rs.getDate(4);
				boolean online    = rs.getBoolean(5);
				
				// isFreund?
				pStmt = con.prepareStatement(seSql.isFreundSql());
				pStmt.setInt(1, userID);
				pStmt.setInt(2, gesuchterID);
				ResultSet rsIsFreund = pStmt.executeQuery();
				int iFreund = 0;
				if (rsIsFreund.next()) {
					iFreund = rsIsFreund.getInt(1);
				}
				boolean isFreund = iFreund == 0 ? false : true;
				
				nutzer.add(new GesuchterNutzer(vorname, nachname, gesuchterID, geburtsdatum, online, isFreund));
			}
		} catch (Exception e) {
			System.out.println("ERROR - SuchergebnisServlet - getNutzer");
			e.printStackTrace();
		} finally {
			try {
				if (dbcon != null) {
					dbcon.close();
				}
			} catch (Exception ignored) {
				ignored.printStackTrace();
			}
		}
		
		return nutzer;
	}
	
	/**
	 * Gibt eine Liste von Gruppen zurück, die den Suchbegriff im Namen oder in der Beschreibung enthalten
	 */
	private List<Gruppe> getGruppen(String search, int userID) { 
		DBConnection dbcon = null;
		SuchergebnisseSql seSql = new SuchergebnisseSql();
		List<Gruppe> gruppen = new ArrayList<>();
		
		try {
			dbcon = new DBConnection();
			Connection con = dbcon.getCon();
			PreparedStatement pStmt = con.prepareStatement(seSql.getUniZuUser());
			pStmt.setInt(1, userID);
			ResultSet uniRs = pStmt.executeQuery();
			int uniID = 0;
			if (uniRs.next()) {
				uniID = uniRs.getInt(1);
			}
			pStmt = con.prepareStatement(seSql.getGruppenSql());
			pStmt.setInt(1, uniID);
			pStmt.setString(2, search);
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				int gruppenID       = rs.getInt(1);
				String name         = rs.getString(2);
				String beschreibung = rs.getString(3);
				String gruendung    = rs.getString(4);
				int adminID         = rs.getInt(5);
				// ADMINNAME 
				String adminName = "Kein Admin";
				PreparedStatement pStmtName = con.prepareStatement(seSql.getNutzerZuId());
				pStmtName.setInt(1, adminID);
				ResultSet rsName = pStmtName.executeQuery();
				if(rsName.next()) {
					adminName = rsName.getString(1) + " " + rsName.getString(2);
				}
				gruppen.add(new Gruppe(gruppenID, name, beschreibung, gruendung, adminName, adminID));
			}
		} catch (Exception e) {
			System.out.println("ERROR - SuchergebnisServlet - getGruppen");
			e.printStackTrace();
		} finally {
			try {
				if (dbcon != null) {
					dbcon.close();
				}
			} catch (Exception ignored) {
				ignored.printStackTrace();
			}
		}
		
		return gruppen;
	}
	
	/**
	 * Gibt eine Liste von Veranstaltungen zurück, die den Suchbegriff im Namen oder in der Beschreibung enthalten
	 */
	private List<Veranstaltung> getVeranstaltungen(String search, int userID) { 
		DBConnection dbcon = null;
		SuchergebnisseSql seSql = new SuchergebnisseSql();
		List<Veranstaltung> events = new ArrayList<>();
		
		try {
			dbcon = new DBConnection();
			Connection con = dbcon.getCon();
			PreparedStatement pStmt = con.prepareStatement(seSql.getVeranstaltungenSql());
			pStmt.setInt(1, userID);
			pStmt.setString(2, search);
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				int eventID         = rs.getInt(1);
				String name         = rs.getString(2);
				String beschreibung = rs.getString(3);
				String dozent       = rs.getString(4);
				String semester     = rs.getString(5);
				
				events.add(new Veranstaltung(eventID, name, dozent, semester, beschreibung));
			}
		} catch (Exception e) {
			System.out.println("ERROR - SuchergebnisServlet - getGruppen");
		} finally {
			try {
				if (dbcon != null) {
					dbcon.close();
				}
			} catch (Exception ignored) {
				ignored.printStackTrace();
			}
		}
		return events;
	}
}
