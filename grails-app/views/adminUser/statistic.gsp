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
<h3>Statistique Utilisateur</h3>
Utilisateur: ${user.firstname} ${user.lastname} <br/>
Dernière connexion: ${user.lastConnexion} <br/>
Nombre d'intrigue public: ${countPublicPlot} <br/>
Nombre d'intrigue privée: ${countPrivatePlot} <br/>
Nombre de connexion: ${user.countConnexion}
</body>
</html>