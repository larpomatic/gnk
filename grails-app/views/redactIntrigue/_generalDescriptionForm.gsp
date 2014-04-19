<%@ page import="org.gnk.selectintrigue.Plot"%>

<div class="tabbable tabs-left plotScreen">
    <ul class="nav nav-tabs leftUl">
        <li class="active leftMenuList">
            <a href="#" data-toggle="tab">
                Nouveaux éléments
            </a>
        </li>
        <li class="leftMenuList">
            <input type="text" placeholder="Créer un objet"/>
        </li>
        <li class="leftMenuList">
            <input type="text" placeholder="Créer un lieu"/>
        </li>
        <li class="leftMenuList">
            <input type="text" placeholder="Créer un indice"/>
        </li>
    </ul>

    <div class="tab-content">
        <div class="tab-pane active" id="newEvent">
            <g:form method="post">
                <g:hiddenField name="id" value="${plotInstance?.id}" />
                <g:hiddenField name="version" value="${plotInstance?.version}" />
                <g:hiddenField name="screenStep" value="0" />
                <div class="row formRow">
                    <div class="span1">
                        <label for="name">
                            <g:message code="redactintrigue.generalDescription.plotName" default="Plot's Name" />
                        </label>
                    </div>
                    <div class="span8">
                        <g:textField name="name" value="${plotInstance?.name}" required="" class="inputLargeWidth"/>
                    </div>
                </div>

                <div class="row formRow">
                    <div class="span1">
                        <label>
                            <g:message code="redactintrigue.generalDescription.plotUnivers" default="Universes" />
                        </label>
                    </div>
                    <div class="span4">
                        <a href="#universesModal" class="btn" data-toggle="modal">Choisir les univers</a>
                    </div>
                    <div class="span1">
                        <label>
                            <g:message code="redactintrigue.generalDescription.tags" default="Tags" />
                        </label>
                    </div>
                    <div class="span4">
                        <a href="#tagsModal" class="btn" data-toggle="modal">Choisir les tags</a>
                    </div>
                </div>
                <div class="row formRow">
                    <div class="span2">
                        <label for="isPublic">
                            <g:message code="redactintrigue.generalDescription.isPublic" default="Public" />
                        </label>
                    </div>
                    <div class="span1">
                        <g:checkBox id="isPublic" name="isPublic" checked="${plotInstance.isPublic}" />
                    </div>
                    <div class="span2">
                        <label for="isMainstream">
                            <g:message code="redactintrigue.generalDescription.isMainstream" default="Mainstream" />
                        </label>
                    </div>
                    <div class="span1">
                        <g:checkBox name="isMainstream" id="isMainstream" checked="${plotInstance.isMainstream}" />
                    </div>
                    <div class="span2">
                        <label for="isEvenemential">
                            <g:message code="redactintrigue.generalDescription.isEvenemential" default="Evenemential" />
                        </label>
                    </div>
                    <div class="span1">
                        <g:checkBox name="isEvenemential" id="isEvenemential" checked="${plotInstance.isEvenemential}" />
                    </div>
                </div>
                <div class="row formRow text-center">
                    <label for="plotDescription">
                        <g:message code="redactintrigue.generalDescription.plotDescription" default="Plot Description" />
                    </label>
                </div>
                <g:textArea name="plotDescription" id="plotDescription" value="${plotInstance.description}" rows="5" cols="100" />

                <g:render template="richTextEditor" />
                <div id="universesModal" class="modal hide fade" tabindex="-1">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">×</button>
                        <h3>Univers</h3>
                    </div>
                    <div class="modal-body">

                        <table>
                            <g:each in="${universList}" status="i" var="universInstance">
                                <g:if test="${i % 3 == 0}">
                                    <tr>
                                </g:if>
                                <td><label for="universes_${universInstance.id}"><g:checkBox
                                        name="universes_${universInstance.id}"
                                        id="universes_${universInstance.id}"
                                        checked="${plotInstance.hasUnivers(universInstance)}" /> ${fieldValue(bean: universInstance, field: "name")}</label></td>
                                <g:if test="${(i+1) % 3 == 0}">
                                    </tr>
                                </g:if>
                            </g:each>

                        </table>
                    </div>
                    <div class="modal-footer">
                        <button class="btn" data-dismiss="modal">Ok</button>
                    </div>
                </div>

                <div id="tagsModal" class="modal hide fade" tabindex="-1">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">×</button>
                        <h3>Tags</h3>
                    </div>
                    <div class="modal-body">

                        <table>
                            <g:each in="${plotTagList}" status="i" var="plotTagInstance">
                                <g:if test="${i % 3 == 0}">
                                    <tr>
                                </g:if>
                                <td><label for="tags_${plotTagInstance.id}" style="float: left"><g:checkBox
                                        name="tags_${plotTagInstance.id}"
                                        id="tags_${plotTagInstance.id}"
                                        onClick="toggle('tags_${plotTagInstance?.id}', 'weight_tags_${plotTagInstance?.id}')"
                                        checked="${plotInstance.hasPlotTag(plotTagInstance)}" /> ${fieldValue(bean: plotTagInstance, field: "name")}</label>
                                    <div style="overflow: hidden; padding-left: .5em;">
                                        <g:if test="${plotInstance.hasPlotTag(plotTagInstance)}">
                                            <g:set var="tagValue" value="${plotInstance.getTagWeight(plotTagInstance)}" scope="page" />
                                        </g:if>
                                        <g:if test="${!plotInstance.hasPlotTag(plotTagInstance)}">
                                            <g:set var="tagValue" value="50" scope="page" />
                                        </g:if>
                                        <input id="weight_tags_${plotTagInstance?.id}" name="weight_tags_${plotTagInstance?.id}" value="${tagValue}" type="number" style="width:40px;"></div>
                                    <script>
                                        toggle('tags_${plotTagInstance?.id}', 'weight_tags_${plotTagInstance?.id}')
                                    </script>


                                </td>
                                <g:if test="${(i+1) % 3 == 0}">
                                    </tr>
                                </g:if>
                            </g:each>

                        </table>
                    </div>
                    <div class="modal-footer">
                        <button class="btn" data-dismiss="modal">Ok</button>
                    </div>
                </div>
                <fieldset class="buttons text-center">
                    <g:actionSubmit class="save" action="update"
                                    value="${message(code: 'default.button.update.label', default: 'Update')}" />
                    <g:actionSubmit class="delete" action="delete"
                                    value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                                    formnovalidate=""
                                    onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
                </fieldset>
            </g:form>
        </div>
    </div>
</div>