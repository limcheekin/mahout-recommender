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
<%@ page import="org.grails.mahout.recommender.MahoutRecommenderConstants" %>
<g:set var="conf" value="${grailsApplication.config}" />
<g:set var="mode" value="${conf.mahout.recommender.mode?:MahoutRecommenderConstants.DEFAULT_MODE}" />
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="main" />
<title>Recommender Settings</title>
<style type="text/css">
     .valueStyle { font-weight: bold }
     select { width: 100% }
     input[type=text] { width: 98% }
</style>
</head>
<body>
<div class="nav">
  <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label" /></a></span>
</div>
<div class="body" style="width: 70%">
<h1>Recommender Settings</h1>
<g:if test="${flash.errors}">
  <div class="errors">${flash.errors}</div>
</g:if>

<form action="../recommender.html" method="post" >
<div class="list">
<table>
	<tbody>
	<tr>
		<td style="width: 40%">User ID.</td>
		<td style="width: 60%"><g:textField name="userID" value="${params.userID}" /></td>
	</tr>
	<tr>
		<td>Number of recommendations</td>
		<td><g:textField name="howMany" value="${params.howMany?:MahoutRecommenderConstants.DEFAULT_HOW_MANY}" /></td>
	</tr>
	<tr>
		<td>Has preference value?</td>
		<td>
		<g:if test="${mode == 'input'}">
		  <g:set var="yesAndNo" value="${['true':'Yes', 'false':'No'].entrySet()}" />
		  <g:select name="p" from="${yesAndNo}" value="${params.p}" 
		  optionKey="key" optionValue="value" />
		</g:if>
		<g:else>
		    <span class="valueStyle">${conf.mahout.recommender.hasPreference}</span>
		</g:else>		  
		</td>
	</tr>			
	<g:if test="${mode != 'class'}">
	<tr>
		<td>Recommender</td>
		<td>
		<g:if test="${mode == 'input'}">
		  <g:select name="r" from="${[1:'1. User-based recommender', 2:'2. Item-based recommender', 3:'3. Slope-one recommender'].entrySet()}" 
		  value="${params.r}" optionKey="key" optionValue="value" />
		</g:if>
		<g:else>
		    <span class="valueStyle">${conf.mahout.recommender.selected}</span>
		</g:else>
		</td>
	</tr>	
	<tr>
		<td>Similarity metric</td>
		<td>
		<g:if test="${mode == 'input'}">
		  <g:select name="s" from="${[PearsonCorrelation:'Pearson correlation', EuclideanDistance:'Euclidean distance', LogLikelihood:'Log-likelihood', TanimotoCoefficient:'Tanimoto coefficient'].entrySet()}" 
		  value="${params.s}" optionKey="key" optionValue="value" />
		</g:if>
		<g:else>
		    <span class="valueStyle">${conf.mahout.recommender.similarity}</span>
		</g:else>		  		  
		</td>
	</tr>		
	<tr>
		<td>With weighting?</td>
		<td>
		<g:if test="${mode == 'input'}">
		  <g:select name="w" from="${yesAndNo}" value="${params.w}" 
		  optionKey="key" optionValue="value" />
		</g:if>
		<g:else>
		    <span class="valueStyle">${conf.mahout.recommender.withWeighting}</span>
		</g:else>		 		  
		</td>
	</tr>					
	<tr>
		<td>Neighborhood (ex. 0.85 or 3)</td>
		<td>
		<g:if test="${mode == 'input'}">
		  <g:textField name="n" value="${params.n}" />
		</g:if>
		<g:else>
		    <span class="valueStyle">${conf.mahout.recommender.neighborhood}</span>
		</g:else>				
		</td>
	</tr>				
	</g:if>
	<g:else>
	<tr>
		<td>Recommender builder class</td>
		<td>
		    <span class="valueStyle">${conf.mahout.recommender.builderClass}</span>
		</td>
	</tr>			
	</g:else>		
	<tr>
		<td>Enabled debug?</td>
		<td>
		  <g:checkBox name="debug" />
		</td>
	</tr>							
	</tbody>
</table>
</div>
<div class="buttons">
    <span class="button"><g:submitButton name="create" class="save" value="Submit" /></span>
</div>
</form>
 
</div>
</body>
</html>
