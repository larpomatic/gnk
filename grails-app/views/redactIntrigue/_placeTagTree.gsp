<li class="modalLi row" data-name="${placeTagInstance.name.toLowerCase()}">
    <label class="pull-left">
        <g:if test="${place == null}">
            <g:checkBox name="placeTags_${placeTagInstance.id}" id="placeTags_${placeTagInstance.id}" checked="false"
                        onClick="hideTags('placeTags_${placeTagInstance.id}', 'placeTagsWeight_${placeTagInstance.id}')"/>
        </g:if>
        <g:else>
            <g:checkBox name="placeTags_${placeTagInstance.id}" id="placeTags${place.id}_${placeTagInstance.id}"
                    checked="${place.hasGenericPlaceTag(placeTagInstance)}"
                    onClick="hideTags('placeTags${place.id}_${placeTagInstance.id}', 'placeTagsWeight${place.id}_${placeTagInstance.id}')"/>
        </g:else>
        ${placeTagInstance.name}
    </label>
    <div class="pull-right">
        <button type="button" class="btn btn-danger banTag"><i class="icon-ban-circle"></i></button>
    </div>
    <div class="pull-right tagWeight">
        <g:if test="${place == null}">
            <input name="placeTagsWeight_${placeTagInstance.id}" value="50" type="number" max="101" class="tagWeightInput"
                   min="-101" style="width:45px;" id="placeTagsWeight_${placeTagInstance.id}">
        </g:if>
        <g:else>
            <g:if test="${place.hasGenericPlaceTag(placeTagInstance)}">
                <input name="placeTagsWeight_${placeTagInstance.id}" value="${place.getGenericPlaceHasTag(placeTagInstance).weight}" class="tagWeightInput"
                   type="number" max="101" min="-101" style="width:45px;" id="placeTagsWeight${place.id}_${placeTagInstance.id}">
            </g:if>
            <g:else>
                <input name="placeTagsWeight_${placeTagInstance.id}" value="50" type="number" max="101" class="tagWeightInput"
                   min="-101" style="width:45px;" id="placeTagsWeight${place.id}_${placeTagInstance.id}">
            </g:else>
        </g:else>
    </div>
    <div class="pull-right">
        <button type="button" class="btn btn-success chooseTag"><i class="icon-ok"></i></button>
    </div>
</li>
<g:if test="${placeTagInstance.children.size() != 0}">
    <li class="modalLi">
        <ul>
            <g:each in="${placeTagInstance.children}" var="child">
                <g:render template="placeTagTree" model="[placeTagInstance: child, place: place]"/>
            </g:each>
        </ul>
    </li>
</g:if>