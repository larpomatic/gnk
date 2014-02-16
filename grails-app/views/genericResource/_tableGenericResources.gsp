<%@ page import="org.gnk.resplacetime.GenericResource" %>

<legend>Administration des ressources génériques existantes</legend>

<table class="table table-bordered">
	<thead>
		<tr>
			<g:sortableColumn property="code" title="${message(code: 'genericResource.code.label', default: 'Nom')}" />
			<g:sortableColumn property="comment" title="${message(code: 'genericResource.comment.label', default: 'Description')}" />
			<g:sortableColumn property="tags" title="${message(code: 'resource.dateCreated.label', default: 'Tags')}" />
			<g:sortableColumn property="delete" title="${message(code: 'resource.dateCreated.label', default: ' ')}" />
		</tr>
	</thead>
	<tbody>
	<g:each in="${genericResourceInstanceList}" status="i" var="genericResourceInstance">
		<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
		
			<td><g:link action="show" id="${genericResourceInstance.id}">${fieldValue(bean: genericResourceInstance, field: "code")}</g:link></td>
			<td>${fieldValue(bean: genericResourceInstance, field: "comment")}</td>
		
			<td>
				<ul class="inline">
					<g:each in="${genericResourceInstance.extTags.toArray()}" status="j" var="extTags">
						<li class="badge badge-info">
							<g:form class="form-small">
								<g:hiddenField name="idTag" value="${extTags.id}" />
								${extTags.tag.name} ${extTags.weight}%
								<g:actionSubmit class="icon-remove remove-action" controller="genericRessource" action="deleteTag" value=" " onclick="return confirm('${message(code: 'adminRef.resource.deleteTag')}');" />
							</g:form>
						</li>
					</g:each>
				</ul>
			</td>

			<td>
				<g:form>
					<fieldset class="buttons">
						<g:hiddenField name="id" value="${genericResourceInstance?.id}" />
						<g:actionSubmit class="btn btn-warning" action="deleteResource" value="${message(code: 'default.delete')}" onclick="return confirm('Supprimer la ressource ?');" />
					</fieldset>
				</g:form>
			</td>
		</tr>
	</g:each>
	</tbody>
</table>