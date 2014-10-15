<li class="modalLi row" data-name="${resourceTagInstance.name.toLowerCase()}">
    <label class="pull-left">
        <g:if test="${resource == null}">
            <g:checkBox name="resourceTags_${resourceTagInstance.id}" id="resourceTags_${resourceTagInstance.id}" checked="false"
                        onClick="hideTags('resourceTags_${resourceTagInstance.id}', 'resourceTagsWeight_${resourceTagInstance.id}')"/>
        </g:if>
        <g:else>
            <g:checkBox name="resourceTags_${resourceTagInstance.id}" id="resourceTags${resource.id}_${resourceTagInstance.id}"
                    checked="${resource.hasGenericResourceTag(resourceTagInstance)}"
                    onClick="hideTags('resourceTags${resource.id}_${resourceTagInstance.id}', 'resourceTagsWeight${resource.id}_${resourceTagInstance.id}')"/>
        </g:else>
        ${resourceTagInstance.name}
    </label>
    <div class="pull-right">
        <button type="button" class="btn btn-danger banTag"><i class="icon-ban-circle"></i></button>
    </div>
    <div class="pull-right tagWeight">
        <g:if test="${resource == null}">
            <input name="resourceTagsWeight_${resourceTagInstance.id}" value="50" type="number" max="101" class="tagWeightInput"
                   min="-101" style="width:45px;" id="resourceTagsWeight_${resourceTagInstance.id}">
        </g:if>
        <g:else>
            <g:if test="${resource.hasGenericResourceTag(resourceTagInstance)}">
                <input name="resourceTagsWeight_${resourceTagInstance.id}" value="${resource.getGenericResourceHasTag(resourceTagInstance).weight}" class="tagWeightInput"
                   type="number" max="101" min="-101" style="width:45px;" id="resourceTagsWeight${resource.id}_${resourceTagInstance.id}">
            </g:if>
            <g:else>
                <input name="resourceTagsWeight_${resourceTagInstance.id}" value="50" type="number" max="101" class="tagWeightInput"
                   min="-101" style="width:45px;" id="resourceTagsWeight${resource.id}_${resourceTagInstance.id}">
            </g:else>
        </g:else>
    </div>
    <div class="pull-right">
        <button type="button" class="btn btn-success chooseTag"><i class="icon-ok"></i></button>
    </div>
</li>
<g:if test="${resourceTagInstance.children.size() != 0}">
    <li class="modalLi">
        <ul>
            <g:each in="${resourceTagInstance.children}" var="child">
                <g:render template="resourceTagTree" model="[resourceTagInstance: child, resource: resource]"/>
            </g:each>
        </ul>
    </li>
</g:if>