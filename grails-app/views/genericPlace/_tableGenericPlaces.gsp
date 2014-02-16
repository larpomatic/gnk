<%@ page import="org.gnk.resplacetime.GenericPlace" %>
<table class="table table-bordered">
    <thead>
    <tr>

        <g:sortableColumn property="code" title="${message(code: 'place.name.label', default: 'Code')}" />
        <g:sortableColumn property="commentaire" title="${message(code: 'place.description.label', default: 'Commentaire')}" />


    </tr>
    </thead>
    <tbody>
    <g:each in="${genericPlaceInstanceList}" status="i" var="genericPlaceInstance">
        <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

            <td><g:link action="show" id="${genericPlaceInstance.id}">${fieldValue(bean: genericPlaceInstance, field: "code")}</g:link></td>
            <td>${fieldValue(bean: genericPlaceInstance, field: "comment")}</td>

        </tr>
    </g:each>
    </tbody>
</table>