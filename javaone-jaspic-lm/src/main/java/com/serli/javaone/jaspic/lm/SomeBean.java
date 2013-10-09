package com.serli.javaone.jaspic.lm;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;

@Stateless
@DeclareRoles({"AuthenticatedUser", "Administrator"})
public class SomeBean {

    public String publicOperation() {
        return "a public value";
    }

    @RolesAllowed("AuthenticatedUser")
    public String securedOperation() {
        return "a private value";
    }

    @RolesAllowed("Administrator")
    public String adminOperation() {
        return "an admin-only value";
    }
}
