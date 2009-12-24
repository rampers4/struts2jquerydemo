package com.samtech.jetty.test;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.bio.SocketConnector;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;


/*import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.ajp.Ajp13SocketConnector;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.webapp.WebAppContext;*/

public class Struts2Start {

	public static void main(String[] args) throws Exception {
		Server server = new Server();
		
		SocketConnector connector =new SocketConnector();// new Ajp13SocketConnector();
		
		// Set some timeout options to make debugging easier.
		connector.setMaxIdleTime(1000 * 60 * 60);
		connector.setSoLingerTime(-1);
		connector.setPort(8088);
		//connector.setConfidentialPort(8443);
		//connector.setConfidentialScheme("https");
		/*SslSocketConnector sslconnector = new SslSocketConnector();
		sslconnector.setPort(8443);
		sslconnector.setMaxIdleTime(1000 * 60 * 60);
		sslconnector.setSoLingerTime(-1);*/
		server.setConnectors(new Connector[] { connector});
		
		Class clazz=org.slf4j.Logger.class;
		WebAppContext bb = new WebAppContext();
		//bb.setClassLoader(TapestryFilter.class.getClassLoader());
		bb.setServer(server);
		bb.setContextPath("/");
		bb.setWar("src/main/webapp");
		
		// START JMX SERVER
		// MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
		// MBeanContainer mBeanContainer = new MBeanContainer(mBeanServer);
		// server.getContainer().addEventListener(mBeanContainer);
		// mBeanContainer.start();
		
		
		/*
		ServletHolder holder=new ServletHolder(new JspServlet());
        bb.addServlet(holder, "*.jsp");
        */
		server.setHandler(bb);
		//server.addHandler(bb);

		try {
			System.out.println(">>> STARTING EMBEDDED JETTY SERVER, PRESS ANY KEY TO STOP");
			server.start();
			System.in.read();
			System.out.println(">>> STOPPING EMBEDDED JETTY SERVER"); 
            // while (System.in.available() == 0) {
			//   Thread.sleep(5000);
			// }
			server.stop();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(100);
		}
	}
}
