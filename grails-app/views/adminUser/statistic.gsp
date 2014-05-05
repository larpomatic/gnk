<%--
  Created by IntelliJ IDEA.
  User: Nico
  Date: 20/04/14
  Time: 12:44
--%>

<%@ page import="org.gnk.admin.right" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Gestion Utilisateur</title>
</head>

<body>

<div role="main">
<h3><g:message code="default.title.statistic.user"/></h3>

<g:message code="default.title.user"/>: ${user.firstname} ${user.lastname} <br/>
<g:message code="default.title.lastConnection"/>: ${user.lastConnexion} <br/>
<g:message code="default.title.nbIntrigPub"/>: ${countPublicPlot} <br/>
<g:message code="default.title.nbIntrigPri"/>: ${countPrivatePlot} <br/>
<g:message code="default.title.nbConnection"/>: ${user.countConnexion}
</body>
</html>