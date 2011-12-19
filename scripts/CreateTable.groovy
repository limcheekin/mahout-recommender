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
import org.apache.mahout.cf.taste.impl.recommender.slopeone.jdbc.AbstractJDBCDiffStorage

includeTargets << new File("${mahoutRecommenderPluginDir}/scripts/_Common.groovy")

target(main: "Create table") {
	depends(bootstrap)
	println "\t1) Preference table"
	println "\t2) Boolean preference table"
	println "\t3) Slope-one diffs table"
	ant.input(message:"Create table:",validargs:"1,2,3", addproperty:"tableSelected")
	tableSelected = ant.antProject.properties["tableSelected"] as Integer
	switch (tableSelected) {
		case 1:
			createTable grailsApp.config.mahout.recommender.preference.table?:AbstractJDBCDataModel.DEFAULT_PREFERENCE_TABLE,
					"${mahoutRecommenderPluginDir}/src/sql/mysql_preference.sql"
		  break
		case 2:
			createTable grailsApp.config.mahout.recommender.preference.table?:AbstractJDBCDataModel.DEFAULT_PREFERENCE_TABLE,
					"${mahoutRecommenderPluginDir}/src/sql/mysql_boolean_preference.sql"
			break
		case 3:
			createTable grailsApp.config.mahout.recommender.slopeone.diffs.table?:AbstractJDBCDiffStorage.DEFAULT_DIFF_TABLE,
					"${mahoutRecommenderPluginDir}/src/sql/mysql_slopeone_diffs.sql"
	}
}

setDefaultTarget(main)

createTable = { String tableName, String sqlFile ->
	Sql sql = new Sql(grailsApp.mainContext.dataSource)
	sqlStrings = new File(sqlFile).text.trim().split(";")
	sqlStrings.each { sqlString -> sql.execute sqlString }
	printMessage "$tableName table created."
}

