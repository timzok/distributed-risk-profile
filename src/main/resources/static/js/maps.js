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
    }
}

var maps = new Map();
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

        getCountriesData($('#fund-selection').val(), code).then(
            drawVectorMap(placeholder, maps.get(code), countriesMap)
        );
    }

    $("#world-map").fadeOut( 200, "linear", complete );
}


function onRegionClick(e, code){
    var regionData = regionsMap[code];
    if (regionData) {
        renderMap(code.toLowerCase());
    } else {
        e.preventDefault();
    }
}

function showWorldMap(){
    function placeholder(code) {
        return '#' + code + '-map';
    }

    for (var key of maps.keys()) {
        $(placeholder(key)).fadeOut();
    }
    $("#world-map").fadeIn();
    drawWorldMap($('#fund-selection').val())
}

function drawWorldMap(fundID){
    $.getJSON( "/api/funds/" + fundID + "/regions", function( data ) {
        regionsMap = data.regions.reduce(function(map, obj) {
            map[obj.regionCode] = obj;
            return map;
        }, {});
        drawMap(data);
        drawWorldMapPieCharts(regionsMap);
    });
}}


function drawMap(regionData) {
    drawVectorMap('#world-map', 'continents_mill', getWorldData(regionData) )
}


function drawVectorMap(mapID, mapName, data, worldMap) {
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
        onRegionTipShow : onTipShow,
        onRegionClick : onRegionClick
    });

}

function setObserver() {
    $( "#fund-selection" ).change(function() {
        drawWorldMap(this.value);
    });
}

function getWorldData(regionData) {
var regionsList = { };
$.each( regionData.regions, function( key, val ) {
    regionsList[val.regionCode] = 2;
  });

  return regionsList;
}

function getCountriesData(fundID, code) {
    return $.getJSON( "/api/funds/" + fundID + "/regions/" + code + "/countries", function( data ) {
        $.each( data.countries, function( key, val ) {
            countriesMap[val.countryCode] = 2;
        });
    });
}