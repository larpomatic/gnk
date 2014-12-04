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
        <li><g:link controller="consolSql" action="terminal"><g:message code="navbar.gestion_console"/></g:link></li>
    </g:hasRights>

</ul>

<div role="main" class="administration">
    <h3><g:message code="default.title.my.profil"/></h3>
    <g:if test="${flash.error}">
        <div class="alert alert-error" style="display: block">${flash.error}</div>
    </g:if>
    <g:if test="${flash.mlastname}">
        <div class="message alert-success" style="display: block">${flash.mlastname}</div>
    </g:if>
    <g:if test="${flash.mpassword}">
        <div class="message alert-success" style="display: block">${flash.mpassword}</div>
    </g:if>
    <g:if test="${flash.mfrstname}">
        <div class="message alert-success" style="display: block">${flash.mfirstname}</div>
    </g:if>
    <g:if test="${flash.mpassword}">
        <div class="message alert-success" style="display: block">${flash.mpassword}</div>
    </g:if>
    <div class="row-fluid">
        <div class="span6">
            <div class="indentProfil">
                <div class="row profil-margin">
                    <g:form method="post" controller="user" action="modifyProfil">
                        <label for="lastname"><g:message code="default.profil.lastname"/> :</label>
                        <g:hasRights lvlright="${right.PROFILMODIFY.value()}">
                            <input type="text" name="lastnamemodif" id="lastname" class="form-control"
                                   placeholder='${currentuser.lastname}'/>
                            <span class="input-group-btn">
                                <button class="btn btn-default btn-submit " type="submit"><g:message
                                        code="default.action.modify"/></button>
                            </span>
                        </g:hasRights>
                        <g:hasNotRights lvlright="${right.PROFILMODIFY.value()}">
                            <span class="normal-txt">${currentuser.lastname}</span>
                        </g:hasNotRights>
                    </g:form>
                </div>

                <div class="row profil-margin">
                    <g:form method="post" controller="user" action="modifyProfil">
                        <label for="firstname"><g:message code="default.profil.firstname"/> :</label>
                        <g:hasRights lvlright="${right.PROFILMODIFY.value()}">
                            <input type="text" name="firstnamemodif" id="firstname" class="form-control"
                                   placeholder="${currentuser.firstname}">
                            <span class="input-group-btn">
                                <button class="btn btn-default btn-submit" type="submit"><g:message
                                        code="default.action.modify"/></button>
                            </span>
                        </g:hasRights>
                        <g:hasNotRights lvlright="${right.PROFILMODIFY.value()}">
                            <span class="normal-txt">${currentuser.firstname}</span>
                        </g:hasNotRights>
                    </g:form>
                </div>

                <div class="row profil-margin">
                    <g:form method="post" controller="user" action="modifyProfil">
                        <label for="email"><g:message code="default.profil.email"/> :</label>
                        <g:hasRights lvlright="${right.PROFILMODIFY.value()}">
                            <input type="text" name="usernamemodif" id="email" class="form-control"
                                   placeholder="${currentuser.username}"/>
                            <span class="input-group-btn">
                                <button class="btn btn-default btn-submit" type="submit"><g:message
                                        code="default.action.modify"/></button>
                            </span>
                        </g:hasRights>
                        <g:hasNotRights lvlright="${right.PROFILMODIFY.value()}">
                            <span class="normal-txt">${currentuser.username}</span>
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
    <g:message code="default.lastConnection.label"/> : ${date}
    <g:hasRights lvlright="${right.RIGHTSHOW.value()}">
        <h3><g:message code="default.profil.right"/> :</h3>
        <input type="hidden" value=${disabled} id="disabledValue"/>
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
                    <td><g:checkBox id="c0" name="checkbox0" value="${lb.get(0)}"></g:checkBox></td>
                    <td><g:checkBox id="c1" name="checkbox1" value="${lb.get(1)}"></g:checkBox></td>
                    <td><g:checkBox id="c2" name="checkbox2" value="${lb.get(2)}"></g:checkBox></td>
                    <td><g:checkBox id="c3" name="checkbox3" value="${lb.get(3)}"></g:checkBox></td>
                    <td><g:checkBox id="c4" name="checkbox4" value="${lb.get(4)}"></g:checkBox></td>
                    <td><g:checkBox id="c5" name="checkbox5" value="${lb.get(5)}"></g:checkBox></td>
                    <td><g:checkBox id="c18" name="checkbox18" value="${lb.get(18)}"></g:checkBox></td>
                    <td><g:checkBox id="c21" name="checkbox21" value="${lb.get(21)}"></g:checkBox></td>
                </tr>
                <tr>
                    <td><g:message code="default.action.modify"/></td>
                    <td><g:checkBox id="c6" name="checkbox6" value="${lb.get(6)}"></g:checkBox></td>
                    <td><g:checkBox id="c7" name="checkbox7" value="${lb.get(7)}"></g:checkBox></td>
                    <td><g:checkBox id="c8" name="checkbox8" value="${lb.get(8)}"></g:checkBox></td>
                    <td><g:checkBox id="c9" name="checkbox9" value="${lb.get(9)}"></g:checkBox></td>
                    <td><g:checkBox id="c10" name="checkbox10" value="${lb.get(10)}"></g:checkBox></td>
                    <td><g:checkBox id="c11" name="checkbox11" value="${lb.get(11)}"></g:checkBox></td>
                    <td><g:checkBox id="c19" name="checkbox19" value="${lb.get(19)}"></g:checkBox></td>
                    <td><g:checkBox id="c22" name="checkbox22" value="${lb.get(22)}"></g:checkBox></td>
                </tr>
                <tr>
                    <td><g:message code="default.action.delete"/></td>
                    <td><g:checkBox id="c12" name="checkbox12" value="${lb.get(12)}"></g:checkBox></td>
                    <td><g:checkBox id="c13" name="checkbox13" value="${lb.get(13)}"></g:checkBox></td>
                    <td><g:checkBox id="c14" name="checkbox14" value="${lb.get(14)}"></g:checkBox></td>
                    <td><g:checkBox id="c15" name="checkbox15" value="${lb.get(15)}"></g:checkBox></td>
                    <td><g:checkBox id="c16" name="checkbox16" value="${lb.get(16)}"></g:checkBox></td>
                    <td><g:checkBox id="c17" name="checkbox17" value="${lb.get(17)}"></g:checkBox></td>
                    <td><g:checkBox id="c20" name="checkbox20" value="${lb.get(20)}"></g:checkBox></td>
                    <td><g:checkBox id="c23" name="checkbox23" value="${lb.get(23)}"></g:checkBox></td>
                </tr>
                </thead>
            </table>
            <button type="submit" class="btn btn-default">Valider</button>
        </g:form>
    </g:hasRights>
</div>
<script type="text/javascript">

    $(function(){
       var isDisabled = $("#disabledValue").val();
        for (var i = 0;isDisabled == "1" && i < 24; i++) {

            $("#c"+i).attr("disabled", "disabled");
        }
    });
</script>
</body>
</html>