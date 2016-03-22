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

import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;

import de.dbae.uninet.dbConnections.DBConnection;
import de.dbae.uninet.javaClasses.Beitrag;
import de.dbae.uninet.sqlClasses.StartseiteSql;

/**
 * Servlet implementation class StartseiteServlet
 */
@WebServlet("/StartseiteServlet")
public class StartseiteServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private HttpSession session;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StartseiteServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection con = new DBConnection().getCon();
		int userID = Integer.parseInt(request.getSession().getAttribute("UserID").toString());
		request.setAttribute("beitragList", new BeitragServlet().getBeitraege(request, con, "Startseite", userID));
		try {
			// Weiterleitung
			request.getRequestDispatcher("Startseite.jsp").forward(request, response);
		} catch (Exception e) {
			response.getWriter().append("SQL-Fehler");
			e.printStackTrace();
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
		doGet(request, response);
	}

}
