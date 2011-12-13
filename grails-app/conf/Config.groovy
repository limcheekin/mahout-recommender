// configuration for plugin testing - will not be included in the plugin zip
 
log4j = {
    // Example of changing the log pattern for the default console
    // appender:
    //
    //appenders {
    //    console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
    //}

    error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
           'org.codehaus.groovy.grails.web.pages', //  GSP
           'org.codehaus.groovy.grails.web.sitemesh', //  layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping', // URL mapping
           'org.codehaus.groovy.grails.commons', // core / classloading
           'org.codehaus.groovy.grails.plugins', // plugins
           'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate'

    warn   'org.mortbay.log'
}

mahout.recommender.preference.table = 'taste_preferences'
mahout.recommender.preference.userIdColumn = 'user_id'
mahout.recommender.preference.itemIdColumn = 'item_id'
mahout.recommender.preference.valueColumn = 'preference'
mahout.recommender.preference.lastUpdatedColumn = 'last_updated'
mahout.recommender.slopeone.diffs.table = 'taste_slopeone_diffs'
mahout.recommender.slopeone.diffs.itemIDAColumn = 'item_id_a'
mahout.recommender.slopeone.diffs.itemIDBColumn = 'item_id_b'
mahout.recommender.slopeone.diffs.countColumn = 'count'
mahout.recommender.slopeone.diffs.avgColumn = 'average_diff'
mahout.recommender.slopeone.diffs.stdevColumn = 'standard_deviation'
mahout.recommender.slopeone.diffs.minDiffCount = 2
mahout.recommender.evaluator.trainingPercentage = 0.7
mahout.recommender.evaluator.evaluationPercentage = 1.0