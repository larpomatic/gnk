modules = {
    application {
        dependsOn 'handlebars_runtime'
        resource url: 'templates/notification.handlebars', attrs: [type: 'js'], bundle:'bundle_application'
        resource url: 'templates/redactIntrigue/LeftMenuLiItem.handlebars', attrs: [type: 'js'], bundle:'bundle_application'
        resource url: 'templates/redactIntrigue/rolePanel.handlebars', attrs: [type: 'js'], bundle:'bundle_application'
        resource url:'js/application.js'
    }
}