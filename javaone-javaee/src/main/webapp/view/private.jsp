<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
    <head>
        <title>Secured page</title>
    </head>
    <body>
        <h1>Authentication succeeded !</h1>
        <p>Authenticated user: ${user}</p>
        <h1>Call to EJB methods:</h1>
        <ul>
            <li>Public operation : ${publicResult}</li>
            <li>Private operation : ${privateResult}</li>
            <li>Admin operation : ${adminResult}</li>
        </ul>
        <p><a href="${pageContext.request.contextPath}/logout">Log out and back to public area</p>
    </body>
</html>