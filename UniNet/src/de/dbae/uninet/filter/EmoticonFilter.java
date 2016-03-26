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

import de.dbae.uninet.javaClasses.Emoticon;
import de.dbae.uninet.servlets.HilfeServlet;

public class EmoticonFilter implements Filter {

	protected FilterConfig config;
	
	@Override
	public void destroy() {
		config = null;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)req;
		String[] attributes = {"kommentar", "nachricht", "beitrag"};
		for (String attribute : attributes) {
			if (request.getParameter(attribute) != null) {
				request.setAttribute(attribute, getString(request.getParameter(attribute).toString()));
			}
		}
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		config = arg0;
	}
	
	private String getString(String eingabe) {
		List<Emoticon> emoticons = new HilfeServlet().getEmoticons();
		for (Emoticon emo : emoticons) {
			eingabe = eingabe.replace(emo.getCode(), emo.getBild());
		}
		return eingabe;
	}

}
