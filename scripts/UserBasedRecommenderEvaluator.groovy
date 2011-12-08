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

target(main: "Run recommender evaluator") {
	depends(parseArguments, bootstrap)
	ant.input(message:"Is the items have preference value?",validargs:"y,n", addproperty:"hasPreference")
	hasPreference = ant.antProject.properties["hasPreference"] == 'y'
	if (hasPreference) {
		println "1) Pearson correlation"
		println "2) Pearson correlation + weighting"
		println "3) Euclidean distance"
		println "4) Euclidean distance + weighting"
		println "5) Log-likelihood"
		println "6) Tanimoto coefficient"
		ant.input(message:"Select similarity metric:", validargs:"1,2,3,4,5,6", addproperty:"similaritySelected")
		similaritySelected = ant.antProject.properties["similaritySelected"]
		switch (similaritySelected) {
			case '1':
			case '2':
				similarity = "PearsonCorrelation"
				break
			case '3':
			case '4':
				similarity = "EuclideanDistance"
				break
			case '5':
				similarity = "LogLikelihood"
				break
			case '6':
				similarity = "TanimotoCoefficient"
		}
	} else {
		println "1) Log-likelihood"
		println "2) Tanimoto coefficient"
		ant.input(message:"Select similarity metric:", validargs:"1,2", addproperty:"similaritySelected")
		similaritySelected = ant.antProject.properties["similaritySelected"]
		switch (similaritySelected) {
			case '1':
				similarity = "LogLikelihood"
				break
			case '2':
				similarity = "TanimotoCoefficient"
		}
	}
	ant.input(message:"Enter value for nearestN neighborhood (ex: 2) or threshold neighborhood (ex: 0.7):", addproperty:"neighborhood")
	neighborhood = ant.antProject.properties["neighborhood"]
	
	ant.input(message:"Enter training percentage:", addproperty:"trainingPercentage", 
		        defaultvalue: grailsApp.config.mahout.recommender.evaluator.trainingPercentage)
	trainingPercentage = ant.antProject.properties["trainingPercentage"] as Double
	
	ant.input(message:"Enter evaluation percentage:", addproperty:"evaluationPercentage", 
		        defaultvalue: grailsApp.config.mahout.recommender.evaluator.evaluationPercentage)
	evaluationPercentage = ant.antProject.properties["evaluationPercentage"] as Double
	
	withWeighting = hasPreference && similaritySelected == '2' | similaritySelected == '4'
	MahoutRecommenderEvaluator = classLoader.loadClass("org.grails.mahout.recommender.MahoutRecommenderEvaluator")
	score = MahoutRecommenderEvaluator.evaluateUserBasedRecommender(hasPreference, similarity, 
					withWeighting, neighborhood, trainingPercentage, evaluationPercentage)
	
	echo "score = $score"
}

setDefaultTarget(main)
