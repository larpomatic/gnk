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
            <g:plotOwner idOwner="${plotInstance.user.id}" lvlright="${right.MINTRIGUEMODIFY.value()}" lvlrightAdmin="${right.INTRIGUEMODIFY.value()}">
                <button data-toggle="confirmation-popout" data-placement="left" class="btn btn-danger"
                        title="Supprimer le lieu?"
                        data-url="<g:createLink controller="GenericPlace" action="Delete" id="${genericPlace.id}"/>"
                        data-object="place" data-id="${genericPlace.id}">
                    <i class="icon-remove pull-right"></i>
                </button>
            </g:plotOwner>
        </li>
    </g:each>
</ul>

<div class="tab-content">
<div class="tab-pane active" id="newPlace">
    <form name="newPlaceForm" data-url="<g:createLink controller="GenericPlace" action="Save"/>">
        <input type="hidden" name="plotId" id="plotId" value="${plotInstance?.id}"/>
        <g:hiddenField name="placeDescription" class="descriptionContent" value=""/>
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
                <button data-target="#bestPlaceModal" id="newbestPlace" type="button" class="btnBestPlace" data-toggle="modal"><i class="btnBestPlace img-circle" ></i></button>
            </div>
        </div>

        <div class="row formRow">
            <div class="span3">
                <label>
                    <g:message code="redactintrigue.objecttype.placetype" default="Place type : "/>
                </label>
            </div>

            <div class="span1">
                <label for="placeObjectToDefine">
                    <g:message code="redactintrigue.objecttype.toDefine" default="To define"/>
                </label>
            </div>

            <div class="span1">
                <g:radio name="placeObject" id="placeObjectToDefine" value="0" checked="checked"/>
            </div>

            <div class="span1">
                <label for="placeObjectInGame">
                    <g:message code="redactintrigue.objecttype.ingame" default="In game"/>
                </label>
            </div>

            <div class="span1">
                <g:radio name="placeObject" id="placeObjectInGame" value="1"/>
            </div>

            <div class="span1">
                <label for="placeObjectSimulated">
                    <g:message code="redactintrigue.objecttype.simulated" default="Simulated"/>
                </label>
            </div>

            <div class="span1">
                <g:radio name="placeObject" id="placeObjectSimulated" value="2"/>
            </div>

            <div class="span1">
                <label for="placeObjectOffGame">
                    <g:message code="redactintrigue.objecttype.offgame" default="Off game"/>
                </label>
            </div>

            <div class="span1">
                <g:radio name="placeObject" id="placeObjectOffGame" value="3"/>
            </div>
        </div>
        <div class="row formRow">
            <div class="span1">
                <label for="placeConstantForm">
                    <g:message code="redactintrigue.generalDescription.associatedConstant" default="Associated constant"/>
                </label>
            </div>
            <div class="span4">
                <g:select name="placeConstantForm" id="placeConstantForm" from="${gnConstantPlaceList}"
                          optionKey="id" required="" optionValue="name" noSelection="${['null':'']}" value="${place?.gnConstant?.id}"/>
            </div>
        </div>
        <div class="row formRow text-center">
            <label for="placeDescription">
                <g:message code="redactintrigue.place.placeDescription" default="Description"/>
            </label>
        </div>

        <div class="fullScreenEditable">
            <g:render template="dropdownButtons"/>

            <!-- Editor -->
            <div id="placeRichTextEditor" contenteditable="true" class="text-left richTextEditor"
                 onblur="saveCarretPos($(this).attr('id'))">
            </div>
        </div>
        %{--<g:textArea name="placeDescription" id="placeDescription" value="" rows="5" cols="100"/>--}%
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
        <g:plotOwner idOwner="${plotInstance.user.id}" lvlright="${right.MINTRIGUEMODIFY.value()}" lvlrightAdmin="${right.INTRIGUEMODIFY.value()}">
            <input type="button" name="Insert" value="Insert" class="btn btn-primary insertPlace"/>
        </g:plotOwner>
    </form>
</div>

<div id="bestPlaceModal" class="modal hide fade tags-modal" tabindex="-1">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">×</button>
        <h3>
            <g:message code="redactintrigue.place.bestPlace" default="Best Places"/>
        </h3>
    </div>

    <div id="modalBestPlace" class="modal-body">
        <div class="span1 placeLoader" style="display:none;"></div>
        <select class="form-control" id="selectUnivers" data-url="<g:createLink controller="GenericPlace" action="getBestPlaces"/>" name="univerTag">
            <option value="" disabled selected style='display:none;'><g:message code="redactintrigue.selectunivers" default="Choose univer ..."/></option>
            <g:each in="${plotUniversList}" status="i" var="plotUniversInstance">
                <option value="${plotUniversInstance.name}">${plotUniversInstance.name}</option>
            </g:each>
        </select>
        <div class="span1 placeLoader" style="display:none; float : right;"><g:img dir="images/substitution" file="loader.gif" width="30" height="30"/></div>
        <br>
        <ul id="listContainer" class="unstyled">
            <li id="templateBest" class="hidden">TEMPLATE</li>
        </ul>
    </div>

    <div class="modal-footer">
        <button class="btn" data-dismiss="modal">Ok</button>
    </div>
</div>

<g:each in="${plotInstance.genericPlaces}" var="place">
    <div class="tab-pane" id="place_${place.id}">
        <form name="updatePlace_${place.id}"
              data-url="<g:createLink controller="GenericPlace" action="Update" id="${place.id}"/>">
            <g:hiddenField name="id" value="${place.id}"/>
            <g:hiddenField name="placeDescription" class="descriptionContent" value=""/>
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
                    <button data-target="#bestPlaceModal" type="button" data-form="updatePlace_${place.id}" class="btnBestPlace bestPlace" data-toggle="modal"><i class="btnBestPlace img-circle" ></i></button>
                </div>
            </div>

            <div class="row formRow">
                <div class="span3">
                    <label>
                        <g:message code="redactintrigue.objecttype.placetype" default="Place type : "/>
                    </label>
                </div>

                <div class="span1">
                    <label for="placeObjectToDefine">
                        <g:message code="redactintrigue.objecttype.toDefine" default="To define"/>
                    </label>
                </div>

                <div class="span1">
                    <g:radio name="placeObject" id="placeObjectToDefine" value="0"
                             checked="${place?.objectType?.id == 0}"/>
                </div>

                <div class="span1">
                    <label for="placeObjectInGame">
                        <g:message code="redactintrigue.objecttype.ingame" default="In game"/>
                    </label>
                </div>

                <div class="span1">
                    <g:radio name="placeObject" id="placeObjectInGame" value="1"
                             checked="${place?.objectType?.id == 1}"/>
                </div>

                <div class="span1">
                    <label for="placeObjectSimulated">
                        <g:message code="redactintrigue.objecttype.simulated" default="Simulated"/>
                    </label>
                </div>

                <div class="span1">
                    <g:radio name="placeObject" id="placeObjectSimulated" value="2"
                             checked="${place?.objectType?.id == 2}"/>
                </div>

                <div class="span1">
                    <label for="placeObjectOffGame">
                        <g:message code="redactintrigue.objecttype.offgame" default="Off game"/>
                    </label>
                </div>
                <div class="span1">

                    <g:radio name="placeObject" id="placeObjectOffGame" value="3"
                             checked="${place?.objectType?.id == 3}"/>
                </div>
            </div>
            <div class="row formRow">
                <div class="span1">
                    <label for="placeConstantForm">
                        <g:message code="redactintrigue.generalDescription.associatedConstant" default="Associated constant"/>
                    </label>
                </div>
                <div class="span4">
                    <g:select name="placeConstantForm" id="placeConstantForm" from="${gnConstantPlaceList}"
                              optionKey="id" required="" optionValue="name" noSelection="${['null':'']}" value="${place?.gnConstant?.id}"/>
                </div>
            </div>
            <div class="row formRow text-center">
                <label for="placeDescription">
                    <g:message code="redactintrigue.place.placeDescription" default="Description"/>
                </label>
            </div>
            %{--<g:textArea name="placeDescription" id="placeDescription" value="${place.comment}" rows="5" cols="100"/>--}%
            <div class="fullScreenEditable">
                <g:render template="dropdownButtons"/>

                <!-- Editor -->
                <div id="placeRichTextEditor${place.id}" contenteditable="true" class="text-left richTextEditor"
                     onblur="saveCarretPos($(this).attr('id'))">
                    ${place.comment?.encodeAsHTML()}
                </div>
            </div>

            <div id="placeTagsModal_${place.id}" class="modal hide fade tags-modal" tabindex="-1">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">×</button>

                    <h3 id="myModalLabel${place.id}">
                        <g:message code="redactintrigue.place.tags" default="Tags"/>
                    </h3>
                    <input class="input-medium search-query" data-content="placeTags${place.id}"
                           placeholder="<g:message code="redactintrigue.generalDescription.search"
                                                   default="Search..."/>"/>
                    <button type="button" class="btn btn-primary modifyTag push">
                        <g:message code="redactintrigue.generalDescription.validatedTags" default="Validated tags"/>
                    </button>
                </div>

                <div class="modal-body">
                    <ul class="placeTags${place.id}">
                        <g:each in="${placeTagList}" var="placeTagInstance">
                            <g:render template="placeTagTree"
                                      model="[placeTagInstance: placeTagInstance, place: place]"/>
                        </g:each>
                    </ul>
                </div>

                <div class="modal-footer">
                    <button class="btn" data-dismiss="modal">Ok</button>
                </div>
            </div>
            <g:plotOwner idOwner="${plotInstance.user.id}" lvlright="${right.MINTRIGUEMODIFY.value()}" lvlrightAdmin="${right.INTRIGUEMODIFY.value()}">
                <input type="button" name="Update" data-id="${place.id}" value="Update"
                       class="btn btn-primary updatePlace"/>
            </g:plotOwner>
        </form>
    </div>
</g:each>
</div>
</div>