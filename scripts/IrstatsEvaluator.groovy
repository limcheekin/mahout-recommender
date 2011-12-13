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
 
includeTargets << new File("${mahoutRecommenderPluginDir}/scripts/_RecommenderBuilderInput.groovy")

target(main: "Evaluating precision and recall") {
	depends(acceptInput)

	ant.input(message:"Enter relevance threshold:", addproperty:"relevanceThreshold")
	relevanceThresholdString = ant.antProject.properties["relevanceThreshold"]
	relevanceThreshold = relevanceThresholdString ? relevanceThresholdString as Double : Double.NaN

	ant.input(message:"Enter number of recommendations to consider (precision and recall at ... ex: 5):", addproperty:"at")
	at = ant.antProject.properties["at"] as Integer

	ant.input(message:"Enter evaluation percentage:", addproperty:"evaluationPercentage",
		defaultvalue: grailsApp.config.mahout.recommender.evaluator.evaluationPercentage)
  evaluationPercentage = ant.antProject.properties["evaluationPercentage"] as Double

	MahoutRecommenderEvaluator = classLoader.loadClass("org.grails.mahout.recommender.MahoutRecommenderEvaluator")
	stats = MahoutRecommenderEvaluator.getIRStatistics(recommenderSelected, hasPreference, similarity, 
					withWeighting, neighborhood, relevanceThreshold, at, evaluationPercentage)
	
	echo "precision = $stats.precision, recall = $stats.recall"
}

setDefaultTarget(main)
