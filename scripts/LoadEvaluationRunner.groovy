/* Copyright 2011 the original author or authors.
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
import org.apache.mahout.cf.taste.impl.eval.LoadEvaluator

includeTargets << new File("${mahoutRecommenderPluginDir}/scripts/_RecommenderBuilderInput.groovy")

target(main: "run load evaluation runner to evaluate performance") {
	depends(acceptInput)
	
	ant.input(message:"Enter number of times the runner execute:", addproperty:"loop", defaultvalue: '5')
	loop = ant.antProject.properties["loop"] as Integer

	/* To be supported in 0.6
	ant.input(message:"Enter number of recommendations return:", addproperty:"howMany")
	howMany = ant.antProject.properties["howMany"] as Integer
  */

	MahoutRecommenderEvaluator = classLoader.loadClass("org.grails.mahout.recommender.MahoutRecommenderEvaluator")
	recommender = MahoutRecommenderEvaluator.getRecommender(recommenderSelected, hasPreference, similarity, withWeighting, neighborhood)
 
	for (int i = 0; i < loop; i++) {
		LoadEvaluator.runLoad recommender
		/* To be supported in 0.6
		loadStats = LoadEvaluator.runLoad(recommender, howMany) 
		if (loadStats) echo "$i) $loadStats" 
		*/
	 }
}

setDefaultTarget(main)
