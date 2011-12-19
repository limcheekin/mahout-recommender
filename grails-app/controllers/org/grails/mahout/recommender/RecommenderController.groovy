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
class RecommenderController {
	def mahoutRecommenderSupport
	
	def index = {
		def recommender
		def userID
		def items
		def numOfTopPrefs
		def sortedPrefs
		
		flash.errors = mahoutRecommenderSupport.validateEnvironmentSetup()
		if (!flash.errors) validateInput()
		if (!flash.errors) {
			recommender = getRecommender()
			userID = params.userID as Long
			def howMany = params.howMany ? params.howMany as Integer : MahoutRecommenderConstants.DEFAULT_HOW_MANY
			items = recommender.recommend(userID, howMany)

			if (params.debug) {
				def rawPrefs = recommender.dataModel.getPreferencesFromUser(userID)
				sortedPrefs = rawPrefs.clone()
				sortedPrefs.sortByValueReversed()
				// Cap this at NUM_TOP_PREFERENCES just to be brief
				numOfTopPrefs = Math.min(MahoutRecommenderConstants.NUM_TOP_PREFERENCES, rawPrefs.length())
			}
		}
		withFormat {
			html {
				if (flash.errors) {
					redirect(action: "settings", params: params)
				}
				if (params.debug) 
					return [userID: userID, 
						recommender: recommender, 
						items:items, 
						numOfTopPrefs:numOfTopPrefs, 
						prefs:sortedPrefs] 
				else 
					return [items:items]
			}
			xml {
				if (!flash.errors) {
					render(contentType:"text/xml") {
						recommendedItems {
							for(recommendedItem in items) {
								item {
									value(recommendedItem.value)
									id(recommendedItem.itemID)
								}
							}
						}
					}
				} else {
					render(contentType:"text/xml") {
						error { message(flash.errors) }
					}
				}
			}
			json {
				if (!flash.errors) {
					render(contentType:"text/json") {
						recommendedItems = array {
							for(recommendedItem in items) {
								item value:recommendedItem.value, id:recommendedItem.itemID	
							}
						}
					}
				} else {
					render(contentType:"text/json") {
            error message: flash.errors 
					}
				}
			}
		}
	}
	
	def evaluator = { }
	
	def evaluateAverageDiff = {
		Double trainingPercentage = grailsApplication.config.mahout.recommender.evaluator.trainingPercentage?:MahoutRecommenderConstants.DEFAULT_TRAINING_PERCENTAGE
		Double evaluationPercentage = grailsApplication.config.mahout.recommender.evaluator.evaluationPercentage?:MahoutRecommenderConstants.DEFAULT_EVALUATION_PERCENTAGE
		Boolean hasPreference
		
		if (params.hasPreference) hasPreference = Boolean.valueOf(params.hasPreference)
		render mahoutRecommenderSupport.evaluateAverageDifference(params.recommenderSelected as Integer, hasPreference, params.similarity,
			   Boolean.valueOf(params.withWeighting), params.neighborhood, trainingPercentage, evaluationPercentage)
	}
	
	def settings = { }
	
	private validateInput() {
		if (!params.userID) {
			flash.errors = 'User ID cannot be blank.'
			return
		} else if (!params.userID.isLong()) {
			flash.errors = 'Invalid User ID! Please enter numeric value.'
			return
		}
		
		if (!params.howMany && !params.howMany.isInteger()) {
			flash.errors = 'Invalid Number of recommendations! Please enter numeric value.'
			return
		}
		
		if (grailsApplication.config.mahout.recommender.mode == 'input') {
			if (!params.r) {
				flash.errors = 'Recommender cannot be blank.'
				return
			} else {
				def recommenderSelected = params.r as Integer
				if (recommenderSelected < 1 | recommenderSelected > 3) {
					flash.errors = 'Invalid recommender! Please enter 1, 2 or 3.'
					return
				}
			}
			
			if (!params.p) {
				flash.errors = 'Has preference cannot be blank.'
				return
			} else if (params.p != 'true' && params.p != 'false') {
				flash.errors = 'Invalid Has preference! Please enter "true" or "false".'
				return
			}
			
			if (!params.s) {
				flash.errors = 'Similarity metric cannot be blank.'
				return
			}
			
			if (!params.w) {
				flash.errors = 'With weighting cannot be blank.'
				return
			} else if (params.w != 'true' && params.w != 'false') {
				flash.errors = 'Invalid With weighting! Please enter "true" or "false".'
				return
			}
			
			if (!params.n) {
				flash.errors = 'Neighborhood cannot be blank.'
				return
			} else if (!params.n.isNumber()) {
				flash.errors = 'Invalid Neighborhood! Please enter 0.85 or 3.'
				return
			}
		}
	}
	
	private getRecommender() {
		def recommender
		def conf = grailsApplication.config
		switch (conf.mahout.recommender.mode?:MahoutRecommenderConstants.DEFAULT_MODE) {
			case 'input':
				def recommenderSelected = params.r as Integer
				def hasPreference = Boolean.valueOf(params.p) 
				def similarity = params.s
				def withWeighting = Boolean.valueOf(params.w) 
				def neighborhood = params.n
				recommender = mahoutRecommenderSupport.getRecommender(recommenderSelected,
						hasPreference, similarity, withWeighting, neighborhood)
				break
			case 'config':
				def recommenderSelected = conf.mahout.recommender.selected
				def hasPreference = conf.mahout.recommender.hasPreference
				def similarity = conf.mahout.recommender.similarity
				def withWeighting = conf.mahout.recommender.withWeighting
				def neighborhood = conf.mahout.recommender.neighborhood as String
				recommender = mahoutRecommenderSupport.getRecommender(recommenderSelected,
						hasPreference, similarity, withWeighting, neighborhood)
				break
			case 'class':
				def hasPreference = conf.mahout.recommender.hasPreference
				recommender = mahoutRecommenderSupport.getRecommender(null, hasPreference, null, null, null)
		}
		return recommender
	}
}
