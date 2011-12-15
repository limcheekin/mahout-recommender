// configuration for plugin testing - will not be included in the plugin zip
 
log4j = {
    // Example of changing the log pattern for the default console
    // appender:
    //
    // appenders {
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
	
	  info 'org.apache.mahout.cf.taste.impl.eval'
	  info 'org.grails.mahout.recommender'
}

// content negotiation support
grails.mime.types = [ xml: ['text/xml', 'application/xml'],
	text: 'text-plain',
	js: 'text/javascript',
	rss: 'application/rss+xml',
	atom: 'application/atom+xml',
	css: 'text/css',
	csv: 'text/csv',
	all: '*/*',
	json: 'text/json',
	html: ['text/html','application/xhtml+xml']
  ]

mahout.recommender.mode = 'input'  // input, config or class
mahout.recommender.data.model = 'file'  // file or mysql
mahout.recommender.data.file = 'intro.csv'
mahout.recommender.builderClass = 'your.custom.RecommenderBuilder'
mahout.recommender.hasPreference = true
mahout.recommender.selected = 1  // 1. user-based, 2. item-based or 3. slope-one
mahout.recommender.similarity = 'LogLikelihood'
mahout.recommender.withWeighting = false
mahout.recommender.neighborhood = 4

mahout.recommender.preference.table = 'taste_preferences'
mahout.recommender.preference.userIdColumn = 'user_id'
mahout.recommender.preference.itemIdColumn = 'item_id'
mahout.recommender.preference.valueColumn = 'preference'
mahout.recommender.preference.timestampColumn = 'last_updated'
mahout.recommender.slopeone.diffs.table = 'taste_slopeone_diffs'
mahout.recommender.slopeone.diffs.itemIDAColumn = 'item_id_a'
mahout.recommender.slopeone.diffs.itemIDBColumn = 'item_id_b'
mahout.recommender.slopeone.diffs.countColumn = 'count'
mahout.recommender.slopeone.diffs.avgColumn = 'average_diff'
mahout.recommender.slopeone.diffs.stdevColumn = 'standard_deviation'
mahout.recommender.slopeone.diffs.minDiffCount = 2
mahout.recommender.evaluator.trainingPercentage = 0.7
mahout.recommender.evaluator.evaluationPercentage = 1.0