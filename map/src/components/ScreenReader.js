// screenReader.js
/* ------ Code outside window.onload goes here ----- */
let VOICE_SYNTH = null
let VOICES = null
let APPROVED_TAGS = null
let ALL_ELEMENTS = []
/** all of the page elements */
let PAGE_MAP = {}
/** maps item IDs to the index in ALL_ELEMENTS */
let current_voice = 0
let voice_speed = 1
// DO NOT IMPLEMENT THIS BEFORE FIGURING OUT YOUR HTML SPEC AND CORE FUNCTIONALITY
//Think of this with a class with two fields: the value and a function
let CURRENT_ELEMENT = {
    // a function that updates this.value to newElement and reads the element
    setAndSpeak: function (newElement) {
        if (newElement.style.hidden !== true && newElement.style.visibility !== "none") {
            if (this.value) {
                /** ensures value isn't null */
                this.value.classList.remove("highlight")
                /** removes the highlight class from the previous element */
            }
            if (VOICE_SYNTH.speaking) {
                VOICE_SYNTH.cancel();
            }
            this.value = newElement
            this.value.classList.add("highlight")
            VOICE_SYNTH.cancel() /** clears the queue to avoid large backup of reading */
            // read out tag (if it's a table), background color, and text content
            if (APPROVED_TAGS.includes(newElement.tagName)) {
                speakThis(newElement.tagName, VOICES[current_voice])
            }
            if (newElement.tagName === "TR") {
                /** speaks a new row instead of "tr" */
                speakThis("Row", VOICES[current_voice])
            }
            if (newElement.tagName === "PROGRESS") {
                progressHandler(newElement)
            }
            // let bgColor = window.getComputedStyle(newElement).backgroundColor
            // below line currently speaks RGB values, how to convert to words?
            // speakThis(bgColor, VOICES[current_voice])
            // doesn't read tr tag to avoid cluttering
            if (newElement.tagName !== "TR") {
                speakThis(newElement.innerText, VOICES[current_voice])
            }
            /** then adds the highlight class */
        }
    },
    // the current element
    value: null
}

/**
 * Speaks the given text in the given voice.
 * @param text: text to speak
 * @param voice: voice to speak text in
 * @constructor
 */
const speakThis = (text, voice) => {
    let utter = new SpeechSynthesisUtterance(text);
    utter.voice = voice //sets the voice to speak in
    utter.rate = voice_speed
    VOICE_SYNTH.speak(utter)
}

/**
 * Initializes ALL_ELEMENTS and PAGE_MAP with HTML elements
 */
const mapPage = () => {
    if (ALL_ELEMENTS.length === 0) {
        ALL_ELEMENTS = document.body.getElementsByTagName("*")
        /** this line gets collection of ALL elements within the body of the HTML (document) */
    }
    for (let i = 0; i < ALL_ELEMENTS.length; i++) {
        const currentElement = ALL_ELEMENTS[i]
        if (!currentElement.id) {
            currentElement.id = i
            /** if the element does not have an ID, we assign it one to access later; this changes the HTML */
        }
        PAGE_MAP[currentElement.id] = i
        /** maps the ID of the element to the index of the element */
    }
}

/**
 * Stores all tags that we want to read out before their content!
 */
const addApprovedTags = () => {
    APPROVED_TAGS = "TABLE"
    APPROVED_TAGS += "BUTTON"
    APPROVED_TAGS += "CAPTION"
    APPROVED_TAGS += "FIGCAPTION"
    APPROVED_TAGS += "FORM"
    APPROVED_TAGS += "LABEL"
    APPROVED_TAGS += "INPUT"
    APPROVED_TAGS += "TEXTAREA"
    APPROVED_TAGS += "AUDIO"
    APPROVED_TAGS += "IMG"
}

/**
 * Handles speaking out the progress bars
 */
const progressHandler = (element) => {
    speakThis("Progress Bar", VOICES[current_voice])
    speakThis("Max", VOICES[current_voice])
    speakThis(element.max, VOICES[current_voice])
    speakThis("Progress", VOICES[current_voice])
    speakThis(element.value, VOICES[current_voice])
}

/**
 * Adds a Read button to the top of the page that will start screen reading on click
 */
const addStartReadingButton = () => {
    let btn = document.createElement("button")
    btn.innerHTML = "Read Page"
    btn.style.display = "block"
    btn.style.marginLeft = "auto"
    btn.addEventListener("click", (event) => {
        // for (i = 0; i < ALL_ELEMENTS.length; i++) {
        //     CURRENT_ELEMENT.setAndSpeak(ALL_ELEMENTS[i])
        //
        // }
        CURRENT_ELEMENT.setAndSpeak(ALL_ELEMENTS[0])
    })
    document.body.prepend(btn);
    window.addEventListener("keydown", (event) => {
        /** This listener, via "window", applies to the entire browser.
         * Upon right arrow, the current element is incremented.
         * Upon left arrow, the current element goes back one.
         * Upon up arrow, the screen reader is paused.
         * Upon down arrow, the screen reader is resumed. */
        if (event.key === "ArrowRight") {
            const currentIndex = PAGE_MAP[CURRENT_ELEMENT.value.id]
            const newElement = ALL_ELEMENTS[currentIndex + 1]
            CURRENT_ELEMENT.setAndSpeak(newElement)
        }
        if (event.key === "ArrowLeft") {
            const currentIndex = PAGE_MAP[CURRENT_ELEMENT.value.id]
            let newElement = ALL_ELEMENTS[currentIndex]
            if (currentIndex !== 0) {
                newElement = ALL_ELEMENTS[currentIndex - 1]
            }
            CURRENT_ELEMENT.setAndSpeak(newElement)
        }
        if (event.key === "ArrowUp") {
            VOICE_SYNTH.pause()
        }
        if (event.key === "ArrowDown") {
            VOICE_SYNTH.resume()
        }
        if (event.key === " " || event.key === "Spacebar") {
            CURRENT_ELEMENT.setAndSpeak(ALL_ELEMENTS[0])
        }
    })
}


function addPlayPauseButton() {
    let btn = document.createElement("button")
    btn.innerText = "Play/Pause"
    btn.style.display = "block"
    btn.style.marginLeft = "auto"
    btn.addEventListener("click", (event) => {
        if (VOICE_SYNTH.speaking) {
            VOICE_SYNTH.pause()
        } else {
            VOICE_SYNTH.resume()
        }
    })
    document.body.prepend(btn)
}


/**
 * Speak an element from a ctrl+click mouse event
 * @param event
 */
function speakElementFromClick(event) {
    if (event.ctrlKey) {
        let elem = document.elementFromPoint(event.clientX, event.clientY);
        CURRENT_ELEMENT.setAndSpeak(ALL_ELEMENTS[elem.id])
    }
}

const checkSpeed = () => {
    window.addEventListener("keydown", (event) => {
        if (event.key === "1") {
            voice_speed = 1
        }
        if (event.key === "2") {
            voice_speed = 2
        }
        if (event.key === "3") {
            voice_speed = 3
        }
        if (event.key === "4") {
            voice_speed = 4
        }
        if (event.key === "5") {
            voice_speed = 5
        }
    })
}


// maps element categories to reading handlers (these should return
// strings)
const HANDLERS = { "PROGRESS": progressHandler }

// maps element tag names to element categories
const ROLES = {}

const screenReader = () => {
    /* Code previously inside window.onload  goes here */
    window.onload = () => {
        VOICE_SYNTH = window.speechSynthesis
        VOICES = VOICE_SYNTH.getVoices()
        /** sets voices var to an array of all possible voices */
        addPlayPauseButton()
        addStartReadingButton()
        /** adds a Read button to the top of
         every page and initializes highlighting functionality*/
        checkSpeed() /** hopefully this event listener will control speed depending on key-input
         */
        mapPage() /** calls mapPage to initialize ID-index map */
        console.log("Web Page has been Loaded!") /** prints this to web console when loaded (access fia F12) */
        addApprovedTags()
        window.addEventListener("click", speakElementFromClick)
    }
}
export default speakThis