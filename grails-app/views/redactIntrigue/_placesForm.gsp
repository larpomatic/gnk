<div class="tabbable tabs-left placeScreen">
    <ul class="nav nav-tabs leftUl">
        <li class="active leftMenuList">
            <a href="#newPlace" data-toggle="tab" class="addPlace">
                <g:message code="redactintrigue.place.addPlace" default="New place"/>
            </a>
        </li>
        <g:each in="${plotInstance.genericPlaces}" status="i5" var="genericPlace">
            <li class="leftMenuList">
                <a href="#place_${genericPlace.id}" data-toggle="tab">
                    ${genericPlace.code}
                </a>
                <button data-toggle="confirmation-popout" data-placement="left" class="btn btn-danger" title="Supprimer le lieu?"
                        data-url="<g:createLink controller="GenericPlace" action="Delete" id="${genericPlace.id}"/>" data-object="place" data-id="${genericPlace.id}">
                    <i class="icon-remove pull-right"></i>
                </button>
            </li>
        </g:each>
    </ul>

    <div class="tab-content">
        <div class="tab-pane active" id="newPlace">
            <form name="newPlaceForm" data-url="<g:createLink controller="GenericPlace" action="Save"/>">
                <input type="hidden" name="plotId" id="plotId" value="${plotInstance?.id}"/>
                <div class="row formRow">
                    <div class="span1">
                        <label for="placeCode">
                            <g:message code="redactintrigue.place.placeCode" default="Name"/>
                        </label>
                    </div>
                    <div class="span4">
                        <g:textField name="placeCode" id="placeCode" value="" required=""/>
                    </div>
                    <div class="span1">
                        <label>
                            <g:message code="redactintrigue.generalDescription.tags" default="Tags"/>
                        </label>
                    </div>
                    <div class="span4">
                        <a href="#placeTagsModal" class="btn" data-toggle="modal">
                            <g:message code="redactintrigue.place.chooseTags" default="Choose tags"/>
                        </a>
                    </div>
                </div>
                <div class="row formRow text-center">
                    <label for="placeDescription">
                        <g:message code="redactintrigue.place.placeDescription" default="Description"/>
                    </label>
                </div>
                <g:textArea name="placeDescription" id="placeDescription" value="" rows="5" cols="100"/>
                <div id="placeTagsModal" class="modal hide fade tags-modal" tabindex="-1">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">×</button>
                        <h3 id="myModalLabel">
                            <g:message code="redactintrigue.place.tags" default="Tags"/>
                        </h3>
                        <input class="input-medium search-query" data-content="placeTags"
                               placeholder="<g:message code="redactintrigue.generalDescription.search" default="Search..."/>"/>
                    </div>

                    <div class="modal-body">
                        <ul class="placeTags">
                            <g:each in="${placeTagList}" var="placeTagInstance">
                                <g:render template="placeTagTree" model="[placeTagInstance: placeTagInstance, place: null]"/>
                            </g:each>
                        </ul>
                    </div>

                    <div class="modal-footer">
                        <button class="btn" data-dismiss="modal">Ok</button>
                    </div>
                </div>
                <input type="button" name="Insert" value="Insert" class="btn btn-primary insertPlace"/>
            </form>
        </div>

        <g:each in="${plotInstance.genericPlaces}" var="place">
            <div class="tab-pane" id="place_${place.id}">
                <form name="updatePlace_${place.id}" data-url="<g:createLink controller="GenericPlace" action="Update" id="${place.id}"/>">
                    <g:hiddenField name="id" value="${place.id}"/>
                    <input type="hidden" name="plotId" id="plotId" value="${plotInstance?.id}"/>
                    <div class="row formRow">
                        <div class="span1">
                            <label for="placeCode">
                                <g:message code="redactintrigue.place.placeCode" default="Name"/>
                            </label>
                        </div>
                        <div class="span4">
                            <g:textField name="placeCode" id="placeCode" value="${place.code}" required=""/>
                        </div>
                        <div class="span1">
                            <label>
                                <g:message code="redactintrigue.generalDescription.tags" default="Tags"/>
                            </label>
                        </div>
                        <div class="span4">
                            <a href="#placeTagsModal_${place.id}" class="btn" data-toggle="modal">
                                <g:message code="redactintrigue.place.chooseTags" default="Choose tags"/>
                            </a>
                        </div>
                    </div>
                    <div class="row formRow text-center">
                        <label for="placeDescription">
                            <g:message code="redactintrigue.place.placeDescription" default="Description"/>
                        </label>
                    </div>
                    <g:textArea name="placeDescription" id="placeDescription" value="${place.comment}" rows="5" cols="100"/>
                    <div id="placeTagsModal_${place.id}" class="modal hide fade tags-modal" tabindex="-1">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">×</button>

                            <h3 id="myModalLabel${place.id}">
                                <g:message code="redactintrigue.place.tags" default="Tags"/>
                            </h3>
                            <input class="input-medium search-query" data-content="placeTags${place.id}"
                                   placeholder="<g:message code="redactintrigue.generalDescription.search" default="Search..."/>"/>
                        </div>

                        <div class="modal-body">
                            <ul class="placeTags${place.id}">
                                <g:each in="${placeTagList}" var="placeTagInstance">
                                    %{--<li class="modalLi" data-name="${placeTagInstance.name.toLowerCase()}">--}%
                                        %{--<label>--}%
                                            %{--<g:checkBox name="placeTags_${placeTagInstance.id}" id="placeTags_${placeTagInstance.id}"--}%
                                                        %{--checked="${place.hasGenericPlaceTag(placeTagInstance)}"/>--}%
                                            %{--${fieldValue(bean: placeTagInstance, field: "name")}--}%
                                        %{--</label>--}%
                                    %{--</li>--}%
                                    <g:render template="placeTagTree" model="[placeTagInstance: placeTagInstance, place: place]"/>
                                </g:each>
                            </ul>
                        </div>

                        <div class="modal-footer">
                            <button class="btn" data-dismiss="modal">Ok</button>
                        </div>
                    </div>
                    <input type="button" name="Update" data-id="${place.id}" value="Update" class="btn btn-primary updatePlace"/>
                </form>
            </div>
        </g:each>
    </div>
</div>