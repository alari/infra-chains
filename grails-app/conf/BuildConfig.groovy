grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"

grails.project.repos.default = "quonb-snapshot"

grails.project.dependency.distribution = {
    String serverRoot = "http://mvn.quonb.org"
    remoteRepository(id: 'quonb-snapshot', url: serverRoot + '/plugins-snapshot-local/')
    remoteRepository(id: 'quonb-release', url: serverRoot + '/plugins-release-local/')
}

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    legacyResolve true // whether to do a secondary resolve on plugin installation, not advised and here for backwards compatibility
    repositories {
        grailsCentral()
        // uncomment the below to enable remote dependency resolution
        // from public Maven repositories
        //mavenLocal()
        mavenCentral()

        mavenRepo "http://mvn.quonb.org/repo"
        grailsRepo "http://mvn.quonb.org/repo", "quonb"
    }
    dependencies {
        test("org.spockframework:spock-grails-support:0.7-groovy-2.0") {
            export = false
        }
    }

    plugins {
        compile ":infra-ca:0.1-SNAPSHOT"

        compile(":tomcat:$grailsVersion",
                ":release:latest.release") {
            export = false
        }

        test(":spock:0.7") {
            exclude "spock-grails-support"
            export = false
        }
    }
}
