#PATHFINDER/SCREEN READER

Screen-Reader that parses and reads all content on screen, contains settings to toggle speed, pitch and accent.
Pathfinder that generates the most optimal route from one point on college hill to another using Dijkstra's algorithm and stakeholder specifications.
Integrated with screen reader component to read out the optimal path.

# README
To build use:
`mvn package`

To run use:
`./run`

To start the server use:
`./run --gui [--port=<port>]`

# TESTING
textForm.html:
Contains deliverables specific to Aries user story. Where certain 
text is highlighted in different colors. Contains images and form

moreComplexPage.html:
Contains deliverables specific to forms, tables and drop down menu

basicText.html:
contains a basic text page with headers and bodies. 

form.html:
contains text page with form (Autocorrector form from lab)

nothing.html:
blank html page.


Between testOne.html and testTwo.html, all relevant HTML tags
are present in our testing files. By running the screen reader on
these two, we can ensure it works on all specified tags! Currently,
when the website is launched, one can press the "Start Reading" button to 
start looping through all elements using
the right and left arrow keys beginning from the top of the page.
