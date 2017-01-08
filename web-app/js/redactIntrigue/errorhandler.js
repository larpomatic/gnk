/**
 * Created by Doc on 08/12/2016.
 */
function errorhandler() {
    var test = $("#handleerror");
    $.ajax({
        type: "POST",
        url: test.attr("data-url"),
        data: form.serialize(),
        dataType: "json",
        success: function (data) {
            createNotification("warning", "dates non vérifiées", "les dates utilisées ne sont pas compatibles");
        }
    });
}

