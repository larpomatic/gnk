<%--
  Created by IntelliJ IDEA.
  User: Nico
  Date: 29/03/14
  Time: 18:33
--%>

<%@ page import="org.gnk.admin.right" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Profil Utilisateur</title>
</head>

<body>
<br/>
<br/>
<ul class="nav nav-pills">
    <li class="active"><g:link controller="user" action="profil"><g:message code="navbar.profil"/></g:link></li>
    <g:hasRights lvlright="${right.USEROPEN.value()}">
        <li><g:link controller="adminUser" action="list"><g:message code="navbar.gestion_user"/></g:link></li>
        <li><g:link controller="consolSql" action="terminal"><g:message code="navbar.gestion_console"/> </g:link> </li>
    </g:hasRights>

</ul>

<div role="main">
    <h3><g:message code="default.title.my.profil"/></h3>

    <div class="row">
        <g:form method="post" controller="user" action="modifyProfil">
            <form>
                <div class="col-lg-6">
                    <g:message code="default.profil.lastname"/>: ${currentuser.lastname} <br/>

                    <div class="input-group">
                        <input type="text" name="lastnamemodif" class="form-control"/>
                        <span class="input-group-btn">
                            <button class="btn btn-default" type="submit">Modifier</button>
                        </span>
                    </div>
                </div>
            </form>
        </g:form>
    </div><!-- /.row -->
    <div class="row">
        <div class="col-lg-6">
            <g:message code="default.profil.firstname"/>: ${currentuser.firstname} <br/> <br/>
        </div><!-- /.col-lg-6 -->
        <div class="col-lg-6">
            <div class="input-group">
                <input type="text" class="form-control">
                <span class="input-group-btn">
                    <button class="btn btn-default" type="button">Modifier</button>
                </span>
            </div><!-- /input-group -->
        </div><!-- /.col-lg-6 -->
    </div><!-- /.row -->

<g:message code="default.profil.email"/>: ${currentuser.username} <br/>
    <g:message code="default.profil.password"/>: <br/>
    <g:hasRights lvlright="${right.RIGHTSHOW.value()}">
        <g:message code="default.profil.lvlright"/>: ${currentuser.gright} <br/>
    </g:hasRights>
    Last Connexion : "${currentuser.lastConnexion}"
    <g:hasRights lvlright="${right.RIGHTMODIF.value()}">
        <h3><g:message code="default.profil.right"/>:</h3>
        <g:form controller="user" class="form-group" action="modifperm" method="post">
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
                </thead>
            </table>
            <button type="submit" class="btn btn-default">Submit</button>
        </g:form>
    </g:hasRights>
</div>
</body>
</html>