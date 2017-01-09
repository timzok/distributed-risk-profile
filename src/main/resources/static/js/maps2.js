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
}

function onRegionClick(e, code){
}

function onCountryClick(e, code){
}

function showWorldMap(){
}

function drawWorldMap(fundID){
    $.getJSON( "/api/funds/" + fundID + "/regions", function( data ) {
        regionsMap = data.regions.reduce(function(map, obj) {
            map[obj.regionCode] = obj;
            return map;
        }, {});
        drawMap(data);
        updateWorldData(regionsMap);
        $('#country-charts').html(''); //Remove potential displayed column charts
        $('#c-topten').hide();
        $("#topten").html('');
        $("#pepsInformations").hide('');
        $("#pepsInformations").html('');

    });
}}


function drawMap(regionData) {
}


function drawVectorMap(mapID, mapName, data, worldMap) {
}

function setObserver() {
    $( "#fund-selection" ).change(function() {
        $(drawWorldMap(selectedFund()));
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