<%@ page import="org.gnk.tag.Tag" %>
<form class="form-inline">
  <g:textField name="name" maxlength="45" value="${tagInstance?.name}"/>
  <g:link controller="tagHasTagFamily" action="create" params="['tag.id': tagInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'tagHasTagFamily.label', default: 'TagHasTagFamily')])}</g:link>
</form>
	<div class="fieldcontain ${hasErrors(bean: tagInstance, field: 'name', 'error')} ">
		<label for="name">
			<g:message code="tag.name.label" default="Name" />
			
		</label>
		<g:textField name="name" maxlength="45" value="${tagInstance?.name}"/>
	</div>
	
	<div class="fieldcontain ${hasErrors(bean: tagInstance, field: 'tagFamilies', 'error')} ">
		<label for="tagFamilies">
			<g:message code="tag.tagFamilies.label" default="Tag Families" />
			
		</label>
		
	<ul class="one-to-many">
	<g:each in="${tagInstance?.tagFamilies?}" var="t">
	    <li><g:link controller="tagHasTagFamily" action="show" id="${t.id}">${t?.encodeAsHTML()}</g:link></li>
	</g:each>
	<li class="add">
	<g:link controller="tagHasTagFamily" action="create" params="['tag.id': tagInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'tagHasTagFamily.label', default: 'TagHasTagFamily')])}</g:link>
	</li>
	</ul>
	</div>
