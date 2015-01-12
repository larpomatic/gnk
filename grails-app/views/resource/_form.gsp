<%@ page import="org.gnk.resplacetime.Resource" %>



<div class="fieldcontain ${hasErrors(bean: resourceInstance, field: 'name', 'error')} ">
    <label for="name">
        <g:message code="resource.name.label" default="Name"/>

    </label>
    <g:textField name="name" maxlength="45" value="${resourceInstance?.name}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: resourceInstance, field: 'description', 'error')} ">
    <label for="description">
        <g:message code="resource.description.label" default="Description"/>

    </label>
    <g:textField name="description" value="${resourceInstance?.description}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: resourceInstance, field: 'gender', 'error')} ">
    <label for="gender">
        <g:message code="resource.gender.label" default="Gender"/>

    </label>
    <g:textField name="gender" maxlength="2" value="${resourceInstance?.gender}"/>
</div>

</ul>

</div>

<div class="fieldcontain ${hasErrors(bean: resourceInstance, field: 'extTags', 'error')} ">
    <label for="extTags">
        <g:message code="resource.extTags.label" default="extTags"/>

    </label>

    <ul class="one-to-many">
        <g:each in="${resourceInstance?.extTags ?}" var="r">
            <li><g:link controller="resourceHasResourceTag" action="show"
                        id="${r.id}">${r?.encodeAsHTML()}</g:link></li>
        </g:each>
        <li class="add">
            <g:link controller="resourceHasResourceTag" action="create"
                    params="['resource.id': resourceInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'resourceHasResourceTag.label', default: 'ResourceHasResourceTag')])}</g:link>
        </li>
    </ul>

</div>

