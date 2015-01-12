$(".deleteTagbtn").click( function(){
    var idTag =  this.previousSibling.previousSibling;
    var url = $("#idTagurl").attr("data-url");
    $.ajax({
        type: "POST",
        url: url,
        data: "idTag="+idTag.value,
        success: function() {
            idTag.parentNode.parentNode.remove()
        },
        error: function() {
            idTag.parentNode.parentNode.remove()
        }
    })
});