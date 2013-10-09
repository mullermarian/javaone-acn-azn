Demo 6 Web application
======================

This module demonstrates how to use the custom JASPIC ServerAuthModule.


Running this demo
=================

This demo should be deployed in a GlassFish 4 server.

To configure the custom provider in GlassFish 4:

	+ Log into the web-based administration console: http://localhost:4848/
	+ Go to Configuration > Security > Message Layer > HttpServlet
	+ Switch to the Providers tab
	+ Create a new provider, and specify:
		+ The name: 'CustomProvider'
		+ The type 'server'
		+ The class name 'com.serli.javaone.jaspic.module.CustomServerAuthModule'

To configure the application to use your provider, modify the glassfish-web.xml deployment descriptor:

	<glassfish-web-app httpservlet-security-provider="CustomProvider">
	
To configure an administrator user, modify the glassfish-web.xml deployment descriptor to map to your user (the principal name is user_<first 5 characters of the token>):

	<security-role-mapping>
        <role-name>Administrator</role-name>
        <principal-name>user_ABCDE</principal-name>
    </security-role-mapping>

You will need to use a browser that supports sending SPNEGO token. This may also require browser configuration.