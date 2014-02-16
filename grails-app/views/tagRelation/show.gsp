
<%@ page import="org.gnk.tag.TagRelation" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'tagRelation.label', default: 'TagRelation')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-tagRelation" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-tagRelation" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list tagRelation">
			
				<g:if test="${tagRelationInstance?.dateCreated}">
				<li class="fieldcontain">
					<span id="dateCreated-label" class="property-label"><g:message code="tagRelation.dateCreated.label" default="Date Created" /></span>
					
						<span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${tagRelationInstance?.dateCreated}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${tagRelationInstance?.isBijective}">
				<li class="fieldcontain">
					<span id="isBijective-label" class="property-label"><g:message code="tagRelation.isBijective.label" default="Is Bijective" /></span>
					
						<span class="property-value" aria-labelledby="isBijective-label"><g:formatBoolean boolean="${tagRelationInstance?.isBijective}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${tagRelationInstance?.lastUpdated}">
				<li class="fieldcontain">
					<span id="lastUpdated-label" class="property-label"><g:message code="tagRelation.lastUpdated.label" default="Last Updated" /></span>
					
						<span class="property-value" aria-labelledby="lastUpdated-label"><g:formatDate date="${tagRelationInstance?.lastUpdated}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${tagRelationInstance?.tag1}">
				<li class="fieldcontain">
					<span id="tag1-label" class="property-label"><g:message code="tagRelation.tag1.label" default="Tag1" /></span>
					
						<span class="property-value" aria-labelledby="tag1-label"><g:link controller="tag" action="show" id="${tagRelationInstance?.tag1?.id}">${tagRelationInstance?.tag1?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${tagRelationInstance?.tag2}">
				<li class="fieldcontain">
					<span id="tag2-label" class="property-label"><g:message code="tagRelation.tag2.label" default="Tag2" /></span>
					
						<span class="property-value" aria-labelledby="tag2-label"><g:link controller="tag" action="show" id="${tagRelationInstance?.tag2?.id}">${tagRelationInstance?.tag2?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${tagRelationInstance?.weight}">
				<li class="fieldcontain">
					<span id="weight-label" class="property-label"><g:message code="tagRelation.weight.label" default="Weight" /></span>
					
						<span class="property-value" aria-labelledby="weight-label"><g:fieldValue bean="${tagRelationInstance}" field="weight"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${tagRelationInstance?.id}" />
					<g:link class="edit" action="edit" id="${tagRelationInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
