$(function () {
    $(".fa").click(function () {
        goTo($(this).attr("data-id1"), $(this).attr("data-id"))
    })

    $(".valueButton").click(function () {
        var idTag = $(this).next().val();
        showDetails(idTag, -1);
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