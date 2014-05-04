<div class="tabbable tabs-left eventScreen">
    <ul class="nav nav-tabs leftUl">
        <li class="active leftMenuList">
            <a href="#newEvent" data-toggle="tab" class="addEvent">
                <g:message code="redactintrigue.event.addEvent" default="New event"/>
            </a>
        </li>

    </ul>
    <div class="tab-content">
        <div class="tab-pane active" id="newEvent">
            <form name="newEventForm" data-url="">
                %{--<div style="margin:auto">--}%
                <div class="row formRow">
                    <div class="span1">
                        <label for="EventName">
                            <g:message code="redactintrigue.event.eventName" default="Name"/>
                        </label>
                    </div>

                    <div class="span4">
                        <g:textField name="EventName" id="EventName" value="" required=""/>
                    </div>

                    <div class="span1">
                        <label for="eventDatetime">
                            <g:message code="redactintrigue.event.eventDatetime" default="Date and Time"/>
                        </label>
                    </div>

                    <div class="span4">
                        <div class="input-append date datetimepicker">
                            <input data-format="dd/MM/yyyy hh:mm" type="text" id="eventDatetime" name="eventDatetime"/>
                            <span class="add-on">
                                <i data-time-icon="icon-time" data-date-icon="icon-calendar">
                                </i>
                            </span>
                        </div>
                    </div>
                </div>
                <div class="row formRow">
                    <div class="span1">
                        <label for="eventPublic">
                            <g:message code="redactintrigue.event.eventPublic" default="Public"/>
                        </label>
                    </div>

                    <div class="span4">
                        <g:checkBox name="eventPublic" id="eventPublic"/>
                    </div>

                    <div class="span1">
                        <label for="eventPlanned">
                            <g:message code="redactintrigue.event.eventPlanned" default="Planned"/>
                        </label>
                    </div>

                    <div class="span4">
                        <g:checkBox name="eventPlanned" id="eventPlanned"/>
                    </div>
                </div>
                <div class="row formRow">
                    <div class="span1">
                        <label for="EventDuration">
                            <g:message code="redactintrigue.event.eventDuration" default="Duration"/>
                        </label>
                    </div>

                    <div class="span4">
                        <g:field type="number" name="EventDuration" id="EventDuration" value="" required=""/>
                    </div>
                    <div class="span1">
                        <label for="EventTiming">
                            <g:message code="redactintrigue.event.eventTiming" default="Timing"/>
                        </label>
                    </div>

                    <div class="span4">
                        <g:field type="number" name="EventTiming" id="EventTiming" value="" required=""/>
                    </div>
                </div>
                <div class="row formRow">
                    <div class="span1">
                        <label for="eventPlace">
                            <g:message code="redactintrigue.event.eventPlace" default="Place"/>
                        </label>
                    </div>

                    <div class="span4">
                        <g:select name="eventPlace" id="eventPlace" from="${['Lieu1', 'Lieu2', 'Lieu3']}"
                                  keys="${['Lieu1', 'Lieu2', 'Lieu3']}" required=""/>
                    </div>

                    <div class="span1">
                        <label for="eventPredecessor">
                            <g:message code="redactintrigue.event.eventPredecessor" default="Predecessor"/>
                        </label>
                    </div>

                    <div class="span4">
                        <g:select name="eventPredecessor" id="eventPredecessor" from="${['Evenement1', 'Evenement2', 'Evenement3']}"
                                  keys="${['Evenement1', 'Evenement2', 'Evenement3']}" required=""/>
                    </div>
                </div>

                <div class="row formRow text-center">
                    <label for="eventDescription">
                        <g:message code="redactintrigue.event.eventDescription" default="Description"/>
                    </label>
                </div>
                <g:textArea name="eventDescription" id="eventDescription" value="" rows="5" cols="100"/>
                %{--</div>--}%
                <input type="button" name="Insert" value="Insert" class="btn btn-primary insertEvent"/>
            </form>
        </div>
    </div>
</div>