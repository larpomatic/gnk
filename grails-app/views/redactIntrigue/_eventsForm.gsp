<%@ page import="org.gnk.resplacetime.Event" %>
<div class="tabbable tabs-left eventScreen">
    <ul class="nav nav-tabs leftUl">
        <li class="active leftMenuList">
            <a href="#newEvent" data-toggle="tab" class="addEvent">
                <g:message code="redactintrigue.event.addEvent" default="New event"/>
            </a>
        </li>
        <g:each in="${Event.findAllByPlot(plotInstance, [sort:'timing',order:'asc'])}" status="i5" var="event">
            <li class="leftMenuList">
                <a href="#event_${event.id}" data-toggle="tab">
                    ${event.name}
                </a>
                <button data-toggle="confirmation-popout" data-placement="left" class="btn btn-danger" title="Supprimer cet évenement?"
                        data-url="<g:createLink controller="Event" action="Delete" id="${event.id}"/>" data-object="event" data-id="${event.id}">
                    <i class="icon-remove pull-right"></i>
                </button>
            </li>
        </g:each>
    </ul>
    <div class="tab-content">
        <div class="tab-pane active" id="newEvent">
            <form name="newEventForm" data-url="">
                %{--<div style="margin:auto">--}%
                <g:hiddenField name="eventDescription" class="descriptionContent" value=""/>
                <div class="row formRow">
                    <div class="span1">
                        <label for="EventName">
                            <g:message code="redactintrigue.event.eventName" default="Name"/>
                        </label>
                    </div>

                    <div class="span8">
                        <g:textField name="EventName" id="EventName" value="" required=""/>
                    </div>

                    %{--<div class="span1">--}%
                        %{--<label for="eventDatetime">--}%
                            %{--<g:message code="redactintrigue.event.eventDatetime" default="Date and Time"/>--}%
                        %{--</label>--}%
                    %{--</div>--}%

                    %{--<div class="span4">--}%
                        %{--<div class="input-append date datetimepicker">--}%
                            %{--<input data-format="dd/MM/yyyy hh:mm" type="text" id="eventDatetime" name="eventDatetime"/>--}%
                            %{--<span class="add-on">--}%
                                %{--<i data-time-icon="icon-time" data-date-icon="icon-calendar">--}%
                                %{--</i>--}%
                            %{--</span>--}%
                        %{--</div>--}%
                    %{--</div>--}%
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
                            <g:message code="redactintrigue.event.eventDuration" default="Duration (min)"/>
                        </label>
                    </div>

                    <div class="span4">
                        <g:field type="number" name="EventDuration" id="EventDuration" value="" required=""/>
                    </div>
                    <div class="span1">
                        <label for="EventTiming">
                            <g:message code="redactintrigue.event.eventTiming" default="Timing (%)"/>
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

                <div class="fullScreenEditable">
                    <g:render template="dropdownButtons" />

                    <!-- Editor -->
                    <div id="eventRichTextEditor" contenteditable="true" class="text-left richTextEditor" onblur="saveCarretPos($(this).attr('id'))"
                         style="margin-top:15px; padding:5px; height:200px; overflow:auto; border:solid 1px #808080; -moz-border-radius:20px 0;
                         -webkit-border-radius:20px 0; border-radius:20px 0; margin-bottom: 10px;">

                    </div>
                </div>
                %{--</div>--}%
                <input type="button" name="Insert" value="Insert" class="btn btn-primary insertEvent"/>
            </form>
        </div>

    <g:each in="${plotInstance.events}" status="i4" var="event">
        <div class="tab-pane" id="event_${event.id}">
            <form name="updateEvent_${event.id}" data-url="<g:createLink controller="Event" action="Update" id="${event.id}"/>">
                <g:hiddenField name="id" value="${event.id}"/>
                <g:hiddenField name="eventDescription" class="descriptionContent" value=""/>
                <input type="hidden" name="plotId" id="plotId" value="${plotInstance?.id}"/>

                    %{--<div style="margin:auto">--}%
                <div class="row formRow">
                    <div class="span1">
                        <label for="EventName">
                            <g:message code="redactintrigue.event.eventName" default="Name"/>
                        </label>
                    </div>

                    <div class="span8">
                        <g:textField name="EventName" id="EventName" value="${event.name}" required=""/>
                    </div>

                    %{--<div class="span1">--}%
                        %{--<label for="eventDatetime">--}%
                            %{--<g:message code="redactintrigue.event.eventDatetime" default="Date and Time"/>--}%
                        %{--</label>--}%
                    %{--</div>--}%

                    %{--<div class="span4">--}%
                        %{--<div class="input-append date datetimepicker">--}%
                            %{--<input data-format="dd/MM/yyyy hh:mm" type="text" id="eventDatetime${event.id}" name="eventDatetime"--}%
                                   %{--value="${event.absoluteDay}/${event.absoluteMonth}/${event.absoluteYear} ${event.absoluteHour}:${event.absoluteMinute}"/>--}%
                            %{--<span class="add-on">--}%
                                %{--<i data-time-icon="icon-time" data-date-icon="icon-calendar">--}%
                                %{--</i>--}%
                            %{--</span>--}%
                        %{--</div>--}%
                    %{--</div>--}%
                </div>
                <div class="row formRow">
                    <div class="span1">
                        <label for="eventPublic">
                            <g:message code="redactintrigue.event.eventPublic" default="Public"/>
                        </label>
                    </div>

                    <div class="span4">
                        <g:checkBox name="eventPublic" id="eventPublic" value="${event.isPublic}"/>
                    </div>

                    <div class="span1">
                        <label for="eventPlanned">
                            <g:message code="redactintrigue.event.eventPlanned" default="Planned"/>
                        </label>
                    </div>

                    <div class="span4">
                        <g:checkBox name="eventPlanned" id="eventPlanned" value="${event.isPlanned}"/>
                    </div>
                </div>
                <div class="row formRow">
                    <div class="span1">
                        <label for="EventDuration">
                            <g:message code="redactintrigue.event.eventDuration" default="Duration (min)"/>
                        </label>
                    </div>

                    <div class="span4">
                        <g:field type="number" name="EventDuration" id="EventDuration" value="${event.duration}" required=""/>
                    </div>
                    <div class="span1">
                        <label for="EventTiming">
                            <g:message code="redactintrigue.event.eventTiming" default="Timing (%)"/>
                        </label>
                    </div>

                    <div class="span4">
                        <g:field type="number" name="EventTiming" id="EventTiming" value="${event.timing}" required=""/>
                    </div>
                </div>
                <div class="row formRow">
                    <div class="span1">
                        <label for="eventPlace">
                            <g:message code="redactintrigue.event.eventPlace" default="Place"/>
                        </label>
                    </div>

                    <div class="span4">
                        <g:select name="eventPlace" id="eventPlace" from="${plotInstance.genericPlaces}" value="${event.genericPlace?.id}"
                                  optionKey="id" required="" optionValue="code" noSelection="${['null':'']}"/>
                    </div>

                    <div class="span1">
                        <label for="eventPredecessor">
                            <g:message code="redactintrigue.event.eventPredecessor" default="Predecessor"/>
                        </label>
                    </div>

                    <div class="span4">
                        <g:select name="eventPredecessor" id="eventPredecessor" from="${plotInstance.events}" value="${event.eventPredecessor?.id}"
                                  optionKey="id"  required="" optionValue="name" noSelection="${['null':'']}"/>
                    </div>
                </div>

                <div class="row formRow text-center">
                    <label for="eventDescription">
                        <g:message code="redactintrigue.event.eventDescription" default="Description"/>
                    </label>
                </div>

                <div class="fullScreenEditable">
                    <g:render template="dropdownButtons" />

                    <!-- Editor -->
                    <div id="eventRichTextEditor${event.id}" contenteditable="true" class="text-left richTextEditor" onblur="saveCarretPos($(this).attr('id'))"
                         style="margin-top:15px; padding:5px; height:200px; overflow:auto; border:solid 1px #808080; -moz-border-radius:20px 0;
                         -webkit-border-radius:20px 0; border-radius:20px 0; margin-bottom: 10px;">
                        ${event.description.encodeAsHTML()}
                    </div>
                </div>
                %{--</div>--}%
                <input type="button" name="Update" data-id="${event.id}" value="Update" class="btn btn-primary updateEvent"/>
            </form>
        </div>
    </g:each>

    </div>
</div>