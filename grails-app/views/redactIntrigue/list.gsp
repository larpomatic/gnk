<%@ page import="org.gnk.selectintrigue.Plot" %>
<%@ page import="org.gnk.admin.right" contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
	<head>
        <link rel="stylesheet" href="${resource(dir: 'css', file: 'redactIntrigueList.css')}" type="text/css">
        <style type="text/css">

        </style>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'plot.label', default: 'Plot')}" />
		<title><g:message code="plot.list.label" default="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-plot" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a>
                <g:hasRights lvlright="${right.MINTRIGUEMODIFY.value()}">
				    <li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
                </g:hasRights>
			</ul>
		</div>
		<div id="list-plot" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table id="listTable" class="table table-striped">
				<thead>
					<tr>
					    <th>
                           <g:message code="plot.name.label" default="Name" />
                        </th>
                        <th>
                            <g:message code="plot.creationDate.label" default="Creation Date" />
                        </th>
                        <th>
                            <g:message code="plot.dateupdated.label" default="Update Date" />
                        </th>
                        <th>
                            <g:message code="plot.description.label" default="Description" />
                        </th>
                        <th>
                            <g:message code="plot.isEvenemential.label" default="Is Evenemential" />
                        </th>
                        <th>
                            <g:message code="plot.isMainstream.label" default="Is Mainstream" />
                        </th>
					</tr>
				</thead>
				<tbody>
				<g:each in="${plotInstanceList}" status="i" var="plotInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
						<td>
                            <g:plotOwner idOwner="${plotInstance.user.id}" lvlright="${right.MINTRIGUEOPEN.value()}" lvlrightAdmin="${right.INTRIGUEOPEN.value()}">
                                <g:link action="show" id="${plotInstance.id}">${fieldValue(bean: plotInstance, field: "name")}</g:link>
                            </g:plotOwner>
                            <g:notPlotOwner idOwner="${plotInstance.user.id}" lvlright="${right.MINTRIGUEOPEN.value()}" lvlrightAdmin="${right.INTRIGUEOPEN.value()}">
                                ${fieldValue(bean: plotInstance, field: "name")}
                            </g:notPlotOwner>
                        </td>
					
						<td>
                            ${plotInstance.creationDate.format("dd/MM/yyyy")}
                        </td>
					
						<td>${plotInstance.lastUpdated.format("dd/MM/yyyy")}</td>
					
						<td>${fieldValue(bean: plotInstance, field: "description")}</td>
					
						<td><i class="trueIMG img-circle"></i></td>
					
						<td><g:formatBoolean boolean="${plotInstance.isMainstream}" /></td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
		</div>

    <script type="application/javascript">
        $("#listTable").DataTable();
    </script>
	</body>
</html>
