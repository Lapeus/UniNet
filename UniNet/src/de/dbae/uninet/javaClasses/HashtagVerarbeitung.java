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
		// Neue Liste fuer die Hashtags
		hashtags = new ArrayList<String>();
		// Wenn die Eingabe mit # beginnt
		if (eingabe.startsWith("#")) 
			// Muss aus Gruenden ein Leerzeichen davor gesetzt werden
			eingabe = " " + eingabe;
		// Solange noch zu ersetzende Hashtags in der Eingabe vorhanden sind
		while (eingabe.contains(" #") || eingabe.contains("<br>#")) {
			// Index des #-Zeichens nach Leerzeichen
			int index = eingabe.indexOf(" #") + 1;
			System.out.println("Index = " + index);
			// Wenn kein #-Zeichen vorgekommen ist
			if (index == 0)
				// Index des #-Zeichens nach Zeilenumbruch
				index = eingabe.indexOf("<br>#") + 4;
			System.out.println("Index = " + index);
			int secondIndex;
			// Index des naechsten Leerzeichens
			int secondIndex1 = eingabe.substring(index).indexOf(" ");
			// Index des naechsten Zeilenumbruchs
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
			if (Character.getNumericValue(eingabe.charAt(secondIndex - 1)) == -1 && secondIndex != eingabe.length()) 
				secondIndex--;
			String hashtag = eingabe.substring(index, secondIndex);
			hashtags.add(hashtag);
			System.out.println(eingabe);
			eingabe = eingabe.substring(0, index) + "<a href='HashtagServlet?tag=" + hashtag.substring(1) + "'>" + hashtag + "</a>"
					+ eingabe.substring(secondIndex);
			System.out.println(eingabe);
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
