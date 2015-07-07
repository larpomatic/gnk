
<%@ page import="org.gnk.genericevent.GenericEvent" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'genericEvent.label', default: 'GenericEvent')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-genericEvent" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-genericEvent" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list genericEvent">
			
				<g:if test="${genericEventInstance?.description}">
				<li class="fieldcontain">
					<span id="description-label" class="property-label"><g:message code="genericEvent.description.label" default="Description" /></span>
					
						<span class="property-value" aria-labelledby="description-label"><g:fieldValue bean="${genericEventInstance}" field="description"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${genericEventInstance?.ageMax}">
				<li class="fieldcontain">
					<span id="ageMax-label" class="property-label"><g:message code="genericEvent.ageMax.label" default="Age Max" /></span>
					
						<span class="property-value" aria-labelledby="ageMax-label"><g:fieldValue bean="${genericEventInstance}" field="ageMax"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${genericEventInstance?.ageMin}">
				<li class="fieldcontain">
					<span id="ageMin-label" class="property-label"><g:message code="genericEvent.ageMin.label" default="Age Min" /></span>
					
						<span class="property-value" aria-labelledby="ageMin-label"><g:fieldValue bean="${genericEventInstance}" field="ageMin"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${genericEventInstance?.dateCreated}">
				<li class="fieldcontain">
					<span id="dateCreated-label" class="property-label"><g:message code="genericEvent.dateCreated.label" default="Date Created" /></span>
					
						<span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${genericEventInstance?.dateCreated}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${genericEventInstance?.genericEventHasTag}">
				<li class="fieldcontain">
					<span id="genericEventHasTag-label" class="property-label"><g:message code="genericEvent.genericEventHasTag.label" default="Generic Event Has Tag" /></span>
					
						<g:each in="${genericEventInstance.genericEventHasTag}" var="g">
						<span class="property-value" aria-labelledby="genericEventHasTag-label"><g:link controller="genericEventHasTag" action="show" id="${g.id}">${g?.encodeAsHTML()}</g:link></span>
						</g:each>
					
				</li>
				</g:if>
			
				<g:if test="${genericEventInstance?.lastUpdated}">
				<li class="fieldcontain">
					<span id="lastUpdated-label" class="property-label"><g:message code="genericEvent.lastUpdated.label" default="Last Updated" /></span>
					
						<span class="property-value" aria-labelledby="lastUpdated-label"><g:formatDate date="${genericEventInstance?.lastUpdated}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${genericEventInstance?.title}">
				<li class="fieldcontain">
					<span id="title-label" class="property-label"><g:message code="genericEvent.title.label" default="Title" /></span>
					
						<span class="property-value" aria-labelledby="title-label"><g:fieldValue bean="${genericEventInstance}" field="title"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${genericEventInstance?.id}" />
					<g:link class="edit" action="edit" id="${genericEventInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
