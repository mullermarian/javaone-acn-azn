package com.serli.javaone.jaas.acn.cbh;

import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import java.util.logging.Logger;

public class Demo1Cbh {

    private static final Logger logger = Logger.getLogger(Demo1Cbh.class.getName());

    public static void main(String[] args) {
        try {
            LoginContext lc = new LoginContext("MyLoginConfig", new MyCallbackHandler());
            lc.login();
            // Login succeeded
            logger.info("Authentication successful");
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

}
