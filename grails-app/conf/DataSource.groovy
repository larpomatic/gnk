hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory'
}
// environment specific settings
environments {
    development {
        dataSource {
            // Log SQL.
            loggingSql = false

            dbCreate = "validate"
            url = "jdbc:mysql://localhost/gnkdb?useUnicode=yes&characterEncoding=UTF-8"
            pooled = true
            driverClassName = "com.mysql.jdbc.Driver"
            dialect = org.hibernate.dialect.MySQL5InnoDBDialect
            username = "gnk"
            password = ""
            properties {
                maxActive = 50
                maxIdle = 25
                minIdle = 5
                initialSize = 5
                minEvictableIdleTimeMillis = 60000
                timeBetweenEvictionRunsMillis = 60000
                maxWait = 10000
                validationQuery = "/* ping */"
            }
        }
    }
    test {
        dataSource {
            // Log SQL.
            // loggingSql = true

            dbCreate = "validate"
            url = "jdbc:mysql://localhost/gnkdb"
            pooled = true
            driverClassName = "com.mysql.jdbc.Driver"
            dialect = org.hibernate.dialect.MySQL5InnoDBDialect
            username = "gnk"
            password = ""
            properties {
                maxActive = 50
                maxIdle = 25
                minIdle = 5
                initialSize = 5
                minEvictableIdleTimeMillis = 60000
                timeBetweenEvictionRunsMillis = 60000
                maxWait = 10000
                validationQuery = "/* ping */"
            }
        }
    }
    rec {
        dataSource {
            pooled = true
            dbCreate = 'validate'
            url = 'jdbc:mysql://localhost/gnkdb_rec?useUnicode=yes&characterEncoding=UTF-8'
            driverClassName = "com.mysql.jdbc.Driver"
            username = 'gnkdb_rec'
            password = 'gnkdb_rec'
            properties {
                maxActive = 50
                maxIdle = 25
                minIdle = 5
                initialSize = 5
                minEvictableIdleTimeMillis = 60000
                timeBetweenEvictionRunsMillis = 60000
                maxWait = 10000
                validationQuery = 'SELECT 1 FROM DUAL'
            }
        }
    }
    production {
        dataSource {
            pooled = true
            dbCreate = 'validate'
            url = 'jdbc:mysql://localhost/gnkdb?useUnicode=yes&characterEncoding=UTF-8'
            driverClassName = "com.mysql.jdbc.Driver"
            username = 'gnkdb'
            password = 's1ck8X0341h56tc'
            properties {
                maxActive = 50
                maxIdle = 25
                minIdle = 5
                initialSize = 5
                minEvictableIdleTimeMillis = 60000
                timeBetweenEvictionRunsMillis = 60000
                maxWait = 10000
                validationQuery = 'SELECT 1 FROM DUAL'
            }
        }
    }
}