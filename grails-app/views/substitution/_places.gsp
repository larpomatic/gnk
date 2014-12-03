<div class="row-fluid">
    <div class="span4"><legend>Lieux</legend></div>

    <div class="span1"><span class="badge badge-important" id="placesPercentage">0 %</span></div>

    <div class="span2"><a id="runSubPlacesButton" class="btn btn-info"><i class="icon-play icon-white"></i> LANCER</a>
    </div>

    <div class="span1" id="placesLoader" style="display: none; float : right;"><g:img dir="images/substitution"
                                                                                      file="loader.gif" width="30"
                                                                                      height="30"/></div>
</div>

<div id="subPlacesAlertContainer">
</div>

<table id="placeTable" class="table table-striped">
    <thead>
    <tr class="upper">
        <th style="text-align: center;">#</th>
        <th>code</th>
        <th>Plot Name</th>
        <th>tags</th>
        <th>comment</th>
        <th>nom</th>
        <th style="text-align: center;">
            A RELANCER <input id="restartPlaceAll" type="checkbox" disabled="true" style="float: right;">
        </th>
    </tr>
    </thead>
    <tbody>
    <g:each status="i" in="${placeList}" var="place">
        <tr id="place${place.id}_plot${place.plotId}">
            <!-- # -->
            <td style="text-align: center;">${i + 1}</td>
            <!-- Code -->
            <td>${place.code}</td>
            <!-- Plot Name -->
            <td>${place.plotName}</td>
            <!-- Tags -->
            <td>
                <ul class="unstyled">
                    <g:each status="j" in="${place.tagList}" var="tag">
                        <li><strong class="cap">${tag.value.encodeAsHTML()}</strong>
                            (<span class="cap">${tag.family.encodeAsHTML()}</span> / ${tag.weight.encodeAsHTML()})</li>
                    </g:each>
                </ul>
            </td>
            <!-- Comment -->
            <td>${place.comment.encodeAsHTML()}</td>
            <!-- Place -->
            <td class="place">
                <input type="radio" name="${place}Radio" id="generatedPlace" checked><select class="bold"
                                                                                             disabled="true"
                                                                                             isEmpty="true"></select><br>
                <input type="radio" name="${place}Radio" id="writtenPlace"><input type="text" id="placeWritten"
                                                                                  class="written">
                %{--<input type="radio" name="${place}Radio" id="manualPlace" ><input type="text" id="placeManual" class="written" placeholder="Add a custom place">--}%
                <a class="btn unban" title="Débannir" disabled="true"><i class="icon-arrow-left"></i></a>
            </td>
            <!-- Restart place -->
            <td class="restartPlace" style="text-align: center;">
                <input type="checkbox" name="option" value="unlock" disabled="true">
            </td>
        </tr>
    </g:each>
    <tbody>
</table>

<!-- Modal Views -->
<!--g:render template="modalViewPlaces" /-->

<g:javascript src="substitution/subPlaces.js"/>

<script type="text/javascript">
    $(document).ready(function () {
        // PlacesJSON
        placesJSON = initPlacesJSON();

        isSubPlacesRunning = false;

        initPlacesEvents("${g.createLink(controller:'substitution', action:'getSubPlaces')}")
    });

    function initPlacesJSON() {
        var jsonObject = new Object();
        // Universe
        jsonObject.universe = "${gnInfo.universe}";

        // BEGIN Places LOOP
        var placeArray = new Array();
        <g:each status="i" in="${placeList}" var="place">
        var place = new Object();
        // Gn id
        place.gnId = "${place.id}"
        // Gn plot id
        place.gnPlotId = "${place.plotId}"
        // Gn plot
        place.plotName = "${place.plotName}"
        // HTML id
        place.htmlId = "place${place.id}_plot${place.plotId}"
        // Code
        place.code = "${place.code}"
        // BEGIN Tags LOOP
        var tagArray = new Array();
        <g:each status="j" in="${place.tagList}" var="tag">
        var tag = new Object();
        tag.value = "${tag.value}";
        tag.family = "${tag.family}";
        tag.weight = "${tag.weight}";
        tagArray.push(tag);
        </g:each>
        // END Tags LOOP
        if (tagArray.length > 0) {
            place.tags = tagArray;
        }
        placeArray.push(place);
        </g:each>
        // END Places LOOP

        jsonObject.places = placeArray;
        return jsonObject;
    }
</script>

