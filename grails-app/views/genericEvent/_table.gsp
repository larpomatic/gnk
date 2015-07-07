<%@ page import="org.gnk.tag.Tag; org.gnk.admin.right" %>

%{--<legend>${message(code: 'adminRef.tag.list')}</legend>--}%
<legend>Liste des generics events existants</legend>

<table id="listTable" class="table table-bordered">
    <thead>
    <tr>
        <th>#</th>
        <th>description</th>
        <th>ageMin</th>
        <th>ageMax</th>

        <g:hasRights lvlright="${right.REFMODIFY.value()}"><th>Edit</th></g:hasRights>
        <g:hasRights lvlright="${right.REFMODIFY.value()}">
            <g:hasRights lvlright="${right.REFDELETE.value()}">
                <th>
                    %{--<g:message code="default.delete"/>--}%
                    Delete
                </th>
            </g:hasRights>
        </g:hasRights>

    </tr>
    </thead>
    <tbody>
    <g:each in="${genericEventInstanceList}" status="i" var="genericEventInstance">
        <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
        <td>${i + 1}</td>
        <td>${fieldValue(bean: genericEventInstance, field: "description")}</td>
        <td>${fieldValue(bean: genericEventInstance, field: "ageMin")}</td>
        <td>${fieldValue(bean: genericEventInstance, field: "ageMax")}</td>

            <td>
            <g:form>
                <fieldset class="buttons">
                    <g:hiddenField name="id" value="${genericEventInstance?.id}"/>
                    <g:hasRights lvlright="${right.REFMODIFY.value()}">
                        <g:actionSubmit class="btn btn-warning" action="edit"
                                        value="${message(code: 'default.edit')}"/>
                    </g:hasRights>
                </fieldset>
            </g:form>
            </td>
            <td>
        <g:form>
            <fieldset class="buttons">
                <g:hiddenField name="id" value="${genericEventInstance?.id}"/>
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

%{--<input type="hidden" name="idTagurl" id="idTagurl" data-url="<g:createLink controller="genericEvent" action="delete"/>"/>--}%
%{--<input type="hidden" name="idTagediturl" id="idTagediturl" data-url="<g:createLink controller="genericEvent" action="edit"/>"/>--}%
%{--<input type="hidden" name="idTagInformationurl" id="idTagInformationurl" data-url="<g:createLink controller="genericEvent" action="show"/>"/>--}%