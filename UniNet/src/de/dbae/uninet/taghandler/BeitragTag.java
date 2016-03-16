package de.dbae.uninet.taghandler;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;

import javax.servlet.jsp.tagext.TagSupport;

import de.dbae.uninet.javaClasses.Beitrag;

public class BeitragTag extends TagSupport {

	private static final long serialVersionUID = 4129407379981290381L;
	
	private Beitrag beitrag;
	private String page;

	public int doStartTag() {
		Writer out = pageContext.getOut();
		String kopfzeile = "";
		InputStream is;
		try {
			is = pageContext.getServletContext().getResourceAsStream("DesignHTML//Beitrag.txt");
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String zeile = br.readLine();
			while (zeile != null) {
				kopfzeile += zeile;
				zeile = br.readLine();
			}
			br.close();
			isr.close();
			is.close();
			kopfzeile = kopfzeile.replace("USERID", beitrag.getUserID() + "");
			kopfzeile = kopfzeile.replace("BEITRAGSID", beitrag.getBeitragsID() + "");
			kopfzeile = kopfzeile.replace("PAGE", page);
			kopfzeile = kopfzeile.replace("NAME", beitrag.getName());
			kopfzeile = kopfzeile.replace("TIMESTAMP", beitrag.getTimeStamp());
			kopfzeile = kopfzeile.replace("NACHRICHT", beitrag.getNachricht());
			kopfzeile = kopfzeile.replace("ANZAHLLIKES", beitrag.getLikes() + "");
			kopfzeile = kopfzeile.replace("ANZAHLKOMMENTARE", beitrag.getKommentare() + "");
			out.append(kopfzeile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}
	
	public Beitrag getBeitrag() {
		return beitrag;
	}

	public void setBeitrag(Beitrag beitrag) {
		this.beitrag = beitrag;
	}
	
	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}
}
