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

        <div class="form-group btn-block">
            <input type="text" name="usersearch" class="form-control" placeholder=<g:message code="default.action.search.label"/>>

        <button type="submit" class="btn btn-default btn-submit"><g:message code="default.action.search.label"/></button>
        </div>
    </form>
</g:form>

<div role="main">
    <br/>
    <g:hasRights lvlright="${right.USERMODIFY.value()}">
    <g:link action="createUser" type="button" class="btn btn-primary"><g:message code="default.action.creatUser"/></g:link>
    </g:hasRights>
    <br/>
    <table class="table">
        <thead>
        <tr>
            <th><g:message code="default.profil.email"/></th>
            <th><g:message code="default.profil.firstname"/></th>
            <th><g:message code="default.profil.lastname"/></th>
            <th></th>

            <th><g:message code="default.button.state.label"/></th>
            <g:hasRights lvlright="${right.USERCLOSE.value()}">
                <th></th>
            </g:hasRights>
        </tr>
        </thead>
        <tbody>
        <g:each in="${users}" var="u">
            <tr>
                <td>${u.username}</td>
                <td>${u.firstname}</td>
                <td>${u.lastname}</td>
                <td>
                    <li>
                        <g:link controller="adminUser" action="edit" id="${u.id}" class="btn btn-small">
                            <g:message code="default.button.edit.label"/>
                        </g:link>
                    </li>
                </td>
                <td>
                    <g:hasRights lvlright="${right.USERMODIFY.value()}">
                    <g:if test="${!u.accountLocked}">
                        <g:link class="btn btn-success"   controller="adminUser" action="lock" id="${u.id}">
                            <g:message code="default.button.actif.label"/>
                        </g:link>
                    </g:if>
                    <g:if test="${u.accountLocked}">
                        <g:link class="btn btn-danger" controller="adminUser" action="lock" id="${u.id}">
                            <g:message code="default.button.inactif.label"/>
                        </g:link>
                    </g:if>
                    </g:hasRights>
                    <g:hasNotRights lvlright="${right.USERMODIFY.value()}">
                        <g:if test="${!u.accountLocked}">
                            <btn class="btn btn-success" disabled="disabled">
                                <g:message code="default.button.actif.label"/>
                            </btn>
                        </g:if>
                        <g:if test="${u.accountLocked}">
                            <btn class="btn btn-danger" disabled="disabled">
                                <g:message code="default.button.inactif.label"/>
                            </btn>
                        </g:if>
                    </g:hasNotRights>
                </td>
                <g:hasRights lvlright="${right.USERCLOSE.value()}">
                <td>
                    <a id="${u.id}" type="button" class="btn btn-danger btn-small" href="#deletemodal${u.id}" data-toggle="modal" ><g:message code="default.action.delete.label"/></a>
                </td>
                </g:hasRights>
            </tr>
        </g:each>
        </tbody>
    </table>
</div>
<g:render template="deletemodalUsers" />
</body>
</html>
