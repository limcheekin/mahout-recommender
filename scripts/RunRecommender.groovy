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

includeTargets << grailsScript("_GrailsBootstrap")

target(main: "Run simple recommender") {
	depends(bootstrap)
	JDBCDataModel model = new MySQLJDBCDataModel(
			grailsApp.mainContext.dataSource, 
			grailsApp.config.mahout.recommender.preference.tableName,
			grailsApp.config.mahout.recommender.preference.userIdColumn,
			grailsApp.config.mahout.recommender.preference.itemIdColumn,
			grailsApp.config.mahout.recommender.preference.valueColumn,
			grailsApp.config.mahout.recommender.preference.lastUpdatedColumn)
	
	UserSimilarity similarity = new PearsonCorrelationSimilarity(model)
	
	UserNeighborhood neighborhood =
			new NearestNUserNeighborhood(2, similarity, model)
	
	Recommender recommender = new GenericUserBasedRecommender(
			model, neighborhood, similarity)
	
	List<RecommendedItem> recommendations = recommender.recommend(1, 1)
	
	for (RecommendedItem recommendation : recommendations) {
		ant.echo recommendation
	}
}

setDefaultTarget(main)
