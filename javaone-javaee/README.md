Demo 4
======

This module demonstrates the use of Java EE security constraints and realms.
It defines 2 servlets that call a "secured" EJB. One servlet is public, the other requires the user to authenticate using the HTTP Basic authentication mechanism.


Running this demo
=================

This demo should be deployed in a GlassFish 4 server.

To configure the 'file' realm in GlassFish 4:

	+ Log into the web-based administration console: http://localhost:4848/
	+ Go to Configuration > Security > Realms
	+ Click on the 'file' realm
	+ From there, you can:
		+ Define a group that all users will be assigned with (set 'users')
		+ Manage the users (create a simple user without groups, and a user with the ID 'admin')


To configure users/groups to roles mapping, modify the glassfish-web.xml deployment descriptor to map each role to a set of users and/or groups:

	<security-role-mapping>
        <role-name>AuthenticatedUser</role-name>
        <group-name>users</group-name>
    </security-role-mapping>
	<security-role-mapping>
        <role-name>Administrator</role-name>
        <principal-name>admin</principal-name>
    </security-role-mapping>

WARNING: The logout JSP destroys the session, but most browsers remember the username and password that were typed by the user, and re-send them automatically on the next requests. To log in as another user, you will need to clear the browser history or cache.
