function updatePlacesJSONUser(placesJSON) {
    var placeArray = placesJSON.places;
    for (key in placeArray) {
        var place = placeArray[key];

        // Place HTML element
        var placeElement = $("#" + place.htmlId);

        // Update place
        var placeSelect = placeElement.children(".place").eq(0).children("select").eq(0);
        var isEmpty = placeSelect.attr("isEmpty");
        if (placeSelect.attr("isEmpty") == "false") {
            var restartPlaceInput = placeElement.children(".restartPlace").eq(0).children("input").eq(0);
            if (typeof(restartPlaceInput.attr("checked")) == "undefined") {
                // Update name selection
                place.selectedName = placeSelect.val();
            }
            else {
                // Delete name selection
                delete place.selectedName;
                // Copy proposed names in banned names
                if (typeof(place.bannedNames) == "undefined") {
                    place.bannedNames = new Array();
                }
                place.bannedNames = place.bannedNames.concat(place.proposedNames);
                // Delete proposed names
                delete place.proposedNames;
            }
        }
    }
}

// Update place HTML elements
function updatePlacesView(placesJSON) {
    // Counters
    var nbOK = 0;
    var nbKO = 0;

    var placeArray = placesJSON.places;
    for (var key in placeArray) {
        var place = placeArray[key];

        // Place HTML Element
        var placeElement = $("#" + place.htmlId);

        // Update place
        if (typeof(place.selectedName) == "undefined") {
            var placeSelect = placeElement.children(".place").eq(0).children("select").eq(0);
            placeSelect.empty();
            var unbanPlace = placeElement.children(".place").eq(0).children("a").eq(0);
            var restartPlaceInput = placeElement.children(".restartPlace").eq(0).children("input").eq(0);
            var proposedNames = place.proposedNames
            if (!(typeof(proposedNames) == "undefined")) {
                // Update name select
                placeSelect.css("background-color", "rgb(108,194,219)").css("color", "#FFF");
                for (var key in proposedNames) {
                    var name = proposedNames[key];
                    placeSelect.append($("<option>").attr("value", name).text(name));
                }
                placeSelect.attr("isEmpty", false);
                placeSelect.attr("disabled", false);

                // Update name restart
                restartPlaceInput.attr("disabled", false);
                restartPlaceInput.attr("checked", false);
//                for (var key in proposedNames) {
//                .substr(0, proposedNames[key].lastIndexOf(" -"))
//                }
            }
            else {
                // Update name select
                placeSelect.css("background-color", "rgb(185,185,185)").css("color", "#FFF");
                placeSelect.append($("<option>").text("AUCUN RESULTAT"));
                placeSelect.attr("isEmpty", true);
                placeSelect.attr("disabled", true);

                // Update name restart
                placeSelect.attr("disabled", true);
                placeSelect.attr("checked", true);
            }
            var bannedNames = place.bannedNames;
            if (!(typeof(bannedNames) == "undefined")) {
                // Update name select
                for (var key in bannedNames) {
                    var name = bannedNames[key];
                    var option = $("<option>").attr("value", name).attr("disabled", true).text(name);
                    option.css("color", "#000");
                    placeSelect.append(option);
                }
                placeSelect.attr("disabled", false);

                // Update unban button
                unbanPlace.attr("disabled", false);
            }
            else {
                // Update unban button
                unbanPlace.attr("disabled", true);
            }
        }

        // Update counters
        if (!(typeof(place.proposedNames) == "undefined")) {
            nbOK += 1;
        }
        else {
            nbKO += 1;
        }
    }

    // Update percentage
    if ((nbOK + nbKO) == 0) {
        var percent = 100
    }
    else {
        var percent = nbOK * 100 / (nbOK + nbKO);
    }
    var placesPercentageSpan = $("#placesPercentage");
    var badgeClass = "badge-success";
    if (percent < 50) {
        badgeClass = "badge-important"
    }
    else if (percent < 100) {
        badgeClass = "badge-warning"
    }
    placesPercentageSpan.attr("class", "badge " + badgeClass);
    placesPercentageSpan.text(Math.round(percent) + " %");
}

// On succes AJAX
function onSuccessAjaxPlaces(data, textStatus, jqXHR) {
    // Update placesJson with substitution result
    placesJSON = data;

    console.log("placesJSON AFTER sending -> ");
    console.log(placesJSON);

    // Update places view
    updatePlacesView(placesJSON);

    addAlert("subPlacesAlertContainer", "alert alert-info", "Information", "La substitution des lieux a été effectuée.")
}

// On error AJAX
function onErrorAjaxPlaces(jqXHR, textStatus, errorThrown) {
    addAlert("subPlacesAlertContainer", "alert alert-error", "Erreur", "La substitution des lieux n'a pas pu s'effectuer.")
}

// After success or error AJAX
function completeAjaxPlaces(jqXHR, textStatus) {
    $("#placesLoader").hide();
    isSubPlacesRunning = false;
}

// Update placesJSON before final sending
function preparePlacesJSONForValidation(placesJSON) {
    var placeArray = placesJSON.places;
    for (key in placeArray) {
        var place = placeArray[key];

        // Place HTML element
        var placeElement = $("#" + place.htmlId);

        // Update name
        var placeWritten = document.getElementById("placeWritten");
        var placeSelect = placeElement.children(".place").eq(0).children("select").eq(0);
        if (placeSelect.attr("isEmpty") == "false") {
            if (document.getElementById("writtenPlace").checked) {
                place.selectedName = placeWritten.value;
            } else {
                place.selectedName = placeSelect.val();
            }
        }
        else {
            return false;
        }
    }

    return true;
}

// Select, unselect all restart
function selectAllRestartPlaces(restartClass, isToCheck) {
    $("." + restartClass).each(function () {
        var restartInput = $(this).eq(0).children("input").eq(0);
        if (typeof(restartInput.attr("disabled")) == "undefined") {
            restartInput.attr("checked", isToCheck);
        }
    });
}

// Initiation of places events
function initPlacesEvents(url) {
    $("#restartPlaceAll").click(function () {
        // Select all
        if (!(typeof($(this).attr("checked")) == "undefined")) {
            selectAllRestartResources("restartPlace", true)
        }
        // Unselect all
        else {
            selectAllRestartResources("restartPlace", false)
        }
    });

    // Unban buttons
    var placeArray = placesJSON.places;
    for (var key in placeArray) {
        var place = placeArray[key];
        var placeHtmlId = place.htmlId
        var placeElement = $("#" + placeHtmlId);

        var unbanPlace = placeElement.children(".place").eq(0).children("a").eq(0);

        // Unban Place
        unbanPlace.click((function (placeHtmlId) {
            return function () {
                if (typeof($(this).attr("disabled")) == "undefined") {
                    // Find placeJSON in placeArray with placeHtmlId
                    var placeJSON = null;
                    var placeArray = placesJSON.places;
                    for (var key in placeArray) {
                        var place = placeArray[key];
                        if (place.htmlId == placeHtmlId) {
                            placeJSON = place;
                            break;
                        }
                    }
                    // Unban
                    // Delete name selection
                    delete placeJSON.selectedName;
                    // Copy banned names in proposed names
                    if (typeof(placeJSON.proposedNames) == "undefined") {
                        placeJSON.proposedNames = new Array();
                    }
                    placeJSON.proposedNames = placeJSON.proposedNames.concat(placeJSON.bannedNames);
                    // Delete banned names
                    delete placeJSON.bannedNames;

                    // Resource HTML Element
                    var placeElement = $("#" + placeHtmlId);
                    // Update Place
                    // Select
                    var placeSelect = placeElement.children(".place").eq(0).children("select").eq(0);
                    placeSelect.empty();
                    placeSelect.css("background-color", "rgb(108,194,219)").css("color", "#FFF");
                    for (var key in placeJSON.proposedNames) {
                        var name = placeJSON.proposedNames[key];
                        placeSelect.append($("<option>").attr("value", name).text(name));
                    }
                    placeSelect.attr("isEmpty", false);
                    placeSelect.attr("disabled", false);
                    // Unban button
                    var unbanPlace = placeElement.children(".place").eq(0).children("a").eq(0);
                    unbanPlace.attr("disabled", true);
                }
            };
        }(placeHtmlId)));
    }

    // Run places substitution
    $("#runSubPlacesButton").click(function () {
        if (!isSubPlacesRunning) {
            isSubPlacesRunning = true;
            // Update JSON with user selection
            updatePlacesJSONUser(placesJSON)

            // Enable selectAll
            $("#restartPlaceAll").removeAttr("disabled");

            $("#customPlace").each(function () {
                $("#customPlace").removeAttr("disabled")
            });

            // Show loader
            $("#placesLoader").show();

            console.log("placesJSON BEFORE sending -> ");
            console.log(placesJSON);

            // Send JSON to controller
            sendJSON(placesJSON, url, onSuccessAjaxPlaces, onErrorAjaxPlaces, completeAjaxPlaces);
        }
    });
}
