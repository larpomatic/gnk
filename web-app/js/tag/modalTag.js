$(function () {
    $(".fa").click(function () {
        goTo($(this).attr("data-id1"), $(this).attr("data-id"))
    });

    $(".valueButton").click(function () {
        var idTag = $(this).next().val();
        showDetails(idTag, -1);
    });

    $(".editButton").click(function () {
        var idTag = $(this).next().val();
        editTag(idTag);
    })

});

function showDetails(idTag, startId) {
    var url = $("#idTagInformationurl").attr("data-url");
    var src = $("#path").val();
    var modal = $("#modalValue" + idTag);
    if (modal.size() == 0) {
        $.ajax({
            type: "POST",
            url: url,
            data: "idTag=" + idTag,
            dataType: "json",
            success: function (jsonTag) {
                var template = Handlebars.templates['templates/tag/modalshow'];
                var context = {
                    jsonTag: jsonTag,
                    src: src
                };
                var html = template(context);
                $('#modalViewTag').append(html);
                var modal = $("#modalValue" + idTag);
                modal.modal("show");
                if (startId != idTag && startId != -1) {
                    $('#fa-container' + idTag).children('#fa' + startId).remove();
                    $('#fa' + startId).clone().appendTo('#fa-container' + idTag);
                    $('#fa' + idTag).remove();
                }
                $(".fa").click(function () {
                    modal.modal("hide");
                    if (startId == -1) {
                        showDetails($(this).attr("data-id1"), idTag);
                    }
                    else {
                        showDetails($(this).attr("data-id1"), startId);
                    }
                });
            }
        })
    }
    else
    {
        modal.modal("show");
        if (startId != idTag && startId != -1) {
            $('#fa-container' + idTag).children('#fa' + startId).remove();
            $('#fa' + startId).clone().appendTo('#fa-container' + idTag);
            $('#fa' + idTag).remove();
        }
        $(".fa").click(function () {
            modal.modal("hide");
            showDetails($(this).attr("data-id1"), startId);
        });
    }
}

function editTag(idTag){
    var url = $("#idTagediturl").attr("data-url");
    var modal = $("#editmodal" + idTag);
    if (modal.size() != 0) {
        modal.remove()
    }
        $.ajax({
            type: "POST",
            url: url,
            data: "idTag=" + idTag,
            dataType: "json",
            success: function (jsonTag) {
                var isTagFN;
                if (jsonTag.tagRelevantFn == "true"){
                    isTagFN = "checked"
                }
                else
                {
                    isTagFN = ""
                }
                var isTagLN;
                if (jsonTag.tagRelevantLn == "true"){
                    isTagLN = "checked"
                }
                else
                {
                    isTagLN = ""
                }
                var isTagRS;
                if (jsonTag.tagRelevantRs == "true"){
                    isTagRS = "checked"
                }
                else
                {
                    isTagRS = ""
                }
                var isTagPlot;
                if (jsonTag.tagRelevantPlot == "true"){
                    isTagPlot = "checked"
                }
                else
                {
                    isTagPlot = ""
                }
                var isTagPL;
                if (jsonTag.tagRelevantPlace == "true"){
                    isTagPL = "checked"
                }
                else
                {
                    isTagPL = ""
                }

                var isTagRL;
                if (jsonTag.tagRelevantRole == "true"){
                    isTagRL = "checked"
                }
                else
                {
                    isTagRL = ""
                }
                var template = Handlebars.templates['templates/tag/modaledit'];
                var context = {
                    jsonTag: jsonTag,
                    isTagRFN : isTagFN,
                    isTagLN : isTagLN,
                    isTagRS : isTagRS,
                    isTagPlot : isTagPlot,
                    isTagPL :  isTagPL,
                    isTagRL : isTagRL
                };
                var html = template(context);
                $('#modalEditTag').append(html);
                var modal = $("#editmodal" + idTag);
                $("#modalValue" + idTag).remove()
                modal.modal("show");

            }
        })
}