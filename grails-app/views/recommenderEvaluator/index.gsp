<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title>Finding an effective recommender</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="\${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
        </div>
        <div class="body">
            <h1>Finding An Effective Recommender</h1>
            <h2>User-based Recommenders</h2>
            <h3>With Preference</h3>
            <h4>Fixed-size neighborhood</h4>
            <div class="list">
            Average absolute difference in estimated and actual preferences when evaluating a user-based
recommender using one of several similarity metrics, and using a nearest-n user neighborhood.
                <table>
                    <thead>
                       <tr>
                          <th>Similarity</th>
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
                          <td>Loglikelihood</td>
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
                        <tr class="even">
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
                    </tbody>
                </table>
            </div>            
            <h4>Threshold-based neighborhood</h4>
            <div class="list">
Average absolute difference in estimated and actual preferences when evaluating a userbased
recommender using one of several similarity metrics, and using a threshold-based user
neighborhood. Some values are "not a number", or undefined, and are denoted by Java's NaN symbol.            
                <table>
                    <thead>
                       <tr>
                          <th>Similarity</th>
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
                          <td>Loglikelihood</td>
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
                        <tr class="even">
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
                    </tbody>
                </table>
            </div>     
            <h3>Without Preference (Boolean Preference)</h3>


        </div>
    </body>
</html>
