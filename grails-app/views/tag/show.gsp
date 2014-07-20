<%@ page import="org.gnk.tag.Tag" %>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'tag.label', default: 'Tag')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<div class="navbar navbar-inverse">
		  <div class="navbar-inner">
		    <a class="brand" href="#">${message(code: 'navbar.adminRef')} : </a>
		    <ul class="nav">
		      	<li><g:link controller="tag" class="list" action="list">${message(code: 'adminRef.navbar.tags')}</g:link></li>
		      	%{--<li><g:link controller="tagFamily" class="list" action="list">${message(code: 'adminRef.navbar.tagFamilies')}</g:link></li>--}%
		      	     	<li><g:link controller="tag" action="stats">${message(code: 'adminRef.navbar.stats')}</g:link></li>
		  </ul>
		</div>
		<div id="show-tag" class="content scaffold-show" role="main">

			<h1><g:message code="adminRef.tag.stats.zoom" /></h1>

			<ol class="property-list tag">
			
				<g:if test="${tagInstance?.name}">
				<li class="fieldcontain">
					<span id="name-label" class="property-label"><g:message code="tag.name.label" default="Name" /></span>
					
						<span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${tagInstance}" field="name"/></span>
					
				</li>
				</g:if>
			
				%{--<g:if test="${tagInstance?.tagFamilies}">--}%
				%{--<li class="fieldcontain">--}%
					%{--<span id="tagFamilies-label" class="property-label"><g:message code="tag.tagFamilies.label" default="Tag Families" /></span>--}%

						%{--<g:each in="${tagInstance.tagFamilies}" var="t">--}%
						%{--<span class="property-value" aria-labelledby="tagFamilies-label"><g:link controller="tagHasTagFamily" action="show" id="${t.id}">${t?.encodeAsHTML()}</g:link></span>--}%
						%{--</g:each>--}%
					
				%{--</li>--}%
				%{--</g:if>--}%
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${tagInstance?.id}" />
					<g:link class="edit" action="edit" id="${tagInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
