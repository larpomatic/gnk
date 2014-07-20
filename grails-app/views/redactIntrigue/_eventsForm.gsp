<%@ page import="org.gnk.selectintrigue.Plot; org.gnk.resplacetime.Event" %>
<div class="tabbable tabs-left eventScreen">
    <ul class="nav nav-tabs leftUl">
        <li class="active leftMenuList">
            <a href="#newEvent" data-toggle="tab" class="addEvent">
                <g:message code="redactintrigue.event.addEvent" default="New event"/>
            </a>
        </li>
        <g:each in="${plotInstance.events.sort{it.timing}}" status="i5" var="event">
            <li class="leftMenuList">
                <a href="#event_${event.id}" data-toggle="tab">
                    ${event.timing}% - ${event.name.encodeAsHTML()}
                </a>
                <button data-toggle="confirmation-popout" data-placement="left" class="btn btn-danger" title="Supprimer l'évènement?"
                        data-url="<g:createLink controller="Event" action="Delete" id="${event.id}"/>" data-object="event" data-id="${event.id}">
                    <i class="icon-remove pull-right"></i>
                </button>
            </li>
        </g:each>
    </ul>
    <div class="tab-content">
        <div class="tab-pane active" id="newEvent">
            <form name="newEventForm" data-url="<g:createLink controller="Event" action="Save"/>">
                <g:hiddenField name="eventDescription" class="descriptionContent" value=""/>
                <g:hiddenField name="eventName" class="titleContent" value=""/>
                <input type="hidden" name="plotId" id="plotId" value="${plotInstance?.id}"/>
                <div class="row formRow text-center">
                    <label for="eventName">
                        <g:message code="redactintrigue.event.eventName" default="Name"/>
                    </label>
                </div>

                <div class="fullScreenEditable">
                    <g:render template="dropdownButtons" />

                    <!-- Editor -->
                    <div id="eventTitleRichTextEditor" contenteditable="true" class="text-left richTextEditor textTitle" onblur="saveCarretPos($(this).attr('id'))">
                    </div>
                </div>
                <div class="row formRow">
                    <div class="span2">
                        <label for="eventPublic">
                            <g:message code="redactintrigue.event.eventPublic" default="Public"/>
                        </label>
                    </div>

                    <div class="span2">
                        <g:checkBox name="eventPublic" id="eventPublic"/>
                    </div>

                    <div class="span2">
                        <label for="eventPlanned">
                            <g:message code="redactintrigue.event.eventPlanned" default="Planned"/>
                        </label>
                    </div>

                    <div class="span2">
                        <g:checkBox name="eventPlanned" id="eventPlanned"/>
                    </div>
                    <div class="span2">
                        <a href="#eventRolesModal" class="btn" data-toggle="modal">
                            <g:message code="redactintrigue.pastScene.chooseRoles" default="Choose roles"/>
                        </a>
                    </div>
                </div>
                <div class="row formRow">
                    <div class="span1">
                        <label for="eventDuration">
                            <g:message code="redactintrigue.event.eventDuration" default="Duration (min)"/>
                        </label>
                    </div>

                    <div class="span4">
                        <g:field type="number" name="eventDuration" id="eventDuration" value="" required=""/>
                    </div>

                    <div class="span1">
                        <label for="eventTiming">
                            <g:message code="redactintrigue.event.eventTiming" default="Timing (%)"/>
                        </label>
                    </div>

                    <div class="span4">
                        <g:field type="number" name="eventTiming" id="eventTiming" value="" required=""/>
                    </div>
                </div>
                <div class="row formRow">
                    <div class="span1">
                        <label for="eventPlace">
                            <g:message code="redactintrigue.event.eventPlace" default="Place"/>
                        </label>
                    </div>

                    <div class="span4">
                        <g:select name="eventPlace" id="eventPlace" from="${plotInstance.genericPlaces}"
                                  optionKey="id" required="" optionValue="code" noSelection="${['null':'']}"/>
                    </div>

                    <div class="span1">
                        <label for="eventPredecessor">
                            <g:message code="redactintrigue.event.eventPredecessor" default="Predecessor"/>
                        </label>
                    </div>

                    <div class="span4">
                        <g:select name="eventPredecessor" id="eventPredecessor" from="${plotInstance.events}"
                                  optionKey="id"  required="" optionValue="name" noSelection="${['null':'']}"/>
                    </div>
                </div>

                <div class="row formRow text-center">
                    <label for="eventDescription">
                        <g:message code="redactintrigue.event.eventDescription" default="Description"/>
                    </label>
                </div>

                <div class="fullScreenEditable">
                    <g:render template="dropdownButtons" />

                    <!-- Editor -->
                    <div id="eventRichTextEditor" contenteditable="true" class="text-left richTextEditor" onblur="saveCarretPos($(this).attr('id'))">
                    </div>
                </div>
                <div id="eventRolesModal" class="modal hide fade largeModal" tabindex="-1">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">×</button>
                        <h3>
                            <g:message code="redactintrigue.pastScene.pastsceneRoles" default="Roles"/>
                        </h3>
                    </div>

                    <div class="modal-body">
                        <div class="tabbable tabs-left">
                            <ul class="nav nav-tabs leftUl">
                                <g:each in="${plotInstance.roles}" var="role">
                                    <li class="">
                                        <a href="#eventRole${role.id}_" data-toggle="tab">
                                            ${role.code}
                                        </a>
                                    </li>
                                </g:each>
                            </ul>
                            <div class="tab-content">
                                <g:each in="${plotInstance.roles}" var="role">
                                    <div class="tab-pane" id="eventRole${role.id}_">
                                        <g:hiddenField name="roleHasEventTitle${role.id}" class="titleContent" value=""/>
                                        <div class="row formRow text-center">
                                            <label>
                                                <g:message code="redactintrigue.role.roleTitle" default="Title"/>
                                            </label>
                                        </div>
                                        <div class="fullScreenEditable">
                                            <g:render template="dropdownButtons" />

                                            <!-- Editor -->
                                            <div id="roleHasEventTitleRichTextEditor${role.id}" contenteditable="true" class="text-left richTextEditor textTitle" onblur="saveCarretPos($(this).attr('id'))">
                                            </div>
                                        </div>
                                        <div class="formRow">
                                            <div class="span1">
                                                <g:message code="redactintrigue.role.roleAnnonced" default="Is Annonced"/>
                                            </div>
                                            <div class="span4">
                                                <g:checkBox name="roleHasEventannounced${role.id}" checked=""/>
                                            </div>
                                        </div>

                                        <div class="text-center roleHasEventTabs">
                                            <div class="span1"></div>
                                            <ul class="nav nav-tabs">
                                                <li class="active">
                                                    <a href="#roleHasEventDescriptionTab${role.id}_" data-toggle="tab">
                                                        <g:message code="redactintrigue.role.roleDescription" default="Description"/>
                                                    </a>
                                                </li>
                                                <li>
                                                    <a href="#roleHasEventCommentTab${role.id}_" data-toggle="tab">
                                                        <g:message code="redactintrigue.event.comment" default="Comment"/>
                                                    </a>
                                                </li>
                                                <li>
                                                    <a href="#roleHasEventEvenementialTab${role.id}_" data-toggle="tab">
                                                        <g:message code="redactintrigue.event.eventEvenementialDescription" default="Evenemential description"/>
                                                    </a>
                                                </li>
                                            </ul>
                                            <div class="tab-content">
                                                <div class="tab-pane active" id="roleHasEventDescriptionTab${role.id}_">
                                                    <g:hiddenField name="roleHasEventDescription${role.id}" class="descriptionContent" value=""/>
                                                    <div class="fullScreenEditable">
                                                        <g:render template="dropdownButtons" />
                                                        <div id="roleHasEventRichTextEditor${role.id}_" contenteditable="true" class="text-left richTextEditor" onblur="saveCarretPos($(this).attr('id'))">
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="tab-pane" id="roleHasEventCommentTab${role.id}_">
                                                    <g:hiddenField name="roleHasEventComment${role.id}" class="commentContent" value=""/>
                                                    <div class="fullScreenEditable">
                                                        <g:render template="dropdownButtons" />
                                                        <div id="roleHasEventCommentRichTextEditor${role.id}_" contenteditable="true" class="text-left richTextEditor" onblur="saveCarretPos($(this).attr('id'))">
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="tab-pane" id="roleHasEventEvenementialTab${role.id}_">
                                                    <g:hiddenField name="roleHasEventEvenemential${role.id}" class="evenementialContent" value=""/>
                                                    <div class="fullScreenEditable">
                                                        <g:render template="dropdownButtons" />
                                                        <div id="roleHasEventEvenementialRichTextEditor${role.id}_" contenteditable="true" class="text-left richTextEditor" onblur="saveCarretPos($(this).attr('id'))">
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <table class="table table-hover">
                                            <thead>
                                            <tr>
                                                <th><g:message code="redactintrigue.tabs.objects" default="Resources"/></th>
                                                <th><g:message code="redactintrigue.resource.quantity" default="Quantity"/></th>
                                            </tr>
                                            </thead>
                                            <tbody data-role="${role.id}">
                                            <g:each in="${plotInstance.genericResources}" var="resource">
                                            <tr>
                                                <td data-id="${resource.id}">${resource.code}</td>
                                                <td><g:field type="number" name="quantity${role.id}_${resource.id}" value=""/></td>
                                            </tr>
                                            </g:each>
                                            </tbody>
                                        </table>
                                    </div>
                                </g:each>
                            </div>
                        </div>
                    </div>

                    <div class="modal-footer">
                        <button class="btn" data-dismiss="modal">Ok</button>
                    </div>
                </div>

                <input type="button" name="Insert" value="Insert" class="btn btn-primary insertEvent"/>
            </form>
        </div>

    <g:each in="${plotInstance.events}" var="event">
        <div class="tab-pane" id="event_${event.id}">
            <form name="updateEvent_${event.id}" data-url="<g:createLink controller="Event" action="Update" id="${event.id}"/>">
                <g:hiddenField name="id" value="${event.id}"/>
                <g:hiddenField name="eventDescription" class="descriptionContent" value=""/>
                <g:hiddenField name="eventName" class="titleContent" value=""/>
                <input type="hidden" name="plotId" id="plotId" value="${plotInstance?.id}"/>
                <div class="row formRow text-center">
                    <label for="eventName">
                        <g:message code="redactintrigue.event.eventName" default="Name"/>
                    </label>
                </div>

                <div class="fullScreenEditable">
                    <g:render template="dropdownButtons" />

                    <!-- Editor -->
                    <div id="eventTitleRichTextEditor${event.id}" contenteditable="true" class="text-left richTextEditor textTitle" onblur="saveCarretPos($(this).attr('id'))">
                        ${event.name.encodeAsHTML()}
                    </div>
                </div>
                <div class="row formRow">
                    <div class="span2">
                        <label for="eventPublic">
                            <g:message code="redactintrigue.event.eventPublic" default="Public"/>
                        </label>
                    </div>

                    <div class="span2">
                        <g:checkBox name="eventPublic" id="eventPublic" value="${event.isPublic}"/>
                    </div>

                    <div class="span2">
                        <label for="eventPlanned">
                            <g:message code="redactintrigue.event.eventPlanned" default="Planned"/>
                        </label>
                    </div>

                    <div class="span2">
                        <g:checkBox name="eventPlanned" id="eventPlanned" value="${event.isPlanned}"/>
                    </div>
                    <div class="span2">
                        <a href="#eventRolesModal${event.id}" class="btn" data-toggle="modal">
                            <g:message code="redactintrigue.pastScene.chooseRoles" default="Choose roles"/>
                        </a>
                    </div>
                </div>
                <div class="row formRow">
                    <div class="span1">
                        <label for="EventDuration">
                            <g:message code="redactintrigue.event.eventDuration" default="Duration (min)"/>
                        </label>
                    </div>

                    <div class="span4">
                        <g:field type="number" name="eventDuration" id="EventDuration" value="${event.duration}" required=""/>
                    </div>

                    <div class="span1">
                        <label for="EventTiming">
                            <g:message code="redactintrigue.event.eventTiming" default="Timing (%)"/>
                        </label>
                    </div>

                    <div class="span4">
                        <g:field type="number" name="eventTiming" id="EventTiming" value="${event.timing}" required=""/>
                    </div>
                </div>
                <div class="row formRow">
                    <div class="span1">
                        <label for="eventPlace">
                            <g:message code="redactintrigue.event.eventPlace" default="Place"/>
                        </label>
                    </div>

                    <div class="span4">
                        <g:select name="eventPlace" id="eventPlace" from="${plotInstance.genericPlaces}" value="${event.genericPlace?.id}"
                                  optionKey="id" required="" optionValue="code" noSelection="${['null':'']}"/>
                    </div>

                    <div class="span1">
                        <label for="eventPredecessor">
                            <g:message code="redactintrigue.event.eventPredecessor" default="Predecessor"/>
                        </label>
                    </div>

                    <div class="span4">
                        <g:select name="eventPredecessor" id="eventPredecessor" from="${plotInstance.events}" value="${event.eventPredecessor?.id}"
                                  optionKey="id"  required="" optionValue="name" noSelection="${['null':'']}"/>
                    </div>
                </div>

                <div class="row formRow text-center">
                    <label for="eventDescription">
                        <g:message code="redactintrigue.event.eventDescription" default="Description"/>
                    </label>
                </div>

                <div class="fullScreenEditable">
                    <g:render template="dropdownButtons" />

                    <!-- Editor -->
                    <div id="eventRichTextEditor${event.id}" contenteditable="true" class="text-left richTextEditor" onblur="saveCarretPos($(this).attr('id'))">
                        ${event.description.encodeAsHTML()}
                    </div>
                </div>

                <div id="eventRolesModal${event.id}" class="modal hide fade largeModal" tabindex="-1">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">×</button>
                        <h3>
                            <g:message code="redactintrigue.pastScene.pastsceneRoles" default="Roles"/>
                        </h3>
                    </div>

                    <div class="modal-body">
                        <div class="tabbable tabs-left">
                            <ul class="nav nav-tabs leftUl">
                                <g:each in="${plotInstance.roles}" var="role">
                                    <li class="">
                                        <a href="#eventRole${role.id}_${event.id}" data-toggle="tab">
                                            ${role.code}
                                        </a>
                                    </li>
                                </g:each>
                            </ul>
                            <div class="tab-content">
                                <g:each in="${plotInstance.roles}" var="role">
                                    <div class="tab-pane" id="eventRole${role.id}_${event.id}">
                                        <g:hiddenField name="roleHasEventTitle${role.id}" class="titleContent" value=""/>
                                        <div class="row formRow text-center">
                                            <label>
                                                <g:message code="redactintrigue.role.roleTitle" default="Title"/>
                                            </label>
                                        </div>
                                        <div class="fullScreenEditable">
                                            <g:render template="dropdownButtons" />

                                            <!-- Editor -->
                                            <div id="roleHasEventTitleRichTextEditor${role.id}" contenteditable="true" class="text-left richTextEditor textTitle" onblur="saveCarretPos($(this).attr('id'))">
                                                ${role.getRoleHasEvent(event)?.title?.encodeAsHTML()}
                                            </div>
                                        </div>
                                        <div class="formRow">
                                            <div class="span1">
                                                <g:message code="redactintrigue.role.roleAnnonced" default="Is Annonced"/>
                                            </div>
                                            <div class="span4">
                                                <g:checkBox name="roleHasEventannounced${role.id}" checked="${role?.getRoleHasEvent(event)?.isAnnounced}"/>
                                            </div>
                                        </div>

                                        <div class="text-center roleHasEventTabs">
                                            <div class="span1"></div>
                                            <ul class="nav nav-tabs">
                                                <li class="active">
                                                    <a href="#roleHasEventDescriptionTab${role.id}_${event.id}" data-toggle="tab">
                                                        <g:message code="redactintrigue.role.roleDescription" default="Description"/>
                                                    </a>
                                                </li>
                                                <li>
                                                    <a href="#roleHasEventCommentTab${role.id}_${event.id}" data-toggle="tab">
                                                        <g:message code="redactintrigue.event.comment" default="Comment"/>
                                                    </a>
                                                </li>
                                                <li>
                                                    <a href="#roleHasEventEvenementialTab${role.id}_${event.id}" data-toggle="tab">
                                                        <g:message code="redactintrigue.event.eventEvenementialDescription" default="Evenemential description"/>
                                                    </a>
                                                </li>
                                            </ul>
                                            <div class="tab-content">
                                                <div class="tab-pane active" id="roleHasEventDescriptionTab${role.id}_${event.id}">
                                                    <g:hiddenField name="roleHasEventDescription${role.id}" class="descriptionContent" value=""/>
                                                    <div class="fullScreenEditable">
                                                        <g:render template="dropdownButtons" />
                                                        <div id="roleHasEventRichTextEditor${role.id}_${event.id}" contenteditable="true" class="text-left richTextEditor" onblur="saveCarretPos($(this).attr('id'))">
                                                            ${role.getRoleHasEvent(event)?.description?.encodeAsHTML()}
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="tab-pane" id="roleHasEventCommentTab${role.id}_${event.id}">
                                                    <g:hiddenField name="roleHasEventComment${role.id}" class="commentContent" value=""/>
                                                    <div class="fullScreenEditable">
                                                        <g:render template="dropdownButtons" />
                                                        <div id="roleHasEventCommentRichTextEditor${role.id}_${event.id}" contenteditable="true" class="text-left richTextEditor" onblur="saveCarretPos($(this).attr('id'))">
                                                            ${role.getRoleHasEvent(event)?.comment?.encodeAsHTML()}
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="tab-pane" id="roleHasEventEvenementialTab${role.id}_${event.id}">
                                                    <g:hiddenField name="roleHasEventEvenemential${role.id}" class="evenementialContent" value=""/>
                                                    <div class="fullScreenEditable">
                                                        <g:render template="dropdownButtons" />
                                                        <div id="roleHasEventEvenementialRichTextEditor${role.id}_${event.id}" contenteditable="true" class="text-left richTextEditor" onblur="saveCarretPos($(this).attr('id'))">
                                                            ${role.getRoleHasEvent(event)?.evenementialDescription?.encodeAsHTML()}
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                        <table class="table table-hover">
                                            <thead>
                                            <tr>
                                                <th><g:message code="redactintrigue.tabs.objects" default="Resources"/></th>
                                                <th><g:message code="redactintrigue.resource.quantity" default="Quantity"/></th>
                                            </tr>
                                            </thead>
                                            <tbody data-role="${role.id}">
                                            <g:each in="${plotInstance.genericResources}" var="resource">
                                                <tr>
                                                    <td data-id="${resource.id}">${resource.code}</td>
                                                    <td><g:field type="number" name="quantity${role.id}_${resource.id}" value="${resource.getGenericResourceHasRoleHasEvent(role.getRoleHasEvent(event))?.quantity}"/></td>
                                                </tr>
                                            </g:each>
                                            </tbody>
                                        </table>
                                    </div>
                                </g:each>
                            </div>
                        </div>
                    </div>

                    <div class="modal-footer">
                        <button class="btn" data-dismiss="modal">Ok</button>
                    </div>
                </div>

                <input type="button" name="Update" data-id="${event.id}" value="Update" class="btn btn-primary updateEvent"/>
            </form>
        </div>
    </g:each>

    </div>
</div>