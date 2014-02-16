
<%@ page import="org.gnk.tag.Univers" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'univers.label', default: 'Univers')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-univers" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-univers" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list univers">
			
				<g:if test="${universInstance?.name}">
				<li class="fieldcontain">
					<span id="name-label" class="property-label"><g:message code="univers.name.label" default="Name" /></span>
					
						<span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${universInstance}" field="name"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${universInstance?.dateCreated}">
				<li class="fieldcontain">
					<span id="dateCreated-label" class="property-label"><g:message code="univers.dateCreated.label" default="Date Created" /></span>
					
						<span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${universInstance?.dateCreated}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${universInstance?.genericTextualClueHasUniverses}">
				<li class="fieldcontain">
					<span id="genericTextualClueHasUniverses-label" class="property-label"><g:message code="univers.genericTextualClueHasUniverses.label" default="Generic Textual Clue Has Universes" /></span>
					
						<g:each in="${universInstance.genericTextualClueHasUniverses}" var="g">
						<span class="property-value" aria-labelledby="genericTextualClueHasUniverses-label"><g:link controller="genericTextualClueHasUnivers" action="show" id="${g.id}">${g?.encodeAsHTML()}</g:link></span>
						</g:each>
					
				</li>
				</g:if>
			
				<g:if test="${universInstance?.lastUpdated}">
				<li class="fieldcontain">
					<span id="lastUpdated-label" class="property-label"><g:message code="univers.lastUpdated.label" default="Last Updated" /></span>
					
						<span class="property-value" aria-labelledby="lastUpdated-label"><g:formatDate date="${universInstance?.lastUpdated}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${universInstance?.placeHasUniverses}">
				<li class="fieldcontain">
					<span id="placeHasUniverses-label" class="property-label"><g:message code="univers.placeHasUniverses.label" default="Place Has Universes" /></span>
					
						<g:each in="${universInstance.placeHasUniverses}" var="p">
						<span class="property-value" aria-labelledby="placeHasUniverses-label"><g:link controller="placeHasUnivers" action="show" id="${p.id}">${p?.encodeAsHTML()}</g:link></span>
						</g:each>
					
				</li>
				</g:if>
			
				<g:if test="${universInstance?.plotHasUniverses}">
				<li class="fieldcontain">
					<span id="plotHasUniverses-label" class="property-label"><g:message code="univers.plotHasUniverses.label" default="Plot Has Universes" /></span>
					
						<g:each in="${universInstance.plotHasUniverses}" var="p">
						<span class="property-value" aria-labelledby="plotHasUniverses-label"><g:link controller="plotHasUnivers" action="show" id="${p.id}">${p?.encodeAsHTML()}</g:link></span>
						</g:each>
					
				</li>
				</g:if>
			
				<g:if test="${universInstance?.resourceHasUniverses}">
				<li class="fieldcontain">
					<span id="resourceHasUniverses-label" class="property-label"><g:message code="univers.ressourceHasUniverses.label" default="Resource Has Universes" /></span>

						<g:each in="${universInstance.resourceHasUniverses}" var="r">
						<span class="property-value" aria-labelledby="resourceHasUniverses-label"><g:link controller="resourceHasUniverses" action="show" id="${r.id}">${r?.encodeAsHTML()}</g:link></span>
						</g:each>

				</li>
				</g:if>

			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${universInstance?.id}" />
					<g:link class="edit" action="edit" id="${universInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
