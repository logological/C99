package uk.ac.man.cs.choif.extend;

import java.rmi.*;
import java.net.*;
import java.util.*;
/**
 * Extension to RMI
 * Creation date: (10/28/99 14:43:33)
 * @author: Freddy Choi
 */
public class RMIx {
	/** Host : Ermintrude (Pentium II at home) */
	public final static URL ermintrude = defineHost("ermintrude.home", 2000, "/root/work/tools");

	/** Host : DoorStop (Sparc IPC in office) */
	public final static URL doorstop = defineHost("r8p.cs.man.ac.uk", 2000, "/home/M97/mphil/choif/work/tools");

/**
 * Connect to the default RMI server
 * Creation date: (10/28/99 15:51:54)
 * @return java.rmi.Remote Server or null if unsuccessful
 * @param name java.lang.String Server name
 */
public final static Remote connect(String name) {
	return connect(Resourcex.rmiHost, name);
}
/**
 * Connect to a specific RMI server
 * Creation date: (10/28/99 15:51:54)
 * @return java.rmi.Remote Server or null if unsuccessful
 * @param host java.net.URL Server host
 * @param name java.lang.String Server name
 */
public final static Remote connect(URL host, String name) {
	//String sname = host.getProtocol() + "://" + host.getHost() + ":" + host.getPort() + "/" + name;
	String sname = "//" + host.getHost() + ":" + host.getPort() + "/" + name;
	try {
		setSystemProperties(host);
		Debugx.msg("RMIx", "Connecting to RMI server (" + sname + ")...");
		Remote server = Naming.lookup(sname);
		if (server != null) {
			Debugx.msg("RMIx", "Connected to server.");
			return server;
		}
		else {
			Debugx.msg("RMIx", "Connection failure.");
			return null;
		}
	}
	catch (Exception e) { 
		Debugx.handle(e);
		Debugx.msg("RMIx", "Connection failure.");
		return null;
	}
}
/**
 * Define a RMI host
 * Creation date: (11/05/99 02:26:37)
 * @return java.net.URL
 * @param hostname java.lang.String
 * @param port int
 * @param codebase java.lang.String
 */
public final static URL defineHost(String hostname, int port, String codebase) {
	try {
		URL host = new URL("http", hostname, port, codebase);
		return host;
	}
	catch (MalformedURLException e) {
		Debugx.handle(e);
		return null;
	}
}
/**
 * Set all the system properties required to use RMI
 * Creation date: (10/28/99 14:48:39)
 * @param host Host RMI Host, e.g. ermintrude
 */
private final static void setSystemProperties(URL host) {
	Properties P = System.getProperties();
	P.put("java.rmi.server.codebase", host.getFile());
	P.put("java.rmi.server.hostname", host.getHost());
	P.put("java.security.policy", "java.security");
	if (System.getSecurityManager() == null) System.setSecurityManager(new RMISecurityManager());
}
/**
 * Start a RMI server with the default host
 * Creation date: (10/28/99 15:41:47)
 * @return boolean Server start was successful or not.
 * @param name java.lang.String Server name
 * @param server java.rmi.Remote
 */
public final static boolean startServer(String name, Remote server) {
	return startServer(Resourcex.rmiHost, name, server);
}
/**
 * Start a RMI server
 * Creation date: (10/28/99 15:41:47)
 * @return boolean Server start was successful or not.
 * @param host java.net.URL Server host
 * @param name java.lang.String Server name
 * @param server java.rmi.Remote
 */
public final static boolean startServer(URL host, String name, Remote server) {
	try {
		setSystemProperties(host);
		//String sname = host.getProtocol() + "://" + host.getHost() + ":" + host.getPort() + "/" + name;
		String sname = "//" + host.getHost() + ":" + host.getPort() + "/" + name;
		Debugx.msg("RMIx", "Starting RMI server (" + sname + ")...");
		Naming.rebind(sname, server);
		Debugx.msg("RMIx", "Server ready.");
		return true;
	} catch (Exception e) {
		Debugx.msg("RMIx", "Unable to start server.");
		Debugx.handle(e);
		return false;
	}

}
}
