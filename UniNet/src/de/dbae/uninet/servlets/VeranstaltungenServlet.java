package de.dbae.uninet.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import de.dbae.uninet.javaClasses.Veranstaltung;
import de.dbae.uninet.sqlClasses.BeitragSql;
import de.dbae.uninet.sqlClasses.VeranstaltungenSql;

/**
 * Servlet implementation class VeranstaltungenServlet
 */
@WebServlet("/VeranstaltungenServlet")
public class VeranstaltungenServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private HttpSession session;
	private Connection con = null;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VeranstaltungenServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Chatfreunde
		new LadeChatFreundeServlet().setChatfreunde(request);
				
		String name = request.getParameter("name");
		if (name == null) {
			name = "Veranstaltung";
		}
		int userID = Integer.parseInt(request.getSession().getAttribute("UserID").toString());
		DBConnection dbcon = new DBConnection();
		con = dbcon.getCon();
		try {
			VeranstaltungenSql sqlSt = new VeranstaltungenSql();
			String sql = sqlSt.getVeranstaltungen();
			PreparedStatement pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, userID);
			ResultSet rs = pStmt.executeQuery();
			List<Veranstaltung> veranstaltungList = new ArrayList<Veranstaltung>();
			while (rs.next()) {
				int id = rs.getInt(1);
				String bezeichnung = rs.getString(2);
				veranstaltungList.add(new Veranstaltung(id, bezeichnung));
			}
			request.setAttribute("veranstaltungList", veranstaltungList);
			switch (name) {
			case "Veranstaltung":
				veranstaltung(request, response);
				break;
			case "Uebersicht":
			case "Suche":
				request.setAttribute("tab", "infos");
				uebersicht(request, response);
				break;
			case "Einschreiben":
				einAusSchreiben(request, response, true);
				break;
			case "Ausschreiben":
				einAusSchreiben(request, response, false);
				break;	
			default:
				break;
			}
		} catch (Exception e) {
			System.out.println("SQL Fehler im VeranstaltungenSerlet");
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
			default:
				doGet(request, response);
				break;
			}
		} catch (Exception e) {
			System.out.println("Fehler in VeranstaltungenServlet");
			e.printStackTrace();
		} finally {
			dbcon.close();
		}
		
	}
	
	private void uebersicht(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
		boolean suche = request.getParameter("suche") != null;
		session = request.getSession();
		int userID = Integer.parseInt(request.getSession().getAttribute("UserID").toString());
		String sql = new VeranstaltungenSql().getAlleVeranstaltungen();
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, userID);
		ResultSet rs = pStmt.executeQuery();
		List<Veranstaltung> veranstaltungen = new ArrayList<Veranstaltung>();
		while (rs.next()) {
			int id = rs.getInt(1);
			String bezeichnung = rs.getString(2);
			boolean anzeigen = false;
			if (suche) {
				String such = request.getParameter("suche");
				if (bezeichnung.toLowerCase().contains(such.toLowerCase())) {
					anzeigen = true;
				}
			}
			if (!suche || anzeigen) {
				veranstaltungen.add(new Veranstaltung(id, bezeichnung));
			}
		}
		request.setAttribute("veranstaltungen", veranstaltungen);
		request.getRequestDispatcher("Veranstaltungsuebersicht.jsp").forward(request, response);
	}
	
	@SuppressWarnings("unchecked")
	private void veranstaltung(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
		session = request.getSession();
		int userID = Integer.parseInt(request.getSession().getAttribute("UserID").toString());
		// VeranstaltungsID lesen und setzen
		int id = Integer.parseInt(request.getParameter("veranstaltungsID"));
		request.setAttribute("veranstaltungsID", id);
		// Aktuellen Tab lesen und setzen
		String tab = request.getParameter("tab");
		request.setAttribute("tab", tab);
		// Kontrolle, ob man bereits in der Veranstaltung eingeschrieben ist
		boolean eingetragen = false;
		for (Veranstaltung veranstaltung : (ArrayList<Veranstaltung>)request.getAttribute("veranstaltungList")) {
			if (veranstaltung.getId() == id) {
				eingetragen = true;
			}
		}
		request.setAttribute("eingetragen", eingetragen);
		// VeranstaltungsInfos laden
		String sql = new VeranstaltungenSql().getInfos();
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, id);
		ResultSet rs = pStmt.executeQuery();
		if (rs.next()) {
			String name = rs.getString(1);
			String semester = rs.getString(2);
			String dozent = rs.getString(3);
			String beschreibung = rs.getString(4);
			String sonstiges = rs.getString(5);
			Veranstaltung veranstaltung = new Veranstaltung(id, name, dozent, semester, beschreibung, sonstiges);
			request.setAttribute("veranstaltung", veranstaltung);
		}
		switch (tab) {
		case "beitraege":
			if (eingetragen) {
				request.setAttribute("beitraegeActive", "active");
				request.setAttribute("beitragList", new BeitragServlet().getBeitraege(request, con, "Veranstaltungen", userID, id + ""));
			} else {
				request.setAttribute("infosActive", "active");
				request.setAttribute("tab", "infos");
			}
			break;
		case "infos":
			request.setAttribute("infosActive", "active");
			break;
		case "mitglieder":
			request.setAttribute("mitglieder", getMitglieder(request, con));
			request.setAttribute("mitgliederActive", "active");
			break;
		default:
			break;
		}
		request.getRequestDispatcher("Veranstaltungen.jsp").forward(request, response);
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
			sql = sqlSt.getBeitragAnlegenSqlVeranstaltung();
			pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, beitragsID);
			int veranstaltungsID = Integer.parseInt(request.getParameter("veranstaltungsID"));
			pStmt.setInt(2, veranstaltungsID);
			pStmt.executeUpdate();
			response.sendRedirect("VeranstaltungenServlet?tab=beitraege&veranstaltungsID=" + veranstaltungsID);
		} else {
			System.out.println("Problem beim Anlegen des Beitrags");
		}
	}
	
	private void einAusSchreiben(HttpServletRequest request, HttpServletResponse response, boolean einschreiben) throws SQLException, IOException {
		VeranstaltungenSql sqlSt = new VeranstaltungenSql();
		int userID = Integer.parseInt(request.getSession().getAttribute("UserID").toString());
		String sql;
		String page = "VeranstaltungenServlet?name=Uebersicht";
		int id = Integer.parseInt(request.getParameter("veranstaltungsID"));
		if (einschreiben)
			sql = sqlSt.getEinschreiben();
		else
			sql = sqlSt.getAusschreiben();
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, id);
		pStmt.setInt(2, userID);
		pStmt.executeUpdate();
		if (einschreiben)
			page = "VeranstaltungenServlet?name=Veranstaltung&veranstaltungsID=" + id + "&tab=beitraege";
		response.sendRedirect(page);	
	}
	
	private List<Student> getMitglieder(HttpServletRequest request, Connection con) throws SQLException {
		boolean sortByV = true;
		if (request.getParameter("sortByV") != null && request.getParameter("sortByV").equals("false")) {
			sortByV = false;
		}
		List<Student> mitglieder = new ArrayList<Student>();
		VeranstaltungenSql sqlSt = new VeranstaltungenSql();
		String sql = sqlSt.getMitglieder();
		if (sortByV) {
			sql += "Vorname, Nachname";
			request.setAttribute("vornameLink", "text-decoration: underline");
		} else {
			sql += "Nachname, Vorname";
			request.setAttribute("nachnameLink", "text-decoration: underline");
		}
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, Integer.parseInt(request.getParameter("veranstaltungsID")));
		ResultSet rs = pStmt.executeQuery();
		while(rs.next()) {
			String vorname = rs.getString(1);
			String nachname = rs.getString(2);
			int userID = rs.getInt(3);
			mitglieder.add(new Student(vorname, nachname, userID));
		}
		request.setAttribute("anzahl", mitglieder.size());
		return mitglieder;
	}
}
