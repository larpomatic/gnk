$(function(){
    updateEvent();

    //ajoute un nouvel évènement dans la base
    $('.insertEvent').click(function() {
        var form = $('form[name="newEventForm"]');
        var description = $('#eventRichTextEditor', form).html();
        description = transformDescription(description);
        $('.descriptionContent', form).val(description);
        var title = $('#eventTitleRichTextEditor', form).html();
        title = transformDescription(title);
        $('.titleContent', form).val(title);
        $('div[id*="roleHasEventTitleRichTextEditor"]', form).each(function() {
            $(".titleContent", $(this).closest(".tab-pane")).val(transformDescription($(this).html()));
        });
        $('div[id*="roleHasEventRichTextEditor"]', form).each(function() {
            $(".descriptionContent", $(this).closest(".tab-pane")).val(transformDescription($(this).html()));
        });
        $('div[id*="roleHasEventCommentRichTextEditor"]', form).each(function() {
            $(".commentContent", $(this).closest(".tab-pane")).val(transformDescription($(this).html()));
        });
        $('div[id*="roleHasEventEvenementialRichTextEditor"]', form).each(function() {
            $(".evenementialContent", $(this).closest(".tab-pane")).val(transformDescription($(this).html()));
        });
        $.ajax({
            type: "POST",
            url: form.attr("data-url"),
            data: form.serialize(),
            dataType: "json",
            success: function(data) {
                if (data.iscreate) {
                    createNotification("success", "Création réussie.", "Votre évènement a bien été ajouté.");
                    var template = Handlebars.templates['templates/redactIntrigue/LeftMenuLiEvent'];
                    var context = {
                        eventId: String(data.event.id),
                        eventName: data.event.name,
                        eventTiming: data.event.timing
                    };
                    var html = template(context);
                    $('.eventScreen > ul').append(html);
                    initConfirm();
                    initDeleteButton();
                    emptyEventForm();
                    createNewEventPanel(data);
                    initSearchBoxes();
                    stopClosingDropdown();
                    $('.datetimepicker').datetimepicker({
                        language: 'fr',
                        pickSeconds: false
                    });
                    $('form[name="updateEvent_' + data.event.id + '"] .btnFullScreen').click(function() {
                        $(this).parent().parent().toggleClass("fullScreenOpen");
                    });
                    var nbEvents = parseInt($('.eventsLi .badge').html()) + 1;
                    $('.eventsLi .badge').html(nbEvents);
                    initQuickObjects();
                    updateEvent();
                }
                else {
                    createNotification("danger", "création échouée.", "Votre évènement n'a pas pu être ajouté, une erreur s'est produite.");
                }
            },
            error: function() {
                createNotification("danger", "création échouée.", "Votre évènement n'a pas pu être ajouté, une erreur s'est produite.");
            }
        })
    });
});

function updateEvent() {
    // modifie un event dans la base
    $('.updateEvent').click(function() {
        var eventId = $(this).attr("data-id");
        var form = $('form[name="updateEvent_' + eventId + '"]');
        var description = $('#eventRichTextEditor' + eventId, form).html();
        description = transformDescription(description);
        $('.descriptionContent', form).val(description);
        var title = $('#eventTitleRichTextEditor' + eventId, form).html();
        title = transformDescription(title);
        $('.titleContent', form).val(title);
        $('div[id*="roleHasEventTitleRichTextEditor"]', form).each(function() {
            $(".titleContent", $(this).closest(".tab-pane")).val(transformDescription($(this).html()));
        });
        $('div[id*="roleHasEventRichTextEditor"]', form).each(function() {
            $(".descriptionContent", $(this).closest(".tab-pane")).val(transformDescription($(this).html()));
        });
        $('div[id*="roleHasEventCommentRichTextEditor"]', form).each(function() {
            $(".commentContent", $(this).closest(".tab-pane")).val(transformDescription($(this).html()));
        });
        $('div[id*="roleHasEventEvenementialRichTextEditor"]', form).each(function() {
            $(".evenementialContent", $(this).closest(".tab-pane")).val(transformDescription($(this).html()));
        });
        $.ajax({
            type: "POST",
            url: form.attr("data-url"),
            data: form.serialize(),
            dataType: "json",
            success: function(data) {
                if (data.object.isupdate) {
                    createNotification("success", "Modifications réussies.", "Votre évènement a bien été modifié.");
                    $('.eventScreen .leftMenuList a[href="#event_' + data.object.id + '"]').html(data.object.timing + "% - " + $('<div/>').text(data.object.name).html());
                    $('select[name="eventPredecessor"] option[value="' + data.object.id + '"]').html($('<div/>').text(data.object.name).html());
                    $('.roleScreen a[data-eventId="' + data.object.id + '"]').html($('<div/>').text(data.object.name).html());
                }
                else {
                    createNotification("danger", "Modifications échouées.", "Votre évènement n'a pas pu être modifié, une erreur s'est produite.");
                }
            },
            error: function() {
                createNotification("danger", "Modifications échouées.", "Votre évènement n'a pas pu être modifié, une erreur s'est produite.");
            }
        })
    });
}

// supprime un event dans la base
function removeEvent(object) {
    var liObject = object.parent();
    $.ajax({
        type: "POST",
        url: object.attr("data-url"),
        dataType: "json",
        success: function(data) {
            if (data.isDelete) {
                liObject.remove();
                var nbEvents = parseInt($('.eventsLi .badge').html()) - 1;
                $('.eventsLi .badge').html(nbEvents);
                $('.addEvent').trigger("click");
                $('input[name="placeEvent_' + data.eventId + '"]', 'ul[class*="placeEvent"]').parent().remove();
                $('.roleScreen div[id="collapseEvent-' + data.eventId + '"]').parent().remove();
                $('.roleScreen div[id*="roleEventsModal"] .accordion').each(function() {
                    var roleId = $(this).attr("id");
                    roleId = roleId.replace("accordionEvent", "");
                    $('.roleScreen div[id="collapseEvent' + roleId + "-" + data.eventId + '"]').parent().remove();
                });
                $('select[name="eventPredecessor"] option[value="' + data.eventId + '"]').remove();
                createNotification("success", "Supression réussie.", "Votre évènement a bien été supprimé.");
            }
            else {
                createNotification("danger", "suppression échouée.", "Votre évènement n'a pas pu être supprimé, une erreur s'est produite.");
            }
            return false;
        },
        error: function() {
            createNotification("danger", "suppression échouée.", "Votre évènement n'a pas pu être supprimé, une erreur s'est produite.");
        }
    })
}

//vide le formulaire d'ajout d'un event
function emptyEventForm() {
    $('form[name="newEventForm"] input[type="text"]').val("");
    $('form[name="newEventForm"] input[type="number"]').val("");
    $('form[name="newEventForm"] input[type="checkbox"]').attr('checked', false);
    $('form[name="newEventForm"] #eventPlace option[value="null"]').attr("selected", "selected");
    $('form[name="newEventForm"] #eventPredecessor option[value="null"]').attr("selected", "selected");
    $('form[name="newEventForm"] .richTextEditor').html("");
}

// créé un tab-pane du nouvel event
function createNewEventPanel(data) {
    Handlebars.registerHelper('encodeAsHtml', function(value) {
        value = value.replace(/>/g, '</span>');
        value = value.replace(/<l:/g, '<span class="label label-warning" contenteditable="false">');
        value = value.replace(/<o:/g, '<span class="label label-important" contenteditable="false">');
        value = value.replace(/<i:/g, '<span class="label label-success" contenteditable="false">');
        value = value.replace(/<u:/g, '<span class="label label-default" contenteditable="false">');
        return new Handlebars.SafeString(value);
    });
    var template = Handlebars.templates['templates/redactIntrigue/eventPanel'];
    var context = {
        event: data.event
    };
    var html = template(context);
    $('.eventScreen > .tab-content').append(html);
    var plotFullscreenEditable = $('.plotScreen .fullScreenEditable').first();
    $('.btn-group', plotFullscreenEditable).clone().prependTo('#event_' + data.event.id + ' .fullScreenEditable');
    $("#newEvent #eventPlace option").clone().appendTo('#event_' + data.event.id + ' #eventPlace');
    $("#newEvent #eventPredecessor option").clone().appendTo('#event_' + data.event.id + ' #eventPredecessor');
    $('select[name="eventPredecessor"]').append('<option value="' + data.event.id + '">' + $('<div/>').text(data.event.name).html() + '</option>');
    if (data.event.eventPredecessorId) {
        $('#event_' + data.event.id + ' #eventPredecessor option[value="'+ data.event.eventPredecessorId +'"]').attr("selected", "selected");
    }
    if (data.event.eventPlaceId) {
        $('#event_' + data.event.id + ' #eventPlace option[value="'+ data.event.eventPlaceId +'"]').attr("selected", "selected");
    }
    $('ul[class*="placeEvent"]').append('<li class="modalLi" data-name="' + $('<div/>').text(data.event.name).html() + '">' +
        '<input type="checkbox" name="placeEvent_' + data.event.id + '" id="placeEvent_' + data.event.id + '">' +
        $('<div/>').text(data.event.name).html() +
        '</li>');
    $('.roleScreen div[id*="roleEventsModal"] .accordion').each(function() {
        var roleId = $(this).attr("id");
        roleId = roleId.replace("accordionEvent", "");
        template = Handlebars.templates['templates/redactIntrigue/addEventInRole'];
        context = {
            eventId: data.event.id,
            eventName: data.event.name,
            roleId: roleId
        };
        html = template(context);
        $(this).append(html);
    });
}