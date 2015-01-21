$(function(){
    initConfirm();
});

function initConfirm() {
    $('[data-toggle="confirmation-popout"]').confirmation({
        popout: true,
        btnOkLabel: "Oui",
        btnCancelLabel: "Non",
        btnOkClass: "btn-success",
        btnCancelClass: "btn-danger",
        onConfirm:function() {
            var button = $(this).closest(".popover").prev();
            var idTag =  button.prev();
            var url = $("#idTagurl").attr("data-url");
            $.ajax({
                type: "POST",
                url: url,
                data: "idTag="+idTag.val(),
                success: function() {
                    idTag.closest("tr").remove();
                },
                error: function() {
//                    idTag.parentNode.parentNode.remove()
                }
            })
        }
    });
}