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
 * 
 * @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
 *
 * @since 0.5
 */

updateConfig()

private void updateConfig() {
	def configFile = new File(basedir, 'grails-app/conf/Config.groovy')
	if (configFile.exists() && configFile.text.indexOf("mahout.recommender.mode") == -1) {
		configFile.withWriterAppend {
			it.writeLine '\n// Added by the Mahout Recommender plugin:'
			it.writeLine "mahout.recommender.mode = 'input'  // 'input', 'config' or 'class'"

	println '''
	************************************************************
	* Your grails-app/conf/Config.groovy has been updated with *
	* default configurations of Mahout Recommender;            *
	* please verify that the values are correct.               *
	************************************************************
	'''
		}
	}
}