package com.serli.javaone.jaspic.module;

import com.serli.javaone.jaspic.module.service.MockAuthenticationService;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.MessagePolicy;
import javax.security.auth.message.callback.CallerPrincipalCallback;
import javax.security.auth.message.callback.GroupPrincipalCallback;
import javax.security.auth.message.module.ServerAuthModule;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomServerAuthModule implements ServerAuthModule {

    private static final Logger logger = Logger.getLogger(CustomServerAuthModule.class.getName());

    // SAM constants
    private static final String AUTH_TYPE_INFO_KEY = "javax.servlet.http.authType";
    private static final Class[] SUPPORTED_MESSAGE_TYPES = {HttpServletRequest.class, HttpServletResponse.class};

    // SAM properties
    private static final String DEBUG_PROPERTY = "debug";
    private static final String GROUPS_PROPERTY = "groups";

    // Protocol headers
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String WWW_AUTHENTICATE_HEADER = "WWW-Authenticate";

    // Session keys
    private static final String RETURN_URL = "ReturnUrl";
    private static final String USER_TOKEN = "Token";

    // Configuration state
    private MessagePolicy reqpol;
    private CallbackHandler handler;
    private boolean debug;
    private ArrayList<String> groups = new ArrayList<String>();

    @Override
    public void initialize(MessagePolicy requestPolicy, MessagePolicy responsePolicy, CallbackHandler callbackHandler, Map options) throws AuthException {
        // Store parameters
        this.reqpol = requestPolicy;
        this.handler = callbackHandler;

        // Read and store module's options
        String dbg = (String) options.get(DEBUG_PROPERTY);
        if (dbg!=null)
            this.debug = dbg.equals("true");
        parseGroups((String) options.get(GROUPS_PROPERTY));
    }
    private void parseGroups(String groupList) {
        if (groupList!=null) {
            // Separate group names by commas
            StringTokenizer tok = new StringTokenizer(groupList, ",");
            // Add each group to the group list
            while (tok.hasMoreTokens()) {
                groups.add(tok.nextToken());
            }
        }
    }

    @Override
    public Class[] getSupportedMessageTypes() {
        // Servlet messages only are supported
        return SUPPORTED_MESSAGE_TYPES;
    }

    @Override
    public AuthStatus validateRequest(MessageInfo messageInfo, Subject clientSubject, Subject serviceSubject) throws AuthException {
        // If authentication not required
        if (!reqpol.isMandatory()) {
            // Do not perform authentication
            return AuthStatus.SUCCESS;
        }

        // If authentication is required

        // Get input and output messages
        HttpServletRequest hsreq = (HttpServletRequest) messageInfo.getRequestMessage();
        HttpServletResponse hsres = (HttpServletResponse) messageInfo.getResponseMessage();

        // If the session contains authentication information, use it
        if (hsreq.getSession().getAttribute(USER_TOKEN) != null) {
            // Retrieve the identifiers in the HTTP session
            String token = (String) hsreq.getSession().getAttribute(USER_TOKEN);
            // Perform authentication
            String[] name = new String[1];
            if (MockAuthenticationService.authenticate(token, name)) {
                // If authentication succeeded, "commit"
                if (commitAuthentication(messageInfo, clientSubject, token, name)) {
                    if (debug)  System.out.println("SAM: SUCCESS");
                    return AuthStatus.SUCCESS;
                } else {
                    hsres.setStatus(hsres.SC_FORBIDDEN);
                    if (debug)  System.out.println("SAM: SEND_FAILURE");
                    return AuthStatus.SEND_FAILURE;
                }
            }
        }
        else {
            String authorizationHeader = hsreq.getHeader(AUTHORIZATION_HEADER);
            // If the request contains authentication info
            if (authorizationHeader != null) {
                // Read token from request header
                String token = authorizationHeader.substring(10);
                if (debug)  System.out.println("SAM: Received token " + token);
                // Perform actual authentication
                String name[] = new String[1];
                if (MockAuthenticationService.authenticate(token, name)) {
                    // If authentication succeeded, "commit"
                    if (commitAuthentication(messageInfo, clientSubject, token, name)) {
                        // If commit succeeded, redirect the user to the webpage he asked for
                        try {
                            Object returnUrl = hsreq.getSession().getAttribute(RETURN_URL);
                            if (returnUrl != null) {
                                hsres.sendRedirect((String) returnUrl);
                            }
                        } catch (IOException ex) {
                            logger.log(Level.SEVERE, null, ex);
                        }
                        if (debug)  System.out.println("SAM: SEND_SUCCESS");
                        return AuthStatus.SEND_SUCCESS;
                    }
                    //else return AuthStatus.SEND_FAILURE;
                }
            }
            // The request does not contain authentication info
            else {
                // Store url for redirection
                StringBuilder returnUrl = new StringBuilder(hsreq.getRequestURI());
                if (hsreq.getQueryString() != null) {
                    returnUrl.append(hsreq.getQueryString());
                }
                hsreq.getSession().setAttribute(RETURN_URL, returnUrl.toString());
                // Send a preliminary response with WWW-Authenticate header to gather authentication information
                if (debug)  System.out.println("SAM: Sending authentication header");
                hsres.addHeader(WWW_AUTHENTICATE_HEADER, "Negotiate");
                hsres.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return AuthStatus.SEND_CONTINUE;
            }
        }
        // If an error occurred, return "failure"
        hsres.setStatus(hsres.SC_FORBIDDEN);
        if (debug)  System.out.println("SAM: SEND_FAILURE");
        return AuthStatus.SEND_FAILURE;
    }
    private boolean commitAuthentication(MessageInfo messageInfo, Subject clientSubject, String token, String[] name) {
        try {
            // Set subject's NamePrincipal
            CallerPrincipalCallback cPCB = new CallerPrincipalCallback(clientSubject, name[0]);
            // Set subject's GroupPrincipals
            String[] the_groups = new String[groups.size()];
            for (int i=0; i<groups.size(); i++)
                the_groups[i] = groups.get(i);
            handler.handle(new Callback[] {cPCB, new GroupPrincipalCallback(cPCB.getSubject(), the_groups)});

            // Set Authentication Type
            messageInfo.getMap().put(AUTH_TYPE_INFO_KEY, CustomServerAuthModule.class.getCanonicalName());

            // Store token in session
            HttpServletRequest hsreq = (HttpServletRequest) messageInfo.getRequestMessage();;
            hsreq.getSession().setAttribute(USER_TOKEN, token);

            return true;
        } catch (UnsupportedCallbackException uce) {
            logger.log(Level.SEVERE, "Failed to handle CallerPrincipalCallback", uce);
            return false;
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public AuthStatus secureResponse(MessageInfo messageInfo, Subject serviceSubject) throws AuthException {
        return AuthStatus.SEND_SUCCESS;
    }

    @Override
    public void cleanSubject(MessageInfo messageInfo, Subject subject) throws AuthException {
        if (subject!=null) {
            subject.getPrincipals().clear();
        }
    }
}
