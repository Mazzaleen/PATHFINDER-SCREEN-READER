import { useEffect, useRef, useState } from "react";

function Canvas(props) {
    const canvasRef = useRef(null)
    const [canvasWidth, setCanvasWidth] = useState(window.innerWidth * 0.75)
    const [canvasHeight, setCanvasHeight] = useState(window.innerHeight * 0.75)

    let defaultCanvas = null;

    /**
     * given lat and long coordinate and canvas dimensions,
     * transforms the coordinate into an X, Y position on the canvas
     * find the offset from the
     * @param coord - lat and long coordinate
     * @param width - width of canvas element
     * @param height - height of canvas element
     */
    const coordToXY = (coord, width, height) => {
        /** latitude is N and S, longitude is W and E
         find offset from left of longitude, find offset from top of latitude
         after finding offset, find the percentage offset to convert to X and Y
         coordinates */
        const dims = props.mapView
        const latLength = dims["northwest"][0] - dims["southeast"][0]
        const longLength = dims["southeast"][1] - dims["northwest"][1]
        const offsetLat = coord[0] - dims["northwest"][0]
        const offsetLong = coord[1] - dims["northwest"][1]
        const x = Math.abs((offsetLong / longLength) * width)
        const y = Math.abs((offsetLat / latLength) * height)
        return [x, y]
    }


    const draw = (ctx, canvasWays, color, width) => {
        // a Way is formatted as a JSON object containing
        // "lat1", "lat2", "lon1", "lon2"
        // Call beginPath before forLoop, then call stroke after forLoop
        if (canvasWays) {
            ctx.beginPath()
            canvasWays.forEach((way) => {
                const startCoord = coordToXY([way[0], way[1]], canvasWidth, canvasHeight)
                const endCoord = coordToXY([way[2], way[3]], canvasWidth, canvasHeight)
                ctx.moveTo(startCoord[0], startCoord[1])
                ctx.lineTo(endCoord[0], endCoord[1])
            })
            ctx.strokeStyle = color
            ctx.lineWidth = width
            ctx.stroke()
            ctx.closePath()
        }
    }

    window.onresize = () => {
        setCanvasHeight(window.innerHeight * 0.75)
        setCanvasWidth(window.innerWidth * 0.75)
    }

    useEffect(() => {
        if (canvasRef) {
            const ctx = canvasRef.current.getContext('2d')
            ctx.clearRect(0, 0, canvasWidth, canvasHeight)
            if (props.ways.current.ways) {
                draw(ctx, props.ways.current.ways, "#000000", 1)
            }
            defaultCanvas = ctx.getImageData(0, 0, canvasWidth, canvasHeight)
        }
    }, [canvasHeight, canvasWidth, props.waysLoaded, props.waysUpdate])

    useEffect(() => {
        if (canvasRef) {
            const ctx = canvasRef.current.getContext('2d')
            if (props.route && props.ways.current.ways) {
                if (props.route['route'] && props.route['route'] != 'error: invalid start or end nodes') {
                    if (defaultCanvas) {
                        ctx.clearRect(0, 0, canvasWidth, canvasHeight)
                        ctx.putImageData(defaultCanvas, 0, 0)
                        console.log(defaultCanvas)
                        draw(ctx, props.route['route'], "#FF0000", 5)
                    }
                }

            }
        }
    }, [props.route, canvasHeight, canvasWidth, props.waysLoaded, props.waysUpdate])
    return (
        <div
            style={{
                width: "100%",
                height: "min-content",
                textAlign: "center"
            }}>
            <canvas id={props.id}
                ref={canvasRef}
                width={canvasWidth}
                height={canvasHeight} />
        </div>
    )
}

export default Canvas;