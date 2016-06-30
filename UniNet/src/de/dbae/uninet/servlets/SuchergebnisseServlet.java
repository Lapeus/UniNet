package de.dbae.uninet.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import de.dbae.uninet.dbConnections.DBConnection;
import de.dbae.uninet.javaClasses.Student;
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
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DBConnection dbcon = null;
		session = request.getSession();
		SuchergebnisseSql seSql = new SuchergebnisseSql();
		// Suchparameter auslesen
		String search = request.getParameter("suchanfrage");
		if (search == null) {
			search = "";
		}
				
		// Attribute setzen
		request.setAttribute("Suche", search);
		search = "%" + search + "%";
		request.setAttribute("Nutzerliste", (List<Student>)getNutzer(search));
		request.getRequestDispatcher("Suchergebnisse.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	
	private List<Student> getNutzer(String search) { 
		DBConnection dbcon = null;
		SuchergebnisseSql seSql = new SuchergebnisseSql();
		List<Student> nutzer = new ArrayList<>();
		
		try {
			dbcon = new DBConnection();
			Connection con = dbcon.getCon();
			PreparedStatement pStmt = con.prepareStatement(seSql.getNutzerSql());
			pStmt.setString(1, search);
			pStmt.setString(2, search);
			System.out.println("GETNUTZER" + pStmt.toString());
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				int userID      = rs.getInt(1);
				String vorname  = rs.getString(2);
				String nachname = rs.getString(3);
				
				nutzer.add(new Student(vorname, nachname, userID));
			}
			
			for (Student student : nutzer) {
				System.out.println("Vorname: " + student.getVorname());
			}
		} catch (Exception e) {
			System.out.println("SuchergebnisServlet - getNutzer");
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
}
