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

function bindEvents(){
    var currentMouseOverRegion = null;
    var mouseEventTimeout = new Map();

    $("path").click(function(event){
        var elem = event.target;

        var regionCode = extractRegion(elem);
        if (regionCode!=null) {
            if (mapCurrentDetailLevel=='world') {
                // Case : the current map displays the full world
                if (currentMouseOverRegion!=null) {
                    currentMouseOverRegion = null;
                    regionDetailActionHandlerEvent(regionCode);
                }
            } else {
                // Case : the current map is focusing on a geographical region
                countryDetailActionHandlerEvent(extractCountry(elem));
            }
        }
    });

    $("path").mouseenter(function(event) {
        // Case : the user move the mouse over a new country
        var elem = event.target;
        var regionCode = extractRegion(elem);

        if ((regionCode!=null) && (global.hasRiskDataForRegion(regionCode))) {
            if (mapCurrentDetailLevel=='world') {
                // Case : the current map displays the full world, so no focusing on a geographical region
                if (currentMouseOverRegion != regionCode) {
                    // Case : the user changed of geographical region
                    // Action : colorize the region pointed by the mouse
                    currentMouseOverRegion = regionCode;
                    colorizeRegion(currentMouseOverRegion, true);
                } else {
                    // Case : the mouse was on a country but moved to another country of the same region
                    // Action : cancel the clear colorize action
                    var timeoutId = mouseEventTimeout.get(regionCode);
                    if (timeoutId) {
                        window.clearTimeout(timeoutId);
                        mouseEventTimeout.delete(regionCode);
                    }
                }
            } else {
                // Case : the current map is focusing on a geographical region
                currentSelectedRegion = regionCode;
                colorizeCountry(extractCountry(elem), regionCode, true);
            }
        }
    });

    $("path").mouseout(function(event){
        // Case : the user move the mouse outside of a country
        if (mapCurrentDetailLevel=='world') {
            // Case : the current map displays the full world, so no focusing on a geographical region
            if (currentMouseOverRegion!=null) {
                var currentSelectedRegionCp = currentMouseOverRegion;
                mouseEventTimeout.set(currentSelectedRegionCp, window.setTimeout(function() {
                    if (currentSelectedRegionCp!=currentMouseOverRegion) {
                        currentMouseOverRegion = null;
                    }
                    colorizeRegion(currentSelectedRegionCp, null);
                }, 1));
            }
        } else {
            // Case : the current map is focusing on a geographical region
            var elem = event.target;
            currentMouseOverRegion = null;
            colorizeCountry(extractCountry(elem), extractRegion(elem), false);
        }
    });
}

function drawWorldMap(fundID){
    $.getJSON( "/api/funds/" + fundID + "/regions", function( data ) {
        regionsMap = data.regions.reduce(function(map, obj) {
            map[obj.regionCode] = obj;
            return map;
        }, {});

        if (data.regions.length > 0) {
            drawMap(data);
            updateWorldData(regionsMap);
            bindEvents();
        } else {
            //alert('Empty fund');
        }

        $(resizeWorldMap());
        $('#country-charts').html(''); //Remove potential displayed column charts
        $('#c-topten').hide();
        $("#topten").html('');
        $("#pepsInformations").hide('');
        $("#pepsInformations").html('');

    });
}}

function drawMap(data) {
    var regionColors = {};

    data.regions.forEach(function (region) {
        regionData[region.regionCode].forEach(function(country) {
            regionColors[country] = 2;
        })
    });
    $('#world-map').html('');
    $('#world-map').vectorMap({
        map: 'world_mill',
        backgroundColor: 'transparent',
        series: {
            regions: [{
                scale: {
                    '1': '#D0DCE9',
                    '2': '#006AC3'
                },
                attribute: 'fill',
                values:  regionColors
            }]
        },
        zoomOnScroll: false,
        zoomButtons : false,
        onRegionClick : onCountryClick
    });
}


function drawVectorMap(mapID, mapName, data, worldMap) {
}

function setObserver() {
    $( "#fund-selection" ).change(function() {
        $(drawWorldMap(selectedFund()));
    });
}

function resizeWorldMap(reduce) {
    if (reduce) {
        $('#maps-container').addClass("col-md-8");
        $('#maps-container').removeClass("col-md-12");
        $("#country-charts").show();
    } else {
        $("#maps-container").addClass("col-md-12");
        $("#maps-container").removeClass("col-md-8");
        $("#country-charts").hide();

    }
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

function onCountryClick(e, code){
    var coutryData = countriesMap[code];
    if (coutryData) {
        resizeWorldMap(true);
        getAndDrawColumnChart(code);
    }
}

function getAndDrawColumnChart(countryCode) {
    //$.getJSON( "../jsonfiles/Country" + countryCode + '.json', function( data ) {
    $.getJSON( "/api/funds/" + selectedFund() + "/countries/" + countryCode , function( data ) {
        drawColumnChart(data);
    });
};