function drawChart(chartID, title, l, m, h) {
    google.charts.load("current", {packages:["corechart"]});
       google.charts.setOnLoadCallback(drawChart);
       function drawChart() {
         var data = google.visualization.arrayToDataTable([
           ['Rank'  , 'Percent'],
           ['Low'   ,  l],
           ['Medium',  m],
           ['High'  ,  h]
         ]);

         var options = {
           title: title,
           pieHole: 0.6,
           colors: [ '#aaba0a', '#fca311', 'c71D06'],
           pieSliceText: 'none',
           backgroundColor: '#edf1f5',
           reverseCategories: true
         };

         var chart = new google.visualization.PieChart(document.getElementById(chartID));
         chart.draw(data, options);
       }
 }

function loadCharts() {
    drawChart('donutchart-NA', 'North America', 666, 1234, 235);
    drawChart('donutchart-EU', 'Europe', 123, 543, 34);
}
