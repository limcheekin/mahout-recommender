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
class MahoutRecommenderConstants {
	static final Integer NUM_TOP_PREFERENCES = 20
	static final Integer DEFAULT_HOW_MANY = 20
	static final Integer DEFAULT_LOAD_EVALUATION_RUNNER_TIMES = 3
	static final Integer DEFAULT_SLOPEONE_DIFFS_MIN_COUNT = 2
	static final Integer DEFAULT_BATCH_SIZE = 10000
	static final Double DEFAULT_EVALUATION_PERCENTAGE = 1.0
	static final Double DEFAULT_TRAINING_PERCENTAGE = 0.7
	static final String DEFAULT_PREFERENCE_TIME_COLUMN = 'last_updated'
	static final String DEFAULT_MODE = 'input'
	static final String DEFAULT_DATA_MODEL = 'file'
	static final String DEFAULT_DATA_FILE = 'data.csv'
}
