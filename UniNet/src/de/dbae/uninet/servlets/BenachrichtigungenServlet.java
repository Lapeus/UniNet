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
import de.dbae.uninet.javaClasses.Benachrichtigung;
import de.dbae.uninet.javaClasses.Freundschaftsanfrage;
import de.dbae.uninet.sqlClasses.BenachrichtigungenSql;

/**
 * Servlet implementation class BenachrichtigungenServlet
 */
@WebServlet("/BenachrichtigungenServlet")
public class BenachrichtigungenServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private HttpSession session;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BenachrichtigungenServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		session = request.getSession();
		int userID = Integer.parseInt(session.getAttribute("UserID").toString());
		// Attribute setzen
		request.setAttribute("Freunschaftsanfragen", (List<Freundschaftsanfrage>)getFreundschaftanfragen(userID));
		request.setAttribute("Benachrichtigungen", (List<Benachrichtigung>)getBenachrichtigungen(userID));
		request.getRequestDispatcher("Benachrichtigungen.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		session = request.getSession();
		int userID = Integer.parseInt(session.getAttribute("UserID").toString());
		int freundID;
		if (request.getParameter("accept") != null) {
			freundID = Integer.parseInt(request.getParameter("accept"));
			bestaetigeFreundschaft(userID, freundID);
			loescheAnfragen(userID, freundID);
		}
		else if(request.getParameter("denie") != null) {
			freundID = Integer.parseInt(request.getParameter("denie"));
			loescheAnfragen(userID, freundID);
		}
		doGet(request, response);
	}
	
	private List<Freundschaftsanfrage> getFreundschaftanfragen(int userID) {
		List<Freundschaftsanfrage> freundesListe = new ArrayList<>();
		DBConnection dbcon = null;
		BenachrichtigungenSql beSql = new BenachrichtigungenSql();
		
		try {
			dbcon = new DBConnection();
			Connection con = dbcon.getCon();
			PreparedStatement pStmt = con.prepareStatement(beSql.getFreundesanfragenSql());
			pStmt.setInt(1, userID);
		
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				String benachrichtigung = rs.getString(1);
				int freundID = rs.getInt(2);
				Date datum = rs.getDate(3);
				pStmt = con.prepareStatement(beSql.getNameZuID());
				pStmt.setInt(1, freundID);
				ResultSet rsName = pStmt.executeQuery();
				String freundName = "Kein Name";
				if (rsName.next()) {
					freundName = rsName.getString(1) + " " + rsName.getString(2);
				}
				freundesListe.add(new Freundschaftsanfrage(benachrichtigung, userID, freundID, freundName, datum));
			}
		} catch (Exception e) {
			System.out.println("ERROR - BenachrichtigungenServlet - getFreundschaftsanfragen");
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
		return freundesListe;
	}
	
	private List<Benachrichtigung> getBenachrichtigungen(int userID) {
		DBConnection dbcon = null;
		BenachrichtigungenSql beSql = new BenachrichtigungenSql();
		
		List<Benachrichtigung> benachrichtigungen = new ArrayList<>();
		try {
			dbcon = new DBConnection();
			Connection con = dbcon.getCon();
			// Benachrichtigungen auslesen
			PreparedStatement pStmt = con.prepareStatement(beSql.getBenachrichtigungSql());
			pStmt.setInt(1, userID);
			
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				String html = rs.getString(1);
				Date date = rs.getDate(2);
				benachrichtigungen.add(new Benachrichtigung(html, date));
			}
			
			// Benachrichtigungen als gelesen makieren
			pStmt = con.prepareStatement(beSql.loescheBenachrictigungSql());
			pStmt.setInt(1, userID);
			pStmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("ERROR - BenachrichtigungenServlet - getBenachrichtigungen");
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
		return benachrichtigungen;
	}
	
	private void bestaetigeFreundschaft(int userID, int freundID) {
		DBConnection dbcon = null;
		BenachrichtigungenSql beSql = new BenachrichtigungenSql();
		
		try {
			dbcon = new DBConnection();
			Connection con = dbcon.getCon();
			PreparedStatement pStmt = con.prepareStatement(beSql.bestaetigeFreundschaftSql());
			pStmt.setInt(1, freundID);
			pStmt.setInt(2, userID);
			pStmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("ERROR - BenachrichtigungenServlet - bestaetigeFreundschaft - catch");
			e.printStackTrace();
		} finally {
			try {
				if (dbcon != null) {
					dbcon.close();
				}
			} catch (Exception ignored) {
				System.out.println("ERROR - BenachrichtigungenServlet - bestaetigeFreundschaft - finally");
				ignored.printStackTrace();
			}
		}
	}
	
	private void loescheAnfragen(int userID, int freundID) {
		DBConnection dbcon = null;
		BenachrichtigungenSql beSql = new BenachrichtigungenSql();
		
		try {
			dbcon = new DBConnection();
			Connection con = dbcon.getCon();
			PreparedStatement pStmt = con.prepareStatement(beSql.lehneFreunschaftAbSql());
			pStmt.setInt(1, userID);
			pStmt.setInt(2, freundID);
			pStmt.setInt(3, freundID);
			pStmt.setInt(4, userID);
			System.out.println(pStmt.toString());
			pStmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("ERROR - BenachrichtigungenServlet - lehneFreundschaftAb - catch");
			e.printStackTrace();
		} finally {
			try {
				if (dbcon != null) {
					dbcon.close();
				}
			} catch (Exception ignored) {
				System.out.println("ERROR - BenachrichtigungenServlet - lehneFreundschaftAb - finally");
				ignored.printStackTrace();
			}
		}
	}
}
