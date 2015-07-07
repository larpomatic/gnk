<%@ page import="org.gnk.genericevent.GenericEvent" %>
<%@ page import="org.gnk.tag.Tag" %>


<div class="fieldcontain ${hasErrors(bean: genericEventInstance, field: 'title', 'error')} ">
    <label for="title">
        <g:message code="genericEvent.title.label" default="Title" />

    </label>
    <g:textField name="title" value="${genericEventInstance?.title}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: genericEventInstance, field: 'description', 'error')} ">
	<label for="description">
		<g:message code="genericEvent.description.label" default="Description" />
		
	</label>
	<g:textField name="description" value="${genericEventInstance?.description}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: genericEventInstance, field: 'ageMin', 'error')} required">
    <label for="ageMin">
        <g:message code="genericEvent.ageMin.label" default="Age Min" />
        <span class="required-indicator">*</span>
    </label>
    <g:field name="ageMin" type="number" value="${genericEventInstance.ageMin}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: genericEventInstance, field: 'ageMax', 'error')} required">
	<label for="ageMax">
		<g:message code="genericEvent.ageMax.label" default="Age Max" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="ageMax" type="number" value="${genericEventInstance.ageMax}" required=""/>
</div>


<table id="listTableTag" class="table table-bordered">
    <thead>
    <tr>
        <th>Tags</th>
    </tr>
    </thead>
    <tbody>
        <tr>
            <td>
            <ul class="inline">
                <g:each in="${genericEventInstance?.genericEventHasTag}" status="j" var="genericEventHasTag">
                    <li class="badge badge-info">
                        <g:form class="form-small">
                            <g:hiddenField name="id" value="${genericEventHasTag.tag.id}"/>
                            <g:if test="${Tag.findByName("Tag Univers").id == genericEventHasTag.tag.parent.id}">
                                <span style="color:#ffffff">${genericEventHasTag.tag.name} ${genericEventHasTag.tag.weight}%</span>
                            </g:if>
                            <g:else>
                                <span style="color:black">${genericEventHasTag.tag.name} ${genericEventHasTag.tag.weight}%</span>
                            </g:else>

                            <g:hasRights lvlright="${right.REFDELETE.value()}">
                                <g:actionSubmit class="icon-remove remove-action" controller="ressource"
                                                action="deleteTag" value=" "
                                                onclick="return confirm('${message(code: 'adminRef.resource.deleteTag')}');"/>
                            </g:hasRights>
                        </g:form>
                    </li>
                </g:each>
            </ul>
            </td>
        </tr>
    </tbody>
</table>

<table id="listTableGenericEvent" class="table table-bordered">
    <thead>
    <tr>
        <th>#</th>
    </tr>
    </thead>
    <tbody>
    <g:each in="${genericEventInstanceList}" status="i" var="genericEventInstance">
        <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
            <td>${i + 1}</td>
        </tr>
    </g:each>
    </tbody>
</table>



