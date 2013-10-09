package com.serli.javaone.jaas.lm.module;

import java.security.Principal;

public class CustomPrincipal implements Principal {

    private String username;

    public CustomPrincipal(String username) {
        if (username == null)
            throw new IllegalArgumentException("A CustomPrincipal cannot be instantiated without a name");
        this.username = username;
    }

    @Override
    public String getName() {
        return username;
    }

    @Override
    public String toString() {
        return "CustomPrincipal : " + username;
    }
}
