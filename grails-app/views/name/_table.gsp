<%@ page import="org.gnk.naming.Name" %>
<%@ page import="org.gnk.admin.right" %>


%{--<legend>${message(code: 'adminRef.tag.list')}</legend>--}%
<legend>Liste des Noms</legend>

<table id="listTable" class="table table-bordered">
    <thead>
    <tr>
        <th>Nom</th>
        <th>Genre</th>
        <th>Date Creation</th>
        <th>Date Modification</th>

        <g:hasRights lvlright="${right.REFMODIFY.value()}">
            <g:hasRights lvlright="${right.REFDELETE.value()}">
                <th>
                    %{--<g:message code="default.delete"/>--}%
                    Suprimmer
                </th>
            </g:hasRights>
        </g:hasRights>

    </tr>
    </thead>
    <tbody>
    <g:each in="${NameInstanceList}" status="i" var="NameInstance">
        <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
            <td>
                <g:hasRights lvlright="${right.REFMODIFY.value()}">
                   <a href="${createLink(action: "edit", id: "${NameInstance.id}")}">
                        ${fieldValue(bean: NameInstance, field: "name")}</a>
                </g:hasRights>
            </td>

            <td>${fieldValue(bean: NameInstance, field: "gender")}</td>
            <td>TAG</td>
            <td>${fieldValue(bean: NameInstance, field: "dateCreated")}</td>
            <td>${fieldValue(bean: NameInstance, field: "lastUpdated")}</td>
            <td>
        <g:form>
            <fieldset class="buttons">
                <g:hiddenField name="id" value="${NameInstance?.id}"/>
                <g:hasRights lvlright="${right.REFDELETE.value()}">
                    <g:actionSubmit class="btn btn-danger" action="delete"
                                    value="${message(code: 'default.delete')}"
                                    onclick="return confirm('Delete this plot ?');"/>
                </g:hasRights>
            </fieldset>
        </g:form>
            </td>
        </tr>
    </g:each>
    </tbody>
</table>