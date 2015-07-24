/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


var sources = ["gold", "newImage", "compare"];

 


/**
 * on change method for the select image box on the index page
 * @param {ref} selectionObj ref to the select box
 * @returns {undefined}
 */
function getTestFromSelectBox(selectionObj)
{
    
    var imgIdx =parseInt($(selectionObj).val());
    loadImages(imgIdx);
    setViewerLinks(imgIdx);
}



function setupComparison()
{
    //set up the selectorbox

    $('#runDate').text(testData.date);

    var listObj = $('#testRunList');
    var firstChoice = false;
    testData.testData.forEach(function (d, i)
    {
        var t = "";
        if (firstChoice === false)
        {
            t = "<option selected value=\"" + i + "\">" + d.testDescription + "</option>";
            firstChoice = true;
        }
        else
        {
            t = "<option value=\"" + i + "\">" + d.testDescription + "</option>";
        }

        listObj.append(t);

    });

    // get the first image
    loadImages(0);

    //set up the view links

    setViewerLinks(0);
}


/**
 * compose the links for the small images to their larger ones
 * @param {int} imgIdx
 * @returns {undefined}
 */
function setViewerLinks(imgIdx)
{

    sources.forEach(function (d, i)
    {
        var newHref = "imageviewer.html?imgIdx=" + imgIdx + "&type=" + d;




        var selector = "a#" + d + "Viewer";
        $(selector).attr("href", newHref);
    });


}

function setUpViewer()
{
    var desc = {};
    //["gold", "newImage", "compare"]
    
    desc["gold"] = "Original gold file for comparison";
    desc["newImage"] = "Newly taken image";
    desc["compare"] = "Result of the comparison";
    
    var imgIdx = parseInt(getQueryVariable('imgIdx'));
    var type = getQueryVariable('type')
    var baseData = testData.testData[imgIdx];
    $('#runDate').text(testData.date);
    $('#imageDescription').text(desc[type]);
    var rootLocation = window.location.protocol + "//"+ window.location.host   + window.location.pathname.split("target")[0];
    var cc = baseData[type + "File"]
    var newSrc = rootLocation + cc;
    $('#viewImage').attr("src", newSrc);



}

/**
 * 
 * read a query string variable. used on the single page
 * viewer page
 * 
 * @param {type} variable
 * @returns {Boolean|getQueryVariable.pair}
 */
function getQueryVariable(variable)
{
    var query = window.location.search.substring(1);
    var vars = query.split("&");
    for (var i = 0; i < vars.length; i++) {
        var pair = vars[i].split("=");
        if (pair[0] == variable) {
            return pair[1];
        }
    }
    return(false);
}

/**
 * set the source,target and compare image sources
 * 
 * @param {int} imgIdx index of the image in the metadata array
 * @returns {undefined}
 */
function loadImages(imgIdx)
{

    sources.forEach(function (d, i)
    {
        var rootLocation = window.location.protocol + "//" + window.location.host + window.location.pathname.split("target")[0];
        var cc = testData.testData[imgIdx][d + "File"]
        var newSrc = rootLocation + cc;

        console.log("cc " + newSrc + " " + d);

        var selector = "#" + d + "Image";
        $(selector).attr("src", newSrc);
    });


}


