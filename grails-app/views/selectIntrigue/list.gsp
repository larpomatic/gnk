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
						<g:sortableColumn property="date" title="${message(code: 'gn.date.label', default: 'Date')}" />
					</tr>
				</thead>
				<tbody>
				<g:each in="${gnInstanceList}" status="i" var="gnInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
						<td>
                            <g:hasRights lvlright="${right.MGNOPEN.value()}">
                                <g:link action="dispatchStep" id="${gnInstance.id}">${fieldValue(bean: gnInstance, field: "name")}</g:link>
                            </g:hasRights>
                            <g:hasNotRights lvlright="${right.MGNOPEN.value()}">
                                ${fieldValue(bean: gnInstance, field: "name")}
                            </g:hasNotRights>
                        </td>
						<td><g:formatDate date="${gnInstance.date}" /></td>
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
