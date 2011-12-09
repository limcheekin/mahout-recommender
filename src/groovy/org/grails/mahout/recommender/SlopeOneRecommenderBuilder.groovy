/* Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
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

import org.apache.mahout.cf.taste.eval.RecommenderBuilder
import org.apache.mahout.cf.taste.common.Weighting
import org.apache.mahout.cf.taste.impl.recommender.slopeone.SlopeOneRecommender
import org.apache.mahout.cf.taste.impl.recommender.slopeone.jdbc.MySQLJDBCDiffStorage
import org.apache.mahout.cf.taste.recommender.Recommender
import org.apache.mahout.cf.taste.recommender.slopeone.DiffStorage
import org.apache.mahout.cf.taste.model.DataModel
import org.apache.mahout.cf.taste.common.TasteException
import org.apache.mahout.cf.taste.model.JDBCDataModel

import org.codehaus.groovy.grails.commons.ApplicationHolder

/**
 *
 * @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
 *
 * @since 0.5
 */
class SlopeOneRecommenderBuilder implements RecommenderBuilder {
	Boolean withWeighting
	
	public Recommender buildRecommender(DataModel dataModel) throws TasteException {
		def config = ApplicationHolder.application.config
		JDBCDataModel model = MahoutRecommenderEvaluator.getDataModel(true)
		DiffStorage diffStorage = new MySQLJDBCDiffStorage(model, 
				config.mahout.recommender.slopeone.diffs.table,
				config.mahout.recommender.slopeone.diffs.itemIDAColumn,
				config.mahout.recommender.slopeone.diffs.itemIDBColumn,
				config.mahout.recommender.slopeone.diffs.countColumn,
				config.mahout.recommender.slopeone.diffs.avgColumn,
				config.mahout.recommender.slopeone.diffs.stdevColumn,
				config.mahout.recommender.slopeone.diffs.minDiffCount)
		Recommender recommender
		if (withWeighting) {
			recommender = new SlopeOneRecommender(model, Weighting.WEIGHTED, Weighting.WEIGHTED, diffStorage)
		} else {
			recommender = new SlopeOneRecommender(model, Weighting.UNWEIGHTED, Weighting.UNWEIGHTED, diffStorage)
		}
		return recommender
	}
}
