<%@ page import="org.gnk.tag.Tag" %>
<legend>${message(code: 'adminRef.tag.list')}</legend>

<table class="table table-bordered">
    <thead>
    <tr>
        <th>#</th>
        <g:sortableColumn property="name" title="${message(code: 'adminRef.tag.tagName')}" />
        <th>Utilisations du tag</th>
        %{--<g:sortableColumn property="tagFamily.value" title="${message(code: 'adminRef.tag.tagFamilies')}" />--}%
        <th>Tag Relevant</th>
        <th><g:message code="default.delete"/></th>
    </tr>
    </thead>
    <tbody>
    <g:each in="${tagInstanceList}" status="i" var="tagInstance">
        <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
            <td>${i+1}</td>
            <td>${fieldValue(bean: tagInstance, field: "name")}</td>
            <td><a href="#modal${tagInstance.id}" role="button" class="btn" data-toggle="modal">voir le détail</a></td>
            <td>
                <a href="#editmodal${tagInstance.id}" role="button" class="btn" data-toggle="modal">Editer</a>
            </td>

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
<g:render template="modalViewTags" />
<g:render template="modaleditViewTags" />