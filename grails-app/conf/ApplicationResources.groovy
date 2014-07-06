modules = {
    application {
        dependsOn 'handlebars_runtime'
        resource url: 'templates/notification.handlebars', attrs: [type: 'js'], bundle:'bundle_application'
        resource url: 'templates/redactIntrigue/LeftMenuLiRole.handlebars', attrs: [type: 'js'], bundle:'bundle_application'
        resource url: 'templates/redactIntrigue/LeftMenuLiRoleRelation.handlebars', attrs: [type: 'js'], bundle:'bundle_application'
        resource url: 'templates/redactIntrigue/LeftMenuLiEvent.handlebars', attrs: [type: 'js'], bundle:'bundle_application'
        resource url: 'templates/redactIntrigue/LeftMenuLiPastScene.handlebars', attrs: [type: 'js'], bundle:'bundle_application'
        resource url: 'templates/redactIntrigue/LeftMenuLiGenericPlace.handlebars', attrs: [type: 'js'], bundle:'bundle_application'
        resource url: 'templates/redactIntrigue/LeftMenuLiGenericResource.handlebars', attrs: [type: 'js'], bundle:'bundle_application'
        resource url: 'templates/redactIntrigue/rolePanel.handlebars', attrs: [type: 'js'], bundle:'bundle_application'
        resource url: 'templates/redactIntrigue/genericPlacePanel.handlebars', attrs: [type: 'js'], bundle:'bundle_application'
        resource url: 'templates/redactIntrigue/genericResourcePanel.handlebars', attrs: [type: 'js'], bundle:'bundle_application'
        resource url: 'templates/redactIntrigue/eventPanel.handlebars', attrs: [type: 'js'], bundle:'bundle_application'
        resource url: 'templates/redactIntrigue/relationPanel.handlebars', attrs: [type: 'js'], bundle:'bundle_application'
        resource url: 'templates/redactIntrigue/pastScenePanel.handlebars', attrs: [type: 'js'], bundle:'bundle_application'
        resource url: 'templates/redactIntrigue/addEntityLiElement.handlebars', attrs: [type: 'js'], bundle:'bundle_application'
        resource url: 'templates/redactIntrigue/addEventInRole.handlebars', attrs: [type: 'js'], bundle:'bundle_application'
        resource url: 'templates/redactIntrigue/addPastSceneInRole.handlebars', attrs: [type: 'js'], bundle:'bundle_application'
        resource url: 'js/application.js'
    }
}