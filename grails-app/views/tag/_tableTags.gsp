<%@ page import="org.gnk.tag.Tag" %>
<legend>${message(code: 'adminRef.tag.list')}</legend>

<table class="table table-bordered">
	<thead>
		<tr>
            <th>#</th>
			<g:sortableColumn property="name" title="${message(code: 'adminRef.tag.tagName')}" />
            <th>Utilisations du tag</th>
			%{--<g:sortableColumn property="tagFamily.value" title="${message(code: 'adminRef.tag.tagFamilies')}" />--}%
            <th><g:message code="default.delete"/></th>
		</tr>
	</thead>
	<tbody>
		<g:each in="${tagInstanceList}" status="i" var="tagInstance">
			<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                <td>${i+1}</td>
				<td>${fieldValue(bean: tagInstance, field: "name")}</td>
                <td><a href="#modal${tagInstance.id}" role="button" class="btn" data-toggle="modal">voir le détail</a></td>
				%{--<td>--}%
					%{--<ul class="inline">--}%
						%{--<g:if test="${tagInstance}">--}%
							%{--<li class="badge badge-info">--}%
								%{--<g:form class="form-small">--}%
									%{--<g:hiddenField name="idTag" value="${tagInstance?.id}" />--}%
									%{--<g:hiddenField name="idFamily" value="${tagInstance?.tagFamily?.id}" />--}%
									%{--${tagInstance?.tagFamily?.value}--}%
									%{--<!--g:actionSubmit class="icon-remove remove-action" controller="tag" action="deleteFamily" value=" " onclick="return confirm('${message(code: 'adminRef.tag.deleteTagFamily')}');" /-->--}%
								%{--</g:form>--}%
							%{--</li>--}%
						%{--</g:if>--}%
					%{--</ul>--}%
				%{--</td>--}%
				<td>
					<g:form>
						<fieldset class="buttons">
							<g:hiddenField name="idTag" value="${tagInstance?.id}" />
							<g:actionSubmit class="btn btn-warning" action="deleteTag" value="${message(code: 'default.delete')}" onclick="return confirm('${message(code: 'adminRef.tag.deleteTag')}');" />
						</fieldset>
					</g:form>
				</td>
			</tr>
		</g:each>
	</tbody>
</table>

<!-- Modal Views -->