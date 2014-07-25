<li class="modalLi row" data-name="${roleTagInstance.name.toLowerCase()}">
    <label class="pull-left">
        <g:if test="${role == null}">
            <g:checkBox name="roleTags_${roleTagInstance.id}" id="roleTags_${roleTagInstance.id}" checked="false"
                        onClick="hideTags('roleTags_${roleTagInstance.id}', 'roleTagsWeight_${roleTagInstance.id}')"/>
        </g:if>
        <g:else>
            <g:checkBox name="roleTags_${roleTagInstance.id}" id="roleTags${role.id}_${roleTagInstance.id}"
                    checked="${role.hasRoleTag(roleTagInstance)}"
                    onClick="hideTags('roleTags${role.id}_${roleTagInstance.id}', 'roleTagsWeight${role.id}_${roleTagInstance.id}')"/>
        </g:else>
        ${roleTagInstance.name}
    </label>
    <div class="pull-right">
        <button type="button" class="btn btn-danger banTag"><i class="icon-ban-circle"></i></button>
    </div>
    <div class="pull-right tagWeight">
        <g:if test="${role == null}">
            <input name="roleTagsWeight_${roleTagInstance.id}" value="50" type="number" max="101" class="tagWeightInput"
                   min="-101" style="width:45px;" id="roleTagsWeight_${roleTagInstance.id}">
        </g:if>
        <g:else>
            <g:if test="${role.hasRoleTag(roleTagInstance)}">
                <input name="roleTagsWeight_${roleTagInstance.id}" value="${role.getRoleHasTag(roleTagInstance).weight}" class="tagWeightInput"
                   type="number" max="101" min="-101" style="width:45px;" id="roleTagsWeight${role.id}_${roleTagInstance.id}">
            </g:if>
            <g:else>
                <input name="roleTagsWeight_${roleTagInstance.id}" value="50" type="number" max="101" class="tagWeightInput"
                   min="-101" style="width:45px;" id="roleTagsWeight${role.id}_${roleTagInstance.id}">
            </g:else>
        </g:else>
    </div>
    <div class="pull-right">
        <button type="button" class="btn btn-success chooseTag"><i class="icon-ok"></i></button>
    </div>
</li>
<g:if test="${roleTagInstance.children.size() != 0}">
    <li class="modalLi">
        <ul>
            <g:each in="${roleTagInstance.children}" var="child">
                <g:render template="roleTagTree" model="[roleTagInstance: child, role: role]"/>
            </g:each>
        </ul>
    </li>
</g:if>