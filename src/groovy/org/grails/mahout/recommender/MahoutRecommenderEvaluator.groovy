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
import org.apache.mahout.common.RandomUtils
import org.apache.mahout.cf.taste.eval.RecommenderBuilder
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator
import org.apache.mahout.cf.taste.common.TasteException
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory

/**
 *
 * @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
 *
 * @since 0.5
 */
class MahoutRecommenderEvaluator {
	static final Log LOG = LogFactory.getLog(MahoutRecommenderEvaluator.class)
	
	static Double evaluateUserBasedRecommender(Boolean hasPreference, 
																			String similarity, 
																			Boolean withWeighting, 
																			String neighborhood, 
																			Double trainingPercentage, 
																			Double evaluationPercentage) {
		LOG.debug "hasPreference = $hasPreference, similarity = $similarity, withWeighting = $withWeighting, neighborhood = $neighborhood"
		RandomUtils.useTestSeed()
		JDBCDataModel model = getDataModel(hasPreference)
		Class UserBasedRecommenderBuilder = MahoutRecommenderEvaluator.classLoader.loadClass("org.grails.mahout.recommender.UserBased${similarity}RecommenderBuilder")
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
		RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator()
		
		Double score = evaluator.evaluate(recommenderBuilder, null, model, trainingPercentage, evaluationPercentage).round(2)
		LOG.debug score
		return score
	}
																			
	static Double evaluateItemBasedRecommender(Boolean hasPreference,
																						String similarity,
																						Boolean withWeighting,
																						Double trainingPercentage,
																						Double evaluationPercentage) {
			LOG.debug "hasPreference = $hasPreference, similarity = $similarity, withWeighting = $withWeighting"
			RandomUtils.useTestSeed()
			JDBCDataModel model = getDataModel(hasPreference)
			Class ItemBasedRecommenderBuilder = MahoutRecommenderEvaluator.classLoader.loadClass("org.grails.mahout.recommender.ItemBased${similarity}RecommenderBuilder")
			RecommenderBuilder recommenderBuilder = ItemBasedRecommenderBuilder.newInstance()
			if (similarity == "LogLikelihood" | similarity == "TanimotoCoefficient") {
				recommenderBuilder.hasPreference = hasPreference
			}
			if (withWeighting) {
				recommenderBuilder.withWeighting = true
			}
			
			RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator()
			
			Double score = evaluator.evaluate(recommenderBuilder, null, model, trainingPercentage, evaluationPercentage).round(2)
			LOG.debug score
			return score
	}
																			
	private static JDBCDataModel getDataModel(Boolean hasPreference) {
		def grailsApp = ApplicationHolder.application
		JDBCDataModel model
		if (hasPreference)
			model = new MySQLJDBCDataModel(
					grailsApp.mainContext.dataSource,
					grailsApp.config.mahout.recommender.preference.table,
					grailsApp.config.mahout.recommender.preference.userIdColumn,
					grailsApp.config.mahout.recommender.preference.itemIdColumn,
					grailsApp.config.mahout.recommender.preference.valueColumn,
					grailsApp.config.mahout.recommender.preference.lastUpdatedColumn)
		else
			model = new MySQLBooleanPrefJDBCDataModel(
					grailsApp.mainContext.dataSource,
					grailsApp.config.mahout.recommender.preference.table,
					grailsApp.config.mahout.recommender.preference.userIdColumn,
					grailsApp.config.mahout.recommender.preference.itemIdColumn,
					grailsApp.config.mahout.recommender.preference.lastUpdatedColumn)
		return model
	}																		
}
