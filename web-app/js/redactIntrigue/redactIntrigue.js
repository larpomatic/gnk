$(function(){
    $('[data-toggle="confirmation-popout"]').confirmation({
        popout: true,
        btnOkLabel: "Oui",
        btnCancelLabel: "Non",
        btnOkClass: "btn-success",
        btnCancelClass: "btn-danger",
        onConfirm:function() {
            var objectButton = $(this).closest("div.popover").prev()
            if (objectButton.attr("data-object") == "role") {
                removeRole(objectButton);
            }
        }
    });

    $('.leftMenuList a').click(function() {
        $('.leftMenuList button').css("right", "-40px");
        var button = $(this).next();
        button.css("right", "0px");
    });

//    $(".leftMenuList i").click(function() {
//       $(".leftMenuList.tt").click();
//    });

    function removeRole(object) {
        var liObject = object.parent();
        $.ajax({
            type: "POST",
            url: object.attr("data-url"),
            success: function(data) {
                if (data.object.isdelete) {
                    liObject.remove();
                    $('.addRole').trigger("click");
                }
                else {
                    //error
                }
            },
            error: function() {
                //error
            }
        })
    }
});