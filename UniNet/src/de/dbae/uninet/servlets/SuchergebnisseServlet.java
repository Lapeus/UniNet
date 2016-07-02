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
 * Servlet implementation class SuchergebnisseServlet
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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
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
			request.setAttribute("Gruppenliste", (List<Gruppe>)getGruppen(search));
			request.setAttribute("Veranstaltungenliste", (List<Veranstaltung>)getVeranstaltungen(search, userID));
		}
		request.getRequestDispatcher("Suchergebnisse.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		session = request.getSession();
		int userID = Integer.parseInt(session.getAttribute("UserID").toString());
		String sfreundID = request.getParameter("freundID");
		String search = request.getParameter("search");
		System.out.println("SUCHE: " + search);
		int freundID = -1;
		if (sfreundID != null) {
			freundID = Integer.parseInt(sfreundID);
		}
		if (search == null) {
			search = "";
		}
		sendFriendRequest(userID, freundID);
		response.sendRedirect("/UniNet/SuchergebnisseServlet?suchanfrage=" + search);;
	}
	
	private void sendFriendRequest(int userID, int freundID) {
		
	}
	
	private List<GesuchterNutzer> getNutzer(String search, int userID) { 
		DBConnection dbcon = null;
		SuchergebnisseSql seSql = new SuchergebnisseSql();
		List<GesuchterNutzer> nutzer = new ArrayList<>();
		
		try {
			dbcon = new DBConnection();
			Connection con = dbcon.getCon();
			PreparedStatement pStmt = con.prepareStatement(seSql.getNutzerSql());
			pStmt.setInt(1, userID);
			pStmt.setString(2, search);
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
					System.out.println("JA ES GIBT EINE AUZSWERTUNG");
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
	
	private List<Gruppe> getGruppen(String search) { 
		DBConnection dbcon = null;
		SuchergebnisseSql seSql = new SuchergebnisseSql();
		List<Gruppe> gruppen = new ArrayList<>();
		
		try {
			dbcon = new DBConnection();
			Connection con = dbcon.getCon();
			PreparedStatement pStmt = con.prepareStatement(seSql.getGruppenSql());
			pStmt.setString(1, search);
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				int gruppenID       = rs.getInt(1);
				String name         = rs.getString(2);
				String beschreibung = rs.getString(3);
				String gruendung    = rs.getString(4);
				int adminID         = rs.getInt(5);
				
				// ADMINNAME TODO
				String adminName = "Marvin";
//				PreparedStatement pStmtName = con.prepareStatement(seSql.getNutzerZuId());
//				pStmtName.setInt(1, adminID);
//				ResultSet rsName = pStmtName.executeQuery();
//				if(rs.next()) {
//					adminName = rsName.getString(1);
//				}
				System.out.println("ADMINNAME: " + adminName);
				gruppen.add(new Gruppe(gruppenID, name, beschreibung, gruendung, adminName, adminID));
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
		
		return gruppen;
	}
	
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
			System.out.println("GETVeranstaltungen: " + pStmt.toString());
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				int eventID         = rs.getInt(1);
				String name         = rs.getString(2);
				String beschreibung = rs.getString(3);
				String dozent       = rs.getString(4);
				String semester     = rs.getString(5);
				
				events.add(new Veranstaltung(eventID, name, dozent, semester, beschreibung));
			}
			
			for (Veranstaltung event : events) {
				System.out.println("Veranstaltunsname: " + event.getName());
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
