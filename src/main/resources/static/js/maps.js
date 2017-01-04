function onRegionTipShow(e, el){
    var map =  $('#world-map').vectorMap('get', 'mapObject');
    map.tip.html(el[0].innerText + " low: 30%, medium:35%, high:35% ");
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
        drawVectorMap($(placeholder), maps.get(code), getCountriesData(code))
    }

    $("#world-map").fadeOut( 200, "linear", complete );
}


function onRegionClick(e, code){
    renderMap(code.toLowerCase());
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
    $.getJSON( "./jsonfiles/MapPerregions" + fundID + ".json", function( data ) {
        drawMap(data.Regions);
        drawWorldMapPieCharts(data.Regions);
    });

}}


function drawMap(regionData) {
    drawVectorMap('#world-map', 'continents_mill', getWorldData(regionData) )
}


function drawVectorMap(mapID, mapName, data) {
    $(mapID).html('');
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
        onRegionTipShow : onRegionTipShow,
        onRegionClick : onRegionClick
    }).resize();

}

function setObserver() {
    $( "#fund-selection" ).change(function() {
        drawWorldMap(this.value);
    });
}

function getWorldData(regionData) {
var regionsList = { };
$.each( regionData, function( key, val ) {
    regionsList[regionData[key].regionCode] = 2;
  });

  return regionsList;
}

function getCountriesData(code) {
    {}
}