package com.serli.javaone.jaspic;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PrivateServlet extends HttpServlet {

    @EJB
    private SomeBean someBean;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("user", req.getRemoteUser());

        try {
            req.setAttribute("publicResult", someBean.publicOperation());
        }
        catch (EJBException e) {
            req.setAttribute("publicResult", "Access denied");
        }
        try {
            req.setAttribute("privateResult", someBean.securedOperation());
        }
        catch (EJBException e) {
            req.setAttribute("privateResult", "Access denied");
        }
        try {
            req.setAttribute("adminResult", someBean.adminOperation());
        }
        catch (EJBException e) {
            req.setAttribute("adminResult", "Access denied");
        }

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/view/private.jsp");
        dispatcher.forward(req, resp);
    }
}
