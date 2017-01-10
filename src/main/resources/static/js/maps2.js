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

function bindEvents(){
    var currentMouseOverRegion = null;

    function removeSelection() {
        $(".mapRegionDataSelected").each(function (i, elem) {
            elem.classList.remove("mapRegionDataSelected");
        });
    }

    function markRegion(regionCode) {
        removeSelection();

        currentMouseOverRegion = regionCode;
        regionData[regionCode].forEach(function (countryCode) {
            var elem = $(".jvectormap-region.jvectormap-element[data-code='" + countryCode + "'")[0];
            if (elem) {
                elem.classList.remove("mapRegionData");
                elem.classList.add("mapRegionDataSelected");
                elem.classList.remove("mapRegionNoData");
                elem.classList.remove("mapRegionMouseOver");
            }
        });
    }

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
                markRegion(regionCode);
            }
        }
    });

    $("svg").mousemove(function(e){
        if (mapCurrentDetailLevel=='world') {
            var path = document.elementFromPoint(e.clientX,e.clientY);
            if (!path.getAttribute("data-code")) {
                removeSelection();
            }
            console.log('Region: ' + path.getAttribute("data-code") + ' at point: ' + e.clientX + ','+ e.clientY);
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
            $("#pepsInformations").show('');
        } else {
            $("#pepsInformations").hide('');
            $("#pepsInformations").html('');
        }

        $(resizeWorldMap());
        $('#country-charts').html(''); //Remove potential displayed column charts
        $('#c-topten').hide();
        $("#topten").html('');

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
                    '2': '#002144' //#006AC3 - Mike: relace color, not enough contrast when having white regions
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
        $("#country-charts").html('');

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