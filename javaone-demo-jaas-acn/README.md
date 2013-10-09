Demo 1
======

This module demonstrates the use of JAAS to perform authentication. It uses Java's Windows NT login module, and therefore requires to be run on Windows.


Running this demo
=================

To define a custom JAAS configuration file, use the system property:

	-Djava.security.auth.login.config=src/test/resources/login.config