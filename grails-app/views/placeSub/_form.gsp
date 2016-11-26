<%@ page import="org.gnk.resplacetime.Place" %>



<div class="fieldcontain ${hasErrors(bean: placeInstance, field: 'name', 'error')} ">
	<label for="name">
		<g:message code="place.name.label" default="Name" />
		
	</label>
	<g:textField name="name" maxlength="45" value="${placeInstance?.name}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: placeInstance, field: 'gender', 'error')} ">
	<label for="gender">
		<g:message code="place.gender.label" default="Gender" />
		
	</label>
	<g:textField name="gender" maxlength="2" value="${placeInstance?.gender}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: placeInstance, field: 'description', 'error')} ">
	<label for="description">
		<g:message code="place.description.label" default="Description" />
		
	</label>
	<g:textField name="description" value="${placeInstance?.description}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: placeInstance, field: 'hasTags', 'error')} ">
	<label for="hasTags">
		<g:message code="place.hasTags.label" default="Has Tags" />
		
	</label>
	
<ul class="one-to-many">
<g:each in="${placeInstance?.hasTags?}" var="h">
    <li><g:link controller="placeHasTag" action="show" id="${h.id}">${h?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="placeHasTag" action="create" params="['place.id': placeInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'placeHasTag.label', default: 'PlaceHasTag')])}</g:link>
</li>
</ul>

</div>

<div class="fieldcontain ${hasErrors(bean: placeInstance, field: 'placeHasUniverses', 'error')} ">
	<label for="placeHasUniverses">
		<g:message code="place.placeHasUniverses.label" default="Place Has Universes" />
		
	</label>
	
<ul class="one-to-many">
<g:each in="${placeInstance?.placeHasUniverses?}" var="p">
    <li><g:link controller="placeHasUnivers" action="show" id="${p.id}">${p?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="placeHasUnivers" action="create" params="['place.id': placeInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'placeHasUnivers.label', default: 'PlaceHasUnivers')])}</g:link>
</li>
</ul>

</div>

