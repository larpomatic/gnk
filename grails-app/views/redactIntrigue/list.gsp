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
		<div id="list-plot" class="content scaffold-list" role="main">
			<h1><g:message code="plot.list.label" default="Plot List" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
            <div class="btn-group subnav">
                <a class="btn home" href="${createLink(uri: '/')}"><g:message code="plot.home" default="default.home.label"/>
                <g:hasRights lvlright="${right.MINTRIGUEMODIFY.value()}">
                    <g:link class="btn" action="create"><g:message code="plot.new" default="default.new.label" /></g:link>
                </g:hasRights>
            </div>
            <br/>
			<table id="listTable" class="table table-striped">
				<thead>
					<tr>
					    <th style="width: 10%">
                           <g:message code="plot.name.label" default="Name" />
                        </th>
                        <th style="width: 8%">
                            <g:message code="plot.creationDate.label" default="Creation Date" />
                        </th>
                        <th style="width: 8%">
                            <g:message code="plot.dateupdated.label" default="Update Date" />
                        </th>
                        <th style="width: 24%">
                            <g:message code="plot.description.label" default="Description" />
                        </th>
                        <th style="width: 8%">
                            <g:message code="plot.author.label" default="Author" />
                        </th>
                        <th style="width: 30%">
                            <g:message code="plot.tags.label" default="Tags List" />
                        </th>
                        <th style="width: 3%">
                            <g:message code="plot.isEvenemential.label" default="Evenemential" />
                        </th>
                        <th style="width: 3%">
                            <g:message code="plot.isMainstream.label" default="Mainstream" />
                        </th>
                        <th style="width: 3%">
                            <g:message code="plot.isPublic.label" default="Public" />
                        </th>
                        <th style="width: 3%">
                            <g:message code="plot.isDraft.label" default="Draft" />
                        </th>
					</tr>
				</thead>
				<tbody>
				<g:each in="${plotInstanceList}" status="i" var="plotInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
						<td>
                            <g:plotOwner idOwner="${plotInstance.user.id}" lvlright="${right.MINTRIGUEOPEN.value()}" lvlrightAdmin="${right.INTRIGUEOPEN.value()}">
                                <g:link class="edit mytool" action="edit" id="${plotInstance?.id}" toggle="tooltip" data-placement="bottom" data-original-title="${plotInstance.getMetric()}">
                                    ${fieldValue(bean: plotInstance, field: "name")}
                                </g:link>
                            </g:plotOwner>
                            <g:notPlotOwner idOwner="${plotInstance.user.id}" lvlright="${right.MINTRIGUEOPEN.value()}" lvlrightAdmin="${right.INTRIGUEOPEN.value()}">
                                <a href="#" class="mytool" toggle="tooltip" data-placement="bottom" data-original-title="${plotInstance.getMetric()}">${fieldValue(bean: plotInstance, field: "name")}</a>
                            </g:notPlotOwner>
                        </td>
					
						<td>
                            ${plotInstance.creationDate.format("dd/MM/yyyy")}
                        </td>
					
						<td>${plotInstance.lastUpdated.format("dd/MM/yyyy")}</td>
					
						<td>${fieldValue(bean: plotInstance, field: "description")}</td>

                        <td>${plotInstance.user.firstname + " " + plotInstance.user.lastname}</td>

                        <td>
                            <g:each in="${plotInstance.getPlotHasTag()}" var="tag">
                                <g:if test="${tag.weight > 50}">
                                    <span class="label mytool label-success" data-tag="none" contenteditable="false" toggle="tooltip"  data-placement="top" data-original-title="${tag.weight}" title="">${tag.tag.name}</span>
                                </g:if>
                                <g:elseif test="${tag.weight >=0}">
                                    <span class="label mytool label-info" data-tag="none" contenteditable="false" toggle="tooltip" data-placement="top" data-original-title="${tag.weight}" title="">${tag.tag.name}</span>
                                </g:elseif>
                                <g:elseif test="${tag.weight >= -50}">
                                    <span class="label mytool label-warning" data-tag="none" contenteditable="false" toggle="tooltip" data-placement="top" data-original-title="${tag.weight}" title="">${tag.tag.name}</span>
                                </g:elseif>
                                <g:else>
                                    <span class="label mytool label-important" data-tag="none" contenteditable="false" toggle="tooltip" data-placement="top" data-original-title="${tag.weight}" title="">${tag.tag.name}</span>
                                </g:else>
                            </g:each>
                        </td>

						<td align="center" style="vertical-align: middle"><div class="${plotInstance.isEvenemential} img-circle">
                            <div class="hidden">${plotInstance.isEvenemential}></div>
                        </div></td>

                        <td align="center" style="vertical-align: middle"><div class="${plotInstance.isMainstream} img-circle mytool" toggle="tooltip" data-placement="top" data-original-title="YES">
                            <div class="hidden">${plotInstance.isMainstream}</div>
                        </div></td>

                        <td align="center" style="vertical-align: middle"><div class="${plotInstance.isPublic} img-circle">
                            <div class="hidden">${plotInstance.isPublic}</div>
                        </div></td>


                        <td align="center" style="vertical-align: middle"><div class="${plotInstance.isDraft} img-circle">
                            <div class="hidden">${plotInstance.isDraft}</div>
                        </div></td>
                    </tr>
				</g:each>
				</tbody>
			</table>
		</div>

    <script type="application/javascript">
        $(".mytool").tooltip({ html: true });
        $("#listTable").DataTable({
            "language": {
                "url": "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/French.json"
            }
        });
    </script>
	</body>
</html>
