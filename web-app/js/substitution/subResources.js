function updateResourcesJSONUser(resourcesJSON) {
    var resourceArray = resourcesJSON.resources;
    for (key in resourceArray) {
        var resource = resourceArray[key];

        // Resource HTML element
        var resourceElement =  $("#" + resource.htmlId);

        // Update resource
        var resourceSelect = resourceElement.children(".resource").eq(0).children("select").eq(0);
        var isEmpty = resourceSelect.attr("isEmpty");
        if (resourceSelect.attr("isEmpty") == "false") {
            var restartResourceInput = resourceElement.children(".restartResource").eq(0).children("input").eq(0);
            if (typeof(restartResourceInput.attr("checked")) == "undefined") {
                // Update name selection
                resource.selectedName = resourceSelect.val();
            }
            else {
                // Delete name selection
                delete resource.selectedName;
                // Copy proposed names in banned names
                if (typeof(resource.bannedNames) == "undefined") {
                    resource.bannedNames = new Array();
                }
                resource.bannedNames = resource.bannedNames.concat(resource.proposedNames);
                // Delete proposed names
                delete resource.proposedNames;
            }
        }
    }
}

// Update resource HTML elements
function updateResourcesView(resourcesJSON)
{
    // Counters
    var nbOK = 0;
    var nbKO = 0;

    var resourceArray = resourcesJSON.resources;
    for (var key in resourceArray) {
        var resource = resourceArray[key];

        // Resource HTML Element
        var resourceElement =  $("#" + resource.htmlId);

        // Update name
        if (typeof(resource.selectedName) == "undefined") {
            var resourceSelect = resourceElement.children(".resource").eq(0).children("select").eq(0);
            resourceSelect.empty();
            var unbanResource = resourceElement.children(".resource").eq(0).children("a").eq(0);
            var restartResourceInput = resourceElement.children(".restartResource").eq(0).children("input").eq(0);
            var proposedNames = resource.proposedNames
            if (!(typeof(proposedNames) == "undefined")) {
                // Update name select
                resourceSelect.css("background-color", "rgb(108,194,219)").css("color", "#FFF");
                for (var key in proposedNames) {
                    var name = proposedNames[key];
                    resourceSelect.append($("<option>").attr("value", name).text(name));
                }
                resourceSelect.attr("isEmpty", false);
                resourceSelect.attr("disabled", false);

                // Update name restart
                restartResourceInput.attr("disabled", false);
                restartResourceInput.attr("checked", false);
            }
            else {
                // Update name select
                resourceSelect.css("background-color", "rgb(185,185,185)").css("color", "#FFF");
                resourceSelect.append($("<option>").text("AUCUN RESULTAT"));
                resourceSelect.attr("isEmpty", true);
                resourceSelect.attr("disabled", true);

                // Update name restart
                resourceSelect.attr("disabled", true);
                resourceSelect.attr("checked", true);
            }
            var bannedNames = resource.bannedNames;
            if (!(typeof(bannedNames) == "undefined")) {
                // Update name select
                for (var key in bannedNames) {
                    var name = bannedNames[key];
                    var option = $("<option>").attr("value", name).attr("disabled", true).text(name);
                    option.css("color", "#000");
                    resourceSelect.append(option);
                }
                resourceSelect.attr("disabled", false);

                // Update unban button
                unbanResource.attr("disabled", false);
            }
            else {
                // Update unban button
                unbanResource.attr("disabled", true);
            }
        }

        // Update counters
        if (!(typeof(resource.proposedNames) == "undefined")) {nbOK += 1;}
        else {nbKO += 1;}
    }

    // Update percentage
    if ((nbOK + nbKO) == 0) {
        var percent = 100
    }
    else {
        var percent = nbOK * 100 / (nbOK + nbKO);
    }
    var resourcesPercentageSpan = $("#resourcesPercentage");
    var badgeClass = "badge-success";
    if (percent < 50) {badgeClass = "badge-important"}
    else if (percent < 100) {badgeClass = "badge-warning"}
    resourcesPercentageSpan.attr("class", "badge " + badgeClass);
    resourcesPercentageSpan.text(Math.round(percent) + " %");

}

// On succes AJAX
function onSuccessAjaxResources(data, textStatus, jqXHR) {
    // Update resourcesJson with substitution result
    resourcesJSON = data;

    console.log("resourcesJSON AFTER sending -> ");
    console.log(resourcesJSON);

    // Update resources view
    updateResourcesView(resourcesJSON);

    addAlert("subResourcesAlertContainer", "alert alert-info", "Information", "La substitution des ressources a été effectuée.")
}

// On error AJAX
function onErrorAjaxResources(jqXHR, textStatus, errorThrown) {
    addAlert("subResourcesAlertContainer", "alert alert-error", "Erreur", "La substitution des ressources n'a pas pu s'effectuer.")
}

// After success or error AJAX
function completeAjaxResources(jqXHR, textStatus) {
    $("#resourcesLoader").hide();
    isSubResourcesRunning = false;
}

// Update resourcesJSON before final sending
function prepareResourcesJSONForValidation(resourcesJSON) {
    var resourceArray = resourcesJSON.resources;
    for (key in resourceArray) {
        var resource = resourceArray[key];

        // Resource HTML element
        var resourceElement =  $("#" + resource.htmlId);

        // Update name
        var resourceSelect = resourceElement.children(".resource").eq(0).children("select").eq(0);
        if (resourceSelect.attr("isEmpty") == "false") {
            resource.selectedName = resourceSelect.val();
        }
        else {
            return false;
        }
    }

    return true;
}

// Select, unselect all restart
function selectAllRestartResources(restartClass, isToCheck) {
    $("." + restartClass).each( function() {
        var restartInput = $(this).eq(0).children("input").eq(0);
        if(typeof(restartInput.attr("disabled")) == "undefined") {
            restartInput.attr("checked", isToCheck);
        }
    });
}

// Initiation of resources events
function initResourcesEvents(url) {
    $("#restartResourceAll").click( function(){
        // Select all
        if (!(typeof($(this).attr("checked")) == "undefined")) {selectAllRestartResources("restartResource", true)}
        // Unselect all
        else {selectAllRestartResources("restartResource", false)}
    });

    // Unban buttons
    var resourceArray = resourcesJSON.resources;

//    if (customResource != null){
//
//        resourceArray.add(customResource);
//    }

    for (var key in resourceArray) {
        var resource = resourceArray[key];
        var resourceHtmlId = resource.htmlId
        var resourceElement =  $("#" + resourceHtmlId);

        var unbanResource = resourceElement.children(".resource").eq(0).children("a").eq(0)

        // Unban Resource
        unbanResource.click( (function(resourceHtmlId){
            return function() {

//                $('.resource #customResource').each(function() {
//                    if ($(this).val().length == 0) {
//                        var jsonObject = new Object();
//
//                    }
//                    });

                if(typeof($(this).attr("disabled")) == "undefined") {
                    // Find resourceJSON in resourceArray with resourceHtmlId
                    var resourceJSON = null;
                    var resourceArray = resourcesJSON.resources;
                    for (var key in resourceArray) {
                        var resource = resourceArray[key];
                        if (resource.htmlId == resourceHtmlId) {
                            resourceJSON = resource;
                            break;
                        }
                    }
                    // Unban
                    // Delete name selection
                    delete resourceJSON.selectedName;
                    // Copy banned names in proposed names
                    if (typeof(resourceJSON.proposedNames) == "undefined") {
                        resourceJSON.proposedNames = new Array();
                    }
                    resourceJSON.proposedNames = resourceJSON.proposedNames.concat(resourceJSON.bannedNames);
                    // Delete banned names
                    delete resourceJSON.bannedNames;

                    // Resource HTML Element
                    var resourceElement =  $("#" + resourceHtmlId);
                    // Update Resource
                    // Select
                    var resourceSelect = resourceElement.children(".resource").eq(0).children("select").eq(0);
                    resourceSelect.empty();
                    resourceSelect.css("background-color", "rgb(108,194,219)").css("color", "#FFF");
                    for (var key in resourceJSON.proposedNames) {
                        var name = resourceJSON.proposedNames[key];
                        resourceSelect.append($("<option>").attr("value", name).text(name));
                    }
                    resourceSelect.attr("isEmpty", false);
                    resourceSelect.attr("disabled", false);
                    // Unban button
                    var unbanResource = resourceElement.children(".resource").eq(0).children("a").eq(0);
                    unbanResource.attr("disabled", true);
                }
            };
        }(resourceHtmlId)));
    }

    // Run resources substitution
    $("#runSubResourcesButton").click( function(){
        if (!isSubResourcesRunning) {
            isSubResourcesRunning = true;
            // Update JSON with user selection
            updateResourcesJSONUser(resourcesJSON)

            // Enable selectAll
            $("#restartResourceAll").removeAttr("disabled");
            $("#restartResourceAll").removeAttr("disabled");



            $("#customResource").each(function() {
                $("#customResource").removeAttr("disabled")
            });

            // Show loader
            $("#resourcesLoader").show();

            console.log("resourcesJSON BEFORE sending -> ");
            console.log(resourcesJSON);

            // Send JSON to controller
            sendJSON(resourcesJSON, url, onSuccessAjaxResources, onErrorAjaxResources, completeAjaxResources);
        }
    });
}