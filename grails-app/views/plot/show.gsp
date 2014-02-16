
<%@ page import="org.gnk.selectintrigue.Plot" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'plot.label', default: 'Plot')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-plot" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-plot" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list plot">
			
				<g:if test="${plotInstance?.name}">
				<li class="fieldcontain">
					<span id="name-label" class="property-label"><g:message code="plot.name.label" default="Name" /></span>
					
						<span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${plotInstance}" field="name"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${plotInstance?.creationDate}">
				<li class="fieldcontain">
					<span id="creationDate-label" class="property-label"><g:message code="plot.creationDate.label" default="Creation Date" /></span>
					
						<span class="property-value" aria-labelledby="creationDate-label"><g:formatDate date="${plotInstance?.creationDate}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${plotInstance?.dateCreated}">
				<li class="fieldcontain">
					<span id="dateCreated-label" class="property-label"><g:message code="plot.dateCreated.label" default="Date Created" /></span>
					
						<span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${plotInstance?.dateCreated}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${plotInstance?.description}">
				<li class="fieldcontain">
					<span id="description-label" class="property-label"><g:message code="plot.description.label" default="Description" /></span>
					
						<span class="property-value" aria-labelledby="description-label"><g:fieldValue bean="${plotInstance}" field="description"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${plotInstance?.events}">
				<li class="fieldcontain">
					<span id="events-label" class="property-label"><g:message code="plot.events.label" default="Events" /></span>
					
						<g:each in="${plotInstance.events}" var="e">
						<span class="property-value" aria-labelledby="events-label"><g:link controller="event" action="show" id="${e.id}">${e?.encodeAsHTML()}</g:link></span>
						</g:each>
					
				</li>
				</g:if>
			
				<g:if test="${plotInstance?.isDraft}">
				<li class="fieldcontain">
					<span id="isDraft-label" class="property-label"><g:message code="plot.isDraft.label" default="Is Draft" /></span>
					
						<span class="property-value" aria-labelledby="isDraft-label"><g:formatBoolean boolean="${plotInstance?.isDraft}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${plotInstance?.isEvenemential}">
				<li class="fieldcontain">
					<span id="isEvenemential-label" class="property-label"><g:message code="plot.isEvenemential.label" default="Is Evenemential" /></span>
					
						<span class="property-value" aria-labelledby="isEvenemential-label"><g:formatBoolean boolean="${plotInstance?.isEvenemential}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${plotInstance?.isMainstream}">
				<li class="fieldcontain">
					<span id="isMainstream-label" class="property-label"><g:message code="plot.isMainstream.label" default="Is Mainstream" /></span>
					
						<span class="property-value" aria-labelledby="isMainstream-label"><g:formatBoolean boolean="${plotInstance?.isMainstream}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${plotInstance?.isPublic}">
				<li class="fieldcontain">
					<span id="isPublic-label" class="property-label"><g:message code="plot.isPublic.label" default="Is Public" /></span>
					
						<span class="property-value" aria-labelledby="isPublic-label"><g:formatBoolean boolean="${plotInstance?.isPublic}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${plotInstance?.lastUpdated}">
				<li class="fieldcontain">
					<span id="lastUpdated-label" class="property-label"><g:message code="plot.lastUpdated.label" default="Last Updated" /></span>
					
						<span class="property-value" aria-labelledby="lastUpdated-label"><g:formatDate date="${plotInstance?.lastUpdated}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${plotInstance?.extTags}">
				<li class="fieldcontain">
					<span id="plotHasPlotTags-label" class="property-label"><g:message code="plot.plotHasPlotTags.label" default="Plot Has Plot Tags" /></span>
					
						<g:each in="${plotInstance.extTags}" var="p">
						<span class="property-value" aria-labelledby="plotHasPlotTags-label"><g:link controller="plotHasPlotTag" action="show" id="${p.id}">${p?.encodeAsHTML()}</g:link></span>
						</g:each>
					
				</li>
				</g:if>
			
				<g:if test="${plotInstance?.plotHasUniverses}">
				<li class="fieldcontain">
					<span id="plotHasUniverses-label" class="property-label"><g:message code="plot.plotHasUniverses.label" default="Plot Has Universes" /></span>
					
						<g:each in="${plotInstance.plotHasUniverses}" var="p">
						<span class="property-value" aria-labelledby="plotHasUniverses-label"><g:link controller="plotHasUnivers" action="show" id="${p.id}">${p?.encodeAsHTML()}</g:link></span>
						</g:each>
					
				</li>
				</g:if>
			
				<g:if test="${plotInstance?.roles}">
				<li class="fieldcontain">
					<span id="roles-label" class="property-label"><g:message code="plot.roles.label" default="Roles" /></span>
					
						<g:each in="${plotInstance.roles}" var="r">
						<span class="property-value" aria-labelledby="roles-label"><g:link controller="role" action="show" id="${r.id}">${r?.encodeAsHTML()}</g:link></span>
						</g:each>
					
				</li>
				</g:if>
			
				<g:if test="${plotInstance?.updatedDate}">
				<li class="fieldcontain">
					<span id="updatedDate-label" class="property-label"><g:message code="plot.updatedDate.label" default="Updated Date" /></span>
					
						<span class="property-value" aria-labelledby="updatedDate-label"><g:formatDate date="${plotInstance?.updatedDate}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${plotInstance?.user}">
				<li class="fieldcontain">
					<span id="user-label" class="property-label"><g:message code="plot.user.label" default="User" /></span>
					
						<span class="property-value" aria-labelledby="user-label"><g:link controller="user" action="show" id="${plotInstance?.user?.id}">${plotInstance?.user?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${plotInstance?.id}" />
					<g:link class="edit" action="edit" id="${plotInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
