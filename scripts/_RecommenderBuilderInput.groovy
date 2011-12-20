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

includeTargets << new File("${mahoutRecommenderPluginDir}/scripts/_Common.groovy")

target(acceptInput: "Accept recommender builder input") {
	depends(bootstrap)
	hasPreference = false
	similarity = null
	withWeighting = false  
	neighborhood = null
	recommenderSelected = null 
	conf = grailsApp.config
	mahoutRecommenderSupport = grailsApp.mainContext.mahoutRecommenderSupport
	MahoutRecommenderConstants = classLoader.loadClass("org.grails.mahout.recommender.MahoutRecommenderConstants")
	
	if (!conf.mahout.recommender.data.model) {
		ant.input(message:"Enter data model:",validargs:"file,mysql", addproperty:"dataModel", defaultvalue: MahoutRecommenderConstants.DEFAULT_DATA_MODEL)
		conf.mahout.recommender.data.model = ant.antProject.properties["dataModel"]
	 } 
	
	if (conf.mahout.recommender.data.model == 'file' && !conf.mahout.recommender.data.file) {
		ant.input(message:"Enter data file name:", addproperty:"file", defaultvalue: MahoutRecommenderConstants.DEFAULT_DATA_FILE)
		conf.mahout.recommender.data.file = ant.antProject.properties["file"]
	 }
	
	switch (conf.mahout.recommender.mode?:MahoutRecommenderConstants.DEFAULT_MODE) {
		case 'input':
			println "\t1) User-based recommender"
			println "\t2) Item-based recommender"
			println "\t3) Slope-one recommender"
			ant.input(message:"Select a recommender:",validargs:"1,2,3", addproperty:"recommenderSelected")
			recommenderSelected = ant.antProject.properties["recommenderSelected"] as Integer
			
			if (recommenderSelected == 1 || recommenderSelected == 2) {
				ant.input(message:"Is the items have preference value?",validargs:"y,n", addproperty:"hasPreference")
				hasPreference = ant.antProject.properties["hasPreference"] == 'y'
				
				if (hasPreference) {
					println "\t1) Pearson correlation"
					println "\t2) Pearson correlation + weighting"
					println "\t3) Euclidean distance"
					println "\t4) Euclidean distance + weighting"
					println "\t5) Log-likelihood"
					println "\t6) Tanimoto coefficient"
					ant.input(message:"Select a similarity metric:", validargs:"1,2,3,4,5,6", addproperty:"similaritySelected")
					similaritySelected = ant.antProject.properties["similaritySelected"]
					switch (similaritySelected) {
						case '1':
						case '2':
							similarity = "PearsonCorrelation"
							break
						case '3':
						case '4':
							similarity = "EuclideanDistance"
							break
						case '5':
							similarity = "LogLikelihood"
							break
						case '6':
							similarity = "TanimotoCoefficient"
					}
				} else {
					println "\t1) Log-likelihood"
					println "\t2) Tanimoto coefficient"
					ant.input(message:"Select similarity metric:", validargs:"1,2", addproperty:"similaritySelected")
					similaritySelected = ant.antProject.properties["similaritySelected"]
					switch (similaritySelected) {
						case '1':
							similarity = "LogLikelihood"
							break
						case '2':
							similarity = "TanimotoCoefficient"
					}
				}
				withWeighting = hasPreference && similaritySelected == '2' | similaritySelected == '4'
				if (recommenderSelected == 1) {
					ant.input(message:"Enter value for nearestN neighborhood (ex: 2) or threshold neighborhood (ex: 0.7):", addproperty:"neighborhood")
					neighborhood = ant.antProject.properties["neighborhood"]
				 }
			} else { // recommenderSelected == 3
			  ant.input(message:"Evaluate slope-one recommender with weighting?",validargs:"y,n", addproperty:"withWeighting")
			  withWeighting = ant.antProject.properties["withWeighting"] == 'y'
			}
			printInput()
		  break
		case 'config':
			recommenderSelected = conf.mahout.recommender.selected
			hasPreference = conf.mahout.recommender.hasPreference
			similarity = conf.mahout.recommender.similarity
			withWeighting = conf.mahout.recommender.withWeighting
			neighborhood = conf.mahout.recommender.neighborhood as String
			printInput()
			break
		case 'class':
		  hasPreference = conf.mahout.recommender.hasPreference
		  printOpenLine()
		  println "\t\tdata model: ${conf.mahout.recommender.data.model}"
		  if (conf.mahout.recommender.data.model == 'file')
		    println "\t\tdata file name: ${conf.mahout.recommender.data.file}"
		  println "\t\thas preference?: $hasPreference"
		  println "\t\trecommender builder class: ${conf.mahout.recommender.builderClass}"
		  printCloseLine()
	}
}

private printOpenLine() {
  println "\n\t***************** Recommender Settings *****************"
  println "                                                            "
}

private printCloseLine() {
	println "\n\t********************************************************\n"
}

private printInput() {
	printOpenLine()
	println "\t\tdata model: ${conf.mahout.recommender.data.model}"
	if (conf.mahout.recommender.data.model == 'file')
	  println "\t\tdata file name: ${conf.mahout.recommender.data.file}"
	switch (recommenderSelected) {
		case 1:
			println "\t\trecommender: User-based recommender"
			break
		case 2:
			println "\t\trecommender: Item-based recommender"
			break
		case 3:
			println "\t\trecommender: Slope-one recommender"
			break
	}
	println "\t\thas preference?: $hasPreference"
	println "\t\tsimilarity metric: $similarity"
	println "\t\twith weighting?: $withWeighting"
	println "\t\tneighborhood: $neighborhood"
	printCloseLine()
}