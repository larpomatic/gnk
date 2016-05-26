<%@ page import="org.gnk.selectintrigue.Plot" %>
<%@ page import="org.gnk.admin.right" contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'gn.label', default: 'GN')}" />
		<title><g:message code="navbar.selectintrigue" /></title>
	</head>
	<body>
        <div>
		    <a href="#list-plot" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        </div>
        <g:render template="subNav" model="['right': right]"/>
		<div id="list-gn" class="content scaffold-list" role="main">
			<legend><g:message code="default.list.label" args="[entityName]" /></legend>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table class="table">
				<thead>
					<tr>
						<g:sortableColumn property="name" title="${message(code: 'gn.name.label', default: 'Name')}" />
						<g:sortableColumn property="step" title="${message(code: 'gn.step.label', default: 'Step')}" />
						<g:sortableColumn property="date" title="${message(code: 'gn.date.label', default: 'Date')}" />
					</tr>
				</thead>
				<tbody>
				<g:each in="${gnInstanceList}" status="i" var="gnInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
						<td>
                        <g:form controller="selectIntrigue" action="dispatchStep" params="[id : gnInstance.id]">
                            <g:hasRights lvlright="${right.MGNOPEN.value()}">
                            <button type="submit" style="" style="border: none" class="btn-link">${fieldValue(bean: gnInstance, field: "name")}</button></td>
                            </g:hasRights>
                            <!--<g:link action="dispatchStep" params="[id: gnInstance.id]">${fieldValue(bean: gnInstance, field: "name")}</g:link>-->
							<g:if test="${gnInstance.step == 'publication'}">
								<td><g:select name='step-${gnInstance.id}'
											  from="${['publication', 'substitution', 'role2perso', 'selectIntrigue']}"/>
							</g:if>
							<g:if test="${gnInstance.step == 'substitution'}">
								<td><g:select name="step-${gnInstance.id}"
											  from="${['substitution', 'role2perso', 'selectIntrigue']}"/></td>
							</g:if>
							<g:if test="${gnInstance.step == 'role2perso'}">
								<td><g:select name="step-${gnInstance.id}"
											  from="${['role2perso', 'selectIntrigue']}"/></td>
							</g:if>
							<g:if test="${gnInstance.step == 'selectIntrigue'}">
								<td><g:select name="step-${gnInstance.id}"
											  from="${['selectIntrigue']}"/></td>
							</g:if>
							<g:hasNotRights lvlright="${right.MGNOPEN.value()}">
                                ${fieldValue(bean: gnInstance, field: "name")}
                            </g:hasNotRights>
                        </g:form>
                        </td>
						<td><g:formatDate date="${gnInstance.date}" /></td>
                        <td>
                            <g:hasRights lvlright="${right.MGNDELETE.value()}">
                                <g:link action="delete" id="${gnInstance.id}" class="btn btn-danger">
                                    <g:message code="default.delete" default="Delete" />
                                </g:link>
                            </g:hasRights>
                        </td>
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${gnInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
