package de.dbae.uninet.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

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
				System.out.println("Das geht doch schon ganz gut");
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
		eingabe = eingabe.replace(":)", "&#x1F60A");
		eingabe = eingabe.replace(":D", "&#x1F603");
		eingabe = eingabe.replace(";)", "&#x1F609");
		eingabe = eingabe.replace("-.-", "&#x1F612");
		eingabe = eingabe.replace(":'(", "&#x1F622");
		eingabe = eingabe.replace(":O", "&#x1F632");
		eingabe = eingabe.replace(":P", "&#x1F61C");
		eingabe = eingabe.replace(":*", "&#x1F618");
		eingabe = eingabe.replace("(stop)", "&#x270B");
		eingabe = eingabe.replace("(peace)", "&#x270C");
		eingabe = eingabe.replace("(check)", "&#x2714");
		eingabe = eingabe.replace("(cross)", "&#x2716");
		eingabe = eingabe.replace("(snow)", "&#x2744");
		eingabe = eingabe.replace("<3", "&#x2764");
		eingabe = eingabe.replace("(bus)", "&#x1F68C");
		eingabe = eingabe.replace("(taxi)", "&#x1F695");
		eingabe = eingabe.replace("(car)", "&#x1F697");
		eingabe = eingabe.replace("(door)", "&#x1F6AA");
		eingabe = eingabe.replace("(dont)", "&#x1F6AB");
		System.out.println("Eingabe: " + eingabe);
		return eingabe;
	}

}
