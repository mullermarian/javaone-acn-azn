package com.serli.javaone.jaas.acn;

import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import java.util.logging.Logger;

public class Demo1 {

    private static final Logger logger = Logger.getLogger(Demo1.class.getName());

    public static void main(String[] args) {
        try {
            LoginContext lc = new LoginContext("MyLoginContext");
            lc.login();
            // Login succeeded
            logger.info("Authentication successful");
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

}
