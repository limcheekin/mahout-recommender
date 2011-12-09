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

import org.apache.mahout.cf.taste.eval.RecommenderBuilder
import org.apache.mahout.cf.taste.impl.neighborhood.*
import org.apache.mahout.cf.taste.impl.recommender.* 
import org.apache.mahout.cf.taste.recommender.Recommender
import org.apache.mahout.cf.taste.impl.similarity.*
import org.apache.mahout.cf.taste.model.DataModel
import org.apache.mahout.cf.taste.similarity.ItemSimilarity 
import org.apache.mahout.cf.taste.common.TasteException
import org.apache.mahout.cf.taste.common.Weighting

/**
*
* @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
*
* @since 0.5
*/
class ItemBasedPearsonCorrelationRecommenderBuilder implements RecommenderBuilder {
	Boolean withWeighting
	
	public Recommender buildRecommender(DataModel model) throws TasteException {
		ItemSimilarity similarity = new PearsonCorrelationSimilarity(model, 
				withWeighting ? Weighting.WEIGHTED : Weighting.UNWEIGHTED)
		return new GenericItemBasedRecommender(model, similarity)
	}
}
