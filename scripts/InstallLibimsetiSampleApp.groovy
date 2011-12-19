/* Copyright 2011-12 the original author or authors.
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
includeTargets << grailsScript("Init")

target(main: "Install Libimseti Sample Application") {
	copySourceFiles()
	updateConfig()
}

private void copySourceFiles() {
	String sampleAppDir="${mahoutRecommenderPluginDir}/src/libimseti-sample-app"
	ant.copy (todir:"${basedir}/src", overwrite: true) { fileset dir:"${sampleAppDir}/src" }
}


private void updateConfig() {
	def configFile = new File(basedir, 'grails-app/conf/Config.groovy')
	
	ant.replaceregexp match:"mahout.recommender.mode = (.*)",
			replace:"mahout.recommender.mode = 'class'  // 'input', 'config' or 'class'",
			flags:"s", byline:"true", file:"${basedir}/grails-app/conf/Config.groovy"
	
	if (configFile.exists() && configFile.text.indexOf("mahout.recommender.builderClass") == -1) {
		configFile.withWriterAppend {
			it.writeLine '// Added by the Libimseti sample app:'
			it.writeLine "mahout.recommender.builderClass = 'mia.recommender.ch05.LibimsetiRecommenderBuilder'"
		}
	} else {
		ant.replaceregexp match:"mahout.recommender.builderClass = (.*)", 
				replace:"mahout.recommender.builderClass = 'mia.recommender.ch05.LibimsetiRecommenderBuilder'", 
				flags:"s", byline:"true", file:"${basedir}/grails-app/conf/Config.groovy"
	}
	
	println '''
	************************************************************
	* Your grails-app/conf/Config.groovy has been updated with *
	* default configurations of Libimseti sample application;  *
	* please verify that the values are correct.               *
	************************************************************
	'''
}

setDefaultTarget(main)
