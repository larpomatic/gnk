$(document).ready(function () {
    $("#fusionbuttonmodal").click(function () {
        var reset1 = $("#reset1");
        var reset2 = $("#reset2");
        reset1.prop("selected", true)
        reset2.prop("selected", true)
        var com2 = $("#com2");
        var com1 = $("#com1");
        com2.html("");
        com1.html("")
    });

    $("#fusionButton").click(function () {
        var loop = $("#loopPlaceList");
        var useless = $("#fusiontbodyplace");
        var place1 = $("#placeMergeable1");
        var place2 = $("#placeMergeable2");
        $.ajax({
            type: "POST",
            url: useless.attr("data-url"),
            dataType: "json",
            data: {place1: place1.val(), place2: place2.val()},
            success: function (place) {
//                loop.attr("in",placeList)
                $(".placeUnity td:contains(" + place1.val() + ")").closest("tr").remove();
                $(".placeUnity td:contains(" + place2.val() + ")").closest("tr").remove();
                var template = Handlebars.templates['templates/substitution/place'];
                var context = {
                    place: place,
                    i: $(".placeUnity").size() + 1
                };
                var html = template(context);
                $('#fusiontbodyplace').append(html);
                var count = 1;
                $('.placeUnity').each(function () {
                    $("td:first-child", this).html(count);
                    count++;
                });
                /*var index = 0;
                var placeArray = placesJSON.places;
                while (index < placeArray.length())
                {

                }*/
            }
        })
    });
    $("#placeMergeable2").change(function () {
        var place2 = $("#placeMergeable2");
        var com2 = $("#com2");
        com2.html("");
        if (place2.val() != "-1") {
            var tags2 = $(".placeUnity td:contains(" + place2.val() + ")").next().next().next().html();
            var comment2 = $(".placeUnity td:contains(" + place2.val() + ")").next().next().html();
            com2.html("Comment : <br/>" + comment2 + "<br/><br/>" + "Liste des Tags : <br/>" + tags2)
        }
        else {
            com2.html("")
        }
    });
    $("#placeMergeable1").change(function () {
        var place1 = $("#placeMergeable1");
        var place2 = $("#placeMergeable2");
        var placel = $("#placeList");
        var com1 = $("#com1");
        com1.html("");

        $('option:not([value="-1"])', place2).remove();
        if (place1.val() != "-1") {
            place2.prop("disabled", false);
            var comment1 = $(".placeUnity td:contains(" + place1.val() + ")").next().next().html();
            var tags1 = $(".placeUnity td:contains(" + place1.val() + ")").next().next().next().html();
            var url = $("#tagListurl");
            $.ajax({
                type: "POST",
                url: url.attr("data-url"),
                dataType: "json",
                data: "tagscode=" + place1.val(),
                success: function (tagList) {
                    com1.html("Comment : <br/>" + comment1 + "<br/><br/>" + "Liste des Tags : <br/>")
                    $(tagList).each(function () {
                        if (this.value == 101 || this.value == -101)
                            com1.html(com1.html() + "<span class='btn-black tagColor'> " + this.code + " : " + this.value + "</span>");
                        if ((this.value <= 100 && this.value > 75) || (this.value < -75 && this.value >= -100))
                            com1.html(com1.html() + "<span class='btn-danger tagColor'> " + this.code + " : " + this.value + "</span>");
                        if ((this.value <= 75 && this.value > 50) || (this.value < -50 && this.value >= -75))
                            com1.html(com1.html() + "<span class='btn-warning tagColor'> " + this.code + " : " + this.value + "</span>");
                        if ((this.value <= 50 && this.value > 25) || (this.value < -25 && this.value >= -50))
                            com1.html(com1.html() + "<span class='btn-primary tagColor'> " + this.code + " : " + this.value + "</span>");
                        if ((this.value <= 25 && this.value >= 0) || (this.value < 0 && this.value >= -25))
                            com1.html(com1.html() + "<span class='btn-success tagColor'> " + this.code + " : " + this.value + "</span>");
                    });
                }
            })
        }
        else {
            place2.prop("disabled", true);
            com1.html("")
        }
        $.ajax({
            type: "POST",
            url: place1.attr("data-url"),
            dataType: "json",
            data: { place1: place1.val(), placel: placel.val()},
            success: function (placeList) {
                $(placeList).each(function () {
                    place2.append('<option value="' + this.code + '">' + this.code + '</option>');
                });
            }
        })
    });
});