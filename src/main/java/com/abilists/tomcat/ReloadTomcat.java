package com.abilists.tomcat;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.Container;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;

public class ReloadTomcat extends ValveBase {

	private static final String RELOAD_CONTEXT_URI = "/client/reloadContext";

	@Override
	public void invoke(Request request, Response response) throws IOException, ServletException {

		Container container = getContainer();

		String requestUri = request.getRequestURI();
		String reloadUri = request.getContextPath() + RELOAD_CONTEXT_URI;
 
		if (requestUri.startsWith(reloadUri) && container instanceof Context) {
			reloadContext(response, container);
			return;
		}
 
		getNext().invoke(request, response);
	}

	private void reloadContext(Response response, Container container) throws IOException {
		((Context) container).reload();
		HttpServletResponse httpResponse = response.getResponse();
 
		httpResponse.setContentType("text/plain;charset=utf-8");
		httpResponse.getWriter().write("Context Reloaded!!");
		httpResponse.getWriter().close();
		return;
	}

}
