<div class="row-fluid">
    <div class="span4"><legend>Personnages</legend></div>

    <div class="span1"><span class="badge badge-important" id="charsPercentage">0 %</span></div>

    <div class="span2"><a id="runSubCharactersButton" class="btn btn-info"><i class="icon-play icon-white"></i> LANCER
    </a></div>

    <div class="span1" id="charsLoader" style="display: none; float : right;"><g:img dir="images/substitution"
                                                                                     file="loader.gif" width="30"
                                                                                     height="30"/></div>
</div>

<div id="subCharsAlertContainer">
</div>

<table id="characterTable" class="table table-striped">
    <thead>
    <tr>
        <th style="text-align: center;">#</th>
        <th>CODE</th>
        <th>TYPE</th>
        <th>GENRE</th>
        <th>
            TAGS
            <g:img dir="images/selectIntrigue" file="locked.png"/>
            <g:img dir="images/selectIntrigue" file="forbidden.png"/>
        </th>
        <th>PRÉNOM</th>
        <th style="text-align: center;">
            A RELANCER <input id="restartFirstnameAll" type="checkbox" disabled="true" style="float: right;">
        </th>
        <th>NOM</th>
        <th style="text-align: center;">
            A RELANCER <input id="restartLastnameAll" type="checkbox" disabled="true" style="float: right;">
        </th>
    </tr>
    </thead>
    <tbody>
    <g:each status="i" in="${characterList}" var="character">
        <g:if test="${!(((org.gnk.substitution.data.Character) character).type == "STF")}">
            <tr id="char${character.id}">
                <!-- # -->
                <td style="text-align: center;">${i + 1}</td>
                <!-- Id - modal button -->
                <td class="code"><a href="#modalChar${i + 1}" role="button" class="btn" data-toggle="modal"
                                    disabled="true">CHAR-${character.id.encodeAsHTML()}</a></td>
                <!-- Type -->
                <td class="upper" style="text-align: center;">${character.type.encodeAsHTML()}</td>
                <!-- Gender -->
                <td class="gender upper" style="text-align: center;">${character.gender.encodeAsHTML()}</td>
                <!-- Tags -->
                <td class="charTags" style="width: 450px">
                    <ul class="unstyled">
                        <g:each status="j" in="${character.tagList}" var="tag">
                            <li><strong class="cap">${tag.value.encodeAsHTML()}</strong>
                                <!-- Lock Tag -->
                                <input class="lockTag" class="btn-danger" type="checkbox">
                                <!-- Ban Tag -->
                                <input class="banTag" type="checkbox">
                                (<span class="cap">${tag.family.encodeAsHTML()}</span> / ${tag.weight.encodeAsHTML()})
                            </li>
                        </g:each>
                    </ul>
                </td>
                <!-- Firstname -->
                <td class="firstname">
                    <select class="firstname_select bold" disabled="true" isEmpty="true">
                    </select>
                    <a class="btn unban" title="Débannir" disabled="true"><i class="icon-arrow-left"></i></a>
                </td>
                <!-- Restart Firstname -->
                <td class="restartFirstname" style="text-align: center;">
                    <input type="checkbox" disabled="true">
                </td>
                <!-- Lastname -->
                <td class="lastname">
                    <select class="lastname_select bold" disabled="true" isEmpty="true">
                    </select>
                    <a class="btn unban" title="Débannir" disabled="true"><i class="icon-arrow-left"></i></a>
                    <input type="text" class="customCharacter written"
                           placeholder="Add a custom character">
                    %{--<a class="btn unban" title="Débannir" disabled="true"><i class="icon-arrow-left"></i></a>--}%
                    <button class="btn customCharacterButton" title="Create the custom character" type="button"
                            data-character-id="${character.id}"><i class="icon-arrow-left"></i>
                    </button>
                </td>
                <!-- Restart Lastname -->
                <td class="restartLastname" style="text-align: center;">
                    <input type="checkbox" disabled="true">
                </td>
            </tr>
        </g:if>
    </g:each>
    <tbody>
</table>

<!-- RelationShip Graph -->

<div class="row-fluid">
    <div class="span12" id="Relations">
        <div class="panel panel-default">
            <div class="panel-heading" style="margin-top: 20px">
                <g:message code="roletoperso.allRelationsSummary"
                           default="All relations between characters summary"/>
                <i class="icon-refresh" id="mybtnrefresh"></i>
            </div>

            <div style="overflow: auto; height:500px;" id="container">
                <g:hiddenField id="relationjson" name="relationjson" value="${relationjson}"/>
                <g:hiddenField id="relationjson_tmp" name="relationjson" value="${relationjson}"/>
                <div id="infovis">
                </div>

                <div id="right-container">
                    <div id="inner-details"></div>
                </div>
                <g:render template="relationSummary"></g:render>
            </div>

            <div class="legend">
            </div>
        </br>
        </div>
    </div>
</div>

<!-- End Graph -->

<table class="table table-striped">
    <thead>
    <tr class="upper">
        <th style="text-align: center;">convention rule</th>
        <th>description</th>
    </tr>
    </thead>
    <tbody>
    <g:each status="n" in="${ruleList}" var="rule">
        <tr id="char${rule.id}">
            <!-- convention rule -->
            <td style="text-align: center;">${n + 1}</td>
            <!-- description -->
            <td class="cap">${rule.description}</td>
        </tr>
    </g:each>
    </tbody>
</table>

<!-- Modal Views -->
<!--g:render template="modalViewCharacters" /-->

<g:javascript src="substitution/subChars.js"/>

<script type="text/javascript">
    $(document).ready(function () {
        // CharsJSON
        charsJSON = initCharsJSON();

        isSubCharactersRunning = false;

        initgraph();

        initCharsEvents("${g.createLink(controller:'substitution', action:'getSubCharacters')}")

        $('.customCharacterButton').click(function(){

            var input = $(this).prev();
            var content = input.val();
            var characterList = $("select", $(this).parent());
            var characterId = $(this).attr("data-character-id");

            if (content != "") {
                var character = new Object();
                // Gn id
                character.gnId = ${gnInfo.dbId}
                // HTML id
                character.htmlId = "char"+ characterId;
                // Gender
                character.gender = $(".gender", $(this).closest("tr")).html();
                // BEGIN Tags LOOP
                character.tags = new Array();
                //character.proposedLastnames.add(content);

                charsJSON.characters.push(character);
                characterList.append($("<option>").attr("value", content).text(content));
                $(this).prev().val('');
            }
        });

    });

    function initgraph() {
        $("#mybtnrefresh").click(function () {
            reloadgraph();
        });
        $(".firstname_select").on({
            change: function () {
                reloadgraph();
            }
        });
        $(".lastname_select").on({
            change: function () {
                reloadgraph();
            }
        });
    }



    function initCharsJSON() {
        var jsonObject = new Object();
        // Universe
        jsonObject.universe = "${gnInfo.universe}";
        jsonObject.gnId = "${gnInfo.dbId}"
        // BEGIN Characters LOOP
        var charArray = new Array();
        <g:each status="i" in="${characterList}" var="character">
        var character = new Object();
        // Gn Id
        character.gnId = "${character.id}"
        // HTML Id
        character.htmlId = "char${character.id}"
        // Gender
        character.gender = "${character.gender}"
        // BEGIN Tags LOOP
        var tagArray = new Array();
        var relationArray = new Array()
        <g:each status="j" in="${character.tagList}" var="tag">
        var tag = new Object();
        tag.value = "${tag.value}";
        tag.family = "${tag.family}";
        tag.weight = "${tag.weight}";
        tagArray.push(tag);
        </g:each>
        <g:each status="j" in="${character.relationList}" var="rel">
        var relationChar = new Object();
        relationChar.role1 = "${rel.role1}";
        relationChar.role2 = "${rel.role2}";
        relationChar.type = "${rel.type}";
        relationArray.push(relationChar);
        </g:each>
        // END Tags LOOP
        if (tagArray.length > 0) {
            character.tags = tagArray;
            character.relationList = relationArray;
        }
        charArray.push(character);
        </g:each>
        // END Characters LOOP

        jsonObject.characters = charArray;
        return jsonObject;
    }
</script>


