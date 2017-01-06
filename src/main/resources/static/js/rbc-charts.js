var mapTitles = new Map();
mapTitles.set("EU", 'Europe');
mapTitles.set("AF", 'Africa');
mapTitles.set("AS", 'Asia');
mapTitles.set("NA", 'North America');
mapTitles.set("OC", 'Oceania');
mapTitles.set("SA", 'South America');

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
            mapTitles.get(r.regionCode),
            r.low.assetValue,
            r.medium.assetValue,
            r.high.assetValue
        );
    });

}
function getAndDisplayTopTen(countryCode) {
    //$.getJSON( "../jsonfiles/Country" + countryCode + '.json', function( data ) {
    $.getJSON( "/jsonfiles/MapPerCountryFund1RegionEU.json", function( data ) {
        countriesMap = data.countries.reduce(function(map, obj) {
            map[obj.countryCode] = obj;
            return map;
        }, {});
        drawTopTenChart(countriesMap);
    });
};
function drawTopTenChart(countryCode){

    $.each( countryCode, function( key, val ) {
        var r = countryCode[key];
        var cID = 'topdonutchart-' + r.countryCode;
        var tableDiv = "<TD><div id='" + cID + "' style='width: 100%; height: 100%'>"
        tableDiv += "</div></TD>"
        $("#topten").append(tableDiv);
        drawPieChart( cID,
            r.countryName + "(" + r.TotalAssetNumber + ")",
            r.low.assetValue,
            r.medium.assetValue,
            r.high.assetValue
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
           //reverseCategories: true,
           chartArea: {left:10, top:40, 'width': '100%', 'height': '100%'}
         };

         var chart = new google.visualization.PieChart(document.getElementById(chartID));
         chart.draw(data, options);
       }
 }

 function selectedFund() {
    return $('#fund-selection').val();
 }

 function getAndDrawColumnChart(countryCode) {
    //$.getJSON( "../jsonfiles/Country" + countryCode + '.json', function( data ) {
    $.getJSON( "/api/funds/" + selectedFund() + "/countries/" + countryCode , function( data ) {
        drawColumnChart(data);
    });
  };



function drawColumnChart(countryData) {
    google.charts.load('current', {'packages':['bar', 'corechart']});
    google.charts.setOnLoadCallback(drawChart);

    var cID = 'country-chart-' + countryData.countryCode;
    var chartDiv  = "<div id='c-" + cID + "' style=\"position:absolute\">"
        chartDiv += "<div id='" + cID + "' style='width: 100%; height: 100%'>"
        chartDiv += "</div>"

    $('#country-charts').append(chartDiv);

    function drawChart() {
        var data = google.visualization.arrayToDataTable([
            ['Rank', 'Acc', 'Cty', 'Total'],
            ['Low', countryData.low.assetValue, countryData.low.percentagePerAssetValue, countryData.low.percentagePerTotalAssetValue],
            ['Medium', countryData.medium.assetValue, countryData.medium.percentagePerAssetValue, countryData.medium.percentagePerTotalAssetValue],
            ['High', countryData.high.assetValue, countryData.high.percentagePerAssetValue, countryData.high.percentagePerTotalAssetValue]
        ]);

        var options = {
            chart: {
                title: 'Bar chart for ' + countryData.countryName
            },
            animation: {
                duration: 1000,
                easing: 'out',
                startup: true
            },
            colors: [ '#aaba0a', '#fca311', 'c71D06']
        };

        var chart = new google.charts.Bar(document.getElementById('country-chart-' + countryData.countryCode));

        chart.draw(data, options);

        $('#c-' + cID).append("<button onClick=\"$('#c-" + cID + "').remove()\" style='position:absolute; top:0; right:0'>Delete</button>")
    }
}

function getAndDisplayPeps(countryCode,riskLevel){
    //$.getJSON( "/api/funds/" + selectedFund() + "/countries/" + countryCode+ "/" + riskLevel, function( data ) {
    $.getJSON( "/jsonfiles/Peps.json" , function( data ) {
        displayPepsInfo(data);
    });
};

function displayPepsInfo(pepsDataForRisk) {

    google.charts.load('current', {'packages':['table']});
    google.charts.setOnLoadCallback(drawTable);

    function drawTable() {
        pepsDataForRisk.legalEntities.forEach(function (legalEntity) {
            var cID = 'legal-entity-' + legalEntity.name;
            //var tableDiv  = "<div id='c-" + cID + "' style=\"position:absolute\">"
            var tableDiv = "<div id='" + cID + "' style='width: 100%; height: 100%'>" +
                "<h1  class='myTitle'>" + legalEntity.name+ "</h1>" +
                "<h2 class='myTitle2'>(" +legalEntity.type + " - " +legalEntity.nature +")</h2>"
            tableDiv += "</div>"
            tableDiv += "<div id='d-" + cID + "' style='width: 100%; height: 100%'>"
            tableDiv += "</div>"
            $('#pepsInformations').append(tableDiv);
            var data = new google.visualization.DataTable();
            data.addColumn('string', 'Fisrt Name');
            data.addColumn('string', 'Last Name');
            data.addColumn('string', 'Role');
            data.addColumn('string', 'Country');
            data.addColumn('string', 'Nationality');
            legalEntity.peps.forEach(function (pep) {
                data.addRow(
                    [pep.firstName,  pep.lastName, pep.role,
                        pep.country,pep.country]);
            });
            var table = new google.visualization.Table(document.getElementById('d-'+cID));

            table.draw(data, {showRowNumber: true, width: '100%', height: '100%'});
       });

    }
}

function loadPieCharts() {
    drawPieChart('donutchart-NA', 'North America', 666, 1234, 235);
    drawPieChart('donutchart-EU', 'Europe', 123, 543, 34);
}


function loadFundDropdown() {
    $.getJSON("/api/funds", function (funds) {
        funds.forEach(function (fund) {
            var option = $('<option/>');
            option.attr({'value': fund.id}).text(fund.name);
            $('#fund-selection').append(option);
        });
    });
}