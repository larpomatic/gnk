<li class="modalLi row" data-name="${plotTagInstance.name.toLowerCase()}">
    <label class="pull-left">
        <g:checkBox name="plotTags_${plotTagInstance.id}" id="plotTags${plotInstance.id}_${plotTagInstance.id}"
                checked="${plotInstance.hasPlotTag(plotTagInstance)}"
                onClick="hideTags('plotTags${plotInstance.id}_${plotTagInstance.id}', 'plotTagsWeight${plotInstance.id}_${plotTagInstance.id}')"/>
        ${fieldValue(bean: plotTagInstance, field: "name")}
    </label>
    <div class="pull-right">
        <button type="button" class="btn btn-danger banTag"><i class="icon-ban-circle"></i></button>
    </div>
    <div class="pull-right tagWeight">
        <g:if test="${plotInstance.hasPlotTag(plotTagInstance)}">
            <input name="plotTagsWeight_${plotTagInstance.id}" value="${plotInstance.getPlotHasTag(plotTagInstance).weight}" class="tagWeightInput"
               type="number" max="101" min="-101" style="width:45px;" id="plotTagsWeight${plotInstance.id}_${plotTagInstance.id}">
        </g:if>
        <g:else>
            <input name="plotTagsWeight_${plotTagInstance.id}" value="50" type="number" max="101" class="tagWeightInput"
               min="-101" style="width:45px;" id="plotTagsWeight${plotInstance.id}_${plotTagInstance.id}">
        </g:else>
    </div>
    <div class="pull-right">
        <button type="button" class="btn btn-success chooseTag"><i class="icon-ok"></i></button>
    </div>
</li>
<g:if test="${plotTagInstance.children.size() != 0}">
    <li class="modalLi">
        <ul>
            <g:each in="${plotTagInstance.getChildren()}" var="child">
                <g:render template="plotTagTree" model="[plotTagInstance: child, plotInstance: plotInstance]"/>
            </g:each>
        </ul>
    </li>
</g:if>