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
         //chart.draw(data, options);

           // initial value
           var percent  = 0;
           var percent2  = 0;
           var countnum = 0;
           //calculate in %
           var count = l+h+m;
           //increment
           var inc1 = l/100
           var inc2 = (count-h)/100;
            // start the animation loop
           var handler = setInterval(function(){
               // values increment
               countnum += 1
               percent += inc1
               percent2 += inc2
               // apply new values;
                data.setValue(0, 1, percent);
                data.setValue(1, 1, m);
                data.setValue(2, 1, count-percent2);
               // update the pie
               chart.draw(data, options);
               // check if we have reached the desired value
               if (countnum > 100) {
                   // stop the loop
                   data.setValue(0, 1, l);
                   data.setValue(1, 1, m);
                   data.setValue(2, 1, h);
                   chart.draw(data, options);
                   clearInterval(handler)
               }
           }, 10)

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
        var data2 = data.clone();

        var options = {
            //chartArea:{left:20,top:0,width:'100%',height:'100%'},
            width:900,
            animation:{
                startup:true,
                duration:10000,
                easing:'out'
            },
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

        var countnum = 0;
        // start the animation loop
        var handler = setInterval(function(){
            // values increment
            countnum += 1
            // apply new values;
            var dataNum;
            for (dataNum = 0; dataNum < data2.getNumberOfRows(); dataNum+=2) {
                data2.setValue(dataNum, 1, data.getValue(dataNum,1)/(100-countnum));
                data2.setValue(dataNum, 2, data.getValue(dataNum,2)/(countnum));
            }
            // update the pie
            chart.draw(data2, options);
            // check if we have reached the desired value
            if (countnum == 99) {
                // stop the loop
                chart.draw(data, options);
                clearInterval(handler)
            }
        }, 10)
    }

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

        //chart.draw(data, options);

        // initial value
        var percentInvestor  = 0;
        var percentInvestor2  = 0;
        var countInvestor = countryData.low.investorCount+countryData.medium.investorCount+countryData.high.investorCount;
        var incInvestor = countryData.low.investorCount/100;
        var incInvestor2 = (countInvestor-countryData.high.investorCount)/100;

        var percentCountry  = 0;
        var percentCountry2  = 0;
        var countCountry = countryData.low.assetValue+countryData.medium.assetValue+countryData.high.assetValue;
        var incCountry = countryData.low.assetValue/100;
        var incCountry2 = (countCountry-countryData.high.assetValue)/100;

        var percentWorld  = 0;
        var percentWorld2  = 0;
        var countWorld = countryData.low.globalAssetValue+countryData.medium.globalAssetValue+countryData.high.globalAssetValue;
        var incWorld = countryData.low.globalAssetValue/100;
        var incWorld2 = (countWorld-countryData.high.globalAssetValue)/100;

        // start the animation
        var countnum=0;
        var handler = setInterval(function(){
            // values increment
            countnum += 1
            //Investor
            percentInvestor += incInvestor
            percentInvestor2 += incInvestor2
            // apply new values;
            data.setValue(0, 1, percentInvestor);
            data.setValue(0, 2, countryData.medium.investorCount);
            data.setValue(0, 3, countInvestor-percentInvestor2);

            //Country
            percentCountry += incCountry
            percentCountry2 += incCountry2
            // apply new values;
            data.setValue(1, 1, percentCountry);
            data.setValue(1, 2, countryData.medium.assetValue);
            data.setValue(1, 3, countCountry-percentCountry2);

            //World
            percentWorld += incWorld
            percentWorld2 += incWorld2
            // apply new values;
            data.setValue(2, 1, percentWorld);
            data.setValue(2, 2, countryData.medium.globalAssetValue);
            data.setValue(2, 3, countWorld-percentWorld2);

            // update the pie
            chart.draw(data, options);
            // check if we have reached the desired value
            if (countnum > 100) {
                // stop the loop
                data.setValue(0, 1, countryData.low.investorCount);
                data.setValue(0, 2, countryData.medium.investorCount);
                data.setValue(0, 3, countryData.high.investorCount);

                //Country

                data.setValue(1, 1, countryData.low.assetValue);
                data.setValue(1, 2, countryData.medium.assetValue);
                data.setValue(1, 3, countryData.high.assetValue);

                //World

                data.setValue(2, 1, countryData.low.globalAssetValue);
                data.setValue(2, 2, countryData.medium.globalAssetValue);
                data.setValue(2, 3, countryData.high.globalAssetValue);

                chart.draw(data, options);
                clearInterval(handler)
            }
        }, 10)

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
    //$.getJSON( "/api/funds/" + selectedFund() + "/countries/" + countryCode+ "/" + riskLevel, function( data ) {
    //$.getJSON( "/jsonfiles/Peps.json" , function( data ) {
        //$('#pepsInformation').html('');
        $("#pepsInformations").show('');
        $('#investorInformation').html('');
        //$('#investorInformation').html('');
        displayPepsInfo(data);

    });
};

function displayPepsInfo(pepsDataForRisk) {


    //$('#pepsInformations').html('');
    var tableDiv ="<table width='100%'>"
    // function drawTable() {
        pepsDataForRisk.legalEntities.forEach(function (legalEntity) {
            var cID = 'legal-entity-' + legalEntity.name;
             tableDiv +="<TR><TD class='centertd20'>" +
                            "<div class='blockquote' onclick='displayPepsDetailInfo(\""+legalEntity.name+"\")'>" +
                                "<Table> <tr> <TD align='left' class='tdcards'><Strong>" + legalEntity.name+ "</strong></TD><TR>"
                                +" <TD class='tdcards'>"+ legalEntity.type+"</TD></TR><TR>"+
                                  "<TD class='tdcards'>" + legalEntity.nature+"</TD></TR></Table>" +
                            "</div>" +
                          "</td><TD class='centertd80'>"
            tableDiv +="<div id='pepsInformation-"+legalEntity.name+"'>&nbsp;</div></TD></TR>"
            // var otherpart = "<div id='pepsInformation-"+legalEntity.name+"'>&nbsp;</div></TD>"
            // $('#investorInformation').append(otherpart);
            localStorage.setItem(legalEntity.name, JSON.stringify(pepsDataForRisk));
            //displayPepsDetailInfo(pepsDataForRisk,legalEntity.name);
            // var data = new google.visualization.DataTable();
            // data.addColumn('string', 'Fisrt Name',{style: 'font-style:bold; font-size:22px;'});
            // data.addColumn('string', 'Last Name',{style: 'font-style:bold; font-size:22px;'});
            // data.addColumn('string', 'Role',{style: 'font-style:bold; font-size:22px;'});
            // data.addColumn('string', 'Country',{style: 'font-style:bold; font-size:22px;'});
            // data.addColumn('string', 'Nationality',{style: 'font-style:bold; font-size:22px;'});
            // legalEntity.peps.forEach(function (pep) {
            //     data.addRow(
            //         [pep.firstName,  pep.lastName, pep.role,
            //             pep.country,pep.country]);
            // });
            // var table = new google.visualization.Table(document.getElementById('d-'+cID));
            // data.setProperty()
            // table.draw(data, {showRowNumber: true, width: '100%', height: '100%'});
       });
    tableDiv +="</table>"
    $('#investorInformation').append(tableDiv);
    // }
}
function displayPepsDetailInfo(legalEntityName){
    var alreadyDisplay = localStorage.getItem(legalEntityName+"-active");
    if("TRUE"==alreadyDisplay){
        localStorage.setItem(legalEntityName+"-active", "FALSE");
        $('#pepsInformation-'+legalEntityName).html('');
    } else {
            var pepsDataForRisk = JSON.parse(localStorage.getItem(legalEntityName));
            //console.log('retrievedObject: ', JSON.parse(pepsDataForRisk));
            google.charts.load('current', {'packages':['table']});
            google.charts.setOnLoadCallback(drawTable);

            function drawTable() {
                pepsDataForRisk.legalEntities.forEach(function (legalEntity) {
                    if (legalEntity.name == legalEntityName) {

                    var data = new google.visualization.DataTable();
                    data.addColumn('string', 'Fisrt Name', {style: 'font-style:bold; font-size:22px;'});
                    data.addColumn('string', 'Last Name', {style: 'font-style:bold; font-size:22px;'});
                    data.addColumn('string', 'Role', {style: 'font-style:bold; font-size:22px;'});
                    data.addColumn('string', 'Country', {style: 'font-style:bold; font-size:22px;'});
                    data.addColumn('string', 'Nationality', {style: 'font-style:bold; font-size:22px;'});
                    legalEntity.peps.forEach(function (pep) {
                        data.addRow(
                            [pep.firstName, pep.lastName, pep.role,
                                pep.country, pep.country]);
                    });
                    var table = new google.visualization.Table(document.getElementById('pepsInformation-'+legalEntity.name));
                    table.draw(data, {showRowNumber: true, width: '100%', height: '100%'});
                    //$('#pepsInformation').show('');
                }
            });
        }
        localStorage.setItem(legalEntityName+"-active", "TRUE");
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