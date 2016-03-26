package de.dbae.uninet.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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
import de.dbae.uninet.javaClasses.Student;
import de.dbae.uninet.javaClasses.Gruppe;
import de.dbae.uninet.sqlClasses.BeitragSql;
import de.dbae.uninet.sqlClasses.GruppenSql;

/**
 * Servlet implementation class GruppenServlet
 */
@WebServlet("/GruppenServlet")
public class GruppenServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private HttpSession session;
	private Connection con = null;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GruppenServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Chatfreunde laden
		new LadeChatFreundeServlet().setChatfreunde(request);
		
		String name = request.getParameter("name");
		if (name == null) {
			name = "Gruppe";
		}
		int userID = Integer.parseInt(request.getSession().getAttribute("UserID").toString());
		DBConnection dbcon = new DBConnection();
		con = dbcon.getCon();
		try {
			GruppenSql sqlSt = new GruppenSql();
			String sql = sqlSt.getGruppen();
			PreparedStatement pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, userID);
			ResultSet rs = pStmt.executeQuery();
			List<Gruppe> gruppen = new ArrayList<Gruppe>();
			while (rs.next()) {
				String bezeichnung = rs.getString(1);
				int id = rs.getInt(2);
				Gruppe gruppe = new Gruppe(id, bezeichnung);
				gruppen.add(gruppe);
			}
			request.setAttribute("gruppenList", gruppen);
			switch (name) {
			case "Gruppe":
				gruppe(request, response);
				break;
			case "Uebersicht":
				request.getRequestDispatcher("Gruppenuebersicht.jsp").forward(request, response);
				break;
			case "Verlassen":
				verlassen(request, response);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			System.out.println("SQL Fehler in GruppenServlet");
			e.printStackTrace();
		} finally {
			dbcon.close();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Chatfreunde
		new LadeChatFreundeServlet().setChatfreunde(request);
				
		DBConnection dbcon = new DBConnection();
		con = dbcon.getCon();
		String name = request.getParameter("name");
		if (name == null) {
			name = "";
		}
		try {
			switch (name) {
			case "BeitragPosten":
				posteBeitrag(request, response);
				break;
			case "Gruenden":
				gruenden(request, response);
				break;
			case "BeschreibungBearbeiten":
				beschreibungAendern(request, response);
				break;
			case "MitgliedZufuegen":
				mitgliederBearbeiten(request, response, true);
				break;
			case "MitgliedEntfernen":
				mitgliederBearbeiten(request, response, false);
				break;
			case "GruppeLoeschen":
				loeschen(request, response);
				break;
			default:
				doGet(request, response);
				break;
			}
		} catch (Exception e) {
			System.out.println("SQL Fehler im GruppenServlet");
			e.printStackTrace();
		} finally {
			dbcon.close();
		}
	}
	
	private void gruppe(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
		session = request.getSession();
		int userID = Integer.parseInt(request.getSession().getAttribute("UserID").toString());
		// GruppenID lesen und setzen
		int id = Integer.parseInt(request.getParameter("gruppenID"));
		request.setAttribute("gruppenID", id);
		// Aktuellen Tab lesen und setzen
		String tab = request.getParameter("tab");
		request.setAttribute("tab", tab);
		// GruppenInfos laden
		String sql = new GruppenSql().getInfos();
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, id);
		ResultSet rs = pStmt.executeQuery();
		if (rs.next()) {
			String name = rs.getString(1);
			String beschreibung = rs.getString(2);
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
			String gruendung = sdf.format(new Date(rs.getDate(3).getTime()));
			String admin = rs.getString(4) + " " + rs.getString(5);
			int adminID = rs.getInt(6);
			Gruppe gruppe = new Gruppe(id, name, beschreibung, gruendung, admin, adminID);
			request.setAttribute("gruppe", gruppe);
		}
		request.setAttribute("userID", userID);
		switch (tab) {
		case "beitraege":
			request.setAttribute("beitraegeActive", "active");
			request.setAttribute("beitragList", new BeitragServlet().getBeitraege(request, con, "Gruppen", userID, id + ""));
			break;
		case "infos":
			request.setAttribute("infosActive", "active");
			break;
		case "mitglieder":
			request.setAttribute("mitglieder", getMitglieder(request));
			request.setAttribute("mitgliederActive", "active");
			break;
		case "bearbeiten":
			request.setAttribute("bearbeitenActive", "active");
			request.setAttribute("mitglieder", getMitglieder(request));
			request.setAttribute("freunde", getFreunde(request));
		default:
			break;
		}
		request.getRequestDispatcher("Gruppen.jsp").forward(request, response);
	}
	
	private void posteBeitrag(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		BeitragSql sqlSt = new BeitragSql();
		String beitrag = request.getAttribute("beitrag").toString();
		int verfasserID = Integer.parseInt(session.getAttribute("UserID").toString());
		String sichtbarkeit = request.getParameter("sichtbarkeit");
		boolean sichtbar = true;
		if (sichtbarkeit.startsWith("Privat")) {
			sichtbar = false;
		}
		// Beitrag anlegen
		String sql = sqlSt.getBeitragAnlegenSql1();
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setString(1, beitrag);
		pStmt.setInt(2, verfasserID);
		pStmt.setBoolean(3, sichtbar);
		pStmt.setDate(4, new java.sql.Date(System.currentTimeMillis()));
		pStmt.setTime(5, new java.sql.Time(System.currentTimeMillis()));
		pStmt.executeUpdate();
		
		// BeitragsID abfragen
		sql = sqlSt.getBeitragAnlegenSql2();
		pStmt = con.prepareStatement(sql);
		ResultSet rs = pStmt.executeQuery();
		int beitragsID;
		if (rs.next()) {
			beitragsID = rs.getInt(1);
			// Veranstaltungsbeitrag eintragen
			sql = sqlSt.getBeitragAnlegenSqlGruppe();
			pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, beitragsID);
			int gruppenID = Integer.parseInt(request.getParameter("gruppenID"));
			pStmt.setInt(2, gruppenID);
			pStmt.executeUpdate();
			response.sendRedirect("GruppenServlet?tab=beitraege&gruppenID=" + gruppenID);
		} else {
			System.out.println("Problem beim Anlegen des Beitrags");
		}
	}
	
	private void verlassen(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		GruppenSql sqlSt = new GruppenSql();
		int userID = Integer.parseInt(session.getAttribute("UserID").toString());
		int id = Integer.parseInt(request.getParameter("gruppenID"));
		String sql = sqlSt.getVerlassen();
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, id);
		pStmt.setInt(2, userID);
		pStmt.executeUpdate();
		response.sendRedirect("GruppenServlet?name=Uebersicht");
		//TODO Was passiert wenn der Admin die Gruppe verlaesst
	}
	
	private List<Student> getMitglieder(HttpServletRequest request) throws SQLException {
		boolean sortByV = true;
		if (request.getParameter("sortByV") != null && request.getParameter("sortByV").equals("false")) {
			sortByV = false;
		}
		List<Student> mitglieder = new ArrayList<Student>();
		GruppenSql sqlSt = new GruppenSql();
		String sql = sqlSt.getMitglieder();
		if (sortByV) {
			sql += "Vorname, Nachname";
			request.setAttribute("vornameLink", "text-decoration: underline");
		} else {
			sql += "Nachname, Vorname";
			request.setAttribute("nachnameLink", "text-decoration: underline");
		}
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, Integer.parseInt(request.getParameter("gruppenID")));
		ResultSet rs = pStmt.executeQuery();
		while(rs.next()) {
			String vorname = rs.getString(1);
			String nachname = rs.getString(2);
			int userID = rs.getInt(3);
			mitglieder.add(new Student(vorname, nachname, userID));
		}
		request.setAttribute("anzahlMitglieder", mitglieder.size());
		return mitglieder;
	}
	
	private void gruenden(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		int userID = Integer.parseInt(request.getSession().getAttribute("UserID").toString());
		GruppenSql sqlSt = new GruppenSql();
		String sql = sqlSt.getGruppeGruenden();
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setString(1, request.getParameter("gruppenname"));
		pStmt.setString(2, "");
		pStmt.setDate(3, new java.sql.Date(System.currentTimeMillis()));
		pStmt.setInt(4, userID);
		pStmt.executeUpdate();
		sql = sqlSt.getNeueGruppenID();
		pStmt = con.prepareStatement(sql);
		ResultSet rs = pStmt.executeQuery();
		int id = 1;
		if (rs.next()) {
			id = rs.getInt(1);
		}
		sql = sqlSt.getStudentHinzufuegen();
		pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, id);
		pStmt.setInt(2, userID);
		pStmt.executeUpdate();
		response.sendRedirect("GruppenServlet?tab=bearbeiten&gruppenID=" + id);
	}
	
	private void loeschen(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		GruppenSql sqlSt = new GruppenSql();
		String sql = sqlSt.getGruppeLoeschen();
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, Integer.parseInt(request.getParameter("gruppenID")));
		pStmt.executeUpdate();
		response.sendRedirect("GruppenServlet?name=Uebersicht");
	}
	
	private void beschreibungAendern(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		GruppenSql sqlSt = new GruppenSql();
		String sql = sqlSt.getBeschreibungAendern();
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setString(1, request.getParameter("beschreibung"));
		pStmt.setInt(2, Integer.parseInt(request.getParameter("gruppenID")));
		pStmt.executeUpdate();
		response.sendRedirect("GruppenServlet?tab=infos&gruppenID=" + request.getParameter("gruppenID"));
	}
	
	private void mitgliederBearbeiten(HttpServletRequest request, HttpServletResponse response, boolean zufuegen) throws SQLException, IOException {
		GruppenSql sqlSt = new GruppenSql();
		String sql = sqlSt.getMitgliederZufuegen();
		if (!zufuegen) {
			sql = sqlSt.getVerlassen();
		}
		int gruppenID = Integer.parseInt(request.getParameter("gruppenID"));
		int userID = Integer.parseInt(request.getParameter("mitgliedID"));
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, gruppenID);
		pStmt.setInt(2, userID);
		pStmt.executeUpdate();
		response.sendRedirect("GruppenServlet?gruppenID=" + gruppenID + "&tab=bearbeiten");
	}
	
	@SuppressWarnings("unchecked")
	private List<Student> getFreunde(HttpServletRequest request) throws SQLException {
		List<Student> freunde = new ArrayList<Student>();
		GruppenSql sqlSt = new GruppenSql();
		String sql = sqlSt.getFreunde();
		int gruppenID = Integer.parseInt(request.getParameter("gruppenID"));
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, gruppenID);
		ResultSet rs = pStmt.executeQuery();
		while (rs.next()) {
			int id = rs.getInt(1);
			String vorname = rs.getString(2);
			String nachname = rs.getString(3);
			boolean mitglied = false;
			for (Student student : (ArrayList<Student>)request.getAttribute("mitglieder")) {
				if (student.getUserID() == id)
					mitglied = true;
			}
			if (!mitglied)
				freunde.add(new Student(vorname, nachname, id));
		}
		return freunde;
	}

}
