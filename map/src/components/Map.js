import { useEffect, useRef, useState } from "react";
import Canvas from "./Canvas";
import TextBox from "./TextBox";
import { AwesomeButton } from "react-awesome-button";
import { Row, Col } from "react-bootstrap";
let VOICE_SYNTH = window.speechSynthesis
let VOICES = VOICE_SYNTH.getVoices

function Map(key, value) {

    // initial view of Brown's canvas
    const INIT_MIN_LON = -71.407971
    const INIT_MAX_LON = -71.392231
    const INIT_MIN_LAT = 41.823142
    const INIT_MAX_LAT = 41.828147

    const ZOOM_AMT = 0.001

    const speakThis = (text) => {
        let utter = new SpeechSynthesisUtterance(text);
        utter.voice = VOICES[0] //sets the voice to speak in
        VOICE_SYNTH.speak(utter)
    }

    // these will be changed on user input
    const [currentMinLon, setCurrentMinLon] = useState(INIT_MIN_LON)
    const [currentMaxLon, setCurrentMaxLon] = useState(INIT_MAX_LON)
    const [currentMinLat, setCurrentMinLat] = useState(INIT_MIN_LAT)
    const [currentMaxLat, setCurrentMaxLat] = useState(INIT_MAX_LAT)

    // ways to pass the canvas
    const canvasWays = useRef([])
    const [canvasRoute, setCanvasRoute] = useState([])
    const [waysLoaded, setWaysLoaded] = useState(false)
    const [waysUpdate, setWaysUpdate] = useState(0)

    // stores an object of way boxes
    const [waysCache, setWaysCache] = useState({})
    // whether or not the ways have been fetched yet (used for whether to draw or not)
    const [waysFetched, setWaysFetched] = useState(false)
    // whether or not there are ways loading
    const [loading, setLoading] = useState(true)

    // the current map view
    const [mapView, setMapView] = useState({
        "northwest": [currentMaxLat, currentMinLon],
        "southeast": [currentMinLat, currentMaxLon]
    })

    const setNewMapView = () => {
        setMapView({
            "northwest": [currentMaxLat, currentMinLon],
            "southeast": [currentMinLat, currentMaxLon]
        })
        setWaysUpdate(waysUpdate + 1)
    }
    const [routeStart, setRouteStart] = useState("Thomas St")
    const [routeStartCross, setRouteStartCross] = useState("Benefit St")
    const [routeEnd, setRouteEnd] = useState("John St")
    const [routeEndCross, setRouteEndCross] = useState("Thayer St")

    const [coordStart, setCoordStart] = useState(null)
    const [coordEnd, setCoordEnd] = useState(null)


    const manualInput = () => {
        const toSend = {
            srclat: routeStart,
            srclong: routeStartCross,
            destlat: routeEnd,
            destlong: routeEndCross
        };
    }

    useEffect(() => {
        // currently this only fetches the default ways
        requestWays().then(ways => {
            canvasWays.current = ways
            setWaysLoaded(true)
            setWaysUpdate(waysUpdate + 1)
        })
        // speakCoords() // speaks the current coordinates
    }, [mapView])

    useEffect(() => {
        requestRoute("street").then(route => {
            setCanvasRoute(route)
        })
    }, [routeStart, routeStartCross, routeEnd, routeEndCross])

    useEffect(() => {
        if (coordStart && coordEnd) {
            requestRoute("coord").then(route => {
                setCanvasRoute(route)
            })
        }
    }, [coordStart, coordEnd])

    //localStorage.setItem(makeCacheKey(currentMinLat, currentMaxLat, currentMinLon, currentMaxLon), useEffect())
    /*
    Returns string of the max/min lat/lon to use as cache key
     */
    function makeCacheKey(lat1, lat2, lon1, lon2) {
        return String(lat1) + String(lat2) + String(lon1) + String(lon2)
    }

    /*
    Speaks the current coordinate bounds
     */
    const speakCoords = () => {
        speakThis("Coordinate Field is as follows.")
        speakThis("Upper right corner:")
        speakThis("Longitude: " + currentMaxLon)
        speakThis("Latitude: " + currentMaxLat)
        speakThis("Bottom left corner:")
        speakThis("Longitude: " + currentMinLon)
        speakThis("Latitude: " + currentMinLat)
    }

    async function requestWays() {
        return new Promise((resolve, reject) => {
            fetch("http://localhost:4567/ways", {
                method: "POST",
                headers: {
                    "Access-Control-Allow-Origin": "*",
                },
                body: JSON.stringify({
                    "northwest": [currentMaxLat, currentMinLon],
                    "southeast": [currentMinLat, currentMaxLon]
                })
            }).then(response => response.json())
                .then(response => {
                    console.log("Ways Response:", response)
                    if ("error" in response) {
                        //TODO: check how the backend responds to error and fix accordingly
                        if (response.error === undefined) {
                            alert("An error occurred")
                        } else {
                            alert(response.error)
                        }
                        reject()
                    } else {
                        resolve({
                            "ways": response.ways
                        })
                    }
                })
        })
    }

    async function requestRoute(type) {
        if (type === "street") {
            return new Promise((resolve, reject) => {
                fetch("http://localhost:4567/route", {
                    method: "POST",
                    headers: {
                        "Access-Control-Allow-Origin": "*",
                    },
                    body: JSON.stringify({
                        "startStreet": [routeStart, routeStartCross],
                        "endStreet": [routeEnd, routeEndCross]
                    })
                }).then(response => response.json())
                    .then(response => {
                        console.log("Route Response:", response)
                        if ("error" in response) {
                            alert("Invalid start or end node")
                            reject()
                        } else {
                            resolve({
                                "route": response.route
                            })
                        }
                    })
            })
        } else if (type = "coord") {
            return new Promise((resolve, reject) => {
                fetch("http://localhost:4567/route", {
                    method: "POST",
                    headers: {
                        "Access-Control-Allow-Origin": "*",
                    },
                    body: JSON.stringify({
                        "start": [coordStart[0], coordStart[1]],
                        "end": [coordEnd[0], coordEnd[1]]
                    })
                }).then(response => response.json())
                    .then(response => {
                        console.log("Route Response:", response)
                        if ("error" in response) {
                            alert("Invalid start or end node")
                            reject()
                        } else {
                            resolve({
                                "route": response.route
                            })
                        }
                    })
            })
        }

    }

    /**
     * Converts x and y to lat and long
     * @param {*} x 
     * @param {*} y 
     * @param {*} width 
     * @param {*} height 
     * @returns 
     */
    const XYToCoord = (x, y, width, height) => {
        const dims = mapView
        const latLength = dims["northwest"][0] - dims["southeast"][0]
        const longLength = dims["southeast"][1] - dims["northwest"][1]
        const lat = ((y / height) * latLength) + dims["northwest"][0]
        const long = ((x / width) * longLength) + dims["northwest"][1]
        return [lat, long]
    }

    const clickStartEnd = (event) => {
        const rect = event.target.getBoundingClientRect();
        const x = event.clientX - rect.left;
        const y = event.clientY - rect.top;
        const width = window.innerWidth * 0.75
        const height = window.innerHeight * 0.75
        const coord = XYToCoord(x, y, width, height);
        console.log("Coord", coord)
        console.log("Lat diff", coord[0] - mapView["northwest"][0])
        console.log("Long diff", coord[1] - mapView["northwest"][1])
        if (event.ctrlKey) {
            setCoordStart(coord)
        } else if (event.shiftKey) {
            setCoordEnd(coord)
        }
    }

    window.onload = () => {
        document.getElementById("Canvas").addEventListener('click', clickStartEnd)
    }

    const zoomIn = () => {
        console.log("Zooming in")
        setCurrentMinLat(currentMinLat + ZOOM_AMT)
        setCurrentMinLon(currentMinLon + ZOOM_AMT)
        setCurrentMaxLat(currentMaxLat - ZOOM_AMT)
        setCurrentMaxLon(currentMaxLon - ZOOM_AMT)
        setNewMapView()
    }

    const zoomOut = () => {
        console.log("Zooming out")
        setCurrentMinLat(currentMinLat - ZOOM_AMT)
        setCurrentMinLon(currentMinLon - ZOOM_AMT)
        setCurrentMaxLat(currentMaxLat + ZOOM_AMT)
        setCurrentMaxLon(currentMaxLon + ZOOM_AMT)
        setNewMapView()
    }


    return (
        <div>
            <div className="flex-box justify-center">
                <Canvas id='Canvas'
                    mapView={mapView}
                    ways={canvasWays}
                    waysFetched={waysFetched}
                    setMapView={setMapView}
                    route={canvasRoute}
                    waysLoaded={waysLoaded}
                    waysUpdate={waysUpdate} />
            </div>

            <div className="flex-box justify-left">
                <h1>Route</h1>
                <TextBox type="text" label={"Street 1"} change={setRouteStart} />
                <TextBox type="text" label={"Cross Street 1"} change={setRouteStartCross} />
                Start: {routeStart} & {routeStartCross}<br />
                <AwesomeButton type="primary" onPress={manualInput}> submit </AwesomeButton>
                <TextBox type="text" label={"Street 2"} change={setRouteEnd} />
                <TextBox type="text" label={"Cross Street 2"} change={setRouteEndCross} />
                End: {routeEnd} & {routeEndCross}<br />
                <AwesomeButton type="primary" onPress={manualInput}> submit </AwesomeButton>
            </div>
            <div>
                <button onClick={zoomIn}>Zoom In</button>
                <button onClick={zoomOut}>Zoom Out</button>

            </div>
        </div>

    )
}

export default Map;