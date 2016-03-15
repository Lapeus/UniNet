package de.dbae.uninet.taghandler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;

import javax.servlet.ServletContext;
import javax.servlet.jsp.tagext.TagSupport;

public class KopfzeileTag extends TagSupport {

	private static final long serialVersionUID = -5650080610199722432L;
	
	public int doStartTag() {
		Writer out = pageContext.getOut();
		try {
			out.append(getHtmlCode(pageContext.getServletContext()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SKIP_BODY;
	}
	
	public String getHtmlCode(ServletContext servletContext) {
		String kopfzeile = "";
		InputStream is;
		try {
			is = servletContext.getResourceAsStream("DesignHTML//Kopfzeile.txt");
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String zeile;
			do {
				zeile = br.readLine();
				kopfzeile += zeile;
			} while (zeile != null);
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return kopfzeile;
	}

}
