<%@ page import="org.gnk.tag.Tag" %>
<h4>${message(code: 'adminRef.tag.list')}</h4>

<table class="table table-bordered">
	<thead>
		<tr>
			<g:sortableColumn property="Name" title="${message(code: 'adminRef.tag.tagName')}" />
			<g:sortableColumn property="Families" title="${message(code: 'adminRef.tag.tagFamilies')}" />
            <th>Utilisations du tag</th>
		</tr>
	</thead>
	<tbody>
		<g:each in="${Tag.list()}" status="i" var="tagInstance">
			<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
				<td><g:link action="show" id="${tagInstance.id}">${tagInstance.name.encodeAsHTML()}</g:link></td>
				<td>
					<ul class="inline">
  						<span class="label label-info">
  						%{--<li class="label label-info">${tagInstance.tagFamily.value} </li>--}%
						</span>
					</ul>
				</td>
                <td>
                    <a href="#modalValue${tagInstance.id}" role="button" class="btn valueButton" data-toggle="modal">voir le détail</a>
                    <input type="hidden" id="idTag" name="idTag" value="${tagInstance.id}">
                </td>
			</tr>
		</g:each>
	</tbody>
</table>
<input type="hidden" name="idTagInformationurl" id="idTagInformationurl" data-url="<g:createLink controller="tag" action="showInformation"/>"/>
<!-- Modal Views -->
<div id="modalViewTag">
</div>
<g:javascript src="tag/modalTag.js"/>