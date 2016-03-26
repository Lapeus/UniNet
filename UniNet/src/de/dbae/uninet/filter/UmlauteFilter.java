package de.dbae.uninet.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

public class UmlauteFilter implements Filter {

	protected FilterConfig config;
	
	@Override
	public void destroy() {
		config = null;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)req;
		String[] attributes = {"kommentar", "nachricht", "beitrag"};
		for (String attribute : attributes) {
			if (request.getParameter(attribute) != null) {
				request.setAttribute(attribute, getString(request.getParameter(attribute).toString()));
			}
		}
		chain.doFilter(request, res);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		config = arg0;
	}
	
	private String getString(String eingabe) {
		Map<String, String> umlaute = new HashMap<String, String>();
		umlaute.put("ä", "&auml");
		umlaute.put("Ä", "&Auml");
		umlaute.put("ö", "&ouml");
		umlaute.put("Ö", "&Ouml");
		umlaute.put("ü", "&uuml");
		umlaute.put("Ü", "&Uuml");
		umlaute.put("ß", "&szlig");
		try {
			for (Entry<String, String> e : umlaute.entrySet()) {
				eingabe = eingabe.replace(new String(e.getKey().getBytes("UTF-8"), "ISO-8859-1"), e.getValue());
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return eingabe;
	}

}
