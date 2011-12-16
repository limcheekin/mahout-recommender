package mia.recommender.ch05

import org.apache.mahout.cf.taste.eval.RecommenderBuilder
import org.apache.mahout.cf.taste.model.DataModel
import org.apache.mahout.cf.taste.recommender.Recommender
import org.apache.mahout.cf.taste.common.TasteException

class LibimsetiRecommenderBuilder implements RecommenderBuilder {
	public Recommender buildRecommender(DataModel model) throws TasteException {
      return new LibimsetiRecommender(model)
	}
}
