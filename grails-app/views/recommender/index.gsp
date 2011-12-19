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
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="main" />
<title>Recommendations</title>
<script type="text/javascript">

</script>
</head>
<body>
<div class="nav">
  <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label" /></a></span>
</div>
<div class="body" style="width: 50%">
<g:if test="${params.debug}">
  <p>User ID: <span style="font-weight: bold">${params.userID}</span></p>
  <p>Recommender: <span style="font-weight: bold">${recommender}</span></p>
	<h1>Top ${numOfTopPrefs} Preferences</h1>
	<div class="list">
	<table>
		<thead>
			<tr>
				<th>Item ID</th>
				<th>Value</th>
			</tr>
		</thead>
		<tbody>
	  <g:each in="${(0..<numOfTopPrefs)}" var="i">
	      <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
	          <td>${prefs.get(i).itemID}</td>
	          <td>${prefs.get(i).value}</td>
	      </tr>
	  </g:each>	
		</tbody>
	</table>
	</div>  
</g:if>

<h1>Recommendations</h1>
<div class="list">
<table>
	<thead>
		<tr>
			<th>Item ID</th>
			<th>Value</th>
		</tr>
	</thead>
	<tbody>
  <g:each in="${items}" status="i" var="item">
      <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
          <td>${item.itemID}</td>
          <td>${fieldValue(bean: item, field: "value")}</td>
      </tr>
  </g:each>	
	</tbody>
</table>
</div>
 
</div>
</body>
</html>
