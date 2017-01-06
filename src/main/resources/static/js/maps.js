var regionsMap = {};
var countriesMap = {};

function onTipShow(e, el, code){
    var map =  $('#world-map').vectorMap('get', 'mapObject');
    var regionData = regionsMap[code];
    if (regionData) {
        map.tip.html(el[0].innerText +
            " low: " + regionData.low.assetValue +
            " %, medium:" + regionData.medium.assetValue +
            "%, high:" + regionData.high.assetValue +"% ");
    } else {
        e.preventDefault();
    }
}

function onCountryTipShow(e, el, code){
    var map =  $(currentMapId).vectorMap('get', 'mapObject');
    var coutryData = countriesMap[code];
    if (coutryData) {
        map.tip.html(el[0].innerText +
            " low: " + coutryData.low.assetValue +
            " %, medium:" + coutryData.medium.assetValue +
            "%, high:" + coutryData.high.assetValue +"% ");
    } else {
        e.preventDefault();
    }
}

var maps = new Map();
var currentMapId = '#world-map';
maps.set("eu", 'europe_mill');
maps.set("af", 'africa_mill');
maps.set("as", 'asia_mill');
maps.set("na", 'north_america_mill');
maps.set("oc", 'oceania_mill');{
maps.set("sa", 'south_america_mill');

function renderMap(code){
    function complete() {
        function placeholder(code) {
            return '#' + code + '-map';
        }

        for (var key of maps.keys()) {
            $(placeholder(key)).fadeOut();
        }

        var placeholder = placeholder(code);
        $(placeholder).fadeIn();

        getCountriesDataAndDrawMap(selectedFund(), code, drawVectorMap, placeholder);
    }

    $("#world-map").fadeOut( 200, "linear", complete );
}

function onRegionClick(e, code){
    $("#worldMapBtn").fadeIn();
    var regionData = regionsMap[code];
    if (regionData) {
        getAndDisplayTopTen(code);
        $('#maps-container').addClass("col-md-8");
        $('#maps-container').removeClass("col-md-12");
        $("#country-charts").show();

        renderMap(code.toLowerCase());
    } else {
        e.preventDefault();
    }
}

function onCountryClick(e, code){
    var coutryData = countriesMap[code];
    if (coutryData) {
        getAndDrawColumnChart(code);
    }
}

function showWorldMap(){
    function complete() {
        $("#world-map").fadeIn();
        $("#worldMapBtn").fadeOut();
        drawWorldMap(selectedFund())
    }

    $(currentMapId).fadeOut(1, 'linear', complete);
}

function drawWorldMap(fundID){
    $.getJSON( "/api/funds/" + fundID + "/regions", function( data ) {
        regionsMap = data.regions.reduce(function(map, obj) {
            map[obj.regionCode] = obj;
            return map;
        }, {});
        drawMap(data);
        drawWorldMapPieCharts(regionsMap);
        $('#country-charts').html(''); //Remove potential displayed column charts
        $('#c-topten').hide();
        $("#topten").html('');
        $("#pepsInformations").hide('');
        $("#pepsInformations").html('');

    });
}}


function drawMap(regionData) {
    drawVectorMap('#world-map', 'continents_mill', getWorldData(regionData), true)
}


function drawVectorMap(mapID, mapName, data, worldMap) {
    currentMapId = mapID;

    if (worldMap) {
        $("#maps-container").addClass("col-md-12");
        $("#maps-container").removeClass("col-md-8");
        $("#country-charts").hide();
    } else {
        $('#maps-container').addClass("col-md-8");
        $('#maps-container').removeClass("col-md-12");
        $("#country-charts").show();
    }

    $(mapID + " div.jvectormap-container").remove();
    $(mapID).vectorMap({
        map: mapName,
        backgroundColor: 'transparent',
        series: {
            regions: [{
                scale: {
                    '1': 'FFF',
                    '2': '#002144'
                },
                attribute: 'fill',
                values:  data
            }]
        },
        onRegionTipShow : worldMap ? onTipShow : onCountryTipShow,
        onRegionClick : worldMap ? onRegionClick : onCountryClick
    });

}

function setObserver() {
    $( "#fund-selection" ).change(function() {
        showWorldMap();
    });
}

function getWorldData(regionData) {
var regionsList = { };
$.each( regionData.regions, function( key, val ) {
    regionsList[val.regionCode] = 2;
  });

  return regionsList;
}

function getCountriesDataAndDrawMap(fundID, code, callback, placeholder) {
    return $.getJSON( "/api/funds/" + fundID + "/regions/" + code + "/countries", function( data ) {
        var countriesList = {};
        $.each( data.countries, function( key, val ) {
            countriesList[val.countryCode] = 2;
        });
        countriesMap = data.countries.reduce(function(map, obj) {
            map[obj.countryCode] = obj;
            return map;
        }, {});
        callback(placeholder, maps.get(code), countriesList);
    });
}