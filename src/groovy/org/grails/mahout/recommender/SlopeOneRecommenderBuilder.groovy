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
import org.apache.mahout.cf.taste.impl.recommender.slopeone.MemoryDiffStorage;
import org.apache.mahout.cf.taste.impl.recommender.slopeone.SlopeOneRecommender
import org.apache.mahout.cf.taste.impl.recommender.slopeone.jdbc.MySQLJDBCDiffStorage
import org.apache.mahout.cf.taste.recommender.Recommender
import org.apache.mahout.cf.taste.recommender.slopeone.DiffStorage
import org.apache.mahout.cf.taste.model.DataModel
import org.apache.mahout.cf.taste.common.TasteException
import org.apache.mahout.cf.taste.model.JDBCDataModel
import org.apache.mahout.cf.taste.impl.recommender.slopeone.jdbc.AbstractJDBCDiffStorage
import org.apache.mahout.cf.taste.impl.recommender.slopeone.MemoryDiffStorage

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
		def grailsApp = ApplicationHolder.application
		def conf = grailsApp.config
		def model 
		DiffStorage diffStorage
		switch (conf.mahout.recommender.data.model?:MahoutRecommenderConstants.DEFAULT_DATA_MODEL) {
		case 'file':
		  model = dataModel
		  diffStorage = new MemoryDiffStorage(
			  model, withWeighting ? Weighting.WEIGHTED : Weighting.UNWEIGHTED, Long.MAX_VALUE);
			break
		case 'mysql':
			model = grailsApp.mainContext.mahoutRecommenderSupport.getDataModel(true)
			diffStorage = new MySQLJDBCDiffStorage(model, 
					conf.mahout.recommender.slopeone.diffs.table?:AbstractJDBCDiffStorage.DEFAULT_DIFF_TABLE,
					conf.mahout.recommender.slopeone.diffs.itemIDAColumn?:AbstractJDBCDiffStorage.DEFAULT_ITEM_A_COLUMN,
					conf.mahout.recommender.slopeone.diffs.itemIDBColumn?:AbstractJDBCDiffStorage.DEFAULT_ITEM_B_COLUMN,
					conf.mahout.recommender.slopeone.diffs.countColumn?:AbstractJDBCDiffStorage.DEFAULT_COUNT_COLUMN,
					conf.mahout.recommender.slopeone.diffs.avgColumn?:AbstractJDBCDiffStorage.DEFAULT_AVERAGE_DIFF_COLUMN,
					conf.mahout.recommender.slopeone.diffs.stdevColumn?:AbstractJDBCDiffStorage.DEFAULT_STDEV_COLUMN,
					conf.mahout.recommender.slopeone.diffs.minDiffCount?:MahoutRecommenderConstants.DEFAULT_SLOPEONE_DIFFS_MIN_COUNT)
		}
		Recommender recommender
		if (withWeighting) {
			recommender = new SlopeOneRecommender(model, Weighting.WEIGHTED, Weighting.WEIGHTED, diffStorage)
		} else {
			recommender = new SlopeOneRecommender(model, Weighting.UNWEIGHTED, Weighting.UNWEIGHTED, diffStorage)
		}
		return recommender
	}
}
