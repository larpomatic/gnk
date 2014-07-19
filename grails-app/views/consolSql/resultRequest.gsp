<%--
  Created by IntelliJ IDEA.
  User: Nico
  Date: 10/07/14
  Time: 22:48
--%> 18:33
--%>

<%@ page import="org.gnk.admin.right" contentType="text/html;charset=UTF-8" %>
<html xmlns="http://www.w3.org/1999/html">
    <head>
        <meta name="layout" content="main"/>
        <title>Result</title>
    </head>
    <body>
        <g:form action="terminal" method="post">
            <button type="submit" class="btn btn-info btn-large">Retour</button>
        </g:form>
        ${result}
    </body>
</html>