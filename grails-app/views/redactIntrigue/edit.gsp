<%@ page import="org.gnk.selectintrigue.Plot"%>
<%@ page import="org.gnk.admin.right" contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>

<meta name="layout" content="main">
<g:set var="entityName"
	value="${plotInstance.name}" />
<title><g:message code="default.edit.label" args="[entityName]" /></title>
<link rel="stylesheet" href="${resource(dir: 'css', file: 'bootstrap-datetimepicker.min.css')}" type="text/css">
<link rel="stylesheet" href="${resource(dir: 'css', file: 'redactIntrigue.css')}" type="text/css">
<link rel="stylesheet" href="${resource(dir: 'css', file: 'bootstrap-editable.css')}" type="text/css">
</head>
<body>
<g:javascript src="redactIntrigue/bootstrap-confirmation.js"/>
<g:javascript src="redactIntrigue/bootstrap-datetimepicker.min.js"/>
<g:javascript src="redactIntrigue/bootstrap-datetimepicker.fr.js"/>
<g:javascript src="redactIntrigue/redactIntrigue.js"/>
<g:javascript src="redactIntrigue/role.js"/>
<g:javascript src="redactIntrigue/event.js"/>
<g:javascript src="redactIntrigue/pastScene.js"/>
<g:javascript src="redactIntrigue/genericPlace.js"/>
<g:javascript src="redactIntrigue/genericResource.js"/>
<g:javascript src="redactIntrigue/relation.js"/>
<g:javascript src="redactIntrigue/generalDescription.js"/>
<g:javascript src="selectIntrigue/bootstrap-editable.js"/>
<g:javascript src="selectIntrigue/bootstrap.min.js"/>
<g:javascript src="selectIntrigue/bootstrap-editable.min.js"/>
<div class="row-fluid" id="hidTest">
    <div class="span4">
        <form id="exportPDFButton" action="${g.createLink(controller:'redactIntrigue', action:'print')}" method="POST">
            <input type="hidden" id="plotid" name="plotid" value="${plotInstance.id}"/>
            <button id="PDFButtonPublication" class="btn" type="submit" style="visibility: visible; display: block; margin-bottom: 10px;" ><i class="icon-ok-sign"></i> Prévisualisation</button>
        </form>
    </div>
    <div class="span4">
        <button id="GeneralSave" class="btn" style="visibility: visible; display: block; margin-bottom: 10px;">Sauvegarde complète</button>
    </div>
</div>
	<div id="edit-plot" class="content scaffold-list">
		<h1>
			<g:message code="default.edit.label" args="[entityName]" />
		</h1>
        <g:render template="testMenuSlide" model="['right': right]" />
	</div>

</body>
</html>
