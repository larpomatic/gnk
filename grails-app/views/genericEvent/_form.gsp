<%@ page import="org.gnk.genericevent.GenericEvent" %>
<%@ page import="org.gnk.tag.Tag" %>
<%@ page import="org.gnk.admin.right" %>


<div class="fieldcontain ${hasErrors(bean: genericEventInstance, field: 'title', 'error')} required">
    <label for="title">
        <g:message code="genericEvent.title.label" default="Title" />
        <span class="required-indicator">*</span>
    </label>
    <g:textField name="title" value="${genericEventInstance?.title}" required=""/>
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

%{--Generic event has tag--}%
<div class="panel panel-default">
    <div class="panel-heading">
        <h3 class="panel-title">
            Generic event has tag
            <a href="#tagsModalHasTag" class="btn" data-toggle="modal">
                Chooses tags
            </a>
        </h3>

    </div>
    <div class="panel-body">
        <table id="listTableGenericEventHasTag" class="table table-bordered">
            <thead>
            <tr>
                <th>Tag</th>
            </tr>

            </thead>
            <tbody>
            <tr>
                <td>
                    <g:each in="${genericEventInstance.genericEventHasTag}" status="i" var="genericEventHasTag">
                        <ul class="inline">
                            <g:if test="${!(Tag.findByName("Tag Univers").id == genericEventHasTag.tag.parent.id)}">
                                <li class="badge badge-info">
                                    <g:form class="form-small">
                                        <g:hiddenField name="id" value="${genericEventHasTag.id}" />
                                        <span style="color:black">${genericEventHasTag.tag.name} ${genericEventHasTag.value}%</span>
                                    %{--<g:hasRights lvlright="${right.REFDELETE.value()}">--}%
                                        <g:actionSubmit class="icon-remove remove-action" controller="genericEvent" action="deleteGenericEventHasTag" value=" " onclick="return confirm('${message(code: 'adminRef.place.deleteTag')}');" />
                                    %{--</g:hasRights>--}%
                                    </g:form>
                                </li>
                            </g:if>
                        </ul>
                    </g:each>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>

%{--Generic event imply tag--}%
<div class="panel panel-default">
    <div class="panel-heading">
        <h3 class="panel-title">
            Generic event has tag
            <a href="#tagsModalImplyTag" class="btn" data-toggle="modal">
                Chooses tags
            </a>
        </h3>

    </div>
    <div class="panel-body">
        <table id="listTableGenericEventImplyTag" class="table table-bordered">
            <thead>
            <tr>
                <th>Tag</th>
            </tr>

            </thead>
            <tbody>
            <tr>
                <td>
                    <ul class="inline">
                        <g:each in="${genericEventInstance.genericEventCanImplyTag}" status="i" var="genericEventImplyTag">
                            <g:if test="${!(Tag.findByName("Tag Univers").id == genericEventImplyTag.tag.parent.id)}">
                                <li class="badge badge-info">
                                    <g:form class="form-small">
                                        <g:hiddenField name="id" value="${genericEventImplyTag.id}" />
                                        <span style="color:black">${genericEventImplyTag.tag.name} ${genericEventImplyTag.value}%</span>
                                    %{--<g:hasRights lvlright="${right.REFDELETE.value()}">--}%
                                        <g:actionSubmit class="icon-remove remove-action" controller="genericEvent" action="deleteGenericEventImplyTag" value=" " onclick="return confirm('${message(code: 'adminRef.place.deleteTag')}');" />
                                    %{--</g:hasRights>--}%
                                    </g:form>
                                </li>
                            </g:if>
                        </g:each>
                    </ul>

                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>

%{--Generic event imply tag--}%
<div class="panel panel-default">
    <div class="panel-heading">
        <h3 class="panel-title">
            Generic event has tag
            <a href="#tagsModalImplyGenericEvent" class="btn" data-toggle="modal">
                Chooses tags
            </a>
        </h3>

    </div>
    <div class="panel-body">
        <table id="listTableGenericEventImplyGenericEvent" class="table table-bordered">
            <thead>
            <tr>
                <th>Tag</th>
            </tr>

            </thead>
            <tbody>
            <tr>
                <td>
                    <ul class="inline">
                        <g:each in="${genericEventInstance.genericEventCanImplyGenericEvent}" status="i" var="genericEventCanImplyGenericEvent">
                            <li class="badge badge-info">
                                <g:form class="form-small">
                                    <g:hiddenField name="id" value="${genericEventCanImplyGenericEvent.id}" />
                                    <span style="color:black">${genericEventCanImplyGenericEvent.genericEvent.title} ${genericEventCanImplyGenericEvent.value}%</span>
                                %{--<g:hasRights lvlright="${right.REFDELETE.value()}">--}%
                                    <g:actionSubmit class="icon-remove remove-action" controller="genericEvent"
                                                    action="deleteGenericEventImplyGenericEvent" value=" "
                                                    onclick="return confirm('${message(code: 'adminRef.place.deleteTag')}');" />
                                %{--</g:hasRights>--}%
                                </g:form>
                            </li>
                        </g:each>
                    </ul>

                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>

%{--Modal--}%
<div id="tagsModalHasTag" class="modal hide fade tags-modal" tabindex="-1">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">×</button>
        <h3>Tags</h3>
        <input class="input-medium search-query" data-content="plotTags"
               placeholder="<g:message code="redactintrigue.generalDescription.search" default="Search..."/>"/>
        <button type="button" class="btn btn-primary modifyTag push">
            <g:message code="redactintrigue.generalDescription.validatedTags" default="Validated tags"/>
        </button>
    </div>

    <div class="modal-body">
        <ul class="plotTags">
            <g:each in="${TagInstanceList}" status="i" var="tagInstance">
                <g:render template="genericEventTagTree" model="[tagInstance: tagInstance, genericEventInstance: genericEventInstance,
                                                                 referenceTag : genericEventInstance.genericEventHasTag]"/>
            </g:each>
        </ul>
    </div>

    <div class="modal-footer">
        <button class="btn" data-dismiss="modal">Ok</button>
    </div>
</div>

<div id="tagsModalImplyTag" class="modal hide fade tags-modal" tabindex="-1">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">×</button>
        <h3>Tags</h3>
        <input class="input-medium search-query" data-content="plotTags"
               placeholder="<g:message code="redactintrigue.generalDescription.search" default="Search..."/>"/>
        <button type="button" class="btn btn-primary modifyTag push">
            <g:message code="redactintrigue.generalDescription.validatedTags" default="Validated tags"/>
        </button>
    </div>

    <div class="modal-body">
        <ul class="plotTags">
            <g:each in="${TagInstanceList}" status="i" var="tagInstance">
                <g:render template="genericEventImplyTagTree" model="[tagInstance: tagInstance, genericEventInstance: genericEventInstance,
                                                                 referenceTag : genericEventInstance.genericEventCanImplyTag]"/>
            </g:each>
        </ul>
    </div>

    <div class="modal-footer">
        <button class="btn" data-dismiss="modal">Ok</button>
    </div>
</div>
