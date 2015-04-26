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
                <g:plotOwner idOwner="${plotInstance.user.id}" lvlright="${right.MINTRIGUEMODIFY.value()}" lvlrightAdmin="${right.INTRIGUEMODIFY.value()}">
                    <button data-toggle="confirmation-popout" data-placement="left" class="btn btn-danger" title="Supprimer le rôle?"
                            data-url="<g:createLink controller="Role" action="Delete" id="${role.id}"/>" data-object="role" data-id="${role.id}">
                        <i class="icon-remove pull-right"></i>
                    </button>
                </g:plotOwner>
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
                        <g:select name="roleType" id="roleType" from="${['Personnage Joueur', 'Personnage Non Joueur (En jeu)', 'Personnage Non Joueur (Hors jeu)', 'Tout Personnage Joueur', 'Personnage Joueur Générique', 'Personnage Staff']}"
                                  keys="${['PJ', 'PNJ', 'PHJ', 'TPJ', 'PJG', 'STF']}" required=""/>
                    </div>
                        <div class="span1 pjgp_new" >
                            <label for="rolePJGP">
                                <g:message code="redactintrigue.role.rolePJGP" default="PJG %"/>
                            </label>
                        </div>
                        <div class="span4 pjgp_new">
                            <g:field type="number" name="rolePJGP" id="rolePJGP" value="0" required=""/>
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
                <g:plotOwner idOwner="${plotInstance.user.id}" lvlright="${right.MINTRIGUEMODIFY.value()}" lvlrightAdmin="${right.INTRIGUEMODIFY.value()}">
                    <input type="button" name="Insert" value="Insert" class="btn btn-primary insertRole"/>
                </g:plotOwner>
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
                            <g:select name="roleType" id="roleType" from="${['Personnage Joueur', 'Personnage Non Joueur (En jeu)', 'Personnage Non Joueur (Hors jeu)', 'Tout Personnage Joueur', 'Personnage Joueur Générique', 'Personnage Staff']}"
                                      keys="${['PJ', 'PNJ', 'PHJ', 'TPJ', 'PJG', 'STF']}" value="${role.type}" required="" data-id="${role.id}"/>
                        </div>

                            <div class="span1  " id="pjg">
                                <label for="rolePJGP">
                                    <g:message code="redactintrigue.role.rolePJGP" default="PJG %"/>
                                </label>
                            </div>
                            <div class="span4  val_pjg" id="pjg_per">
                                <g:field type="number" name="rolePJGP" id="rolePJGP" value="${role.pjgp}" required="" data-id="${role.id}"/>
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
                                <g:message code="redactintrigue.role.choosePastScenes" default="See past Scene"/>
                            </a>
                        </div>
                        <div class="span1">
                            <label>
                                <g:message code="redactintrigue.role.roleEvent" default="Events"/>
                            </label>
                        </div>
                        <div class="span4">
                            <a href="#roleEventsModal_${role.id}" class="btn" data-toggle="modal">
                                <g:message code="redactintrigue.role.chooseEvents" default="See events"/>
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
                            ${role.description?.encodeAsHTML()}
                        </div>
                    </div>

                    <div id="roleEventsModal_${role.id}" class="modal hide fade largeModal" tabindex="-1">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">×</button>
                            <h3>
                                <g:message code="redactintrigue.role.roleEvent" default="Events"/>
                            </h3>
                        </div>

                        <div class="modal-body">
                            <div class="accordion" id="accordionEvent${role.id}">
                                <g:each in="${plotInstance.events.sort{it.timing}}" var="event">
                                    <div class="accordion-group">
                                        <g:if test="${role?.getRoleHasEvent(event)?.title}">
                                            <div class="accordion-heading alert-success">
                                        </g:if>
                                        <g:else>
                                            <div class="accordion-heading">
                                        </g:else>
                                            <a class="accordion-toggle spanLabel" data-toggle="collapse" data-parent="#accordionEvent${role.id}"
                                               href="#collapseEvent${role.id}-${event.id}" data-eventId="${event.id}">
                                                ${event.timing}% - ${event.name?.encodeAsHTML()}
                                            </a>
                                        </div>
                                        <div id="collapseEvent${role.id}-${event.id}" class="accordion-body collapse">
                                            <div class="accordion-inner">
                                                <div class="formRow">
                                                    <div class="row formRow text-center">
                                                        <label>
                                                            <g:message code="redactintrigue.role.roleTitle" default="Title"/>
                                                        </label>
                                                    </div>
                                                </div>
                                                <div contenteditable="false" class="text-left richTextEditor textTitle">
                                                    ${role?.getRoleHasEvent(event)?.title?.encodeAsHTML()}
                                                </div>
                                                <div class="formRow text-center">
                                                    <div class="span4"></div>
                                                    <div class="span2">
                                                        <label>
                                                            <g:message code="redactintrigue.role.roleAnnonced" default="Is Annonced"/>
                                                        </label>
                                                    </div>
                                                    <div class="span1">
                                                        <g:checkBox disabled="disabled" name="roleHasEventannounced${event.id}" checked="${role?.getRoleHasEvent(event)?.isAnnounced}"/>
                                                    </div>
                                                </div>
                                                <div class="row formRow text-center">
                                                    <label>
                                                        <g:message code="redactintrigue.role.roleDescription" default="Description"/>
                                                    </label>
                                                </div>
                                                <div contenteditable="false" class="text-left richTextEditor">
                                                    ${role.getRoleHasEvent(event)?.description?.encodeAsHTML()}
                                                </div>
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
                                <g:each in="${pastscenes}" var="pastscene">
                                    <div class="accordion-group">
                                        <g:if test="${role?.getRoleHasPastScene(pastscene)?.title}">
                                            <div class="accordion-heading alert-success">
                                        </g:if>
                                        <g:else>
                                            <div class="accordion-heading">
                                        </g:else>
                                            <a class="accordion-toggle spanLabel" data-toggle="collapse" data-parent="#accordionPastScene${role.id}"
                                               href="#collapsePastScene${role.id}-${pastscene.id}" data-pastsceneId="${pastscene.id}">

                                                <g:pastsceneTime pastsceneId="${pastscene.id}"/>
                                            </a>
                                        </div>
                                        <div id="collapsePastScene${role.id}-${pastscene.id}" class="accordion-body collapse">
                                            <div class="accordion-inner">
                                                <div class="formRow">
                                                    <div class="row formRow text-center">
                                                        <label>
                                                            <g:message code="redactintrigue.role.roleTitle" default="Title"/>
                                                        </label>
                                                    </div>
                                                </div>
                                                <div contenteditable="false" class="text-left richTextEditor textTitle">
                                                    ${role?.getRoleHasPastScene(pastscene)?.title?.encodeAsHTML()}
                                                </div>
                                                <div class="row formRow text-center">
                                                    <label>
                                                        <g:message code="redactintrigue.role.roleDescription" default="Description"/>
                                                    </label>
                                                </div>
                                                <div contenteditable="false" class="text-left richTextEditor">
                                                    ${role?.getRoleHasPastScene(pastscene)?.description?.encodeAsHTML()}
                                                </div>
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
                    <g:plotOwner idOwner="${plotInstance.user.id}" lvlright="${right.MINTRIGUEMODIFY.value()}" lvlrightAdmin="${right.INTRIGUEMODIFY.value()}">
                        <input type="button" name="Update" data-id="${role.id}" value="Update" class="btn btn-primary updateRole"/>
                    </g:plotOwner>
                </form>
            </div>
        </g:each>
    </div>
</div>