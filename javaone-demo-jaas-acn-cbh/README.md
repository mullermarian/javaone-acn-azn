Demo 1 bis
==========

This module demonstrates the use of JAAS to perform authentication. It uses Java's KeyStore login module, which requires user interaction. Interaction is achieved with a callback handler reading the console input.


Running this demo
=================

To define a custom JAAS configuration file, use the system property:

	-Djava.security.auth.login.config=src/test/resources/login.config

To configure a keystore, in the login context set the module option keyStoreURL:

	MyLoginConfig {
		com.sun.security.auth.module.KeyStoreLoginModule REQUISITE debug=true keyStoreURL="file:./src/test/resources/keystore.jks";
	};

To create or modify a keystore, use the keytool command.


Note: for the supplied keystore:
+ The store password is 'changeit'
+ The key alias is 'mykey'
+ The private key password is 'password'
