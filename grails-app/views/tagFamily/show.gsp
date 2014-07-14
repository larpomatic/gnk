
%{--<%@ page import="org.gnk.tag.TagFamily" %>--}%
%{--<!DOCTYPE html>--}%
%{--<html>--}%
	%{--<head>--}%
		%{--<meta name="layout" content="main">--}%
		%{--<g:set var="entityName" value="${message(code: 'tagFamily.label', default: 'TagFamily')}" />--}%
		%{--<title><g:message code="default.show.label" args="[entityName]" /></title>--}%
	%{--</head>--}%
	%{--<body>--}%
		%{--<a href="#show-tagFamily" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>--}%
		%{--<div class="nav" role="navigation">--}%
			%{--<ul>--}%
				%{--<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>--}%
				%{--<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>--}%
				%{--<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>--}%
			%{--</ul>--}%
		%{--</div>--}%
		%{--<div id="show-tagFamily" class="content scaffold-show" role="main">--}%
			%{--<h1><g:message code="default.show.label" args="[entityName]" /></h1>--}%
			%{--<g:if test="${flash.message}">--}%
			%{--<div class="message" role="status">${flash.message}</div>--}%
			%{--</g:if>--}%
			%{--<ol class="property-list tagFamily">--}%
			%{----}%
				%{--<g:if test="${tagFamilyInstance?.value}">--}%
				%{--<li class="fieldcontain">--}%
					%{--<span id="value-label" class="property-label"><g:message code="tagFamily.value.label" default="Value" /></span>--}%
					%{----}%
						%{--<span class="property-value" aria-labelledby="value-label"><g:fieldValue bean="${tagFamilyInstance}" field="value"/></span>--}%
					%{----}%
				%{--</li>--}%
				%{--</g:if>--}%
			%{----}%
				%{--<g:if test="${tagFamilyInstance?.dateCreated}">--}%
				%{--<li class="fieldcontain">--}%
					%{--<span id="dateCreated-label" class="property-label"><g:message code="tagFamily.dateCreated.label" default="Date Created" /></span>--}%
					%{----}%
						%{--<span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${tagFamilyInstance?.dateCreated}" /></span>--}%
					%{----}%
				%{--</li>--}%
				%{--</g:if>--}%
			%{----}%
				%{--<g:if test="${tagFamilyInstance?.lastUpdated}">--}%
				%{--<li class="fieldcontain">--}%
					%{--<span id="lastUpdated-label" class="property-label"><g:message code="tagFamily.lastUpdated.label" default="Last Updated" /></span>--}%
					%{----}%
						%{--<span class="property-value" aria-labelledby="lastUpdated-label"><g:formatDate date="${tagFamilyInstance?.lastUpdated}" /></span>--}%
					%{----}%
				%{--</li>--}%
				%{--</g:if>--}%
			%{----}%
				%{--<g:if test="${tagFamilyInstance?.tags}">--}%
				%{--<li class="fieldcontain">--}%
					%{--<span id="tags-label" class="property-label"><g:message code="tagFamily.tags.label" default="Tags" /></span>--}%
					%{----}%
						%{--<g:each in="${tagFamilyInstance.tags}" var="t">--}%
						%{--<span class="property-value" aria-labelledby="tags-label"><g:link controller="tagHasTagFamily" action="show" id="${t.id}">${t?.encodeAsHTML()}</g:link></span>--}%
						%{--</g:each>--}%
					%{----}%
				%{--</li>--}%
				%{--</g:if>--}%
			%{----}%
			%{--</ol>--}%
			%{--<g:form>--}%
				%{--<fieldset class="buttons">--}%
					%{--<g:hiddenField name="id" value="${tagFamilyInstance?.id}" />--}%
					%{--<g:link class="edit" action="edit" id="${tagFamilyInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>--}%
					%{--<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />--}%
				%{--</fieldset>--}%
			%{--</g:form>--}%
		%{--</div>--}%
	%{--</body>--}%
%{--</html>--}%
