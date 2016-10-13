function sendJSON(json, url, onSuccesFunc, onErrorFunc, completeFunc) {
    $.ajax({
        type: "POST",
        url: url,
        dataType: "json",
        contentType: 'text/json',
        data: JSON.stringify(json),
        async: true,
        success : function(data, textStatus, jqXHR) {
            onSuccesFunc(data, textStatus, jqXHR);
        },
        error : function(jqXHR, textStatus, errorThrown) {
            onErrorFunc(jqXHR, textStatus, errorThrown);
        },
        complete : function(jqXHR, textStatus) {
            completeFunc(jqXHR, textStatus);
        }
    });
}

// alertClass : [alert alert-error | alert alert-success | alert alert-info]
function addAlert(ContainerId, alertClass, alertHeader, alertMsg) {
    var subAlertContainer = $("#" + ContainerId);
    var subAlertDiv = ($("<div></div>"));
    subAlertDiv.attr("class", alertClass);
    subAlertDiv.append($("<button type='button' class='close' data-dismiss='alert'>&times;</button>)"));
    subAlertDiv.append($("<strong>" + alertHeader + " ! </strong>"));
    subAlertDiv.append($("<span>" + alertMsg + "</span>"));
    subAlertContainer.append(subAlertDiv);
    subAlertDiv.delay(5000).fadeOut(2000);
}