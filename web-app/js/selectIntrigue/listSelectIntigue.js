/**
 * Created by test on 24/01/2017.
 */
$(document).ready(function () {

    var DELAY = 700, clicks = 0, timer = null;
    $(".tet").on("click", function (e) {

        clicks++;  //count clicks

        if (clicks === 1) {

            timer = setTimeout(function () {

                alert("Single Click");  //perform single-click action
                clicks = 0;             //after action performed, reset counter

            }, DELAY);

        } else {

            clearTimeout(timer);    //prevent single-click action
            alert("Double Click");  //perform double-click action
            clicks = 0;             //after action performed, reset counter
        }

    })
        .on("dblclick", function (i) {
            i.stopPropagation();
            var currentEle = $(this);
            var value = $(this).html();
            updateVal(currentEle, value);  //cancel system double-click event
        });
});
/*
$(".tet").dblclick(function (i) {
    i.stopPropagation();
    var currentEle = $(this);
    var value = $(this).html();
    updateVal(currentEle, value);
});
$(".tet").click(function (i) {
    i.stopPropagation();
    $(this).html("YES");
});*/

function updateVal(currentEle, value) {
    $(currentEle).html('<input class="thVal" type="text" value="' + value + '" />');
    $(".thVal").focus();
    $(".thVal").keyup(function (event) {
        if (event.keyCode == 13) {
            $(currentEle).html($(".thVal").val().trim());
        }
    });

    $(document).click(function () { // you can use $('html')
        $(currentEle).html($(".thVal").val().trim());
    });
}