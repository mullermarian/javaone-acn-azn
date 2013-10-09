Demo 2
======

This module demonstrates the use of JAAS to perform authorization. It uses Java's Windows NT login module, and therefore requires to be run on Windows.
It prints the authenticated subject's principals and credentials to the console, and checks whether the subject has an application-specific permission.


Running this demo
=================

To define a custom JAAS configuration file, use the system property:

	-Djava.security.auth.login.config=src/test/resources/login.config

To define a custom security policy file, use the system property:

	-Djava.security.policy=src/test/resources/java.policy


In order to successfully execute this demo, you should alter the java.policy file to match your Windows username.