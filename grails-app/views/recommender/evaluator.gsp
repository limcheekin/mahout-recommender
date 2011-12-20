<%--
/* Copyright 2011-2012 the original author or authors.
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
 *
 * @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
 *
 * @since 0.5
 */
 --%>
<g:set var="grailsMajorVersion" value="${grailsApplication.metadata['app.grails.version'].charAt(0) as Integer}" />
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="main" />
<title>Finding an effective recommender by average difference</title>
<g:if test="${grailsMajorVersion == 2}">
  <r:require module="jquery"/>
  <r:layoutResources/>
</g:if>
<g:else>
  <g:javascript library="jquery" plugin="jquery" />
</g:else>
<style type="text/css">
.mask{
   position: relative;
   overflow: hidden;
   margin: 0px auto;
   width: 100%;
}

.container{
   position: relative;
   width: 100%;
   right: 50%;
}
.col1{
   position: relative;
   overflow: hidden;
   float: left;
   width: 49%;
   left: 101%;
}
.col2{
   position: relative;
   overflow: hidden;
   float: left;
   width: 49%;
   left: 1%;
}

#slopeOneRecommender {
  width: 50%;
} 
</style>
</head>
<body>
<div class="nav">
  <span class="menuButton"><a class="home" href="\${createLink(uri: '/')}"><g:message code="default.home.label" /></a></span>
  <span class="menuButton"><span id="inProgessLabel" style="display: none">Evaluation processing...</span><a id="evaluatorMenuButton" href="#">Run Evaluator</a></span>
</div>
<div class="body">
<h1>Finding An Effective Recommender By Average Difference</h1>
<g:set var="error" value="${grailsApplication.mainContext.mahoutRecommenderSupport.validateEnvironmentSetup()}" />
<g:if test="${error}">
  <div class="errors">${error}</div>
</g:if>
<p>Click on "Run Evaluator" above to start the recommenders' evaluation.
<p>Some values are "not a number", or undefined, and are denoted by Java's NaN symbol.</p>
<h2>User-based Recommenders</h2>
<h3>With Preference</h3>
<h4>Fixed-size neighborhood</h4>
<div class="list">Average absolute difference in estimated and
actual preferences when evaluating a user-based recommender using one of
several similarity metrics, and using a nearest-n user neighborhood.
<table id="fixedSizeNeighborhoodWithPref">
	<thead>
		<tr>
			<th width="30%">Similarity</th>
			<th>1</th>
			<th>2</th>
			<th>4</th>
			<th>8</th>
			<th>16</th>
			<th>32</th>
			<th>64</th>
			<th>128</th>
			<th>256</th>
			<th>512</th>
		</tr>
	</thead>
	<tbody>
		<tr class="odd">
			<td>PearsonCorrelation</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr class="even">
			<td>PearsonCorrelation + weighting</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr class="odd">
			<td>EuclideanDistance</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr class="even">
			<td>EuclideanDistance + weighting</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr class="odd">
			<td>LogLikelihood</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr class="even">
			<td>TanimotoCoefficient</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
	</tbody>
</table>
</div>
<h4 id="thresholdNeighborhoodWithPrefHeader">Threshold-based neighborhood</h4>
<div class="list">Average absolute difference in estimated and
actual preferences when evaluating a user-based recommender using one of
several similarity metrics, and using a threshold-based user
neighborhood. 
<table id="thresholdNeighborhoodWithPref">
	<thead>
		<tr>
			<th width="30%">Similarity</th>
			<th>0.95</th>
			<th>0.90</th>
			<th>0.85</th>
			<th>0.80</th>
			<th>0.75</th>
			<th>0.70</th>
			<th>0.65</th>
			<th>0.60</th>
			<th>0.55</th>
			<th>0.50</th>
		</tr>
	</thead>
	<tbody>
		<tr class="odd">
			<td>PearsonCorrelation</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr class="even">
			<td>PearsonCorrelation + weighting</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr class="odd">
			<td>EuclideanDistance</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr class="even">
			<td>EuclideanDistance + weighting</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr class="odd">
			<td>LogLikelihood</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr class="even">
			<td>TanimotoCoefficient</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
	</tbody>
</table>
</div>
<h3>Without Preference (Boolean Preference)</h3>
<h4>Fixed-size neighborhood</h4>
<div class="list">
<table id="fixedSizeNeighborhoodWithoutPref">
	<thead>
		<tr>
			<th width="30%">Similarity</th>
			<th>1</th>
			<th>2</th>
			<th>4</th>
			<th>8</th>
			<th>16</th>
			<th>32</th>
			<th>64</th>
			<th>128</th>
			<th>256</th>
			<th>512</th>
		</tr>
	</thead>
	<tbody>
		<tr class="odd">
			<td>LogLikelihood</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr class="even">
			<td>TanimotoCoefficient</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
	</tbody>
</table>
</div>
<h4 id="thresholdNeighborhoodWithoutPrefHeader">Threshold-based neighborhood</h4>
<div class="list">
<table id="thresholdNeighborhoodWithoutPref">
	<thead>
		<tr>
			<th width="30%">Similarity</th>
			<th>0.95</th>
			<th>0.90</th>
			<th>0.85</th>
			<th>0.80</th>
			<th>0.75</th>
			<th>0.70</th>
			<th>0.65</th>
			<th>0.60</th>
			<th>0.55</th>
			<th>0.50</th>
		</tr>
	</thead>
	<tbody>
		<tr class="odd">
			<td>LogLikelihood</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr class="even">
			<td>TanimotoCoefficient</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
	</tbody>
</table>
</div>

<div class="mask">
<h2>Item-based Recommenders</h2> 
<div class="container">
  <div class="col1">
  <h3>Without Preference</h3>
			<div class="list">
			<table id="itemWithoutPref">
				<thead>
					<tr>
						<th width="70%">Similarity</th>
						<th>Score</th>
					</tr>
				</thead>
				<tbody>
					<tr class="odd">
						<td>LogLikelihood</td>
						<td></td>
					</tr>
					<tr class="even">
						<td>TanimotoCoefficient</td>
						<td></td>
					</tr>
				</tbody>
			</table>
			</div>  
	</div>
	<div class="col2">
  <h3>With Preference</h3>
			<div class="list">
			<table id="itemWithPref">
				<thead>
					<tr>
						<th width="70%">Similarity</th>
						<th>Score</th>
					</tr>
				</thead>
				<tbody>
					<tr class="odd">
						<td>PearsonCorrelation</td>
						<td></td>
					</tr>
					<tr class="even">
						<td>PearsonCorrelation + weighting</td>
						<td></td>
					</tr>
					<tr class="odd">
						<td>EuclideanDistance</td>
						<td></td>
					</tr>
					<tr class="even">
						<td>EuclideanDistance + weighting</td>
						<td></td>
					</tr>
					<tr class="odd">
						<td>LogLikelihood</td>
						<td></td>
					</tr>
					<tr class="even">
						<td>TanimotoCoefficient</td>
						<td></td>
					</tr>
				</tbody>
			</table>
			</div>  
	</div> 
</div>
</div>

<h2>Slope-One Recommender</h2>
	<div class="list">
	<table id="slopeOneRecommender">
		<thead>
			<tr>
				<th width="70%">Weighting</th>
				<th>Score</th>
			</tr>
		</thead>
		<tbody>
			<tr class="odd">
				<td>With weighting</td>
				<td id="weight"></td>
			</tr>
			<tr class="even">
				<td>Without weighting</td>
				<td id="unweight"></td>
			</tr>
		</tbody>
	</table>
	</div>  
</div>
<g:if test="${grailsMajorVersion == 2}">
  <r:layoutResources/>
</g:if>
<script type="text/javascript">
	$(function() {
		$('#evaluatorMenuButton').click(runEvaluator);
	});
	function runEvaluator(event) {
		toggleMenuButton();
		evaluateUserBasedRecommender('fixedSizeNeighborhoodWithPref', true);
		scrollToElement('thresholdNeighborhoodWithPrefHeader');
		evaluateUserBasedRecommender('thresholdNeighborhoodWithPref', true);
		evaluateUserBasedRecommender('fixedSizeNeighborhoodWithoutPref', false);
		scrollToElement('thresholdNeighborhoodWithoutPrefHeader');
		evaluateUserBasedRecommender('thresholdNeighborhoodWithoutPref', false);
		evaluateItemBasedRecommender('itemWithPref', true);		
		evaluateItemBasedRecommender('itemWithoutPref', false);	
		scrollToElement('slopeOneRecommender');
		evaluateSlopeOneRecommender('#weight', true);	
		evaluateSlopeOneRecommender('#unweight', false);
		toggleMenuButton();	
		return false;
	}

	function evaluateUserBasedRecommender(tableId, hasPreference) {
		var $table = $('#' + tableId);
		var $rowHeaders = $('tr th', $table);
		var $rowDatas = $('tr td', $table);
		var j = 0;
		var similarity;
		var weightingIndex;
		var withWeighting;
		var data = new Object();
		var $td;
		
		for (var i = 0; i < $rowDatas.length; i++) {
			j = i % 11;
			$td = $($rowDatas[i]);
			if (j == 0) {
				similarity = $td.text();
				weightingIndex = similarity.indexOf('+');
				withWeighting = weightingIndex > -1;
				if (withWeighting) {
					similarity = similarity.substring(0, weightingIndex - 1);
				} 
			} else {
				data['recommenderSelected'] = 1;
				data['hasPreference'] = hasPreference;
				data['similarity'] = similarity;
				data['withWeighting'] = withWeighting;
				data['neighborhood'] = $($rowHeaders[j]).text();  
				
				$.ajax({
					url : 'evaluateAverageDiff',
					dataType: "text",
					data: data,
					async: false,
					success : function(response) {
						$td.text(response);
					},
					error : function() {
						$td.html('<span style="color:red">Err</span>');
					}
				}); 
			}
		}				
	}

	function evaluateItemBasedRecommender(tableId, hasPreference) {
		var $table = $('#' + tableId);
		var $rowDatas = $('tr td', $table);
		var j = 0;
		var similarity;
		var weightingIndex;
		var withWeighting;
		var data = new Object();
		var $td;
		
		for (var i = 0; i < $rowDatas.length; i++) {
			j = i % 2;
			$td = $($rowDatas[i]);
			if (j == 0) {
				similarity = $td.text();
				weightingIndex = similarity.indexOf('+');
				withWeighting = weightingIndex > -1;
				if (withWeighting) {
					similarity = similarity.substring(0, weightingIndex - 1);
				} 
			} else {
				data['recommenderSelected'] = 2;
				data['hasPreference'] = hasPreference;
				data['similarity'] = similarity;
				data['withWeighting'] = withWeighting;  
				
				$.ajax({
					url : 'evaluateAverageDiff',
					dataType: "text",
					data: data,
					async: false,
					success : function(response) {
						$td.text(response);
					},
					error : function() {
						$td.html('<span style="color:red">Err</span>');
					}
				}); 
			}
		}				
	}

	function evaluateSlopeOneRecommender(selector, withWeighting) {
		var data = new Object();
		var $td = $(selector);
		data['withWeighting'] = withWeighting;  
		data['recommenderSelected'] = 3;
		
		$.ajax({
			url : 'evaluateAverageDiff',
			dataType: "text",
			data: data,
			async: false,
			success : function(response) {
				$td.text(response);
			},
			error : function() {
				$td.html('<span style="color:red">Err</span>');
			}
		}); 	
	}	
	
function scrollToElement(id) {
	  var new_position = $('#' + id).offset();
	  window.scrollTo(new_position.left,new_position.top);
}	

function toggleMenuButton() {
	var $evaluatorMenuButton = $('#evaluatorMenuButton');
	if ($evaluatorMenuButton.is(":visible")) {
		$evaluatorMenuButton.hide();
		$('#spinner').show();
		$('#inProgessLabel').show();		
		$('body').css("cursor", "progress");
	} else {
		$('#spinner').hide();
		$('body').css("cursor", "auto");
		$('#inProgessLabel').hide();		
		$evaluatorMenuButton.show();
	}
}
</script>
</body>
</html>
