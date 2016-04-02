package de.dbae.uninet.sqlClasses;

public class BeziehungsSql {

	public String getSqlStatement(String action) {
		String sql = "";
		switch (action) {
		case "AnzahlNachrichten":
			sql = "SELECT freund, SUM(anzahlNachrichten) AS AnzahlNachrichten FROM "
					+ "(SELECT COUNT(nachrichtID) as AnzahlNachrichten, SenderID, EmpfaengerID FROM nachrichten GROUP BY SenderID, EmpfaengerID) AS Tab1 "
					+ "INNER JOIN (SELECT * FROM freundeView WHERE nutzer = ?) AS Tab2 ON (senderID = nutzer AND empfaengerID = freund OR senderID = freund AND empfaengerID = nutzer) GROUP BY nutzer, freund";
			break;
		case "AnzahlNachrichten30Tage":
			sql = "SELECT freund, SUM(tab1.anzahlNachrichten) AS AnzahlNachrichten FROM "
					+ "(SELECT COUNT(nachrichtID) as AnzahlNachrichten, SenderID, EmpfaengerID FROM nachrichten WHERE DATE_PART('day', ?::timestamp - datum::timestamp) < 30 GROUP BY SenderID, EmpfaengerID) AS Tab1 "
					+ "RIGHT JOIN (SELECT * FROM freundeView WHERE nutzer = ?) AS Tab2 ON (senderID = nutzer AND empfaengerID = freund OR senderID = freund AND empfaengerID = nutzer) GROUP BY nutzer, freund";
			break;
		case "AnzahlGemeinsamerFreunde":
			sql = "SELECT tab1.freund AS Freund, COUNT(tab3.freund) AS Anzahl FROM (SELECT * FROM freundeView WHERE nutzer = ?) AS tab1 "
					+ "INNER JOIN freundeView as Tab2 ON (tab1.freund = tab2.freund AND tab1.nutzer != tab2.nutzer) "
					+ "INNER JOIN freundeView AS Tab3 ON (tab1.nutzer = tab3.nutzer AND tab2.nutzer = tab3.freund) GROUP BY tab1.freund";
			break;
		case "AnzahlGemeinsamerGruppen":
			sql = "SELECT tab2.studentID, COUNT(tab1.gruppenID) FROM (SELECT * FROM gruppenmitglieder WHERE studentID = ?) AS Tab1 "
					+ "INNER JOIN gruppenmitglieder AS Tab2 ON (tab1.gruppenID = tab2.gruppenID) "
					+ "INNER JOIN freundeView ON (tab1.studentID = nutzer AND tab2.studentID = freund) GROUP BY tab2.studentID";
			break;
		case "AnzahlGemeinsamerVeranstaltungen":
			sql = "SELECT tab2.studentID AS Freund, COUNT(tab1.veranstaltungsID) FROM (SELECT * FROM veranstaltungsmitglieder WHERE studentID = ?) AS Tab1 "
					+ "INNER JOIN veranstaltungsmitglieder AS Tab2 ON (tab1.veranstaltungsID = tab2.veranstaltungsID) "
					+ "INNER JOIN freundeView ON (tab1.studentID = nutzer AND tab2.studentID = freund) GROUP BY tab2.studentID";
			break;
		case "AnzahlBeitragLikesSelbst":
			sql = "SELECT verfasserID, COUNT(beitragsID) AS Anzahl FROM (SELECT * FROM beitragLikes WHERE studentID = ?) AS Tab1 "
					+ "NATURAL JOIN (SELECT * FROM beitraege WHERE verfasserID != ?) AS Tab2 GROUP BY verfasserID";
			break;
		case "AnzahlKommentareSelbst":
			sql = "SELECT tab2.verfasserID, COUNT(kommentarID) FROM (SELECT * FROM kommentare WHERE verfasserID = ?) AS Tab1 "
					+ "INNER JOIN (SELECT * FROM beitraege WHERE verfasserID != ?) AS Tab2 USING (beitragsID) GROUP BY tab2.verfasserID";
			break;
		case "AnzahlBeitragLikesFreund":
			sql = "SELECT studentID, COUNT(beitragsID) AS Anzahl FROM (SELECT * FROM beitragLikes WHERE studentID != ?) AS Tab1 "
					+ "NATURAL JOIN (SELECT * FROM beitraege WHERE verfasserID = ?) AS Tab2 GROUP BY studentID";
			break;
		case "AnzahlKommentareFreund":
			sql = "SELECT tab1.verfasserID, COUNT(kommentarID) FROM (SELECT * FROM kommentare WHERE verfasserID != ?) AS Tab1 "
					+ "INNER JOIN (SELECT * FROM beitraege WHERE verfasserID = ?) AS Tab2 USING (beitragsID) GROUP BY tab1.verfasserID";
			break;
		case "EinfuegenVorbereiten":
			sql = "SELECT * FROM freunde WHERE freund1 = ? AND freund2 = ?";
			break;
		case "SetBewertung1":
			sql = "UPDATE freunde SET bewertung1 = ? WHERE freund1 = ? AND freund2 = ?";
			break;
		case "SetBewertung2":
			sql = "UPDATE freunde SET bewertung2 = ? WHERE freund2 = ? AND freund1 = ?";
			break;
		case "EngsterFreund":
			sql = "SELECT freund FROM freundeView WHERE nutzer = ? AND bewertung = (SELECT Max(bewertung) FROM freundeView WHERE nutzer = ?)";
			break;
		default:
			System.err.println("FEHLER IM BEZIEHUNGSSQL");
			break;
		}
		return sql;
	}
}
