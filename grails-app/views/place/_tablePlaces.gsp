<%@ page import="org.gnk.admin.right; org.gnk.tag.Tag; org.gnk.resplacetime.Place" %>
<table id="listTable" class="table table-bordered">
    <thead>
    <tr>

        <g:sortableColumn property="name" title="${message(code: 'place.name.label', default: 'Name')}" />
        <td>Tags</td>
        <td>Tags Univers</td>

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
                        <g:if test="${!(Tag.findByName("Tag Univers").id == placeHasTags.tag.parent.id)}">
                            <li class="badge badge-info">
                            <g:form class="form-small">
                                <g:hiddenField name="id" value="${placeHasTags.id}" />
                                <span style="color:black">${placeHasTags.tag.name} ${placeHasTags.weight}%</span>
                                <g:hasRights lvlright="${right.REFDELETE.value()}">
                                    <g:actionSubmit class="icon-remove remove-action" controller="place" action="deleteTag" value=" " onclick="return confirm('${message(code: 'adminRef.place.deleteTag')}');" />
                                </g:hasRights>
                            </g:form>
                        </li>
                        </g:if>
                    </g:each>
                </ul>
            </td>

            <td>
                <ul class="inline">
                    <g:each in="${placeInstance.extTags.toArray()}" status="j" var="placeHasTags">
                        <g:if test="${Tag.findByName("Tag Univers").id == placeHasTags.tag.parent.id}">
                            <li class="badge badge-info">
                            <g:form class="form-small">
                                <g:hiddenField name="id" value="${placeHasTags.id}" />
                                    <span style="color:#ffffff"> ${placeHasTags.tag.name} ${placeHasTags.weight}%</span>
                                <g:hasRights lvlright="${right.REFDELETE.value()}">
                                    <g:actionSubmit class="icon-remove remove-action" controller="place" action="deleteTag" value=" " onclick="return confirm('${message(code: 'adminRef.place.deleteTag')}');" />
                                </g:hasRights>
                            </g:form>
                        </li>
                        </g:if>
                    </g:each>
                </ul>
            </td>

            <td>${fieldValue(bean: placeInstance, field: "gender")}</td>

            <td>${fieldValue(bean: placeInstance, field: "description")}</td>


        </tr>
    </g:each>
    </tbody>
</table>