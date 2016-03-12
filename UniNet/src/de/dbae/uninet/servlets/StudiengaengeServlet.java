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
 * Servlet implementation class StudiengaengeServlet
 */
@WebServlet("/StudiengaengeServlet")
public class StudiengaengeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StudiengaengeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection con = new DBConnection().getCon();
		AnmeldeSql sqlSt = new AnmeldeSql();
		String uni = request.getParameter("uni");
		List<String> studiengaenge = new ArrayList<String>();
		try {
			PreparedStatement pStmt = con.prepareStatement(sqlSt.getStudiengaenge(uni));
			ResultSet result = pStmt.executeQuery();
			ResultSetMetaData rsMetaData = result.getMetaData();
			while (result.next()) {
				for (int i = 1; i <= rsMetaData.getColumnCount(); i++) {
					studiengaenge.add(result.getString(i));
				} 
			}
			request.setAttribute("studiengaenge", studiengaenge);
			request.getRequestDispatcher("Anmeldung.jsp").forward(request, response);
		} catch (SQLException e) {
			System.out.println("SQL Fehler - AnmeldeSQL.getStudiengaenge(uni)");
		} finally {
			try {
				if (con!=null) {
					con.close();
					System.out.println("Die Verbindung wurde erfolgreich beendet!");
				}
			} catch (SQLException ignored) {}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
