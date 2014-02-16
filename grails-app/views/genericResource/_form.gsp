<%@ page import="org.gnk.resplacetime.GenericResource" %>



<div class="fieldcontain ${hasErrors(bean: genericResourceInstance, field: 'code', 'error')} required">
	<label for="code">
		<g:message code="genericResource.code.label" default="Code" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="code" maxlength="45" required="" value="${genericResourceInstance?.code}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: genericResourceInstance, field: 'comment', 'error')} ">
	<label for="comment">
		<g:message code="genericResource.comment.label" default="Comment" />
		
	</label>
	<g:textField name="comment" value="${genericResourceInstance?.comment}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: genericResourceInstance, field: 'extTags', 'error')} ">
	<label for="extTags">
		<g:message code="genericResource.extTags.label" default="Ext Tags" />
		
	</label>
	
<ul class="one-to-many">
<g:each in="${genericResourceInstance?.extTags?}" var="e">
    <li><g:link controller="genericResourceHasTag" action="show" id="${e.id}">${e?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="genericResourceHasTag" action="create" params="['genericResource.id': genericResourceInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'genericResourceHasTag.label', default: 'GenericResourceHasTag')])}</g:link>
</li>
</ul>

</div>

<div class="fieldcontain ${hasErrors(bean: genericResourceInstance, field: 'roleHasEventHasRessources', 'error')} ">
	<label for="roleHasEventHasRessources">
		<g:message code="genericResource.roleHasEventHasRessources.label" default="Role Has Event Has Ressources" />
		
	</label>
	
<ul class="one-to-many">
<g:each in="${genericResourceInstance?.roleHasEventHasRessources?}" var="r">
    <li><g:link controller="roleHasEventHasGenericResource" action="show" id="${r.id}">${r?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="roleHasEventHasGenericResource" action="create" params="['genericResource.id': genericResourceInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'roleHasEventHasGenericResource.label', default: 'RoleHasEventHasGenericResource')])}</g:link>
</li>
</ul>

</div>

