package de.dbae.uninet.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import de.dbae.uninet.dbConnections.DBConnection;
import de.dbae.uninet.javaClasses.ChatFreund;
import de.dbae.uninet.sqlClasses.StartseiteSql;

@WebServlet("/LadeChatFreundeServlet")
public class LadeChatFreundeServlet extends HttpServlet{
	
 	private static final long serialVersionUID = 4636772916140807029L;

	public LadeChatFreundeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		setChatfreunde(request);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	public void setChatfreunde(HttpServletRequest request) {
		// Freunde (Online)
		HttpSession session = request.getSession();
		DBConnection dbcon = new DBConnection();
		Connection con = dbcon.getCon();
		StartseiteSql sqlSt = new StartseiteSql();
		List<ChatFreund> chatfreunde = new ArrayList<ChatFreund>();
		try {
			String sql = sqlSt.getFreundeOnlineSql();
			PreparedStatement pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, Integer.parseInt(session.getAttribute("UserID").toString()));
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				String vorname = rs.getString(1);
				String nachname = rs.getString(2);
				int userID = rs.getInt(3);
				ChatFreund freund = new ChatFreund(vorname, nachname, userID, true);
				chatfreunde.add(freund);
			}
		} catch (Exception e) {
			System.out.println("SQL-Fehler ist aufgetreten (ChatFrende)");
		} finally {
			dbcon.close();
		}
		request.setAttribute("chatfreunde", chatfreunde);
	}

}
