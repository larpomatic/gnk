<%@ page import="org.gnk.naming.ConventionHasRule; org.gnk.naming.Rule" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <title>${message(code: 'adminRef.social.conventionRule.title')}</title>
</head>

<body>
<g:render template="../tag/subNav"/>
<fieldset>
    <h2><g:message code="adminRef.social.conventionRule.title"/></h2>
</fieldset>

<ul class="nav nav-tabs">
    <li class="active"><a href="#tab_convention" data-toggle="tab">Conventions et règles</a></li>
    <li><a href="#tab_regles" data-toggle="tab">Tags de règles</a></li>
</ul>

<div class="tab-content">
    <div id="tab_convention" class="tab-pane in active">
        <g:render template="../infosAndErrors" />
        <g:render template="addConventions"/>
        <table class="table table-bordered">
        <thead>
        <tr class="upper">
            <th style="text-align: center;">#</th>
            <th>Nom de la Convention</th>
            <th>Règles</th>
            <th>Supprimer</th>
        </tr>
        </thead>
        <tbody>
    <g:each status="n" in="${conventionList}" var="conv">
        <tr id="char${conv.id}">
            <!-- convention rule -->
            <td style="text-align: center;">${n + 1}</td>
            <!-- description -->
            <td class="cap">${conv.description}</td>
            <!-- edition -->
            <td>
                <a href="#editmodalrule${conv.id}" role="button" class="btn" data-toggle="modal">Editer</a>
            </td>
            <!-- suppression -->
            <td>
                <g:form>
                    <fieldset class="buttons">
                        <g:hiddenField name="idConvention" value="${conv?.id}" />
                        <g:actionSubmit class="btn btn-warning" action="deleteConvention" value="${message(code: 'default.delete')}" onclick="return confirm('${message(code: 'adminRef.social.conventionRule.deleteConvention')}');" />
                    </fieldset>
                </g:form>
            </td>
        </tr>
    </g:each>
    </tbody>
</table>
<!-- Modal Views -->
<g:render template="modalEditConventionRule"/>
</div>

<div id="tab_regles" class="tab-pane">
    <div id="create-tag" class="content scaffold-create" role="main">
        <legend>Administration des tags de règles</legend>
    </div>

    <table class="table table-bordered">
        <thead>
        <tr class="upper">
            <th style="text-align: center;">#</th>
            <th>Règles</th>
            <th>Tags</th>
        </tr>
        <tbody>
        <g:each status="n" in="${org.gnk.naming.Rule.list()}" var="rule">
            <tr id="char${rule.id}">
                <!-- convention rule -->
                <td style="text-align: center;">${n + 1}</td>
                <!-- description -->
                <td class="cap">${rule.description}</td>
                <!-- checkBox -->

                <td>
                    <g:set var="foo" value="${false}"/>
                    <g:each in="${ConventionHasRule.list()}" var="chr">
                        <g:if test="${chr.convention.equals(conv) && chr.rule.equals(rule)}">
                            <g:set var="foo" value="${true}"/>
                        </g:if>
                    </g:each>
                    <g:submitButton name="rule.${rule.id}.submitButton" value="Séléctionner les tags"/>
                </td>
            </tr>
        </g:each>
        </tbody>
    </table>
</div>
</div>
</body>
</html>
