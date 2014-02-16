<%@ page import="org.gnk.selectintrigue.Plot" %>

<div class="fieldcontain ${hasErrors(bean: plotInstance, field: 'name', 'error')} ">
	<label for="name">
		<g:message code="plot.name.label" default="Name" />
		
	</label>
	<g:textField name="name" maxlength="45" value="${plotInstance?.name}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: plotInstance, field: 'description', 'error')} ">
	<label for="description">
		<g:message code="plot.description.label" default="Description" />
		
	</label>
	<g:textField name="description" value="${plotInstance?.description}"/>
</div>

<label class="checkbox">
  <input type="checkbox" value="${plotInstance?.isDraft}">
	  L'intrigue est-elle un brouillon ?
</label>

<label class="checkbox">
  <input type="checkbox" value="${plotInstance?.isEvenemential}">
	  L'intrigue est-elle de type évenementielle ?
</label>

<label class="checkbox">
  <input type="checkbox" value="${plotInstance?.isMainstream}">
	  L'intrigue est-elle de type mainstream ?
</label>

<label class="checkbox">
  <input type="checkbox" value="${plotInstance?.isPublic}">
	  L'intrigue est-elle publique ?
</label>


<div class="fieldcontain ${hasErrors(bean: plotInstance, field: 'user', 'error')} required">
	<label for="user">
		<g:message code="plot.user.label" default="User" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="user" name="user.id" from="${org.gnk.user.User.list()}" optionKey="id" required="" value="${plotInstance?.user?.id}" class="many-to-one"/>
</div>

