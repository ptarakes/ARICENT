<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Nvd3 Report</title>
<script type="text/javascript" src="files/jquery/jquery-1.11.0.min.js"></script>
<script type="text/javascript" src="files/nvd3/d3.v3.min.js"></script>
<script type="text/javascript" src="files/nvd3/nv.d3.min.js"></script>
<link rel="stylesheet" href="files/nvd3/nv.d3.min.css" />
<style>
.chart {
	padding: 10px;
	border: 5px solid #999;
	margin-bottom: 10px;
}

.chart .header {
	border-bottom: 3px dashed #ccc;
	display: inline;
	padding: 5px 25px;
}

svg {
	height: 600px;
	margin-left: -25px;
}
.loading{
	position:fixed;
	top:-5px;
	width:100%;
	text-align:center;
}
.loading-msg{
	display: inline-block;
	padding: 8px 15px;
	border: 2px solid #18A298;
	border-radius: 5px;
	background-color: #ADF2F8;
	font-size: 20px;
	box-shadow: 0 0 10px;
	color: #156868;
}
</style>
</head>
<body onload="getData()">
	<div class="loading">
		<div class="loading-msg">Loading...</div>
	</div>
	<div class="chart">
		<h3 class="header">Chart Using Actual Data</h3>
		<svg id="multiBarChart1" style=""></svg>
	</div>
	<div class="chart">
		<h3 class="header">Claims</h3>
		<svg id="multiBarChart2" style=""></svg>
	</div>
	<div class="chart">
		<h3 class="header">Fault claims (Difference)</h3>
		<svg id="multiBarChart3" style=""></svg>
	</div>
	<script>
		var dataFromDevice = [];
		/*making json data by using claims*/
		var dataFromCustomers = [{
		    "key": "Setu Madhav",
		    "values": [
		               {"y": 1,"x": 1}, {"y": 3,"x": 2}, {"y": 3,"x": 3}, {"y": 2,"x": 4}, {"y": 2,"x": 5}, {"y": 4,"x": 6}, {"y": 1,"x": 7}, {"y": 2,"x": 8}, {"y": 1,"x": 9}, {"y": 1,"x": 10}
		              ]
		}, {
		    "key": "Seafood Express",
		    "values": [
		               {"y": 1,"x": 1}, {"y": 0,"x": 2}, {"y": 3,"x": 3}, {"y": 9,"x": 4}, {"y": 6,"x": 5}, {"y": 1,"x": 6}, {"y": 2,"x": 7}, {"y": 1,"x": 8}, {"y": 1,"x": 9}, {"y": 0,"x": 10}
		    		  ]

		}, {
		    "key": "Aska Sea",
		    "values": [
		               {"y": 1,"x": 1}, {"y": 2,"x": 2}, {"y": 1,"x": 3}, {"y": 3,"x": 4}, {"y": 1,"x": 5}, {"y": 10,"x": 6}, {"y": 2,"x": 7}, {"y": 2,"x": 8}, {"y": 0,"x": 9}, {"y": 2,"x": 10}
		              ]

		}, {
		    "key": "Al Saad",
		    "values": [
		               {"y": 2,"x": 1}, {"y": 8,"x": 2}, {"y": 0,"x": 3}, {"y": 2,"x": 4}, {"y": 2,"x": 5}, {"y": 0,"x": 6}, {"y": 1,"x": 7}, {"y": 0,"x": 8}, {"y": 1,"x": 9}, {"y": 2,"x": 10}
		    		  ]

		}, {
		    "key": "Astra",
		    "values": [
		               {"y": 1, "x": 1}, {"y": 1,"x": 2}, {"y": 1,"x": 3}, {"y": 4,"x": 4}, {"y": 2,"x": 5}, {"y": 2,"x": 6}, {"y": 2,"x": 7}, {"y": 1,"x": 8}, {"y": 4,"x": 9}, {"y": 4,"x": 10}
		    		  ]

		}, {
		    "key": "Hiran",
		    "values": [
		               {"y": 1,"x": 1}, {"y": 2,"x": 2}, {"y": 2,"x": 3}, {"y": 2,"x": 4 }, {"y": 1,"x": 5}, {"y": 1,"x": 6}, {"y": 3,"x": 7}, {"y": 6,"x": 8}, {"y": 5,"x": 9}, {"y":5,"x" : 10} 
		              ]

		}];

		function getData() { /*this function gets the data by calling servlet url(getData) using JQuery ajax call*/
			$.get("getData", /*url*/
			function(res, status) {/*res is the response data(json) which is returned by servlet(GETData)*/
				if (status == "success") {
					dataFromDevice = res;
					multiBarChart(dataFromDevice, "#multiBarChart1");/*calling multiBarChart with data to form the nvd3 chart*/
					multiBarChart(dataFromCustomers, "#multiBarChart2");
					multiBarChart(makeDifferChartData(dataFromDevice,
							dataFromCustomers), "#multiBarChart3");
				}
			});
		}
		/*make json data of difference b/w Claims data and actual data(from Device) */
		function makeDifferChartData(a, c) {
			var jsonArry = [];
			for (var i = 0; i < a.length && i < c.length; i++) {
				var jsonObject = {}, valuesJsonArray = [], aa = [], cc = [], flag = false;
				if(typeof a[i].values !== undefined){
					aa = a[i].values;
				}
				if(typeof c[i].values !== undefined){
					cc = c[i].values;
				}
				for (var j = 0; j < aa.length && j < cc.length; j++) {
					var valuesJsonObject = {};
					valuesJsonObject.x = aa[j].x;
					valuesJsonObject.y = cc[j].y - aa[j].y;
					valuesJsonArray.push(valuesJsonObject);
					flag = true;
				}
				if(flag){
					jsonObject.key = a[i].key;
					jsonObject.values = valuesJsonArray;
					jsonArry.push(jsonObject);
				}
			}
			return jsonArry;
		}
		/*adding chart/graph to the html body*/
		function multiBarChart(chartData, id) {
			nv.addGraph(function() {/*This adds the chart to a global rendering queue. */
				var chart;
				chart = nv.models.multiBarChart();/*Create instance of nvd3 Multi Bar Chart. */
				chart.multibar.stacked(true); // default to stacked
				chart.showControls(false);
				chart.color(d3.scale.category10().range());/* set different colors for each series*/
				chart.xAxis.tickFormat(function(d) {
					return "week " + d; /*set x-axis format*/
				});
				chart.yAxis.tickFormat(d3.format('d'));/*set y-axis format(.2f -> two floating points)*/
				chart.xAxis.axisLabel('<------ Weeks ------>');/*set x-axis label*/
				chart.yAxis.axisLabel('spoiled count');/*set x-axis label (no need)*/
				chart.yDomain([ 0, 25 ]);/*set y-axis domain*/
				d3.select(id)/*Select the document's <svg> element. */
				.datum(chartData)/*Attach data to the <svg> element. */
				.call(chart)/*pass the d3.selection to our Cummulative Chart. */
				;
				nv.utils.windowResize(chart.update);/*Updates the window resize event callback. */
				return chart;
			});
			$('.loading').fadeOut('slow');
		}
	</script>
</body>
</html>