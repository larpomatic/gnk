<%@ page import="org.gnk.selectintrigue.Plot" %>

<div class="tabbable tabs-left plotScreen">
    <ul class="nav nav-tabs leftUl">
        <li class="active leftMenuList">
            <a href="#" data-toggle="tab">
                <g:message code="redactintrigue.generalDescription.newElements" default="New elements"/>
            </a>
        </li>
        <li class="leftMenuList">
            <input data-entity="resource" data-label="important" type="text"
                   placeholder="<g:message code="redactintrigue.generalDescription.newObject" default="Create resource"/>"/>
        </li>
        <li class="leftMenuList">
            <input data-entity="place" data-label="warning" type="text"
                   placeholder="<g:message code="redactintrigue.generalDescription.newPlace" default="Create place"/>"/>
        </li>
        <li class="leftMenuList">
            <input data-entity="role" data-label="success" type="text"
                   placeholder="<g:message code="redactintrigue.generalDescription.newRole" default="Create role"/>"/>
        </li>
    </ul>

    <div class="tab-content">
        <div class="tab-pane active" id="newEvent">
            <g:form method="post" class="savePlotForm">
                <g:hiddenField name="id" value="${plotInstance?.id}"/>
                <g:hiddenField name="version" value="${plotInstance?.version}"/>
                <g:hiddenField name="screenStep" value="0"/>
                <g:hiddenField name="plotDescription" class="descriptionContent" value=""/>
                <g:hiddenField name="plotPitchOrga" class="pitchOrgaContent" value=""/>
                <g:hiddenField name="plotPitchPj" class="pitchPjContent" value=""/>
                <g:hiddenField name="plotPitchPnj" class="pitchPnjContent" value=""/>
                <div class="row formRow">
                    <div class="span1">
                        <label for="name">
                            <g:message code="redactintrigue.generalDescription.plotName" default="Name"/>
                        </label>
                    </div>

                    <div class="span8">
                        <g:textField name="name" value="${plotInstance?.name}" required="" class="inputLargeWidth"/>
                    </div>
                </div>

                <div class="row formRow">
                    <div class="span1">
                        <label>
                            <g:message code="redactintrigue.generalDescription.plotUnivers" default="Universes"/>
                        </label>
                    </div>

                    <div class="span4">
                        <a href="#universesModal" class="btn" data-toggle="modal">
                            <g:message code="redactintrigue.generalDescription.chooseUniverses" default="Chooses universes"/>
                        </a>
                    </div>

                    <div class="span1">
                        <label>
                            <g:message code="redactintrigue.generalDescription.tags" default="Tags"/>
                        </label>
                    </div>

                    <div class="span4">
                        <a href="#tagsModal" class="btn" data-toggle="modal">
                            <g:message code="redactintrigue.generalDescription.chooseTags" default="Chooses tags"/>
                        </a>
                    </div>
                </div>

                <div class="row formRow">
                    <div class="span1">
                        <label for="isMainstream">
                            <g:message code="redactintrigue.generalDescription.isMainstream" default="Mainstream"/>
                        </label>
                    </div>

                    <div class="span4">
                        <g:checkBox name="isMainstream" id="isMainstream" checked="${plotInstance.isMainstream}"/>
                    </div>

                    <div class="span1">
                        <label for="isEvenemential">
                            <g:message code="redactintrigue.generalDescription.isEvenemential" default="Evenemential"/>
                        </label>
                    </div>

                    <div class="span4">
                        <g:checkBox name="isEvenemential" id="isEvenemential" checked="${plotInstance.isEvenemential}"/>
                    </div>
                </div>
                <div class="row formRow">
                    <div class="span1">
                        <label for="isPublic">
                            <g:message code="redactintrigue.generalDescription.isPublic" default="Public"/>
                        </label>
                    </div>

                    <div class="span4">
                        <g:checkBox id="isPublic" name="isPublic" checked="${plotInstance.isPublic}"/>
                    </div>

                    <div class="span1">
                        <label for="isDraft">
                            <g:message code="redactintrigue.generalDescription.isDraft" default="Draft"/>
                        </label>
                    </div>

                    <div class="span4">
                        <g:checkBox id="isDraft" name="isDraft" checked="${plotInstance.isDraft}"/>
                    </div>
                </div>

                <div class="text-center plotTabs">
                    <ul class="nav nav-tabs">
                        <li class="active">
                            <a href="#descriptionTab" data-toggle="tab">
                                <g:message code="redactintrigue.generalDescription.plotDescription" default="Description"/>
                            </a>
                        </li>
                        <li>
                            <a href="#pitchOrgaTab" data-toggle="tab">
                                <g:message code="redactintrigue.generalDescription.pitchOrga" default="pitchOrga"/>
                            </a>
                        </li>
                        <li>
                            <a href="#pitchPjTab" data-toggle="tab">
                                <g:message code="redactintrigue.generalDescription.pitchPj" default="pitchPj"/>
                            </a>
                        </li>
                        <li>
                            <a href="#pitchPnjTab" data-toggle="tab">
                                <g:message code="redactintrigue.generalDescription.pitchPnj" default="pitchPnj"/>
                            </a>
                        </li>
                    </ul>
                    <div class="tab-content">
                        <div class="tab-pane active" id="descriptionTab">
                            <div class="fullScreenEditable">
                                <g:render template="dropdownButtons" />

                                <!-- Editor -->
                                <div id="plotRichTextEditor" contenteditable="true" class="text-left richTextEditor" onblur="saveCarretPos($(this).attr('id'))">
                                    ${plotInstance.description.encodeAsHTML()}
                                </div>
                            </div>
                        </div>
                        <div class="tab-pane" id="pitchOrgaTab">
                            <div class="fullScreenEditable">
                                <g:render template="dropdownButtons" />

                                <!-- Editor -->
                                <div id="plotRichTextEditorPitchOrga" contenteditable="true" class="text-left richTextEditor" onblur="saveCarretPos($(this).attr('id'))">
                                    ${plotInstance.pitchOrga.encodeAsHTML()}
                                </div>
                            </div>
                        </div>
                        <div class="tab-pane" id="pitchPjTab">
                            <div class="fullScreenEditable">
                                <g:render template="dropdownButtons" />

                                <!-- Editor -->
                                <div id="plotRichTextEditorPitchPj" contenteditable="true" class="text-left richTextEditor" onblur="saveCarretPos($(this).attr('id'))">
                                    ${plotInstance.pitchPj.encodeAsHTML()}
                                </div>
                            </div>
                        </div>
                        <div class="tab-pane" id="pitchPnjTab">
                            <div class="fullScreenEditable">
                                <g:render template="dropdownButtons" />

                                <!-- Editor -->
                                <div id="plotRichTextEditorPitchPnj" contenteditable="true" class="text-left richTextEditor" onblur="saveCarretPos($(this).attr('id'))">
                                    ${plotInstance.pitchPnj.encodeAsHTML()}
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div id="universesModal" class="modal hide fade" tabindex="-1">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">×</button>
                        <h3><g:message code="redactintrigue.generalDescription.plotUnivers" default="Universes"/></h3>
                    </div>

                    <div class="modal-body">
                        <ul>
                            <g:each in="${universList}" status="i" var="universInstance">
                                <li class="modalLi">
                                    <label for="universes_${universInstance.id}">
                                        <g:checkBox name="universes_${universInstance.id}"
                                                    id="universes_${universInstance.id}"
                                                    checked="${plotInstance.hasUnivers(universInstance)}"/>
                                        ${fieldValue(bean: universInstance, field: "name")}
                                    </label>
                                </li>
                            </g:each>
                        </ul>
                    </div>

                    <div class="modal-footer">
                        <button class="btn" data-dismiss="modal">Ok</button>
                    </div>
                </div>

                <div id="tagsModal" class="modal hide fade tags-modal" tabindex="-1">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">×</button>
                        <h3>Tags</h3>
                        <input class="input-medium search-query" data-content="plotTags"
                               placeholder="<g:message code="redactintrigue.generalDescription.search" default="Search..."/>"/>
                    </div>

                    <div class="modal-body">
                        <ul class="plotTags">
                            <g:each in="${plotTagList}" status="i" var="plotTagInstance">
                                <g:render template="plotTagTree" model="[plotTagInstance: plotTagInstance, plotInstance: plotInstance]"/>
                            </g:each>
                        </ul>
                    </div>

                    <div class="modal-footer">
                        <button class="btn" data-dismiss="modal">Ok</button>
                    </div>
                </div>
                <fieldset class="buttons text-center">
                    <g:actionSubmit class="save btn btn-primary" action="update"
                                    value="${message(code: 'default.button.update.label', default: 'Update')}"/>
                    <g:actionSubmit class="delete btn btn-danger" action="delete"
                                    value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                                    formnovalidate=""
                                    onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/>
                </fieldset>
            </g:form>
        </div>
    </div>
</div>