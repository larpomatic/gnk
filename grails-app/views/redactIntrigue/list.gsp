<%@ page import="org.gnk.selectintrigue.Plot" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'plot.label', default: 'Plot')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-plot" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-plot" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="name" title="${message(code: 'plot.name.label', default: 'Name')}" />
					
						<g:sortableColumn property="creationDate" title="${message(code: 'plot.creationDate.label', default: 'Creation Date')}" />
					
						<g:sortableColumn property="dateCreated" title="${message(code: 'plot.dateCreated.label', default: 'Date Created')}" />
					
						<g:sortableColumn property="description" title="${message(code: 'plot.description.label', default: 'Description')}" />
					
						<g:sortableColumn property="isEvenemential" title="${message(code: 'plot.isEvenemential.label', default: 'Is Evenemential')}" />
					
						<g:sortableColumn property="isMainstream" title="${message(code: 'plot.isMainstream.label', default: 'Is Mainstream')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${plotInstanceList}" status="i" var="plotInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${plotInstance.id}">${fieldValue(bean: plotInstance, field: "name")}</g:link></td>
					
						<td><g:formatDate date="${plotInstance.creationDate}" /></td>
					
						<td><g:formatDate date="${plotInstance.dateCreated}" /></td>
					
						<td>${fieldValue(bean: plotInstance, field: "description")}</td>
					
						<td><g:formatBoolean boolean="${plotInstance.isEvenemential}" /></td>
					
						<td><g:formatBoolean boolean="${plotInstance.isMainstream}" /></td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${plotInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
