function drawWorldMapPieCharts(regionData) {
    $("#donutchart-AF").html('');
    $("#donutchart-NA").html('');
    $("#donutchart-OC").html('');
    $("#donutchart-AS").html('');
    $("#donutchart-EU").html('');
    $("#donutchart-SA").html('');


    $.each( regionData, function( key, val ) {
        var r = regionData[key];
        drawPieChart( "donutchart-" + r.regionCode,
                      "donutchart-" + r.regionCode,
                      r.Low.assetValue,
                      r.Medium.assetValue,
                      r.High.assetValue
                      );
    });

}


function drawPieChart(chartID, title, l, m, h) {
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
           width: 180,
           height: 180,
           titleTextStyle: { fontSize: 13 },
           pieHole: 0.6,
           colors: [ '#aaba0a', '#fca311', 'c71D06'],
           pieSliceText: 'none',
           backgroundColor: 'none',
           reverseCategories: true,
           chartArea: {left:10, top:40, 'width': '100%', 'height': '100%'}
         };

         var chart = new google.visualization.PieChart(document.getElementById(chartID));
         chart.draw(data, options);
       }
 }


function drawBarChart(chartID, title) {
    google.charts.load('current', {'packages':['bar']});
    google.charts.setOnLoadCallback(drawChart);
    function drawChart() {
    var data = google.visualization.arrayToDataTable([
      ['Year', 'Sales', 'Expenses', 'Profit'],
      ['Low', 1000, 400, 200],
      ['Medium', 1170, 460, 250],
      ['High', 660, 1120, 300]
    ]);

    var options = {
      chart: {
        title: 'Company Performance',
        subtitle: 'Sales, Expenses, and Profit: 2014-2017',
      }
    };

    var chart = new google.charts.Bar(document.getElementById('chartID'));

    chart.draw(data, options);
    }
}



function loadPieCharts() {
    drawPieChart('donutchart-NA', 'North America', 666, 1234, 235);
    drawPieChart('donutchart-EU', 'Europe', 123, 543, 34);
}


function loadBarCharts() {
    drawBarChart('country-A', 'Data for chart A');
    drawBarChart('country-B', 'Data for chart B');
}