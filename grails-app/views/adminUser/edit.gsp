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
<g:hasRights lvlright="${right.USEROPEN.value()}">
    <h3>Profil</h3>

    <div class="row-fluid">
        <div class="span6">
            <div class="indentProfil">
                <div class="row profil-margin">
                    <g:form method="post" controller="user" action="modifyProfil">
                        <label for="lastname"><g:message code="default.profil.lastname"/> :</label>
                        <g:hasRights lvlright="${right.PROFILMODIFY.value()}">
                            <input type="text" name="lastnamemodif" id="lastname" class="form-control"
                                   placeholder='${user.lastname}'/>
                            <span class="input-group-btn">
                                <button class="btn btn-default btn-submit " type="submit"><g:message
                                        code="default.action.modify"/></button>
                            </span>
                        </g:hasRights>
                        <g:hasNotRights lvlright="${right.PROFILMODIFY.value()}">
                            <span class="normal-txt">${user.lastname}</span>
                        </g:hasNotRights>
                    </g:form>
                </div>

                <div class="row profil-margin">
                    <g:form method="post" controller="user" action="modifyProfil">
                        <label for="firstname"><g:message code="default.profil.firstname"/> :</label>
                        <g:hasRights lvlright="${right.PROFILMODIFY.value()}">
                            <input type="text" name="firstnamemodif" id="firstname" class="form-control"
                                   placeholder="${user.firstname}">
                            <span class="input-group-btn">
                                <button class="btn btn-default btn-submit" type="submit"><g:message
                                        code="default.action.modify"/></button>
                            </span>
                        </g:hasRights>
                        <g:hasNotRights lvlright="${right.PROFILMODIFY.value()}">
                            <span class="normal-txt">${user.firstname}</span>
                        </g:hasNotRights>
                    </g:form>
                </div>

                <div class="row profil-margin">
                    <g:form method="post" controller="user" action="modifyProfil">
                        <label for="email"><g:message code="default.profil.email"/> :</label>
                        <g:hasRights lvlright="${right.PROFILMODIFY.value()}">
                            <input type="text" name="usernamemodif" id="email" class="form-control"
                                   placeholder="${user.username}"/>
                            <span class="input-group-btn">
                                <button class="btn btn-default btn-submit" type="submit"><g:message
                                        code="default.action.modify"/></button>
                            </span>
                        </g:hasRights>
                        <g:hasNotRights lvlright="${right.PROFILMODIFY.value()}">
                            <span class="normal-txt">${user.username}</span>
                        </g:hasNotRights>
                    </g:form>
                </div>
            </div>
        </div>

        <g:hasRights lvlright="${right.PROFILMODIFY.value()}">
            <div class="span6 ">
                <div class="indentPassword">
                    <div class="row profil-margin">

                        <div class="row profil-margin">
                            <g:form method="post" controller="user" action="modifyProfil">
                                <label for="cpassword"><g:message
                                        code="default.action.changedPassword"/>  :</label> <input
                                    type="password" id="cpassword" name="passwordChanged"
                                    placeholder="<g:message code="default.minsize.password"/>"/><br/>
                                <label for="copassword"><g:message
                                        code="default.action.acceptPassword"/> :</label> <input
                                    type="password" id="copassword" name="passwordChangedConfirm"/><br/>

                                <div class="input-group">
                                    <span class="input-group-btn center">
                                        <button class="btn btn-default btn-large" type="submit"><g:message
                                                code="default.action.modify"/></button>
                                    </span>
                                </div>
                            </g:form>
                        </div>
                    </div>
                </div>
            </div>
        </g:hasRights>
    </div>

    <div class="btn-group">
        <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
            Action <span class="caret"></span>
        </button>
        <ul class="dropdown-menu" role="menu">
            <li>
                <g:link controller="adminUser" action="lock" id="${user.id}">
                    <g:if test="${user.accountLocked}">
                        <g:message code="default.button.unlock.label"/>
                    </g:if>
                    <g:if test="${!user.accountLocked}">
                        <g:message code="default.button.lock.label"/>
                    </g:if>
                </g:link>
            </li>
        </ul>
    </div> <br/>
    <g:hasRights lvlright="${right.RIGHTSHOW.value()}">
    </g:hasRights>
    <g:link class="btn btn-warning" controller="adminUser" action="statistic" id="${user.id}">
        <g:message code="default.statistique"/>
    </g:link>
    <g:hasRights lvlright="${right.RIGHTMODIF.value()}">
        <h3><g:message code="default.profil.right"/>:</h3>
        <g:form controller="adminUser" class="form-group" action="changeperm" id="${user.id}" method="post">
            <table class="table">
                <thead>
                <tr>
                    <th></th>
                    <th><g:message code="default.title.my.profil"/></th>
                    <th><g:message code="default.title.user"/></th>
                    <th><g:message code="default.title.my.intrigue"/></th>
                    <th><g:message code="default.title.intrigue"/></th>
                    <th><g:message code="default.title.my.gn"/></th>
                    <th><g:message code="default.title.gn"/></th>
                    <th><g:message code="default.title.referentiel"/></th>
                    <th><g:message code="default.title.manageRight"/></th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td><g:message code="default.action.open"/></td>
                    <td><g:checkBox name="checkbox0" value="${lb.get(0)}"></g:checkBox></td>
                    <td><g:checkBox name="checkbox1" value="${lb.get(1)}"></g:checkBox></td>
                    <td><g:checkBox name="checkbox2" value="${lb.get(2)}"></g:checkBox></td>
                    <td><g:checkBox name="checkbox3" value="${lb.get(3)}"></g:checkBox></td>
                    <td><g:checkBox name="checkbox4" value="${lb.get(4)}"></g:checkBox></td>
                    <td><g:checkBox name="checkbox5" value="${lb.get(5)}"></g:checkBox></td>
                    <td><g:checkBox name="checkbox18" value="${lb.get(18)}"></g:checkBox></td>
                    <td><g:checkBox name="checkbox21" value="${lb.get(21)}"></g:checkBox></td>
                </tr>
                <tr>
                    <td><g:message code="default.action.modify"/></td>
                    <td><g:checkBox name="checkbox6" value="${lb.get(6)}"></g:checkBox></td>
                    <td><g:checkBox name="checkbox7" value="${lb.get(7)}"></g:checkBox></td>
                    <td><g:checkBox name="checkbox8" value="${lb.get(8)}"></g:checkBox></td>
                    <td><g:checkBox name="checkbox9" value="${lb.get(9)}"></g:checkBox></td>
                    <td><g:checkBox name="checkbox10" value="${lb.get(10)}"></g:checkBox></td>
                    <td><g:checkBox name="checkbox11" value="${lb.get(11)}"></g:checkBox></td>
                    <td><g:checkBox name="checkbox19" value="${lb.get(19)}"></g:checkBox></td>
                    <td><g:checkBox name="checkbox22" value="${lb.get(22)}"></g:checkBox></td>
                </tr>
                <tr>
                    <td><g:message code="default.action.delete"/></td>
                    <td><g:checkBox name="checkbox12" value="${lb.get(12)}"></g:checkBox></td>
                    <td><g:checkBox name="checkbox13" value="${lb.get(13)}"></g:checkBox></td>
                    <td><g:checkBox name="checkbox14" value="${lb.get(14)}"></g:checkBox></td>
                    <td><g:checkBox name="checkbox15" value="${lb.get(15)}"></g:checkBox></td>
                    <td><g:checkBox name="checkbox16" value="${lb.get(16)}"></g:checkBox></td>
                    <td><g:checkBox name="checkbox17" value="${lb.get(17)}"></g:checkBox></td>
                    <td><g:checkBox name="checkbox20" value="${lb.get(20)}"></g:checkBox></td>
                    <td><g:checkBox name="checkbox23" value="${lb.get(23)}"></g:checkBox></td>
                </tr>
                </tbody>
            </table>
            <button type="submit" class="btn btn-default">Valider</button>
        </g:form>
        </div>
    </g:hasRights>
</g:hasRights>
<g:link type="button" class="btn btn-primary btn-large right" action="list"><g:message code="default.action.back"/></g:link>
</body>
</html>