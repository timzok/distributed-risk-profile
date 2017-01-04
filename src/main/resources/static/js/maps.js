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

function drawWorldMap(){
    function placeholder(code) {
        return '#' + code + '-map';
    }

    for (var key of maps.keys()) {
        $(placeholder(key)).fadeOut();
    }
    $("#world-map").fadeIn( 200);

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
                values: { 'EU': 2, 'NA': 2 }
            }]
        },
        onRegionTipShow : onRegionTipShow,
        onRegionClick : onRegionClick
    });
}}