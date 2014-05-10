<div class="tabbable tabs-left resourceScreen">
    <ul class="nav nav-tabs leftUl">
        <li class="active leftMenuList">
            <a href="#newResource" data-toggle="tab" class="addResource">
                <g:message code="redactintrigue.resource.addResource" default="New object"/>
            </a>
        </li>
        <g:each in="${plotInstance.genericResources}" status="i5" var="genericResource">
            <li class="leftMenuList">
                <a href="#resource_${genericResource.id}" data-toggle="tab">
                    ${genericResource.code}
                </a>
                <button data-toggle="confirmation-popout" data-placement="left" class="btn btn-danger" title="Supprimer cet objet?"
                        data-url="<g:createLink controller="GenericResource" action="Delete" id="${genericResource.id}"/>" data-object="resource" data-id="${genericResource.id}">
                    <i class="icon-remove pull-right"></i>
                </button>
            </li>
        </g:each>
    </ul>

    <div class="tab-content">
        <div class="tab-pane active" id="newResource">
            <form name="newResourceForm" data-url="">
                %{--<div style="margin:auto">--}%
                <div class="row formRow">
                    <div class="span1">
                        <label for="resourceCode">
                            <g:message code="redactintrigue.resource.resourceCode" default="Name"/>
                        </label>
                    </div>

                    <div class="span4">
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
                    </div>

                    <div class="span1">
                        <label>
                            <g:message code="redactintrigue.resource.resourceClues" default="Clues"/>
                        </label>
                    </div>

                    <div class="span4">
                        <a href="#resourceCluesModal" class="btn" data-toggle="modal">
                            <g:message code="redactintrigue.resource.chooseClues" default="Choose clues"/>
                        </a>
                    </div>
                </div>

                <div class="row formRow text-center">
                    <label for="resourceDescription">
                        <g:message code="redactintrigue.resource.resourceDescription" default="Description"/>
                    </label>
                </div>
                <g:textArea name="resourceDescription" id="resourceDescription" value="" rows="5" cols="100"/>
                %{--</div>--}%

                <div id="resourceCluesModal" class="modal hide fade" tabindex="-1">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">×</button>

                        <h3>
                            <g:message code="redactintrigue.resource.resourceClues" default="Clues"/>
                        </h3>
                    </div>

                    <div class="modal-body">
                        <ul>

                        </ul>
                    </div>

                    <div class="modal-footer">
                        <button class="btn" data-dismiss="modal">Ok</button>
                    </div>
                </div>

                <div id="resourceTagsModal" class="modal hide fade" tabindex="-1">
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
                                <li class="modalLi" data-name="${resourceTagInstance.name.toLowerCase()}">
                                    <label>
                                        <g:checkBox name="resourceTags_${resourceTagInstance.id}" id="resourceTags_${resourceTagInstance.id}"/>
                                        ${fieldValue(bean: resourceTagInstance, field: "name")}
                                    </label>
                                </li>
                            </g:each>
                        </ul>
                    </div>

                    <div class="modal-footer">
                        <button class="btn" data-dismiss="modal">Ok</button>
                    </div>
                </div>
                <input type="button" name="Insert" value="Insert" class="btn btn-primary insertResource"/>
            </form>
        </div>

        <g:each in="${plotInstance.genericResources}" var="resource">
            <div class="tab-pane" id="resource_${resource.id}">
                <form name="updateResource_${resource.id}" data-url="<g:createLink controller="GenericResource" action="Update" id="${resource.id}"/>">
                    <g:hiddenField name="id" value="${resource.id}"/>
                    <input type="hidden" name="plotId" id="plotId" value="${plotInstance?.id}"/>
                    %{--<div style="margin:auto">--}%
                    <div class="row formRow">
                        <div class="span1">
                            <label for="resourceCode">
                                <g:message code="redactintrigue.resource.resourceCode" default="Name"/>
                            </label>
                        </div>

                        <div class="span4">
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
                        </div>

                        <div class="span1">
                            <label>
                                <g:message code="redactintrigue.resource.resourceClues" default="Clues"/>
                            </label>
                        </div>

                        <div class="span4">
                            <a href="#resourceCluesModal_${resource.id}" class="btn" data-toggle="modal">
                                <g:message code="redactintrigue.resource.chooseClues" default="Choose clues"/>
                            </a>
                        </div>
                    </div>

                    <div class="row formRow text-center">
                        <label for="resourceDescription">
                            <g:message code="redactintrigue.resource.resourceDescription" default="Description"/>
                        </label>
                    </div>
                    <g:textArea name="resourceDescription" id="resourceDescription" value="${resource.comment}" rows="5" cols="100"/>
                    %{--</div>--}%

                    <div id="resourceCluesModal_${resource.id}" class="modal hide fade" tabindex="-1">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">×</button>

                            <h3>
                                <g:message code="redactintrigue.resource.resourceClues" default="Clues"/>
                            </h3>
                        </div>

                        <div class="modal-body">
                            <ul>

                            </ul>
                        </div>

                        <div class="modal-footer">
                            <button class="btn" data-dismiss="modal">Ok</button>
                        </div>
                    </div>

                    <div id="resourceTagsModal_${resource.id}" class="modal hide fade" tabindex="-1">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">×</button>

                            <h3 id="myModalLabel${resource.id}">
                                <g:message code="redactintrigue.resource.tags" default="Tags"/>
                            </h3>
                            <input class="input-medium search-query" data-content="resourceTags${resource.id}"
                                   placeholder="<g:message code="redactintrigue.generalDescription.search" default="Search..."/>"/>
                        </div>

                        <div class="modal-body">
                            <ul class="resourceTags">
                                <g:each in="${resourceTagList}" var="resourceTagInstance">
                                    <li class="modalLi" data-name="${resourceTagInstance.name.toLowerCase()}">
                                        <label>
                                            <g:checkBox name="resourceTags_${resourceTagInstance.id}" id="resourceTags_${resourceTagInstance.id}"
                                                        checked="${resource.hasGenericResourceTag(resourceTagInstance)}"/>
                                            ${fieldValue(bean: resourceTagInstance, field: "name")}
                                        </label>
                                    </li>
                                </g:each>
                            </ul>
                        </div>

                        <div class="modal-footer">
                            <button class="btn" data-dismiss="modal">Ok</button>
                        </div>
                    </div>
                    <input type="button" name="Update" data-id="${resource.id}" value="Update" class="btn btn-primary updateResource"/>
                </form>
            </div>
        </g:each>
    </div>
</div>