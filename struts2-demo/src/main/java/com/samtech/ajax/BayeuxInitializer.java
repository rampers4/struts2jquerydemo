package com.samtech.ajax;

import java.io.IOException;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.cometd.Bayeux;

public class BayeuxInitializer extends GenericServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7798053067499129594L;

	@Override
	 public void init() throws ServletException
	    {
	        Bayeux bayeux = (Bayeux)getServletContext().getAttribute(Bayeux.ATTRIBUTE);
	        ChatService chatService = new ChatService(bayeux,"chat");
	        getServletContext().setAttribute("chat_service", chatService);
	    }
	
	@Override
	public void service(ServletRequest arg0, ServletResponse arg1)
			throws ServletException, IOException {
		throw new ServletException();

	}

}
