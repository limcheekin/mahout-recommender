package org.grails.mahout.recommender

class RecommenderController {
	private static final Integer NUM_TOP_PREFERENCES = 20
	private static final Integer DEFAULT_HOW_MANY = 20
	
	def index = {
		def recommender = getRecommender()
		def userID = params.userID as Long
		def howMany = params.howMany ? params.howMany as Integer : DEFAULT_HOW_MANY
		def items = recommender.recommend(userID, howMany)
		def numOfTopPrefs
		def sortedPrefs
		if (params.debug) {
			def rawPrefs = recommender.dataModel.getPreferencesFromUser(userID)
			sortedPrefs = rawPrefs.clone()
			sortedPrefs.sortByValueReversed()
			// Cap this at NUM_TOP_PREFERENCES just to be brief
			numOfTopPrefs = Math.min(NUM_TOP_PREFERENCES, rawPrefs.length())
		}
		withFormat {
			html {
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
			}
			json {
				render(contentType:"text/json") {
					recommendedItems {
						item {
							for(recommendedItem in items) {
								i(value: recommendedItem.value, id: recommendedItem.itemID)
							}
						}
					}
				}
			}
		}
	}
	
	private getRecommender() {
		def recommender
		def conf = grailsApplication.config
		switch (conf.mahout.recommender.mode) {
			case 'input':
				def recommenderSelected = params.r as Integer
				def hasPreference = Boolean.valueOf(params.p) 
				def similarity = params.s
				def withWeighting = Boolean.valueOf(params.w) 
				def neighborhood = params.n
				recommender = MahoutRecommenderEvaluator.getRecommender(recommenderSelected,
				hasPreference, similarity, withWeighting, neighborhood)
				break
			case 'config':
				def recommenderSelected = conf.mahout.recommender.selected
				def hasPreference = conf.mahout.recommender.hasPreference
				def similarity = conf.mahout.recommender.similarity
				def withWeighting = conf.mahout.recommender.withWeighting
				def neighborhood = conf.mahout.recommender.neighborhood as String
				recommender = MahoutRecommenderEvaluator.getRecommender(recommenderSelected,
				hasPreference, similarity, withWeighting, neighborhood)
				break
			case 'class':
			  def recommenderBuilder = Class.forName(conf.mahout.recommender.builderClass).newInstance()
			  def model = MahoutRecommenderEvaluator.getDataModel(conf.mahout.recommender.hasPreference)
			  recommender = recommenderBuilder.buildRecommender()
		}
		return recommender
	}
}
