var mapTitles = new Map();
mapTitles.set("EU", 'Europe');
mapTitles.set("AF", 'Africa');
mapTitles.set("AS", 'Asia');
mapTitles.set("NA", 'North America');
mapTitles.set("OC", 'APAC');
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
function getAndDisplayTopTen(regionCode) {
    $.getJSON( "/api/funds/" + selectedFund() + '/regions/'+ regionCode +'/topcountries/10', function( data ) {
    //$.getJSON( "/jsonfiles/Top10CountryEu.json", function( data ) {
        countriesMap = data.countries.reduce(function(map, obj) {
            map[obj.countryCode] = obj;
            return map;
        }, {});

        if (countriesMap) {
            $('#c-topten').show();
            drawTopTenColumnChart(countriesMap);
        } else {
            $('#c-topten').hide();
        }

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
            r.countryName + " (" + r.TotalAssetNumber + ")",
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
             legend: {position: "none"},
           title: title,
           width: 150,
           height: 150,
           titleTextStyle: { fontSize: 13 },
           pieHole: 0.6,
           colors: [ '#aaba0a', '#fca311', 'c71D06'],
           pieSliceText: 'none',
           backgroundColor: 'none',
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

function drawTopTenColumnChart (countryCode){
    google.charts.load('current', {'packages':['bar']});
    google.charts.setOnLoadCallback(drawChart);

    function drawChart() {

        var data = new google.visualization.DataTable()
        data.addColumn('string', 'Countries');
        data.addColumn('number', 'Asset Value');
        data.addColumn('number', 'World %');

        $.each( countryCode, function( key, val ) {
            var r = countryCode[key];
            data.addRow([r.countryCode, r.high.assetValue, (r.high.assetValue/r.high.globalAssetValue)*100]);
        });


        var options = {
            //chartArea:{left:20,top:0,width:'100%',height:'100%'},
            width:900,
            chart: {
                title: 'TopTen High risk Countries',
                subtitle: 'Number of assets on the left, World proportion on the right'
            },
            series: {
                0: { axis: 'assetNum' }, // Bind series 0 to an axis named 'distance'.
                1: { axis: 'compWorld' } // Bind series 1 to an axis named 'brightness'.
            },
            axes: {
                y: {
                    assetNum: {label: 'Asset Number'}, // Left y-axis.
                    compWorld: {side: 'right', label: '%'} // Right y-axis.
                }
            },
            titleTextStyle: { fontSize: 13 },
                title: 'Risk details for ' + countryCode.countryCode,
                colors: [ 'red', 'blue', 'blue']
        };
        var chart = new google.charts.Bar(document.getElementById('topten'));
        chart.draw(data, options);
    }

    // var cID = 'country-topten';
    // //var chartDiv  = "<div id='c-" + cID + "' style=\"position:relative\">"
    // var chartDiv = "<div id='" + cID + "' style='width: 100%; height: 100%'>"
    // chartDiv += "</div>"
    //
    // $('#topten').append(chartDiv);

}

function drawColumnChart(countryData) {

    function drawChart() {

        var data = new google.visualization.arrayToDataTable([
            ['Rank', 'Low', 'Medium', 'High'],
            ['% Investor', countryData.low.investorCount, countryData.medium.investorCount, countryData.high.investorCount],
            ['Country (asset)' , countryData.low.assetValue   , countryData.medium.assetValue, countryData.high.assetValue],
            ['World (asset)'  , countryData.low.globalAssetValue , countryData.medium.globalAssetValue , countryData.high.globalAssetValue]
        ]);

        var options = {
            legend: {position: "none"},
            isStacked: 'percent',
            titleTextStyle: { fontSize: 13 },
            title: 'Distribution of risk ' + countryData.countryName,
            colors: [ '#aaba0a', '#fca311', '#c71D06']
        };

        var chart = new google.visualization.ColumnChart(document.getElementById('country-chart-' + countryData.countryCode));

        google.visualization.events.addListener(chart, 'select', function(e) {
            var selectedItem = chart.getSelection()[0];
            var countryCode = chart.ga.id.replace("country-chart-", "");
            var columnIndex = selectedItem.column;
            var riskMap = {"0": "low", "1": "medium", "2": "high"};
            getAndDisplayPeps(countryCode, riskMap[columnIndex]);
        });

        chart.draw(data, options);
    }

    google.charts.load('current', {'packages':['bar', 'corechart']});

    var cID = 'country-chart-' + countryData.countryCode;
    var chartDiv  = "<div id='c-" + cID + "' style=\"position:relative\">"
        chartDiv += "<div id='" + cID + "' style='width: 100%; height: 100%'>"
        chartDiv += "</div>"

    $('#country-charts').append(chartDiv);


    google.charts.setOnLoadCallback(drawChart);

    var btn = "<a class=\"btn btn-default\" type=\"button\" onclick=\"$('#c-" + cID + "').remove()\" style='position:absolute; top:0; right:0'>";
        btn += "<i class=\"fa fa-trash\"></i> ";
        btn += "</a>";

    $('#c-' + cID).append(btn);
}

function getAndDisplayPeps(countryCode,riskLevel){

    var csvUrl = "/api/funds/" + selectedFund() + "/countries/" + countryCode + "/legalEntitiesExport/rads/" + riskLevel;

    //$.getJSON( "/jsonfiles/Peps.json" , function( data ) {
    $.getJSON( "/api/funds/" + selectedFund() + "/countries/" + countryCode+ "/legalEntities/rads/" + riskLevel, function( data ) {
        displayPepsInfo(data);
        $("#pepsInformations").show('');

        var btn  = "<a class='btn btn-default' type='button' href='" + csvUrl + "' style='position:absolute; top:0; right:0'>";
        btn += "<i class='fa fa-file'>Export to CSV</i>";
        btn += "</a>";

        $('#pepsInformations').append(btn);

    });
};


function displayPepsInfo(pepsDataForRisk) {

    google.charts.load('current', {'packages':['table']});
    google.charts.setOnLoadCallback(drawTable);
    $('#pepsInformations').html('');



    function drawTable() {
        pepsDataForRisk.legalEntities.forEach(function (legalEntity) {
            var cID = 'legal-entity-' + legalEntity.name;
            //var tableDiv  = "<div id='c-" + cID + "' style=\"position:absolute\">"
            var tableDiv = "<div id='" + cID + "' style='width: 100%; height: 100%'>" +
                "<h1 class='myTitle'>" + legalEntity.name+ "</h1>" +
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