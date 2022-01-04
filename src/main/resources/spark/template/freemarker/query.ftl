<#assign content>


<center> 
<div class="row">
<div class="column">

<p> Enter neighbor input data below:
<form method="GET" action="/neighbors">
<label>Please put spaces between each argument </label><br>
<label>Example: 3 0 0 0</label><br>
<label>Example: 3 "Sol"</label><br>
<textarea name="neighbors text" id="neighbors text"></textarea><br>
  <input type="submit" value="Calculate Neighbors">
</form>
</p>
<mark> ${neighbors} </mark>
</div>

<div class="column">
<h1> STARS: The Universe through KDTrees</h1>
<p> Enter stars input data below:
<form method="GET" action="/starsfile">
<label>Please put a full file path on this machine</label><br>
<label>Example: data/stars/stardata.csv</label><br>
<textarea name="stars text" id="stars text"></textarea><br>
  <input type="submit" value="Identify Stars">
</form>
</p>
<mark> ${stars} </mark>
</div>

<div class="column">
<p> Enter radius input data below:
<form method="GET" action="/radius">
<label>Please put spaces between each argument </label><br>
<label>Example: 3 0 0 0</label><br>
<label>Example: 3 "Sol"</label><br>
<textarea name="radius text" id="radius text"></textarea><br>
  <input type="submit" value="Calculate Radius">
</form>
</p>
<mark> ${radius} </mark>
</div>
</div> 
</center>


</#assign>
<#include "main.ftl">