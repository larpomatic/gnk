if (typeof jQuery !== 'undefined') {
	(function($) {
		$('#spinner').ajaxStart(function() {
			$(this).fadeIn();
		}).ajaxStop(function() {
			$(this).fadeOut();
		});


	})(jQuery);

    $(function() {
        $('.search-query').keyup(function() {
            var content = $(this).attr("data-content");
            var value = $(this).val().toLowerCase();
            if (value == "") {
                $('.' + content + ' li').show();
            }
            else {
                $('.' + content + ' li').hide();
                var children = $('.' + content + ' li[data-name*="'+value+'"]');
                children.show();
                children.parents("*").show();
            }
        });

        // initialisation des tags
        $('.tags-modal li').each(function() {
            hideTags($('input[type="checkbox"]', $(this)).attr("id"), $(".tagWeight input", $(this)).attr("id"));
        });

        $('.chooseTag').click(function() {
            $('input', $(this).parent().prev()).val(101);
        });

        $('.banTag').click(function() {
            $('input', $(this).parent().next()).val(-101);
        });
    });

    function createNotification(classe, title, description) { //classe = error, danger, info, success
        var template = Handlebars.templates['templates/notification'];
        var context = {
            classe: classe,
            title: title,
            description: description
        };
        var html = template(context);
        $(html).appendTo("body");
        if (classe === "success") {
            setTimeout(function () {
                $('.alert-success, .alert-info').remove();
            }, 4000);
        }
    }

    function hideTags(checkboxID, toggleID) {
        var checkbox = $("#" + checkboxID);
        var toggle = $("#" + toggleID);

        if (!checkbox.prop("checked")) {
            toggle.attr("disabled", "disabled");
            toggle.parent().prev().addClass("invisible");
            toggle.parent().next().addClass("invisible");
        }
        else {
            toggle.removeAttr("disabled");
            toggle.parent().prev().removeClass("invisible");
            toggle.parent().next().removeClass("invisible");
        }
    }
}

