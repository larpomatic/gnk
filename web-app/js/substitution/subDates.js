function updateDatesJSONUser(dateJSON) {
    var pastSceneArray = dateJSON.pastscenes;
    for (key in pastSceneArray) {
        extractionDateString(key, pastSceneArray, dateJSON.pastscenes);
    }
    // Idem pour les events
    var eventArray = datesJSON.events;
    for (var key in eventArray) {
        extractionDateString(key, eventArray, datesJSON.events);
    }
    console.log("UpdateDateJsonUser->");
    console.log(dateJSON);
}
    function extractionDateString(key, tableJSON, tableEnd){
        // obj peut être une past scene ou un event
        var obj = tableJSON[key];

        // Event HTML Element
        var objElement =  $("#" + obj.htmlId);

        // Update date
        var objSelect = objElement.children(".date").children(".inputDate");

        tableEnd[key].date.day = parseInt(objSelect.val().substring(0,2));
        tableEnd[key].absoluteDay = parseInt(objSelect.val().substring(0,2));
        console.log(parseInt(objSelect.val().substring(0,2)));
        tableEnd[key].date.month = parseInt(objSelect.val().substring(3,5));
        tableEnd[key].absoluteMonth = parseInt(objSelect.val().substring(3,5));
        console.log(parseInt(objSelect.val().substring(3,5)));
        tableEnd[key].date.year = parseInt(objSelect.val().substring(6,10));
        tableEnd[key].absoluteYear = parseInt(objSelect.val().substring(6,10));
        console.log(parseInt(objSelect.val().substring(6,10)));
        tableEnd[key].date.hours = parseInt(objSelect.val().substring(13,15));
        tableEnd[key].absoluteHour = parseInt(objSelect.val().substring(13,15));
        console.log(parseInt(objSelect.val().substring(13,15)));
        tableEnd[key].date.minutes = parseInt(objSelect.val().substring(16,18));
        tableEnd[key].absoluteMinute = parseInt(objSelect.val().substring(16,18));
        console.log(parseInt(objSelect.val().substring(16,18)));

        tableEnd[key].isUpdate = "yes";
        console.log(tableEnd[key]);
    }
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

        sortTable($('#dateTable'), 'asc');
    }

function sortTable($table, order) {
    var $rows = $('tbody > tr', $table);
    $rows.sort(function (a, b) {
        var keyA = new Date($('td:eq(5) input', a)[0].value);
        var keyB = new Date($('td:eq(5) input', b)[0].value);

        if (order == 'asc') {
            return (keyA > keyB) ? 1 : 0;
        } else {
            return (keyA > keyB) ? 0 : 1;
        }
    });
    $.each($rows, function (index, row) {
        $table.append(row);
    });
}

// Update date HTML element
    function updateDateView(date, dateElement) {
        if (!(typeof(date) == "undefined")) {
            // Update date element
            dateElement.css("background-color", "rgb(108,194,219)").css("color", "#FFF");
            var dateS = "";
            if (!(typeof(date.day) == "undefined")) {dateS += prettyDate(date.day) + "/";} else {dateS += "__/";}
            if (!(typeof(date.month) == "undefined")) {dateS += prettyDate(date.month) + "/";} else {dateS += "__/";}
            if (!(typeof(date.year) == "undefined")) {dateS += prettyYear(date.year);} else {dateS += "__";}
            if (!(typeof(date.minutes) == "undefined") && !(typeof(date.hours) == "undefined")) {dateS += " - "}
            if (!(typeof(date.hours) == "undefined")) {dateS += prettyDate(date.hours) + ":";} else {dateS += "__:";}
            if (!(typeof(date.minutes) == "undefined")) {dateS += prettyDate(date.minutes);} else {dateS += "__";}
            dateElement.html("<input class=\"inputDate\" type=\"text\" value=\""+dateS+"\">");

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
    function prettyYear(number) {
        while (number.length < 4) {
            number = "0" + number;
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
    function runDatesSubstitution(url, needUpdateJSON){
        if (!isSubDatesRunning) {
            isSubDatesRunning = true;
            var datesPercentageSpan = $("#datesPercentage");
            var badgeClass = "badge-important";
            datesPercentageSpan.attr("class", "badge " + badgeClass);
            datesPercentageSpan.text("0%");

            // Show loader
            $("#datesLoader").show();

            console.log("datesJSON BEFORE sending -> ");
            console.log(datesJSON);

            if (needUpdateJSON == true) {
                updateDatesJSONUser(datesJSON);
                updateDatesView(datesJSON);
                sendJSON(datesJSON, url, onSuccessAjaxDates, onErrorAjaxDates, completeAjaxDates);
                // Show loader
                //$("#datesLoader").hide();
            } else {
                sendJSON(datesJSON, url, onSuccessAjaxDates, onErrorAjaxDates, completeAjaxDates);
            }
            // Send JSON to controller
            // sendJSON(datesJSON, url, onSuccessAjaxDates, onErrorAjaxDates, completeAjaxDates);
        }
    }


// Init of dates
function initDateList(url) {
    runDatesSubstitution(url, false);
    $("#runSubDateButton").click( function() {
            runDatesSubstitution(url, true);
        }
    );
}