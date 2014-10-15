<%--
  Created by IntelliJ IDEA.
  User: Nico
  Date: 20/04/14
  Time: 12:05
--%>

<%@ page import="org.gnk.admin.right" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Gestion Utilisateur</title>
</head>

<body>
<h1>Création d'un utilisateur</h1>
<g:form action="createUser">
    <input name="username" type="text" placeholder="Username"/> <br/>
    <input name="firstname" type="text" placeholder="Firstname"/> <br/>
    <input name="lastname" type="text" placeholder="Lastname"/> <br/>
    <input name="password" type="password" placeholder="Password"/> <br/>
    <input name="passwordRepeat" type="password" placeholder="Password"/> <br/>

    <div class="btn-group">
        <g:link type="button" action="list" class="btn btn-info">Annuler</g:link>
        <button type="submit" class="btn btn-primary">Valider</button>
    </div>
</g:form>

</div>

</body>
</html>