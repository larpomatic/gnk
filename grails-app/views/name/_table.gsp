<%@ page import="org.gnk.naming.Name" %>
<%@ page import="org.gnk.admin.right" %>


%{--<legend>${message(code: 'adminRef.tag.list')}</legend>--}%
<legend>Liste des Noms</legend>

<table id="listTable" class="table table-bordered">
    <thead>
    <tr>
        <th>Nom</th>
        <th>Genre</th>
        <th>Tag</th>
        <th>Date Creation</th>
        <th>Date Modification</th>

        <g:hasRights lvlright="${right.REFMODIFY.value()}">
            <g:hasRights lvlright="${right.REFDELETE.value()}">
                <th>
                    %{--<g:message code="default.delete"/>--}%
                    Supprimer
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
            <td>
                <g:each in="${NameInstance.getNameHasTag()}" var="tag">
                    <g:if test="${tag.weight > 50}">
                        <span class="label mytool label-success" data-tag="none" contenteditable="false" toggle="tooltip"  data-placement="top" data-original-title="${tag.weight}" title="">${tag.tag.name}</span>
                    </g:if>
                    <g:elseif test="${tag.weight >=0}">
                        <span class="label mytool label-info" data-tag="none" contenteditable="false" toggle="tooltip" data-placement="top" data-original-title="${tag.weight}" title="">${tag.tag.name}</span>
                    </g:elseif>
                    <g:elseif test="${tag.weight >= -50}">
                        <span class="label mytool label-warning" data-tag="none" contenteditable="false" toggle="tooltip" data-placement="top" data-original-title="${tag.weight}" title="">${tag.tag.name}</span>
                    </g:elseif>
                    <g:else>
                        <span class="label mytool label-important" data-tag="none" contenteditable="false" toggle="tooltip" data-placement="top" data-original-title="${tag.weight}" title="">${tag.tag.name}</span>
                    </g:else>
                </g:each>
            </td>
            <td>${NameInstance.dateCreated.format("dd/MM/yyyy")}</td>
            <td>${NameInstance.lastUpdated.format("dd/MM/yyyy")}</td>
            <td>
        <g:form>
            <fieldset class="buttons">
                <g:hiddenField name="id" value="${NameInstance.id}"/>
                <g:hasRights lvlright="${right.REFDELETE.value()}">
                    <g:actionSubmit class="btn btn-danger" action="delete"
                                    value="${message(code: 'default.delete')}"
                                    onclick="return confirm('Delete this name?');"/>
                </g:hasRights>
            </fieldset>
        </g:form>
            </td>
        </tr>
    </g:each>
    </tbody>
</table>