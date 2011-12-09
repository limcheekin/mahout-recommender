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
includeTargets << grailsScript("_GrailsBootstrap")

target(main: "Run slope-one recommender evaluator") {
	depends(bootstrap)
	ant.input(message:"Run slope-one recommender evaluator with weighting?",validargs:"y,n", addproperty:"withWeighting")
	withWeighting = ant.antProject.properties["withWeighting"] == 'y'
	
	ant.input(message:"Enter training percentage:", addproperty:"trainingPercentage",
			defaultvalue: grailsApp.config.mahout.recommender.evaluator.trainingPercentage)
	trainingPercentage = ant.antProject.properties["trainingPercentage"] as Double
	
	ant.input(message:"Enter evaluation percentage:", addproperty:"evaluationPercentage",
			defaultvalue: grailsApp.config.mahout.recommender.evaluator.evaluationPercentage)
	evaluationPercentage = ant.antProject.properties["evaluationPercentage"] as Double
	
	MahoutRecommenderEvaluator = classLoader.loadClass("org.grails.mahout.recommender.MahoutRecommenderEvaluator")
	score = MahoutRecommenderEvaluator.evaluateSlopeOneRecommender(withWeighting, trainingPercentage, evaluationPercentage)
	
	echo "score = $score"
}

setDefaultTarget(main)
