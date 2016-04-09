package de.dbae.uninet.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * Dieser Filter &uuml;bersetzt fehlerhaft kodierte Umlaute in Html-Code, dabei sie korrekt angezeigt werden k&ouml;nnen.
 * @author Christian Ackermann
 */
public class UmlauteFilter implements Filter {

	/**
	 * FilterConfig
	 */
	protected FilterConfig config;
	
	@Override
	public void destroy() {
		config = null;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		// Cast von ServletRequest nach HttpServletRequest, da sonst die Parameter nicht auszulesen sind
		HttpServletRequest request = (HttpServletRequest)req;
		if (!ServletFileUpload.isMultipartContent(request)) {
			// Auflistung aller existenter Parameternamen
			Enumeration<String> parameters = request.getParameterNames();
			// Iteration ueber alle Namen
			while (parameters.hasMoreElements()) {
				// Der aktuelle Parameter
				String parameter = parameters.nextElement();
				// Ersetze alle Umlaute des aktuellen Parameters durch ihre Html-Codierungen
				request.setAttribute(parameter, getString(request.getParameter(parameter)));
			}
		}
		chain.doFilter(request, res);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		config = arg0;
	}
	
	/**
	 * Ersetzt alle Umlaute im Eingabe-String durch ihre Html-Codierungen.
	 * @param eingabe Der Eingabe-String
	 * @return Der Eingabe-String mit den ersetzten Umlauten
	 */
	private String getString(String eingabe) {
		// Eine Map fuer alle Umlaute
		Map<String, String> umlaute = new HashMap<String, String>();
		// Fuellung der Map mit dem Umlaut und der entsprechenden Html-Codierung
		umlaute.put("ä", "&auml");
		umlaute.put("Ä", "&Auml");
		umlaute.put("ö", "&ouml");
		umlaute.put("Ö", "&Ouml");
		umlaute.put("ü", "&uuml");
		umlaute.put("Ü", "&Uuml");
		umlaute.put("ß", "&szlig");
		// Zeilenumbruch durch <br> ersetzen
		umlaute.put("\n", "<br>");
		try {
			// Fuer jeden Eintrag der Map
			for (Entry<String, String> e : umlaute.entrySet()) {
				// Ersetze die ISO-8859-1 Codierung des Umlautes im Eingabe-String durch die Html-Codierung
				eingabe = eingabe.replace(new String(e.getKey().getBytes("UTF-8"), "ISO-8859-1"), e.getValue());
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return eingabe;
	}

}
