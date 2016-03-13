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

import org.eclipse.jdt.internal.compiler.ast.ForeachStatement;

import de.dbae.uninet.dbConnections.DBConnection;
import de.dbae.uninet.sqlClasses.AnmeldeSql;

/**
 * Servlet implementation class StudiengaengeServlet
 */
@WebServlet("/AnmeldeAktivServlet")
public class AnmeldeAktivServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AnmeldeAktivServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection con = new DBConnection().getCon();
		AnmeldeSql sqlSt = new AnmeldeSql();
		
		if (!request.getParameter("laden").equals("Laden")) {
			String anrede = request.getParameter("anrede");
			boolean bAnrede = anrede.equals("Herr") ? true : false; 
			String vorname = request.getParameter("vorname");
			String nachname = request.getParameter("nachname");
			String email = request.getParameter("email");
			String password1 = request.getParameter("password1");
			String password2 = request.getParameter("password2");
			
			try {
				PreparedStatement pStmt = con.prepareStatement(sqlSt.getRegistrierungsSql(bAnrede, vorname, nachname, email, password1));
				if(password1.equals(password2)) {
					pStmt.execute();
					request.getRequestDispatcher("Startseite.jsp").forward(request, response);
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
		} else {
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
	}

}
