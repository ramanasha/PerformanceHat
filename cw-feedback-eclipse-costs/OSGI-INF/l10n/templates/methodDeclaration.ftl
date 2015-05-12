<style>
	<#include "style.css">
</style>

<table>
	<tr>
		<td><div class="arrow_box">incoming requests</div></td>
		<td clas="spacer"></td>
		<td>
			<div class="service_box">services.currency</div>
		</td>
	</tr>
	<tr>
		<td>
			<ul label="Frequency">
				<li>Min:	20 req/s</li>
				<li>Avg:	45 req/s</li>
				<li>Max:	100 req/s</li>
			</ul>
		
			<ul label="Callers">
			<#list requests as request>
				 <li>${request_index + 1}. ${request.getCaller()}<#if request_has_next>,</#if></li>
			</#list> 
			</ul>
		</td>
		<td></td>
		<td class="costs">
			<ul label="Status">
				<li>Number of instances: 5</li>
				<li>Max. req/s per instance: 100</li>
				<li>Costs per instance hours: USD 0.1</li>
			</ul>
			
			<ul label="Expected impact">
				<li>Additional instances: 5</li>
				<li>Additional costs per hour: 0.5</li>
				<li>Cost Trend: +50%</li>
			</ul>
		</td> 
	  </tr>
</table>