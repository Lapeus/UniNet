package de.dbae.uninet.taghandler;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import de.dbae.uninet.dbConnections.DBConnection;
import de.dbae.uninet.sqlClasses.NachrichtenSql;

public class ChatTag extends TagSupport {
	private String idUser;
	private String idFreund;
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -419600882859325849L;

	@Override
	public int doStartTag() throws JspException {
		NachrichtenSql nSql = new NachrichtenSql();
		Connection con = new DBConnection().getCon();
		String finalSql = "";
		
		
		try {
			PreparedStatement p = con.prepareStatement(nSql.getName());
			p.setInt(1, Integer.parseInt(idFreund));
			ResultSet r = p.executeQuery();
			r.next();
			String n = r.getString(1) + r.getString(2);
			finalSql += getTopPart(n);
			
			PreparedStatement pStmt = con.prepareStatement(nSql.getNachrichtenListe());
			pStmt.setInt(1, Integer.parseInt(idUser));
			pStmt.setInt(2, Integer.parseInt(idFreund));
			pStmt.setInt(3, Integer.parseInt(idFreund));
			pStmt.setInt(4, Integer.parseInt(idUser));
			ResultSet result = pStmt.executeQuery();
			
			while (result.next()) {
				String name = result.getString(1) +" "+ result.getString(2);
				String nachricht = result.getString(3);
				finalSql += getProNachricht(name ,nachricht);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finalSql += bottomPart();
		try {
			pageContext.getOut().append(finalSql);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}
	public int doEndTag() {
		return EVAL_PAGE;
	}
	
	private String getTopPart(String name) {
		String sql = "";
		sql += "<div class='container'>";
		sql += "<div class='row'>";
		sql += "<div class='panel panel-chat'>";
		sql += "<div class='panel-heading'>";
		sql += "<a href='#' class='chatMinimize' onclick='return false'>";
		sql += "<span>"
				+ name
				+ "</span>";
		sql += "<a href='#' class='chatClose' onclick='return false'><i>class='glyphicon glyphicon-remove'></i></a>";
		sql += "<div class='clearFix'></div>";
		sql += "</div>";
		sql += "<div class='panel-body'>";
		return sql;
	}
	
	private String getProNachricht(String name, String nachricht) {
		String sql = "<div class='messageMe'>"
				+ name
				+ "<span>"
				+ nachricht
				+ "</span>"
				+ "<div class='clearFix'></div></div>";
		return sql;
	}
	
	private String bottomPart() {
		String sql = "<div class='panel-footer'><form action='LadeChatFreundeServlet' method='POST'>"
				+ "<div class='send-wrap'>"
				+ "<textarea class='form-control send-message' rows='3'placeholder='Schreibe jetzt...' name='nachricht'></textarea>"
				+ "</div><div class='btn-panel'>"
				+ "<input type='submit' class=' col-lg-3 btn send-message-btn ' name='reload' value='Reload'/>"
				+ "<input type='submit' class='col-lg-4 text-right btn send-message-btn pull-right' name='senden' />"
				+ "</div></form></div></div></div></div>";
		return sql;
	}
	public String getIdUser() {
		return idUser;
	}
	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}
	public String getIdFreund() {
		return idFreund;
	}
	public void setIdFreund(String idFreund) {
		this.idFreund = idFreund;
	}
}
