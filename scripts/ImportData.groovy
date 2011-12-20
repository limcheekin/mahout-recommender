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
import groovy.sql.Sql
import org.apache.mahout.cf.taste.impl.model.jdbc.AbstractJDBCDataModel

includeTargets << new File("${mahoutRecommenderPluginDir}/scripts/_Common.groovy")

USAGE = '''
Usage:
  grails import-data fileName [numberOfLines]

Examples:
  grails import-data grails-app/conf/data.csv
  grails import-data data.csv 100000
'''
target(main: "Insert data file to database") {
	depends(parseArguments, bootstrap)
	
	if (argsMap["params"].size() == 0) {
		errorMessage USAGE
		exit 1
	} else {
		Sql sql = new Sql(grailsApp.mainContext.dataSource)
		def preferenceTableName = grailsApp.config.mahout.recommender.preference.table?:AbstractJDBCDataModel.DEFAULT_PREFERENCE_TABLE
		def userIdColumn = grailsApp.config.mahout.recommender.preference.userIdColumn?:AbstractJDBCDataModel.DEFAULT_USER_ID_COLUMN
		def itemIdColumn = grailsApp.config.mahout.recommender.preference.itemIdColumn?:AbstractJDBCDataModel.DEFAULT_ITEM_ID_COLUMN
		def valueColumn = grailsApp.config.mahout.recommender.preference.valueColumn?:AbstractJDBCDataModel.DEFAULT_PREFERENCE_COLUMN
		def fileName = argsMap["params"][0]
		def numberOfLines = argsMap["params"][1] ? argsMap["params"][1] as Long : Long.MAX_VALUE
		def MahoutRecommenderConstants = classLoader.loadClass("org.grails.mahout.recommender.MahoutRecommenderConstants")
		println "Import data to $preferenceTableName table:"
		def fullFilename = "$basedir/$fileName"
		println "Importing ${fullFilename}..."
		FileReader fr = new FileReader(new File(fullFilename))
		BufferedReader br = new BufferedReader(fr)
		String line
		Long lineCount = 0
		def batchSize = MahoutRecommenderConstants.DEFAULT_BATCH_SIZE
		def data
		
		sql.withBatch(batchSize) { stmt ->
		  // read file line by line
			while ((line = br.readLine()) != null && lineCount < numberOfLines) {
				if (line) {
					data = line.split(",")
					if (data.size() == 2) {
						stmt.addBatch "insert into $preferenceTableName ($userIdColumn, $itemIdColumn) values (${data[0]}, ${data[1]})"
					} else if (data.size() == 3) {
						stmt.addBatch "insert into $preferenceTableName ($userIdColumn, $itemIdColumn, $valueColumn) values (${data[0]}, ${data[1]}, ${data[2]})"
					} else {
						errorMessage "Invalid data file! Only 2 or 3 columns CSV file supported."
						exit 1 
					} 
				  ++lineCount
				  if (lineCount % batchSize == 0) {
					  printMessage "* $lineCount rows imported."
				    }
				}
			}
		}
		fr.close()
		printMessage "[Done. $lineCount rows imported.]"
	}
}

setDefaultTarget(main)
