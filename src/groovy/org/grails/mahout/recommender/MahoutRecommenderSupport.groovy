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
	
	String validateEnvironmentSetup() {
		def conf = ApplicationHolder.application.config
		String model = conf.mahout.recommender.data.model?:MahoutRecommenderConstants.DEFAULT_DATA_MODEL
		String error = null
		if (model == 'file' && !conf.mahout.recommender.data.file) {
			error = "Data model supported is 'file'. Please specify data file name for conf.mahout.recommenderd.data.file in Config.groovy."
		}
		return error
	}
	
	Double evaluateAverageDifference(Integer recommenderSelected,
	                                 Boolean hasPreference,
	                                 String similarity,
	                                 Boolean withWeighting,
	                                 String neighborhood,
	                                 Double trainingPercentage,
	                                 Double evaluationPercentage) {
		LOG.debug "recommenderSelected = $recommenderSelected, hasPreference = $hasPreference, similarity = $similarity, withWeighting = $withWeighting, neighborhood = $neighborhood, trainingPercentage = $trainingPercentage, evaluationPercentage = $evaluationPercentage"
		RandomUtils.useTestSeed()
		
		DataModel model = getDataModel(hasPreference)
		
		RecommenderBuilder recommenderBuilder = getRecommenderBuilder(recommenderSelected, hasPreference, similarity, withWeighting, neighborhood)
		
		RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator()
		
		Double score = evaluator.evaluate(recommenderBuilder, null, model, trainingPercentage, evaluationPercentage).round(2)
		
		LOG.debug "score = $score"
		
		return score
	}
	
	private RecommenderBuilder getUserBasedRecommenderBuilder(Boolean hasPreference, 
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
	
	private RecommenderBuilder getItemBasedRecommenderBuilder(Boolean hasPreference,
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
	
	private RecommenderBuilder getSlopeOneRecommenderBuilder(Boolean withWeighting) {
		RecommenderBuilder recommenderBuilder = new SlopeOneRecommenderBuilder()
		recommenderBuilder.withWeighting = withWeighting
		return recommenderBuilder
	}
	
	DataModel getDataModel(Boolean hasPreference) {
		def grailsApp = ApplicationHolder.application
		def conf = grailsApp.config
		DataModel model
		switch (conf.mahout.recommender.data.model?:MahoutRecommenderConstants.DEFAULT_DATA_MODEL) {
			case 'file':
				String fileName = conf.mahout.recommender.data.file
				LOG.info "Instanstiate file model for ${fileName}"
				model = new FileDataModel(new ClassPathResource(fileName).file)
				break
			case 'mysql':
				if (hasPreference)
					model = new MySQLJDBCDataModel(
							grailsApp.mainContext.dataSource,
							conf.mahout.recommender.preference.table?:AbstractJDBCDataModel.DEFAULT_PREFERENCE_TABLE,
							conf.mahout.recommender.preference.userIdColumn?:AbstractJDBCDataModel.DEFAULT_USER_ID_COLUMN,
							conf.mahout.recommender.preference.itemIdColumn?:AbstractJDBCDataModel.DEFAULT_ITEM_ID_COLUMN,
							conf.mahout.recommender.preference.valueColumn?:AbstractJDBCDataModel.DEFAULT_PREFERENCE_COLUMN,
							conf.mahout.recommender.preference.timestampColumn?:MahoutRecommenderConstants.DEFAULT_PREFERENCE_TIME_COLUMN)
				else
					model = new MySQLBooleanPrefJDBCDataModel(
							grailsApp.mainContext.dataSource,
							conf.mahout.recommender.preference.table?:AbstractJDBCDataModel.DEFAULT_PREFERENCE_TABLE,
							conf.mahout.recommender.preference.userIdColumn?:AbstractJDBCDataModel.DEFAULT_USER_ID_COLUMN,
							conf.mahout.recommender.preference.itemIdColumn?:AbstractJDBCDataModel.DEFAULT_ITEM_ID_COLUMN,
							conf.mahout.recommender.preference.timestampColumn?:MahoutRecommenderConstants.DEFAULT_PREFERENCE_TIME_COLUMN)
				break
		}	
		return model
	}							
	
	IRStatistics getIRStatistics(Integer recommenderSelected, Boolean hasPreference, String similarity,
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
	
	private RecommenderBuilder getRecommenderBuilder(Integer recommenderSelected, Boolean hasPreference, String similarity,
	                                                 Boolean withWeighting, String neighborhood) {
		LOG.debug "recommenderSelected = $recommenderSelected, hasPreference = $hasPreference, similarity = $similarity, withWeighting = $withWeighting, neighborhood = $neighborhood"
		RecommenderBuilder recommenderBuilder
		def conf = ApplicationHolder.application.config
		String mode = conf.mahout.recommender.mode?:MahoutRecommenderConstants.DEFAULT_MODE
		
		switch (mode) {
			case 'input':
			case 'config':	
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
				break
			case 'class':
				recommenderBuilder = getCustomRecommenderBuilder(conf)
		}	
		return recommenderBuilder
	}
	
	private RecommenderBuilder getCustomRecommenderBuilder(conf) {
		MahoutRecommenderSupport.classLoader.loadClass(conf.mahout.recommender.builderClass).newInstance()
	}
	
	Recommender getRecommender(Integer recommenderSelected, Boolean hasPreference, String similarity,
	                           Boolean withWeighting, String neighborhood) {
		LOG.debug "recommenderSelected = $recommenderSelected, hasPreference = $hasPreference, similarity = $similarity, withWeighting = $withWeighting, neighborhood = $neighborhood"
		RecommenderBuilder recommenderBuilder = getRecommenderBuilder(recommenderSelected, hasPreference, similarity, withWeighting, neighborhood)
		
		DataModel model = getDataModel(hasPreference)
		
		return recommenderBuilder.buildRecommender(model)
	}
}
