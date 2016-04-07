package de.dbae.uninet.servlets;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import de.dbae.uninet.dbConnections.DBConnection;

/**
 * Dieses Servlet speichert ein hochgeladenes Profilbild in der DB ab oder l&auml;dt ein abgespeichertes Profilbild.
 * @author Christian Ackermann
 */
@WebServlet("/LadeProfilbildServlet")
@MultipartConfig
public class LadeProfilbildServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LadeProfilbildServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Die ID des Users, dessen Bild geladen werden soll
		int userID;
		// Oeffne neue DB-Verbindung
		DBConnection dbcon = new DBConnection();
		Connection con = dbcon.getCon();
		try {
			if (request.getParameter("userID") == null || request.getParameter("userID").equals("")) {
				userID = Integer.parseInt(request.getSession().getAttribute("UserID").toString());
			} else {
				userID = Integer.parseInt(request.getParameter("userID"));
			}
			// Sql-Statement um das Profilbild zu bekommen
			String sql = "SELECT profilbild FROM nutzer WHERE userID = ?";
			PreparedStatement pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, userID);
			ResultSet rs = pStmt.executeQuery();
			if (rs.next()) {
				// ByteArray aus der DB laden
				byte[] byteArray = rs.getBytes(1);
				// Neues Bild daraus erzeugen
				BufferedImage img = ImageIO.read(new ByteArrayInputStream(byteArray));
				// Das Bild an das Response-Objekt anhaengen
				ImageIO.write(img, "jpg", response.getOutputStream());
			}
		} catch (NullPointerException npex) {
			response.sendRedirect("FehlerServlet?fehler=Session");
		} catch (SQLException sqlex) {
			response.sendRedirect("FehlerServlet?fehler=DBCon");
		} finally {
			// Verbindung schliessen
			dbcon.close();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DBConnection dbcon = new DBConnection();
		Connection con = dbcon.getCon();
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		try {
			List<FileItem> items = upload.parseRequest(request);
			for (FileItem item : items) {
				InputStream is = item.getInputStream();
				BufferedImage img = ImageIO.read(is);
				int seite = Math.min(img.getHeight(), img.getWidth());
				img = img.getSubimage(Math.round((img.getWidth() - seite) / 2), Math.round((img.getHeight() - seite) / 2), seite, seite); 
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(img, "jpg", baos);
				String sql = "UPDATE nutzer SET profilbild = ? WHERE userID = ?";
				PreparedStatement pStmt = con.prepareStatement(sql);
				pStmt.setBytes(1, baos.toByteArray());
				pStmt.setInt(2, Integer.parseInt(request.getSession().getAttribute("UserID").toString()));
				pStmt.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO Fehler
		} finally {
			// Verbindung schliessen
			dbcon.close();
			// Weiterleitung
			response.sendRedirect("ProfilBearbeitenServlet");
		}
	}
	
}
