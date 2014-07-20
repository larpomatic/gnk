<div class="tabbable tabs-left pastSceneScreen">
    <ul class="nav nav-tabs leftUl">
        <li class="active leftMenuList">
            <a href="#newPastScene" data-toggle="tab" class="addPastScene">
                <g:message code="redactintrigue.pastScene.addPastScene" default="New object"/>
            </a>
        </li>
        <g:each in="${plotInstance.pastescenes}" status="i5" var="pastScene">
            <li class="leftMenuList">
                <a href="#pastScene_${pastScene.id}" data-toggle="tab">
                    ${pastScene.title.encodeAsHTML()}
                </a>
                <button data-toggle="confirmation-popout" data-placement="left" class="btn btn-danger" title="Supprimer la scène?"
                        data-url="<g:createLink controller="PastScene" action="Delete" id="${pastScene.id}"/>" data-object="pastScene" data-id="${pastScene.id}">
                    <i class="icon-remove pull-right"></i>
                </button>
            </li>
        </g:each>
    </ul>

    <div class="tab-content">
        <div class="tab-pane active" id="newPastScene">
            <form name="newPastSceneForm" data-url="<g:createLink controller="PastScene" action="Save"/>">
                <g:hiddenField name="pastSceneDescription" class="descriptionContent" value=""/>
                <g:hiddenField name="pastSceneTitle" class="titleContent" value=""/>
                <input type="hidden" name="plotId" id="plotId" value="${plotInstance?.id}"/>
                <div class="row formRow text-center">
                    <label for="pastSceneTitle">
                        <g:message code="redactintrigue.pastScene.pastsceneTitle" default="Titre"/>
                    </label>
                </div>

                <div class="fullScreenEditable">
                    <g:render template="dropdownButtons" />

                    <!-- Editor -->
                    <div id="pastSceneTitleRichTextEditor" contenteditable="true" class="text-left richTextEditor textTitle" onblur="saveCarretPos($(this).attr('id'))">
                    </div>
                </div>

                <div class="row formRow">
                    <div class="span1">
                        <label>
                            <g:message code="redactintrigue.pastScene.pastsceneDatetime" default="Time"/>
                        </label>
                    </div>
                    <div class="span3 pastSceneRelative">
                        <div class="input-append shortInput">
                            <g:field type="number" name="pastSceneRelative" id="pastSceneRelative" value=""/>
                            <g:hiddenField name="pastSceneRelativeUnit" class="pastSceneRelativeUnit" value="Y"/>
                            <div class="btn-group">
                                <button class="btn dropdown-toggle" data-toggle="dropdown">
                                    <span class="relativeTimeMessage">
                                        <g:message code="redactintrigue.pastScene.pastSceneYear" default="Time"/>
                                    </span>
                                    <span class="caret"></span>
                                </button>
                                <ul class="dropdown-menu">
                                    <li>
                                        <a class="pastSceneRelativeTimeUnit" data-unitTime="Y">
                                            <g:message code="redactintrigue.pastScene.pastSceneYear" default="Year"/>
                                        </a>
                                    </li>
                                    <li>
                                        <a class="pastSceneRelativeTimeUnit" data-unitTime="M">
                                            <g:message code="redactintrigue.pastScene.pastSceneMonth" default="Month"/>
                                        </a>
                                    </li>
                                    <li>
                                        <a class="pastSceneRelativeTimeUnit" data-unitTime="d">
                                            <g:message code="redactintrigue.pastScene.pastSceneDay" default="Day"/>
                                        </a>
                                    </li>
                                    <li>
                                        <a class="pastSceneRelativeTimeUnit" data-unitTime="h">
                                            <g:message code="redactintrigue.pastScene.pastSceneHour" default="Hour"/>
                                        </a>
                                    </li>
                                    <li>
                                        <a class="pastSceneRelativeTimeUnit" data-unitTime="m">
                                            <g:message code="redactintrigue.pastScene.pastSceneMinute" default="Minute"/>
                                        </a>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>

                    <div class="span2">
                        <label for="pastScenePublic">
                            <g:message code="redactintrigue.pastScene.pastscenePublic" default="Public"/>
                        </label>
                    </div>

                    <div class="span2">
                        <g:checkBox name="pastScenePublic" id="pastScenePublic"/>
                    </div>

                    <div class="span2">
                        <a href="#pastsceneRolesModal" class="btn" data-toggle="modal">
                            <g:message code="redactintrigue.pastScene.chooseRoles" default="Choose roles"/>
                        </a>
                    </div>
                </div>

                <div class="row formRow text-center">
                    <label for="pastSceneDescription">
                        <g:message code="redactintrigue.pastScene.absolute" default="Absolute time"/>
                    </label>
                </div>

                <div class="row formRow">
                    <div class="span2">Année</div>
                    <div class="span2">Mois</div>
                    <div class="span2">Jour</div>
                    <div class="span2">Heure</div>
                    <div class="span2">Minute</div>
                </div>
                <div class="row formRow littleRow">
                    <div class="span2 shortInput"><g:field type="number" name="year" id="year"/></div>
                    <div class="span2 shortInput"><g:field type="number" name="month" id="month"/></div>
                    <div class="span2 shortInput"><g:field type="number" name="day" id="day"/></div>
                    <div class="span2 shortInput"><g:field type="number" name="hour" id="hour"/></div>
                    <div class="span2 shortInput"><g:field type="number" name="minute" id="minute"/></div>
                </div>

                <div class="row formRow">
                    <div class="span1">
                        <label for="pastScenePlace">
                            <g:message code="redactintrigue.pastScene.pastscenePlace" default="Place"/>
                        </label>
                    </div>

                    <div class="span4">
                        <g:select name="pastScenePlace" id="pastScenePlace" from="${plotInstance.genericPlaces}"
                                  optionKey="id" required="" optionValue="code" noSelection="${['null':'']}"/>
                    </div>
                    <div class="span1">
                        <label for="pastScenePredecessor">
                            <g:message code="redactintrigue.pastScene.pastscenePredecessor" default="Predecessor"/>
                        </label>
                    </div>

                    <div class="span4">
                        <g:select name="pastScenePredecessor" id="pastScenePredecessor" from="${plotInstance.pastescenes}"
                                  optionKey="id"  required="" optionValue="title" noSelection="${['null':'']}"/>
                    </div>
                </div>

                <div class="row formRow text-center">
                    <label for="pastSceneDescription">
                        <g:message code="redactintrigue.pastScene.pastsceneDescription" default="Description"/>
                    </label>
                </div>

                <div class="fullScreenEditable">
                    <g:render template="dropdownButtons" />

                    <!-- Editor -->
                    <div id="pastSceneRichTextEditor" contenteditable="true" class="text-left richTextEditor" onblur="saveCarretPos($(this).attr('id'))">
                    </div>
                </div>

                <div id="pastsceneRolesModal" class="modal hide fade largeModal" tabindex="-1">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">×</button>
                        <h3>
                            <g:message code="redactintrigue.pastScene.pastsceneRoles" default="Roles"/>
                        </h3>
                    </div>

                    <div class="modal-body">
                        <div class="tabbable tabs-left">
                            <ul class="nav nav-tabs leftUl">
                                <g:each in="${plotInstance.roles}" status="i4" var="role">
                                <li class="">
                                    <a href="#pastsceneRole${role.id}_" data-toggle="tab">
                                        ${role.code}
                                    </a>
                                </li>
                                </g:each>
                            </ul>
                            <div class="tab-content">
                                <g:each in="${plotInstance.roles}" status="i4" var="role">
                                <div class="tab-pane" id="pastsceneRole${role.id}_">
                                    <g:hiddenField name="roleHasPastSceneDescription${role.id}" class="descriptionContent" value=""/>
                                    <g:hiddenField name="roleHasPastSceneTitle${role.id}" class="titleContent" value=""/>
                                    <div class="row formRow text-center">
                                        <label>
                                            <g:message code="redactintrigue.role.roleTitle" default="Title"/>
                                        </label>
                                    </div>
                                    <div class="fullScreenEditable">
                                        <g:render template="dropdownButtons" />

                                        <!-- Editor -->
                                        <div id="roleHasPastSceneTitleRichTextEditor${role.id}" contenteditable="true" class="text-left richTextEditor textTitle" onblur="saveCarretPos($(this).attr('id'))">
                                        </div>
                                    </div>
                                    <div class="row formRow text-center">
                                        <label>
                                            <g:message code="redactintrigue.role.roleDescription" default="Description"/>
                                        </label>
                                    </div>
                                    <div class="fullScreenEditable">
                                        <g:render template="dropdownButtons" />
                                        <div id="roleHasPastSceneRichTextEditor${role.id}" contenteditable="true" class="text-left richTextEditor" onblur="saveCarretPos($(this).attr('id'))">
                                        </div>
                                    </div>
                                </div>
                                </g:each>
                            </div>
                        </div>
                    </div>

                    <div class="modal-footer">
                        <button class="btn" data-dismiss="modal">Ok</button>
                    </div>
                </div>

                <input type="button" name="Insert" value="Insert" class="btn btn-primary insertPastScene"/>
            </form>
        </div>

        <g:each in="${plotInstance.pastescenes}" var="pastScene">
            <div class="tab-pane" id="pastScene_${pastScene.id}">
                <form name="updatePastScene_${pastScene.id}" data-url="<g:createLink controller="PastScene" action="Update" id="${pastScene.id}"/>">
                    <g:hiddenField name="id" value="${pastScene.id}"/>
                    <g:hiddenField name="pastSceneDescription" class="descriptionContent" value=""/>
                    <g:hiddenField name="pastSceneTitle" class="titleContent" value=""/>
                    <input type="hidden" name="plotId" id="plotId" value="${plotInstance?.id}"/>
                    <div class="row formRow text-center">
                        <label for="pastSceneTitle">
                            <g:message code="redactintrigue.pastScene.pastsceneTitle" default="Titre"/>
                        </label>
                    </div>

                    <div class="fullScreenEditable">
                        <g:render template="dropdownButtons" />

                        <!-- Editor -->
                        <div id="pastSceneTitleRichTextEditor${pastScene.id}" contenteditable="true" class="text-left richTextEditor textTitle" onblur="saveCarretPos($(this).attr('id'))">
                            ${pastScene.title.encodeAsHTML()}
                        </div>
                    </div>

                    <div class="row formRow">
                        <div class="span1">
                            <label>
                                <g:message code="redactintrigue.pastScene.pastsceneDatetime" default="Date and Time"/>
                            </label>
                        </div>
                        <div class="span3 pastSceneRelative">
                            <div class="input-append shortInput">
                                <g:field type="number" name="pastSceneRelative" id="pastSceneRelative${pastScene.id}" value="${pastScene.timingRelative}"/>
                                <g:hiddenField name="pastSceneRelativeUnit" class="pastSceneRelativeUnit" value="${pastScene.unitTimingRelative}"/>
                                <div class="btn-group">
                                    <button class="btn dropdown-toggle" data-toggle="dropdown">
                                        <span class="relativeTimeMessage">
                                            <g:timeUnit unit="${pastScene.unitTimingRelative}" />
                                        </span>
                                        <span class="caret"></span>
                                    </button>
                                    <ul class="dropdown-menu">
                                        <li>
                                            <a class="pastSceneRelativeTimeUnit" data-unitTime="Y">
                                                <g:message code="redactintrigue.pastScene.pastSceneYear" default="Year"/>
                                            </a>
                                        </li>
                                        <li>
                                            <a class="pastSceneRelativeTimeUnit" data-unitTime="M">
                                                <g:message code="redactintrigue.pastScene.pastSceneMonth" default="Month"/>
                                            </a>
                                        </li>
                                        <li>
                                            <a class="pastSceneRelativeTimeUnit" data-unitTime="d">
                                                <g:message code="redactintrigue.pastScene.pastSceneDay" default="Day"/>
                                            </a>
                                        </li>
                                        <li>
                                            <a class="pastSceneRelativeTimeUnit" data-unitTime="h">
                                                <g:message code="redactintrigue.pastScene.pastSceneHour" default="Hour"/>
                                            </a>
                                        </li>
                                        <li>
                                            <a class="pastSceneRelativeTimeUnit" data-unitTime="m">
                                                <g:message code="redactintrigue.pastScene.pastSceneMinute" default="Minute"/>
                                            </a>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </div>

                        <div class="span2">
                            <label for="pastScenePublic">
                                <g:message code="redactintrigue.pastScene.pastscenePublic" default="Public"/>
                            </label>
                        </div>
                        <div class="span2">
                            <g:checkBox name="pastScenePublic" id="pastScenePublic" value="${pastScene.isPublic}"/>
                        </div>
                        <div class="span2">
                            <a href="#pastsceneRolesModal${pastScene.id}" class="btn" data-toggle="modal">
                                <g:message code="redactintrigue.pastScene.chooseRoles" default="Choose roles"/>
                            </a>
                        </div>
                    </div>

                    <div class="row formRow text-center">
                        <label for="pastSceneDescription">
                            <g:message code="redactintrigue.pastScene.absolute" default="Absolute time"/>
                        </label>
                    </div>

                    <div class="row formRow">
                        <div class="span2">Année</div>
                        <div class="span2">Mois</div>
                        <div class="span2">Jour</div>
                        <div class="span2">Heure</div>
                        <div class="span2">Minute</div>
                    </div>
                    <div class="row formRow littleRow">
                        <div class="span2 shortInput"><g:field type="number" name="year" id="year" value="${pastScene.dateYear}"/></div>
                        <div class="span2 shortInput"><g:field type="number" name="month" id="month" value="${pastScene.dateMonth}"/></div>
                        <div class="span2 shortInput"><g:field type="number" name="day" id="day" value="${pastScene.dateDay}"/></div>
                        <div class="span2 shortInput"><g:field type="number" name="hour" id="hour" value="${pastScene.dateHour}"/></div>
                        <div class="span2 shortInput"><g:field type="number" name="minute" id="minute" value="${pastScene.dateMinute}"/></div>
                    </div>

                    <div class="row formRow">
                        <div class="span1">
                            <label for="pastScenePlace">
                                <g:message code="redactintrigue.pastScene.pastscenePlace" default="Place"/>
                            </label>
                        </div>

                        <div class="span4">
                            <g:select name="pastScenePlace" id="pastScenePlace" from="${plotInstance.genericPlaces}" value="${pastScene.genericPlace?.id}"
                                      optionKey="id" required="" optionValue="code" noSelection="${['null':'']}"/>
                        </div>
                        <div class="span1">
                            <label for="pastScenePredecessor">
                                <g:message code="redactintrigue.pastScene.pastscenePredecessor" default="Predecessor"/>
                            </label>
                        </div>

                        <div class="span4">
                            <g:select name="pastScenePredecessor" id="pastScenePredecessor" from="${plotInstance.pastescenes}" value="${pastScene.pastscenePredecessor?.id}"
                                      optionKey="id"  required="" optionValue="title" noSelection="${['null':'']}"/>
                        </div>
                    </div>

                    <div class="row formRow text-center">
                        <label for="pastSceneDescription">
                            <g:message code="redactintrigue.pastScene.pastsceneDescription" default="Description"/>
                        </label>
                    </div>

                    <div class="fullScreenEditable">
                        <g:render template="dropdownButtons" />

                        <!-- Editor -->
                        <div id="pastSceneRichTextEditor${pastScene.id}" contenteditable="true" class="text-left richTextEditor" onblur="saveCarretPos($(this).attr('id'))">
                            ${pastScene.description.encodeAsHTML()}
                        </div>
                    </div>
                    <div id="pastsceneRolesModal${pastScene.id}" class="modal hide fade largeModal" tabindex="-1">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">×</button>
                            <h3>
                                <g:message code="redactintrigue.pastScene.pastsceneRoles" default="Roles"/>
                            </h3>
                        </div>

                        <div class="modal-body">
                            <div class="tabbable tabs-left">
                                <ul class="nav nav-tabs leftUl">
                                    <g:each in="${plotInstance.roles}" status="i4" var="role">
                                        <li>
                                            <a href="#pastsceneRole${role.id}_${pastScene.id}" data-toggle="tab">
                                                ${role.code}
                                            </a>
                                        </li>
                                    </g:each>
                                </ul>
                                <div class="tab-content">
                                    <g:each in="${plotInstance.roles}" var="role">
                                        <div class="tab-pane" id="pastsceneRole${role.id}_${pastScene.id}">
                                            <g:hiddenField name="roleHasPastSceneDescription${role.id}" class="descriptionContent" value=""/>
                                            <g:hiddenField name="roleHasPastSceneTitle${role.id}" class="titleContent" value=""/>
                                            <div class="row formRow text-center">
                                                <label>
                                                    <g:message code="redactintrigue.role.roleTitle" default="Title"/>
                                                </label>
                                            </div>
                                            <div class="fullScreenEditable">
                                                <g:render template="dropdownButtons" />

                                                <!-- Editor -->
                                                <div id="roleHasPastSceneTitleRichTextEditor${role.id}_${pastScene.id}" contenteditable="true" class="text-left richTextEditor textTitle" onblur="saveCarretPos($(this).attr('id'))">
                                                    ${role?.getRoleHasPastScene(pastScene)?.title?.encodeAsHTML()}
                                                </div>
                                            </div>
                                            <div class="row formRow text-center">
                                                <label>
                                                    <g:message code="redactintrigue.role.roleDescription" default="Description"/>
                                                </label>
                                            </div>
                                            <div class="fullScreenEditable">
                                                <g:render template="dropdownButtons" />
                                                <div id="roleHasPastSceneRichTextEditor${role.id}_${pastScene.id}" contenteditable="true" class="text-left richTextEditor" onblur="saveCarretPos($(this).attr('id'))">
                                                    ${role?.getRoleHasPastScene(pastScene)?.description?.encodeAsHTML()}
                                                </div>
                                            </div>
                                        </div>
                                    </g:each>
                                </div>
                            </div>
                        </div>

                        <div class="modal-footer">
                            <button class="btn" data-dismiss="modal">Ok</button>
                        </div>
                    </div>

                    <input type="button" name="Update" data-id="${pastScene.id}" value="Update" class="btn btn-primary updatePastScene"/>
                </form>
            </div>
        </g:each>
    </div>

</div>