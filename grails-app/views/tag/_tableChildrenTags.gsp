<%@ page import="org.gnk.tag.Tag; org.gnk.admin.right" %>
<legend>${message(code: 'adminRef.tag.list')}</legend>

<table class="table table-bordered">
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
            <td><a href="#modal${tagInstance.id}" role="button" class="btn" data-toggle="modal">voir le détail</a></td>
            <g:hasRights lvlright="${right.REFMODIFY.value()}">
                <td>
                    <a href="#editmodal${tagInstance.id}" role="button" class="btn" data-toggle="modal">Editer</a>
                </td>
            </g:hasRights>
            <g:hasRights lvlright="${right.REFMODIFY.value()}">
                <g:hasRights lvlright="${right.REFDELETE.value()}">
                    <td>
                        <g:form>
                            <fieldset class="buttons">
                                <g:hiddenField name="idTag" value="${tagInstance?.id}"/>
                                <g:actionSubmit class="btn btn-warning" action="deleteTag"
                                                value="${message(code: 'default.delete')}"
                                                onclick="return confirm('${message(code: 'adminRef.tag.deleteTag')}');"/>
                            </fieldset>
                        </g:form>
                    </td>
                </g:hasRights>
            </g:hasRights>
            <td>
                <a href="#tagListmodal${tagInstance.id}" class="btn" data-toggle="modal">
                    Tag fils
                </a>
                <a href="#addchildmodal" class="btn btn-primary" id="buttonAdd" data-toggle="modal">
                    +
                </a>
                <input type="hidden" id="idParent" name="idParent" value="${tagInstance.id}">
            </td>
        </tr>
    </g:each>
    </tbody>
</table>

<!-- Modal Views -->
<g:render template="modalViewTags" model="[listTagParent: listTagParent]"/>
<g:render template="modaleditViewTags"/>
<g:render template="detailTagChildren"/>

<div id="addchildmodal" class="modal hide fade" style="width: 400px; margin-left: -400px;"
     role="dialog" aria-hidden="true">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3 id="myModalLabel">Editer les Tags Relevant</h3>
    </div>
    <g:form action="save">

        <input type="hidden" id="idParentSave" name="idParentSave">
        <div class="modal-body">
        <label>Nom du fils<input id="nameTag" name="nameTag"/></label>

        <div class="modal-footer">
            <button class="btn btn-primary" type="submit">Valider</button>
            <a class="btn" data-dismiss="modal" aria-hidden="true">Annuler</a>
        </div>
    </g:form>
</div>

<g:javascript src="tag/addTagChild.js"/>