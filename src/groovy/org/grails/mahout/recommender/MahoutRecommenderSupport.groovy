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

import org.apache.mahout.cf.taste.impl.neighborhood.*
import org.apache.mahout.cf.taste.impl.recommender.*
import org.apache.mahout.cf.taste.impl.similarity.*
import org.apache.mahout.cf.taste.model.*
import org.apache.mahout.cf.taste.neighborhood.*
import org.apache.mahout.cf.taste.recommender.*
import org.apache.mahout.cf.taste.similarity.*
import org.apache.mahout.cf.taste.impl.model.jdbc.*
import org.apache.mahout.cf.taste.impl.model.file.*
import org.apache.mahout.common.RandomUtils
import org.apache.mahout.cf.taste.eval.RecommenderBuilder
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator
import org.apache.mahout.cf.taste.eval.IRStatistics
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator
import org.apache.mahout.cf.taste.common.TasteException
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.springframework.core.io.ClassPathResource

/**
 *
 * @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
 *
 * @since 0.5
 */
class MahoutRecommenderSupport {
	static final Log LOG = LogFactory.getLog(MahoutRecommenderSupport.class)
	
	static Double evaluateUserBasedRecommender(Boolean hasPreference, 
																			String similarity, 
																			Boolean withWeighting, 
																			String neighborhood, 
																			Double trainingPercentage, 
																			Double evaluationPercentage) {
		LOG.debug "hasPreference = $hasPreference, similarity = $similarity, withWeighting = $withWeighting, neighborhood = $neighborhood, trainingPercentage = $trainingPercentage, evaluationPercentage = $evaluationPercentage"
		RandomUtils.useTestSeed()
		DataModel model = getDataModel(hasPreference)
		
		RecommenderBuilder recommenderBuilder = getUserBasedRecommenderBuilder(hasPreference, similarity, withWeighting, neighborhood)
		
		RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator()
		
		Double score = evaluator.evaluate(recommenderBuilder, null, model, trainingPercentage, evaluationPercentage).round(2)
		LOG.debug "score = $score"
		return score
	}
	
	private static RecommenderBuilder getUserBasedRecommenderBuilder(Boolean hasPreference, 
																																	String similarity, 
																																	Boolean withWeighting, 
																																	String neighborhood) {
		Class UserBasedRecommenderBuilder = MahoutRecommenderSupport.classLoader.loadClass("org.grails.mahout.recommender.UserBased${similarity}RecommenderBuilder")
		RecommenderBuilder recommenderBuilder = UserBasedRecommenderBuilder.newInstance()
		if (similarity == "LogLikelihood" | similarity == "TanimotoCoefficient") {
			recommenderBuilder.hasPreference = hasPreference
		}
		if (withWeighting) {
			recommenderBuilder.withWeighting = true
		}
		if (neighborhood.indexOf('.') > -1) {
			recommenderBuilder.threshold = neighborhood as Double
		} else {
			recommenderBuilder.nearestN = neighborhood as Integer
		}
		return recommenderBuilder
	}				
																																																																	
	static Double evaluateItemBasedRecommender(Boolean hasPreference,
																						String similarity,
																						Boolean withWeighting,
																						Double trainingPercentage,
																						Double evaluationPercentage) {
			LOG.debug "hasPreference = $hasPreference, similarity = $similarity, withWeighting = $withWeighting, trainingPercentage = $trainingPercentage, evaluationPercentage = $evaluationPercentage"
			RandomUtils.useTestSeed()
			DataModel model = getDataModel(hasPreference)

			RecommenderBuilder recommenderBuilder = getItemBasedRecommenderBuilder(hasPreference, similarity, withWeighting)
			
			RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator()
			
			Double score = evaluator.evaluate(recommenderBuilder, null, model, trainingPercentage, evaluationPercentage).round(2)
			LOG.debug "score = $score"
			return score
	}
																						
	private static RecommenderBuilder getItemBasedRecommenderBuilder(Boolean hasPreference,
																																	String similarity,
																																	Boolean withWeighting) {
		Class ItemBasedRecommenderBuilder = MahoutRecommenderSupport.classLoader.loadClass("org.grails.mahout.recommender.ItemBased${similarity}RecommenderBuilder")
		RecommenderBuilder recommenderBuilder = ItemBasedRecommenderBuilder.newInstance()
		if (similarity == "LogLikelihood" | similarity == "TanimotoCoefficient") {
			recommenderBuilder.hasPreference = hasPreference
		}
		if (withWeighting) {
			recommenderBuilder.withWeighting = true
		}
		return recommenderBuilder
	}	  
	
	static Double evaluateSlopeOneRecommender(Boolean withWeighting,
																						Double trainingPercentage,
																						Double evaluationPercentage) {
			LOG.debug "withWeighting = $withWeighting, trainingPercentage = $trainingPercentage, evaluationPercentage = $evaluationPercentage"
			RandomUtils.useTestSeed()
			DataModel model = getDataModel(true)
			
			RecommenderBuilder recommenderBuilder = getSlopeOneRecommenderBuilder(withWeighting)

			RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator()
			
			Double score = evaluator.evaluate(recommenderBuilder, null, model, trainingPercentage, evaluationPercentage).round(2)
			LOG.debug "score = $score"
			return score
	}
																						
	private static RecommenderBuilder getSlopeOneRecommenderBuilder(Boolean withWeighting) {
		RecommenderBuilder recommenderBuilder = new SlopeOneRecommenderBuilder()
		recommenderBuilder.withWeighting = withWeighting
		return recommenderBuilder
	}
																																									
	static DataModel getDataModel(Boolean hasPreference) {
		def grailsApp = ApplicationHolder.application
		def conf = grailsApp.config
		DataModel model
		switch (conf.mahout.recommender.data.model) {
		case 'file':
		  String fileName = conf.mahout.recommender.data.file
		  LOG.info "Instanstiate file model for ${fileName}"
		  model = new FileDataModel(new ClassPathResource(fileName).file)
		  break
		case 'mysql':
			if (hasPreference)
				model = new MySQLJDBCDataModel(
						grailsApp.mainContext.dataSource,
						conf.mahout.recommender.preference.table,
						conf.mahout.recommender.preference.userIdColumn,
						conf.mahout.recommender.preference.itemIdColumn,
						conf.mahout.recommender.preference.valueColumn,
						conf.mahout.recommender.preference.lastUpdatedColumn)
			else
				model = new MySQLBooleanPrefJDBCDataModel(
						grailsApp.mainContext.dataSource,
						conf.mahout.recommender.preference.table,
						conf.mahout.recommender.preference.userIdColumn,
						conf.mahout.recommender.preference.itemIdColumn,
						conf.mahout.recommender.preference.lastUpdatedColumn)
			break
		}	
		return model
	}							
	
	static IRStatistics getIRStatistics(Integer recommenderSelected, Boolean hasPreference, String similarity,
		Boolean withWeighting, String neighborhood, Double relevanceThreshold, Integer at, Double evaluationPercentage) {
		LOG.debug "recommenderSelected = $recommenderSelected, hasPreference = $hasPreference, similarity = $similarity, withWeighting = $withWeighting, neighborhood = $neighborhood, relevanceThreshold = $relevanceThreshold, at = $at, evaluationPercentage = $evaluationPercentage"
		RecommenderBuilder recommenderBuilder = getRecommenderBuilder(recommenderSelected, hasPreference, similarity, withWeighting, neighborhood)
		
		RandomUtils.useTestSeed()

		DataModel model = getDataModel(hasPreference)
  
	  RecommenderIRStatsEvaluator evaluator = new GenericRecommenderIRStatsEvaluator()

	  IRStatistics stats = evaluator.evaluate(recommenderBuilder,
											  null, model, null, at,
											  relevanceThreshold, evaluationPercentage)
	  
	  LOG.debug "precision = $stats.precision, recall = $stats.recall"
	  return stats
	}
		
	private static RecommenderBuilder getRecommenderBuilder(Integer recommenderSelected, Boolean hasPreference, String similarity,
		Boolean withWeighting, String neighborhood) {
		LOG.debug "recommenderSelected = $recommenderSelected, hasPreference = $hasPreference, similarity = $similarity, withWeighting = $withWeighting, neighborhood = $neighborhood"
		RecommenderBuilder recommenderBuilder
			
		switch (recommenderSelected) {
			case 1:
			  recommenderBuilder = getUserBasedRecommenderBuilder(hasPreference, similarity, withWeighting, neighborhood)
			  break
			case 2:
				recommenderBuilder = getItemBasedRecommenderBuilder(hasPreference, similarity, withWeighting)
				break
			case 3:
		    recommenderBuilder = getSlopeOneRecommenderBuilder(withWeighting)
		}
		
		return recommenderBuilder
	}
		
	static Recommender getRecommender(Integer recommenderSelected, Boolean hasPreference, String similarity,
			Boolean withWeighting, String neighborhood) {
			LOG.debug "recommenderSelected = $recommenderSelected, hasPreference = $hasPreference, similarity = $similarity, withWeighting = $withWeighting, neighborhood = $neighborhood"
			RecommenderBuilder recommenderBuilder = getRecommenderBuilder(recommenderSelected, hasPreference, similarity, withWeighting, neighborhood)
	
			DataModel model = getDataModel(hasPreference)
	  
			return recommenderBuilder.buildRecommender(model)
		}
}
