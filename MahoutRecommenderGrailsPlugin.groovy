/* Copyright 2011-2012 the original author or authors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

/**
*
* @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
*
* @since 0.5
*/
class MahoutRecommenderGrailsPlugin {
    // the plugin version
    def version = "0.5.2"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.3 > *"
    // the other plugins this plugin depends on
    def dependsOn = [jquery:'1.4 > *']
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
          "grails-app/views/error.gsp",
			    "grails-app/views/layouts/main.gsp", 
				  "grails-app/conf/intro.csv",
					"lib/mysql-connector-java-5.1.18.jar"
    ]

    // TODO Fill in these fields
    def author = "Lim Chee Kin"
    def authorEmail = "limcheekin@vobject.com"
    def title = "Mahout Recommender Plugin - A Scalable Recommendation System"
    def description = '''\
The Mahout Recommender plugin enabled you to use [Apache Mahout|http://mahout.apache.org/] recommendation algorithms in your Grails project.
With the plugin, you can find an effective recommender, evaluating precision and recall, and evaluating the performance of the 
selected recommender without writing single line of code. 

* Project Site: [https://github.com/limcheekin/mahout-recommender] 
* Documentation: [http://limcheekin.github.com/mahout-recommender]
* Support: [https://github.com/limcheekin/mahout-recommender/issues] 
* Discussion Forum: [http://groups.google.com/group/grails-mahout-recommender]
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/mahout-recommender"

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional), this event occurs before 
    }

    def doWithSpring = {
		  mahoutRecommenderSupport(org.grails.mahout.recommender.MahoutRecommenderSupport)
    }

    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)
    }

    def doWithApplicationContext = { applicationContext ->
        // TODO Implement post initialization spring config (optional)
    }

    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }
}
