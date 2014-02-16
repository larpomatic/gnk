// Update date HTML elements
function updateDatesView(datesJSON)
{
    // Counters
    var nbOK = 0;
    var nbKO = 0;

    var pastsceneArray = datesJSON.pastscenes;
    for (var key in pastsceneArray) {
        var pastscene = pastsceneArray[key];

        // Pastscene HTML Element
        var pastsceneElement =  $("#" + pastscene.htmlId);

        var dateElement = pastsceneElement.children(".date").eq(0);
        var date = pastscene.date;

        // Update pastscene
        updateDateView(date, dateElement);

        // Update counters
        if (!(typeof(date) == "undefined")) {nbOK += 1;}
        else {nbKO += 1;}
    }

    var eventArray = datesJSON.events;
    for (var key in eventArray) {
        var event = eventArray[key];

        // Event HTML Element
        var eventElement =  $("#" + event.htmlId);

        var dateElement = eventElement.children(".date").eq(0);
        var date = event.date;

        // Update pastscene
        updateDateView(date, dateElement);

        // Update counters
        if (!(typeof(date) == "undefined")) {nbOK += 1;}
        else {nbKO += 1;}
    }

    // Update percentage
    if ((nbOK + nbKO) == 0) {
        var percent = 100
    }
    else {
        var percent = nbOK * 100 / (nbOK + nbKO);
    }
    var datesPercentageSpan = $("#datesPercentage");
    var badgeClass = "badge-success";
    if (percent < 50) {badgeClass = "badge-important"}
    else if (percent < 100) {badgeClass = "badge-warning"}
    datesPercentageSpan.attr("class", "badge " + badgeClass);
    datesPercentageSpan.text(Math.round(percent) + " %");
}

// Update date HTML element
function updateDateView(date, dateElement) {
    if (!(typeof(date) == "undefined")) {
        // Update date element
        dateElement.css("background-color", "rgb(108,194,219)").css("color", "#FFF");
        var dateS = "";
        if (!(typeof(date.day) == "undefined")) {dateS += prettyDate(date.day) + "/";} else {dateS += "__/";}
        if (!(typeof(date.month) == "undefined")) {dateS += prettyDate(date.month) + "/";} else {dateS += "__/";}
        if (!(typeof(date.year) == "undefined")) {dateS += prettyDate(date.year);} else {dateS += "__";}
        if (!(typeof(date.minutes) == "undefined") && !(typeof(date.hours) == "undefined")) {dateS += " - "}
        if (!(typeof(date.hours) == "undefined")) {dateS += prettyDate(date.hours) + ":";} else {dateS += "__:";}
        if (!(typeof(date.minutes) == "undefined")) {dateS += prettyDate(date.minutes);} else {dateS += "__";}
        dateElement.text(dateS);

        dateElement.attr("isEmpty", false);
    }
    else {
        // Update date element
        dateElement.css("background-color", "rgb(185,185,185)").css("color", "#FFF");
        dateElement.text("AUCUN RESULTAT");

        dateElement.attr("isEmpty", true);
    }
}

function prettyDate(number) {
    if (number < 10) {
        return "0" + number;
    }
    return number;
}

// On succes AJAX
function onSuccessAjaxDates(data, textStatus, jqXHR) {
    // Update datesJson with substitution result
    datesJSON = data;

    console.log("datesJSON AFTER sending -> ");
    console.log(datesJSON);

    // Update dates view
    updateDatesView(datesJSON);

    addAlert("subDatesAlertContainer", "alert alert-info", "Information", "La substitution des dates a été effectuée.")
}

// On error AJAX
function onErrorAjaxDates(jqXHR, textStatus, errorThrown) {
    addAlert("subDatesAlertContainer", "alert alert-error", "Erreur", "La substitution des dates n'a pas pu s'effectuer.")
}

// After success or error AJAX
function completeAjaxDates(jqXHR, textStatus) {
    $("#datesLoader").hide();
    isSubDatesRunning = false;
}

// Update datesJSON before final sending
function prepareDatesJSONForValidation(datesJSON) {
    var pastsceneArray = datesJSON.pastscenes;
    for (var key in pastsceneArray) {
        var pastscene = pastsceneArray[key];

        // Pastscene HTML Element
        var pastsceneElement =  $("#" + pastscene.htmlId);

        var dateElement = pastsceneElement.children(".date").eq(0);
        if (dateElement.attr("isEmpty") == "true") {
            return false;
        }
    }
    var eventArray = datesJSON.events;
    for (var key in eventArray) {
        var event = eventArray[key];

        // Event HTML Element
        var eventElement =  $("#" + event.htmlId);

        var dateElement = eventElement.children(".date").eq(0);
        if (dateElement.attr("isEmpty") == "true") {
            return false;
        }
    }

    return true;
}

// Run dates substitution
function runDatesSubstitution(url){
    if (!isSubDatesRunning) {
        isSubDatesRunning = true;

        // Show loader
        $("#datesLoader").show();

        console.log("datesJSON BEFORE sending -> ");
        console.log(datesJSON);

        // Send JSON to controller
        sendJSON(datesJSON, url, onSuccessAjaxDates, onErrorAjaxDates, completeAjaxDates);
    }
}