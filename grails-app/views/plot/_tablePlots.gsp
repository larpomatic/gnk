<%@ page import="org.gnk.selectintrigue.Plot" %>
<legend>${message(code: 'adminRef.plot.list')}</legend>

<table class="table table-bordered">
	<thead>
		<tr>
			<g:sortableColumn property="name" title="${message(code: 'plot.name.label', default: 'Name')}" />
			<g:sortableColumn property="tags" title="Tags" />
			<g:sortableColumn property="Univers" title="Univers" />
			<g:sortableColumn property="description" title="${message(code: 'plot.description.label', default: 'Description')}" />
            <th><g:message code="default.delete"/></th>
		</tr>
	</thead>
    <tbody>

	<g:each in="${plotInstanceList}" status="i" var="plotInstance">
		<tr>
			<td><g:link action="show" controller="redactIntrigue" id="${plotInstance.id}">${fieldValue(bean: plotInstance, field: "name")}</g:link></td>
			<td>
				<ul class="inline">
					<g:each in="${plotInstance.extTags}" status="j" var="plotHasPlotTags">
						<li class="badge badge-info">
							<g:form class="form-small">
								<g:hiddenField name="plotHaTagId" value="${plotHasPlotTags.id}" />
								${plotHasPlotTags.tag.name} ${plotHasPlotTags.weight}%
								<g:actionSubmit class="icon-remove remove-action" controller="plot" action="deleteTag" value=" " onclick="return confirm('${message(code: 'adminRef.plot.deleteTag')}');" />
							</g:form>
						</li>
					</g:each>
				</ul>
            </td>
			<td>
                <ul class="inline">
                    <g:each in="${plotInstance.plotHasUniverses.toArray()}" status="j" var="plotHasUniverses">
                        <li class="badge badge-info">
                            <g:form class="form-small">
                                <g:hiddenField name="plotHasUniversesId" value="${plotHasUniverses.id}" />
                                ${plotHasUniverses.univers.name} ${plotHasUniverses.weight}%
                                <g:actionSubmit class="icon-remove remove-action" controller="plot" action="removeUnivers" value=" " onclick="return confirm('${message(code: 'adminRef.plot.removeUnivers')}');" />
                            </g:form>
                        </li>
                    </g:each>
                </ul>
			</td>

			<td>${fieldValue(bean: plotInstance, field: "description")}</td>
	

			<td>
				<g:form>
					<fieldset class="buttons">
						<g:hiddenField name="id" value="${plotInstance?.id}" />
						<g:actionSubmit class="btn btn-warning" action="deleteFullPlot" value="${message(code: 'default.delete')}" onclick="return confirm('${message(code: 'adminRef.plot.deletePlot')}');" />
					</fieldset>
				</g:form>
			</td>
		</tr>
	</g:each>
	</tbody>
</table>