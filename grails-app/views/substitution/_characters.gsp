<div class="row-fluid">
    <div class="span4"><legend>Personnages</legend></div>
    <div class="span1"><span class="badge badge-important" id="charsPercentage">0 %</span></div>
    <div class="span2"><a id="runSubCharactersButton" class="btn btn-info"><i class="icon-play icon-white"></i> LANCER</a></div>
    <div class="span1" id="charsLoader" style="display: none; float : right;"><g:img dir="images/substitution" file="loader.gif" width="30" height="30"/></div>
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
        <tr id="char${character.id}">
            <!-- # -->
            <td style="text-align: center;">${i + 1}</td>
            <!-- Id - modal button -->
            <td><a href="#modalChar${i + 1}" role="button" class="btn" data-toggle="modal" disabled="true">CHAR-${character.id.encodeAsHTML()}</a></td>
            <!-- Type -->
            <td class="upper" style="text-align: center;">${character.type.encodeAsHTML()}</td>
            <!-- Gender -->
            <td class="upper" style="text-align: center;">${character.gender.encodeAsHTML()}</td>
            <!-- Tags -->
            <td class="charTags">
                <ul class="unstyled">
                    <g:each status="j" in="${character.tagList}" var="tag">
                        <li><strong class="cap">${tag.value.encodeAsHTML()}</strong>
                            <!-- Lock Tag -->
                            <input class="lockTag" class="btn-danger" type="checkbox">
                            <!-- Ban Tag -->
                            <input class="banTag" type="checkbox">
                            <br>
                            (<span class="cap">${tag.family.encodeAsHTML()}</span> / ${tag.weight.encodeAsHTML()})
                        </li>
                    </g:each>
                </ul>
            </td>
            <!-- Firstname -->
            <td class="firstname">
                <select class="bold" disabled="true" isEmpty="true">
                </select>
                <a class="btn unban" title="Débannir" disabled="true"><i class="icon-arrow-left"></i></a>
            </td>
            <!-- Restart Firstname -->
            <td class="restartFirstname" style="text-align: center;">
                <input type="checkbox" disabled="true">
            </td>
            <!-- Lastname -->
            <td class="lastname">
                <select class="bold" disabled="true" isEmpty="true">
                </select>
                <a class="btn unban" title="Débannir" disabled="true"><i class="icon-arrow-left"></i></a>
            </td>
            <!-- Restart Lastname -->
            <td class="restartLastname" style="text-align: center;">
                <input type="checkbox" disabled="true">
            </td>
        </tr>
    </g:each>
    <tbody>
</table>

<!-- Modal Views -->
<!--g:render template="modalViewCharacters" /-->

<g:javascript src="substitution/subChars.js" />

<script type="text/javascript">
    $(document).ready(function() {
        // CharsJSON
        charsJSON = initCharsJSON();

        isSubCharactersRunning = false;

        initCharsEvents("${g.createLink(controller:'substitution', action:'getSubCharacters')}")
    });

    function initCharsJSON() {
        var jsonObject = new Object();
        // Universe
        jsonObject.universe = "${gnInfo.universe}";

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
        <g:each status="j" in="${character.tagList}" var="tag">
        var tag = new Object();
        tag.value = "${tag.value}";
        tag.family = "${tag.family}";
        tag.weight = "${tag.weight}";
        tagArray.push(tag);
        </g:each>
        // END Tags LOOP
        if (tagArray.length > 0) {character.tags = tagArray;}
        charArray.push(character);
        </g:each>
        // END Characters LOOP

        jsonObject.characters = charArray;
        return jsonObject;
    }
</script>


