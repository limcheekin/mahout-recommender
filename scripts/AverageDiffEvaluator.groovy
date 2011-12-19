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

target(main: "Evaluating average difference") {
	depends(acceptInput)

	ant.input(message:"Enter training percentage:", addproperty:"trainingPercentage", 
		        defaultvalue: conf.mahout.recommender.evaluator.trainingPercentage?:MahoutRecommenderConstants.DEFAULT_TRAINING_PERCENTAGE)
	trainingPercentage = ant.antProject.properties["trainingPercentage"] as Double
	
	ant.input(message:"Enter evaluation percentage:", addproperty:"evaluationPercentage",
		        defaultvalue: conf.mahout.recommender.evaluator.evaluationPercentage?:MahoutRecommenderConstants.DEFAULT_EVALUATION_PERCENTAGE)
  evaluationPercentage = ant.antProject.properties["evaluationPercentage"] as Double

	score = mahoutRecommenderSupport.evaluateAverageDifference(recommenderSelected, hasPreference, similarity, 
					withWeighting, neighborhood, trainingPercentage, evaluationPercentage)
	
	printMessage "score = $score"
}

setDefaultTarget(main)
