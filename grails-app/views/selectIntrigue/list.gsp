<%@ page import="org.gnk.gn.GnHasUser; org.gnk.selectintrigue.SelectIntrigueController" %>
<%@ page import="org.gnk.admin.right" contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
	<head>
        <link rel="stylesheet" href="${resource(dir: 'css', file: 'selectIntrigue.css')}" type="text/css">
        <style type="text/css">
        </style>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'gn.label', default: 'GN')}" />
		<title><g:message code="navbar.selectintrigue" /></title>
	</head>
	<body>
        <g:render template="subNav" model="['right': right]"/>
		<div id="list-gn" class="content scaffold-list" role="main">
        <h1><g:message code="navbar.selectintrigue" default="Plot List" /></h1>
        <g:if test="${flash.message}">
            <div class="message" role="status">${flash.message}</div>
        </g:if>


        <table id="listTable2" class="table table-striped">
				<thead>
					<tr>
                        <th><g:message code="gn.name.label" default="Name" /></th>
                        <th><g:message code="gn.date.label" default="Date" /></th>
                        <th><g:message code="gn.Creation.label" default="Author" /></th>
                        <th><g:message code="gn.lastUpdate.label" default="last update" /></th>
                        <th style="display: none"><g:message code="gn.date.label" /></th>
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
                        <td>${gnInstance.dateCreated.format("dd/MM/yyyy")}</td>
                        <td>${gnInstance.gnHasUsers.find {x-> x.gn.id = gnInstance.id}.user.username}</td>

                        <td>${gnInstance.lastUpdated.format("dd/MM/yyyy")}</td>

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
		</div>


    <script type="text/javascript"String src="jquery.dataTables.js"String></script>
    <script type="text/javascript"String src="//cdn.datatables.net/plug-ins/1.10.11/sorting/date-euro.js"String></script>


    <script type="application/javascript">
        $(".mytool").tooltip({ html: true });
        $("#listTable2").DataTable({
            "order": [[ 2, "desc" ]],
            "columnDefs": [
                { "type": 'date-euro', "targets": 1 },
                { "type": 'date-euro', "targets": 3 }
]        });
    </script>
    </body>
</html>
