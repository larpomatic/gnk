import org.apache.log4j.DailyRollingFileAppender

// locations to search for config files that get merged into the main config;
// config files can be ConfigSlurper scripts, Java properties files, or classes
// in the classpath in ConfigSlurper format

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

// if (System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }

grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination
grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false
grails.mime.types = [
    all:           '*/*',
    atom:          'application/atom+xml',
    css:           'text/css',
    csv:           'text/csv',
    form:          'application/x-www-form-urlencoded',
    html:          ['text/html','application/xhtml+xml'],
    js:            'text/javascript',
    json:          ['application/json', 'text/json'],
    multipartForm: 'multipart/form-data',
    rss:           'application/rss+xml',
    text:          'text/plain',
    xml:           ['text/xml', 'application/xml']
]

grails.resources.modules = {}

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// What URL patterns should be processed by the resources plugin
grails.resources.adhoc.patterns = ['/images/*', '/css/*', '/js/*', '/plugins/*']

// The default codec used to encode data with ${}
grails.views.default.codec = "none" // none, html, base64
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
// enable Sitemesh preprocessing of GSP pages
grails.views.gsp.sitemesh.preprocess = true
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []
// whether to disable processing of multi part requests
grails.web.disable.multipart=false

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// configure auto-caching of queries by default (if false you can cache individual queries with 'cache: true')
grails.hibernate.cache.queries = false

// log4j configuration
String conversionPattern = '%-5p [%d{dd-MM-yyyy HH:mm:ss}] %m%n'
String conversionPatternTracking = '%d{dd-MM-yyyy_HH:mm:ss} %m%n'
log4j = {
    appenders {
        // Console.
        console name: 'stdout',
                layout: pattern(conversionPattern: conversionPattern)
        environments {
            development {
                // Tracking.
                appender new DailyRollingFileAppender(name: 'gnk_track_appender',
                        fileName: './logs/track_gnk.log',
                        layout: pattern(conversionPattern:conversionPatternTracking))
            }
            appfog {
                // Stacktrace.
                appender new DailyRollingFileAppender(
                        name: 'stacktrace',
                        fileName: './logs/root.log',
                        layout: pattern(conversionPattern:conversionPattern))
                // Tracking.
                appender new DailyRollingFileAppender(
                        name: 'gnk_track_appender',
                        fileName: './logs/track_gnk.log',
                        layout: pattern(conversionPattern:conversionPatternTracking))
                appender new DailyRollingFileAppender(
                        name: 'gnk_log_appender',
                        fileName: './logs/gnk.log',
                        layout: pattern(conversionPattern:conversionPattern))
                appender new DailyRollingFileAppender(
                        name: 'gnk_trace_appender',
                        fileName: './logs/trace_gnk.log',
                        layout: pattern(conversionPattern:conversionPattern))
            }
        }
    }
    environments {
        development {
            root { warn 'stdout' }
            warn 	stdout:	'org.codehaus.groovy.grails.web.servlet',
                            'org.codehaus.groovy.grails.web.pages',
                            'org.codehaus.groovy.grails.web.sitemesh',
                            'org.codehaus.groovy.grails.web.mapping.filter',
                            'org.codehaus.groovy.grails.web.mapping',
                            'org.codehaus.groovy.grails.commons',
                            'org.codehaus.groovy.grails.plugins',
                            'org.codehaus.groovy.grails.orm.hibernate',
                            'org.hibernate',
                            'org.springframework',
                            'net.sf.ehcache.hibernate',
                            'com.sun.jersey.spi.spring.container.servlet',
                            'org.apache.catalina',
                            'grails.app'

            error	stdout:	'com.sun.jersey.spi.container.servlet.WebComponent'
            // Tracking.
            info	stdout:	'trackLog'
            // Trace.
            debug 	stdout:	'org.gnk'
        }
        appfog {
            root { warn 'stdout' }
            error 	stdout:	'org.codehaus.groovy.grails.web.servlet',
                            'org.codehaus.groovy.grails.web.pages',
                            'org.codehaus.groovy.grails.web.sitemesh',
                            'org.codehaus.groovy.grails.web.mapping.filter',
                            'org.codehaus.groovy.grails.web.mapping',
                            'org.codehaus.groovy.grails.commons',
                            'org.codehaus.groovy.grails.plugins',
                            'org.codehaus.groovy.grails.orm.hibernate',
                            'org.springframework',
                            'org.hibernate',
                            'net.sf.ehcache.hibernate',
                            'com.sun.jersey.spi.container.servlet',
                            'com.sun.jersey.spi.spring.container.servlet',
                            'org.apache.catalina',
                            'grails.app'
            // StackTrace.
            error 	stacktrace:	'stacktrace'
            // Tracking.
            info	stdout:	'trackLog'
            // Trace.
            debug 	stdout:	'org.gnk'
        }
    }
}

// Added by the Spring Security Core plugin:
grails.plugins.springsecurity.userLookup.userDomainClassName = 'org.gnk.user.User'
grails.plugins.springsecurity.userLookup.authorityJoinClassName = 'org.gnk.user.UserSecRole'
grails.plugins.springsecurity.authority.className = 'org.gnk.user.SecRole'

grails.plugins.twitterbootstrap.fixtaglib=true