<form name="updateRelation${role.id}_${relationFrom.id}" data-url="<g:createLink controller="Relation" action="Update" id="${relationFrom.id}"/>">
    <g:hiddenField name="relationDescription" class="descriptionContent" value=""/>
    <g:if test="${isRole1}">
        <g:hiddenField name="relationFrom" value="${role.id}"/>
    </g:if>
    <g:else>
        <g:hiddenField name="relationTo" value="${role.id}"/>
    </g:else>
    <g:hiddenField name="id" value="${relationFrom.id}"/>
    <div class="row formRow">
        <div class="span1">
            <label for="relationType">
                <g:message code="redactintrigue.relation.type" default="Relation type"/>
            </label>
        </div>
        <div class="span4">
            <g:select name="relationType" id="relationType" from="${relationTypes}"
                      value="${relationFrom.roleRelationType?.id}" optionKey="id" required="" optionValue="name"/>
        </div>
        <div class="span1">
            <label for="relationTo">
                <g:message code="redactintrigue.relation.to" default="To"/>
            </label>
        </div>
        <div class="span4">
            <g:if test="${isRole1}">
                <g:select name="relationTo" id="relationTo" from="${plotInstance.roles}"
                          value="${relationFrom.role2?.id}" optionKey="id" required="" optionValue="code"/>
            </g:if>
            <g:else>
                <g:select name="relationFrom" id="relationFrom" from="${plotInstance.roles}"
                          value="${relationFrom.role1?.id}" optionKey="id" required="" optionValue="code"/>
            </g:else>
        </div>
    </div>
    <div class="row formRow">
        <div class="span1">
            <label for="relationBijective">
                <g:message code="redactintrigue.relation.bijective" default="Bijective"/>
            </label>
        </div>
        <div class="span4">
            <g:checkBox name="relationBijective" id="relationBijective" value="${relationFrom.isBijective}"/>
        </div>
        <div class="span1">
            <label for="relationExclusive">
                <g:message code="redactintrigue.relation.exclusive" default="Exclusive"/>
            </label>
        </div>
        <div class="span4">
            <g:checkBox name="relationExclusive" id="relationExclusive" value="${relationFrom.isExclusive}"/>
        </div>
    </div>
    <div class="row formRow">
        <div class="span1">
            <label for="relationWeight">
                <g:message code="redactintrigue.relation.weight" default="Weight"/>
            </label>
        </div>
        <div class="span4">
            <g:field type="number" name="relationWeight" id="relationWeight" required="" value="${relationFrom.weight}"/>
        </div>
        <div class="span1">
            <label for="relationHidden">
                <g:message code="redactintrigue.relation.hidden" default="Hidden"/>
            </label>
        </div>
        <div class="span4">
            <g:checkBox name="relationHidden" id="relationHidden" value="${relationFrom.isHidden}"/>
        </div>
    </div>
    <div class="row formRow text-center">
        <label for="relationDescription">
            <g:message code="redactintrigue.relation.description" default="Description"/>
        </label>
    </div>
    <div class="fullScreenEditable">
        <g:render template="dropdownButtons" />

        <!-- Editor -->
        <div id="relationRichTextEditor${role.id}_${relationFrom.id}" contenteditable="true" class="text-left richTextEditor" onblur="saveCarretPos($(this).attr('id'))">
            ${relationFrom.description?.encodeAsHTML()}
        </div>
    </div>
    %{--<g:textArea name="relationDescription" id="relationDescription" value="${relationFrom.description}" rows="5" cols="100"/>--}%
    <g:if test="${isRole1}">
        <input type="button" name="Update" data-id="${relationFrom.id}" data-roleFromId="${relationFrom.role1?.id}"
               data-oldRoleToId="${relationFrom.role2?.id}" value="Update" class="btn btn-primary updateRelation"
               data-wasBijective="${relationFrom.isBijective}"/>
    </g:if>
    <g:else>
        <input type="button" name="Update" data-id="${relationFrom.id}" data-roleFromId="${relationFrom.role2?.id}"
               data-oldRoleToId="${relationFrom.role1?.id}" value="Update" class="btn btn-primary updateRelation"
               data-wasBijective="${relationFrom.isBijective}"/>
    </g:else>
</form>