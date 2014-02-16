<%@ page import="org.gnk.selectintrigue.Plot" %>
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
        <g:render template="subNav"/>
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
						<td><g:link action="selectIntrigue" id="${gnInstance.id}">${fieldValue(bean: gnInstance, field: "name")}</g:link></td>
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
