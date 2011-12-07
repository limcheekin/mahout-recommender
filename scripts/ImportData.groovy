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

includeTargets << grailsScript("_GrailsBootstrap")

target(main: "Insert data files to database") {
	depends(parseArguments, bootstrap)
	if (argsMap["params"].size() == 0) {
		ant.echo "Import data command (import-data) usages:"
		ant.echo "\tgrails import-data file1.csv"
		ant.echo "\tgrails import-data file1.csv file2.csv file3.csv"
		exit 1
	} else {
		Sql sql = new Sql(grailsApp.mainContext.dataSource)
		preferenceTableName = grailsApp.config.mahout.recommender.preference.table
		userIdColumn = grailsApp.config.mahout.recommender.preference.userIdColumn
		itemIdColumn = grailsApp.config.mahout.recommender.preference.itemIdColumn
		valueColumn = grailsApp.config.mahout.recommender.preference.valueColumn
		println "Import data to $preferenceTableName table:"
		argsMap["params"].each { fileName ->
			fullFilename = "$basedir/$fileName"
			print "* Importing ${fullFilename}..."
			new File(fullFilename).eachLine { line -> 
				if (line) {
					data = line.split(",")
					if (data.size() == 2) {
						sql.executeInsert("insert into $preferenceTableName ($userIdColumn, $itemIdColumn) values (?,?)", data)
					} else if (data.size() == 3) {
						sql.executeInsert("insert into $preferenceTableName ($userIdColumn, $itemIdColumn, $valueColumn) values (?,?,?)", data)
					} else {
						ant.echo "Invalid data file! Only 2 or 3 columns CSV file supported."
						exit 1
					}
				}
			}
			println " [Completed]"
		}
	}
}

setDefaultTarget(main)
