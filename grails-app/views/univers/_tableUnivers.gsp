<%@ page import="org.gnk.selectintrigue.Plot; org.gnk.tag.Univers" %>
<legend>${message(code: 'adminRef.univers.list')}</legend>

<table class="table table-bordered">
	<thead>
		<tr>
			<g:sortableColumn property="name" title="${message(code: 'adminRef.univers.name', default: 'Name')}" />
			<g:sortableColumn property="Plots" title="Intrigues" />
            <th><g:message code="default.delete"/></th>
		</tr>
	</thead>
	<tbody>
	<g:each in="${universInstanceList}" status="i" var="universInstance">
		<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
			<td><g:link action="show" id="${universInstance.id}">${fieldValue(bean: universInstance, field: "name")}</g:link></td>
			<td>
                <ul class="inline">
                    <g:each in="${Plot.list()}" var="plot">
                        <g:each in="${plot.plotHasUniverses}" var="plotHasUniversesVar">
                            <g:if test="${plotHasUniversesVar.univers.equals(universInstance)}">
                                <li class="badge badge-info">
                                    ${plot.name} ${plotHasUniversesVar.weight}%
                                </li>
                            </g:if>
                        </g:each>
                    </g:each>
                </ul>
            </td>
			<td>
				<g:form>
					<fieldset class="buttons">
						<g:hiddenField name="idUnivers" value="${universInstance?.id}" />
						<g:actionSubmit class="btn btn-warning" action="deleteUnivers" value="${message(code: 'default.delete')}" onclick="return confirm('${message(code: 'adminRef.univers.deleteUnivers')}');" />
					</fieldset>
				</g:form>
			</td>
		</tr>
	</g:each>
	</tbody>
</table>
