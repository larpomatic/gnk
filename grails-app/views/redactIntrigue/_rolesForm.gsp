<%@ page import="org.gnk.selectintrigue.Plot" %>
<g:hiddenField name="screenStep" value="1"/>

<div class="tabbable tabs-left roleScreen">

    <ul class="nav nav-tabs leftUl">
        <li class="active leftMenuList">
            <a href="#newRole" data-toggle="tab" class="addRole">
                <g:message code="redactintrigue.role.addRole" default="New role"/>
            </a>
        </li>
        <g:each in="${plotInstance.roles}" status="i5" var="role">
            <li class="leftMenuList">
                <a href="#role_${role.id}" data-toggle="tab">
                    ${role.code}
                </a>
                <button data-toggle="confirmation-popout" data-placement="left" class="btn btn-danger" title="Supprimer ce rôle?"
                        data-url="<g:createLink controller="Role" action="Delete" id="${role.id}"/>" data-object="role" data-id="${role.id}">
                    <i class="icon-remove pull-right"></i>
                </button>
            </li>
        </g:each>
    </ul>

    <div class="tab-content">
        <div class="tab-pane active" id="newRole">
            <form name="newRoleForm" data-url="<g:createLink controller="Role" action="Save"/>">
            %{--<g:form name="newRoleForm" url="[controller: 'role', action: 'save']">--}%
                <input type="hidden" name="plotId" id="plotId" value="${plotInstance?.id}"/>
                %{--<div style="margin:auto">--}%
                    <div class="row formRow">
                        <div class="span1">
                            <label for="roleCode">
                                <g:message code="redactintrigue.role.roleCode" default="Name"/>
                            </label>
                        </div>
                        <div class="span8">
                            <g:textField name="roleCode" id="roleCode" value="" required=""/>
                        </div>
                    </div>
                    <div class="row formRow">
                        <div class="span1">
                            <label for="rolePipi">
                                <g:message code="redactintrigue.role.rolePipi" default="PIPI"/>
                            </label>
                        </div>
                        <div class="span4">
                            <g:field type="number" name="rolePipi" id="rolePipi" value="" required=""/>
                        </div>
                        <div class="span1">
                            <label for="rolePipr">
                                <g:message code="redactintrigue.role.rolePipr" default="PIPR"/>
                            </label>
                        </div>
                        <div class="span4">
                            <g:field type="number" name="rolePipr" id="rolePipr" value="" required=""/>
                        </div>
                    </div>
                    <div class="row formRow">
                        <div class="span1">
                            <label>
                                <g:message code="redactintrigue.generalDescription.tags" default="Tags"/>
                            </label>
                        </div>
                        <div class="span4">
                            <a href="#roleTagsModal" class="btn" data-toggle="modal">
                                <g:message code="redactintrigue.role.chooseTags" default="Choose tags"/>
                            </a>
                        </div>
                        <div class="span1">
                            <label for="roleType">
                                <g:message code="redactintrigue.role.roleType" default="Type"/>
                            </label>
                        </div>
                        <div class="span4">
                            <g:select name="roleType" id="roleType" from="${['PJ', 'PNJ', 'PHJ']}"
                                      keys="${['PJ', 'PNJ', 'PHJ']}" required=""/>
                        </div>
                    </div>
                    <div class="row formRow">
                        <div class="span1">
                            <label>
                                <g:message code="redactintrigue.role.rolePastScene" default="Past Scene"/>
                            </label>
                        </div>
                        <div class="span4">
                            <a href="#rolePastScenesModal" class="btn" data-toggle="modal">
                                <g:message code="redactintrigue.role.choosePastScenes" default="Choose past scenes"/>
                            </a>
                        </div>
                        <div class="span1">
                            <label>
                                <g:message code="redactintrigue.role.roleEvent" default="Events"/>
                            </label>
                        </div>
                        <div class="span4">
                            <a href="#roleEventsModal" class="btn" data-toggle="modal">
                                <g:message code="redactintrigue.role.chooseEvents" default="Choose events"/>
                            </a>
                        </div>
                    </div>
                    <div class="row formRow text-center">
                        <label for="roleDescription">
                            <g:message code="redactintrigue.role.roleDescription" default="Description"/>
                        </label>
                    </div>
                    <g:textArea name="roleDescription" id="roleDescription" value="" rows="5" cols="100"/>
                %{--</div>--}%

                <div id="roleEventsModal" class="modal hide fade" tabindex="-1">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">×</button>
                        <h3>
                            <g:message code="redactintrigue.role.roleEvent" default="Events"/>
                        </h3>
                        <input class="input-medium search-query" data-content="roleEvent"
                               placeholder="<g:message code="redactintrigue.generalDescription.search" default="Search..."/>"/>
                    </div>

                    <div class="modal-body">
                        <ul class="roleEvent">
                            <g:each in="${plotInstance.events}" var="event">
                                <g:each in="${event.roleHasEvents}" var="roleHasEvent">
                                    <li class="modalLi" data-name="${roleHasEvent.title.toLowerCase()}">
                                        <label>
                                            <g:checkBox name="roleEvent_${roleHasEvent.id}" id="roleEvent_${roleHasEvent.id}"/>
                                            ${fieldValue(bean: roleHasEvent, field: "title")}
                                        </label>
                                    </li>
                                </g:each>
                            </g:each>
                        </ul>
                    </div>

                    <div class="modal-footer">
                        <button class="btn" data-dismiss="modal">Ok</button>
                    </div>
                </div>
                <div id="rolePastScenesModal" class="modal hide fade" tabindex="-1">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">×</button>
                        <h3>
                            <g:message code="redactintrigue.role.rolePastScene" default="Past Scene"/>
                        </h3>
                        <input class="input-medium search-query" data-content="rolePastScene"
                               placeholder="<g:message code="redactintrigue.generalDescription.search" default="Search..."/>"/>
                    </div>

                    <div class="modal-body">
                        <ul class="rolePastScene">
                            <g:each in="${plotInstance.pastescenes}" var="pastscenes">
                                <g:each in="${pastscenes.roleHasPastscenes}" var="roleHasPastScene">
                                    <li class="modalLi" data-name="${roleHasPastScene.title.toLowerCase()}">
                                        <label>
                                            <g:checkBox name="rolePastScene_${roleHasPastScene.id}" id="rolePastScene_${roleHasPastScene.id}"/>
                                            ${fieldValue(bean: roleHasPastScene, field: "title")}
                                        </label>
                                    </li>
                                </g:each>
                            </g:each>
                        </ul>
                    </div>

                    <div class="modal-footer">
                        <button class="btn" data-dismiss="modal">Ok</button>
                    </div>
                </div>
                <div id="roleTagsModal" class="modal hide fade" tabindex="-1">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">×</button>
                        <h3 id="myModalLabel">
                            <g:message code="redactintrigue.role.tags" default="Tags"/>
                        </h3>
                        <input class="input-medium search-query" data-content="roleTagsCreate"
                               placeholder="<g:message code="redactintrigue.generalDescription.search" default="Search..."/>"/>
                    </div>

                    <div class="modal-body">
                        <ul class="roleTagsCreate">
                            <g:each in="${roleTagList}" status="i2" var="roleTagInstance">
                                <li class="modalLi" data-name="${roleTagInstance.name.toLowerCase()}">
                                    <label for="roleTags_${roleTagInstance.id}">
                                        <g:checkBox name="roleTags_${roleTagInstance.id}" id="roleTags_${roleTagInstance.id}"
                                        checked="false"/>
                                        ${fieldValue(bean: roleTagInstance, field: "name")}
                                    </label>
                                </li>
                            </g:each>
                        </ul>
                    </div>

                    <div class="modal-footer">
                        <button class="btn" data-dismiss="modal">Ok</button>
                    </div>
                </div>
                <input type="button" name="Insert" value="Insert" class="btn btn-primary insertRole"/>
                %{--<g:submitButton name="Insert" value="Insert" class="btn btn-primary"/>--}%
            </form>
        </div>

        <g:each in="${plotInstance.roles}" status="i4" var="role">
            <div class="tab-pane" id="role_${role.id}">
                <form name="updateRole_${role.id}" data-url="<g:createLink controller="Role" action="Update" id="${role.id}"/>">
                %{--<g:form name="updateRole_${role.id}" data-url="<g:createLink controller='Role' action='Update' id='${role.id}' />">--}%
                    <g:hiddenField name="id" value="${role.id}"/>
                    <input type="hidden" name="plotId" id="plotId" value="${plotInstance?.id}"/>

                    %{--<div style="margin:auto">--}%
                        <div class="row formRow">
                            <div class="span1">
                                <label for="roleCode">
                                    <g:message code="redactintrigue.role.roleCode" default="Name"/>
                                </label>
                            </div>
                            <div class="span8">
                                <g:textField name="roleCode" id="roleCode" value="${role.code}" required=""/>
                            </div>
                        </div>
                        <div class="row formRow">
                            <div class="span1">
                                <label for="rolePipi">
                                    <g:message code="redactintrigue.role.rolePipi" default="PIPI"/>
                                </label>
                            </div>
                            <div class="span4">
                                <g:field type="number" name="rolePipi" id="rolePipi" value="${role.pipi}" required=""/>
                            </div>
                            <div class="span1">
                                <label for="rolePipr">
                                    <g:message code="redactintrigue.role.rolePipr" default="PIPR"/>
                                </label>
                            </div>
                            <div class="span4">
                                <g:field type="number" name="rolePipr" id="rolePipr" value="${role.pipr}" required=""/>
                            </div>
                        </div>
                        <div class="row formRow">
                            <div class="span1">
                                <label>
                                    <g:message code="redactintrigue.generalDescription.tags" default="Tags"/>
                                </label>
                            </div>
                            <div class="span4">
                                <a href="#roleTagsModal_${role.id}" class="btn" data-toggle="modal">
                                    <g:message code="redactintrigue.role.chooseTags" default="Choose tags"/>
                                </a>
                            </div>
                            <div class="span1">
                                <label for="roleType">
                                    <g:message code="redactintrigue.role.roleType" default="Type"/>
                                </label>
                            </div>
                            <div class="span4">
                                <g:select name="roleType" id="roleType" from="${['PJ', 'PNJ', 'PHJ']}"
                                          keys="${['PJ', 'PNJ', 'PHJ']}" value="${role.type}" required=""/>
                            </div>
                        </div>
                        <div class="row formRow">
                            <div class="span1">
                                <label>
                                    <g:message code="redactintrigue.role.rolePastScene" default="Past Scene"/>
                                </label>
                            </div>
                            <div class="span4">
                                <a href="#rolePastScenesModal_${role.id}" class="btn" data-toggle="modal">
                                    <g:message code="redactintrigue.role.choosePastScenes" default="Choose past Scene"/>
                                </a>
                            </div>
                            <div class="span1">
                                <label>
                                    <g:message code="redactintrigue.role.roleEvent" default="Events"/>
                                </label>
                            </div>
                            <div class="span4">
                                <a href="#roleEventsModal_${role.id}" class="btn" data-toggle="modal">
                                    <g:message code="redactintrigue.role.chooseEvents" default="Choose events"/>
                                </a>
                            </div>
                        </div>
                        <div class="row formRow text-center">
                            <label for="roleDescription">
                                <g:message code="redactintrigue.role.roleDescription" default="Description"/>
                            </label>
                        </div>
                        <g:textArea name="roleDescription" id="roleDescription" value="${role.description}" rows="5" cols="100"/>

                    <div id="roleEventsModal_${role.id}" class="modal hide fade" tabindex="-1">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">×</button>
                            <h3>
                                <g:message code="redactintrigue.role.roleEvent" default="Events"/>
                            </h3>
                            <input class="input-medium search-query" data-content="roleEvent${role.id}"
                                   placeholder="<g:message code="redactintrigue.generalDescription.search" default="Search..."/>"/>
                        </div>

                        <div class="modal-body">
                            <ul class="roleEvent${role.id}">
                                <g:each in="${plotInstance.events}" var="event">
                                    <g:each in="${event.roleHasEvents}" var="roleHasEvent">
                                        <li class="modalLi" data-name="${roleHasEvent.title.toLowerCase()}">
                                            <label>
                                                <g:checkBox name="roleEvent_${roleHasEvent.id}" id="roleEvent_${roleHasEvent.id}"
                                                            checked="${role.roleHasEvents.contains(roleHasEvent)}"/>
                                                ${fieldValue(bean: roleHasEvent, field: "title")}
                                            </label>
                                        </li>
                                    </g:each>
                                </g:each>
                            </ul>
                        </div>

                        <div class="modal-footer">
                            <button class="btn" data-dismiss="modal">Ok</button>
                        </div>
                    </div>
                    <div id="rolePastScenesModal_${role.id}" class="modal hide fade" tabindex="-1">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">×</button>
                            <h3>
                                <g:message code="redactintrigue.role.rolePastScene" default="Past Scenes"/>
                            </h3>
                            <input class="input-medium search-query" data-content="rolePastScene${role.id}"
                                        placeholder="<g:message code="redactintrigue.generalDescription.search" default="Search..."/>"/>
                        </div>

                        <div class="modal-body">
                            <ul class="rolePastScene${role.id}">
                                <g:each in="${plotInstance.pastescenes}" var="pastscenes">
                                    <g:each in="${pastscenes.roleHasPastscenes}" var="roleHasPastScene">
                                        <li class="modalLi" data-name="${roleHasPastScene.title.toLowerCase()}">
                                            <label>
                                                <g:checkBox name="rolePastScene_${roleHasPastScene.id}" id="rolePastScene_${roleHasPastScene.id}"
                                                            checked="${role.roleHasPastscenes.contains(roleHasPastScene)}"/>
                                                ${fieldValue(bean: roleHasPastScene, field: "title")}
                                            </label>
                                        </li>
                                    </g:each>
                                </g:each>
                            </ul>
                        </div>

                        <div class="modal-footer">
                            <button class="btn" data-dismiss="modal">Ok</button>
                        </div>
                    </div>

                    <div id="roleTagsModal_${role.id}" class="modal hide fade" tabindex="-1">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">×</button>
                            <h3 id="myModalLabel${role.id}">
                                <g:message code="redactintrigue.role.tags" default="Tags"/>
                            </h3>
                            <input class="input-medium search-query" data-content="roleTags${role.id}"
                                   placeholder="<g:message code="redactintrigue.generalDescription.search" default="Search..."/>"/>
                        </div>

                        <div class="modal-body">
                            <ul class="roleTags${role.id}">
                                <g:each in="${roleTagList}" status="i3" var="roleTagInstance">
                                    <li class="modalLi" data-name="${roleTagInstance.name.toLowerCase()}">
                                        <label>
                                            <g:checkBox name="roleTags_${roleTagInstance.id}" id="roleTags_${roleTagInstance.id}"
                                            checked="${role.hasRoleTag(roleTagInstance)}"/> ${fieldValue(bean: roleTagInstance, field: "name")}
                                        </label>
                                    </li>
                                </g:each>
                            </ul>
                        </div>

                        <div class="modal-footer">
                            <button class="btn" data-dismiss="modal">Ok</button>
                        </div>
                    </div>
                    <input type="button" name="Update" data-id="${role.id}" value="Update" class="btn btn-primary updateRole"/>
                    %{--<g:submitButton name="Update" value="Update" class="btn btn-primary updateRole"/>--}%
                    %{--<g:actionSubmit class="delete" controller="role" action="delete"--}%
                                    %{--value="${message(code: 'default.button.delete.label', default: 'Delete')}"--}%
                                    %{--formnovalidate=""--}%
                                    %{--onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/>--}%
                %{--</g:form>--}%
                </form>
            </div>
        </g:each>
    </div>
</div>