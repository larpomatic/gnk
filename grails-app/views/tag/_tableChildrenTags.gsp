<%@ page import="org.gnk.tag.Tag; org.gnk.admin.right" %>
<legend>${message(code: 'adminRef.tag.list')}</legend>

<table id="listTable" class="table table-bordered">
    <thead>
    <tr>
        <th>#</th>
        <g:sortableColumn property="name" title="${message(code: 'adminRef.tag.tagName')}"/>
        <th>Utilisations du tag</th>

        %{--<g:sortableColumn property="tagFamily.value" title="${message(code: 'adminRef.tag.tagFamilies')}" />--}%

        <g:hasRights lvlright="${right.REFMODIFY.value()}"><th>Tag Relevant</th></g:hasRights>
        <g:hasRights lvlright="${right.REFMODIFY.value()}">
            <g:hasRights lvlright="${right.REFDELETE.value()}">
                <th>
                    <g:message code="default.delete"/>
                </th>
            </g:hasRights>
        </g:hasRights>
        <th>
            Tag fils
        </th>
    </tr>
    </thead>
    <tbody>
    <g:each in="${tagInstanceList}" status="i" var="tagInstance">
        <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
            <td>${i + 1}</td>
            <td>${fieldValue(bean: tagInstance, field: "name")}</td>
            <td><a href="#modalValue" role="button" class="btn  valueButton" data-toggle="modal">voir le détail</a>
                <input type="hidden" id="idTag" name="idTag" value="${tagInstance.id}"></td>
            <g:hasRights lvlright="${right.REFMODIFY.value()}">
                <td>
                    <a href="#editmodal${tagInstance.id}" role="button" class="btn" data-toggle="modal">Editer</a>
                </td>
            </g:hasRights>
            <g:hasRights lvlright="${right.REFMODIFY.value()}">
                <g:hasRights lvlright="${right.REFDELETE.value()}">
                    <td>
                        <input type="hidden" name="idTag" value="${tagInstance?.id}"/>
                        <button data-toggle="confirmation-popout" data-placement="left" class="deleteTagbtn btn btn-warning">${message(code: 'default.delete')}</button>
                    </td>
                </g:hasRights>
            </g:hasRights>
            <td>
                <a href="#tagListmodal${tagInstance.id}" class="btn" data-toggle="modal">
                    Tag fils
                </a>
                <g:hasRights lvlright="${right.REFMODIFY.value()}">
                    <a href="#addchildmodal" class="btn btn-primary buttonAdd" data-toggle="modal">
                        +
                    </a>
                </g:hasRights>
                <input type="hidden" id="idParent" name="idParent" value="${tagInstance.id}">
            </td>
        </tr>
    </g:each>
    </tbody>
</table>
<input type="hidden" name="idTagurl" id="idTagurl" data-url="<g:createLink controller="tag" action="deleteTag"/>"/>
<input type="hidden" name="idTagInformationurl" id="idTagInformationurl" data-url="<g:createLink controller="tag" action="showInformation"/>"/>
<!-- Modal Views -->
<g:render template="modaleditViewTags"/>
<div id="modalViewTag">

</div>
<g:render template="detailTagChildren"/>
<g:render template="addTags"/>


