<div class="tabbable tabs-left placeScreen">
    <ul class="nav nav-tabs leftUl">
        <li class="active leftMenuList">
            <a href="#newPlace" data-toggle="tab" class="addPlace">
                <g:message code="redactintrigue.place.addPlace" default="New place"/>
            </a>
        </li>

    </ul>

    <div class="tab-content">
        <div class="tab-pane active" id="newPlace">
            <form name="newPlaceForm" data-url="">
                %{--<div style="margin:auto">--}%
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
                    <div class="row formRow">
                        <div class="span1">
                            <label>
                                <g:message code="redactintrigue.place.placePastScene" default="Past Scene"/>
                            </label>
                        </div>
                        <div class="span4">
                            <a href="#placePastScenesModal" class="btn" data-toggle="modal">
                                <g:message code="redactintrigue.place.choosePastscenes" default="Choose past scenes"/>
                            </a>
                        </div>
                        <div class="span1">
                            <label>
                                <g:message code="redactintrigue.place.placeEvent" default="Events"/>
                            </label>
                        </div>
                        <div class="span4">
                            <a href="#placeEventsModal" class="btn" data-toggle="modal">
                                <g:message code="redactintrigue.place.chooseEvents" default="Choose events"/>
                            </a>
                        </div>
                    </div>
                    <div class="row formRow text-center">
                        <label for="placeDescription">
                            <g:message code="redactintrigue.place.placeDescription" default="Description"/>
                        </label>
                    </div>
                    <g:textArea name="placeDescription" id="placeDescription" value="" rows="5" cols="100"/>
                %{--</div>--}%

                <div id="placeEventsModal" class="modal hide fade" tabindex="-1">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">×</button>
                        <h3>
                            <g:message code="redactintrigue.place.placeEvent" default="Events"/>
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
                <div id="placePastScenesModal" class="modal hide fade" tabindex="-1">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">×</button>
                        <h3>
                            <g:message code="redactintrigue.place.placePastScene" default="Past Scenes"/>
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
                <div id="placeTagsModal" class="modal hide fade" tabindex="-1">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">×</button>

                        <h3 id="myModalLabel">
                            <g:message code="redactintrigue.place.tags" default="Tags"/>
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
                <input type="button" name="Insert" value="Insert" class="btn btn-primary insertPlace"/>
            </form>
        </div>
    </div>
</div>