modules = {
    application {
        dependsOn 'handlebars_runtime'
        resource url: 'templates/notification.handlebars', attrs: [type: 'js'], bundle:'bundle_application'
        resource url: 'templates/redactIntrigue/LeftMenuLiRole.handlebars', attrs: [type: 'js'], bundle:'bundle_application'
        resource url: 'templates/redactIntrigue/LeftMenuLiEvent.handlebars', attrs: [type: 'js'], bundle:'bundle_application'
        resource url: 'templates/redactIntrigue/rolePanel.handlebars', attrs: [type: 'js'], bundle:'bundle_application'
        resource url: 'templates/redactIntrigue/eventPanel.handlebars', attrs: [type: 'js'], bundle:'bundle_application'
        resource url: 'templates/redactIntrigue/addEntityLiElement.handlebars', attrs: [type: 'js'], bundle:'bundle_application'
        resource url: 'templates/redactIntrigue/addEventInRole.handlebars', attrs: [type: 'js'], bundle:'bundle_application'
        resource url:'js/application.js'
    }
}