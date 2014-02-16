
<%@ page import="org.gnk.resplacetime.GenericResource" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'genericResource.label', default: 'GenericResource')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-genericResource" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-genericResource" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list genericResource">
			
				<g:if test="${genericResourceInstance?.code}">
				<li class="fieldcontain">
					<span id="code-label" class="property-label"><g:message code="genericResource.code.label" default="Code" /></span>
					
						<span class="property-value" aria-labelledby="code-label"><g:fieldValue bean="${genericResourceInstance}" field="code"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${genericResourceInstance?.comment}">
				<li class="fieldcontain">
					<span id="comment-label" class="property-label"><g:message code="genericResource.comment.label" default="Comment" /></span>
					
						<span class="property-value" aria-labelledby="comment-label"><g:fieldValue bean="${genericResourceInstance}" field="comment"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${genericResourceInstance?.dateCreated}">
				<li class="fieldcontain">
					<span id="dateCreated-label" class="property-label"><g:message code="genericResource.dateCreated.label" default="Date Created" /></span>
					
						<span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${genericResourceInstance?.dateCreated}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${genericResourceInstance?.extTags}">
				<li class="fieldcontain">
					<span id="extTags-label" class="property-label"><g:message code="genericResource.extTags.label" default="Ext Tags" /></span>
					
						<g:each in="${genericResourceInstance.extTags}" var="e">
						<span class="property-value" aria-labelledby="extTags-label"><g:link controller="genericResourceHasTag" action="show" id="${e.id}">${e?.encodeAsHTML()}</g:link></span>
						</g:each>
					
				</li>
				</g:if>
			
				<g:if test="${genericResourceInstance?.lastUpdated}">
				<li class="fieldcontain">
					<span id="lastUpdated-label" class="property-label"><g:message code="genericResource.lastUpdated.label" default="Last Updated" /></span>
					
						<span class="property-value" aria-labelledby="lastUpdated-label"><g:formatDate date="${genericResourceInstance?.lastUpdated}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${genericResourceInstance?.roleHasEventHasRessources}">
				<li class="fieldcontain">
					<span id="roleHasEventHasRessources-label" class="property-label"><g:message code="genericResource.roleHasEventHasRessources.label" default="Role Has Event Has Ressources" /></span>
					
						<g:each in="${genericResourceInstance.roleHasEventHasRessources}" var="r">
						<span class="property-value" aria-labelledby="roleHasEventHasRessources-label"><g:link controller="roleHasEventHasGenericResource" action="show" id="${r.id}">${r?.encodeAsHTML()}</g:link></span>
						</g:each>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${genericResourceInstance?.id}" />
					<g:link class="edit" action="edit" id="${genericResourceInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
