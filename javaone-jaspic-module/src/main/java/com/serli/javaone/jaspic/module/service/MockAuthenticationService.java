package com.serli.javaone.jaspic.module.service;

public class MockAuthenticationService {

    public static boolean authenticate(String token, String[] name) {
        name[0] = "user_" + token.substring(0, 5);
        return true;
    }

}
