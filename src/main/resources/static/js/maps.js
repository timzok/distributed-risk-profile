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
}

function drawWorldMap(fundID){
    $.getJSON( "./jsonfiles/MapPerregions" + fundID + ".json", function( data ) {
        loadRegionData(data.Regions);
    });

}}


function loadRegionData(fundData) {
    var regionList = { };
    $.each( fundData, function( key, val ) {
        regionList[fundData[key].regionCode] = 2;
      });

    $('#world-map').html('');

    $('#world-map').vectorMap({
        map: 'continents_mill',
        backgroundColor: 'transparent',
        series: {
            regions: [{
                scale: {
                    '1': 'FFF',
                    '2': '#002144'
                },
                attribute: 'fill',
                values:  regionList //{ 'EU': 2, 'NA': 2 }
            }]
        },
        onRegionTipShow : onRegionTipShow,
        onRegionClick : onRegionClick
    });
}

function initializeMap() {
    $( "#fund-selection" ).change(function() {
        drawWorldMap(this.value);
    });
}