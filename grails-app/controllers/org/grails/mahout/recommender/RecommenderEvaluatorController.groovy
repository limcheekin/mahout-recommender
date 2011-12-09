package org.grails.mahout.recommender

class RecommenderEvaluatorController {
	
	def index = { }
	
	def evaluateUserBasedRecommender = {
		Double trainingPercentage = grailsApplication.config.mahout.recommender.evaluator.trainingPercentage
		Double evaluationPercentage = grailsApplication.config.mahout.recommender.evaluator.evaluationPercentage
		render MahoutRecommenderEvaluator.evaluateUserBasedRecommender(Boolean.valueOf(params.hasPreference), params.similarity,
		       Boolean.valueOf(params.withWeighting), params.neighborhood, trainingPercentage, evaluationPercentage)
	}
}
