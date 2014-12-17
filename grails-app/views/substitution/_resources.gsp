<div class="row-fluid">
    <div class="span4"><legend>Ressources</legend></div>

    <div class="span1"><span class="badge badge-important" id="resourcesPercentage">0 %</span></div>

    <div class="span2"><a id="runSubResourcesButton" class="btn btn-info"><i class="icon-play icon-white"></i> LANCER
    </a></div>

    <div class="span1" id="resourcesLoader" style="display: none; float : right;"><g:img dir="images/substitution"
                                                                                         file="loader.gif" width="30"
                                                                                         height="30"/></div>
</div>

<div id="subResourcesAlertContainer">
</div>

<table id="resourceTable" class="table table-striped">
    <thead>
    <tr class="upper">
        <th style="text-align: center;">#</th>
        <th>Code</th>
        <th>Type</th>
        <th>Plot name</th>
        <th>tags</th>
        <th>comment</th>
        <th>Appartient à</th>
        <th>nom</th>
        <th style="text-align: center;">
            A RELANCER <input id="restartResourceAll" type="checkbox" disabled="true" style="float: right;">
        </th>
    </tr>
    </thead>
    <tbody>
    <g:each status="i" in="${resourceList}" var="resource">
        <tr id="res${resource.id}_plot${resource.plotId}">
            <!-- # -->
            <td style="text-align: center;">${i + 1}</td>
            <!-- Code - modal button -->
            <td>${resource.code}</td>
            <!-- objectType -->
            <td>${resource.objectType}</td>
            <!-- Plot name -->
            <td>${resource.plot}</td>
            <!-- Tags -->
            <td>
                <ul class="unstyled">
                    <g:each status="j" in="${resource.tagList}" var="tag">
                        <li><strong class="cap">${tag.value.encodeAsHTML()}</strong>
                            (<span class="cap">${tag.family.encodeAsHTML()}</span> / ${tag.weight.encodeAsHTML()})</li>
                    </g:each>
                </ul>
            </td>
            <!-- Comment -->
            <td>${resource.comment.encodeAsHTML()}</td>
            <!-- Character owner -->
            <td class="upper">
                <g:if test="${resource.character != null}">
                    CHAR - ${resource.character.id}
                </g:if> <g:else>
                    Info indisponible
                </g:else>
            </td>
            <!-- Resources -->
            <td class="resource">
                <select class="bold" disabled="disabled" isEmpty="true">
                </select>
                <input type="text" id="customResource" class="written" disabled="disabled"
                       placeholder="Add a custom resource">
                <button type="button" class="btn customRessource" data-plot-id="${resource.plotId}" data-id="${resource.id}" title="Create the custom resource"><i class="icon-arrow-left"></i></button>
            </td>
            <!-- Restart resource -->
            <td class="restartResource" style="text-align: center;">
                <input type="checkbox" name="option" value="unlock" disabled="disabled">
            </td>
        </tr>
    </g:each>
    <tbody>
</table>

<!-- Modal Views -->
<!--g:render template="modalViewResources" /-->

<g:javascript src="substitution/subResources.js"/>

<script type="text/javascript">
    $(document).ready(function () {
        // ResourcesJSON
        resourcesJSON = initResourcesJSON();

        isSubResourcesRunning = false;

        initResourcesEvents("${g.createLink(controller:'substitution', action:'getSubResources')}")

        $('.customRessource').click(function(){
            var input = $(this).prev();
            var content = input.val();
            var resourcesList = $("select", $(this).parent());
            var genericId = $(this).attr("data-id");
            var plotId = $(this).attr("data-plot-id");
            if (content != "") {
                var resource = new Object();
                // Gn id
                resource.gnId = ${gnInfo.dbId}
                // Gn plot id
                resource.gnPlotId = plotId;
                // HTML id
                resource.htmlId = "res"+genericId+"_plot" + plotId;
                // Code
                resource.code = content;
                // BEGIN Tags LOOP
                resource.tags = new Array();
                resourcesJSON.resources.push(resource);
                resourcesList.append($("<option>").attr("value", content).text(content));
                $(this).prev().val('');
            }
        });

        $('.resource #customResource').keyup(function () {

            var empty = true;

            $('.resource #customResource').each(function () {
                if ($(this).val().length == 0) {
                    empty = false;
                }
            });

            if (empty) {
                $('.resource a').each(function () {
                    $('.resource a').attr('disabled', 'disabled');
                });
            }
            else {
                $('.resource a').each(function () {
                    $('.resource a').removeAttr('disabled');
                });
            }
        });
    });

    function initResourcesJSON() {
        var jsonObject = new Object();
        // Universe
        jsonObject.universe = "${gnInfo.universe}";

        // BEGIN Resources LOOP
        var resourceArray = new Array();
        <g:each status="i" in="${resourceList}" var="resource">
        var resource = new Object();
        // Gn id
        resource.gnId = "${resource.id}"
        // Gn plot id
        resource.gnPlotId = "${resource.plotId}"
        // HTML id
        resource.htmlId = "res${resource.id}_plot${resource.plotId}"
        // Code
        resource.code = "${resource.code}"
        // BEGIN Tags LOOP
        var tagArray = new Array();
        <g:each status="j" in="${resource.tagList}" var="tag">
        var tag = new Object();
        tag.value = "${tag.value}";
        tag.family = "${tag.family}";
        tag.weight = "${tag.weight}";
        tagArray.push(tag);
        </g:each>
        // END Tags LOOP
        if (tagArray.length > 0) {
            resource.tags = tagArray;
        }
        resourceArray.push(resource);
        </g:each>
        // END Resources LOOP

        jsonObject.resources = resourceArray;
        return jsonObject;
    }
</script>

