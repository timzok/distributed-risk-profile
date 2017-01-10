var regionsMap = {};
var countriesMap = {};
var maps = new Map();
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
        $("#country-charts").html('');

    }
}

function getCountriesDataAndMarkMap(fundID, code) {
    return $.getJSON( "/api/funds/" + fundID + "/regions/" + code + "/countries", function( data ) {
        data.countries.forEach(function(country) {
            $("[data-code='" + country.countryCode +"']").each(function (i, elem) {
                elem.classList.add("hasData"); //Test
            });
        });
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