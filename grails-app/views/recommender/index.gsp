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
  <span class="menuButton"><a class="home" href="\${createLink(uri: '/')}"><g:message code="default.home.label" /></a></span>
</div>
<div class="body">
<g:if test="${params.debug}">
  <p>User ID: <span style="font-weight: bold">${params.userID}</span></p>
  <p>Recommender: <span style="font-weight: bold">${recommender}</span></p>
	<h1>Top ${numOfTopPrefs} Preferences</h1>
	<table style="width: 50%">
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
<table style="width: 50%">
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
