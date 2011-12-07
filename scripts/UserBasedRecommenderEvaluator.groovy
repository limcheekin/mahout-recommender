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

includeTargets << grailsScript("_GrailsBootstrap")

target(main: "Run recommender evaluator") {
	depends(parseArguments, bootstrap)
	String similarity = "EuclideanDistance"
	
	RandomUtils.useTestSeed()
	JDBCDataModel dataModel = new MySQLJDBCDataModel(
			grailsApp.mainContext.dataSource,
			grailsApp.config.mahout.recommender.preference.table,
			grailsApp.config.mahout.recommender.preference.userIdColumn,
			grailsApp.config.mahout.recommender.preference.itemIdColumn,
			grailsApp.config.mahout.recommender.preference.valueColumn,
			grailsApp.config.mahout.recommender.preference.lastUpdatedColumn)
	
	RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator()
	// Build the same recommender for testing that we did last time:
	UserBasedRecommenderBuilder = classLoader.loadClass("org.grails.mahout.recommender.UserBased${similarity}RecommenderBuilder")
	RecommenderBuilder recommenderBuilder = UserBasedRecommenderBuilder.newInstance()
	recommenderBuilder.threshold = 0.7
	
	// Use 70% of the data to train test using the other 30%.
	double score = evaluator.evaluate(recommenderBuilder, null, dataModel, 0.7, 1.0)
	echo "score = $score"
}

setDefaultTarget(main)
