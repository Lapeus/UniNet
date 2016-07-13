package de.dbae.uninet.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.servlet.ServletFileUpload;

import de.dbae.uninet.javaClasses.Emoticon;
import de.dbae.uninet.servlets.EmoticonServlet;

/**
 * Dieser Filter &uuml;bersetzt K&uuml;rzel f&uuml;r Emoticons in die entsprechenden Unicode-Zeichen, um sie entsprechend
 * anzeigen zu k&ouml;nnen.
 * @see Emoticon
 * @author Christian Ackermann
 *
 */
public class EmoticonFilter implements Filter {

	/**
	 * FilterConfig
	 */
	protected FilterConfig config;
	
	@Override
	public void destroy() {
		config = null;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// Cast von ServletRequest in HttpServletRequest, da sonst die Parameter nicht auszulesen sind
		HttpServletRequest request = (HttpServletRequest)req;
		if (!ServletFileUpload.isMultipartContent(request)) {
			// Alle Parameternamen, in denen die Emoticon-Kuerzel ersetzt werden sollen
			String[] parameters = {"kommentar", "nachricht", "beitrag", "neuerBeitrag"};
			// Fuer jeden Paramter
			for (String parameter : parameters) {
				// Wenn der Parameter gesetzt wurde
				if (request.getAttribute(parameter) != null && !request.getAttribute(parameter).equals("")) {
					// Setze den ersetzten Parameter als Attribut
					request.setAttribute(parameter, getString(request.getAttribute(parameter).toString()));
				}
			}
		}
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		config = arg0;
	}
	
	/**
	 * Ersetzt alle K&uuml;rzel im Eingabe-String durch ihre Unicode-Zeichen.
	 * @param eingabe Der Eingabe-String
	 * @return Der Eingabe-String mit den Ersetzungen
	 */
	private String getString(String eingabe) {
		// Lade alle Emoticons
		List<Emoticon> emoticons = new EmoticonServlet().getEmoticons();
		for (Emoticon emo : emoticons) {
			// Ersetze alle Kuerzel durch ihre Unicode-Zeichen
			eingabe = eingabe.replace(emo.getCode(), emo.getBild());
		}
		return eingabe;
	}

}
