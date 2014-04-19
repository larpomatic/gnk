<%--
  Created by IntelliJ IDEA.
  User: Nico
  Date: 29/03/14
  Time: 18:33
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Profil Utilisateur</title>
</head>
<body>
<div role="main">
    <h3>Votre Profil</h3>
    Nom: "${currentuser.lastname}" <br/>
    Prénom: "${currentuser.firstname}" <br/>
    Adresse mail: "${currentuser.username}" <br/>
    Mot de passe: <br/>
        Dernière connexion : "${currentuser.lastConnection}"
<sec:ifAnyGranted roles="ROLE_ADMIN">
    <h3>Gestion des Droits</h3>
    <g:form controller="user" class="form-group" action="modifperm"  method="post">
<table class="table">
    <thead>
    <tr>
        <th></th>
        <th>Mon Profil</th>
        <th>Profil</th>
        <th>Mes Intrigues</th>
        <th>Intrigues</th>
        <th>Mes Gn</th>
        <th>Gn</th>
    </tr>
    <tr>
        <td>Ouvrir</td>
        <td><g:checkBox name="o1" value="${co.get(0)}"></g:checkBox></td>
        <td><g:checkBox name="o2" value="${co.get(1)}"></g:checkBox></td>
        <td><g:checkBox name="o3" value="${co.get(2)}"></g:checkBox></td>
        <td><g:checkBox name="o4" value="${co.get(3)}"></g:checkBox></td>
        <td><g:checkBox name="o5" value="${co.get(4)}"></g:checkBox></td>
        <td><g:checkBox name="o6" value="${co.get(5)}"></g:checkBox></td>
    </tr>
    <tr>
        <td>Modifier</td>
        <td><g:checkBox name="m1" value="${cm.get(0)}"></g:checkBox></td>
        <td><g:checkBox name="m2" value="${cm.get(1)}"></g:checkBox></td>
        <td><g:checkBox name="m3" value="${cm.get(2)}"></g:checkBox></td>
        <td><g:checkBox name="m4" value="${cm.get(3)}"></g:checkBox></td>
        <td><g:checkBox name="m5" value="${cm.get(4)}"></g:checkBox></td>
        <td><g:checkBox name="m6" value="${cm.get(5)}"></g:checkBox></td>
    </tr>
    <tr>
        <td>Supprimer</td>
        <td><g:checkBox name="d1" value="${cd.get(0)}"></g:checkBox></td>
        <td><g:checkBox name="d2" value="${cd.get(1)}"></g:checkBox></td>
        <td><g:checkBox name="d3" value="${cd.get(2)}"></g:checkBox></td>
        <td><g:checkBox name="d4" value="${cd.get(3)}"></g:checkBox></td>
        <td><g:checkBox name="d5" value="${cd.get(4)}"></g:checkBox></td>
        <td><g:checkBox name="d6" value="${cd.get(5)}"></g:checkBox></td>
    </tr>
    </thead>
    </table>

         <button type="submit" class="btn btn-default">Valider</button>
    </g:form>
</sec:ifAnyGranted>
</div>
</body>
</html>