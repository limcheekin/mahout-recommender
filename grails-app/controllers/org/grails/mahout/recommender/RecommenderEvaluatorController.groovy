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
package org.grails.mahout.recommender

/**
*
* @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
*
* @since 0.5
*/
class RecommenderEvaluatorController {
	
	def index = { }
	
	def evaluateUserBasedRecommender = {
		Double trainingPercentage = grailsApplication.config.mahout.recommender.evaluator.trainingPercentage
		Double evaluationPercentage = grailsApplication.config.mahout.recommender.evaluator.evaluationPercentage
		render MahoutRecommenderEvaluator.evaluateUserBasedRecommender(Boolean.valueOf(params.hasPreference), params.similarity,
		       Boolean.valueOf(params.withWeighting), params.neighborhood, trainingPercentage, evaluationPercentage)
	}
	
	def evaluateItemBasedRecommender = {
		Double trainingPercentage = grailsApplication.config.mahout.recommender.evaluator.trainingPercentage
		Double evaluationPercentage = grailsApplication.config.mahout.recommender.evaluator.evaluationPercentage
		render MahoutRecommenderEvaluator.evaluateItemBasedRecommender(Boolean.valueOf(params.hasPreference), params.similarity,
			   Boolean.valueOf(params.withWeighting), trainingPercentage, evaluationPercentage)
	}
	
	def evaluateSlopeOneRecommender = {
		Double trainingPercentage = grailsApplication.config.mahout.recommender.evaluator.trainingPercentage
		Double evaluationPercentage = grailsApplication.config.mahout.recommender.evaluator.evaluationPercentage
		render MahoutRecommenderEvaluator.evaluateSlopeOneRecommender(Boolean.valueOf(params.withWeighting), trainingPercentage, evaluationPercentage)
	}
}
