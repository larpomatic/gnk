
<%@ page import="org.gnk.resplacetime.GenericPlace" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'genericPlace.label', default: 'GenericPlace')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-genericPlace" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-genericPlace" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list genericPlace">
			
				<g:if test="${genericPlaceInstance?.code}">
				<li class="fieldcontain">
					<span id="code-label" class="property-label"><g:message code="genericPlace.code.label" default="Code" /></span>
					
						<span class="property-value" aria-labelledby="code-label"><g:fieldValue bean="${genericPlaceInstance}" field="code"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${genericPlaceInstance?.comment}">
				<li class="fieldcontain">
					<span id="comment-label" class="property-label"><g:message code="genericPlace.comment.label" default="Comment" /></span>
					
						<span class="property-value" aria-labelledby="comment-label"><g:fieldValue bean="${genericPlaceInstance}" field="comment"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${genericPlaceInstance?.dateCreated}">
				<li class="fieldcontain">
					<span id="dateCreated-label" class="property-label"><g:message code="genericPlace.dateCreated.label" default="Date Created" /></span>
					
						<span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${genericPlaceInstance?.dateCreated}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${genericPlaceInstance?.events}">
				<li class="fieldcontain">
					<span id="events-label" class="property-label"><g:message code="genericPlace.events.label" default="Events" /></span>
					
						<g:each in="${genericPlaceInstance.events}" var="e">
						<span class="property-value" aria-labelledby="events-label"><g:link controller="event" action="show" id="${e.id}">${e?.encodeAsHTML()}</g:link></span>
						</g:each>
					
				</li>
				</g:if>
			
				<g:if test="${genericPlaceInstance?.extTags}">
				<li class="fieldcontain">
					<span id="extTags-label" class="property-label"><g:message code="genericPlace.extTags.label" default="Ext Tags" /></span>
					
						<g:each in="${genericPlaceInstance.extTags}" var="e">
						<span class="property-value" aria-labelledby="extTags-label"><g:link controller="genericPlaceHasTag" action="show" id="${e.id}">${e?.encodeAsHTML()}</g:link></span>
						</g:each>
					
				</li>
				</g:if>
			
				<g:if test="${genericPlaceInstance?.lastUpdated}">
				<li class="fieldcontain">
					<span id="lastUpdated-label" class="property-label"><g:message code="genericPlace.lastUpdated.label" default="Last Updated" /></span>
					
						<span class="property-value" aria-labelledby="lastUpdated-label"><g:formatDate date="${genericPlaceInstance?.lastUpdated}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${genericPlaceInstance?.pastscenes}">
				<li class="fieldcontain">
					<span id="pastscenes-label" class="property-label"><g:message code="genericPlace.pastscenes.label" default="Pastscenes" /></span>
					
						<g:each in="${genericPlaceInstance.pastscenes}" var="p">
						<span class="property-value" aria-labelledby="pastscenes-label"><g:link controller="pastscene" action="show" id="${p.id}">${p?.encodeAsHTML()}</g:link></span>
						</g:each>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${genericPlaceInstance?.id}" />
					<g:link class="edit" action="edit" id="${genericPlaceInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
