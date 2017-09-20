<%@ page import="org.gnk.gn.GnHasUser; org.gnk.selectintrigue.SelectIntrigueController" %>
<%@ page import="org.gnk.admin.right" contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'selectIntrigue.css')}" type="text/css">
    <meta name="layout" content="main">
    <title><g:message code="navbar.selectintrigue"/></title>
</head>
<g:javascript src="selectIntrigue/listSelectIntigue.js"/>
<body>
<g:set var="step1" value="${message(code: 'selectIntrigue.step.selectIntrigue')}"/>
<g:set var="step2" value="${message(code: 'selectIntrigue.step.characters')}"/>
<g:set var="step2_5" value="${message(code: 'selectIntrigue.step.life')}"/>
<g:set var="step3" value="${message(code: 'selectIntrigue.step.identity')}"/>
<g:set var="step4" value="${message(code: 'selectIntrigue.step.ressource')}"/>
<g:set var="step5" value="${message(code: 'selectIntrigue.step.place')}"/>
<g:set var="step6" value="${message(code: 'selectIntrigue.step.time')}"/>
<g:set var="step7" value="${message(code: 'selectIntrigue.step.publication')}"/>

<div id="list-gn" class="content scaffold-list" role="main">
    <h1><g:message code="navbar.selectintrigue" default="Plot List"/></h1>

    <div class="container margin-bottom-20">
        <g:render template="subNav" model="['right': right]"/>
    </div>

    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>


    <table id="listTable2" class="table table-striped">
        <thead>
        <tr>
            <th><g:message code="gn.name.label" default="Name"/></th>
            <th><g:message code="gn.step.label" default="Step"/></th>
            <th><g:message code="gn.date.label" default="Date"/></th>
            <th><g:message code="gn.Creation.label" default="Author"/></th>
            <th><g:message code="gn.lastUpdate.label" default="last update"/></th>
            <th style="display: none"><g:message code="gn.date.label"/></th>
        </tr>
        </thead>
        <tbody>
        <g:each in="${gnInstanceList}" status="i" var="gnInstance">
            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
						<td>
            <g:form controller="selectIntrigue" action="dispatchStep" params="[id: gnInstance.id]">
                <g:hasRights lvlright="${right.MGNOPEN.value()}">
                    <button type="submit" style="" style="border: none"
                            class="btn-link">${fieldValue(bean: gnInstance, field: "name")}</button></td>
                </g:hasRights>
                <g:if test="${gnInstance.step == null}">
                    <td><g:select name='step-${gnInstance.id}'
                                  from="${[]}"/>
                </g:if>
                <g:if test="${gnInstance.step == "publication"}">
                    <td><g:select name='step-${gnInstance.id}'
                                  from="${["${step7}","${step6}", "${step5}", "${step4}", "${step3}", "${step2_5}", "${step2}", "${step1}"]}"/>
                </g:if>

                <g:if test="${gnInstance.step == 'substitution'}">
                    <td><g:select name="step-${gnInstance.id}"
                                  from="${['naming', 'life', 'role2perso', 'selectIntrigue']}"/></td>
                </g:if>

                <g:if test="${gnInstance.step == "time"}">
                    <td><g:select name='step-${gnInstance.id}'
                                  from="${["${step6}", "${step5}", "${step4}", "${step3}", "${step2_5}", "${step2}", "${step1}"]}"/>
                </g:if>
                <g:if test="${gnInstance.step ==  "place"}">
                    <td><g:select name='step-${gnInstance.id}'
                                  from="${[ "${step5}", "${step4}", "${step3}", "${step2_5}", "${step2}", "${step1}"]}"/>
                </g:if>
                <g:if test="${gnInstance.step == "ressource"}">
                    <td><g:select name='step-${gnInstance.id}'
                                  from="${["${step4}", "${step3}", "${step2_5}", "${step2}", "${step1}"]}"/>
                </g:if>
                <g:if test="${gnInstance.step == "naming"}">
                    <td><g:select name='step-${gnInstance.id}'
                                  from="${["${step3}", "${step2_5}", "${step2}", "${step1}"]}"/>
                </g:if>
                <g:if test="${gnInstance.step == "life"}">
                    <td><g:select name="step-${gnInstance.id}"
                                  from="${["${step2_5}", "${step2}", "${step1}"]}"/></td>
                </g:if>
                <g:if test="${gnInstance.step == "role2perso"}">
                    <td><g:select name="step-${gnInstance.id}"
                                  from="${["${step2}", "${step1}"]}"/></td>
                </g:if>
                <g:if test="${gnInstance.step == "selectIntrigue"}">
                    <td><g:select name="step-${gnInstance.id}"
                                  from="${["${step1}"]}"/></td>
                </g:if>
                <g:hasNotRights lvlright="${right.MGNOPEN.value()}">
                    ${fieldValue(bean: gnInstance, field: "name")}
                </g:hasNotRights>
            </g:form>
            </td>
            <td>${gnInstance.dateCreated.format("dd/MM/yyyy")}</td>
            <td>${gnInstance.gnHasUsers.find { x -> x.gn.id = gnInstance.id }.user.username}</td>

            <td>${gnInstance.lastUpdated.format("dd/MM/yyyy")}</td>

            <td>
                <g:hasRights lvlright="${right.MGNDELETE.value()}">
                    <g:link action="delete" onclick="return confirm('êtes vous sur ?');" id="${gnInstance.id}" class="btn btn-danger">
                        <g:message code="default.delete" default="Delete"/>
                    </g:link>
                </g:hasRights>
            </td>
            </tr>
        </g:each>
        </tbody>
    </table>
</div>


<script type="text/javascript" String src="jquery.dataTables.js" String></script>
<script type="text/javascript" String src="//cdn.datatables.net/plug-ins/1.10.11/sorting/date-euro.js" String></script>


<script type="application/javascript">
    $(".mytool").tooltip({html: true});
    $("#listTable2").DataTable({
        "order": [[2, "desc"]],
        "columnDefs": [

            {"type": 'date-euro', "targets": 2},
            {"type": 'date-euro', "targets": 4}
        ]
    });
</script>
</body>
</html>
