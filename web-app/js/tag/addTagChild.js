$("#buttonAdd").click( function(){
    var parent =  this.nextElementSibling;
    var idparent = $("#idParentSave");
    idparent.val(parent.value);
});