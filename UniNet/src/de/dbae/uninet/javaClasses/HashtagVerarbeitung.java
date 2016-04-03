package de.dbae.uninet.javaClasses;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HashtagVerarbeitung {

	private String eingabe;
	private List<String> hashtags;
	
	public HashtagVerarbeitung(String eingabe) {
		this.eingabe = eingabe;
	}
	
	public String setHashTags() {
		hashtags = new ArrayList<String>();
		// Hashtag am Anfang
		if (eingabe.startsWith("#")) 
			eingabe = " " + eingabe;
		while (eingabe.contains(" #") || eingabe.contains("<br>#")) {
			int index = eingabe.indexOf(" #") + 1;
			if (index == 0)
				index = eingabe.indexOf("<br>#") + 4;
			int secondIndex;
			int secondIndex1 = eingabe.substring(index).indexOf(" ");
			int secondIndex2 = eingabe.substring(index).indexOf("<br>");
			// Wenn beides drin ist
			if (secondIndex1 >= 0 && secondIndex2 >= 0)
				// Nimm das mit dem kleineren Index
				secondIndex = Math.min(secondIndex1, secondIndex2);
			else 
				// Nimm das, was nicht -1 ist, also das groessere
				secondIndex = Math.max(secondIndex1, secondIndex2);
			// Wenn beide -1 sind
			if (secondIndex == -1) 
				// Nimm das letzte Zeichen
				secondIndex = eingabe.substring(index).length();
			secondIndex += index;
			String hashtag = eingabe.substring(index, secondIndex);
			hashtags.add(hashtag);
			eingabe = eingabe.replace(hashtag, "<a href='HashtagServlet?tag=" + hashtag + "'>" + hashtag + "</a>");
		}
		return eingabe;
	}
	
	public List<String> getAndSetHashTags() {
		setHashTags();
		return hashtags;
	}
	
	public String getEingabe() {
		return eingabe;
	}
	
	public String setHashTags(Connection con, int beitragsID) throws SQLException {
		String eingabe = setHashTags();
		for (String hashtag : hashtags) {
			String sql = "INSERT INTO hashtags(beitragsID, hashtag) VALUES (?, ?)";
			PreparedStatement pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, beitragsID);
			pStmt.setString(2, hashtag);
			pStmt.executeUpdate();
		}
		return eingabe;
	}
	
	public void setHashTags(Connection con, List<String> hashtags, int beitragsID) throws SQLException {
		for (String hashtag : hashtags) {
			String sql = "INSERT INTO hashtags(beitragsID, hashtag) VALUES (?, ?)";
			PreparedStatement pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, beitragsID);
			pStmt.setString(2, hashtag);
			pStmt.executeUpdate();
		}
	}
}
