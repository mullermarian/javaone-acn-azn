Demo 5
======

This module demonstrates the use of Java EE security constraints and JASPIC authentication.
It defines 2 servlets that call a "secured" EJB. One servlet is public, the other requires the user to authenticate using the OpenID JASPIC authentication provider.


Running this demo
=================

This demo should be deployed in a GlassFish 4 server.

To configure the OpenID provider in GlassFish 4:

	+ Copy all the JARs from src/test/resources to glassfish4/glassfish/lib/
	+ Start the GlassFish server
	+ Log into the web-based administration console: http://localhost:4848/
	+ Go to Configuration > Security > Message Layer > HttpServlet
	+ Switch to the Providers tab
	+ Create a new provider, and specify:
		+ The name: 'OpenID4JavaProvider'
		+ The type 'server'
		+ The class name 'org.imixs.openid.openid4java.OpenID4JavaAuthModule'

To configure the application to use your provider, modify the glassfish-web.xml deployment descriptor:

	<glassfish-web-app httpservlet-security-provider="OpenID4JavaProvider">

You will need an OpenID account to test this application. There are several OpenID providers that you can pick.