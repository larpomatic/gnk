<%@ page import="org.gnk.resplacetime.GenericPlace" %>



<div class="fieldcontain ${hasErrors(bean: genericPlaceInstance, field: 'code', 'error')} required">
	<label for="code">
		<g:message code="genericPlace.code.label" default="Code" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="code" maxlength="45" required="" value="${genericPlaceInstance?.code}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: genericPlaceInstance, field: 'comment', 'error')} ">
	<label for="comment">
		<g:message code="genericPlace.comment.label" default="Comment" />
		
	</label>
	<g:textField name="comment" value="${genericPlaceInstance?.comment}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: genericPlaceInstance, field: 'events', 'error')} ">
	<label for="events">
		<g:message code="genericPlace.events.label" default="Events" />
		
	</label>
	
<ul class="one-to-many">
<g:each in="${genericPlaceInstance?.events?}" var="e">
    <li><g:link controller="event" action="show" id="${e.id}">${e?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="event" action="create" params="['genericPlace.id': genericPlaceInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'event.label', default: 'Event')])}</g:link>
</li>
</ul>

</div>

<div class="fieldcontain ${hasErrors(bean: genericPlaceInstance, field: 'extTags', 'error')} ">
	<label for="extTags">
		<g:message code="genericPlace.extTags.label" default="Ext Tags" />
		
	</label>
	
<ul class="one-to-many">
<g:each in="${genericPlaceInstance?.extTags?}" var="e">
    <li><g:link controller="genericPlaceHasTag" action="show" id="${e.id}">${e?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="genericPlaceHasTag" action="create" params="['genericPlace.id': genericPlaceInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'genericPlaceHasTag.label', default: 'GenericPlaceHasTag')])}</g:link>
</li>
</ul>

</div>

<div class="fieldcontain ${hasErrors(bean: genericPlaceInstance, field: 'pastscenes', 'error')} ">
	<label for="pastscenes">
		<g:message code="genericPlace.pastscenes.label" default="Pastscenes" />
		
	</label>
	
<ul class="one-to-many">
<g:each in="${genericPlaceInstance?.pastscenes?}" var="p">
    <li><g:link controller="pastscene" action="show" id="${p.id}">${p?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="pastscene" action="create" params="['genericPlace.id': genericPlaceInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'pastscene.label', default: 'Pastscene')])}</g:link>
</li>
</ul>

</div>

