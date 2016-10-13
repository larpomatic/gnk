<%@ page import="org.gnk.selectintrigue.Plot" %>
<div class="tabbable tabs-left plotScreen">
    <div class="tab-content">
        <div class="tab-pane active" id="newPlot">
            <form class="savePlotForm" data-url="<g:createLink controller="RedactIntrigue" action="Update" id="${plotInstance.id}"/>">
                <g:hiddenField name="id" value="${plotInstance?.id}"/>
                <g:hiddenField name="version" value="${plotInstance?.version}"/>
                <g:hiddenField name="screenStep" value="0"/>
                <g:hiddenField name="plotDescription" class="descriptionContent" value=""/>
                <g:hiddenField name="plotPitchOrga" class="pitchOrgaContent" value=""/>
                <g:hiddenField name="plotPitchPj" class="pitchPjContent" value=""/>
                <g:hiddenField name="plotPitchPnj" class="pitchPnjContent" value=""/>
                <g:hiddenField name="plotVariantField" class="variantContent" value=""/>
                <div class="row formRow">
                    <div class="span1"></div>
                    <div class="span1">
                        <label for="name">
                            <g:message code="redactintrigue.generalDescription.plotName" default="Name"/>
                        </label>
                    </div>

                    <div class="span10">
                        <g:textField name="name" value="${plotInstance?.name}" required="" class="inputLargeWidth"/>
                    </div>

                </div>

                <div class="row formRow">
                    <div class="span2">
                        <label for="plotVariant">
                            <g:message code="redactintrigue.generalDescription.plotVariant" default="Select a variant"/>
                        </label>
                    </div>
                    <div class="span2">
                        <g:select name="plotVariant" id="plotVariant" from="${availableVariant}" value="${plotInstance.variant}"
                                  optionKey="id" optionValue="name" noSelection="${['':'Intrigue originale']}"/>
                    </div>

                    <div id="plotVariants" class="span8">
                        <g:if test="${plotInstance.variant != null}">
                        <table style="width:100%">
                            <tr>
                                Variantes de cette intrigue :
                            </tr>
                            <g:each in="${variantMap.get(plotInstance)}" status="i5" var="plot">
                                <tr>
                                    ${plot.name}
                                </tr>
                            </g:each>
                        </table>
                        </g:if>
                    </div>
                </div>


                <div class="row formRow">
                    <div class="span1"></div>

                    <div class="span1">
                        <label>
                            <g:message code="redactintrigue.generalDescription.tags" default="Tags"/>
                        </label>
                    </div>

                    <div class="span3">
                        <a href="#tagsModal" class="btn" data-toggle="modal">
                            <g:message code="redactintrigue.generalDescription.chooseTags" default="Chooses tags"/>
                        </a>
                    </div>

                    <div class="span1">
                        <label>
                            <g:message code="redactintrigue.generalDescription.plotUnivers" default="Univers"/>
                        </label>
                    </div>

                    <div class="span3">
                        <a href="#universModal" class="btn" data-toggle="modal">
                            <g:message code="redactintrigue.generalDescription.chooseUniverses" default="Choose univers"/>
                        </a>
                    </div>

                    <div class="span1">
                        <label for="isPublic">
                            <g:message code="redactintrigue.generalDescription.isPublic" default="Public"/>
                        </label>
                    </div>

                    <div class="span3">
                        <g:checkBox id="isPublic" name="isPublic" checked="${plotInstance.isPublic}"/>
                    </div>
                </div>

                <div class="row formRow">
                    <div class="span1"></div>
                    <div class="span1">
                        <label for="isMainstream">
                            <g:message code="redactintrigue.generalDescription.isMainstream" default="Mainstream"/>
                        </label>
                    </div>

                    <div class="span3">
                        <g:checkBox name="isMainstream" id="isMainstream" checked="${plotInstance.isMainstream}"/>
                    </div>

                    <div class="span1">
                        <label for="isEvenemential">
                            <g:message code="redactintrigue.generalDescription.isEvenemential" default="Evenemential"/>
                        </label>
                    </div>

                    <div class="span3">
                        <g:checkBox name="isEvenemential" id="isEvenemential" checked="${plotInstance.isEvenemential}"/>
                    </div>

                    <div class="span1">
                        <label for="isDraft">
                            <g:message code="redactintrigue.generalDescription.isDraft" default="Draft"/>
                        </label>
                    </div>

                    <div class="span3">
                        <g:checkBox id="isDraft" name="isDraft" checked="${plotInstance.isDraft}"/>
                    </div>

                </div>
                <div class="span14">
                    <div class="panel-heading">
                        <h3 class="panel-title">Sommaire des descriptions</h3>
                    </div>
                    <div class="list-group" id="divFields">
                        <li class="list-group-item">Contexte</li>
                        <li class="list-group-item">La vie du héros</li>
                        <li class="list-group-item">Le secret</li>
                        <li class="list-group-item">Une description</li>
                    </div>
                </div>
                <div id="new_render">
                    <g:render template="pitchForm"/>
                </div>

                <div class="buttonAdd">
                    <div type="button" class="btn btn-success" onclick="return addDescription()" style="width: 150px">Ajouter une autre description</div>
                </div>

                <div id="tagsModal" class="modal hide fade tags-modal" tabindex="-1">
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
                            <g:each in="${plotTagList}" status="i" var="plotTagInstance">
                                <g:render template="plotTagTree" model="[plotTagInstance: plotTagInstance, plotInstance: plotInstance]"/>
                            </g:each>
                        </ul>
                    </div>

                    <div class="modal-footer">
                        <button class="btn" data-dismiss="modal">Ok</button>
                    </div>
                </div>


                <div id="universModal" class="modal hide fade tags-modal" tabindex="-1">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">×</button>
                        <h3>Univers</h3>
                        <input class="input-medium search-query" data-content="plotTags"
                               placeholder="<g:message code="redactintrigue.generalDescription.search" default="Search..."/>"/>
                        <button type="button" class="btn btn-primary modifyTag push">
                            <g:message code="redactintrigue.generalDescription.validatedTags" default="Validated tags"/>
                        </button>
                    </div>

                    <div class="modal-body">
                        <ul class="plotTags">
                            <g:each in="${plotUniversList}" status="i" var="plotUniversInstance">
                                <g:render template="plotTagTree" model="[plotTagInstance: plotUniversInstance, plotInstance: plotInstance]"/>
                            </g:each>
                        </ul>
                    </div>

                    <div class="modal-footer">
                        <button class="btn" data-dismiss="modal">Ok</button>
                    </div>
                </div>
                <fieldset class="buttons text-center">
                    <g:plotOwner idOwner="${plotInstance.user.id}" lvlright="${right.MINTRIGUEMODIFY.value()}" lvlrightAdmin="${right.INTRIGUEMODIFY.value()}">
                        <input type="button" name="Update" value="${message(code: 'default.button.update.label', default: 'Update')}" class="btn btn-primary updatePlot"/>
                    </g:plotOwner>
                    <g:plotOwner idOwner="${plotInstance.user.id}" lvlright="${right.MINTRIGUEDELETE.value()}" lvlrightAdmin="${right.INTRIGUEDELETE.value()}">
                        <input type="button" name="Delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" data-redirect="<g:createLink controller="RedactIntrigue" action="list"/>"
                               class="btn btn-danger deletePlot" data-toggle="confirmation-popout" data-placement="right" title="Supprimer l'intrigue ?"
                               data-url="<g:createLink controller="RedactIntrigue" action="delete" id="${plotInstance.id}"/>" data-object="plot"/>
                    </g:plotOwner>
                </fieldset>
            </form>
        </div>
    </div>
</div>

<script type="application/javascript">
    function addDescription() {
        var $clone = $("#render").clone();
        $('<div id="new_render"/>').appendTo($('.render'));
        $('.new_render').append($clone);
    }
</script>