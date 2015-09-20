<div class="tabbable tabs-left resourceScreen">
    <ul class="nav nav-tabs leftUl">
        <li class="active leftMenuList">
            <a href="#newResource" data-toggle="tab" class="addResource">
                <g:message code="redactintrigue.resource.addResource" default="New resource"/>
            </a>
        </li>
        <g:each in="${plotInstance.genericResources}" status="i5" var="genericResource">
            <li class="leftMenuList">
                <a href="#resource_${genericResource.id}" data-toggle="tab">
                    ${genericResource.code}
                </a>
                <g:plotOwner idOwner="${plotInstance.user.id}" lvlright="${right.MINTRIGUEMODIFY.value()}" lvlrightAdmin="${right.INTRIGUEMODIFY.value()}">
                    <button data-toggle="confirmation-popout" data-placement="left" class="btn btn-danger" title="Supprimer la ressource?"
                            data-url="<g:createLink controller="GenericResource" action="Delete" id="${genericResource.id}"/>" data-object="resource" data-id="${genericResource.id}">
                        <i class="icon-remove pull-right"></i>
                    </button>
                </g:plotOwner>
            </li>
        </g:each>
    </ul>

    <div class="tab-content">
        <div class="tab-pane active" id="newResource">
            <form name="newResourceForm" data-url="<g:createLink controller="GenericResource" action="Save"/>">
                <input type="hidden" name="plotId" id="plotId" value="${plotInstance?.id}"/>
                <g:hiddenField name="resourceComment" class="commentContent" value=""/>
                <g:hiddenField name="resourceRoleFrom" class="fromRoleTextContent" value=""/>
                <g:hiddenField name="resourceRoleTo" class="toRoleTextContent" value=""/>
                <g:hiddenField name="resourceDescription" class="descriptionContent" value=""/>
                <div class="row formRow">
                    <div class="span1">
                        <label for="resourceCode">
                            <g:message code="redactintrigue.resource.resourceCode" default="Code"/>
                        </label>
                    </div>
                    <div class="span8">
                        <g:textField name="resourceCode" id="resourceCode" value="" required=""/>
                    </div>
                </div>

                <div class="row formRow">
                    <div class="span1">
                        <label>
                            <g:message code="redactintrigue.generalDescription.tags" default="Tags"/>
                        </label>
                    </div>
                    <div class="span4">
                        <a href="#resourceTagsModal" class="btn" data-toggle="modal">
                            <g:message code="redactintrigue.resource.chooseTags" default="Choose tags"/>
                        </a>
                        <button data-target="#bestResourceModal" id="newbestResource" type="button" class="btnBestResource" data-toggle="modal"><i class="btnBestResource img-circle" ></i></button>
                    </div>

                    <!-- Test for generic ressource possessor -->
                    <div class="span1">
                        <label for="resourceRolePossessor">
                            <g:message code="redactintrigue.resource.resourceRolePossessor" default="Possesser"/>
                        </label>
                    </div>
                    <div class="span4">
                        <g:select name="resourceRolePossessor" id="resourceRolePossessor" from="${plotInstance.roles}"
                                  optionKey="id" optionValue="code" noSelection="${['':'Orga']}"/>
                    </div>
                    <!-- End test -->

                </div>
                <div class="row formRow">
                    <div class="span3">
                        <label>
                            <g:message code="redactintrigue.objecttype.resourcetype" default="Resource type : "/>
                        </label>
                    </div>
                    <div class="span1">
                        <label for="resourceObjectToDefine">
                            <g:message code="redactintrigue.objecttype.toDefine" default="To define"/>
                        </label>
                    </div>
                    <div class="span1">
                        <g:radio name="resourceObject" id="resourceObjectToDefine" value="0" checked="checked"/>
                    </div>
                    <div class="span1">
                        <label for="resourceObjectInGame">
                            <g:message code="redactintrigue.objecttype.ingame" default="In game"/>
                        </label>
                    </div>
                    <div class="span1">
                        <g:radio name="resourceObject" id="resourceObjectInGame" value="1"/>
                    </div>
                    <div class="span1">
                        <label for="resourceObjectSimulated">
                            <g:message code="redactintrigue.objecttype.simulated" default="Simulated"/>
                        </label>
                    </div>
                    <div class="span1">
                        <g:radio name="resourceObject" id="resourceObjectSimulated" value="2"/>
                    </div>
                    <div class="span1">
                        <label for="resourceObjectOffGame">
                            <g:message code="redactintrigue.objecttype.offgame" default="Off game"/>
                        </label>
                    </div>
                    <div class="span1">
                        <g:radio name="resourceObject" id="resourceObjectOffGame" value="3"/>
                    </div>
                </div>
                <div class="row formRow text-center">
                    <label for="resourceComment">
                        <g:message code="redactintrigue.resource.resourceComment" default="Comment"/>
                    </label>
                </div>
                <div class="fullScreenEditable">
                    <g:render template="dropdownButtons" />

                    <!-- Editor -->
                    <div id="resourceRichTextEditor" contenteditable="true" class="text-left richTextEditor" onblur="saveCarretPos($(this).attr('id'))">
                    </div>
                </div>
                <div class="row formRow">
                    <div class="span2">
                        <label for="resourceIsClue">
                            <g:message code="redactintrigue.resource.isClue" default="Clue"/>
                        </label>
                    </div>
                    <div class="span3">
                        <g:checkBox name="resourceIsClue" id="resourceIsClue"/>
                    </div>
                </div>
                <div class="row formRow hidden clueRow">
                    <div class="span1">
                        <label for="resourceTitle">
                            <g:message code="redactintrigue.resource.resourceTitle" default="Title"/>
                        </label>
                    </div>
                    <div class="span4">
                        <g:textField name="resourceTitle" id="resourceTitle" value=""/>
                    </div>

                    <!-- To delete ressource possessor
                    <div class="span1">
                        <label for="resourceRolePossessor">
                            <g:message code="redactintrigue.resource.resourceRolePossessor" default="Possesser"/>
                        </label>
                    </div>
                    <div class="span4">
                        <g:select name="resourceRolePossessor" id="resourceRolePossessor" from="${plotInstance.roles}"
                                  optionKey="id" optionValue="code"/>
                    </div>
                     End test -->

                </div>
                %{--<div class="row formRow hidden clueRow">--}%
                    %{--<div class="span1">--}%
                        %{--<label for="resourceRoleFrom">--}%
                            %{--<g:message code="redactintrigue.resource.resourceRoleFrom" default="From Role"/>--}%
                        %{--</label>--}%
                    %{--</div>--}%

                    %{--<div class="span4">--}%
                        %{--<g:select name="resourceRoleFrom" id="resourceRoleFrom" from="${plotInstance.roles}"--}%
                                  %{--optionKey="id" optionValue="code"/>--}%
                    %{--</div>--}%
                    %{--<div class="span1">--}%
                        %{--<label for="resourceRoleTo">--}%
                            %{--<g:message code="redactintrigue.resource.resourceRoleTo" default="To Role"/>--}%
                        %{--</label>--}%
                    %{--</div>--}%

                    %{--<div class="span4">--}%
                        %{--<g:select name="resourceRoleTo" id="resourceRoleTo" from="${plotInstance.roles}"--}%
                                  %{--optionKey="id" optionValue="code"/>--}%
                    %{--</div>--}%
                %{--</div>--}%

%{--texte du role from--}%
                <div class="row formRow text-center hidden clueRow">
                    <label for="resourceRoleFrom">
                        <g:message code="redactintrigue.resource.resourceRoleFrom" default="Role from"/>
                    </label>
                </div>
                <div class="fullScreenEditable hidden clueRow">
                    <g:render template="dropdownButtons" />

                    <!-- Editor -->
                    <div id="clueRichTextEditor" contenteditable="true" class="text-left richTextEditor" onblur="saveCarretPos($(this).attr('id'))">
                    </div>
                </div>

                %{--texte du role to--}%
                <div class="row formRow text-center hidden clueRow">
                    <label for="resourceRoleTo">
                        <g:message code="redactintrigue.resource.resourceRoleTo" default="Role to"/>
                    </label>
                </div>
                <div class="fullScreenEditable hidden clueRow">
                    <g:render template="dropdownButtons" />

                    <!-- Editor -->
                    <div id="clueRichTextEditor" contenteditable="true" class="text-left richTextEditor" onblur="saveCarretPos($(this).attr('id'))">
                    </div>
                </div>


                <div class="row formRow text-center hidden clueRow">
                    <label for="resourceDescription">
                        <g:message code="redactintrigue.resource.resourceDescription" default="Description"/>
                    </label>
                </div>
                <div class="fullScreenEditable hidden clueRow">
                    <g:render template="dropdownButtons" />

                    <!-- Editor -->
                    <div id="clueRichTextEditor" contenteditable="true" class="text-left richTextEditor" onblur="saveCarretPos($(this).attr('id'))">
                    </div>
                </div>

                <div id="resourceTagsModal" class="modal hide fade tags-modal" tabindex="-1">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">×</button>

                        <h3 id="myModalLabel">
                            <g:message code="redactintrigue.resource.tags" default="Tags"/>
                        </h3>
                        <input class="input-medium search-query" data-content="resourceTags"
                               placeholder="<g:message code="redactintrigue.generalDescription.search" default="Search..."/>"/>
                    </div>

                    <div class="modal-body">
                        <ul class="resourceTags">
                            <g:each in="${resourceTagList}" var="resourceTagInstance">
                                <g:render template="resourceTagTree" model="[resourceTagInstance: resourceTagInstance, resource: null]"/>
                            </g:each>
                        </ul>
                    </div>

                    <div class="modal-footer">
                        <button class="btn" data-dismiss="modal">Ok</button>
                    </div>
                </div>
                <g:plotOwner idOwner="${plotInstance.user.id}" lvlright="${right.MINTRIGUEMODIFY.value()}" lvlrightAdmin="${right.INTRIGUEMODIFY.value()}">
                    <input type="button" name="Insert" value="Insert" class="btn btn-primary insertResource"/>
                </g:plotOwner>
            </form>
        </div>

    <div id="bestResourceModal" class="modal hide fade tags-modal" tabindex="-1">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal">×</button>
            <h3>
                <g:message code="redactintrigue.resource.bestResource" default="Best Resources"/>
            </h3>
        </div>

        <div id="modalBestResource" class="modal-body">
            <div class="span1 ressLoader" style="display:none;"></div>
            <select class="form-control" id="selectUniversResource" data-url="<g:createLink controller="GenericResource" action="getBestResources"/>" name="univerTag">
                <option value="" disabled selected style='display:none;'><g:message code="redactintrigue.selectunivers" default="Choose univer ..."/></option>
                <g:each in="${plotUniversList}" status="i" var="plotUniversInstance">
                    <option value="${plotUniversInstance.name}">${plotUniversInstance.name}</option>
                </g:each>
            </select>
            <div class="span1 ressLoader" style="display:none; float : right;"><g:img dir="images/substitution" file="loader.gif" width="30" height="30"/></div>
            <br>
            <ul id="listContainerResource" class="unstyled">
                <li id="templateBestResource" class="hidden">TEMPLATE</li>
            </ul>
        </div>

        <div class="modal-footer">
            <button class="btn" data-dismiss="modal">Ok</button>
        </div>
    </div>

        <g:each in="${plotInstance.genericResources}" var="resource">
            <div class="tab-pane" id="resource_${resource.id}">
                <form name="updateResource_${resource.id}" data-url="<g:createLink controller="GenericResource" action="Update" id="${resource.id}"/>">
                    <g:hiddenField name="id" value="${resource.id}"/>
                    <g:hiddenField name="resourceComment" class="commentContent" value=""/>
                    <g:hiddenField name="resourceRoleFrom" class="fromRoleTextContent" value=""/>
                    <g:hiddenField name="resourceRoleTo" class="toRoleTextContent" value=""/>
                    <g:hiddenField name="resourceDescription" class="descriptionContent" value=""/>
                    <input type="hidden" name="plotId" id="plotId" value="${plotInstance?.id}"/>
                    <div class="row formRow">
                        <div class="span1">
                            <label for="resourceCode">
                                <g:message code="redactintrigue.resource.resourceCode" default="Name"/>
                            </label>
                        </div>

                        <div class="span8">
                            <g:textField name="resourceCode" id="resourceCode" value="${resource.code}" required=""/>
                        </div>
                    </div>

                    <div class="row formRow">
                        <div class="span1">
                            <label>
                                <g:message code="redactintrigue.generalDescription.tags" default="Tags"/>
                            </label>
                        </div>

                        <div class="span4">
                            <a href="#resourceTagsModal_${resource.id}" class="btn" data-toggle="modal">
                                <g:message code="redactintrigue.resource.chooseTags" default="Choose tags"/>
                            </a>
                            <button data-target="#bestResourceModal" type="button" data-form="updateResource_${resource.id}" class="btnBestResource bestResource" data-toggle="modal"><i class="btnBestResource img-circle" ></i></button>
                        </div>

                        <!-- Test for generic ressource possessor -->
                        <div class="span1">
                            <label for="resourceRolePossessor">
                                <g:message code="redactintrigue.resource.resourceRolePossessor" default="Possesser"/>
                            </label>
                        </div>
                        <div class="span4">
                            <g:select name="resourceRolePossessor" id="resourceRolePossessor" from="${plotInstance.roles}"
                                      optionKey="id" optionValue="code" noSelection="${['':'Orga']}"
                                      value="${resource.possessedByRole?.id}"/>
                        </div>
                        <!-- End test -->

                    </div>


                    <div class="row formRow">
                        <div class="span3">
                            <label>
                                <g:message code="redactintrigue.objecttype.resourcetype" default="Resource type : "/>
                            </label>
                        </div>
                        <div class="span1">
                            <label for="resourceObjectToDefine">
                                <g:message code="redactintrigue.objecttype.toDefine" default="To define"/>
                            </label>
                        </div>
                        <div class="span1">
                            <g:radio name="resourceObject" id="resourceObjectToDefine" value="0" checked="${resource?.objectType?.id == 0}"/>
                        </div>
                        <div class="span1">
                            <label for="resourceObjectInGame">
                                <g:message code="redactintrigue.objecttype.ingame" default="In game"/>
                            </label>
                        </div>
                        <div class="span1">
                            <g:radio name="resourceObject" id="resourceObjectInGame" value="1"  checked="${resource?.objectType?.id == 1}" />
                        </div>
                        <div class="span1">
                            <label for="resourceObjectSimulated">
                                <g:message code="redactintrigue.objecttype.simulated" default="Simulated"/>
                            </label>
                        </div>
                        <div class="span1">
                            <g:radio name="resourceObject" id="resourceObjectSimulated" value="2"  checked="${resource?.objectType?.id == 2}"/>
                        </div>
                        <div class="span1">
                            <label for="resourceObjectOffGame">
                                <g:message code="redactintrigue.objecttype.offgame" default="Off game"/>
                            </label>
                        </div>
                        <div class="span1">
                            <g:radio name="resourceObject" id="resourceObjectOffGame" value="3"  checked="${resource?.objectType?.id == 3}"/>
                        </div>
                    </div>

                    <div class="row formRow text-center">
                        <label for="resourceComment">
                            <g:message code="redactintrigue.resource.resourceComment" default="Comment"/>
                        </label>
                    </div>
                    <div class="fullScreenEditable">
                        <g:render template="dropdownButtons" />

                        <!-- Editor -->
                        <div id="resourceRichTextEditor${resource.id}" contenteditable="true" class="text-left richTextEditor" onblur="saveCarretPos($(this).attr('id'))">
                            ${resource.comment?.encodeAsHTML()}
                        </div>
                    </div>

                    <div class="row formRow">
                        <div class="span2">
                            <label for="resourceIsClue">
                                <g:message code="redactintrigue.resource.isClue" default="Clue"/>
                            </label>
                        </div>
                        <div class="span3">
                            <g:if test="${resource.title != null}">
                                <g:checkBox name="resourceIsClue" id="resourceIsClue" checked="true" />
                            </g:if>
                            <g:else>
                                <g:checkBox name="resourceIsClue" id="resourceIsClue"/>
                            </g:else>
                        </div>
                    </div>
                    <g:if test="${resource.title != null}">
                        <div class="row formRow clueRow">
                    </g:if>
                    <g:else>
                        <div class="row formRow clueRow hidden">
                    </g:else>
                            <div class="span1">
                                <label for="resourceTitle">
                                    <g:message code="redactintrigue.resource.resourceTitle" default="Title"/>
                                </label>
                            </div>
                            <div class="span4">
                                <g:textField name="resourceTitle" id="resourceTitle" value="${resource.title}"/>
                            </div>

                    <!-- To delete resource possessor
                            <div class="span1">
                                <label for="resourceRolePossessor">
                                    <g:message code="redactintrigue.resource.resourceRolePossessor" default="Possesser"/>
                                </label>
                            </div>
                            <div class="span4">
                                <g:select name="resourceRolePossessor" id="resourceRolePossessor" from="${plotInstance.roles}"
                                          optionKey="id" optionValue="code" value="${resource.possessedByRole?.id}"/>
                            </div>
                     End test -->
                        </div>

                    %{--<g:if test="${resource.title != null}">--}%
                        %{--<div class="row formRow clueRow">--}%
                    %{--</g:if>--}%
                    %{--<g:else>--}%
                        %{--<div class="row formRow clueRow hidden">--}%
                    %{--</g:else>--}%
                            %{--<div class="span1">--}%
                                %{--<label for="resourceRoleFrom">--}%
                                    %{--<g:message code="redactintrigue.resource.resourceRoleFrom" default="From Role"/>--}%
                                %{--</label>--}%
                            %{--</div>--}%
                            %{--<div class="span4">--}%
                                %{--<g:select name="resourceRoleFrom" id="resourceRoleFrom" from="${plotInstance.roles}"--}%
                                          %{--optionKey="id" optionValue="code" value="${resource.fromRole?.id}"/>--}%
                            %{--</div>--}%
                            %{--<div class="span1">--}%
                                %{--<label for="resourceRoleTo">--}%
                                    %{--<g:message code="redactintrigue.resource.resourceRoleTo" default="To Role"/>--}%
                                %{--</label>--}%
                            %{--</div>--}%
                            %{--<div class="span4">--}%
                                %{--<g:select name="resourceRoleTo" id="resourceRoleTo" from="${plotInstance.roles}"--}%
                                          %{--optionKey="id" optionValue="code" value="${resource.toRole?.id}"/>--}%
                            %{--</div>--}%
                        %{--</div>--}%

                    <g:if test="${resource.title != null}">
                        <div class="row formRow text-center clueRow">
                    </g:if>
                    <g:else>
                        <div class="row formRow text-center clueRow hidden">
                    </g:else>
                    <label for="resourceRoleFrom">
                        <g:message code="redactintrigue.resource.resourceRoleFrom" default="Role from"/>
                    </label>
                </div>
                    <g:if test="${resource.title != null}">
                        <div class="fullScreenEditable clueRow">
                    </g:if>
                    <g:else>
                        <div class="fullScreenEditable clueRow hidden">
                    </g:else>
                    <g:render template="dropdownButtons" />

                <!-- Editor -->
                    <div id="clueRichTextEditor1" contenteditable="true" class="text-left richTextEditor" onblur="saveCarretPos($(this).attr('id'))">
                        ${resource.fromRoleText?.encodeAsHTML()}
                    </div>
                </div>

                    <g:if test="${resource.title != null}">
                        <div class="row formRow text-center clueRow">
                    </g:if>
                    <g:else>
                        <div class="row formRow text-center clueRow hidden">
                    </g:else>
                    <label for="resourceRoleTo">
                        <g:message code="redactintrigue.resource.resourceRoleTo" default="Role to"/>
                    </label>
                </div>
                    <g:if test="${resource.title != null}">
                        <div class="fullScreenEditable clueRow">
                    </g:if>
                    <g:else>
                        <div class="fullScreenEditable clueRow hidden">
                    </g:else>
                    <g:render template="dropdownButtons" />

                <!-- Editor -->
                    <div id="clueRichTextEditor2" contenteditable="true" class="text-left richTextEditor" onblur="saveCarretPos($(this).attr('id'))">
                        ${resource.toRoleText?.encodeAsHTML()}
                    </div>
                </div>

                        <g:if test="${resource.title != null}">
                            <div class="row formRow text-center clueRow">
                        </g:if>
                        <g:else>
                            <div class="row formRow text-center clueRow hidden">
                        </g:else>
                            <label for="resourceDescription">
                                <g:message code="redactintrigue.resource.resourceDescription" default="Description"/>
                            </label>
                        </div>

                        <g:if test="${resource.title != null}">
                            <div class="fullScreenEditable clueRow">
                        </g:if>
                        <g:else>
                            <div class="fullScreenEditable clueRow hidden">
                        </g:else>
                            <g:render template="dropdownButtons" />

                            <!-- Editor -->
                            <div id="clueRichTextEditor${resource.id}" contenteditable="true" class="text-left richTextEditor" onblur="saveCarretPos($(this).attr('id'))">
                                ${resource.description?.encodeAsHTML()}
                            </div>
                        </div>

                    <div id="resourceTagsModal_${resource.id}" class="modal hide fade tags-modal" tabindex="-1">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">×</button>

                            <h3 id="myModalLabel${resource.id}">
                                <g:message code="redactintrigue.resource.tags" default="Tags"/>
                            </h3>
                            <input class="input-medium search-query" data-content="resourceTags${resource.id}"
                                   placeholder="<g:message code="redactintrigue.generalDescription.search" default="Search..."/>"/>
                            <button type="button" class="btn btn-primary modifyTag push">
                                <g:message code="redactintrigue.generalDescription.validatedTags" default="Validated tags"/>
                            </button>
                        </div>

                        <div class="modal-body">
                            <ul class="resourceTags${resource.id}">
                                <g:each in="${resourceTagList}" var="resourceTagInstance">
                                    <g:render template="resourceTagTree" model="[resourceTagInstance: resourceTagInstance, resource: resource]"/>
                                </g:each>
                            </ul>
                        </div>

                        <div class="modal-footer">
                            <button class="btn" data-dismiss="modal">Ok</button>
                        </div>
                    </div>
                    <g:plotOwner idOwner="${plotInstance.user.id}" lvlright="${right.MINTRIGUEMODIFY.value()}" lvlrightAdmin="${right.INTRIGUEMODIFY.value()}">
                        <input type="button" name="Update" data-id="${resource.id}" value="Update" class="btn btn-primary updateResource"/>
                    </g:plotOwner>
                </form>
            </div>
        </g:each>
    </div>
</div>