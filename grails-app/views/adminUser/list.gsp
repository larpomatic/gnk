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
<ul class="nav nav-pills">
    <li><g:link controller="user" action="profil"><g:message code="navbar.profil"/></g:link></li>
    <g:hasRights lvlright="${right.USEROPEN.value()}">
        <li class="active"><g:link controller="adminUser" action="list"><g:message code="navbar.gestion_user"/></g:link></li>
        <li><g:link controller="consolSql" action="terminal"><g:message code="navbar.gestion_console"/> </g:link> </li>
    </g:hasRights>
</ul>
<g:form action="list" class="right pull-right">
    <form role="search">
        <div class="form-group">
            <input type="text" name="usersearch" class="form-control" placeholder="Recherche">
        </div>
        <button type="submit" class="btn btn-default">Submit</button>
    </form>
</g:form>

<div role="main">
    <br/>
    <g:link action="createUser" type="button" class="btn btn-primary">Créer un Utilisateur</g:link>
    <br/>
    <table class="table">
        <thead>
        <tr>
            <th><g:message code="default.profil.email"/></th>
            <th><g:message code="default.profil.firstname"/></th>
            <th><g:message code="default.profil.lastname"/></th>
            <g:hasRights lvlright="${right.USERMODIFY.value()}">
            <th></th>
            </g:hasRights>

            <th><g:message code="default.button.state.label"/></th>
            <g:hasRights lvlright="${right.USERCLOSE.value()}">
                <th><g:message code="default.button.delete.label"/></th>
            </g:hasRights>
        </tr>
        </thead>
        <tbody>
        <g:each in="${users}" var="u">
            <tr>
                <td>${u.username}</td>
                <td>${u.firstname}</td>
                <td>${u.lastname}</td>
                <g:hasRights lvlright="${right.USERMODIFY.value()}">
                <td>
                    <li>
                        <g:link controller="adminUser" action="edit" id="${u.id}" class="btn btn-small">
                            <g:message code="default.button.edit.label"/>
                        </g:link>
                    </li>
                </td>
                </g:hasRights>

                <td>
                    <g:if test="${!u.accountLocked}">
                        <g:message code="default.button.unlock.label"/>
                    </g:if>
                    <g:if test="${u.accountLocked}">
                        <g:message code="default.button.lock.label"/>
                    </g:if>
                </td>
                <g:hasRights lvlright="${right.USERCLOSE.value()}">
                <td>
                    <g:link controller="adminUser" action="deleteUser" id="${u.id}" type="button" class="btn btn-danger btn-small"><g:message code="default.button.delete.label"/></g:link>
                </td>
                </g:hasRights>
            </tr>
        </g:each>
        </tbody>
    </table>
</div>

</body>
</html>