<li class="modalLi row" data-name="${tagInstance.name.toLowerCase()}">
    <label class="pull-left">
        <g:checkBox name="eventGenericTags_${tagInstance.id}" id="eventGenericTags${genericEventInstance.id}_${tagInstance.id}"
                checked="${genericEventInstance.hasTag(tagInstance)}"
                onClick="hideTags('eventGenericTags${genericEventInstance.id}_${tagInstance.id}', 'eventGenericTagsWeight${genericEventInstance.id}_${tagInstance.id}')"/>
        ${tagInstance.name}
    </label>
    <div class="pull-right">
        <button type="button" class="btn btn-danger banTag"><i class="icon-ban-circle"></i></button>
    </div>
    <div class="pull-right tagWeight">
        <g:if test="${genericEventInstance.hasTag(tagInstance)}">
            <input name="eventGenericTagsWeight_${tagInstance.id}" value="${genericEventInstance.hasTagValue(tagInstance).value}" class="tagWeightInput"
               type="number" max="101" min="-101" style="width:45px;" id="eventGenericTagsWeight${genericEventInstance.id}_${tagInstance.id}">
        </g:if>
        <g:else>
            <input name="eventGenericTagsWeight_${tagInstance.id}" value="50" type="number" max="101" class="tagWeightInput"
               min="-101" style="width:45px;" id="eventGenericTagsWeight${genericEventInstance.id}_${tagInstance.id}">
        </g:else>
    </div>
    <div class="pull-right">
        <button type="button" class="btn btn-success chooseTag"><i class="icon-ok"></i></button>
    </div>
</li>
<g:if test="${tagInstance.children.size() != 0}">
    <li class="modalLi">
        <ul>
            <g:each in="${tagInstance.getChildren()}" var="child">
                <g:render template="genericEventTagTree" model="[tagInstance: child, genericEventInstance: genericEventInstance, referenceTag : referenceTag]"/>
            </g:each>
        </ul>
    </li>
</g:if>