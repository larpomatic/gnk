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
                <button data-toggle="confirmation-popout" data-placement="left" class="btn btn-danger" title="Supprimer le rôle?"
                        data-url="<g:createLink controller="Role" action="Delete" id="${role.id}"/>" data-object="role" data-id="${role.id}">
                    <i class="icon-remove pull-right"></i>
                </button>
            </li>
        </g:each>
    </ul>

    <div class="tab-content">
        <div class="tab-pane active" id="newRole">
            <form name="newRoleForm" data-url="<g:createLink controller="Role" action="Save"/>">
                <input type="hidden" name="plotId" id="plotId" value="${plotInstance?.id}"/>
                <g:hiddenField name="roleDescription" class="descriptionContent" value=""/>
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
                    <div class="span3">
                        <a href="#roleTagsModal" class="btn" data-toggle="modal">
                            <g:message code="redactintrigue.role.chooseTags" default="Choose tags"/>
                        </a>
                    </div>
                    <div class="span1">
                        <label for="roleType">
                            <g:message code="redactintrigue.role.roleType" default="Type"/>
                        </label>
                    </div>
                    <div class="span5">
                        <g:select name="roleType" id="roleType" from="${['Personnage Joueur', 'Personnage Non Joueur (En jeu)', 'Personnage Non Joueur (Hors jeu)', 'Tout Personnage Joueur', 'Personnage Joueur Générique']}"
                                  keys="${['PJ', 'PNJ', 'PHJ', 'TPJ', 'PJG']}" required=""/>
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
                <div class="fullScreenEditable">
                    <g:render template="dropdownButtons" />

                    <!-- Editor -->
                    <div id="roleRichTextEditor" contenteditable="true" class="text-left richTextEditor" onblur="saveCarretPos($(this).attr('id'))">
                    </div>
                </div>
                %{--<g:textArea name="roleDescription" id="roleDescription" value="" rows="5" cols="100"/>--}%

                <div id="roleEventsModal" class="modal hide fade largeModal" tabindex="-1">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">×</button>
                        <h3>
                            <g:message code="redactintrigue.role.roleEvent" default="Events"/>
                        </h3>
                    </div>

                    <div class="modal-body">
                        <div class="accordion" id="accordionEvent">
                            <g:each in="${plotInstance.events}" var="event">
                                <div class="accordion-group">
                                    <div class="accordion-heading">
                                        <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordionEvent"
                                           href="#collapseEvent-${event.id}" data-eventId="${event.id}">
                                            ${event.name}
                                        </a>
                                    </div>
                                    <div id="collapseEvent-${event.id}" class="accordion-body collapse">
                                        <div class="accordion-inner">
                                            <div class="formRow">
                                                <div class="span1">
                                                    <g:message code="redactintrigue.role.roleTitle" default="Title"/>
                                                </div>
                                                <div class="span4">
                                                    <g:textField name="roleHasEventTitle${event.id}" value=""/>
                                                </div>
                                                <div class="span1">
                                                    <g:message code="redactintrigue.role.roleAnnonced" default="Is Annonced"/>
                                                </div>
                                                <div class="span4">
                                                    <g:checkBox name="roleHasEventannounced${event.id}" checked=""/>
                                                </div>
                                            </div>
                                            <div class="row formRow text-center">
                                                <label for="roleDescription">
                                                    <g:message code="redactintrigue.role.roleDescription" default="Description"/>
                                                </label>
                                            </div>
                                            <g:textArea name="roleHasEventDescription${event.id}" value="" rows="5" cols="100"/>
                                        </div>
                                    </div>
                                </div>
                            </g:each>
                        </div>
                    </div>

                    <div class="modal-footer">
                        <button class="btn" data-dismiss="modal">Ok</button>
                    </div>
                </div>
                <div id="rolePastScenesModal" class="modal hide fade largeModal" tabindex="-1">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">×</button>
                        <h3>
                            <g:message code="redactintrigue.role.rolePastScene" default="Past Scene"/>
                        </h3>
                    </div>

                    <div class="modal-body">
                        <div class="accordion" id="accordionPastScene">
                            <g:each in="${plotInstance.pastescenes}" var="pastscene">
                                <div class="accordion-group">
                                    <div class="accordion-heading">
                                        <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordionPastScene"
                                           href="#collapsePastScene-${pastscene.id}" data-pastsceneId="${pastscene.id}">
                                            ${pastscene.title}
                                        </a>
                                    </div>
                                    <div id="collapsePastScene-${pastscene.id}" class="accordion-body collapse">
                                        <div class="accordion-inner">
                                            <div class="formRow">
                                                <div class="span1">
                                                    <g:message code="redactintrigue.role.roleTitle" default="Title"/>
                                                </div>
                                                <div class="span8">
                                                    <g:textField name="roleHasPastSceneTitle${pastscene.id}" value=""/>
                                                </div>
                                            </div>
                                            <div class="row formRow text-center">
                                                <label for="roleDescription">
                                                    <g:message code="redactintrigue.role.roleDescription" default="Description"/>
                                                </label>
                                            </div>
                                            <g:textArea name="roleHasPastSceneDescription${pastscene.id}" value="" rows="5" cols="100"/>
                                        </div>
                                    </div>
                                </div>
                            </g:each>
                        </div>
                    </div>

                    <div class="modal-footer">
                        <button class="btn" data-dismiss="modal">Ok</button>
                    </div>
                </div>
                <div id="roleTagsModal" class="modal hide fade tags-modal" tabindex="-1">
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
                                <g:render template="roleTagTree" model="[roleTagInstance: roleTagInstance, role: null]"/>
                            </g:each>
                        </ul>
                    </div>

                    <div class="modal-footer">
                        <button class="btn" data-dismiss="modal">Ok</button>
                    </div>
                </div>
                <input type="button" name="Insert" value="Insert" class="btn btn-primary insertRole"/>
            </form>
        </div>

        <g:each in="${plotInstance.roles}" status="i4" var="role">
            <div class="tab-pane" id="role_${role.id}">
                <form name="updateRole_${role.id}" data-url="<g:createLink controller="Role" action="Update" id="${role.id}"/>">
                    <g:hiddenField name="id" value="${role.id}"/>
                    <g:hiddenField name="roleDescription" class="descriptionContent" value=""/>
                    <input type="hidden" name="plotId" id="plotId" value="${plotInstance?.id}"/>
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
                        <div class="span3">
                            <a href="#roleTagsModal_${role.id}" class="btn" data-toggle="modal">
                                <g:message code="redactintrigue.role.chooseTags" default="Choose tags"/>
                            </a>
                        </div>
                        <div class="span1">
                            <label for="roleType">
                                <g:message code="redactintrigue.role.roleType" default="Type"/>
                            </label>
                        </div>
                        <div class="span5">
                            <g:select name="roleType" id="roleType" from="${['Personnage Joueur', 'Personnage Non Joueur (En jeu)', 'Personnage Non Joueur (Hors jeu)', 'Tout Personnage Joueur', 'Personnage Joueur Générique']}"
                                      keys="${['PJ', 'PNJ', 'PHJ', 'TPJ', 'PJG']}" value="${role.type}" required=""/>
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

                    <div class="fullScreenEditable">
                        <g:render template="dropdownButtons" />

                        <!-- Editor -->
                        <div id="roleRichTextEditor${role.id}" contenteditable="true" class="text-left richTextEditor" onblur="saveCarretPos($(this).attr('id'))">
                            ${role.description.encodeAsHTML()}
                        </div>
                    </div>
                    %{--<g:textArea name="roleDescription" id="roleDescription" value="${role.description}" rows="5" cols="100"/>--}%

                    <div id="roleEventsModal_${role.id}" class="modal hide fade largeModal" tabindex="-1">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">×</button>
                            <h3>
                                <g:message code="redactintrigue.role.roleEvent" default="Events"/>
                            </h3>
                        </div>

                        <div class="modal-body">
                            <div class="accordion" id="accordionEvent${role.id}">
                                <g:each in="${plotInstance.events}" var="event">
                                    <div class="accordion-group">
                                        <div class="accordion-heading">
                                            <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordionEvent${role.id}"
                                               href="#collapseEvent${role.id}-${event.id}" data-eventId="${event.id}">
                                                ${event.name}
                                            </a>
                                        </div>
                                        <div id="collapseEvent${role.id}-${event.id}" class="accordion-body collapse">
                                            <div class="accordion-inner">
                                                <div class="formRow">
                                                    <div class="span1">
                                                        <label>
                                                            <g:message code="redactintrigue.role.roleTitle" default="Title"/>
                                                        </label>
                                                    </div>
                                                    <div class="span4">
                                                        <g:textField name="roleHasEventTitle${event.id}" value="${role?.getRoleHasEvent(event)?.title}"/>
                                                    </div>
                                                    <div class="span1">
                                                        <label>
                                                            <g:message code="redactintrigue.role.roleAnnonced" default="Is Annonced"/>
                                                        </label>
                                                    </div>
                                                    <div class="span4">
                                                        <g:checkBox name="roleHasEventannounced${event.id}" checked="${role?.getRoleHasEvent(event)?.isAnnounced}"/>
                                                    </div>
                                                </div>
                                                <div class="row formRow text-center">
                                                    <label>
                                                        <g:message code="redactintrigue.role.roleDescription" default="Description"/>
                                                    </label>
                                                </div>
                                                <g:textArea name="roleHasEventDescription${event.id}" value="${role.getRoleHasEvent(event)?.description}" rows="5" cols="100"/>
                                            </div>
                                        </div>
                                    </div>
                                </g:each>
                            </div>
                        </div>

                        <div class="modal-footer">
                            <button class="btn" data-dismiss="modal">Ok</button>
                        </div>
                    </div>
                    <div id="rolePastScenesModal_${role.id}" class="modal hide fade largeModal" tabindex="-1">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">×</button>
                            <h3>
                                <g:message code="redactintrigue.role.rolePastScene" default="Past Scenes"/>
                            </h3>
                        </div>

                        <div class="modal-body">
                            <div class="accordion" id="accordionPastScene${role.id}">
                                <g:each in="${plotInstance.pastescenes}" var="pastscene">
                                    <div class="accordion-group">
                                        <div class="accordion-heading">
                                            <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordionPastScene${role.id}"
                                               href="#collapsePastScene${role.id}-${pastscene.id}" data-pastsceneId="${pastscene.id}">
                                                ${pastscene.title}
                                            </a>
                                        </div>
                                        <div id="collapsePastScene${role.id}-${pastscene.id}" class="accordion-body collapse">
                                            <div class="accordion-inner">
                                                <div class="formRow">
                                                    <div class="span1">
                                                        <g:message code="redactintrigue.role.roleTitle" default="Title"/>
                                                    </div>
                                                    <div class="span8">
                                                        <g:textField name="roleHasPastSceneTitle${pastscene.id}" value="${role?.getRoleHasPastScene(pastscene)?.title}"/>
                                                    </div>
                                                </div>
                                                <div class="row formRow text-center">
                                                    <label for="roleDescription">
                                                        <g:message code="redactintrigue.role.roleDescription" default="Description"/>
                                                    </label>
                                                </div>
                                                <g:textArea name="roleHasPastSceneDescription${pastscene.id}" value="${role?.getRoleHasPastScene(pastscene)?.description}" rows="5" cols="100"/>
                                            </div>
                                        </div>
                                    </div>
                                </g:each>
                            </div>
                        </div>

                        <div class="modal-footer">
                            <button class="btn" data-dismiss="modal">Ok</button>
                        </div>
                    </div>

                    <div id="roleTagsModal_${role.id}" class="modal hide fade tags-modal" tabindex="-1">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">×</button>
                            <h3 id="myModalLabel${role.id}">
                                <g:message code="redactintrigue.role.tags" default="Tags"/>
                            </h3>
                            <input class="input-medium search-query" data-content="roleTags${role.id}"
                                   placeholder="<g:message code="redactintrigue.generalDescription.search" default="Search..."/>"/>
                            <button type="button" class="btn btn-primary modifyTag push">
                                <g:message code="redactintrigue.generalDescription.validatedTags" default="Validated tags"/>
                            </button>
                        </div>

                        <div class="modal-body">
                            <ul class="roleTags${role.id}">
                                <g:each in="${roleTagList}" status="i3" var="roleTagInstance">
                                    <g:render template="roleTagTree" model="[roleTagInstance: roleTagInstance, role: role]"/>
                                </g:each>
                            </ul>
                        </div>

                        <div class="modal-footer">
                            <button class="btn" data-dismiss="modal">Ok</button>
                        </div>
                    </div>
                    <input type="button" name="Update" data-id="${role.id}" value="Update" class="btn btn-primary updateRole"/>
                </form>
            </div>
        </g:each>
    </div>
</div>