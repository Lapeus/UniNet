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
		AnmeldeSql sqlSt = new AnmeldeSql();
		List<String> unis = new ArrayList<>();
		PreparedStatement pStmt;
		try {
			pStmt = con.prepareStatement(sqlSt.getUniList());
			ResultSet result = pStmt.executeQuery();
			ResultSetMetaData rsMetaData = result.getMetaData();
			while (result.next()) {
				for (int i = 1; i <= rsMetaData.getColumnCount(); i++) {
					unis.add(result.getString(i));
				} 
			}

			request.setAttribute("unis", unis);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection con = new DBConnection().getCon();
		AnmeldeSql sqlSt = new AnmeldeSql();
		
		String anrede = request.getParameter("anrede");
		boolean bAnrede = anrede.equals("Herr") ? true : false; 
		String vorname = request.getParameter("vorname");
		String nachname = request.getParameter("nachname");
		String email = request.getParameter("email");
		String password1 = request.getParameter("password1");
		String password2 = request.getParameter("password2");
		
		try {
			PreparedStatement pStmt = con.prepareStatement(sqlSt.getRegistrierungsSql(bAnrede, vorname, nachname, email, password1));
			System.out.println(sqlSt.getRegistrierungsSql(bAnrede, vorname, nachname, email, password1));
			if(password1.equals(password2)) {
				pStmt.execute();
			} 
			} catch (Exception e) {
				response.getWriter().append("SQL-Fehler " + (con == null));
			} finally {
				try {
					if (con!=null) {
						con.close();
						System.out.println("Die Verbindung wurde erfolgreich beendet!");
					}
			} catch (SQLException ignored) {}
		}
	}

}
