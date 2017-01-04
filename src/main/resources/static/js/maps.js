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
        $(placeholder).vectorMap({map: maps.get(code),
        backgroundColor: 'transparent'});
        //drawMap($(placeholder), maps.get(code), {})

    }

    $("#world-map").fadeOut( 200, "linear", complete );
}

//function renderMap2(code){
//    function complete() {
//        drawMap(maps.get(code), {})
//        $('#world-map').fadeIn().resize();
//    }
//    $("#world-map").fadeOut( 200, "linear", complete );
//}




function onRegionClick(e, code){
    renderMap(code.toLowerCase());
   //renderMap2(code.toLowerCase());
}

function showWorldMap(){
    function placeholder(code) {
        return '#' + code + '-map';
    }

    for (var key of maps.keys()) {
        $(placeholder(key)).fadeOut();
    }
    $("#world-map").fadeIn();
}

function drawWorldMap(fundID){
    $.getJSON( "./jsonfiles/MapPerregions" + fundID + ".json", function( data ) {
        loadRegionData(data.Regions);
        drawWorldMapPieCharts(data.Regions);
    });

}}


function loadRegionData(regionData) {
    var regionList = { };
    $.each( regionData, function( key, val ) {
        regionList[regionData[key].regionCode] = 2;
      });

    drawMap('#world-map', 'continents_mill', regionList)

}

function drawMap(mapID, mapName, data) {

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