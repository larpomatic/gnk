<%@ page import="org.gnk.resplacetime.Place" %>
<table class="table table-bordered">
    <thead>
    <tr>

        <g:sortableColumn property="name" title="${message(code: 'place.name.label', default: 'Name')}" />
        <td>Tags</td>
        <td>Univers</td>

        <g:sortableColumn property="gender" title="${message(code: 'place.gender.label', default: 'Gender')}" />

        <g:sortableColumn property="description" title="${message(code: 'place.description.label', default: 'Description')}" />


    </tr>
    </thead>
    <tbody>
    <g:each in="${placeInstanceList}" status="i" var="placeInstance">
        <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

            <td><g:link action="show" id="${placeInstance.id}">${fieldValue(bean: placeInstance, field: "name")}</g:link></td>

            <td>
                <ul class="inline">
                    <g:each in="${placeInstance.extTags.toArray()}" status="j" var="placeHasTags">
                        <li class="badge badge-info">
                            <g:form class="form-small">
                                <g:hiddenField name="id" value="${placeHasTags.id}" />
                                ${placeHasTags.tag.name} ${placeHasTags.weight}%
                                <g:actionSubmit class="icon-remove remove-action" controller="place" action="deleteTag" value=" " onclick="return confirm('${message(code: 'adminRef.place.deleteTag')}');" />
                            </g:form>
                        </li>
                    </g:each>
                </ul>
            </td>

            <td>
                <ul class="inline">
                    <g:each in="${placeInstance.placeHasUniverses.toArray()}" status="j" var="placeHasUniverses">
                        <span class="badge badge-info">
                            <li class="label label-info">
                                <g:form class="form-small">
                                    <g:hiddenField name="id" value="${placeHasUniverses.id}" />
                                    ${placeHasUniverses.univers.name} ${placeHasUniverses.weight}%
                                    <g:actionSubmit class="icon-remove remove-action" controller="place" action="deleteUnivers" value=" " onclick="return confirm('${message(code: 'adminRef.place.deleteUnivers')}');" />
                                </g:form>
                            </li>
                        </span>
                    </g:each>
                </ul>
            </td>

            <td>${fieldValue(bean: placeInstance, field: "gender")}</td>

            <td>${fieldValue(bean: placeInstance, field: "description")}</td>


        </tr>
    </g:each>
    </tbody>
</table>