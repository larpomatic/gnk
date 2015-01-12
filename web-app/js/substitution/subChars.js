function updateCharsJSONUser(charsJSON) {
    var charArray = charsJSON.characters;
    for (key in charArray) {
        var character = charArray[key];

        // Character HTML element
        var charElement =  $("#" + character.htmlId);

        //Lock & Ban Tags
        var tagArray = character.tags;
        for (var n in tagArray) {
            var tag = tagArray[n];
            var lockTagInput = charElement.children(".charTags").eq(0).children("ul").eq(0).children("li").eq(n).children(".lockTag").eq(0);
            var banTagInput = charElement.children(".charTags").eq(0).children("ul").eq(0).children("li").eq(n).children(".banTag").eq(0);
            if (lockTagInput.is(":checked") && !banTagInput.is(":checked"))
                tag.weight = 101;
            else if (!lockTagInput.is(":checked") && banTagInput.is(":checked"))
                tag.weight = -101;
        }

        // Update firstname
        var firstnameSelect = charElement.children(".firstname").eq(0).children("select").eq(0);
        var isEmpty = firstnameSelect.attr("isEmpty");
        if (firstnameSelect.attr("isEmpty") == "false") {
            var restartfirstnameInput = charElement.children(".restartFirstname").eq(0).children("input").eq(0);
            if (typeof(restartfirstnameInput.attr("checked")) == "undefined") {
                // Update firstname selection
                character.selectedFirstname = firstnameSelect.val();
            }
            else {
                // Delete firstname selection
                delete character.selectedFirstname;
                // Copy proposed firstnames in banned firstnames
                if (typeof(character.bannedFirstnames) == "undefined") {
                    character.bannedFirstnames = new Array();
                }
                character.bannedFirstnames = character.bannedFirstnames.concat(character.proposedFirstnames);
                // Delete proposed firstnames
                delete character.proposedFirstnames;
            }
        }

        // Update lastname
        var lastnameSelect = charElement.children(".lastname").eq(0).children("select").eq(0);
        if (lastnameSelect.attr("isEmpty") == "false") {
            var restartLastnameInput = charElement.children(".restartLastname").eq(0).children("input").eq(0);
            if (typeof(restartLastnameInput.attr("checked")) == "undefined") {
                // Update lastname selection
                character.selectedLastname = lastnameSelect.val();
            }
            else {
                // Delete lastname selection
                delete character.selectedLastname;
                // Copy proposed lastnames in banned lastnames
                if (typeof(character.bannedLastnames) == "undefined") {
                    character.bannedLastnames = new Array();
                }
                character.bannedLastnames = character.bannedLastnames.concat(character.proposedLastnames);
                // Delete proposed lastnames
                delete character.proposedLastnames;
            }
        }
    }
}

// Update char HTML elements
function updateCharsView(charsJSON)
{
    // Counters
    var nbOK = 0;
    var nbKO = 0;

    var charArray = charsJSON.characters;
    for (var key in charArray) {
        var character = charArray[key];

        for(var tag in character.tagList) {
            tag.weight = 101
        }
        // Character HTML Element
        var charElement =  $("#" + character.htmlId);

        // Update firstname
        if (typeof(character.selectedFirstname) == "undefined") {
            var firstnameSelect = charElement.children(".firstname").eq(0).children("select").eq(0);
            firstnameSelect.empty();
            var unbanFirstname = charElement.children(".firstname").eq(0).children("a").eq(0);
            var restartFirstnameInput = charElement.children(".restartFirstname").eq(0).children("input").eq(0);
            var proposedFirstnames = character.proposedFirstnames
            if (!(typeof(proposedFirstnames) == "undefined")) {
                // Update firstname select
                firstnameSelect.css("background-color", "rgb(108,194,219)").css("color", "#FFF");
                for (var key in proposedFirstnames) {
                    var firstname = proposedFirstnames[key];
                    firstnameSelect.append($("<option>").attr("value", firstname).text(firstname));
                }
                firstnameSelect.attr("isEmpty", false);
                firstnameSelect.attr("disabled", false);

                // Update firstname restart
                restartFirstnameInput.attr("disabled", false);
                restartFirstnameInput.attr("checked", false);
            }
            else {
                // Update firstname select
                firstnameSelect.css("background-color", "rgb(185,185,185)").css("color", "#FFF");
                firstnameSelect.append($("<option>").text("AUCUN RESULTAT"));
                firstnameSelect.attr("isEmpty", true);
                firstnameSelect.attr("disabled", true);

                // Update firstname restart
                restartFirstnameInput.attr("disabled", true);
                restartFirstnameInput.attr("checked", true);
            }
            var bannedFirstnames = character.bannedFirstnames;
            if (!(typeof(bannedFirstnames) == "undefined")) {
                // Update firstname select
                for (var key in bannedFirstnames) {
                    var firstname = bannedFirstnames[key];
                    var option = $("<option>").attr("value", firstname).attr("disabled", true).text(firstname);
                    option.css("color", "#000");
                    firstnameSelect.append(option);
                }
                firstnameSelect.attr("disabled", false);

                // Update unban button
                unbanFirstname.attr("disabled", false);
            }
            else {
                // Update unban button
                unbanFirstname.attr("disabled", true);
            }
        }

        // Update lastname
        if (typeof(character.selectedLastname) == "undefined") {
            var lastnameSelect = charElement.children(".lastname").eq(0).children("select").eq(0);
            lastnameSelect.empty();
            var unbanLastname = charElement.children(".lastname").eq(0).children("a").eq(0);
            var restartLastnameInput = charElement.children(".restartLastname").eq(0).children("input").eq(0);
            var proposedLastnames = character.proposedLastnames
            if (!(typeof(proposedLastnames) == "undefined")) {
                // Update lastname select
                lastnameSelect.css("background-color", "rgb(108,194,219)").css("color", "#FFF");
                for (var key in proposedLastnames) {
                    var lastname = proposedLastnames[key];
                    lastnameSelect.append($("<option>").attr("value", lastname).text(lastname));
                }
                lastnameSelect.attr("isEmpty", false);
                lastnameSelect.attr("disabled", false);

                // Update lastname restart
                restartLastnameInput.attr("disabled", false);
                restartLastnameInput.attr("checked", false);
            }
            else {
                // Update lastname select
                lastnameSelect.css("background-color", "rgb(185,185,185)").css("color", "#FFF");;
                lastnameSelect.append($("<option>").text("AUCUN RESULTAT"));
                lastnameSelect.attr("isEmpty", true);
                lastnameSelect.attr("disabled", true);

                // Update lastname restart
                restartLastnameInput.attr("disabled", true);
                restartLastnameInput.attr("checked", true);
            }
            var bannedLastnames = character.bannedLastnames;
            if (!(typeof(bannedLastnames) == "undefined")) {
                // Update lastname select
                for (var key in bannedLastnames) {
                    var lastname = bannedLastnames[key];
                    var option = $("<option>").attr("value", lastname).attr("disabled", true).text(lastname);
                    option.css("color", "#000");
                    lastnameSelect.append(option);
                }
                lastnameSelect.attr("disabled", false);

                // Update unban button
                unbanLastname.attr("disabled", false);
            }
            else {
                // Update unban button
                unbanLastname.attr("disabled", true);
            }
        }

        // Update counters
        if (!(typeof(character.proposedFirstnames) == "undefined")) {nbOK += 1;}
        else {nbKO += 1;}
        if (!(typeof(character.proposedLastnames) == "undefined")) {nbOK += 1;}
        else {nbKO += 1;}
    }

    // Update percentage
    var percent = nbOK * 100 / (nbOK + nbKO);
    var charsPercentageSpan = $("#charsPercentage");
    var badgeClass = "badge-success";
    if (percent < 50) {badgeClass = "badge-important"}
    else if (percent < 100) {badgeClass = "badge-warning"}
    charsPercentageSpan.attr("class", "badge " + badgeClass);
    charsPercentageSpan.text(Math.round(percent) + " %");
}


// Reload graph function
function reloadgraph() {
    $("#infovis-canvaswidget").remove();
    var old_json = $("#relationjson_tmp").val();
    $("#characterTable > tbody > tr").each(function() {
        var firstname = $("td.firstname select", this).val();
        var lastname = $("td.lastname select", this).val();
        var code = $("td.code a", this).html();
        //code = code.replace(/\-/g, '');
        if ((firstname != null) && (lastname != null))
        {
            old_json = old_json.replace(new RegExp(code, 'g'), firstname + " " + lastname);
        }
    });
    $("#relationjson").val(old_json);
    init();
}

// On succes AJAX
function onSuccessAjaxChars(data, textStatus, jqXHR) {
    // Update charsJson with substitution result
    charsJSON = data;

    console.log("charsJSON AFTER sending -> ");
    console.log(charsJSON);

    // Update characters view
    updateCharsView(charsJSON);

    addAlert("subCharsAlertContainer", "alert alert-info", "Information", "La substitution des personnages a été effectuée.")

    debugger;

    reloadgraph();
}

// On error AJAX
function onErrorAjaxChars(jqXHR, textStatus, errorThrown) {
    addAlert("subCharsAlertContainer", "alert alert-error", "Erreur", "La substitution des personnages n'a pas pu s'effectuer.")
}

// After success or error AJAX
function completeAjaxChars(jqXHR, textStatus) {
    $("#charsLoader").hide();
    isSubCharactersRunning = false;
}

// Update charsJSON before final sending
function prepareCharsJSONForValidation(charsJSON) {
    var charArray = charsJSON.characters;
    for (key in charArray) {
        var character = charArray[key];

        // Character HTML element
        var charElement =  $("#" + character.htmlId);

        // Update firstname
        var firstnameSelect = charElement.children(".firstname").eq(0).children("select").eq(0);
        if (firstnameSelect.attr("isEmpty") == "false") {
            character.selectedFirstname = firstnameSelect.val();
        }
        else {
            return false;
        }

        // Update lastname
        var lastnameSelect = charElement.children(".lastname").eq(0).children("select").eq(0);
        if (lastnameSelect.attr("isEmpty") == "false") {
            character.selectedLastname = lastnameSelect.val();
        }
        else {
            return false;
        }
    }

    return true;
}

// Select, unselect all restart
function selectAllRestartChars(restartClass, isToCheck) {
    $("." + restartClass).each( function() {
        var restartInput = $(this).eq(0).children("input").eq(0);
        if(typeof(restartInput.attr("disabled")) == "undefined") {
            restartInput.attr("checked", isToCheck);
        }
    });
}

// Initiation of chars events
function initCharsEvents(url) {
    $("#restartFirstnameAll").click( function(){
        // Select all
        if (!(typeof($(this).attr("checked")) == "undefined")) {selectAllRestartChars("restartFirstname", true)}
        // Unselect all
        else {selectAllRestartChars("restartFirstname", false)}
    });

    $("#restartLastnameAll").click( function(){
        // Select all
        if (!(typeof($(this).attr("checked")) == "undefined")) {selectAllRestartChars("restartLastname", true)}
        // Unselect all
        else {selectAllRestartChars("restartLastname", false)}
    });

    // Unban buttons
    var charArray = charsJSON.characters;
    for (var key in charArray) {
        var character = charArray[key];
        var charHtmlId = character.htmlId
        var charElement =  $("#" + charHtmlId);

        var unbanFirstname = charElement.children(".firstname").eq(0).children("a").eq(0);
        var unbanLastname = charElement.children(".lastname").eq(0).children("a").eq(0);

        // Unban firstname
        unbanFirstname.click( (function(charHtmlId){
            return function() {
                if(typeof($(this).attr("disabled")) == "undefined") {
                    // Find charJSON in charArray with charHtmlId
                    var charJSON = null;
                    var charArray = charsJSON.characters;
                    for (var key in charArray) {
                        var character = charArray[key];
                        if (character.htmlId == charHtmlId) {
                            charJSON = character;
                            break;
                        }
                    }
                    // Unban
                    // Delete firstname selection
                    delete charJSON.selectedFirstname;
                    // Copy banned firstnames in proposed firstnames
                    if (typeof(charJSON.proposedFirstnames) == "undefined") {
                        charJSON.proposedFirstnames = new Array();
                    }
                    charJSON.proposedFirstnames = charJSON.proposedFirstnames.concat(charJSON.bannedFirstnames);
                    // Delete banned firstnames
                    delete charJSON.bannedFirstnames;

                    // Character HTML Element
                    var charElement =  $("#" + charHtmlId);
                    // Update firstname
                    // Select
                    var firstnameSelect = charElement.children(".firstname").eq(0).children("select").eq(0);
                    firstnameSelect.empty();
                    firstnameSelect.css("background-color", "rgb(108,194,219)").css("color", "#FFF");
                    for (var key in charJSON.proposedFirstnames) {
                        var firstname = charJSON.proposedFirstnames[key];
                        firstnameSelect.append($("<option>").attr("value", firstname).text(firstname));
                    }
                    firstnameSelect.attr("isEmpty", false);
                    firstnameSelect.attr("disabled", false);
                    // Unban button
                    var unbanFirstname = charElement.children(".firstname").eq(0).children("a").eq(0);
                    unbanFirstname.attr("disabled", true);
                }
            };
        }(charHtmlId)));

        // Unban lastname
        unbanLastname.click( (function(charHtmlId){
            return function() {
                if(typeof($(this).attr("disabled")) == "undefined") {
                    // Find charJSON in charArray with charHtmlId
                    var charJSON = null;
                    var charArray = charsJSON.characters;
                    for (var key in charArray) {
                        var character = charArray[key];
                        if (character.htmlId == charHtmlId) {
                            charJSON = character;
                            break;
                        }
                    }
                    // Unban
                    // Delete lastname selection
                    delete charJSON.selectedLastname;
                    // Copy banned lastnames in proposed lastnames
                    if (typeof(charJSON.proposedLastnames) == "undefined") {
                        charJSON.proposedLastnames = new Array();
                    }
                    charJSON.proposedLastnames = charJSON.proposedLastnames.concat(charJSON.bannedLastnames);
                    // Delete banned lastname
                    delete charJSON.bannedLastnames;

                    // Character HTML Element
                    var charElement =  $("#" + charHtmlId);
                    // Update lastname
                    // Select
                    var lastnameSelect = charElement.children(".lastname").eq(0).children("select").eq(0);
                    lastnameSelect.empty();
                    lastnameSelect.css("background-color", "rgb(108,194,219)").css("color", "#FFF");
                    for (var key in charJSON.proposedLastnames) {
                        var lastname = charJSON.proposedLastnames[key];
                        lastnameSelect.append($("<option>").attr("value", lastname).text(lastname));
                    }
                    lastnameSelect.attr("isEmpty", false);
                    lastnameSelect.attr("disabled", false);
                    // Unban button
                    var unbanLastname = charElement.children(".lastname").eq(0).children("a").eq(0);
                    unbanLastname.attr("disabled", true);
                }
            };
        }(charHtmlId)));
    }

    // Run characters substitution
    $("#runSubCharactersButton").click( function(){
        $("#customCharacter").each(function() {
            $(this).removeAttr("disabled")
        });
        if (!isSubCharactersRunning) {
            isSubCharactersRunning = true;
            // Update JSON with user selection
            updateCharsJSONUser(charsJSON)


            // Enable selectAll
            $("#restartFirstnameAll").removeAttr("disabled");
            $("#restartLastnameAll").removeAttr("disabled");

            // Show loader
            $("#charsLoader").show();

            console.log("charsJSON BEFORE sending to " + url + " -> ");
            console.log(charsJSON);

            // Send JSON to controller
            sendJSON(charsJSON, url, onSuccessAjaxChars, onErrorAjaxChars, completeAjaxChars);
        }
    });
}