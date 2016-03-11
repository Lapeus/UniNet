package de.dbae.uninet.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.dbae.uninet.dbConnections.DBConnection;
import de.dbae.uninet.sqlClasses.AnmeldeSql;

/**
 * Servlet implementation class testservlet
 */
@WebServlet("/testservlet")
public class testservlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public testservlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection con = new DBConnection().getCon();
		AnmeldeSql sqlSt = new AnmeldeSql(con);
		for (String s : sqlSt.getUniList()) {
			System.out.println(s);
		}

		request.setAttribute("unis", sqlSt.getUniList());
		
		/*
		Connection con = null;
		String name = request.getParameter("name");
		String sql = "SELECT * FROM Nutzer WHERE Vorname=?;";

		try {
			con = new DBConnection().getCon();
			PreparedStatement pStmt = con.prepareStatement(sql);
			pStmt.setString(1, name);
			ResultSet result = pStmt.executeQuery();
			
			List<String> resultList = new ArrayList<String>();
			
			ResultSetMetaData rsMetaData = result.getMetaData();
			resultList.add("<tr><th colspan='4'>Ergebnis</th></tr>");
			resultList.add("<tr>");
			for (int i = 1; i <= rsMetaData.getColumnCount(); i++) {
				resultList.add("<th>" + rsMetaData.getColumnLabel(i) + "</th>");
			}
			resultList.add("</tr>");
			while (result.next()) {
				resultList.add("<tr>");
				for (int i = 1; i <= rsMetaData.getColumnCount(); i++) {
					resultList.add("<td>" + result.getString(i) + "</td>");
				} 
				resultList.add("</tr>");
			}
			request.setAttribute("result", resultList);
			request.getRequestDispatcher("index.jsp").forward(request, response);
		} catch (Exception e) {
			response.getWriter().append("SQL-Fehler " + (con == null));
			//e.printStackTrace();
		} finally {
			try {
				if (con!=null) {
					con.close();
					System.out.println("Die Verbindung wurde erfolgreich beendet!");
				}
			} catch (SQLException ignored) {}
		}
		*/
		try {
			if(!con.isClosed()) {
				con.close();
				System.out.println("Connection closed");
			}
		} catch (SQLException e) {
			System.out.println("No Connection");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection con = new DBConnection().getCon();
		AnmeldeSql sqlSt = new AnmeldeSql(con);
		String anrede = request.getParameter("anrede");
		boolean bAnrede = anrede.equals("Herr") ? true : false; 
		String vorname = request.getParameter("vorname");
		String nachname = request.getParameter("nachname");
		String email = request.getParameter("email");
		String password1 = request.getParameter("password1");
		String password2 = request.getParameter("password2");
		
		if(password1.equals(password2)) {
			sqlSt.registrierungsSql(bAnrede, vorname, nachname, email, password1);
		} else {
			request.getRequestDispatcher("Anmeldung.jsp").forward(request, response);
		}
		try {
			if(!con.isClosed()) {
				con.close();
				System.out.println("Connection closed");
			}
		} catch (SQLException e) {
			System.out.println("No Connection");
		}
	}

}
