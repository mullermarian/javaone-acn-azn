Demo 3
======

This module demonstrates how to write a custom JAAS login module. The module only checks that the username and password are equal.


Running this demo
=================

To define a custom JAAS configuration file, use the system property:

	-Djava.security.auth.login.config=src/test/resources/login.config

To define a custom security policy file, use the system property:

	-Djava.security.policy=src/test/resources/java.policy


Note: the user 'user1' has the custom permission (defined in java.policy file).