<li data-name="${tagInstance.name.toLowerCase()}" class="row">
    <label for="${tagPrefix }${tagInstance?.id}" class="pull-left">
        <g:checkBox name="${tagPrefix }${tagInstance?.id}" id="${tagPrefix }${tagInstance?.id}"
                    checked="${myOwner?.hasTag(tagInstance, tagListName)}"
                    onClick="hideTags('${tagPrefix }${tagInstance?.id}', '${weightTagPrefix}${tagInstance?.id}')"/>
        ${fieldValue(bean: tagInstance, field: "name")}
    </label>
    <div class="pull-right">
        <button type="button" class="btn btn-danger banTag"><i class="icon-ban-circle"></i></button>
    </div>
    <div class="pull-right tagWeight">
        <g:if test="${myOwner != null}">
            <input id="${weightTagPrefix}${tagInstance?.id}" name="${weightTagPrefix}${tagInstance?.id}"
                   value="${myOwner?.getTagWeight(tagInstance, tagListName)}" style="width:45px;"
                   type="number" max="101" min="-101">
        </g:if>
        <g:if test="${myOwner == null}">
            <input id="${weightTagPrefix}${tagInstance?.id}" name="${weightTagPrefix}${tagInstance?.id}"
                   value="50" type="number" max="101" min="-101" style="width:45px;">
        </g:if>
    </div>
    <div class="pull-right">
        <button type="button" class="btn btn-success chooseTag"><i class="icon-ok"></i></button>
    </div>
</li>
<g:if test="${tagInstance.children.size() != 0}">
    <li class="modalLi">
        <ul>
            <g:each in="${tagInstance.children}" var="child">
                <g:render template="plotTagTree" model="[tagInstance: child, tagPrefix: tagPrefix, myOwner: myOwner, weightTagPrefix: weightTagPrefix, tagListName: tagListName]"/>
            </g:each>
        </ul>
    </li>
</g:if>