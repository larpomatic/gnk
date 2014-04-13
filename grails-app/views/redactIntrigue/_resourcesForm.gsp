<div class="tabbable tabs-left resourceScreen">
    <ul class="nav nav-tabs" style="height:400pt;width:175pt;overflow-y: auto;overflow-x: hidden;">
        <li class="active leftMenuList">
            <a href="#newResource" data-toggle="tab" class="addResource">
                Nouvel objet
            </a>
        </li>

    </ul>

    <div class="tab-content">
        <div class="tab-pane active" id="newResource">
            <form name="newResourceForm" data-url="">
                <div style="margin:auto">
                    <div class="row formRow">
                        <div class="span1">
                            <label for="resourceCode">
                                <g:message code="redactintrigue.resource.resourceCode" default="code ressource"/>
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
                            <a href="#resourceTagsModal" class="btn" data-toggle="modal">Choisir les tags</a>
                        </div>
                        <div class="span1">
                            <label>
                                <g:message code="redactintrigue.resource.resourceClues" default="Indices"/>
                            </label>
                        </div>
                        <div class="span4">
                            <a href="#resourceCluesModal" class="btn" data-toggle="modal">Choisir les indices</a>
                        </div>
                    </div>
                    <div class="row formRow text-center">
                        <label for="resourceDescription">
                            <g:message code="redactintrigue.resource.resourceDescription" default="Commentaire"/>
                        </label>
                    </div>
                    <g:textArea name="resourceDescription" id="resourceDescription" value="" rows="5" cols="100"/>
                </div>

                <div id="resourceCluesModal" class="modal hide fade" tabindex="-1">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">×</button>

                        <h3>Indices</h3>
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

                        <h3 id="myModalLabel">Tags</h3>
                    </div>

                    <div class="modal-body">
                        <ul>

                        </ul>
                    </div>

                    <div class="modal-footer">
                        <button class="btn" data-dismiss="modal">Ok</button>
                    </div>
                </div>
                <input type="button" name="Insert" value="Insert" class="btn btn-primary insertResource"/>
            </form>
        </div>
    </div>
</div>