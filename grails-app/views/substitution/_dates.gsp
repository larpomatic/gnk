<div class="row-fluid">
    <div class="span4"><legend>Dates</legend></div>
    <div class="span1"><span class="badge badge-important" id="datesPercentage">0 %</span></div>
    <div class="span2"><a id="runSubDateButton" class="btn btn-info"><i class="icon-play icon-white"></i> LANCER</a></div>
    <div class="span1" id="datesLoader" style="display: none; float : right;"><g:img dir="images/substitution" file="loader.gif" width="30" height="30"/></div>
</div>

<div id="subDatesAlertContainer">
</div>

<table id="dateTable" class="table table-striped tablesorter">
    <thead>
    <tr class="upper">
        <th style="text-align: center;">#</th>
        <th>type</th>
        <th>code</th>
        <th>titre</th>
        <th>Intrigue</th>
        <th>Planifié</th>
        <th style="text-align: center;">date</th>
    </tr>
    </thead>
    <tbody>
    <g:each status="i" in="${pastsceneList}" var="pastscene">
        <tr id="pastscene${pastscene.id}_plot${pastscene.plotId}">
            <!-- # -->
            <td style="text-align: center;">${i + 1}</td>
            <!-- Type -->
            <th>Scène passée</th>
            <!-- Code - modal button -->
            <td><a href="#modalPas${i + 1}" role="button" class="btn" data-toggle="modal">PAS-${pastscene.id.encodeAsHTML()}_${pastscene.plotId.encodeAsHTML()}</a></td>
            <!-- Title -->
            <td>${pastscene.title.encodeAsHTML()}</td>
            <!-- Plot -->
            <td>${pastscene.plotName.encodeAsHTML()}</td>
            <!-- Is planned -->
            <td><span class="label label-info">OUI</span></td>
            <!-- Date -->
            <td style="text-align: center; font-weight: bold; font-size: 120%;" class="date" isEmpty="true"></td>
        </tr>
    </g:each>
    <g:each status="i" in="${eventList}" var="event">
        <tr id="event${event.id}_plot${event.plotId}">
            <!-- # -->
            <td style="text-align: center;">${pastsceneList.size() + i + 1}</td>
            <!-- Type -->
            <th>Événement</th>
            <!-- Code - modal button -->
            <td><a href="#modalEve${i + 1}" role="button" class="btn" data-toggle="modal">EVE-${event.id.encodeAsHTML()}_${event.plotId.encodeAsHTML()}</a></td>
            <!-- Title -->
            <td>${event.title.encodeAsHTML()}</td>
            <!-- Plot -->
            <td>${event.plotName.encodeAsHTML()}</td>
            <!-- Is planned -->
            <td>
                <g:if test="${event.isPlanned}">
                    <span class="label label-info">OUI</span>
                </g:if>
                <g:else>
                    <span class="label label-warning">NON</span>
                </g:else></td>
            <!-- Date -->
            <td style="text-align: center; font-weight: bold; font-size: 120%;" class="date" isEmpty="false" ></td>
        </tr>
    </g:each>
    <tbody>
</table>

<!-- Modal Views -->
<!--g:render template="modalViewDates" /-->

<g:javascript src="substitution/subDates.js" />

<script type="text/javascript">
    $(document).ready(function() {
        // DatesJSON
        datesJSON = initDatesJSON();

        isSubDatesRunning = false;

        // Run dates substitution
        //runDatesSubstitution("${g.createLink(controller:'substitution', action:'getSubDates')}");
        initDateList("${g.createLink(controller:'substitution', action:'getSubDates')}");
    });

    function initDatesJSON() {
        var jsonObject = new Object();

        // Begin gn date
        jsonObject.beginDate = "${gnInfo.t0Date.format('yyyy.MM.dd HH:mm')}";
        // Duration
        jsonObject.duration = "${gnInfo.duration}";

        // BEGIN Pastscenes LOOP
        var pastsceneArray = new Array();
        <g:each status="i" in="${pastsceneList}" var="pastscene">
        var pastscene = new Object();
        // Gn id
        pastscene.gnId = "${pastscene.id}";
        // Gn plot id
        pastscene.gnPlotId = "${pastscene.plotId}";
        // HTML id
        pastscene.htmlId = "pastscene${pastscene.id}_plot${pastscene.plotId}";
        // Time
        pastscene.relativeTime = "${pastscene.relativeTime}";
        pastscene.relativeTimeUnit = "${pastscene.relativeTimeUnit}";
        pastscene.absoluteYear = "${pastscene.absoluteYear}";
        pastscene.absoluteMonth = "${pastscene.absoluteMonth}";
        pastscene.absoluteDay = "${pastscene.absoluteDay}";
        pastscene.absoluteHour = "${pastscene.absoluteHour}";
        pastscene.absoluteMinute = "${pastscene.absoluteMin}";
        pastscene.isUpdate = "";
        pastsceneArray.push(pastscene);
        </g:each>
        // END Pastscenes LOOP
        jsonObject.pastscenes = pastsceneArray;

        // BEGIN Events LOOP
        var eventArray = new Array();
        <g:each status="i" in="${eventList}" var="event">
        var event = new Object();
        // Gn id
        event.gnId = "${event.id}";
        // Gn plot id
        event.gnPlotId = "${event.plotId}";
        // HTML id
        event.htmlId = "event${event.id}_plot${event.plotId}";
        eventArray.push(event);
        // Time
        event.timing = "${event.timing}";
        </g:each>
        // END Events LOOP
        jsonObject.events = eventArray;
        return jsonObject;
    }
</script>

