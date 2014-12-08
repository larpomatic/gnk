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
            </td>
        </tr>
    </g:each>
    </tbody>
</table>

<!-- Modal Views -->
<g:render template="modalViewTags" model="[listTagParent: listTagParent]"/>
<g:render template="modaleditViewTags"/>

<g:each in="${Tag.list()}" var="tag">
    <div id="tagListmodal${tag.id}" class="modal hide fade tags-modal" tabindex="-1">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal">×</button>
            <h3 id="myModalLabel">Choix du parent</h3>
            <input class="input-medium search-query" data-content="tagList"
                   placeholder="<g:message code="selectintrigue.search" default="Search..."/>"/>
        </div>
        <div class="modal-body">
            <ul class="tagList">
                <g:each in="${tag}" status="i" var="tagInstance">
                    <g:render template="TagTreeDetails" model="[tagInstance: tagInstance]"/>
                </g:each>
            </ul>
        </div>
    </div>
</g:each>