grails.servlet.version = "2.5" // Change depending on target container compliance (2.5 or 3.0)
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.target.level = 1.6
grails.project.source.level = 1.6
grails.project.war.file = "target/gnk.war"
grails.server.port.http = 8090

// uncomment (and adjust settings) to fork the JVM to isolate classpaths
//grails.project.fork = [
//   run: [maxMemory:1024, minMemory:64, debug:false, maxPerm:256]
//]

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // specify dependency exclusions here; for example, uncomment this to disable ehcache:
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    checksums true // Whether to verify checksums on resolve
    legacyResolve false // whether to do a secondary resolve on plugin installation, not advised and here for backwards compatibility

    repositories {
        inherits true // Whether to inherit repository definitions from plugins

        grailsPlugins()
        grailsHome()
        grailsCentral()

        mavenLocal()
        mavenCentral()

        // uncomment these (or add new ones) to enable remote dependency resolution from public Maven repositories
        mavenRepo "http://snapshots.repository.codehaus.org"
        mavenRepo "http://repository.codehaus.org"
        mavenRepo "http://download.java.net/maven/2/"
        mavenRepo "http://repository.jboss.com/maven2/"
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes e.g.

        compile 'org.javatuples:javatuples:1.2'

        runtime 'mysql:mysql-connector-java:5.1.22'
        runtime 'org.docx4j:docx4j:2.7.1'
        runtime 'org.apache.commons:commons-lang3:3.3.2'

        test 'org.seleniumhq.selenium:selenium-java:2.41.0'
        test 'org.seleniumhq.selenium:selenium-firefox-driver:2.41.0'
        test 'org.seleniumhq.selenium:selenium-chrome-driver:2.41.0'

        test 'junit:junit:4.11'
    }

    plugins {
        runtime ":hibernate:$grailsVersion"
        runtime ":jquery:1.8.3"
        runtime ":resources:1.2.7"
        runtime ":handlebars-resources:1.3.0"
        runtime ':console:1.3'
        compile ":cookie-session:2.0.14"

        // Uncomment these (or add new ones) to enable additional resources capabilities
        //runtime ":zipped-resources:1.0"
        //runtime ":cached-resources:1.0"
        //runtime ":yui-minify-resources:0.1.5"

        build ":tomcat:$grailsVersion"

        compile ':cache:1.0.1'

        compile ':build-info:1.2.5'

        compile ':twitter-bootstrap:2.3.2'
        compile ':spring-security-core:1.2.7.3'
    }
}
    /*
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes e.g.

        compile 'org.javatuples:javatuples:1.2'

        runtime 'mysql:mysql-connector-java:5.1.22'
        runtime 'org.docx4j:docx4j:2.7.1'
        runtime 'org.apache.commons:commons-lang3:3.3.2'

        test 'org.seleniumhq.selenium:selenium-java:2.41.0'
        test 'org.seleniumhq.selenium:selenium-firefox-driver:2.41.0'
        test 'org.seleniumhq.selenium:selenium-chrome-driver:2.41.0'

        test 'junit:junit:4.11'
    }

    plugins {
        runtime ":hibernate:$grailsVersion"
        runtime ":jquery:1.8.3"
        runtime ":resources:1.2.7"
        runtime ":handlebars-resources:1.3.0"
        runtime ':console:1.3'
        compile ":cookie-session:2.0.14"

        // Uncomment these (or add new ones) to enable additional resources capabilities
        //runtime ":zipped-resources:1.0"
        //runtime ":cached-resources:1.0"
        //runtime ":yui-minify-resources:0.1.5"

        build ":tomcat:$grailsVersion"

        compile ':cache:1.0.1'

        compile ':build-info:1.2.5'

        compile ':twitter-bootstrap:2.3.2'
        compile ':spring-security-core:1.2.7.3'
    }
}
*/

