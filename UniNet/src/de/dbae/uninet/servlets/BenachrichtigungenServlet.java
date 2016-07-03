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
		request.getRequestDispatcher("Benachrichtigungen.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
	
	private List<Freundschaftsanfrage> getFreundschaftanfragen(int userID) {
		List<Freundschaftsanfrage> freundesListe = new ArrayList<>();
		DBConnection dbcon = null;
		BenachrichtigungenSql beSql = new BenachrichtigungenSql();
		
		try {
			dbcon = new DBConnection();
			Connection con = dbcon.getCon();
			PreparedStatement pStmt = con.prepareStatement(beSql.getFreundesanfragen());
			pStmt.setInt(1, userID);
			ResultSet rs = pStmt.executeQuery();
			
			while (rs.next()) {
				String benachrichtigung = rs.getString(1);
				Date datum = rs.getDate(2);
				freundesListe.add(new Freundschaftsanfrage(benachrichtigung, datum));
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

}
